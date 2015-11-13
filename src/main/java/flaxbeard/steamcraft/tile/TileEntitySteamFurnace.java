package flaxbeard.steamcraft.tile;

import flaxbeard.steamcraft.api.SteamcraftRegistry;
import net.minecraft.block.BlockFurnace;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.util.ForgeDirection;
import org.apache.commons.lang3.tuple.MutablePair;

public class TileEntitySteamFurnace extends TileEntityFurnace {
    //private ItemStack[] furnaceItemStacks = new ItemStack[3];

    @Override
    public void updateEntity() {
        int numHeaters = 0;

        for (int i = 0; i < 6; i++) {
            ForgeDirection dir2 = ForgeDirection.getOrientation(i);
            int x = xCoord + dir2.offsetX;
            int y = yCoord + dir2.offsetY;
            int z = zCoord + dir2.offsetZ;
            if (this.worldObj.getTileEntity(x, y, z) != null) {
                if (this.worldObj.getTileEntity(x, y, z) instanceof TileEntitySteamHeater && ((TileEntitySteamHeater) this.worldObj.getTileEntity(x, y, z)).getSteamShare() > 2 && this.worldObj.getBlockMetadata(x, y, z) == ForgeDirection.OPPOSITES[i]) {
                    numHeaters++;
                }
            }
        }
        if (numHeaters == 0) {
            TileEntitySteamHeater.replace(this);
        }

        boolean flag = this.furnaceBurnTime > 0;
        boolean flag1 = false;

        if (this.furnaceBurnTime > 0) {
            --this.furnaceBurnTime;
        }

        if (!this.worldObj.isRemote) {
            if (this.furnaceBurnTime == 0 && this.canSmelt()) {
                this.currentItemBurnTime = this.furnaceBurnTime = getItemBurnTime(this.getStackInSlot(1));

                if (this.furnaceBurnTime > 0) {
                    flag1 = true;

                    if (this.getStackInSlot(1) != null) {
                        ItemStack copy = this.getStackInSlot(1).copy();
                        copy.stackSize--;
                        this.setInventorySlotContents(1, copy);

                        if (this.getStackInSlot(1).stackSize == 0) {
                            setInventorySlotContents(1, getStackInSlot(1).getItem().getContainerItem(getStackInSlot(1)));
                        }
                    }
                }
            }

            if (this.isBurning() && this.canSmelt()) {
                ++this.furnaceCookTime;

                if (this.furnaceCookTime == 200) {
                    this.furnaceCookTime = 0;
                    this.smeltItem();
                    flag1 = true;
                }
            } else {
                this.furnaceCookTime = 0;
            }

            if (flag != this.furnaceBurnTime > 0) {
                flag1 = true;
                BlockFurnace.updateFurnaceBlockState(this.furnaceBurnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            }
        }

        if (flag1) {
            this.markDirty();
        }
    }

    public boolean canSmelt() {
        if (this.getStackInSlot(0) == null) {
            return false;
        } else {
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.getStackInSlot(0));
            if (itemstack == null) return false;
            if (SteamcraftRegistry.steamedFoods.containsKey(MutablePair.of(itemstack.getItem(), itemstack.getItemDamage()))) {
                int meta = SteamcraftRegistry.steamedFoods.get(MutablePair.of(itemstack.getItem(), itemstack.getItemDamage())).right;
                Item item = SteamcraftRegistry.steamedFoods.get(MutablePair.of(itemstack.getItem(), itemstack.getItemDamage())).left;
                if (meta == -1) {
                    itemstack = new ItemStack(item);
                } else {
                    itemstack = new ItemStack(item, 1, meta);
                }
            } else if (SteamcraftRegistry.steamedFoods.containsKey(MutablePair.of(itemstack.getItem(), -1))) {
                int meta = SteamcraftRegistry.steamedFoods.get(MutablePair.of(itemstack.getItem(), -1)).right;
                Item item = SteamcraftRegistry.steamedFoods.get(MutablePair.of(itemstack.getItem(), -1)).left;
                if (meta == -1) {
                    itemstack = new ItemStack(item);
                } else {
                    itemstack = new ItemStack(item, 1, meta);
                }
            }
            if (this.getStackInSlot(2) == null) return true;
            if (!this.getStackInSlot(2).isItemEqual(itemstack)) return false;
            int result = getStackInSlot(2).stackSize + itemstack.stackSize;
            return result <= getInventoryStackLimit() && result <= this.getStackInSlot(2).getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.
        }
        //return true;
    }

    @Override
    public void smeltItem() {
        if (this.canSmelt()) {
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.getStackInSlot(0));
            if (SteamcraftRegistry.steamedFoods.containsKey(MutablePair.of(itemstack.getItem(), itemstack.getItemDamage()))) {
                int meta = SteamcraftRegistry.steamedFoods.get(MutablePair.of(itemstack.getItem(), itemstack.getItemDamage())).right;
                Item item = SteamcraftRegistry.steamedFoods.get(MutablePair.of(itemstack.getItem(), itemstack.getItemDamage())).left;
                if (meta == -1) {
                    itemstack = new ItemStack(item);
                } else {
                    itemstack = new ItemStack(item, 1, meta);
                }
            } else if (SteamcraftRegistry.steamedFoods.containsKey(MutablePair.of(itemstack.getItem(), -1))) {
                int meta = SteamcraftRegistry.steamedFoods.get(MutablePair.of(itemstack.getItem(), -1)).right;
                Item item = SteamcraftRegistry.steamedFoods.get(MutablePair.of(itemstack.getItem(), -1)).left;
                if (meta == -1) {
                    itemstack = new ItemStack(item);
                } else {
                    itemstack = new ItemStack(item, 1, meta);
                }
            }
            if (this.getStackInSlot(2) == null) {
                this.setInventorySlotContents(2, itemstack.copy());
            } else if (this.getStackInSlot(2).getItem() == itemstack.getItem()) {
                ItemStack copy = this.getStackInSlot(2).copy();
                copy.stackSize += itemstack.stackSize;
                this.setInventorySlotContents(2, copy);
            }

            ItemStack copy = this.getStackInSlot(0).copy();
            copy.stackSize--;
            this.setInventorySlotContents(0, copy);

            if (this.getStackInSlot(0).stackSize <= 0) {
                this.setInventorySlotContents(0, null);
            }
        }
    }

}
