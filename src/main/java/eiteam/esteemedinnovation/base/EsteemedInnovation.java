package eiteam.esteemedinnovation.base;


import eiteam.esteemedinnovation.base.module.Module;
import eiteam.esteemedinnovation.base.module.ModuleManager;
import eiteam.esteemedinnovation.modules.materials.MaterialsModule;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("esteemedinnovation")
public class EsteemedInnovation {
    
    public static final String MODID = "esteemedinnovation";
    public static final Logger LOGGER = LogManager.getLogger("EsteemedInnovation");
    public static EsteemedInnovation instance;
    
    public final ModuleManager moduleManager;
    
    private final ForgeConfigSpec configSpec;
    
    public EsteemedInnovation() {
        instance = this;
        moduleManager = new ModuleManager();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::reloadConfig);
        registerModules();
        registerModuleEvents();
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        moduleManager.setupConfig(configBuilder);
        configSpec = configBuilder.build();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, configSpec);
    }
    
    private void registerModules() {
        moduleManager.registerModule(new MaterialsModule());
    }
    
    private void registerModuleEvents() {
        for (Module module : moduleManager.getModules().values()) {
            if (module.hasEvents) {
                FMLJavaModLoadingContext.get().getModEventBus().register(module);
            }
        }
    }
    
    private void setup(final FMLCommonSetupEvent event) {
        moduleManager.setup(event);
    }
    
    private void setupClient(final FMLClientSetupEvent event) {
        moduleManager.setupClient(event);
    }
    
    private void reloadConfig(final ModConfig.Reloading configEvent) {
        if (MODID.equals(configEvent.getConfig().getModId())) {
            moduleManager.reloadConfig();
        }
    }
    
    public static ResourceLocation resourceLocation(String path) {
        return new ResourceLocation(MODID, path);
    }
    
    public static final ItemGroup ITEM_GROUP = new ItemGroup("esteemedInnovation") {
        @Override
        public ItemStack createIcon() {
            return ItemStack.EMPTY;
        }
    };
}
