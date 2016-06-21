package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.tile.TileEntityChargingPad;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockChargingPad extends BlockSteamTransporter implements IWrenchable {
    public static PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockChargingPad() {
        super(Material.IRON);
        setHardness(3.5F);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, entity.getHorizontalFacing().getOpposite()));
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityChargingPad();
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, IBlockState state, float hitX, float hitY, float hitZ) {
        if (facing != EnumFacing.DOWN && facing != EnumFacing.UP) {
            EnumFacing output = facing;
            switch (facing) {
                case NORTH: {
                    output = EnumFacing.NORTH;
                    break;
                }
                case SOUTH: {
                    output = EnumFacing.DOWN;
                    break;
                }
                case WEST: {
                    output = EnumFacing.UP;
                    break;
                }
                case EAST: {
                    output = EnumFacing.SOUTH;
                    break;
                }
            }
            if (output == facing && facing.getIndex() > 1 && facing.getIndex() < 6) {
                switch (facing.getOpposite()) {
                    case NORTH: {
                        output = EnumFacing.NORTH;
                        break;
                    }
                    case SOUTH: {
                        output = EnumFacing.DOWN;
                        break;
                    }
                    case WEST: {
                        output = EnumFacing.UP;
                        break;
                    }
                    case EAST: {
                        output = EnumFacing.SOUTH;
                        break;
                    }
                }
            }
            world.setBlockState(pos, state.withProperty(FACING, output), 2);
            return true;
        }
        return false;
    }

}
