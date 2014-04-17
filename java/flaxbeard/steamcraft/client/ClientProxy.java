package flaxbeard.steamcraft.client;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.client.render.BlockSteamHeaterRenderer;
import flaxbeard.steamcraft.client.render.BlockSteamPipeRenderer;
import flaxbeard.steamcraft.client.render.IInventoryTESR;
import flaxbeard.steamcraft.client.render.ItemTESRRenderer;
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
    	TileEntitySpecialRenderer renderCrucible = new TileEntityCrucibleRenderer();
    	ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrucible.class, renderCrucible);
    	MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SteamcraftBlocks.crucible), new ItemTESRRenderer((IInventoryTESR) renderCrucible, new TileEntityCrucible()));
    	
    	TileEntitySpecialRenderer renderMold = new TileEntityMoldRenderer();
    	ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMold.class, new TileEntityMoldRenderer());
    	MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SteamcraftBlocks.mold), new ItemTESRRenderer((IInventoryTESR) renderMold, new TileEntityMold()));
    
    	RenderingRegistry.registerBlockHandler(Steamcraft.tubeRenderID, new BlockSteamPipeRenderer());
    	RenderingRegistry.registerBlockHandler(Steamcraft.heaterRenderID, new BlockSteamHeaterRenderer());
    }
	
}