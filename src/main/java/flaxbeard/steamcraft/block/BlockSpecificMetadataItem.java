package flaxbeard.steamcraft.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class BlockSpecificMetadataItem extends ItemBlock {
	
	public BlockSpecificMetadataItem(Block p_i45328_1_) {
		super(p_i45328_1_);
	}

	public int getMetadata(int par1)
	{
		return 3;
	}
}
