package eiteam.esteemedinnovation.modules.materials;

import eiteam.esteemedinnovation.base.ModNames;

public enum MetalTypes {
    COPPER(ModNames.COPPER),
    ZINC(ModNames.ZINC),
    BRASS(ModNames.BRASS),
    GILDED_IRON(ModNames.GILDED_IRON);
    
    private String name;
    
    MetalTypes(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public static MetalTypes[] getRawTypes() {
        return new MetalTypes[]{COPPER, ZINC};
    }
}
