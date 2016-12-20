package eiteam.esteemedinnovation.init.blocks;

import eiteam.esteemedinnovation.Config;
import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.api.book.BookRecipeRegistry;
import eiteam.esteemedinnovation.api.crucible.CrucibleRegistry;
import eiteam.esteemedinnovation.block.BlockEngineeringTable;
import eiteam.esteemedinnovation.block.BlockFunnel;
import eiteam.esteemedinnovation.block.BlockOreDepositGenerator;
import eiteam.esteemedinnovation.block.BlockWoodenCone;
import eiteam.esteemedinnovation.init.IInitCategory;
import eiteam.esteemedinnovation.init.misc.DefaultCrucibleLiquids;
import eiteam.esteemedinnovation.init.misc.OreDictEntries;
import eiteam.esteemedinnovation.item.BlockManyMetadataItem;
import eiteam.esteemedinnovation.misc.RecipeUtility;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class MiscellaneousBlocks implements IInitCategory {
    public enum Blocks {
        ENGINEERING_TABLE(new BlockEngineeringTable(), "engineering"),
        WOODEN_CONE(new BlockWoodenCone(), "wooden_cone"),
        FUNNEL(new BlockFunnel(), "funnel"),
        ORE_DEPOSIT_BLOCK(new BlockOreDepositGenerator(), true, "ore_deposit_generator");

        public static Blocks[] LOOKUP = new Blocks[values().length];

        static {
            for (Blocks block : values()) {
                if (block.isEnabled()) {
                    LOOKUP[block.ordinal()] = block;
                }
            }
        }

        private Block block;

        Blocks(Block block, boolean meta, String name) {
            this(block, new BlockManyMetadataItem(block), name);
        }

        Blocks(Block block, String name) {
            this(block, new ItemBlock(block), name);
        }

        // You probably won't actually be able to use this one since ItemBlock constructors take the Block.
        Blocks(Block block, ItemBlock itemBlock, String name) {
            block.setUnlocalizedName(EsteemedInnovation.MOD_ID + ":" + name);
            block.setRegistryName(EsteemedInnovation.MOD_ID, name);
            GameRegistry.register(block);
            GameRegistry.register(itemBlock.setRegistryName(block.getRegistryName()));
            this.block = block;
        }

        public boolean isEnabled() {
            switch (this) {
                case ENGINEERING_TABLE: {
                    return Config.enableEngineering;
                }
                case WOODEN_CONE: {
                    return true;
                }
                case FUNNEL: {
                    return WOODEN_CONE.isEnabled() && Config.enableFunnel;
                }
                case ORE_DEPOSIT_BLOCK: {
                    return true;
                }
            }
            return false;
        }

        public Block getBlock() {
            return block;
        }
    }

    @Override
    public void oreDict() {
        Blocks.values();
    }

    @Override
    public void recipes() {
        for (Blocks block : Blocks.LOOKUP) {
            switch (block) {
                case ENGINEERING_TABLE: {
                    BookRecipeRegistry.addRecipe("engineering", new ShapedOreRecipe(block.getBlock(),
                      "xzx",
                      "x x",
                      "xxx",
                      'x', OreDictEntries.COBBLESTONE_ORE,
                      'z', OreDictEntries.PLATE_THIN_IRON
                    ));
                    break;
                }
                case WOODEN_CONE: {
                    BookRecipeRegistry.addRecipe("woodenCone", new ShapedOreRecipe(block.getBlock(),
                      " s ",
                      "p p",
                      "l l",
                      's', OreDictEntries.STICK_WOOD,
                      'p', OreDictEntries.PLANK_WOOD,
                      'l', OreDictEntries.LOG_WOOD
                    ));
                    if (Config.removeHopperRecipe) {
                        RecipeUtility.removeRecipe(recipe -> {
                            ItemStack output = recipe.getRecipeOutput();
                            return output != null && output.getItem() == Item.getItemFromBlock(net.minecraft.init.Blocks.HOPPER);
                        });
                    }
                    CrucibleRegistry.registerDunkRecipe(Item.getItemFromBlock(block.getBlock()),
                      DefaultCrucibleLiquids.Liquids.IRON_LIQUID.getLiquid(), 45,
                      new ItemStack(net.minecraft.init.Blocks.HOPPER));
                    break;
                }
                case FUNNEL: {
                    CrucibleRegistry.registerDunkRecipe(
                      Item.getItemFromBlock(Blocks.WOODEN_CONE.getBlock()),
                      DefaultCrucibleLiquids.Liquids.COPPER_LIQUID.getLiquid(), 45,
                      new ItemStack(block.getBlock()));
                    break;
                }
                case ORE_DEPOSIT_BLOCK: {
                    break;
                }
            }
        }
    }
}
