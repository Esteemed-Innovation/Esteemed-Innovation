package eiteam.esteemedinnovation.base.module;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * Implemented by a {@link Module} that requires CLIENT configs.
 * CLIENT configs are client side only configs
 */
public interface IClientConfigProvider {
    void setupClientConfig(final ForgeConfigSpec.Builder builder);
}
