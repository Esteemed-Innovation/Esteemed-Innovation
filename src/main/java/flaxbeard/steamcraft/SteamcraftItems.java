package flaxbeard.steamcraft;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import flaxbeard.steamcraft.api.ICrucibleMold;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.enhancement.IEnhancement;
import flaxbeard.steamcraft.api.enhancement.IRocket;
import flaxbeard.steamcraft.api.exosuit.ExosuitPlate;
import flaxbeard.steamcraft.api.exosuit.ExosuitSlot;
import flaxbeard.steamcraft.integration.baubles.BaublesIntegration;
import flaxbeard.steamcraft.item.*;
import flaxbeard.steamcraft.item.firearm.*;
import flaxbeard.steamcraft.item.tool.*;
import flaxbeard.steamcraft.item.tool.steam.*;

public class SteamcraftItems {
    public static HashMap<String, Item> tools = new HashMap<String, Item>();

    // firearms
    public static Item musketCartridge;
    public static Item musket;
    public static Item pistol;
    public static Item revolver;
    public static Item rocketLauncher;
    public static Item blunderbuss;
    public static Item enhancementAblaze;
    public static Item enhancementRevolver;
    public static Item enhancementSpeedloader;
    public static Item enhancementSilencer;
    public static Item enhancementRecoil;
    public static Item enhancementSpeedy;
    public static Item enhancementFastRockets;
    public static Item enhancementAirStrike;
    public static Item rocket;
    public static Item rocketConcussive;
    public static Item rocketMiner;
    public static Item enhancementAmmo;

    // misc
    public static Item book;
    public static Item spyglass;
    public static Item survivalist;
    public static Item astrolabe;
    public static Item wrench;
    public static Item smashedOre;
    public static Item canister;

    // molds
    public static Item blankMold;
    public static Item ingotMold;
    public static Item nuggetMold;
    public static Item plateMold;

    // metals
    public static Item steamcraftIngot;
    public static Item steamcraftNugget;
    public static Item steamcraftPlate;
    public static Item exosuitPlate;
    public static Item steamcraftCrafting;

    // exosuit
    public static Item exoArmorHead;
    public static Item exoArmorBody;
    public static Item exoArmorLegs;
    public static Item exoArmorFeet;

    public static Item monacle;
    public static Item goggles;
    public static Item tophat;
    public static Item tophatNoEmerald;

    // exosuit upgrades
    public static Item upgradeFlippers; //nyi
    public static Item jetpack;
    public static Item wings;
    public static Item powerFist;
    public static Item extendoFist;
    public static Item thrusters;
    public static Item fallAssist;
    public static Item jumpAssist;
    public static Item runAssist;
    public static Item doubleJump;
    //	public static Item antiFire;
    public static Item stealthUpgrade;
    public static Item canner;
    public static Item pitonDeployer;
    public static Item enderShroud;

    //public static Item fakeOre;

    // exosuit tanks
    public static Item reinforcedTank;
    public static Item uberReinforcedTank;

    // steam tools
    public static Item steamDrill;
    public static Item steamAxe;
    public static Item steamShovel;

    // food
    public static Item steamedPorkchop;
    public static Item steamedFish;
    public static Item steamedBeef;
    public static Item steamedChicken;
    public static Item steamedSalmon;

    public static void registerItems() {

        registerMisc();
        registerFirearms();
        registerExosuit();
        registerExosuitUpgrades();
        registerSteamTools();
        registerMolds();
        registerFood();
        registerMetals();
        registerMetalThings();

    }

    private static void registerMisc() {
        book = new ItemSteamcraftBook().setUnlocalizedName("steamcraft:book").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:book");
        GameRegistry.registerItem(book, "book");

        if (Config.enableSpyglass) {
            spyglass = new ItemSpyglass().setUnlocalizedName("steamcraft:spyglass").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:spyglass");
            GameRegistry.registerItem(spyglass, "spyglass");
            SteamcraftRegistry.registerEnhancement((IEnhancement) spyglass);
        }

        if (Config.enableAstrolabe) {
            astrolabe = new ItemAstrolabe().setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:astrolabe").setTextureName("steamcraft:astrolabe").setMaxStackSize(1);
            GameRegistry.registerItem(astrolabe, "astrolabe");
        }

        if (Config.enableCanister) {
            canister = new Item().setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:canister").setTextureName("steamcraft:canister");
            GameRegistry.registerItem(canister, "canister");
        }

        if (Config.enableSurvivalist) {
            if (!Loader.isModLoaded("Baubles")) {
                survivalist = new Item().setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:survivalist").setTextureName("steamcraft:toolkit").setMaxStackSize(1);
            } else {
                survivalist = BaublesIntegration.getSurvivalist();
            }
            GameRegistry.registerItem(survivalist, "survivalist");
        }

        wrench = new ItemWrench().setUnlocalizedName("steamcraft:wrench").setTextureName("steamcraft:wrench").setCreativeTab(Steamcraft.tab);
        GameRegistry.registerItem(wrench, "wrench");

        smashedOre = new ItemSmashedOre().setUnlocalizedName("steamcraft:smashedOre").setMaxStackSize(64).setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:smashedOre");
        GameRegistry.registerItem(smashedOre, "smashedOre");

        steamcraftCrafting = new ItemSteamcraftCrafting().setUnlocalizedName("steamcraft:crafting").setCreativeTab(Steamcraft.tab);
        GameRegistry.registerItem(steamcraftCrafting, "steamcraftCrafting");
    }

