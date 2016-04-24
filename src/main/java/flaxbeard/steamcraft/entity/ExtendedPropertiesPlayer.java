package flaxbeard.steamcraft.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.*;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import org.apache.commons.lang3.tuple.MutablePair;

public class ExtendedPropertiesPlayer implements IExtendedEntityProperties {
    public World world;
    public EntityPlayer player;
    public MutablePair<Double, Double> lastMotions = null;
    public Float prevStep = null;
    public boolean isRangeExtended;
    public int tickCache;

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        if (this.lastMotions != null) {
            NBTTagCompound motionList = new NBTTagCompound();
            motionList.setDouble("left", this.lastMotions.left);
            motionList.setDouble("right", this.lastMotions.right);
            compound.setTag("lastMotions", motionList);
        }

        compound.setInteger("tickCache", this.tickCache);

        if (this.prevStep != null) {
            compound.setFloat("prevStep", this.prevStep);
        }

        compound.setBoolean("isRangeExtended", this.isRangeExtended);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        if (compound.hasKey("lastMotions")) {
            NBTTagCompound motionList = compound.getCompoundTag("lastMotions");
            this.lastMotions.left = motionList.getDouble("left");
            this.lastMotions.right = motionList.getDouble("right");
        }

        if (compound.hasKey("prevStep")) {
            this.prevStep = compound.getFloat("prevStep");
        }

        this.isRangeExtended = compound.getBoolean("isRangeExtended");
        this.tickCache = compound.getInteger("tickCache");
    }

    @Override
    public void init(Entity entity, World world) {
        this.world = world;
        this.player = (EntityPlayer) entity;

        this.isRangeExtended = false;
        this.tickCache = -1;

        if (this.lastMotions == null) {
            this.lastMotions = MutablePair.of(this.player.posX, this.player.posZ);
        }
    }
}
