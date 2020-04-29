package eiteam.esteemedinnovation.base;


import eiteam.esteemedinnovation.base.module.ModuleManager;
import eiteam.esteemedinnovation.modules.materials.MaterialsModule;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
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
    
    public final ModuleManager moduleManager;
    
    public EsteemedInnovation() {
        moduleManager = new ModuleManager();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
        registerModules();
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
    
    public static final ItemGroup ITEM_GROUP = new ItemGroup("esteemedInnovation") {
        @Override
        public ItemStack createIcon() {
            return ItemStack.EMPTY;
        }
    };
}
