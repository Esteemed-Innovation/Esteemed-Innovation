package flaxbeard.steamcraft.init.items.tools;

import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.DrillHeadRecipe;
import flaxbeard.steamcraft.api.book.BookRecipeRegistry;
import flaxbeard.steamcraft.api.tool.SteamToolSlot;
import flaxbeard.steamcraft.init.blocks.SteamMachineryBlocks;
import flaxbeard.steamcraft.init.blocks.SteamNetworkBlocks;
import flaxbeard.steamcraft.init.items.CraftingComponentItems;
import flaxbeard.steamcraft.init.IInitCategory;
import flaxbeard.steamcraft.item.tool.steam.ItemDrillHeadUpgrade;
import flaxbeard.steamcraft.item.tool.steam.ItemSteamToolUpgrade;
import flaxbeard.steamcraft.item.tool.steam.ItemTheVoidUpgrade;
import flaxbeard.steamcraft.misc.DrillHeadMaterial;

import java.util.Map;

import static net.minecraft.init.Items.*;
import static net.minecraft.init.Blocks.*;
import static flaxbeard.steamcraft.init.misc.OreDictEntries.*;

public class ToolUpgradeItems implements IInitCategory {
    public enum Items {
        THE_VOID(new ItemTheVoidUpgrade(), "theVoid"),
        EXOTHERMIC_PROJECTOR(SteamToolSlot.TOOL_CORE, "furnace", null, 0, "autosmelting"),
        OVERCLOCKER(SteamToolSlot.TOOL_CORE, "overclocker", null, 0, "overclocker"),
        BIG_DRILL(SteamToolSlot.DRILL_HEAD, "drillBig", null, 1, "bigDrill"),
        BATTLE_DRILL(SteamToolSlot.DRILL_HEAD, "combatdrill", null, 1, "battleDrill"),
        STONE_GRINDER(SteamToolSlot.DRILL_HEAD, "grinder", null, 1, "stoneGrinder"),
        PRECISE_CUTTING_HEAD(SteamToolSlot.DRILL_HEAD, "preciseCuttingHead", null, 1, "preciseCuttingHead"),
        THERMAL_DRILL(SteamToolSlot.DRILL_HEAD, "thermalDrill", null, 1, "thermalDrill"),
        MULTIPLICATIVE_RESONATOR(SteamToolSlot.DRILL_HEAD, "fortunedrill", null, 1, "drillFortune"),
        CHARGE_PLACER(SteamToolSlot.DRILL_HEAD, "chargePlacer", null, 1, "chargePlacer"),
        DRILL_HEAD(new ItemDrillHeadUpgrade(), "drillHead"),
        INTERNAL_PROCESSING_UNIT(SteamToolSlot.DRILL_CORE, "drillProcessor", null, 0, "internalProcessor"),
        LEAF_BLOWER(SteamToolSlot.SAW_HEAD, "axeBlower", null, 1, "leafBlower"),
        TIMBER_CHAIN(SteamToolSlot.SAW_HEAD, "timberHead", null, 1, "feller"),
        CHAINSAW(SteamToolSlot.SAW_HEAD, "chainAxe", null, 1, "chainsaw"),
        FOREST_FIRE(SteamToolSlot.SAW_HEAD, "fireAxe", null, 1, "forestFire"),
        CULTIVATOR(SteamToolSlot.SHOVEL_HEAD, "cultivatorHead", null, 1, "cultivator"),
        ROTARY_BLADES(SteamToolSlot.SHOVEL_HEAD, "rotaryShovel", null, 1, "rotaryBlades"),
        SIFTER(SteamToolSlot.SHOVEL_CORE, "sifterShovel", null, 0, "sifter"),
        BACKHOE(SteamToolSlot.SHOVEL_HEAD, "backhoeShovel", null, 1, "backhoe");

        private Item item;

        public static Items[] LOOKUP = new Items[values().length];

        static {
            for (Items item : values()) {
                if (item.isEnabled()) {
                    LOOKUP[item.ordinal()] = item;
                }
            }
        }

