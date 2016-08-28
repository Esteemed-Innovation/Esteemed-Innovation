package eiteam.esteemedinnovation.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import eiteam.esteemedinnovation.init.items.CraftingComponentItems;

import java.util.List;

public class ItemCraftingComponent extends Item {
    public ItemCraftingComponent() {
        setHasSubtypes(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> subItems) {
        for (CraftingComponentItems.Items component : CraftingComponentItems.Items.values()) {
            subItems.add(component.createItemStack());
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + stack.getItemDamage();
    }
}
