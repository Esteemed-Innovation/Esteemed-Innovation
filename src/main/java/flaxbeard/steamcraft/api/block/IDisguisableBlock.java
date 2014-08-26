package flaxbeard.steamcraft.api.block;

import net.minecraft.block.Block;

public interface IDisguisableBlock {
	public Block getDisguiseBlock();
	public int getDisguiseMeta();
	public void setDisguiseBlock(Block block);
	public void setDisguiseMeta(int meta);
}
