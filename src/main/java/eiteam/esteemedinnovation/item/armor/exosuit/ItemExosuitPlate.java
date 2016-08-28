package eiteam.esteemedinnovation.item.armor.exosuit;

import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.init.items.armor.ExosuitUpgradeItems;
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
        for (ExosuitUpgradeItems.PlateItems plate : ExosuitUpgradeItems.PlateItems.values()) {
            subItems.add(plate.createItemStack());
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + stack.getItemDamage();
    }
}
