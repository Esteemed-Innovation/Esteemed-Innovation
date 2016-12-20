package eiteam.esteemedinnovation.commons;

import eiteam.esteemedinnovation.book.EsteemedInnovationJournal;
import eiteam.esteemedinnovation.api.Constants;
import eiteam.esteemedinnovation.api.firearm.rocketlauncher.EntityRocket;
import eiteam.esteemedinnovation.api.util.SPLog;
import eiteam.esteemedinnovation.misc.TileEntityDummyBlock;
import eiteam.esteemedinnovation.boiler.TileEntityBoiler;
import eiteam.esteemedinnovation.buzzsaw.TileEntitySaw;
import eiteam.esteemedinnovation.charging.TileEntityChargingPad;
import eiteam.esteemedinnovation.charging.TileEntitySteamCharger;
import eiteam.esteemedinnovation.armor.exosuit.ExosuitModelCache;
import eiteam.esteemedinnovation.converter.TileEntityFluidSteamConverter;
import eiteam.esteemedinnovation.armor.exosuit.upgrades.frequency.AnimalDataStorage;
import eiteam.esteemedinnovation.armor.exosuit.upgrades.frequency.IAnimalData;
import eiteam.esteemedinnovation.commons.capabilities.player.IPlayerData;
import eiteam.esteemedinnovation.commons.capabilities.player.PlayerDataStorage;
import eiteam.esteemedinnovation.armor.tophat.IVillagerData;
import eiteam.esteemedinnovation.armor.tophat.VillagerDataStorage;
import eiteam.esteemedinnovation.processing.hammer.ItemNamePacket;
import eiteam.esteemedinnovation.processing.hammer.ItemNamePacketHandler;
import eiteam.esteemedinnovation.processing.hammer.TileEntitySteamHammer;
import eiteam.esteemedinnovation.processing.heater.TileEntitySteamFurnace;
import eiteam.esteemedinnovation.processing.heater.TileEntitySteamHeater;
import eiteam.esteemedinnovation.processing.smasher.TileEntitySmasher;
import eiteam.esteemedinnovation.processing.thumper.TileEntityThumper;
import eiteam.esteemedinnovation.steamsafety.disc.TileEntityRuptureDisc;
import eiteam.esteemedinnovation.steamsafety.gauge.TileEntitySteamGauge;
import eiteam.esteemedinnovation.steamsafety.whistle.TileEntityWhistle;
import eiteam.esteemedinnovation.storage.steam.TileEntityCreativeTank;
import eiteam.esteemedinnovation.storage.steam.TileEntitySteamTank;
import eiteam.esteemedinnovation.transport.block.TileEntityPlonker;
import eiteam.esteemedinnovation.transport.entity.TileEntityFan;
import eiteam.esteemedinnovation.transport.entity.TileEntityVacuum;
import eiteam.esteemedinnovation.transport.fluid.funnel.TileEntityFunnel;
import eiteam.esteemedinnovation.transport.fluid.screw.TileEntityPump;
import eiteam.esteemedinnovation.transport.item.TileEntityItemMortar;
import eiteam.esteemedinnovation.workshop.SteamEngineerCareer;
import eiteam.esteemedinnovation.engineeringtable.TileEntityEngineeringTable;
import eiteam.esteemedinnovation.storage.item.canister.EntityCanisterItem;
import eiteam.esteemedinnovation.fishfarm.EntityFloatingItem;
import eiteam.esteemedinnovation.transport.item.EntityMortarItem;
import eiteam.esteemedinnovation.commons.handler.GuiHandler;
import eiteam.esteemedinnovation.commons.handler.FieldHandler;
import eiteam.esteemedinnovation.commons.handler.GenericEventHandler;
import eiteam.esteemedinnovation.commons.handler.GenericTickHandler;
import eiteam.esteemedinnovation.armor.exosuit.upgrades.PhobicCoatingHandler;
import eiteam.esteemedinnovation.init.blocks.BlockCategories;
import eiteam.esteemedinnovation.init.blocks.PipeBlocks;
import eiteam.esteemedinnovation.init.items.ItemCategories;
import eiteam.esteemedinnovation.init.items.tools.GadgetItems;
import eiteam.esteemedinnovation.init.items.tools.ToolItems;
import eiteam.esteemedinnovation.init.items.tools.ToolUpgradeItems;
import eiteam.esteemedinnovation.init.misc.MiscellaneousCategories;
import eiteam.esteemedinnovation.metalcasting.TileEntityCrucible;
import eiteam.esteemedinnovation.metalcasting.mold.TileEntityMold;
import eiteam.esteemedinnovation.tools.steam.upgrades.drillhead.DrillHeadMaterial;
import eiteam.esteemedinnovation.tools.steam.upgrades.drillhead.DrillHeadRecipe;
import eiteam.esteemedinnovation.commons.util.OreDictHelper;
import eiteam.esteemedinnovation.commons.network.*;
import eiteam.esteemedinnovation.transport.fluid.pipes.TileEntityColdFluidPipe;
import eiteam.esteemedinnovation.transport.steam.TileEntitySteamPipe;
import eiteam.esteemedinnovation.transport.steam.TileEntityValvePipe;
import eiteam.esteemedinnovation.workshop.ComponentSteamWorkshop;
import eiteam.esteemedinnovation.metals.raw.ExtraDimensionalOreGenerator;
import eiteam.esteemedinnovation.workshop.SteamWorkshopCreationHandler;
import eiteam.esteemedinnovation.metals.raw.SurfaceOreGenerator;
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

