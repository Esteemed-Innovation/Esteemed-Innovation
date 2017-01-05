package eiteam.esteemedinnovation.tools;

import baubles.api.BaubleType;
import eiteam.esteemedinnovation.api.Constants;
import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.api.tool.SteamToolSlot;
import eiteam.esteemedinnovation.api.tool.SteamToolUpgrade;
import eiteam.esteemedinnovation.api.tool.ToolUpgradeRegistry;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.CrossMod;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import eiteam.esteemedinnovation.misc.ItemBauble;
import eiteam.esteemedinnovation.tools.standard.*;
import eiteam.esteemedinnovation.tools.steam.*;
import eiteam.esteemedinnovation.tools.steam.upgrades.ItemExothermicProjector;
import eiteam.esteemedinnovation.tools.steam.upgrades.ItemOverclockerUpgrade;
import eiteam.esteemedinnovation.tools.steam.upgrades.ItemSteamToolUpgrade;
import eiteam.esteemedinnovation.tools.steam.upgrades.ItemTheVoidUpgrade;
import eiteam.esteemedinnovation.tools.steam.upgrades.drillhead.DrillHeadMaterial;
import eiteam.esteemedinnovation.tools.steam.upgrades.drillhead.DrillHeadRecipe;
import eiteam.esteemedinnovation.tools.steam.upgrades.drillhead.ItemDrillHeadUpgrade;
import eiteam.esteemedinnovation.tools.steam.upgrades.drillhead.SteamDrillHeadUpgradeColorHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import static eiteam.esteemedinnovation.commons.EsteemedInnovation.GADGET_CATEGORY;
import static eiteam.esteemedinnovation.commons.EsteemedInnovation.STEAMTOOL_CATEGORY;
import static eiteam.esteemedinnovation.commons.EsteemedInnovation.tabTools;
import static eiteam.esteemedinnovation.commons.OreDictEntries.*;
import static eiteam.esteemedinnovation.heater.HeaterModule.STEAM_HEATER;
import static eiteam.esteemedinnovation.misc.ItemCraftingComponent.Types.BRASS_PISTON;
import static eiteam.esteemedinnovation.misc.ItemCraftingComponent.Types.BRASS_TURBINE;
import static eiteam.esteemedinnovation.misc.MiscellaneousModule.COMPONENT;
import static eiteam.esteemedinnovation.smasher.SmasherModule.ROCK_SMASHER;
import static eiteam.esteemedinnovation.transport.TransportationModule.*;
import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;

public class ToolsModule extends ContentModule {
    public static Item WRENCH;
    public static Item SURVIVALIST_TOOLKIT;

    public static Item BRASS_SWORD;
    public static Item BRASS_PICKAXE;
    public static Item BRASS_AXE;
    public static Item BRASS_SHOVEL;
    public static Item BRASS_HOE;

    public static Item GILDED_IRON_SWORD;
    public static Item GILDED_IRON_PICKAXE;
    public static Item GILDED_IRON_AXE;
    public static Item GILDED_IRON_SHOVEL;
    public static Item GILDED_IRON_HOE;

    public static Item STEAM_DRILL;
    public static Item STEAM_SAW;
    public static Item STEAM_SHOVEL;

    public static Item THE_VOID;
    public static Item EXOTHERMIC_PROJECTOR;
    public static Item OVERCLOCKER;
    public static Item BIG_DRILL;
    public static Item BATTLE_DRILL;
    public static Item STONE_GRINDER;
    public static Item PRECISE_CUTTING_HEAD;
    public static Item THERMAL_DRILL;
    public static Item MULTIPLICATIVE_RESONATOR;
    public static Item CHARGE_PLACER;
    public static Item DRILL_HEAD;
    public static Item INTERNAL_PROCESSING_UNIT;
    public static Item LEAF_BLOWER;
    public static Item TIMBER_CHAIN;
    public static Item CHAINSAW;
    public static Item FOREST_FIRE;
    public static Item CULTIVATOR;
    public static Item ROTARY_BLADES;
    public static Item SIFTER;
    public static Item BACKHOE;

    public static final Item.ToolMaterial GILDED_IRON_MAT = EnumHelper.addToolMaterial("GILDEDIRON", 2, 250, 6F, 2F, 22);
    public static final Item.ToolMaterial BRASS_MAT = EnumHelper.addToolMaterial("BRASS", 2, 191, 7F, 2.5F, 14);
    public static final Item.ToolMaterial STEAMDRILL_MAT = EnumHelper.addToolMaterial("STEAMDRILL", 2, 320, 1F, -1F, 0);
    public static final Item.ToolMaterial STEAMSAW_MAT = EnumHelper.addToolMaterial("STEAMSAW", 2, 320, 1F, -1F, 0);
    public static final Item.ToolMaterial STEAMSHOVEL_MAT = EnumHelper.addToolMaterial("STEAMSHOVEL", 2, 320, 1F, -1F, 0);


