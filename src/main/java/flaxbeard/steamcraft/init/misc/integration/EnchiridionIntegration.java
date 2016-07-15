package flaxbeard.steamcraft.init.misc.integration;

import joshie.enchiridion.items.ItemEnchiridion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class EnchiridionIntegration {

    public static boolean hasBook(Item class1, EntityPlayer player) {
        boolean foundBook = false;
        for (int p = 0; p < player.inventory.getSizeInventory(); p++) {
            ItemStack binder = player.inventory.getStackInSlot(p);
            if ((binder != null) && (binder.getItem() instanceof ItemEnchiridion) &&
              (binder.getItemDamage() == 1)) {
                NBTTagCompound loader = binder.hasTagCompound() ? binder.getTagCompound() :
                  new NBTTagCompound();
                NBTTagList nbttaglist = loader.getTagList("Inventory", 10);
                if (nbttaglist != null) {
                    for (int i = 0; i < nbttaglist.tagCount(); i++) {
                        NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
                        if (ItemStack.loadItemStackFromNBT(nbttagcompound1) != null &&
                          ItemStack.loadItemStackFromNBT(nbttagcompound1).getItem() == class1) {
                            foundBook = true;
                            break;
                        }
                    }
                }
            }
        }
        return foundBook;
    }

    public static ItemStack findBook(Item class1, EntityPlayer player) {
        for (int p = 0; p < player.inventory.getSizeInventory(); p++) {
            ItemStack binder = player.inventory.getStackInSlot(p);
            if ((binder != null) && (binder.getItem() instanceof ItemEnchiridion) &&
              (binder.getItemDamage() == 1)) {
                NBTTagCompound loader = binder.hasTagCompound() ? binder.getTagCompound() :
                  new NBTTagCompound();
                NBTTagList nbttaglist = loader.getTagList("Inventory", 10);
                if (nbttaglist != null) {
                    for (int i = 0; i < nbttaglist.tagCount(); i++) {
                        NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
                        if (ItemStack.loadItemStackFromNBT(nbttagcompound1) != null &&
                          ItemStack.loadItemStackFromNBT(nbttagcompound1).getItem() == class1) {
                            return ItemStack.loadItemStackFromNBT(nbttagcompound1);
                        }
                    }
                }
            }
        }
        return null;
    }
}
