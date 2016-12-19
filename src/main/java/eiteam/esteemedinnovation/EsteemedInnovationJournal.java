package eiteam.esteemedinnovation;

import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.api.exosuit.ExosuitPlate;
import eiteam.esteemedinnovation.api.exosuit.ExosuitRegistry;
import eiteam.esteemedinnovation.init.items.MetalItems;
import eiteam.esteemedinnovation.init.misc.OreDictEntries;
import eiteam.esteemedinnovation.init.misc.integration.CrossMod;
import eiteam.esteemedinnovation.item.armor.exosuit.ItemExosuitArmor;
import eiteam.esteemedinnovation.misc.DrillHeadMaterial;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

import static eiteam.esteemedinnovation.init.blocks.CastingBlocks.Blocks.*;
import static eiteam.esteemedinnovation.init.blocks.MiscellaneousBlocks.Blocks.FUNNEL;
import static eiteam.esteemedinnovation.init.blocks.OreBlocks.Blocks.OVERWORLD_COPPER_ORE;
import static eiteam.esteemedinnovation.init.blocks.OreBlocks.Blocks.OVERWORLD_ZINC_ORE;
import static eiteam.esteemedinnovation.init.blocks.PipeBlocks.Blocks.BRASS_PIPE;
import static eiteam.esteemedinnovation.init.blocks.SteamMachineryBlocks.Blocks.*;
import static eiteam.esteemedinnovation.init.blocks.SteamNetworkBlocks.Blocks.*;
import static eiteam.esteemedinnovation.init.items.CraftingComponentItems.Items.*;
import static eiteam.esteemedinnovation.init.items.MetalItems.Items.*;
import static eiteam.esteemedinnovation.init.items.MetalcastingItems.Items.*;
import static eiteam.esteemedinnovation.init.items.armor.ArmorItems.Items.*;
import static eiteam.esteemedinnovation.init.items.armor.ExosuitUpgradeItems.Items.*;
import static eiteam.esteemedinnovation.init.items.armor.ExosuitUpgradeItems.PlateItems.*;
import static eiteam.esteemedinnovation.init.items.firearms.FirearmAmmunitionItems.Items.*;
import static eiteam.esteemedinnovation.init.items.firearms.FirearmItems.Items.*;
import static eiteam.esteemedinnovation.init.items.firearms.FirearmUpgradeItems.Items.*;
import static eiteam.esteemedinnovation.init.items.tools.GadgetItems.Items.*;
import static eiteam.esteemedinnovation.init.items.tools.ToolItems.Items.*;
import static eiteam.esteemedinnovation.init.items.tools.ToolUpgradeItems.Items.*;
import static eiteam.esteemedinnovation.init.misc.DefaultCrucibleLiquids.Liquids.BRASS_LIQUID;
import static eiteam.esteemedinnovation.init.misc.DefaultCrucibleLiquids.Liquids.GOLD_LIQUID;

public class EsteemedInnovationJournal {
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
        BookEntry entryBook = new BookEntry("research.Book.name",
          new BookPageItem("research.Book.name", "research.Book.0", new ItemStack(BOOK.getItem())),
          new BookPageCrafting("", "book"));
        BookEntry entryOres = new BookEntry("research.Ores.name",
          new BookPageItem("research.Ores.name", "research.Ores.0", OVERWORLD_COPPER_ORE.createItemStack(), OVERWORLD_ZINC_ORE.createItemStack()));
        BookEntry entryBits = new BookEntry("research.Bits.name",
          new BookPageItem("research.Bits.name", "research.Bits.0", BRASS_PISTON.createItemStack(), BRASS_TURBINE.createItemStack()));
        BookEntry entrySteamSystem = new BookEntry("research.SteamSystem.name",
          new BookPageItem("research.SteamSystem.name", "research.SteamSystem.0", new ItemStack(BOILER.getBlock()), new ItemStack(BRASS_PIPE.getBlock())),
          new BookPageText("research.SteamSystem.name", "research.SteamSystem.1"),
          new BookPageText("research.SteamSystem.name", "research.SteamSystem.2"),
          new BookPageText("research.SteamSystem.name", "research.SteamSystem.3"),
          new BookPageText("research.SteamSystem.name", "research.SteamSystem.4"),
          new BookPageText("research.SteamSystem.name", "research.SteamSystem.5"));
        BookEntry entryCamo = new BookEntry("research.Camouflage.name",
          new BookPageItem("research.Camouflage.name", "research.Camouflage.0", new ItemStack(BRASS_PIPE.getBlock()), new ItemStack(Blocks.STONEBRICK)),
          new BookPageText("research.Camouflage.name", "research.Camouflage.1"));

