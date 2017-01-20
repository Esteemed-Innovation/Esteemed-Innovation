package eiteam.esteemedinnovation.armor;

import eiteam.esteemedinnovation.api.Constants;
import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.api.crucible.CrucibleLiquid;
import eiteam.esteemedinnovation.api.crucible.CrucibleRegistry;
import eiteam.esteemedinnovation.api.exosuit.ExosuitPlate;
import eiteam.esteemedinnovation.api.exosuit.ExosuitRegistry;
import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.armor.exosuit.ExosuitItemModelLoader;
import eiteam.esteemedinnovation.armor.exosuit.ExosuitModelCache;
import eiteam.esteemedinnovation.armor.exosuit.ItemExosuitArmor;
import eiteam.esteemedinnovation.armor.exosuit.ItemExosuitColorHandler;
import eiteam.esteemedinnovation.armor.exosuit.upgrades.*;
import eiteam.esteemedinnovation.armor.exosuit.upgrades.anchors.ItemExosuitAnchorHeels;
import eiteam.esteemedinnovation.armor.exosuit.upgrades.frequency.AnimalData;
import eiteam.esteemedinnovation.armor.exosuit.upgrades.frequency.AnimalDataStorage;
import eiteam.esteemedinnovation.armor.exosuit.upgrades.frequency.ItemExosuitFrequencyShifter;
import eiteam.esteemedinnovation.armor.exosuit.upgrades.jetpack.ItemExosuitJetpack;
import eiteam.esteemedinnovation.armor.exosuit.upgrades.plates.*;
import eiteam.esteemedinnovation.armor.exosuit.upgrades.pulsenozzle.*;
import eiteam.esteemedinnovation.armor.exosuit.upgrades.reloading.ItemExosuitReloadingHolster;
import eiteam.esteemedinnovation.armor.exosuit.upgrades.roar.ItemExosuitDragonRoar;
import eiteam.esteemedinnovation.armor.exosuit.upgrades.thrusters.ItemExosuitSidepack;
import eiteam.esteemedinnovation.armor.exosuit.upgrades.wings.ItemExosuitWings;
import eiteam.esteemedinnovation.armor.tophat.ItemTophat;
import eiteam.esteemedinnovation.armor.tophat.VillagerData;
import eiteam.esteemedinnovation.armor.tophat.VillagerDataStorage;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import eiteam.esteemedinnovation.storage.steam.ItemTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static eiteam.esteemedinnovation.commons.EsteemedInnovation.EXOSUIT_CATEGORY;
import static eiteam.esteemedinnovation.commons.EsteemedInnovation.GADGET_CATEGORY;
import static eiteam.esteemedinnovation.commons.OreDictEntries.*;
import static eiteam.esteemedinnovation.firearms.FirearmModule.REVOLVER_CHAMBER;
import static eiteam.esteemedinnovation.firearms.FirearmModule.SPYGLASS;
import static eiteam.esteemedinnovation.heater.HeaterModule.STEAM_HEATER;
import static eiteam.esteemedinnovation.materials.MaterialsModule.*;
import static eiteam.esteemedinnovation.misc.ItemCraftingComponent.Types.BRASS_PISTON;
import static eiteam.esteemedinnovation.misc.MiscellaneousModule.COMPONENT;
import static eiteam.esteemedinnovation.steamsafety.SafetyModule.STEAM_GAUGE;
import static eiteam.esteemedinnovation.storage.StorageModule.STEAM_TANK;
import static eiteam.esteemedinnovation.transport.TransportationModule.*;
import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;

public class ArmorModule extends ContentModule {
    public static final ItemArmor.ArmorMaterial EXO_MAT = EnumHelper.addArmorMaterial("EXOSUIT", EsteemedInnovation.MOD_ID + ":exo", 15, new int[] { 2, 5, 4, 1 }, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0F);
    @CapabilityInject(AnimalData.class)
    public static final Capability<AnimalData> ANIMAL_DATA = null;
    @CapabilityInject(VillagerData.class)
    public static final Capability<VillagerData> VILLAGER_DATA = null;

    public static ItemExosuitArmor EXO_HEAD;
    public static ItemExosuitArmor EXO_CHEST;
    public static ItemExosuitArmor EXO_LEGS;
    public static ItemExosuitArmor EXO_BOOTS;

    public static Item JETPACK;
    public static Item WINGS;
    public static Item POWER_FIST;
    public static Item EXTENDO_FIST;
    public static Item THRUSTERS;
    public static Item FALL_ASSIST;
    public static Item JUMP_ASSIST;
    public static Item DOUBLE_JUMP;
    public static Item RUN_ASSIST;
    public static Item CANNING_MACHINE;
    public static Item PITON_DEPLOYER;
    public static Item STEALTH;
    public static Item ENDER_SHROUD;
    public static Item REINFORCED_TANK;
    public static Item UBER_REINFORCED_TANK;
    public static Item REBREATHER;
    public static Item HYDROPHOBIC_COATINGS;
    public static Item PYROPHOBIC_COATINGS;
    public static Item ANCHOR_HEELS;
    public static Item RELOADING_HOLSTERS;
    public static Item FREQUENCY_SHIFTER;
    public static Item DRAGON_ROAR;
    public static Item EXOSUIT_PLATE;
    public static Item PISTON_PUSH;

    public static final int IRON_PLATE_META = 0;
    public static ExosuitPlate IRON_PLATE = new ExosuitPlateIron();
    public static final int GOLD_PLATE_META = 1;
    public static ExosuitPlate GOLD_PLATE = new ExosuitPlate("Gold", null, "Gold", "Gold", Constants.EI_MODID + ".plate.gold");
    public static final int COPPER_PLATE_META = 2;
    public static ExosuitPlate COPPER_PLATE = new ExosuitPlateCopper();
    public static final int ZINC_PLATE_META = 3;
    public static ExosuitPlate ZINC_PLATE = new ExosuitPlate("Zinc", null, "Zinc", "Zinc", Constants.EI_MODID + ".plate.zinc");
    public static final int BRASS_PLATE_META = 4;
    public static ExosuitPlate BRASS_PLATE = new ExosuitPlateBrass();
    public static final int GILDED_IRON_PLATE_META = 5;
    public static ExosuitPlate GILDED_IRON_PLATE = new ExosuitPlateGildedIron();
    public static final int LEAD_PLATE_META = 6;
    public static ExosuitPlate LEAD_PLATE = new ExosuitPlateLead();
    public static final int MAX_PLATE_META = LEAD_PLATE_META;

    public static final ItemArmor.ArmorMaterial GILDED_MAT = EnumHelper.addArmorMaterial("GILDEDGOLD", "minecraft:gold", 15, new int[] {2, 6, 5, 2 }, 9, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0F);
    public static Item GILDED_HEAD;
    public static Item GILDED_CHEST;
    public static Item GILDED_LEGS;
    public static Item GILDED_BOOTS;

