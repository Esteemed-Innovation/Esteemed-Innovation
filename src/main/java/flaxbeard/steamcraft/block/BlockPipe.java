package flaxbeard.steamcraft.block;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidHandler;
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
    @SideOnly (Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) 
    {
    	Minecraft mc = Minecraft.getMinecraft();
    	if (mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemWrench)) {
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
			ArrayList<ForgeDirection> myDirections = new ArrayList<ForgeDirection>();
			for (ForgeDirection direction : ForgeDirection.values()) {
				if (world.getTileEntity(i+direction.offsetX, j+direction.offsetY, k+direction.offsetZ) != null) {
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
					else if (tile instanceof IFluidHandler && Steamcraft.steamRegistered) {
						IFluidHandler target = (IFluidHandler) tile;
						if (target.canDrain(direction.getOpposite(), FluidRegistry.getFluid("steam")) || target.canFill(direction.getOpposite(), FluidRegistry.getFluid("steam"))) {
							myDirections.add(direction);
							if (direction.offsetX == 1) {
								maxX = 1.0F-2*px;
							}
							if (direction.offsetY == 1) {
								maxY = 1.0F-2*px;
							}
							if (direction.offsetZ == 1) {
								maxZ = 1.0F-2*px;
							}
							if (direction.offsetX == -1) {
								minX = 0.0F+2*px;
							}
							if (direction.offsetY == -1) {
								minY = 0.0F+2*px;
							}
							if (direction.offsetZ == -1) {
								minZ = 0.0F+2*px;
							}
						}
					}
				}
			}
			setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
    	}
		return super.getSelectedBoundingBoxFromPool(world, i, j, k);
    }
    
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
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
		ArrayList<ForgeDirection> myDirections = new ArrayList<ForgeDirection>();
		for (ForgeDirection direction : ForgeDirection.values()) {
			if (world.getTileEntity(i+direction.offsetX, j+direction.offsetY, k+direction.offsetZ) != null) {
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
    	if ((event.target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) && (event.player.worldObj.getBlock(event.target.blockX, event.target.blockY, event.target.blockZ) instanceof BlockPipe) && (event.player.getCurrentEquippedItem() != null) && ((event.player.getCurrentEquippedItem().getItem() instanceof ItemWrench))) {
    		RayTracer.retraceBlock(event.player.worldObj, event.player, event.target.blockX, event.target.blockY, event.target.blockZ);
    	}
    }
    
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 start, Vec3 end)
    {
    	TileEntity tile = world.getTileEntity(x, y, z);
    	if ((tile == null) || (!(tile instanceof TileEntitySteamPipe))) {
    		return super.collisionRayTrace(world, x, y, z, start, end);
      	}
    	List<IndexedCuboid6> cuboids = new LinkedList();
    	((TileEntitySteamPipe)tile).addTraceableCuboids(cuboids);
    	MovingObjectPosition mop = this.rayTracer.rayTraceCuboids(new Vector3(start), new Vector3(end), cuboids, new BlockCoord(x, y, z), this);
    	return mop;
    }
}
