package flaxbeard.steamcraft.gui;

import flaxbeard.steamcraft.api.IEngineerable;
import flaxbeard.steamcraft.tile.TileEntityEngineeringTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.MutablePair;

import javax.annotation.Nullable;

public class ContainerEngineeringTable extends Container {
    private static final EntityEquipmentSlot[] ARMOR_SLOTS = new EntityEquipmentSlot[] {
      EntityEquipmentSlot.HEAD,
      EntityEquipmentSlot.CHEST,
      EntityEquipmentSlot.LEGS,
      EntityEquipmentSlot.FEET
    };

    private TileEntityEngineeringTable tileEntity;

    public ContainerEngineeringTable(InventoryPlayer inventoryPlayer, TileEntityEngineeringTable tileEntityEngineeringTable) {
        tileEntity = tileEntityEngineeringTable;
        final InventoryPlayer inv = inventoryPlayer;

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
            final EntityEquipmentSlot equipmentSlot = ARMOR_SLOTS[i];
            // The index stuff is terrible. Sorry.
            addSlotToContainer(new Slot(inventoryPlayer, inventoryPlayer.getSizeInventory() - 2 - i, 8, 8 + i * 18) {
                private EntityPlayer player = inv.player;

                @Override
                public int getSlotStackLimit() {
                    return 1;
                }

                @Override
                public boolean isItemValid(ItemStack stack) {
                    return stack != null && stack.getItem().isValidArmor(stack, equipmentSlot, player);
                }

                @SideOnly(Side.CLIENT)
                public String getSlotTexture() {
                    return ItemArmor.EMPTY_SLOT_NAMES[equipmentSlot.getIndex()];
                }
            });
        }

        addSlotToContainer(new Slot(inventoryPlayer, 40, 30, 62) {
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
    public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        return null;
    }

    private void updateSlots() {
        boolean hasEngineer = false;

        ItemStack stackInSlotZero = tileEntity.getStackInSlot(0);
        if (stackInSlotZero != null) {
            if (stackInSlotZero.getItem() instanceof IEngineerable) {
                IEngineerable item = (IEngineerable) stackInSlotZero.getItem();
                hasEngineer = true;
                int i = 1;
                for (MutablePair<Integer, Integer> pair : item.engineerCoordinates()) {
                    int x = pair.left;
                    int y = pair.right;
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
        updateSlots();
        tileEntity.markDirty();
//        tileEntity.getWorld().markBlockForUpdate(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
        return toReturn;
    }
}
