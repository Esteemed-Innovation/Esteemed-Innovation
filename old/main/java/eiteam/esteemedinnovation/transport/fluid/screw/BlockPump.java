package eiteam.esteemedinnovation.transport.fluid.screw;

import eiteam.esteemedinnovation.api.block.BlockSteamTransporter;
import eiteam.esteemedinnovation.api.wrench.Wrenchable;
import eiteam.esteemedinnovation.commons.util.WorldHelper;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockPump extends BlockSteamTransporter implements Wrenchable {
    public static PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockPump() {
        super(Material.IRON);
        setHardness(5F);
        setResistance(10F);
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase elb, ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, elb.getHorizontalFacing()), 2);
    }

//    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
//		return null;
//    }
//
//    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int xp, int yp, int zp)
//    {
//    	int meta = blockAccess.getBlockMetadata(xp, yp, zp);
//    	float px = 1.0F/16.0F;
//		float x = 0;
//		float y = 0;
//		float z = 5*px;
//		float x2 = 15*px;
//		float y2 = 12*px;
//		float z2 = 11*px;
//		switch (meta) {
//		case 0:
//			this.setBlockBounds(z, y, x, z2, y2, x2);
//			break;
//		case 1:
//			this.setBlockBounds(1-x2, y, 1-z2, 1-x, y2, 1-z);
//			break;
//		case 2:	
//			this.setBlockBounds(1-z2, y, 1-x2, 1-z, y2, 1-x);
//			break;
//		case 3:
//			this.setBlockBounds(x, y, z, x2, y2, z2);
//			break;
//
//		}
//    }
//

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityPump();
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, IBlockState state, float hitX, float hitY, float hitZ) {
        if (facing == EnumFacing.DOWN || facing == EnumFacing.UP) {
            return false;
        }
        WorldHelper.rotateProperly(FACING, world, state, pos, facing);
        return true;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return false;
    }
}