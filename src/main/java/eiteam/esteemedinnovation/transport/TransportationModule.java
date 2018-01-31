package eiteam.esteemedinnovation.transport;

import eiteam.esteemedinnovation.api.Constants;
import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.api.crucible.CrucibleRegistry;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import eiteam.esteemedinnovation.transport.block.BlockPlonker;
import eiteam.esteemedinnovation.transport.block.TileEntityPlonker;
import eiteam.esteemedinnovation.transport.entity.*;
import eiteam.esteemedinnovation.transport.fluid.funnel.BlockFunnel;
import eiteam.esteemedinnovation.transport.fluid.funnel.TileEntityFunnel;
import eiteam.esteemedinnovation.transport.fluid.pipes.BlockColdFluidPipe;
import eiteam.esteemedinnovation.transport.fluid.pipes.TileEntityColdFluidPipe;
import eiteam.esteemedinnovation.transport.fluid.screw.BlockPump;
import eiteam.esteemedinnovation.transport.fluid.screw.TileEntityPump;
import eiteam.esteemedinnovation.transport.fluid.screw.TileEntityPumpRenderer;
import eiteam.esteemedinnovation.transport.item.*;
import eiteam.esteemedinnovation.transport.steam.*;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static eiteam.esteemedinnovation.commons.EsteemedInnovation.*;
import static eiteam.esteemedinnovation.commons.OreDictEntries.*;
import static eiteam.esteemedinnovation.materials.MaterialsModule.COPPER_LIQUID;
import static eiteam.esteemedinnovation.misc.ItemCraftingComponent.Types.BRASS_PISTON;
import static eiteam.esteemedinnovation.misc.ItemCraftingComponent.Types.BRASS_TURBINE;
import static eiteam.esteemedinnovation.misc.MiscellaneousModule.COMPONENT;
import static eiteam.esteemedinnovation.woodcone.WoodenConeModule.WOODEN_CONE;
import static net.minecraft.init.Blocks.IRON_BARS;

public class TransportationModule extends ContentModule {
    public static Block ARCHIMEDES_SCREW;
    public static Block VACUUM;
    public static Block FAN;
    public static Block PLONKER;
    public static Block ITEM_MORTAR;
    public static Item ASTROLABE;
    public static Block FUNNEL;
    public static Block BRASS_PIPE;
    public static Block COPPER_PIPE;
    public static Block VALVE_PIPE;

    @Override
    public void create(Side side) {
        channel.registerMessage(ConnectPacketHandler.class, ConnectPacket.class, 2, Side.SERVER);

        EntityRegistry.registerModEntity(new ResourceLocation(Constants.EI_MODID, "MortarItem"), EntityMortarItem.class, "MortarItem", 1, instance, 64, 20, true);
    }

    @Override
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        ARCHIMEDES_SCREW = setup(event, new BlockPump(), "pump");
        FAN = setup(event, new BlockFan(), "fan");
        VACUUM = setup(event, new BlockVacuum(), "vacuum");
        PLONKER = setup(event, new BlockPlonker(), "plonker");
        ITEM_MORTAR = setup(event, new BlockItemMortar(), "item_mortar");
        FUNNEL = setup(event, new BlockFunnel(), "funnel");
        BRASS_PIPE = setup(event, new BlockSteamPipe(), "brass_pipe");
        COPPER_PIPE = setup(event, new BlockColdFluidPipe(), "copper_pipe");
        VALVE_PIPE = setup(event, new BlockValvePipe(), "valve_pipe");