    @Override
    public void create(Side side) {
        WRENCH = setup(new ItemWrench(), "wrench");
        SURVIVALIST_TOOLKIT = setup(CrossMod.BAUBLES ? new ItemBauble(BaubleType.BELT).setMaxStackSize(1) : new Item().setMaxStackSize(1), "survivalist");

        BRASS_SWORD = setup(new ItemGenericSword(BRASS_MAT, INGOT_BRASS), "brass_sword", tabTools);
        BRASS_PICKAXE = setup(new ItemGenericPickaxe(BRASS_MAT, INGOT_BRASS), "brass_pickaxe", tabTools);
        BRASS_AXE = setup(new ItemGenericAxe(BRASS_MAT, INGOT_BRASS), "brass_axe", tabTools);
        BRASS_SHOVEL = setup(new ItemGenericShovel(BRASS_MAT, INGOT_BRASS), "brass_shovel", tabTools);
        BRASS_HOE = setup(new ItemGenericHoe(BRASS_MAT, INGOT_BRASS), "brass_hoe", tabTools);

        GILDED_IRON_SWORD = setup(new ItemGenericSword(GILDED_IRON_MAT, INGOT_GILDED_IRON), "gilded_iron_sword", tabTools);
        GILDED_IRON_PICKAXE = setup(new ItemGenericPickaxe(GILDED_IRON_MAT, INGOT_GILDED_IRON), "gilded_iron_pickaxe", tabTools);
        GILDED_IRON_AXE = setup(new ItemGenericAxe(GILDED_IRON_MAT, INGOT_GILDED_IRON), "gilded_iron_axe", tabTools);
        GILDED_IRON_SHOVEL = setup(new ItemGenericShovel(GILDED_IRON_MAT, INGOT_GILDED_IRON), "gilded_iron_shovel", tabTools);
        GILDED_IRON_HOE = setup(new ItemGenericHoe(GILDED_IRON_MAT, INGOT_GILDED_IRON), "gilded_iron_hoe", tabTools);

        STEAM_DRILL = setup(new ItemSteamDrill(), "steam_drill");
        STEAM_SAW = setup(new ItemSteamAxe(), "steam_saw");
        STEAM_SHOVEL = setup(new ItemSteamShovel(), "steam_shovel");

        THE_VOID = setupUpgrade(new ItemTheVoidUpgrade(), "the_void");
        EXOTHERMIC_PROJECTOR = setupUpgrade(new ItemExothermicProjector(), "exothermic_projector");
        OVERCLOCKER = setupUpgrade(new ItemOverclockerUpgrade(), "overclocker");
        BIG_DRILL = setupUpgrade(new ItemSteamToolUpgrade(SteamToolSlot.DRILL_HEAD, upgradeResource("big"), null, 1), "big_drill");
        BATTLE_DRILL = setupUpgrade(new ItemSteamToolUpgrade(SteamToolSlot.DRILL_HEAD, upgradeResource("combat"), null, 1), "battle_drill");
        STONE_GRINDER = setupUpgrade(new ItemSteamToolUpgrade(SteamToolSlot.DRILL_HEAD, upgradeResource("grinder"), null, 1), "stone_grinder");
        PRECISE_CUTTING_HEAD = setupUpgrade(new ItemSteamToolUpgrade(SteamToolSlot.DRILL_HEAD, upgradeResource("preciseCuttingHead"), null, 1), "precise_cutting_head");
        THERMAL_DRILL = setupUpgrade(new ItemSteamToolUpgrade(SteamToolSlot.DRILL_HEAD, upgradeResource("thermal"), null, 1), "thermal_drill");
        MULTIPLICATIVE_RESONATOR = setupUpgrade(new ItemSteamToolUpgrade(SteamToolSlot.DRILL_HEAD, upgradeResource("fortune"), null, 1), "multiplicative_resonator");
        CHARGE_PLACER = setupUpgrade(new ItemSteamToolUpgrade(SteamToolSlot.DRILL_HEAD, upgradeResource("chargePlacer"), null, 1), "charge_placer");
        DRILL_HEAD = setupUpgrade(new ItemDrillHeadUpgrade(), "drill_head");
        INTERNAL_PROCESSING_UNIT = setupUpgrade(new ItemSteamToolUpgrade(SteamToolSlot.DRILL_CORE, upgradeResource("processor"), null, 0), "internal_processing_unit");
        LEAF_BLOWER = setupUpgrade(new ItemSteamToolUpgrade(SteamToolSlot.SAW_HEAD, upgradeResource("blower"), null, 1), "leaf_blower");
        TIMBER_CHAIN = setupUpgrade(new ItemSteamToolUpgrade(SteamToolSlot.SAW_HEAD, upgradeResource("timberHead"), null, 1), "timber_chain");
        CHAINSAW = setupUpgrade(new ItemSteamToolUpgrade(SteamToolSlot.SAW_HEAD, upgradeResource("chain"), null, 1), "chainsaw");
        FOREST_FIRE = setupUpgrade(new ItemSteamToolUpgrade(SteamToolSlot.SAW_HEAD, upgradeResource("fire"), null, 1), "forest_fire");
        CULTIVATOR = setupUpgrade(new ItemSteamToolUpgrade(SteamToolSlot.SHOVEL_HEAD, upgradeResource("cultivatorHead"), null, 1), "cultivator");
        ROTARY_BLADES = setupUpgrade(new ItemSteamToolUpgrade(SteamToolSlot.SHOVEL_HEAD, upgradeResource("rotary"), null, 1), "rotary_blades");
        SIFTER = setupUpgrade(new ItemSteamToolUpgrade(SteamToolSlot.SHOVEL_CORE, upgradeResource("sifter"), null, 0), "sifter");
        BACKHOE = setupUpgrade(new ItemSteamToolUpgrade(SteamToolSlot.SHOVEL_HEAD, upgradeResource("backhoe"), null, 1), "backhoe");
    }

