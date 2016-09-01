package eiteam.esteemedinnovation.init.items;

import eiteam.esteemedinnovation.Config;
import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.api.book.BookRecipeRegistry;
import eiteam.esteemedinnovation.init.IInitCategory;
import eiteam.esteemedinnovation.init.blocks.PipeBlocks;
import eiteam.esteemedinnovation.item.ItemCraftingComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static eiteam.esteemedinnovation.init.misc.OreDictEntries.*;
import static net.minecraft.init.Blocks.PISTON;
import static net.minecraft.init.Items.FLINT_AND_STEEL;

public class CraftingComponentItems implements IInitCategory {
    public enum Items {
        BRASS_PISTON(0),
        BRASS_TURBINE(1),
        GUN_STOCK(2),
        FLINTLOCK(3),
        IRON_BARREL(4),
        BLUNDERBUSS_BARREL(5);

        private int metadata;
        public static Item ITEM = new ItemCraftingComponent();

        static {
            ITEM.setUnlocalizedName(EsteemedInnovation.MOD_ID + ":crafting");
            ITEM.setRegistryName(EsteemedInnovation.MOD_ID, "crafting");
            ITEM.setCreativeTab(EsteemedInnovation.tab);
            GameRegistry.register(ITEM);
        }

        Items(int metadata) {
            this.metadata = metadata;
        }

        public ItemStack createItemStack(int size) {
            return new ItemStack(ITEM, size, metadata);
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
                      'i', PipeBlocks.Blocks.BRASS_PIPE.getBlock()
                    ));
                    BookRecipeRegistry.addRecipe("piston2", new ShapedOreRecipe(item.createItemStack(),
                      " x ",
                      "xpx",
                      " i ",
                      'x', PLATE_THIN_BRASS,
                      'p', PISTON,
                      'i', PipeBlocks.Blocks.BRASS_PIPE.getBlock()
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
                      'x', PLATE_THIN_BRASS,
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
                      'i', PLATE_THIN_IRON,
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
                          'i', PLATE_THIN_IRON
                        ));
                    }
                    BookRecipeRegistry.addRecipe("barrel2", new ShapedOreRecipe(item.createItemStack(),
                      "i  ",
                      " i ",
                      "  i",
                      'i', PLATE_THIN_IRON
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
                      'i', PLATE_THIN_BRASS
                    ));
                    break;
                }
            }
        }
    }
}