@Mod(
  modid = EsteemedInnovation.MOD_ID,
  name = "Esteemed Innovation",
  version = Config.VERSION,
  dependencies="required-after:CodeChickenLib@[2.4.2,)"
)
public class EsteemedInnovation {
    // TODO: Migrate uses of MOD_ID to EI_MODID
    public static final String MOD_ID = Constants.EI_MODID;

    @Mod.Instance(MOD_ID)
    public static EsteemedInnovation instance;

    @CapabilityInject(IPlayerData.class)
    public static final Capability<IPlayerData> PLAYER_DATA = null;

    @CapabilityInject(IAnimalData.class)
    public static final Capability<IAnimalData> ANIMAL_DATA = null;

    @CapabilityInject(IVillagerData.class)
    public static final Capability<IVillagerData> VILLAGER_DATA = null;

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

    public static String CONFIG_DIR;

    @SidedProxy(clientSide = "eiteam.esteemedinnovation.commons.ClientProxy", serverSide = "eiteam.esteemedinnovation.commons.CommonProxy")
    public static CommonProxy proxy;

    private static void registerTileEntity(Class<? extends TileEntity> clazz, String key) {
        GameRegistry.registerTileEntityWithAlternatives(clazz, EsteemedInnovation.MOD_ID + ":" + key);
    }

