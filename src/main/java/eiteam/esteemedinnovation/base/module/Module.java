package eiteam.esteemedinnovation.base.module;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class Module {
	
	private String name;
	
	public Module(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setup(final FMLCommonSetupEvent event) {
	
	}
	
	public void setupClient(final FMLClientSetupEvent event) {
	
	}
}
