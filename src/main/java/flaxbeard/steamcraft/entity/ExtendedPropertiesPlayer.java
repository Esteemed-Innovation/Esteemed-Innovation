package flaxbeard.steamcraft.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import org.apache.commons.lang3.tuple.MutablePair;

public class ExtendedPropertiesPlayer implements IExtendedEntityProperties {
    public World world;
    public EntityPlayer player;
    public MutablePair<Double, Double> lastMotions = null;
    public Float prevStep = null;

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setDouble("left", this.lastMotions.left);
        nbt.setDouble("right", this.lastMotions.right);
        compound.setTag("lastMotions", nbt);
        compound.setFloat("prevStep", this.prevStep);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        NBTTagCompound nbt = compound.getCompoundTag("lastMotions");
        this.lastMotions.left = nbt.getDouble("left");
        this.lastMotions.right = nbt.getDouble("right");
        this.prevStep = compound.getFloat("prevStep");
    }

    @Override
    public void init(Entity entity, World world) {
        this.world = world;
        this.player = (EntityPlayer) entity;
        if (this.prevStep == null) {
            this.prevStep = this.player.stepHeight;
        }
        if (this.lastMotions == null) {
            this.lastMotions = MutablePair.of(this.player.posX, this.player.posZ);
        }
    }
}
