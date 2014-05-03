package flaxbeard.steamcraft.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidContainerItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.tile.TileEntityBoiler;

public class ContainerBoiler extends Container
{
    private TileEntityBoiler furnace;
    private int lastCookTime;
    private int lastBurnTime;
    private int lastItemBurnTime;

    public ContainerBoiler(InventoryPlayer par1InventoryPlayer, TileEntityBoiler par2TileEntityBoiler)
    {
        this.furnace = par2TileEntityBoiler;
        
        this.addSlotToContainer(new Slot(par2TileEntityBoiler, 1, 58, 55));
        this.addSlotToContainer(new Slot(par2TileEntityBoiler, 0, 58, 32));
        int i;

        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, i, 8 + i * 18, 142));
        }
    }

    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        super.addCraftingToCrafters(par1ICrafting);
        par1ICrafting.sendProgressBarUpdate(this, 0, this.furnace.furnaceCookTime);
        par1ICrafting.sendProgressBarUpdate(this, 1, this.furnace.furnaceBurnTime);
        par1ICrafting.sendProgressBarUpdate(this, 2, this.furnace.getItemBurnTime(null));
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);

            if (this.lastCookTime != this.furnace.furnaceCookTime)
            {
                icrafting.sendProgressBarUpdate(this, 0, this.furnace.furnaceCookTime);
            }

            if (this.lastBurnTime != this.furnace.furnaceBurnTime)
            {
                icrafting.sendProgressBarUpdate(this, 1, this.furnace.furnaceBurnTime);
            }

            if (this.lastItemBurnTime != this.furnace.currentItemBurnTime)
            {
                icrafting.sendProgressBarUpdate(this, 2, this.furnace.currentItemBurnTime);
            }
        }

        this.lastCookTime = this.furnace.furnaceCookTime;
        this.lastBurnTime = this.furnace.furnaceBurnTime;
        this.lastItemBurnTime = this.furnace.currentItemBurnTime;
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2)
    {
        if (par1 == 0)
        {
            this.furnace.furnaceCookTime = par2;
        }

        if (par1 == 1)
        {
            this.furnace.furnaceBurnTime = par2;
        }

        if (par1 == 2)
        {
            this.furnace.currentItemBurnTime = par2;
        }
    }

    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.furnace.isUseableByPlayer(par1EntityPlayer);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 == 2)
            {

                return null;
            }
            else if (par2 != 1 && par2 != 0)
            {
            	if (itemstack1.getItem() ==  Items.water_bucket || (itemstack1.getItem() instanceof IFluidContainerItem && ((IFluidContainerItem)itemstack1.getItem()).getFluid(itemstack1).getFluid() == FluidRegistry.WATER))
                {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return null;
                    }
                }
            	else if (TileEntityFurnace.isItemFuel(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false))
                    {
                        return null;
                    }
                }
                else if (par2 >= 2 && par2 < 30)
                {
                    if (!this.mergeItemStack(itemstack1, 30, 38, false))
                    {
                        return null;
                    }
                }
                else if (par2 >= 30 && par2 < 39 && !this.mergeItemStack(itemstack1, 3, 30, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 3, 38, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
        }

        return itemstack;
    }
}
