package parkourterminal.data.landingblock.intf;

import javax.vecmath.Vector2d;
import java.util.ArrayList;
import java.util.List;

public class Segment {
    private Vertex pos1;
    private Vertex pos2;
    private final int axis; //x-0;z-1
    public Segment(int axis){
        this.axis=axis;
    }

    public int getAxis() {
        return axis;
    }

    public Vertex getPos1() {
        return pos1;
    }

    public Vertex getPos2() {
        return pos2;
    }

    public void setPos1(Vertex pos1) {
        this.pos1 = pos1;
    }

    public void setPos2(Vertex pos2) {
        this.pos2 = pos2;
    }
    public Vertex getAnotherEndpoint(Vertex vertex){
        if(vertex.posEquals(pos1)){
            return pos2;
        }
        return pos1;
    }
    public double distance(Vector2d pos){
        Vector2d segStart=getPos1().getPos();
        Vector2d segEnd=getPos2().getPos();
        Vector2d ab = new Vector2d();
        ab.sub(segEnd, segStart);

        Vector2d ap = new Vector2d();
        ap.sub(pos, segStart);

        double abLenSq = ab.lengthSquared();
        if (abLenSq == 0.0) {
            return distanceTo(pos,segStart);
        }
        double t = ap.dot(ab) / abLenSq;
        t = Math.max(0, Math.min(1, t));
        Vector2d projection = new Vector2d(ab);
        projection.scale(t);
        projection.add(segStart);

        return distanceTo(pos,projection);
    }
    private static double distanceTo(Vector2d a, Vector2d b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double centeredLineValue() {
        if (axis == 0) return pos1.getPos().y;
        return pos1.getPos().x;
    }
    public Vertex getMin() {
        Vertex minPos=pos1;
        if (axis == 0) {
            if(pos2.getPos().x<pos1.getPos().x){
                minPos=pos2;
            }
        }
        else{
            if(pos2.getPos().y<pos1.getPos().y){
                minPos=pos2;
            }
        }
        return minPos;
    }

    public Vertex getMax() {
        Vertex maxPos =pos1;
        if (axis == 0) {
            if(pos2.getPos().x>pos1.getPos().x){
                maxPos =pos2;
            }
        }
        else{
            if(pos2.getPos().y>pos1.getPos().y){
                maxPos =pos2;
            }
        }
        return maxPos;
    }
    public double getAxisMinValue() {
        if (axis == 0) return getMin().getPos().x;
        else return getMin().getPos().y;
    }

    public double getAxisMaxValue() {
        if (axis == 0) return getMax().getPos().x;
        else return getMax().getPos().y;
    }

    public List<Segment> subtract(Segment other) {
        List<Segment> result = new ArrayList<Segment>();

        if (!this.intersects(other)) {
            result.add(this);
            return result;
        }
        double thisMin = this.getAxisMinValue();
        double thisMax = this.getAxisMaxValue();
        double otherMin = other.getAxisMinValue();
        double otherMax = other.getAxisMaxValue();

        if (otherMin > thisMin) {
            Segment segment=new Segment(axis);
            segment.setPos1(other.getMin());
            segment.setPos2(this.getMin());
            if(axis==0){
                other.getMin().setxSegment(segment);
                this.getMin().setxSegment(segment);
            }else{
                other.getMin().setzSegment(segment);
                this.getMin().setzSegment(segment);
            }

            result.add(segment);
        }

        if (otherMax < thisMax) {
            Segment segment=new Segment(axis);
            segment.setPos1(other.getMax());
            segment.setPos2(this.getMax());
            if(axis==0){
                other.getMax().setxSegment(segment);
                this.getMax().setxSegment(segment);
            }else{
                other.getMax().setzSegment(segment);
                this.getMax().setzSegment(segment);
            }

            result.add(segment);
        }

        return result;
    }
    private boolean intersects(Segment other) {
        if (this.axis != other.axis) {
            return false;
        }
        if(centeredLineValue()!= other.centeredLineValue()){
            return false;
        }
        return this.getAxisMaxValue()>other.getAxisMinValue()&&other.getAxisMaxValue()>this.getAxisMinValue()&&this.getMax().getMaxY()==other.getMax().getMaxY();
    }
    @Override
    public String toString(){
        return "seg:pos1:"+pos1+",pos2:"+pos2;
    }
}
