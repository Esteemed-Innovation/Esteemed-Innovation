package eiteam.esteemedinnovation.transport.fluid.pipes;

import eiteam.esteemedinnovation.api.wrench.IWrenchable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import static eiteam.esteemedinnovation.transport.fluid.pipes.FluidPipeBlockCapabilities.*;
import static net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;

public class BlockColdFluidPipe extends Block implements IWrenchable {
    public static final int MAX_TEMPERATURE = 400;
    public static final int MIN_TEMPERATURE = 200;

    public BlockColdFluidPipe() {
        super(Material.IRON);
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, MODE, NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(MODE, Mode.META_LOOKUP[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(MODE).ordinal();
    }

    private boolean isFluidTransporter(IBlockAccess world, BlockPos pos, EnumFacing dir) {
        TileEntity tile = world.getTileEntity(pos.offset(dir));
        return tile != null && (tile.hasCapability(FLUID_HANDLER_CAPABILITY, dir) || tile instanceof IFluidHandler ||
          tile instanceof net.minecraftforge.fluids.IFluidHandler);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile != null && tile instanceof TileEntityColdFluidPipe) {
            return state
              .withProperty(NORTH, isFluidTransporter(world, pos, EnumFacing.NORTH))
              .withProperty(EAST, isFluidTransporter(world, pos, EnumFacing.EAST))
              .withProperty(SOUTH, isFluidTransporter(world, pos, EnumFacing.SOUTH))
              .withProperty(WEST, isFluidTransporter(world, pos, EnumFacing.WEST))
              .withProperty(UP, isFluidTransporter(world, pos, EnumFacing.UP))
              .withProperty(DOWN, isFluidTransporter(world, pos, EnumFacing.DOWN));
        }

        return state;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityColdFluidPipe();
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, IBlockState state, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile != null && tile instanceof TileEntityColdFluidPipe) {
                FluidStack stackF = ((TileEntityColdFluidPipe) tile).tank.getFluid();
                player.addChatMessage(new TextComponentString(stackF == null ? "No fluid" : stackF.getLocalizedName() + "x" + stackF.amount));
            }
            return true;
        }
        return world.setBlockState(pos, state.withProperty(MODE, state.getValue(MODE).next()));
    }
}
