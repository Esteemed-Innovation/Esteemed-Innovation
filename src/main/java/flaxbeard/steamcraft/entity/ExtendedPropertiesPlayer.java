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
    public MutablePair<Integer, Integer> axeInfo = null;
    public MutablePair<Integer, Integer> drillInfo = null;
    public MutablePair<Integer, Integer> shovelInfo = null;

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

        if (this.axeInfo != null) {
            compound.setTag("axeInfo", setToolData(this.axeInfo));
        }

        if (this.drillInfo != null) {
            compound.setTag("drillInfo", setToolData(this.drillInfo));
        }

        if (this.shovelInfo != null) {
            compound.setTag("shovelInfo", setToolData(this.shovelInfo));
        }
    }

    /**
     * Gets a new NBTTagCompound of the tool data for the given MutablePair.
     * @param pair The MutablePair to create the compound from.
     * @return The new NBTTagCompound containing ticks and speed info for the tool.
     */
    private NBTTagCompound setToolData(MutablePair<Integer, Integer> pair) {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("ticks", pair.left);
        compound.setInteger("speed", pair.right);
        return compound;
    }

    /**
     * Gets the needed MutablePair for the given tool and NBTTagCompound.
     * @param id The string identifier for the tag compound.
     * @param base The tag compound to get the data from.
     * @return A new MutablePair of ticks (left) and speed (right).
     */
    private MutablePair<Integer, Integer> getToolData(String id, NBTTagCompound base) {
        NBTTagCompound toolCompound = base.getCompoundTag(id);
        return MutablePair.of(toolCompound.getInteger("ticks"), toolCompound.getInteger("speed"));
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

        if (compound.hasKey("axeInfo")) {
            this.axeInfo = getToolData("axeInfo", compound);
        }

        if (compound.hasKey("drillInfo")) {
            this.drillInfo = getToolData("drillInfo", compound);
        }

        if (compound.hasKey("shovelInfo")) {
            this.shovelInfo = getToolData("shovelInfo", compound);
        }
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

        if (this.axeInfo == null) {
            this.axeInfo = MutablePair.of(0, 0);
        }

        if (this.drillInfo == null) {
            this.drillInfo = MutablePair.of(0, 0);
        }

        if (this.shovelInfo == null) {
            this.shovelInfo = MutablePair.of(0, 0);
        }
    }
}
