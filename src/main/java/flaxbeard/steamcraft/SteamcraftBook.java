package flaxbeard.steamcraft;

import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.book.*;
import flaxbeard.steamcraft.api.exosuit.ExosuitPlate;
import flaxbeard.steamcraft.init.misc.integration.CrossMod;
import flaxbeard.steamcraft.item.armor.exosuit.ItemExosuitArmor;
import flaxbeard.steamcraft.misc.DrillHeadMaterial;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static flaxbeard.steamcraft.init.blocks.SteamNetworkBlocks.Blocks.*;
import static flaxbeard.steamcraft.init.blocks.OreBlocks.Blocks.*;
import static flaxbeard.steamcraft.init.blocks.CastingBlocks.Blocks.*;
import static flaxbeard.steamcraft.init.blocks.SteamMachineryBlocks.Blocks.*;
import static flaxbeard.steamcraft.init.blocks.MiscellaneousBlocks.Blocks.*;
import static flaxbeard.steamcraft.init.items.tools.GadgetItems.Items.*;
import static flaxbeard.steamcraft.init.items.CraftingComponentItems.Items.*;
import static flaxbeard.steamcraft.init.items.firearms.FirearmItems.Items.*;
import static flaxbeard.steamcraft.init.items.firearms.FirearmUpgradeItems.Items.*;
import static flaxbeard.steamcraft.init.items.firearms.FirearmAmmunitionItems.Items.*;
import static flaxbeard.steamcraft.init.items.MetalcastingItems.Items.*;
import static flaxbeard.steamcraft.init.items.MetalItems.Items.*;
import static flaxbeard.steamcraft.init.items.armor.ArmorItems.Items.*;
import static flaxbeard.steamcraft.init.items.tools.ToolItems.Items.*;
import static flaxbeard.steamcraft.init.items.tools.ToolUpgradeItems.Items.*;
import static flaxbeard.steamcraft.init.items.armor.ExosuitUpgradeItems.Items.*;
import static flaxbeard.steamcraft.init.items.armor.ExosuitUpgradeItems.PlateItems.*;
import static flaxbeard.steamcraft.init.misc.DefaultCrucibleLiquids.Liquids.*;

public class SteamcraftBook {
    //Here's a secret for all of you addon devs: Setting the category of a research to the name of an existing research, with a ! at the beginning, will append to that research instead of making its own.

    // TODO: Change if (Config.XXX) to if (thing.isEnabled())
    public static void registerBookResearch() {
        if (Config.hasAllCrucial) {
            registerBasics();
            registerFirearms();
            registerCasting();
            registerGadgets();
            registerSteamPower();
            registerExosuit();
            registerMisc();
            //registerAuto();
        }

    }

    public static void registerBasics() {
        SteamcraftRegistry.addCategory("category.Basics.name");

        SteamcraftRegistry.addResearch(
          "research.Book.name",
          "category.Basics.name",
          new BookPageItem("research.Book.name", "research.Book.0", new ItemStack(BOOK.getItem())),
          new BookPageCrafting("", "book")
        );

        SteamcraftRegistry.addResearch(
          "research.Ores.name",
          "category.Basics.name",
          new BookPageItem(
            "research.Ores.name",
            "research.Ores.0",
            OVERWORLD_COPPER_ORE.createItemStack(),
            OVERWORLD_ZINC_ORE.createItemStack()
          )
        );

        SteamcraftRegistry.addResearch(
          "research.Bits.name",
          "category.Basics.name",
          new BookPageItem(
            "research.Bits.name",
            "research.Bits.0",
            BRASS_PISTON.createItemStack(),
            BRASS_TURBINE.createItemStack()
          ),
          new BookPageCrafting("", "piston1", "piston2"),
          new BookPageText("research.Bits.name", "research.Bits.1"),
          new BookPageCrafting("", "turbine1", "turbine2")
        );

        SteamcraftRegistry.addResearch(
          "research.SteamSystem.name",
          "category.Basics.name",
          new BookPageItem(
            "research.SteamSystem.name",
            "research.SteamSystem.0",
            new ItemStack(BOILER.getBlock(), 1, 0),
            new ItemStack(PIPE.getBlock(), 1, 0)
          ),
          new BookPageText("research.SteamSystem.name", "research.SteamSystem.1"),
          new BookPageText("research.SteamSystem.name", "research.SteamSystem.2"),
          new BookPageText("research.SteamSystem.name", "research.SteamSystem.3"),
          new BookPageText("research.SteamSystem.name", "research.SteamSystem.4"),
          new BookPageText("research.SteamSystem.name", "research.SteamSystem.5")
        );

        SteamcraftRegistry.addResearch(
          "research.Camouflage.name",
          "category.Basics.name",
          new BookPageItem(
            "research.Camouflage.name",
            "research.Camouflage.0",
            new ItemStack(PIPE.getBlock()),
            new ItemStack(Blocks.STONEBRICK)),
          new BookPageText("research.Camouflage.name", "research.Camouflage.1")
        );
    }

