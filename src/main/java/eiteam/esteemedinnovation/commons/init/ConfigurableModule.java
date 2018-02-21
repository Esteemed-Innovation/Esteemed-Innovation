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

    /**
     * @param configSetting The config_setting option as defined by the Minecraft/Forge recipe JSON structure.
     * @return Whether this recipe belongs to this content module.
     */
    boolean doesRecipeBelongTo(String configSetting);

    /**
     * Called from the {@link eiteam.esteemedinnovation.commons.recipe.ConfigConditionFactory} to determine whether
     * a recipe is enabled or not.
     * @param configSetting The config_setting option as defined by the Minecraft/Forge recipe JSON structure.
     * @return Whether this recipe should be enabled or not.
     */
    boolean isRecipeEnabled(String configSetting);
}
