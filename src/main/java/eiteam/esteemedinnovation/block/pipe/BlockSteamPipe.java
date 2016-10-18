package eiteam.esteemedinnovation.block.pipe;

import eiteam.esteemedinnovation.api.wrench.IPipeWrench;
import eiteam.esteemedinnovation.api.block.BlockSteamTransporter;
import eiteam.esteemedinnovation.tile.pipe.TileEntitySteamPipe;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.raytracer.RayTracer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

public class BlockSteamPipe extends BlockSteamTransporter {
    public static final float BASE_MIN = 4F / 16F;
    public static final float BASE_MAX = 12F / 16F;

    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");

    public BlockSteamPipe() {
        super(Material.IRON);
        setHardness(2.5F);
        setResistance(5F);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntitySteamPipe();
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, NORTH, SOUTH, EAST, WEST, UP, DOWN);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        HashMap<IProperty, Boolean> vals = new HashMap<>();
        if (tile == null || !(tile instanceof TileEntitySteamPipe)) {
            vals.put(NORTH, false);
            vals.put(SOUTH, false);
            vals.put(EAST, false);
            vals.put(WEST, false);
            vals.put(UP, false);
            vals.put(DOWN, false);
        } else {
            ArrayList<EnumFacing> connections = ((TileEntitySteamPipe) tile).getMyDirections();
            vals.put(NORTH, connections.contains(EnumFacing.NORTH));
            vals.put(SOUTH, connections.contains(EnumFacing.SOUTH));
            vals.put(EAST, connections.contains(EnumFacing.EAST));
            vals.put(WEST, connections.contains(EnumFacing.WEST));
            vals.put(UP, connections.contains(EnumFacing.UP));
            vals.put(DOWN, connections.contains(EnumFacing.DOWN));
        }

