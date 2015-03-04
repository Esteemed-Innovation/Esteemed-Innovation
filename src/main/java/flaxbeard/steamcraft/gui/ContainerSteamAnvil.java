package flaxbeard.steamcraft.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.tile.TileEntitySteamHammer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.Map;

public class ContainerSteamAnvil extends Container {
    private static final Logger logger = LogManager.getLogger();
    private static final String __OBFID = "CL_00001732";
    /**
     * The player that has this container open.
     */
    private final EntityPlayer thePlayer;
    public TileEntitySteamHammer hammer;
    private World theWorld;
    private int field_82861_i;
    /** The maximum cost of repairing/renaming in the anvil. */
    // public int hammer.cost;
    private int field_82858_j;
    private int field_82859_k;
    /**
     * determined by damage of input item and stackSize of repair materials
     */
    private int stackSizeToBeUsedInRepair;

    public ContainerSteamAnvil(InventoryPlayer par1InventoryPlayer, TileEntitySteamHammer par2Hammer, final World par2World, final int par3, final int par4, final int par5, EntityPlayer par6EntityPlayer) {
        //super(par1InventoryPlayer, par2World, par3, par4, par5, par6EntityPlayer);
        this.theWorld = par2World;
        this.field_82861_i = par3;
        this.field_82858_j = par4;
        this.field_82859_k = par5;
        this.thePlayer = par6EntityPlayer;
        hammer = par2Hammer;
        this.addSlotToContainer(new Slot(hammer, 0, 27, 47) {
            public void onSlotChanged() {
                ContainerSteamAnvil.this.onCraftMatrixChanged(hammer);
                super.onSlotChanged();
            }
        });
        this.addSlotToContainer(new Slot(hammer, 1, 76, 47) {
            public void onSlotChanged() {
                ContainerSteamAnvil.this.onCraftMatrixChanged(hammer);
                super.onSlotChanged();
            }
        });
        this.addSlotToContainer(new Slot(hammer, 2, 134, 47) {
            private static final String __OBFID = "CL_00001734";

            /**
             * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
             * GG Flax^
             */
            public boolean isItemValid(ItemStack par1ItemStack) {
                return false;
            }

            /**
             * Return whether this slot's stack can be taken from this slot.
             */
            public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
                return hammer.cost > 0 && hammer.cost == hammer.progress && this.getHasStack();
            }

            public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack) {
//                if (!par1EntityPlayer.capabilities.isCreativeMode)
//                {
//                    par1EntityPlayer.addExperienceLevel(-ContainerSteamAnvil.this.hammer.cost);
//                }
                hammer.progress = 0;
                hammer.setInventorySlotContents(0, (ItemStack) null);
                onCraftMatrixChanged(hammer);

                if (ContainerSteamAnvil.this.stackSizeToBeUsedInRepair > 0) {
                    ItemStack itemstack1 = hammer.getStackInSlot(1);

                    if (itemstack1 != null && itemstack1.stackSize > ContainerSteamAnvil.this.stackSizeToBeUsedInRepair) {
                        itemstack1.stackSize -= ContainerSteamAnvil.this.stackSizeToBeUsedInRepair;
                        hammer.setInventorySlotContents(1, itemstack1);
                        onCraftMatrixChanged(hammer);
                    } else {
                        hammer.setInventorySlotContents(1, (ItemStack) null);
                        onCraftMatrixChanged(hammer);
                    }
                } else {
                    hammer.setInventorySlotContents(1, (ItemStack) null);
                    onCraftMatrixChanged(hammer);
                }

                ContainerSteamAnvil.this.hammer.cost = 0;

                if (!par1EntityPlayer.capabilities.isCreativeMode && !par2World.isRemote && par2World.getBlock(par3, par4, par5) == Blocks.anvil && par1EntityPlayer.getRNG().nextFloat() < 0.12F) {
                    int i1 = par2World.getBlockMetadata(par3, par4, par5);
                    int k = i1 & 3;
                    int l = i1 >> 2;
                    ++l;

                    if (l > 2) {
                        par2World.setBlockToAir(par3, par4, par5);
                        par2World.playAuxSFX(1020, par3, par4, par5, 0);
                    } else {
                        par2World.setBlockMetadataWithNotify(par3, par4, par5, k | l << 2, 2);
                        par2World.playAuxSFX(1021, par3, par4, par5, 0);
                    }
                } else if (!par2World.isRemote) {
                    par2World.playAuxSFX(1021, par3, par4, par5, 0);
                }
            }
        });
        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, i, 8 + i * 18, 142));
        }
        onCraftMatrixChanged(hammer);

    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    public void onCraftMatrixChanged(IInventory par1IInventory) {
        super.onCraftMatrixChanged(par1IInventory);

        if (par1IInventory == this.hammer) {
            this.updateRepairOutput();
        }
    }

    /**
     * called when the Anvil Input Slot changes, calculates the new result and puts it in the output slot
     */
    public void updateRepairOutput() {
        int oldCost = this.hammer.cost;
        ItemStack old = hammer.getStackInSlot(2);
        ItemStack itemstack = hammer.getStackInSlot(0);
        this.hammer.cost = 0;
        int i = 0;
        byte b0 = 0;
        int j = 0;

        if (itemstack == null) {
            hammer.setInventorySlotContents(2, (ItemStack) null);
            //onCraftMatrixChanged(hammer);
            this.hammer.cost = 0;
        } else {
            ItemStack itemstack1 = itemstack.copy();
            ItemStack itemstack2 = hammer.getStackInSlot(1);
            Map map = EnchantmentHelper.getEnchantments(itemstack1);
            boolean flag = false;
            int k2 = b0 + itemstack.getRepairCost() + (itemstack2 == null ? 0 : itemstack2.getRepairCost());
            this.stackSizeToBeUsedInRepair = 0;
            int k;
            int l;
            int i1;
            int k1;
            int l1;
            Iterator iterator1;
            Enchantment enchantment;

            if (itemstack2 != null) {
                // if (!ForgeHooks.onAnvilChange(this, itemstack, itemstack2, outputSlot, repairedItemName, k2)) return;
                flag = itemstack2.getItem() == Items.enchanted_book && Items.enchanted_book.func_92110_g(itemstack2).tagCount() > 0;

                if (itemstack1.isItemStackDamageable() && itemstack1.getItem().getIsRepairable(itemstack, itemstack2)) {
                    k = Math.min(itemstack1.getItemDamageForDisplay(), itemstack1.getMaxDamage() / 4);

                    if (k <= 0) {
                        hammer.setInventorySlotContents(2, (ItemStack) null);
                        //onCraftMatrixChanged(hammer);
                        this.hammer.cost = 0;
                        return;
                    }

                    for (l = 0; k > 0 && l < itemstack2.stackSize; ++l) {
                        i1 = itemstack1.getItemDamageForDisplay() - k;
                        itemstack1.setItemDamage(i1);
                        i += Math.max(1, k / 100) + map.size();
                        k = Math.min(itemstack1.getItemDamageForDisplay(), itemstack1.getMaxDamage() / 4);
                    }

                    this.stackSizeToBeUsedInRepair = l;
                } else {
                    if (!flag && (itemstack1.getItem() != itemstack2.getItem() || !itemstack1.isItemStackDamageable())) {
                        hammer.setInventorySlotContents(2, (ItemStack) null);
                        //onCraftMatrixChanged(hammer);
                        this.hammer.cost = 0;
                        return;
                    }

                    if (itemstack1.isItemStackDamageable() && !flag) {
                        k = itemstack.getMaxDamage() - itemstack.getItemDamageForDisplay();
                        l = itemstack2.getMaxDamage() - itemstack2.getItemDamageForDisplay();
                        i1 = l + itemstack1.getMaxDamage() * 12 / 100;
                        int j1 = k + i1;
                        k1 = itemstack1.getMaxDamage() - j1;

                        if (k1 < 0) {
                            k1 = 0;
                        }

                        if (k1 < itemstack1.getItemDamage()) {
                            itemstack1.setItemDamage(k1);
                            i += Math.max(1, i1 / 100);
                        }
                    }

                    Map map1 = EnchantmentHelper.getEnchantments(itemstack2);
                    iterator1 = map1.keySet().iterator();

                    while (iterator1.hasNext()) {
                        i1 = ((Integer) iterator1.next()).intValue();
                        enchantment = Enchantment.enchantmentsList[i1];
                        k1 = map.containsKey(Integer.valueOf(i1)) ? ((Integer) map.get(Integer.valueOf(i1))).intValue() : 0;
                        l1 = ((Integer) map1.get(Integer.valueOf(i1))).intValue();
                        int i3;

                        if (k1 == l1) {
                            ++l1;
                            i3 = l1;
                        } else {
                            i3 = Math.max(l1, k1);
                        }

                        l1 = i3;
                        int i2 = l1 - k1;
                        boolean flag1 = enchantment.canApply(itemstack);

                        if (this.thePlayer.capabilities.isCreativeMode || itemstack.getItem() == Items.enchanted_book) {
                            flag1 = true;
                        }

                        Iterator iterator = map.keySet().iterator();

                        while (iterator.hasNext()) {
                            int j2 = ((Integer) iterator.next()).intValue();

                            if (j2 != i1 && !enchantment.canApplyTogether(Enchantment.enchantmentsList[j2])) {
                                flag1 = false;
                                i += i2;
                            }
                        }

                        if (flag1) {
                            if (l1 > enchantment.getMaxLevel()) {
                                l1 = enchantment.getMaxLevel();
                            }

                            map.put(Integer.valueOf(i1), Integer.valueOf(l1));
                            int l2 = 0;

                            switch (enchantment.getWeight()) {
                                case 1:
                                    l2 = 8;
                                    break;
                                case 2:
                                    l2 = 4;
                                case 3:
                                case 4:
                                case 6:
                                case 7:
                                case 8:
                                case 9:
                                default:
                                    break;
                                case 5:
                                    l2 = 2;
                                    break;
                                case 10:
                                    l2 = 1;
                            }

                            if (flag) {
                                l2 = Math.max(1, l2 / 2);
                            }

                            i += l2 * i2;
                        }
                    }
                }
            }

            if (StringUtils.isBlank(hammer.itemName)) {
                if (itemstack.hasDisplayName()) {
                    j = itemstack.isItemStackDamageable() ? 7 : itemstack.stackSize * 5;
                    i += j;
                    itemstack1.func_135074_t();
                }
            } else if (!hammer.itemName.equals(itemstack.getDisplayName())) {
                j = itemstack.isItemStackDamageable() ? 7 : itemstack.stackSize * 5;
                i += j;

                if (itemstack.hasDisplayName()) {
                    k2 += j / 2;
                }

                itemstack1.setStackDisplayName(hammer.itemName);
            }

            k = 0;

            for (iterator1 = map.keySet().iterator(); iterator1.hasNext(); k2 += k + k1 * l1) {
                i1 = ((Integer) iterator1.next()).intValue();
                enchantment = Enchantment.enchantmentsList[i1];
                k1 = ((Integer) map.get(Integer.valueOf(i1))).intValue();
                l1 = 0;
                ++k;

                switch (enchantment.getWeight()) {
                    case 1:
                        l1 = 8;
                        break;
                    case 2:
                        l1 = 4;
                    case 3:
                    case 4:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    default:
                        break;
                    case 5:
                        l1 = 2;
                        break;
                    case 10:
                        l1 = 1;
                }

                if (flag) {
                    l1 = Math.max(1, l1 / 2);
                }
            }

            if (flag) {
                k2 = Math.max(1, k2 / 2);
            }

            if (flag && !itemstack1.getItem().isBookEnchantable(itemstack1, itemstack2)) itemstack1 = null;

            this.hammer.cost = k2 + i;

            if (i <= 0) {
                itemstack1 = null;
            }

            if (j == i && j > 0 && this.hammer.cost >= 40) {
                this.hammer.cost = 39;
            }

            if (this.hammer.cost >= 40 && !this.thePlayer.capabilities.isCreativeMode) {
                itemstack1 = null;
            }

            if (itemstack1 != null) {
                l = itemstack1.getRepairCost();

                if (itemstack2 != null && l < itemstack2.getRepairCost()) {
                    l = itemstack2.getRepairCost();
                }

                if (itemstack1.hasDisplayName()) {
                    l -= 9;
                }

                if (l < 0) {
                    l = 0;
                }

                l += 2;
                itemstack1.setRepairCost(l);
                EnchantmentHelper.setEnchantments(map, itemstack1);
            }

            hammer.setInventorySlotContents(2, itemstack1);
            //onCraftMatrixChanged(hammer);
            this.detectAndSendChanges();
        }
        if (!hammer.getStackInSlot(2).areItemStacksEqual(hammer.getStackInSlot(2), old) || !hammer.getStackInSlot(2).areItemStackTagsEqual(hammer.getStackInSlot(2), old)) {
            hammer.progress = 0;
        }
    }

    @Override
    public void addCraftingToCrafters(ICrafting par1ICrafting) {
        super.addCraftingToCrafters(par1ICrafting);
        par1ICrafting.sendProgressBarUpdate(this, 0, this.hammer.cost);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        if (par1 == 0) {
            this.hammer.cost = par2;
        }
    }

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(EntityPlayer par1EntityPlayer) {
        super.onContainerClosed(par1EntityPlayer);

//        if (!this.theWorld.isRemote)
//        {
//            for (int i = 0; i < hammer.getSizeInventory(); ++i)
//            {
//                ItemStack itemstack = hammer.getStackInSlotOnClosing(i);
//
//                if (itemstack != null)
//                {
//                    par1EntityPlayer.dropPlayerItemWithRandomChoice(itemstack, false);
//                }
//            }
//        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
        return this.theWorld.getBlock(this.field_82861_i, this.field_82858_j, this.field_82859_k) != Blocks.anvil ? false : par1EntityPlayer.getDistanceSq((double) this.field_82861_i + 0.5D, (double) this.field_82858_j + 0.5D, (double) this.field_82859_k + 0.5D) <= 64.0D;
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 == 2) {
                if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (par2 != 0 && par2 != 1) {
                if (par2 >= 3 && par2 < 39 && !this.mergeItemStack(itemstack1, 0, 2, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
                return null;
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
        }
        onCraftMatrixChanged(hammer);
        return itemstack;
    }

    /**
     * used by the Anvil GUI to update the Item Name being typed by the player
     */
    public void updateItemName(String par1Str) {
        //  hammer.itemName = par1Str;

        if (this.getSlot(2).getHasStack()) {
            ItemStack itemstack = this.getSlot(2).getStack();

            if (StringUtils.isBlank(par1Str)) {
                itemstack.func_135074_t();
            } else {
                itemstack.setStackDisplayName(hammer.itemName);
            }
        }

        this.updateRepairOutput();
    }
}