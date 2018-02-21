package eiteam.esteemedinnovation.commons.recipe;

import com.google.gson.JsonObject;
import eiteam.esteemedinnovation.commons.init.ConfigurableModule;
import eiteam.esteemedinnovation.commons.init.ContentModuleHandler;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.function.BooleanSupplier;

/**
 * This is a dependency in resources/recipes/_factories.json.
 */
@SuppressWarnings("unused")
public class ConfigConditionFactory implements IConditionFactory {
    @Override
    public BooleanSupplier parse(JsonContext context, JsonObject json) {
        String configSetting = JsonUtils.getString(json, "config_setting", "");

        Set<ConfigurableModule> matchingModules = ContentModuleHandler.findContentModulesForRecipe(configSetting);
        Collection<Boolean> results = new ArrayList<>();
        for (ConfigurableModule module : matchingModules) {
            results.add(module.isRecipeEnabled(configSetting));
        }
        return () -> results.stream().allMatch(result -> result);
    }
}
