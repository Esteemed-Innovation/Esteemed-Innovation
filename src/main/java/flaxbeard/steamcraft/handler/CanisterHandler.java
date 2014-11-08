package flaxbeard.steamcraft.handler;

import flaxbeard.steamcraft.SteamcraftItems;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class CanisterHandler implements IRecipe {

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        ItemStack output = null;
        boolean hasCan = false;
        boolean canCraft = true;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            if (inv.getStackInSlot(i) != null) {
                ItemStack stack = inv.getStackInSlot(i);
                if (stack.getItem() == SteamcraftItems.canister) {

                    if (hasCan == false) {

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

        if (canCraft && hasCan && output != null) {

            if (output.hasTagCompound() && output.stackTagCompound.hasKey("canned")) {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack output = null;
        boolean hasCan = false;
        boolean canCraft = true;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            if (inv.getStackInSlot(i) != null) {
                ItemStack stack = inv.getStackInSlot(i);
                if (stack.getItem() == SteamcraftItems.canister) {

                    if (hasCan == false) {

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
        if (canCraft && hasCan && output != null) {
            if (output.hasTagCompound() && output.stackTagCompound.hasKey("canned")) {
                return null;
            }
            if (!output.hasTagCompound()) {
                output.setTagCompound(new NBTTagCompound());
            }
            output.stackTagCompound.setInteger("canned", 0);
            output.stackSize = 1;
            return output;
        }
        return null;
    }

    @Override
    public int getRecipeSize() {
        return 0;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

}
