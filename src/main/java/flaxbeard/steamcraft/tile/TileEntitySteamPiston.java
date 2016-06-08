package flaxbeard.steamcraft.tile;

import flaxbeard.steamcraft.SteamcraftBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TileEntitySteamPiston extends TileEntity {
    private static final String __OBFID = "CL_00000369";
    private Block storedBlock;
    private int storedMetadata;
    /**
     * the side the front of the piston is on
     */
    private int storedOrientation;
    /**
     * if this piston is extending or not
     */
    private boolean extending;
    private boolean shouldHeadBeRendered;
    private float progress;
    /**
     * the progress in (de)extending
     */
    private float lastProgress;
    private List pushedObjects = new ArrayList();

    public TileEntitySteamPiston(Block p_i45444_1_, int p_i45444_2_, int p_i45444_3_, boolean p_i45444_4_, boolean p_i45444_5_) {
        this.storedBlock = p_i45444_1_;
        this.storedMetadata = p_i45444_2_;
        this.storedOrientation = p_i45444_3_;
        this.extending = p_i45444_4_;
        this.shouldHeadBeRendered = p_i45444_5_;
    }

    public Block getStoredBlockID() {
        return this.storedBlock;
    }

    public int getBlockMetadata() {
        return this.storedMetadata;
    }

    /**
     * Returns true if a piston is extending
     */
    public boolean isExtending() {
        return this.extending;
    }

    public int getPistonOrientation() {
        return this.storedOrientation;
    }

    @SideOnly(Side.CLIENT)
    public boolean func_145867_d() {
        return this.shouldHeadBeRendered;
    }

    public float func_145860_a(float p_145860_1_) {
        if (p_145860_1_ > 1.0F) {
            p_145860_1_ = 1.0F;
        }

        return this.lastProgress + (this.progress - this.lastProgress) * p_145860_1_;
    }

    private void func_145863_a(float p_145863_1_, float p_145863_2_) {
        if (this.extending) {
            p_145863_1_ = 1.0F - p_145863_1_;
        } else {
            --p_145863_1_;
        }

        AxisAlignedBB axisalignedbb = SteamcraftBlocks.steamPiston_extension
          .func_149964_a(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.storedBlock, p_145863_1_,
            this.storedOrientation);

        if (axisalignedbb != null) {
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb);

            if (!list.isEmpty()) {
                this.pushedObjects.addAll(list);
                Iterator iterator = this.pushedObjects.iterator();

                while (iterator.hasNext()) {
                    Entity entity = (Entity) iterator.next();
                    entity.moveEntity((double) (p_145863_2_ * (float) Facing.offsetsXForSide[this.storedOrientation]), (double) (p_145863_2_ * (float) Facing.offsetsYForSide[this.storedOrientation]), (double) (p_145863_2_ * (float) Facing.offsetsZForSide[this.storedOrientation]));
                }

                this.pushedObjects.clear();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public float func_145865_b(float p_145865_1_) {
        return this.extending ? (this.func_145860_a(p_145865_1_) - 1.0F) * (float) Facing.offsetsXForSide[this.storedOrientation] : (1.0F - this.func_145860_a(p_145865_1_)) * (float) Facing.offsetsXForSide[this.storedOrientation];
    }

    @SideOnly(Side.CLIENT)
    public float func_145862_c(float p_145862_1_) {
        return this.extending ? (this.func_145860_a(p_145862_1_) - 1.0F) * (float) Facing.offsetsYForSide[this.storedOrientation] : (1.0F - this.func_145860_a(p_145862_1_)) * (float) Facing.offsetsYForSide[this.storedOrientation];
    }

    @SideOnly(Side.CLIENT)
    public float func_145859_d(float p_145859_1_) {
        return this.extending ? (this.func_145860_a(p_145859_1_) - 1.0F) * (float) Facing.offsetsZForSide[this.storedOrientation] : (1.0F - this.func_145860_a(p_145859_1_)) * (float) Facing.offsetsZForSide[this.storedOrientation];
    }

    /**
     * removes a piston's tile entity (and if the piston is moving, stops it)
     */
    public void clearPistonTileEntity() {
        if (this.lastProgress < 1.0F && this.worldObj != null) {
            this.lastProgress = this.progress = 1.0F;
            this.worldObj.removeTileEntity(this.xCoord, this.yCoord, this.zCoord);
            this.invalidate();

            if (this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord) == SteamcraftBlocks.steamPiston_extension) {
                this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, this.storedBlock, this.storedMetadata, 3);
                this.worldObj.notifyBlockOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.storedBlock);
            }
        }
    }

    @Override
    public void updateEntity() {
        this.lastProgress = this.progress;

        if (this.lastProgress >= 1.0F) {
            this.func_145863_a(1.0F, 0.25F);
            this.worldObj.removeTileEntity(this.xCoord, this.yCoord, this.zCoord);
            this.invalidate();

            if (this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord) == SteamcraftBlocks.steamPiston_extension) {
                this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, this.storedBlock, this.storedMetadata, 3);
                this.worldObj.notifyBlockOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.storedBlock);
            }
        } else {
            this.progress += 0.5F;

            if (this.progress >= 1.0F) {
                this.progress = 1.0F;
            }

            if (this.extending) {
                this.func_145863_a(this.progress, this.progress - this.lastProgress + 0.0625F);
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.storedBlock = Block.getBlockById(nbt.getInteger("blockId"));
        this.storedMetadata = nbt.getInteger("blockData");
        this.storedOrientation = nbt.getInteger("facing");
        this.lastProgress = this.progress = nbt.getFloat("progress");
        this.extending = nbt.getBoolean("extending");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("blockId", Block.getIdFromBlock(this.storedBlock));
        nbt.setInteger("blockData", this.storedMetadata);
        nbt.setInteger("facing", this.storedOrientation);
        nbt.setFloat("progress", this.lastProgress);
        nbt.setBoolean("extending", this.extending);
    }
}