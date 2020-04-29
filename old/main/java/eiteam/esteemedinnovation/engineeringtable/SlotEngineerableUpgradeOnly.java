package eiteam.esteemedinnovation.engineeringtable;

import eiteam.esteemedinnovation.api.Engineerable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotEngineerableUpgradeOnly extends SlotItemHandler {
    SlotEngineerableUpgradeOnly(IItemHandler engineerableContainer, int index, int xPosition, int yPosition) {
        super(engineerableContainer, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack check) {
        ItemStack engineerableStack = getItemHandler().getStackInSlot(0);
        Item engineerableItem = engineerableStack.getItem();
        if (engineerableItem instanceof Engineerable) {
            Engineerable engineerable = (Engineerable) engineerableItem;
            return engineerable.canPutInSlot(engineerableStack, getSlotIndex() - 1, check);
        }
        return false;
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }
}
