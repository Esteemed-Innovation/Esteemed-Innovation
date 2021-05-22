package eiteam.esteemedinnovation.heater;

import eiteam.esteemedinnovation.api.heater.SteamingRegistry;
import eiteam.esteemedinnovation.api.heater.HeatableRegistry;
import eiteam.esteemedinnovation.api.heater.Steamable;
import net.minecraft.block.BlockFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class VanillaFurnaceHandler implements HeatableRegistry.HeatHandler {
    @Override
    public Steamable apply(World world, BlockPos pos) {
        if (world.isBlockLoaded(pos)) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileEntityFurnace) {
                return new VanillaFurnaceSteamable((TileEntityFurnace) tile);
            }
        }
        return null;
    }

    private static class VanillaFurnaceSteamable implements Steamable {
        public static final int FURNACE_BURN_TIME_ID = 0;
        public static final int CURRENT_ITEM_BURN_TIME_ID = 1;
        public static final int COOK_TIME_ID = 2;
        public static final int TOTAL_COOK_TIME_ID = 3;

        final TileEntityFurnace furnace;
        boolean hasStarted = false;

        VanillaFurnaceSteamable(TileEntityFurnace furnace) {
            this.furnace = furnace;
        }

        @Override
        public boolean acceptsSteam() {
            //Copy of TileEntityFurnace#canSmelt, except it uses the steaming registry
            if (furnace.getStackInSlot(0).isEmpty()) {
                return false;
            } else {
                ItemStack itemstack = SteamingRegistry.getSteamingResult(furnace.getStackInSlot(0));

                if (itemstack.isEmpty()) {
                    return false;
                } else {
                    ItemStack itemstack1 = furnace.getStackInSlot(2);

                    if (itemstack1.isEmpty()) {
                        return true;
                    } else if (!itemstack1.isItemEqual(itemstack)) {
                        return false;
                    } else if (itemstack1.getCount() + itemstack.getCount() <= furnace.getInventoryStackLimit() && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) {
                        return true;
                    } else {
                        return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize();
                    }
                }
            }
        }

        @Override
        public void steam() {
            final boolean needStart = !isBurning();
            final int topBurnTime = 200;
            final int totalCookTime = furnace.getField(TOTAL_COOK_TIME_ID);
            if (!hasStarted && furnace.getWorld().tickableTileEntities.contains(furnace)) {
                DisableTileEntityHandler.tileEntitiesToRemove.add(furnace);
                hasStarted = true;
            }
            if (furnace.getField(FURNACE_BURN_TIME_ID) < topBurnTime) {
                furnace.setField(FURNACE_BURN_TIME_ID, furnace.getField(FURNACE_BURN_TIME_ID) + 1);
                if (needStart) {
                    BlockFurnace.setState(furnace.getField(FURNACE_BURN_TIME_ID) > 0, furnace.getWorld(), furnace.getPos());
                }
            } else {
                furnace.setField(COOK_TIME_ID, furnace.getField(COOK_TIME_ID) + 1);
                final int cookTime = furnace.getField(COOK_TIME_ID);
                if (cookTime >= totalCookTime) {
                    smeltItem();
                    furnace.setField(COOK_TIME_ID, 0);
                }
            }
        }

        private void smeltItem() {
            if (acceptsSteam()) {
                ItemStack input = furnace.getStackInSlot(0);
                ItemStack result = SteamingRegistry.getSteamingResult(input);
                ItemStack output = furnace.getStackInSlot(2);

                if (output.isEmpty()) {
                    furnace.setInventorySlotContents(2, result.copy());
                } else if (output.getItem() == result.getItem()) {
                    output.grow(result.getCount());
                }

                input.shrink(1);
            }
        }

        @Override
        public void stopSteam() {
            if (!furnace.getWorld().tickableTileEntities.contains(furnace)) {
                DisableTileEntityHandler.tileEntitiesToAdd.add(furnace);
            }
            hasStarted = false;
        }

        @Override
        public boolean isBurning() {
            return furnace.isBurning();
        }
    }
}
