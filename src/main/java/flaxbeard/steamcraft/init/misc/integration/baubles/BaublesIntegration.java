package flaxbeard.steamcraft.init.misc.integration.baubles;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.init.items.tools.GadgetItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BaublesIntegration {
    public static boolean checkForUpgrade(EntityPlayer player, Item item) {
        IInventory inventory = BaublesApi.getBaubles(player);
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stackInSlot = inventory.getStackInSlot(i);
            if (stackInSlot != null && stackInSlot.getItem() == item) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkForSurvivalist(EntityPlayer player) {
        return checkForUpgrade(player, GadgetItems.Items.SURVIVALIST_TOOLKIT.getItem());
    }

    public static boolean checkForSteamCellFiller(EntityPlayer player) {
        return checkForUpgrade(player, GadgetItems.Items.STEAM_CELL_FILLER.getItem());
    }

    public static Item getSurvivalist() {
        return new ItemBauble(BaubleType.BELT).setCreativeTab(Steamcraft.tab)
          .setUnlocalizedName("steamcraft:survivalist").setMaxStackSize(1);
    }

    public static Item getSteamCellFiller() {
        return new ItemBauble(BaubleType.AMULET).setCreativeTab(Steamcraft.tab)
          .setUnlocalizedName("steamcraft:steamcellFiller").setMaxStackSize(1);
    }
}
