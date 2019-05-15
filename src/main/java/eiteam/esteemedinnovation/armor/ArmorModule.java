package eiteam.esteemedinnovation.armor;

import eiteam.esteemedinnovation.api.Constants;
import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.api.crucible.CrucibleLiquid;
import eiteam.esteemedinnovation.api.crucible.CrucibleRegistry;
import eiteam.esteemedinnovation.api.exosuit.ExosuitPlate;
import eiteam.esteemedinnovation.api.exosuit.ExosuitRegistry;
import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.armor.exosuit.leather.ItemLeatherExosuitArmor;
import eiteam.esteemedinnovation.armor.exosuit.leather.LeatherExosuitItemModel;
import eiteam.esteemedinnovation.armor.exosuit.leather.LeatherExosuitModelCache;
import eiteam.esteemedinnovation.armor.exosuit.plates.*;
import eiteam.esteemedinnovation.armor.exosuit.steam.ItemSteamExosuitArmor;
import eiteam.esteemedinnovation.armor.exosuit.steam.ItemSteamExosuitColorHandler;
import eiteam.esteemedinnovation.armor.exosuit.steam.SteamExosuitItemModel;
import eiteam.esteemedinnovation.armor.exosuit.steam.SteamExosuitModelCache;
import eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.*;
import eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.anchors.ItemAnchorHeelsUpgrade;
import eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.frequency.AnimalData;
import eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.frequency.AnimalDataStorage;
import eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.frequency.ItemFrequencyShifterUpgrade;
import eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.jetpack.ItemJetpackUpgrade;
import eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.phobiccoatings.ItemHydrophobicCoatingUpgrade;
import eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.phobiccoatings.ItemPyrophobicCoatingUpgrade;
import eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.pulsenozzle.*;
import eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.reloading.ItemReloadingHolsterUpgrade;
import eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.roar.ItemDragonRoarUpgrade;
import eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.thrusters.ItemSidepackUpgrade;
import eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.wings.ItemWingsUpgrade;
import eiteam.esteemedinnovation.armor.tophat.ItemTophat;
import eiteam.esteemedinnovation.armor.tophat.VillagerData;
import eiteam.esteemedinnovation.armor.tophat.VillagerDataStorage;
import eiteam.esteemedinnovation.commons.init.ConfigurableModule;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import eiteam.esteemedinnovation.commons.util.RecipeUtility;
import eiteam.esteemedinnovation.commons.visual.GenericModelLoaderLocationMatch;
import eiteam.esteemedinnovation.engineeringtable.EngineeringTableModule;
import eiteam.esteemedinnovation.storage.steam.ItemTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static eiteam.esteemedinnovation.commons.Config.*;
import static eiteam.esteemedinnovation.commons.EsteemedInnovation.*;
import static eiteam.esteemedinnovation.commons.OreDictEntries.*;
import static eiteam.esteemedinnovation.firearms.FirearmModule.*;
import static eiteam.esteemedinnovation.heater.HeaterModule.STEAM_HEATER;
import static eiteam.esteemedinnovation.materials.MaterialsModule.*;
import static eiteam.esteemedinnovation.misc.ItemCraftingComponent.Types.BRASS_PISTON;
import static eiteam.esteemedinnovation.misc.MiscellaneousModule.COMPONENT;
import static eiteam.esteemedinnovation.steamsafety.SafetyModule.STEAM_GAUGE;
import static eiteam.esteemedinnovation.storage.StorageModule.STEAM_TANK;
import static eiteam.esteemedinnovation.storage.StorageModule.enableCanister;
import static eiteam.esteemedinnovation.transport.TransportationModule.*;
import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;

public class ArmorModule extends ContentModule implements ConfigurableModule {
    public static final ItemArmor.ArmorMaterial STEAM_EXO_MAT = EnumHelper.addArmorMaterial("STEAMEXOSUIT", MOD_ID + ":steam_exo", 15, new int[] {2, 5, 4, 1}, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0F);
    @CapabilityInject(AnimalData.class)
    public static final Capability<AnimalData> ANIMAL_DATA = null;
    @CapabilityInject(VillagerData.class)
    public static final Capability<VillagerData> VILLAGER_DATA = null;
    public static final float fallAssistDivisor = 2;
    public static final float extendedRange = 2.0F; //Range extension in blocks
    private static final int DRAGON_ROAR_CONSUMPTION_DEFAULT = 20000;
    private static final int RELOADING_CONSUMPTION_DEFAULT = 15;
    private static final int PISTON_PUSH_CONSUMPTION_DEFAULT = 5;
    private static final int HYDROPHOBIC_CONSUMPTION_DEFAULT = 10;
    private static final int PYROPHOBIC_CONSUMPTION_DEFAULT = 5;
    private static final int REBREATHER_CONSUMPTION_DEFAULT = 5;
    private static final int ZINC_PLATE_CONSUMPTION_DEFAULT = 30;
    private static final int UBER_REINFORCED_TANK_CAPACITY_DEFAULT = 144000;
    private static final int REINFORCED_TANK_CAPACITY_DEFAULT = 72000;
    private static final int EXO_CONSUMPTION_DEFAULT = 5;
    private static final int RUN_ASSIST_CONSUMPTION_DEFAULT = 5;
    private static final int THRUSTER_CONSUMPTION_DEFAULT = 5;
    private static final int POWER_FIST_CONSUMPTION_DEFAULT = 5;
    private static final int JUMP_BOOST_CONSUMPTION_SHIFT_BOOST_DEFAULT = 10;
    private static final int JETPACK_CONSUMPTION_DEFAULT = 10;
    private static final int JUMP_BOOST_CONSUMPTION_DEFAULT = 10;

    public static ItemSteamExosuitArmor STEAM_EXO_HEAD;
    public static ItemSteamExosuitArmor STEAM_EXO_CHEST;
    public static ItemSteamExosuitArmor STEAM_EXO_LEGS;
    public static ItemSteamExosuitArmor STEAM_EXO_BOOTS;

    // Most values taken from LEATHER ArmorMaterial enum value
    public static final ItemArmor.ArmorMaterial LEATHER_EXO_MAT = EnumHelper.addArmorMaterial(
      "LEATHEREXOSUIT", "", 5, new int[]{1, 2, 3, 1},
      0, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0F
    );
    public static ItemLeatherExosuitArmor LEATHER_EXO_HEAD;
    public static ItemLeatherExosuitArmor LEATHER_EXO_CHEST;
    public static ItemLeatherExosuitArmor LEATHER_EXO_LEGS;
    public static ItemLeatherExosuitArmor LEATHER_EXO_BOOTS;

    public static Item JETPACK;
    public static Item WINGS;
    public static Item POWER_FIST;
    public static Item EXTENDO_FIST;
    public static Item THRUSTERS;
    public static Item FALL_ASSIST;
    public static Item LEAP_ACTUATOR;
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
    public static ExosuitPlate GOLD_PLATE = new ExosuitPlateGold();
    public static final int COPPER_PLATE_META = 2;
    public static ExosuitPlate COPPER_PLATE = new ExosuitPlateCopper();
    public static final int ZINC_PLATE_META = 3;
    public static ExosuitPlate ZINC_PLATE = new ExosuitPlateZinc();
    public static final int BRASS_PLATE_META = 4;
    public static ExosuitPlate BRASS_PLATE = new ExosuitPlateBrass();
    public static final int GILDED_IRON_PLATE_META = 5;
    public static ExosuitPlate GILDED_IRON_PLATE = new ExosuitPlateGildedIron();
    public static final int LEAD_PLATE_META = 6;
    public static ExosuitPlate LEAD_PLATE = new ExosuitPlateLead();
    public static final int MAX_PLATE_META = LEAD_PLATE_META;

