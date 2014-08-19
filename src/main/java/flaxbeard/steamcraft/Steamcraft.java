package flaxbeard.steamcraft;



import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
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
import flaxbeard.steamcraft.gui.SteamcraftGuiHandler;
import flaxbeard.steamcraft.handler.MechHandler;
import flaxbeard.steamcraft.handler.SteamcraftEventHandler;
import flaxbeard.steamcraft.handler.SteamcraftTickHandler;
import flaxbeard.steamcraft.integration.BloodMagicIntegration;
import flaxbeard.steamcraft.integration.BotaniaIntegration;
import flaxbeard.steamcraft.integration.EnderIOIntegration;
import flaxbeard.steamcraft.integration.ThaumcraftIntegration;
import flaxbeard.steamcraft.integration.ThermalFoundationIntegration;
import flaxbeard.steamcraft.integration.TwilightForestIntegration;
import flaxbeard.steamcraft.item.ItemSmashedOre;
import flaxbeard.steamcraft.misc.SteamcraftPotion;
import flaxbeard.steamcraft.tile.TileEntityBoiler;
import flaxbeard.steamcraft.tile.TileEntityChargingPad;
import flaxbeard.steamcraft.tile.TileEntityCreativeTank;
import flaxbeard.steamcraft.tile.TileEntityCrucible;
import flaxbeard.steamcraft.tile.TileEntityEngineeringTable;
import flaxbeard.steamcraft.tile.TileEntityFan;
import flaxbeard.steamcraft.tile.TileEntityFlashBoiler;
import flaxbeard.steamcraft.tile.TileEntityFluidSteamConverter;
import flaxbeard.steamcraft.tile.TileEntityItemMortar;
import flaxbeard.steamcraft.tile.TileEntityMold;
import flaxbeard.steamcraft.tile.TileEntityPump;
import flaxbeard.steamcraft.tile.TileEntityRuptureDisc;
import flaxbeard.steamcraft.tile.TileEntitySmasher;
import flaxbeard.steamcraft.tile.TileEntitySteamCharger;
import flaxbeard.steamcraft.tile.TileEntitySteamFurnace;
import flaxbeard.steamcraft.tile.TileEntitySteamGauge;
import flaxbeard.steamcraft.tile.TileEntitySteamHammer;
import flaxbeard.steamcraft.tile.TileEntitySteamHeater;
import flaxbeard.steamcraft.tile.TileEntitySteamPipe;
import flaxbeard.steamcraft.tile.TileEntitySteamPiston;
import flaxbeard.steamcraft.tile.TileEntitySteamTank;
import flaxbeard.steamcraft.tile.TileEntityThumper;
import flaxbeard.steamcraft.tile.TileEntityVacuum;
import flaxbeard.steamcraft.tile.TileEntityValvePipe;
import flaxbeard.steamcraft.tile.TileEntityWhistle;
import flaxbeard.steamcraft.world.ComponentSteamWorkshop;
import flaxbeard.steamcraft.world.PoorOreGeneratorZinc;
import flaxbeard.steamcraft.world.SteamWorkshopCreationHandler;
import flaxbeard.steamcraft.world.SteamcraftOreGen;
import flaxbeard.steamcraft.world.SteampunkVillagerTradeHandler;

@Mod(modid = "Steamcraft", name = "Flaxbeard's Steam Power", version = Config.VERSION)
public class Steamcraft {
	
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

    public static boolean steamRegistered;
    public static Potion semiInvisible;
    public static final OreGenEvent.GenerateMinable.EventType EVENT_TYPE = (OreGenEvent.GenerateMinable.EventType)EnumHelper.addEnum(OreGenEvent.GenerateMinable.EventType.class, "FSP_POOR_ZINC", new Class[0], new Object[0]);
	@SidedProxy(clientSide = "flaxbeard.steamcraft.client.ClientProxy", serverSide = "flaxbeard.steamcraft.common.CommonProxy")
	public static CommonProxy proxy;



	 
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		Config.load(event);
		
		tab = new SCTab(CreativeTabs.getNextID(), "steamcraft", false);
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

 //  EntityRegistry.registerModEntity(EntitySteamHorse.class, "SteamHorse", 2, Steamcraft.instance, 64, 1, true);
	    //EntityList.addMapping(EntitySteamHorse.class, "SteamHorse", 5, 0xE2B23E, 0xB38E2F);

		GameRegistry.registerTileEntity(TileEntityCrucible.class, "steamcraftCrucible");
		GameRegistry.registerTileEntity(TileEntityMold.class, "mold");
		GameRegistry.registerTileEntity(TileEntityBoiler.class, "boiler");
		GameRegistry.registerTileEntity(TileEntitySteamPipe.class, "pipe");
		GameRegistry.registerTileEntity(TileEntityValvePipe.class, "valvePipe");

