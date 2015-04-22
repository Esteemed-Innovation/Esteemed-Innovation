package flaxbeard.steamcraft;


import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.exosuit.ExosuitPlate;
import flaxbeard.steamcraft.api.util.SPLog;
import flaxbeard.steamcraft.block.TileEntityDummyBlock;
import flaxbeard.steamcraft.common.CommonProxy;
import flaxbeard.steamcraft.entity.EntityCanisterItem;
import flaxbeard.steamcraft.entity.EntityFloatingItem;
import flaxbeard.steamcraft.entity.EntityMortarItem;
import flaxbeard.steamcraft.entity.EntityRocket;
import flaxbeard.steamcraft.gui.SteamcraftGuiHandler;
import flaxbeard.steamcraft.handler.MechHandler;
import flaxbeard.steamcraft.handler.SteamcraftEventHandler;
import flaxbeard.steamcraft.handler.SteamcraftTickHandler;
import flaxbeard.steamcraft.integration.*;
import flaxbeard.steamcraft.integration.ic2.IndustrialCraftIntegration;
import flaxbeard.steamcraft.integration.natura.NaturaIntegration;
import flaxbeard.steamcraft.integration.thaumcraft.ThaumcraftIntegration;
import flaxbeard.steamcraft.item.ItemSmashedOre;
import flaxbeard.steamcraft.tile.*;
import flaxbeard.steamcraft.world.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

@Mod(modid = "Steamcraft", name = "Flaxbeard's Steam Power", version = Config.VERSION)
public class Steamcraft {

    public static final OreGenEvent.GenerateMinable.EventType EVENT_TYPE = (OreGenEvent.GenerateMinable.EventType) EnumHelper.addEnum(OreGenEvent.GenerateMinable.EventType.class, "FSP_POOR_ZINC", new Class[0], new Object[0]);
    @Instance("Steamcraft")
    public static Steamcraft instance;

    public static SPLog log = SPLog.getInstance().setLogLevel(SPLog.NONE);

    public static FMLEventChannel channel;

    public static EnumRarity upgrade;

    public static CreativeTabs tab;
    public static CreativeTabs tabTools;

    public static int tubeRenderID;
    public static int heaterRenderID;
    public static int chargerRenderID;
    public static int genocideRenderID;
    public static int gaugeRenderID;
    public static int ruptureDiscRenderID;
    public static int whistleRenderID;
    public static int boilerRenderID;
    public static int customCraftingTableRenderID;
    public static int furnaceRenderID;
    public static int sawRenderID;
    public static int bloodBoilerRenderID;

    public static boolean steamRegistered;

    public static Potion semiInvisible;

    @SidedProxy(clientSide = "flaxbeard.steamcraft.client.ClientProxy", serverSide = "flaxbeard.steamcraft.common.CommonProxy")
    public static CommonProxy proxy;

