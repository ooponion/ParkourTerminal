package parkourterminal.data.landingblock.intf;

import javax.vecmath.Vector2d;

public class CondZone {
    private double xMin;
    private double xMax;
    private double zMin;
    private double zMax;
    private double y;
    public CondZone(double xMin,double xMax, double zMin,double zMax,double y){
        this.xMax=xMax;
        this.xMin=xMin;
        this.zMax=zMax;
        this.zMin=zMin;
        this.y=y;
    }

    public double getY() {
        return y;
    }

    public double getxMax() {
        return xMax;
    }

    public double getxMin() {
        return xMin;
    }

    public double getzMax() {
        return zMax;
    }

    public double getzMin() {
        return zMin;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setxMax(double xMax) {
        this.xMax = xMax;
    }

    public void setxMin(double xMin) {
        this.xMin = xMin;
    }

    public void setzMax(double zMax) {
        this.zMax = zMax;
    }

    public void setzMin(double zMin) {
        this.zMin = zMin;
    }

    public boolean insides(Vector2d pos) {
        return pos.x>=this.xMin&&pos.x<=this.xMax&&pos.y>=this.zMin&&pos.y<=this.zMax;
    }
}