    public static final ItemArmor.ArmorMaterial BRASS_MAT = EnumHelper.addArmorMaterial("BRASS", EsteemedInnovation.MOD_ID + ":brass", 11, new int[] { 2, 7, 6, 3 }, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0F);
    public static Item BRASS_HEAD;
    public static Item BRASS_CHEST;
    public static Item BRASS_LEGS;
    public static Item BRASS_BOOTS;

    public static final ItemArmor.ArmorMaterial MONOCLE_MAT = EnumHelper.addArmorMaterial("MONOCLE", EsteemedInnovation.MOD_ID + ":monocle", 5, new int[] { 1, 3, 2, 1 }, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0F);
    public static Item MONOCLE;
    public static Item GOGGLES;
    public static Item TOP_HAT;
    public static Item ENTREPRENEUR_TOP_HAT;

    private static Set<Item> toRegisterNormally = new HashSet<>();

    @Override
    public void create(Side side) {
        EXO_HEAD = (ItemExosuitArmor) setup(new ItemExosuitArmor(EntityEquipmentSlot.HEAD, EXO_MAT), "exosuit_head");
        EXO_CHEST = (ItemExosuitArmor) setup(new ItemExosuitArmor(EntityEquipmentSlot.CHEST, EXO_MAT), "exosuit_body");
        EXO_LEGS = (ItemExosuitArmor) setup(new ItemExosuitArmor(EntityEquipmentSlot.LEGS, EXO_MAT), "exosuit_legs");
        EXO_BOOTS = (ItemExosuitArmor) setup(new ItemExosuitArmor(EntityEquipmentSlot.FEET, EXO_MAT), "exosuit_feet");

        JETPACK = setup(new ItemExosuitJetpack(), "jetpack");
        WINGS = setup(new ItemExosuitWings(), "wings");
        POWER_FIST = setup(new ItemPowerFistUpgrade(), "power_fist");
        EXTENDO_FIST = setup(new ItemExosuitUpgrade(ExosuitSlot.BODY_HAND, resource("extendoFist"), null, 0), "extendo_fist");
        THRUSTERS = setup(new ItemExosuitSidepack(), "thrusters");
        FALL_ASSIST = setup(new ItemFallAssistUpgrade(), "fall_assist");
        JUMP_ASSIST = setup(new ItemJumpAssistUpgrade(), "jump_assist");
        DOUBLE_JUMP = setup(new ItemDoubleJumpUpgrade(), "double_jump");
        RUN_ASSIST = setup(new ItemModularAcceleratorUpgrade(), "run_assist");
        CANNING_MACHINE = setup(new ItemExosuitUpgrade(ExosuitSlot.LEGS_HIPS, resource("canner"), null, 1), "canner");
        PITON_DEPLOYER = setup(new ItemExosuitUpgrade(ExosuitSlot.BODY_HAND, resource("pitonDeployer"), null, 1), "piton_deployer");
        STEALTH = setup(new ItemExosuitUpgrade(ExosuitSlot.LEGS_LEGS, resource("stealthUpgrade"), null, 0), "stealth_upgrade");
        ENDER_SHROUD = setup(new ItemExosuitUpgrade(ExosuitSlot.VANITY, null, null, 0), "ender_shroud");
        REINFORCED_TANK = setup(new ItemTank(Config.reinforcedTankCapacity, EsteemedInnovation.MOD_ID + ":textures/models/armor/reinforcedTank.png", EsteemedInnovation.MOD_ID + ":textures/models/armor/reinforcedTank_grey.png"), "reinforced_tank");
        UBER_REINFORCED_TANK = setup(new ItemTank(Config.uberReinforcedTankCapacity, EsteemedInnovation.MOD_ID + ":textures/models/armor/uberReinforcedTank.png", EsteemedInnovation.MOD_ID + ":textures/models/armor/uberReinforcedTank_grey.png"), "uber_reinforced_tank");
        REBREATHER = setup(new ItemRebreatherUpgrade(), "rebreather");
        HYDROPHOBIC_COATINGS = setup(new ItemExosuitUpgrade(ExosuitSlot.BOOTS_TOP, resource("hydrophobiccoating"), null, 0), "hydrophobic_coatings");
        PYROPHOBIC_COATINGS = setup(new ItemExosuitUpgrade(ExosuitSlot.BOOTS_TOP, resource("pyrophobiccoating"), null, 0), "pyrophobic_coatings");
        ANCHOR_HEELS = setup(new ItemExosuitAnchorHeels(), "anchor_heels");
        RELOADING_HOLSTERS = setup(new ItemExosuitReloadingHolster(), "reloading_holsters");
        FREQUENCY_SHIFTER = setup(new ItemExosuitFrequencyShifter(), "frequency_shifter");
        DRAGON_ROAR = setup(new ItemExosuitDragonRoar(), "dragon_roar");
        EXOSUIT_PLATE = setup(new ItemExosuitPlate(), "exosuit_plate", EsteemedInnovation.tab, false);
        PISTON_PUSH = setup(new ItemExosuitUpgrade(ExosuitSlot.BODY_HAND, resource("pistonarm"), null, 0), "piston_push");

        IRON_PLATE.setItem(plateStack(IRON_PLATE_META));
        ExosuitRegistry.addExosuitPlate(IRON_PLATE);
        GOLD_PLATE.setItem(plateStack(GOLD_PLATE_META));
        ExosuitRegistry.addExosuitPlate(GOLD_PLATE);
        COPPER_PLATE.setItem(plateStack(COPPER_PLATE_META));
        ExosuitRegistry.addExosuitPlate(COPPER_PLATE);
        ZINC_PLATE.setItem(plateStack(ZINC_PLATE_META));
        ExosuitRegistry.addExosuitPlate(ZINC_PLATE);
        BRASS_PLATE.setItem(plateStack(BRASS_PLATE_META));
        ExosuitRegistry.addExosuitPlate(BRASS_PLATE);
        GILDED_IRON_PLATE.setItem(plateStack(GILDED_IRON_PLATE_META));
        ExosuitRegistry.addExosuitPlate(GILDED_IRON_PLATE);
        LEAD_PLATE.setItem(plateStack(LEAD_PLATE_META));
        ExosuitRegistry.addExosuitPlate(LEAD_PLATE);

        GILDED_HEAD = setup(new ItemGenericArmor(GILDED_MAT, 2, EntityEquipmentSlot.HEAD, INGOT_GILDED_IRON, "GildedIron"), "gilded_iron_helmet", EsteemedInnovation.tabTools);
        GILDED_CHEST = setup(new ItemGenericArmor(GILDED_MAT, 2, EntityEquipmentSlot.CHEST, INGOT_GILDED_IRON, "GildedIron"), "gilded_iron_chestplate", EsteemedInnovation.tabTools);
        GILDED_LEGS = setup(new ItemGenericArmor(GILDED_MAT, 2, EntityEquipmentSlot.LEGS, INGOT_GILDED_IRON, "GildedIron"), "gilded_iron_leggings", EsteemedInnovation.tabTools);
        GILDED_BOOTS = setup(new ItemGenericArmor(GILDED_MAT, 2, EntityEquipmentSlot.FEET, INGOT_GILDED_IRON, "GildedIron"), "gilded_iron_boots", EsteemedInnovation.tabTools);

        BRASS_HEAD = setup(new ItemGenericArmor(BRASS_MAT, 2, EntityEquipmentSlot.HEAD, INGOT_BRASS, "Brass"), "brass_helmet", EsteemedInnovation.tabTools);
        BRASS_CHEST = setup(new ItemGenericArmor(BRASS_MAT, 2, EntityEquipmentSlot.CHEST, INGOT_BRASS, "Brass"), "brass_chestplate", EsteemedInnovation.tabTools);
        BRASS_LEGS = setup(new ItemGenericArmor(BRASS_MAT, 2, EntityEquipmentSlot.LEGS, INGOT_BRASS, "Brass"), "brass_leggings", EsteemedInnovation.tabTools);
        BRASS_BOOTS = setup(new ItemGenericArmor(BRASS_MAT, 2, EntityEquipmentSlot.FEET, INGOT_BRASS, "Brass"), "brass_boots", EsteemedInnovation.tabTools);

        MONOCLE = setup(new ItemGoggles(MONOCLE_MAT, 2, EntityEquipmentSlot.HEAD, LEATHER, "Monocle"), "monocle", EsteemedInnovation.tabTools);
        GOGGLES = setup(new ItemGoggles(MONOCLE_MAT, 2, EntityEquipmentSlot.HEAD, LEATHER, "Goggles"), "goggles", EsteemedInnovation.tabTools);
        TOP_HAT = setup(new ItemTophat(MONOCLE_MAT, 2, EntityEquipmentSlot.HEAD, false), "tophat_no_emerald", EsteemedInnovation.tabTools);
        ENTREPRENEUR_TOP_HAT = setup(new ItemTophat(MONOCLE_MAT, 2, EntityEquipmentSlot.HEAD, true), "tophat", EsteemedInnovation.tabTools);

        CapabilityManager.INSTANCE.register(AnimalData.class, new AnimalDataStorage(), AnimalData.DefaultImplementation.class);
        CapabilityManager.INSTANCE.register(VillagerData.class, new VillagerDataStorage(), VillagerData.DefaultImplementation.class);

        EsteemedInnovation.channel.registerMessage(DoubleJumpServerActionPacketHandler.class, DoubleJumpServerActionPacket.class, 5, Side.SERVER);
        EsteemedInnovation.channel.registerMessage(DoubleJumpClientResponsePacketHandler.class, DoubleJumpClientResponsePacket.class, 6, Side.CLIENT);
    }

