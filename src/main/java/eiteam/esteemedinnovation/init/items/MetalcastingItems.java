package eiteam.esteemedinnovation.init.items;

import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.api.book.BookRecipeRegistry;
import eiteam.esteemedinnovation.api.mold.MoldRegistry;
import eiteam.esteemedinnovation.init.IInitCategory;
import eiteam.esteemedinnovation.metalcasting.mold.molds.ItemIngotMold;
import eiteam.esteemedinnovation.metalcasting.mold.molds.ItemNuggetMold;
import eiteam.esteemedinnovation.metalcasting.mold.molds.ItemPipeMold;
import eiteam.esteemedinnovation.metalcasting.mold.molds.ItemPlateMold;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static net.minecraft.init.Items.BRICK;

public class MetalcastingItems implements IInitCategory {
    public enum Items {
        INGOT_MOLD(new ItemIngotMold(), "ingot_mold", false),
        NUGGET_MOLD(new ItemNuggetMold(), "nugget_mold", false),
        PLATE_MOLD(new ItemPlateMold(), "plate_mold", false),
        PIPE_MOLD(new ItemPipeMold(), "pipe_mold", false),
        BLANK_MOLD(new Item(), "blank_mold", true);

        private Item item;

        public static Items[] LOOKUP = new Items[values().length];

        static {
            for (Items item : values()) {
                if (item.isEnabled()) {
                    LOOKUP[item.ordinal()] = item;
                }
            }
        }

        Items(Item item, String name, boolean stackOfOne) {
            item.setUnlocalizedName(EsteemedInnovation.MOD_ID + ":" + name);
            item.setCreativeTab(EsteemedInnovation.tab);
            if (stackOfOne) {
                item.setMaxStackSize(1);
            }
            item.setRegistryName(EsteemedInnovation.MOD_ID, name);
            GameRegistry.register(item);
            MoldRegistry.addCarvableMold(item);
            this.item = item;
        }

        public boolean isEnabled() {
            return Config.enableMold;
        }

        public Item getItem() {
            return item;
        }
    }
    @Override
    public void oreDict() {}

    @Override
    public void recipes() {
        if (Items.BLANK_MOLD.isEnabled()) {
            BookRecipeRegistry.addRecipe("blankMold", new ShapedOreRecipe(Items.BLANK_MOLD.getItem(), "xx", 'x', BRICK));
        }
    }
}
