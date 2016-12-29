package eiteam.esteemedinnovation.api;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

public interface Engineerable {
    Pair<Integer, Integer>[] engineerCoordinates();

    ItemStack getStackInSlot(ItemStack me, int var1);

    void setInventorySlotContents(ItemStack me, int var1, ItemStack stack);

    boolean isItemValidForSlot(ItemStack me, int var1, ItemStack var2);

    ItemStack decrStackSize(ItemStack me, int var1, int var2);

    void drawSlot(GuiContainer guiEngineeringTable, int slotnum, int i, int j);

    boolean canPutInSlot(ItemStack me, int slotNum, ItemStack upgrade);

    void drawBackground(GuiContainer guiEngineeringTable, int i, int j, int k);
}
