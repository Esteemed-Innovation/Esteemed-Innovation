package eiteam.esteemedinnovation.init.blocks;

import eiteam.esteemedinnovation.init.InitCategory;

public enum BlockCategories {
    METAL_BLOCKS(new MetalBlocks()),
    CASTING_BLOCKS(new CastingBlocks()),
    STEAM_NETWORK_BLOCKS(new SteamNetworkBlocks()),
    PIPE_BLOCKS(new PipeBlocks()),
    STEAM_MACHINERY(new SteamMachineryBlocks()),
    PRESSURE_PLATES(new PressurePlateBlocks()),
    MISCELLANEOUS(new MiscellaneousBlocks()),
    ORE_BLOCKS(new OreBlocks());

    private InitCategory category;

    BlockCategories(InitCategory category) {
        this.category = category;
    }

    public InitCategory getCategory() {
        return category;
    }
}
