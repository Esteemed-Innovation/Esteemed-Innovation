package flaxbeard.steamcraft.tile;

import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.tile.SteamTransporterTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;

public class TileEntityThumper extends SteamTransporterTileEntity implements ISteamTransporter {
    public int progress = 0;
    private boolean isRunning = false;
    private static final Material[] VALID_MATERIALS = {
      Material.SAND,
      Material.GROUND,
      Material.ROCK,
      Material.CLAY,
      Material.GRASS
    };
    private static final float ANVIL_VOLUME = Blocks.ANVIL.getSoundType().getVolume();

    public TileEntityThumper() {
        super(new EnumFacing[] {
          EnumFacing.DOWN,
          EnumFacing.NORTH,
          EnumFacing.SOUTH,
          EnumFacing.EAST,
          EnumFacing.WEST
        });
        addSidesToGaugeBlacklist(EnumFacing.VALUES);
    }

    @Override
    public void update() {
        super.update();
        if (worldObj.isRemote) {
            if (isRunning) {
                if (progress < 110) {
                    progress++;
                } else {
                    progress = 0;
                    isRunning = false;
                }
            } else {
                progress = 0;
                isRunning = false;
            }
        } else {
            if (getSteamShare() >= 2000 && progress == 0) {
                if (!isRunning) {
                    isRunning = true;
                }
                progress++;
                decrSteam(2000);
                markForUpdate();
            } else if (progress > 0 && !isRunning) {
                isRunning = true;
                markForUpdate();
            }
            if (progress == 15) {
                worldObj.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, Steamcraft.SOUND_HISS,
                  SoundCategory.BLOCKS, ANVIL_VOLUME, 0.9F, false);
            }

            if (progress > 0 && progress < 110) {
                progress++;
            }
            if (progress >= 110) {
                progress = 0;
                worldObj.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 8F,
                  (1F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
                //	        List players = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(xCoord-4.5F, yCoord-4.5F, zCoord-4.5F, xCoord+5.5F, yCoord+5.5F, zCoord+5.5F));
                //	        for (Object obj : players) {
                //	        	if (obj instanceof EntityPlayer && this.worldObj.isRemote) {
                //	        		EntityPlayer player = (EntityPlayer) obj;
                //		        	player.rotationPitch += (this.worldObj.rand.nextInt(9) - 4)*1F;
                //		        	player.rotationYaw += (this.worldObj.rand.nextInt(9) - 4)*1F;
                //	        	}
                //
                //	        }
                if (!worldObj.isRemote) {
                    for (int z = 0; z < 4; z++) {
                        boolean hasTarget = false;
                        int i = 0;
                        BlockPos target = new BlockPos(pos.getX(), pos.getY() - 10, pos.getZ());
                        int meta = getBlockMetadata();
                        EnumFacing[] moveDirs;
                        EnumFacing[] moveDirsNotUp;
                        EnumFacing[] forbiddenDirs;
                        if (meta == 1 || meta == 3) {
                            EnumFacing[] moveDirs2 = {EnumFacing.DOWN, EnumFacing.EAST, EnumFacing.EAST, EnumFacing.EAST, EnumFacing.EAST, EnumFacing.EAST, EnumFacing.UP, EnumFacing.WEST, EnumFacing.WEST, EnumFacing.WEST, EnumFacing.WEST, EnumFacing.WEST};
                            EnumFacing[] moveDirsNotUp2 = {EnumFacing.DOWN, EnumFacing.EAST, EnumFacing.EAST, EnumFacing.EAST, EnumFacing.EAST, EnumFacing.EAST, EnumFacing.WEST, EnumFacing.WEST, EnumFacing.WEST, EnumFacing.WEST, EnumFacing.WEST};
                            EnumFacing[] forbiddenDirs2 = {EnumFacing.NORTH, EnumFacing.SOUTH};

                            moveDirs = moveDirs2;
                            moveDirsNotUp = moveDirsNotUp2;
                            forbiddenDirs = forbiddenDirs2;
                        } else {
                            EnumFacing[] moveDirs2 = {EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.NORTH, EnumFacing.NORTH, EnumFacing.NORTH, EnumFacing.NORTH, EnumFacing.UP, EnumFacing.SOUTH, EnumFacing.SOUTH, EnumFacing.SOUTH, EnumFacing.SOUTH, EnumFacing.SOUTH};
                            EnumFacing[] moveDirsNotUp2 = {EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.NORTH, EnumFacing.NORTH, EnumFacing.NORTH, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.SOUTH, EnumFacing.SOUTH, EnumFacing.SOUTH, EnumFacing.SOUTH};
                            EnumFacing[] forbiddenDirs2 = {EnumFacing.EAST, EnumFacing.WEST};

                            moveDirs = moveDirs2;
                            moveDirsNotUp = moveDirsNotUp2;
                            forbiddenDirs = forbiddenDirs2;
                        }

                        while (!hasTarget && i < 160) {
                            if (!worldObj.isAirBlock(target) &&
                              Arrays.asList(VALID_MATERIALS).contains(worldObj.getBlockState(target).getMaterial()) &&
                              (worldObj.getBlockState(target).getBlockHardness(worldObj, target) != -1.0F) &&
                              !worldObj.canBlockSeeSky(new BlockPos(target.getX() - 1, target.getY() + 1, target.getZ())) &&
                              !worldObj.canBlockSeeSky(new BlockPos(target.getX() + 1, target.getY() + 1, target.getZ())) &&
                              !worldObj.canBlockSeeSky(new BlockPos(target.getX(), target.getY() + 1, target.getZ() - 1)) &&
                              !worldObj.canBlockSeeSky(new BlockPos(target.getX(), target.getY() + 1, target.getZ() + 1)) &&
                              !worldObj.canBlockSeeSky(new BlockPos(target.getX(), target.getY() + 1, target.getZ()))) {
                                hasTarget = true;
                            } else {
                                if (target.getY() < pos.getY() - 3) {
                                    EnumFacing direction = moveDirs[worldObj.rand.nextInt(moveDirs.length)];
                                    if (worldObj.rand.nextInt(50) == 0) {
                                        direction = forbiddenDirs[worldObj.rand.nextInt(forbiddenDirs.length)];
                                    }
                                    target = new BlockPos(target.getX() + direction.getFrontOffsetX(),
                                      target.getY() + direction.getFrontOffsetY(),
                                      target.getZ() + direction.getFrontOffsetZ());
                                } else {
                                    EnumFacing direction = moveDirsNotUp[worldObj.rand.nextInt(moveDirsNotUp.length)];
                                    if (worldObj.rand.nextInt(50) == 0) {
                                        direction = forbiddenDirs[worldObj.rand.nextInt(forbiddenDirs.length)];
                                    }
                                    target = new BlockPos(target.getX() + direction.getFrontOffsetX(),
                                      target.getY() + direction.getFrontOffsetY(),
                                      target.getZ() + direction.getFrontOffsetZ());
                                }
                            }
                            i++;
                        }
                        if (hasTarget) {
                            IBlockState state = worldObj.getBlockState(target);
                            Block block = state.getBlock();
                            if (Config.dropItem) {
                                block.dropBlockAsItem(worldObj, target, state, 0);
                            }
                            worldObj.setBlockToAir(target);
                        }
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 5, pos.getZ() + 1);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        progress = nbt.getShort("progress");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setShort("progress", (short) progress);
        return nbt;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound access = super.getUpdateTag();

        access.setInteger("progress", progress);
        access.setBoolean("isRunning", isRunning);

        return new SPacketUpdateTileEntity(pos, 1, access);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound access = pkt.getNbtCompound();
        progress = access.getInteger("progress");
        isRunning = access.getBoolean("isRunning");

        markForUpdate();
    }
}