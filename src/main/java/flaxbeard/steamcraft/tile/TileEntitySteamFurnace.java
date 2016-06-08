package flaxbeard.steamcraft.tile;

import flaxbeard.steamcraft.api.SteamcraftRegistry;
import net.minecraft.block.BlockFurnace;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.MutablePair;

public class TileEntitySteamFurnace extends TileEntityFurnace {
    /**
     * See getField in TileEntityFurnace.
     */
    private static final int FURNACE_BURN_TIME_ID = 0;
    private static final int CURRENT_ITEM_BURN_TIME_ID = 1;
    private static final int COOK_TIME_ID = 2;
    private static final int TOTAL_COOK_TIME_ID = 3;

    @Override
    public void update() {
        int numHeaters = 0;

        for (int i = 0; i < 6; i++) {
            EnumFacing dir2 = EnumFacing.getFront(i);
            int x = pos.getX() + dir2.getFrontOffsetX();
            int y = pos.getY() + dir2.getFrontOffsetY();
            int z = pos.getZ() + dir2.getFrontOffsetZ();
            BlockPos offsetPos = new BlockPos(x, y, z);
            TileEntity tile = worldObj.getTileEntity(offsetPos);
            if (tile != null && tile instanceof TileEntitySteamHeater &&
              ((TileEntitySteamHeater) tile).getSteamShare() > 2 && getBlockMetadata() == dir2.getOpposite().ordinal()) {
                numHeaters++;
            }
        }
        if (numHeaters == 0) {
            TileEntitySteamHeater.replace(this);
        }

        int furnaceBurnTime = super.getField(FURNACE_BURN_TIME_ID);
        boolean flag = furnaceBurnTime > 0;
        boolean flag1 = false;

        if (furnaceBurnTime > 0) {
            super.setField(FURNACE_BURN_TIME_ID, furnaceBurnTime - 1);
            furnaceBurnTime--;
        }

        if (!worldObj.isRemote) {
            if (furnaceBurnTime == 0 && canSmelt()) {
                ItemStack inSlot1 = getStackInSlot(1);
                int currentItemBurnTime = furnaceBurnTime = getItemBurnTime(inSlot1);
                super.setField(CURRENT_ITEM_BURN_TIME_ID, currentItemBurnTime);
                super.setField(FURNACE_BURN_TIME_ID, furnaceBurnTime);

                if (furnaceBurnTime > 0) {
                    flag1 = true;

                    if (inSlot1 != null) {
                        ItemStack copy = inSlot1.copy();
                        copy.stackSize--;
                        setInventorySlotContents(1, copy);

                        if (inSlot1.stackSize == 0) {
                            setInventorySlotContents(1, inSlot1.getItem().getContainerItem(inSlot1));
                        }
                    }
                }
            }

            int furnaceCookTime;
            if (isBurning() && canSmelt()) {
                furnaceCookTime = super.getField(COOK_TIME_ID);
                ++furnaceCookTime;
                super.setField(COOK_TIME_ID, furnaceCookTime);

                if (furnaceCookTime == 200) {
                    furnaceCookTime = 0;
                    smeltItem();
                    super.setField(COOK_TIME_ID, furnaceCookTime);
                    flag1 = true;
                }
            } else {
                furnaceCookTime = 0;
                super.setField(COOK_TIME_ID, furnaceCookTime);
            }

            if (flag != furnaceBurnTime > 0) {
                flag1 = true;
                BlockFurnace.setState(furnaceBurnTime > 0, worldObj, pos);
            }
        }

        if (flag1) {
            markDirty();
        }
    }

    public boolean canSmelt() {
        ItemStack slot0 = getStackInSlot(0);
        if (slot0 == null) {
            return false;
        } else {
            ItemStack output = FurnaceRecipes.instance().getSmeltingResult(slot0);
            if (output == null) {
                return false;
            }
            ItemStack slot2 = getStackInSlot(2);
            if (slot2 == null) {
                return true;
            }
            if (!slot2.isItemEqual(output)) {
                return false;
            }
            int result = slot2.stackSize + output.stackSize;
            // Forge BugFix: Make it respect stack sizes properly.
            return result <= getInventoryStackLimit() && result <= slot2.getMaxStackSize();
        }
    }

    @Override
    public void smeltItem() {
        ItemStack slot0 = getStackInSlot(0);
        if (canSmelt()) {
            ItemStack output = FurnaceRecipes.instance().getSmeltingResult(slot0);
            if (SteamcraftRegistry.steamingRecipes.containsKey(MutablePair.of(output.getItem(), output.getItemDamage()))) {
                int meta = SteamcraftRegistry.steamingRecipes.get(MutablePair.of(output.getItem(), output.getItemDamage())).right;
                Item item = SteamcraftRegistry.steamingRecipes.get(MutablePair.of(output.getItem(), output.getItemDamage())).left;
                if (meta == -1) {
                    output = new ItemStack(item);
                } else {
                    output = new ItemStack(item, 1, meta);
                }
            } else if (SteamcraftRegistry.steamingRecipes.containsKey(MutablePair.of(output.getItem(), -1))) {
                int meta = SteamcraftRegistry.steamingRecipes.get(MutablePair.of(output.getItem(), -1)).right;
                Item item = SteamcraftRegistry.steamingRecipes.get(MutablePair.of(output.getItem(), -1)).left;
                if (meta == -1) {
                    output = new ItemStack(item);
                } else {
                    output = new ItemStack(item, 1, meta);
                }
            }
            ItemStack slot2 = getStackInSlot(2);
            if (slot2 == null) {
                this.setInventorySlotContents(2, output.copy());
            } else if (slot2.getItem() == output.getItem()) {
                ItemStack copy = slot2.copy();
                copy.stackSize += output.stackSize;
                this.setInventorySlotContents(2, copy);
            }

            ItemStack copy = output.copy();
            copy.stackSize--;
            this.setInventorySlotContents(0, copy);

            if (output.stackSize <= 0) {
                this.setInventorySlotContents(0, null);
            }
        }
    }

}