        Items(Item item, String name) {
            if (isEnabled()) {
                item.setUnlocalizedName(Steamcraft.MOD_ID + ":" + name);
                item.setCreativeTab(Steamcraft.tabTools);
                item.setRegistryName(Steamcraft.MOD_ID, name);
                GameRegistry.register(item);
            }
            this.item = item;
        }

        Items(SteamToolSlot slot, String resourceDir, String info, int prio, String name) {
            this(new ItemSteamToolUpgrade(slot, Steamcraft.MOD_ID + ":toolUpgrades/" + resourceDir, info, prio), name);
        }

        public boolean isEnabled() {
            boolean drillEnabled = ToolItems.Items.STEAM_DRILL.isEnabled();
            boolean sawEnabled = ToolItems.Items.STEAM_SAW.isEnabled();
            boolean shovelEnabled = ToolItems.Items.STEAM_SHOVEL.isEnabled();
            if (drillEnabled) {
                switch (this) {
                    case BIG_DRILL: {
                        return Config.enableBigDrill;
                    }
                    case BATTLE_DRILL: {
                        return Config.enableBattleDrill;
                    }
                    case STONE_GRINDER: {
                        return Config.enableStoneGrinder;
                    }
                    case PRECISE_CUTTING_HEAD: {
                        return Config.enablePreciseCuttingHead;
                    }
                    case THERMAL_DRILL: {
                        return Config.enableThermalDrill;
                    }
                    case MULTIPLICATIVE_RESONATOR: {
                        return Config.enableFortune;
                    }
                    case CHARGE_PLACER: {
                        return Config.enableChargePlacer;
                    }
                    case DRILL_HEAD: {
                        return true;
                    }
                    case INTERNAL_PROCESSING_UNIT: {
                        return Config.enableInternalProcessingUnit;
                    }
                    default: {}
                }
            }

            if (sawEnabled) {
                switch (this) {
                    case LEAF_BLOWER: {
                        return Config.enableLeafBlower;
                    }
                    case TIMBER_CHAIN: {
                        return Config.enableTreeFeller;
                    }
                    case CHAINSAW: {
                        return Config.enableChainsaw;
                    }
                    case FOREST_FIRE: {
                        return Config.enableForestFire;
                    }
                    default: {}
                }
            }

            if (shovelEnabled) {
                switch (this) {
                    case CULTIVATOR: {
                        return Config.enableCultivator;
                    }
                    case ROTARY_BLADES: {
                        return Config.enableRotaryBlades;
                    }
                    case SIFTER: {
                        return Config.enableSifter;
                    }
                    case BACKHOE: {
                        return Config.enableBackhoe;
                    }
                    default: {}
                }
            }

            if (drillEnabled || sawEnabled || shovelEnabled) {
                switch (this) {
                    case THE_VOID: {
                        return Config.enableTheVoid;
                    }
                    case EXOTHERMIC_PROJECTOR: {
                        return Config.enableAutosmelting;
                    }
                    case OVERCLOCKER: {
                        return Config.enableOverclocker;
                    }
                    default: {}
                }
            }

            return false;
        }

        public Item getItem() {
            return item;
        }
    }

    @Override
    public void oreDict() {}

    /* For this item category, we have to do all recipe adding in PostInitialization. */

    @Override
    public void recipes() {}

