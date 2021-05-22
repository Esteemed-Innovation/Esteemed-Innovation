package eiteam.esteemedinnovation.armor.exosuit.steam.upgrades;

import eiteam.esteemedinnovation.api.ChargableUtility;
import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.commons.util.OreDictHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

import javax.annotation.Nonnull;

import static eiteam.esteemedinnovation.armor.ArmorModule.resource;
import static eiteam.esteemedinnovation.storage.StorageModule.ITEM_CANISTER;

public class ItemCanningMachineUpgrade extends ItemSteamExosuitUpgrade {
    public ItemCanningMachineUpgrade() {
        super(ExosuitSlot.LEGS_HIPS, resource("canner"), null, 1);
    }

    @Override
    public void onPlayerPickupItem(EntityItemPickupEvent event, @Nonnull ItemStack armorStack, EntityEquipmentSlot slot) {
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        if (player.world.isRemote) {
            return;
        }

        if (ChargableUtility.hasPower(player, 10)) {
            ItemStack item = event.getItem().getItem();
            if (item.hasTagCompound() && item.getTagCompound().hasKey("Canned")) {
                return;
            }

            boolean isCannable = OreDictHelper.mapHasItem(OreDictHelper.ingots, item.getItem()) ||
              OreDictHelper.mapHasItem(OreDictHelper.gems, item.getItem()) ||
              OreDictHelper.listHasItem(OreDictHelper.nuggets, item.getItem());
            if (!isCannable) {
                return;
            }

            int numCans = 0;
            for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                ItemStack stackInSlot = player.inventory.getStackInSlot(i);
                if (stackInSlot.getItem() == ITEM_CANISTER) {
                    numCans += stackInSlot.getCount();
                }
            }
            if (numCans >= item.getCount()) {
                if (!item.hasTagCompound()) {
                    item.setTagCompound(new NBTTagCompound());
                }
                item.getTagCompound().setInteger("Canned", 0);
                for (int i = 0; i < item.getCount(); i++) {
                    eiteam.esteemedinnovation.api.util.InventoryUtility.consumeInventoryItem(player, ITEM_CANISTER);
                    player.inventoryContainer.detectAndSendChanges();
                }
            } else if (numCans != 0) {
                item.shrink(numCans);
                event.getItem().setItem(item);
                ItemStack item2 = item.copy();
                item2.setCount(numCans);
                if (!item2.hasTagCompound()) {
                    item2.setTagCompound(new NBTTagCompound());
                }
                item2.getTagCompound().setInteger("Canned", 0);
                for (int i = 0; i < numCans; i++) {
                    eiteam.esteemedinnovation.api.util.InventoryUtility.consumeInventoryItem(player, ITEM_CANISTER);
                    player.inventoryContainer.detectAndSendChanges();
                }
            }
        }
    }
}
