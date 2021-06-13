package eiteam.esteemedinnovation.api;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class APIConfig {
    public static File getConfigFile(String fileName) {
        return new File(APIMod.INSTANCE.configDir.getPath() + "/" + fileName);
    }

    public static boolean safeMode;

    static void load() {
        Configuration config = new Configuration(getConfigFile("API.cfg"));
        config.load();

        safeMode = config.get("Other", "Enable safe mode (no explosions)", false).getBoolean();

        config.save();
    }
}
