package eiteam.esteemedinnovation.metalcasting;

import crafttweaker.CraftTweakerAPI;
import eiteam.esteemedinnovation.api.Constants;
import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.api.crucible.CrucibleRegistry;
import eiteam.esteemedinnovation.api.mold.MoldRegistry;
import eiteam.esteemedinnovation.api.research.ResearchRecipe;
import eiteam.esteemedinnovation.commons.CrossMod;
import eiteam.esteemedinnovation.commons.init.ConfigurableModule;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import eiteam.esteemedinnovation.commons.util.RecipeUtility;
import eiteam.esteemedinnovation.metalcasting.crucible.BlockCrucible;
import eiteam.esteemedinnovation.metalcasting.crucible.CrucibleTweaker;
import eiteam.esteemedinnovation.metalcasting.crucible.TileEntityCrucible;
import eiteam.esteemedinnovation.metalcasting.crucible.TileEntityCrucibleRenderer;
import eiteam.esteemedinnovation.metalcasting.hut.MetalcastingHutComponent;
import eiteam.esteemedinnovation.metalcasting.hut.MetalcastingHutCreationHandler;
import eiteam.esteemedinnovation.metalcasting.mold.*;
import eiteam.esteemedinnovation.misc.ItemCraftingComponent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;

import static eiteam.esteemedinnovation.commons.Config.CATEGORY_BLOCKS;
import static eiteam.esteemedinnovation.commons.Config.CATEGORY_WORLD_GENERATION;
import static eiteam.esteemedinnovation.commons.EsteemedInnovation.CASTING_SECTION;
import static eiteam.esteemedinnovation.commons.OreDictEntries.*;
import static eiteam.esteemedinnovation.materials.MaterialsModule.*;
import static eiteam.esteemedinnovation.materials.refined.ItemMetalIngot.Types.BRASS_INGOT;
import static eiteam.esteemedinnovation.materials.refined.ItemMetalIngot.Types.COPPER_INGOT;
import static eiteam.esteemedinnovation.materials.refined.ItemMetalIngot.Types.ZINC_INGOT;
import static eiteam.esteemedinnovation.materials.refined.ItemMetalNugget.Types.BRASS_NUGGET;
import static eiteam.esteemedinnovation.materials.refined.ItemMetalNugget.Types.COPPER_NUGGET;
import static eiteam.esteemedinnovation.materials.refined.ItemMetalNugget.Types.ZINC_NUGGET;
import static eiteam.esteemedinnovation.materials.refined.plates.ItemMetalPlate.Types.*;
import static eiteam.esteemedinnovation.misc.MiscellaneousModule.COMPONENT;
import static eiteam.esteemedinnovation.transport.TransportationModule.BRASS_PIPE;
import static eiteam.esteemedinnovation.transport.TransportationModule.COPPER_PIPE;
import static net.minecraft.init.Items.*;

public class MetalcastingModule extends ContentModule implements ConfigurableModule {
    public static final ResourceLocation CARVING_TABLE_LOOT = new ResourceLocation(Constants.EI_MODID, "metalcasting_hut_mold_chest");
    public static Block CRUCIBLE;
    public static Block HELL_CRUCIBLE;
    public static Block CARVING_TABLE;
    public static Block MOLD;
    public static Item BLANK_MOLD;
    public static Item MOLD_ITEM;
    public static int metalcastingHutWeight;
    public static int metalcastingHutLimit;
    private static boolean enableMold;
    private static boolean enableHellCrucible;
    public static boolean enableCrucible;

    @Override
    public void create(Side side) {
        VillagerRegistry.instance().registerVillageCreationHandler(new MetalcastingHutCreationHandler());
        MapGenStructureIO.registerStructureComponent(MetalcastingHutComponent.class, Constants.EI_MODID + ":metalcasting_hut");
        MinecraftForge.EVENT_BUS.register(new MetalcastingBookSection.Unlocker());
    }

    @Override
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        CRUCIBLE = setup(event, new BlockCrucible(), "crucible");
        HELL_CRUCIBLE = setup(event, new BlockCrucible(), "hell_crucible");
        CARVING_TABLE = setup(event, new BlockCarvingTable(), "carving_table");
        MOLD = setup(event, new BlockMold(), "mold");

