package eiteam.esteemedinnovation.materials.raw.config;

import eiteam.esteemedinnovation.materials.raw.BlockGenericOre;

public class OreGenerationDefinition {
    private final BiomeDefinition[] biomeDefinitions;
    private final String oreName;

    public OreGenerationDefinition(BiomeDefinition[] biomeDefinitions, String oreName) {
        this.biomeDefinitions = biomeDefinitions;
        this.oreName = oreName;
    }

    public BiomeDefinition[] getBiomeDefinitions() {
        return biomeDefinitions;
    }

    public String getOreName() {
        return oreName;
    }

    public BlockGenericOre.OreBlockTypes getOreType(int dimension) {
        BlockGenericOre.OreBlockTypes fallback = null;
        for (BlockGenericOre.OreBlockTypes type : BlockGenericOre.OreBlockTypes.LOOKUP) {
            if (type.getOreMaterial().equals(getOreName())) {
                fallback = type;
                if (type.getPreferredDimension() == dimension) {
                    return type;
                }
            }
        }
        return fallback;
    }
}
