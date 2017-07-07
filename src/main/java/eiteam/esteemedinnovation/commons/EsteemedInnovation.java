package eiteam.esteemedinnovation.commons;

import eiteam.esteemedinnovation.api.Constants;
import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.api.util.SPLog;
import eiteam.esteemedinnovation.book.BookModule;
import eiteam.esteemedinnovation.commons.capabilities.player.PlayerData;
import eiteam.esteemedinnovation.commons.capabilities.player.PlayerDataStorage;
import eiteam.esteemedinnovation.commons.handler.GenericEventHandler;
import eiteam.esteemedinnovation.commons.handler.GenericTickHandler;
import eiteam.esteemedinnovation.commons.handler.GuiHandler;
import eiteam.esteemedinnovation.commons.init.ContentModuleHandler;
import eiteam.esteemedinnovation.commons.network.CamoPacket;
import eiteam.esteemedinnovation.commons.network.CamoPacketHandler;
import eiteam.esteemedinnovation.commons.network.JumpValueChangePacket;
import eiteam.esteemedinnovation.commons.network.JumpValueChangePacketHandler;
import eiteam.esteemedinnovation.commons.util.OreDictHelper;
import eiteam.esteemedinnovation.commons.util.ReflectionHelper;
import eiteam.esteemedinnovation.metalcasting.MetalcastingBookSection;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextFormatting;
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
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.List;

import static eiteam.esteemedinnovation.book.BookModule.BOOK;
import static eiteam.esteemedinnovation.tools.ToolsModule.BRASS_PICKAXE;

@Mod(
  modid = EsteemedInnovation.MOD_ID,
  name = "Esteemed Innovation",
  version = Config.VERSION,
  dependencies = "required-after:CodeChickenLib@[2.4.2,);required-after:Baubles;required-after:" + Constants.API_MODID
)
public class EsteemedInnovation {
    // TODO: Migrate uses of MOD_ID to EI_MODID
    public static final String MOD_ID = Constants.EI_MODID;

    @Mod.Instance(MOD_ID)
    public static EsteemedInnovation instance;

    @CapabilityInject(PlayerData.class)
    public static final Capability<PlayerData> PLAYER_DATA = null;

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

    @SidedProxy(clientSide = "eiteam.esteemedinnovation.commons.ClientProxy", serverSide = "eiteam.esteemedinnovation.commons.CommonProxy")
    public static CommonProxy proxy;

    public static final String BASICS_SECTION = "section.Basics.name";
    public static final String FLINTLOCK_SECTION = "section.Flintlock.name";
    public static final String CASTING_SECTION = "section.MetalCasting.name";
    public static final String GADGET_SECTION = "section.Gadgets.name";
    public static final String STEAMPOWER_SECTION = "section.SteamPower.name";
    public static final String EXOSUIT_SECTION = "section.SteamExosuit.name";
    public static final String MISC_SECTION = "section.Misc.name";
    public static final String STEAMTOOL_SECTION = "section.SteamTools.name";

    private static SoundEvent registerSound(String soundName) {
        final ResourceLocation soundID = new ResourceLocation(MOD_ID, soundName);
        return GameRegistry.register(new SoundEvent(soundID).setRegistryName(soundID));
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Config.load();

        tab = new EsteemedInnovationTab(CreativeTabs.getNextID(), MOD_ID, false).setBackgroundImageName("item_search.png");
        tabTools = new EsteemedInnovationTab(CreativeTabs.getNextID(), MOD_ID + "Tools", true);

        upgrade = EnumHelper.addRarity("UPGRADE", TextFormatting.RED, "Upgrade");

        channel = NetworkRegistry.INSTANCE.newSimpleChannel("eiChannel");
        channel.registerMessage(CamoPacketHandler.class, CamoPacket.class, 0, Side.SERVER);
        channel.registerMessage(JumpValueChangePacketHandler.class, JumpValueChangePacket.class, 3, Side.SERVER);

        SOUND_HISS = registerSound("hiss");
        SOUND_CANNON = registerSound("cannon");
        SOUND_LEAK = registerSound("leaking");
        SOUND_ROCKET = registerSound("rocket");
        SOUND_WRENCH = registerSound("wrench");
        SOUND_WHISTLE = registerSound("horn");

        ContentModuleHandler.preInit();

        CapabilityManager.INSTANCE.register(PlayerData.class, new PlayerDataStorage(),
          PlayerData.DefaultImplementation.class);

        proxy.registerTexturesToStitch();
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

        ReflectionHelper.init();
        MinecraftForge.EVENT_BUS.register(new GenericEventHandler());
        MinecraftForge.EVENT_BUS.register(new GenericTickHandler());

        ContentModuleHandler.init();
    }


    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
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

        // We set up all of the top categories before calling #finish, so that every module has access to every category.
        // Ideally we'd have a better way to deal with this. I can't really think of anything that is better though.
        BookPageRegistry.addSection(0, new BookSection(BASICS_SECTION));
        // This is actually the Preface section that starts the book. It's barely about the journal, so that's why it's
        // not in the BookModule. If it is in the BookModule it might get pushed into a strange location in the Basics
        // category.
        BookPageRegistry.addCategoryToSection(BASICS_SECTION, 0,
          new BookCategory("category.Book.name",
            new BookEntry("research.Book.name",
              new BookPageItem("research.Book.name", "research.Book.0", new ItemStack(BOOK)),
              new BookPageCrafting("", "book"))));
        BookPageRegistry.addSection(1, new BookSection(FLINTLOCK_SECTION));
        BookPageRegistry.addSection(2, new MetalcastingBookSection());
        BookPageRegistry.addSection(3, new BookSection(GADGET_SECTION));
        BookPageRegistry.addSection(4, new BookSection(STEAMPOWER_SECTION));
        BookPageRegistry.addSection(5, new BookSection(EXOSUIT_SECTION));
        BookPageRegistry.addSection(6, new BookSection(STEAMTOOL_SECTION));
        BookPageRegistry.addSection(7, new BookSection(MISC_SECTION));
        ContentModuleHandler.postInit();
        // FIXME: This is terrible object oriented design.
        BookModule.generateAllHints();
    }

    // FIXME: This is terrible OOD.
    private class EsteemedInnovationTab extends CreativeTabs {
        boolean isToolTab;

        public EsteemedInnovationTab(int index, String label, boolean toolTab) {
            super(index, label);
            isToolTab = toolTab;
        }


        @Nonnull
        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack getTabIconItem() {
            return new ItemStack(isToolTab ? BRASS_PICKAXE : BOOK);
        }

        @Override
        public boolean hasSearchBar() {
            return !isToolTab;
        }
    }
}