    public static void registerFirearms() {
        if (Config.enableFirearms || Config.enableRL) {
            SteamcraftRegistry.addCategory("category.Flintlock.name");
            SteamcraftRegistry.addResearch("research.Parts.name", "category.Flintlock.name",
              new BookPageItem("research.Parts.name", "research.Parts.0",
                BLUNDERBUSS_BARREL.createItemStack(),
                FLINTLOCK.createItemStack(),
                GUN_STOCK.createItemStack(),
                IRON_BARREL.createItemStack()
              ),
              new BookPageCrafting("", "stock"), new BookPageCrafting("", "barrel1", "barrel2"),
              new BookPageCrafting("", "blunderBarrel1", "blunderBarrel2"),
              new BookPageCrafting("", "flintlock1", "flintlock2"));
        }
        if (Config.enableFirearms) {
            SteamcraftRegistry.addResearch("research.Musket.name", "category.Flintlock.name",
              new BookPageItem("research.Musket.name", "research.Musket.0", new ItemStack(MUSKET.getItem())),
              new BookPageCrafting("", "cartridge1", "cartridge2", "cartridge3", "cartridge4"),
              new BookPageCrafting("", "musket"));
            if (Config.enableSpyglass) {
                SteamcraftRegistry.addResearch("research.EnhancementSpyglass.name", "!research.Musket.name",
                  new BookPageItem("research.EnhancementSpyglass.name", "research.EnhancementSpyglass.0", true,
                    new ItemStack(SPYGLASS.getItem())));
            }
            if (Config.enableEnhancementAblaze) {
                SteamcraftRegistry.addResearch("research.EnhancementAblaze.name", "!research.Musket.name",
                  new BookPageItem("research.EnhancementAblaze.name", "research.EnhancementAblaze.0", true,
                    new ItemStack(BLAZE_BARREL.getItem())), new BookPageCrafting("", "ablaze"));
            }
            if (Config.enableEnhancementSpeedloader) {
                SteamcraftRegistry.addResearch("research.EnhancementSpeedloader.name", "!research.Musket.name",
                  new BookPageItem("research.EnhancementSpeedloader.name", "research.EnhancementSpeedloader.0", true,
                    new ItemStack(BOLT_ACTION.getItem())),
                  new BookPageCrafting("", "speedloader1", "speedloader2"));
            }
            SteamcraftRegistry.addResearch("research.Blunderbuss.name", "category.Flintlock.name",
              new BookPageItem("research.Blunderbuss.name", "research.Blunderbuss.0",
                new ItemStack(BLUNDERBUSS.getItem())), new BookPageCrafting("", "blunderbuss"));
            if (Config.enableEnhancementAblaze) {
                SteamcraftRegistry.addResearch("research.EnhancementAblaze2.name", "!research.Blunderbuss.name",
                  new BookPageItem("research.EnhancementAblaze2.name", "research.EnhancementAblaze2.0", true,
                    new ItemStack(BLAZE_BARREL.getItem())), new BookPageCrafting("", "ablaze"));
            }
            if (Config.enableEnhancementSpeedloader) {
                SteamcraftRegistry.addResearch("research.EnhancementSpeedloader2.name", "!research.Blunderbuss.name",
                  new BookPageItem("research.EnhancementSpeedloader2.name", "research.EnhancementSpeedloader2.0", true,
                    new ItemStack(BOLT_ACTION.getItem())), new BookPageCrafting("", "speedloader1", "speedloader2"));
            }
            if (Config.enableEnhancementRecoil) {
                SteamcraftRegistry.addResearch("research.EnhancementRecoil.name", "!research.Blunderbuss.name",
                  new BookPageItem("research.EnhancementRecoil.name", "research.EnhancementRecoil.0", true,
                    new ItemStack(RECOIL_PAD.getItem())), new BookPageCrafting("", "recoil"));
            }
            SteamcraftRegistry.addResearch("research.Pistol.name", "category.Flintlock.name",
              new BookPageItem("research.Pistol.name", "research.Pistol.0", new ItemStack(PISTOL.getItem())),
              new BookPageCrafting("", "pistol"));
            if (Config.enableEnhancementRevolver) {
                SteamcraftRegistry.addResearch("research.EnhancementRevolver.name", "!research.Pistol.name",
                  new BookPageItem("research.EnhancementRevolver.name", "research.EnhancementRevolver.0", true,
                    new ItemStack(REVOLVER_CHAMBER.getItem())), new BookPageCrafting("", "revolver"));
            }
            if (Config.enableEnhancementSilencer) {
                SteamcraftRegistry.addResearch("research.EnhancementSilencer.name", "!research.Pistol.name",
                  new BookPageItem("research.EnhancementSilencer.name", "research.EnhancementSilencer.0", true,
                    new ItemStack(MAKESHIFT_SUPPRESSOR.getItem())), new BookPageCrafting("", "silencer"));
            }
            if (Config.enableEnhancementSpeedy) {
                SteamcraftRegistry.addResearch("research.EnhancementSpeedy.name", "!research.Pistol.name",
                  new BookPageItem("research.EnhancementSpeedy.name", "research.EnhancementSpeedy.0", true,
                    new ItemStack(BREECH.getItem())), new BookPageCrafting("", "speedy"));
            }
        }
        if (Config.enableRL) {
            SteamcraftRegistry.addResearch("research.RocketLauncher.name", "category.Flintlock.name",
              new BookPageItem("research.RocketLauncher.name", "research.RocketLauncher.0",
                new ItemStack(ROCKET_LAUNCHER.getItem())),
              new BookPageCrafting("", "rocket1", "rocket2", "rocket3", "rocket4"));
            if (Config.enableEnhancementFastRockets) {
                SteamcraftRegistry.addResearch("research.EnhancementFastRockets.name", "!research.RocketLauncher.name",
                  new BookPageItem("research.EnhancementFastRockets.name", "research.EnhancementFastRockets.0", true,
                    new ItemStack(STREAMLINED_BARREL.getItem())), new BookPageCrafting("", "fastRockets"));
            }
            if (Config.enableEnhancementAmmo) {
                SteamcraftRegistry.addResearch("research.EnhancementAmmo.name", "!research.RocketLauncher.name",
                  new BookPageItem("research.EnhancementAmmo.name", "research.EnhancementAmmo.0", true,
                    new ItemStack(EXTENDED_MAGAZINE.getItem())), new BookPageCrafting("", "ammo1", "ammo2"));
            }
            if (Config.enableEnhancementAirStrike) {
                SteamcraftRegistry.addResearch("research.EnhancementAirStrike.name", "!research.RocketLauncher.name",
                  new BookPageItem("research.EnhancementAirStrike.name", "research.EnhancementAirStrike.0", true,
                    new ItemStack(AIR_STRIKE_CONVERSION_KIT.getItem())),
                  new BookPageCrafting("", "airStrike1", "airStrike2"));
            }
            SteamcraftRegistry.addResearch("research.Rockets.name", "category.Flintlock.name");
            if (Config.enableRocket) {
                SteamcraftRegistry.addResearch("research.Rocket.name", "!research.Rockets.name",
                  new BookPageItem("research.Rocket.name", "research.Rocket.0", true, new ItemStack(ROCKET.getItem())),
                  new BookPageCrafting("", "normalRocket1", "normalRocket2"));
            }
            if (Config.enableRocketConcussive) {
                if (Config.enableRocket) {
                    SteamcraftRegistry.addResearch("research.RocketConcussive.name", "!research.Rockets.name",
                      new BookPageItem("research.RocketConcussive.name", "research.RocketConcussive.0", true,
                        new ItemStack(CONCUSSIVE_ROCKET.getItem())), new BookPageCrafting("", "concussiveRocket"));
                } else {
                    SteamcraftRegistry.addResearch("research.RocketConcussive.name", "!research.Rockets.name",
                      new BookPageItem("research.RocketConcussive.name", "research.RocketConcussive.0", true,
                        new ItemStack(CONCUSSIVE_ROCKET.getItem())),
                      new BookPageCrafting("", "concussiveRocket1", "concussiveRocket2"));
                }
            }
            if (Config.enableRocketMining) {
                SteamcraftRegistry.addResearch("research.RocketMining.name", "!research.Rockets.name",
                  new BookPageItem("research.RocketMining.name", "research.RocketMining.0", true,
                    new ItemStack(MINING_ROCKET.getItem())), new BookPageCrafting("", "miningRocket"));
            }
        }
    }

