package eiteam.esteemedinnovation.heater;

import eiteam.esteemedinnovation.api.SteamingRegistry;
import net.minecraft.block.BlockFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TileEntitySteamFurnace extends TileEntityFurnace {
    /**
     * See getField in TileEntityFurnace.
     */
    public static final int FURNACE_BURN_TIME_ID = 0;
    public static final int CURRENT_ITEM_BURN_TIME_ID = 1;
    public static final int COOK_TIME_ID = 2;
    public static final int TOTAL_COOK_TIME_ID = 3;

    @Override
    public void update() {
        int numHeaters = 0;

        for (EnumFacing dir2 : EnumFacing.VALUES) {
            BlockPos offsetPos = pos.offset(dir2);
            TileEntity tile = world.getTileEntity(offsetPos);
            if (tile instanceof TileEntitySteamHeater && ((TileEntitySteamHeater) tile).getSteamShare() > 2 &&
              world.getBlockState(offsetPos).getValue(BlockSteamHeater.FACING) == dir2.getOpposite()) {
                numHeaters++;
            }
        }
        numHeaters = Math.min(4, numHeaters);
        if (numHeaters == 0) {
            TileEntitySteamHeater.replaceWith(this, new TileEntityFurnace());
        }

        int furnaceBurnTime = getField(FURNACE_BURN_TIME_ID);
        boolean flag = furnaceBurnTime > 0;
        boolean flag1 = false;

        if (furnaceBurnTime > 0) {
            setField(FURNACE_BURN_TIME_ID, furnaceBurnTime - 1);
            furnaceBurnTime--;
        }

        if (!world.isRemote) {
            if (furnaceBurnTime == 0 && canSmelt()) {
                ItemStack inSlot1 = getStackInSlot(1);
                int currentItemBurnTime = furnaceBurnTime = getItemBurnTime(inSlot1);
                setField(CURRENT_ITEM_BURN_TIME_ID, currentItemBurnTime);
                setField(FURNACE_BURN_TIME_ID, furnaceBurnTime);

                if (furnaceBurnTime > 0) {
                    flag1 = true;

                    ItemStack copy = inSlot1.copy();
                    copy.shrink(1);
                    setInventorySlotContents(1, copy);

                    if (inSlot1.isEmpty()) {
                        setInventorySlotContents(1, inSlot1.getItem().getContainerItem(inSlot1));
                    }
                }
            }

            int furnaceCookTime;
            if (isBurning() && canSmelt()) {
                furnaceCookTime = getField(COOK_TIME_ID);
                ++furnaceCookTime;
                setField(COOK_TIME_ID, furnaceCookTime);

                if (furnaceCookTime == 200) {
                    furnaceCookTime = 0;
                    smeltItem();
                    setField(COOK_TIME_ID, furnaceCookTime);
                    flag1 = true;
                }
            } else {
                furnaceCookTime = 0;
                setField(COOK_TIME_ID, furnaceCookTime);
            }

            if (flag != furnaceBurnTime > 0) {
                flag1 = true;
                BlockFurnace.setState(furnaceBurnTime > 0, world, pos);
            }
        }

        if (flag1) {
            markDirty();
        }
    }

    public boolean canSmelt() {
        ItemStack slot0 = getStackInSlot(0);
        if (slot0.isEmpty()) {
            return false;
        } else {
            ItemStack output = SteamingRegistry.getSteamingResult(slot0);
            if (output == null) {
                return false;
            }
            ItemStack slot2 = getStackInSlot(2);
            if (slot2.isEmpty()) {
                return true;
            }
            if (!slot2.isItemEqual(output)) {
                return false;
            }
            int result = slot2.getCount() + output.getCount();
            // Forge BugFix: Make it respect stack sizes properly.
            return result <= getInventoryStackLimit() && result <= slot2.getMaxStackSize();
        }
    }

    @Override
    public void smeltItem() {
        ItemStack slot0 = getStackInSlot(0);
        if (canSmelt()) {
            assert !slot0.isEmpty();
            ItemStack output = SteamingRegistry.getSteamingResult(slot0);
            ItemStack slot2 = getStackInSlot(2);
            if (slot2.isEmpty()) {
                setInventorySlotContents(2, output.copy());
            } else if (slot2.getItem() == output.getItem()) {
                ItemStack copy = slot2.copy();
                copy.grow(output.getCount());
                setInventorySlotContents(2, copy);
            }

            ItemStack copy = slot0.copy();
            copy.shrink(1);
            setInventorySlotContents(0, copy);
        }
    }
}
