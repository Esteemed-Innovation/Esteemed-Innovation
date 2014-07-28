package flaxbeard.steamcraft;



import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.fluids.FluidRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.block.TileEntityDummyBlock;
import flaxbeard.steamcraft.common.CommonProxy;
import flaxbeard.steamcraft.entity.EntityFloatingItem;
import flaxbeard.steamcraft.entity.EntityMortarItem;
import flaxbeard.steamcraft.gui.SteamcraftGuiHandler;
import flaxbeard.steamcraft.handler.MechHandler;
import flaxbeard.steamcraft.handler.SteamcraftEventHandler;
import flaxbeard.steamcraft.handler.SteamcraftTickHandler;
import flaxbeard.steamcraft.integration.BotaniaIntegration;
import flaxbeard.steamcraft.integration.ThaumcraftIntegration;
import flaxbeard.steamcraft.item.ItemSmashedOre;
import flaxbeard.steamcraft.tile.TileEntityBoiler;
import flaxbeard.steamcraft.tile.TileEntityCreativeTank;
import flaxbeard.steamcraft.tile.TileEntityCrucible;
import flaxbeard.steamcraft.tile.TileEntityEngineeringTable;
import flaxbeard.steamcraft.tile.TileEntityFlashBoiler;
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
import flaxbeard.steamcraft.tile.TileEntityValvePipe;
import flaxbeard.steamcraft.world.PoorOreGeneratorZinc;
import flaxbeard.steamcraft.world.SteamcraftOreGen;

@Mod(modid = "Steamcraft", name = "Flaxbeard's Steam Power", version = Config.VERSION, dependencies="after:EnderIO;after:Mekanism;after:TConstruct;after:IC2;after:ThermalExpansion")
public class Steamcraft {
	
    @Instance("Steamcraft")
    public static Steamcraft instance;
    
    public static FMLEventChannel channel;
    
    
    public static CreativeTabs tab;
    public static CreativeTabs tabTools;

    public static int tubeRenderID;
    public static int heaterRenderID;
	public static int chargerRenderID;
	public static int genocideRenderID;
	public static int gaugeRenderID;
	public static int ruptureDiscRenderID;

    public static boolean steamRegistered;
    public static final OreGenEvent.GenerateMinable.EventType EVENT_TYPE = (OreGenEvent.GenerateMinable.EventType)EnumHelper.addEnum(OreGenEvent.GenerateMinable.EventType.class, "FSP_POOR_ZINC", new Class[0], new Object[0]);
	@SidedProxy(clientSide = "flaxbeard.steamcraft.client.ClientProxy", serverSide = "flaxbeard.steamcraft.common.CommonProxy")
	public static CommonProxy proxy;


	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Config.load(event);
		
		tab = new SCTab(CreativeTabs.getNextID(), "steamcraft", false);
		tabTools = new SCTab(CreativeTabs.getNextID(), "steamcraftTools", true);
		
		SteamcraftBlocks.registerBlocks();
		SteamcraftItems.registerItems();
		
		GameRegistry.registerWorldGenerator(new SteamcraftOreGen(), 1);


	    EntityRegistry.registerModEntity(EntityFloatingItem.class, "FloatingItem", 0, Steamcraft.instance, 64, 20, true);
	    EntityRegistry.registerModEntity(EntityMortarItem.class, "MortarItem", 1, Steamcraft.instance, 64, 20, true);
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
		genocideRenderID  = RenderingRegistry.getNextAvailableRenderId();
		gaugeRenderID  = RenderingRegistry.getNextAvailableRenderId();
		ruptureDiscRenderID  = RenderingRegistry.getNextAvailableRenderId();


		proxy.registerRenderers();
		SteamcraftRecipes.registerRecipes();

      //  FMLInterModComms.sendMessage("Waila", "register", "flaxbeard.steamcraft.integration.waila.WailaIntegration.callbackRegister");
		if (Loader.isModLoaded("Railcraft") && Config.genPoorOre) {
			MinecraftForge.ORE_GEN_BUS.register(new PoorOreGeneratorZinc(EVENT_TYPE,8, 70, 3, 29));
		}
	}
	
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		steamRegistered = FluidRegistry.isFluidRegistered("steam");
		if (Loader.isModLoaded("Thaumcraft")) {
			ThaumcraftIntegration.addThaumiumLiquid();
		}
		if (Loader.isModLoaded("Botania")) {
			BotaniaIntegration.addItems();
		}
		SteamcraftBook.registerBookResearch();
		ItemSmashedOre iso = (ItemSmashedOre) SteamcraftItems.smashedOre; 
		iso.addSmelting();
		iso.registerDusts();
		SteamcraftItems.reregisterPlates();
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