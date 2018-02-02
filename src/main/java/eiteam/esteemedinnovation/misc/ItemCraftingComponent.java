package eiteam.esteemedinnovation.misc;

import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ItemCraftingComponent extends Item {
    public ItemCraftingComponent() {
        setHasSubtypes(true);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (tab == EsteemedInnovation.tab) {
            for (Types component : Types.values()) {
                items.add(new ItemStack(MiscellaneousModule.COMPONENT, 1, component.getMetadata()));
            }
        }
    }

    @Nonnull
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName() + "." + stack.getItemDamage();
    }

    public enum Types {
        BRASS_PISTON(0),
        BRASS_TURBINE(1),
        GUN_STOCK(2),
        FLINTLOCK(3),
        IRON_BARREL(4),
        BLUNDERBUSS_BARREL(5),
        NETHERBRICK_DUST(6),
        HELLFORGE_BRICK_RAW(7),
        HELLFORGE_BRICK(8);

        private final int metadata;

        Types(int metadata) {
            this.metadata = metadata;
        }

        public int getMetadata() {
            return metadata;
        }
    }
}