    private static void registerFirearms() {
        if (Config.enableFirearms) {
            musketCartridge = new Item().setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:musketCartridge").setTextureName("steamcraft:cartridge");
            GameRegistry.registerItem(musketCartridge, "musketCartridge");

            musket = new ItemFirearm(20.0F, 84, 0.2F, 5.0F, false, 1, "ingotIron").setUnlocalizedName("steamcraft:musket").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:weaponMusket");
            GameRegistry.registerItem(musket, "musket");

            pistol = new ItemFirearm(15.0F, 42, 0.5F, 2.0F, false, 1, "ingotIron").setUnlocalizedName("steamcraft:pistol").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:weaponPistol");
            GameRegistry.registerItem(pistol, "pistol");

            blunderbuss = new ItemFirearm(25.0F, 95, 3.5F, 7.5F, true, 1, "ingotBrass").setUnlocalizedName("steamcraft:blunderbuss").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:weaponBlunderbuss");
            GameRegistry.registerItem(blunderbuss, "blunderbuss");

            if (Config.enableEnhancementRevolver) {
                enhancementRevolver = new ItemEnhancementRevolver().setUnlocalizedName("steamcraft:enhancementRevolver").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:enhancementRevolver");
                GameRegistry.registerItem(enhancementRevolver, "enhancementRevolver");
                SteamcraftRegistry.registerEnhancement((IEnhancement) enhancementRevolver);
            }
            if (Config.enableEnhancementAblaze) {
                enhancementAblaze = new ItemEnhancementFireMusket().setUnlocalizedName("steamcraft:enhancementAblaze").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:enhancementAblaze");
                GameRegistry.registerItem(enhancementAblaze, "enhancementAblaze");
                SteamcraftRegistry.registerEnhancement((IEnhancement) enhancementAblaze);
            }
            if (Config.enableEnhancementSpeedloader) {
                enhancementSpeedloader = new ItemEnhancementSpeedloader().setUnlocalizedName("steamcraft:enhancementSpeedloader").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:enhancementSpeedloader");
                GameRegistry.registerItem(enhancementSpeedloader, "enhancementSpeedloader");
                SteamcraftRegistry.registerEnhancement((IEnhancement) enhancementSpeedloader);
            }
            if (Config.enableEnhancementSilencer) {
                enhancementSilencer = new ItemEnhancementSilencer().setUnlocalizedName("steamcraft:enhancementSilencer").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:enhancementSilencer");
                GameRegistry.registerItem(enhancementSilencer, "enhancementSilencer");
                SteamcraftRegistry.registerEnhancement((IEnhancement) enhancementSilencer);
            }
            if (Config.enableEnhancementRecoil) {
                enhancementRecoil = new ItemEnhancementRecoil().setUnlocalizedName("steamcraft:enhancementRecoil").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:enhancementRecoil");
                GameRegistry.registerItem(enhancementRecoil, "enhancementRecoil");
                SteamcraftRegistry.registerEnhancement((IEnhancement) enhancementRecoil);
            }
            if (Config.enableEnhancementSpeedy) {
                enhancementSpeedy = new ItemEnhancementSpeedy().setUnlocalizedName("steamcraft:enhancementSpeedy").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:enhancementSpeedy");
                GameRegistry.registerItem(enhancementSpeedy, "enhancementSpeedy");
                SteamcraftRegistry.registerEnhancement((IEnhancement) enhancementSpeedy);
            }
        }

        if (Config.enableRL) {
            rocketLauncher = new ItemRocketLauncher(2.0F, 95, 10, 3.5F, 4, "ingotIron").setUnlocalizedName("steamcraft:rocketLauncher").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:weaponRocketLauncher");
            GameRegistry.registerItem(rocketLauncher, "rocketLauncher");
            if (Config.enableEnhancementFastRockets) {
                enhancementFastRockets = new ItemEnhancementFastRockets().setUnlocalizedName("steamcraft:enhancementFastRockets").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:enhancementFastRockets");
                GameRegistry.registerItem(enhancementFastRockets, "enhancementFastRockets");
                SteamcraftRegistry.registerEnhancement((IEnhancement) enhancementFastRockets);
            }
            if (Config.enableEnhancementAirStrike) {
                enhancementAirStrike = new ItemEnhancementAirStrike().setUnlocalizedName("steamcraft:enhancementAirStrike").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:enhancementAirStrike");
                GameRegistry.registerItem(enhancementAirStrike, "enhancementAirStrike");
                SteamcraftRegistry.registerEnhancement((IEnhancement) enhancementAirStrike);
            }
            if (Config.enableEnhancementAmmo) {
                enhancementAmmo = new ItemEnhancementAmmo().setUnlocalizedName("steamcraft:enhancementAmmo").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:enhancementAmmo");
                GameRegistry.registerItem(enhancementAmmo, "enhancementAmmo");
                SteamcraftRegistry.registerEnhancement((IEnhancement) enhancementAmmo);
            }
            if (Config.enableRocket) {
                rocket = new ItemRocketBasic().setUnlocalizedName("steamcraft:rocket").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:ammoRocket");
                GameRegistry.registerItem(rocket, "rocket");
                SteamcraftRegistry.registerRocket((IRocket) rocket);
            }
            if (Config.enableRocketConcussive) {
                rocketConcussive = new ItemRocketConcussive().setUnlocalizedName("steamcraft:rocketConcussive").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:ammoRocketConcussive");
                GameRegistry.registerItem(rocketConcussive, "rocketConcussive");
                SteamcraftRegistry.registerRocket((IRocket) rocketConcussive);
            }
            if (Config.enableRocketMining) {
                rocketMiner = new ItemRocketMining().setUnlocalizedName("steamcraft:rocketMiner").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:ammoRocketMiner");
                GameRegistry.registerItem(rocketMiner, "rocketMiner");
                SteamcraftRegistry.registerRocket((IRocket) rocketMiner);
            }
        }
    }