    public static void registerCasting() {
        SteamcraftRegistry.addCategory("category.MetalCasting.name");
        if (Config.enableCrucible) {
            SteamcraftRegistry.addResearch("research.Crucible.name", "category.MetalCasting.name",
              new BookPageItem("research.Crucible.name", "research.Crucible.0",
                new ItemStack(CRUCIBLE.getBlock())),
              new BookPageText("research.Crucible.name", "research.Crucible.1"), new BookPageCrafting("", "crucible"));
            if (Config.enableHellCrucible) {
                SteamcraftRegistry.addResearch("research.HellCrucible.name", "category.MetalCasting.name",
                  new BookPageItem("research.HellCrucible.name", "research.HellCrucible.0",
                    new ItemStack(NETHER_CRUCIBLE.getBlock())), new BookPageCrafting("", "hellCrucible"));
            }
        }
        if (Config.enableMold) {
            SteamcraftRegistry.addResearch("research.Mold.name", "category.MetalCasting.name",
              new BookPageItem("research.Mold.name", "research.Mold.0",
                new ItemStack(MOLD.getBlock())), new BookPageText("research.Mold.name", "research.Mold.1"),
              new BookPageCrafting("", "mold"));
            SteamcraftRegistry.addResearch("research.Molds.name", "category.MetalCasting.name",
              new BookPageItem("research.Molds.name", "research.Molds.0", new ItemStack(PLATE_MOLD.getItem()),
                new ItemStack(INGOT_MOLD.getItem()), new ItemStack(NUGGET_MOLD.getItem())),
              new BookPageCrafting("", "blankMold"), new BookPageCrafting("", "carving"));
        }
        SteamcraftRegistry.addResearch("research.Plates.name", "category.MetalCasting.name",
          new BookPageItem("research.Plates.name", "research.Plates.0",
            COPPER_PLATE.createItemStack(),
            ZINC_PLATE.createItemStack(),
            GOLD_PLATE.createItemStack(),
            IRON_PLATE.createItemStack(),
            BRASS_PLATE.createItemStack(),
            GILDED_IRON_PLATE.createItemStack()));
        SteamcraftRegistry.addResearch("research.Brass.name", "category.MetalCasting.name",
          new BookPageItem("research.Brass.name", "research.Brass.0", BRASS_INGOT.createItemStack()),
          new BookPageAlloy("", BRASS_LIQUID.getLiquid(), BRASS_LIQUID.getLiquid().recipe));

        if (Config.enableCrucible) {
            SteamcraftRegistry.addResearch("research.GildedGold.name", "category.MetalCasting.name",
              new BookPageItem("research.GildedGold.name", "research.GildedGold.0", GILDED_IRON_INGOT.createItemStack()),
              new BookPageText("research.GildedGold.name", "research.GildedGold.1"),
              new BookPageDip("", GOLD_LIQUID.getLiquid(), 1, new ItemStack(Items.IRON_INGOT), GILDED_IRON_PLATE.createItemStack()));
        }
    }

    public static void registerGadgets() {
        SteamcraftRegistry.addCategory("category.Gadgets.name");

        if (Config.enableWrench) {
            SteamcraftRegistry.addResearch("research.Wrench.name", "category.Gadgets.name",
              new BookPageItem("research.Wrench.name", "research.Wrench.0", new ItemStack(WRENCH.getItem())),
              new BookPageText("research.Wrench.name", "research.Wrench.1"),
              new BookPageCrafting("", "wrench1", "wrench2"));
        }
        if (Config.enableSpyglass) {
            SteamcraftRegistry.addResearch("research.Spyglass.name", "category.Gadgets.name",
              new BookPageItem("research.Spyglass.name", "research.Spyglass.0", new ItemStack(SPYGLASS.getItem())),
              new BookPageCrafting("", "spyglass1", "spyglass2"));
        }
        if (Config.enableSurvivalist) {
            if (CrossMod.BAUBLES) {
                SteamcraftRegistry.addResearch("research.Survivalist.name", "category.Gadgets.name",
                  new BookPageItem("research.Survivalist.name", "research.SurvivalistBaubles.0", new ItemStack(SURVIVALIST_TOOLKIT.getItem())),
                  new BookPageCrafting("", "survivalist"));
            } else {
                SteamcraftRegistry.addResearch("research.Survivalist.name", "category.Gadgets.name",
                  new BookPageItem("research.Survivalist.name", "research.Survivalist.0", new ItemStack(SURVIVALIST_TOOLKIT.getItem())),
                  new BookPageCrafting("", "survivalist"));
            }
        }
        if (Config.enableCanister) {
            ItemStack output = new ItemStack(Items.DIAMOND_SWORD);
            output.setTagCompound(new NBTTagCompound());
            output.getTagCompound().setInteger("canned", 0);
            SteamcraftRegistry.addResearch("research.Canister.name", "category.Gadgets.name",
              new BookPageItem("research.Canister.name", "research.Canister.0", new ItemStack(ITEM_CANISTER.getItem())),
              new BookPageCrafting("", "canister"),
              new BookPageCrafting("", true, output, Items.DIAMOND_SWORD, ITEM_CANISTER.getItem()));
        }
        if (Config.enableTopHat) {
            SteamcraftRegistry.addResearch("research.TopHat.name", "category.Gadgets.name",
              new BookPageItem("research.TopHat.name", "research.TopHat.0", new ItemStack(TOP_HAT.getItem())),
              new BookPageCrafting("", "hat"));
            if (Config.enableEmeraldHat) {
                SteamcraftRegistry.addResearch("research.TopHatEmerald.name", "category.Gadgets.name",
                  new BookPageItem("research.TopHatEmerald.name", "research.TopHatEmerald.0",
                    new ItemStack(ENTREPRENEUR_TOP_HAT.getItem())), new BookPageCrafting("", "hatEmerald"));
            }
        }
        if (Config.enableGoggles) {
            SteamcraftRegistry.addResearch("research.Goggles.name", "category.Gadgets.name",
              new BookPageItem("research.Goggles.name", "research.Goggles.0", new ItemStack(GOGGLES.getItem()),
                new ItemStack(MONOCLE.getItem())),
              new BookPageCrafting("", "goggles1", "goggles2"),
              new BookPageCrafting("", "monocle1", "monocle2"));
        }

        if (Config.enableSteamCell) {
            SteamcraftRegistry.addResearch("research.SteamCell.name", "category.Gadgets.name",
              new BookPageItem("research.SteamCell.name", "research.SteamCell.0",
                new ItemStack(STEAM_CELL_EMPTY.getItem()),
                new ItemStack(STEAM_CELL_FULL.getItem())),
              new BookPageCrafting("", "steamcell"));
        }

        if (Config.enableSteamCellBauble && CrossMod.BAUBLES) {
            SteamcraftRegistry.addResearch("research.SteamCellFiller.name", "category.Gadgets.name",
              new BookPageItem("research.SteamCellFiller.name", "research.SteamCellFiller.0",
                new ItemStack(STEAM_CELL_FILLER.getItem())),
              new BookPageCrafting("", "steamcellFiller"));
        }
    }

