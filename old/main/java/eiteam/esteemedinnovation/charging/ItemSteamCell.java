package eiteam.esteemedinnovation.charging;

import eiteam.esteemedinnovation.api.SteamChargable;
import eiteam.esteemedinnovation.armor.exosuit.steam.ItemSteamExosuitArmor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

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
        ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        int steamToAdd = ChargingModule.steamCellCapacity;
        if (!skipExo && !chest.isEmpty() && chest.getItem() instanceof ItemSteamExosuitArmor) {
            ItemSteamExosuitArmor armor = (ItemSteamExosuitArmor) chest.getItem();
            boolean bool = armor.addSteam(chest, steamToAdd, player);
            return bool || chargeItems(player, true);
        } else {
            for (int i = 0; i < InventoryPlayer.getHotbarSize(); i++) {
                ItemStack item = player.inventory.getStackInSlot(i);
                if (!item.isEmpty() && item.getItem() instanceof SteamChargable) {
                    SteamChargable cha = (SteamChargable) item.getItem();
                    if (cha.canCharge(item) && cha.addSteam(item, steamToAdd, player)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack me = player.getHeldItem(hand);
        if (chargeItems(player, false)) {
            me.shrink(1);
        }
        return ActionResult.newResult(EnumActionResult.PASS, me);
    }
}