    private static void registerExosuit() {
        if (Config.enableExosuit) {
            //if (Loader.isModLoaded("Thaumcraft")) {
            //	exoArmorHead = new ItemExosuitArmorThaum(0).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:exoArmorHead").setTextureName("steamcraft:exoArmorHead");
            //}
            //else
            //{
            ArmorMaterial mat = EnumHelper.addArmorMaterial("exosuit", 15, new int[]{2, 5, 4, 1}, 0);
            if (Loader.isModLoaded("Thaumcraft")) {
                exoArmorHead = new ItemExosuitArmorThaum(0, mat).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:exoArmorHead").setTextureName("steamcraft:exoArmorHead");
                GameRegistry.registerItem(exoArmorHead, "exoArmorHead");
                exoArmorBody = new ItemExosuitArmorThaum(1, mat).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:exoArmorBody").setTextureName("steamcraft:exoArmorBody");
                GameRegistry.registerItem(exoArmorBody, "exoArmorBody");
                exoArmorLegs = new ItemExosuitArmorThaum(2, mat).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:exoArmorLegs").setTextureName("steamcraft:exoArmorLegs");
                GameRegistry.registerItem(exoArmorLegs, "exoArmorLegs");
                exoArmorFeet = new ItemExosuitArmorThaum(3, mat).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:exoArmorFeet").setTextureName("steamcraft:exoArmorFeet");
                GameRegistry.registerItem(exoArmorFeet, "exoArmorFeet");
            } else {
                exoArmorHead = new ItemExosuitArmor(0, mat).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:exoArmorHead").setTextureName("steamcraft:exoArmorHead");
                GameRegistry.registerItem(exoArmorHead, "exoArmorHead");
                exoArmorBody = new ItemExosuitArmor(1, mat).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:exoArmorBody").setTextureName("steamcraft:exoArmorBody");
                GameRegistry.registerItem(exoArmorBody, "exoArmorBody");
                exoArmorLegs = new ItemExosuitArmor(2, mat).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:exoArmorLegs").setTextureName("steamcraft:exoArmorLegs");
                GameRegistry.registerItem(exoArmorLegs, "exoArmorLegs");
                exoArmorFeet = new ItemExosuitArmor(3, mat).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:exoArmorFeet").setTextureName("steamcraft:exoArmorFeet");
                GameRegistry.registerItem(exoArmorFeet, "exoArmorFeet");
            }
        }

    }