    public static void registerSteamTools() {
        if (!Config.enableSteamTools) {
            return;
        }
        SteamcraftRegistry.addCategory("category.SteamTools.name");
        SteamcraftRegistry.addResearch("research.SteamTools.name", "category.SteamTools.name",
          new BookPageItem("research.SteamTools.name", "research.SteamTools.0",
            new ItemStack(STEAM_DRILL.getItem()),
            new ItemStack(STEAM_SAW.getItem()),
            new ItemStack(STEAM_SHOVEL.getItem())),
          new BookPageText("research.SteamTools.name", "research.SteamTools.1"),
          new BookPageCrafting("", "drill1", "drill2", "drill3", "drill4"),
          new BookPageCrafting("", "axe1", "axe2", "axe3", "axe4"),
          new BookPageCrafting("", "shovel1", "shovel2", "shovel3", "shovel4")
        );

        SteamcraftRegistry.addResearch("research.SteamDrillHead.name", "category.SteamTools.name");

        ArrayList<String> drillMatsArray = new ArrayList<>();
        for (DrillHeadMaterial material : DrillHeadMaterial.materials.values()) {
            String loc = material.locName;
            String string = I18n.hasKey(loc) ? I18n.format(loc) : material.materialName;
            drillMatsArray.add(string);
        }

        StringBuilder drillMats = new StringBuilder();
        String delimiter = I18n.format("steamcraft.book.listjoiner");
        Iterator iter = drillMatsArray.iterator();
        while (iter.hasNext()) {
            drillMats.append(iter.next());
            if (iter.hasNext()) {
                drillMats.append(delimiter);
            }
        }

        SteamcraftRegistry.addResearch("research.DrillHeads.name", "!research.SteamDrillHead.name",
          new BookPageItem("research.DrillHeads.name", "research.DrillHeads.0",
            new Object[] { drillMats.toString() }, true,
            new ItemStack(DRILL_HEAD.getItem())), new BookPage(""));

        if (Config.enableFortune) {
            SteamcraftRegistry.addResearch("research.MultiplicativeResonator.name",
              "!research.SteamDrillHead.name",
              new BookPageItem("research.MultiplicativeResonator.name",
                "research.MultiplicativeResonator.0", true,
                new ItemStack(MULTIPLICATIVE_RESONATOR.getItem())),
              new BookPageCrafting("", "multiplicativeResonator"));
        }

        if (Config.enableBigDrill) {
            SteamcraftRegistry.addResearch("research.BigDrill.name",
              "!research.SteamDrillHead.name",
              new BookPageItem("research.BigDrill.name", "research.BigDrill.0", true,
                new ItemStack(BIG_DRILL.getItem())),
              new BookPageCrafting("", "bigDrill"));
        }

        if (Config.enableBattleDrill) {
            SteamcraftRegistry.addResearch("research.BattleDrill.name",
              "!research.SteamDrillHead.name",
              new BookPageItem("research.BattleDrill.name", "research.BattleDrill.0", true,
                new ItemStack(BATTLE_DRILL.getItem())),
              new BookPageCrafting("", "battleDrill"));
        }

        if (Config.enablePreciseCuttingHead) {
            SteamcraftRegistry.addResearch("research.PreciseCuttingHead.name",
              "!research.SteamDrillHead.name",
              new BookPageItem("research.PreciseCuttingHead.name",
                "research.PreciseCuttingHead.0", true, new ItemStack(PRECISE_CUTTING_HEAD.getItem())),
              new BookPageCrafting("", "preciseCuttingHead"));
        }

        if (Config.enableStoneGrinder) {
            SteamcraftRegistry.addResearch("research.StoneGrinder.name",
              "!research.SteamDrillHead.name",
              new BookPageItem("research.StoneGrinder.name", "research.StoneGrinder.0", true,
                new ItemStack(STONE_GRINDER.getItem())),
              new BookPageCrafting("", "stoneGrinder"));
        }

        if (Config.enableThermalDrill) {
            SteamcraftRegistry.addResearch("research.ThermalDrill.name",
              "!research.SteamDrillHead.name", new BookPageItem("research.ThermalDrill.name",
                "research.ThermalDrill.0", true, new ItemStack(THERMAL_DRILL.getItem())),
              new BookPageCrafting("", "thermalDrill"));
        }

        if (Config.enableChargePlacer) {
            SteamcraftRegistry.addResearch("research.CalamityInjector.name",
              "!research.SteamDrillHead.name", new BookPageItem("research.CalamityInjector.name",
                "research.CalamityInjector.0", true, new ItemStack(CHARGE_PLACER.getItem())),
              new BookPageCrafting("", "chargePlacer"));
        }

        SteamcraftRegistry.addResearch("research.SteamDrillCore.name", "category.SteamTools.name");

        if (Config.enableInternalProcessingUnit) {
            SteamcraftRegistry.addResearch("research.InternalProcessingUnit.name",
              "!research.SteamDrillCore.name", new BookPageItem(
                "research.InternalProcessingUnit.name", "research.InternalProcessingUnit.0", true,
                new ItemStack(INTERNAL_PROCESSING_UNIT.getItem())),
              new BookPageCrafting("", "internalProcessingUnit"));
        }

        SteamcraftRegistry.addResearch("research.SteamSawHead.name", "category.SteamTools.name");

        if (Config.enableForestFire) {
            SteamcraftRegistry.addResearch("research.ForestFire.name",
              "!research.SteamSawHead.name", new BookPageItem("research.ForestFire.name",
                "research.ForestFire.0", true, new ItemStack(FOREST_FIRE.getItem())),
              new BookPageCrafting("", "forestFire"));
        }

        if (Config.enableTreeFeller) {
            SteamcraftRegistry.addResearch("research.TimberChain.name",
              "!research.SteamSawHead.name", new BookPageItem("research.TimberChain.name",
                "research.TimberChain.0", true, new ItemStack(TIMBER_CHAIN.getItem())),
              new BookPageCrafting("", "treeFeller"));
        }

        if (Config.enableLeafBlower) {
            SteamcraftRegistry.addResearch("research.LeafBlower.name",
              "!research.SteamSawHead.name", new BookPageItem("research.LeafBlower.name",
                "research.LeafBlower.0", true, new ItemStack(LEAF_BLOWER.getItem())),
              new BookPageCrafting("", "leafBlower"));
        }

        if (Config.enableChainsaw) {
            SteamcraftRegistry.addResearch("research.Chainsaw.name", "!research.SteamSawHead.name",
              new BookPageItem("research.Chainsaw.name", "research.Chainsaw.0", true,
                new ItemStack(CHAINSAW.getItem())), new BookPageCrafting("", "chainsaw"));
        }

        SteamcraftRegistry.addResearch("research.SteamSawCore.name", "category.SteamTools.name");

        SteamcraftRegistry.addResearch("research.SteamShovelHead.name", "category.SteamTools.name");

        if (Config.enableBackhoe) {
            SteamcraftRegistry.addResearch("research.Backhoe.name",
              "!research.SteamShovelHead.name", new BookPageItem("research.Backhoe.name",
                "research.Backhoe.0", true, new ItemStack(BACKHOE.getItem())),
              new BookPageCrafting("", "backhoe"));
        }

        if (Config.enableCultivator) {
            SteamcraftRegistry.addResearch("research.Cultivator.name",
              "!research.SteamShovelHead.name", new BookPageItem("research.Cultivator.name",
                "research.Cultivator.0", true, new ItemStack(CULTIVATOR.getItem())),
              new BookPageCrafting("", "cultivator"));
        }

        if (Config.enableRotaryBlades) {
            SteamcraftRegistry.addResearch("research.RotaryBlades.name",
              "!research.SteamShovelHead.name", new BookPageItem("research.RotaryBlades.name",
                "research.RotaryBlades.0", true, new ItemStack(ROTARY_BLADES.getItem())),
              new BookPageCrafting("", "rotaryBlades"));
        }

        SteamcraftRegistry.addResearch("research.SteamShovelCore.name", "category.SteamTools.name");

        if (Config.enableSifter) {
            SteamcraftRegistry.addResearch("research.Sifter.name", "!research.SteamShovelCore.name",
              new BookPageItem("research.Sifter.name", "research.Sifter.0", true,
                new ItemStack(SIFTER.getItem())), new BookPageCrafting("", "sifter"));
        }

        SteamcraftRegistry.addResearch("research.SteamUniversalCore.name", "category.SteamTools.name");

        if (Config.enableOverclocker) {
            SteamcraftRegistry.addResearch("research.Overclocker.name",
              "!research.SteamUniversalCore.name", new BookPageItem("research.Overclocker.name",
                "research.Overclocker.0", true, new ItemStack(OVERCLOCKER.getItem())),
              new BookPageCrafting("", "overclocker"));
        }

        if (Config.enableAutosmelting) {
            SteamcraftRegistry.addResearch("research.ExothermicProjector.name",
              "!research.SteamUniversalCore.name",
              new BookPageItem("research.ExothermicProjector.name",
                "research.ExothermicProjector.0", true, new ItemStack(EXOTHERMIC_PROJECTOR.getItem())),
              new BookPageCrafting("", "autosmelting"));
        }

        if (Config.enableTheVoid) {
            SteamcraftRegistry.addResearch("research.TheVoid.name",
              "!research.SteamUniversalCore.name", new BookPageItem("research.TheVoid.name",
                "research.TheVoid.0", true, new ItemStack(THE_VOID.getItem())),
              new BookPageCrafting("", "theVoid"));
        }
    }

