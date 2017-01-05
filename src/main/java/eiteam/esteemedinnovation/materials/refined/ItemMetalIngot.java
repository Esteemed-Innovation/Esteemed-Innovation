package eiteam.esteemedinnovation.materials.refined;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemMetalIngot extends Item {
    public ItemMetalIngot() {
        setHasSubtypes(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> subItems) {
        for (Types type : Types.values()) {
            subItems.add(new ItemStack(this, 1, type.getMeta()));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + stack.getItemDamage();
    }

    public enum Types {
        COPPER_INGOT(0),
        ZINC_INGOT(1),
        BRASS_INGOT(2),
        GILDED_IRON_INGOT(3);

        private final int meta;

        Types(int meta) {
            this.meta = meta;
        }

        public int getMeta() {
            return meta;
        }
    }
}