    @Override
    protected Item setup(Item startingItem, String path, CreativeTabs tab) {
        return setup(startingItem, path, tab, true);
    }

    private Item setup(Item startingItem, String path, CreativeTabs tab, boolean normal) {
        startingItem = super.setup(startingItem, path, tab);
        if (normal) {
            toRegisterNormally.add(startingItem);
        }
        return startingItem;
    }

    public static String resource(@Nonnull String base) {
        return Constants.EI_MODID + ":textures/models/armor/" + base + ".png";
    }

    @Override
    public void recipes(Side side) {
        if (Config.enableGoggles) {
            BookRecipeRegistry.addRecipe("monocle1", new ShapedOreRecipe(MONOCLE,
              " l ",
              "l l",
              "btb",
              'b', INGOT_BRASS,
              'l', LEATHER_ORE,
              't', SPYGLASS
            ));
            BookRecipeRegistry.addRecipe("monocle2", new ShapedOreRecipe(MONOCLE,
              " l ",
              "l l",
              "btb",
              'b', PLATE_THIN_BRASS,
              'l', LEATHER_ORE,
              't', SPYGLASS
            ));
            BookRecipeRegistry.addRecipe("goggles1", new ShapedOreRecipe(GOGGLES,
              " l ",
              "l l",
              "tbg",
              'b', INGOT_BRASS,
              'l', LEATHER_ORE,
              't', SPYGLASS,
              'g', BLOCK_GLASS_COLORLESS
            ));
            BookRecipeRegistry.addRecipe("goggles2", new ShapedOreRecipe(GOGGLES,
              " l ",
              "l l",
              "tbg",
              'b', PLATE_THIN_BRASS,
              'l', LEATHER_ORE,
              't', SPYGLASS,
              'g', BLOCK_GLASS_COLORLESS
            ));
        }
        if (Config.enableTopHat) {
            BookRecipeRegistry.addRecipe("hat", new ShapedOreRecipe(TOP_HAT,
              " l ",
              " l ",
              "lll",
              'e', GEM_EMERALD,
              'l', new ItemStack(WOOL, 1, EnumDyeColor.BLACK.getMetadata())
            ));
            if (Config.enableEmeraldHat) {
                BookRecipeRegistry.addRecipe("hatEmerald", new ShapelessOreRecipe(
                  ENTREPRENEUR_TOP_HAT, TOP_HAT, BLOCK_EMERALD));
            }
        }
        if (Config.enableExosuit) {
            BookRecipeRegistry.addRecipe("exoHead", new ShapedOreRecipe(EXO_HEAD,
              "xyx",
              "p p",
              "xyx",
              'x', PLATE_THIN_BRASS,
              'y', NUGGET_BRASS,
              'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata())
            ));
            BookRecipeRegistry.addRecipe("exoBody", new ShapedOreRecipe(
              new ItemStack(EXO_CHEST, 1, EXO_CHEST.getMaxDamage() - 1),
              "p p",
              "ygy",
              "xxx",
              'x', PLATE_THIN_BRASS,
              'y', NUGGET_BRASS,
              'g', STEAM_GAUGE,
              'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata())
            ));
            BookRecipeRegistry.addRecipe("exoLegs", new ShapedOreRecipe(EXO_LEGS,
              "yxy",
              "p p",
              "x x",
              'x', PLATE_THIN_BRASS,
              'y', NUGGET_BRASS,
              'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata())
            ));
            BookRecipeRegistry.addRecipe("exoFeet", new ShapedOreRecipe(EXO_BOOTS,
              "p p",
              "x x",
              'x', PLATE_THIN_BRASS,
              'y', NUGGET_BRASS,
              'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata())
            ));

            if (Config.enableJetpack) {
                BookRecipeRegistry.addRecipe("jetpack1", new ShapedOreRecipe(JETPACK,
                  "p p",
                  "ptg",
                  "p p",
                  'p', BRASS_PIPE,
                  'g', STEAM_GAUGE,
                  't', INGOT_BRASS
                ));
                BookRecipeRegistry.addRecipe("jetpack2", new ShapedOreRecipe(JETPACK,
                  "p p",
                  "ptg",
                  "p p",
                  'p', BRASS_PIPE,
                  'g', STEAM_GAUGE,
                  't', PLATE_THIN_BRASS
                ));
            }
            if (Config.enableWings) {
                BookRecipeRegistry.addRecipe("wings1", new ShapedOreRecipe(WINGS,
                  "xxx",
                  "ccc",
                  "c c",
                  'x', INGOT_BRASS,
                  'c', PLATE_THIN_COPPER
                ));
                BookRecipeRegistry.addRecipe("wings2", new ShapedOreRecipe(WINGS,
                  "xxx",
                  "ccc",
                  "c c",
                  'x', PLATE_THIN_BRASS,
                  'c', PLATE_THIN_COPPER
                ));
            }
            if (Config.enablePowerFist) {
                BookRecipeRegistry.addRecipe("powerFist1", new ShapedOreRecipe(POWER_FIST,
                  "b i",
                  "bpi",
                  "b i",
                  'i', NUGGET_IRON,
                  'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
                  'b', NUGGET_BRASS
                ));
                BookRecipeRegistry.addRecipe("powerFist2", new ShapedOreRecipe(POWER_FIST,
                  "b i",
                  "bpi",
                  "b i",
                  'i', PLATE_THIN_IRON,
                  'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
                  'b', NUGGET_BRASS
                ));
            }
            if (Config.enableExtendoFist) {
                BookRecipeRegistry.addRecipe("extendoFist1", new ShapedOreRecipe(EXTENDO_FIST,
                  " ii",
                  "bbi",
                  "bb ",
                  'i', INGOT_IRON,
                  'b', NUGGET_BRASS
                ));
                BookRecipeRegistry.addRecipe("extendoFist2", new ShapedOreRecipe(EXTENDO_FIST,
                  " ii",
                  "bbi",
                  "bb ",
                  'i', PLATE_THIN_IRON,
                  'b', NUGGET_BRASS
                ));
            }
            if (Config.enableThrusters) {
                BookRecipeRegistry.addRecipe("thrusters1", new ShapedOreRecipe(THRUSTERS,
                  "tnt",
                  "ptp",
                  "tnt",
                  'p', BRASS_PIPE,
                  't', INGOT_BRASS,
                  'n', NUGGET_BRASS
                ));
                BookRecipeRegistry.addRecipe("thrusters2", new ShapedOreRecipe(THRUSTERS,
                  "tnt",
                  "ptp",
                  "tnt",
                  'p', BRASS_PIPE,
                  't', PLATE_THIN_BRASS,
                  'n', NUGGET_BRASS
                ));
            }
            if (Config.enableFallAssist) {
                BookRecipeRegistry.addRecipe("noFall", new ShapedOreRecipe(FALL_ASSIST,
                  "pbp",
                  "sss",
                  'b', LEATHER_BOOTS,
                  'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
                  's', SLIMEBALL_ORE
                ));
            }
            if (Config.enableJumpAssist) {
                BookRecipeRegistry.addRecipe("jumpAssist1", new ShapedOreRecipe(JUMP_ASSIST,
                  "s s",
                  "pbp",
                  "s s",
                  'b', LEATHER_BOOTS,
                  'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
                  's', INGOT_BRASS
                ));
                BookRecipeRegistry.addRecipe("jumpAssist2", new ShapedOreRecipe(JUMP_ASSIST,
                  "s s",
                  "pbp",
                  "s s",
                  'b', LEATHER_BOOTS,
                  'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
                  's', PLATE_THIN_BRASS
                ));
            }
            if (Config.enableRunAssist) {
                BookRecipeRegistry.addRecipe("runAssist1", new ShapedOreRecipe(RUN_ASSIST,
                  "p p",
                  "s s",
                  "p p",
                  'b', LEATHER_BOOTS,
                  'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
                  's', INGOT_BRASS
                ));
                BookRecipeRegistry.addRecipe("runAssist2", new ShapedOreRecipe(RUN_ASSIST,
                  "p p",
                  "s s",
                  "p p",
                  'b', LEATHER_BOOTS,
                  'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
                  's', PLATE_THIN_BRASS
                ));
            }
            if (Config.enableDoubleJump) {
                BookRecipeRegistry.addRecipe("doubleJump1", new ShapedOreRecipe(DOUBLE_JUMP,
                  "s s",
                  "v v",
                  'v', VALVE_PIPE,
                  's', INGOT_BRASS
                ));
                BookRecipeRegistry.addRecipe("doubleJump2", new ShapedOreRecipe(DOUBLE_JUMP,
                  "s s",
                  "v v",
                  'v', VALVE_PIPE,
                  's', PLATE_THIN_BRASS
                ));
            }
            if (Config.enableCanningMachine && Config.enableCanister) {
                BookRecipeRegistry.addRecipe("canner1", new ShapedOreRecipe(CANNING_MACHINE,
                  "bbn",
                  "p p",
                  "i i",
                  'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
                  'b', INGOT_BRASS,
                  'n', NUGGET_IRON,
                  'i', INGOT_IRON
                ));
                BookRecipeRegistry.addRecipe("canner2", new ShapedOreRecipe(CANNING_MACHINE,
                  "bbn",
                  "p p",
                  "i i",
                  'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
                  'b', PLATE_THIN_BRASS,
                  'n', NUGGET_IRON,
                  'i', PLATE_THIN_IRON
                ));
                BookRecipeRegistry.addRecipe("canner3", new ShapedOreRecipe(CANNING_MACHINE,
                  "bbn",
                  "p p",
                  "i i",
                  'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
                  'b', INGOT_BRASS,
                  'n', NUGGET_IRON,
                  'i', INGOT_IRON
                ));
                BookRecipeRegistry.addRecipe("canner4", new ShapedOreRecipe(CANNING_MACHINE,
                  "bbn",
                  "p p",
                  "i i",
                  'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
                  'b', PLATE_THIN_BRASS,
                  'n', NUGGET_IRON,
                  'i', PLATE_THIN_IRON
                ));
            }
            if (Config.enablePitonDeployer) {
                BookRecipeRegistry.addRecipe("pitonDeployer", new ShapedOreRecipe(PITON_DEPLOYER,
                  " i ",
                  "lli",
                  "ll ",
                  'l', LEATHER,
                  'i', NUGGET_IRON
                ));
            }
            if (Config.enableStealthUpgrade) {
                BookRecipeRegistry.addRecipe("stealthUpgrade", new ShapedOreRecipe(STEALTH,
                  "lwl",
                  "lsl",
                  "lwl",
                  'l', LEATHER,
                  'w', WOOL,
                  's', STRING
                ));
            }
            if (Config.enableEnderShroud) {
                BookRecipeRegistry.addRecipe("enderShroud", new ShapedOreRecipe(ENDER_SHROUD,
                  " g ",
                  "geg",
                  " g ",
                  'g', GLASS,
                  'e', ENDER_PEARL
                ));
            }
            if (Config.enableReinforcedTank) {
                BookRecipeRegistry.addRecipe("reinforcedTank1", new ShapedOreRecipe(REINFORCED_TANK,
                  "ppp",
                  "tpt",
                  "ppp",
                  't', STEAM_TANK,
                  'p', INGOT_BRASS
                ));
                BookRecipeRegistry.addRecipe("reinforcedTank2", new ShapedOreRecipe(REINFORCED_TANK,
                  "ppp",
                  "tpt",
                  "ppp",
                  't', STEAM_TANK,
                  'p', PLATE_THIN_BRASS
                ));
                if (Config.enableUberReinforcedTank) {
                    BookRecipeRegistry.addRecipe("uberReinforcedTank1", new ShapedOreRecipe(UBER_REINFORCED_TANK,
                      "ppp",
                      "tbt",
                      "ppp",
                      't', REINFORCED_TANK,
                      'p', INGOT_BRASS,
                      'b', BLOCK_BRASS
                    ));
                    BookRecipeRegistry.addRecipe("uberReinforcedTank2", new ShapedOreRecipe(UBER_REINFORCED_TANK,
                      "ppp",
                      "tbt",
                      "ppp",
                      't', REINFORCED_TANK,
                      'p', PLATE_THIN_BRASS,
                      'b', BLOCK_BRASS
                    ));
                }
            }
            if (Config.enableRebreather) {
                BookRecipeRegistry.addRecipe("rebreather",
                  new ShapedOreRecipe(REBREATHER,
                    " l ",
                    "ptp",
                    " c ",
                    'l', LEATHER,
                    'p', VALVE_PIPE,
                    't', STEAM_TANK,
                    'c', new ItemStack(CARPET, 1, OreDictionary.WILDCARD_VALUE)
                  ));
            }
            if (Config.enableHydrophobic) {
                BookRecipeRegistry.addRecipe("hydrophobic",
                  new ShapedOreRecipe(HYDROPHOBIC_COATINGS,
                    "zbz",
                    "hzh",
                    "sss",
                    'z', PLATE_THIN_ZINC,
                    'b', LEATHER_BOOTS,
                    'h', STEAM_HEATER,
                    's', SLIMEBALL_ORE
                  ));
            }
            if (Config.enablePyrophobic) {
                BookRecipeRegistry.addRecipe("pyrophobic",
                  new ShapedOreRecipe(PYROPHOBIC_COATINGS,
                    "cbc",
                    "hch",
                    "mmm",
                    'c', PLATE_THIN_COPPER,
                    'b', LEATHER_BOOTS,
                    'h', STEAM_HEATER,
                    'm', MAGMA_CREAM
                  ));
            }
            if (Config.enableAnchorHeels) {
                if (Config.enableLeadPlate && !OreDictionary.getOres(INGOT_LEAD).isEmpty() &&
                  !Config.enableAnchorAnvilRecipe) {
                    BookRecipeRegistry.addRecipe("anchorHeels", new ShapedOreRecipe(
                      new ItemStack(ANCHOR_HEELS),
                      "p p",
                      "e e",
                      'p', PLATE_THIN_LEAD,
                      'e', plateStack(LEAD_PLATE_META)
                    ));
                } else {
                    BookRecipeRegistry.addRecipe("anchorHeels", new ShapedOreRecipe(
                      new ItemStack(ANCHOR_HEELS),
                      "p p",
                      "eae",
                      'p', PLATE_THIN_IRON,
                      'e', plateStack(IRON_PLATE_META),
                      'a', ANVIL
                    ));
                }
            }
            if (Config.enablePistonPush) {
                BookRecipeRegistry.addRecipe("pistonPush",
                  new ShapedOreRecipe(PISTON_PUSH,
                    "n p",
                    "nbp",
                    "n p",
                    'n', NUGGET_BRASS,
                    'p', PISTON,
                    'b', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata())
                  ));
            }
            if (Config.enableReloadingHolsters && Config.enableFirearms && Config.enableEnhancementRevolver) {
                BookRecipeRegistry.addRecipe("reloadingHolsters",
                  new ShapedOreRecipe(RELOADING_HOLSTERS,
                    "lbl",
                    "c c",
                    "p p",
                    'l', LEATHER,
                    'b', PLATE_THIN_BRASS,
                    'c', REVOLVER_CHAMBER,
                    'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata())
                  ));
            }
            if (Config.enableFrequencyShifter) {
                BookRecipeRegistry.addRecipe("frequencyShifter",
                  new ShapedOreRecipe(FREQUENCY_SHIFTER,
                    "t n",
                    "pjp",
                    " p ",
                    't', REDSTONE_TORCH,
                    'n', NUGGET_BRASS,
                    'p', PLATE_THIN_BRASS,
                    'j', JUKEBOX
                  ));
            }
            if (Config.enableDragonRoar) {
                BookRecipeRegistry.addRecipe("dragonRoar",
                  new ShapedOreRecipe(DRAGON_ROAR,
                    "ccc",
                    "cfc",
                    "cpc",
                    'c', PLATE_THIN_COPPER,
                    'f', FAN,
                    'p', VALVE_PIPE
                  ));
            }
            if (Config.enableIronPlate) {
                addExosuitPlateRecipes("exoIron", PLATE_THIN_IRON, plateStack(IRON_PLATE_META), IRON_LIQUID);
            }
            if (Config.enableGoldPlate) {
                addExosuitPlateRecipes("exoGold", PLATE_THIN_GOLD, plateStack(GOLD_PLATE_META), GOLD_LIQUID);
            }
            if (Config.enableCopperPlate) {
                addExosuitPlateRecipes("exoCopper", PLATE_THIN_COPPER, plateStack(COPPER_PLATE_META), COPPER_LIQUID);
            }
            if (Config.enableZincPlate) {
                addExosuitPlateRecipes("exoZinc", PLATE_THIN_ZINC, plateStack(ZINC_PLATE_META), ZINC_LIQUID);
            }
            if (Config.enableBrassPlate) {
                addExosuitPlateRecipes("exoBrass", PLATE_THIN_BRASS, plateStack(BRASS_PLATE_META), BRASS_LIQUID);
            }
            if (Config.enableGildedIronPlate) {
                addExosuitPlateRecipes("exoGildedIron", PLATE_THIN_GILDED_IRON, plateStack(GILDED_IRON_PLATE_META));
            }
            if (Config.enableLeadPlate) {
                addExosuitPlateRecipes("exoLead", PLATE_THIN_LEAD, plateStack(LEAD_PLATE_META), LEAD_LIQUID);
            }
        }

