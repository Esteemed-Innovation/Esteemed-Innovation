package flaxbeard.steamcraft.tile;

import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.api.tile.SteamTransporterTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.world.World;
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

		Block blockAbove = worldObj.getBlock(x, y + 1, z);

		ForgeDirection[] nesw = {
		  ForgeDirection.NORTH,
		  ForgeDirection.EAST,
		  ForgeDirection.SOUTH,
		  ForgeDirection.WEST
		};

		if (worldObj.isBlockIndirectlyGettingPowered(x, y, z)) {
			isPowered = true;
		}

		ForgeDirection[] validRotations = blockAbove.getValidRotations(worldObj, x, y, z);

		if (!worldObj.isRemote && getSteamShare() > cost && isPowered &&
		  !blockAbove.isAir(worldObj, x, y, z) && validRotations != null) {
			System.out.println(
			  String.format("%1$s can rotate.", blockAbove.getLocalizedName()));
			isPowered = false;

			for (ForgeDirection axis : validRotations) {
				if (blockAbove.rotateBlock(worldObj, x, y, z, axis)) {
					worldObj.markBlockForUpdate(x, y + 1, z);
					System.out.println("Block was rotated");
					this.decrSteam(cost);
				} else {
					System.out.println("Block was not rotated.");
				}
			}
		}
	}
}
