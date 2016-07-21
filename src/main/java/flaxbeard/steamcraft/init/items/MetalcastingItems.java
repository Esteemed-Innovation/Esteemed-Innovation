package flaxbeard.steamcraft.init.items;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.book.BookRecipeRegistry;
import flaxbeard.steamcraft.init.IInitCategory;
import flaxbeard.steamcraft.item.ItemIngotMold;
import flaxbeard.steamcraft.item.ItemNuggetMold;
import flaxbeard.steamcraft.item.ItemPlateMold;

import static net.minecraft.init.Items.*;

public class MetalcastingItems implements IInitCategory {
    public enum Items {
        INGOT_MOLD(new ItemIngotMold(), "ingot_mold", false),
        NUGGET_MOLD(new ItemNuggetMold(), "nugget_mold", false),
        PLATE_MOLD(new ItemPlateMold(), "plate_mold", false),
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
            item.setUnlocalizedName(Steamcraft.MOD_ID + ":" + name);
            item.setCreativeTab(Steamcraft.tab);
            if (stackOfOne) {
                item.setMaxStackSize(1);
            }
            item.setRegistryName(Steamcraft.MOD_ID, name);
            GameRegistry.register(item);
            SteamcraftRegistry.addCarvableMold(item);
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
