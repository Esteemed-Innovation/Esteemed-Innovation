package flaxbeard.steamcraft.api;

import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.tuple.MutablePair;

import flaxbeard.steamcraft.gui.GuiEngineeringTable;

public interface IEngineerable {
	public MutablePair<Integer,Integer>[] engineerCoordinates();

	public ItemStack getStackInSlot(ItemStack me, int var1);

	public void setInventorySlotContents(ItemStack me, int var1, ItemStack stack);

	public boolean isItemValidForSlot(ItemStack me, int var1, ItemStack var2);

	public ItemStack decrStackSize(ItemStack me, int var1, int var2);

	public void drawSlot(GuiEngineeringTable guiEngineeringTable, int slotnum, int i, int j);

	boolean canPutInSlot(ItemStack me, int slotNum, ItemStack upgrade);
}
