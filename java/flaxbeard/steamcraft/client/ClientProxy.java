package flaxbeard.steamcraft.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import flaxbeard.steamcraft.client.render.TileEntityCrucibleRenderer;
import flaxbeard.steamcraft.client.render.TileEntityMoldRenderer;
import flaxbeard.steamcraft.common.CommonProxy;
import flaxbeard.steamcraft.tile.TileEntityCrucible;
import flaxbeard.steamcraft.tile.TileEntityMold;


public class ClientProxy extends CommonProxy
{
    @Override
    public void registerRenderers()
    {
    	ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrucible.class, new TileEntityCrucibleRenderer());
    	ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMold.class, new TileEntityMoldRenderer());
    }
	
}