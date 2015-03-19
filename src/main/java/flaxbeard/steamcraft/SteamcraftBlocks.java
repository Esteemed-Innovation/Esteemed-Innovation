package flaxbeard.steamcraft;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import flaxbeard.steamcraft.block.*;
import flaxbeard.steamcraft.item.BlockManyMetadataItem;
import flaxbeard.steamcraft.item.BlockRuptureDiscItem;
import flaxbeard.steamcraft.item.BlockTankItem;
import flaxbeard.steamcraft.item.BlockThumperItem;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class SteamcraftBlocks {

    // metals
    public static Block steamcraftOre;
    public static Block blockBrass;
    public static Block blockCopper;
    public static Block blockZinc;

    // casting
    public static Block crucible;
    public static Block hellCrucible;
    public static Block mold;
    public static Block carving;

    // steam net
    public static Block blockPlacer;
    public static Block boiler;
    public static Block boilerOn;
    public static Block flashBoiler;
    public static Block pipe;
    public static Block valvePipe;
    public static Block tank;
    public static Block gauge;
    public static Block horn;
    public static Block fluidSteamConverter;
    public static Block ruptureDisc;
    public static Block geoBoiler;
    public static Block geoBoiler_on;
    public static Block bloodBoiler;
    public static Block bloodBoiler_on;

    // steam machines
    public static Block heater;
    public static Block charger;
    public static Block genocide;
    public static Block hammer;
    public static Block conveyor;
    public static Block itemMortar;
    public static Block thumper;
    public static Block thumperDummy;
    public static Block chargingPad;
    public static Block saw;

    public static Block fan;
    public static Block vacuum;
    public static Block pump;
    public static Block smasher;
    public static Block dummy;

    // misc
    public static Block engineering;
    public static BlockSteamPistonBase steamPiston;
    public static BlockSteamPistonMoving steamPiston_extension;
    public static BlockSteamPistonExtension steamPiston_head;
    public static Block customCrafingTable;
    public static Block customFurnace;
    public static Block customFurnaceOff;

    public static void registerBlocks() {
        registerMetals();
        registerCasting();
        registerSteamnet();
        registerSteamMachines();
        registerAdvancedAutomationMachines();
        registerMisc();
    }

    public static void registerMetals() {
        steamcraftOre = new BlockSteamcraftOre().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:ore").setHardness(3.0F).setResistance(5.0F);
        GameRegistry.registerBlock(steamcraftOre, BlockManyMetadataItem.class, "steamcraftOre");
        OreDictionary.registerOre("oreCopper", new ItemStack(steamcraftOre, 1, 0));
        OreDictionary.registerOre("oreZinc", new ItemStack(steamcraftOre, 1, 1));
        if (Loader.isModLoaded("Railcraft") && Config.genPoorOre) {
            OreDictionary.registerOre("orePoorZinc", new ItemStack(steamcraftOre, 1, 2));
        }
        blockBrass = new BlockBeacon(Material.iron).setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:blockBrass").setBlockTextureName("steamcraft:blockBrass").setHardness(5.0F).setResistance(10.0F);
        GameRegistry.registerBlock(blockBrass, "blockBrass");
        OreDictionary.registerOre("blockBrass", blockBrass);

        blockZinc = new BlockBeacon(Material.iron).setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:blockZinc").setBlockTextureName("steamcraft:blockZinc").setHardness(5.0F).setResistance(10.0F);
        GameRegistry.registerBlock(blockZinc, "blockZinc");
        OreDictionary.registerOre("blockZinc", blockZinc);

        blockCopper = new BlockBeacon(Material.iron).setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:blockCopper").setBlockTextureName("steamcraft:blockCopper").setHardness(5.0F).setResistance(10.0F);
        GameRegistry.registerBlock(blockCopper, "blockCopper");
        OreDictionary.registerOre("blockCopper", blockCopper);
    }

    public static void registerCasting() {
        if (Config.enableCrucible) {
            crucible = new BlockSteamcraftCrucible().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:crucible").setBlockTextureName("steamcraft:crucible").setHardness(3.5F);
            GameRegistry.registerBlock(crucible, "crucible");
            if (Config.enableHellCrucible) {
                hellCrucible = new BlockSteamcraftCrucible().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:hellCrucible").setBlockTextureName("steamcraft:hellCrucible").setHardness(3.5F);
                GameRegistry.registerBlock(hellCrucible, "hellCrucible");
            }
        }

        if (Config.enableMold) {
            mold = new BlockMold().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:mold").setBlockTextureName("steamcraft:mold").setHardness(3.5F);
            GameRegistry.registerBlock(mold, "mold");
            carving = new BlockCarvingTable().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:carving").setBlockTextureName("steamcraft:carving").setHardness(2.5F).setStepSound(Block.soundTypeWood);
            GameRegistry.registerBlock(carving, "carving");
        }
    }

    public static void registerSteamnet() {
        if (Config.enableBoiler) {
            boiler = new BlockBoiler(false).setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:boiler").setHardness(5.0F).setResistance(10.0F);
            GameRegistry.registerBlock(boiler, "boiler");
            boilerOn = new BlockBoiler(true).setBlockName("steamcraft:boiler").setHardness(5.0F).setResistance(10.0F);
            GameRegistry.registerBlock(boilerOn, "boilerOn");
        }

        if (Config.enableFlashBoiler) {
            flashBoiler = new BlockFlashBoiler().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:flashBoiler").setHardness(5.0f).setResistance(10.0F);
            GameRegistry.registerBlock(flashBoiler, "flashBoiler");
        }

        if (Config.enablePipe) {
            pipe = new BlockPipe().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:pipe").setHardness(2.5F).setResistance(5.0F);
            GameRegistry.registerBlock(pipe, "pipe");
        }
        if (Config.enableValvePipe && Config.enablePipe) {
            valvePipe = new BlockValvePipe().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:valvePipe").setHardness(2.5F).setResistance(5.0F);
            GameRegistry.registerBlock(valvePipe, "valvePipe");
        }

        if (Config.enableTank) {
            tank = new BlockSteamTank().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:tank").setBlockTextureName("steamcraft:brassTank").setHardness(5.0F).setResistance(10.0F);
            GameRegistry.registerBlock(tank, BlockTankItem.class, "steamTank");
        }

        if (Config.enableGauge && Config.enablePipe) {
            gauge = new BlockSteamGauge().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:meter").setHardness(1.0F);
            GameRegistry.registerBlock(gauge, "meter");
        }

        if (Config.enableRuptureDisc && Config.enablePipe) {
            ruptureDisc = new BlockRuptureDisc().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:ruptureDisc").setHardness(1.0F);
            GameRegistry.registerBlock(ruptureDisc, BlockRuptureDiscItem.class, "ruptureDisc");
        }

        if (Config.enableHorn) {
            horn = new BlockWhistle().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:horn").setHardness(1.0F);
            GameRegistry.registerBlock(horn, "horn");
        }

        if (Config.enableFluidSteamConverter) {
            //fluidSteamConverter = new BlockFluidSteamConverter().setBlockName("steamcraft:fluidSteamConverter").setBlockTextureName("steamcraft:blockBrass").setResistance(7.5F).setHardness(3.5F).setCreativeTab(Steamcraft.tab);
            fluidSteamConverter = new BlockFluidSteamConverter().setBlockName("steamcraft:fluidSteamConverter").setBlockTextureName("steamcraft:blockBrass").setResistance(7.5F).setHardness(3.5F);
            GameRegistry.registerBlock(fluidSteamConverter, "fluidSteamConverter");
        }

        /*
        if (Loader.isModLoaded("AWayofTime") && Config.enableBloodMagicIntegration && Config.enableBloodBoiler){
            bloodBoiler = new BlockBloodBoiler(false).setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:bloodBoiler").setHardness(5.0F).setResistance(10.0F);
            GameRegistry.registerBlock(bloodBoiler, "bloodBoiler");
            bloodBoiler_on = new BlockBloodBoiler(true).setBlockName("steamcraft:bloodBoiler").setHardness(5.0F).setResistance(10.0F);
            GameRegistry.registerBlock(bloodBoiler_on, "bloodBoiler_on");
        }
        */
    }

    public static void registerAdvancedAutomationMachines() {
        if (Config.enableSaw) {
            saw = new BlockSaw().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:saw")
              .setHardness(5.0F).setResistance(10.F);
            GameRegistry.registerBlock(saw, "saw");
        }
        if (Config.enableBlockPlacer) {
            blockPlacer = new BlockPlacer().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:blockPlacer").setHardness(5.0F).setResistance(10.0F);
            GameRegistry.registerBlock(blockPlacer, "blockPlacer");
        }
        if (Config.enableGenocide) {
            genocide = new BlockFishGenocideMachine().setCreativeTab(Steamcraft.tab)
              .setBlockName("steamcraft:genocide").setHardness(5.0F).setResistance(10.0F);
            GameRegistry.registerBlock(genocide, "genocide");
        }
    }

    public static void registerSteamMachines() {
        if (Config.enablePump) {
            pump = new BlockPump().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:pump").setHardness(5.0F).setResistance(10.0F);
            GameRegistry.registerBlock(pump, "pump");
        }

        if (Config.enableSmasher) {
            smasher = new BlockSmasher().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:smasher").setHardness(5.0F).setResistance(10.0F);
            GameRegistry.registerBlock(smasher, "smasher");
            dummy = new BlockDummy().setBlockName("steamcraft:dummy");
            GameRegistry.registerBlock(dummy, "dummy");
        }

        if (Config.enableHeater && Config.enablePipe) {
            heater = new BlockSteamHeater().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:heater").setBlockTextureName("steamcraft:blockBrass").setHardness(3.625F).setResistance(7.5F);
            GameRegistry.registerBlock(heater, "heater");
        }

        if (Config.enableChargingPad && Config.enableCharger) {
            chargingPad = new BlockChargingPad().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:chargingPad").setBlockTextureName("steamcraft:blockBrass").setHardness(3.5F);
            GameRegistry.registerBlock(chargingPad, "chargingPad");
        }

        if (Config.enableCharger) {
            charger = new BlockSteamCharger().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:charger").setBlockTextureName("steamcraft:blockBrass").setHardness(3.5F);
            GameRegistry.registerBlock(charger, "charger");
        }

        if (Config.enableHammer) {
            hammer = new BlockSteamHammer().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:hammer").setBlockTextureName("steamcraft:blockBrass").setHardness(3.5F);
            GameRegistry.registerBlock(hammer, "hammer");
        }

        if (Config.enableMortar) {
            itemMortar = new BlockItemMortar().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:itemMortar").setBlockTextureName("steamcraft:blankTexture").setHardness(3.5F);
            GameRegistry.registerBlock(itemMortar, "itemMortar");
        }

        if (Config.enableThumper) {
            thumper = new BlockThumper().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:thumper").setBlockTextureName("steamcraft:blankTexture").setHardness(3.5F);
            GameRegistry.registerBlock(thumper, BlockThumperItem.class, "thumper");
            thumperDummy = new BlockThumperDummy().setBlockName("steamcraft:thumperDummy").setBlockTextureName("steamcraft:blankTexture").setHardness(3.5F);
            GameRegistry.registerBlock(thumperDummy, "thumperDummy");
        }

        if (Config.enableFan) {
            fan = new BlockFan().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:fan").setResistance(7.5F).setBlockTextureName("steamcraft:blankTexture").setHardness(3.5F);
            GameRegistry.registerBlock(fan, "fan");
        }

        if (Config.enableVacuum && Config.enableFan) {
            vacuum = new BlockVacuum().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:vacuum").setBlockTextureName("steamcraft:blankTexture").setResistance(7.5F).setHardness(3.5F);
            GameRegistry.registerBlock(vacuum, "vacuum");
        }

//		customCrafingTable = new BlockCustomCraftingTable().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:customCraftingTable").setBlockTextureName("steamcraft:blankTexture").setHardness(2.5F);
//		GameRegistry.registerBlock(customCrafingTable, "customCraftingTable");
//		
//		customFurnace = new BlockCustomFurnace(true).setBlockName("steamcraft:customFurnace").setBlockTextureName("steamcraft:blankTexture").setHardness(3.5F);
//		GameRegistry.registerBlock(customFurnace, "customFurnace");
//		
//		customFurnaceOff = new BlockCustomFurnace(false).setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:customFurnace").setBlockTextureName("steamcraft:blankTexture").setHardness(3.5F);
//		GameRegistry.registerBlock(customFurnaceOff, "customFurnaceOff");
//		
    }

    public static void registerMisc() {
        if (Config.enableEngineering) {
            engineering = new BlockEngineeringTable().setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:engineering").setBlockTextureName("steamcraft:engineering").setHardness(3.5F);
            GameRegistry.registerBlock(engineering, "engineering");
        }
        //steamPiston = (BlockSteamPistonBase) new BlockSteamPistonBase(false).setCreativeTab(Steamcraft.tab).setBlockName("steamcraft:piston");
        //GameRegistry.registerBlock(steamPiston, "steamPiston");
        //steamPiston_head = new BlockSteamPistonExtension();
        //GameRegistry.registerBlock(steamPiston_head, "steamPiston_head");
        //steamPiston_extension = new BlockSteamPistonMoving();
        //GameRegistry.registerBlock(steamPiston_extension, "steamPiston_extension");
    }
}