    private static void registerExosuitUpgrades() {
        if (Config.enableExosuit) {
            if (Config.enableJetpack) {
                jetpack = new ItemExosuitJetpack().setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:jetpack").setTextureName("steamcraft:jetpack");
                GameRegistry.registerItem(jetpack, "jetpack");
            }
            if (Config.enableWings) {
                wings = new ItemExosuitWings().setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:wings").setTextureName("steamcraft:wings");
                GameRegistry.registerItem(wings, "wings");
            }
            if (Config.enablePowerFist) {
                powerFist = new ItemExosuitUpgrade(ExosuitSlot.bodyHand, "steamcraft:textures/models/armor/fireFist.png", null, 0).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:powerFist").setTextureName("steamcraft:powerFist");
                GameRegistry.registerItem(powerFist, "powerFist");
            }
            if (Config.enableExtendoFist) {
                extendoFist = new ItemExosuitUpgrade(ExosuitSlot.bodyHand, "steamcraft:textures/models/armor/extendoFist.png", null, 0).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:extendoFist").setTextureName("steamcraft:extendoFist");
                GameRegistry.registerItem(extendoFist, "extendoFist");
            }
            if (Config.enableThrusters) {
                thrusters = new ItemExosuitSidepack().setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:thrusters").setTextureName("steamcraft:thrusters");
                GameRegistry.registerItem(thrusters, "thrusters");
            }
            if (Config.enableFallAssist) {
                fallAssist = new ItemExosuitUpgrade(ExosuitSlot.bootsTop, "steamcraft:textures/models/armor/fallUpgrade.png", null, 0).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:fallAssist").setTextureName("steamcraft:fallUpgrade");
                GameRegistry.registerItem(fallAssist, "fallAssist");
            }
            if (Config.enableJumpAssist) {
                jumpAssist = new ItemExosuitUpgrade(ExosuitSlot.bootsTop, "steamcraft:textures/models/armor/jumpUpgrade.png", null, 0).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:jumpAssist").setTextureName("steamcraft:jumpUpgrade");
                GameRegistry.registerItem(jumpAssist, "jumpAssist");
            }
            if (Config.enableRunAssist) {
                runAssist = new ItemExosuitUpgrade(ExosuitSlot.legsLegs, "steamcraft:textures/models/armor/runUpgrade.png", null, 0).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:runAssist").setTextureName("steamcraft:runUpgrade");
                GameRegistry.registerItem(runAssist, "runAssist");
            }
            if (Config.enableDoubleJump) {
                doubleJump = new ItemExosuitUpgrade(ExosuitSlot.bootsFeet, "steamcraft:textures/models/armor/doubleJump.png", null, 1).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:doubleJump").setTextureName("steamcraft:doubleJump");
                GameRegistry.registerItem(doubleJump, "doubleJump");
            }
            if (Config.enableCanningMachine) {
                canner = new ItemExosuitUpgrade(ExosuitSlot.legsHips, "steamcraft:textures/models/armor/canner.png", null, 1).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:canner").setTextureName("steamcraft:canningMachine");
                GameRegistry.registerItem(canner, "canner");
            }
            if (Config.enablePitonDeployer) {
                pitonDeployer = new ItemExosuitUpgrade(ExosuitSlot.bodyHand, "steamcraft:textures/models/armor/pitonDeployer.png", null, 1).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:pitonDeployer").setTextureName("steamcraft:pitonDeployer");
                GameRegistry.registerItem(pitonDeployer, "pitonDeployer");
            }
            //antiFire = new ItemExosuitUpgrade(ExosuitSlot.legsHips, "steamcraft:textures/models/armor/antiFire.png",null,1).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:antiFire").setTextureName("steamcraft:antiFire");
            //GameRegistry.registerItem(antiFire, "antiFire");

            if (Config.enableStealthUpgrade) {
                stealthUpgrade = new ItemExosuitUpgrade(ExosuitSlot.legsLegs, "steamcraft:textures/models/armor/stealthUpgrade.png", null, 0).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:stealthUpgrade").setTextureName("steamcraft:stealthUpgrade");
                GameRegistry.registerItem(stealthUpgrade, "stealthUpgrade");
            }

            if (Config.enableEnderShroud) {
                enderShroud = new ItemExosuitUpgrade(ExosuitSlot.vanity, "", null, 0).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:enderShroud").setTextureName("steamcraft:enderShroud");
                GameRegistry.registerItem(enderShroud, "enderShroud");
            }

            if (Config.enableReinforcedTank) {
                reinforcedTank = new ItemTank(Config.reinforcedTankCapacity, "steamcraft:textures/models/armor/reinforcedTank.png", "steamcraft:textures/models/armor/reinforcedTank_grey.png").setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:reinforcedTank").setTextureName("steamcraft:reinforcedTank");
                GameRegistry.registerItem(reinforcedTank, "reinforcedTank");
            }
            if (Config.enableUberReinforcedTank) {
                uberReinforcedTank = new ItemTank(Config.uberReinforcedTankCapacity, "steamcraft:textures/models/armor/uberReinforcedTank.png", "steamcraft:textures/models/armor/uberReinforcedTank_grey.png").setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:uberReinforcedTank").setTextureName("steamcraft:uberReinforcedTank");
                GameRegistry.registerItem(uberReinforcedTank, "uberReinforcedTank");
            }
            //doubleJump = new ItemExosuitUpgrade(ExosuitSlot.bootsTop, "steamcraft:textures/models/armor/fallUpgrade.png",null,0).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:doubleJump").setTextureName("steamcraft:doubleJump");
            //GameRegistry.registerItem(doubleJump, "doubleJump");
        }

    }