        registerTileEntity(TileEntitySteamPipe.class, "brass_pipe");
        registerTileEntity(TileEntityColdFluidPipe.class, "copper_pipe");
        registerTileEntity(TileEntityValvePipe.class, "valvePipe");
        registerTileEntity(TileEntityItemMortar.class, "itemMortar");
        registerTileEntity(TileEntityPump.class, "pump");
        registerTileEntity(TileEntityFan.class, "fan");
        registerTileEntity(TileEntityVacuum.class, "vacuum");
        registerTileEntity(TileEntityFunnel.class, "funnel");
        registerTileEntity(TileEntityPlonker.class, "plonker");
    }

    @Override
    public void registerItems(RegistryEvent.Register<Item> event) {
        setupItemBlock(event, ARCHIMEDES_SCREW);
        setupItemBlock(event, FAN);
        setupItemBlock(event, VACUUM);
        setupItemBlock(event, PLONKER);
        setupItemBlock(event, ITEM_MORTAR);
        setupItemBlock(event, FUNNEL);
        setupItemBlock(event, BRASS_PIPE);
        setupItemBlock(event, COPPER_PIPE);
        setupItemBlock(event, VALVE_PIPE);

        ASTROLABE = setup(event, new ItemAstrolabe(), "astrolabe");
    }

    @Override
    public void recipes(Side side) {
        //TODO: transfer recipes to json
        /*if (Config.enablePump) {
            BookRecipeRegistry.addRecipe("pump1", new ShapedOreRecipe(ARCHIMEDES_SCREW,
              "gng",
              "iii",
              "ngn",
              'i', PLATE_THIN_BRASS,
              'n', NUGGET_BRASS,
              'g', PANE_GLASS_COLORLESS
            ));
            BookRecipeRegistry.addRecipe("pump2", new ShapedOreRecipe(ARCHIMEDES_SCREW,
              "gng",
              "iii",
              "ngn",
              'i', INGOT_BRASS,
              'n', NUGGET_BRASS,
              'g', PANE_GLASS_COLORLESS
            ));
        }
        if (Config.enableFan) {
            BookRecipeRegistry.addRecipe("fan1", new ShapedOreRecipe(FAN,
              "xxx",
              "btb",
              "xxx",
              'x', INGOT_BRASS,
              'b', IRON_BARS,
              't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
            ));
            BookRecipeRegistry.addRecipe("fan2", new ShapedOreRecipe(FAN,
              "xxx",
              "btb",
              "xxx",
              'x', PLATE_THIN_BRASS,
              'b', IRON_BARS,
              't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
            ));
            if (Config.enableVacuum) {
                BookRecipeRegistry.addRecipe("vacuum1", new ShapedOreRecipe(VACUUM,
                  " x ",
                  "pfx",
                  " x ",
                  'x', INGOT_BRASS,
                  'p', BRASS_PIPE,
                  'f', FAN
                ));
                BookRecipeRegistry.addRecipe("vacuum2", new ShapedOreRecipe(VACUUM,
                  " x ",
                  "pfx",
                  " x ",
                  'x', PLATE_THIN_BRASS,
                  'p', BRASS_PIPE,
                  'f', FAN
                ));
            }
        }
        if (Config.enablePlonker) {
            BookRecipeRegistry.addRecipe("plonker", new ShapedOreRecipe(PLONKER,
              "ccb",
              "c r",
              "ccb",
              'c', COBBLESTONE_ORE,
              'b', PLATE_THIN_BRASS,
              'r', DUST_REDSTONE
            ));
        }
        if (Config.enableMortar) {
            BookRecipeRegistry.addRecipe("itemMortar1", new ShapedOreRecipe(ITEM_MORTAR,
              "p p",
              "pbp",
              "ccc",
              'p', PLATE_THIN_BRASS,
              'c', PLATE_THIN_COPPER,
              'b', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()
            )));
            BookRecipeRegistry.addRecipe("itemMortar2", new ShapedOreRecipe(ITEM_MORTAR,
              "p p",
              "pbp",
              "ccc",
              'p', INGOT_BRASS,
              'c', PLATE_THIN_COPPER,
              'b', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()
            )));
            BookRecipeRegistry.addRecipe("itemMortar3", new ShapedOreRecipe(ITEM_MORTAR,
              "p p",
              "pbp",
              "ccc",
              'p', PLATE_THIN_BRASS,
              'c', INGOT_COPPER,
              'b', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()
            )));
            BookRecipeRegistry.addRecipe("itemMortar4", new ShapedOreRecipe(ITEM_MORTAR,
              "p p",
              "pbp",
              "ccc",
              'p', INGOT_BRASS,
              'c', INGOT_COPPER,
              'b', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()
            )));
        }
        if (Config.enableAstrolabe) {
            BookRecipeRegistry.addRecipe("astrolabe", new ShapedOreRecipe(ASTROLABE,
              " x ",
              "xrx",
              " x ",
              'x', INGOT_BRASS,
              'r', DUST_REDSTONE
            ));
        }
        if (Config.enableFunnel) {
            CrucibleRegistry.registerDunkRecipe(
              Item.getItemFromBlock(WOODEN_CONE), COPPER_LIQUID, 45, new ItemStack(FUNNEL));
        }
        */
    }

    @Override
    public void finish(Side side) {
        if (Config.enablePipe) {
            MinecraftForge.EVENT_BUS.register(BRASS_PIPE);
        }

        BookPageRegistry.addCategoryToSection(BASICS_SECTION, 4,
          new BookCategory("category.Camouflage.name",
            new BookEntry("research.Camouflage.name",
              new BookPageItem("research.Camouflage.name", "research.Camouflage.0",
                new ItemStack(BRASS_PIPE),
                new ItemStack(Blocks.STONEBRICK)),
              new BookPageText("research.Camouflage.name", "research.Camouflage.1"))));

        BookPageRegistry.addCategoryToSection(STEAMPOWER_SECTION, 1,
          new BookCategory("category.Pipe.name",
            new BookEntry("research.Pipe.name",
              new BookPageItem("research.Pipe.name", "research.Pipe.0", new ItemStack(BRASS_PIPE), new ItemStack(VALVE_PIPE)),
              new BookPageText("research.Pipe.name", "research.Pipe.1"))));

        if (Config.enableMortar && Config.enableAstrolabe) {
            BookPageRegistry.addCategoryToSection(STEAMPOWER_SECTION, 9,
              new BookCategory("category.ItemMortar.name",
                new BookEntry("research.ItemMortar.name",
                  new BookPageItem("research.ItemMortar.name", "research.ItemMortar.0", new ItemStack(ITEM_MORTAR)),
                  new BookPageText("research.ItemMortar.name", "research.ItemMortar.1"),
                  new BookPageCrafting("", "astrolabe"),
                  new BookPageCrafting("", "itemMortar2", "itemMortar3"))));
        }

        if (Config.enablePump) {
            BookPageRegistry.addCategoryToSection(STEAMPOWER_SECTION, 11,
              new BookCategory("category.Screw.name",
                new BookEntry("research.Screw.name",
                  new BookPageItem("research.Screw.name", "research.Screw.0", new ItemStack(ARCHIMEDES_SCREW)),
                  new BookPageCrafting("", "pump1", "pump2"))));
        }

        if (Config.enableFan) {
            BookPageRegistry.addCategoryToSection(STEAMPOWER_SECTION, 14,
              new BookCategory("category.Fan.name",
                new BookEntry("research.Fan.name",
                  new BookPageItem("research.Fan.name", "research.Fan.0", new ItemStack(FAN)),
                  new BookPageCrafting("", "fan1", "fan2"))));
            if (Config.enableVacuum) {
                BookPageRegistry.addCategoryToSection(STEAMPOWER_SECTION, 15,
                  new BookCategory("category.Vacuum.name",
                    new BookEntry("research.Vacuum.name",
                      new BookPageItem("research.Vacuum.name", "research.Vacuum.0", new ItemStack(VACUUM)),
                      new BookPageCrafting("", "vacuum1", "vacuum2"))));
            }
        }

        if (Config.enableFunnel) {
            BookPageRegistry.addCategoryToSection(MISC_SECTION,
              new BookCategory("category.Funnel.name",
                new BookEntry("research.Funnel.name",
                  new BookPageItem("research.Funnel.name", "research.Funnel.0", true, new ItemStack(FUNNEL)),
                  new BookPageDip("", COPPER_LIQUID, 45, new ItemStack(WOODEN_CONE), new ItemStack(FUNNEL)))));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(ModelRegistryEvent event) {
        registerModel(BRASS_PIPE);
        ModelLoader.setCustomStateMapper(VALVE_PIPE, new StateMapperValvePipe());
        registerModel(VALVE_PIPE);
        registerModel(FAN);
        registerModel(VACUUM);
        registerModel(ITEM_MORTAR);
        registerModel(FUNNEL);
        registerModel(ASTROLABE);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initClient() {
        RenderingRegistry.registerEntityRenderingHandler(EntityMortarItem.class, RenderMortarItem::new);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityItemMortar.class, new TileEntityItemMortarRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPump.class, new TileEntityPumpRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityValvePipe.class, new TileEntityValvePipeRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFan.class, new TileEntityFanRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityVacuum.class, new TileEntityVacuumRenderer());
    }
}
