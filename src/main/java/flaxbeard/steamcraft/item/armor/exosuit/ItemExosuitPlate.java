package flaxbeard.steamcraft.item.armor.exosuit;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.init.items.armor.ExosuitUpgradeItems;
import flaxbeard.steamcraft.integration.CrossMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class ItemExosuitPlate extends Item {
    public ItemExosuitPlate() {
        setHasSubtypes(true);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return Steamcraft.upgrade;
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