    private static SoundEvent registerSound(String soundName) {
        final ResourceLocation soundID = new ResourceLocation(MOD_ID, soundName);
        return GameRegistry.register(new SoundEvent(soundID).setRegistryName(soundID));
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Config.load(event);

        CONFIG_DIR = event.getModConfigurationDirectory().toString();
        tab = new EsteemedInnovationTab(CreativeTabs.getNextID(), MOD_ID, false).setBackgroundImageName("item_search.png");
        tabTools = new EsteemedInnovationTab(CreativeTabs.getNextID(), MOD_ID + "Tools", true);

        upgrade = EnumHelper.addRarity("UPGRADE", TextFormatting.RED, "Upgrade");

        GameRegistry.registerWorldGenerator(new ExtraDimensionalOreGenerator(), 1);
        GameRegistry.registerWorldGenerator(new SurfaceOreGenerator(), 1);

        channel = NetworkRegistry.INSTANCE.newSimpleChannel("eiChannel");
        channel.registerMessage(CamoPacketHandler.class, CamoPacket.class, 0, Side.SERVER);
        channel.registerMessage(ItemNamePacketHandler.class, ItemNamePacket.class, 1, Side.SERVER);
        channel.registerMessage(ConnectPacketHandler.class, ConnectPacket.class, 2, Side.SERVER);
        channel.registerMessage(JumpValueChangePacketHandler.class, JumpValueChangePacket.class, 3, Side.SERVER);

        SOUND_HISS = registerSound("hiss");
        SOUND_CANNON = registerSound("cannon");
        SOUND_LEAK = registerSound("leaking");
        SOUND_ROCKET = registerSound("rocket");
        SOUND_WRENCH = registerSound("wrench");
        SOUND_WHISTLE = registerSound("horn");

        // Forcibly initialize every item and block category, calling their static blocks and enums and stuff.
        for (BlockCategories category : BlockCategories.values()) {
            category.getCategory().oreDict();
        }

        for (ItemCategories category : ItemCategories.values()) {
            category.getCategory().oreDict();
        }

        VillagerRegistry.instance().registerVillageCreationHandler(new SteamWorkshopCreationHandler());
        MapGenStructureIO.registerStructureComponent(ComponentSteamWorkshop.class, MOD_ID + ":workshop");

        EntityRegistry.registerModEntity(EntityFloatingItem.class, "FloatingItem", 0, this, 64, 20, true);
        EntityRegistry.registerModEntity(EntityMortarItem.class, "MortarItem", 1, this, 64, 20, true);
        EntityRegistry.registerModEntity(EntityCanisterItem.class, "CanisterItem", 2, this, 64, 20, true);
        EntityRegistry.registerModEntity(EntityRocket.class, "Rocket", 3, this, 64, 20, true);
        // EntityRegistry.registerModEntity(EntityHarpoon.class, "Harpoon", 4, EsteemedInnovation.instance, 64, 20, true);

        //  EntityRegistry.registerModEntity(EntitySteamHorse.class, "SteamHorse", 2, EsteemedInnovation.instance, 64, 1, true);
        //EntityList.addMapping(EntitySteamHorse.class, "SteamHorse", 5, 0xE2B23E, 0xB38E2F);

        registerTileEntity(TileEntityCrucible.class, "crucible");
        registerTileEntity(TileEntityMold.class, "mold");
        registerTileEntity(TileEntityBoiler.class, "boiler");
        registerTileEntity(TileEntitySteamPipe.class, "brass_pipe");
        registerTileEntity(TileEntityColdFluidPipe.class, "copper_pipe");
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
        registerTileEntity(TileEntityFan.class, "fan");
        registerTileEntity(TileEntityVacuum.class, "vacuum");
        registerTileEntity(TileEntityFluidSteamConverter.class, "fluidSteamConverter");
        registerTileEntity(TileEntityWhistle.class, "whistle");
        registerTileEntity(TileEntityChargingPad.class, "chargingPad");
        registerTileEntity(TileEntityFunnel.class, "funnel");
        registerTileEntity(TileEntitySaw.class, "saw");
        registerTileEntity(TileEntityPlonker.class, "plonker");

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
        proxy.registerTexturesToStitch();

        proxy.registerModels();
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

        FieldHandler.init();
        MinecraftForge.EVENT_BUS.register(new GenericEventHandler());
        MinecraftForge.EVENT_BUS.register(new PhobicCoatingHandler());

        MinecraftForge.EVENT_BUS.register(new GenericTickHandler());
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
        if (Config.enablePipe) {
            MinecraftForge.EVENT_BUS.register(PipeBlocks.Blocks.BRASS_PIPE.getBlock());
        }

        EsteemedInnovationJournal.registerBookResearch();

        long start = System.currentTimeMillis();
        String[] ores = OreDictionary.getOreNames();
        for (String s : ores) {
            List<ItemStack> stacks = OreDictionary.getOres(s);
            for (ItemStack stack : stacks) {
                OreDictHelper.initializeOreDicts(s, stack);
            }
        }
        OreDictHelper.initializeGeneral();
        long end = System.currentTimeMillis();
        int time = (int) (end - start);
        FMLLog.info("Finished initializing Esteemed Innovation OreDictHelper in %s ms", time);

        RecipeSorter.register(MOD_ID + ":drill_head", DrillHeadRecipe.class, RecipeSorter.Category.SHAPED, "before:forge:shapedore");
        DrillHeadMaterial.registerDefaults();
        ((ToolUpgradeItems) ItemCategories.TOOL_UPGRADES.getCategory()).postInit();
        if (event.getSide() == Side.CLIENT) {
            EsteemedInnovationJournal.registerSteamTools();
        }

        for (MiscellaneousCategories category : MiscellaneousCategories.values()) {
            if (category.isEnabled()) {
                category.getCategory().postInit(event);
            }
        }
    }


    private class EsteemedInnovationTab extends CreativeTabs {
        boolean isToolTab;

        public EsteemedInnovationTab(int index, String label, boolean toolTab) {
            super(index, label);
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