    private Item setupUpgrade(SteamToolUpgrade upgrade, String path) {
        upgrade = (SteamToolUpgrade) setup((Item) upgrade, path);
        ToolUpgradeRegistry.register(upgrade);
        return (Item) upgrade;
    }

    private static String upgradeResource(String resource) {
        return Constants.EI_MODID + ":items/toolUpgrades/" + resource;
    }

    @Override
    public void recipes(Side side) {
        BookRecipeRegistry.addRecipe("wrench1", new ShapedOreRecipe(WRENCH,
          "  i",
          " bb",
          "b  ",
          'i', INGOT_IRON,
          'b', PLATE_THIN_BRASS
        ));
        BookRecipeRegistry.addRecipe("wrench2", new ShapedOreRecipe(WRENCH,
          "  i",
          " bb",
          "b  ",
          'i', INGOT_IRON,
          'b', INGOT_BRASS
        ));

        if (Config.enableSurvivalist) {
            BookRecipeRegistry.addRecipe("survivalist", new ShapedOreRecipe(SURVIVALIST_TOOLKIT,
              "b s",
              "xwx",
              "xxx",
              'x', LEATHER_ORE,
              's', STRING_ORE,
              'b', BRICK,
              'w', STICK_WOOD
            ));
        }

        if (Config.enableSteamTools) {
            ItemStack drill = new ItemStack(STEAM_DRILL, 1, STEAM_DRILL.getMaxDamage() - 1);
            BookRecipeRegistry.addRecipe("drill1", new ShapedOreRecipe(drill,
              "xii",
              "pti",
              "xpx",
              'x', INGOT_BRASS,
              'i', INGOT_IRON,
              'p', BRASS_PIPE,
              't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
            ));
            BookRecipeRegistry.addRecipe("drill2", new ShapedOreRecipe(drill,
              "xii",
              "pti",
              "xpx",
              'x', PLATE_THIN_BRASS,
              'i', INGOT_IRON,
              'p', BRASS_PIPE,
              't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
            ));
            BookRecipeRegistry.addRecipe("drill3", new ShapedOreRecipe(drill,
              "xii",
              "pti",
              "xpx",
              'x', INGOT_BRASS,
              'i', PLATE_THIN_IRON,
              'p', BRASS_PIPE,
              't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
            ));
            BookRecipeRegistry.addRecipe("drill4", new ShapedOreRecipe(drill,
              "xii",
              "pti",
              "xpx",
              'x', PLATE_THIN_BRASS,
              'i', PLATE_THIN_IRON,
              'p', BRASS_PIPE,
              't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
            ));

            ItemStack saw = new ItemStack(STEAM_SAW, 1, STEAM_SAW.getMaxDamage() - 1);
            BookRecipeRegistry.addRecipe("axe1", new ShapedOreRecipe(saw,
              "ini",
              "ptn",
              "xpi",
              'x', INGOT_BRASS,
              'i', INGOT_IRON,
              'p', BRASS_PIPE,
              't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata()),
              'n', NUGGET_IRON
            ));
            BookRecipeRegistry.addRecipe("axe2", new ShapedOreRecipe(saw,
              "ini",
              "ptn",
              "xpi",
              'x', PLATE_THIN_BRASS,
              'i', INGOT_IRON,
              'p', BRASS_PIPE,
              't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata()),
              'n', NUGGET_IRON
            ));
            BookRecipeRegistry.addRecipe("axe3", new ShapedOreRecipe(saw,
              "ini",
              "ptn",
              "xpi",
              'x', INGOT_BRASS,
              'i', PLATE_THIN_IRON,
              'p', BRASS_PIPE,
              't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata()),
              'n', NUGGET_IRON
            ));
            BookRecipeRegistry.addRecipe("axe4", new ShapedOreRecipe(saw,
              "ini",
              "ptn",
              "xpi",
              'x', PLATE_THIN_BRASS,
              'i', PLATE_THIN_IRON,
              'p', BRASS_PIPE,
              't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata()),
              'n', NUGGET_IRON
            ));

            ItemStack shovel = new ItemStack(STEAM_SHOVEL, 1, STEAM_SHOVEL.getMaxDamage() - 1);
            BookRecipeRegistry.addRecipe("shovel1", new ShapedOreRecipe(shovel,
              "ixi",
              "ptx",
              "xpi",
              'x', INGOT_BRASS,
              'i', INGOT_IRON,
              'p', BRASS_PIPE,
              't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
            ));
            BookRecipeRegistry.addRecipe("shovel2", new ShapedOreRecipe(shovel,
              "ixi",
              "ptx",
              "xpi",
              'x', PLATE_THIN_BRASS,
              'i', INGOT_IRON,
              'p', BRASS_PIPE,
              't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
            ));
            BookRecipeRegistry.addRecipe("shovel3", new ShapedOreRecipe(shovel,
              "ixi",
              "ptx",
              "xpi",
              'x', INGOT_BRASS,
              'i', PLATE_THIN_IRON,
              'p', BRASS_PIPE,
              't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
            ));
            BookRecipeRegistry.addRecipe("shovel4", new ShapedOreRecipe(shovel,
              "ixi",
              "ptx",
              "xpi",
              'x', PLATE_THIN_BRASS,
              'i', PLATE_THIN_IRON,
              'p', BRASS_PIPE,
              't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
            ));

            for (Map.Entry<String, DrillHeadMaterial> entry : DrillHeadMaterial.materials.entrySet()) {
                String materialString = entry.getKey();
                DrillHeadMaterial headMat = entry.getValue();
                if (headMat.standard) {
                    GameRegistry.addRecipe(new DrillHeadRecipe(DRILL_HEAD,
                      " n ",
                      "iii",
                      "ppp",
                      'n', PREFIX_NUGGET + materialString,
                      'i', PREFIX_INGOT + materialString,
                      'p', PLATE_THIN_IRON)
                    );
                } else {
                    GameRegistry.addRecipe(new DrillHeadRecipe(DRILL_HEAD,
                      " g ",
                      "ggg",
                      "ppp",
                      'g', headMat.oreName,
                      'p', PLATE_THIN_IRON)
                    );
                }
            }

            if (Config.enableBigDrill) {
                BookRecipeRegistry.addRecipe("bigDrill", new ShapedOreRecipe(BIG_DRILL,
                  " p ",
                  "pip",
                  "ibi",
                  'p', PLATE_THIN_IRON,
                  'i', INGOT_IRON,
                  'b', BLOCK_IRON
                ));
            }
            if (Config.enableBattleDrill) {
                BookRecipeRegistry.addRecipe("battleDrill",
                  new ShapedOreRecipe(BATTLE_DRILL,
                    " s ",
                    "sbs",
                    " p ",
                    's', IRON_SWORD,
                    'b', PLATE_THIN_BRASS,
                    'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata())
                  ));
            }
            if (Config.enableStoneGrinder) {
                BookRecipeRegistry.addRecipe("stoneGrinder",
                  new ShapedOreRecipe(STONE_GRINDER,
                    "i i",
                    "ctc",
                    "pcp",
                    'i', INGOT_IRON,
                    'c', COBBLESTONE_ORE,
                    'p', PLATE_THIN_IRON,
                    't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
                  ));
            }
            if (Config.enablePreciseCuttingHead) {
                BookRecipeRegistry.addRecipe("preciseCuttingHead",
                  new ShapedOreRecipe(PRECISE_CUTTING_HEAD,
                    "f f",
                    "pdp",
                    "p p",
                    'f', FLINT,
                    'p', PLATE_THIN_BRASS,
                    'd', GEM_DIAMOND
                  ));
            }
            if (Config.enableThermalDrill) {
                BookRecipeRegistry.addRecipe("thermalDrill",
                  new ShapedOreRecipe(THERMAL_DRILL,
                    " b ",
                    "bnb",
                    "iii",
                    'b', BLAZE_ROD,
                    'n', NETHER_BRICK,
                    'i', INGOT_BRASS
                  ));
            }
            if (Config.enableFortune) {
                ItemStack fortuneBook = new ItemStack(ENCHANTED_BOOK);
                fortuneBook.addEnchantment(Enchantments.FORTUNE, 3);
                BookRecipeRegistry.addRecipe("multiplicativeResonator",
                  new ShapedOreRecipe(MULTIPLICATIVE_RESONATOR,
                    "rgr",
                    "rbr",
                    "rgr",
                    'r', DUST_REDSTONE,
                    'g', PLATE_THIN_GILDED_IRON,
                    'b', fortuneBook
                  ));
            }
            if (Config.enableChargePlacer) {
                BookRecipeRegistry.addRecipe("chargePlacer",
                  new ShapedOreRecipe(CHARGE_PLACER,
                    "g g",
                    "vbv",
                    "sps",
                    'g', GOLDEN_SWORD,
                    'v', VALVE_PIPE,
                    'b', STONE_BUTTON,
                    's', BRASS_PIPE,
                    'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata())
                  ));
            }
            if (Config.enableInternalProcessingUnit) {
                BookRecipeRegistry.addRecipe("internalProcessingUnit",
                  new ShapedOreRecipe(INTERNAL_PROCESSING_UNIT,
                    "sco",
                    's', ROCK_SMASHER,
                    'c', CHEST,
                    'o', OBSIDIAN
                  ));
            }
            if (Config.enableLeafBlower) {
                BookRecipeRegistry.addRecipe("leafBlower",
                  new ShapedOreRecipe(LEAF_BLOWER,
                    " p ",
                    "ptp",
                    " p ",
                    'p', PLATE_THIN_BRASS,
                    't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
                  ));
            }
            if (Config.enableTreeFeller) {
                BookRecipeRegistry.addRecipe("treeFeller",
                  new ShapedOreRecipe(TIMBER_CHAIN,
                    "fpf",
                    "p p",
                    "fpf",
                    'f', FLINT,
                    'p', PLATE_THIN_IRON
                  ));
            }
            if (Config.enableChainsaw) {
                BookRecipeRegistry.addRecipe("chainsaw",
                  new ShapedOreRecipe(CHAINSAW,
                    " s ",
                    "sps",
                    " t ",
                    's', IRON_SWORD,
                    'p', PLATE_THIN_BRASS,
                    't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
                  ));
            }
            if (Config.enableForestFire) {
                BookRecipeRegistry.addRecipe("forestFire",
                  new ShapedOreRecipe(FOREST_FIRE,
                    " b ",
                    "btb",
                    " b ",
                    'b', BLAZE_ROD,
                    't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
                  ));
            }
            if (Config.enableCultivator) {
                BookRecipeRegistry.addRecipe("cultivator",
                  new ShapedOreRecipe(CULTIVATOR,
                    "zsz",
                    " z ",
                    'z', PLATE_THIN_ZINC,
                    's', STICK_WOOD
                  ));
            }
            if (Config.enableRotaryBlades) {
                BookRecipeRegistry.addRecipe("rotaryBlades",
                  new ShapedOreRecipe(ROTARY_BLADES,
                    " f ",
                    "ftf",
                    " f ",
                    'f', FLINT,
                    't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
                  ));
            }
            if (Config.enableSifter) {
                BookRecipeRegistry.addRecipe("sifter",
                  new ShapedOreRecipe(SIFTER,
                    " p ",
                    "ctc",
                    " p ",
                    'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
                    'c', COBBLESTONE_ORE,
                    't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
                  ));
            }
            if (Config.enableBackhoe) {
                BookRecipeRegistry.addRecipe("backhoe",
                  new ShapedOreRecipe(BACKHOE,
                    "s",
                    "p",
                    "p",
                    's', BRASS_SHOVEL,
                    'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata())
                  ));
            }
            if (Config.enableTheVoid) {
                BookRecipeRegistry.addRecipe("theVoid", new ShapelessOreRecipe(THE_VOID, ENDER_CHEST, HOPPER));
            }
            if (Config.enableAutosmelting) {
                BookRecipeRegistry.addRecipe("autosmelting",
                  new ShapedOreRecipe(EXOTHERMIC_PROJECTOR,
                    " f ",
                    " h ",
                    "rpr",
                    'f', FAN,
                    'h', STEAM_HEATER,
                    'r', BLAZE_ROD,
                    'p', BLAZE_POWDER
                  ));
            }
            if (Config.enableOverclocker) {
                BookRecipeRegistry.addRecipe("overclocker",
                  new ShapedOreRecipe(OVERCLOCKER,
                    "r r",
                    "btb",
                    "r r",
                    'r', DUST_REDSTONE,
                    'b', INGOT_BRASS,
                    't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
                  ));
            }
        }

        addAxeRecipe(BRASS_AXE, INGOT_BRASS);
        addPickaxeRecipe(BRASS_PICKAXE, INGOT_BRASS);
        addShovelRecipe(BRASS_SHOVEL, INGOT_BRASS);
        addSwordRecipe(BRASS_SWORD, INGOT_BRASS);
        addHoeRecipe(BRASS_HOE, INGOT_BRASS);
        addAxeRecipe(GILDED_IRON_AXE, INGOT_GILDED_IRON);
        addPickaxeRecipe(GILDED_IRON_PICKAXE, INGOT_GILDED_IRON);
        addShovelRecipe(GILDED_IRON_SHOVEL, INGOT_GILDED_IRON);
        addSwordRecipe(GILDED_IRON_SWORD, INGOT_GILDED_IRON);
        addHoeRecipe(GILDED_IRON_HOE, INGOT_GILDED_IRON);
    }