        addHelmetRecipe(GILDED_HEAD, INGOT_GILDED_IRON);
        addHelmetRecipe(BRASS_HEAD, INGOT_BRASS);
        addChestRecipe(GILDED_CHEST, INGOT_GILDED_IRON);
        addChestRecipe(BRASS_CHEST, INGOT_BRASS);
        addLegRecipe(GILDED_LEGS, INGOT_GILDED_IRON);
        addLegRecipe(BRASS_LEGS, INGOT_BRASS);
        addFootRecipe(GILDED_BOOTS, INGOT_GILDED_IRON);
        addFootRecipe(BRASS_BOOTS, INGOT_BRASS);

        MinecraftForge.EVENT_BUS.register(new PhobicCoatingHandler());
    }

    private static void addLegRecipe(Item item, String ore) {
        GameRegistry.addRecipe(new ShapedOreRecipe(item,
          "xxx",
          "x x",
          "x x",
          'x', ore
        ));
    }

    private static void addHelmetRecipe(Item item, String ore) {
        GameRegistry.addRecipe(new ShapedOreRecipe(item,
          "xxx",
          "x x",
          'x', ore
        ));
    }

    private static void addChestRecipe(Item item, String ore) {
        GameRegistry.addRecipe(new ShapedOreRecipe(item,
          "x x",
          "xxx",
          "xxx",
          'x', ore
        ));
    }

    private static void addFootRecipe(Item item, String ore) {
        GameRegistry.addRecipe(new ShapedOreRecipe(item,
          "x x",
          "x x",
          'x', ore
        ));
    }

    /**
     * Adds exosuit plate recipe (2x2 of ingredient), and a melting recipe for the itemstack.
     * @param str The name of the recipe for the book.
     * @param ingredient The ingredient, either an ItemStack or an OreDict string.
     * @param plate The output plate.
     */
    private static void addExosuitPlateRecipes(String str, Object ingredient, ItemStack plate, CrucibleLiquid liq) {
        addExosuitPlateRecipes(str, ingredient, plate);
        CrucibleRegistry.registerMeltRecipe(plate.getItem(), plate.getItemDamage(), liq, 24);
    }

    /**
     * Adds an exosuit plate recipe (2x2 of ingredient), without a melting recipe for the itemstack.
     * @param str The name of the recipe for the book.
     * @param ingredient The ingredient, either an ItemStack or an OreDict string
     * @param plate The output plate.
     */
    private static void addExosuitPlateRecipes(String str, Object ingredient, ItemStack plate) {
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

    @Override
    public void finish(Side side) {
        if (Config.enableTopHat) {
            BookPageRegistry.addEntryToCategory(GADGET_CATEGORY, new BookEntry("research.TopHat.name",
              new BookPageItem("research.TopHat.name", "research.TopHat.0", new ItemStack(TOP_HAT)),
              new BookPageCrafting("", "hat")));
            if (Config.enableEmeraldHat) {
                BookPageRegistry.addEntryToCategory(GADGET_CATEGORY, new BookEntry("research.TopHatEmerald.name",
                  new BookPageItem("research.TopHatEmerald.name", "research.TopHatEmerald.0", new ItemStack(ENTREPRENEUR_TOP_HAT)),
                  new BookPageCrafting("", "hatEmerald")));
            }
        }

        if (Config.enableGoggles) {
            BookPageRegistry.addEntryToCategory(GADGET_CATEGORY, new BookEntry("research.Goggles.name",
              new BookPageItem("research.Goggles.name", "research.Goggles.0", new ItemStack(GOGGLES), new ItemStack(MONOCLE)),
              new BookPageCrafting("", "goggles1", "goggles2"),
              new BookPageCrafting("", "monocle1", "monocle2")));
        }

        if (Config.enableExosuit && Config.enableEngineering) {
            BookPageRegistry.addEntryToCategory(EXOSUIT_CATEGORY, new BookEntry("research.Exosuit.name",
              new BookPageItem("research.Exosuit.name", "research.Exosuit.0",
                new ItemStack(EXO_HEAD),
                new ItemStack(EXO_CHEST),
                new ItemStack(EXO_LEGS),
                new ItemStack(EXO_BOOTS)),
              new BookPageText("research.Exosuit.name", "research.Exosuit.1"),
              new BookPageCrafting("", "engineering"),
              new BookPageCrafting("", "exoHead"),
              new BookPageCrafting("", "exoBody"),
              new BookPageCrafting("", "exoLegs"),
              new BookPageCrafting("", "exoFeet")));

            {
                BookCategory.Factory tankFactory = new BookCategory.Factory("research.ExoTank.name")
                  .append(new BookEntry("research.ExoTank.name",
                    new BookPageItem("research.ExoTank.name", "research.ExoTank.0", new ItemStack(STEAM_TANK)),
                    new BookPageItem("research.ExoTankBase.name", "research.ExoTankBase.0", true, new ItemStack(STEAM_TANK))));
                if (Config.enableReinforcedTank) {
                    tankFactory.append(new BookEntry("research.ExoTankReinforced.name",
                      new BookPageItem("research.ExoTankReinforced.name", "research.ExoTankReinforced.0", true, new ItemStack(REINFORCED_TANK)),
                      new BookPageCrafting("", "reinforcedTank1", "reinforcedTank2")));
                }
                if (Config.enableUberReinforcedTank) {
                    tankFactory.append(new BookEntry("research.ExoTankUberReinforced",
                      new BookPageItem("research.ExoTankUberReinforced.name", "research.ExoTankUberReinforced.0", true, new ItemStack(UBER_REINFORCED_TANK)),
                      new BookPageCrafting("", "uberReinforcedTank1", "uberReinforcedTank2")));
                }
                BookPageRegistry.addSubcategoryToCategory(EXOSUIT_CATEGORY, tankFactory.build());
            }

            {
                ItemStack[] stacks = new ItemStack[4];
                for (int i = 0; i < 4; i++) {
                    ItemStack stack = new ItemStack(EXO_CHEST);
                    stack.setTagCompound(new NBTTagCompound());
                    ItemStack plate = null;
                    Collection<ExosuitPlate> values = ExosuitRegistry.plates.values();
                    Object item = values.toArray(new ExosuitPlate[values.size()])[i].getItem();
                    if (item instanceof String) {
                        if (!OreDictionary.getOres((String) item).isEmpty()) {
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
                      new BookPageItem("research.PlateCopper.name", "research.PlateCopper.0", true, plateStack(COPPER_PLATE_META)),
                      new BookPageCrafting("", "exoCopper")));
                }
                if (Config.enableIronPlate) {
                    plateFactory.append(new BookEntry("research.PlateIron.name",
                      new BookPageItem("research.PlateIron.name", "research.PlateIron.0", true, plateStack(IRON_PLATE_META)),
                      new BookPageCrafting("", "exoIron")));
                }
                if (Config.enableBrassPlate) {
                    plateFactory.append(new BookEntry("research.PlateBrass.name",
                      new BookPageItem("research.PlateBrass.name", "research.PlateBrass.0", true, plateStack(BRASS_PLATE_META)),
                      new BookPageCrafting("", "exoBrass")));
                }
                if (Config.enableGoldPlate) {
                    plateFactory.append(new BookEntry("research.PlateGold.name",
                      new BookPageItem("research.PlateGold.name", "research.PlateGold.0", true, plateStack(GOLD_PLATE_META)),
                      new BookPageCrafting("", "exoGold")));
                }
                if (Config.enableGildedIronPlate) {
                    plateFactory.append(new BookEntry("research.PlateGilded.name",
                      new BookPageItem("research.PlateGilded.name", "research.PlateGilded.0", true, plateStack(GILDED_IRON_PLATE_META)),
                      new BookPageCrafting("", "exoGildedIron")));
                }
                if (Config.enableLeadPlate && !OreDictionary.getOres(INGOT_LEAD).isEmpty()) {
                    plateFactory.append(new BookEntry("research.PlateLead.name",
                      new BookPageItem("research.PlateLead.name", "research.PlateLead.0", true, plateStack(LEAD_PLATE_META)),
                      new BookPageCrafting("", "exoLead")));
                }
                BookPageRegistry.addSubcategoryToCategory(EXOSUIT_CATEGORY, plateFactory.build());
            }
            {
                ItemStack[] stacks = new ItemStack[4];
                for (int i = 0; i < 4; i++) {
                    ItemStack stack = new ItemStack(EXO_CHEST);
                    stack.setTagCompound(new NBTTagCompound());
                    ItemStack dye = new ItemStack(DYE, 1, i);
                    ((ItemExosuitArmor) stack.getItem()).setInventorySlotContents(stack, 2, dye);
                    stacks[i] = stack;
                }
                ItemStack[] dyes = {
                  new ItemStack(DYE, 1, 0),
                  new ItemStack(DYE, 1, 1),
                  new ItemStack(DYE, 1, 2),
                  new ItemStack(DYE, 1, 3)
                };
                BookCategory.Factory vanityFactory = new BookCategory.Factory("research.ExoVanity.name")
                  .append(new BookEntry("research.ExoVanity.name",
                    new BookPageItem("research.ExoVanity.name", "research.ExoVanity.0", stacks)))
                  .append(new BookEntry("research.ExoDyes.name",
                    new BookPageItem("research.ExoDyes.name", "research.ExoDyes.0", true, dyes)));
                if (Config.enableEnderShroud) {
                    vanityFactory.append(new BookEntry("research.EnderShroud.name",
                      new BookPageItem("research.EnderShroud.name", "research.EnderShroud.0", true, new ItemStack(ENDER_SHROUD)),
                      new BookPageCrafting("", "enderShroud")));
                }
                BookPageRegistry.addSubcategoryToCategory(EXOSUIT_CATEGORY, vanityFactory.build());
            }

            {
                BookCategory.Factory headHelmFactory = new BookCategory.Factory("research.ExoHeadHelm.name");
                if (Config.enableTopHat) {
                    headHelmFactory.append(new BookEntry("research.ExoTopHat.name",
                      new BookPageItem("research.ExoTopHat.name", "research.ExoTopHat.0", true, new ItemStack(TOP_HAT), new ItemStack(ENTREPRENEUR_TOP_HAT)),
                      new BookPage("")));
                }
                if (Config.enableFrequencyShifter) {
                    headHelmFactory.append(new BookEntry("research.FrequencyShifter.name",
                      new BookPageItem("research.FrequencyShifter.name", "research.FrequencyShifter.0", true, new ItemStack(FREQUENCY_SHIFTER)),
                      new BookPageCrafting("", "frequencyShifter")));
                }
                BookPageRegistry.addSubcategoryToCategory(EXOSUIT_CATEGORY, headHelmFactory.build());
            }

            {
                BookCategory.Factory headGoggleFactory = new BookCategory.Factory("research.ExoHeadGoggle.name");
                if (Config.enableGoggles) {
                    headGoggleFactory.append(new BookEntry("research.ExoGoggles.name",
                      new BookPageItem("research.ExoGoggles.name", "research.ExoGoggles.0", true, new ItemStack(GOGGLES), new ItemStack(MONOCLE)),
                      new BookPage("")));
                }
                if (Config.enableRebreather) {
                    headGoggleFactory.append(new BookEntry("research.Rebreather.name",
                      new BookPageItem("research.Rebreather.name", "research.Rebreather.0", true, new ItemStack(REBREATHER)),
                      new BookPageCrafting("", "rebreather")));
                }
                if (Config.enableDragonRoar) {
                    headGoggleFactory.append(new BookEntry("research.DragonRoar.name",
                      new BookPageItem("research.DragonRoar.name", "research.DragonRoar.0", true, new ItemStack(DRAGON_ROAR)),
                      new BookPageCrafting("", "dragonRoar")));
                }
                BookPageRegistry.addSubcategoryToCategory(EXOSUIT_CATEGORY, headGoggleFactory.build());
            }

            {
                BookCategory.Factory backFactory = new BookCategory.Factory("research.ExoBack.name");
                if (Config.enableJetpack) {
                    backFactory.append(new BookEntry("research.Jetpack.name",
                      new BookPageItem("research.Jetpack.name", "research.Jetpack.0", true, new ItemStack(JETPACK)),
                      new BookPageCrafting("", "jetpack1", "jetpack2")));
                }
                if (Config.enableWings) {
                    backFactory.append(new BookEntry("research.Wings.name",
                      new BookPageItem("research.Wings.name", "research.Wings.0", true, new ItemStack(WINGS)),
                      new BookPageCrafting("", "wings1", "wings2")));
                }
                BookPageRegistry.addSubcategoryToCategory(EXOSUIT_CATEGORY, backFactory.build());
            }

            {
                BookCategory.Factory armFactory = new BookCategory.Factory("research.ExoArm.name");
                if (Config.enablePowerFist) {
                    armFactory.append(new BookEntry("research.Fist.name",
                      new BookPageItem("research.Fist.name", "research.Fist.0", true, new ItemStack(POWER_FIST)),
                      new BookPageCrafting("", "powerFist1", "powerFist2")));
                }
                if (Config.enableExtendoFist) {
                    armFactory.append(new BookEntry("research.ExtendoFist.name",
                      new BookPageItem("research.ExtendoFist.name", "research.ExtendoFist.0", true, new ItemStack(EXTENDO_FIST)),
                      new BookPageCrafting("", "extendoFist1", "extendoFist2")));
                }
                if (Config.enablePitonDeployer) {
                    armFactory.append(new BookEntry("research.PitonDeployer.name",
                      new BookPageItem("research.PitonDeployer.name", "research.PitonDeployer.0", true, new ItemStack(PITON_DEPLOYER)),
                      new BookPageCrafting("", "pitonDeployer")));
                }
                if (Config.enablePistonPush) {
                    armFactory.append(new BookEntry("research.PistonPush.name",
                      new BookPageItem("research.PistonPush.name", "research.PistonPush.0", true, new ItemStack(PISTON_PUSH)),
                      new BookPageCrafting("", "pistonPush")));
                }
                BookPageRegistry.addSubcategoryToCategory(EXOSUIT_CATEGORY, armFactory.build());
            }

            {
                BookCategory.Factory hipFactory = new BookCategory.Factory("research.ExoHip.name");
                if (Config.enableThrusters) {
                    hipFactory.append(new BookEntry("research.Thrusters.name",
                      new BookPageItem("research.Thrusters.name", "research.Thrusters.0", true, new ItemStack(THRUSTERS)),
                      new BookPageCrafting("", "thrusters1", "thrusters2")));
                }
                if (Config.enableCanningMachine) {
                    hipFactory.append(new BookEntry("research.Canner.name",
                      new BookPageItem("research.Canner.name", "research.Canner.0", true, new ItemStack(CANNING_MACHINE)),
                      new BookPageCrafting("", "canner1", "canner2", "canner3", "canner4")));
                }
                if (Config.enableReloadingHolsters) {
                    hipFactory.append(new BookEntry("research.ReloadingHolsters.name",
                      new BookPageItem("research.ReloadingHolsters.name", "research.ReloadingHolsters.0", true, new ItemStack(RELOADING_HOLSTERS)),
                      new BookPageCrafting("", "reloadingHolsters")));
                }
                BookPageRegistry.addSubcategoryToCategory(EXOSUIT_CATEGORY, hipFactory.build());
            }

            {
                BookCategory.Factory legFactory = new BookCategory.Factory("research.ExoLeg.name");
                if (Config.enableRunAssist) {
                    legFactory.append(new BookEntry("research.RunAssist.name",
                      new BookPageItem("research.RunAssist.name", "research.RunAssist.0", true, new ItemStack(RUN_ASSIST)),
                      new BookPageCrafting("", "runAssist1", "runAssist2")));
                }
                if (Config.enableStealthUpgrade) {
                    legFactory.append(new BookEntry("research.StealthUpgrade.name",
                      new BookPageItem("research.StealthUpgrade.name", "research.StealthUpgrade.0", true, new ItemStack(STEALTH)),
                      new BookPageCrafting("", "stealthUpgrade")));
                }
                BookPageRegistry.addSubcategoryToCategory(EXOSUIT_CATEGORY, legFactory.build());
            }

            {
                BookCategory.Factory heelFactory = new BookCategory.Factory("research.ExoHell.name");
                if (Config.enableFallAssist) {
                    heelFactory.append(new BookEntry("research.FallAssist.name",
                      new BookPageItem("research.FallAssist.name", "research.FallAssist.0", true, new ItemStack(FALL_ASSIST)),
                      new BookPageCrafting("", "noFall")));
                }
                if (Config.enableAnchorHeels) {
                    boolean lead = Config.enableLeadPlate &&
                      !OreDictionary.getOres(INGOT_LEAD).isEmpty() && !Config.enableAnchorAnvilRecipe;
                    String desc = lead ? "research.AnchorHeelsLead.0" : "research.AnchorHeelsIron.0";
                    heelFactory.append(new BookEntry("research.AnchorHeels.name",
                      new BookPageItem("research.AnchorHeels.name", desc, true, new ItemStack(ANCHOR_HEELS)),
                      new BookPageCrafting("", "anchorHeels")));
                }
                BookPageRegistry.addSubcategoryToCategory(EXOSUIT_CATEGORY, heelFactory.build());
            }

            {
                BookCategory.Factory footFactory = new BookCategory.Factory("research.ExoFoot.name");
                if (Config.enableDoubleJump) {
                    footFactory.append(new BookEntry("research.DoubleJump.name",
                      new BookPageItem("research.DoubleJump.name", "research.DoubleJump.0", true, new ItemStack(DOUBLE_JUMP)),
                      new BookPageCrafting("", "doubleJump1", "doubleJump2")));
                }
                if (Config.enableHydrophobic) {
                    footFactory.append(new BookEntry("research.Hydrophobic.name",
                      new BookPageItem("research.Hydrophobic.name", "research.Hydrophobic.0", true, new ItemStack(HYDROPHOBIC_COATINGS)),
                      new BookPageCrafting("", "hydrophobic")));
                }
                if (Config.enablePyrophobic) {
                    footFactory.append(new BookEntry("research.Pyrophobic.name",
                      new BookPageItem("research.Pyrophobic.name", "research.Pyrophobic.0", true, new ItemStack(PYROPHOBIC_COATINGS)),
                      new BookPageCrafting("", "pyrophobic")));
                }
                if (Config.enableJumpAssist) {
                    footFactory.append(new BookEntry("research.JumpAssist.name",
                      new BookPageItem("research.JumpAssist.name", "research.JumpAssist.0", true, new ItemStack(JUMP_ASSIST)),
                      new BookPageCrafting("", "jumpAssist1", "jumpAssist2")));
                }
                BookPageRegistry.addSubcategoryToCategory(EXOSUIT_CATEGORY, footFactory.build());
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void preInitClient() {
        ModelLoaderRegistry.registerLoader(new ExosuitItemModelLoader());
        toRegisterNormally.forEach(this::registerModel);
        for (int i = 0; i < MAX_PLATE_META; i++) {
            registerModelItemStack(plateStack(i));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initClient() {
        ItemColors colors = Minecraft.getMinecraft().getItemColors();
        colors.registerItemColorHandler(new ItemExosuitColorHandler(), EXO_BOOTS);
        colors.registerItemColorHandler(new ItemExosuitColorHandler(), EXO_CHEST);
        colors.registerItemColorHandler(new ItemExosuitColorHandler(), EXO_HEAD);
        colors.registerItemColorHandler(new ItemExosuitColorHandler(), EXO_LEGS);
        MinecraftForge.EVENT_BUS.register(ExosuitModelCache.INSTANCE);
    }

    public static ItemStack plateStack(int meta, int size) {
        return new ItemStack(EXOSUIT_PLATE, size, meta);
    }

    public static ItemStack plateStack(int meta) {
        return plateStack(meta, 1);
    }
}
