package eiteam.esteemedinnovation.materials.raw.config;

import com.google.gson.*;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.commons.OreDictEntries;
import net.minecraft.block.Block;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OreConfigurationParser {
    private OreGenerationDefinition[] ores;
    private String filename;
    private Gson gson;

    public OreConfigurationParser(String filename) {
        this.filename = filename;
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Tries to write the default ores if the file does not exist, then parses the file as JSON into this object's
     * {@link #ores} array.
     */
    public void load() throws IOException {
        writeDefault();
        parse();
    }

    /**
     * @return All of the OreGenerationDefinitions for this config file.
     */
    public OreGenerationDefinition[] getOres() {
        return ores;
    }

    /**
     * @return The file name for this configuration object.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * The default replaceable OreDict values for surface ores (dim 0).
     */
    private static final List<String> DEFAULT_SURFACE_REPLACEABLE_OREDICTS = new ArrayList<String>() {{
        add(OreDictEntries.STONE_ORE);
        add(OreDictEntries.DIRT_ORE);
        add(OreDictEntries.SAND_ORE);
        add(OreDictEntries.SANDSTONE_ORE);
        add(OreDictEntries.GRAVEL_ORE);
        add(OreDictEntries.GRASS_ORE);
    }};

    private static final List<Pair<Block, Integer>> DEFAULT_MESA_REPLACEABLE_BLOCKS = new ArrayList<Pair<Block, Integer>>() {{
        add(Pair.of(Blocks.HARDENED_CLAY, OreDictionary.WILDCARD_VALUE));
        add(Pair.of(Blocks.STAINED_HARDENED_CLAY, OreDictionary.WILDCARD_VALUE));
    }};

    /**
     * The default replaceable OreDict values for nether ores (dim -1).
     */
    private static final List<String> DEFAULT_NETHER_REPLACEABLE_OREDICTS = new ArrayList<String>() {{
        add(OreDictEntries.NETHERRACK_ORE);
    }};

    /**
     * The default replaceable OreDict values for end ores (dim 1).
     */
    private static final List<String> DEFAULT_END_REPLACEABLE_OREDICTS = new ArrayList<String>() {{
        add(OreDictEntries.ENDSTONE_ORE);
    }};

    /**
     * The default {@link OreGenerationDefinition}s, providing the following:
     *
     * Copper:
     * - Extreme Hills (all kinds), overworld, Y80-128, 5 per chunk
     * - Hell, nether dimension, Y0-128, 10 per chunk
     * - Sky, end dimension, Y0-128, 10 per chunk
     *
     * Zinc:
     * - Desert (and hills), overworld, Y65-80, 5 per chunk
     * - Mesa (all kinds), overworld, Y65-80, 5 per chunk
     * - Hell, nether dimension, Y0-128, 10 per chunk
     * - Sky, end dimension, Y0-128, 10 per chunk
     */
    private static final OreGenerationDefinition[] DEFAULT_ORES = {
      new OreGenerationDefinition(
        new BiomeDefinition[] {
          new BiomeDefinition(0, Biomes.EXTREME_HILLS, 60, 90, 8, 5, DEFAULT_SURFACE_REPLACEABLE_OREDICTS, new ArrayList<>()),
          new BiomeDefinition(0, Biomes.EXTREME_HILLS_EDGE, 60, 90, 8, 5, DEFAULT_SURFACE_REPLACEABLE_OREDICTS, new ArrayList<>()),
          new BiomeDefinition(0, Biomes.EXTREME_HILLS_WITH_TREES, 60, 90, 8, 5, DEFAULT_SURFACE_REPLACEABLE_OREDICTS, new ArrayList<>()),
          new BiomeDefinition(-1, Biomes.HELL, 0, 128, 8, 10, DEFAULT_NETHER_REPLACEABLE_OREDICTS, new ArrayList<>()),
          new BiomeDefinition(1, Biomes.SKY, 0, 128, 8, 10, DEFAULT_END_REPLACEABLE_OREDICTS, new ArrayList<>())
        },
        OreDictEntries.MATERIAL_COPPER),
      new OreGenerationDefinition(
        new BiomeDefinition[] {
          new BiomeDefinition(0, Biomes.DESERT, 40, 70, 8, 5, DEFAULT_SURFACE_REPLACEABLE_OREDICTS, new ArrayList<>()),
          new BiomeDefinition(0, Biomes.DESERT_HILLS, 60, 80, 8, 5, DEFAULT_SURFACE_REPLACEABLE_OREDICTS, new ArrayList<>()),
          new BiomeDefinition(0, Biomes.MESA, 40, 80, 8, 5, DEFAULT_SURFACE_REPLACEABLE_OREDICTS, DEFAULT_MESA_REPLACEABLE_BLOCKS),
          new BiomeDefinition(0, Biomes.MESA_CLEAR_ROCK, 40, 80, 8, 5, DEFAULT_SURFACE_REPLACEABLE_OREDICTS, DEFAULT_MESA_REPLACEABLE_BLOCKS),
          new BiomeDefinition(0, Biomes.MESA_ROCK, 40, 80, 8, 5, DEFAULT_SURFACE_REPLACEABLE_OREDICTS, DEFAULT_MESA_REPLACEABLE_BLOCKS),
          new BiomeDefinition(-1, Biomes.HELL, 0, 128, 8, 10, DEFAULT_NETHER_REPLACEABLE_OREDICTS, new ArrayList<>()),
          new BiomeDefinition(1, Biomes.SKY, 0, 128, 8, 10, DEFAULT_END_REPLACEABLE_OREDICTS, new ArrayList<>())
        },
        OreDictEntries.MATERIAL_ZINC)
    };

    /**
     * Converts a list of strings into a JsonArray of strings.
     */
    private JsonArray writeStringsToArray(Iterable<String> strings) {
        JsonArray ary = new JsonArray();
        for (String entry : strings) {
            ary.add(gson.fromJson(entry, JsonElement.class));
        }
        return ary;
    }

    /**
     * Converts a list of Block;Integer pairs into a JsonArray of strings. The pair is turned into the format
     * domain:path(:metadata) where () is optional.
     */
    private JsonArray writeBlocksToArray(Iterable<Pair<Block, Integer>> blocks) {
        JsonArray ary = new JsonArray();
        for (Pair<Block, Integer> entry : blocks) {
            Block block = entry.getLeft();
            Integer meta = entry.getRight();
            StringBuilder out = new StringBuilder();
            out.append(block.getRegistryName());
            if (meta != OreDictionary.WILDCARD_VALUE) {
                out.append(":").append(meta);
            }
            ary.add(new JsonPrimitive(out.toString()));
        }
        return ary;
    }

    /**
     * Converts a {@link BiomeDefinition} into a JsonObject, with keys "Dimension", "Biome", "MinY", "MaxY",
     * "MaxVeinSize", "MaxVeinsPerChunk", and "ReplaceableBlocks".
     */
    private JsonObject writeBiomeToObject(BiomeDefinition biome) {
        JsonObject obj = new JsonObject();
        obj.addProperty("Dimension", biome.getDimension());
        Biome actualBiome = biome.getBiomeMatcher().getBiome();
        obj.addProperty("Biome", actualBiome == null ? "*" : actualBiome.getRegistryName().toString());
        obj.addProperty("MinY", biome.getMinY());
        obj.addProperty("MaxY", biome.getMaxY());
        obj.addProperty("MaxVeinSize", biome.getMaxVeinSize());
        obj.addProperty("MaxVeinsPerChunk", biome.getMaxVeinsPerChunk());

        JsonArray oreDicts = writeStringsToArray(biome.getReplaceableBlocksOreDict());
        JsonArray blocks = writeBlocksToArray(biome.getReplaceableBlocksAndMeta());
        blocks.addAll(oreDicts);
        obj.add("ReplaceableBlocks", blocks);

        return obj;
    }

    /**
     * Converts an array of {@link BiomeDefinition}s into a JsonArray of JsonObjects.
     * @see #writeBiomeToObject(BiomeDefinition)
     */
    private JsonArray writeBiomesToArray(BiomeDefinition[] biomes) {
        JsonArray ary = new JsonArray();
        for (BiomeDefinition biome : biomes) {
            ary.add(writeBiomeToObject(biome));
        }
        return ary;
    }

    /**
     * Writes the default ores ({@link #DEFAULT_ORES}) to the file ({@link #getFilename()}) using all of the write
     * methods.
     * <br />
     * If the file already exists, it does not write the default ores to this file and returns without modifying it at
     * all.
     */
    private void writeDefault() throws IOException {
        File file = new File(getFilename());
        if (file.exists() && file.isFile()) {
            return;
        }

        JsonObject main = new JsonObject();

        JsonObject ores = new JsonObject();
        for (OreGenerationDefinition ore : DEFAULT_ORES) {
            ores.add(ore.getOreName(), writeBiomesToArray(ore.getBiomeDefinitions()));
        }
        main.add("Ores", ores);
        File parent = file.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        try (Writer writer = new BufferedWriter(new FileWriter(getFilename()))) {
            writer.write(gson.toJson(main));
        }
    }

    /**
     * Converts a JsonArray of strings into a List of strings. It excludes all invalid OreDict names, for
     * safety, and to skip domain:path(:metadata) values in ReplaceableBlocks.
     */
    private List<String> getOreDictsFromArray(JsonArray ary) {
        List<String> oreDicts = new ArrayList<>();
        ary.forEach((element) -> {
            String str = element.getAsString();
            if (OreDictionary.doesOreNameExist(str)) {
                oreDicts.add(str);
            }
        });
        return oreDicts;
    }

    /**
     * Converts a JsonArray of strings into a List of Block;Integer pairs. It expects string values in the format
     * domain:path(:metadata) where () is optional.
     */
    private List<Pair<Block, Integer>> getBlockMetaPairsFromArray(JsonArray ary) {
        List<Pair<Block, Integer>> blockMetaPairs = new ArrayList<>();
        ary.forEach((element) -> {
            String str = element.getAsString();
            String[] pieces = str.split(":");

            String domain = "minecraft";
            String path;
            int meta = OreDictionary.WILDCARD_VALUE;
            if (pieces.length == 1) {
                path = pieces[0];
            } else if (pieces.length == 2) {
                domain = pieces[0];
                path = pieces[1];
            } else {
                if (pieces.length > 3) {
                    EsteemedInnovation.logger.warn("More than 3 values separated by : in ReplaceableBlocks '%s' for Ore generation config. Expected maximum: 'domain:path:meta'. This may indicate a bug in your config!", str);
                }
                domain = pieces[0];
                path = pieces[1];
                meta = Integer.valueOf(pieces[2]);
            }
            blockMetaPairs.add(Pair.of(Block.REGISTRY.getObject(new ResourceLocation(domain, path)), meta));
        });
        return blockMetaPairs;
    }

    /**
     * Converts a JsonObject into a {@link BiomeDefinition}. It expects "Biome", "ReplaceableBlocks", "Dimension",
     * "MinY", "MaxY", "MaxVeinSize", and "MaxVeinsPerChunk" keys.
     */
    private BiomeDefinition parseBiome(JsonObject obj) {
        JsonArray replaceableBlocksAry = obj.getAsJsonArray("ReplaceableBlocks");
        List<String> oreDicts = getOreDictsFromArray(replaceableBlocksAry);
        List<Pair<Block, Integer>> blockMetaPairs = getBlockMetaPairsFromArray(replaceableBlocksAry);

        String biomeString = obj.get("Biome").getAsString();
        BiomeMatcher biomeMatcher = new BiomeMatcher("*".equals(biomeString) ? null : Biome.REGISTRY.getObject(new ResourceLocation(biomeString)));

        return new BiomeDefinition(obj.get("Dimension").getAsInt(), biomeMatcher, obj.get("MinY").getAsInt(),
          obj.get("MaxY").getAsInt(), obj.get("MaxVeinSize").getAsInt(), obj.get("MaxVeinsPerChunk").getAsInt(),
          oreDicts, blockMetaPairs);
    }

    /**
     * Converts a JsonObject into a list of {@link OreGenerationDefinition}s. The JsonObject is expected to be in the
     * format of { "OreName": {object} }, where object is a {@link BiomeDefinition} parsed using
     * {@link #parseBiome(JsonObject)}. It skips every OreName that ends with "comment" (because some people like
     * _comment, some like comment, some like __comment, etc.).
     */
    private List<OreGenerationDefinition> parseOreArrayObject(JsonObject object) {
        List<OreGenerationDefinition> ores = new ArrayList<>();
        for (Map.Entry<String, JsonElement> ore : object.entrySet()) {
            String oreName = ore.getKey();
            if (oreName.endsWith("comment")) {
                continue;
            }
            if (oreName.equals(OreDictEntries.MATERIAL_COPPER) || oreName.equals(OreDictEntries.MATERIAL_ZINC)) {
                JsonElement value = ore.getValue();
                if (value.isJsonArray()) {
                    JsonArray array = value.getAsJsonArray();
                    List<BiomeDefinition> biomes = new ArrayList<>();
                    array.forEach((element) -> {
                        if (element.isJsonObject()) {
                            biomes.add(parseBiome(element.getAsJsonObject()));
                        } else {
                            throw new IllegalArgumentException(String.format("A value in %s array is not an object.", oreName));
                        }
                    });
                    ores.add(new OreGenerationDefinition(biomes.toArray(new BiomeDefinition[biomes.size()]), oreName));
                } else {
                    throw new IllegalArgumentException(String.format("Ore value for %s must be an array.", oreName));
                }
            } else {
                throw new IllegalArgumentException(String.format("Ore %s is defined, but is not a valid material. Only 'Zinc' and 'Copper' are allowed.", oreName));
            }
        }
        return ores;
    }

    /**
     * Parses the file name's JSON into the ores and fallbacks arrays.
     *
     * Expects the file to already exist, so it's likely that before calling you should call writeDefault().
     */
    private void parse() throws IOException {
        JsonParser parser = new JsonParser();
        JsonObject main = parser.parse(FileUtils.readFileToString(new File(getFilename()))).getAsJsonObject();
        List<OreGenerationDefinition> ores = parseOreArrayObject(main.getAsJsonObject("Ores"));
        this.ores = ores.toArray(new OreGenerationDefinition[ores.size()]);
    }
}
