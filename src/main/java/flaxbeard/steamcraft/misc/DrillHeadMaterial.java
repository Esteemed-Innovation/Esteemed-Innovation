package flaxbeard.steamcraft.misc;

import com.google.gson.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.MutablePair;

import flaxbeard.steamcraft.Steamcraft;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
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
    public static HashMap<String, MutablePair<ArrayList<ItemStack>, ArrayList<ItemStack>>> materialOres = new HashMap<>();

    /**
     * The materials organized by their oredicts.
     * Key: material name (e.g., "diamond").
     * Value: All oredict entries (e.g., gemDiamond entries).
     */
    public static HashMap<String, ArrayList<ItemStack>> nonStandardMaterials = new HashMap<>();

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
        DrillHeadMaterial material = new DrillHeadMaterial(materialName,locName, harvestLevel, true, color);
        materials.put(materialName, material);
        ArrayList<ItemStack> ingots = OreDictionary.getOres("ingot" + materialName);
        ArrayList<ItemStack> nuggets = OreDictionary.getOres("nugget" + materialName);
        materialOres.put(materialName, MutablePair.of(ingots, nuggets));
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
        ArrayList<ItemStack> ores = OreDictionary.getOres(ore);
        nonStandardMaterials.put(material, ores);

    }

    /**
     * Registers all of the default drill head materials added by FSP. If a JSON file at
     * minecraft/config/FSP-materials.json is present, it will use that. Otherwise, it uses default
     * values. Using YAML instead of JSON for the materials was considered, but decided against
     * because Minecraft itself uses JSON in many cases, and Java's YAML libraries are terrible.
     *
     * This method must be called AFTER ore dictionary entries have been registered.
     */
    public static void registerDefaults() {
        String jsonFilePath = Steamcraft.CONFIG_DIR + "/FSP-materials.json";
        File jsonFile = new File(jsonFilePath);
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
                                String color = obj.get("color").getAsString();
                                registerDrillMaterial(entry.getKey(), obj.get("locName").getAsString(),
                                  obj.get("harvestLevel").getAsInt(), obj.get("color").getAsString());
                            } else {
                                String color = obj.get("color").getAsString();
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

        registerDrillMaterial("Gold", "drill.gold", 1, "#CED652");
        registerDrillMaterial("Iron", "drill.iron", 2, "#DEDEDE");
        registerDrillMaterial("Brass", "drill.brass", 2, "#FABD3F");
        registerNonStandardDrillMaterial("Diamond", "gemDiamond", "drill.diamond", 3, "#29C6AD");
        registerNonStandardDrillMaterial("Emerald", "gemEmerald", "drill.emerald", 3, "#17DD62");
//        registerDrillMaterial("gildedGold", "drill.gilded", 2); TODO OreDictionary entry for gilded gold.

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(materials);
        try {
            FileWriter writer = new FileWriter(jsonFilePath);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
