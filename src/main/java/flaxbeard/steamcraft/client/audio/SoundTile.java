package flaxbeard.steamcraft.client.audio;

import net.minecraft.client.audio.ITickableSound;
import net.minecraft.util.ResourceLocation;

import java.lang.ref.WeakReference;

/**
 * @author dmillerw
 */
public class SoundTile implements ITickableSound {

    private WeakReference<ISoundTile> soundTileReference;

    private ResourceLocation location;

    private float zPosF = 0F;
    private float xPosF = 0F;
    private float yPosF = 0F;

    public float volume = 1F;

    private boolean donePlaying = false;

    public SoundTile(ISoundTile soundTile) {
        this.soundTileReference = new WeakReference<ISoundTile>(soundTile);
        this.location = soundTile.getSound();
    }

    @Override
    public boolean isDonePlaying() {
        return donePlaying;
    }

    @Override
    public ResourceLocation getPositionedSoundLocation() {
        return location;
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
        return xPosF;
    }

    @Override
    public float getYPosF() {
        return yPosF;
    }

    @Override
    public float getZPosF() {
        return zPosF;
    }

    @Override
    public AttenuationType getAttenuationType() {
        return AttenuationType.LINEAR;
    }

    @Override
    public void update() {
        ISoundTile soundTile = this.soundTileReference != null ? this.soundTileReference.get() : null;

        if (soundTile == null || soundTile.getTileEntity().isInvalid()) {
            this.soundTileReference = null;
            if (this.volume >= 0.0005F) {
                this.volume *= 0.9F;
            } else {
                this.donePlaying = true;
            }
        } else {
            this.xPosF = soundTile.getTileEntity().xCoord + 0.5F;
            this.yPosF = soundTile.getTileEntity().yCoord + 0.5F;
            this.zPosF = soundTile.getTileEntity().zCoord + 0.5F;

            if (soundTile.handleUpdate()) {
                soundTile.update(this);
            } else {
                if (soundTile.shouldPlay()) {
                    if (this.volume < 0.995) {
                        this.volume = (1.0F - (1.0F - this.volume) * 0.9F);
                    } else {
                        this.volume = 1.0F;
                    }
                } else if (this.volume > 0.0005D) {
                    this.volume *= 0.9F;
                } else {
                    this.volume = 0.0F;
                }
            }
        }
    }
}
