package flaxbeard.steamcraft;



import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
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
import flaxbeard.steamcraft.block.BlockBoiler;
import flaxbeard.steamcraft.block.BlockManyMetadataItem;
import flaxbeard.steamcraft.block.BlockMold;
import flaxbeard.steamcraft.block.BlockPipe;
import flaxbeard.steamcraft.block.BlockSteamPistonBase;
import flaxbeard.steamcraft.block.BlockSteamPistonExtension;
import flaxbeard.steamcraft.block.BlockSteamPistonMoving;
import flaxbeard.steamcraft.block.BlockSteamcraftCrucible;
import flaxbeard.steamcraft.block.BlockSteamcraftOre;
import flaxbeard.steamcraft.common.CommonProxy;
import flaxbeard.steamcraft.gui.SteamcraftGuiHandler;
import flaxbeard.steamcraft.handler.MechHandler;
import flaxbeard.steamcraft.handler.SpyglassHandler;
import flaxbeard.steamcraft.item.ItemFirearm;
import flaxbeard.steamcraft.item.ItemIngotMold;
import flaxbeard.steamcraft.item.ItemNuggetMold;
import flaxbeard.steamcraft.item.ItemPlateMold;
import flaxbeard.steamcraft.item.ItemSpyglass;
import flaxbeard.steamcraft.item.ItemSteamcraftIngot;
import flaxbeard.steamcraft.item.ItemSteamcraftNugget;
import flaxbeard.steamcraft.item.ItemSteamcraftPlate;
import flaxbeard.steamcraft.tile.TileEntityBoiler;
import flaxbeard.steamcraft.tile.TileEntityCrucible;
import flaxbeard.steamcraft.tile.TileEntityMold;
import flaxbeard.steamcraft.tile.TileEntitySteamHeater;
import flaxbeard.steamcraft.tile.TileEntitySteamPipe;
import flaxbeard.steamcraft.tile.TileEntitySteamPiston;
import flaxbeard.steamcraft.world.SteamcraftOreGen;

@Mod(modid = "Steamcraft", name = "Steamcraft", version = "1.0.0")

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
		
		MinecraftForge.EVENT_BUS.register(new MechHandler());
		
		FMLCommonHandler.instance().bus().register(new SpyglassHandler());
		
		tubeRenderID = RenderingRegistry.getNextAvailableRenderId();
		heaterRenderID = RenderingRegistry.getNextAvailableRenderId();
		
		proxy.registerRenderers();
		liquidIron = new CrucibleLiquid(new ItemStack(Items.iron_ingot), new ItemStack(SteamcraftItems.steamcraftPlate,1,2), new ItemStack(SteamcraftItems.steamcraftNugget,1,2), null,200,200,200);
		CrucibleLiquid.liquids.add(liquidIron);
		liquidGold = new CrucibleLiquid(new ItemStack(Items.gold_ingot), new ItemStack(SteamcraftItems.steamcraftPlate,1,3), new ItemStack(Items.gold_nugget), null,220,157,11);
		CrucibleLiquid.liquids.add(liquidGold);
		liquidZinc = new CrucibleLiquid(new ItemStack(SteamcraftItems.steamcraftIngot,1,1), new ItemStack(SteamcraftItems.steamcraftPlate,1,1), new ItemStack(SteamcraftItems.steamcraftNugget,1,1), null,225,225,225);
		CrucibleLiquid.liquids.add(liquidZinc);
		liquidCopper = new CrucibleLiquid(new ItemStack(SteamcraftItems.steamcraftIngot,1,0), new ItemStack(SteamcraftItems.steamcraftPlate,1,0), new ItemStack(SteamcraftItems.steamcraftNugget,1,0), null,140,66,12);
		CrucibleLiquid.liquids.add(liquidCopper);
		liquidBrass = new CrucibleLiquid(new ItemStack(SteamcraftItems.steamcraftIngot,1,2), new ItemStack(SteamcraftItems.steamcraftPlate,1,4), new ItemStack(SteamcraftItems.steamcraftNugget,1,3), new CrucibleFormula(liquidZinc, 1, liquidCopper, 3, 4),242,191,66);
		CrucibleLiquid.liquids.add(liquidBrass);
		
		CrucibleLiquid.registerSmeltThingOredict("ingotGold", liquidGold, 9);
		CrucibleLiquid.registerSmeltThingOredict("ingotIron", liquidIron, 9);
		CrucibleLiquid.registerSmeltThingOredict("ingotZinc", liquidZinc, 9);
		CrucibleLiquid.registerSmeltThingOredict("ingotCopper", liquidCopper, 9);
		CrucibleLiquid.registerSmeltThingOredict("ingotBrass", liquidBrass, 9);
		
		CrucibleLiquid.registerSmeltThingOredict("plateGold", liquidGold, 9);
		CrucibleLiquid.registerSmeltThingOredict("plateIron", liquidIron, 9);
		CrucibleLiquid.registerSmeltThingOredict("plateZinc", liquidZinc, 9);
		CrucibleLiquid.registerSmeltThingOredict("plateCopper", liquidCopper, 9);
		CrucibleLiquid.registerSmeltThingOredict("plateBrass", liquidBrass, 9);
		
		CrucibleLiquid.registerSmeltThingOredict("nuggetGold", liquidGold, 1);
		CrucibleLiquid.registerSmeltThingOredict("nuggetIron", liquidIron, 1);
		CrucibleLiquid.registerSmeltThingOredict("nuggetZinc", liquidZinc, 1);
		CrucibleLiquid.registerSmeltThingOredict("nuggetCopper", liquidCopper, 1);
		CrucibleLiquid.registerSmeltThingOredict("nuggetBrass", liquidBrass, 1);
		CrucibleLiquid.registerSmeltThing(Items.gold_nugget, liquidGold, 1);
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