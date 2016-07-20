package flaxbeard.steamcraft;

import flaxbeard.steamcraft.api.DrillHeadRecipe;
import flaxbeard.steamcraft.api.util.SPLog;
import flaxbeard.steamcraft.block.TileEntityDummyBlock;
import flaxbeard.steamcraft.client.render.TextureStitcher;
import flaxbeard.steamcraft.client.render.model.exosuit.ExosuitModelCache;
import flaxbeard.steamcraft.common.CommonProxy;
import flaxbeard.steamcraft.data.capabilities.animal.AnimalDataStorage;
import flaxbeard.steamcraft.data.capabilities.animal.IAnimalData;
import flaxbeard.steamcraft.data.capabilities.player.IPlayerData;
import flaxbeard.steamcraft.data.capabilities.player.PlayerDataStorage;
import flaxbeard.steamcraft.data.capabilities.villager.IVillagerData;
import flaxbeard.steamcraft.data.capabilities.villager.VillagerDataStorage;
import flaxbeard.steamcraft.data.village.SteamEngineerCareer;
import flaxbeard.steamcraft.entity.item.EntityCanisterItem;
import flaxbeard.steamcraft.entity.item.EntityFloatingItem;
import flaxbeard.steamcraft.entity.item.EntityMortarItem;
import flaxbeard.steamcraft.entity.projectile.EntityRocket;
import flaxbeard.steamcraft.gui.SteamcraftGuiHandler;
import flaxbeard.steamcraft.handler.PhobicCoatingHandler;
import flaxbeard.steamcraft.handler.SteamcraftEventHandler;
import flaxbeard.steamcraft.handler.SteamcraftTickHandler;
import flaxbeard.steamcraft.init.blocks.BlockCategories;
import flaxbeard.steamcraft.init.blocks.SteamNetworkBlocks;
import flaxbeard.steamcraft.init.items.ItemCategories;
import flaxbeard.steamcraft.init.items.MetalItems;
import flaxbeard.steamcraft.init.items.tools.GadgetItems;
import flaxbeard.steamcraft.init.items.tools.ToolItems;
import flaxbeard.steamcraft.init.items.tools.ToolUpgradeItems;
import flaxbeard.steamcraft.init.misc.MiscellaneousCategories;
import flaxbeard.steamcraft.item.ItemSmashedOre;
import flaxbeard.steamcraft.misc.DrillHeadMaterial;
import flaxbeard.steamcraft.misc.OreDictHelper;
import flaxbeard.steamcraft.network.*;
import flaxbeard.steamcraft.tile.*;
import flaxbeard.steamcraft.world.ComponentSteamWorkshop;
import flaxbeard.steamcraft.world.SteamWorkshopCreationHandler;
import flaxbeard.steamcraft.world.SteamcraftOreGen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;

import java.util.List;

import javax.swing.*;

@Mod(modid = Steamcraft.MOD_ID, name = "Flaxbeard's Steam Power", version = Config.VERSION)
public class Steamcraft {
    @Mod.Instance("Steamcraft")
    public static Steamcraft instance;

    @CapabilityInject(IPlayerData.class)
    public static final Capability<IPlayerData> PLAYER_DATA = null;

    @CapabilityInject(IAnimalData.class)
    public static final Capability<IAnimalData> ANIMAL_DATA = null;

    @CapabilityInject(IVillagerData.class)
    public static final Capability<IVillagerData> VILLAGER_DATA = null;

    public static final String MOD_ID = "steamcraft";
    public static SPLog log = SPLog.getInstance().setLogLevel(SPLog.NONE);

    public static SimpleNetworkWrapper channel;

    public static EnumRarity upgrade;

    public static CreativeTabs tab;
    public static CreativeTabs tabTools;

    public static SoundEvent SOUND_HISS;
    public static SoundEvent SOUND_ROCKET;
    public static SoundEvent SOUND_WRENCH;
    public static SoundEvent SOUND_CANNON;
    public static SoundEvent SOUND_LEAK;
    public static SoundEvent SOUND_WHISTLE;

    public static VillagerRegistry.VillagerProfession STEAM_ENGINEER_PROFESSION;
    public static VillagerRegistry.VillagerCareer STEAM_ENGINEER_CAREER;

    public static boolean steamRegistered;

    public static String CONFIG_DIR;

    @SidedProxy(clientSide = "flaxbeard.steamcraft.client.ClientProxy", serverSide = "flaxbeard.steamcraft.common.CommonProxy")
    public static CommonProxy proxy;

    private static void registerTileEntity(Class<? extends TileEntity> clazz, String key) {
        GameRegistry.registerTileEntityWithAlternatives(clazz, "steamcraft:" + key);
    }

