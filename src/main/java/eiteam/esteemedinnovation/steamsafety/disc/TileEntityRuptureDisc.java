package eiteam.esteemedinnovation.steamsafety.disc;

import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.api.tile.SteamReactorTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;

public class TileEntityRuptureDisc extends SteamReactorTileEntity implements ITickable {
    private boolean isLeaking = false;

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
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
        IBlockState startingState = worldObj.getBlockState(pos);
        EnumFacing dir = startingState.getValue(BlockRuptureDisc.FACING);
        if (worldObj.isRemote) {
            if (isLeaking) {
                float offset = 10.0F / 16.0F;
                float xOffset = dir.getOpposite().getFrontOffsetX() * offset;
                float yOffset = dir.getOpposite().getFrontOffsetY() * offset;
                float zOffset = dir.getOpposite().getFrontOffsetZ() * offset;
                worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + 0.5F + xOffset,
                  pos.getY() + 0.5F + yOffset, pos.getZ() + 0.5F + zOffset, dir.getFrontOffsetX() * 0.1F,
                  dir.getFrontOffsetY() * 0.1F, dir.getFrontOffsetZ() * 0.1F);
            }
        } else {
            if (getPressure(dir) > 1.1F && !startingState.getValue(BlockRuptureDisc.IS_BURST)) {
                worldObj.createExplosion(null, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 0.0F, true);
                worldObj.setBlockState(pos, startingState.withProperty(BlockRuptureDisc.IS_BURST, true));
            }
            // We may or may not change the state up there^
            startingState = worldObj.getBlockState(pos);
            if (startingState.getValue(BlockRuptureDisc.IS_BURST)) {
                int i = 0;
                if (getSteam(dir) > 0) {
                    if (!isLeaking) {
                        isLeaking = true;
                        markForResync(startingState);
                        markDirty();
                    }

                    worldObj.playSound(null, pos, EsteemedInnovation.SOUND_LEAK, SoundCategory.BLOCKS, 2F, 0.9F);
                } else {
                    if (isLeaking) {
                        isLeaking = false;
                        markForResync(startingState);
                    }
                }
                while (getSteam(dir) > 0 && i < 10) {
                    drainSteam(10, dir);
                    i++;
                }
            } else {
                if (isLeaking) {
                    isLeaking = false;
                    markForResync(startingState);
                }
            }
        }
    }
}
