package eiteam.esteemedinnovation.armor.exosuit.plates;

import eiteam.esteemedinnovation.armor.ArmorModule;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemExosuitPlate extends Item {
    public ItemExosuitPlate() {
        setHasSubtypes(true);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EsteemedInnovation.upgrade;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> subItems) {
        for (int i = 0; i < ArmorModule.MAX_PLATE_META; i++) {
            subItems.add(ArmorModule.plateStack(i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName() + "." + stack.getItemDamage();
    }
}
