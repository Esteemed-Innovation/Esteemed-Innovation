package flaxbeard.steamcraft;



import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
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
import flaxbeard.steamcraft.tile.TileEntitySteamPipe;
import flaxbeard.steamcraft.world.SteamcraftOreGen;

@Mod(modid = "Steamcraft", name = "Steamcraft", version = "1.0.0")

public class Steamcraft {
	
    @Instance("Steamcraft")
    public static Steamcraft instance;
    
    public static FMLEventChannel channel;
    
    public static Block steamcraftOre;
    public static Block crucible;
    public static Block mold;
    public static Block boiler;
    public static Block pipe;
    
    public static CreativeTabs tab;
    
    public static Item musketCartridge;
    public static Item musket;
    public static Item pistol;
    public static Item revolver;
    public static Item blunderbuss;
    public static Item spyglass;
    
    public static Item ingotMold;
    public static Item nuggetMold;
    public static Item plateMold;
    
    public static Item steamcraftIngot;
    public static Item steamcraftNugget;
    public static Item steamcraftPlate;
    
    public static CrucibleLiquid liquidIron;
    public static CrucibleLiquid liquidZinc;
    public static CrucibleLiquid liquidCopper;
    public static CrucibleLiquid liquidGold;
    public static CrucibleLiquid liquidBrass;
	
	@SidedProxy(clientSide = "flaxbeard.steamcraft.client.ClientProxy", serverSide = "flaxbeard.steamcraft.common.CommonProxy")
	public static CommonProxy proxy;

	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		tab = new SCTab(CreativeTabs.getNextID(), "steamcraft");
		
		GameRegistry.registerWorldGenerator(new SteamcraftOreGen(), 1);
		
		steamcraftOre = new BlockSteamcraftOre().setCreativeTab(tab).setBlockName("steamcraft:ore");
		GameRegistry.registerBlock(steamcraftOre, BlockManyMetadataItem.class, "steamcraftOre");
		OreDictionary.registerOre("oreCopper", new ItemStack(steamcraftOre,1,0));
		OreDictionary.registerOre("oreZinc", new ItemStack(steamcraftOre,1,1));
		
		crucible = new BlockSteamcraftCrucible().setCreativeTab(tab).setBlockName("steamcraft:crucible").setBlockTextureName("steamcraft:crucible");
		GameRegistry.registerBlock(crucible, "crucible");
		
		boiler = new BlockBoiler().setCreativeTab(tab).setBlockName("steamcraft:boiler").setBlockTextureName("steamcraft:boiler");
		GameRegistry.registerBlock(boiler, "boiler");
		
		pipe = new BlockPipe().setCreativeTab(tab).setBlockName("steamcraft:pipe").setBlockTextureName("steamcraft:pipe");
		GameRegistry.registerBlock(pipe, "pipe");
		
		
		mold = new BlockMold().setCreativeTab(tab).setBlockName("steamcraft:mold").setBlockTextureName("steamcraft:mold");
		GameRegistry.registerBlock(mold, "mold");
		
		musketCartridge = new Item().setCreativeTab(tab).setUnlocalizedName("steamcraft:musketCartridge").setTextureName("steamcraft:cartridge");
		GameRegistry.registerItem(musketCartridge, "musketCartridge");
		musket = new ItemFirearm(20.0F, 84,0.1F, 5.0F, false, 1).setUnlocalizedName("steamcraft:musket").setCreativeTab(tab).setTextureName("steamcraft:weaponMusket");
		GameRegistry.registerItem(musket, "musket");
		pistol = new ItemFirearm(15.0F, 42,0.3F, 2.0F, false, 1).setUnlocalizedName("steamcraft:pistol").setCreativeTab(tab).setTextureName("steamcraft:weaponPistol");
		GameRegistry.registerItem(pistol, "pistol");
		revolver = new ItemFirearm(12.5F, 84,0.4F, 1.0F, false, 6).setUnlocalizedName("steamcraft:revolver").setCreativeTab(tab).setTextureName("steamcraft:weaponRevolver");
		GameRegistry.registerItem(revolver, "revolver");
		blunderbuss = new ItemFirearm(25.0F, 95,3.5F, 7.5F, true, 1).setUnlocalizedName("steamcraft:blunderbuss").setCreativeTab(tab).setTextureName("steamcraft:weaponBlunderbuss");
		GameRegistry.registerItem(blunderbuss, "blunderbuss");
		spyglass = new ItemSpyglass().setUnlocalizedName("steamcraft:spyglass").setCreativeTab(tab).setTextureName("steamcraft:spyglass");
		GameRegistry.registerItem(spyglass, "spyglass");
		
