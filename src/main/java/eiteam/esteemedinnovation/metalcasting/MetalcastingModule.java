package eiteam.esteemedinnovation.metalcasting;

import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.api.crucible.CrucibleRegistry;
import eiteam.esteemedinnovation.api.mold.MoldRegistry;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.CrossMod;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import eiteam.esteemedinnovation.metalcasting.mold.*;
import eiteam.esteemedinnovation.misc.ItemCraftingComponent;
import minetweaker.MineTweakerAPI;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static eiteam.esteemedinnovation.commons.EsteemedInnovation.CASTING_CATEGORY;
import static eiteam.esteemedinnovation.commons.OreDictEntries.*;
import static eiteam.esteemedinnovation.materials.MaterialsModule.*;
import static eiteam.esteemedinnovation.materials.refined.ItemMetalIngot.Types.*;
import static eiteam.esteemedinnovation.materials.refined.ItemMetalNugget.Types.*;
import static eiteam.esteemedinnovation.materials.refined.plates.ItemMetalPlate.Types.*;
import static eiteam.esteemedinnovation.misc.MiscellaneousModule.COMPONENT;
import static eiteam.esteemedinnovation.transport.TransportationModule.BRASS_PIPE;
import static eiteam.esteemedinnovation.transport.TransportationModule.COPPER_PIPE;
import static net.minecraft.init.Items.*;

public class MetalcastingModule extends ContentModule {
    public static Block CRUCIBLE;
    public static Block HELL_CRUCIBLE;
    public static Block CARVING_TABLE;
    public static Block MOLD;
    public static Item BLANK_MOLD;
    public static Item MOLD_ITEM;

    @Override
    public void create(Side side) {
        CRUCIBLE = setup(new BlockCrucible(), "crucible");
        HELL_CRUCIBLE = setup(new BlockCrucible(), "hell_crucible");
        CARVING_TABLE = setup(new BlockCarvingTable(), "carving_table");
        MOLD = setup(new BlockMold(), "mold");
        BLANK_MOLD = setup(new Item().setMaxStackSize(1), "blank_mold");
        MoldRegistry.addCarvableMold(BLANK_MOLD);
        MOLD_ITEM = setup(new ItemMold(), "mold_item");
        for (ItemMold.Type type : ItemMold.Type.LOOKUP) {
            MoldRegistry.addCarvableMold(type.createItemStack(MOLD_ITEM));
        }

        registerTileEntity(TileEntityCrucible.class, "crucible");
        registerTileEntity(TileEntityMold.class, "mold");
    }

    private static ItemStack findFirstOre(String ore) {
        for (ItemStack stack : OreDictionary.getOres(ore)) {
            if (stack != null) {
                return stack;
            }
        }
        return null;
    }

