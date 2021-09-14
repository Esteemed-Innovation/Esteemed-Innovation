package eiteam.esteemedinnovation.modules.transport;

import eiteam.esteemedinnovation.api.network.NetworkRegistry;
import eiteam.esteemedinnovation.base.EsteemedInnovation;
import eiteam.esteemedinnovation.base.module.Module;
import eiteam.esteemedinnovation.modules.transport.steam.SteamNetworkFactory;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TransportModule extends Module {
    
    public static final ResourceLocation STEAM_NETWORK_FACTORY = new ResourceLocation(EsteemedInnovation.MODID, "steam");
    
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, EsteemedInnovation.MODID);
    
    public static final ResourceLocation STEAM_STILL_RESOURCE = new ResourceLocation(EsteemedInnovation.MODID, "block/steam");
    public static final ResourceLocation STEAM_FLOWING_RESOURCE = new ResourceLocation(EsteemedInnovation.MODID, "block/steam");
    public static final ResourceLocation STEAM_OVERLAY_RESOURCE = new ResourceLocation(EsteemedInnovation.MODID, "block/steam");
    
    public static final RegistryObject<Fluid> STEAM_SOURCE = FLUIDS.register("steam", () -> new ForgeFlowingFluid.Source(makeSteamProperties()));
    public static final RegistryObject<Fluid> STEAM_FLOWING = FLUIDS.register("steam_flowing", () -> new ForgeFlowingFluid.Flowing(makeSteamProperties()));
    
    public TransportModule(String name) {
        super(name);
        FLUIDS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    @Override
    public void setup(FMLCommonSetupEvent event) {
        NetworkRegistry.INSTANCE.addFactory(STEAM_NETWORK_FACTORY, new SteamNetworkFactory());
    }
    
    private static ForgeFlowingFluid.Properties makeSteamProperties() {
        return new ForgeFlowingFluid.Properties(STEAM_SOURCE, STEAM_FLOWING,
          FluidAttributes.builder(STEAM_STILL_RESOURCE, STEAM_FLOWING_RESOURCE)
            .overlay(STEAM_OVERLAY_RESOURCE)
            .density(-60)
            .temperature(750)
            .viscosity(200)
            .gaseous());
    }
}
