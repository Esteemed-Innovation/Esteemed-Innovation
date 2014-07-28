package flaxbeard.steamcraft.block;

import java.util.ArrayList;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.tile.TileEntitySteamPipe;

public class BlockPipe extends BlockSteamTransporter {
	
	public IIcon sideIcon;
	public IIcon copperIcon;
    public int pass = 0;
    
    @Override
    public boolean canRenderInPass(int x) {
    	pass = x;
    	return x <= 1;
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
    public void setBlockBoundsBasedOnState(IBlockAccess world, int i, int j, int k)
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
		setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
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
}
