package flaxbeard.steamcraft.tile;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.tile.SteamReactorTileEntity;
import flaxbeard.steamcraft.client.audio.ISoundTile;
import flaxbeard.steamcraft.client.audio.SoundTile;
import flaxbeard.steamcraft.client.audio.Sounds;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityWhistle extends SteamReactorTileEntity implements ISoundTile, ITickable {
    private float volume = 0F;
    private boolean isSoundRegistered = false;
    private boolean isSounding = false;
    private int steamTick = 0;

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        super.getUpdatePacket();
        NBTTagCompound access = new NBTTagCompound();
        access.setBoolean("isSounding", isSounding);

        return new SPacketUpdateTileEntity(pos, 1, access);
    }


    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound access = pkt.getNbtCompound();
        isSounding = access.getBoolean("isSounding");

        markForUpdate();
    }

    @Override
    public void update() {
        if (worldObj.isRemote) {
            updateSound();
        } else {
            if (shouldPlay()) {
                if (!isSounding) {
                    isSounding = true;
                }
            } else {
                if (isSounding) {
                    isSounding = false;
                }
            }
            markForUpdate();
        }
    }

    @SideOnly(Side.CLIENT)
    private void updateSound() {
        if (!isSoundRegistered) {
            if (worldObj.isRemote) {
                Sounds.addSoundTile(this);
            }
            isSoundRegistered = true;
        }

        if (isSounding) {
            if (steamTick == 0) {
                EnumFacing d = EnumFacing.getFront(getBlockMetadata());
                TileEntity te = worldObj.getTileEntity(new BlockPos(pos.getX() + d.getFrontOffsetX(), pos.getY(),
                  pos.getZ() + d.getFrontOffsetZ()));
                float offset = 2.0F / 16.0F;
                if (te != null && te instanceof TileEntitySteamPipe) {
                    offset = 6.0F / 16.0F;
                }
                float offset2 = (2.0F / 16.0F / 3.0F);

                float xOffset = d.getFrontOffsetX() * offset;
                float zOffset = d.getFrontOffsetZ() * offset;
                float xOffset2 = d.getFrontOffsetX() * offset2;
                float zOffset2 = d.getFrontOffsetZ() * offset2;
                worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + 0.5D + xOffset, pos.getY() + 0.7D,
                  pos.getZ() + zOffset, 0F - xOffset2, 0.05F, 0F - zOffset2);
            }
            steamTick++;
            if (steamTick >= 4) {
                steamTick = 0;
            }

            if (volume < 0.75F) {
                volume += 0.01F;
            }
        } else if (volume > 0F) {
            volume -= 0.25F;
        } else {
            volume = 0F;
        }
    }

    public float getVolume() {
        return volume;
    }

    @Override
    public boolean shouldPlay() {
        return getPressure() > 1.02F;
    }

    @Override
    public ResourceLocation getSound() {
        return Steamcraft.SOUND_WHISTLE.getSoundName();
    }

    @Override
    public TileEntity getTileEntity() {
        return this;
    }

    @Override
    public boolean handleUpdate() {
        return true;
    }

    @Override
    public void update(SoundTile soundTile) {
        soundTile.volume = getVolume();
    }
}