    @Override
    public void recipes(Side side) {
        CrucibleRegistry.registerMoldingRecipe(IRON_LIQUID, ItemMold.Type.INGOT.createItemStack(MOLD_ITEM), new ItemStack(IRON_INGOT));
        CrucibleRegistry.registerMoldingRecipe(IRON_LIQUID, ItemMold.Type.NUGGET.createItemStack(MOLD_ITEM), new ItemStack(METAL_NUGGET, 1, IRON_NUGGET.getMeta()));
        CrucibleRegistry.registerMoldingRecipe(IRON_LIQUID, ItemMold.Type.PLATE.createItemStack(MOLD_ITEM), new ItemStack(METAL_PLATE, 1, IRON_PLATE.getMeta()));

        CrucibleRegistry.registerMoldingRecipe(GOLD_LIQUID, ItemMold.Type.INGOT.createItemStack(MOLD_ITEM), new ItemStack(GOLD_INGOT));
        CrucibleRegistry.registerMoldingRecipe(GOLD_LIQUID, ItemMold.Type.NUGGET.createItemStack(MOLD_ITEM), new ItemStack(GOLD_NUGGET));
        CrucibleRegistry.registerMoldingRecipe(GOLD_LIQUID, ItemMold.Type.PLATE.createItemStack(MOLD_ITEM), new ItemStack(METAL_PLATE, 1, GOLD_PLATE.getMeta()));

        CrucibleRegistry.registerMoldingRecipe(ZINC_LIQUID, ItemMold.Type.INGOT.createItemStack(MOLD_ITEM), new ItemStack(METAL_INGOT, 1, ZINC_INGOT.getMeta()));
        CrucibleRegistry.registerMoldingRecipe(ZINC_LIQUID, ItemMold.Type.NUGGET.createItemStack(MOLD_ITEM), new ItemStack(METAL_NUGGET, 1, ZINC_NUGGET.getMeta()));
        CrucibleRegistry.registerMoldingRecipe(ZINC_LIQUID, ItemMold.Type.PLATE.createItemStack(MOLD_ITEM), new ItemStack(METAL_PLATE, 1, ZINC_PLATE.getMeta()));

        CrucibleRegistry.registerMoldingRecipe(COPPER_LIQUID, ItemMold.Type.INGOT.createItemStack(MOLD_ITEM), new ItemStack(METAL_INGOT, 1, COPPER_INGOT.getMeta()));
        CrucibleRegistry.registerMoldingRecipe(COPPER_LIQUID, ItemMold.Type.NUGGET.createItemStack(MOLD_ITEM), new ItemStack(METAL_NUGGET, 1, COPPER_NUGGET.getMeta()));
        CrucibleRegistry.registerMoldingRecipe(COPPER_LIQUID, ItemMold.Type.PLATE.createItemStack(MOLD_ITEM), new ItemStack(METAL_PLATE, 1, COPPER_PLATE.getMeta()));
        CrucibleRegistry.registerMoldingRecipe(COPPER_LIQUID, ItemMold.Type.PIPE.createItemStack(MOLD_ITEM), new ItemStack(COPPER_PIPE));

        CrucibleRegistry.registerMoldingRecipe(BRASS_LIQUID, ItemMold.Type.INGOT.createItemStack(MOLD_ITEM), new ItemStack(METAL_INGOT, 1, BRASS_INGOT.getMeta()));
        CrucibleRegistry.registerMoldingRecipe(BRASS_LIQUID, ItemMold.Type.NUGGET.createItemStack(MOLD_ITEM), new ItemStack(METAL_NUGGET, 1, BRASS_NUGGET.getMeta()));
        CrucibleRegistry.registerMoldingRecipe(BRASS_LIQUID, ItemMold.Type.PLATE.createItemStack(MOLD_ITEM), new ItemStack(METAL_PLATE, 1, BRASS_PLATE.getMeta()));
        CrucibleRegistry.registerMoldingRecipe(BRASS_LIQUID, ItemMold.Type.PIPE.createItemStack(MOLD_ITEM), new ItemStack(BRASS_PIPE));

        CrucibleRegistry.registerMoldingRecipe(LEAD_LIQUID, ItemMold.Type.INGOT.createItemStack(MOLD_ITEM), findFirstOre(INGOT_LEAD));
        CrucibleRegistry.registerMoldingRecipe(LEAD_LIQUID, ItemMold.Type.NUGGET.createItemStack(MOLD_ITEM), findFirstOre(NUGGET_LEAD));
        CrucibleRegistry.registerMoldingRecipe(LEAD_LIQUID, ItemMold.Type.PLATE.createItemStack(MOLD_ITEM), findFirstOre(PLATE_THIN_LEAD));

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
                ItemMold.Type.PLATE.createItemStack(MOLD_ITEM),
                ItemMold.Type.INGOT.createItemStack(MOLD_ITEM),
                ItemMold.Type.NUGGET.createItemStack(MOLD_ITEM),
                ItemMold.Type.PIPE.createItemStack(MOLD_ITEM)),
              new BookPageCrafting("", "blankMold"),
              new BookPageCrafting("", "carving")));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void preInitClient() {
        registerModel(CRUCIBLE);
        registerModel(HELL_CRUCIBLE);
        registerModel(CARVING_TABLE);
        registerModel(MOLD);
        registerModel(BLANK_MOLD);
        registerModelItemStack(ItemMold.Type.INGOT.createItemStack(MOLD_ITEM));
        registerModelItemStack(ItemMold.Type.NUGGET.createItemStack(MOLD_ITEM));
        registerModelItemStack(ItemMold.Type.PLATE.createItemStack(MOLD_ITEM));
        registerModelItemStack(ItemMold.Type.PIPE.createItemStack(MOLD_ITEM));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initClient() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrucible.class, new TileEntityCrucibleRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMold.class, new TileEntityMoldRenderer());
    }
}
