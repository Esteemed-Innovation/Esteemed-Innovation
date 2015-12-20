package flaxbeard.steamcraft.api.block;

import net.minecraft.block.Block;

public interface IDisguisableBlock {
    Block getDisguiseBlock();

    void setDisguiseBlock(Block block);

    int getDisguiseMeta();

    void setDisguiseMeta(int meta);
}
