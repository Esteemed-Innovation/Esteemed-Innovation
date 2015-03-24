package flaxbeard.steamcraft.tile;

import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.api.tile.SteamTransporterTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntitySpinner extends SteamTransporterTileEntity {

	private boolean isPowered = false;
	private int cost = Config.spinnerConsumption;

	@Override
	public boolean canInsert(ForgeDirection dir) {
		if (dir == ForgeDirection.UP) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		int x = xCoord;
		int y = yCoord;
		int z = zCoord;

		ForgeDirection never = ForgeDirection.NORTH;
		ForgeDirection eat = ForgeDirection.EAST;
		ForgeDirection soggy = ForgeDirection.SOUTH;
		ForgeDirection waffles = ForgeDirection.WEST;
		ForgeDirection[] nesw = {never, eat, soggy, waffles};

		Block blockAbove = worldObj.getBlock(x, y + 1, z);

		if (worldObj.isBlockIndirectlyGettingPowered(x, y, z)) {
			isPowered = true;
		}

		if (isPowered && !worldObj.isRemote && getSteamShare() > cost && blockAbove != null &&
		  blockAbove != null && !blockAbove.isAir(worldObj, x, y, z)) {
			if (blockAbove.getValidRotations(worldObj, x, y, z) != null) {
				System.out.println(
				  String.format("%1$s can rotate.", blockAbove.getLocalizedName()));

				if (blockAbove.rotateBlock(worldObj, x, y, z, never)) {
					worldObj.markBlockForUpdate(x, y + 1, z);
					System.out.println(String.format("Block was rotated to %1$s.", never));
					this.decrSteam(cost);
					isPowered = false;
				} else {
					isPowered = false;
					System.out.println("Block was not rotated.");
				}
			} else {
				System.out.println(
				  String.format("%1$s cannot rotate.", blockAbove.getLocalizedName()));
			}
		}
	}
}
