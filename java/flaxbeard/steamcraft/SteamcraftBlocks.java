package flaxbeard.steamcraft;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;
import flaxbeard.steamcraft.block.BlockBoiler;
import flaxbeard.steamcraft.block.BlockCarvingTable;
import flaxbeard.steamcraft.block.BlockEngineeringTable;
import flaxbeard.steamcraft.block.BlockManyMetadataItem;
import flaxbeard.steamcraft.block.BlockMold;
import flaxbeard.steamcraft.block.BlockPipe;
import flaxbeard.steamcraft.block.BlockSteamCharger;
import flaxbeard.steamcraft.block.BlockSteamHeater;
import flaxbeard.steamcraft.block.BlockSteamPistonBase;
import flaxbeard.steamcraft.block.BlockSteamPistonExtension;
import flaxbeard.steamcraft.block.BlockSteamPistonMoving;
import flaxbeard.steamcraft.block.BlockSteamTank;
import flaxbeard.steamcraft.block.BlockSteamcraft;
import flaxbeard.steamcraft.block.BlockSteamcraftCrucible;
import flaxbeard.steamcraft.block.BlockSteamcraftOre;

public class SteamcraftBlocks {
	public static Block steamcraftOre;
	public static Block crucible;
	public static Block mold;
	public static Block boiler;
	public static Block boilerOn;
	public static Block pipe;
	public static Block heater;
	public static Block charger;
	public static Block blockBrass;
	public static Block tank;

	public static Block carving;
	public static Block engineering;
	public static BlockSteamPistonBase steamPiston;
	public static BlockSteamPistonMoving steamPiston_extension;
	public static BlockSteamPistonExtension steamPiston_head;
	
	public static void registerBlocks() {
		steamcraftOre = new BlockSteamcraftOre().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:ore");
		GameRegistry.registerBlock(steamcraftOre, BlockManyMetadataItem.class, "steamcraftOre");
		OreDictionary.registerOre("oreCopper", new ItemStack(steamcraftOre,1,0));
		OreDictionary.registerOre("oreZinc", new ItemStack(steamcraftOre,1,1));
		
		crucible = new BlockSteamcraftCrucible().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:crucible").setBlockTextureName("steamcraft:crucible");
		GameRegistry.registerBlock(crucible, "crucible");
		
		blockBrass = new BlockSteamcraft(Material.iron).setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:blockBrass").setBlockTextureName("steamcraft:blockBrass");
		GameRegistry.registerBlock(blockBrass, "blockBrass");
		OreDictionary.registerOre("blockBrass", blockBrass);
		
		tank = new BlockSteamTank().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:tank").setBlockTextureName("steamcraft:brassTank");
		GameRegistry.registerBlock(tank, "steamTank");
		
		boiler = new BlockBoiler(false).setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:boiler");
		GameRegistry.registerBlock(boiler,"boiler");
		boilerOn = new BlockBoiler(true).setBlockName("steamcraft:boiler");
		GameRegistry.registerBlock(boilerOn, "boilerOn");
		
		pipe = new BlockPipe().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:pipe");
		GameRegistry.registerBlock(pipe, "pipe");
		
		carving = new BlockCarvingTable().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:carving").setBlockTextureName("steamcraft:carving").setHardness(2.5F).setStepSound(Block.soundTypeWood);
		GameRegistry.registerBlock(carving, "carving");
		
		engineering = new BlockEngineeringTable().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:engineering").setBlockTextureName("steamcraft:engineering");
		GameRegistry.registerBlock(engineering, "engineering");
		
		heater = new BlockSteamHeater().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:heater").setBlockTextureName("steamcraft:blockBrass");
		GameRegistry.registerBlock(heater, "heater");
		
		charger = new BlockSteamCharger().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:charger").setBlockTextureName("steamcraft:blockBrass");
		GameRegistry.registerBlock(charger, "charger");

//	
//		steamPiston = (BlockSteamPistonBase) new BlockSteamPistonBase(false).setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:piston");
//		GameRegistry.registerBlock(steamPiston, "steamPiston");
//		steamPiston_head = new BlockSteamPistonExtension();
//		GameRegistry.registerBlock(steamPiston_head, "steamPiston_head");
//	    steamPiston_extension = new BlockSteamPistonMoving();
//		GameRegistry.registerBlock(steamPiston_extension, "steamPiston_extension");
		
		mold = new BlockMold().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:mold").setBlockTextureName("steamcraft:mold");
		GameRegistry.registerBlock(mold, "mold");
	}
}
