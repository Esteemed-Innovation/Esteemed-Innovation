package eiteam.esteemedinnovation.base.module;

import eiteam.esteemedinnovation.base.EsteemedInnovation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.HashMap;
import java.util.Map;

public class ModuleManager {
    
    Map<String, Module> modules;
    
    public ModuleManager() {
        modules = new HashMap<>();
    }
    
    public Map<String, Module> getModules() {
        return modules;
    }
    
    public Module getModule(String name) {
        return modules.get(name);
    }
    
    public void registerModule(Module module) {
        modules.put(module.getName(), module);
        EsteemedInnovation.LOGGER.debug("Registered Module: " + module.getName());
    }
    
    public final void setupConfigs() {
        ForgeConfigSpec.Builder serverConfigBuilder = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder clientConfigBuilder = new ForgeConfigSpec.Builder();
        modules.entrySet().stream()
          .filter(entry -> entry.getValue() instanceof IServerConfigProvider).forEach(entry -> {
                serverConfigBuilder.push(entry.getKey());
                ((IServerConfigProvider) entry.getValue()).setupServerConfig(serverConfigBuilder);
                serverConfigBuilder.pop();
        });
        modules.entrySet().stream()
          .filter(entry -> entry.getValue() instanceof IClientConfigProvider).forEach(entry -> {
            clientConfigBuilder.push(entry.getKey());
            ((IClientConfigProvider) entry.getValue()).setupClientConfig(clientConfigBuilder);
            clientConfigBuilder.pop();
        });
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, serverConfigBuilder.build());
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, clientConfigBuilder.build());
    }
    
    public void setup(final FMLCommonSetupEvent event) {
        modules.forEach((name, module) -> module.setup(event));
    }
    
    public void setupClient(final FMLClientSetupEvent event) {
        modules.forEach((name, module) -> module.setupClient(event));
    }
}