		ingotMold = new ItemIngotMold().setUnlocalizedName("steamcraft:ingotMold").setCreativeTab(tab).setTextureName("steamcraft:moldIngot");
		GameRegistry.registerItem(ingotMold, "ingotMold");
		nuggetMold = new ItemNuggetMold().setUnlocalizedName("steamcraft:nuggetMold").setCreativeTab(tab).setTextureName("steamcraft:moldNugget");
		GameRegistry.registerItem(nuggetMold, "nuggetMold");
		plateMold = new ItemPlateMold().setUnlocalizedName("steamcraft:plateMold").setCreativeTab(tab).setTextureName("steamcraft:moldPlate");
		GameRegistry.registerItem(plateMold, "plateMold");
		
		steamcraftIngot = new ItemSteamcraftIngot().setUnlocalizedName("steamcraft:ingot").setCreativeTab(tab);
		GameRegistry.registerItem(steamcraftIngot, "steamcraftIngot");
		OreDictionary.registerOre("ingotCopper", new ItemStack(steamcraftIngot,1,0));
		OreDictionary.registerOre("ingotZinc", new ItemStack(steamcraftIngot,1,1));
		OreDictionary.registerOre("ingotBrass", new ItemStack(steamcraftIngot,1,2));
		
		steamcraftNugget = new ItemSteamcraftNugget().setUnlocalizedName("steamcraft:nugget").setCreativeTab(tab);
		GameRegistry.registerItem(steamcraftNugget, "steamcraftNugget");
		OreDictionary.registerOre("nuggetCopper", new ItemStack(steamcraftNugget,1,0));
		OreDictionary.registerOre("nuggetZinc", new ItemStack(steamcraftNugget,1,1));
		OreDictionary.registerOre("nuggetIron", new ItemStack(steamcraftNugget,1,2));
		OreDictionary.registerOre("nuggetBrass", new ItemStack(steamcraftNugget,1,3));
		
		steamcraftPlate = new ItemSteamcraftPlate().setUnlocalizedName("steamcraft:plate").setCreativeTab(tab);
		GameRegistry.registerItem(steamcraftPlate, "steamcraftPlate");
		OreDictionary.registerOre("plateCopper", new ItemStack(steamcraftPlate,1,0));
		OreDictionary.registerOre("plateZinc", new ItemStack(steamcraftPlate,1,1));
		OreDictionary.registerOre("plateIron", new ItemStack(steamcraftPlate,1,2));
		OreDictionary.registerOre("plateGold", new ItemStack(steamcraftPlate,1,3));
		OreDictionary.registerOre("plateBrass", new ItemStack(steamcraftPlate,1,4));
		
		GameRegistry.registerTileEntity(TileEntityCrucible.class, "steamcraftCrucible");
		GameRegistry.registerTileEntity(TileEntityMold.class, "mold");
		GameRegistry.registerTileEntity(TileEntityBoiler.class, "boiler");
		GameRegistry.registerTileEntity(TileEntitySteamPipe.class, "pipe");
	}
	
	

	@EventHandler
	public void load(FMLInitializationEvent event) {
		
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new SteamcraftGuiHandler());
		 MinecraftForge.EVENT_BUS.register(new MechHandler());
		FMLCommonHandler.instance().bus().register(new SpyglassHandler());
		proxy.registerRenderers();
		liquidIron = new CrucibleLiquid(new ItemStack(Items.iron_ingot), new ItemStack(steamcraftPlate,1,2), new ItemStack(steamcraftNugget,1,2), null,200,200,200);
		CrucibleLiquid.liquids.add(liquidIron);
		liquidGold = new CrucibleLiquid(new ItemStack(Items.gold_ingot), new ItemStack(steamcraftPlate,1,3), new ItemStack(Items.gold_nugget), null,220,157,11);
		CrucibleLiquid.liquids.add(liquidGold);
		liquidZinc = new CrucibleLiquid(new ItemStack(Steamcraft.steamcraftIngot,1,1), new ItemStack(steamcraftPlate,1,1), new ItemStack(steamcraftNugget,1,1), null,225,225,225);
		CrucibleLiquid.liquids.add(liquidZinc);
		liquidCopper = new CrucibleLiquid(new ItemStack(Steamcraft.steamcraftIngot,1,0), new ItemStack(steamcraftPlate,1,0), new ItemStack(steamcraftNugget,1,0), null,140,66,12);
		CrucibleLiquid.liquids.add(liquidCopper);
		liquidBrass = new CrucibleLiquid(new ItemStack(Steamcraft.steamcraftIngot,1,2), new ItemStack(steamcraftPlate,1,4), new ItemStack(steamcraftNugget,1,3), new CrucibleFormula(liquidZinc, 1, liquidCopper, 3, 4),242,191,66);
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
		
			return Item.getItemFromBlock(Steamcraft.crucible);
		}	
	}
}