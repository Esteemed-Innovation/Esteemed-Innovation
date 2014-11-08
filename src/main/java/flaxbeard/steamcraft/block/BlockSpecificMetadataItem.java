package flaxbeard.steamcraft.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class BlockSpecificMetadataItem extends ItemBlock {

    public BlockSpecificMetadataItem(Block block) {
        super(block);
    }

    @Override
    public int getMetadata(int par1) {
        return 3;
    }
}
