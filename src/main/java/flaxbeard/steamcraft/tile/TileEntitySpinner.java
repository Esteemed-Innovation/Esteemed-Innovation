package flaxbeard.steamcraft.tile;

import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.api.tile.SteamTransporterTileEntity;

import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntitySpinner extends SteamTransporterTileEntity {

	private boolean isPowered = false;
	private int spinTicks = 0;
	private int cost      = Config.spinnerConsumption;

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
		int x = xCoord;
		int y = yCoord;
		int z = zCoord;
        ForgeDirection axis = ForgeDirection.WEST;

		if (worldObj.isBlockIndirectlyGettingPowered(x, y, z)) {
			isPowered = true;
		}


		if (isPowered && !worldObj.isRemote && getSteamShare() >= cost && spinTicks == 15) {
			Block blockAboveSpinner = worldObj.getBlock(x, y + 1, z);

			if (blockAboveSpinner.getValidRotations(worldObj, x, y, z) != null) {
				System.out.println(
				  String.format("%1$s can rotate.", blockAboveSpinner.getLocalizedName()));

				spinTicks++;
				if (blockAboveSpinner.rotateBlock(worldObj, x, y, z, axis)) {
					System.out.println("Block was rotated.");
					this.decrSteam(cost);
					isPowered = false;
				} else {
					System.out.println("Block was not rotated.");
				}
			} else {
				System.out.println(
				  String.format("%1$s cannot rotate.", blockAboveSpinner.getLocalizedName()));
			}
		}

		if (isPowered) {
			spinTicks = 0;
		}

		if (spinTicks > 15) {
			isPowered = false;
		}
	}
}
