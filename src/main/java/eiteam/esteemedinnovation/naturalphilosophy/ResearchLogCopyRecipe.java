package eiteam.esteemedinnovation.naturalphilosophy;

import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.RecipeSorter;

import javax.annotation.Nonnull;

public class ResearchLogCopyRecipe implements IRecipe {
    static {
        RecipeSorter.register(EsteemedInnovation.MOD_ID + ":research_log_copy", ResearchLogCopyRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
    }

    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World worldIn) {
        boolean foundBlankLog = false;
        boolean foundLogToCopy = false;
        // Return true if and only if there are exactly 2 items: 1 blank log and 1 log to copy
        for (int slot = 0; slot < inv.getSizeInventory(); slot++) {
            Item itemInSlot = inv.getStackInSlot(slot).getItem();
            if (itemInSlot == NaturalPhilosophyModule.BLANK_RESEARCH_LOG) {
                if (foundBlankLog) {
                    return false;
                } else {
                    foundBlankLog = true;
                }
            } else if (itemInSlot instanceof ItemResearchLog) {
                if (foundLogToCopy) {
                    return false;
                } else {
                    foundLogToCopy = true;
                }
            } else if (itemInSlot != Items.AIR) {
                return false;
            }
        }
        return foundBlankLog && foundLogToCopy;
    }

    @Nonnull
    @Override
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
        for (int slot = 0; slot < inv.getSizeInventory(); slot++) {
            ItemStack stackInSlot = inv.getStackInSlot(slot);
            if (stackInSlot.getItem() instanceof ItemResearchLog) {
                return stackInSlot.copy();
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int getRecipeSize() {
        return 2;
    }

    @Nonnull
    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public NonNullList<ItemStack> getRemainingItems(@Nonnull InventoryCrafting inv) {
        NonNullList<ItemStack> remaining = NonNullList.create();
        for (int slot = 0; slot < inv.getSizeInventory(); slot++) {
            ItemStack stackInSlot = inv.getStackInSlot(slot);
            if (stackInSlot.getItem() instanceof ItemResearchLog) {
                remaining.add(stackInSlot.copy());
            } else {
                remaining.add(ItemStack.EMPTY);
            }
        }
        return remaining;
    }
}
