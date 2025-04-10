package parkourterminal.util.renderhelper;

import net.minecraft.util.AxisAlignedBB;

import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuadFace {
    private final Vector3d min,max;
    private final int fixedAxis;
    public QuadFace(Vector3d pos1,Vector3d pos2,int fixedAxis){
        this.min=new Vector3d(Math.min(pos1.x,pos2.x),Math.min(pos1.y,pos2.y),Math.min(pos1.z,pos2.z));
        this.max=new Vector3d(Math.max(pos1.x,pos2.x),Math.max(pos1.y,pos2.y),Math.max(pos1.z,pos2.z));
        this.fixedAxis=fixedAxis;
    }
    public Vector3d getMin() {
        return min;
    }

    public Vector3d getMax() {
        return max;
    }

    public int getFixedAxis() {
        return fixedAxis;
    }

    public double getMin(int axis) {
        if (axis == 0) return min.x;
        if (axis == 1) return min.y;
        return min.z;
    }

    public double getMax(int axis) {
        if (axis == 0) return max.x;
        if (axis == 1) return max.y;
        return max.z;
    }
    public boolean intersects(QuadFace other) {
        if (this.fixedAxis != other.fixedAxis||getMin(fixedAxis) != other.getMin(fixedAxis)||getMax(fixedAxis) != other.getMax(fixedAxis)) return false;
        int u = (fixedAxis + 1) % 3;
        int v = (fixedAxis + 2) % 3;
        return getMin(u) < other.getMax(u) && getMax(u) > other.getMin(u) &&
                getMin(v) < other.getMax(v) && getMax(v) > other.getMin(v);
    }

    public List<QuadFace> subtract(QuadFace other) {
        List<QuadFace> result = new ArrayList<QuadFace>();
        if (!this.intersects(other)) {
            result.add(this);
            return result;
        }

        int u = (fixedAxis + 1) % 3;
        int v = (fixedAxis + 2) % 3;

        double aMinU = getMin(u), aMaxU = getMax(u);
        double aMinV = getMin(v), aMaxV = getMax(v);

        double bMinU = other.getMin(u), bMaxU = other.getMax(u);
        double bMinV = other.getMin(v), bMaxV = other.getMax(v);

        double fixed = getMin(fixedAxis);

        if (aMinV < bMinV) {
            result.add(fromUV(aMinU, aMinV, aMaxU, Math.min(aMaxV, bMinV), fixed, fixedAxis));
        }
        if (aMaxV > bMaxV) {
            result.add(fromUV(aMinU, Math.max(aMinV, bMaxV), aMaxU, aMaxV, fixed, fixedAxis));
        }
        if (aMinU < bMinU) {
            double vMin = Math.max(aMinV, bMinV);
            double vMax = Math.min(aMaxV, bMaxV);
            if (vMin < vMax)
                result.add(fromUV(aMinU, vMin, Math.min(aMaxU, bMinU), vMax, fixed, fixedAxis));
        }
        if (aMaxU > bMaxU) {
            double vMin = Math.max(aMinV, bMinV);
            double vMax = Math.min(aMaxV, bMaxV);
            if (vMin < vMax)
                result.add(fromUV(Math.max(aMinU, bMaxU), vMin, aMaxU, vMax, fixed, fixedAxis));
        }

        return result;
    }

    private static QuadFace fromUV(double u1, double v1, double u2, double v2, double fixed, int fixedAxis) {
        Vector3d p1 = new Vector3d();
        Vector3d p2 = new Vector3d();
        int u = (fixedAxis + 1) % 3;
        int v = (fixedAxis + 2) % 3;

        if (fixedAxis == 0) { p1.x = fixed; p2.x = fixed; }
        if (fixedAxis == 1) { p1.y = fixed; p2.y = fixed; }
        if (fixedAxis == 2) { p1.z = fixed; p2.z = fixed; }

        if (u == 0) { p1.x = u1; p2.x = u2; }
        if (u == 1) { p1.y = u1; p2.y = u2; }
        if (u == 2) { p1.z = u1; p2.z = u2; }

        if (v == 0) { p1.x = v1; p2.x = v2; }
        if (v == 1) { p1.y = v1; p2.y = v2; }
        if (v == 2) { p1.z = v1; p2.z = v2; }

        return new QuadFace(p1, p2, fixedAxis);
    }
    public static List<QuadFace> subtractQuadFaceList(List<QuadFace> list) {
        List<QuadFace> result = new ArrayList<QuadFace>();
        for (int i = 0; i < list.size(); i++) {
            QuadFace a = list.get(i);
            List<QuadFace> current = new ArrayList<QuadFace>();
            current.add(a);
            for (int j = 0; j < list.size(); j++) {
                if (i == j) continue;
                QuadFace b = list.get(j);
                List<QuadFace> next = new ArrayList<QuadFace>();
                for (QuadFace piece : current) {
                    next.addAll(piece.subtract(b));
                }
                current = next;
            }
            result.addAll(current);
        }
        return result;
    }
    public static List<QuadFace> fromAABB(AxisAlignedBB aabb){
        List<QuadFace> faces = new ArrayList<QuadFace>();

        // Y轴固定
        faces.add(new QuadFace(
                new Vector3d(aabb.minX, aabb.minY, aabb.minZ),
                new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ),
                1
        ));
        faces.add(new QuadFace(
                new Vector3d(aabb.minX, aabb.maxY, aabb.minZ),
                new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ),
                1
        ));

        // Z轴固定
        faces.add(new QuadFace(
                new Vector3d(aabb.minX, aabb.minY, aabb.minZ),
                new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ),
                2
        ));
        faces.add(new QuadFace(
                new Vector3d(aabb.minX, aabb.minY, aabb.maxZ),
                new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ),
                2
        ));

        // X轴固定
        faces.add(new QuadFace(
                new Vector3d(aabb.minX, aabb.minY, aabb.minZ),
                new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ),
                0
        ));
        faces.add(new QuadFace(
                new Vector3d(aabb.maxX, aabb.minY, aabb.minZ),
                new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ),
                0
        ));

        return faces;
    }
    public List<Vector3d> getVerticesClockwise() {
        List<Vector3d> vertices = new ArrayList<Vector3d>(4);
        int u = (fixedAxis + 1) % 3;
        int v = (fixedAxis + 2) % 3;

        double[] minArr = {min.x, min.y, min.z};
        double[] maxArr = {max.x, max.y, max.z};
        double fixedValue = minArr[fixedAxis];

        double u0 = minArr[u], u1 = maxArr[u];
        double v0 = minArr[v], v1 = maxArr[v];

        Vector3d[] coords = new Vector3d[4];
        if (fixedAxis == 0) {
            coords[0] = new Vector3d(fixedValue, u0, v0);
            coords[1] = new Vector3d(fixedValue, u1, v0);
            coords[2] = new Vector3d(fixedValue, u1, v1);
            coords[3] = new Vector3d(fixedValue, u0, v1);
        } else if (fixedAxis == 1) {
            coords[0] = new Vector3d(v0, fixedValue, u0);
            coords[1] = new Vector3d(v1, fixedValue, u0);
            coords[2] = new Vector3d(v1, fixedValue, u1);
            coords[3] = new Vector3d(v0, fixedValue, u1);
        } else if (fixedAxis == 2) {
            coords[0] = new Vector3d(u0, v0, fixedValue);
            coords[1] = new Vector3d(u1, v0, fixedValue);
            coords[2] = new Vector3d(u1, v1, fixedValue);
            coords[3] = new Vector3d(u0, v1, fixedValue);
        }

        Collections.addAll(vertices, coords);
        return vertices;
    }
    public List<QuadLine> getEdges() {
        List<Vector3d> verts = getVerticesClockwise();
        List<QuadLine> edges = new ArrayList<QuadLine>();

        for (int i = 0; i < 4; i++) {
            Vector3d start = verts.get(i);
            Vector3d end = verts.get((i + 1) % 4);

            // 中心点
            Vector3d center = new Vector3d(
                    (start.x + end.x) / 2,
                    (start.y + end.y) / 2,
                    (start.z + end.z) / 2
            );

            // 长度
            double dx = start.x - end.x;
            double dy = start.y - end.y;
            double dz = start.z - end.z;
            double length = Math.abs(dx+dy+dz);

            // 轴向
            int axis = -1;
            if (start.x != end.x && start.y == end.y && start.z == end.z) axis = 0;
            else if (start.y != end.y && start.x == end.x && start.z == end.z) axis = 1;
            else if (start.z != end.z && start.x == end.x && start.y == end.y) axis = 2;

            if (axis != -1) {
                edges.add(new QuadLine(center, length, axis,fixedAxis));
            } else {
                throw new IllegalStateException("Invalid edge between points: " + start + " and " + end);
            }
        }

        return edges;
    }
    @Override
    public String toString(){
        return "Quad->min:"+getMin()+";max:"+getMax();
    }
}
