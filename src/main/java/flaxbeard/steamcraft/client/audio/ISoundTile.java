package flaxbeard.steamcraft.client.audio;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

/**
 * @author dmillerw
 */
public interface ISoundTile {

    public boolean shouldPlay();

    public ResourceLocation getSound();

    public TileEntity getTileEntity();

    public boolean handleUpdate();

    public void update(SoundTile soundTile);
}
