package flaxbeard.steamcraft;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import flaxbeard.steamcraft.block.BlockBoiler;
import flaxbeard.steamcraft.block.BlockCarvingTable;
import flaxbeard.steamcraft.block.BlockDummy;
import flaxbeard.steamcraft.block.BlockEngineeringTable;
import flaxbeard.steamcraft.block.BlockFlashBoiler;
import flaxbeard.steamcraft.block.BlockItemMortar;
import flaxbeard.steamcraft.block.BlockMold;
import flaxbeard.steamcraft.block.BlockPipe;
import flaxbeard.steamcraft.block.BlockPump;
import flaxbeard.steamcraft.block.BlockRuptureDisc;
import flaxbeard.steamcraft.block.BlockSmasher;
import flaxbeard.steamcraft.block.BlockSteam;
import flaxbeard.steamcraft.block.BlockSteamCharger;
import flaxbeard.steamcraft.block.BlockSteamGauge;
import flaxbeard.steamcraft.block.BlockSteamHammer;
import flaxbeard.steamcraft.block.BlockSteamHeater;
import flaxbeard.steamcraft.block.BlockSteamPistonBase;
import flaxbeard.steamcraft.block.BlockSteamPistonExtension;
import flaxbeard.steamcraft.block.BlockSteamPistonMoving;
import flaxbeard.steamcraft.block.BlockSteamTank;
import flaxbeard.steamcraft.block.BlockSteamcraft;
import flaxbeard.steamcraft.block.BlockSteamcraftCrucible;
import flaxbeard.steamcraft.block.BlockSteamcraftOre;
import flaxbeard.steamcraft.block.BlockThumper;
import flaxbeard.steamcraft.block.BlockThumperDummy;
import flaxbeard.steamcraft.block.BlockValvePipe;
import flaxbeard.steamcraft.block.MaterialAirlike;
import flaxbeard.steamcraft.item.BlockManyMetadataItem;
import flaxbeard.steamcraft.item.BlockRuptureDiscItem;
import flaxbeard.steamcraft.item.BlockTankItem;
import flaxbeard.steamcraft.item.BlockThumperItem;

public class SteamcraftBlocks {
	public static Block steamcraftOre;
	public static Block crucible;
	public static Block mold;
	public static Block boiler;
	public static Block flashBoiler;
	public static Block boilerOn;
	public static Block pipe;
	public static Block valvePipe;
	public static Block heater;
	public static Block charger;
	public static Block genocide;
	public static Block blockBrass;
	public static Block blockCopper;
	public static Block blockZinc;
	public static Block tank;
	public static Block meter;
	public static Block ruptureDisc;
	public static Block hammer;
	public static Block conveyor;
	public static Block itemMortar;
	public static Block thumper;
	public static Block thumperDummy;
	
	public static Block pump;
	
	public static Block smasher;
	public static Block dummy;

	public static Block carving;
	public static Block engineering;
	public static BlockSteamPistonBase steamPiston;
	public static BlockSteamPistonMoving steamPiston_extension;
	public static BlockSteamPistonExtension steamPiston_head;
	
	public static Material airlike;
	public static Block steam;
	