    private static void registerMolds() {
        if (Config.enableMold) {
            ingotMold = new ItemIngotMold().setUnlocalizedName("steamcraft:ingotMold").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:moldIngot");
            GameRegistry.registerItem(ingotMold, "ingotMold");
            SteamcraftRegistry.addCarvableMold((ICrucibleMold) ingotMold);
            nuggetMold = new ItemNuggetMold().setUnlocalizedName("steamcraft:nuggetMold").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:moldNugget");
            GameRegistry.registerItem(nuggetMold, "nuggetMold");
            SteamcraftRegistry.addCarvableMold((ICrucibleMold) nuggetMold);
            plateMold = new ItemPlateMold().setUnlocalizedName("steamcraft:plateMold").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:moldPlate");
            GameRegistry.registerItem(plateMold, "plateMold");
            SteamcraftRegistry.addCarvableMold((ICrucibleMold) plateMold);
            blankMold = new Item().setUnlocalizedName("steamcraft:blankMold").setMaxStackSize(1).setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:moldBlank");
            GameRegistry.registerItem(blankMold, "blankMold");
        }
    }

    private static void registerMetals() {
        steamcraftIngot = new ItemSteamcraftIngot().setUnlocalizedName("steamcraft:ingot").setCreativeTab(Steamcraft.tab);
        GameRegistry.registerItem(steamcraftIngot, "steamcraftIngot");
        OreDictionary.registerOre("ingotCopper", new ItemStack(steamcraftIngot, 1, 0));
        OreDictionary.registerOre("ingotZinc", new ItemStack(steamcraftIngot, 1, 1));
        OreDictionary.registerOre("ingotBrass", new ItemStack(steamcraftIngot, 1, 2));


        ItemArmor.ArmorMaterial tool = EnumHelper.addArmorMaterial("MONACLE", 5, new int[]{1, 3, 2, 1}, 15);

        if (Config.enableGoggles) {
            monacle = new ItemSteamcraftGoggles(tool, 2, 0, Items.leather, "Monocle").setUnlocalizedName("steamcraft:monacle").setCreativeTab(Steamcraft.tabTools).setTextureName("steamcraft:monocle");
            GameRegistry.registerItem(monacle, "monacle");
            goggles = new ItemSteamcraftGoggles(tool, 2, 0, Items.leather, "Goggles").setUnlocalizedName("steamcraft:goggles").setCreativeTab(Steamcraft.tabTools).setTextureName("steamcraft:goggles");
            GameRegistry.registerItem(goggles, "goggles");
        }
        if (Config.enableTopHat) {
            tophatNoEmerald = new ItemTophat(tool, 2, 0, false).setUnlocalizedName("steamcraft:tophatNoEmerald").setCreativeTab(Steamcraft.tabTools).setTextureName("steamcraft:tophat");
            GameRegistry.registerItem(tophatNoEmerald, "tophatNoEmerald");
            if (Config.enableEmeraldHat) {
                tophat = new ItemTophat(tool, 2, 0, true).setUnlocalizedName("steamcraft:tophat").setCreativeTab(Steamcraft.tabTools).setTextureName("steamcraft:tophatemerald");
                GameRegistry.registerItem(tophat, "tophat");
            }
        }


        steamcraftNugget = new ItemSteamcraftNugget().setUnlocalizedName("steamcraft:nugget").setCreativeTab(Steamcraft.tab);
        GameRegistry.registerItem(steamcraftNugget, "steamcraftNugget");
        OreDictionary.registerOre("nuggetCopper", new ItemStack(steamcraftNugget, 1, 0));
        OreDictionary.registerOre("nuggetZinc", new ItemStack(steamcraftNugget, 1, 1));
        OreDictionary.registerOre("nuggetIron", new ItemStack(steamcraftNugget, 1, 2));
        OreDictionary.registerOre("nuggetBrass", new ItemStack(steamcraftNugget, 1, 3));

        steamcraftPlate = new ItemSteamcraftPlate().setUnlocalizedName("steamcraft:plate").setCreativeTab(Steamcraft.tab);
        GameRegistry.registerItem(steamcraftPlate, "steamcraftPlate");
        OreDictionary.registerOre("plateSteamcraftCopper", new ItemStack(steamcraftPlate, 1, 0));
        OreDictionary.registerOre("plateSteamcraftZinc", new ItemStack(steamcraftPlate, 1, 1));
        OreDictionary.registerOre("plateSteamcraftIron", new ItemStack(steamcraftPlate, 1, 2));
        OreDictionary.registerOre("plateSteamcraftGold", new ItemStack(steamcraftPlate, 1, 3));
        OreDictionary.registerOre("plateSteamcraftBrass", new ItemStack(steamcraftPlate, 1, 4));
        OreDictionary.registerOre("plateSteamcraftThaumium", new ItemStack(steamcraftPlate, 1, 5));
        OreDictionary.registerOre("plateSteamcraftTerrasteel", new ItemStack(steamcraftPlate, 1, 6));
        OreDictionary.registerOre("plateSteamcraftElementium", new ItemStack(steamcraftPlate, 1, 7));
        OreDictionary.registerOre("plateSteamcraftFiery", new ItemStack(steamcraftPlate, 1, 8));
        OreDictionary.registerOre("plateSteamcraftLead", new ItemStack(steamcraftPlate, 1, 9));
        OreDictionary.registerOre("plateSteamcraftVibrant", new ItemStack(steamcraftPlate, 1, 10));
        OreDictionary.registerOre("plateSteamcraftEnderium", new ItemStack(steamcraftPlate, 1, 11));

        exosuitPlate = new ItemExosuitPlate().setUnlocalizedName("steamcraft:exosuitPlate").setCreativeTab(Steamcraft.tab);
        GameRegistry.registerItem(exosuitPlate, "exosuitPlate");

        if (Config.enableCopperPlate) {
            SteamcraftRegistry.addExosuitPlate(new ExosuitPlate("Copper", new ItemStack(exosuitPlate, 1, 0), "Copper", "Copper", "steamcraft.plate.copper"));
        }
        if (Config.enableIronPlate) {
            SteamcraftRegistry.addExosuitPlate(new ExosuitPlate("Iron", new ItemStack(exosuitPlate, 1, 2), "Iron", "Iron", "steamcraft.plate.iron"));
        }
        if (Config.enableGoldPlate) {
            SteamcraftRegistry.addExosuitPlate(new ExosuitPlate("Gold", new ItemStack(exosuitPlate, 1, 3), "Gold", "Gold", "steamcraft.plate.gold"));
        }
        if (Config.enableBrassPlate) {
            SteamcraftRegistry.addExosuitPlate(new ExosuitPlate("Brass", new ItemStack(exosuitPlate, 1, 4), "Brass", "Brass", "steamcraft.plate.brass"));
        }
    }

