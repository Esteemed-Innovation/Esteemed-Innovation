package eiteam.esteemedinnovation.init.items.firearms;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.api.book.BookRecipeRegistry;
import eiteam.esteemedinnovation.init.items.CraftingComponentItems;
import eiteam.esteemedinnovation.init.InitCategory;
import eiteam.esteemedinnovation.firearms.flintlock.ItemFirearm;
import eiteam.esteemedinnovation.firearms.rocket.ItemRocketLauncher;

import static eiteam.esteemedinnovation.init.misc.OreDictEntries.*;

public class FirearmItems implements InitCategory {
    public enum Items {
        MUSKET(new ItemFirearm(Config.musketDamage, 84, 0.2F, 5.0F, false, 1, INGOT_IRON), "musket"),
        PISTOL(new ItemFirearm(Config.pistolDamage, 42, 0.5F, 2.0F, false, 1, INGOT_IRON), "pistol"),
        ROCKET_LAUNCHER(new ItemRocketLauncher(2.0F, 95, 10, 3.5F, 4, INGOT_IRON), "rocket_launcher"),
        BLUNDERBUSS(new ItemFirearm(Config.blunderbussDamage, 95, 3.5F, 7.5F, true, 1, INGOT_BRASS), "blunderbuss");

        private Item item;
        public static Items[] LOOKUP = new Items[values().length];

        static {
            for (Items item : values()) {
                if (item.isEnabled()) {
                    LOOKUP[item.ordinal()] = item;
                }
            }
        }

        Items(Item item, String name) {
            item.setUnlocalizedName(EsteemedInnovation.MOD_ID + ":" + name);
            item.setCreativeTab(EsteemedInnovation.tab);
            item.setRegistryName(EsteemedInnovation.MOD_ID, name);
            GameRegistry.register(item);
            this.item = item;
        }

        public Item getItem() {
            return item;
        }

        public boolean isEnabled() {
            return this == ROCKET_LAUNCHER ? Config.enableRL : Config.enableFirearms;
        }
    }

    @Override
    public void oreDict() {}

    @Override
    public void recipes() {
        for (Items item : Items.LOOKUP) {
            switch (item) {
                case MUSKET: {
                    BookRecipeRegistry.addRecipe("musket", new ShapedOreRecipe(item.getItem(),
                      "b  ",
                      " bf",
                      "  s",
                      'b', CraftingComponentItems.Items.IRON_BARREL.createItemStack(),
                      'f', CraftingComponentItems.Items.FLINTLOCK.createItemStack(),
                      's', CraftingComponentItems.Items.GUN_STOCK.createItemStack()
                    ));
                }
                case PISTOL: {
                    BookRecipeRegistry.addRecipe("pistol", new ShapedOreRecipe(item.getItem(),
                      "b  ",
                      " pf",
                      " p ",
                      'b', CraftingComponentItems.Items.IRON_BARREL.createItemStack(),
                      'p', PLANK_WOOD,
                      'f', CraftingComponentItems.Items.FLINTLOCK.createItemStack()
                    ));
                }
                case ROCKET_LAUNCHER: {
                    addRocketLauncherRecipe("rocket1", PLATE_THIN_BRASS, PLATE_THIN_COPPER);
                    addRocketLauncherRecipe("rocket2", INGOT_BRASS, PLATE_THIN_COPPER);
                    addRocketLauncherRecipe("rocket3", PLATE_THIN_BRASS, INGOT_COPPER);
                    addRocketLauncherRecipe("rocket4", INGOT_BRASS, INGOT_COPPER);
                }
                case BLUNDERBUSS: {
                    BookRecipeRegistry.addRecipe("blunderbuss", new ShapedOreRecipe(item.getItem(),
                      "b  ",
                      " bf",
                      "  s",
                      'b', CraftingComponentItems.Items.BLUNDERBUSS_BARREL.createItemStack(),
                      'f', CraftingComponentItems.Items.FLINTLOCK.createItemStack(),
                      's', CraftingComponentItems.Items.GUN_STOCK.createItemStack()
                    ));
                }
            }
        }
    }

    private static void addRocketLauncherRecipe(String name, String brassOre, String copperOre) {
        BookRecipeRegistry.addRecipe(name, new ShapedOreRecipe(Items.ROCKET_LAUNCHER.getItem(),
          "bx ",
          "fic",
          " pi",
          'i', CraftingComponentItems.Items.IRON_BARREL.createItemStack(),
          'x', brassOre,
          'c', copperOre,
          'p', PLANK_WOOD,
          'b', CraftingComponentItems.Items.BLUNDERBUSS_BARREL.createItemStack(),
          'f', CraftingComponentItems.Items.FLINTLOCK.createItemStack()
        ));
    }
}
