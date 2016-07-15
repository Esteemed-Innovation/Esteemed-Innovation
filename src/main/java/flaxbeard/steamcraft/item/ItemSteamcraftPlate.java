package flaxbeard.steamcraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import flaxbeard.steamcraft.init.items.MetalItems;


import java.util.List;

public class ItemSteamcraftPlate extends Item {
    public ItemSteamcraftPlate() {
        setHasSubtypes(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> subItems) {
        for (MetalItems.Items metal : MetalItems.Items.PLATES) {
            subItems.add(metal.createItemStack());
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + stack.getItemDamage();
    }
}