    public void postInit() {
        for (Items item : Items.LOOKUP) {
            switch (item) {
                case BACKHOE: {
                    BookRecipeRegistry.addRecipe("backhoe",
                      new ShapedOreRecipe(item.getItem(),
                        "s",
                        "p",
                        "p",
                        's', ToolItems.Items.BRASS_SHOVEL.getItem(),
                        'p', CraftingComponentItems.Items.BRASS_PISTON.createItemStack()
                      ));
                    break;
                }
                case BATTLE_DRILL: {
                    BookRecipeRegistry.addRecipe("battleDrill",
                      new ShapedOreRecipe(item.getItem(),
                        " s ",
                        "sbs",
                        " p ",
                        's', IRON_SWORD,
                        'b', PLATE_BRASS,
                        'p', CraftingComponentItems.Items.BRASS_PISTON.createItemStack()
                      ));
                    break;
                }
                case BIG_DRILL: {
                    BookRecipeRegistry.addRecipe("bigDrill", new ShapedOreRecipe(item.getItem(),
                      " p ",
                      "pip",
                      "ibi",
                      'p', PLATE_IRON,
                      'i', INGOT_IRON,
                      'b', BLOCK_IRON
                    ));
                    break;
                }
                case CHAINSAW: {
                    BookRecipeRegistry.addRecipe("chainsaw",
                      new ShapedOreRecipe(item.getItem(),
                        " s ",
                        "sps",
                        " t ",
                        's', IRON_SWORD,
                        'p', PLATE_BRASS,
                        't', CraftingComponentItems.Items.BRASS_TURBINE.createItemStack()
                      ));
                    break;
                }
                case CHARGE_PLACER: {
                    BookRecipeRegistry.addRecipe("chargePlacer",
                      new ShapedOreRecipe(item.getItem(),
                        "g g",
                        "vbv",
                        "sps",
                        'g', GOLDEN_SWORD,
                        'v', SteamNetworkBlocks.Blocks.VALVE_PIPE.getBlock(),
                        'b', STONE_BUTTON,
                        's', SteamNetworkBlocks.Blocks.PIPE.getBlock(),
                        'p', CraftingComponentItems.Items.BRASS_PISTON.createItemStack()
                      ));
                    break;
                }
                case CULTIVATOR: {
                    BookRecipeRegistry.addRecipe("cultivator",
                      new ShapedOreRecipe(item.getItem(),
                        "zsz",
                        " z ",
                        'z', PLATE_ZINC,
                        's', STICK_WOOD
                      ));
                    break;
                }
                case DRILL_HEAD: {
                    for (Map.Entry<String, DrillHeadMaterial> entry : DrillHeadMaterial.materials.entrySet()) {
                        String materialString = entry.getKey();
                        DrillHeadMaterial headMat = entry.getValue();
                        if (headMat.standard) {
                            GameRegistry.addRecipe(new DrillHeadRecipe(item.getItem(),
                              " n ",
                              "iii",
                              "ppp",
                              'n', PREFIX_NUGGET + materialString,
                              'i', PREFIX_INGOT + materialString,
                              'p', PLATE_IRON)
                            );
                        } else {
                            GameRegistry.addRecipe(new DrillHeadRecipe(item.getItem(),
                              " g ",
                              "ggg",
                              "ppp",
                              'g', headMat.oreName,
                              'p', PLATE_IRON)
                            );
                        }
                    }
                    break;
                }
                case EXOTHERMIC_PROJECTOR: {
                    BookRecipeRegistry.addRecipe("autosmelting",
                      new ShapedOreRecipe(item.getItem(),
                        " f ",
                        " h ",
                        "rpr",
                        'f', SteamMachineryBlocks.Blocks.FAN.getBlock(),
                        'h', SteamMachineryBlocks.Blocks.STEAM_HEATER.getBlock(),
                        'r', BLAZE_ROD,
                        'p', BLAZE_POWDER
                      ));
                    break;
                }
                case FOREST_FIRE: {
                    BookRecipeRegistry.addRecipe("forestFire",
                      new ShapedOreRecipe(item.getItem(),
                        " b ",
                        "btb",
                        " b ",
                        'b', BLAZE_ROD,
                        't', CraftingComponentItems.Items.BRASS_TURBINE
                      ));
                    break;
                }
                case INTERNAL_PROCESSING_UNIT: {
                    BookRecipeRegistry.addRecipe("internalProcessingUnit",
                      new ShapedOreRecipe(item.getItem(),
                        "sco",
                        's', SteamMachineryBlocks.Blocks.ROCK_SMASHER.getBlock(),
                        'c', CHEST,
                        'o', OBSIDIAN
                      ));
                    break;
                }
                case LEAF_BLOWER: {
                    BookRecipeRegistry.addRecipe("leafBlower",
                      new ShapedOreRecipe(item.getItem(),
                        " p ",
                        "ptp",
                        " p ",
                        'p', PLATE_BRASS,
                        't', CraftingComponentItems.Items.BRASS_TURBINE.createItemStack()
                      ));
                    break;
                }
                case MULTIPLICATIVE_RESONATOR: {
                    ItemStack fortuneBook = new ItemStack(ENCHANTED_BOOK);
                    fortuneBook.addEnchantment(Enchantments.FORTUNE, 3);
                    BookRecipeRegistry.addRecipe("multiplicativeResonator",
                      new ShapedOreRecipe(item.getItem(),
                        "rgr",
                        "rbr",
                        "rgr",
                        'r', DUST_REDSTONE,
                        'g', PLATE_GILDED_IRON,
                        'b', fortuneBook
                      ));
                    break;
                }
                case OVERCLOCKER: {
                    BookRecipeRegistry.addRecipe("overclocker",
                      new ShapedOreRecipe(item.getItem(),
                        "r r",
                        "btb",
                        "r r",
                        'r', DUST_REDSTONE,
                        'b', INGOT_BRASS,
                        't', CraftingComponentItems.Items.BRASS_TURBINE.createItemStack()
                      ));
                    break;
                }
                case PRECISE_CUTTING_HEAD: {
                    BookRecipeRegistry.addRecipe("preciseCuttingHead",
                      new ShapedOreRecipe(item.getItem(),
                        "f f",
                        "pdp",
                        "p p",
                        'f', FLINT,
                        'p', PLATE_BRASS,
                        'd', GEM_DIAMOND
                      ));
                    break;
                }
                case ROTARY_BLADES: {
                    BookRecipeRegistry.addRecipe("rotaryBlades",
                      new ShapedOreRecipe(item.getItem(),
                        " f ",
                        "ftf",
                        " f ",
                        'f', FLINT,
                        't', CraftingComponentItems.Items.BRASS_TURBINE.createItemStack()
                      ));
                    break;
                }
                case SIFTER: {
                    BookRecipeRegistry.addRecipe("sifter",
                      new ShapedOreRecipe(item.getItem(),
                        " p ",
                        "ctc",
                        " p ",
                        'p', CraftingComponentItems.Items.BRASS_PISTON.createItemStack(),
                        'c', COBBLESTONE_ORE,
                        't', CraftingComponentItems.Items.BRASS_TURBINE.createItemStack()
                      ));
                    break;
                }
                case STONE_GRINDER: {
                    BookRecipeRegistry.addRecipe("stoneGrinder",
                      new ShapedOreRecipe(item.getItem(),
                        "i i",
                        "ctc",
                        "pcp",
                        'i', INGOT_IRON,
                        'c', COBBLESTONE_ORE,
                        'p', PLATE_IRON,
                        't', CraftingComponentItems.Items.BRASS_TURBINE.createItemStack()
                      ));
                    break;
                }
                case THE_VOID: {
                    BookRecipeRegistry.addRecipe("theVoid", new ShapelessOreRecipe(item.getItem(), ENDER_CHEST, HOPPER));
                    break;
                }
                case THERMAL_DRILL: {
                    BookRecipeRegistry.addRecipe("thermalDrill",
                      new ShapedOreRecipe(item.getItem(),
                        " b ",
                        "bnb",
                        "iii",
                        'b', BLAZE_ROD,
                        'n', NETHER_BRICK,
                        'i', INGOT_BRASS
                      ));
                    break;
                }
                case TIMBER_CHAIN: {
                    BookRecipeRegistry.addRecipe("treeFeller",
                      new ShapedOreRecipe(item.getItem(),
                        "fpf",
                        "p p",
                        "fpf",
                        'f', FLINT,
                        'p', PLATE_IRON
                      ));
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }
}
