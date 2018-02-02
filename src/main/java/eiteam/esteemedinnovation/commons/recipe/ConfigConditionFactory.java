package eiteam.esteemedinnovation.commons.recipe;

import com.google.gson.JsonObject;
import eiteam.esteemedinnovation.commons.Config;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

import java.util.function.BooleanSupplier;

public class ConfigConditionFactory implements IConditionFactory {
    @Override
    public BooleanSupplier parse(JsonContext context, JsonObject json) {
        String configSetting = JsonUtils.getString(json, "config_setting", "");

        switch (configSetting) {
            //TODO: add config options here
            case "enableAstrolabe": return () -> Config.enableAstrolabe;
            default:
                return () -> false;
        }
    }
}
