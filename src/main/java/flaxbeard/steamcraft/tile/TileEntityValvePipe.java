package flaxbeard.steamcraft.tile;

import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.steamnet.SteamNetwork;
import flaxbeard.steamcraft.api.steamnet.SteamNetworkRegistry;
import flaxbeard.steamcraft.block.BlockValvePipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class TileEntityValvePipe extends TileEntitySteamPipe {
    public boolean open = true;
    public int turnTicks = 0;
    private boolean turning;
    private boolean wasTurning = false;
    private boolean redstoneState;
    private boolean waitingOpen = false;

    public TileEntityValvePipe() {
        super(0);
    }

    /**
     * Updates the valve's redstone state, and opens/closes it accordingly.
     * @param flag True to isOpen it, false to close it.
     */
    public void updateRedstoneState(boolean flag) {
		if (Config.enableRedstoneValvePipe) {
			if (!isTurning()) {
                setOpen(flag);
            }
		}
        redstoneState = flag;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound access = super.getDescriptionTag();

        access.setBoolean("turning", turning);
        access.setBoolean("isOpen", open);
        access.setBoolean("leaking", isLeaking);
        access.setInteger("turnTicks", turnTicks);

        return new SPacketUpdateTileEntity(pos, 1, access);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound access = pkt.getNbtCompound();
        if (turnTicks == 0) {
            turnTicks = access.getInteger("turnTicks");
        }
        turning = access.getBoolean("turning");
        isLeaking = access.getBoolean("leaking");
        open = access.getBoolean("isOpen");
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        open = nbt.getBoolean("isOpen");
        redstoneState = nbt.getBoolean("redstoneState");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("isOpen", open);
        nbt.setBoolean("redstoneState", redstoneState);
        return nbt;
    }

    public EnumFacing dir() {
        return worldObj.getBlockState(pos).getValue(BlockValvePipe.FACING);
    }

    @Override
    public boolean doesConnect(EnumFacing face) {
        return face != dir() && super.doesConnect(face);
    }

    @Override
    public ArrayList<EnumFacing> getMyDirections() {
        return super.getMyDirections();
    }

    @Override
    public void update() {
        super.superUpdate();
        if (worldObj.isRemote) {
            if (turning && turnTicks < 10) {
                turnTicks++;
            }
            if (turnTicks >= 10) {
                turning = false;
                setOpen(!open);
                turnTicks = 0;
            }
            if (!turning) {
                turnTicks = 0;
            }

            if (isLeaking) {
                ArrayList<EnumFacing> myDirections = getMyDirections();
                int i = 0;
                if (myDirections.size() > 0) {
                    EnumFacing direction = myDirections.get(0).getOpposite();
                    BlockPos dirPos = new BlockPos(pos.getX() + direction.getFrontOffsetX(), pos.getY() + direction.getFrontOffsetY(), pos.getZ() + direction.getFrontOffsetZ());
                    while (myDirections.size() == 2 && open && i < 10 && (worldObj.isAirBlock(dirPos) || !worldObj.isSideSolid(dirPos, direction.getOpposite()))) {
                        //this.decrSteam(1);
                        worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + 0.5F, pos.getY() + 0.5F,
                          pos.getZ() + 0.5F, direction.getFrontOffsetX() * 0.1F, direction.getFrontOffsetY() * 0.1F,
                          direction.getFrontOffsetZ() * 0.1F);
                        i++;
                    }
                }
            }
        } else {
            if (waitingOpen) {
                //Steamcraft.log.debug("Waiting for isOpen");
                setOpen(!open);
            }
            if (turning != wasTurning) {
                wasTurning = turning;
                markForUpdate();
            }
            if (turning && turnTicks < 10) {
                turnTicks++;
            }
            if (turnTicks >= 10) {
                turning = false;
                setOpen(!open);
                turnTicks = 0;
            }
            if (!turning) {
                if (wasTurning) {
                    markForUpdate();
                }
                turnTicks = 0;
            }
            ArrayList<EnumFacing> myDirections = getMyDirections();
            
            if (myDirections.size() > 0) {
                EnumFacing direction = myDirections.get(0).getOpposite();
                while (!doesConnect(direction)) {
                    direction = EnumFacing.getFront((direction.getIndex() + 1) % 5);
                }

                BlockPos dirPos = new BlockPos(pos.getX() + direction.getFrontOffsetX(), pos.getY() + direction.getFrontOffsetY(), pos.getZ() + direction.getFrontOffsetZ());
                if (myDirections.size() == 2 && open && getNetwork() != null && getNetwork().getSteam() > 0 &&
                  (worldObj.isAirBlock(dirPos) || !worldObj.isSideSolid(dirPos, direction.getOpposite()))) {
                    // Steamcraft.log.debug("isOpen and should be leaking");
                    if (!isLeaking) {
                        isLeaking = true;
                        markForUpdate();
                    }
                    decrSteam(100);
                    worldObj.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, Steamcraft.SOUND_LEAK,
                      SoundCategory.BLOCKS, 2F, 0.9F, false);
                } else {
                    // Steamcraft.log.debug("Probably shouldn't be leaking");
                    if (isLeaking) {
                        isLeaking = false;
                        markForUpdate();
                    }
                }

            } else {
                if (isLeaking) {
                    isLeaking = false;
                    markForUpdate();
                }
            }
        }
    }

    @Override
    public boolean acceptsGauge(EnumFacing face) {
        return face != dir().getOpposite();
    }

    public boolean isTurning() {
        return turning;
    }

    public void setTurning() {
        turning = true;
        turnTicks = 0;
    }

    public boolean isOpen() {
        return open;
    }

    private void setOpen(boolean open) {
        this.open = open;
        boolean changed = true;
        if (!worldObj.isRemote) {
            if (open) {
                //Steamcraft.log.debug("Joining");
                if (SteamNetworkRegistry.getInstance().isInitialized(getDimension())) {
                    SteamNetwork.newOrJoin(this);
                } else {
                    changed = false;
                    this.waitingOpen = true;
                }
            } else {
                //Steamcraft.log.debug("Splitting");
                if (getNetwork() != null) {
                    getNetwork().split(this, true);
                } else {
                    changed = false;
                    waitingOpen = true;
                }
            }
        }
        if (!changed) {
            this.open = !open;
        } else {
            waitingOpen = false;
            markForUpdate();
        }
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, IBlockState state, float hitX, float hitY, float hitZ) {
        return false;
    }
}
