package eiteam.esteemedinnovation.misc;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemCraftingComponent extends Item {
    public ItemCraftingComponent() {
        setHasSubtypes(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> subItems) {
        for (Types component : Types.values()) {
            subItems.add(new ItemStack(MiscellaneousModule.COMPONENT, 1, component.getMetadata()));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + stack.getItemDamage();
    }

    public enum Types {
        BRASS_PISTON(0),
        BRASS_TURBINE(1),
        GUN_STOCK(2),
        FLINTLOCK(3),
        IRON_BARREL(4),
        BLUNDERBUSS_BARREL(5);

        private int metadata;

        Types(int metadata) {
            this.metadata = metadata;
        }

        public int getMetadata() {
            return metadata;
        }
    }
}
