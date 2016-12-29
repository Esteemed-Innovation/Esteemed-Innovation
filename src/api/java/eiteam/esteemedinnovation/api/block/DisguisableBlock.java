package eiteam.esteemedinnovation.api.block;

import net.minecraft.block.Block;

public interface DisguisableBlock {
    Block getDisguiseBlock();

    void setDisguiseBlock(Block block);

    int getDisguiseMeta();

    void setDisguiseMeta(int meta);
}