    @Override
    public void finish(Side side) {
        RecipeSorter.register(Constants.EI_MODID + ":drill_head", DrillHeadRecipe.class, RecipeSorter.Category.SHAPED, "before:forge:shapedore");
        DrillHeadMaterial.registerDefaults();

        if (Config.enableWrench) {
            BookPageRegistry.addEntryToCategory(GADGET_CATEGORY, new BookEntry("research.Wrench.name",
              new BookPageItem("research.Wrench.name", "research.Wrench.0", new ItemStack(WRENCH)),
              new BookPageText("research.Wrench.name", "research.Wrench.1"),
              new BookPageCrafting("", "wrench1", "wrench2")));
        }
        if (Config.enableSurvivalist) {
            BookPageRegistry.addEntryToCategory(GADGET_CATEGORY, new BookEntry("research.Survivalist.name",
              new BookPageItem("research.Survivalist.name", String.format("research.Survivalist%s.0", CrossMod.BAUBLES ? "Baubles": ""), new ItemStack(SURVIVALIST_TOOLKIT)),
              new BookPageCrafting("", "survivalist")));
        }

        if (Config.enableSteamTools) {
            BookPageRegistry.addEntryToCategory(STEAMTOOL_CATEGORY, new BookEntry("research.SteamTools.name",
              new BookPageItem("research.SteamTools.name", "research.SteamTools.0",
                new ItemStack(STEAM_DRILL), new ItemStack(STEAM_SAW), new ItemStack(STEAM_SHOVEL)),
              new BookPageText("research.SteamTools.name", "research.SteamTools.1"),
              new BookPageCrafting("", "drill1", "drill2", "drill3", "drill4"),
              new BookPageCrafting("", "axe1", "axe2", "axe3", "axe4"),
              new BookPageCrafting("", "shovel1", "shovel2", "shovel3", "shovel4")));

            {
                BookCategory.Factory drillHeadFactory = new BookCategory.Factory("research.SteamDrillHead.name");

                // FIXME: Because of I18n, this can only exist in the client.
                if (side == Side.CLIENT) {
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
                      new BookPageItem("research.DrillHeads.name", "research.DrillHeads.0", new Object[]{drillMats.toString()}, true, new ItemStack(DRILL_HEAD)),
                      new BookPage("")));
                }

                if (Config.enableFortune) {
                    drillHeadFactory.append(new BookEntry("research.MultiplicativeResonator.name",
                      new BookPageItem("research.MultiplicativeResonator.name", "research.MultiplicativeResonator.0", true, new ItemStack(MULTIPLICATIVE_RESONATOR)),
                      new BookPageCrafting("", "multiplicativeResonator")));
                }

                if (Config.enableBigDrill) {
                    drillHeadFactory.append(new BookEntry("research.BigDrill.name",
                      new BookPageItem("research.BigDrill.name", "research.BigDrill.0", true, new ItemStack(BIG_DRILL)),
                      new BookPageCrafting("", "bigDrill")));
                }

                if (Config.enableBattleDrill) {
                    drillHeadFactory.append(new BookEntry("research.BattleDrill.name",
                      new BookPageItem("research.BattleDrill.name", "research.BattleDrill.0", true, new ItemStack(BATTLE_DRILL)),
                      new BookPageCrafting("", "battleDrill")));
                }

                if (Config.enablePreciseCuttingHead) {
                    drillHeadFactory.append(new BookEntry("research.PreciseCuttingHead.name",
                      new BookPageItem("research.PreciseCuttingHead.name", "research.PreciseCuttingHead.0", true, new ItemStack(PRECISE_CUTTING_HEAD)),
                      new BookPageCrafting("", "preciseCuttingHead")));
                }

                if (Config.enableStoneGrinder) {
                    drillHeadFactory.append(new BookEntry("research.StoneGrinder.name",
                      new BookPageItem("research.StoneGrinder.name", "research.StoneGrinder.0", true, new ItemStack(STONE_GRINDER)),
                      new BookPageCrafting("", "stoneGrinder")));
                }

                if (Config.enableThermalDrill) {
                    drillHeadFactory.append(new BookEntry("research.ThermalDrill.name",
                      new BookPageItem("research.ThermalDrill.name", "research.ThermalDrill.0", true, new ItemStack(THERMAL_DRILL)),
                      new BookPageCrafting("", "thermalDrill")));
                }

                if (Config.enableChargePlacer) {
                    drillHeadFactory.append(new BookEntry("research.CalamityInjector.name",
                      new BookPageItem("research.CalamityInjector.name", "research.CalamityInjector.0", true, new ItemStack(CHARGE_PLACER)),
                      new BookPageCrafting("", "chargePlacer")));
                }

                BookPageRegistry.addSubcategoryToCategory(STEAMTOOL_CATEGORY, drillHeadFactory.build());
            }
            {
                BookCategory.Factory drillCoreFactory = new BookCategory.Factory("research.SteamDrillCore.name");

                if (Config.enableInternalProcessingUnit) {
                    drillCoreFactory.append(new BookEntry("research.InternalProcessingUnit.name",
                      new BookPageItem("research.InternalProcessingUnit.name", "research.InternalProcessingUnit.0", true, new ItemStack(INTERNAL_PROCESSING_UNIT)),
                      new BookPageCrafting("", "internalProcessingUnit")));
                }

                BookPageRegistry.addSubcategoryToCategory(STEAMTOOL_CATEGORY, drillCoreFactory.build());
            }

