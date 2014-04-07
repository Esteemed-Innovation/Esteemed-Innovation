package flaxbeard.steamcraft;



import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.block.BlockSteamcraftOre;
import flaxbeard.steamcraft.common.CommonProxy;
import flaxbeard.steamcraft.item.ItemFirearm;
import flaxbeard.steamcraft.world.SteamcraftOreGen;
import flaxbeard.thaumicexploration.ThaumicExploration;


@Mod(modid = "Steamcraft", name = "Steamcraft", version = "1.0.0")

public class Steamcraft {
	
    @Instance("Steamcraft")
    public static Steamcraft instance;
    
    public static FMLEventChannel channel;
    
    public static Block steamcraftOre;
    public static CreativeTabs tab;
    
    public static Item musketCartridge;
    public static Item musket;
    public static Item pistol;
    public static Item revolver;
    public static Item blunderbuss;
	
	@SidedProxy(clientSide = "flaxbeard.steamcraft.client.ClientProxy", serverSide = "flaxbeard.steamcraft.common.CommonProxy")
	public static CommonProxy proxy;
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		tab = new SCTab(CreativeTabs.getNextID(), "steamcraft");
		GameRegistry.registerWorldGenerator(new SteamcraftOreGen(), 1);
		steamcraftOre = new BlockSteamcraftOre().setCreativeTab(tab).setBlockName("steamcraft:ore");
		GameRegistry.registerBlock(steamcraftOre, "steamcraftOre");
		musketCartridge = new Item().setCreativeTab(tab).setUnlocalizedName("steamcraft:mustketCartridge").setTextureName("steamcraft:cartridge");
		GameRegistry.registerItem(musketCartridge, "musketCartridge");
		musket = (new ItemFirearm(20.0F, 84,0.1F, 5.0F, false, 1)).setUnlocalizedName("musket").setCreativeTab(tab).setTextureName("steamcraft:weaponMusket");
		GameRegistry.registerItem(musket, "musket");
		pistol = (new ItemFirearm(15.0F, 42,0.3F, 2.0F, false, 1)).setUnlocalizedName("pistol").setCreativeTab(tab).setTextureName("steamcraft:weaponPistol");
		GameRegistry.registerItem(pistol, "pistol");
		revolver = (new ItemFirearm(12.5F, 84,0.4F, 1.0F, false, 6)).setUnlocalizedName("revolver").setCreativeTab(tab).setTextureName("steamcraft:weaponRevolver");
		GameRegistry.registerItem(revolver, "revolver");
		blunderbuss = (new ItemFirearm(25.0F, 95,3.5F, 7.5F, true, 1)).setUnlocalizedName("blunderbuss").setCreativeTab(tab).setTextureName("steamcraft:weaponBlunderbuss");
		GameRegistry.registerItem(blunderbuss, "blunderbuss");
	}
	
	

	@EventHandler
	public void load(FMLInitializationEvent event) {

		
		proxy.registerRenderers();
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
		
			return Item.getItemFromBlock(ThaumicExploration.thinkTankJar);
		}	
	}
}