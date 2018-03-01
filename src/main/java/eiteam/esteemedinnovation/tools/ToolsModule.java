package eiteam.esteemedinnovation.tools;

import baubles.api.BaubleType;
import eiteam.esteemedinnovation.api.Constants;
import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.api.crucible.CrucibleRegistry;
import eiteam.esteemedinnovation.api.tool.SteamToolUpgrade;
import eiteam.esteemedinnovation.api.tool.ToolUpgradeRegistry;
import eiteam.esteemedinnovation.commons.init.ConfigurableModule;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import eiteam.esteemedinnovation.commons.util.RecipeUtility;
import eiteam.esteemedinnovation.commons.visual.GenericModelLoaderLocationMatch;
import eiteam.esteemedinnovation.metalcasting.mold.ItemMold;
import eiteam.esteemedinnovation.misc.ItemBauble;
import eiteam.esteemedinnovation.tools.standard.*;
import eiteam.esteemedinnovation.tools.steam.*;
import eiteam.esteemedinnovation.tools.steam.upgrades.*;
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
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.RecipeSorter;

import java.util.*;
import java.util.function.Supplier;

import static eiteam.esteemedinnovation.commons.Config.CATEGORY_ITEMS;
import static eiteam.esteemedinnovation.commons.Config.CATEGORY_STEAM_TOOL_UPGRADES;
import static eiteam.esteemedinnovation.commons.EsteemedInnovation.*;
import static eiteam.esteemedinnovation.commons.OreDictEntries.*;
import static eiteam.esteemedinnovation.heater.HeaterModule.STEAM_HEATER;
import static eiteam.esteemedinnovation.materials.MaterialsModule.BRASS_LIQUID;
import static eiteam.esteemedinnovation.materials.MaterialsModule.GOLD_LIQUID;
import static eiteam.esteemedinnovation.metalcasting.MetalcastingModule.MOLD_ITEM;
import static eiteam.esteemedinnovation.misc.ItemCraftingComponent.Types.BRASS_PISTON;
import static eiteam.esteemedinnovation.misc.ItemCraftingComponent.Types.BRASS_TURBINE;
import static eiteam.esteemedinnovation.misc.MiscellaneousModule.COMPONENT;
import static eiteam.esteemedinnovation.smasher.SmasherModule.ROCK_SMASHER;
import static eiteam.esteemedinnovation.transport.TransportationModule.BRASS_PIPE;
import static eiteam.esteemedinnovation.transport.TransportationModule.FAN;
import static eiteam.esteemedinnovation.transport.TransportationModule.VALVE_PIPE;
import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;

public class ToolsModule extends ContentModule implements ConfigurableModule {
    private static final int STEAM_TOOL_CONSUMPTION_DEFAULT = 800;
    private static final int BATTLE_DRILL_CONSUMPTION_DEFAULT = 20;
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
    public static Item CALAMITY_INJECTOR;
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
    public static List<String> blacklistedStoneGrinderNuggets;
    public static int backhoeRange;
    private static boolean removeVanillaMetalToolRecipes;
    private static boolean enableBackhoe;
    private static boolean enableSifter;
    private static boolean enableRotaryBlades;
    private static boolean enableCultivator;
    private static boolean enableForestFire;
    private static boolean enableChainsaw;
    private static boolean enableTreeFeller;
    private static boolean enableLeafBlower;
    public static int battleDrillConsumption;
    private static boolean enableChargePlacer;
    private static boolean enableThermalDrill;
    private static boolean enableInternalProcessingUnit;
    private static boolean enablePreciseCuttingHead;
    private static boolean enableStoneGrinder;
    private static boolean enableBattleDrill;
    private static boolean enableBigDrill;
    private static boolean enableFortune;
    private static boolean enableOverclocker;
    private static boolean enableAutosmelting;
    private static boolean enableTheVoid;
    private static boolean enableSurvivalist;
    private static boolean enableSteamTools;
    private static boolean enableWrench;
    public static int steamToolConsumptionShovel;
    public static int steamToolConsumptionAxe;
    public static int steamToolConsumptionDrill;

