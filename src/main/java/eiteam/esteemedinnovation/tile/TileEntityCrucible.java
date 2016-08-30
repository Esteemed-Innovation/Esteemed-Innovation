package eiteam.esteemedinnovation.tile;

import eiteam.esteemedinnovation.api.crucible.CrucibleFormula;
import eiteam.esteemedinnovation.api.crucible.CrucibleLiquid;
import eiteam.esteemedinnovation.api.crucible.CrucibleRegistry;
import eiteam.esteemedinnovation.api.mold.ICrucibleMold;
import eiteam.esteemedinnovation.api.tile.TileEntityBase;
import eiteam.esteemedinnovation.block.BlockCrucible;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.HashMap;

public class TileEntityCrucible extends TileEntityBase implements ITickable {
    public ArrayList<CrucibleLiquid> contents = new ArrayList<>();
    public HashMap<CrucibleLiquid, Integer> number = new HashMap<>();
    public boolean hasUpdated = true;
    public boolean needsUpdate = false;
    public int tipTicks = 0;
    private int targetFill = -1;
    private boolean tipping;
    public boolean isPowered;
    private int lastComparatorOutput = 0;

    public TileEntityCrucible() {
        isPowered = false;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTTagList nbttaglist = (NBTTagList) nbt.getTag("liquids");

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            CrucibleLiquid liquid = CrucibleRegistry.getLiquidFromName(nbttagcompound1.getString("name"));
            if (liquid != null) {
                contents.add(liquid);
                number.put(liquid, (int) nbttagcompound1.getShort("amount"));
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        NBTTagList nbttaglist = new NBTTagList();

        for (CrucibleLiquid liquid : contents) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.setShort("amount", (short) (int) number.get(liquid));
            nbttagcompound1.setString("name", liquid.name);
            nbttaglist.appendTag(nbttagcompound1);
        }

        nbt.setTag("liquids", nbttaglist);

        return nbt;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        super.getUpdatePacket();
        NBTTagCompound access = new NBTTagCompound();
        NBTTagList nbttaglist = new NBTTagList();
        access.setInteger("tipTicks", tipTicks);
        access.setBoolean("tipping", tipping);

        for (CrucibleLiquid liquid : contents) {
            if (liquid != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setShort("amount", (short) (int) number.get(liquid));
                nbttagcompound1.setString("name", liquid.name);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        access.setTag("liquids", nbttaglist);

        return new SPacketUpdateTileEntity(pos, 1, access);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound access = pkt.getNbtCompound();
        NBTTagList nbttaglist = (NBTTagList) access.getTag("liquids");

        contents = new ArrayList<>();
        number = new HashMap<>();
        if (tipTicks == 0) {
            tipTicks = access.getInteger("tipTicks");
        }
        tipping = access.getBoolean("tipping");
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            CrucibleLiquid liquid = CrucibleRegistry.getLiquidFromName(nbttagcompound1.getString("name"));
            contents.add(liquid);
            number.put(liquid, (int) nbttagcompound1.getShort("amount"));
        }
        markForResync();
    }

    @Override
    public void update() {
        if (targetFill < 0) {
            targetFill = getFill();
        }
        if (this.getFill() == targetFill) {
            hasUpdated = true;
        }
        IBlockState state = worldObj.getBlockState(pos);
        EnumFacing myDir = state.getValue(BlockCrucible.FACING);

        if (worldObj.isBlockIndirectlyGettingPowered(pos) > 0) {
            isPowered = true;
        }
        if (tipping || isPowered) {
            tipTicks++;
            if (tipTicks == 45 && !worldObj.isRemote) {
                int posX = pos.getX() + myDir.getFrontOffsetX();
                int posY = pos.getY();
                int posZ = pos.getZ() + myDir.getFrontOffsetZ();
                BlockPos offsetPos = new BlockPos(posX, posY, posZ);
                TileEntity tile = worldObj.getTileEntity(offsetPos);
                if (tile != null && tile instanceof TileEntityMold) {
                    TileEntityMold mold = (TileEntityMold) tile;
                    if (mold.canPour() && contents.size() > 0) {
                        ICrucibleMold crucibleMold = (ICrucibleMold) mold.mold.getItem();
                        CrucibleLiquid liquid = this.getNextLiquid(crucibleMold);
                        if (liquid != null) {
                            if (!worldObj.isRemote) {
                                mold.pour(liquid);
                            }
                            int currNum = number.get(liquid);
                            currNum -= crucibleMold.getCostToMold(liquid);
                            if (currNum == 0) {
                                contents.remove(liquid);
                            }
                            number.remove(liquid);
                            if (currNum > 0) {
                                number.put(liquid, currNum);
                            }
                            needsUpdate = true;
                        }
                    }
                }
            }
            if (tipTicks > 140) {
                tipTicks = 0;
                tipping = false;
                isPowered = false;
            }
        }

        // Optimization: Don't iterate over all the liquids if we have nothing.
        if (!contents.isEmpty()) {
            for (CrucibleLiquid liquid : CrucibleRegistry.liquids) {
                if (liquid.recipe == null) {
                    continue;
                }

                CrucibleFormula recipe = liquid.recipe;
                if (recipe.matches(contents, number, recipe)) {
                    int currNum = number.get(recipe.liquid1);
                    currNum -= recipe.liquid1num;
                    if (currNum == 0) {
                        contents.remove(recipe.liquid1);
                    }
                    number.remove(recipe.liquid1);
                    number.put(recipe.liquid1, currNum);

                    currNum = number.get(recipe.liquid2);
                    currNum -= recipe.liquid2num;
                    if (currNum == 0) {
                        contents.remove(recipe.liquid2);
                    }
                    number.remove(recipe.liquid2);
                    number.put(recipe.liquid2, currNum);

                    if (!number.containsKey(liquid)) {
                        contents.add(liquid);
                        number.put(liquid, 0);
                    }
                    currNum = number.get(liquid);
                    currNum += recipe.output;
                    number.remove(liquid);
                    number.put(liquid, currNum);
                    needsUpdate = true;
                }
            }
        }

        if (getComparatorOutput() != lastComparatorOutput){
            lastComparatorOutput = getComparatorOutput();
            markDirty();
        }
        if (needsUpdate) {
            markForResync(state);
            needsUpdate = false;
        }

    }

    public int getFill() {
        int fill = 0;
        for (CrucibleLiquid liquid : contents) {
            fill += number.get(liquid);
        }
        return fill;
    }

    public ItemStack fillWith(ItemStack stack, int amount, MutablePair output) {
        int fill = getFill();
        if (!worldObj.isRemote) {
            if (fill + amount <= 90 && hasUpdated) {
                CrucibleLiquid fluid = (CrucibleLiquid) output.left;
                if (!contents.contains(fluid)) {
                    contents.add(fluid);
                    number.put(fluid, 0);
                }
                int currAmount = number.get(fluid);
                currAmount += amount;
                number.remove(fluid);
                number.put(fluid, currAmount);
                stack.stackSize--;
                hasUpdated = false;
                targetFill = fill + amount;
                needsUpdate = true;
            }
        }
        return stack;
    }

    public CrucibleLiquid getNextLiquid(ICrucibleMold mold) {
        for (CrucibleLiquid liquid : CrucibleRegistry.liquids) {
            if (number.containsKey(liquid)) {
                if (mold.canUseOn(liquid) && number.get(liquid) >= mold.getCostToMold(liquid)) {
                    return liquid;
                }
            }
        }
        return null;
    }

    public boolean isTipping() {
        return !(!tipping || tipTicks > 90);
    }

    public void setTipping() {
        tipping = true;
        tipTicks = 0;
    }

    public int getComparatorOutput() {
        return (int) ((double) 15 * (((double) getFill() / 90D)));
    }
}
