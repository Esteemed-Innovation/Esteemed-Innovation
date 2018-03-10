package eiteam.esteemedinnovation.commons.init;

import net.minecraftforge.common.config.Configuration;

/**
 * An interface that is intended to be put onto classes inheriting {@link ContentModule}. It provides functionality for
 * handling configuration options inside of the ContentModule itself rather than in the base {@link eiteam.esteemedinnovation.commons.Config}
 * class.
 */
public interface ConfigurableModule {
    /**
     * Called after the main mod configuration file has been loaded, and before it has been saved. There is no need to
     * save it in this function, because it will be handled automatically.
     * @param config The Configuration object representing the loaded config file.
     */
    void loadConfigurationOptions(Configuration config);

    default boolean areCrucialOptionsEnabled() {
        return true;
    }
}
