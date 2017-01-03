package eiteam.esteemedinnovation.metals.refined;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemMetalNugget extends Item {
    public ItemMetalNugget() {
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
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
    }

    public enum Types {
        COPPER_NUGGET(0),
        ZINC_NUGGET(1),
        BRASS_NUGGET(2),
        GILDED_IRON_NUGGET(3),
        IRON_NUGGET(4); // I hate you, Vanilla, and your lack of iron nuggets.

        private final int meta;

        Types(int meta) {
            this.meta = meta;
        }

        public int getMeta() {
            return meta;
        }
    }
}
