package eiteam.esteemedinnovation.armor.exosuit.plates;

import eiteam.esteemedinnovation.armor.ArmorModule;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

public class ItemExosuitPlate extends Item {
    public ItemExosuitPlate() {
        setHasSubtypes(true);
    }

    @Nonnull
    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EsteemedInnovation.upgrade;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            for (int i = 0; i < ArmorModule.MAX_PLATE_META; i++) {
                items.add(ArmorModule.plateStack(i));
            }
        }
    }

    @Nonnull
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName() + "." + stack.getItemDamage();
    }
}
