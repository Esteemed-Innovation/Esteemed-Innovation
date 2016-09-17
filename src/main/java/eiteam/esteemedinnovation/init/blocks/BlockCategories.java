package eiteam.esteemedinnovation.init.blocks;

import eiteam.esteemedinnovation.init.IInitCategory;

public enum BlockCategories {
    METAL_BLOCKS(new MetalBlocks()),
    CASTING_BLOCKS(new CastingBlocks()),
    STEAM_NETWORK_BLOCKS(new SteamNetworkBlocks()),
    PIPE_BLOCKS(new PipeBlocks()),
    STEAM_MACHINERY(new SteamMachineryBlocks()),
    PRESSURE_PLATES(new PressurePlateBlocks()),
    MISCELLANEOUS(new MiscellaneousBlocks());

    private IInitCategory category;

    BlockCategories(IInitCategory category) {
        this.category = category;
    }

    public IInitCategory getCategory() {
        return category;
    }
}
