package eiteam.esteemedinnovation.storage.item.canister;

import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static eiteam.esteemedinnovation.storage.StorageModule.ITEM_CANISTER;

public class CanisterRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    static {
        RecipeSorter.register(EsteemedInnovation.MOD_ID + ":canisterHandler", CanisterRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
    }

    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World world) {
        ItemStack toCan = getItemToCan(inv);

        return toCan != null;
    }

    @Nonnull
    @Override
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
        ItemStack toCan = getItemToCan(inv);
        if (toCan != null) {
            if (!toCan.hasTagCompound()) {
                toCan.setTagCompound(new NBTTagCompound());
            }
            toCan.getTagCompound().setInteger("Canned", 0);
            toCan.setCount(1);
            return toCan;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height == 2;
    }

    /**
     * @param inv The crafting inventory
     * @return A copy of the input ItemStack that is going to be canned. Will be null if the input is wrong (i.e.,
     *         multiple cans, multiple non-cans, input is already canned).
     */
    @Nullable
    private ItemStack getItemToCan(InventoryCrafting inv) {
        ItemStack output = null;
        int cans = 0;
        int noncans = 0;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack itemStack = inv.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                if (itemStack.getItem() == ITEM_CANISTER) {
                    cans++;
                } else {
                    noncans++;
                    if (output == null) {
                        output = itemStack.copy();
                    }
                }
            }
        }

        if (output != null && output.hasTagCompound() && output.getTagCompound().hasKey("Canned")) {
            return null;
        }

        return cans == 1 && noncans == 1 ? output : null;
    }

    @Nonnull
    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public NonNullList<ItemStack> getRemainingItems(@Nonnull InventoryCrafting inv) {
        return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
    }
}