        registerTileEntity(TileEntityCrucible.class, "crucible");
        registerTileEntity(TileEntityMold.class, "mold");
    }

    @Override
    public void registerItems(RegistryEvent.Register<Item> event) {
        setupItemBlock(event, CRUCIBLE);
        setupItemBlock(event, HELL_CRUCIBLE);
        setupItemBlock(event, CARVING_TABLE);
        setupItemBlock(event, MOLD);

        BLANK_MOLD = setup(event, new Item().setMaxStackSize(1), "blank_mold");
        MoldRegistry.addCarvableMold(BLANK_MOLD);
        MOLD_ITEM = setup(event, new ItemMold(), "mold_item");
        for (ItemMold.Type type : ItemMold.Type.LOOKUP) {
            MoldRegistry.addCarvableMold(type.createItemStack(MOLD_ITEM));
        }
    }

    @Nonnull
    private static ItemStack findFirstOre(String ore) {
        for (ItemStack stack : OreDictionary.getOres(ore)) {
            if (!stack.isEmpty()) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void recipes(RegistryEvent.Register<IRecipe> event) {
        CrucibleRegistry.registerMoldingRecipe(IRON_LIQUID, ItemMold.Type.INGOT.createItemStack(MOLD_ITEM), new ItemStack(IRON_INGOT));
        CrucibleRegistry.registerMoldingRecipe(IRON_LIQUID, ItemMold.Type.NUGGET.createItemStack(MOLD_ITEM), new ItemStack(IRON_NUGGET));
        CrucibleRegistry.registerMoldingRecipe(IRON_LIQUID, ItemMold.Type.THIN_PLATE.createItemStack(MOLD_ITEM), new ItemStack(METAL_PLATE, 1, IRON_PLATE.getMeta()));

        CrucibleRegistry.registerMoldingRecipe(GOLD_LIQUID, ItemMold.Type.INGOT.createItemStack(MOLD_ITEM), new ItemStack(GOLD_INGOT));
        CrucibleRegistry.registerMoldingRecipe(GOLD_LIQUID, ItemMold.Type.NUGGET.createItemStack(MOLD_ITEM), new ItemStack(GOLD_NUGGET));
        CrucibleRegistry.registerMoldingRecipe(GOLD_LIQUID, ItemMold.Type.THIN_PLATE.createItemStack(MOLD_ITEM), new ItemStack(METAL_PLATE, 1, GOLD_PLATE.getMeta()));

        CrucibleRegistry.registerMoldingRecipe(ZINC_LIQUID, ItemMold.Type.INGOT.createItemStack(MOLD_ITEM), new ItemStack(METAL_INGOT, 1, ZINC_INGOT.getMeta()));
        CrucibleRegistry.registerMoldingRecipe(ZINC_LIQUID, ItemMold.Type.NUGGET.createItemStack(MOLD_ITEM), new ItemStack(METAL_NUGGET, 1, ZINC_NUGGET.getMeta()));
        CrucibleRegistry.registerMoldingRecipe(ZINC_LIQUID, ItemMold.Type.THIN_PLATE.createItemStack(MOLD_ITEM), new ItemStack(METAL_PLATE, 1, ZINC_PLATE.getMeta()));

        CrucibleRegistry.registerMoldingRecipe(COPPER_LIQUID, ItemMold.Type.INGOT.createItemStack(MOLD_ITEM), new ItemStack(METAL_INGOT, 1, COPPER_INGOT.getMeta()));
        CrucibleRegistry.registerMoldingRecipe(COPPER_LIQUID, ItemMold.Type.NUGGET.createItemStack(MOLD_ITEM), new ItemStack(METAL_NUGGET, 1, COPPER_NUGGET.getMeta()));
        CrucibleRegistry.registerMoldingRecipe(COPPER_LIQUID, ItemMold.Type.THIN_PLATE.createItemStack(MOLD_ITEM), new ItemStack(METAL_PLATE, 1, COPPER_PLATE.getMeta()));
        CrucibleRegistry.registerMoldingRecipe(COPPER_LIQUID, ItemMold.Type.PIPE.createItemStack(MOLD_ITEM), new ItemStack(COPPER_PIPE));

        CrucibleRegistry.registerMoldingRecipe(BRASS_LIQUID, ItemMold.Type.INGOT.createItemStack(MOLD_ITEM), new ItemStack(METAL_INGOT, 1, BRASS_INGOT.getMeta()));
        CrucibleRegistry.registerMoldingRecipe(BRASS_LIQUID, ItemMold.Type.NUGGET.createItemStack(MOLD_ITEM), new ItemStack(METAL_NUGGET, 1, BRASS_NUGGET.getMeta()));
        CrucibleRegistry.registerMoldingRecipe(BRASS_LIQUID, ItemMold.Type.THIN_PLATE.createItemStack(MOLD_ITEM), new ItemStack(METAL_PLATE, 1, BRASS_PLATE.getMeta()));
        CrucibleRegistry.registerMoldingRecipe(BRASS_LIQUID, ItemMold.Type.PIPE.createItemStack(MOLD_ITEM), new ItemStack(BRASS_PIPE));

        CrucibleRegistry.registerMoldingRecipe(LEAD_LIQUID, ItemMold.Type.INGOT.createItemStack(MOLD_ITEM), findFirstOre(INGOT_LEAD));
        CrucibleRegistry.registerMoldingRecipe(LEAD_LIQUID, ItemMold.Type.NUGGET.createItemStack(MOLD_ITEM), findFirstOre(NUGGET_LEAD));
        CrucibleRegistry.registerMoldingRecipe(LEAD_LIQUID, ItemMold.Type.THIN_PLATE.createItemStack(MOLD_ITEM), findFirstOre(PLATE_THIN_LEAD));

        if (enableCrucible) {
            RecipeUtility.addRecipe(event, true, "crucible", new ResearchRecipe(new ItemStack(CRUCIBLE), MetalcastingBookSection.NAME,
              "x x",
              "x x",
              "xxx",
              'x', BRICK
            ));
            if (enableHellCrucible) {
                RecipeUtility.addRecipe(event, true, "hellCrucible", new ItemStack(HELL_CRUCIBLE),
                  "x x",
                  "x x",
                  "xxx",
                  'x', new ItemStack(COMPONENT, 1, ItemCraftingComponent.Types.HELLFORGE_BRICK.getMetadata())
                );
            }
        }
        if (enableMold) {
            RecipeUtility.addRecipe(event, true, "carving", new ResearchRecipe(new ItemStack(CARVING_TABLE), MetalcastingBookSection.NAME,
              "xzx",
              "x x",
              "xxx",
              'x', PLANK_WOOD,
              'z', BLANK_MOLD
            ));
            RecipeUtility.addRecipe(event, true, "mold", new ResearchRecipe(new ItemStack(MOLD), MetalcastingBookSection.NAME,
              "xxx",
              "xxx",
              'x', BRICK
            ));
            RecipeUtility.addRecipe(event, true, "blankMold", BLANK_MOLD, "xx", 'x', BRICK);
        }
    }

    @Override
    public void finish(Side side) {
        if (CrossMod.CRAFTTWEAKER) {
            CraftTweakerAPI.registerClass(CrucibleTweaker.class);
            CraftTweakerAPI.registerClass(CarvingTableTweaker.class);
        }

        if (enableCrucible) {
            BookPageRegistry.addCategoryToSection(CASTING_SECTION, 0,
              new BookCategory("category.Crucible.name",
                new BookEntry("research.Crucible.name",
                  new BookPageItem("research.Crucible.name", "research.Crucible.0", new ItemStack(CRUCIBLE)),
                  new BookPageText("research.Crucible.name", "research.Crucible.1"),
                  new BookPageCrafting("", "crucible"))));
            if (enableHellCrucible) {
                BookPageRegistry.addCategoryToSection(CASTING_SECTION, 1,
                  new BookCategory("category.HellCrucible.name",
                    new BookEntry("research.HellCrucible.name",
                      new BookPageItem("research.HellCrucible.name", "research.HellCrucible.0", new ItemStack(HELL_CRUCIBLE)),
                      new BookPageCrafting("", "hellCrucible"))));
            }
        }
        if (enableMold) {
            BookPageRegistry.addCategoryToSection(CASTING_SECTION, 2,
              new BookCategory("category.Mold.name",
                new BookEntry("research.Mold.name",
                  new BookPageItem("research.Mold.name", "research.Mold.0", new ItemStack(MOLD)),
                  new BookPageText("research.Mold.name", "research.Mold.1"),
                  new BookPageCrafting("", "mold"))));
            BookPageRegistry.addCategoryToSection(CASTING_SECTION, 3,
              new BookCategory("category.Molds.name",
                new BookEntry("research.Molds.name",
                  new BookPageItem("research.Molds.name", "research.Molds.0",
                    ItemMold.Type.THIN_PLATE.createItemStack(MOLD_ITEM),
                    ItemMold.Type.INGOT.createItemStack(MOLD_ITEM),
                    ItemMold.Type.NUGGET.createItemStack(MOLD_ITEM),
                    ItemMold.Type.PIPE.createItemStack(MOLD_ITEM)),
                  new BookPageCrafting("", "blankMold"),
                  new BookPageCrafting("", "carving"))));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(ModelRegistryEvent event) {
        registerModel(CRUCIBLE);
        registerModel(HELL_CRUCIBLE);
        registerModel(CARVING_TABLE);
        registerModel(MOLD);
        registerModel(BLANK_MOLD);
        registerModelItemStack(ItemMold.Type.INGOT.createItemStack(MOLD_ITEM));
        registerModelItemStack(ItemMold.Type.NUGGET.createItemStack(MOLD_ITEM));
        registerModelItemStack(ItemMold.Type.THIN_PLATE.createItemStack(MOLD_ITEM));
        registerModelItemStack(ItemMold.Type.PIPE.createItemStack(MOLD_ITEM));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initClient() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrucible.class, new TileEntityCrucibleRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMold.class, new TileEntityMoldRenderer());
    }

    @Override
    public void loadConfigurationOptions(Configuration config) {
        metalcastingHutLimit = config.get(CATEGORY_WORLD_GENERATION, "Maximum number of Metalcasting Huts allowed to generate per village", 1).getInt();
        metalcastingHutWeight = config.get(CATEGORY_WORLD_GENERATION, "Metalcasting Hut spawn weight", 5).getInt();
        enableCrucible = config.get(CATEGORY_BLOCKS, "Enable Crucible", true).getBoolean();
        enableHellCrucible = config.get(CATEGORY_BLOCKS, "Enable Nether Crucible", true).getBoolean();
        enableMold = config.get(CATEGORY_BLOCKS, "Enable Mold block", true).getBoolean();
    }
}
