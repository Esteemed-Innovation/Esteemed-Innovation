package flaxbeard.steamcraft.api.block;

import net.minecraft.block.Block;

public interface IDisguisableBlock {
    public Block getDisguiseBlock();

    public void setDisguiseBlock(Block block);

    public int getDisguiseMeta();

    public void setDisguiseMeta(int meta);
}