        return state
          .withProperty(NORTH, vals.get(NORTH))
          .withProperty(SOUTH, vals.get(SOUTH))
          .withProperty(EAST, vals.get(EAST))
          .withProperty(WEST, vals.get(WEST))
          .withProperty(UP, vals.get(UP))
          .withProperty(DOWN, vals.get(DOWN));
    }

    /*
    @Override
    @SideOnly (Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k)
    {
    	Minecraft mc = Minecraft.getMinecraft();
    	TileEntitySteamPipe pipe = (TileEntitySteamPipe) world.getTileEntity(i,j, k);
    	if (mc.thePlayer.isSneaking() || mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemWrench)) {
	 		if (pipe != null && pipe.disguiseBlock != null && pipe.disguiseBlock != Blocks.air) {
				setBlockBounds(0.0F,0.0F,0.0F,1.0F,1.0F,1.0F);
	    	}
			else  {
				float baseMin = 4.0F/16.0F;
				float baseMax = 12.0F/16.0F;
				float ringMin = 4.0F/16.0F;
				float ringMax = 12.0F/16.0F;
				float px = 1.0F/16.0F;
				float minX = baseMin;
				float maxX = baseMax;
				float minY = baseMin;
				float maxY = baseMax;
				float minZ = baseMin;
				float maxZ = baseMax;
		    	if (pipe != null) {
					ArrayList<ForgeDirection> myDirections = new ArrayList<ForgeDirection>();
					for (ForgeDirection direction : ForgeDirection.values()) {
						if (pipe.doesConnect(direction) && world.getTileEntity(i+direction.offsetX, j+direction.offsetY, k+direction.offsetZ) != null) {
							TileEntity tile = world.getTileEntity(i+direction.offsetX, j+direction.offsetY, k+direction.offsetZ);
							if (tile instanceof ISteamTransporter) {
								ISteamTransporter target = (ISteamTransporter) tile;
								if (target.doesConnect(direction.getOpposite())) {
									myDirections.add(direction);
									if (direction.offsetX == 1) {
										maxX = 1.0F;
									}
									if (direction.offsetY == 1) {
										maxY = 1.0F;
									}
									if (direction.offsetZ == 1) {
										maxZ = 1.0F;
									}
									if (direction.offsetX == -1) {
										minX = 0.0F;
									}
									if (direction.offsetY == -1) {
										minY = 0.0F;
									}
									if (direction.offsetZ == -1) {
										minZ = 0.0F;
									}
								}
							}
						}
					}
					if (myDirections.size() == 2) {
						ForgeDirection direction = myDirections.get(0).getOpposite();
						while (!pipe.doesConnect(direction) || direction == myDirections.get(0)) {
							direction = ForgeDirection.getOrientation((direction.ordinal()+1)%5);
						}
						if (direction.offsetX == 1) {
							maxX = 1.0F;
						}
						if (direction.offsetY == 1) {
							maxY = 1.0F;
						}
						if (direction.offsetZ == 1) {
							maxZ = 1.0F;
						}
						if (direction.offsetX == -1) {
							minX = 0.0F;
						}
						if (direction.offsetY == -1) {
							minY = 0.0F;
						}
						if (direction.offsetZ == -1) {
							minZ = 0.0F;
						}
					}
					setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
		    	}
	    	}
    	}
		EsteemedInnovation.log.debug("Ls");

		return super.getSelectedBoundingBoxFromPool(world, i, j, k);
    }
    */

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null && te instanceof TileEntitySteamPipe) {
            TileEntitySteamPipe pipe = (TileEntitySteamPipe) te;
            if (pipe.disguiseBlock == null || pipe.disguiseBlock == Blocks.AIR) {
                float minX = BASE_MIN;
                float maxX = BASE_MAX;
                float minY = BASE_MIN;
                float maxY = BASE_MAX;
                float minZ = BASE_MIN;
                float maxZ = BASE_MAX;
                IBlockState actualState = getActualState(state, world, pos);
                // The OR check resolves the problem with "long" pipes that don't connect to anything having a shorter
                // bounding box in that direction.
                int numDirs = pipe.getMyDirections().size();
                boolean hasNorth = actualState.getValue(NORTH);
                boolean hasSouth = actualState.getValue(SOUTH);
                boolean hasWest = actualState.getValue(WEST);
                boolean hasEast = actualState.getValue(EAST);
                boolean hasDown = actualState.getValue(DOWN);
                boolean hasUp = actualState.getValue(UP);
                if (TileEntitySteamPipe.shouldStretchInDirection(hasNorth, hasSouth, numDirs)) {
                    minZ = 0F;
                }
                if (TileEntitySteamPipe.shouldStretchInDirection(hasSouth, hasNorth, numDirs)) {
                    maxZ = 1F;
                }
                if (TileEntitySteamPipe.shouldStretchInDirection(hasWest, hasEast, numDirs)) {
                    minX = 0F;
                }
                if (TileEntitySteamPipe.shouldStretchInDirection(hasEast, hasWest, numDirs)) {
                    maxX = 1F;
                }
                if (TileEntitySteamPipe.shouldStretchInDirection(hasDown, hasUp, numDirs)) {
                    minY = 0F;
                }
                if (TileEntitySteamPipe.shouldStretchInDirection(hasUp, hasDown, numDirs)) {
                    maxY = 1F;
                }
                return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
            }
        }
        return super.getBoundingBox(state, world, pos);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onBlockHighlight(DrawBlockHighlightEvent event) {
        EntityPlayer player = event.getPlayer();
        ItemStack mainHandStack = player.getHeldItemMainhand();
        if (mainHandStack == null) {
            return;
        }
        Item equipped = mainHandStack.getItem();
        RayTraceResult rtr = event.getTarget();
        BlockPos rtrPos = rtr.getBlockPos();
        if (rtr.typeOfHit == RayTraceResult.Type.BLOCK &&
          player.worldObj.getBlockState(rtr.getBlockPos()).getBlock() instanceof BlockSteamPipe &&
          equipped instanceof IPipeWrench) {
            IPipeWrench wrench = (IPipeWrench) equipped;
            if (wrench.canWrench(player, rtrPos)) {
                RayTracer.retraceBlock(player.worldObj, player, rtrPos);
            }
        }
    }

    protected void superAddCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entity) {
        super.addCollisionBoxToList(state, world, pos, entityBox, collidingBoxes, entity);
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entity) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile == null || !(tile instanceof TileEntitySteamPipe)) {
            return;
        }
        List<IndexedCuboid6> cuboids = new LinkedList<>();
        ((TileEntitySteamPipe) tile).addTraceableCuboids(cuboids);
        for (IndexedCuboid6 cuboid : cuboids) {
            AxisAlignedBB aabb = cuboid.aabb();
            if (aabb.intersectsWith(entityBox)) {
                collidingBoxes.add(aabb);
            }
        }
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
}
