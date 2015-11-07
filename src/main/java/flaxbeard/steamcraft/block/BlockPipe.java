package flaxbeard.steamcraft.block;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.IPipeWrench;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.codechicken.lib.raytracer.IndexedCuboid6;
import flaxbeard.steamcraft.codechicken.lib.raytracer.RayTracer;
import flaxbeard.steamcraft.codechicken.lib.vec.BlockCoord;
import flaxbeard.steamcraft.codechicken.lib.vec.Vector3;
import flaxbeard.steamcraft.item.ItemWrench;
import flaxbeard.steamcraft.tile.TileEntitySteamPipe;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BlockPipe extends BlockSteamTransporter {

    public IIcon sideIcon;
    public IIcon copperIcon;
    public int pass = 0;
    private RayTracer rayTracer = new RayTracer();

    public BlockPipe() {
        super(Material.iron);
    }

    @Override
    public boolean canRenderInPass(int x) {
        pass = x;
        return x == 0;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntitySteamPipe();
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return Steamcraft.tubeRenderID;
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

    @Override
    @SideOnly(Side.CLIENT)
    public void setBlockBoundsBasedOnState(IBlockAccess world, int i, int j, int k) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer.isSneaking() || mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemWrench)) {
            TileEntity te = world.getTileEntity(i, j, k);
            if (te instanceof TileEntitySteamPipe) {

                TileEntitySteamPipe pipe = (TileEntitySteamPipe) te;
                if (pipe != null && pipe.disguiseBlock != null && pipe.disguiseBlock != Blocks.air) {
                    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                } else {
                    float baseMin = 4.0F / 16.0F;
                    float baseMax = 12.0F / 16.0F;
                    float ringMin = 4.0F / 16.0F;
                    float ringMax = 12.0F / 16.0F;
                    float px = 1.0F / 16.0F;
                    float minX = baseMin;
                    float maxX = baseMax;
                    float minY = baseMin;
                    float maxY = baseMax;
                    float minZ = baseMin;
                    float maxZ = baseMax;
                    if (pipe != null) {
                        ArrayList<ForgeDirection> myDirections = new ArrayList<ForgeDirection>();
                        for (ForgeDirection direction : ForgeDirection.values()) {
                            if (pipe.doesConnect(direction) && world.getTileEntity(i + direction.offsetX, j + direction.offsetY, k + direction.offsetZ) != null) {
                                TileEntity tile = world.getTileEntity(i + direction.offsetX, j + direction.offsetY, k + direction.offsetZ);
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
                                direction = ForgeDirection.getOrientation((direction.ordinal() + 1) % 5);
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

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
        TileEntity te = world.getTileEntity(i, j, k);
        if (te instanceof TileEntitySteamPipe) {

            TileEntitySteamPipe pipe = (TileEntitySteamPipe) te;
            if (pipe != null && pipe.disguiseBlock != null && pipe.disguiseBlock != Blocks.air) {
                return AxisAlignedBB.getBoundingBox(i, j, k, i + 1, j + 1, k + 1);
            } else {
                float baseMin = 4.0F / 16.0F;
                float baseMax = 12.0F / 16.0F;
                float ringMin = 4.0F / 16.0F;
                float ringMax = 12.0F / 16.0F;
                float px = 1.0F / 16.0F;
                float minX = baseMin;
                float maxX = baseMax;
                float minY = baseMin;
                float maxY = baseMax;
                float minZ = baseMin;
                float maxZ = baseMax;
                if (pipe != null) {
                    ArrayList<ForgeDirection> myDirections = new ArrayList<ForgeDirection>();
                    for (ForgeDirection direction : ForgeDirection.values()) {
                        if (pipe.doesConnect(direction) && world.getTileEntity(i + direction.offsetX, j + direction.offsetY, k + direction.offsetZ) != null) {
                            TileEntity tile = world.getTileEntity(i + direction.offsetX, j + direction.offsetY, k + direction.offsetZ);
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
                            direction = ForgeDirection.getOrientation((direction.ordinal() + 1) % 5);
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
                    return AxisAlignedBB.getBoundingBox(i + minX, j + minY, k + minZ, i + maxX, j + maxY, k + maxZ);
                }
            }
        }
        return super.getCollisionBoundingBoxFromPool(world, i, j, k);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister ir) {
        this.blockIcon = ir.registerIcon("steamcraft:blockBrass");
        this.sideIcon = ir.registerIcon("steamcraft:blockBrass" + "_pipe");
        this.copperIcon = ir.registerIcon("steamcraft:blockCopper");

    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onBlockHighlight(DrawBlockHighlightEvent event) {
        Item equipped = event.player.getCurrentEquippedItem() != null ? event.player.getCurrentEquippedItem().getItem() : null;
        if ((event.target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) && (event.player.worldObj.getBlock(event.target.blockX, event.target.blockY, event.target.blockZ) instanceof BlockPipe)
                && (event.player.getCurrentEquippedItem() != null) && ((event.player.getCurrentEquippedItem().getItem() instanceof IPipeWrench && ((IPipeWrench) equipped).canWrench(event.player, event.target.blockX, event.target.blockY, event.target.blockZ)))) {
            RayTracer.retraceBlock(event.player.worldObj, event.player, event.target.blockX, event.target.blockY, event.target.blockZ);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 start, Vec3 end) {

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if(player != null) {
            Item equipped = player.getCurrentEquippedItem() != null ? player.getCurrentEquippedItem().getItem() : null;
            TileEntity tile = world.getTileEntity(x, y, z);
            if ((tile == null) || (!(tile instanceof TileEntitySteamPipe)) || player == null || player.isSneaking() || !((player.getCurrentEquippedItem() != null) && (player.getCurrentEquippedItem().getItem() instanceof IPipeWrench && ((IPipeWrench) equipped).canWrench(player, x, y, z)))) {
                return super.collisionRayTrace(world, x, y, z, start, end);
            }
            List<IndexedCuboid6> cuboids = new LinkedList();
            ((TileEntitySteamPipe) tile).addTraceableCuboids(cuboids);
            MovingObjectPosition mop = this.rayTracer.rayTraceCuboids(new Vector3(start), new Vector3(end), cuboids, new BlockCoord(x, y, z), this);
            return mop;
        }
        
        return null;
    }

}
