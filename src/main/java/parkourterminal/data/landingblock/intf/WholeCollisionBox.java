package parkourterminal.data.landingblock.intf;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import parkourterminal.util.BlockUtils;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;
import java.util.*;

public class WholeCollisionBox {
    private CondZone condZone=new CondZone(0,0,0,0,-100);
    private final List<AxisAlignedBB> boxes=new ArrayList<AxisAlignedBB>();
    private final List<AxisAlignedBB> OriginalBoxes;
    private final List<AxisAlignedBB> separateFromWall=new ArrayList<AxisAlignedBB>();
    private List<Segment> segs=null;//只在segments()方法内使用
    private boolean clipAgainstWall=true;
    private AxisAlignedBB touchBox=null;
    private Vector3d touchPoint=null;
    private int mode=0;
    private boolean touchBoxEnabled=false;
    public WholeCollisionBox(List<AxisAlignedBB> boxes,LBbox lBbox,boolean touchBoxEnabled){
        this.touchBoxEnabled=touchBoxEnabled;
        List<AxisAlignedBB> result=new ArrayList<AxisAlignedBB>();
        if(lBbox==LBbox.NON_BOX){

            for(AxisAlignedBB aabb:boxes){
                AxisAlignedBB box=BlockUtils.getExtendedBox(aabb);
                result.add(box);
            }
        }else{
            result.addAll(boxes);
        }
        OriginalBoxes=new ArrayList<AxisAlignedBB>(result);
        result=BlockUtils.unionAABBs(result);
        this.boxes.addAll(result);
        separateFromWall.addAll(result);
        this.subtractWalls(Minecraft.getMinecraft().theWorld);
        UpdateCondZone();
    }
    public WholeCollisionBox(List<AxisAlignedBB> boxes,LBbox lBbox){
        this(boxes,lBbox,false);
    }
    public WholeCollisionBox(List<AxisAlignedBB> boxes,LBbox lBbox,Vector3d touchPoint){
        this(boxes,lBbox,true);
        this.touchPoint=touchPoint;
        mode=1;
    }
    public WholeCollisionBox(List<AxisAlignedBB> boxes,LBbox lBbox,AxisAlignedBB touchBox){
        this(boxes, lBbox,true);
        this.touchBox=touchBox;
        mode=2;
    }

    public boolean isClipAgainstWall() {
        return clipAgainstWall;
    }

    public boolean isTouchBoxEnabled() {
        return touchBoxEnabled;
    }

    public void setClipAgainstWall(boolean clipAgainstWall) {
        this.clipAgainstWall = clipAgainstWall;
        segments(true);
        UpdateCondZone();
    }

    public List<AxisAlignedBB> getBoxes() {
        return clipAgainstWall?separateFromWall:boxes;
    }

