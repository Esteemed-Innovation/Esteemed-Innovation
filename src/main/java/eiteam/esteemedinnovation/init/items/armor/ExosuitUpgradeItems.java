package eiteam.esteemedinnovation.init.items.armor;

import eiteam.esteemedinnovation.Config;
import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.api.book.BookRecipeRegistry;
import eiteam.esteemedinnovation.api.crucible.CrucibleLiquid;
import eiteam.esteemedinnovation.api.crucible.CrucibleRegistry;
import eiteam.esteemedinnovation.api.exosuit.ExosuitPlate;
import eiteam.esteemedinnovation.api.exosuit.ExosuitRegistry;
import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.init.IInitCategory;
import eiteam.esteemedinnovation.init.blocks.PipeBlocks;
import eiteam.esteemedinnovation.init.blocks.SteamMachineryBlocks;
import eiteam.esteemedinnovation.init.blocks.SteamNetworkBlocks;
import eiteam.esteemedinnovation.init.items.CraftingComponentItems;
import eiteam.esteemedinnovation.init.items.firearms.FirearmUpgradeItems;
import eiteam.esteemedinnovation.init.misc.DefaultCrucibleLiquids;
import eiteam.esteemedinnovation.item.ItemTank;
import eiteam.esteemedinnovation.item.armor.exosuit.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import static eiteam.esteemedinnovation.init.misc.OreDictEntries.*;
import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;

public class ExosuitUpgradeItems implements IInitCategory {
    public enum Items {
        JETPACK(new ItemExosuitJetpack(), "jetpack"),
        WINGS(new ItemExosuitWings(), "wings"),
        POWER_FIST(ExosuitSlot.BODY_HAND, "fireFist", null, 0, "powerFist"),
        EXTENDO_FIST(ExosuitSlot.BODY_HAND, "extendoFist", null, 0, "extendoFist"),
        THRUSTERS(new ItemExosuitSidepack(), "thrusters"),
        FALL_ASSIST(ExosuitSlot.BOOTS_TOP, "fallUpgrade", null, 0, "fallAssist"),
        JUMP_ASSIST(ExosuitSlot.BOOTS_TOP, "jumpUpgrade", null, 0, "jumpAssist"),
        DOUBLE_JUMP(ExosuitSlot.BOOTS_FEET, "doubleJump", null, 1, "doubleJump"),
        RUN_ASSIST(ExosuitSlot.LEGS_LEGS, "runUpgrade", null, 0, "runAssist"),
        CANNING_MACHINE(ExosuitSlot.LEGS_HIPS, "canner", null, 1, "canner"),
        PITON_DEPLOYER(ExosuitSlot.BODY_HAND, EsteemedInnovation.MOD_ID + ":textures/models/armor/pitonDeployer.png", null, 1, "pitonDeployer"),
        STEALTH(ExosuitSlot.LEGS_LEGS, "stealthUpgrade", null, 0, "stealthUpgrade"),
        ENDER_SHROUD(ExosuitSlot.VANITY, "", null, 0, "enderShroud"),
        REINFORCED_TANK(new ItemTank(Config.reinforcedTankCapacity, EsteemedInnovation.MOD_ID + ":textures/models/armor/reinforcedTank.png", EsteemedInnovation.MOD_ID + ":textures/models/armor/reinforcedTank_grey.png"), "reinforcedTank"),
        UBER_REINFORCED_TANK(new ItemTank(Config.uberReinforcedTankCapacity, EsteemedInnovation.MOD_ID + ":textures/models/armor/uberReinforcedTank.png", EsteemedInnovation.MOD_ID + ":textures/models/armor/uberReinforcedTank_grey.png"), "uberReinforcedTank"),
        REBREATHER(ExosuitSlot.HEAD_GOGGLES, "rebreatherUpgrade", null, 1, "rebreather"),
        HYDROPHOBIC_COATINGS(ExosuitSlot.BOOTS_TOP, "", null, 0, "hydrophobicCoatings"),
        PYROPHOBIC_COATINGS(ExosuitSlot.BOOTS_TOP, "", null, 0, "pyrophobicCoatings"),
        ANCHOR_HEELS(new ItemExosuitAnchorHeels(), "anchorHeels"),
        RELOADING_HOLSTERS(new ItemExosuitReloadingHolster(), "reloadingHolsters"),
        FREQUENCY_SHIFTER(new ItemExosuitFrequencyShifter(), "frequencyShifter"),
        DRAGON_ROAR(new ItemExosuitDragonRoar(), "dragonRoar"),
        EXOSUIT_PLATE(new ItemExosuitPlate(), "exosuitPlate"),
        PISTON_PUSH(ExosuitSlot.BODY_HAND, "", null, 0, "pistonPush");

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
            item.setUnlocalizedName(EsteemedInnovation.MOD_ID + ":" + name);
            item.setCreativeTab(EsteemedInnovation.tab);
            item.setRegistryName(EsteemedInnovation.MOD_ID, name);
            GameRegistry.register(item);
            this.item = item;
        }

