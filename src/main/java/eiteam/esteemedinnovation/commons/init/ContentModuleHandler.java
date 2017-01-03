package eiteam.esteemedinnovation.commons.init;

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
import eiteam.esteemedinnovation.metalcasting.MetalcastingModule;
import eiteam.esteemedinnovation.metals.MetalsModule;
import eiteam.esteemedinnovation.misc.MiscellaneousModule;
import eiteam.esteemedinnovation.naturalphilosophy.NaturalPhilosophyModule;
import eiteam.esteemedinnovation.smasher.SmasherModule;
import eiteam.esteemedinnovation.steamsafety.SafetyModule;
import eiteam.esteemedinnovation.storage.StorageModule;
import eiteam.esteemedinnovation.thumper.ThumperModule;
import eiteam.esteemedinnovation.tools.ToolsModule;
import eiteam.esteemedinnovation.transport.TransportationModule;
import eiteam.esteemedinnovation.woodcone.WoodenConeModule;
import eiteam.esteemedinnovation.workshop.SteamWorkshopModule;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class ContentModuleHandler {
    private static final Set<ContentModule> modules = new HashSet<>();

    // This isn't preferential, however I fail to see good alternatives.
    static {
        registerAllModules(
          new ArmorModule(),
          new BoilerModule(),
          new BookModule(),
          new BuzzsawModule(),
          new ChargingModule(),
          new ConverterModule(),
          new EngineeringTableModule(),
          new FirearmModule(),
          new FishFarmModule(),
          new HammerModule(),
          new HeaterModule(),
          new IslandModule(),
          new MetalcastingModule(),
          new MetalsModule(),
          new MiscellaneousModule(),
          new NaturalPhilosophyModule(),
          new SmasherModule(),
          new SafetyModule(),
          new StorageModule(),
          new ThumperModule(),
          new ToolsModule(),
          new TransportationModule(),
          new WoodenConeModule(),
          new SteamWorkshopModule()
        );
    }

    public static void registerModule(ContentModule module) {
        modules.add(module);
    }

    public static void registerAllModules(ContentModule... modules) {
        ContentModuleHandler.modules.addAll(Arrays.asList(modules));
    }

    public static void preInit() {
        doForEachModule(false, module -> { module.create(getSide()); module.oreDict(getSide()); }, ContentModule::preInitClient);
    }

    public static void init() {
        doForEachModule(false, (contentModule) -> contentModule.recipes(getSide()), ContentModule::initClient);
    }

    public static void postInit() {
        doForEachModule(true, (contentModule) -> contentModule.finish(getSide()), ContentModule::postInitClient);
    }

    private static boolean isClient() {
        return getSide() == Side.CLIENT;
    }

    private static Side getSide() {
        return FMLCommonHandler.instance().getEffectiveSide();
    }

    private static void doForEachModule(boolean clientFirst, Consumer<ContentModule> common, Consumer<ContentModule> client) {
        boolean isClient = isClient();
        for (ContentModule module : modules) {
            if (clientFirst) {
                if (isClient) {
                    client.accept(module);
                }
                common.accept(module);
            } else {
                common.accept(module);
                if (isClient) {
                    client.accept(module);
                }
            }
        }
    }
}
