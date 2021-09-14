package eiteam.esteemedinnovation.base;


import eiteam.esteemedinnovation.api.network.NetworkManager;
import eiteam.esteemedinnovation.api.network.NetworkRegistry;
import eiteam.esteemedinnovation.base.module.ModuleManager;
import eiteam.esteemedinnovation.modules.materials.MaterialsModule;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
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
    
    
    public EsteemedInnovation() {
        instance = this;
        moduleManager = new ModuleManager();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
        registerModules();
        moduleManager.setupConfigs();
        
        MinecraftForge.EVENT_BUS.addListener(this::onWorldTick);
    }
    
    private void registerModules() {
        moduleManager.registerModule(new MaterialsModule());
    }
    
    private void setup(final FMLCommonSetupEvent event) {
        moduleManager.setup(event);
    }
    
    private void setupClient(final FMLClientSetupEvent event) {
        moduleManager.setupClient(event);
    }
    
    private void onWorldTick(TickEvent.WorldTickEvent event) {
        if(!event.world.isRemote && event.phase == TickEvent.Phase.START) {
            NetworkManager.get(event.world).getNetworkData().update();
        }
    }
    
    public static ResourceLocation resourceLocation(String path) {
        return new ResourceLocation(MODID, path);
    }
    
    public static ResourceLocation forgeLocation(String path) {
        return new ResourceLocation("forge", path);
    }
    
    public static final ItemGroup ITEM_GROUP = new ItemGroup("esteemedInnovation") {
        @Override
        public ItemStack createIcon() {
            return ItemStack.EMPTY;
        }
    };
}
