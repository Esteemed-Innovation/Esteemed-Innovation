package eiteam.esteemedinnovation.boiler;

import eiteam.esteemedinnovation.api.util.FluidHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerBoiler extends Container {
    private TileEntityBoiler tileEntity;
    private int lastCookTime;
    private int lastBurnTime;
    private int lastItemBurnTime;
    private int lastPressure;
    private int lastWater;

    public ContainerBoiler(InventoryPlayer inv, TileEntityBoiler tileEntity) {
        this.tileEntity = tileEntity;

        addSlotToContainer(new Slot(tileEntity, 1, 58, 55));
        addSlotToContainer(new Slot(tileEntity, 0, 58, 32));
        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlotToContainer(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            addSlotToContainer(new Slot(inv, i, 8 + i * 18, 142));
        }
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        listener.sendProgressBarUpdate(this, 0, tileEntity.furnaceCookTime);
        listener.sendProgressBarUpdate(this, 1, tileEntity.furnaceBurnTime);
        listener.sendProgressBarUpdate(this, 2, TileEntityBoiler.getItemBurnTime(null));
        listener.sendProgressBarUpdate(this, 3, (int) Math.floor((double) tileEntity.getPressure() * 1000));
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (IContainerListener listener : listeners) {
            if (lastCookTime != tileEntity.furnaceCookTime) {
                listener.sendProgressBarUpdate(this, 0, tileEntity.furnaceCookTime);
            }

            if (lastBurnTime != tileEntity.furnaceBurnTime) {
                listener.sendProgressBarUpdate(this, 1, tileEntity.furnaceBurnTime);
            }

            if (lastItemBurnTime != tileEntity.currentItemBurnTime) {
                listener.sendProgressBarUpdate(this, 2, tileEntity.currentItemBurnTime);
            }

            if (lastPressure != tileEntity.getPressureAsInt()) {
                listener.sendProgressBarUpdate(this, 3, tileEntity.getPressureAsInt());
            }

            if (this.lastWater != tileEntity.getTank().getFluidAmount()) {
                listener.sendProgressBarUpdate(this, 4, tileEntity.getTank().getFluidAmount());
            }
        }

        lastCookTime = tileEntity.furnaceCookTime;
        lastBurnTime = tileEntity.furnaceBurnTime;
        lastItemBurnTime = tileEntity.currentItemBurnTime;
        lastPressure = tileEntity.getPressureAsInt();
        lastWater = tileEntity.getTank().getFluidAmount();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int id, int data) {
        if (id == 0) {
            tileEntity.furnaceCookTime = data;
        }

        if (id == 1) {
            tileEntity.furnaceBurnTime = data;
        }

        if (id == 2) {
            tileEntity.currentItemBurnTime = data;
        }
        if (id == 3) {
            tileEntity.setPressure((float) data / 1000F);
        }
        if (id == 4) {
            int current = tileEntity.getTank().getFluidAmount();
            int diff = data - current;
            if (diff > 0) {
                tileEntity.getTank().fill(new FluidStack(FluidHelper.getWaterFluid(), diff), true);
            } else {
                tileEntity.getTank().drain(-1 * diff, true);
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
        return tileEntity.isUseableByPlayer(par1EntityPlayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack itemstack = null;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 2) {
                return null;
            } else if (index != 1 && index != 0) {
                if (FluidHelper.itemStackIsWaterContainer(itemstack1)) {
                    if (!mergeItemStack(itemstack1, 0, 1, false)) {
                        return null;
                    }
                } else if (TileEntityFurnace.isItemFuel(itemstack1)) {
                    if (!mergeItemStack(itemstack1, 1, 2, false)) {
                        return null;
                    }
                } else if (index >= 2 && index < 30) {
                    if (!mergeItemStack(itemstack1, 30, 38, false)) {
                        return null;
                    }
                } else if (index >= 30 && index < 39 && !mergeItemStack(itemstack1, 3, 30, false)) {
                    return null;
                }
            } else if (!mergeItemStack(itemstack1, 3, 38, false)) {
                return null;
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }
}