    private static void registerTileEntity(Class<? extends TileEntity> clazz, String key) {
        GameRegistry.registerTileEntityWithAlternatives(clazz, "steamcraft:" + key);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        Config.load(event);

        tab = new SCTab(CreativeTabs.getNextID(), "steamcraft", false).setBackgroundImageName("item_search.png");
        tabTools = new SCTab(CreativeTabs.getNextID(), "steamcraftTools", true);

        upgrade = EnumHelper.addRarity("UPGRADE", EnumChatFormatting.RED, "Upgrade");
        SteamcraftBlocks.registerBlocks();
        SteamcraftItems.registerItems();

        GameRegistry.registerWorldGenerator(new SteamcraftOreGen(), 1);

        int id = Config.villagerId;
        VillagerRegistry.instance().registerVillagerId(id);
        VillagerRegistry.instance().registerVillageTradeHandler(id, new SteampunkVillagerTradeHandler());
        VillagerRegistry.instance().registerVillageCreationHandler(new SteamWorkshopCreationHandler());
        MapGenStructureIO.func_143031_a(ComponentSteamWorkshop.class, "steamcraft:workshop");
        EntityRegistry.registerModEntity(EntityFloatingItem.class, "FloatingItem", 0, Steamcraft.instance, 64, 20, true);
        EntityRegistry.registerModEntity(EntityMortarItem.class, "MortarItem", 1, Steamcraft.instance, 64, 20, true);
        EntityRegistry.registerModEntity(EntityCanisterItem.class, "CanisterItem", 2, Steamcraft.instance, 64, 20, true);
        EntityRegistry.registerModEntity(EntityRocket.class, "Rocket", 3, Steamcraft.instance, 64, 20, true);
        // EntityRegistry.registerModEntity(EntityHarpoon.class, "Harpoon", 4, Steamcraft.instance, 64, 20, true);

        //  EntityRegistry.registerModEntity(EntitySteamHorse.class, "SteamHorse", 2, Steamcraft.instance, 64, 1, true);
        //EntityList.addMapping(EntitySteamHorse.class, "SteamHorse", 5, 0xE2B23E, 0xB38E2F);

        registerTileEntity(TileEntityCrucible.class, "steamcraftCrucible");
        registerTileEntity(TileEntityMold.class, "mold");
        registerTileEntity(TileEntityBoiler.class, "boiler");
        registerTileEntity(TileEntitySteamPipe.class, "pipe");
        registerTileEntity(TileEntityValvePipe.class, "valvePipe");

        registerTileEntity(TileEntitySteamPiston.class, "steamPiston");
        registerTileEntity(TileEntitySteamHeater.class, "heater");
        registerTileEntity(TileEntityEngineeringTable.class, "engineeringTable");
        registerTileEntity(TileEntitySteamCharger.class, "steamCharger");
        registerTileEntity(TileEntitySteamTank.class, "steamTank");
        registerTileEntity(TileEntitySteamGauge.class, "steamGauge");
        registerTileEntity(TileEntityRuptureDisc.class, "ruptureDisc");
        registerTileEntity(TileEntityCreativeTank.class, "creativeSteamTank");
        registerTileEntity(TileEntitySteamHammer.class, "steamHammer");
        registerTileEntity(TileEntityItemMortar.class, "itemMortar");
        registerTileEntity(TileEntitySteamFurnace.class, "steamFurnace");
        registerTileEntity(TileEntityPump.class, "pump");
        registerTileEntity(TileEntityThumper.class, "thumper");
        registerTileEntity(TileEntitySmasher.class, "smasher");
        registerTileEntity(TileEntityDummyBlock.class, "dummy");
        registerTileEntity(TileEntityFlashBoiler.class, "flashBoiler");
        registerTileEntity(TileEntityFan.class, "fan");
        registerTileEntity(TileEntityVacuum.class, "vacuum");
        registerTileEntity(TileEntityFluidSteamConverter.class, "fluidSteamConverter");
        registerTileEntity(TileEntityWhistle.class, "whistle");
        registerTileEntity(TileEntityChargingPad.class, "chargingPad");
        registerTileEntity(TileEntityBlockPlacer.class, "blockPlacer");
        registerTileEntity(TileEntitySaw.class, "buzzsaw");
        registerTileEntity(TileEntitySteamDistributor.class, "distributor");
        registerTileEntity(TileEntityCustomCraftingTable.class, "customCraftingTable");
        registerTileEntity(TileEntityCustomFurnace.class, "customFurnace");

    }

    @EventHandler
    public void load(FMLInitializationEvent event) {
        channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("steamcraft");
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new SteamcraftGuiHandler());

        MinecraftForge.EVENT_BUS.register(new SteamcraftEventHandler());
        MinecraftForge.EVENT_BUS.register(new MechHandler());

        FMLCommonHandler.instance().bus().register(new SteamcraftTickHandler());

        tubeRenderID = RenderingRegistry.getNextAvailableRenderId();
        heaterRenderID = RenderingRegistry.getNextAvailableRenderId();
        chargerRenderID = RenderingRegistry.getNextAvailableRenderId();
        genocideRenderID = RenderingRegistry.getNextAvailableRenderId();
        gaugeRenderID = RenderingRegistry.getNextAvailableRenderId();
        ruptureDiscRenderID = RenderingRegistry.getNextAvailableRenderId();
        whistleRenderID = RenderingRegistry.getNextAvailableRenderId();
        boilerRenderID = RenderingRegistry.getNextAvailableRenderId();
        customCraftingTableRenderID = RenderingRegistry.getNextAvailableRenderId();
        furnaceRenderID = RenderingRegistry.getNextAvailableRenderId();
        //sawRenderID = RenderingRegistry.getNextAvailableRenderId();
        //bloodBoilerRenderID = RenderingRegistry.getNextAvailableRenderId();


        proxy.registerRenderers();
        proxy.registerHotkeys();
        SteamcraftRecipes.registerRecipes();