    public static final ItemArmor.ArmorMaterial GILDED_MAT = EnumHelper.addArmorMaterial("GILDEDGOLD", "minecraft:gold", 15, new int[] {2, 6, 5, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0F);
    public static Item GILDED_HEAD;
    public static Item GILDED_CHEST;
    public static Item GILDED_LEGS;
    public static Item GILDED_BOOTS;

    public static final ItemArmor.ArmorMaterial BRASS_MAT = EnumHelper.addArmorMaterial("BRASS", MOD_ID + ":brass", 11, new int[] {2, 7, 6, 3}, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0F);
    public static Item BRASS_HEAD;
    public static Item BRASS_CHEST;
    public static Item BRASS_LEGS;
    public static Item BRASS_BOOTS;

    public static final ItemArmor.ArmorMaterial MONOCLE_MAT = EnumHelper.addArmorMaterial("MONOCLE", MOD_ID + ":monocle", 5, new int[] {1, 3, 2, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0F);
    public static Item MONOCLE;
    public static Item GOGGLES;
    public static Item TOP_HAT;
    public static Item ENTREPRENEUR_TOP_HAT;

    @SideOnly(Side.CLIENT)
    public static KeyBinding MONOCLE_KEY;

    private static boolean enableWings;
    private static boolean enableGildedIronPlate;
    private static boolean enableEnderiumPlate;
    private static boolean enableVibrantPlate;
    private static boolean enableSadistPlate;
    private static boolean enableFieryPlate;
    private static boolean enableYetiPlate;
    private static boolean enableTerrasteelPlate;
    private static boolean enableElementiumPlate;
    private static boolean enableThaumiumPlate;
    private static boolean enableLeadPlate;
    private static boolean enableZincPlate;
    private static boolean enableBrassPlate;
    private static boolean enableGoldPlate;
    private static boolean enableIronPlate;
    private static boolean enableCopperPlate;
    private static boolean enableDragonRoar;
    private static boolean enableFrequencyShifter;
    private static boolean enableReloadingHolsters;
    private static boolean enablePistonPush;
    private static boolean enableAnchorHeels;
    private static boolean enablePyrophobic;
    private static boolean enableHydrophobic;
    private static boolean enableRebreather;
    private static boolean enableEnderShroud;
    private static boolean enableUberReinforcedTank;
    private static boolean enableReinforcedTank;
    private static boolean enablePitonDeployer;
    private static boolean enableExtendoFist;
    private static boolean enableCanningMachine;
    private static boolean enableThrusters;
    private static boolean enablePowerFist;
    private static boolean enableJetpack;
    private static boolean enableFallAssist;
    private static boolean enableLeatherExosuit;
    private static boolean enableSteamExosuit;
    private static boolean enableExosuit;
    private static boolean enableStealthUpgrade;
    private static boolean enableRunAssist;
    private static boolean enableJumpAssist;
    private static boolean enableDoubleJump;
    private static boolean enableGoggles;
    private static boolean enableEmeraldHat;
    private static boolean enableTopHat;
    public static int dragonRoarConsumption;
    public static int reloadingConsumption;
    public static int pistonPushConsumption;
    public static int pyrophobicConsumption;
    public static int hydrophobicConsumption;
    public static int rebreatherConsumption;
    public static int zincPlateConsumption;
    public static int powerFistConsumption;
    public static int runAssistConsumption;
    public static int thrusterConsumption;
    public static int jumpBoostConsumptionShiftJump;
    public static int jetpackConsumption;
    public static int jumpBoostConsumption;
    private static int uberReinforcedTankCapacity;
    private static int reinforcedTankCapacity;
    public static int exoConsumption;
    private static boolean enableAnchorAnvilRecipe;
    public static boolean passiveDrain;

    private static Set<Item> toRegisterNormally = new HashSet<>();

    @Override
    public void create(Side side) {
        CapabilityManager.INSTANCE.register(AnimalData.class, new AnimalDataStorage(), AnimalData.DefaultImplementation.class);
        CapabilityManager.INSTANCE.register(VillagerData.class, new VillagerDataStorage(), VillagerData.DefaultImplementation.class);

        channel.registerMessage(DoubleJumpServerActionPacketHandler.class, DoubleJumpServerActionPacket.class, 5, Side.SERVER);
        channel.registerMessage(DoubleJumpClientResponsePacketHandler.class, DoubleJumpClientResponsePacket.class, 6, Side.CLIENT);
    }

    @Override
    public void registerItems(RegistryEvent.Register<Item> event) {
        STEAM_EXO_HEAD = (ItemSteamExosuitArmor) setup(event, new ItemSteamExosuitArmor(EntityEquipmentSlot.HEAD, STEAM_EXO_MAT), "steam_exosuit_head");
        STEAM_EXO_CHEST = (ItemSteamExosuitArmor) setup(event, new ItemSteamExosuitArmor(EntityEquipmentSlot.CHEST, STEAM_EXO_MAT), "steam_exosuit_body");
        STEAM_EXO_LEGS = (ItemSteamExosuitArmor) setup(event, new ItemSteamExosuitArmor(EntityEquipmentSlot.LEGS, STEAM_EXO_MAT), "steam_exosuit_legs");
        STEAM_EXO_BOOTS = (ItemSteamExosuitArmor) setup(event, new ItemSteamExosuitArmor(EntityEquipmentSlot.FEET, STEAM_EXO_MAT), "steam_exosuit_feet");

        LEATHER_EXO_HEAD = (ItemLeatherExosuitArmor) setup(event, new ItemLeatherExosuitArmor(EntityEquipmentSlot.HEAD), "leather_exosuit_head");
        LEATHER_EXO_CHEST = (ItemLeatherExosuitArmor) setup(event, new ItemLeatherExosuitArmor(EntityEquipmentSlot.CHEST), "leather_exosuit_body");
        LEATHER_EXO_LEGS = (ItemLeatherExosuitArmor) setup(event, new ItemLeatherExosuitArmor(EntityEquipmentSlot.LEGS), "leather_exosuit_legs");
        LEATHER_EXO_BOOTS = (ItemLeatherExosuitArmor) setup(event, new ItemLeatherExosuitArmor(EntityEquipmentSlot.FEET), "leather_exosuit_feet");

        JETPACK = setup(event, new ItemJetpackUpgrade(), "jetpack");
        WINGS = setup(event, new ItemWingsUpgrade(), "wings");
        POWER_FIST = setup(event, new ItemPowerFistUpgrade(), "power_fist");
        EXTENDO_FIST = setup(event, new ItemExtendoFistUpgrade(), "extendo_fist");
        THRUSTERS = setup(event, new ItemSidepackUpgrade(), "thrusters");
        FALL_ASSIST = setup(event, new ItemFallAssistUpgrade(), "fall_assist");
        LEAP_ACTUATOR = setup(event, new ItemLeapActuatorUpgrade(), "jump_assist");
        DOUBLE_JUMP = setup(event, new ItemDoubleJumpUpgrade(), "double_jump");
        RUN_ASSIST = setup(event, new ItemModularAcceleratorUpgrade(), "run_assist");
        CANNING_MACHINE = setup(event, new ItemCanningMachineUpgrade(), "canner");
        PITON_DEPLOYER = setup(event, new ItemPitonDeployerUpgrade(), "piton_deployer");
        STEALTH = setup(event, new ItemAcousticDampenerUpgrade(), "stealth_upgrade");
        ENDER_SHROUD = setup(event, new ItemSteamExosuitUpgrade(ExosuitSlot.VANITY, null, null, 0), "ender_shroud");
        REINFORCED_TANK = setup(event, new ItemTank(reinforcedTankCapacity, MOD_ID + ":textures/models/armor/reinforcedTank.png", MOD_ID + ":textures/models/armor/reinforcedTank_grey.png"), "reinforced_tank");
        UBER_REINFORCED_TANK = setup(event, new ItemTank(uberReinforcedTankCapacity, MOD_ID + ":textures/models/armor/uberReinforcedTank.png", MOD_ID + ":textures/models/armor/uberReinforcedTank_grey.png"), "uber_reinforced_tank");
        REBREATHER = setup(event, new ItemRebreatherUpgrade(), "rebreather");
        HYDROPHOBIC_COATINGS = setup(event, new ItemHydrophobicCoatingUpgrade(), "hydrophobic_coatings");
        PYROPHOBIC_COATINGS = setup(event, new ItemPyrophobicCoatingUpgrade(), "pyrophobic_coatings");
        ANCHOR_HEELS = setup(event, new ItemAnchorHeelsUpgrade(), "anchor_heels");
        RELOADING_HOLSTERS = setup(event, new ItemReloadingHolsterUpgrade(), "reloading_holsters");
        FREQUENCY_SHIFTER = setup(event, new ItemFrequencyShifterUpgrade(), "frequency_shifter");
        DRAGON_ROAR = setup(event, new ItemDragonRoarUpgrade(), "dragon_roar");
        EXOSUIT_PLATE = setup(event, new ItemExosuitPlate(), "exosuit_plate", tab, false);
        PISTON_PUSH = setup(event, new ItemPistonPushUpgrade(), "piston_push");

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

        GILDED_HEAD = setup(event, new ItemGenericArmor(GILDED_MAT, 2, EntityEquipmentSlot.HEAD, INGOT_GILDED_IRON, "GildedIron"), "gilded_iron_helmet", tabTools);
        GILDED_CHEST = setup(event, new ItemGenericArmor(GILDED_MAT, 2, EntityEquipmentSlot.CHEST, INGOT_GILDED_IRON, "GildedIron"), "gilded_iron_chestplate", tabTools);
        GILDED_LEGS = setup(event, new ItemGenericArmor(GILDED_MAT, 2, EntityEquipmentSlot.LEGS, INGOT_GILDED_IRON, "GildedIron"), "gilded_iron_leggings", tabTools);
        GILDED_BOOTS = setup(event, new ItemGenericArmor(GILDED_MAT, 2, EntityEquipmentSlot.FEET, INGOT_GILDED_IRON, "GildedIron"), "gilded_iron_boots", tabTools);

        BRASS_HEAD = setup(event, new ItemGenericArmor(BRASS_MAT, 2, EntityEquipmentSlot.HEAD, INGOT_BRASS, "Brass"), "brass_helmet", tabTools);
        BRASS_CHEST = setup(event, new ItemGenericArmor(BRASS_MAT, 2, EntityEquipmentSlot.CHEST, INGOT_BRASS, "Brass"), "brass_chestplate", tabTools);
        BRASS_LEGS = setup(event, new ItemGenericArmor(BRASS_MAT, 2, EntityEquipmentSlot.LEGS, INGOT_BRASS, "Brass"), "brass_leggings", tabTools);
        BRASS_BOOTS = setup(event, new ItemGenericArmor(BRASS_MAT, 2, EntityEquipmentSlot.FEET, INGOT_BRASS, "Brass"), "brass_boots", tabTools);

        MONOCLE = setup(event, new ItemGoggles(MONOCLE_MAT, 2, EntityEquipmentSlot.HEAD, LEATHER, "Monocle"), "monocle", tabTools);
        GOGGLES = setup(event, new ItemGoggles(MONOCLE_MAT, 2, EntityEquipmentSlot.HEAD, LEATHER, "Goggles"), "goggles", tabTools);
        TOP_HAT = setup(event, new ItemTophat(MONOCLE_MAT, 2, EntityEquipmentSlot.HEAD, false), "tophat_no_emerald", tabTools);
        ENTREPRENEUR_TOP_HAT = setup(event, new ItemTophat(MONOCLE_MAT, 2, EntityEquipmentSlot.HEAD, true), "tophat", tabTools);

    }

    @Override
    protected Item setup(RegistryEvent.Register<Item> event, Item startingItem, String path, CreativeTabs tab) {
        return setup(event, startingItem, path, tab, true);
    }

    private Item setup(RegistryEvent.Register<Item> event, Item startingItem, String path, CreativeTabs tab, boolean normal) {
        startingItem = super.setup(event, startingItem, path, tab);
        if (normal) {
            toRegisterNormally.add(startingItem);
        }
        return startingItem;
    }

    public static String resource(@Nonnull String base) {
        return Constants.EI_MODID + ":textures/models/armor/" + base + ".png";
    }

    @Override
    public void recipes(RegistryEvent.Register<IRecipe> event) {
        if (enableGoggles) {
            RecipeUtility.addRecipe(event, true, "monocle1", MONOCLE,
              " l ",
              "l l",
              "btb",
              'b', INGOT_BRASS,
              'l', LEATHER_ORE,
              't', SPYGLASS
            );
            RecipeUtility.addRecipe(event, true, "monocle2", MONOCLE,
              " l ",
              "l l",
              "btb",
              'b', PLATE_THIN_BRASS,
              'l', LEATHER_ORE,
              't', SPYGLASS
            );
            RecipeUtility.addRecipe(event, true, "goggles1", GOGGLES,
              " l ",
              "l l",
              "tbg",
              'b', INGOT_BRASS,
              'l', LEATHER_ORE,
              't', SPYGLASS,
              'g', BLOCK_GLASS_COLORLESS
            );
            RecipeUtility.addRecipe(event, true, "goggles2", GOGGLES,
              " l ",
              "l l",
              "tbg",
              'b', PLATE_THIN_BRASS,
              'l', LEATHER_ORE,
              't', SPYGLASS,
              'g', BLOCK_GLASS_COLORLESS
            );
        }
        if (enableTopHat) {
            RecipeUtility.addRecipe(event, true, "hat", TOP_HAT,
              " l ",
              " l ",
              "lll",
              'l', new ItemStack(WOOL, 1, EnumDyeColor.BLACK.getMetadata())
            );
            if (enableEmeraldHat) {
                RecipeUtility.addShapelessRecipe(event, true, "hatEmerald",
                  ENTREPRENEUR_TOP_HAT, TOP_HAT, BLOCK_EMERALD);
            }
        }
        if (enableExosuit) {
            if (enableSteamExosuit) {
                RecipeUtility.addRecipe(event, true, "steamExoHead", STEAM_EXO_HEAD,
                  "xyx",
                  "p p",
                  "xyx",
                  'x', PLATE_THIN_BRASS,
                  'y', NUGGET_BRASS,
                  'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata())
                );
                RecipeUtility.addRecipe(event, true, "steamExoBody",
                  new ItemStack(STEAM_EXO_CHEST, 1, STEAM_EXO_CHEST.getMaxDamage() - 1),
                  "p p",
                  "ygy",
                  "xxx",
                  'x', PLATE_THIN_BRASS,
                  'y', NUGGET_BRASS,
                  'g', STEAM_GAUGE,
                  'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata())
                );
                RecipeUtility.addRecipe(event, true, "steamExoLegs", STEAM_EXO_LEGS,
                  "yxy",
                  "p p",
                  "x x",
                  'x', PLATE_THIN_BRASS,
                  'y', NUGGET_BRASS,
                  'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata())
                );
                RecipeUtility.addRecipe(event, true, "steamExoFeet", STEAM_EXO_BOOTS,
                  "p p",
                  "x x",
                  'x', PLATE_THIN_BRASS,
                  'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata())
                );

                if (enableJetpack) {
                    RecipeUtility.addRecipe(event, true, "jetpack1", JETPACK,
                      "p p",
                      "ptg",
                      "p p",
                      'p', BRASS_PIPE,
                      'g', STEAM_GAUGE,
                      't', INGOT_BRASS
                    );
                    RecipeUtility.addRecipe(event, true, "jetpack2", JETPACK,
                      "p p",
                      "ptg",
                      "p p",
                      'p', BRASS_PIPE,
                      'g', STEAM_GAUGE,
                      't', PLATE_THIN_BRASS
                    );
                }
                if (enableWings) {
                    RecipeUtility.addRecipe(event, true, "wings1", WINGS,
                      "xxx",
                      "ccc",
                      "c c",
                      'x', INGOT_BRASS,
                      'c', PLATE_THIN_COPPER
                    );
                    RecipeUtility.addRecipe(event, true, "wings2", WINGS,
                      "xxx",
                      "ccc",
                      "c c",
                      'x', PLATE_THIN_BRASS,
                      'c', PLATE_THIN_COPPER
                    );
                }
                if (enablePowerFist) {
                    RecipeUtility.addRecipe(event, true, "powerFist1", POWER_FIST,
                      "b i",
                      "bpi",
                      "b i",
                      'i', NUGGET_IRON,
                      'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
                      'b', NUGGET_BRASS
                    );
                    RecipeUtility.addRecipe(event, true, "powerFist2", POWER_FIST,
                      "b i",
                      "bpi",
                      "b i",
                      'i', PLATE_THIN_IRON,
                      'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
                      'b', NUGGET_BRASS
                    );
                }
                if (enableExtendoFist) {
                    RecipeUtility.addRecipe(event, true, "extendoFist1", EXTENDO_FIST,
                      " ii",
                      "bbi",
                      "bb ",
                      'i', INGOT_IRON,
                      'b', NUGGET_BRASS
                    );
                    RecipeUtility.addRecipe(event, true, "extendoFist2", EXTENDO_FIST,
                      " ii",
                      "bbi",
                      "bb ",
                      'i', PLATE_THIN_IRON,
                      'b', NUGGET_BRASS
                    );
                }
                if (enableThrusters) {
                    RecipeUtility.addRecipe(event, true, "thrusters1", THRUSTERS,
                      "tnt",
                      "ptp",
                      "tnt",
                      'p', BRASS_PIPE,
                      't', INGOT_BRASS,
                      'n', NUGGET_BRASS
                    );
                    RecipeUtility.addRecipe(event, true, "thrusters2", THRUSTERS,
                      "tnt",
                      "ptp",
                      "tnt",
                      'p', BRASS_PIPE,
                      't', PLATE_THIN_BRASS,
                      'n', NUGGET_BRASS
                    );
                }
                if (enableFallAssist) {
                    RecipeUtility.addRecipe(event, true, "noFall", FALL_ASSIST,
                      "pbp",
                      "sss",
                      'b', LEATHER_BOOTS,
                      'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
                      's', SLIMEBALL_ORE
                    );
                }
                if (enableJumpAssist) {
                    RecipeUtility.addRecipe(event, true, "jumpAssist1", LEAP_ACTUATOR,
                      "s s",
                      "pbp",
                      "s s",
                      'b', LEATHER_BOOTS,
                      'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
                      's', INGOT_BRASS
                    );
                    RecipeUtility.addRecipe(event, true, "jumpAssist2", LEAP_ACTUATOR,
                      "s s",
                      "pbp",
                      "s s",
                      'b', LEATHER_BOOTS,
                      'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
                      's', PLATE_THIN_BRASS
                    );
                }
                if (enableRunAssist) {
                    RecipeUtility.addRecipe(event, true, "runAssist1", RUN_ASSIST,
                      "p p",
                      "s s",
                      "p p",
                      'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
                      's', INGOT_BRASS
                    );
                    RecipeUtility.addRecipe(event, true, "runAssist2", RUN_ASSIST,
                      "p p",
                      "s s",
                      "p p",
                      'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
                      's', PLATE_THIN_BRASS
                    );
                }
                if (enableDoubleJump) {
                    RecipeUtility.addRecipe(event, true, "doubleJump1", DOUBLE_JUMP,
                      "s s",
                      "v v",
                      'v', VALVE_PIPE,
                      's', INGOT_BRASS
                    );
                    RecipeUtility.addRecipe(event, true, "doubleJump2", DOUBLE_JUMP,
                      "s s",
                      "v v",
                      'v', VALVE_PIPE,
                      's', PLATE_THIN_BRASS
                    );
                }
                if (enableCanningMachine && enableCanister) {
                    RecipeUtility.addRecipe(event, true, "canner1", CANNING_MACHINE,
                      "bbn",
                      "p p",
                      "i i",
                      'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
                      'b', INGOT_BRASS,
                      'n', NUGGET_IRON,
                      'i', INGOT_IRON
                    );
                    RecipeUtility.addRecipe(event, true, "canner2", CANNING_MACHINE,
                      "bbn",
                      "p p",
                      "i i",
                      'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
                      'b', PLATE_THIN_BRASS,
                      'n', NUGGET_IRON,
                      'i', PLATE_THIN_IRON
                    );
                    RecipeUtility.addRecipe(event, true, "canner3", CANNING_MACHINE,
                      "bbn",
                      "p p",
                      "i i",
                      'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
                      'b', INGOT_BRASS,
                      'n', NUGGET_IRON,
                      'i', INGOT_IRON
                    );
                    RecipeUtility.addRecipe(event, true, "canner4", CANNING_MACHINE,
                      "bbn",
                      "p p",
                      "i i",
                      'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
                      'b', PLATE_THIN_BRASS,
                      'n', NUGGET_IRON,
                      'i', PLATE_THIN_IRON
                    );
                }
                if (enablePitonDeployer) {
                    RecipeUtility.addRecipe(event, true, "pitonDeployer", PITON_DEPLOYER,
                      " i ",
                      "lli",
                      "ll ",
                      'l', LEATHER,
                      'i', NUGGET_IRON
                    );
                }
                if (enableStealthUpgrade) {
                    RecipeUtility.addRecipe(event, true, "stealthUpgrade", STEALTH,
                      "lwl",
                      "lsl",
                      "lwl",
                      'l', LEATHER,
                      'w', WOOL,
                      's', STRING
                    );
                }
                if (enableEnderShroud) {
                    RecipeUtility.addRecipe(event, true, "enderShroud", ENDER_SHROUD,
                      " g ",
                      "geg",
                      " g ",
                      'g', GLASS,
                      'e', ENDER_PEARL
                    );
                }
                if (enableReinforcedTank) {
                    RecipeUtility.addRecipe(event, true, "reinforcedTank1", REINFORCED_TANK,
                      "ppp",
                      "tpt",
                      "ppp",
                      't', STEAM_TANK,
                      'p', INGOT_BRASS
                    );
                    RecipeUtility.addRecipe(event, true, "reinforcedTank2", REINFORCED_TANK,
                      "ppp",
                      "tpt",
                      "ppp",
                      't', STEAM_TANK,
                      'p', PLATE_THIN_BRASS
                    );
                    if (enableUberReinforcedTank) {
                        RecipeUtility.addRecipe(event, true, "uberReinforcedTank1", UBER_REINFORCED_TANK,
                          "ppp",
                          "tbt",
                          "ppp",
                          't', REINFORCED_TANK,
                          'p', INGOT_BRASS,
                          'b', BLOCK_BRASS
                        );
                        RecipeUtility.addRecipe(event, true, "uberReinforcedTank2", UBER_REINFORCED_TANK,
                          "ppp",
                          "tbt",
                          "ppp",
                          't', REINFORCED_TANK,
                          'p', PLATE_THIN_BRASS,
                          'b', BLOCK_BRASS
                        );
                    }
                }
                if (enableRebreather) {
                    RecipeUtility.addRecipe(event, true, "rebreather",
                      REBREATHER,
                      " l ",
                      "ptp",
                      " c ",
                      'l', LEATHER,
                      'p', VALVE_PIPE,
                      't', STEAM_TANK,
                      'c', new ItemStack(CARPET, 1, OreDictionary.WILDCARD_VALUE)
                    );
                }
                if (enableHydrophobic) {
                    RecipeUtility.addRecipe(event, true, "hydrophobic",
                      HYDROPHOBIC_COATINGS,
                      "zbz",
                      "hzh",
                      "sss",
                      'z', PLATE_THIN_ZINC,
                      'b', LEATHER_BOOTS,
                      'h', STEAM_HEATER,
                      's', SLIMEBALL_ORE
                    );
                }
                if (enablePyrophobic) {
                    RecipeUtility.addRecipe(event, true, "pyrophobic",
                      PYROPHOBIC_COATINGS,
                      "cbc",
                      "hch",
                      "mmm",
                      'c', PLATE_THIN_COPPER,
                      'b', LEATHER_BOOTS,
                      'h', STEAM_HEATER,
                      'm', MAGMA_CREAM
                    );
                }
                if (enableAnchorHeels) {
                    if (enableLeadPlate && !OreDictionary.getOres(INGOT_LEAD).isEmpty() &&
                      !enableAnchorAnvilRecipe) {
                        RecipeUtility.addRecipe(event, true, "anchorHeels",
                          new ItemStack(ANCHOR_HEELS),
                          "p p",
                          "e e",
                          'p', PLATE_THIN_LEAD,
                          'e', plateStack(LEAD_PLATE_META)
                        );
                    } else {
                        RecipeUtility.addRecipe(event, true, "anchorHeels",
                          new ItemStack(ANCHOR_HEELS),
                          "p p",
                          "eae",
                          'p', PLATE_THIN_IRON,
                          'e', plateStack(IRON_PLATE_META),
                          'a', ANVIL
                        );
                    }
                }
                if (enablePistonPush) {
                    RecipeUtility.addRecipe(event, true, "pistonPush",
                      PISTON_PUSH,
                      "n p",
                      "nbp",
                      "n p",
                      'n', NUGGET_BRASS,
                      'p', PISTON,
                      'b', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata())
                    );
                }
                if (enableReloadingHolsters && enableFirearms && enableEnhancementRevolver) {
                    RecipeUtility.addRecipe(event, true, "reloadingHolsters",
                      RELOADING_HOLSTERS,
                      "lbl",
                      "c c",
                      "p p",
                      'l', LEATHER,
                      'b', PLATE_THIN_BRASS,
                      'c', REVOLVER_CHAMBER,
                      'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata())
                    );
                }
                if (enableFrequencyShifter) {
                    RecipeUtility.addRecipe(event, true, "frequencyShifter",
                      FREQUENCY_SHIFTER,
                      "t n",
                      "pjp",
                      " p ",
                      't', REDSTONE_TORCH,
                      'n', NUGGET_BRASS,
                      'p', PLATE_THIN_BRASS,
                      'j', JUKEBOX
                    );
                }
                if (enableDragonRoar) {
                    RecipeUtility.addRecipe(event, true, "dragonRoar",
                      DRAGON_ROAR,
                      "ccc",
                      "cfc",
                      "cpc",
                      'c', PLATE_THIN_COPPER,
                      'f', FAN,
                      'p', VALVE_PIPE
                    );
                }
            }

            if (enableLeatherExosuit) {
                RecipeUtility.addRecipe(event, true, "leatherExoHead", LEATHER_EXO_HEAD,
                  "xyx",
                  "p p",
                  "xyx",
                  'x', PLATE_THIN_BRASS,
                  'y', NUGGET_BRASS,
                  'p', LEATHER_HELMET
                );
                RecipeUtility.addRecipe(event, true, "leatherExoBody", LEATHER_EXO_CHEST,
                  "p p",
                  "ygy",
                  "xxx",
                  'x', PLATE_THIN_BRASS,
                  'y', NUGGET_BRASS,
                  'g', LEATHER_ORE,
                  'p', LEATHER_CHESTPLATE
                );
                RecipeUtility.addRecipe(event, true, "leatherExoLegs", LEATHER_EXO_LEGS,
                  "yxy",
                  "p p",
                  "x x",
                  'x', PLATE_THIN_BRASS,
                  'y', NUGGET_BRASS,
                  'p', LEATHER_LEGGINGS
                );
                RecipeUtility.addRecipe(event, true, "leatherExoFeet", LEATHER_EXO_BOOTS,
                  "p p",
                  "x x",
                  'x', PLATE_THIN_BRASS,
                  'p', LEATHER_BOOTS
                );
            }

            if (enableIronPlate) {
                addExosuitPlateRecipes(event, "exoIron", PLATE_THIN_IRON, plateStack(IRON_PLATE_META), IRON_LIQUID);
            }
            if (enableGoldPlate) {
                addExosuitPlateRecipes(event, "exoGold", PLATE_THIN_GOLD, plateStack(GOLD_PLATE_META), GOLD_LIQUID);
            }
            if (enableCopperPlate) {
                addExosuitPlateRecipes(event, "exoCopper", PLATE_THIN_COPPER, plateStack(COPPER_PLATE_META), COPPER_LIQUID);
            }
            if (enableZincPlate) {
                addExosuitPlateRecipes(event, "exoZinc", PLATE_THIN_ZINC, plateStack(ZINC_PLATE_META), ZINC_LIQUID);
            }
            if (enableBrassPlate) {
                addExosuitPlateRecipes(event, "exoBrass", PLATE_THIN_BRASS, plateStack(BRASS_PLATE_META), BRASS_LIQUID);
            }
            if (enableGildedIronPlate) {
                addExosuitPlateRecipes(event, "exoGildedIron", PLATE_THIN_GILDED_IRON, plateStack(GILDED_IRON_PLATE_META));
            }
            if (enableLeadPlate) {
                addExosuitPlateRecipes(event, "exoLead", PLATE_THIN_LEAD, plateStack(LEAD_PLATE_META), LEAD_LIQUID);
            }
        }

        addHelmetRecipe(event, GILDED_HEAD, INGOT_GILDED_IRON);
        addHelmetRecipe(event, BRASS_HEAD, INGOT_BRASS);
        addChestRecipe(event, GILDED_CHEST, INGOT_GILDED_IRON);
        addChestRecipe(event, BRASS_CHEST, INGOT_BRASS);
        addLegRecipe(event, GILDED_LEGS, INGOT_GILDED_IRON);
        addLegRecipe(event, BRASS_LEGS, INGOT_BRASS);
        addFootRecipe(event, GILDED_BOOTS, INGOT_GILDED_IRON);
        addFootRecipe(event, BRASS_BOOTS, INGOT_BRASS);
    }


    private static void addLegRecipe(RegistryEvent.Register<IRecipe> event, Item item, String ore) {
        RecipeUtility.addRecipe(event, false, ore + "leg", item,
          "xxx",
          "x x",
          "x x",
          'x', ore
        );
    }

    private static void addHelmetRecipe(RegistryEvent.Register<IRecipe> event, Item item, String ore) {
        RecipeUtility.addRecipe(event, false, ore + "helmet", item,
          "xxx",
          "x x",
          'x', ore
        );
    }

    private static void addChestRecipe(RegistryEvent.Register<IRecipe> event, Item item, String ore) {
        RecipeUtility.addRecipe(event, false, ore + "chest", item,
          "x x",
          "xxx",
          "xxx",
          'x', ore
        );
    }

    private static void addFootRecipe(RegistryEvent.Register<IRecipe> event, Item item, String ore) {
        RecipeUtility.addRecipe(event, false, ore + "foot", item,
          "x x",
          "x x",
          'x', ore
        );
    }


    /**
     * Adds exosuit plate recipe (2x2 of ingredient), and a melting recipe for the itemstack.
     * @param str The name of the recipe for the book.
     * @param ingredient The ingredient, either an ItemStack or an OreDict string.
     * @param plate The output plate.
     */
    private static void addExosuitPlateRecipes(RegistryEvent.Register<IRecipe> event, String str, Object ingredient, ItemStack plate, CrucibleLiquid liq) {
        addExosuitPlateRecipes(event, str, ingredient, plate);
        CrucibleRegistry.registerMeltRecipe(plate.getItem(), plate.getItemDamage(), liq, 24);
    }

    /**
     * Adds an exosuit plate recipe (2x2 of ingredient), without a melting recipe for the itemstack.
     * @param str The name of the recipe for the book.
     * @param ingredient The ingredient, either an ItemStack or an OreDict string
     * @param plate The output plate.
     */
    private static void addExosuitPlateRecipes(RegistryEvent.Register<IRecipe> event, String str, Object ingredient, ItemStack plate) {
        RecipeUtility.addRecipe(event, true, str, plate, "xx", "xx", 'x', ingredient);
        if (ingredient instanceof ItemStack) {
            ItemStack stack = ((ItemStack) ingredient).copy();
            stack.setCount(4);
            RecipeUtility.addShapelessRecipe(event, false, stack.getItem().getRegistryName().getPath()
              + plate.getItem().getRegistryName().getPath(), stack, plate);
        } else if (ingredient instanceof String) {
            for (ItemStack ore : OreDictionary.getOres((String) ingredient)) {
                ItemStack stack = ore.copy();
                stack.setCount(4);
                RecipeUtility.addShapelessRecipe(event, false, stack.getItem().getRegistryName().getPath()
                  + plate.getItem().getRegistryName().getPath(), stack, plate);
            }
        }
    }


    @Override
    public void finish(Side side) {
        if (enableTopHat) {
            BookPageRegistry.addCategoryToSection(GADGET_SECTION, 4,
              new BookCategory("category.TopHat.name",
                new BookEntry("research.TopHat.name",
                  new BookPageItem("research.TopHat.name", "research.TopHat.0", new ItemStack(TOP_HAT)),
                  new BookPageCrafting("", "hat")
                )
              )
            );
            if (enableEmeraldHat) {
                BookPageRegistry.addCategoryToSection(GADGET_SECTION, 5,
                  new BookCategory("category.TopHatEmerald.name",
                    new BookEntry("research.TopHatEmerald.name",
                      new BookPageItem("research.TopHatEmerald.name", "research.TopHatEmerald.0", new ItemStack(ENTREPRENEUR_TOP_HAT)),
                      new BookPageCrafting("", "hatEmerald")
                    )
                  )
                );
            }
        }

        if (enableGoggles) {
            BookPageRegistry.addCategoryToSection(GADGET_SECTION, 6,
              new BookCategory("category.Goggles.name",
                new BookEntry("research.Goggles.name",
                  new BookPageItem("research.Goggles.name", "research.Goggles.0", new ItemStack(GOGGLES), new ItemStack(MONOCLE)),
                  new BookPageCrafting("", "goggles1", "goggles2"),
                  new BookPageCrafting("", "monocle1", "monocle2")
                )
              )
            );
        }

        if (enableExosuit && EngineeringTableModule.enableEngineering) {
            BookPageRegistry.addCategoryToSection(EXOSUIT_SECTION,
              new BookCategory("category.SteamExosuit.name",
                new BookEntry("research.SteamExosuit.name",
                  new BookPageItem("research.SteamExosuit.name", "research.SteamExosuit.0",
                    new ItemStack(STEAM_EXO_HEAD),
                    new ItemStack(STEAM_EXO_CHEST),
                    new ItemStack(STEAM_EXO_LEGS),
                    new ItemStack(STEAM_EXO_BOOTS)),
                  new BookPageText("research.SteamExosuit.name", "research.SteamExosuit.1"),
                  new BookPageCrafting("", "engineering"),
                  new BookPageCrafting("", "steamExoHead"),
                  new BookPageCrafting("", "steamExoBody"),
                  new BookPageCrafting("", "steamExoLegs"),
                  new BookPageCrafting("", "steamExoFeet")
                )
              )
            );

            // TODO: Entries for Leather Exosuit

            BookCategory tankCategory = new BookCategory("category.ExoTank.name",
              new BookEntry("research.ExoTank.name",
                new BookPageItem("research.ExoTank.name", "research.ExoTank.0", new ItemStack(STEAM_TANK)),
                new BookPageItem("research.ExoTankBase.name", "research.ExoTankBase.0", true, new ItemStack(STEAM_TANK))
              )
            );

            if (enableReinforcedTank) {
                tankCategory.appendEntries(new BookEntry("research.ExoTankUberReinforced",
                  new BookPageItem("research.ExoTankUberReinforced.name", "research.ExoTankUberReinforced.0", true, new ItemStack(UBER_REINFORCED_TANK)),
                  new BookPageCrafting("", "uberReinforcedTank1", "uberReinforcedTank2")));
            }
            if (enableUberReinforcedTank) {
                tankCategory.appendEntries(new BookEntry("research.ExoTankUberReinforced",
                  new BookPageItem("research.ExoTankUberReinforced.name", "research.ExoTankUberReinforced.0", true, new ItemStack(UBER_REINFORCED_TANK)),
                  new BookPageCrafting("", "uberReinforcedTank1", "uberReinforcedTank2")));
            }
            BookPageRegistry.addCategoryToSection(EXOSUIT_SECTION, tankCategory);

            {
                ItemStack[] stacks = new ItemStack[4];
                for (int i = 0; i < 4; i++) {
                    ItemStack stack = new ItemStack(STEAM_EXO_CHEST);
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
                    ((ItemSteamExosuitArmor) stack.getItem()).setInventorySlotContents(stack, 1, plate);
                    stacks[i] = stack;
                }

                BookCategory plateCategory = new BookCategory("category.ExoPlates.name",
                  new BookEntry("research.ExoPlates.name",
                    new BookPageItem("research.ExoPlates.name", "research.ExoPlates.0", stacks),
                    new BookPageText("", "research.ExoPlates.1")
                  )
                );

                if (enableCopperPlate) {
                    plateCategory.appendEntries(new BookEntry("research.PlateCopper.name",
                      new BookPageItem("research.PlateCopper.name", "research.PlateCopper.0", true, plateStack(COPPER_PLATE_META)),
                      new BookPageCrafting("", "exoCopper")));
                }
                if (enableIronPlate) {
                    plateCategory.appendEntries(new BookEntry("research.PlateIron.name",
                      new BookPageItem("research.PlateIron.name", "research.PlateIron.0", true, plateStack(IRON_PLATE_META)),
                      new BookPageCrafting("", "exoIron")));
                }
                if (enableBrassPlate) {
                    plateCategory.appendEntries(new BookEntry("research.PlateBrass.name",
                      new BookPageItem("research.PlateBrass.name", "research.PlateBrass.0", true, plateStack(BRASS_PLATE_META)),
                      new BookPageCrafting("", "exoBrass")));
                }
                if (enableGoldPlate) {
                    plateCategory.appendEntries(new BookEntry("research.PlateGold.name",
                      new BookPageItem("research.PlateGold.name", "research.PlateGold.0", true, plateStack(GOLD_PLATE_META)),
                      new BookPageCrafting("", "exoGold")));
                }
                if (enableGildedIronPlate) {
                    plateCategory.appendEntries(new BookEntry("research.PlateGilded.name",
                      new BookPageItem("research.PlateGilded.name", "research.PlateGilded.0", true, plateStack(GILDED_IRON_PLATE_META)),
                      new BookPageCrafting("", "exoGildedIron")));
                }
                if (enableLeadPlate && !OreDictionary.getOres(INGOT_LEAD).isEmpty()) {
                    plateCategory.appendEntries(new BookEntry("research.PlateLead.name",
                      new BookPageItem("research.PlateLead.name", "research.PlateLead.0", true, plateStack(LEAD_PLATE_META)),
                      new BookPageCrafting("", "exoLead")));
                }
                BookPageRegistry.addCategoryToSection(EXOSUIT_SECTION, plateCategory);
            }
            {
                ItemStack[] stacks = new ItemStack[4];
                for (int i = 0; i < 4; i++) {
                    ItemStack stack = new ItemStack(STEAM_EXO_CHEST);
                    stack.setTagCompound(new NBTTagCompound());
                    ItemStack dye = new ItemStack(DYE, 1, i);
                    ((ItemSteamExosuitArmor) stack.getItem()).setInventorySlotContents(stack, 2, dye);
                    stacks[i] = stack;
                }
                ItemStack[] dyes = {
                  new ItemStack(DYE, 1, 0),
                  new ItemStack(DYE, 1, 1),
                  new ItemStack(DYE, 1, 2),
                  new ItemStack(DYE, 1, 3)
                };
                BookCategory vanityCategory = new BookCategory("category.ExoVanity.name",
                  new BookEntry("research.ExoVanity.name",
                    new BookPageItem("research.ExoVanity.name", "research.ExoVanity.0", stacks)),
                  new BookEntry("research.ExoDyes.name",
                    new BookPageItem("research.ExoDyes.name", "research.ExoDyes.0", true, dyes)));
                if (enableEnderShroud) {
                    vanityCategory.appendEntries(
                      new BookEntry("research.EnderShroud.name",
                        new BookPageItem("research.EnderShroud.name", "research.EnderShroud.0", true, new ItemStack(ENDER_SHROUD)),
                        new BookPageCrafting("", "enderShroud")));
                }
                BookPageRegistry.addCategoryToSection(EXOSUIT_SECTION, vanityCategory);
            }

            {
                BookCategory headHelmCategory = new BookCategory("category.ExoHeadHelm.name");
                if (enableTopHat) {
                    headHelmCategory.appendEntries(new BookEntry("research.ExoTopHat.name",
                      new BookPageItem("research.ExoTopHat.name", "research.ExoTopHat.0", true, new ItemStack(TOP_HAT), new ItemStack(ENTREPRENEUR_TOP_HAT)),
                      new BookPage("")));
                }
                if (enableFrequencyShifter) {
                    headHelmCategory.appendEntries(new BookEntry("research.FrequencyShifter.name",
                      new BookPageItem("research.FrequencyShifter.name", "research.FrequencyShifter.0", true, new ItemStack(FREQUENCY_SHIFTER)),
                      new BookPageCrafting("", "frequencyShifter")));
                }
                BookPageRegistry.addCategoryToSection(EXOSUIT_SECTION, headHelmCategory);
            }

            {
                BookCategory headGoggleCategory = new BookCategory("category.ExoHeadGoggle.name");
                if (enableGoggles) {
                    headGoggleCategory.appendEntries(new BookEntry("research.ExoGoggles.name",
                      new BookPageItem("research.ExoGoggles.name", "research.ExoGoggles.0", true, new ItemStack(GOGGLES), new ItemStack(MONOCLE)),
                      new BookPage("")));
                }
                if (enableRebreather) {
                    headGoggleCategory.appendEntries(new BookEntry("research.Rebreather.name",
                      new BookPageItem("research.Rebreather.name", "research.Rebreather.0", true, new ItemStack(REBREATHER)),
                      new BookPageCrafting("", "rebreather")));
                }
                if (enableDragonRoar) {
                    headGoggleCategory.appendEntries(new BookEntry("research.DragonRoar.name",
                      new BookPageItem("research.DragonRoar.name", "research.DragonRoar.0", true, new ItemStack(DRAGON_ROAR)),
                      new BookPageCrafting("", "dragonRoar")));
                }
                BookPageRegistry.addCategoryToSection(EXOSUIT_SECTION, headGoggleCategory);
            }

            {
                BookCategory backCategory = new BookCategory("category.ExoBack.name");
                if (enableJetpack) {
                    backCategory.appendEntries(new BookEntry("research.Jetpack.name",
                      new BookPageItem("research.Jetpack.name", "research.Jetpack.0", true, new ItemStack(JETPACK)),
                      new BookPageCrafting("", "jetpack1", "jetpack2")));
                }
                if (enableWings) {
                    backCategory.appendEntries(new BookEntry("research.Wings.name",
                      new BookPageItem("research.Wings.name", "research.Wings.0", true, new ItemStack(WINGS)),
                      new BookPageCrafting("", "wings1", "wings2")));
                }
                BookPageRegistry.addCategoryToSection(EXOSUIT_SECTION, backCategory);
            }

            {
                BookCategory armCategory = new BookCategory("category.ExoArm.name");
                if (enablePowerFist) {
                    armCategory.appendEntries(new BookEntry("research.Fist.name",
                      new BookPageItem("research.Fist.name", "research.Fist.0", true, new ItemStack(POWER_FIST)),
                      new BookPageCrafting("", "powerFist1", "powerFist2")));
                }
                if (enableExtendoFist) {
                    armCategory.appendEntries(new BookEntry("research.ExtendoFist.name",
                      new BookPageItem("research.ExtendoFist.name", "research.ExtendoFist.0", true, new ItemStack(EXTENDO_FIST)),
                      new BookPageCrafting("", "extendoFist1", "extendoFist2")));
                }
                if (enablePitonDeployer) {
                    armCategory.appendEntries(new BookEntry("research.PitonDeployer.name",
                      new BookPageItem("research.PitonDeployer.name", "research.PitonDeployer.0", true, new ItemStack(PITON_DEPLOYER)),
                      new BookPageCrafting("", "pitonDeployer")));
                }
                if (enablePistonPush) {
                    armCategory.appendEntries(new BookEntry("research.PistonPush.name",
                      new BookPageItem("research.PistonPush.name", "research.PistonPush.0", true, new ItemStack(PISTON_PUSH)),
                      new BookPageCrafting("", "pistonPush")));
                }
                BookPageRegistry.addCategoryToSection(EXOSUIT_SECTION, armCategory);
            }

            {
                BookCategory hipCategory = new BookCategory("category.ExoHip.name");
                if (enableThrusters) {
                    hipCategory.appendEntries(new BookEntry("research.Thrusters.name",
                      new BookPageItem("research.Thrusters.name", "research.Thrusters.0", true, new ItemStack(THRUSTERS)),
                      new BookPageCrafting("", "thrusters1", "thrusters2")));
                }
                if (enableCanningMachine) {
                    hipCategory.appendEntries(new BookEntry("research.Canner.name",
                      new BookPageItem("research.Canner.name", "research.Canner.0", true, new ItemStack(CANNING_MACHINE)),
                      new BookPageCrafting("", "canner1", "canner2", "canner3", "canner4")));
                }
                if (enableReloadingHolsters) {
                    hipCategory.appendEntries(new BookEntry("research.ReloadingHolsters.name",
                      new BookPageItem("research.ReloadingHolsters.name", "research.ReloadingHolsters.0", true, new ItemStack(RELOADING_HOLSTERS)),
                      new BookPageCrafting("", "reloadingHolsters")));
                }
                BookPageRegistry.addCategoryToSection(EXOSUIT_SECTION, hipCategory);
            }

            {
                BookCategory legCategory = new BookCategory("category.ExoLeg.name");
                if (enableRunAssist) {
                    legCategory.appendEntries(new BookEntry("research.RunAssist.name",
                      new BookPageItem("research.RunAssist.name", "research.RunAssist.0", true, new ItemStack(RUN_ASSIST)),
                      new BookPageCrafting("", "runAssist1", "runAssist2")));
                }
                if (enableStealthUpgrade) {
                    legCategory.appendEntries(new BookEntry("research.StealthUpgrade.name",
                      new BookPageItem("research.StealthUpgrade.name", "research.StealthUpgrade.0", true, new ItemStack(STEALTH)),
                      new BookPageCrafting("", "stealthUpgrade")));
                }
                BookPageRegistry.addCategoryToSection(EXOSUIT_SECTION, legCategory);
            }

            {
                BookCategory heelCategory = new BookCategory("category.ExoHeel.name");
                if (enableFallAssist) {
                    heelCategory.appendEntries(new BookEntry("research.FallAssist.name",
                      new BookPageItem("research.FallAssist.name", "research.FallAssist.0", true, new ItemStack(FALL_ASSIST)),
                      new BookPageCrafting("", "noFall")));
                }
                if (enableAnchorHeels) {
                    boolean lead = enableLeadPlate &&
                      !OreDictionary.getOres(INGOT_LEAD).isEmpty() && !enableAnchorAnvilRecipe;
                    String desc = lead ? "research.AnchorHeelsLead.0" : "research.AnchorHeelsIron.0";
                    heelCategory.appendEntries(new BookEntry("research.AnchorHeels.name",
                      new BookPageItem("research.AnchorHeels.name", desc, true, new ItemStack(ANCHOR_HEELS)),
                      new BookPageCrafting("", "anchorHeels")));
                }
                BookPageRegistry.addCategoryToSection(EXOSUIT_SECTION, heelCategory);
            }

            {
                BookCategory footCategory = new BookCategory("category.ExoFoot.name");
                if (enableDoubleJump) {
                    footCategory.appendEntries(new BookEntry("research.DoubleJump.name",
                      new BookPageItem("research.DoubleJump.name", "research.DoubleJump.0", true, new ItemStack(DOUBLE_JUMP)),
                      new BookPageCrafting("", "doubleJump1", "doubleJump2")));
                }
                if (enableHydrophobic) {
                    footCategory.appendEntries(new BookEntry("research.Hydrophobic.name",
                      new BookPageItem("research.Hydrophobic.name", "research.Hydrophobic.0", true, new ItemStack(HYDROPHOBIC_COATINGS)),
                      new BookPageCrafting("", "hydrophobic")));
                }
                if (enablePyrophobic) {
                    footCategory.appendEntries(new BookEntry("research.Pyrophobic.name",
                      new BookPageItem("research.Pyrophobic.name", "research.Pyrophobic.0", true, new ItemStack(PYROPHOBIC_COATINGS)),
                      new BookPageCrafting("", "pyrophobic")));
                }
                if (enableJumpAssist) {
                    footCategory.appendEntries(new BookEntry("research.JumpAssist.name",
                      new BookPageItem("research.JumpAssist.name", "research.JumpAssist.0", true, new ItemStack(LEAP_ACTUATOR)),
                      new BookPageCrafting("", "jumpAssist1", "jumpAssist2")));
                }
                BookPageRegistry.addCategoryToSection(EXOSUIT_SECTION, footCategory);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(ModelRegistryEvent event) {
        ModelLoaderRegistry.registerLoader(new GenericModelLoaderLocationMatch(SteamExosuitItemModel.GENERIC_MODEL, new ResourceLocation(MOD_ID, "models/block/steam_exosuit_piece")));
        ModelLoaderRegistry.registerLoader(new GenericModelLoaderLocationMatch(LeatherExosuitItemModel.GENERIC_MODEL, new ResourceLocation(MOD_ID, "models/block/leather_exosuit_piece")));
        toRegisterNormally.forEach(this::registerModel);
        for (int i = 0; i < MAX_PLATE_META; i++) {
            registerModelItemStack(plateStack(i));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initClient() {

        MinecraftForge.EVENT_BUS.register(SteamExosuitModelCache.INSTANCE);
        MinecraftForge.EVENT_BUS.register(LeatherExosuitModelCache.INSTANCE);

        MONOCLE_KEY = new KeyBinding("key.monocle.desc", Keyboard.KEY_Z, "key." + MOD_ID + ".category");
        ClientRegistry.registerKeyBinding(MONOCLE_KEY);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void postInitClient() {
        ItemColors colors = Minecraft.getMinecraft().getItemColors();
        colors.registerItemColorHandler(new ItemSteamExosuitColorHandler(), STEAM_EXO_BOOTS);
        colors.registerItemColorHandler(new ItemSteamExosuitColorHandler(), STEAM_EXO_CHEST);
        colors.registerItemColorHandler(new ItemSteamExosuitColorHandler(), STEAM_EXO_HEAD);
        colors.registerItemColorHandler(new ItemSteamExosuitColorHandler(), STEAM_EXO_LEGS);
    }

    public static ItemStack plateStack(int meta, int size) {
        return new ItemStack(EXOSUIT_PLATE, size, meta);
    }

    public static ItemStack plateStack(int meta) {
        return plateStack(meta, 1);
    }

    @Override
    public void loadConfigurationOptions(Configuration config) {
        enableTopHat = config.get(CATEGORY_ITEMS, "Enable Top Hat", true).getBoolean();
        enableEmeraldHat = config.get(CATEGORY_ITEMS, "Enable Emerald Top Hat", true).getBoolean();
        enableGoggles = config.get(CATEGORY_ITEMS, "Enable Goggles/Monocle", true).getBoolean();

        // EXOSUIT
        passiveDrain = config.get(CATEGORY_EXOSUIT, "Passively drain steam while in use", true).getBoolean();
        enableExosuit = config.get(CATEGORY_EXOSUIT, "Enable Exosuits in general (disabling disables both suits, all upgrades, and plates)", true).getBoolean();
        enableSteamExosuit = config.get(CATEGORY_EXOSUIT, "Enable Steam Exosuit (disabling disabled all its upgrades, as well)", true).getBoolean();
        enableLeatherExosuit = config.get(CATEGORY_EXOSUIT, "Enable Leather Exosuit (disabling only disables the suit)", true).getBoolean();
        exoConsumption = config.get(CATEGORY_EXOSUIT, "The amount of steam the Exosuit consumes", EXO_CONSUMPTION_DEFAULT).getInt();
        jumpBoostConsumption = config.get(CATEGORY_EXOSUIT, "The amount of steam jump boost consumes", JUMP_BOOST_CONSUMPTION_DEFAULT).getInt();
        jetpackConsumption = config.get(CATEGORY_EXOSUIT, "The amount of steam the Jetpack consumes", JETPACK_CONSUMPTION_DEFAULT).getInt();
        jumpBoostConsumptionShiftJump = config.get(CATEGORY_EXOSUIT, "The amount of steam the jump boost shift jump consumes", JUMP_BOOST_CONSUMPTION_SHIFT_BOOST_DEFAULT).getInt();
        thrusterConsumption = config.get(CATEGORY_EXOSUIT, "The amount of steam the Exosuit Thrusters consumes", THRUSTER_CONSUMPTION_DEFAULT).getInt();
        runAssistConsumption = config.get(CATEGORY_EXOSUIT, "The amount of steam the Exosuit Run Assist consumes", RUN_ASSIST_CONSUMPTION_DEFAULT).getInt();
        powerFistConsumption = config.get(CATEGORY_EXOSUIT, "The amount of steam the Exosuit Power Fist consumes", POWER_FIST_CONSUMPTION_DEFAULT).getInt();
        zincPlateConsumption = config.get(CATEGORY_EXOSUIT, "The amount of steam the Exosuit Zinc Plate consumes", ZINC_PLATE_CONSUMPTION_DEFAULT).getInt();
        rebreatherConsumption = config.get(CATEGORY_EXOSUIT, "The amount of steam the Rebreather consumes", REBREATHER_CONSUMPTION_DEFAULT).getInt();
        hydrophobicConsumption = config.get(CATEGORY_EXOSUIT, "The amount of steam the Hydrophobic Coatings consume", HYDROPHOBIC_CONSUMPTION_DEFAULT).getInt();
        pyrophobicConsumption = config.get(CATEGORY_EXOSUIT, "The amount of steam the Pyrophobic Coatings consume", PYROPHOBIC_CONSUMPTION_DEFAULT).getInt();
        enableAnchorAnvilRecipe = config.get(CATEGORY_EXOSUIT, "Use the leadless Anchor Heels recipe. This will always be true if there is no lead available.", false).getBoolean();
        pistonPushConsumption = config.get(CATEGORY_EXOSUIT, "The amount of steam the Piston Push consumes", PISTON_PUSH_CONSUMPTION_DEFAULT).getInt();
        reloadingConsumption = config.get(CATEGORY_EXOSUIT, "The amount of steam the Reloading Holsters consume", RELOADING_CONSUMPTION_DEFAULT).getInt();
        dragonRoarConsumption = config.get(CATEGORY_EXOSUIT, "The amount of steam the Dragon Roar consumes", DRAGON_ROAR_CONSUMPTION_DEFAULT).getInt();

        // EXOSUIT UPGRADES
        enableFallAssist = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Fall Assist", true).getBoolean();
        enableJumpAssist = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Leap Actuator", true).getBoolean();
        enableDoubleJump = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Pulse Nozzle", true).getBoolean();
        enableRunAssist = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Modular Accelerator", true).getBoolean();
        enableStealthUpgrade = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Acoustic Dampener", true).getBoolean();
        enableJetpack = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Steam Jetpack", true).getBoolean();
        enableThrusters = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Thrusters", true).getBoolean();
        enableWings = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Wings", true).getBoolean();
        enablePowerFist = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Power Fist", true).getBoolean();
        enableCanningMachine = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Canner", true).getBoolean();
        enableExtendoFist = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Extendo Fist", true).getBoolean();
        enablePitonDeployer = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Piton Deployer", true).getBoolean();
        enableReinforcedTank = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Reinforced Tank", true).getBoolean();
        enableUberReinforcedTank = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Heavily Reinforced Tank", true).getBoolean();
        enableEnderShroud = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Ender Shroud", true).getBoolean();
        enableRebreather = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Rebreather", true).getBoolean();
        enableHydrophobic = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Hydrophobic Coatings", true).getBoolean();
        enablePyrophobic = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Pyrophobic Coatings", true).getBoolean();
        enableAnchorHeels = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Anchor Heels", true).getBoolean();
        enablePistonPush = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Piston Push", true).getBoolean();
        enableReloadingHolsters = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Reloading Holsters", true).getBoolean();
        enableFrequencyShifter = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Frequency Shifter", true).getBoolean();
        enableDragonRoar = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Dragon Roar", true).getBoolean();

        enableCopperPlate = config.get(CATEGORY_EXOSUIT_PLATES, "Enable copper plate", true).getBoolean();
        enableZincPlate = config.get(CATEGORY_EXOSUIT_PLATES, "Enable zinc plate", true).getBoolean();
        enableIronPlate = config.get(CATEGORY_EXOSUIT_PLATES, "Enable iron plate", true).getBoolean();
        enableGoldPlate = config.get(CATEGORY_EXOSUIT_PLATES, "Enable gold plate", true).getBoolean();
        enableBrassPlate = config.get(CATEGORY_EXOSUIT_PLATES, "Enable brass plate", true).getBoolean();
        enableLeadPlate = config.get(CATEGORY_EXOSUIT_PLATES, "Enable lead plate", true).getBoolean();
        enableThaumiumPlate = config.get(CATEGORY_EXOSUIT_PLATES, "Enable thaumium plate", true).getBoolean();
        enableElementiumPlate = config.get(CATEGORY_EXOSUIT_PLATES, "Enable elementium plate", true).getBoolean();
        enableTerrasteelPlate = config.get(CATEGORY_EXOSUIT_PLATES, "Enable terrasteel plate", true).getBoolean();
        enableYetiPlate = config.get(CATEGORY_EXOSUIT_PLATES, "Enable yeti plate", true).getBoolean();
        enableFieryPlate = config.get(CATEGORY_EXOSUIT_PLATES, "Enable fiery plate", true).getBoolean();
        enableSadistPlate = config.get(CATEGORY_EXOSUIT_PLATES, "Enable sadist plate", true).getBoolean();
        enableVibrantPlate = config.get(CATEGORY_EXOSUIT_PLATES, "Enable vibrant plate", true).getBoolean();
        enableEnderiumPlate = config.get(CATEGORY_EXOSUIT_PLATES, "Enable enderium plate", true).getBoolean();
        enableGildedIronPlate = config.get(CATEGORY_EXOSUIT_PLATES, "Enable gilded iron plate", true).getBoolean();

        reinforcedTankCapacity = config.get(CATEGORY_EXOSUIT_UPGRADES, "The amount of steam the reinforced tank can hold", REINFORCED_TANK_CAPACITY_DEFAULT).getInt();
        uberReinforcedTankCapacity = config.get(CATEGORY_EXOSUIT_UPGRADES, "The amount of steam the heavily reinforced tank can hold", UBER_REINFORCED_TANK_CAPACITY_DEFAULT).getInt();
        //enableDoubleJump = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable double jump", true).getBoolean();
    }
}