    private static void registerFood() {

        steamedFish = new ItemSteamedFood((ItemFood) Items.cooked_fished)
          .setUnlocalizedName("steamcraft:steamedFish").setCreativeTab(Steamcraft.tab);
        GameRegistry.registerItem(steamedFish, "steamedFish");
        ItemFishFood.FishType[] afishtype = ItemFishFood.FishType.values();
        SteamcraftRegistry.addSteamFood(Items.cooked_fished, steamedFish);


        steamedSalmon = new ItemSteamedFood((ItemFood)
          new ItemStack(Items.cooked_fished, 1, 1).getItem());
        steamedSalmon.setUnlocalizedName("steamcraft:steamedSalmon");
        steamedSalmon.setCreativeTab(Steamcraft.tab);
        GameRegistry.registerItem(steamedSalmon, "steamedSalmon");
        ItemFishFood.FishType[] salmonType = ItemFishFood.FishType.values();
        SteamcraftRegistry.addSteamFood(new ItemStack(Items.cooked_fished, 1, 1).getItem(),
          steamedSalmon);
        steamedSalmon.setTextureName("minecraft:fish_salmon_cooked");

        steamedChicken = new ItemSteamedFood((ItemFood) Items.cooked_chicken)
          .setUnlocalizedName("steamcraft:steamedChicken").setCreativeTab(Steamcraft.tab);
        GameRegistry.registerItem(steamedChicken, "steamedChicken");
        SteamcraftRegistry.addSteamFood(Items.cooked_chicken, steamedChicken);

        steamedBeef = new ItemSteamedFood((ItemFood) Items.cooked_beef)
          .setUnlocalizedName("steamcraft:steamedBeef").setCreativeTab(Steamcraft.tab);
        GameRegistry.registerItem(steamedBeef, "steamedBeef");
        SteamcraftRegistry.addSteamFood(Items.cooked_beef, steamedBeef);

        steamedPorkchop = new ItemSteamedFood((ItemFood) Items.cooked_porkchop)
          .setUnlocalizedName("steamcraft:steamedPorkchop").setCreativeTab(Steamcraft.tab);
        GameRegistry.registerItem(steamedPorkchop, "steamedPorkchop");
        SteamcraftRegistry.addSteamFood(Items.cooked_porkchop, steamedPorkchop);
    }

    private static void registerSteamTools() {
        if (Config.enableSteamTools) {
            steamDrill = new ItemSteamDrill().setUnlocalizedName("steamcraft:steamDrill").setCreativeTab(Steamcraft.tabTools);
            GameRegistry.registerItem(steamDrill, "steamDrill");

            steamAxe = new ItemSteamAxe().setUnlocalizedName("steamcraft:steamAxe").setCreativeTab(Steamcraft.tabTools);
            GameRegistry.registerItem(steamAxe, "steamAxe");

            steamShovel = new ItemSteamShovel().setUnlocalizedName("steamcraft:steamShovel").setCreativeTab(Steamcraft.tabTools);
            GameRegistry.registerItem(steamShovel, "steamShovel");
        }
    }

    private static void registerMetalThings() {
        ToolMaterial brass = EnumHelper.addToolMaterial("BRASS", 2, 191, 7.0F, 2.5F, 14);
        ItemArmor.ArmorMaterial mat = EnumHelper.addArmorMaterial("BRASS", 11, new int[]{2, 7, 6, 3}, 9);
        registerToolSet(brass, "Brass", "ingotBrass", true);
        registerArmorSet(mat, "Brass", "ingotBrass", true);
        registerGildedTools();
        registerGildedArmor();
    }

