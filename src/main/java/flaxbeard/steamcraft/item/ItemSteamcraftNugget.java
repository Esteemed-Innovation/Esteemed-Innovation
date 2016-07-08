package flaxbeard.steamcraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import flaxbeard.steamcraft.init.items.MetalItems;

import java.util.List;

public class ItemSteamcraftNugget extends Item {
    public ItemSteamcraftNugget() {
        setHasSubtypes(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> subItems) {
        for (MetalItems.Items metal : MetalItems.Items.NUGGETS) {
            subItems.add(metal.createItemStack());
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
    }
}