    @Override
    public void registerItems(RegistryEvent.Register<Item> event) {
        WRENCH = setup(event, new ItemWrench(), "wrench");
        SURVIVALIST_TOOLKIT = setup(event, new ItemBauble(BaubleType.BELT).setMaxStackSize(1), "survivalist");

        BRASS_SWORD = setup(event, new ItemGenericSword(BRASS_MAT, INGOT_BRASS), "brass_sword", tabTools);
        BRASS_PICKAXE = setup(event, new ItemGenericPickaxe(BRASS_MAT, INGOT_BRASS), "brass_pickaxe", tabTools);
        BRASS_AXE = setup(event, new ItemGenericAxe(BRASS_MAT, INGOT_BRASS), "brass_axe", tabTools);
        BRASS_SHOVEL = setup(event, new ItemGenericShovel(BRASS_MAT, INGOT_BRASS), "brass_shovel", tabTools);
        BRASS_HOE = setup(event, new ItemGenericHoe(BRASS_MAT, INGOT_BRASS), "brass_hoe", tabTools);

        GILDED_IRON_SWORD = setup(event, new ItemGenericSword(GILDED_IRON_MAT, INGOT_GILDED_IRON), "gilded_iron_sword", tabTools);
        GILDED_IRON_PICKAXE = setup(event, new ItemGenericPickaxe(GILDED_IRON_MAT, INGOT_GILDED_IRON), "gilded_iron_pickaxe", tabTools);
        GILDED_IRON_AXE = setup(event, new ItemGenericAxe(GILDED_IRON_MAT, INGOT_GILDED_IRON), "gilded_iron_axe", tabTools);
        GILDED_IRON_SHOVEL = setup(event, new ItemGenericShovel(GILDED_IRON_MAT, INGOT_GILDED_IRON), "gilded_iron_shovel", tabTools);
        GILDED_IRON_HOE = setup(event, new ItemGenericHoe(GILDED_IRON_MAT, INGOT_GILDED_IRON), "gilded_iron_hoe", tabTools);

        STEAM_DRILL = setup(event, new ItemSteamDrill(), "steam_drill");
        STEAM_SAW = setup(event, new ItemSteamAxe(), "steam_saw");
        STEAM_SHOVEL = setup(event, new ItemSteamShovel(), "steam_shovel");

        THE_VOID = setupUpgrade(event, new ItemTheVoidUpgrade(), "the_void");
        EXOTHERMIC_PROJECTOR = setupUpgrade(event, new ItemExothermicProjector(), "exothermic_projector");
        OVERCLOCKER = setupUpgrade(event, new ItemOverclockerUpgrade(), "overclocker");
        BIG_DRILL = setupUpgrade(event, new ItemBigDrillUpgrade(), "big_drill");
        BATTLE_DRILL = setupUpgrade(event, new ItemBattleDrillUpgrade(), "battle_drill");
        STONE_GRINDER = setupUpgrade(event, new ItemStoneGrinderUpgrade(), "stone_grinder");
        PRECISE_CUTTING_HEAD = setupUpgrade(event, new ItemPreciseCuttingHeadUpgrade(), "precise_cutting_head");
        THERMAL_DRILL = setupUpgrade(event, new ItemThermalDrillUpgrade(), "thermal_drill");
        MULTIPLICATIVE_RESONATOR = setupUpgrade(event, new ItemMultiplicativeResonatorUpgrade(), "multiplicative_resonator");
        CALAMITY_INJECTOR = setupUpgrade(event, new ItemCalamityInjectorUpgrade(), "charge_placer");
        DRILL_HEAD = setupUpgrade(event, new ItemDrillHeadUpgrade(), "drill_head");
        INTERNAL_PROCESSING_UNIT = setupUpgrade(event, new ItemInternalProcessingUnitUpgrade(), "internal_processing_unit");
        LEAF_BLOWER = setupUpgrade(event, new ItemLeafBlowerUpgrade(), "leaf_blower");
        TIMBER_CHAIN = setupUpgrade(event, new ItemTimberChainUpgrade(), "timber_chain");
        CHAINSAW = setupUpgrade(event, new ItemChainsawUpgrade(), "chainsaw");
        FOREST_FIRE = setupUpgrade(event, new ItemForestFireUpgrade(), "forest_fire");
        CULTIVATOR = setupUpgrade(event, new ItemCultivatorUpgrade(), "cultivator");
        ROTARY_BLADES = setupUpgrade(event, new ItemRotaryBladesUpgrade(), "rotary_blades");
        SIFTER = setupUpgrade(event, new ItemSifterUpgrade(), "sifter");
        BACKHOE = setupUpgrade(event, new ItemBackhoeUpgrade(), "backhoe");
    }

    private Item setupUpgrade(RegistryEvent.Register<Item> event, SteamToolUpgrade upgrade, String path) {
        upgrade = (SteamToolUpgrade) setup(event, (Item) upgrade, path);
        ToolUpgradeRegistry.register(upgrade);
        return (Item) upgrade;
    }

    public static String upgradeResource(String resource) {
        return Constants.EI_MODID + ":items/tool_upgrades/" + resource;
    }

