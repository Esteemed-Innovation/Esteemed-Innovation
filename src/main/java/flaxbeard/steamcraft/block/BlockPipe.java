package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.api.IPipeWrench;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.tile.TileEntitySteamPipe;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.raytracer.RayTracer;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Vector3;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BlockPipe extends BlockSteamTransporter {
    private static final float BASE_MIN = 4.0F / 16.0F;
    private static final float BASE_MAX = 12.0F / 16.0F;
    public int pass = 0;
    private RayTracer rayTracer = new RayTracer();

    public BlockPipe() {
        super(Material.IRON);
        setHardness(2.5F);
        setResistance(5F);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntitySteamPipe();
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
		Steamcraft.log.debug("Ls");

		return super.getSelectedBoundingBoxFromPool(world, i, j, k);
    }
    */

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null && te instanceof TileEntitySteamPipe) {
            TileEntitySteamPipe pipe = (TileEntitySteamPipe) te;
            if (pipe.disguiseBlock != null && pipe.disguiseBlock != Blocks.AIR) {
                return new AxisAlignedBB(pos);
            } else {
                float minX = BASE_MIN;
                float maxX = BASE_MAX;
                float minY = BASE_MIN;
                float maxY = BASE_MAX;
                float minZ = BASE_MIN;
                float maxZ = BASE_MAX;
                ArrayList<EnumFacing> myDirections = new ArrayList<>();
                for (EnumFacing direction : EnumFacing.VALUES) {
                    TileEntity tile = world.getTileEntity(new BlockPos(pos.getX() + direction.getFrontOffsetX(), pos.getY() + direction.getFrontOffsetY(), pos.getZ() + direction.getFrontOffsetZ()));
                    if (pipe.doesConnect(direction) && tile != null && tile instanceof ISteamTransporter) {
                        ISteamTransporter target = (ISteamTransporter) tile;
                        if (target.doesConnect(direction.getOpposite())) {
                            myDirections.add(direction);
                            if (direction.getFrontOffsetX() == 1) {
                                maxX = 1.0F;
                            }
                            if (direction.getFrontOffsetY() == 1) {
                                maxY = 1.0F;
                            }
                            if (direction.getFrontOffsetZ() == 1) {
                                maxZ = 1.0F;
                            }
                            if (direction.getFrontOffsetX() == -1) {
                                minX = 0.0F;
                            }
                            if (direction.getFrontOffsetY() == -1) {
                                minY = 0.0F;
                            }
                            if (direction.getFrontOffsetZ() == -1) {
                                minZ = 0.0F;
                            }
                        }
                    }
                }
                if (myDirections.size() == 2) {
                    EnumFacing direction = myDirections.get(0).getOpposite();
                    while (!pipe.doesConnect(direction) || direction == myDirections.get(0)) {
                        direction = EnumFacing.getFront((direction.ordinal() + 1) % 5);
                    }
                    if (direction.getFrontOffsetX() == 1) {
                        maxX = 1.0F;
                    }
                    if (direction.getFrontOffsetY() == 1) {
                        maxY = 1.0F;
                    }
                    if (direction.getFrontOffsetZ() == 1) {
                        maxZ = 1.0F;
                    }
                    if (direction.getFrontOffsetX() == -1) {
                        minX = 0.0F;
                    }
                    if (direction.getFrontOffsetY() == -1) {
                        minY = 0.0F;
                    }
                    if (direction.getFrontOffsetZ() == -1) {
                        minZ = 0.0F;
                    }
                }
                return new AxisAlignedBB(pos.getX() + minX, pos.getY() + minY, pos.getZ() + minZ, pos.getX() + maxX, pos.getY() + maxY, pos.getZ() + maxZ);
            }
        }
        return super.getCollisionBoundingBox(state, world, pos);
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
          player.worldObj.getBlockState(rtr.getBlockPos()).getBlock() instanceof BlockPipe &&
          equipped instanceof IPipeWrench) {
            IPipeWrench wrench = (IPipeWrench) equipped;
            if (wrench.canWrench(player, rtrPos)) {
                RayTracer.retraceBlock(player.worldObj, player, rtrPos);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player == null) {
            return null;
        }
        ItemStack mainHandStack = player.getHeldItemMainhand();
        if (mainHandStack == null) {
            return null;
        }
        Item equipped = mainHandStack.getItem();
        TileEntity tile = world.getTileEntity(pos);
        if (tile == null || !(tile instanceof TileEntitySteamPipe) || player.isSneaking() ||
          !(equipped instanceof IPipeWrench && ((IPipeWrench) equipped).canWrench(player, pos))) {
            return super.collisionRayTrace(state, world, pos, start, end);
        }
        List<IndexedCuboid6> cuboids = new LinkedList<>();
        ((TileEntitySteamPipe) tile).addTraceableCuboids(cuboids);

        return rayTracer.rayTraceCuboids(new Vector3(start), new Vector3(end), cuboids, new BlockCoord(pos));
    }
}
