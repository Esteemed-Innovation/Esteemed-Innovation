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
        if (lastMotions != null) {
            nbt.setDouble("left", lastMotions.left);
            nbt.setDouble("right", lastMotions.right);
            compound.setTag("lastMotions", nbt);
        }

        if (prevStep != null) {
            compound.setFloat("prevStep", prevStep);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        NBTTagCompound nbt = compound.getCompoundTag("lastMotions");
        lastMotions.setLeft(nbt.getDouble("left"));
        lastMotions.setRight(nbt.getDouble("right"));
        prevStep = nbt.getFloat("prevStep");
    }

    @Override
    public void init(Entity entity, World world) {
        this.world = world;
        this.player = (EntityPlayer) entity;
    }
}