    @Override
    public void recipes(RegistryEvent.Register<IRecipe> event) {
        RecipeSorter.register(Constants.EI_MODID + ":drill_head", DrillHeadRecipe.class, RecipeSorter.Category.SHAPED, "before:forge:shapedore");

        addRecipe(event, true, "wrench1", WRENCH,
          "  i",
          " bb",
          "b  ",
          'i', INGOT_IRON,
          'b', PLATE_THIN_BRASS
        );
        addRecipe(event, true, "wrench2", WRENCH,
          "  i",
          " bb",
          "b  ",
          'i', INGOT_IRON,
          'b', INGOT_BRASS
        );

        if (enableSurvivalist) {
            addRecipe(event, true, "survivalist", SURVIVALIST_TOOLKIT,
              "b s",
              "xwx",
              "xxx",
              'x', LEATHER_ORE,
              's', STRING_ORE,
              'b', BRICK,
              'w', STICK_WOOD
            );
        }

        if (enableSteamTools) {
            ItemStack drill = new ItemStack(STEAM_DRILL, 1, STEAM_DRILL.getMaxDamage() - 1);
            addRecipe(event, true, "drill1", drill,
              "xii",
              "pti",
              "xpx",
              'x', INGOT_BRASS,
              'i', INGOT_IRON,
              'p', BRASS_PIPE,
              't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
            );
            addRecipe(event, true, "drill2", drill,
              "xii",
              "pti",
              "xpx",
              'x', PLATE_THIN_BRASS,
              'i', INGOT_IRON,
              'p', BRASS_PIPE,
              't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
            );
            addRecipe(event, true, "drill3", drill,
              "xii",
              "pti",
              "xpx",
              'x', INGOT_BRASS,
              'i', PLATE_THIN_IRON,
              'p', BRASS_PIPE,
              't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
            );
            addRecipe(event, true, "drill4", drill,
              "xii",
              "pti",
              "xpx",
              'x', PLATE_THIN_BRASS,
              'i', PLATE_THIN_IRON,
              'p', BRASS_PIPE,
              't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
            );

            ItemStack saw = new ItemStack(STEAM_SAW, 1, STEAM_SAW.getMaxDamage() - 1);
            addRecipe(event, true, "axe1", saw,
              "ini",
              "ptn",
              "xpi",
              'x', INGOT_BRASS,
              'i', INGOT_IRON,
              'p', BRASS_PIPE,
              't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata()),
              'n', NUGGET_IRON
            );
            addRecipe(event, true, "axe2", saw,
              "ini",
              "ptn",
              "xpi",
              'x', PLATE_THIN_BRASS,
              'i', INGOT_IRON,
              'p', BRASS_PIPE,
              't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata()),
              'n', NUGGET_IRON
            );
            addRecipe(event, true, "axe3", saw,
              "ini",
              "ptn",
              "xpi",
              'x', INGOT_BRASS,
              'i', PLATE_THIN_IRON,
              'p', BRASS_PIPE,
              't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata()),
              'n', NUGGET_IRON
            );
            addRecipe(event, true, "axe4", saw,
              "ini",
              "ptn",
              "xpi",
              'x', PLATE_THIN_BRASS,
              'i', PLATE_THIN_IRON,
              'p', BRASS_PIPE,
              't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata()),
              'n', NUGGET_IRON
            );

            ItemStack shovel = new ItemStack(STEAM_SHOVEL, 1, STEAM_SHOVEL.getMaxDamage() - 1);
            addRecipe(event, true, "shovel1", shovel,
              "ixi",
              "ptx",
              "xpi",
              'x', INGOT_BRASS,
              'i', INGOT_IRON,
              'p', BRASS_PIPE,
              't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
            );
            addRecipe(event, true, "shovel2", shovel,
              "ixi",
              "ptx",
              "xpi",
              'x', PLATE_THIN_BRASS,
              'i', INGOT_IRON,
              'p', BRASS_PIPE,
              't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
            );
            addRecipe(event, true, "shovel3", shovel,
              "ixi",
              "ptx",
              "xpi",
              'x', INGOT_BRASS,
              'i', PLATE_THIN_IRON,
              'p', BRASS_PIPE,
              't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
            );
            addRecipe(event, true, "shovel4", shovel,
              "ixi",
              "ptx",
              "xpi",
              'x', PLATE_THIN_BRASS,
              'i', PLATE_THIN_IRON,
              'p', BRASS_PIPE,
              't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
            );

            for (Map.Entry<String, DrillHeadMaterial> entry : DrillHeadMaterial.materials.entrySet()) {
                String materialString = entry.getKey();
                DrillHeadMaterial headMat = entry.getValue();
                if (headMat.standard) {
                    addRecipe(event, false,"drillHead1", new DrillHeadRecipe(DRILL_HEAD,
                      " n ",
                      "iii",
                      "ppp",
                      'n', PREFIX_NUGGET + materialString,
                      'i', PREFIX_INGOT + materialString,
                      'p', PLATE_THIN_IRON)
                    );
                } else {
                    addRecipe(event, false,"drillHead2", new DrillHeadRecipe(DRILL_HEAD,
                      " g ",
                      "ggg",
                      "ppp",
                      'g', headMat.oreName,
                      'p', PLATE_THIN_IRON)
                    );
                }
            }

            if (enableBigDrill) {
                addRecipe(event, true, "bigDrill", BIG_DRILL,
                  " p ",
                  "pip",
                  "ibi",
                  'p', PLATE_THIN_IRON,
                  'i', INGOT_IRON,
                  'b', BLOCK_IRON
                );
            }
            if (enableBattleDrill) {
                addRecipe(event, true, "battleDrill",
                  BATTLE_DRILL,
                    " s ",
                    "sbs",
                    " p ",
                    's', IRON_SWORD,
                    'b', PLATE_THIN_BRASS,
                    'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata())
                  );
            }
            if (enableStoneGrinder) {
                addRecipe(event, true, "stoneGrinder",
                  STONE_GRINDER,
                    "i i",
                    "ctc",
                    "pcp",
                    'i', INGOT_IRON,
                    'c', COBBLESTONE_ORE,
                    'p', PLATE_THIN_IRON,
                    't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
                  );
            }
            if (enablePreciseCuttingHead) {
                addRecipe(event, true, "preciseCuttingHead",
                  PRECISE_CUTTING_HEAD,
                    "f f",
                    "pdp",
                    "p p",
                    'f', FLINT,
                    'p', PLATE_THIN_BRASS,
                    'd', GEM_DIAMOND
                  );
            }
            if (enableThermalDrill) {
                addRecipe(event, true, "thermalDrill",
                  THERMAL_DRILL,
                    " b ",
                    "bnb",
                    "iii",
                    'b', BLAZE_ROD,
                    'n', NETHER_BRICK,
                    'i', INGOT_BRASS
                  );
            }
            if (enableFortune) {
                ItemStack fortuneBook = new ItemStack(ENCHANTED_BOOK);
                fortuneBook.addEnchantment(Enchantments.FORTUNE, 3);
                addRecipe(event, true, "multiplicativeResonator",
                  MULTIPLICATIVE_RESONATOR,
                    "rgr",
                    "rbr",
                    "rgr",
                    'r', DUST_REDSTONE,
                    'g', PLATE_THIN_GILDED_IRON,
                    'b', fortuneBook
                  );
            }
            if (enableChargePlacer) {
                addRecipe(event, true, "chargePlacer",
                  CALAMITY_INJECTOR,
                    "g g",
                    "vbv",
                    "sps",
                    'g', GOLDEN_SWORD,
                    'v', VALVE_PIPE,
                    'b', STONE_BUTTON,
                    's', BRASS_PIPE,
                    'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata())
                  );
            }
            if (enableInternalProcessingUnit) {
                addRecipe(event, true, "internalProcessingUnit",
                  INTERNAL_PROCESSING_UNIT,
                    "sco",
                    's', ROCK_SMASHER,
                    'c', CHEST,
                    'o', OBSIDIAN
                  );
            }
            if (enableLeafBlower) {
                addRecipe(event, true, "leafBlower",
                  LEAF_BLOWER,
                    " p ",
                    "ptp",
                    " p ",
                    'p', PLATE_THIN_BRASS,
                    't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
                  );
            }
            if (enableTreeFeller) {
                addRecipe(event, true, "treeFeller",
                  TIMBER_CHAIN,
                    "fpf",
                    "p p",
                    "fpf",
                    'f', FLINT,
                    'p', PLATE_THIN_IRON
                  );
            }
            if (enableChainsaw) {
                addRecipe(event, true, "chainsaw",
                  CHAINSAW,
                    " s ",
                    "sps",
                    " t ",
                    's', IRON_SWORD,
                    'p', PLATE_THIN_BRASS,
                    't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
                  );
            }
            if (enableForestFire) {
                addRecipe(event, true, "forestFire",
                  FOREST_FIRE,
                    " b ",
                    "btb",
                    " b ",
                    'b', BLAZE_ROD,
                    't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
                  );
            }
            if (enableCultivator) {
                addRecipe(event, true, "cultivator",
                  CULTIVATOR,
                    "zsz",
                    " z ",
                    'z', PLATE_THIN_ZINC,
                    's', STICK_WOOD
                  );
            }
            if (enableRotaryBlades) {
                addRecipe(event, true, "rotaryBlades",
                  ROTARY_BLADES,
                    " f ",
                    "ftf",
                    " f ",
                    'f', FLINT,
                    't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
                  );
            }
            if (enableSifter) {
                addRecipe(event, true, "sifter",
                  SIFTER,
                    " p ",
                    "ctc",
                    " p ",
                    'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
                    'c', COBBLESTONE_ORE,
                    't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
                  );
            }
            if (enableBackhoe) {
                addRecipe(event, true, "backhoe",
                  BACKHOE,
                    "s",
                    "p",
                    "p",
                    's', BRASS_SHOVEL,
                    'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata())
                  );
            }
            if (enableTheVoid) {
                addShapelessRecipe(event, true, "theVoid", THE_VOID, ENDER_CHEST, HOPPER);
            }
            if (enableAutosmelting) {
                addRecipe(event, true, "autosmelting",
                  EXOTHERMIC_PROJECTOR,
                    " f ",
                    " h ",
                    "rpr",
                    'f', FAN,
                    'h', STEAM_HEATER,
                    'r', BLAZE_ROD,
                    'p', BLAZE_POWDER
                  );
            }
            if (enableOverclocker) {
                addRecipe(event, true, "overclocker",
                  OVERCLOCKER,
                    "r r",
                    "btb",
                    "r r",
                    'r', DUST_REDSTONE,
                    'b', INGOT_BRASS,
                    't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata())
                  );
            }

        }

        List<Item> castableTools = Arrays.asList(GOLDEN_AXE, GOLDEN_PICKAXE, GOLDEN_SHOVEL, GOLDEN_SWORD, GOLDEN_HOE);

        if (removeVanillaMetalToolRecipes) {
            castableTools.forEach(RecipeUtility::removeRecipeByOutput);
        }

        CrucibleRegistry.registerMoldingRecipe(GOLD_LIQUID, ItemMold.Type.AXE.createItemStack(MOLD_ITEM), new ItemStack(GOLDEN_AXE));
        CrucibleRegistry.registerMoldingRecipe(GOLD_LIQUID, ItemMold.Type.PICKAXE.createItemStack(MOLD_ITEM), new ItemStack(GOLDEN_PICKAXE));
        CrucibleRegistry.registerMoldingRecipe(GOLD_LIQUID, ItemMold.Type.SHOVEL.createItemStack(MOLD_ITEM), new ItemStack(GOLDEN_SHOVEL));
        CrucibleRegistry.registerMoldingRecipe(GOLD_LIQUID, ItemMold.Type.SWORD.createItemStack(MOLD_ITEM), new ItemStack(GOLDEN_SWORD));
        CrucibleRegistry.registerMoldingRecipe(GOLD_LIQUID, ItemMold.Type.HOE.createItemStack(MOLD_ITEM), new ItemStack(GOLDEN_HOE));
        CrucibleRegistry.registerMoldingRecipe(BRASS_LIQUID, ItemMold.Type.AXE.createItemStack(MOLD_ITEM), new ItemStack(BRASS_AXE));
        CrucibleRegistry.registerMoldingRecipe(BRASS_LIQUID, ItemMold.Type.PICKAXE.createItemStack(MOLD_ITEM), new ItemStack(BRASS_PICKAXE));
        CrucibleRegistry.registerMoldingRecipe(BRASS_LIQUID, ItemMold.Type.SHOVEL.createItemStack(MOLD_ITEM), new ItemStack(BRASS_SHOVEL));
        CrucibleRegistry.registerMoldingRecipe(BRASS_LIQUID, ItemMold.Type.SWORD.createItemStack(MOLD_ITEM), new ItemStack(BRASS_SWORD));
        CrucibleRegistry.registerMoldingRecipe(BRASS_LIQUID, ItemMold.Type.HOE.createItemStack(MOLD_ITEM), new ItemStack(BRASS_HOE));

        addAxeRecipe(event, GILDED_IRON_AXE, INGOT_GILDED_IRON);
        addPickaxeRecipe(event, GILDED_IRON_PICKAXE, INGOT_GILDED_IRON);
        addShovelRecipe(event, GILDED_IRON_SHOVEL, INGOT_GILDED_IRON);
        addSwordRecipe(event, GILDED_IRON_SWORD, INGOT_GILDED_IRON);
        addHoeRecipe(event, GILDED_IRON_HOE, INGOT_GILDED_IRON);
    }

