package flaxbeard.steamcraft.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import flaxbeard.steamcraft.tile.TileEntityBoiler;
import flaxbeard.steamcraft.tile.TileEntityEngineeringTable;
import flaxbeard.steamcraft.tile.TileEntitySteamHammer;

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
		case 2:
			if(entity != null && entity instanceof TileEntityEngineeringTable) {
				return new ContainerEngineeringTable(player.inventory, (TileEntityEngineeringTable) entity);
			} else {
				return null;
			}
		case 3:
			if(entity != null && entity instanceof TileEntitySteamHammer) {
				return new ContainerSteamAnvil(player.inventory, (TileEntitySteamHammer) entity, world, x, y-1, z, player);
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
		case 1:
			return new GuiSteamcraftBook(player, player.getHeldItem(), false);
		case 2:
			if(entity != null && entity instanceof TileEntityEngineeringTable) {
				return new GuiEngineeringTable(player.inventory, (TileEntityEngineeringTable) entity);
			} else {
				return null;
			}
		case 3:
			if(entity != null && entity instanceof TileEntitySteamHammer) {
				return new GuiSteamAnvil(player.inventory, (TileEntitySteamHammer) entity, world, x, y-1, z);
			} else {
				return null;
			}
		default:
			return null;
		}
	}
}