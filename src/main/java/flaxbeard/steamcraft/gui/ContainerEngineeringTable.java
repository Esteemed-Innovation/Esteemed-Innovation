package flaxbeard.steamcraft.gui;

import org.apache.commons.lang3.tuple.MutablePair;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidContainerItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.api.IEngineerable;
import flaxbeard.steamcraft.tile.TileEntityEngineeringTable;

public class ContainerEngineeringTable extends Container
{
    private TileEntityEngineeringTable furnace;
    private int lastCookTime;
    private int lastBurnTime;
    private int lastItemBurnTime;
    private final InventoryPlayer inv;

    public ContainerEngineeringTable(InventoryPlayer par1InventoryPlayer, TileEntityEngineeringTable entity)
    {
        this.furnace = entity;
        inv = par1InventoryPlayer;
        
        this.addSlotToContainer(new Slot(entity, 0, 30, 35));
        for (int i = 1; i<10; i++) {
            this.addSlotToContainer(new SlotLimitedStackSize(entity, i, -100, -100));
        }
        this.updateSlots();
    	
        int i;

        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 4; ++i)
        {
            final int k = i;
            this.addSlotToContainer(new Slot(par1InventoryPlayer, par1InventoryPlayer.getSizeInventory() - 1 - i, 8, 8 + i * 18)
            {
            	private EntityPlayer player = inv.player;
                private static final String __OBFID = "CL_00001755";
                /**
                 * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1
                 * in the case of armor slots)
                 */
                public int getSlotStackLimit()
                {
                    return 1;
                }
                /**
                 * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
                 */
                public boolean isItemValid(ItemStack par1ItemStack)
                {
                    if (par1ItemStack == null) return false;
                    return par1ItemStack.getItem().isValidArmor(par1ItemStack, k, this.player);
                }
                /**
                 * Returns the icon index on items.png that is used as background image of the slot.
                 */
                @SideOnly(Side.CLIENT)
                public IIcon getBackgroundIconIndex()
                {
                    return ItemArmor.func_94602_b(k);
                }
            });
        }
        
        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, i, 8 + i * 18, 142));
        }
    }

    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        super.addCraftingToCrafters(par1ICrafting);
    }


    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2)
    {
       
    }

    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return true;
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
//        ItemStack itemstack = null;
//        Slot slot = (Slot)this.inventorySlots.get(par2);
//
//        if (slot != null && slot.getHasStack())
//        {
//            ItemStack itemstack1 = slot.getStack();
//            itemstack = itemstack1.copy();
//
//            if (par2 == 2)
//            {
//
//                return null;
//            }
//            else if (par2 != 1 && par2 != 0)
//            {
//            	if (itemstack1.getItem() ==  Items.water_bucket || (itemstack1.getItem() instanceof IFluidContainerItem && ((IFluidContainerItem)itemstack1.getItem()).getFluid(itemstack1).getFluid() == FluidRegistry.WATER))
//                {
//                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
//                    {
//                        return null;
//                    }
//                }
//            	else if (TileEntityFurnace.isItemFuel(itemstack1))
//                {
//                    if (!this.mergeItemStack(itemstack1, 1, 2, false))
//                    {
//                        return null;
//                    }
//                }
//                else if (par2 >= 2 && par2 < 30)
//                {
//                    if (!this.mergeItemStack(itemstack1, 30, 38, false))
//                    {
//                        return null;
//                    }
//                }
//                else if (par2 >= 30 && par2 < 39 && !this.mergeItemStack(itemstack1, 3, 30, false))
//                {
//                    return null;
//                }
//            }
//            else if (!this.mergeItemStack(itemstack1, 3, 38, false))
//            {
//                return null;
//            }
//
//            if (itemstack1.stackSize == 0)
//            {
//                slot.putStack((ItemStack)null);
//            }
//            else
//            {
//                slot.onSlotChanged();
//            }
//
//            if (itemstack1.stackSize == itemstack.stackSize)
//            {
//                return null;
//            }
//
//            slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
//        }

        return null;
    }
    
    public void updateSlots() {
    	boolean hasEngineer = false;

    	if (furnace.getStackInSlot(0) != null) {

        	if (furnace.getStackInSlot(0).getItem() instanceof IEngineerable) {

        		IEngineerable item = (IEngineerable) furnace.getStackInSlot(0).getItem();
        		hasEngineer = true;
        		int i = 1;
        		for (MutablePair<Integer,Integer> pair : item.engineerCoordinates()) {
        			int x = pair.left;
        			int y = pair.right;
        			((SlotLimitedStackSize)this.getSlot(i)).setSlotStackLimit(1);
        			this.getSlot(i).xDisplayPosition = x+53;
    	    		this.getSlot(i).yDisplayPosition = y+9;
    	    		
        			i++;
        			
        		}
        		
        	}
        }
    	if (!hasEngineer) {
	    	for (int i = 1; i<10; i++) {
	    	//	//Steamcraft.log.debug("KKK");
	    		this.getSlot(i).xDisplayPosition = -100;
	    		this.getSlot(i).yDisplayPosition = -100;

	    	}
    	}
    }
    
    public void detectAndSendChanges()
    {
    	super.detectAndSendChanges();
    }
    
    @Override
    public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer) {
        //this.updateSlots();
    	//if (par1 == 0) {
	        ItemStack toReturn = super.slotClick(par1, par2, par3, par4EntityPlayer);
	        this.updateSlots();
	    	furnace.getWorldObj().markBlockForUpdate(furnace.xCoord, furnace.yCoord, furnace.zCoord);
	        return toReturn;
    	//}
    	//return super.slotClick(par1, par2, par3, par4EntityPlayer);
    }
}