            {
                BookCategory.Factory sawHeadFactory = new BookCategory.Factory("research.SteamSawHead.name");

                if (Config.enableForestFire) {
                    sawHeadFactory.append(new BookEntry("research.ForestFire.name",
                      new BookPageItem("research.ForestFire.name", "research.ForestFire.0", true, new ItemStack(FOREST_FIRE)),
                      new BookPageCrafting("", "forestFire")));
                }

                if (Config.enableTreeFeller) {
                    sawHeadFactory.append(new BookEntry("research.TimberChain.name",
                      new BookPageItem("research.TimberChain.name", "research.TimberChain.0", true, new ItemStack(TIMBER_CHAIN)),
                      new BookPageCrafting("", "treeFeller")));
                }

                if (Config.enableLeafBlower) {
                    sawHeadFactory.append(new BookEntry("research.LeafBlower.name",
                      new BookPageItem("research.LeafBlower.name", "research.LeafBlower.0", true, new ItemStack(LEAF_BLOWER)),
                      new BookPageCrafting("", "leafBlower")));
                }

                if (Config.enableChainsaw) {
                    sawHeadFactory.append(new BookEntry("research.Chainsaw.name",
                      new BookPageItem("research.Chainsaw.name", "research.Chainsaw.0", true, new ItemStack(CHAINSAW)),
                      new BookPageCrafting("", "chainsaw")));
                }

                BookPageRegistry.addSubcategoryToCategory(STEAMTOOL_CATEGORY, sawHeadFactory.build());
            }