    public static void registerSteamPower() {
        SteamcraftRegistry.addCategory("category.SteamPower.name");
        SteamcraftRegistry.addResearch("research.Boiler.name", "category.SteamPower.name",
          new BookPageItem("research.Boiler.name", "research.Boiler.0", new ItemStack(BOILER.getBlock())),
          new BookPageCrafting("", "boiler1", "boiler2"));

        SteamcraftRegistry.addResearch("research.Pipe.name", "category.SteamPower.name",
          new BookPageItem("research.Pipe.name", "research.Pipe.0", new ItemStack(PIPE.getBlock())),
          new BookPageCrafting("", "pipe1", "pipe2"),
          new BookPageText("research.Pipe.name", "research.Pipe.1"),
          new BookPageCrafting("", "valvePipe"));

        if (Config.enableRuptureDisc) {
            SteamcraftRegistry.addResearch("research.RuptureDisc.name", "category.SteamPower.name",
              new BookPageItem("research.RuptureDisc.name", "research.RuptureDisc.0", new ItemStack(RUPTURE_DISC.getBlock())),
              new BookPageText("research.RuptureDisc.name", "research.RuptureDisc.1"),
              new BookPageCrafting("", "disc"));
        }

        if (Config.enableHorn) {
            SteamcraftRegistry.addResearch("research.Whistle.name", "category.SteamPower.name",
              new BookPageItem("research.Whistle.name", "research.Whistle.0", new ItemStack(STEAM_WHISTLE.getBlock())),
              new BookPageCrafting("", "whistle1", "whistle2"));
        }

        if (Config.enableGauge) {
            SteamcraftRegistry.addResearch("research.Gauge.name", "category.SteamPower.name",
              new BookPageItem("research.Gauge.name", "research.Gauge.0", new ItemStack(STEAM_GAUGE.getBlock())),
              new BookPageCrafting("", "gauge"));
        }
        SteamcraftRegistry.addResearch("research.Tank.name", "category.SteamPower.name",
          new BookPageItem("research.Tank.name", "research.Tank.0", new ItemStack(TANK.getBlock())),
          new BookPageCrafting("", "tank1", "tank2"));
        SteamcraftRegistry.addResearch("research.CreativeTank.name", "category.NOTREAL.name",
          new BookPageItem("research.CreativeTank.name", "research.CreativeTank.0", new ItemStack(Items.BOWL)));
        SteamcraftRegistry.bookRecipes.put(new ItemStack(TANK.getBlock(), 1, 1), MutablePair.of("research.CreativeTank.name", 0));
        if (Config.enableCharger) {
            SteamcraftRegistry.addResearch("research.Filler.name", "category.SteamPower.name",
              new BookPageItem("research.Filler.name", "research.Filler.0", new ItemStack(STEAM_FILLER.getBlock())),
              new BookPageText("research.Filler.name", "research.Filler.1"),
              new BookPageCrafting("", "filler"));
        }

        if (Config.enableChargingPad && Config.enableCharger) {
            SteamcraftRegistry.addResearch("research.FillingPad.name", "category.SteamPower.name",
              new BookPageItem("research.FillingPad.name", "research.FillingPad.0", new ItemStack(FILLING_PAD.getBlock())),
              new BookPageCrafting("", "fillingPad1", "fillingPad2"));
        }

        if (Config.enableHeater) {
            SteamcraftRegistry.addResearch("research.Heater.name", "category.SteamPower.name",
              new BookPageItem("research.Heater.name", "research.Heater.0", new ItemStack(STEAM_HEATER.getBlock())),
              new BookPageCrafting("", "heater1", "heater2"));
        }

        if (Config.enableMortar && Config.enableAstrolabe) {
            SteamcraftRegistry.addResearch("research.ItemMortar.name", "category.SteamPower.name",
              new BookPageItem("research.ItemMortar.name", "research.ItemMortar.0", new ItemStack(ITEM_MORTAR.getBlock())),
              new BookPageText("research.ItemMortar.name", "research.ItemMortar.1"),
              new BookPageCrafting("", "astrolabe"),
              new BookPageCrafting("", "itemMortar2", "itemMortar3"));
        }

        if (Config.enableHammer) {
            SteamcraftRegistry.addResearch("research.Hammer.name", "category.SteamPower.name",
              new BookPageItem("research.Hammer.name", "research.Hammer.0", new ItemStack(STEAM_HAMMER.getBlock())),
              new BookPageText("research.Hammer.name", "research.Hammer.1"),
              new BookPageCrafting("", "hammer1", "hammer2"));
        }
        if (Config.enablePump) {
            SteamcraftRegistry.addResearch("research.Screw.name", "category.SteamPower.name",
              new BookPageItem("research.Screw.name", "research.Screw.0", new ItemStack(ARCHIMEDES_SCREW.getBlock())),
              new BookPageCrafting("", "pump1", "pump2"));
        }

        if (Config.enableSmasher) {
            SteamcraftRegistry.addResearch("research.Smasher.name", "category.SteamPower.name",
              new BookPageItem("research.Smasher.name", "research.Smasher.0", new ItemStack(ROCK_SMASHER.getBlock())),
              new BookPageText("research.Smasher.name", "research.Smasher.1"),
              new BookPageCrafting("", "smasher1", "smasher2", "smasher3", "smasher4"));
        }

        if (Config.enableThumper) {
            SteamcraftRegistry.addResearch("research.Thumper.name", "category.SteamPower.name",
              new BookPageItem("research.Thumper.name", "research.Thumper.0", new ItemStack(THUMPER.getBlock())),
              new BookPageText("research.Thumper.name", "research.Thumper.1"),
              new BookPageCrafting("", "thumper1", "thumper2"));
        }

        if (Config.enableFan) {
            SteamcraftRegistry.addResearch("research.Fan.name", "category.SteamPower.name",
              new BookPageItem("research.Fan.name", "research.Fan.0", new ItemStack(FAN.getBlock())),
              new BookPageCrafting("", "fan1", "fan2"));
            if (Config.enableVacuum) {
                SteamcraftRegistry.addResearch("research.Vacuum.name", "category.SteamPower.name",
                  new BookPageItem("research.Vacuum.name", "research.Vacuum.0", new ItemStack(VACUUM.getBlock())),
                  new BookPageCrafting("", "vacuum1", "vacuum2"));
            }
        }

        if (Config.enableFluidSteamConverter) {
            SteamcraftRegistry.addResearch("research.FSC.name", "category.SteamPower.name",
              new BookPageItem("research.FSC.name", "research.FSC.0", new ItemStack(PRESSURE_CONVERTER.getBlock())),
              new BookPageCrafting("", "fsc1", "fsc2"),
              new BookPageText("", "research.FSC.1"),
              new BookPageText("", "research.FSC.2"));
        }

    }

