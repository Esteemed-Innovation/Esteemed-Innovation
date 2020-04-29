package eiteam.esteemedinnovation.misc.integration;

import joshie.enchiridion.items.ItemEnchiridion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import javax.annotation.Nonnull;

public class EnchiridionIntegration {
    public static boolean hasBook(Item class1, EntityPlayer player) {
        boolean foundBook = false;
        for (int p = 0; p < player.inventory.getSizeInventory(); p++) {
            ItemStack binder = player.inventory.getStackInSlot(p);
            if (binder.getItem() instanceof ItemEnchiridion && binder.getItemDamage() == 1) {
                NBTTagCompound loader = binder.hasTagCompound() ? binder.getTagCompound() :
                  new NBTTagCompound();
                NBTTagList nbttaglist = loader.getTagList("Inventory", 10);
                for (int i = 0; i < nbttaglist.tagCount(); i++) {
                    NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
                    if (new ItemStack(nbttagcompound1).getItem() == class1) {
                        foundBook = true;
                        break;
                    }
                }
            }
        }
        return foundBook;
    }

    @Nonnull
    public static ItemStack findBook(Item class1, EntityPlayer player) {
        for (int p = 0; p < player.inventory.getSizeInventory(); p++) {
            ItemStack binder = player.inventory.getStackInSlot(p);
            if (binder.getItem() instanceof ItemEnchiridion && binder.getItemDamage() == 1) {
                NBTTagCompound loader = binder.hasTagCompound() ? binder.getTagCompound() :
                  new NBTTagCompound();
                NBTTagList nbttaglist = loader.getTagList("Inventory", 10);
                for (int i = 0; i < nbttaglist.tagCount(); i++) {
                    NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
                    if (new ItemStack(nbttagcompound1).getItem() == class1) {
                        return new ItemStack(nbttagcompound1);
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }
}
