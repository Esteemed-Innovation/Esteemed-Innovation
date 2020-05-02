package eiteam.esteemedinnovation.base.datagen;

import eiteam.esteemedinnovation.base.EsteemedInnovation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = EsteemedInnovation.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
	
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		if (event.includeServer()) {
			event.getGenerator().addProvider(new ItemModelProvider(event.getGenerator(), event.getExistingFileHelper()));
			event.getGenerator().addProvider(new BlockStateProvider(event.getGenerator(), event.getExistingFileHelper()));
		}
	}
}
