package eiteam.esteemedinnovation.armor.exosuit.steam.upgrades;

import eiteam.esteemedinnovation.api.ChargableUtility;
import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.commons.util.InventoryUtility;
import eiteam.esteemedinnovation.commons.util.OreDictHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

import static eiteam.esteemedinnovation.armor.ArmorModule.resource;
import static eiteam.esteemedinnovation.storage.StorageModule.ITEM_CANISTER;

public class ItemCanningMachineUpgrade extends ItemSteamExosuitUpgrade {
    public ItemCanningMachineUpgrade() {
        super(ExosuitSlot.LEGS_HIPS, resource("canner"), null, 1);
    }

    @Override
    public void onPlayerPickupItem(EntityItemPickupEvent event, ItemStack armorStack, EntityEquipmentSlot slot) {
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        if (player.worldObj.isRemote) {
            return;
        }

        if (ChargableUtility.hasPower(player, 10)) {
            ItemStack item = event.getItem().getEntityItem();
            if (item.hasTagCompound() && item.getTagCompound().hasKey("canned")) {
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
                if (stackInSlot != null && stackInSlot.getItem() == ITEM_CANISTER) {
                    numCans += stackInSlot.stackSize;
                }
            }
            if (numCans >= item.stackSize) {
                if (!item.hasTagCompound()) {
                    item.setTagCompound(new NBTTagCompound());
                }
                item.getTagCompound().setInteger("canned", 0);
                for (int i = 0; i < item.stackSize; i++) {
                    InventoryUtility.consumeInventoryItem(player, ITEM_CANISTER);
                    player.inventoryContainer.detectAndSendChanges();
                }
            } else if (numCans != 0) {
                item.stackSize -= numCans;
                event.getItem().setEntityItemStack(item);
                ItemStack item2 = item.copy();
                item2.stackSize = numCans;
                if (!item2.hasTagCompound()) {
                    item2.setTagCompound(new NBTTagCompound());
                }
                item2.getTagCompound().setInteger("canned", 0);
                for (int i = 0; i < numCans; i++) {
                    InventoryUtility.consumeInventoryItem(player, ITEM_CANISTER);
                    player.inventoryContainer.detectAndSendChanges();
                }
            }
        }
    }
}
