package flaxbeard.steamcraft.init.blocks;

import flaxbeard.steamcraft.init.IInitCategory;

public enum BlockCategories {
    METAL_BLOCKS(new MetalBlocks()),
    CASTING_BLOCKS(new CastingBlocks()),
    STEAM_NETWORK_BLOCKS(new SteamNetworkBlocks()),
    STEAM_MACHINERY(new SteamMachineryBlocks()),
    MISCELLANEOUS(new MiscellaneousBlocks());

    private IInitCategory category;

    BlockCategories(IInitCategory category) {
        this.category = category;
    }

    public IInitCategory getCategory() {
        return category;
    }
}
