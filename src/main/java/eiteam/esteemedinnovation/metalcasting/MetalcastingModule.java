package eiteam.esteemedinnovation.metalcasting;

import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.api.mold.MoldRegistry;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.CrossMod;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import eiteam.esteemedinnovation.metalcasting.mold.*;
import eiteam.esteemedinnovation.metalcasting.mold.molds.ItemIngotMold;
import eiteam.esteemedinnovation.metalcasting.mold.molds.ItemNuggetMold;
import eiteam.esteemedinnovation.metalcasting.mold.molds.ItemPipeMold;
import eiteam.esteemedinnovation.metalcasting.mold.molds.ItemPlateMold;
import eiteam.esteemedinnovation.misc.ItemCraftingComponent;
import minetweaker.MineTweakerAPI;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static eiteam.esteemedinnovation.commons.EsteemedInnovation.CASTING_CATEGORY;
import static eiteam.esteemedinnovation.commons.OreDictEntries.PLANK_WOOD;
import static eiteam.esteemedinnovation.misc.MiscellaneousModule.COMPONENT;
import static net.minecraft.init.Items.BRICK;

public class MetalcastingModule extends ContentModule {
    public static Block CRUCIBLE;
    public static Block HELL_CRUCIBLE;
    public static Block CARVING_TABLE;
    public static Block MOLD;
    public static Item BLANK_MOLD;
    public static Item INGOT_MOLD;
    public static Item PLATE_MOLD;
    public static Item NUGGET_MOLD;
    public static Item PIPE_MOLD;

    @Override
    public void create(Side side) {
        CRUCIBLE = setup(new BlockCrucible(), "crucible");
        HELL_CRUCIBLE = setup(new BlockCrucible(), "hell_crucible");
        CARVING_TABLE = setup(new BlockCarvingTable(), "carving_table");
        MOLD = setup(new BlockMold(), "mold");
        BLANK_MOLD = setup(new Item().setMaxStackSize(1), "blank_mold");
        MoldRegistry.addCarvableMold(BLANK_MOLD);
        INGOT_MOLD = setup(new ItemIngotMold(), "ingot_mold");
        MoldRegistry.addCarvableMold(INGOT_MOLD);
        PLATE_MOLD = setup(new ItemPlateMold(), "plate_mold");
        MoldRegistry.addCarvableMold(PLATE_MOLD);
        NUGGET_MOLD = setup(new ItemNuggetMold(), "nugget_mold");
        MoldRegistry.addCarvableMold(NUGGET_MOLD);
        PIPE_MOLD = setup(new ItemPipeMold(), "pipe_mold");
        MoldRegistry.addCarvableMold(PIPE_MOLD);

        registerTileEntity(TileEntityCrucible.class, "crucible");
        registerTileEntity(TileEntityMold.class, "mold");
    }

    @Override
    public void recipes(Side side) {
        if (Config.enableCrucible) {
            BookRecipeRegistry.addRecipe("crucible", new ItemStack(CRUCIBLE),
              "x x",
              "x x",
              "xxx",
              'x', BRICK
            );
            if (Config.enableHellCrucible) {
                BookRecipeRegistry.addRecipe("hellCrucible", new ItemStack(HELL_CRUCIBLE),
                  "x x",
                  "x x",
                  "xxx",
                  'x', new ItemStack(COMPONENT, 1, ItemCraftingComponent.Types.HELLFORGE_BRICK.getMetadata())
                );
            }
        }
        if (Config.enableMold) {
            BookRecipeRegistry.addRecipe("carving", new ShapedOreRecipe(CARVING_TABLE,
              "xzx",
              "x x",
              "xxx",
              'x', PLANK_WOOD,
              'z', BLANK_MOLD
            ));
            BookRecipeRegistry.addRecipe("mold", new ItemStack(MOLD),
              "xxx",
              "xxx",
              'x', BRICK
            );
            BookRecipeRegistry.addRecipe("blankMold", new ShapedOreRecipe(BLANK_MOLD, "xx", 'x', BRICK));
        }
    }

    @Override
    public void finish(Side side) {
        if (CrossMod.CRAFTTWEAKER) {
            MineTweakerAPI.registerClass(CrucibleTweaker.class);
            MineTweakerAPI.registerClass(CarvingTableTweaker.class);
        }

        if (Config.enableCrucible) {
            BookPageRegistry.addEntryToCategory(CASTING_CATEGORY, new BookEntry("research.Crucible.name",
              new BookPageItem("research.Crucible.name", "research.Crucible.0", new ItemStack(CRUCIBLE)),
              new BookPageText("research.Crucible.name", "research.Crucible.1"),
              new BookPageCrafting("", "crucible")));
            if (Config.enableHellCrucible) {
                BookPageRegistry.addEntryToCategory(CASTING_CATEGORY, new BookEntry("research.HellCrucible.name",
                  new BookPageItem("research.HellCrucible.name", "research.HellCrucible.0", new ItemStack(HELL_CRUCIBLE)),
                  new BookPageCrafting("", "hellCrucible")));
            }
        }
        if (Config.enableMold) {
            BookPageRegistry.addEntryToCategory(CASTING_CATEGORY, new BookEntry("research.Mold.name",
              new BookPageItem("research.Mold.name", "research.Mold.0", new ItemStack(MOLD)),
              new BookPageText("research.Mold.name", "research.Mold.1"),
              new BookPageCrafting("", "mold")));
            BookPageRegistry.addEntryToCategory(CASTING_CATEGORY, new BookEntry("research.Molds.name",
              new BookPageItem("research.Molds.name", "research.Molds.0",
                new ItemStack(PLATE_MOLD),
                new ItemStack(INGOT_MOLD),
                new ItemStack(NUGGET_MOLD),
                new ItemStack(PIPE_MOLD)),
              new BookPageCrafting("", "blankMold"),
              new BookPageCrafting("", "carving")));
        }
    }

    @Override
    public void preInitClient() {
        registerModel(CRUCIBLE);
        registerModel(HELL_CRUCIBLE);
        registerModel(CARVING_TABLE);
        registerModel(MOLD);
        registerModel(BLANK_MOLD);
        registerModel(INGOT_MOLD);
        registerModel(NUGGET_MOLD);
        registerModel(PLATE_MOLD);
        registerModel(PIPE_MOLD);
    }

    @Override
    public void initClient() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrucible.class, new TileEntityCrucibleRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMold.class, new TileEntityMoldRenderer());
    }
}