		GameRegistry.registerTileEntity(TileEntitySteamPiston.class, "steamPiston");
		GameRegistry.registerTileEntity(TileEntitySteamHeater.class, "heater");
		GameRegistry.registerTileEntity(TileEntityEngineeringTable.class, "engineeringTable");
		GameRegistry.registerTileEntity(TileEntitySteamCharger.class, "steamCharger");
		GameRegistry.registerTileEntity(TileEntitySteamTank.class, "steamTank");
		GameRegistry.registerTileEntity(TileEntitySteamGauge.class, "steamGauge");
		GameRegistry.registerTileEntity(TileEntityRuptureDisc.class, "ruptureDisc");
		GameRegistry.registerTileEntity(TileEntityCreativeTank.class, "creativeSteamTank");
		GameRegistry.registerTileEntity(TileEntitySteamHammer.class, "steamHammer");
		GameRegistry.registerTileEntity(TileEntityItemMortar.class, "itemMortar");
		GameRegistry.registerTileEntity(TileEntitySteamFurnace.class, "steamFurnace");
		GameRegistry.registerTileEntity(TileEntityPump.class, "pump");
		GameRegistry.registerTileEntity(TileEntityThumper.class, "thumper");
		GameRegistry.registerTileEntity(TileEntitySmasher.class, "smasher");
		GameRegistry.registerTileEntity(TileEntityDummyBlock.class, "dummy");
		GameRegistry.registerTileEntity(TileEntityFlashBoiler.class, "flashBoiler");
		GameRegistry.registerTileEntity(TileEntityFan.class, "fan");
		GameRegistry.registerTileEntity(TileEntityVacuum.class, "vacuum");
		GameRegistry.registerTileEntity(TileEntityFluidSteamConverter.class, "fluidSteamConverter");
		GameRegistry.registerTileEntity(TileEntityWhistle.class, "whistle");
		GameRegistry.registerTileEntity(TileEntityChargingPad.class, "chargingPad");


	}
	
	@EventHandler
	public void load(FMLInitializationEvent event) {
		channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("steamcraft");
		semiInvisible = (new SteamcraftPotion(Config.potionId, false, 0)).setIconIndex(0, 1).setPotionName("potion.partialInvisible");
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new SteamcraftGuiHandler());
		
		MinecraftForge.EVENT_BUS.register(new SteamcraftEventHandler());
		MinecraftForge.EVENT_BUS.register(new MechHandler());
		
		FMLCommonHandler.instance().bus().register(new SteamcraftTickHandler());
		
		tubeRenderID = RenderingRegistry.getNextAvailableRenderId();
		heaterRenderID = RenderingRegistry.getNextAvailableRenderId();
		chargerRenderID = RenderingRegistry.getNextAvailableRenderId();
		genocideRenderID  = RenderingRegistry.getNextAvailableRenderId();
		gaugeRenderID  = RenderingRegistry.getNextAvailableRenderId();
		ruptureDiscRenderID  = RenderingRegistry.getNextAvailableRenderId();
		whistleRenderID  = RenderingRegistry.getNextAvailableRenderId();
		boilerRenderID  = RenderingRegistry.getNextAvailableRenderId();


		proxy.registerRenderers();
		proxy.registerHotkeys();
		SteamcraftRecipes.registerRecipes();

		if (Loader.isModLoaded("Railcraft") && Config.genPoorOre) {
			MinecraftForge.ORE_GEN_BUS.register(new PoorOreGeneratorZinc(EVENT_TYPE,8, 70, 3, 29));
		}
	}
	
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		if (Config.enablePipe){
			MinecraftForge.EVENT_BUS.register(SteamcraftBlocks.pipe);
		}
		steamRegistered = FluidRegistry.isFluidRegistered("steam");
		if (steamRegistered && Config.enableFluidSteamConverter) {
			SteamcraftBlocks.fluidSteamConverter.setCreativeTab(Steamcraft.tab);
		}
		if (Loader.isModLoaded("Thaumcraft")) {
			ThaumcraftIntegration.addThaumiumLiquid();
		}
		if (Loader.isModLoaded("Botania")) {
			BotaniaIntegration.addBotaniaLiquid();
		}
		if (Loader.isModLoaded("TwilightForest")) {
			TwilightForestIntegration.addTwilightForestLiquid();
		}
		if (Loader.isModLoaded("AWWayofTime")) {
			BloodMagicIntegration.addBloodMagicStuff();
		}
		if (Loader.isModLoaded("EnderIO")) {
			EnderIOIntegration.addEIOLiquid();
		}
		if (Loader.isModLoaded("ThermalFoundation")) {
			ThermalFoundationIntegration.addThermalFoundationLiquid();
		}
		if (OreDictionary.getOres("ingotLead").size() > 0) {
			CrucibleLiquid liquidLead = new CrucibleLiquid("lead", OreDictionary.getOres("ingotLead").get(0), new ItemStack(SteamcraftItems.steamcraftPlate,1,9), OreDictionary.getOres("nuggetLead").size() > 0 ? OreDictionary.getOres("nuggetLead").get(0) : null, null,118,128,157);
			SteamcraftRegistry.liquids.add(liquidLead);
			
			
			SteamcraftRegistry.registerSmeltThingOredict("ingotLead", liquidLead, 9);
			SteamcraftRegistry.registerSmeltThingOredict("nuggetLead", liquidLead, 1);
			SteamcraftRegistry.registerSmeltThingOredict("plateSteamcraftLead", liquidLead, 6);
			SteamcraftRegistry.addExosuitPlate(new ExosuitPlate("Lead",new ItemStack(SteamcraftItems.exosuitPlate,1,11),"Lead","Lead","steamcraft.plate.lead"));
			SteamcraftRecipes.addExosuitPlateRecipes("exoLead","plateSteamcraftLead",new ItemStack(SteamcraftItems.exosuitPlate,1,11),liquidLead);
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
		boolean tt;
		public SCTab(int par1, String par2Str, boolean toolTab) {
			super(par1, par2Str);
			tt = toolTab;
		}
		
      
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			if (tt) {
				return SteamcraftItems.pick("Brass");
			}
			return SteamcraftItems.book;
		}	
	}
}