            {
                BookCategory.Factory sawCoreFactory = new BookCategory.Factory("research.SteamSawCore.name");

                BookPageRegistry.addSubcategoryToCategory(STEAMTOOL_CATEGORY, sawCoreFactory.build());
            }

            {
                BookCategory.Factory shovelHeadFactory = new BookCategory.Factory("research.SteamShovelHead.name");

                if (Config.enableBackhoe) {
                    shovelHeadFactory.append(new BookEntry("research.Backhoe.name",
                      new BookPageItem("research.Backhoe.name", "research.Backhoe.0", true, new ItemStack(BACKHOE)),
                      new BookPageCrafting("", "backhoe")));
                }

                if (Config.enableCultivator) {
                    shovelHeadFactory.append(new BookEntry("research.Cultivator.name",
                      new BookPageItem("research.Cultivator.name", "research.Cultivator.0", true, new ItemStack(CULTIVATOR)),
                      new BookPageCrafting("", "cultivator")));
                }

                if (Config.enableRotaryBlades) {
                    shovelHeadFactory.append(new BookEntry("research.RotaryBlades.name",
                      new BookPageItem("research.RotaryBlades.name", "research.RotaryBlades.0", true, new ItemStack(ROTARY_BLADES)),
                      new BookPageCrafting("", "rotaryBlades")));
                }

                BookPageRegistry.addSubcategoryToCategory(STEAMTOOL_CATEGORY, shovelHeadFactory.build());
            }

