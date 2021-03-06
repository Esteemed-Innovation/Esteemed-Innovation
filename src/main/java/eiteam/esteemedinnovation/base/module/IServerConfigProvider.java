package eiteam.esteemedinnovation.base.module;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * Implemented by a {@link Module} that requires SERVER configs.
 * SERVER configs are per-world and automatically sync to connecting clients
 */
public interface IServerConfigProvider {
    void setupServerConfig(final ForgeConfigSpec.Builder builder);
}
