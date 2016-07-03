package flaxbeard.steamcraft.client.render.tile;

import net.minecraft.tileentity.TileEntity;

public interface IInventoryTESR {
    void renderInventoryTileEntityAt(TileEntity tile, double x, double y, double z, float var8);
}