    public void subtractWalls(World world){//这个方法只在更新的时候调用
        List<AxisAlignedBB> aabbs=new ArrayList<AxisAlignedBB>();
        for(AxisAlignedBB box: OriginalBoxes){
            List<AxisAlignedBB> walls=BlockUtils.getWallsOfAABB(box,world);
            for(int i=0;i<walls.size();i++){
                walls.set(i,BlockUtils.getExtendedBox(walls.get(i)));
            }
            aabbs.addAll(BlockUtils.subtractAABBWithSubtractors(box,walls));
        }
        if(touchBoxEnabled){//分离
            if(mode==1&&this.touchPoint!=null){
                aabbs=BlockUtils.nearestGroupedAABBs(this.touchPoint,aabbs);
            } else if (mode==2&&this.touchBox!=null) {
                aabbs=BlockUtils.collectConnectedAABBs(this.touchBox,aabbs);
                aabbs.remove(this.touchBox);
            }
        }
        aabbs=BlockUtils.unionAABBs(aabbs);
        separateFromWall.clear();
        separateFromWall.addAll(aabbs);
        segments(true);
        UpdateCondZone();
    }
    public CondZone getCondZone(){
        return  condZone;
    };
    public void UpdateCondZone(){
        AxisAlignedBB outline=BlockUtils.UnionAll(getBoxes());
        if(outline==null){
            return;
        }
        AxisAlignedBB cond =BlockUtils.getExtendedBox(outline,1);
        this.condZone=new CondZone(cond.minX,cond.maxX,cond.minZ,cond.maxZ,cond.maxY);
    }
    public Vector3d calculateOffset(EntityPlayerSP player,LBmod lBmod,double last2posZ){
        double posX=player.lastTickPosX;
        double posZ=player.lastTickPosZ;
        if(lBmod==LBmod.Land){
            posX=player.lastTickPosX;
            posZ=player.lastTickPosZ;
        } else if (lBmod==LBmod.Hit) {
            posX=player.posX;
            posZ=player.posZ;
        } else if (lBmod==LBmod.Z_neo) {
            posX=player.lastTickPosX;
            posZ=last2posZ;
        }else if (lBmod==LBmod.Enter){
            posX=player.posX;
            posZ=player.posZ;

        }
        Vector2d pos=new Vector2d(posX,posZ);

        if(!getCondZone().insides(pos)){
            return null;
        }

        Segment nearestSegment=null;
        double nearestDistance=Double.POSITIVE_INFINITY;
        for(Segment segment:segments(false)){
            double dis=segment.distance(pos);
            if(dis<nearestDistance){
                nearestDistance=dis;
                nearestSegment=segment;
            }
        }
        if(nearestSegment==null){
            return null;
        }
        double maxY=nearestSegment.getMax().getMaxY();
        double minY=nearestSegment.getMax().getMinY();
        if(!(player.posY<=maxY&&maxY<player.lastTickPosY)&&lBmod!=LBmod.Enter){
            return null;
        }
        if(!(player.posY<maxY&&player.posY>minY&&player.posY<player.lastTickPosY)&&lBmod==LBmod.Enter){
            return null;
        }

        Vector2d offsetVec=nearestSegment.getPos1().getOffset(pos);
        Vector2d offsetVec2=nearestSegment.getPos2().getOffset(pos);
        if(offsetVec2.length()<offsetVec.length()){
            offsetVec=offsetVec2;
        }
        double totalOffset;
        double Xoffset=offsetVec.x;
        double Zoffset=offsetVec.y;
        if(inBox(pos)){
            Xoffset=Math.abs(Xoffset);
            Zoffset=Math.abs(Zoffset);
        }
        if(Xoffset*Zoffset>0){
            totalOffset=Math.hypot(Xoffset,Zoffset);
            if(Xoffset<0|Zoffset<0){
                totalOffset*=-1;
            }
        }else{
            totalOffset=Math.min(Xoffset,Zoffset);
        }
        return new Vector3d(Xoffset,Zoffset,totalOffset);
    }
    public List<Segment> segments(boolean update){
        if(segs!=null&&!update){
            return segs;
        }
        List<Vertex> vertices =vertices();
        Set<Segment> segmentSet = new HashSet<Segment>();
        for (Vertex vertex : vertices) {
            segmentSet.add(vertex.getxSegment());
            segmentSet.add(vertex.getzSegment());
        }
        segs=subtractOverlappedQuadLine(new ArrayList<Segment>(segmentSet));
        return segs;
    }
    private List<Vertex> vertices(){
        List<Vertex> vertices =new ArrayList<Vertex>();
        for(AxisAlignedBB box:getBoxes()){
            for(Vertex vertex:BlockUtils.QuadVertices(box)){
                int index=Vertex.findVertexWithSamePos(vertices,vertex);
                if(index<0){
                    vertices.add(vertex);
                    continue;
                }
                vertex.eliminate(vertices.get(index));
                vertices.remove(index);
            }
        }
        return vertices;
    }

    private static List<Segment> subtractOverlappedQuadLine(List<Segment> list) {
        List<Segment> result = new ArrayList<Segment>();
        for (int i = 0; i < list.size(); i++) {
            Segment a = list.get(i);
            List<Segment> current = new ArrayList<Segment>();
            current.add(a);
            for (int j = 0; j < list.size(); j++) {
                if (i == j) continue;
                Segment b = list.get(j);
                List<Segment> next = new ArrayList<Segment>();
                for (Segment piece : current) {
                    next.addAll(piece.subtract(b));
                }
                current = next;
            }
            result.addAll(current);
        }
        return result;
    }
    private boolean inBox(Vector2d pos){
        boolean isIn=false;
        for(AxisAlignedBB axisAlignedBB:getBoxes()){
            isIn|=inOneBox(axisAlignedBB,pos);
        }
        return isIn;
    }
    private boolean inOneBox(AxisAlignedBB aabb,Vector2d pos){
        return aabb.minX<=pos.x&&aabb.maxX>=pos.x&&aabb.minZ<=pos.y&&aabb.maxZ>=pos.y;
    }
}
