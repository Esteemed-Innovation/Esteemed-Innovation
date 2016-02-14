package flaxbeard.steamcraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.api.ISteamChargable;

public class ItemSteamCell extends Item {
    public ItemSteamCell() {
        super();
    }

    /**
     * Charges the items in the player's inventory, starting at the exosuit, then going down the hotbar.
     * @param player The player
     * @param skipExo Whether to skip the exosuit.
     * @return If it successfully charged an item.
     */
    public static boolean chargeItems(EntityPlayer player, boolean skipExo) {
        ItemStack chest = player.getEquipmentInSlot(3);
        int steamToAdd = Config.steamCellCapacity;
        if (!skipExo && chest != null && chest.getItem() instanceof ItemExosuitArmor) {
            ItemExosuitArmor armor = (ItemExosuitArmor) chest.getItem();
            boolean bool = armor.addSteam(chest, steamToAdd, player);
            return bool || chargeItems(player, true);
        } else {
            for (int i = 0; i < InventoryPlayer.getHotbarSize(); i++) {
                ItemStack item = player.inventory.getStackInSlot(i);
                if (item != null && item.getItem() instanceof ISteamChargable) {
                    ISteamChargable cha = (ISteamChargable) item.getItem();
                    if (cha.canCharge(item) && cha.addSteam(item, steamToAdd, player)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack me, World world, EntityPlayer player) {
        if (chargeItems(player, false)) {
            me.stackSize--;
        }
        return me;
    }
}
