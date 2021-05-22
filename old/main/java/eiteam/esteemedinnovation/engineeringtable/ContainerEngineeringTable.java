package eiteam.esteemedinnovation.engineeringtable;

import eiteam.esteemedinnovation.api.Engineerable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;

import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

public class ContainerEngineeringTable extends Container {
    private static final EntityEquipmentSlot[] ARMOR_SLOTS = {
      EntityEquipmentSlot.HEAD,
      EntityEquipmentSlot.CHEST,
      EntityEquipmentSlot.LEGS,
      EntityEquipmentSlot.FEET
    };

    private TileEntityEngineeringTable tileEntity;
    private IItemHandler itemHandler;
    private int prevNumSlots;
    private int numBaseSlots;
    private boolean upgradeSlotsInitialized;
    private ItemStack prevItemStack = ItemStack.EMPTY;

    public ContainerEngineeringTable(InventoryPlayer inventoryPlayer, TileEntityEngineeringTable tileEntityEngineeringTable) {
        tileEntity = tileEntityEngineeringTable;

        itemHandler = tileEntityEngineeringTable.getCapability(ITEM_HANDLER_CAPABILITY, null);
        addSlotToContainer(new SlotItemHandler(itemHandler, 0, 30, 35));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 4; ++i) {
            EntityEquipmentSlot equipmentSlot = ARMOR_SLOTS[i];
            // The index stuff is terrible. Sorry.
            addSlotToContainer(new Slot(inventoryPlayer, inventoryPlayer.getSizeInventory() - 2 - i, 8, 8 + i * 18) {
                private EntityPlayer player = inventoryPlayer.player;

                @Override
                public int getSlotStackLimit() {
                    return 1;
                }

                @Override
                public boolean isItemValid(ItemStack stack) {
                    return !stack.isEmpty() && stack.getItem().isValidArmor(stack, equipmentSlot, player);
                }

                @Override
                @SideOnly(Side.CLIENT)
                public String getSlotTexture() {
                    return ItemArmor.EMPTY_SLOT_NAMES[equipmentSlot.getIndex()];
                }
            });
        }

        addSlotToContainer(new Slot(inventoryPlayer, 40, 30, 62) {
            @Override
            @SideOnly(Side.CLIENT)
            public String getSlotTexture() {
                return "minecraft:items/empty_armor_slot_shield";
            }
        });

        for (int i = 0; i < 9; ++i) {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
        }

        // We do it here after the base slots have been initialized because the rest in the list are more modular
        // In other words, we cannot ensure nicely how many slots there are at this point, so instead we put them
        // all at the end.
        prevNumSlots = inventorySlots.size();
        numBaseSlots = prevNumSlots;

        updateUpgradeSlots();
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer player) {
        return true;
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        Slot slot = this.inventorySlots.get(index);

        if(slot != null && slot.getHasStack()) {
            ItemStack itemStack = slot.getStack();

            //If clicked slot is in the player inventory
            if(index <= 41 && index != 0) {
                //Attempt to put it in engineering slot
                if(!mergeItemStack(itemStack, 0, 1, false)) {
                    //Now try the other slots (not player inventory
                    if (!mergeItemStack(itemStack, 42, inventorySlots.size(), false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else {
                //Clicked within engineering slot or modifiers
                //Try placing itemstack in inventory
                //If we don't want shift click to go into offhand slot, either need to change the order of the slots,
                //or nest this like the one above
                if(!mergeItemStack(itemStack, 1, 42, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if(itemStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return ItemStack.EMPTY;
    }

    private void updateUpgradeSlots() {
        ItemStack stackInSlotZero = itemHandler.getStackInSlot(0);
        Item itemInSlotZero = stackInSlotZero.getItem();
        if (itemInSlotZero instanceof Engineerable) {
            if (!upgradeSlotsInitialized) {
                Engineerable item = (Engineerable) itemInSlotZero;
                int i = 1;
                for (Pair<Integer, Integer> pair : item.engineerCoordinates()) {
                    addSlotToContainer(new SlotEngineerableUpgradeOnly(itemHandler, i, pair.getLeft() + 53, pair.getRight() + 9));
                    i++;
                }
                ((ItemStackHandler) itemHandler).setSize(i);
                itemHandler.insertItem(0, stackInSlotZero, false);
                for (int internalSlot = 1; internalSlot < i; internalSlot++) {
                    itemHandler.insertItem(internalSlot, item.getStackInSlot(stackInSlotZero, internalSlot), false);
                }
                upgradeSlotsInitialized = true;
            } else if (prevItemStack != stackInSlotZero) {
                for (int i = numBaseSlots; i < prevNumSlots; i++) {
                    int index = inventorySlots.size() - 1;
                    inventorySlots.remove(index);
                    inventoryItemStacks.remove(index);
                }
                ((ItemStackHandler) itemHandler).setSize(1);
                itemHandler.insertItem(0, stackInSlotZero, false);
                upgradeSlotsInitialized = false;
                updateUpgradeSlots();
            }
        } else {
            for (int i = numBaseSlots; i < prevNumSlots; i++) {
                int index = inventorySlots.size() - 1;
                inventorySlots.remove(index);
                inventoryItemStacks.remove(index);
            }
            ((ItemStackHandler) itemHandler).setSize(1);
            itemHandler.insertItem(0, stackInSlotZero, false);
            upgradeSlotsInitialized = false;
        }
        prevNumSlots = inventorySlots.size();
        prevItemStack = stackInSlotZero;
    }

    @Nonnull
    @Override
    public ItemStack slotClick(int slotID, int dragType, ClickType clickType, EntityPlayer player) {
        ItemStack toReturn = super.slotClick(slotID, dragType, clickType, player);
        updateUpgradeSlots();
        detectAndSendChanges();
        tileEntity.markForResync();
        return toReturn;
    }
}