	public static void registerBlocks() {
		airlike = new MaterialAirlike(MapColor.airColor);
		steam = new BlockSteam().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:steam").setBlockTextureName("steamcraft:steam2").setHardness(0.0F).setResistance(0.0F);
		GameRegistry.registerBlock(steam, BlockManyMetadataItem.class, "steam");

		steamcraftOre = new BlockSteamcraftOre().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:ore").setHardness(3.0F).setResistance(5.0F);
		GameRegistry.registerBlock(steamcraftOre, BlockManyMetadataItem.class, "steamcraftOre");
		OreDictionary.registerOre("oreCopper", new ItemStack(steamcraftOre,1,0));
		OreDictionary.registerOre("oreZinc", new ItemStack(steamcraftOre,1,1));
		if (Loader.isModLoaded("Railcraft") && Config.genPoorOre) {
			OreDictionary.registerOre("orePoorZinc", new ItemStack(steamcraftOre,1,2));
		}
		
		crucible = new BlockSteamcraftCrucible().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:crucible").setBlockTextureName("steamcraft:crucible").setHardness(3.5F);
		GameRegistry.registerBlock(crucible, "crucible");
		
		blockBrass = new BlockSteamcraft(Material.iron).setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:blockBrass").setBlockTextureName("steamcraft:blockBrass").setHardness(5.0F).setResistance(10.0F);
		GameRegistry.registerBlock(blockBrass, "blockBrass");
		OreDictionary.registerOre("blockBrass", blockBrass);
		
		blockZinc = new BlockSteamcraft(Material.iron).setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:blockZinc").setBlockTextureName("steamcraft:blockZinc").setHardness(5.0F).setResistance(10.0F);
		GameRegistry.registerBlock(blockZinc, "blockZinc");
		OreDictionary.registerOre("blockZinc", blockZinc);
		
		blockCopper = new BlockSteamcraft(Material.iron).setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:blockCopper").setBlockTextureName("steamcraft:blockCopper").setHardness(5.0F).setResistance(10.0F);
		GameRegistry.registerBlock(blockCopper, "blockCopper");
		OreDictionary.registerOre("blockCopper", blockCopper);
		
		tank = new BlockSteamTank().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:tank").setBlockTextureName("steamcraft:brassTank").setHardness(5.0F).setResistance(10.0F);
		GameRegistry.registerBlock(tank, BlockTankItem.class, "steamTank");
		
		pump = new BlockPump().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:pump").setHardness(5.0F).setResistance(10.0F);
		GameRegistry.registerBlock(pump,"pump");
		
		smasher = new BlockSmasher().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:smasher").setHardness(5.0F).setResistance(10.0F);
		GameRegistry.registerBlock(smasher, "smasher");
		dummy = new BlockDummy().setBlockName("steamcraft:dummy");
		GameRegistry.registerBlock(dummy, "dummy");
		
		boiler = new BlockBoiler(false).setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:boiler").setHardness(5.0F).setResistance(10.0F);
		GameRegistry.registerBlock(boiler,"boiler");
		boilerOn = new BlockBoiler(true).setBlockName("steamcraft:boiler").setHardness(5.0F).setResistance(10.0F);
		GameRegistry.registerBlock(boilerOn, "boilerOn");
		
		flashBoiler = new BlockFlashBoiler().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:flashBoiler").setHardness(5.0f).setResistance(10.0F);
		GameRegistry.registerBlock(flashBoiler, "flashBoiler");
		
		pipe = new BlockPipe().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:pipe").setHardness(2.5F).setResistance(5.0F);
		GameRegistry.registerBlock(pipe, "pipe");
		valvePipe = new BlockValvePipe().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:valvePipe").setHardness(2.5F).setResistance(5.0F);
		GameRegistry.registerBlock(valvePipe, "valvePipe");
		
		
		
		carving = new BlockCarvingTable().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:carving").setBlockTextureName("steamcraft:carving").setHardness(2.5F).setStepSound(Block.soundTypeWood);
		GameRegistry.registerBlock(carving, "carving");
		
		engineering = new BlockEngineeringTable().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:engineering").setBlockTextureName("steamcraft:engineering").setHardness(3.5F);
		GameRegistry.registerBlock(engineering, "engineering");
		
		heater = new BlockSteamHeater().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:heater").setBlockTextureName("steamcraft:blockBrass").setHardness(3.625F).setResistance(7.5F);
		GameRegistry.registerBlock(heater, "heater");
		
		charger = new BlockSteamCharger().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:charger").setBlockTextureName("steamcraft:blockBrass").setHardness(3.5F);
		GameRegistry.registerBlock(charger, "charger");
		
		hammer = new BlockSteamHammer().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:hammer").setBlockTextureName("steamcraft:blockBrass").setHardness(3.5F);
		GameRegistry.registerBlock(hammer, "hammer");
		
		itemMortar = new BlockItemMortar().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:itemMortar").setBlockTextureName("steamcraft:blankTexture").setHardness(3.5F);
		GameRegistry.registerBlock(itemMortar, "itemMortar");
		
		meter = new BlockSteamGauge().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:meter").setHardness(1.0F);
		GameRegistry.registerBlock(meter, "meter");
		
		ruptureDisc = new BlockRuptureDisc().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:ruptureDisc").setHardness(1.0F);
		GameRegistry.registerBlock(ruptureDisc, BlockRuptureDiscItem.class, "ruptureDisc");
		
		thumper = new BlockThumper().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:thumper").setBlockTextureName("steamcraft:blankTexture").setHardness(3.5F);
		GameRegistry.registerBlock(thumper, BlockThumperItem.class, "thumper");
		thumperDummy = new BlockThumperDummy().setBlockName("steamcraft:thumperDummy").setBlockTextureName("steamcraft:blankTexture").setHardness(3.5F);
		GameRegistry.registerBlock(thumperDummy, "thumperDummy");


//	
//		steamPiston = (BlockSteamPistonBase) new BlockSteamPistonBase(false).setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:piston");
//		GameRegistry.registerBlock(steamPiston, "steamPiston");
//		steamPiston_head = new BlockSteamPistonExtension();
//		GameRegistry.registerBlock(steamPiston_head, "steamPiston_head");
//	    steamPiston_extension = new BlockSteamPistonMoving();
//		GameRegistry.registerBlock(steamPiston_extension, "steamPiston_extension");
		
		mold = new BlockMold().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:mold").setBlockTextureName("steamcraft:mold").setHardness(3.5F);
		GameRegistry.registerBlock(mold, "mold");
	}
}
