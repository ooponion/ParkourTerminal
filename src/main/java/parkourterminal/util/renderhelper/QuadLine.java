package parkourterminal.util.renderhelper;

import net.minecraft.client.renderer.WorldRenderer;

import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.List;

public class QuadLine {
    private final Vector3d center;
    private final double length;
    private final int axis;
    private final int fixedAxis;
    public QuadLine(Vector3d center,double length,int axis,int fixedAxis){
        this.center=center;
        this.length=length;
        this.axis=axis;
        this.fixedAxis=fixedAxis;
    }
    public Vector3d centeredCoord() {
        if (axis == 0) return new Vector3d(0,center.y,center.z);
        else if (axis == 1) return new Vector3d(center.x,0,center.z);
        return new Vector3d(center.x,center.y,0);
    }
    public Vector3d getHalfVector() {
        if (axis == 0) return new Vector3d(length/2,0,0);
        else if (axis == 1) return new Vector3d(0,length/2,0);
        else return new Vector3d(0,0,length/2);
    }
    public Vector3d getMin() {
        Vector3d newCenter=new Vector3d(center);
        Vector3d negate=getHalfVector();
        negate.negate();
        newCenter.add(negate);
        return newCenter;
    }

    public Vector3d getMax() {
        Vector3d newCenter=new Vector3d(center);
        newCenter.add(getHalfVector());
        return newCenter;
    }
    public double getMinAxis() {
        if (axis == 0) return getMin().x;
        else if (axis == 1) return getMin().y;
        else return getMin().z;
    }

    public double getMaxAxis() {
        if (axis == 0) return getMax().x;
        else if (axis == 1) return getMax().y;
        else return getMax().z;
    }
    @Override
    public boolean equals(Object other){
        if(other instanceof  QuadLine){
            QuadLine line=(QuadLine) other;
            return center.equals(line.center) && length == line.length && this.axis == line.axis;
        }
        return false;
    }
    public boolean intersects(QuadLine other) {
        if (this.axis != other.axis) {
            return false;
        }
        if (this.fixedAxis != other.fixedAxis) {
            return false;
        }
        if(!this.centeredCoord().equals(other.centeredCoord())){
            return false;
        }
        return this.getMaxAxis()>other.getMinAxis()&&other.getMaxAxis()>this.getMinAxis();
    }
    public List<QuadLine> subtract(QuadLine other) {
        List<QuadLine> result = new ArrayList<QuadLine>();

        if (!this.intersects(other)) {
            result.add(this);
            return result;
        }
        double thisMin = this.getMinAxis();
        double thisMax = this.getMaxAxis();
        double otherMin = other.getMinAxis();
        double otherMax = other.getMaxAxis();

        if (otherMin > thisMin) {
            double len = otherMin - thisMin;
            double centerPos = thisMin + len / 2;
            Vector3d newCenter = createCenterWithAxis(centerPos);
            result.add(new QuadLine(newCenter, len, axis,fixedAxis));
        }

        if (otherMax < thisMax) {
            double len = thisMax - otherMax;
            double centerPos = otherMax + len / 2;
            Vector3d newCenter = createCenterWithAxis(centerPos);
            result.add(new QuadLine(newCenter, len, axis,fixedAxis));
        }

        return result;
    }
    private Vector3d createCenterWithAxis(double pos) {
        if (axis == 0) return new Vector3d(pos, center.y, center.z);
        if (axis == 1) return new Vector3d(center.x, pos, center.z);
        return new Vector3d(center.x, center.y, pos);
    }
    public static List<QuadLine> subtractQuadLineList(List<QuadLine> list) {
        List<QuadLine> result = new ArrayList<QuadLine>();
        for (int i = 0; i < list.size(); i++) {
            QuadLine a = list.get(i);
            List<QuadLine> current = new ArrayList<QuadLine>();
            current.add(a);
            for (int j = 0; j < list.size(); j++) {
                if (i == j) continue;
                QuadLine b = list.get(j);
                List<QuadLine> next = new ArrayList<QuadLine>();
                for (QuadLine piece : current) {
                    next.addAll(piece.subtract(b));
                }
                current = next;
            }
            result.addAll(current);
        }
        return result;
    }
    @Override
    public String toString(){
        return "line->min:"+getMin()+";max:"+getMax()+";center:"+center;
    }
}
