package eiteam.esteemedinnovation.client.audio;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public interface ISoundTile {
    boolean shouldPlay();
    ResourceLocation getSound();
    TileEntity getTileEntity();
    boolean handleUpdate();
    void update(SoundTile soundTile);
}