    private static SoundEvent registerSound(String soundName) {
        final ResourceLocation soundID = new ResourceLocation(MOD_ID, soundName);
        return GameRegistry.register(new SoundEvent(soundID).setRegistryName(soundID));
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        try {
            // Try to get any class in CodeChickenLib.
            Class.forName("codechicken.lib.raytracer.RayTracer");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            int input = JOptionPane.showOptionDialog(null, "Flaxbeard's Steam Power requires CodeChickenLib.",
              "CodeChickenLib Not Found!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);

            if (input == JOptionPane.OK_OPTION || input == JOptionPane.CANCEL_OPTION) {
                FMLCommonHandler.instance().exitJava(1, false);
            }
        }

        Config.load(event);

        CONFIG_DIR = event.getModConfigurationDirectory().toString();
        tab = new SCTab(CreativeTabs.getNextID(), "steamcraft", false).setBackgroundImageName("item_search.png");
        tabTools = new SCTab(CreativeTabs.getNextID(), "steamcraftTools", true);

        upgrade = EnumHelper.addRarity("UPGRADE", TextFormatting.RED, "Upgrade");

        GameRegistry.registerWorldGenerator(new SteamcraftOreGen(), 1);

        channel = NetworkRegistry.INSTANCE.newSimpleChannel("fspChannel");
        channel.registerMessage(CamoPacketHandler.class, CamoPacket.class, 0, Side.SERVER);
        channel.registerMessage(ItemNamePacketHandler.class, ItemNamePacket.class, 1, Side.SERVER);
        channel.registerMessage(ConnectPacketHandler.class, ConnectPacket.class, 2, Side.SERVER);

        SOUND_HISS = registerSound("hiss");
        SOUND_CANNON = registerSound("cannon");
        SOUND_LEAK = registerSound("leaking");
        SOUND_ROCKET = registerSound("rocket");
        SOUND_WRENCH = registerSound("wrench");
        SOUND_WHISTLE = registerSound("horn");

        // Forcibly initialize every item and ORE_BLOCK category, calling their static blocks and enums and stuff.
        for (BlockCategories category : BlockCategories.values()) {
            category.getCategory().oreDict();
        }

        for (ItemCategories category : ItemCategories.values()) {
            category.getCategory().oreDict();
        }

        VillagerRegistry.instance().registerVillageCreationHandler(new SteamWorkshopCreationHandler());
        MapGenStructureIO.registerStructureComponent(ComponentSteamWorkshop.class, "steamcraft:workshop");

        EntityRegistry.registerModEntity(EntityFloatingItem.class, "FloatingItem", 0, this, 64, 20, true);
        EntityRegistry.registerModEntity(EntityMortarItem.class, "MortarItem", 1, this, 64, 20, true);
        EntityRegistry.registerModEntity(EntityCanisterItem.class, "CanisterItem", 2, this, 64, 20, true);
        EntityRegistry.registerModEntity(EntityRocket.class, "Rocket", 3, this, 64, 20, true);
        // EntityRegistry.registerModEntity(EntityHarpoon.class, "Harpoon", 4, Steamcraft.instance, 64, 20, true);

        //  EntityRegistry.registerModEntity(EntitySteamHorse.class, "SteamHorse", 2, Steamcraft.instance, 64, 1, true);
        //EntityList.addMapping(EntitySteamHorse.class, "SteamHorse", 5, 0xE2B23E, 0xB38E2F);

        registerTileEntity(TileEntityCrucible.class, "steamcraftCrucible");
        registerTileEntity(TileEntityMold.class, "mold");
        registerTileEntity(TileEntityBoiler.class, "boiler");
        registerTileEntity(TileEntitySteamPipe.class, "pipe");
        registerTileEntity(TileEntityValvePipe.class, "valvePipe");

        registerTileEntity(TileEntitySteamHeater.class, "heater");
        registerTileEntity(TileEntityEngineeringTable.class, "engineeringTable");
        registerTileEntity(TileEntitySteamCharger.class, "steamCharger");
        registerTileEntity(TileEntitySteamTank.class, "steamTank");
        registerTileEntity(TileEntitySteamGauge.class, "steamGauge");
        registerTileEntity(TileEntityRuptureDisc.class, "ruptureDisc");
        registerTileEntity(TileEntityCreativeTank.class, "creativeSteamTank");
        registerTileEntity(TileEntitySteamHammer.class, "steamHammer");
        registerTileEntity(TileEntityItemMortar.class, "itemMortar");
        registerTileEntity(TileEntitySteamFurnace.class, "steamFurnace");
        registerTileEntity(TileEntityPump.class, "pump");
        registerTileEntity(TileEntityThumper.class, "thumper");
        registerTileEntity(TileEntitySmasher.class, "smasher");
        registerTileEntity(TileEntityDummyBlock.class, "dummy");
        registerTileEntity(TileEntityFlashBoiler.class, "flashBoiler");
        registerTileEntity(TileEntityFan.class, "fan");
        registerTileEntity(TileEntityVacuum.class, "vacuum");
        registerTileEntity(TileEntityFluidSteamConverter.class, "fluidSteamConverter");
        registerTileEntity(TileEntityWhistle.class, "whistle");
        registerTileEntity(TileEntityChargingPad.class, "chargingPad");

        CapabilityManager.INSTANCE.register(IPlayerData.class, new PlayerDataStorage(),
          IPlayerData.DefaultImplementation.class);
        CapabilityManager.INSTANCE.register(IAnimalData.class, new AnimalDataStorage(),
          IAnimalData.DefaultImplementation.class);
        CapabilityManager.INSTANCE.register(IVillagerData.class, new VillagerDataStorage(),
          IVillagerData.DefaultImplementation.class);

        for (MiscellaneousCategories category : MiscellaneousCategories.values()) {
            if (category.isEnabled()) {
                category.getCategory().preInit(event);
            }
        }

        proxy.registerModels();
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new SteamcraftGuiHandler());

        MinecraftForge.EVENT_BUS.register(new SteamcraftEventHandler());
        MinecraftForge.EVENT_BUS.register(new PhobicCoatingHandler());

        MinecraftForge.EVENT_BUS.register(new SteamcraftTickHandler());
        MinecraftForge.EVENT_BUS.register(new PhobicCoatingHandler());

        if (event.getSide() == Side.CLIENT) {
            MinecraftForge.EVENT_BUS.register(new ExosuitModelCache());
        }

        proxy.registerRenderers();
        proxy.registerHotkeys();

        for (BlockCategories category : BlockCategories.values()) {
            category.getCategory().recipes();
        }

        for (ItemCategories category : ItemCategories.values()) {
            category.getCategory().recipes();
        }

        // This deprecation is moderately useless unless we add our own engineer zombie texture.
        //noinspection deprecation
        STEAM_ENGINEER_PROFESSION = new VillagerRegistry.VillagerProfession(MOD_ID + ":steam_engineer",
          MOD_ID + ":textures/models/villager.png");
        STEAM_ENGINEER_CAREER = new SteamEngineerCareer();
        VillagerRegistry.instance().register(STEAM_ENGINEER_PROFESSION);
        VillagerRegistry.instance().registerVillageCreationHandler(new SteamWorkshopCreationHandler());

        for (MiscellaneousCategories category : MiscellaneousCategories.values()) {
            if (category.isEnabled()) {
                category.getCategory().init(event);
            }
        }
    }


    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            MinecraftForge.EVENT_BUS.register(new TextureStitcher());
        }
        if (Config.enablePipe) {
            MinecraftForge.EVENT_BUS.register(SteamNetworkBlocks.Blocks.PIPE.getBlock());
        }

        ItemSmashedOre iso = (ItemSmashedOre) MetalItems.Items.SMASHED_ORE.getItem();
        iso.registerDusts();
        iso.addSmelting();
        iso.registerDusts();
        SteamcraftBook.registerBookResearch();

        long start = System.currentTimeMillis();
        String[] ores = OreDictionary.getOreNames();
        for (String s : ores) {
            List<ItemStack> stacks = OreDictionary.getOres(s);
            for (ItemStack stack : stacks) {
                OreDictHelper.initializeOreDicts(s, stack);
            }
        }
        long end = System.currentTimeMillis();
        int time = (int) (end - start);
        FMLLog.info("Finished initializing Flaxbeard's Steam Power OreDictHelper in %s ms", time);

        RecipeSorter.register(MOD_ID + ":drill_head", DrillHeadRecipe.class, RecipeSorter.Category.SHAPED, "before:forge:shapedore");
        DrillHeadMaterial.registerDefaults();
        ((ToolUpgradeItems) ItemCategories.TOOL_UPGRADES.getCategory()).postInit();
        SteamcraftBook.registerSteamTools();

        for (MiscellaneousCategories category : MiscellaneousCategories.values()) {
            if (category.isEnabled()) {
                category.getCategory().postInit(event);
            }
        }
    }


    private class SCTab extends CreativeTabs {
        boolean isToolTab;

        public SCTab(int par1, String par2Str, boolean toolTab) {
            super(par1, par2Str);
            isToolTab = toolTab;
        }


        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return isToolTab ? ToolItems.Items.BRASS_PICKAXE.getItem() : GadgetItems.Items.BOOK.getItem();
        }

        @Override
        public boolean hasSearchBar() {
            return !isToolTab;
        }
    }
}