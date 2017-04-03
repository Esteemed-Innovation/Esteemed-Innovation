package eiteam.esteemedinnovation.materials.raw.config;

import net.minecraft.world.biome.Biome;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A simple matcher for Biomes, which should be used in the Ore config stuff instead of the actual Biome type.
 * It provides a method, {@link #matches(Biome)}, which returns whether the provided biome matches this one.
 * The BiomeMatcher allows for a null value to be passed as the Biome, which functions as a wildcard value ("*" in the config).
 */
public class BiomeMatcher {
    @Nullable
    private final Biome biome;

    public BiomeMatcher(@Nullable Biome biome) {
        this.biome = biome;
    }

    boolean matches(@SuppressWarnings("TypeMayBeWeakened") @Nonnull Biome other) {
        return biome == null || biome.getRegistryName().equals(other.getRegistryName());
    }

    @Nullable
    Biome getBiome() {
        return biome;
    }
}