            {
                BookCategory.Factory shovelCoreFactory = new BookCategory.Factory("research.SteamShovelCore.name");

                if (Config.enableSifter) {
                    shovelCoreFactory.append(new BookEntry("research.Sifter.name",
                      new BookPageItem("research.Sifter.name", "research.Sifter.0", true, new ItemStack(SIFTER)),
                      new BookPageCrafting("", "sifter")));
                }

                BookPageRegistry.addSubcategoryToCategory(STEAMTOOL_CATEGORY, shovelCoreFactory.build());
            }

            {
                BookCategory.Factory universalCoreFactory = new BookCategory.Factory("research.SteamUniversalCore.name");

                if (Config.enableOverclocker) {
                    universalCoreFactory.append(new BookEntry("research.Overclocker.name",
                      new BookPageItem("research.Overclocker.name", "research.Overclocker.0", true, new ItemStack(OVERCLOCKER)),
                      new BookPageCrafting("", "overclocker")));
                }

                if (Config.enableAutosmelting) {
                    universalCoreFactory.append(new BookEntry("research.ExothermicProjector.name",
                      new BookPageItem("research.ExothermicProjector.name", "research.ExothermicProjector.0", true, new ItemStack(EXOTHERMIC_PROJECTOR)),
                      new BookPageCrafting("", "autosmelting")));
                }

                if (Config.enableTheVoid) {
                    universalCoreFactory.append(new BookEntry("research.TheVoid.name",
                      new BookPageItem("research.TheVoid.name", "research.TheVoid.0", true, new ItemStack(THE_VOID)),
                      new BookPageCrafting("", "theVoid")));
                }

                BookPageRegistry.addSubcategoryToCategory(STEAMTOOL_CATEGORY, universalCoreFactory.build());
            }
        }
    }

    @Override
    public void preInitClient() {
        ModelLoaderRegistry.registerLoader(new SteamToolModelLoader());
        registerModel(SURVIVALIST_TOOLKIT);
        registerModel(WRENCH);
        registerModel(BRASS_SWORD);
        registerModel(BRASS_PICKAXE);
        registerModel(BRASS_AXE);
        registerModel(BRASS_SHOVEL);
        registerModel(BRASS_HOE);
        registerModel(GILDED_IRON_SWORD);
        registerModel(GILDED_IRON_PICKAXE);
        registerModel(GILDED_IRON_AXE);
        registerModel(GILDED_IRON_SHOVEL);
        registerModel(GILDED_IRON_HOE);
        registerModel(STEAM_DRILL);
        registerModel(STEAM_SAW);
        registerModel(STEAM_SHOVEL);
        registerModel(THE_VOID);
        registerModel(EXOTHERMIC_PROJECTOR);
        registerModel(OVERCLOCKER);
        registerModel(BIG_DRILL);
        registerModel(BATTLE_DRILL);
        registerModel(STONE_GRINDER);
        registerModel(PRECISE_CUTTING_HEAD);
        registerModel(THERMAL_DRILL);
        registerModel(MULTIPLICATIVE_RESONATOR);
        registerModel(CHARGE_PLACER);
        registerModel(DRILL_HEAD);
        registerModel(INTERNAL_PROCESSING_UNIT);
        registerModel(LEAF_BLOWER);
        registerModel(TIMBER_CHAIN);
        registerModel(CHAINSAW);
        registerModel(FOREST_FIRE);
        registerModel(CULTIVATOR);
        registerModel(ROTARY_BLADES);
        registerModel(SIFTER);
        registerModel(BACKHOE);
    }

    @Override
    public void initClient() {
        ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
        itemColors.registerItemColorHandler(new SteamDrillColorHandler(), STEAM_DRILL);
        itemColors.registerItemColorHandler(new SteamDrillHeadUpgradeColorHandler(), DRILL_HEAD);
    }

    private static void addAxeRecipe(Item out, String material) {
        addAxeRecipe(new ItemStack(out), material);
    }

    private static void addAxeRecipe(ItemStack out, String material) {
        GameRegistry.addRecipe(new ShapedOreRecipe(out,
          "xx",
          "xs",
          " s",
          'x', material,
          's', STICK_WOOD
        ));
    }

    private static void addPickaxeRecipe(Item out, String material) {
        addPickaxeRecipe(new ItemStack(out), material);
    }

    private static void addPickaxeRecipe(ItemStack out, String material) {
        GameRegistry.addRecipe(new ShapedOreRecipe(out,
          "xxx",
          " s ",
          " s ",
          'x', material,
          's', STICK_WOOD
        ));
    }

    private static void addShovelRecipe(Item out, String material) {
        addShovelRecipe(new ItemStack(out), material);
    }

    private static void addShovelRecipe(ItemStack out, String material) {
        GameRegistry.addRecipe(new ShapedOreRecipe(out,
          "x",
          "s",
          "s",
          'x', material,
          's', STICK_WOOD
        ));
    }

    private static void addSwordRecipe(Item out, String material) {
        addSwordRecipe(new ItemStack(out), material);
    }

    private static void addSwordRecipe(ItemStack out, String material) {
        GameRegistry.addRecipe(new ShapedOreRecipe(out,
          "x",
          "x",
          "s",
          'x', material,
          's', STICK_WOOD
        ));
    }

    private static void addHoeRecipe(Item out, String material) {
        addHoeRecipe(new ItemStack(out), material);
    }

    private static void addHoeRecipe(ItemStack out, String material) {
        GameRegistry.addRecipe(new ShapedOreRecipe(out,
            "xx",
          " s",
          " s",
          'x', material,
          's', STICK_WOOD
        ));
    }
}
