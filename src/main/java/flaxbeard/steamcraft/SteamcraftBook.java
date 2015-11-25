package flaxbeard.steamcraft;

import cpw.mods.fml.common.Loader;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.book.*;
import flaxbeard.steamcraft.api.exosuit.ExosuitPlate;
import flaxbeard.steamcraft.integration.BotaniaIntegration;
import flaxbeard.steamcraft.integration.CrossMod;
import flaxbeard.steamcraft.integration.thaumcraft.ThaumcraftIntegration;
import flaxbeard.steamcraft.item.ItemExosuitArmor;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;

public class SteamcraftBook {
    //Here's a secret for all of you addon devs: Setting the category of a research to the name of an existing research, with a ! at the beginning, will append to that research instead of making its own.

    public static void registerBookResearch() {
        if (Config.hasAllCrucial) {
            registerRecentCreations();
            registerBasics();
            registerFirearms();
            registerCasting();
            registerGadgets();
            registerSteamPower();
            registerExosuit();
            //registerAuto();
            /* registerMisc(); Uncomment this when shit is added to it.*/
        }

    }

    public static void registerBasics() {
        SteamcraftRegistry.addCategory("category.Basics.name");

        SteamcraftRegistry.addResearch("research.Book.name", "category.Basics.name", new BookPageItem("research.Book.name", "research.Book.0", new ItemStack(SteamcraftItems.book)), new BookPageCrafting("", "book"));
        SteamcraftRegistry.addResearch("research.Ores.name", "category.Basics.name", new BookPageItem("research.Ores.name", "research.Ores.0", new ItemStack(SteamcraftBlocks.steamcraftOre, 1, 0), new ItemStack(SteamcraftBlocks.steamcraftOre, 1, 1)));
        SteamcraftRegistry.addResearch("research.Bits.name", "category.Basics.name", new BookPageItem("research.Bits.name", "research.Bits.0", new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0), new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 5)), new BookPageCrafting("", "piston1", "piston2"), new BookPageText("research.Bits.name", "research.Bits.1"), new BookPageCrafting("", "turbine1", "turbine2"));

        SteamcraftRegistry.addResearch(
                "research.SteamSystem.name",
                "category.Basics.name",
                new BookPageItem(
                        "research.SteamSystem.name",
                        "research.SteamSystem.0",
                        new ItemStack(SteamcraftBlocks.boiler, 1, 0),
                        new ItemStack(SteamcraftBlocks.pipe, 1, 0)
                ),
                new BookPageText("research.SteamSystem.name", "research.SteamSystem.1"),
                new BookPageText("research.SteamSystem.name", "research.SteamSystem.2"),
                new BookPageText("research.SteamSystem.name", "research.SteamSystem.3"),
                new BookPageText("research.SteamSystem.name", "research.SteamSystem.4"),
                new BookPageText("research.SteamSystem.name", "research.SteamSystem.5")
        );

        SteamcraftRegistry.addResearch("research.Camouflage.name", "category.Basics.name", new BookPageItem("research.Camouflage.name", "research.Camouflage.0", new ItemStack(SteamcraftBlocks.pipe), new ItemStack(Blocks.stonebrick)), new BookPageText("research.Camouflage.name", "research.Camouflage.1"));


    }

    public static void registerRecentCreations() {
        //There should be 5 recent creations at all times. Remove the bottom one when you are adding one.

        SteamcraftRegistry.addCategory("category.RecentCreations.name");
        /*
        if (Config.enableBlockPlacer){
            SteamcraftRegistry.addResearch("#research.BlockPlacer.name", "category.RecentCreations.name", new BookPageItem("", "", new ItemStack(Items.apple)));
        }
        */

        if (Config.enableRocket && Config.enableRocketConcussive && Config.enableRocketMining && Config.enableEnhancementFastRockets && Config.enableRL){
            SteamcraftRegistry.addResearch("#research.RocketLauncher.name", "category.RecentCreations.name", new BookPageItem("", "", new ItemStack(Items.apple)));
        }

        if (Config.enableChargingPad && Config.enableCharger) {
            SteamcraftRegistry.addResearch("#research.FillingPad.name", "category.RecentCreations.name", new BookPageItem("", "", new ItemStack(Items.apple)));
        }

        if (Config.enableFirearms) {
            SteamcraftRegistry.addResearch("#research.Musket.name", "category.RecentCreations.name", new BookPageItem("", "", new ItemStack(Items.apple)));
            SteamcraftRegistry.addResearch("#research.Blunderbuss.name", "category.RecentCreations.name", new BookPageItem("", "", new ItemStack(Items.apple)));
            SteamcraftRegistry.addResearch("#research.Pistol.name", "category.RecentCreations.name", new BookPageItem("", "", new ItemStack(Items.apple)));
        }

        if (Config.enableCanister) {
            SteamcraftRegistry.addResearch("#research.Canister.name", "category.RecentCreations.name", new BookPageItem("", "", new ItemStack(Items.apple)));
        }

        if (Config.enableGoggles) {
            SteamcraftRegistry.addResearch("#research.Goggles.name","category.RecentCreations.name",new BookPageItem("","", new ItemStack(Items.apple)));
        }
    }

    public static void registerFirearms() {
        if (Config.enableFirearms || Config.enableRL) {
            SteamcraftRegistry.addCategory("category.Flintlock.name");
            SteamcraftRegistry.addResearch("research.Parts.name", "category.Flintlock.name", new BookPageItem("research.Parts.name", "research.Parts.0", new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 1), new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 2), new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 3), new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 4)),
                    new BookPageCrafting("", "stock"), new BookPageCrafting("", "barrel1", "barrel2"), new BookPageCrafting("", "blunderBarrel1", "blunderBarrel2"), new BookPageCrafting("", "flintlock1", "flintlock2"));
        }
        if (Config.enableFirearms) {
            SteamcraftRegistry.addResearch("research.Musket.name", "category.Flintlock.name", new BookPageItem("research.Musket.name", "research.Musket.0", new ItemStack(SteamcraftItems.musket)), new BookPageCrafting("", "cartridge1", "cartridge2", "cartridge3", "cartridge4"), new BookPageCrafting("", "musket"));
            if (Config.enableSpyglass) {
                SteamcraftRegistry.addResearch("research.EnhancementSpyglass.name", "!research.Musket.name", new BookPageItem("research.EnhancementSpyglass.name", "research.EnhancementSpyglass.0", true, new ItemStack(SteamcraftItems.spyglass)));
            }
            if (Config.enableEnhancementAblaze) {
                SteamcraftRegistry.addResearch("research.EnhancementAblaze.name", "!research.Musket.name", new BookPageItem("research.EnhancementAblaze.name", "research.EnhancementAblaze.0", true, new ItemStack(SteamcraftItems.enhancementAblaze)), new BookPageCrafting("", "ablaze"));
            }
            if (Config.enableEnhancementSpeedloader) {
                SteamcraftRegistry.addResearch("research.EnhancementSpeedloader.name", "!research.Musket.name", new BookPageItem("research.EnhancementSpeedloader.name", "research.EnhancementSpeedloader.0", true, new ItemStack(SteamcraftItems.enhancementSpeedloader)), new BookPageCrafting("", "speedloader1", "speedloader2"));
            }
            SteamcraftRegistry.addResearch("research.Blunderbuss.name", "category.Flintlock.name", new BookPageItem("research.Blunderbuss.name", "research.Blunderbuss.0", new ItemStack(SteamcraftItems.blunderbuss)), new BookPageCrafting("", "blunderbuss"));
            if (Config.enableEnhancementAblaze) {
                SteamcraftRegistry.addResearch("research.EnhancementAblaze2.name", "!research.Blunderbuss.name", new BookPageItem("research.EnhancementAblaze2.name", "research.EnhancementAblaze2.0", true, new ItemStack(SteamcraftItems.enhancementAblaze)), new BookPageCrafting("", "ablaze"));
            }
            if (Config.enableEnhancementSpeedloader) {
                SteamcraftRegistry.addResearch("research.EnhancementSpeedloader2.name", "!research.Blunderbuss.name", new BookPageItem("research.EnhancementSpeedloader2.name", "research.EnhancementSpeedloader2.0", true, new ItemStack(SteamcraftItems.enhancementSpeedloader)), new BookPageCrafting("", "speedloader1", "speedloader2"));
            }
            if (Config.enableEnhancementRecoil) {
                SteamcraftRegistry.addResearch("research.EnhancementRecoil.name", "!research.Blunderbuss.name", new BookPageItem("research.EnhancementRecoil.name", "research.EnhancementRecoil.0", true, new ItemStack(SteamcraftItems.enhancementRecoil)), new BookPageCrafting("", "recoil"));
            }
            SteamcraftRegistry.addResearch("research.Pistol.name", "category.Flintlock.name", new BookPageItem("research.Pistol.name", "research.Pistol.0", new ItemStack(SteamcraftItems.pistol)), new BookPageCrafting("", "pistol"));
            if (Config.enableEnhancementRevolver) {
                SteamcraftRegistry.addResearch("research.EnhancementRevolver.name", "!research.Pistol.name", new BookPageItem("research.EnhancementRevolver.name", "research.EnhancementRevolver.0", true, new ItemStack(SteamcraftItems.enhancementRevolver)), new BookPageCrafting("", "revolver1", "revolver2"));
            }
            if (Config.enableEnhancementSilencer) {
                SteamcraftRegistry.addResearch("research.EnhancementSilencer.name", "!research.Pistol.name", new BookPageItem("research.EnhancementSilencer.name", "research.EnhancementSilencer.0", true, new ItemStack(SteamcraftItems.enhancementSilencer)), new BookPageCrafting("", "silencer"));
            }
            if (Config.enableEnhancementSpeedy) {
                SteamcraftRegistry.addResearch("research.EnhancementSpeedy.name", "!research.Pistol.name", new BookPageItem("research.EnhancementSpeedy.name", "research.EnhancementSpeedy.0", true, new ItemStack(SteamcraftItems.enhancementSpeedy)), new BookPageCrafting("", "speedy1", "speedy2"));
            }
        }
        if (Config.enableRL) {
            SteamcraftRegistry.addResearch("research.RocketLauncher.name", "category.Flintlock.name", new BookPageItem("research.RocketLauncher.name", "research.RocketLauncher.0", new ItemStack(SteamcraftItems.rocketLauncher)), new BookPageCrafting("", "rocket1", "rocket2", "rocket3", "rocket4"));
            if (Config.enableEnhancementFastRockets) {
                SteamcraftRegistry.addResearch("research.EnhancementFastRockets.name", "!research.RocketLauncher.name", new BookPageItem("research.EnhancementFastRockets.name", "research.EnhancementFastRockets.0", true, new ItemStack(SteamcraftItems.enhancementFastRockets)), new BookPageCrafting("", "fastRockets"));
            }
            if (Config.enableEnhancementAmmo) {
                SteamcraftRegistry.addResearch("research.EnhancementAmmo.name", "!research.RocketLauncher.name", new BookPageItem("research.EnhancementAmmo.name", "research.EnhancementAmmo.0", true, new ItemStack(SteamcraftItems.enhancementAmmo)), new BookPageCrafting("", "ammo1", "ammo2"));
            }
            if (Config.enableEnhancementAirStrike) {
                SteamcraftRegistry.addResearch("research.EnhancementAirStrike.name", "!research.RocketLauncher.name", new BookPageItem("research.EnhancementAirStrike.name", "research.EnhancementAirStrike.0", true, new ItemStack(SteamcraftItems.enhancementAirStrike)), new BookPageCrafting("", "airStrike1", "airStrike2"));
            }
            SteamcraftRegistry.addResearch("research.Rockets.name", "category.Flintlock.name");
            if (Config.enableRocket) {
                SteamcraftRegistry.addResearch("research.Rocket.name", "!research.Rockets.name", new BookPageItem("research.Rocket.name", "research.Rocket.0", true, new ItemStack(SteamcraftItems.rocket)), new BookPageCrafting("", "normalRocket1", "normalRocket2"));
            }
            if (Config.enableRocketConcussive) {
                if (Config.enableRocket) {
                    SteamcraftRegistry.addResearch("research.RocketConcussive.name", "!research.Rockets.name", new BookPageItem("research.RocketConcussive.name", "research.RocketConcussive.0", true, new ItemStack(SteamcraftItems.rocketConcussive)), new BookPageCrafting("", "concussiveRocket"));
                } else {
                    SteamcraftRegistry.addResearch("research.RocketConcussive.name", "!research.Rockets.name", new BookPageItem("research.RocketConcussive.name", "research.RocketConcussive.0", true, new ItemStack(SteamcraftItems.rocketConcussive)), new BookPageCrafting("", "concussiveRocket1", "concussiveRocket2"));
                }
            }
            if (Config.enableRocketMining) {
                SteamcraftRegistry.addResearch("research.RocketMining.name", "!research.Rockets.name", new BookPageItem("research.RocketMining.name", "research.RocketMining.0", true, new ItemStack(SteamcraftItems.rocketMiner)), new BookPageCrafting("", "miningRocket"));
            }
        }
    }

    public static void registerCasting() {

        SteamcraftRegistry.addCategory("category.MetalCasting.name");
        if (Config.enableCrucible) {
            SteamcraftRegistry.addResearch("research.Crucible.name", "category.MetalCasting.name", new BookPageItem("research.Crucible.name", "research.Crucible.0", new ItemStack(SteamcraftBlocks.crucible)), new BookPageText("research.Crucible.name", "research.Crucible.1"), new BookPageCrafting("", "crucible"));
            if (Config.enableHellCrucible) {
                SteamcraftRegistry.addResearch("research.HellCrucible.name", "category.MetalCasting.name", new BookPageItem("research.HellCrucible.name", "research.HellCrucible.0", new ItemStack(SteamcraftBlocks.hellCrucible)), new BookPageCrafting("", "hellCrucible"));
            }
        }
        if (Config.enableMold) {
            SteamcraftRegistry.addResearch("research.Mold.name", "category.MetalCasting.name", new BookPageItem("research.Mold.name", "research.Mold.0", new ItemStack(SteamcraftBlocks.mold)), new BookPageText("research.Mold.name", "research.Mold.1"), new BookPageCrafting("", "mold"));
            SteamcraftRegistry.addResearch("research.Molds.name", "category.MetalCasting.name", new BookPageItem("research.Molds.name", "research.Molds.0", new ItemStack(SteamcraftItems.plateMold), new ItemStack(SteamcraftItems.ingotMold), new ItemStack(SteamcraftItems.nuggetMold)), new BookPageCrafting("", "blankMold"), new BookPageCrafting("", "carving"));
        }
        SteamcraftRegistry.addResearch("research.Plates.name", "category.MetalCasting.name", new BookPageItem("research.Plates.name", "research.Plates.0", new ItemStack(SteamcraftItems.steamcraftPlate, 1, 0), new ItemStack(SteamcraftItems.steamcraftPlate, 1, 1), new ItemStack(SteamcraftItems.steamcraftPlate, 1, 2), new ItemStack(SteamcraftItems.steamcraftPlate, 1, 3)));
        SteamcraftRegistry.addResearch("research.Brass.name", "category.MetalCasting.name", new BookPageItem("research.Brass.name", "research.Brass.0", new ItemStack(SteamcraftItems.steamcraftIngot, 1, 2)), new BookPageAlloy("", SteamcraftRecipes.liquidBrass, SteamcraftRecipes.liquidBrass.recipe));

        if (Config.enableCrucible) {
            SteamcraftRegistry.addResearch("research.GildedGold.name", "category.MetalCasting.name", new BookPageItem("research.GildedGold.name", "research.GildedGold.0", new ItemStack(SteamcraftItems.steamcraftIngot, 1, 3)), new BookPageText("research.GildedGold.name", "research.GildedGold.1"), new BookPageDip("", SteamcraftRecipes.liquidGold, 1, new ItemStack(Items.iron_ingot), new ItemStack(SteamcraftItems.steamcraftIngot, 1, 3)));
        }
    }

    public static void registerGadgets() {
        SteamcraftRegistry.addCategory("category.Gadgets.name");

        if (Config.enableWrench) {
            SteamcraftRegistry.addResearch("research.Wrench.name", "category.Gadgets.name", new BookPageItem("research.Wrench.name", "research.Wrench.0", new ItemStack(SteamcraftItems.wrench)), new BookPageText("research.Wrench.name", "research.Wrench.1"), new BookPageCrafting("", "wrench1", "wrench2"));
        }
        if (Config.enableSteamTools) {
            SteamcraftRegistry.addResearch("research.SteamTools.name", "category.Gadgets.name", new BookPageItem("research.SteamTools.name", "research.SteamTools.0", new ItemStack(SteamcraftItems.steamDrill), new ItemStack(SteamcraftItems.steamAxe), new ItemStack(SteamcraftItems.steamShovel)), new BookPageText("research.SteamTools.name", "research.SteamTools.1"), new BookPageCrafting("", "drill1", "drill2", "drill3", "drill4"),
                    new BookPageCrafting("", "axe1", "axe2", "axe3", "axe4"),
                    new BookPageCrafting("", "shovel1", "shovel2", "shovel3", "shovel4"));
        }
        if (Config.enableSpyglass) {
            SteamcraftRegistry.addResearch("research.Spyglass.name", "category.Gadgets.name", new BookPageItem("research.Spyglass.name", "research.Spyglass.0", new ItemStack(SteamcraftItems.spyglass)), new BookPageCrafting("", "spyglass1", "spyglass2"));
        }
        if (Config.enableSurvivalist) {
            if (CrossMod.BAUBLES) {
                SteamcraftRegistry.addResearch("research.Survivalist.name", "category.Gadgets.name", new BookPageItem("research.Survivalist.name", "research.SurvivalistBaubles.0", new ItemStack(SteamcraftItems.survivalist)), new BookPageCrafting("", "survivalist"));
            } else {
                SteamcraftRegistry.addResearch("research.Survivalist.name", "category.Gadgets.name", new BookPageItem("research.Survivalist.name", "research.Survivalist.0", new ItemStack(SteamcraftItems.survivalist)), new BookPageCrafting("", "survivalist"));
            }
        }
        if (Config.enableCanister) {
            ItemStack output = new ItemStack(Items.diamond_sword);
            output.setTagCompound(new NBTTagCompound());
            output.stackTagCompound.setInteger("canned", 0);
            SteamcraftRegistry.addResearch("research.Canister.name", "category.Gadgets.name", new BookPageItem("research.Canister.name", "research.Canister.0", new ItemStack(SteamcraftItems.canister)), new BookPageCrafting("", "canister")
                    , new BookPageCrafting("", true, output, Items.diamond_sword, SteamcraftItems.canister));
        }
        if (Config.enableTopHat) {
            SteamcraftRegistry.addResearch("research.TopHat.name", "category.Gadgets.name", new BookPageItem("research.TopHat.name", "research.TopHat.0", new ItemStack(SteamcraftItems.tophatNoEmerald)), new BookPageCrafting("", "hat"));
            if (Config.enableEmeraldHat) {
                SteamcraftRegistry.addResearch("research.TopHatEmerald.name", "category.Gadgets.name", new BookPageItem("research.TopHatEmerald.name", "research.TopHatEmerald.0", new ItemStack(SteamcraftItems.tophat)), new BookPageCrafting("", "hatEmerald"));
            }
        }
        if (Config.enableGoggles) {
            SteamcraftRegistry.addResearch("research.Goggles.name", "category.Gadgets.name", new BookPageItem("research.Goggles.name", "research.Goggles.0", new ItemStack(SteamcraftItems.goggles), new ItemStack(SteamcraftItems.monacle)), new BookPageCrafting("", "goggles1", "goggles2"), new BookPageCrafting("", "monocle1", "monocle2"));
        }
    }

    public static void registerSteamPower() {
        SteamcraftRegistry.addCategory("category.SteamPower.name");
        SteamcraftRegistry.addResearch("research.Boiler.name", "category.SteamPower.name", new BookPageItem("research.Boiler.name", "research.Boiler.0", new ItemStack(SteamcraftBlocks.boiler)), new BookPageCrafting("", "boiler1", "boiler2"));

        if (Config.enableFlashBoiler) {
            SteamcraftRegistry.addResearch("research.FlashBoiler.name", "category.SteamPower.name", new BookPageItem("research.FlashBoiler.name", "research.FlashBoiler.0", new ItemStack(SteamcraftBlocks.flashBoiler)), new BookPageText("research.FlashBoiler.name", "research.FlashBoiler.1"), new BookPageCrafting("", "flashBoiler1", "flashBoiler2"));
        }
        SteamcraftRegistry.addResearch("research.Pipe.name", "category.SteamPower.name", new BookPageItem("research.Pipe.name", "research.Pipe.0", new ItemStack(SteamcraftBlocks.pipe)), new BookPageCrafting("", "pipe1", "pipe2"), new BookPageText("research.Pipe.name", "research.Pipe.1"), new BookPageCrafting("", "valvePipe"));

        if (Config.enableRuptureDisc) {
            SteamcraftRegistry.addResearch("research.RuptureDisc.name", "category.SteamPower.name", new BookPageItem("research.RuptureDisc.name", "research.RuptureDisc.0", new ItemStack(SteamcraftBlocks.ruptureDisc)), new BookPageText("research.RuptureDisc.name", "research.RuptureDisc.1"), new BookPageCrafting("", "disc"));
        }

        if (Config.enableHorn) {
            SteamcraftRegistry.addResearch("research.Whistle.name", "category.SteamPower.name", new BookPageItem("research.Whistle.name", "research.Whistle.0", new ItemStack(SteamcraftBlocks.horn)), new BookPageCrafting("", "whistle1", "whistle2"));
        }

        if (Config.enableGauge) {
            SteamcraftRegistry.addResearch("research.Gauge.name", "category.SteamPower.name", new BookPageItem("research.Gauge.name", "research.Gauge.0", new ItemStack(SteamcraftBlocks.gauge)), new BookPageCrafting("", "gauge"));
        }
        SteamcraftRegistry.addResearch("research.Tank.name", "category.SteamPower.name", new BookPageItem("research.Tank.name", "research.Tank.0", new ItemStack(SteamcraftBlocks.tank)), new BookPageCrafting("", "tank1", "tank2"));
        SteamcraftRegistry.addResearch("research.CreativeTank.name", "category.NOTREAL.name", new BookPageItem("research.CreativeTank.name", "research.CreativeTank.0", new ItemStack(Items.bowl)));
        SteamcraftRegistry.bookRecipes.put(new ItemStack(SteamcraftBlocks.tank, 1, 1), MutablePair.of("research.CreativeTank.name", 0));

        if (Config.enableCharger) {
            SteamcraftRegistry.addResearch("research.Filler.name", "category.SteamPower.name", new BookPageItem("research.Filler.name", "research.Filler.0", new ItemStack(SteamcraftBlocks.charger)), new BookPageText("research.Filler.name", "research.Filler.1"), new BookPageCrafting("", "filler1", "filler2"));
        }

        if (Config.enableChargingPad && Config.enableCharger) {
            SteamcraftRegistry.addResearch("research.FillingPad.name", "category.SteamPower.name", new BookPageItem("research.FillingPad.name", "research.FillingPad.0", new ItemStack(SteamcraftBlocks.chargingPad)), new BookPageCrafting("", "fillingPad1", "fillingPad2"));
        }

        if (Config.enableHeater) {
            SteamcraftRegistry.addResearch("research.Heater.name", "category.SteamPower.name", new BookPageItem("research.Heater.name", "research.Heater.0", new ItemStack(SteamcraftBlocks.heater)), new BookPageCrafting("", "heater1", "heater2"));
        }

        if (Config.enableMortar && Config.enableAstrolabe) {
            SteamcraftRegistry.addResearch("research.ItemMortar.name", "category.SteamPower.name", new BookPageItem("research.ItemMortar.name", "research.ItemMortar.0", new ItemStack(SteamcraftBlocks.itemMortar)), new BookPageText("research.ItemMortar.name", "research.ItemMortar.1"), new BookPageCrafting("", "astrolabe"), new BookPageCrafting("", "itemMortar2", "itemMortar3"));
        }

        if (Config.enableHammer) {
            SteamcraftRegistry.addResearch("research.Hammer.name", "category.SteamPower.name", new BookPageItem("research.Hammer.name", "research.Hammer.0", new ItemStack(SteamcraftBlocks.hammer)), new BookPageText("research.Hammer.name", "research.Hammer.1"), new BookPageCrafting("", "hammer1", "hammer2"));
        }
        if (Config.enablePump) {
            SteamcraftRegistry.addResearch("research.Screw.name", "category.SteamPower.name", new BookPageItem("research.Screw.name", "research.Screw.0", new ItemStack(SteamcraftBlocks.pump)), new BookPageCrafting("", "pump1", "pump2"));
        }

        if (Config.enableSmasher) {
            SteamcraftRegistry.addResearch("research.Smasher.name", "category.SteamPower.name", new BookPageItem("research.Smasher.name", "research.Smasher.0", new ItemStack(SteamcraftBlocks.smasher)), new BookPageText("research.Smasher.name", "research.Smasher.1"), new BookPageCrafting("", "smasher1", "smasher2", "smasher3", "smasher4"));
        }

        if (Config.enableThumper) {
            SteamcraftRegistry.addResearch("research.Thumper.name", "category.SteamPower.name", new BookPageItem("research.Thumper.name", "research.Thumper.0", new ItemStack(SteamcraftBlocks.thumper)), new BookPageText("research.Thumper.name", "research.Thumper.1"), new BookPageCrafting("", "thumper1", "thumper2"));
        }

        if (Config.enableFan) {
            SteamcraftRegistry.addResearch("research.Fan.name", "category.SteamPower.name", new BookPageItem("research.Fan.name", "research.Fan.0", new ItemStack(SteamcraftBlocks.fan)), new BookPageCrafting("", "fan1", "fan2"));
            if (Config.enableVacuum) {
                SteamcraftRegistry.addResearch("research.Vacuum.name", "category.SteamPower.name", new BookPageItem("research.Vacuum.name", "research.Vacuum.0", new ItemStack(SteamcraftBlocks.vacuum)), new BookPageCrafting("", "vacuum1", "vacuum2"));
            }
        }

        if (Config.enableFluidSteamConverter) {
            SteamcraftRegistry.addResearch("research.FSC.name", "category.SteamPower.name", new BookPageItem("research.FSC.name", "research.FSC.0", new ItemStack(SteamcraftBlocks.fluidSteamConverter)), new BookPageCrafting("", "fsc1", "fsc2"), new BookPageText("", "research.FSC.1"), new BookPageText("", "research.FSC.2"));
        }

    }

    public static void registerExosuit() {
        if (Config.enableExosuit && Config.enableEngineering) {
            SteamcraftRegistry.addCategory("category.Exosuit.name");
            SteamcraftRegistry.addResearch("research.Exosuit.name", "category.Exosuit.name", new BookPageItem("research.Exosuit.name", "research.Exosuit.0", new ItemStack(SteamcraftItems.exoArmorHead), new ItemStack(SteamcraftItems.exoArmorBody), new ItemStack(SteamcraftItems.exoArmorLegs), new ItemStack(SteamcraftItems.exoArmorFeet)),
                    new BookPageText("research.Exosuit.name", "research.Exosuit.1"), new BookPageCrafting("", "engineering1", "engineering2"), new BookPageCrafting("", "exoHead"), new BookPageCrafting("", "exoBody"), new BookPageCrafting("", "exoLegs"), new BookPageCrafting("", "exoFeet"));
            ItemStack[] stacks = new ItemStack[4];
            for (int i = 0; i < 4; i++) {
                ItemStack stack = new ItemStack(SteamcraftItems.exoArmorBody);
                stack.setTagCompound(new NBTTagCompound());
                ItemStack plate = null;
                Object item = SteamcraftRegistry.plates.values().toArray(new ExosuitPlate[0])[i].getItem();
                if (item instanceof String) {
                    if (OreDictionary.getOres((String) item).size() > 0) {
                        plate = OreDictionary.getOres((String) item).get(0);
                    }
                } else if (item instanceof ItemStack) {
                    plate = (ItemStack) item;
                }
                ((ItemExosuitArmor) stack.getItem()).setInventorySlotContents(stack, 1, plate);
                stacks[i] = stack;
            }
            SteamcraftRegistry.addResearch("research.ExoTank.name", "category.Exosuit.name", new BookPageItem("research.ExoTank.name", "research.ExoTank.0", new ItemStack(SteamcraftBlocks.tank)));
            SteamcraftRegistry.addResearch("research.ExoTankBase.name", "!research.ExoTank.name", new BookPageItem("research.ExoTankBase.name", "research.ExoTankBase.0", true, new ItemStack(SteamcraftBlocks.tank)));
            if (Config.enableReinforcedTank) {
                SteamcraftRegistry.addResearch("research.ExoTankReinforced.name", "!research.ExoTank.name", new BookPageItem("research.ExoTankReinforced.name", "research.ExoTankReinforced.0", true, new ItemStack(SteamcraftItems.reinforcedTank)), new BookPageCrafting("", "reinforcedTank1", "reinforcedTank2"));
            }
            if (Config.enableUberReinforcedTank) {
                SteamcraftRegistry.addResearch("research.ExoTankUberReinforced.name", "!research.ExoTank.name", new BookPageItem("research.ExoTankUberReinforced.name", "research.ExoTankUberReinforced.0", true, new ItemStack(SteamcraftItems.uberReinforcedTank)), new BookPageCrafting("", "uberReinforcedTank1", "uberReinforcedTank2"));
            }

            SteamcraftRegistry.addResearch("research.ExoPlates.name", "category.Exosuit.name", new BookPageItem("research.ExoPlates.name", "research.ExoPlates.0", stacks), new BookPageText("", "research.ExoPlates.1"));

            if (Config.enableCopperPlate) {
                SteamcraftRegistry.addResearch("research.PlateCopper.name", "!research.ExoPlates.name", new BookPageItem("research.PlateCopper.name", "research.PlateCopper.0", true, new ItemStack(SteamcraftItems.exosuitPlate, 1, 0)), new BookPageCrafting("", "exoCopper"));
            }
            if (Config.enableIronPlate) {
                SteamcraftRegistry.addResearch("research.PlateIron.name", "!research.ExoPlates.name", new BookPageItem("research.PlateIron.name", "research.PlateIron.0", true, new ItemStack(SteamcraftItems.exosuitPlate, 1, 2)), new BookPageCrafting("", "exoIron"));
            }
            if (Config.enableBrassPlate) {
                SteamcraftRegistry.addResearch("research.PlateBrass.name", "!research.ExoPlates.name", new BookPageItem("research.PlateBrass.name", "research.PlateBrass.0", true, new ItemStack(SteamcraftItems.exosuitPlate, 1, 4)), new BookPageCrafting("", "exoBrass"));
            }
            if (Config.enableGoldPlate) {
                SteamcraftRegistry.addResearch("research.PlateGold.name", "!research.ExoPlates.name", new BookPageItem("research.PlateGold.name", "research.PlateGold.0", true, new ItemStack(SteamcraftItems.exosuitPlate, 1, 3)), new BookPageCrafting("", "exoGold"));
            }
            if (Config.enableLeadPlate && OreDictionary.getOres("ingotLead").size() > 0) {
                SteamcraftRegistry.addResearch("research.PlateLead.name", "!research.ExoPlates.name", new BookPageItem("research.PlateLead.name", "research.PlateLead.0", true, new ItemStack(SteamcraftItems.exosuitPlate, 1, 11)), new BookPageCrafting("", "exoLead"));
            }
            if (Config.enableVibrantPlate && CrossMod.ENDER_IO) {
                SteamcraftRegistry.addResearch("research.PlateVibrant.name", "!research.ExoPlates.name", new BookPageItem("research.PlateVibrant.name", "research.PlateVibrant.0", true, new ItemStack(SteamcraftItems.exosuitPlate, 1, 12)), new BookPageCrafting("", "exoVibrant"));
            }
            if (Config.enableEnderiumPlate && CrossMod.THERMAL_FOUNDATION) {
                SteamcraftRegistry.addResearch("research.PlateEnderium.name", "!research.ExoPlates.name", new BookPageItem("research.PlateEnderium.name", "research.PlateEnderium.0", true, new ItemStack(SteamcraftItems.exosuitPlate, 1, 13)), new BookPageCrafting("", "exoEnderium"));
            }
            if (Config.enableThaumiumPlate && CrossMod.THAUMCRAFT) {
                SteamcraftRegistry.addResearch("research.PlateThaumium.name", "!research.ExoPlates.name", new BookPageItem("research.PlateThaumium.name", "research.PlateThaumium.0", true, new ItemStack(SteamcraftItems.exosuitPlate, 1, 5)), new BookPageCrafting("", "exoThaumium"));
            }
            if (CrossMod.BOTANIA) {
                if (Config.enableTerrasteelPlate) {
                    SteamcraftRegistry.addResearch("research.PlateTerrasteel.name", "!research.ExoPlates.name", new BookPageItem("research.PlateTerrasteel.name", "research.PlateTerrasteel.0", true, new ItemStack(SteamcraftItems.exosuitPlate, 1, 6)), new BookPageCrafting("", "exoTerrasteel"));
                }
                if (Config.enableElementiumPlate) {
                    SteamcraftRegistry.addResearch("research.PlateElementium.name", "!research.ExoPlates.name", new BookPageItem("research.PlateElementium.name", "research.PlateElementium.0", true, new ItemStack(SteamcraftItems.exosuitPlate, 1, 7)), new BookPageCrafting("", "exoElementium"));
                }
            }
            if (CrossMod.TWILIGHT_FOREST) {
                if (Config.enableFieryPlate) {
                    SteamcraftRegistry.addResearch("research.PlateFiery.name", "!research.ExoPlates.name", new BookPageItem("research.PlateFiery.name", "research.PlateFiery.0", true, new ItemStack(SteamcraftItems.exosuitPlate, 1, 8)), new BookPageCrafting("", "exoFiery"));
                }
                if (Config.enableYetiPlate) {
                    SteamcraftRegistry.addResearch("research.PlateYeti.name", "!research.ExoPlates.name", new BookPageItem("research.PlateYeti.name", "research.PlateYeti.0", true, new ItemStack(SteamcraftItems.exosuitPlate, 1, 9)), new BookPageCrafting("", "exoYeti"));
                }
            }
            if (Config.enableSadistPlate && CrossMod.BLOOD_MAGIC) {
                SteamcraftRegistry.addResearch("research.PlateSadist.name", "!research.ExoPlates.name", new BookPageItem("research.PlateSadist.name", "research.PlateSadist.0", true, new ItemStack(SteamcraftItems.exosuitPlate, 1, 10)), new BookPageCrafting("", "exoSadist"));
            }
            ItemStack[] stacks2 = new ItemStack[4];
            for (int i = 0; i < 4; i++) {
                ItemStack stack = new ItemStack(SteamcraftItems.exoArmorBody);
                stack.setTagCompound(new NBTTagCompound());
                ItemStack dye = new ItemStack(Items.dye, 1, i);
                ((ItemExosuitArmor) stack.getItem()).setInventorySlotContents(stack, 2, dye);
                stacks2[i] = stack;
            }
            SteamcraftRegistry.addResearch("research.ExoVanity.name", "category.Exosuit.name", new BookPageItem("research.ExoVanity.name", "research.ExoVanity.0", stacks2));
            SteamcraftRegistry.addResearch("research.ExoDyes.name", "!research.ExoVanity.name", new BookPageItem("research.ExoDyes.name", "research.ExoDyes.0", true, new ItemStack(Items.dye, 1, 0), new ItemStack(Items.dye, 1, 1), new ItemStack(Items.dye, 1, 2), new ItemStack(Items.dye, 1, 3)));
            if (Config.enableEnderShroud) {
                SteamcraftRegistry.addResearch("research.EnderShroud.name", "!research.ExoVanity.name", new BookPageItem("research.EnderShroud.name", "research.EnderShroud.0", true, new ItemStack(SteamcraftItems.enderShroud)), new BookPageCrafting("", "enderShroud"));
            }
            SteamcraftRegistry.addResearch("research.ExoHeadHelm.name", "category.Exosuit.name");
            if (CrossMod.BOTANIA) {
                SteamcraftRegistry.addResearch("research.FloralLaurel.name", "!research.ExoHeadHelm.name", new BookPageItem("research.FloralLaurel.name", "research.FloralLaurel.0", true, new ItemStack(BotaniaIntegration.floralLaurel)), new BookPageCrafting("", "floralLaurel0", "floralLaurel1", "floralLaurel2", "floralLaurel3", "floralLaurel4", "floralLaurel5", "floralLaurel6", "floralLaurel7", "floralLaurel8", "floralLaurel9", "floralLaurel0", "floralLaurel11", "floralLaurel12", "floralLaurel13", "floralLaurel14", "floralLaurel15"));
            }
            if (Config.enableTopHat) {
                SteamcraftRegistry.addResearch("research.ExoTopHat.name", "!research.ExoHeadHelm.name", new BookPageItem("research.ExoTopHat.name", "research.ExoTopHat.0", true, new ItemStack(SteamcraftItems.tophatNoEmerald), new ItemStack(SteamcraftItems.tophat)));
            }
            SteamcraftRegistry.addResearch("research.ExoHeadGoggle.name", "category.Exosuit.name");
            if (CrossMod.THAUMCRAFT) {
                SteamcraftRegistry.addResearch("research.Mask.name", "!research.ExoHeadGoggle.name", new BookPageItem("research.Mask.name", "research.Mask.0", true, new ItemStack(ThaumcraftIntegration.goggleUpgrade)), new BookPageCrafting("", "mask"));
            }
            if (Config.enableGoggles) {
                SteamcraftRegistry.addResearch("research.ExoGoggles.name", "!research.ExoHeadGoggle.name", new BookPageItem("research.ExoGoggles.name", "research.ExoGoggles.0", true, new ItemStack(SteamcraftItems.goggles), new ItemStack(SteamcraftItems.monacle)));
            }
            SteamcraftRegistry.addResearch("research.ExoBack.name", "category.Exosuit.name");
            if (Config.enableJetpack) {
                SteamcraftRegistry.addResearch("research.Jetpack.name", "!research.ExoBack.name", new BookPageItem("research.Jetpack.name", "research.Jetpack.0", true, new ItemStack(SteamcraftItems.jetpack)), new BookPageCrafting("", "jetpack1", "jetpack2"));
            }
            if (Config.enableWings) {
                SteamcraftRegistry.addResearch("research.Wings.name", "!research.ExoBack.name", new BookPageItem("research.Wings.name", "research.Wings.0", true, new ItemStack(SteamcraftItems.wings)), new BookPageCrafting("", "wings1", "wings2"));
            }

            SteamcraftRegistry.addResearch("research.ExoArm.name", "category.Exosuit.name");
            if (Config.enablePowerFist) {
                SteamcraftRegistry.addResearch("research.Fist.name", "!research.ExoArm.name", new BookPageItem("research.Fist.name", "research.Fist.0", true, new ItemStack(SteamcraftItems.powerFist)), new BookPageCrafting("", "powerFist1", "powerFist2"));
            }
            if (Config.enableExtendoFist) {
                SteamcraftRegistry.addResearch("research.ExtendoFist.name", "!research.ExoArm.name", new BookPageItem("research.ExtendoFist.name", "research.ExtendoFist.0", true, new ItemStack(SteamcraftItems.extendoFist)), new BookPageCrafting("", "extendoFist1", "extendoFist2"));
            }
            if (Config.enablePitonDeployer) {
                SteamcraftRegistry.addResearch("research.PitonDeployer.name", "!research.ExoArm.name", new BookPageItem("research.PitonDeployer.name", "research.PitonDeployer.0", true, new ItemStack(SteamcraftItems.pitonDeployer)), new BookPageCrafting("", "pitonDeployer"));
            }
            SteamcraftRegistry.addResearch("research.ExoHip.name", "category.Exosuit.name");
            if (Config.enableThrusters) {
                SteamcraftRegistry.addResearch("research.Thrusters.name", "!research.ExoHip.name", new BookPageItem("research.Thrusters.name", "research.Thrusters.0", true, new ItemStack(SteamcraftItems.thrusters)), new BookPageCrafting("", "thrusters1", "thrusters2"));
            }
            if (Config.enableCanningMachine) {
                SteamcraftRegistry.addResearch("research.Canner.name", "!research.ExoHip.name", new BookPageItem("research.Canner.name", "research.Canner.0", true, new ItemStack(SteamcraftItems.canner)), new BookPageCrafting("", "canner1", "canner2", "canner3", "canner4"));
            }

            SteamcraftRegistry.addResearch("research.ExoLeg.name", "category.Exosuit.name");
            if (Config.enableRunAssist) {
                SteamcraftRegistry.addResearch("research.RunAssist.name", "!research.ExoLeg.name", new BookPageItem("research.RunAssist.name", "research.RunAssist.0", true, new ItemStack(SteamcraftItems.runAssist)), new BookPageCrafting("", "runAssist1", "runAssist2"));
            }
            if (Config.enableStealthUpgrade) {
                SteamcraftRegistry.addResearch("research.StealthUpgrade.name", "!research.ExoLeg.name", new BookPageItem("research.StealthUpgrade.name", "research.StealthUpgrade.0", true, new ItemStack(SteamcraftItems.stealthUpgrade)), new BookPageCrafting("", "stealthUpgrade"));
            }
            SteamcraftRegistry.addResearch("research.ExoHeel.name", "category.Exosuit.name");
            if (Config.enableFallAssist) {
                SteamcraftRegistry.addResearch("research.FallAssist.name", "!research.ExoHeel.name", new BookPageItem("research.FallAssist.name", "research.FallAssist.0", true, new ItemStack(SteamcraftItems.fallAssist)), new BookPageCrafting("", "noFall"));
            }
            if (Config.enableJumpAssist) {
                SteamcraftRegistry.addResearch("research.JumpAssist.name", "!research.ExoHeel.name", new BookPageItem("research.JumpAssist.name", "research.JumpAssist.0", true, new ItemStack(SteamcraftItems.jumpAssist)), new BookPageCrafting("", "jumpAssist1", "jumpAssist2"));
            }
            // Uncomment this when there is actually lang things.
            /*if (Config.enableAnchorHeels) {
                if (Config.enableAnchorAnvilRecipe) {
                    SteamcraftRegistry.addResearch(
                      "research.AnchorHeels.name",
                      "!research.AnchorHeels.name",
                      new BookPageItem("research.AnchorHeels.name",
                        "research.AnchorHeelsAnvil.0", true,
                        new ItemStack(SteamcraftItems.anchorHeels)),
                      new BookPageCrafting("", "anchorHeels")
                    );
                } else {
                    SteamcraftRegistry.addResearch(
                      "research.AnchorHeels.name",
                      "!research.AnchorHeels.name",
                      new BookPageItem("research.AnchorHeels.name",
                        "research.AnchorHeels.0", true,
                        new ItemStack(SteamcraftItems.anchorHeels)),
                      new BookPageCrafting("", "anchorHeels")
                    );
                }
            }*/
            SteamcraftRegistry.addResearch("research.ExoFoot.name", "category.Exosuit.name");
            if (Config.enableDoubleJump) {
                SteamcraftRegistry.addResearch("research.DoubleJump.name", "!research.ExoFoot.name", new BookPageItem("research.DoubleJump.name", "research.DoubleJump.0", true, new ItemStack(SteamcraftItems.doubleJump)), new BookPageCrafting("", "doubleJump1", "doubleJump2"));
            }
        }
    }

    /*
    public static  void registerAuto(){
        SteamcraftRegistry.addCategory("category.Automation.name");

        if (Config.enableBlockPlacer){
            SteamcraftRegistry.addResearch("research.BlockPlacer.name", "category.Automation.name", new BookPageItem("research.BlockPlacer.name", "research.BlockPlacer.0", true, new ItemStack(SteamcraftBlocks.blockPlacer)), new BookPageCrafting("", "blockPlacer", "blockPlacer1"));
        }
    }
    */

    public static void registerMisc() {

    }

    public static ItemStack[] getOreDict(String str) {
        ArrayList<ItemStack> planks = new ArrayList<ItemStack>();
        for (ItemStack stack : OreDictionary.getOres(str)) {
            planks.add(stack);
        }
        if (str == "plankWood") {
            planks.add(new ItemStack(Blocks.planks, 1, 1));
            planks.add(new ItemStack(Blocks.planks, 1, 2));
            planks.add(new ItemStack(Blocks.planks, 1, 3));
            planks.add(new ItemStack(Blocks.planks, 1, 4));
            planks.add(new ItemStack(Blocks.planks, 1, 5));
        }
        return planks.toArray(new ItemStack[0]);
    }
}
