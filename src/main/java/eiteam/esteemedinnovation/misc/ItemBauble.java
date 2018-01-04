package eiteam.esteemedinnovation.misc;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemBauble extends Item implements IBauble {
    private BaubleType baubleType;

    public ItemBauble(BaubleType t) {
        baubleType = t;
    }

    @Override
    public BaubleType getBaubleType(ItemStack stack) {
        return baubleType;
    }

    @Override
    public void onEquipped(ItemStack stack, EntityLivingBase player) {}

    @Override
    public void onUnequipped(ItemStack stack, EntityLivingBase player) {}

    @Override
    public void onWornTick(ItemStack stack, EntityLivingBase player) {}

    @Override
    public boolean canEquip(ItemStack stack, EntityLivingBase player) {
        return true;
    }

    @Override
    public boolean canUnequip(ItemStack stack, EntityLivingBase player) {
        return true;
    }
}
