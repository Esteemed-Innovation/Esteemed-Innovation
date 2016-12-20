package eiteam.esteemedinnovation.commons.gui;

import eiteam.esteemedinnovation.api.IEngineerable;
import eiteam.esteemedinnovation.engineeringtable.TileEntityEngineeringTable;
import net.minecraft.inventory.Slot;

public class SlotLimitedStackSize extends Slot {

    private int limit = 64;
    private TileEntityEngineeringTable furnace;
    private int myNum;

    public SlotLimitedStackSize(TileEntityEngineeringTable par1iInventory, int par2, int par3,
                                int par4) {
        super(par1iInventory, par2, par3, par4);
        this.furnace = par1iInventory;
        this.myNum = par2;
    }

    @Override
    public boolean isItemValid(net.minecraft.item.ItemStack par1ItemStack) {
        if (furnace.getStackInSlot(0) != null) {
            if (furnace.getStackInSlot(0).getItem() instanceof IEngineerable) {
                IEngineerable item = (IEngineerable) furnace.getStackInSlot(0).getItem();
                if (item.canPutInSlot(furnace.getStackInSlot(0), myNum - 1, par1ItemStack)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getSlotStackLimit() {
        return limit;
    }

    public void setSlotStackLimit(int i) {
        limit = i;
    }
}
