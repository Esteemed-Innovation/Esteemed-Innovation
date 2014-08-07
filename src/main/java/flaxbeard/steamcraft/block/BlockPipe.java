package flaxbeard.steamcraft.block;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.codechicken.lib.raytracer.IndexedCuboid6;
import flaxbeard.steamcraft.codechicken.lib.raytracer.RayTracer;
import flaxbeard.steamcraft.codechicken.lib.vec.BlockCoord;
import flaxbeard.steamcraft.codechicken.lib.vec.Vector3;
import flaxbeard.steamcraft.item.ItemWrench;
import flaxbeard.steamcraft.tile.TileEntitySteamPipe;

public class BlockPipe extends BlockSteamTransporter {
	
	public IIcon sideIcon;
	public IIcon copperIcon;
    public int pass = 0;
    
    @Override
    public boolean canRenderInPass(int x) {
    	pass = x;
    	return x == 0;
    }
    
	public BlockPipe() {
		super(Material.iron);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntitySteamPipe();
	}

    public boolean isOpaqueCube()
    {
        return false;
    }
    
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    public int getRenderType()
    {
        return Steamcraft.tubeRenderID;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void setBlockBoundsBasedOnState(IBlockAccess world, int i, int j, int k)
    {
    	Minecraft mc = Minecraft.getMinecraft();

	    if (mc.thePlayer.isSneaking() || mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemWrench)) {
	    	TileEntity te = world.getTileEntity(i,j, k);
	    	if (te instanceof TileEntitySteamPipe) {
	    		
	    		TileEntitySteamPipe pipe = (TileEntitySteamPipe) te;
	    		if (pipe != null && pipe.disguiseBlock != null && pipe.disguiseBlock != Blocks.air) {
	    			setBlockBounds(0.0F,0.0F,0.0F,1.0F,1.0F,1.0F);
	        	}
	    		else
	    		{
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
    	}
    }
    
//    @Override
//    @SideOnly (Side.CLIENT)
//    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) 
//    {
//    	Minecraft mc = Minecraft.getMinecraft();
//    	TileEntitySteamPipe pipe = (TileEntitySteamPipe) world.getTileEntity(i,j, k);
//    	if (mc.thePlayer.isSneaking() || mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemWrench)) {
//	 		if (pipe != null && pipe.disguiseBlock != null && pipe.disguiseBlock != Blocks.air) {
//				setBlockBounds(0.0F,0.0F,0.0F,1.0F,1.0F,1.0F);
//	    	}
//			else  {
//				float baseMin = 4.0F/16.0F;
//				float baseMax = 12.0F/16.0F;
//				float ringMin = 4.0F/16.0F;
//				float ringMax = 12.0F/16.0F;
//				float px = 1.0F/16.0F;
//				float minX = baseMin;
//				float maxX = baseMax;
//				float minY = baseMin;
//				float maxY = baseMax;
//				float minZ = baseMin;
//				float maxZ = baseMax;
//		    	if (pipe != null) {
//					ArrayList<ForgeDirection> myDirections = new ArrayList<ForgeDirection>();
//					for (ForgeDirection direction : ForgeDirection.values()) {
//						if (pipe.doesConnect(direction) && world.getTileEntity(i+direction.offsetX, j+direction.offsetY, k+direction.offsetZ) != null) {
//							TileEntity tile = world.getTileEntity(i+direction.offsetX, j+direction.offsetY, k+direction.offsetZ);
//							if (tile instanceof ISteamTransporter) {
//								ISteamTransporter target = (ISteamTransporter) tile;
//								if (target.doesConnect(direction.getOpposite())) {
//									myDirections.add(direction);
//									if (direction.offsetX == 1) {
//										maxX = 1.0F;
//									}
//									if (direction.offsetY == 1) {
//										maxY = 1.0F;
//									}
//									if (direction.offsetZ == 1) {
//										maxZ = 1.0F;
//									}
//									if (direction.offsetX == -1) {
//										minX = 0.0F;
//									}
//									if (direction.offsetY == -1) {
//										minY = 0.0F;
//									}
//									if (direction.offsetZ == -1) {
//										minZ = 0.0F;
//									}
//								}
//							}
//						}
//					}
//					if (myDirections.size() == 2) {
//						ForgeDirection direction = myDirections.get(0).getOpposite();
//						while (!pipe.doesConnect(direction) || direction == myDirections.get(0)) {
//							direction = ForgeDirection.getOrientation((direction.ordinal()+1)%5);
//						}
//						if (direction.offsetX == 1) {
//							maxX = 1.0F;
//						}
//						if (direction.offsetY == 1) {
//							maxY = 1.0F;
//						}
//						if (direction.offsetZ == 1) {
//							maxZ = 1.0F;
//						}
//						if (direction.offsetX == -1) {
//							minX = 0.0F;
//						}
//						if (direction.offsetY == -1) {
//							minY = 0.0F;
//						}
//						if (direction.offsetZ == -1) {
//							minZ = 0.0F;
//						}
//					}
//					setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
//		    	}
//	    	}
//    	}
//		Steamcraft.log.debug("Ls");
//
//		return super.getSelectedBoundingBoxFromPool(world, i, j, k);
//    }
    
    
    
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
    	TileEntity te = world.getTileEntity(i,j, k);
    	if (te instanceof TileEntitySteamPipe) {
    		
    		TileEntitySteamPipe pipe = (TileEntitySteamPipe) te;
    		if (pipe != null && pipe.disguiseBlock != null && pipe.disguiseBlock != Blocks.air) {
				return AxisAlignedBB.getBoundingBox(i, j, k, i+1, j+1, k+1);
    		}
    		else
    		{
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
					return AxisAlignedBB.getBoundingBox(i+minX, j+minY, k+minZ, i+maxX, j+maxY, k+maxZ);
		    	}
	    	}
    	}
		return super.getCollisionBoundingBoxFromPool(world, i, j, k);
    }

    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blockIcon = p_149651_1_.registerIcon("steamcraft:blockBrass");
        this.sideIcon = p_149651_1_.registerIcon("steamcraft:blockBrass" + "_pipe");
        this.copperIcon = p_149651_1_.registerIcon("steamcraft:blockCopper");

    }
    
    private RayTracer rayTracer = new RayTracer();
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onBlockHighlight(DrawBlockHighlightEvent event)
    {
    	if ((event.target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) && (event.player.worldObj.getBlock(event.target.blockX, event.target.blockY, event.target.blockZ) instanceof BlockPipe) 
    			&& (event.player.getCurrentEquippedItem() != null) && ((event.player.getCurrentEquippedItem().getItem() instanceof ItemWrench))) {
    		RayTracer.retraceBlock(event.player.worldObj, event.player, event.target.blockX, event.target.blockY, event.target.blockZ);
    	}
    }
    
    @SideOnly (Side.CLIENT)

    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 start, Vec3 end)
    {
    	
    	EntityPlayer player = Minecraft.getMinecraft().thePlayer;
    	TileEntity tile = world.getTileEntity(x, y, z);
    	if ((tile == null) || (!(tile instanceof TileEntitySteamPipe)) || player.isSneaking() || !((player.getCurrentEquippedItem() != null) && (player.getCurrentEquippedItem().getItem() instanceof ItemWrench))) {
    		return super.collisionRayTrace(world, x, y, z, start, end);
      	}
    	List<IndexedCuboid6> cuboids = new LinkedList();
    	((TileEntitySteamPipe)tile).addTraceableCuboids(cuboids);
    	MovingObjectPosition mop = this.rayTracer.rayTraceCuboids(new Vector3(start), new Vector3(end), cuboids, new BlockCoord(x, y, z), this);
    	return mop;
    }
    
}
