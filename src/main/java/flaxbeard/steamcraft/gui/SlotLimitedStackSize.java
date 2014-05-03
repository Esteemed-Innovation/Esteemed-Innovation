package flaxbeard.steamcraft.gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class SlotLimitedStackSize extends Slot {

	private int limit = 64;
	
	public SlotLimitedStackSize(IInventory par1iInventory, int par2, int par3,
			int par4) {
		super(par1iInventory, par2, par3, par4);
	}
	
	@Override
    public int getSlotStackLimit()
    {
		return limit;
    }
	
	public void setSlotStackLimit(int i)
	{
		limit = i;
	}
}
