package eiteam.esteemedinnovation.charging;

import baubles.api.BaubleType;
import eiteam.esteemedinnovation.misc.ItemBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ItemSteamCellFiller extends ItemBauble {
    public ItemSteamCellFiller() {
        super(BaubleType.AMULET);
        setMaxStackSize(1);
    }

    @Override
    public void onWornTick(ItemStack itemStack, EntityLivingBase wearer) {
        if (!(wearer instanceof EntityPlayer)) {
            return;
        }

        EntityPlayer player = (EntityPlayer) wearer;

        if (player.ticksExisted % 50 == 0) {
            for (int i = 0; i < InventoryPlayer.getHotbarSize(); i++) {
                ItemStack item = player.inventory.getStackInSlot(i);
                if (item != null && item.getItem() instanceof ItemSteamCell &&
                  ItemSteamCell.chargeItems(player, false)) {
                    player.inventory.decrStackSize(i, 1);
                    break;
                }
            }
        }
    }
}
