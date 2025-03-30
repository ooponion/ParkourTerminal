package parkourterminal.data.landingblock.intf;

import net.minecraft.util.AxisAlignedBB;

public class AABB {
    AxisAlignedBB axisAlignedBB;
    public AABB(AxisAlignedBB axisAlignedBB){
        this.axisAlignedBB=axisAlignedBB;
    }
    public AxisAlignedBB getAxisAlignedBB() {
        return axisAlignedBB;
    }

    public void setAxisAlignedBB(AxisAlignedBB axisAlignedBB) {
        this.axisAlignedBB = axisAlignedBB;
    }
    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (!(obj instanceof AABB)) return false;

        AABB other = (AABB) obj;
        return this.axisAlignedBB.minX == other.axisAlignedBB.minX && this.axisAlignedBB.minY == other.axisAlignedBB.minY && this.axisAlignedBB.minZ == other.axisAlignedBB.minZ &&
                this.axisAlignedBB.maxX == other.axisAlignedBB.maxX && this.axisAlignedBB.maxY == other.axisAlignedBB.maxY && this.axisAlignedBB.maxZ == other.axisAlignedBB.maxZ;
    }
}
