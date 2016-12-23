package eiteam.esteemedinnovation.steamsafety.gauge;

import eiteam.esteemedinnovation.api.ISteamTransporter;
import eiteam.esteemedinnovation.steamsafety.disc.BlockRuptureDisc;
import eiteam.esteemedinnovation.transport.steam.TileEntitySteamPipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSteamGauge extends Block {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool ON_PIPE = PropertyBool.create("on_pipe");
    private static final float PX_MIN = 4 / 16F;
    private static final float PX_MAX = 12 / 16F;

    public BlockSteamGauge() {
        super(Material.IRON);
        setHardness(1F);
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, ON_PIPE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos.offset(state.getValue(FACING).getOpposite()));
        return state.withProperty(ON_PIPE, tile != null && tile instanceof TileEntitySteamPipe);
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null && te instanceof TileEntitySteamGauge) {
            return ((TileEntitySteamGauge) te).getComparatorOutput(state.getValue(FACING));
        }
        return 0;
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing dir) {
        TileEntity tile = world.getTileEntity(pos.offset(dir.getOpposite()));

        if (tile != null && tile instanceof ISteamTransporter) {
            ISteamTransporter trans = (ISteamTransporter) tile;
            return trans.acceptsGauge(dir.getOpposite());
        }
        return false;
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (pos != neighbor && tileEntity != null) {
            World actualWorld = tileEntity.getWorld();
            IBlockState state = world.getBlockState(pos);
            EnumFacing dir = state.getValue(FACING);
            if (!canPlaceBlockOnSide(actualWorld, pos, dir)) {
                dropBlockAsItem(actualWorld, pos, state, 0);
                actualWorld.setBlockToAir(pos);
            }
        }
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, side);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntitySteamGauge();
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos) {
        return null;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        boolean pipe = state.getActualState(source, pos).getValue(ON_PIPE);
        float minX = 4 * BlockRuptureDisc.UNIT;
        float minY = 4 * BlockRuptureDisc.UNIT;
        float minZ = pipe ? -4 * BlockRuptureDisc.UNIT : 0F;
        float maxX = 12 * BlockRuptureDisc.UNIT;
        float maxY = 12 * BlockRuptureDisc.UNIT;
        float maxZ = pipe ? -3 * BlockRuptureDisc.UNIT + 0.0005F : BlockRuptureDisc.UNIT;
        return BlockRuptureDisc.getDirectionalBoundingBox(state.getValue(FACING), minX, minY, minZ, maxX, maxY, maxZ, false);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    /*
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xf, float yf, float zf){
    	TileEntitySteamGauge gauge = (TileEntitySteamGauge)world.getTileEntity(x, y, z);
    	int pressurePerc = (int)(gauge.getPressure() * 100);
    	if (world.isRemote){
    		String color = "";

    		if (pressurePerc > 120){
    			color = "�4";
    		} else if (pressurePerc > 115){
    			color = "�c";
    		} else if (pressurePerc > 100){
    			color = "�e";
    		}

    		player.addChatComponentMessage(new ChatComponentText(color+"Current pressure: "+pressurePerc+"%"));

    	}

    	return true;
    }
    */
}