    public static void registerExosuit() {
        if (Config.enableExosuit && Config.enableEngineering) {
            SteamcraftRegistry.addCategory("category.Exosuit.name");
            SteamcraftRegistry.addResearch("research.Exosuit.name", "category.Exosuit.name",
              new BookPageItem("research.Exosuit.name", "research.Exosuit.0",
                new ItemStack(EXOSUIT_HEADPIECE.getItem()),
                new ItemStack(EXOSUIT_CHESTPIECE.getItem()),
                new ItemStack(EXOSUIT_LEGPIECE.getItem()),
                new ItemStack(EXOSUIT_FOOTPIECE.getItem())),
              new BookPageText("research.Exosuit.name", "research.Exosuit.1"),
              new BookPageCrafting("", "engineering"),
              new BookPageCrafting("", "exoHead"),
              new BookPageCrafting("", "exoBody"),
              new BookPageCrafting("", "exoLegs"),
              new BookPageCrafting("", "exoFeet")
            );
            ItemStack[] stacks = new ItemStack[4];
            for (int i = 0; i < 4; i++) {
                ItemStack stack = new ItemStack(EXOSUIT_CHESTPIECE.getItem());
                stack.setTagCompound(new NBTTagCompound());
                ItemStack plate = null;
                Collection<ExosuitPlate> values = SteamcraftRegistry.plates.values();
                Object item = values.toArray(new ExosuitPlate[values.size()])[i].getItem();
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
            SteamcraftRegistry.addResearch("research.ExoTank.name", "category.Exosuit.name",
              new BookPageItem("research.ExoTank.name", "research.ExoTank.0", new ItemStack(TANK.getBlock())));
            SteamcraftRegistry.addResearch("research.ExoTankBase.name", "!research.ExoTank.name",
              new BookPageItem("research.ExoTankBase.name", "research.ExoTankBase.0", true, new ItemStack(TANK.getBlock())));
            if (Config.enableReinforcedTank) {
                SteamcraftRegistry.addResearch("research.ExoTankReinforced.name", "!research.ExoTank.name",
                  new BookPageItem("research.ExoTankReinforced.name", "research.ExoTankReinforced.0", true,
                    new ItemStack(REINFORCED_TANK.getItem())),
                  new BookPageCrafting("", "reinforcedTank1", "reinforcedTank2"));
            }
            if (Config.enableUberReinforcedTank) {
                SteamcraftRegistry.addResearch("research.ExoTankUberReinforced.name", "!research.ExoTank.name",
                  new BookPageItem("research.ExoTankUberReinforced.name", "research.ExoTankUberReinforced.0", true,
                    new ItemStack(UBER_REINFORCED_TANK.getItem())),
                  new BookPageCrafting("", "uberReinforcedTank1", "uberReinforcedTank2"));
            }

            SteamcraftRegistry.addResearch("research.ExoPlates.name", "category.Exosuit.name",
              new BookPageItem("research.ExoPlates.name", "research.ExoPlates.0", stacks),
              new BookPageText("", "research.ExoPlates.1"));

            if (Config.enableCopperPlate) {
                SteamcraftRegistry.addResearch("research.PlateCopper.name", "!research.ExoPlates.name",
                  new BookPageItem("research.PlateCopper.name", "research.PlateCopper.0", true, COPPER_EXO.createItemStack()),
                  new BookPageCrafting("", "exoCopper"));
            }
            if (Config.enableIronPlate) {
                SteamcraftRegistry.addResearch("research.PlateIron.name", "!research.ExoPlates.name",
                  new BookPageItem("research.PlateIron.name", "research.PlateIron.0", true, IRON_EXO.createItemStack()),
                  new BookPageCrafting("", "exoIron"));
            }
            if (Config.enableBrassPlate) {
                SteamcraftRegistry.addResearch("research.PlateBrass.name", "!research.ExoPlates.name",
                  new BookPageItem("research.PlateBrass.name", "research.PlateBrass.0", true, BRASS_EXO.createItemStack()),
                  new BookPageCrafting("", "exoBrass"));
            }
            if (Config.enableGoldPlate) {
                SteamcraftRegistry.addResearch("research.PlateGold.name", "!research.ExoPlates.name",
                  new BookPageItem("research.PlateGold.name", "research.PlateGold.0", true, GOLD_EXO.createItemStack()),
                  new BookPageCrafting("", "exoGold"));
            }
            if (Config.enableGildedIronPlate) {
                SteamcraftRegistry.addResearch("research.PlateGilded.name", "!research.ExoPlates.name",
                  new BookPageItem("research.PlateGilded.name", "research.PlateGilded.0", true, GILDED_IRON_EXO.createItemStack()),
                  new BookPageCrafting("", "exoGildedIron"));
            }
            if (Config.enableLeadPlate && OreDictionary.getOres("ingotLead").size() > 0) {
                SteamcraftRegistry.addResearch("research.PlateLead.name", "!research.ExoPlates.name",
                  new BookPageItem("research.PlateLead.name", "research.PlateLead.0", true, LEAD_EXO.createItemStack()),
                  new BookPageCrafting("", "exoLead"));
            }
            ItemStack[] stacks2 = new ItemStack[4];
            for (int i = 0; i < 4; i++) {
                ItemStack stack = new ItemStack(EXOSUIT_CHESTPIECE.getItem());
                stack.setTagCompound(new NBTTagCompound());
                ItemStack dye = new ItemStack(Items.DYE, 1, i);
                ((ItemExosuitArmor) stack.getItem()).setInventorySlotContents(stack, 2, dye);
                stacks2[i] = stack;
            }
            SteamcraftRegistry.addResearch("research.ExoVanity.name", "category.Exosuit.name",
              new BookPageItem("research.ExoVanity.name", "research.ExoVanity.0", stacks2));
            SteamcraftRegistry.addResearch("research.ExoDyes.name", "!research.ExoVanity.name",
              new BookPageItem("research.ExoDyes.name", "research.ExoDyes.0", true,
                new ItemStack(Items.DYE, 1, 0), new ItemStack(Items.DYE, 1, 1), new ItemStack(Items.DYE, 1, 2),
                new ItemStack(Items.DYE, 1, 3)));
            if (Config.enableEnderShroud) {
                SteamcraftRegistry.addResearch("research.EnderShroud.name", "!research.ExoVanity.name",
                  new BookPageItem("research.EnderShroud.name", "research.EnderShroud.0", true, new ItemStack(ENDER_SHROUD.getItem())),
                  new BookPageCrafting("", "enderShroud"));
            }
            SteamcraftRegistry.addResearch("research.ExoHeadHelm.name", "category.Exosuit.name");
            if (Config.enableTopHat) {
                SteamcraftRegistry.addResearch("research.ExoTopHat.name", "!research.ExoHeadHelm.name",
                  new BookPageItem("research.ExoTopHat.name", "research.ExoTopHat.0", true,
                    new ItemStack(TOP_HAT.getItem()), new ItemStack(ENTREPRENEUR_TOP_HAT.getItem())),
                  new BookPage(""));
            }
            if (Config.enableFrequencyShifter) {
                SteamcraftRegistry.addResearch(
                  "research.FrequencyShifter.name",
                  "!research.ExoHeadHelm.name",
                  new BookPageItem("research.FrequencyShifter.name", "research.FrequencyShifter.0",
                    true, new ItemStack(FREQUENCY_SHIFTER.getItem())),
                  new BookPageCrafting("", "frequencyShifter")
                );
            }

            SteamcraftRegistry.addResearch("research.ExoHeadGoggle.name", "category.Exosuit.name");
            if (Config.enableGoggles) {
                SteamcraftRegistry.addResearch("research.ExoGoggles.name", "!research.ExoHeadGoggle.name",
                  new BookPageItem("research.ExoGoggles.name", "research.ExoGoggles.0", true,
                    new ItemStack(GOGGLES.getItem()), new ItemStack(MONOCLE.getItem())),
                  new BookPage(""));
            }
            if (Config.enableRebreather) {
                SteamcraftRegistry.addResearch(
                  "research.Rebreather.name",
                  "!research.ExoHeadGoggle.name",
                  new BookPageItem("research.Rebreather.name", "research.Rebreather.0", true, new ItemStack(REBREATHER.getItem())),
                  new BookPageCrafting("", "rebreather")
                );
            }
            if (Config.enableDragonRoar) {
                SteamcraftRegistry.addResearch(
                  "research.DragonRoar.name",
                  "!research.ExoHeadGoggle.name",
                  new BookPageItem("research.DragonRoar.name", "research.DragonRoar.0", true, new ItemStack(DRAGON_ROAR.getItem())),
                  new BookPageCrafting("", "dragonRoar")
                );
            }

            SteamcraftRegistry.addResearch("research.ExoBack.name", "category.Exosuit.name");
            if (Config.enableJetpack) {
                SteamcraftRegistry.addResearch("research.Jetpack.name", "!research.ExoBack.name",
                  new BookPageItem("research.Jetpack.name", "research.Jetpack.0", true, new ItemStack(JETPACK.getItem())),
                  new BookPageCrafting("", "jetpack1", "jetpack2"));
            }
            if (Config.enableWings) {
                SteamcraftRegistry.addResearch("research.Wings.name", "!research.ExoBack.name",
                  new BookPageItem("research.Wings.name", "research.Wings.0", true, new ItemStack(WINGS.getItem())),
                  new BookPageCrafting("", "wings1", "wings2"));
            }

            SteamcraftRegistry.addResearch("research.ExoArm.name", "category.Exosuit.name");
            if (Config.enablePowerFist) {
                SteamcraftRegistry.addResearch("research.Fist.name", "!research.ExoArm.name",
                  new BookPageItem("research.Fist.name", "research.Fist.0", true, new ItemStack(POWER_FIST.getItem())),
                  new BookPageCrafting("", "powerFist1", "powerFist2"));
            }
            if (Config.enableExtendoFist) {
                SteamcraftRegistry.addResearch("research.ExtendoFist.name", "!research.ExoArm.name",
                  new BookPageItem("research.ExtendoFist.name", "research.ExtendoFist.0", true, new ItemStack(EXTENDO_FIST.getItem())),
                  new BookPageCrafting("", "extendoFist1", "extendoFist2"));
            }
            if (Config.enablePitonDeployer) {
                SteamcraftRegistry.addResearch("research.PitonDeployer.name", "!research.ExoArm.name",
                  new BookPageItem("research.PitonDeployer.name", "research.PitonDeployer.0", true, new ItemStack(PITON_DEPLOYER.getItem())),
                  new BookPageCrafting("", "pitonDeployer"));
            }
            if (Config.enablePistonPush) {
                SteamcraftRegistry.addResearch(
                  "research.PistonPush.name",
                  "!research.ExoArm.name",
                  new BookPageItem("research.PistonPush.name", "research.PistonPush.0", true, new ItemStack(PISTON_PUSH.getItem())),
                  new BookPageCrafting("", "pistonPush")
                );
            }

            SteamcraftRegistry.addResearch("research.ExoHip.name", "category.Exosuit.name");
            if (Config.enableThrusters) {
                SteamcraftRegistry.addResearch("research.Thrusters.name", "!research.ExoHip.name",
                  new BookPageItem("research.Thrusters.name", "research.Thrusters.0", true, new ItemStack(THRUSTERS.getItem())),
                  new BookPageCrafting("", "thrusters1", "thrusters2"));
            }
            if (Config.enableCanningMachine) {
                SteamcraftRegistry.addResearch("research.Canner.name", "!research.ExoHip.name",
                  new BookPageItem("research.Canner.name", "research.Canner.0", true, new ItemStack(CANNING_MACHINE.getItem())),
                  new BookPageCrafting("", "canner1", "canner2", "canner3", "canner4"));
            }
            if (Config.enableReloadingHolsters) {
                SteamcraftRegistry.addResearch("research.ReloadingHolsters.name", "!research.ExoHip.name",
                  new BookPageItem("research.ReloadingHolsters.name", "research.ReloadingHolsters.0", true, new ItemStack(RELOADING_HOLSTERS.getItem())),
                  new BookPageCrafting("", "reloadingHolsters")
                );
            }

            SteamcraftRegistry.addResearch("research.ExoLeg.name", "category.Exosuit.name");
            if (Config.enableRunAssist) {
                SteamcraftRegistry.addResearch("research.RunAssist.name", "!research.ExoLeg.name",
                  new BookPageItem("research.RunAssist.name", "research.RunAssist.0", true, new ItemStack(RUN_ASSIST.getItem())),
                  new BookPageCrafting("", "runAssist1", "runAssist2"));
            }
            if (Config.enableStealthUpgrade) {
                SteamcraftRegistry.addResearch("research.StealthUpgrade.name", "!research.ExoLeg.name",
                  new BookPageItem("research.StealthUpgrade.name", "research.StealthUpgrade.0", true, new ItemStack(STEALTH.getItem())),
                  new BookPageCrafting("", "stealthUpgrade"));
            }
            SteamcraftRegistry.addResearch("research.ExoHeel.name", "category.Exosuit.name");
            if (Config.enableFallAssist) {
                SteamcraftRegistry.addResearch("research.FallAssist.name", "!research.ExoHeel.name",
                  new BookPageItem("research.FallAssist.name", "research.FallAssist.0", true, new ItemStack(FALL_ASSIST.getItem())),
                  new BookPageCrafting("", "noFall"));
            }
            if (Config.enableAnchorHeels) {
                boolean lead = Config.enableLeadPlate &&
                  OreDictionary.getOres("ingotLead").size() > 0 && !Config.enableAnchorAnvilRecipe;
                String research = lead ? "research.AnchorHeelsLead.0" : "research.AnchorHeelsIron.0";
                SteamcraftRegistry.addResearch("research.AnchorHeels.name", "!research.ExoHeel.name",
                  new BookPageItem("research.AnchorHeels.name", research, true, new ItemStack(ANCHOR_HEELS.getItem())),
                  new BookPageCrafting("", "anchorHeels"));
            }
            SteamcraftRegistry.addResearch("research.ExoFoot.name", "category.Exosuit.name");
            if (Config.enableDoubleJump) {
                SteamcraftRegistry.addResearch("research.DoubleJump.name", "!research.ExoFoot.name",
                  new BookPageItem("research.DoubleJump.name", "research.DoubleJump.0", true, new ItemStack(DOUBLE_JUMP.getItem())),
                  new BookPageCrafting("", "doubleJump1", "doubleJump2"));
            }
            if (Config.enableHydrophobic) {
                SteamcraftRegistry.addResearch("research.Hydrophobic.name", "!research.ExoFoot.name",
                  new BookPageItem("research.Hydrophobic.name", "research.Hydrophobic.0", true, new ItemStack(HYDROPHOBIC_COATINGS.getItem())),
                  new BookPageCrafting("", "hydrophobic")
                );
            }
            if (Config.enablePyrophobic) {
                SteamcraftRegistry.addResearch("research.Pyrophobic.name", "!research.ExoFoot.name",
                  new BookPageItem("research.Pyrophobic.name", "research.Pyrophobic.0", true, new ItemStack(PYROPHOBIC_COATINGS.getItem())),
                  new BookPageCrafting("", "pyrophobic")
                );
            }
            if (Config.enableJumpAssist) {
                SteamcraftRegistry.addResearch("research.JumpAssist.name", "!research.ExoFoot.name",
                  new BookPageItem("research.JumpAssist.name", "research.JumpAssist.0", true, new ItemStack(JUMP_ASSIST.getItem())),
                  new BookPageCrafting("", "jumpAssist1", "jumpAssist2"));
            }
        }
    }

    private static void registerMisc() {
        SteamcraftRegistry.addCategory("category.Misc.name");
        if (FUNNEL.isEnabled()) {
            SteamcraftRegistry.addResearch("research.Funnel.name", "category.Misc.name",
              new BookPageItem("research.Funnel.name", "research.Funnel.0", true, new ItemStack(FUNNEL.getBlock())),
              new BookPageCrafting("", "funnel")
            );
        }
    }
}
