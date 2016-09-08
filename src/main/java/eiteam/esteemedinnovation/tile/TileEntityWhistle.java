package eiteam.esteemedinnovation.tile;

import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.api.tile.SteamReactorTileEntity;
import eiteam.esteemedinnovation.block.BlockWhistle;
import eiteam.esteemedinnovation.client.audio.ISoundTile;
import eiteam.esteemedinnovation.client.audio.SoundTile;
import eiteam.esteemedinnovation.tile.pipe.TileEntitySteamPipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityWhistle extends SteamReactorTileEntity implements ISoundTile, ITickable {
    private float volume;
    private boolean isSoundRegistered;
    private boolean isSounding;
    private int steamTick;

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
            markForResync();
        }
    }

    @SideOnly(Side.CLIENT)
    private void updateSound() {
        if (!isSoundRegistered) {
            worldObj.playSound(null, pos, EsteemedInnovation.SOUND_WHISTLE, SoundCategory.BLOCKS, volume, 1F);
//                Sounds.addSoundTile(this);
            isSoundRegistered = true;
        }

        if (isSounding) {
            if (steamTick == 0) {
                EnumFacing dir = worldObj.getBlockState(pos).getValue(BlockWhistle.FACING).getOpposite();
                TileEntity te = worldObj.getTileEntity(pos.offset(dir));
                float offset = 2.0F / 16.0F;
                if (te instanceof TileEntitySteamPipe) {
                    offset = 6.0F / 16.0F;
                }
                float offset2 = (2.0F / 16.0F / 3.0F);

                float xOffset = dir.getFrontOffsetX() * offset;
                float zOffset = dir.getFrontOffsetZ() * offset;
                float yOffset = dir.getFrontOffsetY() * offset;
                float xOffset2 = dir.getFrontOffsetX() * offset2;
                float zOffset2 = dir.getFrontOffsetZ() * offset2;
                worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + 0.5D + xOffset,
                  pos.getY() + 0.7D + yOffset, pos.getZ() + 0.5D + zOffset, 0F - xOffset2, 0.05F, 0F - zOffset2);

                worldObj.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                  EsteemedInnovation.SOUND_WHISTLE, SoundCategory.BLOCKS, volume, 1F, true);
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

    @Override
    public boolean shouldPlay() {
        return getPressure(worldObj.getBlockState(pos).getValue(BlockWhistle.FACING)) > 1.02F;
    }

    @Override
    public ResourceLocation getSound() {
        return EsteemedInnovation.SOUND_WHISTLE.getSoundName();
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
        soundTile.volume = volume;
    }
}