        Items(ExosuitSlot slot, String resource, String info, int prio, String name) {
            resource = EsteemedInnovation.MOD_ID + ":textures/models/armor/" + resource + ".png";
            Item item = new ItemExosuitUpgrade(slot, resource, info, prio);
            item.setUnlocalizedName(EsteemedInnovation.MOD_ID + ":" + name);
            item.setCreativeTab(EsteemedInnovation.tab);
            item.setRegistryName(EsteemedInnovation.MOD_ID, name);
            GameRegistry.register(item);
            this.item = item;
        }

        public Item getItem() {
            return item;
        }

        public boolean isEnabled() {
            if (!ArmorItems.Items.EXOSUIT_CHESTPIECE.isEnabled()) {
                return false;
            }
            switch (this) {
                case JETPACK: {
                    return Config.enableJetpack;
                }
                case WINGS: {
                    return Config.enableWings;
                }
                case POWER_FIST: {
                    return Config.enablePowerFist;
                }
                case EXTENDO_FIST: {
                    return Config.enableExtendoFist;
                }
                case THRUSTERS: {
                    return Config.enableThrusters;
                }
                case FALL_ASSIST: {
                    return Config.enableFallAssist;
                }
                case JUMP_ASSIST: {
                    return Config.enableJumpAssist;
                }
                case RUN_ASSIST: {
                    return Config.enableRunAssist;
                }
                case DOUBLE_JUMP: {
                    return Config.enableDoubleJump;
                }
                case CANNING_MACHINE: {
                    return Config.enableCanningMachine && Config.enableCanister;
                }
                case PITON_DEPLOYER: {
                    return Config.enablePitonDeployer;
                }
                case STEALTH: {
                    return Config.enableStealthUpgrade;
                }
                case ENDER_SHROUD: {
                    return Config.enableEnderShroud;
                }
                case REINFORCED_TANK: {
                    return Config.enableReinforcedTank;
                }
                case UBER_REINFORCED_TANK: {
                    return Config.enableReinforcedTank && Config.enableUberReinforcedTank;
                }
                case REBREATHER: {
                    return Config.enableRebreather;
                }
                case HYDROPHOBIC_COATINGS: {
                    return Config.enableHydrophobic;
                }
                case PYROPHOBIC_COATINGS: {
                    return Config.enablePyrophobic;
                }
                case ANCHOR_HEELS: {
                    return Config.enableAnchorHeels;
                }
                case PISTON_PUSH: {
                    return Config.enablePistonPush;
                }
                case RELOADING_HOLSTERS: {
                    return Config.enableReloadingHolsters && Config.enableFirearms && Config.enableEnhancementRevolver;
                }
                case FREQUENCY_SHIFTER: {
                    return Config.enableFrequencyShifter;
                }
                case DRAGON_ROAR: {
                    return Config.enableDragonRoar;
                }
                case EXOSUIT_PLATE: {
                    return true;
                }
                default: {
                    return false;
                }
            }
        }
    }

    public enum PlateItems {
        IRON_EXO("Iron", 0, "Iron", "iron"),
        GOLD_EXO("Gold", 1, "Gold", "gold"),
        COPPER_EXO("Copper", 2, "Copper", "copper"),
        ZINC_EXO("Zinc", 3, "Zinc", "zinc"),
        BRASS_EXO("Brass", 4, "Brass", "brass"),
        GILDED_IRON_EXO("Gilded Iron", 5, "GildedIron", "gilded"),
        LEAD_EXO("Lead", 6, "Lead", "lead");

        private int metadata;
        private String id;

        public static PlateItems[] LOOKUP = new PlateItems[values().length];

        static {
            for (PlateItems item : values()) {
                if (item.isEnabled()) {
                    LOOKUP[item.ordinal()] = item;
                }
            }
        }

        PlateItems(String id, int metadata, String resource, String langSuffix) {
            ExosuitPlate plate = new ExosuitPlate(id, createItemStack(), resource, resource,
              EsteemedInnovation.MOD_ID + ".plate." + langSuffix);
            ExosuitRegistry.addExosuitPlate(plate);
            this.metadata = metadata;
            this.id = id;
        }

        public int getMetadata() {
            return metadata;
        }

        public String getIdentifier() {
            return id;
        }

        public ItemStack createItemStack() {
            return createItemStack(1);
        }

        public ItemStack createItemStack(int size) {
            return new ItemStack(Items.EXOSUIT_PLATE.getItem(), size, getMetadata());
        }

        public boolean isEnabled() {
            if (this == IRON_EXO) {
                return Config.enableIronPlate;
            } else if (this == GOLD_EXO) {
                return Config.enableGoldPlate;
            } else if (this == COPPER_EXO) {
                return Config.enableCopperPlate;
            } else if (this == ZINC_EXO) {
                return Config.enableZincPlate;
            } else if (this == BRASS_EXO) {
                return Config.enableBrassPlate;
            } else if (this == GILDED_IRON_EXO) {
                return Config.enableGildedIronPlate;
            } else if (this == LEAD_EXO) {
                return Config.enableLeadPlate;
            }
            return false;
        }
    }

    @Override
    public void oreDict() {}

    @Override
    public void recipes() {
        for (Items item : Items.LOOKUP) {
            switch (item) {
                case JETPACK: {
                    BookRecipeRegistry.addRecipe("jetpack1", new ShapedOreRecipe(item.getItem(),
                      "p p",
                      "ptg",
                      "p p",
                      'p', PipeBlocks.Blocks.BRASS_PIPE.getBlock(),
                      'g', SteamNetworkBlocks.Blocks.STEAM_GAUGE.getBlock(),
                      't', INGOT_BRASS
                    ));
                    BookRecipeRegistry.addRecipe("jetpack2", new ShapedOreRecipe(item.getItem(),
                      "p p",
                      "ptg",
                      "p p",
                      'p', PipeBlocks.Blocks.BRASS_PIPE.getBlock(),
                      'g', SteamNetworkBlocks.Blocks.STEAM_GAUGE.getBlock(),
                      't', PLATE_THIN_BRASS
                    ));
                    break;
                }
                case WINGS: {
                    BookRecipeRegistry.addRecipe("wings1", new ShapedOreRecipe(item.getItem(),
                      "xxx",
                      "ccc",
                      "c c",
                      'x', INGOT_BRASS,
                      'c', PLATE_THIN_COPPER
                    ));
                    BookRecipeRegistry.addRecipe("wings2", new ShapedOreRecipe(item.getItem(),
                      "xxx",
                      "ccc",
                      "c c",
                      'x', PLATE_THIN_BRASS,
                      'c', PLATE_THIN_COPPER
                    ));
                    break;
                }
                case POWER_FIST: {
                    BookRecipeRegistry.addRecipe("powerFist1", new ShapedOreRecipe(item.getItem(),
                      "b i",
                      "bpi",
                      "b i",
                      'i', NUGGET_IRON,
                      'p', CraftingComponentItems.Items.BRASS_PISTON.createItemStack(),
                      'b', NUGGET_BRASS
                    ));
                    BookRecipeRegistry.addRecipe("powerFist2", new ShapedOreRecipe(item.getItem(),
                      "b i",
                      "bpi",
                      "b i",
                      'i', PLATE_THIN_IRON,
                      'p', CraftingComponentItems.Items.BRASS_PISTON.createItemStack(),
                      'b', NUGGET_BRASS
                    ));
                    break;
                }
                case EXTENDO_FIST: {
                    BookRecipeRegistry.addRecipe("extendoFist1", new ShapedOreRecipe(item.getItem(),
                      " ii",
                      "bbi",
                      "bb ",
                      'i', INGOT_IRON,
                      'b', NUGGET_BRASS
                    ));
                    BookRecipeRegistry.addRecipe("extendoFist2", new ShapedOreRecipe(item.getItem(),
                      " ii",
                      "bbi",
                      "bb ",
                      'i', PLATE_THIN_IRON,
                      'b', NUGGET_BRASS
                    ));
                    break;
                }
                case THRUSTERS: {
                    BookRecipeRegistry.addRecipe("thrusters1", new ShapedOreRecipe(item.getItem(),
                      "tnt",
                      "ptp",
                      "tnt",
                      'p', PipeBlocks.Blocks.BRASS_PIPE.getBlock(),
                      't', INGOT_BRASS,
                      'n', NUGGET_BRASS
                    ));
                    BookRecipeRegistry.addRecipe("thrusters2", new ShapedOreRecipe(item.getItem(),
                      "tnt",
                      "ptp",
                      "tnt",
                      'p', PipeBlocks.Blocks.BRASS_PIPE.getBlock(),
                      't', PLATE_THIN_BRASS,
                      'n', NUGGET_BRASS
                    ));
                    break;
                }
                case FALL_ASSIST: {
                    BookRecipeRegistry.addRecipe("noFall", new ShapedOreRecipe(item.getItem(),
                      "pbp",
                      "sss",
                      'b', LEATHER_BOOTS,
                      'p', CraftingComponentItems.Items.BRASS_PISTON.createItemStack(),
                      's', SLIMEBALL_ORE
                    ));
                    break;
                }
                case JUMP_ASSIST: {
                    BookRecipeRegistry.addRecipe("jumpAssist1", new ShapedOreRecipe(item.getItem(),
                      "s s",
                      "pbp",
                      "s s",
                      'b', LEATHER_BOOTS,
                      'p', CraftingComponentItems.Items.BRASS_PISTON.createItemStack(),
                      's', INGOT_BRASS
                    ));
                    BookRecipeRegistry.addRecipe("jumpAssist2", new ShapedOreRecipe(item.getItem(),
                      "s s",
                      "pbp",
                      "s s",
                      'b', LEATHER_BOOTS,
                      'p', CraftingComponentItems.Items.BRASS_PISTON.createItemStack(),
                      's', PLATE_THIN_BRASS
                    ));
                    break;
                }
                case RUN_ASSIST: {
                    BookRecipeRegistry.addRecipe("runAssist1", new ShapedOreRecipe(item.getItem(),
                      "p p",
                      "s s",
                      "p p",
                      'b', LEATHER_BOOTS,
                      'p', CraftingComponentItems.Items.BRASS_PISTON.createItemStack(),
                      's', INGOT_BRASS
                    ));
                    BookRecipeRegistry.addRecipe("runAssist2", new ShapedOreRecipe(item.getItem(),
                      "p p",
                      "s s",
                      "p p",
                      'b', LEATHER_BOOTS,
                      'p', CraftingComponentItems.Items.BRASS_PISTON.createItemStack(),
                      's', PLATE_THIN_BRASS
                    ));
                    break;
                }
                case DOUBLE_JUMP: {
                    BookRecipeRegistry.addRecipe("doubleJump1", new ShapedOreRecipe(item.getItem(),
                      "s s",
                      "v v",
                      'v', SteamNetworkBlocks.Blocks.VALVE_PIPE.getBlock(),
                      's', INGOT_BRASS
                    ));
                    BookRecipeRegistry.addRecipe("doubleJump2", new ShapedOreRecipe(item.getItem(),
                      "s s",
                      "v v",
                      'v', SteamNetworkBlocks.Blocks.VALVE_PIPE.getBlock(),
                      's', PLATE_THIN_BRASS
                    ));
                    break;
                }
                case CANNING_MACHINE: {
                    BookRecipeRegistry.addRecipe("canner1", new ShapedOreRecipe(item.getItem(),
                      "bbn",
                      "p p",
                      "i i",
                      'p', CraftingComponentItems.Items.BRASS_PISTON.createItemStack(),
                      'b', INGOT_BRASS,
                      'n', NUGGET_IRON,
                      'i', INGOT_IRON
                    ));
                    BookRecipeRegistry.addRecipe("canner2", new ShapedOreRecipe(item.getItem(),
                      "bbn",
                      "p p",
                      "i i",
                      'p', CraftingComponentItems.Items.BRASS_PISTON.createItemStack(),
                      'b', PLATE_THIN_BRASS,
                      'n', NUGGET_IRON,
                      'i', PLATE_THIN_IRON
                    ));
                    BookRecipeRegistry.addRecipe("canner3", new ShapedOreRecipe(item.getItem(),
                      "bbn",
                      "p p",
                      "i i",
                      'p', CraftingComponentItems.Items.BRASS_PISTON.createItemStack(),
                      'b', INGOT_BRASS,
                      'n', NUGGET_IRON,
                      'i', INGOT_IRON
                    ));
                    BookRecipeRegistry.addRecipe("canner4", new ShapedOreRecipe(item.getItem(),
                      "bbn",
                      "p p",
                      "i i",
                      'p', CraftingComponentItems.Items.BRASS_PISTON.createItemStack(),
                      'b', PLATE_THIN_BRASS,
                      'n', NUGGET_IRON,
                      'i', PLATE_THIN_IRON
                    ));
                    break;
                }
                case PITON_DEPLOYER: {
                    BookRecipeRegistry.addRecipe("pitonDeployer", new ShapedOreRecipe(item.getItem(),
                      " i ",
                      "lli",
                      "ll ",
                      'l', LEATHER,
                      'i', NUGGET_IRON
                    ));
                    break;
                }
                case STEALTH: {
                    BookRecipeRegistry.addRecipe("stealthUpgrade", new ShapedOreRecipe(item.getItem(),
                      "lwl",
                      "lsl",
                      "lwl",
                      'l', LEATHER,
                      'w', WOOL,
                      's', STRING
                    ));
                    break;
                }
                case ENDER_SHROUD: {
                    BookRecipeRegistry.addRecipe("enderShroud", new ShapedOreRecipe(item.getItem(),
                      " g ",
                      "geg",
                      " g ",
                      'g', GLASS,
                      'e', ENDER_PEARL
                    ));
                    break;
                }
                case REINFORCED_TANK: {
                    BookRecipeRegistry.addRecipe("reinforcedTank1", new ShapedOreRecipe(item.getItem(),
                      "ppp",
                      "tpt",
                      "ppp",
                      't', SteamNetworkBlocks.Blocks.TANK.getBlock(),
                      'p', INGOT_BRASS
                    ));
                    BookRecipeRegistry.addRecipe("reinforcedTank2", new ShapedOreRecipe(item.getItem(),
                      "ppp",
                      "tpt",
                      "ppp",
                      't', SteamNetworkBlocks.Blocks.TANK.getBlock(),
                      'p', PLATE_THIN_BRASS
                    ));
                    break;
                }
                case UBER_REINFORCED_TANK: {
                    BookRecipeRegistry.addRecipe("uberReinforcedTank1", new ShapedOreRecipe(item.getItem(),
                      "ppp",
                      "tbt",
                      "ppp",
                      't', Items.REINFORCED_TANK.getItem(),
                      'p', INGOT_BRASS,
                      'b', BLOCK_BRASS
                    ));
                    BookRecipeRegistry.addRecipe("uberReinforcedTank2", new ShapedOreRecipe(item.getItem(),
                      "ppp",
                      "tbt",
                      "ppp",
                      't', Items.REINFORCED_TANK.getItem(),
                      'p', PLATE_THIN_BRASS,
                      'b', BLOCK_BRASS
                    ));
                    break;
                }
                case REBREATHER: {
                    BookRecipeRegistry.addRecipe("rebreather",
                      new ShapedOreRecipe(item.getItem(),
                        " l ",
                        "ptp",
                        " c ",
                        'l', LEATHER,
                        'p', SteamNetworkBlocks.Blocks.VALVE_PIPE.getBlock(),
                        't', SteamNetworkBlocks.Blocks.TANK.getBlock(),
                        'c', new ItemStack(CARPET, 1, OreDictionary.WILDCARD_VALUE)
                      ));
                    break;
                }
                case HYDROPHOBIC_COATINGS: {
                    BookRecipeRegistry.addRecipe("hydrophobic",
                      new ShapedOreRecipe(item.getItem(),
                        "zbz",
                        "hzh",
                        "sss",
                        'z', PLATE_THIN_ZINC,
                        'b', LEATHER_BOOTS,
                        'h', SteamMachineryBlocks.Blocks.STEAM_HEATER.getBlock(),
                        's', SLIMEBALL_ORE
                      ));
                    break;
                }
                case PYROPHOBIC_COATINGS: {
                    BookRecipeRegistry.addRecipe("pyrophobic",
                      new ShapedOreRecipe(item.getItem(),
                        "cbc",
                        "hch",
                        "mmm",
                        'c', PLATE_THIN_COPPER,
                        'b', LEATHER_BOOTS,
                        'h', SteamMachineryBlocks.Blocks.STEAM_HEATER.getBlock(),
                        'm', MAGMA_CREAM
                      ));
                    break;
                }
                case ANCHOR_HEELS: {
                    if (Config.enableLeadPlate && OreDictionary.getOres("ingotLead").size() > 0 &&
                      !Config.enableAnchorAnvilRecipe) {
                        BookRecipeRegistry.addRecipe("anchorHeels", new ShapedOreRecipe(
                          new ItemStack(item.getItem()),
                          "p p",
                          "e e",
                          'p', PLATE_THIN_LEAD,
                          'e', PlateItems.LEAD_EXO.createItemStack()
                        ));
                    } else {
                        BookRecipeRegistry.addRecipe("anchorHeels", new ShapedOreRecipe(
                          new ItemStack(item.getItem()),
                          "p p",
                          "eae",
                          'p', PLATE_THIN_IRON,
                          'e', PlateItems.IRON_EXO.createItemStack(),
                          'a', ANVIL
                        ));
                    }
                    break;
                }
                case PISTON_PUSH: {
                    BookRecipeRegistry.addRecipe("pistonPush",
                      new ShapedOreRecipe(item.getItem(),
                        "n p",
                        "nbp",
                        "n p",
                        'n', NUGGET_BRASS,
                        'p', PISTON,
                        'b', CraftingComponentItems.Items.BRASS_PISTON.createItemStack()
                      ));
                    break;
                }
                case RELOADING_HOLSTERS: {
                    BookRecipeRegistry.addRecipe("reloadingHolsters",
                      new ShapedOreRecipe(item.getItem(),
                        "lbl",
                        "c c",
                        "p p",
                        'l', LEATHER,
                        'b', PLATE_THIN_BRASS,
                        'c', FirearmUpgradeItems.Items.REVOLVER_CHAMBER.getItem(),
                        'p', CraftingComponentItems.Items.BRASS_PISTON.createItemStack()
                      ));
                    break;
                }
                case FREQUENCY_SHIFTER: {
                    BookRecipeRegistry.addRecipe("frequencyShifter",
                      new ShapedOreRecipe(item.getItem(),
                        "t n",
                        "pjp",
                        " p ",
                        't', REDSTONE_TORCH,
                        'n', NUGGET_BRASS,
                        'p', PLATE_THIN_BRASS,
                        'j', JUKEBOX
                      ));
                    break;
                }
                case DRAGON_ROAR: {
                    BookRecipeRegistry.addRecipe("dragonRoar",
                      new ShapedOreRecipe(item.getItem(),
                        "ccc",
                        "cfc",
                        "cpc",
                        'c', PLATE_THIN_COPPER,
                        'f', SteamMachineryBlocks.Blocks.FAN.getBlock(),
                        'p', SteamNetworkBlocks.Blocks.VALVE_PIPE.getBlock()
                      ));
                    break;
                }
                default: {
                    break;
                }
            }
        }

        for (PlateItems item : PlateItems.LOOKUP) {
            switch (item) {
                case IRON_EXO: {
                    addExosuitPlateRecipes("exoIron", PLATE_THIN_IRON, item.createItemStack(), DefaultCrucibleLiquids.Liquids.IRON_LIQUID.getLiquid());
                    break;
                }
                case GOLD_EXO: {
                    addExosuitPlateRecipes("exoGold", PLATE_THIN_GOLD, item.createItemStack(), DefaultCrucibleLiquids.Liquids.GOLD_LIQUID.getLiquid());
                    break;
                }
                case COPPER_EXO: {
                    addExosuitPlateRecipes("exoCopper", PLATE_THIN_COPPER, item.createItemStack(), DefaultCrucibleLiquids.Liquids.COPPER_LIQUID.getLiquid());
                    break;
                }
                case ZINC_EXO: {
                    addExosuitPlateRecipes("exoZinc", PLATE_THIN_ZINC, item.createItemStack(), DefaultCrucibleLiquids.Liquids.ZINC_LIQUID.getLiquid());
                    break;
                }
                case BRASS_EXO: {
                    addExosuitPlateRecipes("exoBrass", PLATE_THIN_BRASS, item.createItemStack(), DefaultCrucibleLiquids.Liquids.BRASS_LIQUID.getLiquid());
                    break;
                }
                case GILDED_IRON_EXO: {
                    addExosuitPlateRecipes("exoGildedIron", PLATE_THIN_GILDED_IRON, item.createItemStack());
                    break;
                }
                case LEAD_EXO: {

                    addExosuitPlateRecipes("exoLead", PLATE_THIN_LEAD, item.createItemStack(), DefaultCrucibleLiquids.Liquids.LEAD_LIQUID.getLiquid());
                }
            }
        }
    }

    /**
     * Adds exosuit plate recipe (2x2 of ingredient), and a melting recipe for the itemstack.
     * @param str The name of the recipe for the book.
     * @param ingredient The ingredient, either an ItemStack or an OreDict string.
     * @param plate The output plate.
     */
    public void addExosuitPlateRecipes(String str, Object ingredient, ItemStack plate, CrucibleLiquid liq) {
        addExosuitPlateRecipes(str, ingredient, plate);
        CrucibleRegistry.registerMeltRecipe(plate.getItem(), plate.getItemDamage(), liq, 24);
    }

    /**
     * Adds an exosuit plate recipe (2x2 of ingredient), without a melting recipe for the itemstack.
     * @param str The name of the recipe for the book.
     * @param ingredient The ingredient, either an ItemStack or an OreDict string
     * @param plate The output plate.
     */
    public void addExosuitPlateRecipes(String str, Object ingredient, ItemStack plate) {
        BookRecipeRegistry.addRecipe(str, new ShapedOreRecipe(plate, "xx", "xx", 'x', ingredient));
        if (ingredient instanceof ItemStack) {
            ItemStack stack = ((ItemStack) ingredient).copy();
            stack.stackSize = 4;
            GameRegistry.addRecipe(new ShapelessOreRecipe(stack, plate));
        } else if (ingredient instanceof String) {
            for (ItemStack ore : OreDictionary.getOres((String) ingredient)) {
                ItemStack stack = ore.copy();
                stack.stackSize = 4;
                GameRegistry.addRecipe(new ShapelessOreRecipe(stack, plate));
            }
        }
    }
}