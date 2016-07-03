package flaxbeard.steamcraft.misc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidTank;

public class FluidHelper {
	private static Fluid water = FluidRegistry.WATER;

	/**
	 * If this mod is used with other mods that use different types of water (fresh water, salt water etc),
	 * this can change which fluid FSB consider water thus increasing compatibility.
	 */
	public static void changeWaterFluid(Fluid newWater) {
		water = newWater;
	}

    /**
     * Check if the player is holding a water container.
     * @param player The player.
     * @return Whether the player is holding a container and it has water in it.
     */
	public static boolean playerIsHoldingWaterContainer(EntityPlayer player) {
		ItemStack heldItem = ItemStackUtility.getHeldItemStack(player);
		return itemStackIsWaterContainer(heldItem);
	}

    /**
     * Check if the ItemStack is a water container.
     * @param itemStack The ItemStack.
     * @return Whether the ItemStack is a container and has water in it.
     */
	public static boolean itemStackIsWaterContainer(ItemStack itemStack) {
		FluidStack fluid = getFluidFromItemStack(itemStack);

		return itemStack != null && fluid != null && fluid.getFluid() == water;
	}

    /**
     * Gets the FluidStack inside the ItemStack container.
     * @param itemStack The ItemStack.
     * @return The FluidStack in the ItemStack fluid container.
     */
	private static FluidStack getFluidFromItemStack(ItemStack itemStack) {
		if (itemStack == null) {
			return null;
		}

		FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(itemStack);

		if (fluid != null) {
			return fluid;
		}

		if (itemStack.getItem() instanceof IFluidContainerItem) {
			IFluidContainerItem fluidContainerItem = (IFluidContainerItem) itemStack.getItem();
			return fluidContainerItem.getFluid(itemStack);
		}

		return null;
	}

    /**
     * Fills the IFluidTank with the player's currently held item.
     * @param player The player.
     * @param tank The tank to fill.
     */
	public static void fillTankFromHeldItem(EntityPlayer player, IFluidTank tank) {
		ItemStack newContainer = fillTankFromItem(ItemStackUtility.getHeldItemStack(player), tank);

		if (player.capabilities.isCreativeMode) {
			return;
		}

		replaceHeldItemWithDrainedContainer(player, newContainer);
	}

    /**
     * Fills the IFluidTank with an ItemStack fluid container.
     * @param container The ItemStack holding the fluid.
     * @param tank The tank to fill.
     * @return The modified ItemStack, which probably has no fluid in it anymore.
     */
	public static ItemStack fillTankFromItem(ItemStack container, IFluidTank tank) {
		if (container == null) {
			return null;
		}

        /*
         * Special case for IFluidContainerItems. These items drain as much as they want (up to a limit)
         * and then update the item.
         */
		if (container.getItem() instanceof IFluidContainerItem) {
			IFluidContainerItem fluidContainerItem = (IFluidContainerItem) container.getItem();
			int roomLeftInContainer = getRoomLeftInTank(tank);

			if (roomLeftInContainer > 0) {
				FluidStack drained = fluidContainerItem.drain(container, roomLeftInContainer, true);
				tank.fill(drained, true);
			}

			return container;
		}

        /*
         * For all other containers, FluidContainerRegistry will contain the fluid, and we must
         * create a new ItemStack to represent the drained container.
         */
		FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(container);

		int amountFilled = tank.fill(fluid, true);

		if (amountFilled == 0) {
			return container;
		}

		FluidStack leftovers = fluid.copy();
		leftovers.amount = fluid.amount - amountFilled;

		return getContainerWithUpdatedAmount(container, leftovers);
	}

    /**
     * Gets how much room there is in the tank for more fluids.
     * @param tank The tank to check.
     * @return How much space there is in the tank for more fluid.
     */
	private static int getRoomLeftInTank(IFluidTank tank) {
		return tank.getCapacity() - tank.getFluidAmount();
	}

    /**
     * Replaces the player's currently held item with the new ItemStack.
     * @param player The player.
     * @param newContainer The new ItemStack.
     */
	private static void replaceHeldItemWithDrainedContainer(EntityPlayer player, ItemStack newContainer) {
		player.inventory.setInventorySlotContents(player.inventory.currentItem, newContainer);
		player.inventoryContainer.detectAndSendChanges();
	}

    /**
     * Returns the ItemStack with an updated amount of fluid in it.
     * @param container The ItemStack to update.
     * @param newFluidAmount The Fluid to remove from the ItemStack.
     * @return The modified ItemStack.
     */
	private static ItemStack getContainerWithUpdatedAmount(ItemStack container, FluidStack newFluidAmount) {
		if (container.getItem() instanceof IFluidContainerItem) {
			IFluidContainerItem fluidContainerItem = (IFluidContainerItem) container.getItem();
			fluidContainerItem.drain(container, newFluidAmount.amount, true);
			return container;
		}

		ItemStack newContainer = FluidContainerRegistry.drainFluidContainer(container);

		if (newFluidAmount.amount > 0) {
			return FluidContainerRegistry.fillFluidContainer(newFluidAmount, newContainer);
		}

		return newContainer;
	}
}
