package eiteam.esteemedinnovation.storage.item.canister;

import eiteam.esteemedinnovation.api.Tuple3;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.oredict.RecipeSorter;

import static eiteam.esteemedinnovation.storage.StorageModule.ITEM_CANISTER;

public class CanisterHandler implements IRecipe {
    static {
        RecipeSorter.register(EsteemedInnovation.MOD_ID + ":canisterHandler", CanisterHandler.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
    }

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        Tuple3<Boolean, Boolean, ItemStack> triplet = getCanCraftAndHasCanAndOutput(inv);
        ItemStack output = triplet.getThird();
        boolean hasCan = triplet.getSecond();
        boolean canCraft = triplet.getFirst();

        return canCraft && hasCan && output != null && !(output.hasTagCompound() &&
          output.getTagCompound().hasKey("canned"));
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        Tuple3<Boolean, Boolean, ItemStack> triplet = getCanCraftAndHasCanAndOutput(inv);
        ItemStack output = triplet.getThird();
        boolean hasCan = triplet.getSecond();
        boolean canCraft = triplet.getFirst();
        if (canCraft && hasCan && output != null) {
            if (output.hasTagCompound() && output.getTagCompound().hasKey("canned")) {
                return null;
            }
            if (!output.hasTagCompound()) {
                output.setTagCompound(new NBTTagCompound());
            }
            output.getTagCompound().setInteger("canned", 0);
            output.stackSize = 1;
            return output;
        }
        return null;
    }

    /**
     * Gets a tuple containing canCraft, hasCan, and output values. Implemented for the sake of less duplicate code.
     * Chained/combined returning in order to prevent A) more duplicate code, and B) more iterations.
     * It's sort of a strange approach, but it makes the most sense for the best performance.
     * @param inv The crafting inventory
     * @return A tuple3 of 2 booleans (canCraft, hasCan) and the ItemStack output
     */
    private Tuple3<Boolean, Boolean, ItemStack> getCanCraftAndHasCanAndOutput(InventoryCrafting inv) {
        ItemStack output = null;
        boolean hasCan = false;
        boolean canCraft = true;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            if (inv.getStackInSlot(i) != null) {
                ItemStack stack = inv.getStackInSlot(i);
                if (stack.getItem() == ITEM_CANISTER) {
                    if (!hasCan) {
                        hasCan = true;
                    } else {
                        canCraft = false;
                    }
                } else {
                    if (output == null) {
                        output = stack.copy();
                    } else {
                        canCraft = false;
                    }
                }
            }
        }
        return new Tuple3<>(canCraft, hasCan, output);
    }

    @Override
    public int getRecipeSize() {
        return 0;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inv) {
        return new ItemStack[0];
    }
}
