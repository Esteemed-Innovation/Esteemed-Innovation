package flaxbeard.steamcraft;



import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.api.CrucibleFormula;
import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.common.CommonProxy;
import flaxbeard.steamcraft.gui.SteamcraftGuiHandler;
import flaxbeard.steamcraft.handler.MechHandler;
import flaxbeard.steamcraft.handler.SpyglassHandler;
import flaxbeard.steamcraft.handler.SteamcraftEventHandler;
import flaxbeard.steamcraft.tile.TileEntityBoiler;
import flaxbeard.steamcraft.tile.TileEntityCrucible;
import flaxbeard.steamcraft.tile.TileEntityMold;
import flaxbeard.steamcraft.tile.TileEntitySteamHeater;
import flaxbeard.steamcraft.tile.TileEntitySteamPipe;
import flaxbeard.steamcraft.tile.TileEntitySteamPiston;
import flaxbeard.steamcraft.world.SteamcraftOreGen;

@Mod(modid = "Steamcraft", name = "Professor Flaxbeard's Wonderous Steam Power Mod", version = "1.0.0")

public class Steamcraft {
	
    @Instance("Steamcraft")
    public static Steamcraft instance;
    
    public static FMLEventChannel channel;
    
    
    public static CreativeTabs tab;
    
    public static CrucibleLiquid liquidIron;
    public static CrucibleLiquid liquidZinc;
    public static CrucibleLiquid liquidCopper;
    public static CrucibleLiquid liquidGold;
    public static CrucibleLiquid liquidBrass;
	
    public static int tubeRenderID;
    public static int heaterRenderID;
    
	@SidedProxy(clientSide = "flaxbeard.steamcraft.client.ClientProxy", serverSide = "flaxbeard.steamcraft.common.CommonProxy")
	public static CommonProxy proxy;

	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		tab = new SCTab(CreativeTabs.getNextID(), "steamcraft");
		
		SteamcraftBlocks.registerBlocks();
		SteamcraftItems.registerItems();
		
		GameRegistry.registerWorldGenerator(new SteamcraftOreGen(), 1);

		GameRegistry.registerTileEntity(TileEntityCrucible.class, "steamcraftCrucible");
		GameRegistry.registerTileEntity(TileEntityMold.class, "mold");
		GameRegistry.registerTileEntity(TileEntityBoiler.class, "boiler");
		GameRegistry.registerTileEntity(TileEntitySteamPipe.class, "pipe");
		GameRegistry.registerTileEntity(TileEntitySteamPiston.class, "steamPiston");
		GameRegistry.registerTileEntity(TileEntitySteamHeater.class, "heater");
	}
	
	

	@EventHandler
	public void load(FMLInitializationEvent event) {
		
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new SteamcraftGuiHandler());
		
		MinecraftForge.EVENT_BUS.register(new SteamcraftEventHandler());
		MinecraftForge.EVENT_BUS.register(new MechHandler());
		
		FMLCommonHandler.instance().bus().register(new SpyglassHandler());
		
		tubeRenderID = RenderingRegistry.getNextAvailableRenderId();
		heaterRenderID = RenderingRegistry.getNextAvailableRenderId();
		
		proxy.registerRenderers();
		liquidIron = new CrucibleLiquid(new ItemStack(Items.iron_ingot), new ItemStack(SteamcraftItems.steamcraftPlate,1,2), new ItemStack(SteamcraftItems.steamcraftNugget,1,2), null,200,200,200);
		SteamcraftRegistry.liquids.add(liquidIron);
		liquidGold = new CrucibleLiquid(new ItemStack(Items.gold_ingot), new ItemStack(SteamcraftItems.steamcraftPlate,1,3), new ItemStack(Items.gold_nugget), null,220,157,11);
		SteamcraftRegistry.liquids.add(liquidGold);
		liquidZinc = new CrucibleLiquid(new ItemStack(SteamcraftItems.steamcraftIngot,1,1), new ItemStack(SteamcraftItems.steamcraftPlate,1,1), new ItemStack(SteamcraftItems.steamcraftNugget,1,1), null,225,225,225);
		SteamcraftRegistry.liquids.add(liquidZinc);
		liquidCopper = new CrucibleLiquid(new ItemStack(SteamcraftItems.steamcraftIngot,1,0), new ItemStack(SteamcraftItems.steamcraftPlate,1,0), new ItemStack(SteamcraftItems.steamcraftNugget,1,0), null,140,66,12);
		SteamcraftRegistry.liquids.add(liquidCopper);
		liquidBrass = new CrucibleLiquid(new ItemStack(SteamcraftItems.steamcraftIngot,1,2), new ItemStack(SteamcraftItems.steamcraftPlate,1,4), new ItemStack(SteamcraftItems.steamcraftNugget,1,3), new CrucibleFormula(liquidZinc, 1, liquidCopper, 3, 4),242,191,66);
		SteamcraftRegistry.liquids.add(liquidBrass);
		
		SteamcraftRegistry.registerSmeltThingOredict("ingotGold", liquidGold, 9);
		SteamcraftRegistry.registerSmeltThingOredict("ingotIron", liquidIron, 9);
		SteamcraftRegistry.registerSmeltThingOredict("ingotZinc", liquidZinc, 9);
		SteamcraftRegistry.registerSmeltThingOredict("ingotCopper", liquidCopper, 9);
		SteamcraftRegistry.registerSmeltThingOredict("ingotBrass", liquidBrass, 9);
		
		SteamcraftRegistry.registerSmeltThingOredict("plateGold", liquidGold, 9);
		SteamcraftRegistry.registerSmeltThingOredict("plateIron", liquidIron, 9);
		SteamcraftRegistry.registerSmeltThingOredict("plateZinc", liquidZinc, 9);
		SteamcraftRegistry.registerSmeltThingOredict("plateCopper", liquidCopper, 9);
		SteamcraftRegistry.registerSmeltThingOredict("plateBrass", liquidBrass, 9);
		
		SteamcraftRegistry.registerSmeltThingOredict("nuggetGold", liquidGold, 1);
		SteamcraftRegistry.registerSmeltThingOredict("nuggetIron", liquidIron, 1);
		SteamcraftRegistry.registerSmeltThingOredict("nuggetZinc", liquidZinc, 1);
		SteamcraftRegistry.registerSmeltThingOredict("nuggetCopper", liquidCopper, 1);
		SteamcraftRegistry.registerSmeltThingOredict("nuggetBrass", liquidBrass, 1);
		SteamcraftRegistry.registerSmeltThing(Items.gold_nugget, liquidGold, 1);
	}
	
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
	    
	}


	private class SCTab extends CreativeTabs {
		public SCTab(int par1, String par2Str) {
			super(par1, par2Str);
		}
		
      
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
		
			return Item.getItemFromBlock(SteamcraftBlocks.crucible);
		}	
	}
}