        if (Loader.isModLoaded("Railcraft") && Config.genPoorOre) {
            MinecraftForge.ORE_GEN_BUS.register(new PoorOreGeneratorZinc(EVENT_TYPE, 8, 70, 3, 29));
        }
    }


    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        SteamcraftRecipes.registerCasting();

        if (Config.enablePipe) {
            MinecraftForge.EVENT_BUS.register(SteamcraftBlocks.pipe);
        }
        steamRegistered = FluidRegistry.isFluidRegistered("steam");
        if (steamRegistered && Config.enableFluidSteamConverter) {
            SteamcraftBlocks.fluidSteamConverter.setCreativeTab(Steamcraft.tab);
        }
        if (Loader.isModLoaded("Thaumcraft") && Config.enableThaumcraftIntegration) {
            ThaumcraftIntegration.grabItems();
            ThaumcraftIntegration.addThaumiumLiquid();
        }
        if (Loader.isModLoaded("Botania") && Config.enableBotaniaIntegration) {
            BotaniaIntegration.grabItems();
            BotaniaIntegration.addBotaniaLiquid();
        }
        if (Loader.isModLoaded("Enchridion") && Config.enableEnchiridionIntegration) {
            EnchiridionIntegration.grabClass();
        }
        if (Loader.isModLoaded("TwilightForest") && Config.enableTwilightForestIntegration) {
            TwilightForestIntegration.grabItems();
            TwilightForestIntegration.addTwilightForestLiquid();
        }
        if (Loader.isModLoaded("AWWayofTime") && Config.enableBloodMagicIntegration) {
            BloodMagicIntegration.grabItems();
            BloodMagicIntegration.addBloodMagicStuff();
        }
        if (Loader.isModLoaded("EnderIO") && Config.enableEnderIOIntegration) {
            EnderIOIntegration.grabItems();
            EnderIOIntegration.addEIOLiquid();
        }
        if (Loader.isModLoaded("ThermalFoundation") && Config.enableThermalFoundationIntegration) {
            ThermalFoundationIntegration.grabItems();
            ThermalFoundationIntegration.addThermalFoundationLiquid();
        }
        if(Loader.isModLoaded("IC2") && Config.enableIC2Integration){
            IndustrialCraftIntegration.addIC2Recipes();
        }
        if(Loader.isModLoaded("Natura") && Config.enableNaturaIntegration){
        	NaturaIntegration.grabItems();
        	NaturaIntegration.registerStuff();
        }
        if (OreDictionary.getOres("ingotLead").size() > 0) {
            CrucibleLiquid liquidLead = new CrucibleLiquid("lead", OreDictionary.getOres("ingotLead").get(0), new ItemStack(SteamcraftItems.steamcraftPlate, 1, 9), OreDictionary.getOres("nuggetLead").size() > 0 ? OreDictionary.getOres("nuggetLead").get(0) : null, null, 118, 128, 157);
            SteamcraftRegistry.liquids.add(liquidLead);

            SteamcraftRegistry.registerSmeltThingOredict("ingotLead", liquidLead, 9);
            SteamcraftRegistry.registerSmeltThingOredict("nuggetLead", liquidLead, 1);
            SteamcraftRegistry.registerSmeltThingOredict("plateSteamcraftLead", liquidLead, 6);
            SteamcraftRegistry.addExosuitPlate(new ExosuitPlate("Lead", new ItemStack(SteamcraftItems.exosuitPlate, 1, 11), "Lead", "Lead", "steamcraft.plate.lead"));
            SteamcraftRecipes.addExosuitPlateRecipes("exoLead", "plateSteamcraftLead", new ItemStack(SteamcraftItems.exosuitPlate, 1, 11), liquidLead);
        }

        SteamcraftBook.registerBookResearch();
        ItemSmashedOre iso = (ItemSmashedOre) SteamcraftItems.smashedOre;
        iso.registerDusts();
        iso.addSmelting();
        iso.registerDusts();
        SteamcraftItems.reregisterPlates();
        SteamcraftRecipes.registerDustLiquids();
    }


    private class SCTab extends CreativeTabs {
        boolean isToolTab;

        public SCTab(int par1, String par2Str, boolean toolTab) {
            super(par1, par2Str);
            isToolTab = toolTab;
        }


        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            if (isToolTab)
                return SteamcraftItems.pick("Brass");
            return SteamcraftItems.book;
        }

        @Override
        public boolean hasSearchBar() {
            return !isToolTab;
        }
    }
}
