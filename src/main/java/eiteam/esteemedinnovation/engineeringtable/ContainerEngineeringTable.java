package eiteam.esteemedinnovation.engineeringtable;

import eiteam.esteemedinnovation.api.Engineerable;
import eiteam.esteemedinnovation.commons.gui.SlotLimitedStackSize;
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
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;

public class ContainerEngineeringTable extends Container {
    private static final EntityEquipmentSlot[] ARMOR_SLOTS = {
      EntityEquipmentSlot.HEAD,
      EntityEquipmentSlot.CHEST,
      EntityEquipmentSlot.LEGS,
      EntityEquipmentSlot.FEET
    };

    private TileEntityEngineeringTable tileEntity;

    public ContainerEngineeringTable(InventoryPlayer inventoryPlayer, TileEntityEngineeringTable tileEntityEngineeringTable) {
        tileEntity = tileEntityEngineeringTable;

        addSlotToContainer(new Slot(tileEntityEngineeringTable, 0, 30, 35));
        for (int i = 1; i < 10; i++) {
            addSlotToContainer(new SlotLimitedStackSize(tileEntityEngineeringTable, i, -1000, -1000));
        }
        updateSlots();

        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 4; ++i) {
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

        for (i = 0; i < 9; ++i) {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer player) {
        return true;
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        return ItemStack.EMPTY;
    }

    private void updateSlots() {
        ItemStack stackInSlotZero = tileEntity.getStackInSlot(0);
        Item itemInSlotZero = stackInSlotZero.getItem();
        if (itemInSlotZero instanceof Engineerable) {
            Engineerable item = (Engineerable) itemInSlotZero;
            int i = 1;
            for (Pair<Integer, Integer> pair : item.engineerCoordinates()) {
                int x = pair.getLeft();
                int y = pair.getRight();
                ((SlotLimitedStackSize) getSlot(i)).setSlotStackLimit(1);
                getSlot(i).xPos = x + 53;
                getSlot(i).yPos = y + 9;

                i++;
            }
        } else {
            for (int i = 1; i < 10; i++) {
                getSlot(i).xPos = -1000;
                getSlot(i).yPos = -1000;
            }
        }
    }

    @Nonnull
    @Override
    public ItemStack slotClick(int slotID, int dragType, ClickType clickType, EntityPlayer player) {
        ItemStack toReturn = super.slotClick(slotID, dragType, clickType, player);
        updateSlots();
        detectAndSendChanges();
        tileEntity.markForResync();
        return toReturn;
    }
}
