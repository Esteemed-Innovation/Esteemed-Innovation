package flaxbeard.steamcraft.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import flaxbeard.steamcraft.tile.TileEntityBoiler;

public class SteamcraftGuiHandler implements IGuiHandler {
	public SteamcraftGuiHandler() {

	}
	
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity entity = world.getTileEntity(x, y, z);
		int side = 5;
		switch(id) {
		case 0:
			if(entity != null && entity instanceof TileEntityBoiler) {
				return new ContainerBoiler(player.inventory, (TileEntityBoiler) entity);
			} else {
				return null;
			}
		default:
			return null;
		}
	}
		
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity entity = world.getTileEntity(x, y, z);
		int side = 5;
		switch(id) {
		case 0:
			if(entity != null && entity instanceof TileEntityBoiler) {
				return new GuiBoiler(player.inventory, (TileEntityBoiler) entity);
			} else {
				return null;
			}
		default:
			return null;
		}
	}
}