    public static void registerToolSet(ToolMaterial tool, String string, Object repair, boolean addRecipes) {
        Item pick = new ItemSteamcraftPickaxe(tool, repair).setUnlocalizedName("steamcraft:pick" + string).setCreativeTab(Steamcraft.tabTools).setTextureName("steamcraft:pick" + string);
        GameRegistry.registerItem(pick, "pick" + string);
        tools.put("pick" + string, pick);

        Item axe = new ItemSteamcraftAxe(tool, repair).setUnlocalizedName("steamcraft:axe" + string).setCreativeTab(Steamcraft.tabTools).setTextureName("steamcraft:axe" + string);
        GameRegistry.registerItem(axe, "axe" + string);
        tools.put("axe" + string, axe);

        Item shovel = new ItemSteamcraftShovel(tool, repair).setUnlocalizedName("steamcraft:shovel" + string).setCreativeTab(Steamcraft.tabTools).setTextureName("steamcraft:shovel" + string);
        GameRegistry.registerItem(shovel, "shovel" + string);
        tools.put("shovel" + string, shovel);

        Item hoe = new ItemSteamcraftHoe(tool, repair).setUnlocalizedName("steamcraft:hoe" + string).setCreativeTab(Steamcraft.tabTools).setTextureName("steamcraft:hoe" + string);
        GameRegistry.registerItem(hoe, "hoe" + string);
        tools.put("hoe" + string, hoe);

        Item sword = new ItemSteamcraftSword(tool, repair).setUnlocalizedName("steamcraft:sword" + string).setCreativeTab(Steamcraft.tabTools).setTextureName("steamcraft:sword" + string);
        GameRegistry.registerItem(sword, "sword" + string);
        tools.put("sword" + string, sword);
        if (addRecipes) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(pick), "xxx", " s ", " s ",
                    'x', repair, 's', "stickWood"));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(axe), "xx", "xs", " s",
                    'x', repair, 's', "stickWood"));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(shovel), "x", "s", "s",
                    'x', repair, 's', "stickWood"));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(hoe), "xx", " s", " s",
                    'x', repair, 's', "stickWood"));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(sword), "x", "x", "s",
                    'x', repair, 's', "stickWood"));
        }
    }

    public static void registerArmorSet(ItemArmor.ArmorMaterial tool, String string, Object repair, boolean addRecipes) {
        Item helm = new ItemSteamcraftArmor(tool, 2, 0, repair, string).setUnlocalizedName("steamcraft:helm" + string).setCreativeTab(Steamcraft.tabTools).setTextureName("steamcraft:helm" + string);
        GameRegistry.registerItem(helm, "helm" + string);
        tools.put("helm" + string, helm);

        Item chest = new ItemSteamcraftArmor(tool, 2, 1, repair, string).setUnlocalizedName("steamcraft:chest" + string).setCreativeTab(Steamcraft.tabTools).setTextureName("steamcraft:chest" + string);
        GameRegistry.registerItem(chest, "chest" + string);
        tools.put("chest" + string, chest);

        Item legs = new ItemSteamcraftArmor(tool, 2, 2, repair, string).setUnlocalizedName("steamcraft:legs" + string).setCreativeTab(Steamcraft.tabTools).setTextureName("steamcraft:legs" + string);
        GameRegistry.registerItem(legs, "legs" + string);
        tools.put("legs" + string, legs);

        Item feet = new ItemSteamcraftArmor(tool, 2, 3, repair, string).setUnlocalizedName("steamcraft:feet" + string).setCreativeTab(Steamcraft.tabTools).setTextureName("steamcraft:feet" + string);
        GameRegistry.registerItem(feet, "feet" + string);
        tools.put("feet" + string, feet);

        if (addRecipes) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(helm), "xxx", "x x",
                    'x', repair));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(chest), "x x", "xxx", "xxx",
                    'x', repair));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(legs), "xxx", "x x", "x x",
                    'x', repair));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(feet), "x x", "x x",
                    'x', repair));
        }
    }

    public static void registerGildedTools() {
        ToolMaterial tool = EnumHelper.addToolMaterial("GILDEDGOLD", 2, 250, 6.0F, 2.0F, 22);

        Item pick = new ItemSteamcraftPickaxe(tool, new ItemStack(steamcraftIngot, 1, 3)).setUnlocalizedName("steamcraft:pickGildedGold").setCreativeTab(Steamcraft.tabTools).setTextureName("gold_pickaxe");
        GameRegistry.registerItem(pick, "pickGildedGold");
        tools.put("pickGildedGold", pick);

        Item axe = new ItemSteamcraftAxe(tool, new ItemStack(steamcraftIngot, 1, 3)).setUnlocalizedName("steamcraft:axeGildedGold").setCreativeTab(Steamcraft.tabTools).setTextureName("gold_axe");
        GameRegistry.registerItem(axe, "axeGildedGold");
        tools.put("axeGildedGold", axe);

        Item shovel = new ItemSteamcraftShovel(tool, new ItemStack(steamcraftIngot, 1, 3)).setUnlocalizedName("steamcraft:shovelGildedGold").setCreativeTab(Steamcraft.tabTools).setTextureName("gold_shovel");
        GameRegistry.registerItem(shovel, "shovelGildedGold");
        tools.put("shovelGildedGold", shovel);

        Item hoe = new ItemSteamcraftHoe(tool, new ItemStack(steamcraftIngot, 1, 3)).setUnlocalizedName("steamcraft:hoeGildedGold").setCreativeTab(Steamcraft.tabTools).setTextureName("gold_hoe");
        GameRegistry.registerItem(hoe, "hoeGildedGold");
        tools.put("hoeGildedGold", hoe);

        Item sword = new ItemSteamcraftSword(tool, new ItemStack(steamcraftIngot, 1, 3)).setUnlocalizedName("steamcraft:swordGildedGold").setCreativeTab(Steamcraft.tabTools).setTextureName("gold_sword");
        GameRegistry.registerItem(sword, "swordGildedGold");
        tools.put("swordGildedGold", sword);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(pick), "xxx", " s ", " s ",
                'x', new ItemStack(steamcraftIngot, 1, 3), 's', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(axe), "xx", "xs", " s",
                'x', new ItemStack(steamcraftIngot, 1, 3), 's', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(shovel), "x", "s", "s",
                'x', new ItemStack(steamcraftIngot, 1, 3), 's', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(hoe), "xx", " s", " s",
                'x', new ItemStack(steamcraftIngot, 1, 3), 's', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(sword), "x", "x", "s",
                'x', new ItemStack(steamcraftIngot, 1, 3), 's', "stickWood"));
    }

    public static void registerGildedArmor() {
        ItemArmor.ArmorMaterial tool = EnumHelper.addArmorMaterial("GILDEDGOLD", 15, new int[]{2, 6, 5, 2}, 9);

        Item helm = new ItemSteamcraftArmor(tool, 2, 0, new ItemStack(steamcraftIngot, 1, 3), "Gilded").setUnlocalizedName("steamcraft:helmGildedGold").setCreativeTab(Steamcraft.tabTools).setTextureName("gold_helmet");
        GameRegistry.registerItem(helm, "helmGildedGold");
        tools.put("helmGildedGold", helm);

        Item chest = new ItemSteamcraftArmor(tool, 2, 1, new ItemStack(steamcraftIngot, 1, 3), "Gilded").setUnlocalizedName("steamcraft:chestGildedGold").setCreativeTab(Steamcraft.tabTools).setTextureName("gold_chestplate");
        GameRegistry.registerItem(chest, "chestGildedGold");
        tools.put("chestGildedGold", chest);

        Item legs = new ItemSteamcraftArmor(tool, 2, 2, new ItemStack(steamcraftIngot, 1, 3), "Gilded").setUnlocalizedName("steamcraft:legsGildedGold").setCreativeTab(Steamcraft.tabTools).setTextureName("gold_leggings");
        GameRegistry.registerItem(legs, "legsGildedGold");
        tools.put("legsGildedGold", legs);

        Item feet = new ItemSteamcraftArmor(tool, 2, 3, new ItemStack(steamcraftIngot, 1, 3), "Gilded").setUnlocalizedName("steamcraft:feetGildedGold").setCreativeTab(Steamcraft.tabTools).setTextureName("gold_boots");
        GameRegistry.registerItem(feet, "feetGildedGold");
        tools.put("feetGildedGold", feet);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(helm), "xxx", "x x",
                'x', new ItemStack(steamcraftIngot, 1, 3)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(chest), "x x", "xxx", "xxx",
                'x', new ItemStack(steamcraftIngot, 1, 3)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(legs), "xxx", "x x", "x x",
                'x', new ItemStack(steamcraftIngot, 1, 3)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(feet), "x x", "x x",
                'x', new ItemStack(steamcraftIngot, 1, 3)));

    }

    public static Item pick(String string) {
        return tools.get("pick" + string);
    }

    public static Item axe(String string) {
        return tools.get("axe" + string);
    }

    public static Item shovel(String string) {
        return tools.get("shovel" + string);
    }

    public static Item hoe(String string) {
        return tools.get("hoe" + string);
    }

    public static Item sword(String string) {
        return tools.get("sword" + string);
    }

    public static Item helm(String string) {
        return tools.get("helm" + string);
    }

    public static Item chest(String string) {
        return tools.get("chest" + string);
    }

    public static Item legs(String string) {
        return tools.get("legs" + string);
    }

    public static Item feet(String string) {
        return tools.get("feet" + string);
    }

    public static void reregisterPlates() {
        String[] plates = {"Iron", "Copper", "Zinc", "Brass", "Thaumium", "Gold", "Enderium", "Vibrant", "Lead", "Fiery", "Terrasteel", "Elementium"};
        for (String plate : plates) {
            ArrayList<ItemStack> items = OreDictionary.getOres("plate" + plate);
            for (ItemStack item : items) {
                OreDictionary.registerOre("plateSteamcraft" + plate, item);
            }
        }

    }

}
