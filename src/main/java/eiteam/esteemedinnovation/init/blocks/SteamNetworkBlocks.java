package eiteam.esteemedinnovation.init.blocks;

import eiteam.esteemedinnovation.Config;
import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.api.book.BookRecipeRegistry;
import eiteam.esteemedinnovation.block.*;
import eiteam.esteemedinnovation.block.pipe.BlockValvePipe;
import eiteam.esteemedinnovation.init.IInitCategory;
import eiteam.esteemedinnovation.item.BlockTankItem;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.lang.reflect.InvocationTargetException;

import static eiteam.esteemedinnovation.init.misc.OreDictEntries.*;
import static net.minecraft.init.Blocks.FURNACE;
import static net.minecraft.init.Blocks.LEVER;
import static net.minecraft.init.Items.COMPASS;

public class SteamNetworkBlocks implements IInitCategory {
    public enum Blocks {
        BOILER(new BlockBoiler(), "boiler"),
        VALVE_PIPE(new BlockValvePipe(), "valve_pipe"),
        TANK(new BlockSteamTank(), "steam_tank", BlockTankItem.class),
        STEAM_GAUGE(new BlockSteamGauge(), "steam_gauge"),
        RUPTURE_DISC(new BlockRuptureDisc(), "rupture_disc", BlockTankItem.class),
        STEAM_WHISTLE(new BlockWhistle(), "steam_whistle"),
        PRESSURE_CONVERTER(new BlockFluidSteamConverter(), "pressure_converter");

        private Block block;

        public static Blocks[] LOOKUP = new Blocks[values().length];
        static {
            for (Blocks block : values()) {
                if (block.isEnabled()) {
                    LOOKUP[block.ordinal()] = block;
                }
            }
        }

        Blocks(Block block, String name) {
            this(block, name, ItemBlock.class);
        }

        Blocks(Block block, String name, Class<? extends ItemBlock> itemBlockClass) {
            block.setCreativeTab(EsteemedInnovation.tab);
            block.setUnlocalizedName(EsteemedInnovation.MOD_ID + ":" + name);
            block.setRegistryName(EsteemedInnovation.MOD_ID, name);
            GameRegistry.register(block);
            ItemBlock itemBlock = null;
            try {
                itemBlock = itemBlockClass.getDeclaredConstructor(Block.class).newInstance(block);
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
            if (itemBlock != null) {
                itemBlock.setRegistryName(block.getRegistryName());
                GameRegistry.register(itemBlock);
            }
            this.block = block;
        }

        public boolean isEnabled() {
            switch (this) {
                case BOILER: {
                    return Config.enableBoiler;
                }
                case VALVE_PIPE: {
                    return Config.enableValvePipe;
                }
                case TANK: {
                    return Config.enableTank;
                }
                case STEAM_GAUGE: {
                    return Config.enableGauge;
                }
                case RUPTURE_DISC: {
                    return Config.enableRuptureDisc;
                }
                case STEAM_WHISTLE: {
                    return Config.enableHorn;
                }
                case PRESSURE_CONVERTER: {
                    return Config.enableFluidSteamConverter;
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
                case BOILER: {
                    BookRecipeRegistry.addRecipe("boiler1", new ShapedOreRecipe(block.getBlock(),
                      "xxx",
                      "xfx",
                      "xxx",
                      'x', INGOT_BRASS,
                      'f', FURNACE
                    ));
                    BookRecipeRegistry.addRecipe("boiler2", new ShapedOreRecipe(block.getBlock(),
                      "xxx",
                      "xfx",
                      "xxx",
                      'x', PLATE_THIN_BRASS,
                      'f', FURNACE
                    ));
                    break;
                }
                case VALVE_PIPE: {
                    BookRecipeRegistry.addRecipe("valvePipe", new ShapelessOreRecipe(block.getBlock(),
                      PipeBlocks.Blocks.BRASS_PIPE.getBlock(), LEVER));
                    break;
                }
                case TANK: {
                    BookRecipeRegistry.addRecipe("tank1", new ShapedOreRecipe(block.getBlock(),
                      "iii",
                      "i i",
                      "iii",
                      'i', PLATE_THIN_BRASS
                    ));
                    BookRecipeRegistry.addRecipe("tank2", new ShapedOreRecipe(block.getBlock(),
                      "iii",
                      "i i",
                      "iii",
                      'i', INGOT_BRASS
                    ));
                    break;
                }
                case STEAM_GAUGE: {
                    BookRecipeRegistry.addRecipe("gauge", new ShapedOreRecipe(block.getBlock(),
                      " x ",
                      "xrx",
                      " x ",
                      'x', INGOT_BRASS,
                      'r', COMPASS));
                    break;
                }
                case STEAM_WHISTLE: {
                    BookRecipeRegistry.addRecipe("whistle1", new ShapedOreRecipe(block.getBlock(),
                      " bb",
                      " bn",
                      "pp ",
                      'n', NUGGET_BRASS,
                      'b', PLATE_THIN_BRASS,
                      'p', PipeBlocks.Blocks.BRASS_PIPE.getBlock()
                    ));
                    BookRecipeRegistry.addRecipe("whistle2", new ShapedOreRecipe(block.getBlock(),
                      " bb",
                      " bn",
                      "pp ",
                      'n', NUGGET_BRASS,
                      'b', INGOT_BRASS,
                      'p', PipeBlocks.Blocks.BRASS_PIPE.getBlock()
                    ));
                    break;
                }
                case RUPTURE_DISC: {
                    BookRecipeRegistry.addRecipe("disc", new ShapedOreRecipe(block.getBlock(),
                      " x ",
                      "xrx",
                      " x ",
                      'x', NUGGET_BRASS,
                      'r', PLATE_THIN_ZINC
                    ));
                    GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(block.getBlock(), 1, 0),
                      PLATE_THIN_ZINC, new ItemStack(block.getBlock(), 1, 1)));
                    break;
                }
                case PRESSURE_CONVERTER: {
                    BookRecipeRegistry.addRecipe("fsc1", new ShapedOreRecipe(block.getBlock(),
                      "xlx",
                      "gpp",
                      "xlx",
                      'x', "ingotBrass",
                      'l', LEATHER_ORE,
                      'p', PipeBlocks.Blocks.BRASS_PIPE.getBlock(),
                      'g', PANE_GLASS_COLORLESS
                    ));
                    BookRecipeRegistry.addRecipe("fsc1", new ShapedOreRecipe(block.getBlock(),
                      "xlx",
                      "gpp",
                      "xlx",
                      'x', PLATE_THIN_BRASS,
                      'l', LEATHER_ORE,
                      'p', PipeBlocks.Blocks.BRASS_PIPE.getBlock(),
                      'g', PANE_GLASS_COLORLESS
                    ));
                    break;
                }
            }
        }
    }
}
