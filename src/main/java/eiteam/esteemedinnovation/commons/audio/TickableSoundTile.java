package eiteam.esteemedinnovation.commons.audio;

import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

import java.lang.ref.WeakReference;

public class TickableSoundTile implements ITickableSound {
    private WeakReference<SoundTile> soundTileReference;
    private ResourceLocation location;
    private BlockPos pos;
    public float volume = 1F;
    private boolean donePlaying = false;
    private Sound sound;

    public TickableSoundTile(SoundTile soundTile) {
        this.soundTileReference = new WeakReference<>(soundTile);
        this.location = soundTile.getSound();
    }

    @Override
    public boolean isDonePlaying() {
        return donePlaying;
    }

    @Override
    public ResourceLocation getSoundLocation() {
        return location;
    }

    @Override
    public SoundEventAccessor createAccessor(SoundHandler handler) {
        SoundEventAccessor soundEventAccessor = handler.getAccessor(getSoundLocation());
        sound = soundEventAccessor == null ? SoundHandler.MISSING_SOUND : soundEventAccessor.cloneEntry();
        return soundEventAccessor;
    }

    @Override
    public Sound getSound() {
        return sound;
    }

    @Override
    public SoundCategory getCategory() {
        return SoundCategory.BLOCKS;
    }

    @Override
    public boolean canRepeat() {
        return true;
    }

    @Override
    public int getRepeatDelay() {
        return 0;
    }

    @Override
    public float getVolume() {
        return volume;
    }

    @Override
    public float getPitch() {
        return 1F;
    }

    @Override
    public float getXPosF() {
        return (float) pos.getX();
    }

    @Override
    public float getYPosF() {
        return (float) pos.getY();
    }

    @Override
    public float getZPosF() {
        return (float) pos.getZ();
    }

    @Override
    public AttenuationType getAttenuationType() {
        return AttenuationType.LINEAR;
    }

    @Override
    public void update() {
        SoundTile soundTile = soundTileReference != null ? soundTileReference.get() : null;

        if (soundTile == null || soundTile.getTileEntity().isInvalid()) {
            soundTileReference = null;
            if (volume >= 0.0005F) {
                volume *= 0.9F;
            } else {
                donePlaying = true;
            }
        } else {
            pos = soundTile.getTileEntity().getPos();

            if (soundTile.handleUpdate()) {
                soundTile.update(this);
            } else {
                if (soundTile.shouldPlay()) {
                    if (volume < 0.995) {
                        volume = (1.0F - (1.0F - volume) * 0.9F);
                    } else {
                        volume = 1.0F;
                    }
                } else if (volume > 0.0005D) {
                    volume *= 0.9F;
                } else {
                    volume = 0.0F;
                }
            }
        }
    }
}
