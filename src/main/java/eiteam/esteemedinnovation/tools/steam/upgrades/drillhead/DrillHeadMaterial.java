package eiteam.esteemedinnovation.tools.steam.upgrades.drillhead;

import com.google.gson.*;
import eiteam.esteemedinnovation.api.APIConfig;
import eiteam.esteemedinnovation.commons.OreDictEntries;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DrillHeadMaterial {
    /**
     * The materials organized by their ID
     * Key: material name (e.g., "iron")
     * Value: Material
     */
    public static HashMap<String, DrillHeadMaterial> materials = new HashMap<>();

    /**
     * The materials organized by their oredicts.
     * Key: material name (e.g., "iron").
     * Value: Pair of all oredict entries "ingotMaterial" (left) and "nuggetMaterial" (right).
     */
    public static HashMap<String, Pair<List<ItemStack>, List<ItemStack>>> materialOres = new HashMap<>();

    /**
     * The materials organized by their oredicts.
     * Key: material name (e.g., "diamond").
     * Value: All oredict entries (e.g., gemDiamond entries).
     */
    public static HashMap<String, List<ItemStack>> nonStandardMaterials = new HashMap<>();

    public String materialName;
    public String locName;
    public int harvestLevel;
    public boolean standard;
    public String oreName;
    private String color;

    public DrillHeadMaterial(String materialIngot, String locName, int harvestLevel, boolean standard, String color) {
        this(materialIngot, locName, harvestLevel, standard, color, null);
    }

    public DrillHeadMaterial(String materialIngot, String locName, int harvestLevel, boolean standard, String color, String oreName) {
        this.materialName = materialIngot;
        this.locName = locName;
        this.harvestLevel = harvestLevel;
        this.standard = standard;
        this.oreName = oreName;
        this.color = color;
    }

    /**
     * Gets the color as the RGB integer used internally by Minecraft.
     * @return The color integer.
     */
    public int getColorInt() {
        return new Color(Integer.parseInt(color.replace("#", ""), 16)).getRGB();
    }

    /**
     * Gets the color's base string, just in case you want to do that.
     * @return The color string (including the # if present).
     */
    public String getColorString() {
        return color;
    }

    /**
     * Basic toString() implementation for the material.
     * @return An English String representation of the drill material.
     */
    public String toString() {
        String s = standard ? "Standard" : "Non-Standard";
        String o = oreName == null ? "" : "(" + oreName + ")";
        return String.format("%s DrillHeadMaterial %s %s, localized at %s, with %s harvest level",
          s, materialName, o, locName, harvestLevel);
    }

    /**
     * Registers a drill material.
     * @param materialName The material suffix, e.g., "iron" in "ingotIron" and "nuggetIron".
     * @param locName The localization key.
     * @param harvestLevel The harvest level this head provides.
     * @param color The color (#FFFFFF).
     */
    public static void registerDrillMaterial(String materialName, String locName, int harvestLevel, String color) {
        DrillHeadMaterial material = new DrillHeadMaterial(materialName, locName, harvestLevel, true, color);
        materials.put(materialName, material);
        List<ItemStack> ingots = OreDictionary.getOres(OreDictEntries.PREFIX_INGOT + materialName);
        List<ItemStack> nuggets = OreDictionary.getOres(OreDictEntries.PREFIX_NUGGET + materialName);
        materialOres.put(materialName, Pair.of(ingots, nuggets));
    }

    /**
     * Registers a drill material that does not use ingots.
     * @param material The material suffix, e.g., "diamond"
     * @param ore The ore dictionary entry to use, e.g., "gemDiamond"
     * @param loc The localization key.
     * @param harvestLevel The harvest level this head provides.
     * @param color The color (#FFFFFF).
     */
    public static void registerNonStandardDrillMaterial(String material, String ore, String loc, int harvestLevel, String color) {
        DrillHeadMaterial head = new DrillHeadMaterial(material, loc, harvestLevel, false, color, ore);
        materials.put(material, head);
        List<ItemStack> ores = OreDictionary.getOres(ore);
        nonStandardMaterials.put(material, ores);

    }

    /**
     * Registers all of the default drill head materials added by EI. If a JSON file at
     * minecraft/config/EI-materials.json is present, it will use that. Otherwise, it uses default
     * values. Using YAML instead of JSON for the materials was considered, but decided against
     * because Minecraft itself uses JSON in many cases, and Java's YAML libraries are terrible.
     *
     * This method must be called AFTER ore dictionary entries have been registered.
     */
    public static void registerDefaults() {
        File jsonFile = APIConfig.getConfigFile("DrillHeadMaterials.json");
        if (jsonFile.exists()) {
            try {
                String str = FileUtils.readFileToString(jsonFile);
                JsonElement jsonObject = new JsonParser().parse(str);

                if (jsonObject.isJsonObject()) {
                    Set<Map.Entry<String, JsonElement>> entries = ((JsonObject) jsonObject).entrySet();
                    if (entries != null) {
                        for (Map.Entry<String, JsonElement> entry : entries) {
                            JsonObject obj = entry.getValue().getAsJsonObject();
                            if (obj.get("standard").getAsBoolean()) {
                                registerDrillMaterial(entry.getKey(), obj.get("locName").getAsString(),
                                  obj.get("harvestLevel").getAsInt(), obj.get("color").getAsString());
                            } else {
                                registerNonStandardDrillMaterial(entry.getKey(), obj.get("oreName").getAsString(),
                                  obj.get("locName").getAsString(), obj.get("harvestLevel").getAsInt(),
                                  obj.get("color").getAsString());
                            }
                        }
                    }
                }
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        registerDrillMaterial(OreDictEntries.MATERIAL_GOLD, "drill.gold", 1, "#CED652");
        registerDrillMaterial(OreDictEntries.MATERIAL_IRON, "drill.iron", 2, "#DEDEDE");
        registerDrillMaterial(OreDictEntries.MATERIAL_BRASS, "drill.brass", 2, "#FABD3F");
        registerDrillMaterial(OreDictEntries.MATERIAL_GILDED_IRON, "drill.gilded_iron", 2, "#CED652");
        registerNonStandardDrillMaterial(OreDictEntries.MATERIAL_DIAMOND, OreDictEntries.GEM_DIAMOND, "drill.diamond", 3, "#29C6AD");
        registerNonStandardDrillMaterial(OreDictEntries.MATERIAL_EMERALD, OreDictEntries.GEM_EMERALD, "drill.emerald", 3, "#17DD62");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(materials);
        try {
            FileWriter writer = new FileWriter(jsonFile.getPath());
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
