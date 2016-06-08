package flaxbeard.steamcraft.tile;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.tile.SteamReactorTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;

public class TileEntityRuptureDisc extends SteamReactorTileEntity implements ITickable {
    private boolean isLeaking = false;

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound access = new NBTTagCompound();
        access.setBoolean("isLeaking", isLeaking);
        return new SPacketUpdateTileEntity(pos, 1, access);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound access = pkt.getNbtCompound();
        isLeaking = access.getBoolean("isLeaking");
        markForUpdate();
    }

    @Override
    public void update() {
        if (worldObj.isRemote) {
            if (isLeaking) {
                EnumFacing dir = EnumFacing.getFront(getBlockMetadata());
                float offset = 10.0F / 16.0F;
                float xOffset = dir.getOpposite().getFrontOffsetX() * offset;
                float yOffset = dir.getOpposite().getFrontOffsetY() * offset;
                float zOffset = dir.getOpposite().getFrontOffsetZ() * offset;
                //for (int i = 0; i < 10; i++){
                worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + 0.5F + xOffset,
                  pos.getY() + 0.5F + yOffset, pos.getZ() + 0.5F + zOffset, dir.getFrontOffsetX() * 0.1F,
                  dir.getFrontOffsetY() * 0.1F, dir.getFrontOffsetZ() * 0.1F);
            }
        } else {
            if (getPressure() > 1.1F) {
                if (getBlockMetadata() < 6) {
                    worldObj.createExplosion(null, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 0.0F, true);
                    worldObj.setBlockState(pos, getBlockType().getStateFromMeta(getBlockMetadata() + 10), 2);
                }
            }
            if (getBlockMetadata() > 9) {
                int i = 0;
                if (getSteam() > 0) {
                    if (!isLeaking) {
                        isLeaking = true;
                        markForUpdate();
                    }
                    worldObj.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, Steamcraft.SOUND_LEAK,
                      SoundCategory.BLOCKS, 2F, 0.9F, false);
                } else {
                    if (isLeaking) {
                        isLeaking = false;
                        markForUpdate();
                    }
                }
                while (getSteam() > 0 && i < 10) {
                    drainSteam(10);
                    i++;
                }
            } else {
                if (isLeaking) {
                    isLeaking = false;
                    markForUpdate();
                }
            }
        }
    }
}
