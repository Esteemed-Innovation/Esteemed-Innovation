package flaxbeard.steamcraft.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.steamnet.SteamNetwork;
import flaxbeard.steamcraft.api.tile.SteamTransporterTileEntity;

public class TileEntityFluidSteamConverter extends SteamTransporterTileEntity implements ISteamTransporter,IFluidHandler,IWrenchable {
	private FluidTank dummyTank;
	private boolean isInitialized = false;

	@Override
	public void updateEntity() {
		if (!this.isInitialized) {
			this.setDistributionDirections(new ForgeDirection[] { ForgeDirection.getOrientation( this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord)).getOpposite()});
		}
		super.updateEntity();
	}
	
	@Override
	public boolean onWrench(ItemStack stack, EntityPlayer player, World world,
			int x, int y, int z, int side, float xO, float yO, float zO) {
		int steam = this.getSteam();
		this.getNetwork().split(this, true);
		this.setDistributionDirections(new ForgeDirection[] { ForgeDirection.getOrientation( this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord)).getOpposite()});
		
		SteamNetwork.newOrJoin(this);
		this.getNetwork().addSteam(steam);
		return false;
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		int meta = this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		if (from.ordinal() != meta) {
			return 0;
		}
        if (resource.fluidID == FluidRegistry.getFluid("steam").getID()) {
        	if (doFill) {
        		this.insertSteam(resource.amount, from);
        	}
        	return resource.amount;
        }
        return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) {
		int meta = this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		if (from.ordinal() != meta) {
			return null;
		}
		Fluid fluid = FluidRegistry.getFluid("steam");
        int drained = resource.amount;
        if (this.getSteam() < drained)
        {
            drained = this.getSteam();
        }

        FluidStack stack = new FluidStack(fluid, drained);
        if (doDrain)
        {
            this.decrSteam(drained);
        }
        return stack;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		int meta = this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		if (from.ordinal() != meta) {
			return null;
		}
		Fluid fluid = FluidRegistry.getFluid("steam");
        int drained = maxDrain;
        if (this.getSteam() < drained)
        {
            drained = this.getSteam();
        }

        FluidStack stack = new FluidStack(fluid, drained);
        if (doDrain)
        {
            this.decrSteam(drained);
        }
        return stack;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		int meta = this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		return from.ordinal() != meta;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		int meta = this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		System.out.println(from.ordinal() != meta);
		return from.ordinal() != meta;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		dummyTank = new FluidTank(new FluidStack(FluidRegistry.getFluid("steam"),this.getSteam()), this.getCapacity());
		return new FluidTankInfo[] {dummyTank.getInfo()};
	}

}
