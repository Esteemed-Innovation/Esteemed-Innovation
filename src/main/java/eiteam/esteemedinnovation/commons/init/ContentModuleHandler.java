package eiteam.esteemedinnovation.commons.init;

import eiteam.esteemedinnovation.api.Constants;
import eiteam.esteemedinnovation.armor.ArmorModule;
import eiteam.esteemedinnovation.boiler.BoilerModule;
import eiteam.esteemedinnovation.book.BookModule;
import eiteam.esteemedinnovation.buzzsaw.BuzzsawModule;
import eiteam.esteemedinnovation.charging.ChargingModule;
import eiteam.esteemedinnovation.converter.ConverterModule;
import eiteam.esteemedinnovation.engineeringtable.EngineeringTableModule;
import eiteam.esteemedinnovation.firearms.FirearmModule;
import eiteam.esteemedinnovation.fishfarm.FishFarmModule;
import eiteam.esteemedinnovation.hammer.HammerModule;
import eiteam.esteemedinnovation.heater.HeaterModule;
import eiteam.esteemedinnovation.island.IslandModule;
import eiteam.esteemedinnovation.materials.MaterialsModule;
import eiteam.esteemedinnovation.metalcasting.MetalcastingModule;
import eiteam.esteemedinnovation.misc.MiscellaneousModule;
import eiteam.esteemedinnovation.naturalphilosophy.NaturalPhilosophyModule;
import eiteam.esteemedinnovation.pendulum.PendulumModule;
import eiteam.esteemedinnovation.smasher.SmasherModule;
import eiteam.esteemedinnovation.steamsafety.SafetyModule;
import eiteam.esteemedinnovation.storage.StorageModule;
import eiteam.esteemedinnovation.thumper.ThumperModule;
import eiteam.esteemedinnovation.tools.ToolsModule;
import eiteam.esteemedinnovation.transport.TransportationModule;
import eiteam.esteemedinnovation.woodcone.WoodenConeModule;
import eiteam.esteemedinnovation.workshop.SteamWorkshopModule;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = Constants.EI_MODID)
public class ContentModuleHandler {
    private static final Set<ContentModule> modules = new HashSet<>();
    private static final Collection<ConfigurableModule> configurableModules = new HashSet<>();

    // This isn't preferential, however I fail to see good alternatives.
    static {
        registerModule(new ArmorModule());
        registerModule(new BoilerModule());
        registerModule(new BookModule());
        registerModule(new BuzzsawModule());
        registerModule(new ChargingModule());
        registerModule(new ConverterModule());
        registerModule(new EngineeringTableModule());
        registerModule(new FirearmModule());
        registerModule(new FishFarmModule());
        registerModule(new HammerModule());
        registerModule(new HeaterModule());
        registerModule(new IslandModule());
        registerModule(new MetalcastingModule());
        registerModule(new MaterialsModule());
        registerModule(new MiscellaneousModule());
        registerModule(new NaturalPhilosophyModule());
        registerModule(new SmasherModule());
        registerModule(new SafetyModule());
        registerModule(new StorageModule());
        registerModule(new ThumperModule());
        registerModule(new ToolsModule());
        registerModule(new TransportationModule());
        registerModule(new WoodenConeModule());
        registerModule(new SteamWorkshopModule());
        registerModule(new PendulumModule());
    }

    private static void registerModule(ContentModule module) {
        modules.add(module);
        if (module instanceof ConfigurableModule) {
            configurableModules.add((ConfigurableModule) module);
        }
    }

    public static void preInit() {
        Side side = getSide();
        boolean isClient = side == Side.CLIENT;
        for (ContentModule module : modules) {
            module.create(side);
            if (isClient) {
                module.preInitClient();
            }
        }
    }

    public static void init() {
        Side side = getSide();
        boolean isClient = side == Side.CLIENT;
        for (ContentModule module : modules) {
            module.oreDict(side);
            if (isClient) {
                module.initClient();
            }
        }
    }

    public static void postInit() {
        Side side = getSide();
        boolean isClient = side == Side.CLIENT;
        for (ContentModule module : modules) {
            if (isClient) {
                module.postInitClient();
            }
            module.finish(side);
        }
    }

    public static void loadConfigs(Configuration configuration) {
        for (ConfigurableModule module : configurableModules) {
            module.loadConfigurationOptions(configuration);
        }
    }

    public static Set<ConfigurableModule> findContentModulesForRecipe(String configSetting) {
        return configurableModules
          .stream()
          .filter(module -> module.doesRecipeBelongTo(configSetting))
          .collect(Collectors.toSet());
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        for (ContentModule module : modules) {
            module.registerBlocks(event);
        }
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        for (ContentModule module : modules) {
            module.registerItems(event);
        }
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        for (ContentModule module : modules) {
            module.recipes(event);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        for (ContentModule module : modules) {
            module.registerModels(event);
        }
    }

    private static Side getSide() {
        return FMLCommonHandler.instance().getEffectiveSide();
    }
}