    @Override
    public void finish(Side side) {
        DrillHeadMaterial.registerDefaults();
        if (enableWrench) {
            BookPageRegistry.addCategoryToSection(GADGET_SECTION, 0,
              new BookCategory("category.Wrench.name",
                new BookEntry("research.Wrench.name",
                  new BookPageItem("research.Wrench.name", "research.Wrench.0", new ItemStack(WRENCH)),
                  new BookPageText("research.Wrench.name", "research.Wrench.1"),
                  new BookPageCrafting("", "wrench1", "wrench2"))));
        }
        if (enableSurvivalist) {
            BookPageRegistry.addCategoryToSection(GADGET_SECTION, 2,
              new BookCategory("category.Survivalist.name",
                new BookEntry("research.Survivalist.name",
                  new BookPageItem("research.Survivalist.name", "research.Survivalist.0", new ItemStack(SURVIVALIST_TOOLKIT)),
                  new BookPageCrafting("", "survivalist"))));
        }

        if (enableSteamTools) {
            BookPageRegistry.addCategoryToSection(STEAMTOOL_SECTION,
              new BookCategory("category.SteamTools.name",
                new BookEntry("research.SteamTools.name",
                  new BookPageItem("research.SteamTools.name", "research.SteamTools.0",
                    new ItemStack(STEAM_DRILL),
                    new ItemStack(STEAM_SAW),
                    new ItemStack(STEAM_SHOVEL)),
                  new BookPageText("research.SteamTools.name", "research.SteamTools.1"),
                  new BookPageCrafting("", "drill1", "drill2", "drill3", "drill4"),
                  new BookPageCrafting("", "axe1", "axe2", "axe3", "axe4"),
                  new BookPageCrafting("", "shovel1", "shovel2", "shovel3", "shovel4"))));

            {
                BookCategory drillHeadCategory = new BookCategory("category.SteamDrillHead.name");

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

                    drillHeadCategory.appendEntries(new BookEntry("research.SteamDrillHead.name",
                      new BookPageItem("research.DrillHeads.name", "research.DrillHeads.0", new Object[]{drillMats.toString()}, true, new ItemStack(DRILL_HEAD)),
                      new BookPage("")));
                }

                if (enableFortune) {
                    drillHeadCategory.appendEntries(new BookEntry("research.MultiplicativeResonator.name",
                      new BookPageItem("research.MultiplicativeResonator.name", "research.MultiplicativeResonator.0", true, new ItemStack(MULTIPLICATIVE_RESONATOR)),
                      new BookPageCrafting("", "multiplicativeResonator")));
                }

                if (enableBigDrill) {
                    drillHeadCategory.appendEntries(new BookEntry("research.BigDrill.name",
                      new BookPageItem("research.BigDrill.name", "research.BigDrill.0", true, new ItemStack(BIG_DRILL)),
                      new BookPageCrafting("", "bigDrill")));
                }

                if (enableBattleDrill) {
                    drillHeadCategory.appendEntries(new BookEntry("research.BattleDrill.name",
                      new BookPageItem("research.BattleDrill.name", "research.BattleDrill.0", true, new ItemStack(BATTLE_DRILL)),
                      new BookPageCrafting("", "battleDrill")));
                }

                if (enablePreciseCuttingHead) {
                    drillHeadCategory.appendEntries(new BookEntry("research.PreciseCuttingHead.name",
                      new BookPageItem("research.PreciseCuttingHead.name", "research.PreciseCuttingHead.0", true, new ItemStack(PRECISE_CUTTING_HEAD)),
                      new BookPageCrafting("", "preciseCuttingHead")));
                }

                if (enableStoneGrinder) {
                    drillHeadCategory.appendEntries(new BookEntry("research.StoneGrinder.name",
                      new BookPageItem("research.StoneGrinder.name", "research.StoneGrinder.0", true, new ItemStack(STONE_GRINDER)),
                      new BookPageCrafting("", "stoneGrinder")));
                }

                if (enableThermalDrill) {
                    drillHeadCategory.appendEntries(new BookEntry("research.ThermalDrill.name",
                      new BookPageItem("research.ThermalDrill.name", "research.ThermalDrill.0", true, new ItemStack(THERMAL_DRILL)),
                      new BookPageCrafting("", "thermalDrill")));
                }

                if (enableChargePlacer) {
                    drillHeadCategory.appendEntries(new BookEntry("research.CalamityInjector.name",
                      new BookPageItem("research.CalamityInjector.name", "research.CalamityInjector.0", true, new ItemStack(CALAMITY_INJECTOR)),
                      new BookPageCrafting("", "chargePlacer")));
                }

                BookPageRegistry.addCategoryToSection(STEAMTOOL_SECTION, drillHeadCategory);
            }
            {
                BookCategory drillCoreCategory = new BookCategory("category.SteamDrillCore.name");

                if (enableInternalProcessingUnit) {
                    drillCoreCategory.appendEntries(new BookEntry("research.InternalProcessingUnit.name",
                      new BookPageItem("research.InternalProcessingUnit.name", "research.InternalProcessingUnit.0", true, new ItemStack(INTERNAL_PROCESSING_UNIT)),
                      new BookPageCrafting("", "internalProcessingUnit")));
                }

                BookPageRegistry.addCategoryToSection(STEAMTOOL_SECTION, drillCoreCategory);
            }

            {
                BookCategory sawHeadCategory = new BookCategory("category.SteamSawHead.name");

                if (enableForestFire) {
                    sawHeadCategory.appendEntries(new BookEntry("research.ForestFire.name",
                      new BookPageItem("research.ForestFire.name", "research.ForestFire.0", true, new ItemStack(FOREST_FIRE)),
                      new BookPageCrafting("", "forestFire")));
                }

                if (enableTreeFeller) {
                    sawHeadCategory.appendEntries(new BookEntry("research.TimberChain.name",
                      new BookPageItem("research.TimberChain.name", "research.TimberChain.0", true, new ItemStack(TIMBER_CHAIN)),
                      new BookPageCrafting("", "treeFeller")));
                }

                if (enableLeafBlower) {
                    sawHeadCategory.appendEntries(new BookEntry("research.LeafBlower.name",
                      new BookPageItem("research.LeafBlower.name", "research.LeafBlower.0", true, new ItemStack(LEAF_BLOWER)),
                      new BookPageCrafting("", "leafBlower")));
                }

                if (enableChainsaw) {
                    sawHeadCategory.appendEntries(new BookEntry("research.Chainsaw.name",
                      new BookPageItem("research.Chainsaw.name", "research.Chainsaw.0", true, new ItemStack(CHAINSAW)),
                      new BookPageCrafting("", "chainsaw")));
                }

                BookPageRegistry.addCategoryToSection(STEAMTOOL_SECTION, sawHeadCategory);
            }

            {
                BookCategory sawCoreCategory = new BookCategory("category.SteamSawCore.name");

                BookPageRegistry.addCategoryToSection(STEAMTOOL_SECTION, sawCoreCategory);
            }

            {
                BookCategory shovelHeadCategory = new BookCategory("category.SteamShovelHead.name");

                if (enableBackhoe) {
                    shovelHeadCategory.appendEntries(new BookEntry("research.Backhoe.name",
                      new BookPageItem("research.Backhoe.name", "research.Backhoe.0", true, new ItemStack(BACKHOE)),
                      new BookPageCrafting("", "backhoe")));
                }

                if (enableCultivator) {
                    shovelHeadCategory.appendEntries(new BookEntry("research.Cultivator.name",
                      new BookPageItem("research.Cultivator.name", "research.Cultivator.0", true, new ItemStack(CULTIVATOR)),
                      new BookPageCrafting("", "cultivator")));
                }

                if (enableRotaryBlades) {
                    shovelHeadCategory.appendEntries(new BookEntry("research.RotaryBlades.name",
                      new BookPageItem("research.RotaryBlades.name", "research.RotaryBlades.0", true, new ItemStack(ROTARY_BLADES)),
                      new BookPageCrafting("", "rotaryBlades")));
                }

                BookPageRegistry.addCategoryToSection(STEAMTOOL_SECTION, shovelHeadCategory);
            }

            {
                BookCategory shovelCoreCategory = new BookCategory("category.SteamShovelCore.name");

                if (enableSifter) {
                    shovelCoreCategory.appendEntries(new BookEntry("research.Sifter.name",
                      new BookPageItem("research.Sifter.name", "research.Sifter.0", true, new ItemStack(SIFTER)),
                      new BookPageCrafting("", "sifter")));
                }

                BookPageRegistry.addCategoryToSection(STEAMTOOL_SECTION, shovelCoreCategory);
            }

            {
                BookCategory universalCoreCategory = new BookCategory("category.SteamUniversalCore.name");

                if (enableOverclocker) {
                    universalCoreCategory.appendEntries(new BookEntry("research.Overclocker.name",
                      new BookPageItem("research.Overclocker.name", "research.Overclocker.0", true, new ItemStack(OVERCLOCKER)),
                      new BookPageCrafting("", "overclocker")));
                }

                if (enableAutosmelting) {
                    universalCoreCategory.appendEntries(new BookEntry("research.ExothermicProjector.name",
                      new BookPageItem("research.ExothermicProjector.name", "research.ExothermicProjector.0", true, new ItemStack(EXOTHERMIC_PROJECTOR)),
                      new BookPageCrafting("", "autosmelting")));
                }

                if (enableTheVoid) {
                    universalCoreCategory.appendEntries(new BookEntry("research.TheVoid.name",
                      new BookPageItem("research.TheVoid.name", "research.TheVoid.0", true, new ItemStack(THE_VOID)),
                      new BookPageCrafting("", "theVoid")));
                }

                BookPageRegistry.addCategoryToSection(STEAMTOOL_SECTION, universalCoreCategory);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(ModelRegistryEvent event) {
        ModelLoaderRegistry.registerLoader(new GenericModelLoaderLocationMatch(SteamToolModel.GENERIC_MODEL, new ResourceLocation(MOD_ID, "models/block/steam_tool")));
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
        registerModel(CALAMITY_INJECTOR);
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

    @SideOnly(Side.CLIENT)
    @Override
    public void postInitClient() {
        ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
        itemColors.registerItemColorHandler(new SteamDrillColorHandler(), STEAM_DRILL);
        itemColors.registerItemColorHandler(new SteamDrillHeadUpgradeColorHandler(), DRILL_HEAD);
    }

    private static final Map<String, Supplier<Boolean>> RECIPE_CHECKERS = new HashMap<String, Supplier<Boolean>>() {{
        put("enableSurvivalist", () -> enableSurvivalist);
        put("enableSteamTools", () -> enableSteamTools);
        put("enableBigDrill", () -> enableSteamTools && enableBigDrill);
        put("enableBattleDrill", () -> enableSteamTools && enableBattleDrill);
        put("enableStoneGrinder", () -> enableSteamTools && enableStoneGrinder);
        put("enablePreciseCuttingHead", () -> enableSteamTools && enablePreciseCuttingHead);
        put("enableThermalDrill", () -> enableSteamTools && enableThermalDrill);
        put("enableFortune", () -> enableSteamTools && enableFortune);
        put("enableChargePlacer", () -> enableSteamTools && enableChargePlacer);
        put("enableInternalProcessingUnit", () -> enableSteamTools && enableInternalProcessingUnit);
        put("enableLeafBlower", () -> enableSteamTools && enableLeafBlower);
        put("enableTreeFeller", () -> enableSteamTools && enableTreeFeller);
        put("enableChainsaw", () -> enableSteamTools && enableChainsaw);
        put("enableForestFire", () -> enableSteamTools && enableForestFire);
        put("enableCultivator", () -> enableSteamTools && enableCultivator);
        put("enableRotaryBlades", () -> enableSteamTools && enableRotaryBlades);
        put("enableSifter", () -> enableSteamTools && enableSifter);
        put("enableBackhoe", () -> enableSteamTools && enableBackhoe);
        put("enableTheVoid", () -> enableSteamTools && enableTheVoid);
        put("enableAutosmelting", () -> enableSteamTools && enableAutosmelting);
        put("enableOverclocker", () -> enableSteamTools && enableOverclocker);
    }};

    @Override
    public void loadConfigurationOptions(Configuration config) {
        enableSteamTools = config.get(CATEGORY_ITEMS, "Enable steam tools", true).getBoolean();
        enableSurvivalist = config.get(CATEGORY_ITEMS, "Enable Survivalist's Toolkit", true).getBoolean();
        enableWrench = config.get(CATEGORY_ITEMS, "Enable Pipe Wrench", true).getBoolean();
        // STEAM TOOL UPGRADES
        enableBigDrill = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Drill's Hammer Head upgrade", true).getBoolean();
        enableLeafBlower = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Axe's Leaf Blower upgrade", true).getBoolean();
        enableCultivator = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Shovel's Cultivator upgrade", true).getBoolean();
        enableRotaryBlades = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Shovel's Rotary Blades upgrade", true).getBoolean();
        enableBattleDrill = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable BattleDrill", true).getBoolean();
        enableSifter = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Shovel's Sifter upgrade", true).getBoolean();
        enableStoneGrinder = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Drill's Stone Grinder upgrade", true).getBoolean();
        enableBackhoe = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Shovel's Backhoe upgrade", true).getBoolean();
        enableTheVoid = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Tool core upgrade the Void", true).getBoolean();
        enableAutosmelting = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Tool core upgrade autosmelting", true).getBoolean();
        enableOverclocker = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Tool core upgrade overclocker", true).getBoolean();
        enablePreciseCuttingHead = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Drill's Precise Cutting Head", true).getBoolean();
        enableInternalProcessingUnit = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Drill's Internal Processing Unit", true).getBoolean();
        enableTreeFeller = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Axe's Tree Felling upgrade", true).getBoolean();
        enableChainsaw = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Axe's Chainsaw upgrade", true).getBoolean();
        enableFortune = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Drill's fortune upgrade", true).getBoolean();
        enableForestFire = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Axe's Forest Fire upgrade", true).getBoolean();
        enableThermalDrill = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Drill's Thermal Drill upgrade", true).getBoolean();
        enableChargePlacer = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Drill's Charge Placer upgrade", true).getBoolean();

        steamToolConsumptionAxe = config.get(CATEGORY_ITEMS, "The consumption rate of the Steam Axe", STEAM_TOOL_CONSUMPTION_DEFAULT).getInt();
        steamToolConsumptionDrill = config.get(CATEGORY_ITEMS, "The consumption rate of the Steam Drill", STEAM_TOOL_CONSUMPTION_DEFAULT).getInt();
        steamToolConsumptionShovel = config.get(CATEGORY_ITEMS, "The consumption rate of the Steam Shovel", STEAM_TOOL_CONSUMPTION_DEFAULT).getInt();

        backhoeRange = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "The range (in each direction) that the Backhoe upgrade effects", 16).getInt();
        battleDrillConsumption = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Steam consumption for the " +
            "BattleDrill. This is not the actual amount of steam, but the relative item damage.",
          BATTLE_DRILL_CONSUMPTION_DEFAULT).getInt();
        blacklistedStoneGrinderNuggets = Arrays.asList(config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Nuggets that the Stone Grinder cannot produce. These are OreDict entries", new String[]{}).getStringList());

        removeVanillaMetalToolRecipes = config.get(CATEGORY_ITEMS, "Remove Vanilla-style tool recipes for castable tools", true).getBoolean();
    }

    @Override
    public boolean doesRecipeBelongTo(String configSetting) {
        return RECIPE_CHECKERS.containsKey(configSetting);
    }

    @Override
    public boolean isRecipeEnabled(String configSetting) {
        return RECIPE_CHECKERS.get(configSetting).get();
    }

    private static void addAxeRecipe(RegistryEvent.Register<IRecipe> event, Item out, String material) {
        addAxeRecipe(event, new ItemStack(out), material);
    }

    private static void addAxeRecipe(RegistryEvent.Register<IRecipe> event, ItemStack out, String material) {
        addRecipe(event, false, material + "Axe", out,
          "xx",
          "xs",
          " s",
          'x', material,
          's', STICK_WOOD
        );
    }

    private static void addPickaxeRecipe(RegistryEvent.Register<IRecipe> event, Item out, String material) {
        addPickaxeRecipe(event, new ItemStack(out), material);
    }

    private static void addPickaxeRecipe(RegistryEvent.Register<IRecipe> event, ItemStack out, String material) {
        addRecipe(event, false, material + "Pickaxe", out,
          "xxx",
          " s ",
          " s ",
          'x', material,
          's', STICK_WOOD
        );
    }

    private static void addShovelRecipe(RegistryEvent.Register<IRecipe> event, Item out, String material) {
        addShovelRecipe(event, new ItemStack(out), material);
    }

    private static void addShovelRecipe(RegistryEvent.Register<IRecipe> event, ItemStack out, String material) {
        addRecipe(event, false, material + "Shovel", out,
          "x",
          "s",
          "s",
          'x', material,
          's', STICK_WOOD
        );
    }

    private static void addSwordRecipe(RegistryEvent.Register<IRecipe> event, Item out, String material) {
        addSwordRecipe(event, new ItemStack(out), material);
    }

    private static void addSwordRecipe(RegistryEvent.Register<IRecipe> event, ItemStack out, String material) {
        addRecipe(event, false, material + "Sword", out,
          "x",
          "x",
          "s",
          'x', material,
          's', STICK_WOOD
        );
    }

    private static void addHoeRecipe(RegistryEvent.Register<IRecipe> event, Item out, String material) {
        addHoeRecipe(event, new ItemStack(out), material);
    }

    private static void addHoeRecipe(RegistryEvent.Register<IRecipe> event, ItemStack out, String material) {
        addRecipe(event, false, material + "Hoe", out,
          "xx",
          " s",
          " s",
          'x', material,
          's', STICK_WOOD
        );
    }

}
