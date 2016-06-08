package flaxbeard.steamcraft.init.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.book.BookRecipeRegistry;
import flaxbeard.steamcraft.init.IInitCategory;
import flaxbeard.steamcraft.init.blocks.SteamNetworkBlocks;
import flaxbeard.steamcraft.item.ItemSteamcraftCrafting;

import static net.minecraft.init.Items.*;
import static net.minecraft.init.Blocks.*;
import static flaxbeard.steamcraft.init.misc.OreDictEntries.*;

public class CraftingComponentItems implements IInitCategory {
    public enum Items {
        BRASS_PISTON(0),
        BRASS_TURBINE(1),
        GUN_STOCK(2),
        FLINTLOCK(3),
        IRON_BARREL(4),
        BLUNDERBUSS_BARREL(5);

        private int metadata;
        private static Item item = new ItemSteamcraftCrafting();

        static {
            item.setUnlocalizedName(Steamcraft.MOD_ID + ":crafting");
            item.setRegistryName(Steamcraft.MOD_ID, "crafting");
            item.setCreativeTab(Steamcraft.tab);
            GameRegistry.register(item);
        }

        Items(int metadata) {
            this.metadata = metadata;
        }

        public ItemStack createItemStack(int size) {
            return new ItemStack(item, size, metadata);
        }

        public ItemStack createItemStack() {
            return createItemStack(1);
        }
    }

    @Override
    public void oreDict() {}

    @Override
    public void recipes() {
        for (Items item : Items.values()) {
            switch (item) {
                case BRASS_PISTON: {
                    BookRecipeRegistry.addRecipe("piston1", new ShapedOreRecipe(item.createItemStack(),
                      " x ",
                      "xpx",
                      " i ",
                      'x', INGOT_BRASS,
                      'p', PISTON,
                      'i', SteamNetworkBlocks.Blocks.PIPE.getBlock()
                    ));
                    BookRecipeRegistry.addRecipe("piston2", new ShapedOreRecipe(item.createItemStack(),
                      " x ",
                      "xpx",
                      " i ",
                      'x', PLATE_BRASS,
                      'p', PISTON,
                      'i', SteamNetworkBlocks.Blocks.PIPE.getBlock()
                    ));
                    break;
                }
                case BRASS_TURBINE: {
                    BookRecipeRegistry.addRecipe("turbine1", new ShapedOreRecipe(item.createItemStack(),
                      " x ",
                      "xnx",
                      " x ",
                      'x', INGOT_BRASS,
                      'n', NUGGET_BRASS
                    ));
                    BookRecipeRegistry.addRecipe("turbine2", new ShapedOreRecipe(item.createItemStack(),
                      " x ",
                      "xnx",
                      " x ",
                      'x', PLATE_BRASS,
                      'n', NUGGET_BRASS
                    ));
                    break;
                }
                case GUN_STOCK: {
                    BookRecipeRegistry.addRecipe("stock", new ShapedOreRecipe(item.createItemStack(),
                      "p  ",
                      " p ",
                      " pp",
                      'p', PLANK_WOOD
                    ));
                    break;
                }
                case FLINTLOCK: {
                    BookRecipeRegistry.addRecipe("flintlock1", new ShapedOreRecipe(item.createItemStack(),
                      "f i",
                      "iri",
                      'i', INGOT_IRON,
                      'r', DUST_REDSTONE,
                      'f', FLINT_AND_STEEL
                    ));
                    BookRecipeRegistry.addRecipe("flintlock2", new ShapedOreRecipe(item.createItemStack(),
                      "f i",
                      "iri",
                      'i', PLATE_IRON,
                      'r', DUST_REDSTONE,
                      'f', FLINT_AND_STEEL
                    ));
                    break;
                }
                case IRON_BARREL: {
                    if (Config.disableMainBarrelRecipe) {
                        BookRecipeRegistry.addRecipe("barrel1", new ShapedOreRecipe(item.createItemStack(),
                          "i  ",
                          " i ",
                          "  i",
                          'i', INGOT_IRON
                        ));
                    } else {
                        BookRecipeRegistry.addRecipe("barrel1", new ShapedOreRecipe(item.createItemStack(),
                          "i  ",
                          " i ",
                          "  i",
                          'i', PLATE_IRON
                        ));
                    }
                    BookRecipeRegistry.addRecipe("barrel2", new ShapedOreRecipe(item.createItemStack(),
                      "i  ",
                      " i ",
                      "  i",
                      'i', PLATE_IRON
                    ));
                    break;
                }
                case BLUNDERBUSS_BARREL: {
                    BookRecipeRegistry.addRecipe("blunderBarrel1", new ShapedOreRecipe(item.createItemStack(),
                      "i  ",
                      " i ",
                      "  i",
                      'i', INGOT_BRASS
                    ));
                    BookRecipeRegistry.addRecipe("blunderBarrel2", new ShapedOreRecipe(item.createItemStack(),
                      "i  ",
                      " i ",
                      "  i",
                      'i', PLATE_BRASS
                    ));
                    break;
                }
            }
        }
    }
}
