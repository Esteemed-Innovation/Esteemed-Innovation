package flaxbeard.steamcraft.misc;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockContainer extends Block {
    private Block myBlock;
    private int mySide;

    public BlockContainer(Block block, int side) {
        super(block.getMaterial());
        myBlock = block;
        mySide = side;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
        return side == mySide ? super.shouldSideBeRendered(blockAccess, x, y, z, side) : false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess p_149673_1_, int p_149673_2_, int p_149673_3_, int p_149673_4_, int p_149673_5_) {
        return myBlock.getIcon(p_149673_1_, p_149673_2_, p_149673_3_, p_149673_4_, p_149673_5_);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return myBlock.getIcon(side, meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon func_149735_b(int p_149735_1_, int p_149735_2_) {
        return myBlock.getIcon(p_149735_1_, p_149735_2_);
    }
}
