package eiteam.esteemedinnovation.materials.refined;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemMetalNugget extends Item {
    public ItemMetalNugget() {
        setHasSubtypes(true);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            for (Types type : Types.values()) {
                items.add(new ItemStack(this, 1, type.getMeta()));
            }
        }
    }

    @Override
    public String getTranslationKey(ItemStack par1ItemStack) {
        return super.getTranslationKey() + "." + par1ItemStack.getItemDamage();
    }

    public enum Types {
        COPPER_NUGGET(0),
        ZINC_NUGGET(1),
        BRASS_NUGGET(2),
        GILDED_IRON_NUGGET(3);

        private final int meta;

        Types(int meta) {
            this.meta = meta;
        }

        public int getMeta() {
            return meta;
        }
    }
}
