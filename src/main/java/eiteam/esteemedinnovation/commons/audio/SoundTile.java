package eiteam.esteemedinnovation.commons.audio;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public interface SoundTile {
    boolean shouldPlay();
    ResourceLocation getSound();
    TileEntity getTileEntity();
    boolean handleUpdate();
    void update(TickableSoundTile tickableSoundTile);
}
