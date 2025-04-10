package parkourterminal.data.landingblock.intf;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;
import java.util.List;

public class Vertex {
    private final Vector2d pos;
    private final double maxY;
    private final double minY;
    private Segment xSegment;
    private Segment zSegment;
    private final int azimuth;
    public Vertex(Vector2d pos, int azimuth,double maxY,double minY){
        this.maxY=maxY;
        this.minY=minY;
        this.pos=pos;
        this.azimuth=azimuth;
    }

    public double getMaxY() {
        return maxY;
    }

    public double getMinY() {
        return minY;
    }

    public Segment getxSegment() {
        return xSegment;
    }

    public Segment getzSegment() {
        return zSegment;
    }

    public void setxSegment(Segment xSegment) {
        this.xSegment = xSegment;
    }

    public void setzSegment(Segment zSegment) {
        this.zSegment = zSegment;
    }

    public Vector2d getPos() {
        return pos;
    }
    public boolean posEquals(Vertex other) {
        return pos.x==other.getPos().x&&pos.y==other.getPos().y&&this.maxY==other.getMaxY();
    }
    public void eliminate(Vertex other){
        if(!posEquals(other)){
            return;
        }
        Vertex xAdj=this.xSegment.getAnotherEndpoint(this);
        Vertex zAdj=this.zSegment.getAnotherEndpoint(this);
        Vertex oxAdj=other.xSegment.getAnotherEndpoint(other);
        Vertex ozAdj=other.zSegment.getAnotherEndpoint(other);
        Segment xSeg=new Segment(0);
        Segment zSeg=new Segment(1);
        xSeg.setPos1(xAdj);
        xSeg.setPos2(oxAdj);
        zSeg.setPos1(zAdj);
        zSeg.setPos2(ozAdj);
        xAdj.setxSegment(xSeg);
        oxAdj.setxSegment(xSeg);
        zAdj.setzSegment(zSeg);
        ozAdj.setzSegment(zSeg);

    }
    public static int findVertexWithSamePos(List<Vertex> vertices, Vertex obj){
        for(int i=0;i<vertices.size();i++){
            if(vertices.get(i).posEquals(obj)){
                return i;
            }
        }
        return -1;
    }
    public Vector2d getOffset(Vector2d pos)//returns the offset to this vertex.
    {
        switch (azimuth){
            case 0: return new Vector2d(pos.x-this.pos.x,pos.y-this.pos.y);
            case 1: return new Vector2d(pos.x-this.pos.x,this.pos.y-pos.y);
            case 2: return new Vector2d(this.pos.x-pos.x,this.pos.y-pos.y);
            case 3: return new Vector2d(this.pos.x-pos.x,pos.y-this.pos.y);
            default: return new Vector2d(-0,-0);
        }
    }
    @Override
    public String toString(){
        return "vertex:"+pos;
    }
}
