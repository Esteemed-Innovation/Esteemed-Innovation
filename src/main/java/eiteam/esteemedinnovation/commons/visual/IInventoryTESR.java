package eiteam.esteemedinnovation.commons.visual;

import net.minecraft.tileentity.TileEntity;

public interface IInventoryTESR<T extends TileEntity> {
    void renderInventoryTileEntityAt(T tile, double x, double y, double z, float var8);
}
