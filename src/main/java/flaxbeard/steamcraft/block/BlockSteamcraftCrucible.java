package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.Tuple3;
import flaxbeard.steamcraft.init.blocks.CastingBlocks;
import flaxbeard.steamcraft.tile.TileEntityCrucible;
import flaxbeard.steamcraft.tile.TileEntitySteamHeater;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.MutablePair;

public class BlockSteamcraftCrucible extends BlockContainer implements IWrenchable {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    private static final float PX = (1.0F / 16.0F);
    public TextureAtlasSprite liquidIcon;

    public BlockSteamcraftCrucible() {
        super(Material.ROCK);
        setHardness(3.5F);
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
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        return new AxisAlignedBB(x + PX, y + 0.0F + PX, z + PX, x + 1.0F - PX, y + 1.0F - PX, z + 1.0F - PX);
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null && te instanceof TileEntityCrucible) {
            TileEntityCrucible crucible = (TileEntityCrucible) te;
            return crucible.getComparatorOutput();
        }
        return 0;
    }

    public boolean isCrucibleHeated(World world, BlockPos pos) {
        BlockPos underCruciblePosition = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ());
        IBlockState stateUnderCrucible = world.getBlockState(underCruciblePosition);
        TileEntity tileUnderCrucible = world.getTileEntity(underCruciblePosition);

        if (this == CastingBlocks.Blocks.NETHER_CRUCIBLE.getBlock() ||
          stateUnderCrucible.getMaterial() == Material.FIRE ||
          stateUnderCrucible.getMaterial() == Material.LAVA) {
            return true;
        }

        if (tileUnderCrucible instanceof TileEntitySteamHeater) {
            TileEntitySteamHeater steamHeater = (TileEntitySteamHeater) tileUnderCrucible;

            if (world.getBlockState(steamHeater.getPos()).getValue(BlockSteamHeater.FACING) == EnumFacing.UP &&
              steamHeater.getSteam() >= TileEntitySteamHeater.CONSUMPTION) {
                steamHeater.decrSteam(TileEntitySteamHeater.CONSUMPTION);
                return true;
            }
        }

        return false;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        if (entity instanceof EntityItem) {
            EntityItem item = (EntityItem) entity;
            if (isCrucibleHeated(world, pos)) {
                MutablePair output;
                if (SteamcraftRegistry.liquidRecipes.containsKey(MutablePair.of(item.getEntityItem().getItem(), item.getEntityItem().getItemDamage()))) {
                    output = SteamcraftRegistry.liquidRecipes.get(MutablePair.of(item.getEntityItem().getItem(), item.getEntityItem().getItemDamage()));
                } else if (SteamcraftRegistry.liquidRecipes.containsKey(MutablePair.of(item.getEntityItem().getItem(), -1))) {
                    output = SteamcraftRegistry.liquidRecipes.get(MutablePair.of(item.getEntityItem().getItem(), -1));

                } else {
                    return;
                }
                TileEntityCrucible crucible = (TileEntityCrucible) world.getTileEntity(pos);
                int amount = (Integer) output.right;
                if (crucible != null) {
                    // TODO: Waiting for response to MinecraftForge#3092. If it is not accepted, we will have to do
                    // reflection to get the delayBeforeCanPickup value.
//                    if (item.delayBeforeCanPickup > 2){
//                        item.delayBeforeCanPickup = 2;
//                    } else if (item.delayBeforeCanPickup <= 1) {
                        ItemStack stack = item.getEntityItem();
                        ItemStack out = crucible.fillWith(stack, amount, output);
                        if (crucible.getFill() + amount <= 90) {
//                            item.delayBeforeCanPickup = 2;
                        }

                        if (out.stackSize <= 0) {
                            entity.setDead();
                        } else {
                            item.setEntityItemStack(out);
                        }
//                    }
                }
            }
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()));
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntityCrucible();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntityCrucible tile = (TileEntityCrucible) world.getTileEntity(pos);
        if (tile == null) {
            return false;
        }
        if (!player.isSneaking() && heldItem == null) {
            if (!tile.isTipping()) {
                tile.setTipping();
                tile.needsUpdate = true;
            }
        } else if (heldItem != null) {
            for (CrucibleLiquid liquid : tile.contents) {
                Tuple3<Item, Integer, CrucibleLiquid> tuple = new Tuple3<>(heldItem.getItem(), heldItem.getItemDamage(), liquid);
                boolean valid;
                if (!SteamcraftRegistry.dunkRecipes.containsKey(tuple)) {
                    tuple = new Tuple3<>(heldItem.getItem(), -1, liquid);
                    valid = SteamcraftRegistry.dunkRecipes.containsKey(tuple);
                } else {
                    valid = true;
                }

                if (valid) {
                    MutablePair<Integer, ItemStack> pair = SteamcraftRegistry.dunkRecipes.get(tuple);
                    int needed = pair.getLeft();
                    ItemStack result = pair.getRight().copy();
                    if (tile.number.get(liquid) >= needed) {
                        player.inventory.decrStackSize(player.inventory.currentItem, 1);
                        int currNum = tile.number.get(liquid);
                        currNum -= needed;
                        if (currNum == 0) {
                            tile.contents.remove(liquid);
                        }
                        tile.number.remove(liquid);
                        tile.number.put(liquid, currNum);
                        if (!player.inventory.addItemStackToInventory(result)) {
                            if (!player.worldObj.isRemote) {
                                player.entityDropItem(result, 0.0F);
                            }
                        }
                        tile.needsUpdate = true;

                        break;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, IBlockState state, float hitX, float hitY, float hitZ) {
        if (facing != EnumFacing.DOWN && facing != EnumFacing.UP) {
            world.setBlockState(pos, state.withProperty(FACING, facing.getOpposite()), 2);
            return true;
        }
        return false;
    }
}
