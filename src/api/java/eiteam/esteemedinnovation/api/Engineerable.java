package eiteam.esteemedinnovation.api;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;

public interface Engineerable {
    Pair[] engineerCoordinates();

    @Nonnull
    ItemStack getStackInSlot(@Nonnull ItemStack me, int var1);

    void setInventorySlotContents(@Nonnull ItemStack me, int var1, @Nonnull ItemStack stack);

    boolean isItemValidForSlot(@Nonnull ItemStack me, int var1, @Nonnull ItemStack var2);

    @Nonnull
    ItemStack decrStackSize(@Nonnull ItemStack me, int var1, int var2);

    void drawSlot(GuiContainer guiEngineeringTable, int slotnum, int i, int j);

    boolean canPutInSlot(@Nonnull ItemStack me, int slotNum, @Nonnull ItemStack upgrade);

    void drawBackground(GuiContainer guiEngineeringTable, int i, int j, int k);
}
