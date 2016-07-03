package flaxbeard.steamcraft.gui;

import flaxbeard.steamcraft.api.IEngineerable;
import flaxbeard.steamcraft.misc.ItemStackUtility;
import flaxbeard.steamcraft.tile.TileEntityEngineeringTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.MutablePair;

public class ContainerEngineeringTable extends Container {
    private TileEntityEngineeringTable tileEntity;

    public ContainerEngineeringTable(InventoryPlayer inventoryPlayer, TileEntityEngineeringTable tileEntityEngineeringTable) {
        tileEntity = tileEntityEngineeringTable;
        final InventoryPlayer inv = inventoryPlayer;

        addSlotToContainer(new Slot(tileEntityEngineeringTable, 0, 30, 35));
        for (int i = 1; i < 10; i++) {
            addSlotToContainer(new SlotLimitedStackSize(tileEntityEngineeringTable, i, -1000, -1000));
        }
        this.updateSlots();

        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 4; ++i) {
            final int k = i;
            addSlotToContainer(new Slot(inventoryPlayer, inventoryPlayer.getSizeInventory() - 1 - i, 8, 8 + i * 18) {
                private EntityPlayer player = inv.player;

                public int getSlotStackLimit() {
                    return 1;
                }

                public boolean isItemValid(ItemStack stack) {
                    return stack != null && stack.getItem().isValidArmor(stack, ItemStackUtility.getSlotFromIndex(k), player);
                }

                @SideOnly(Side.CLIENT)
                public String getSlotTexture() {
                    return ItemArmor.EMPTY_SLOT_NAMES[k];
                }
            });
        }

        for (i = 0; i < 9; ++i) {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
        return true;
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        return null;
    }

    public void updateSlots() {
        boolean hasEngineer = false;

        ItemStack stackInSlotZero = tileEntity.getStackInSlot(0);
        if (stackInSlotZero != null) {
            if (stackInSlotZero.getItem() instanceof IEngineerable) {
                IEngineerable item = (IEngineerable) stackInSlotZero.getItem();
                hasEngineer = true;
                int i = 1;
                for (MutablePair<Integer, Integer> pair : item.engineerCoordinates()) {
                    int x = pair.left;
                    @SuppressWarnings("SuspiciousNameCombination") int y = pair.right;
                    ((SlotLimitedStackSize) getSlot(i)).setSlotStackLimit(1);
                    getSlot(i).xDisplayPosition = x + 53;
                    getSlot(i).yDisplayPosition = y + 9;

                    i++;
                }
            }
        }

        if (!hasEngineer) {
            for (int i = 1; i < 10; i++) {
                getSlot(i).xDisplayPosition = -1000;
                getSlot(i).yDisplayPosition = -1000;
            }
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
    }

    @Override
    public ItemStack slotClick(int slotID, int dragType, ClickType clickType, EntityPlayer player) {
        ItemStack toReturn = super.slotClick(slotID, dragType, clickType, player);
        this.updateSlots();
        tileEntity.markDirty();
//        tileEntity.getWorld().markBlockForUpdate(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
        return toReturn;
    }
}
