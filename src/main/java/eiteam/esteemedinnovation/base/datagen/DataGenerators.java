package eiteam.esteemedinnovation.base.datagen;

import eiteam.esteemedinnovation.base.EsteemedInnovation;
import eiteam.esteemedinnovation.base.module.Module;
import net.minecraft.data.IDataProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = EsteemedInnovation.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        if (event.includeServer()) {
            for (Module module : EsteemedInnovation.instance.moduleManager.getModules().values()) {
                for (IDataProvider provider : module.getDataProviders(event.getGenerator(), event.getExistingFileHelper())) {
                    event.getGenerator().addProvider(provider);
                }
            }
        }
    }
}
