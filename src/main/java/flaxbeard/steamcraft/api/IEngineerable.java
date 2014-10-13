package flaxbeard.steamcraft.api;

import flaxbeard.steamcraft.gui.GuiEngineeringTable;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.MutablePair;

public interface IEngineerable {
    public MutablePair<Integer, Integer>[] engineerCoordinates();

    public ItemStack getStackInSlot(ItemStack me, int var1);

    public void setInventorySlotContents(ItemStack me, int var1, ItemStack stack);

    public boolean isItemValidForSlot(ItemStack me, int var1, ItemStack var2);

    public ItemStack decrStackSize(ItemStack me, int var1, int var2);

    public void drawSlot(GuiContainer guiEngineeringTable, int slotnum, int i, int j);

    boolean canPutInSlot(ItemStack me, int slotNum, ItemStack upgrade);

    public void drawBackground(GuiEngineeringTable guiEngineeringTable, int i,
                               int j, int k);
}