        BookPageRegistry.addTopCategory(new BookCategory.Factory("category.Basics.name")
          .append(entryBook).append(entryOres).append(entryBits).append(entrySteamSystem).append(entryCamo)
          .build());
    }

    public static void registerFirearms() {
        if (!Config.enableFirearms && !Config.enableRL) {
            return;
        }

        BookEntry entryParts = new BookEntry("research.Parts.name",
          new BookPageItem("research.Parts.name", "research.Parts.0", BLUNDERBUSS_BARREL.createItemStack(),
            FLINTLOCK.createItemStack(), GUN_STOCK.createItemStack(), IRON_BARREL.createItemStack()),
          new BookPageCrafting("", "stock"),
          new BookPageCrafting("", "barrel1", "barrel2"),
          new BookPageCrafting("", "blunderBarrel1", "blunderBarrel2"),
          new BookPageCrafting("", "flintlock1", "flintlock2"));
        BookCategory.Factory catFlintlockFactory = new BookCategory.Factory("category.Flintlock.name")
          .append(entryParts);

        if (Config.enableFirearms) {
            BookEntry entryMusket = new BookEntry("research.Musket.name",
              new BookPageItem("research.Musket.name", "research.Musket.0", new ItemStack(MUSKET.getItem())),
              new BookPageCrafting("", "cartridge1", "cartridge2", "cartridge3", "cartridge4"),
              new BookPageCrafting("", "musket"));
            BookCategory.Factory catMusketFactory = new BookCategory.Factory("research.Musket.name").append(entryMusket);

            BookEntry entryBlunderbuss = new BookEntry("research.Blunderbuss.name",
              new BookPageItem("research.Blunderbuss.name", "research.Blunderbuss.0",
                new ItemStack(BLUNDERBUSS.getItem())), new BookPageCrafting("", "blunderbuss"));
            BookCategory.Factory catBlunderbussFactory = new BookCategory.Factory("research.Blunderbuss.name").append(entryBlunderbuss);

            if (Config.enableSpyglass) {
                catMusketFactory.append(new BookEntry("research.EnhancementSpyglass.name",
                  new BookPageItem("research.EnhancementSpyglass.name", "research.EnhancementSpyglass.0", true, new ItemStack(SPYGLASS.getItem()))));
            }
            if (Config.enableEnhancementAblaze) {
                BookPageCrafting ablazeCraft = new BookPageCrafting("", "ablaze");
                catMusketFactory.append(new BookEntry("research.EnhancementAblaze.name",
                  new BookPageItem("research.EnhancementAblaze.name", "research.EnhancementAblaze.0", true, new ItemStack(BLAZE_BARREL.getItem())),
                  ablazeCraft));
                catBlunderbussFactory.append(new BookEntry("research.EnhancementAblaze2.name",
                  new BookPageItem("research.EnhancementAblaze2.name", "research.EnhancementAblaze2.0", true, new ItemStack(BLAZE_BARREL.getItem())),
                  ablazeCraft));
            }
            if (Config.enableEnhancementSpeedloader) {
                BookPageCrafting speedloaderCraft = new BookPageCrafting("", "speedloader1", "speedloader2");
                catMusketFactory.append(new BookEntry("research.EnhancementSpeedloader.name",
                  new BookPageItem("research.EnhancementSpeedloader.name", "research.EnhancementSpeedloader.0", true, new ItemStack(BOLT_ACTION.getItem())),
                  speedloaderCraft));
                catBlunderbussFactory.append(new BookEntry("research.EnhancementSpeedloader2.name",
                  new BookPageItem("research.EnhancementSpeedloader2.name", "research.EnhancementSpeedloader2.0", true, new ItemStack(BOLT_ACTION.getItem())),
                  speedloaderCraft));
            }
            if (Config.enableEnhancementRecoil) {
                catBlunderbussFactory.append(new BookEntry("research.EnhancementRecoil.name",
                  new BookPageItem("research.EnhancementRecoil.name", "research.EnhancementRecoil.0", true, new ItemStack(RECOIL_PAD.getItem())),
                  new BookPageCrafting("", "recoil")));
            }

            catFlintlockFactory.append(catMusketFactory.build());
            catFlintlockFactory.append(catBlunderbussFactory.build());

            BookEntry entryPistol = new BookEntry("research.Pistol.name",
              new BookPageItem("research.Pistol.name", "research.Pistol.0", new ItemStack(PISTOL.getItem())),
              new BookPageCrafting("", "pistol"));
            BookCategory.Factory catPistol = new BookCategory.Factory("research.Pistol.name").append(entryPistol);

            if (Config.enableEnhancementRevolver) {
                catPistol.append(new BookEntry("research.EnhancementRevolver.name",
                  new BookPageItem("research.EnhancementRevolver.name", "research.EnhancementRevolver.0", true, new ItemStack(REVOLVER_CHAMBER.getItem())),
                  new BookPageCrafting("", "revolver")));
            }
            if (Config.enableEnhancementSilencer) {
                catPistol.append(new BookEntry("research.EnhancementSilencer.name",
                  new BookPageItem("research.EnhancementSilencer.name", "research.EnhancementSilencer.0", true, new ItemStack(MAKESHIFT_SUPPRESSOR.getItem())),
                  new BookPageCrafting("", "silencer")));
            }
            if (Config.enableEnhancementSpeedy) {
                catPistol.append(new BookEntry("research.EnhancementSpeedy.name",
                  new BookPageItem("research.EnhancementSpeedy.name", "research.EnhancementSpeedy.0", true, new ItemStack(BREECH.getItem())),
                  new BookPageCrafting("", "speedy")));
            }

            catFlintlockFactory.append(catPistol.build());
        }
        if (Config.enableRL) {
            BookEntry entryRL = new BookEntry("research.RocketLauncher.name",
              new BookPageItem("research.RocketLauncher.name", "research.RocketLauncher.0", new ItemStack(ROCKET_LAUNCHER.getItem())),
              new BookPageCrafting("", "rocket1", "rocket2", "rocket3", "rocket4"));
            BookCategory.Factory catRLFactory = new BookCategory.Factory("research.RocketLauncher.name").append(entryRL);

            if (Config.enableEnhancementFastRockets) {
                catRLFactory.append(new BookEntry("research.EnhancementFastRockets.name",
                  new BookPageItem("research.EnhancementFastRockets.name", "research.EnhancementFastRockets.0", true, new ItemStack(STREAMLINED_BARREL.getItem())),
                  new BookPageCrafting("", "fastRockets")));
            }
            if (Config.enableEnhancementAmmo) {
                catRLFactory.append(new BookEntry("research.EnhancementAmmo.name",
                  new BookPageItem("research.EnhancementAmmo.name", "research.EnhancementAmmo.0", true, new ItemStack(EXTENDED_MAGAZINE.getItem())),
                  new BookPageCrafting("", "ammo1", "ammo2")));
            }
            if (Config.enableEnhancementAirStrike) {
                catRLFactory.append(new BookEntry("research.EnhancemenAirStrike.name",
                  new BookPageItem("research.EnhancementAirStrike.name", "research.EnhancementAirStrike.0", true, new ItemStack(AIR_STRIKE_CONVERSION_KIT.getItem())),
                  new BookPageCrafting("", "airStrike1", "airStrike2")));
            }

            BookCategory.Factory catRocketsFactory = new BookCategory.Factory("research.Rockets.name");
            if (Config.enableRocket) {
                catRocketsFactory.append(new BookEntry("research.Rocket.name",
                  new BookPageItem("research.Rocket.name", "research.Rocket.0", true, new ItemStack(ROCKET.getItem())),
                  new BookPageCrafting("", "normalRocket1", "normalRocket2")));
            }
            if (Config.enableRocketConcussive) {
                BookPageCrafting crafting = Config.enableRocket ? new BookPageCrafting("", "concussiveRocket") : new BookPageCrafting("", "concussiveRocket1", "concussiveRocket2");
                catRocketsFactory.append(new BookEntry("research.RocketConcussive.name",
                  new BookPageItem("research.RocketConcussive.name", "research.RocketConcussive.0", true, new ItemStack(CONCUSSIVE_ROCKET.getItem())),
                  crafting));
            }
            if (Config.enableRocketMining) {
                catRocketsFactory.append(new BookEntry("research.RocketMining.name",
                  new BookPageItem("research.RocketMining.name", "research.RocketMining.0", true, new ItemStack(MINING_ROCKET.getItem())),
                  new BookPageCrafting("", "miningRocket")));
            }

            catRLFactory.append(catRocketsFactory.build());
            catFlintlockFactory.append(catRLFactory.build());
        }

        BookPageRegistry.addTopCategory(catFlintlockFactory.build());
    }

    public static void registerCasting() {
        BookCategory.Factory castingFactory = new BookCategory.Factory("category.MetalCasting.name");
        if (Config.enableCrucible) {
            castingFactory.append(new BookEntry("research.Crucible.name",
              new BookPageItem("research.Crucible.name", "research.Crucible.0", new ItemStack(CRUCIBLE.getBlock())),
              new BookPageText("research.Crucible.name", "research.Crucible.1"),
              new BookPageCrafting("", "crucible")));
            if (Config.enableHellCrucible) {
                castingFactory.append(new BookEntry("research.HellCrucible.name",
                  new BookPageItem("research.HellCrucible.name", "research.HellCrucible.0", new ItemStack(NETHER_CRUCIBLE.getBlock())),
                  new BookPageCrafting("", "hellCrucible")));
            }
        }
        if (Config.enableMold) {
            castingFactory.append(new BookEntry("research.Mold.name",
              new BookPageItem("research.Mold.name", "research.Mold.0", new ItemStack(MOLD.getBlock())),
              new BookPageText("research.Mold.name", "research.Mold.1"),
              new BookPageCrafting("", "mold")));
            castingFactory.append(new BookEntry("research.Molds.name",
              new BookPageItem("research.Molds.name", "research.Molds.0",
                new ItemStack(PLATE_MOLD.getItem()),
                new ItemStack(INGOT_MOLD.getItem()),
                new ItemStack(NUGGET_MOLD.getItem()),
                new ItemStack(PIPE_MOLD.getItem())),
              new BookPageCrafting("", "blankMold"),
              new BookPageCrafting("", "carving")));
        }

        BookEntry platesEntry = new BookEntry("research.Plates.name",
          new BookPageItem("research.Plates.name", "research.Plates.0",
            Arrays.stream(PLATES).map(MetalItems.Items::createItemStack).collect(Collectors.toList()).toArray(new ItemStack[PLATES.length])));
        BookEntry brassEntry = new BookEntry("research.Brass.name",
          new BookPageItem("research.Brass.name", "research.Brass.0", BRASS_INGOT.createItemStack()),
          new BookPageAlloy("", BRASS_LIQUID.getLiquid(), BRASS_LIQUID.getLiquid().recipe));
        castingFactory.append(platesEntry).append(brassEntry);

        if (Config.enableCrucible) {
            castingFactory.append(new BookEntry("research.GildedGold.name",
              new BookPageItem("research.GildedGold.name", "research.GildedGold.0", GILDED_IRON_INGOT.createItemStack()),
              new BookPageText("research.GildedGold.name", "research.GildedGold.1"),
              new BookPageDip("", GOLD_LIQUID.getLiquid(), 1, new ItemStack(Items.IRON_INGOT), GILDED_IRON_PLATE.createItemStack())));
        }
        BookPageRegistry.addTopCategory(castingFactory.build());
    }

    public static void registerGadgets() {
        BookCategory.Factory gadgetsFactory = new BookCategory.Factory("category.Gadgets.name");

        if (Config.enableWrench) {
            gadgetsFactory.append(new BookEntry("research.Wrench.name",
              new BookPageItem("research.Wrench.name", "research.Wrench.0", new ItemStack(WRENCH.getItem())),
              new BookPageText("research.Wrench.name", "research.Wrench.1"),
              new BookPageCrafting("", "wrench1", "wrench2")));
        }
        if (Config.enableSpyglass) {
            gadgetsFactory.append(new BookEntry("research.Spyglass.name",
              new BookPageItem("research.Spyglass.name", "research.Spyglass.0", new ItemStack(SPYGLASS.getItem())),
              new BookPageCrafting("", "spyglass1", "spyglass2")));
        }
        if (Config.enableSurvivalist) {
            gadgetsFactory.append(new BookEntry("research.Survivalist.name",
              new BookPageItem("research.Survivalist.name", String.format("research.Survivalist%s.0", CrossMod.BAUBLES ? "Baubles": ""), new ItemStack(SURVIVALIST_TOOLKIT.getItem())),
              new BookPageCrafting("", "survivalist")));
        }
        if (Config.enableCanister) {
            ItemStack output = new ItemStack(Items.DIAMOND_SWORD);
            output.setTagCompound(new NBTTagCompound());
            output.getTagCompound().setInteger("canned", 0);
            gadgetsFactory.append(new BookEntry("research.Canister.name",
              new BookPageItem("research.Canister.name", "research.Canister.0", new ItemStack(ITEM_CANISTER.getItem())),
              new BookPageCrafting("", "canister"),
              new BookPageCrafting("", true, output, Items.DIAMOND_SWORD, ITEM_CANISTER.getItem())));
        }
        if (Config.enableTopHat) {
            gadgetsFactory.append(new BookEntry("research.TopHat.name",
              new BookPageItem("research.TopHat.name", "research.TopHat.0", new ItemStack(TOP_HAT.getItem())),
              new BookPageCrafting("", "hat")));
            if (Config.enableEmeraldHat) {
                gadgetsFactory.append(new BookEntry("research.TopHatEmerald.name",
                  new BookPageItem("research.TopHatEmerald.name", "research.TopHatEmerald.0", new ItemStack(ENTREPRENEUR_TOP_HAT.getItem())),
                  new BookPageCrafting("", "hatEmerald")));
            }
        }
        if (Config.enableGoggles) {
            gadgetsFactory.append(new BookEntry("research.Goggles.name",
              new BookPageItem("research.Goggles.name", "research.Goggles.0", new ItemStack(GOGGLES.getItem()), new ItemStack(MONOCLE.getItem())),
              new BookPageCrafting("", "goggles1", "goggles2"),
              new BookPageCrafting("", "monocle1", "monocle2")));
        }

        if (Config.enableSteamCell) {
            gadgetsFactory.append(new BookEntry("research.SteamCell.name",
              new BookPageItem("research.SteamCell.name", "research.SteamCell.0", new ItemStack(STEAM_CELL_EMPTY.getItem()), new ItemStack(STEAM_CELL_FULL.getItem())),
              new BookPageCrafting("", "steamcell")));
        }

        if (Config.enableSteamCellBauble && CrossMod.BAUBLES) {
            gadgetsFactory.append(new BookEntry("research.SteamCellFiller.name",
              new BookPageItem("research.SteamCellFiller.name", "research.SteamCellFiller.0", new ItemStack(STEAM_CELL_FILLER.getItem())),
              new BookPageCrafting("", "steamcellFiller")));
        }

        BookPageRegistry.addTopCategory(gadgetsFactory.build());
    }

    public static void registerSteamTools() {
        if (!Config.enableSteamTools) {
            return;
        }
        BookEntry toolEntry = new BookEntry("research.SteamTools.name",
          new BookPageItem("research.SteamTools.name", "research.SteamTools.0", new ItemStack(STEAM_DRILL.getItem()), new ItemStack(STEAM_SAW.getItem()), new ItemStack(STEAM_SHOVEL.getItem())),
          new BookPageText("research.SteamTools.name", "research.SteamTools.1"),
          new BookPageCrafting("", "drill1", "drill2", "drill3", "drill4"),
          new BookPageCrafting("", "axe1", "axe2", "axe3", "axe4"),
          new BookPageCrafting("", "shovel1", "shovel2", "shovel3", "shovel4"));
        BookCategory.Factory toolFactory = new BookCategory.Factory("category.SteamTools.name").append(toolEntry);

        {
            BookCategory.Factory drillHeadFactory = new BookCategory.Factory("research.SteamDrillHead.name");

            ArrayList<String> drillMatsArray = new ArrayList<>();
            for (DrillHeadMaterial material : DrillHeadMaterial.materials.values()) {
                String loc = material.locName;
                String string = I18n.hasKey(loc) ? I18n.format(loc) : material.materialName;
                drillMatsArray.add(string);
            }

            StringBuilder drillMats = new StringBuilder();
            String delimiter = I18n.format("esteemedinnovation.book.listjoiner");
            Iterator iter = drillMatsArray.iterator();
            while (iter.hasNext()) {
                drillMats.append(iter.next());
                if (iter.hasNext()) {
                    drillMats.append(delimiter);
                }
            }

            drillHeadFactory.append(new BookEntry("research.SteamDrillHead.name",
              new BookPageItem("research.DrillHeads.name", "research.DrillHeads.0", new Object[]{drillMats.toString()}, true, new ItemStack(DRILL_HEAD.getItem())),
              new BookPage("")));

            if (Config.enableFortune) {
                drillHeadFactory.append(new BookEntry("research.MultiplicativeResonator.name",
                  new BookPageItem("research.MultiplicativeResonator.name", "research.MultiplicativeResonator.0", true, new ItemStack(MULTIPLICATIVE_RESONATOR.getItem())),
                  new BookPageCrafting("", "multiplicativeResonator")));
            }

            if (Config.enableBigDrill) {
                drillHeadFactory.append(new BookEntry("research.BigDrill.name",
                  new BookPageItem("research.BigDrill.name", "reseach.BigDrill.0", true, new ItemStack(BIG_DRILL.getItem())),
                  new BookPageCrafting("", "bigDrill")));
            }

            if (Config.enableBattleDrill) {
                drillHeadFactory.append(new BookEntry("research.BattleDrill.name",
                  new BookPageItem("research.BattleDrill.name", "research.BattleDrill.0", true, new ItemStack(BATTLE_DRILL.getItem())),
                  new BookPageCrafting("", "battleDrill")));
            }

            if (Config.enablePreciseCuttingHead) {
                drillHeadFactory.append(new BookEntry("research.PreciseCuttingHead.name",
                  new BookPageItem("research.PreciseCuttingHead.name", "research.PreciseCuttingHead.0", true, new ItemStack(PRECISE_CUTTING_HEAD.getItem())),
                  new BookPageCrafting("", "preciseCuttingHead")));
            }

            if (Config.enableStoneGrinder) {
                drillHeadFactory.append(new BookEntry("research.StoneGrinder.name",
                  new BookPageItem("research.StoneGrinder.name", "research.StoneGrinder.0", true, new ItemStack(STONE_GRINDER.getItem())),
                  new BookPageCrafting("", "stoneGrinder")));
            }

            if (Config.enableThermalDrill) {
                drillHeadFactory.append(new BookEntry("research.ThermalDrill.name",
                  new BookPageItem("research.ThermalDrill.name", "research.ThermalDrill.0", true, new ItemStack(THERMAL_DRILL.getItem())),
                  new BookPageCrafting("", "thermalDrill")));
            }

            if (Config.enableChargePlacer) {
                drillHeadFactory.append(new BookEntry("research.CalamityInjector.name",
                  new BookPageItem("research.CalamityInjector.name", "research.CalamityInjector.0", true, new ItemStack(CHARGE_PLACER.getItem())),
                  new BookPageCrafting("", "chargePlacer")));
            }

            toolFactory.append(drillHeadFactory.build());
        }
        {
            BookCategory.Factory drillCoreFactory = new BookCategory.Factory("research.SteamDrillCore.name");

            if (Config.enableInternalProcessingUnit) {
                drillCoreFactory.append(new BookEntry("research.InternalProcessingUnit.name",
                  new BookPageItem("research.InternalProcessingUnit.name", "research.InternalProcessingUnit.0", true, new ItemStack(INTERNAL_PROCESSING_UNIT.getItem())),
                  new BookPageCrafting("", "internalProcessingUnit")));
            }

            toolFactory.append(drillCoreFactory.build());
        }

        {
            BookCategory.Factory sawHeadFactory = new BookCategory.Factory("research.SteamSawHead.name");

            if (Config.enableForestFire) {
                sawHeadFactory.append(new BookEntry("research.ForestFire.name",
                  new BookPageItem("research.ForestFire.name", "research.ForestFire.0", true, new ItemStack(FOREST_FIRE.getItem())),
                  new BookPageCrafting("", "forestFire")));
            }

            if (Config.enableTreeFeller) {
                sawHeadFactory.append(new BookEntry("research.TimberChain.name",
                  new BookPageItem("research.TimberChain.name", "research.TimberChain.0", true, new ItemStack(TIMBER_CHAIN.getItem())),
                  new BookPageCrafting("", "treeFeller")));
            }

            if (Config.enableLeafBlower) {
                sawHeadFactory.append(new BookEntry("research.LeafBlower.name",
                  new BookPageItem("research.LeafBlower.name", "research.LeafBlower.0", true, new ItemStack(LEAF_BLOWER.getItem())),
                  new BookPageCrafting("", "leafBlower")));
            }

            if (Config.enableChainsaw) {
                sawHeadFactory.append(new BookEntry("research.Chainsaw.name",
                  new BookPageItem("research.Chainsaw.name", "research.Chainsaw.0", true, new ItemStack(CHAINSAW.getItem())),
                  new BookPageCrafting("", "chainsaw")));
            }

            toolFactory.append(sawHeadFactory.build());
        }

        {
            BookCategory.Factory sawCoreFactory = new BookCategory.Factory("research.SteamSawCore.name");
            toolFactory.append(sawCoreFactory.build());
        }

        {
            BookCategory.Factory shovelHeadFactory = new BookCategory.Factory("research.SteamShovelHead.name");

            if (Config.enableBackhoe) {
                shovelHeadFactory.append(new BookEntry("research.Backhoe.name",
                  new BookPageItem("research.Backhoe.name", "research.Backhoe.0", true, new ItemStack(BACKHOE.getItem())),
                  new BookPageCrafting("", "backhoe")));
            }

            if (Config.enableCultivator) {
                shovelHeadFactory.append(new BookEntry("research.Cultivator.name",
                  new BookPageItem("research.Cultivator.name", "research.Cultivator.0", true, new ItemStack(CULTIVATOR.getItem())),
                  new BookPageCrafting("", "cultivator")));
            }

            if (Config.enableRotaryBlades) {
                shovelHeadFactory.append(new BookEntry("research.RotaryBlades.name",
                  new BookPageItem("research.RotaryBlades.name", "research.RotaryBlades.0", true, new ItemStack(ROTARY_BLADES.getItem())),
                  new BookPageCrafting("", "rotaryBlades")));
            }

            toolFactory.append(shovelHeadFactory.build());
        }

        {
            BookCategory.Factory shovelCoreFactory = new BookCategory.Factory("research.SteamShovelCore.name");

            if (Config.enableSifter) {
                shovelCoreFactory.append(new BookEntry("research.Sifter.name",
                  new BookPageItem("research.Sifter.name", "research.Sifter.0", true, new ItemStack(SIFTER.getItem())),
                  new BookPageCrafting("", "sifter")));
            }

            toolFactory.append(shovelCoreFactory.build());
        }

        {
            BookCategory.Factory universalCoreFactory = new BookCategory.Factory("research.SteamUniversalCore.name");

            if (Config.enableOverclocker) {
                universalCoreFactory.append(new BookEntry("research.Overclocker.name",
                  new BookPageItem("research.Overclocker.name", "research.Overclocker.0", true, new ItemStack(OVERCLOCKER.getItem())),
                  new BookPageCrafting("", "overclocker")));
            }

            if (Config.enableAutosmelting) {
                universalCoreFactory.append(new BookEntry("research.ExothermicProjector.name",
                  new BookPageItem("research.ExothermicProjector.name", "research.ExothermicProjector.0", true, new ItemStack(EXOTHERMIC_PROJECTOR.getItem())),
                  new BookPageCrafting("", "autosmelting")));
            }

            if (Config.enableTheVoid) {
                universalCoreFactory.append(new BookEntry("research.TheVoid.name",
                  new BookPageItem("research.TheVoid.name", "research.TheVoid.0", true, new ItemStack(THE_VOID.getItem())),
                  new BookPageCrafting("", "theVoid")));
            }

            toolFactory.append(universalCoreFactory.build());
        }

        BookPageRegistry.addTopCategory(toolFactory.build());
    }

    public static void registerSteamPower() {
        BookEntry boilerEntry = new BookEntry("research.Boiler.name",
          new BookPageItem("research.Boiler.name", "research.Boiler.0", new ItemStack(BOILER.getBlock())),
          new BookPageCrafting("", "boiler1", "boiler2"));
        BookEntry pipeEntry = new BookEntry("research.Pipe.name",
          new BookPageItem("research.Pipe.name", "research.Pipe.0", new ItemStack(BRASS_PIPE.getBlock()), new ItemStack(VALVE_PIPE.getBlock())),
          new BookPageText("research.Pipe.name", "research.Pipe.1"));
        BookEntry tankEntry = new BookEntry("research.Tank.name",
          new BookPageItem("research.Tank.name", "research.Tank.0", new ItemStack(TANK.getBlock())),
          new BookPageCrafting("", "tank1", "tank2"));
        BookCategory.Factory steamPowerFactory = new BookCategory.Factory("category.SteamPower.name")
          .append(boilerEntry).append(pipeEntry).append(tankEntry);

        if (Config.enableRuptureDisc) {
            steamPowerFactory.append(new BookEntry("research.RuptureDisc.name",
              new BookPageItem("research.RuptureDisc.name", "research.RuptureDisc.0", new ItemStack(RUPTURE_DISC.getBlock())),
              new BookPageText("research.RuptureDisc.name", "research.RuptureDisc.1"),
              new BookPageCrafting("", "disc")));
        }

        if (Config.enableHorn) {
            steamPowerFactory.append(new BookEntry("research.Whistle.name",
              new BookPageItem("research.Whistle.name", "research.Whistle.0", new ItemStack(STEAM_WHISTLE.getBlock())),
              new BookPageCrafting("", "whistle1", "whistle2")));
        }

        if (Config.enableGauge) {
            steamPowerFactory.append(new BookEntry("research.Gauge.name",
              new BookPageItem("research.Gauge.name", "research.Gauge.0", new ItemStack(STEAM_GAUGE.getBlock())),
              new BookPageCrafting("", "gauge")));
        }

        if (Config.enableCharger) {
            steamPowerFactory.append(new BookEntry("research.Filler.name",
              new BookPageItem("research.Filler.name", "research.Filler.0", new ItemStack(STEAM_FILLER.getBlock())),
              new BookPageText("research.Filler.name", "research.Filler.1"),
              new BookPageCrafting("", "filler")));
        }

        if (Config.enableChargingPad && Config.enableCharger) {
            steamPowerFactory.append(new BookEntry("research.FillingPad.name",
              new BookPageItem("research.FillingPad.name", "research.FillingPad.0", new ItemStack(FILLING_PAD.getBlock())),
              new BookPageCrafting("", "fillingPad1", "fillingPad2")));
        }

        if (Config.enableHeater) {
            steamPowerFactory.append(new BookEntry("research.Heater.name",
              new BookPageItem("research.Heater.name", "research.Heater.0", new ItemStack(STEAM_HEATER.getBlock())),
              new BookPageCrafting("", "heater1", "heater2")));
        }

        if (Config.enableMortar && Config.enableAstrolabe) {
            steamPowerFactory.append(new BookEntry("research.ItemMortar.name",
              new BookPageItem("research.ItemMortar.name", "research.ItemMortar.0", new ItemStack(ITEM_MORTAR.getBlock())),
              new BookPageText("research.ItemMortar.name", "research.ItemMortar.1"),
              new BookPageCrafting("", "astrolabe"),
              new BookPageCrafting("", "itemMortar2", "itemMortar3")));
        }

        if (Config.enableHammer) {
            steamPowerFactory.append(new BookEntry("research.Hammer.name",
              new BookPageItem("research.Hammer.name", "research.Hammer.0", new ItemStack(STEAM_HAMMER.getBlock())),
              new BookPageText("research.Hammer.name", "research.Hammer.1"),
              new BookPageCrafting("", "hammer1", "hammer2")));
        }
        if (Config.enablePump) {
            steamPowerFactory.append(new BookEntry("research.Screw.name",
              new BookPageItem("research.Screw.name", "research.Screw.0", new ItemStack(ARCHIMEDES_SCREW.getBlock())),
              new BookPageCrafting("", "pump1", "pump2")));
        }

        if (Config.enableSmasher) {
            steamPowerFactory.append(new BookEntry("research.Smasher.name",
              new BookPageItem("research.Smasher.name", "research.Smasher.0", new ItemStack(ROCK_SMASHER.getBlock())),
              new BookPageText("research.Smasher.name", "research.Smasher.1"),
              new BookPageCrafting("", "smasher1", "smasher2", "smasher3", "smasher4")));
        }

        if (Config.enableThumper) {
            steamPowerFactory.append(new BookEntry("research.Thumper.name",
              new BookPageItem("research.Thumper.name", "research.Thumper.0", new ItemStack(THUMPER.getBlock())),
              new BookPageText("research.Thumper.name", "research.Thumper.1"),
              new BookPageCrafting("", "thumper1", "thumper2")));
        }

        if (Config.enableFan) {
            steamPowerFactory.append(new BookEntry("research.Fan.name",
              new BookPageItem("research.Fan.name", "research.Fan.0", new ItemStack(FAN.getBlock())),
              new BookPageCrafting("", "fan1", "fan2")));
            if (Config.enableVacuum) {
                steamPowerFactory.append(new BookEntry("research.Vacuum.name",
                  new BookPageItem("research.Vacuum.name", "research.Vacuum.0", new ItemStack(VACUUM.getBlock())),
                  new BookPageCrafting("", "vacuum1", "vacuum2")));
            }
        }

        if (Config.enableFluidSteamConverter) {
            steamPowerFactory.append(new BookEntry("research.FSC.name",
              new BookPageItem("research.FSC.name", "research.FSC.0", new ItemStack(PRESSURE_CONVERTER.getBlock())),
              new BookPageCrafting("", "fsc1", "fsc2"),
              new BookPageText("", "research.FSC.1"),
              new BookPageText("", "research.FSC.2")));
        }

        BookPageRegistry.addTopCategory(steamPowerFactory.build());

        BookPageRegistry.bookRecipes.put(new ItemStack(TANK.getBlock(), 1, 1), Pair.of("research.CreativeTank.name", 0));
        BookPageRegistry.addTopCategory(new BookCategory("category.NOTREAL.name", new BookEntry[] {
          new BookEntry("research.CreativeTank.name", new BookPageItem("research.CreativeTank.name", "research.CreativeTank.0", new ItemStack(Items.BOWL)))
        }) {
            @Override
            public boolean isHidden(EntityPlayer player) {
                return true;
            }
        });
    }

    public static void registerExosuit() {
        if (!Config.enableExosuit || !Config.enableEngineering) {
            return;
        }

        BookEntry exosuitEntry = new BookEntry("research.Exosuit.name",
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
          new BookPageCrafting("", "exoFeet"));
        BookCategory.Factory exosuitFactory = new BookCategory.Factory("category.Exosuit.name").append(exosuitEntry);

        {
            BookCategory.Factory tankFactory = new BookCategory.Factory("research.ExoTank.name")
              .append(new BookEntry("research.ExoTank.name",
                new BookPageItem("research.ExoTank.name", "research.ExoTank.0", new ItemStack(TANK.getBlock())),
                new BookPageItem("research.ExoTankBase.name", "research.ExoTankBase.0", true, new ItemStack(TANK.getBlock()))));
            if (Config.enableReinforcedTank) {
                tankFactory.append(new BookEntry("research.ExoTankReinforced.name",
                  new BookPageItem("research.ExoTankReinforced.name", "research.ExoTankReinforced.0", true, new ItemStack(REINFORCED_TANK.getItem())),
                  new BookPageCrafting("", "reinforcedTank1", "reinforcedTank2")));
            }
            if (Config.enableUberReinforcedTank) {
                tankFactory.append(new BookEntry("research.ExoTankUberReinforced",
                  new BookPageItem("research.ExoTankUberReinforced.name", "research.ExoTankUberReinforced.0", true, new ItemStack(UBER_REINFORCED_TANK.getItem())),
                  new BookPageCrafting("", "uberReinforcedTank1", "uberReinforcedTank2")));
            }
            exosuitFactory.append(tankFactory.build());
        }

        {
            ItemStack[] stacks = new ItemStack[4];
            for (int i = 0; i < 4; i++) {
                ItemStack stack = new ItemStack(EXOSUIT_CHESTPIECE.getItem());
                stack.setTagCompound(new NBTTagCompound());
                ItemStack plate = null;
                Collection<ExosuitPlate> values = ExosuitRegistry.plates.values();
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

            BookCategory.Factory plateFactory = new BookCategory.Factory("research.ExoPlates.name")
              .append(new BookEntry("research.ExoPlates.name",
                new BookPageItem("research.ExoPlates.name", "research.ExoPlates.0", stacks),
                new BookPageText("", "research.ExoPlates.1")));

            if (Config.enableCopperPlate) {
                plateFactory.append(new BookEntry("research.PlateCopper.name",
                  new BookPageItem("research.PlateCopper.name", "research.PlateCopper.0", true, COPPER_EXO.createItemStack()),
                  new BookPageCrafting("", "exoCopper")));
            }
            if (Config.enableIronPlate) {
                plateFactory.append(new BookEntry("research.PlateIron.name",
                  new BookPageItem("research.PlateIron.name", "research.PlateIron.0", true, IRON_EXO.createItemStack()),
                  new BookPageCrafting("", "exoIron")));
            }
            if (Config.enableBrassPlate) {
                plateFactory.append(new BookEntry("research.PlateBrass.name",
                  new BookPageItem("research.PlateBrass.name", "research.PlateBrass.0", true, BRASS_EXO.createItemStack()),
                  new BookPageCrafting("", "exoBrass")));
            }
            if (Config.enableGoldPlate) {
                plateFactory.append(new BookEntry("research.PlateGold.name",
                  new BookPageItem("research.PlateGold.name", "research.PlateGold.0", true, GOLD_EXO.createItemStack()),
                  new BookPageCrafting("", "exoGold")));
            }
            if (Config.enableGildedIronPlate) {
                plateFactory.append(new BookEntry("research.PlateGilded.name",
                  new BookPageItem("research.PlateGilded.name", "research.PlateGilded.0", true, GILDED_IRON_EXO.createItemStack()),
                  new BookPageCrafting("", "exoGildedIron")));
            }
            if (Config.enableLeadPlate && OreDictionary.getOres(OreDictEntries.INGOT_LEAD).size() > 0) {
                plateFactory.append(new BookEntry("research.PlateLead.name",
                  new BookPageItem("research.PlateLead.name", "research.PlateLead.0", true, LEAD_EXO.createItemStack()),
                  new BookPageCrafting("", "exoLead")));
            }
            exosuitFactory.append(plateFactory.build());
        }
        {
            ItemStack[] stacks = new ItemStack[4];
            for (int i = 0; i < 4; i++) {
                ItemStack stack = new ItemStack(EXOSUIT_CHESTPIECE.getItem());
                stack.setTagCompound(new NBTTagCompound());
                ItemStack dye = new ItemStack(Items.DYE, 1, i);
                ((ItemExosuitArmor) stack.getItem()).setInventorySlotContents(stack, 2, dye);
                stacks[i] = stack;
            }
            ItemStack[] dyes = {
              new ItemStack(Items.DYE, 1, 0),
              new ItemStack(Items.DYE, 1, 1),
              new ItemStack(Items.DYE, 1, 2),
              new ItemStack(Items.DYE, 1, 3)
            };
            BookCategory.Factory vanityFactory = new BookCategory.Factory("research.ExoVanity.name")
              .append(new BookEntry("research.ExoVanity.name",
                new BookPageItem("research.ExoVanity.name", "research.ExoVanity.0", stacks)))
              .append(new BookEntry("research.ExoDyes.name",
                new BookPageItem("research.ExoDyes.name", "research.ExoDyes.0", true, dyes)));
            if (Config.enableEnderShroud) {
                vanityFactory.append(new BookEntry("research.EnderShroud.name",
                  new BookPageItem("research.EnderShroud.name", "research.EnderShroud.0", true, new ItemStack(ENDER_SHROUD.getItem())),
                  new BookPageCrafting("", "enderShroud")));
            }
            exosuitFactory.append(vanityFactory.build());
        }

        {
            BookCategory.Factory headHelmFactory = new BookCategory.Factory("research.ExoHeadHelm.name");
            if (Config.enableTopHat) {
                headHelmFactory.append(new BookEntry("research.ExoTopHat.name",
                  new BookPageItem("research.ExoTopHat.name", "research.ExoTopHat.0", true, new ItemStack(TOP_HAT.getItem()), new ItemStack(ENTREPRENEUR_TOP_HAT.getItem())),
                  new BookPage("")));
            }
            if (Config.enableFrequencyShifter) {
                headHelmFactory.append(new BookEntry("research.FrequencyShifter.name",
                  new BookPageItem("research.FrequencyShifter.name", "research.FrequencyShifter.0", true, new ItemStack(FREQUENCY_SHIFTER.getItem())),
                  new BookPageCrafting("", "frequencyShifter")));
            }
            exosuitFactory.append(headHelmFactory.build());
        }

        {
            BookCategory.Factory headGoggleFactory = new BookCategory.Factory("research.ExoHeadGoggle.name");
            if (Config.enableGoggles) {
                headGoggleFactory.append(new BookEntry("research.ExoGoggles.name",
                  new BookPageItem("research.ExoGoggles.name", "research.ExoGoggles.0", true, new ItemStack(GOGGLES.getItem()), new ItemStack(MONOCLE.getItem())),
                  new BookPage("")));
            }
            if (Config.enableRebreather) {
                headGoggleFactory.append(new BookEntry("research.Rebreather.name",
                  new BookPageItem("research.Rebreather.name", "research.Rebreather.0", true, new ItemStack(REBREATHER.getItem())),
                  new BookPageCrafting("", "rebreather")));
            }
            if (Config.enableDragonRoar) {
                headGoggleFactory.append(new BookEntry("research.DragonRoar.name",
                  new BookPageItem("research.DragonRoar.name", "research.DragonRoar.0", true, new ItemStack(DRAGON_ROAR.getItem())),
                  new BookPageCrafting("", "dragonRoar")));
            }
            exosuitFactory.append(headGoggleFactory.build());
        }

        {
            BookCategory.Factory backFactory = new BookCategory.Factory("research.ExoBack.name");
            if (Config.enableJetpack) {
                backFactory.append(new BookEntry("research.Jetpack.name",
                  new BookPageItem("research.Jetpack.name", "research.Jetpack.0", true, new ItemStack(JETPACK.getItem())),
                  new BookPageCrafting("", "jetpack1", "jetpack2")));
            }
            if (Config.enableWings) {
                backFactory.append(new BookEntry("research.Wings.name",
                  new BookPageItem("research.Wings.name", "research.Wings.0", true, new ItemStack(WINGS.getItem())),
                  new BookPageCrafting("", "wings1", "wings2")));
            }
            exosuitFactory.append(backFactory.build());
        }

        {
            BookCategory.Factory armFactory = new BookCategory.Factory("research.ExoArm.name");
            if (Config.enablePowerFist) {
                armFactory.append(new BookEntry("research.Fist.name",
                  new BookPageItem("research.Fist.name", "research.Fist.0", true, new ItemStack(POWER_FIST.getItem())),
                  new BookPageCrafting("", "powerFist1", "powerFist2")));
            }
            if (Config.enableExtendoFist) {
                armFactory.append(new BookEntry("research.ExtendoFist.name",
                  new BookPageItem("research.ExtendoFist.name", "research.ExtendoFist.0", true, new ItemStack(EXTENDO_FIST.getItem())),
                  new BookPageCrafting("", "extendoFist1", "extendoFist2")));
            }
            if (Config.enablePitonDeployer) {
                armFactory.append(new BookEntry("research.PitonDeployer.name",
                  new BookPageItem("research.PitonDeployer.name", "research.PitonDeployer.0", true, new ItemStack(PITON_DEPLOYER.getItem())),
                  new BookPageCrafting("", "pitonDeployer")));
            }
            if (Config.enablePistonPush) {
                armFactory.append(new BookEntry("research.PistonPush.name",
                  new BookPageItem("research.PistonPush.name", "research.PistonPush.0", true, new ItemStack(PISTON_PUSH.getItem())),
                  new BookPageCrafting("", "pistonPush")));
            }
            exosuitFactory.append(armFactory.build());
        }

        {
            BookCategory.Factory hipFactory = new BookCategory.Factory("research.ExoHip.name");
            if (Config.enableThrusters) {
                hipFactory.append(new BookEntry("research.Thrusters.name",
                  new BookPageItem("research.Thrusters.name", "research.Thrusters.0", true, new ItemStack(THRUSTERS.getItem())),
                  new BookPageCrafting("", "thrusters1", "thrusters2")));
            }
            if (Config.enableCanningMachine) {
                hipFactory.append(new BookEntry("research.Canner.name",
                  new BookPageItem("research.Canner.name", "research.Canner.0", true, new ItemStack(CANNING_MACHINE.getItem())),
                  new BookPageCrafting("", "canner1", "canner2", "canner3", "canner4")));
            }
            if (Config.enableReloadingHolsters) {
                hipFactory.append(new BookEntry("research.ReloadingHolsters.name",
                  new BookPageItem("research.ReloadingHolsters.name", "research.ReloadingHolsters.0", true, new ItemStack(RELOADING_HOLSTERS.getItem())),
                  new BookPageCrafting("", "reloadingHolsters")));
            }
            exosuitFactory.append(hipFactory.build());
        }

        {
            BookCategory.Factory legFactory = new BookCategory.Factory("research.ExoLeg.name");
            if (Config.enableRunAssist) {
                legFactory.append(new BookEntry("research.RunAssist.name",
                  new BookPageItem("research.RunAssist.name", "research.RunAssist.0", true, new ItemStack(RUN_ASSIST.getItem())),
                  new BookPageCrafting("", "runAssist1", "runAssist2")));
            }
            if (Config.enableStealthUpgrade) {
                legFactory.append(new BookEntry("research.StealthUpgrade.name",
                  new BookPageItem("research.StealthUpgrade.name", "research.StealthUpgrade.0", true, new ItemStack(STEALTH.getItem())),
                  new BookPageCrafting("", "stealthUpgrade")));
            }
            exosuitFactory.append(legFactory.build());
        }

        {
            BookCategory.Factory heelFactory = new BookCategory.Factory("research.ExoHell.name");
            if (Config.enableFallAssist) {
                heelFactory.append(new BookEntry("research.FallAssist.name",
                  new BookPageItem("research.FallAssist.name", "research.FallAssist.0", true, new ItemStack(FALL_ASSIST.getItem())),
                  new BookPageCrafting("", "noFall")));
            }
            if (Config.enableAnchorHeels) {
                boolean lead = Config.enableLeadPlate &&
                  OreDictionary.getOres(OreDictEntries.INGOT_LEAD).size() > 0 && !Config.enableAnchorAnvilRecipe;
                String desc = lead ? "research.AnchorHeelsLead.0" : "research.AnchorHeelsIron.0";
                heelFactory.append(new BookEntry("research.AnchorHeels.name",
                  new BookPageItem("research.AnchorHeels.name", desc, true, new ItemStack(ANCHOR_HEELS.getItem())),
                  new BookPageCrafting("", "anchorHeels")));
            }
            exosuitFactory.append(heelFactory.build());
        }

        {
            BookCategory.Factory footFactory = new BookCategory.Factory("research.ExoFoot.name");
            if (Config.enableDoubleJump) {
                footFactory.append(new BookEntry("research.DoubleJump.name",
                  new BookPageItem("research.DoubleJump.name", "research.DoubleJump.0", true, new ItemStack(DOUBLE_JUMP.getItem())),
                  new BookPageCrafting("", "doubleJump1", "doubleJump2")));
            }
            if (Config.enableHydrophobic) {
                footFactory.append(new BookEntry("research.Hydrophobic.name",
                  new BookPageItem("research.Hydrophobic.name", "research.Hydrophobic.0", true, new ItemStack(HYDROPHOBIC_COATINGS.getItem())),
                  new BookPageCrafting("", "hydrophobic")));
            }
            if (Config.enablePyrophobic) {
                footFactory.append(new BookEntry("research.Pyrophobic.name",
                  new BookPageItem("research.Pyrophobic.name", "research.Pyrophobic.0", true, new ItemStack(PYROPHOBIC_COATINGS.getItem())),
                  new BookPageCrafting("", "pyrophobic")));
            }
            if (Config.enableJumpAssist) {
                footFactory.append(new BookEntry("research.JumpAssist.name",
                  new BookPageItem("research.JumpAssist.name", "research.JumpAssist.0", true, new ItemStack(JUMP_ASSIST.getItem())),
                  new BookPageCrafting("", "jumpAssist1", "jumpAssist2")));
            }
            exosuitFactory.append(footFactory.build());
        }

        BookPageRegistry.addTopCategory(exosuitFactory.build());
    }

    private static void registerMisc() {
        BookCategory.Factory miscFactory = new BookCategory.Factory("category.Misc.name");
        if (FUNNEL.isEnabled()) {
            miscFactory.append(new BookEntry("research.Funnel.name",
              new BookPageItem("research.Funnel.name", "research.Funnel.0", true, new ItemStack(FUNNEL.getBlock())),
              new BookPageCrafting("", "funnel")));
        }
        BookPageRegistry.addTopCategory(miscFactory.build());
    }
}
