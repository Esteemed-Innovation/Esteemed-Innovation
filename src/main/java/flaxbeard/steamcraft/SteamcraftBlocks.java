package flaxbeard.steamcraft;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;
import flaxbeard.steamcraft.block.BlockBoiler;
import flaxbeard.steamcraft.block.BlockCarvingTable;
import flaxbeard.steamcraft.block.BlockEngineeringTable;
import flaxbeard.steamcraft.block.BlockItemMortar;
import flaxbeard.steamcraft.block.BlockManyMetadataItem;
import flaxbeard.steamcraft.block.BlockMold;
import flaxbeard.steamcraft.block.BlockPipe;
import flaxbeard.steamcraft.block.BlockPump;
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
import flaxbeard.steamcraft.block.BlockValvePipe;
import flaxbeard.steamcraft.item.BlockTankItem;

public class SteamcraftBlocks {
	public static Block steamcraftOre;
	public static Block crucible;
	public static Block mold;
	public static Block boiler;
	public static Block boilerOn;
	public static Block pipe;
	public static Block valvePipe;
	public static Block heater;
	public static Block charger;
	public static Block genocide;
	public static Block blockBrass;
	public static Block blockCopper;
	public static Block tank;
	public static Block meter;
	public static Block hammer;
	public static Block conveyor;
	public static Block itemMortar;
	
	public static Block pump;

	public static Block carving;
	public static Block engineering;
	public static BlockSteamPistonBase steamPiston;
	public static BlockSteamPistonMoving steamPiston_extension;
	public static BlockSteamPistonExtension steamPiston_head;
	
	public static void registerBlocks() {
		steamcraftOre = new BlockSteamcraftOre().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:ore").setHardness(3.0F).setResistance(5.0F);
		GameRegistry.registerBlock(steamcraftOre, BlockManyMetadataItem.class, "steamcraftOre");
		OreDictionary.registerOre("oreCopper", new ItemStack(steamcraftOre,1,0));
		OreDictionary.registerOre("oreZinc", new ItemStack(steamcraftOre,1,1));
		
		crucible = new BlockSteamcraftCrucible().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:crucible").setBlockTextureName("steamcraft:crucible").setHardness(3.5F);
		GameRegistry.registerBlock(crucible, "crucible");
		
		blockBrass = new BlockSteamcraft(Material.iron).setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:blockBrass").setBlockTextureName("steamcraft:blockBrass").setHardness(5.0F).setResistance(10.0F);
		GameRegistry.registerBlock(blockBrass, "blockBrass");
		OreDictionary.registerOre("blockBrass", blockBrass);
		
		blockCopper = new BlockSteamcraft(Material.iron).setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:blockCopper").setBlockTextureName("steamcraft:blockCopper").setHardness(5.0F).setResistance(10.0F);
		GameRegistry.registerBlock(blockCopper, "blockCopper");
		OreDictionary.registerOre("blockCopper", blockCopper);
		
		tank = new BlockSteamTank().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:tank").setBlockTextureName("steamcraft:brassTank").setHardness(5.0F).setResistance(10.0F);
		GameRegistry.registerBlock(tank, BlockTankItem.class, "steamTank");
		
		pump = new BlockPump().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:pump").setHardness(5.0F).setResistance(10.0F);
		GameRegistry.registerBlock(pump,"pump");
		
		boiler = new BlockBoiler(false).setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:boiler").setHardness(5.0F).setResistance(10.0F);
		GameRegistry.registerBlock(boiler,"boiler");
		boilerOn = new BlockBoiler(true).setBlockName("steamcraft:boiler").setHardness(5.0F).setResistance(10.0F);
		GameRegistry.registerBlock(boilerOn, "boilerOn");
		
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
		
		meter = new BlockSteamGauge().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:meter");
		GameRegistry.registerBlock(meter, "meter");


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
