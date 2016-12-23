package eiteam.esteemedinnovation.hammer;

import net.minecraft.block.BlockAnvil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.Map;

public class ContainerSteamAnvil extends Container {
    private final EntityPlayer thePlayer;
    public TileEntitySteamHammer hammer;
    private World theWorld;
    private int xPos;
    private int yPos;
    private int zPos;
    private BlockPos pos;
    /**
     * determined by damage of input item and stackSize of repair materials
     */
    private int stackSizeToBeUsedInRepair;

    public ContainerSteamAnvil(InventoryPlayer playerInv, TileEntitySteamHammer tileEntity, final World world, final int x, final int y, final int z, EntityPlayer player) {
        //super(playerInv, world, par3, y, z, player);
        theWorld = world;
        xPos = x;
        yPos = y;
        zPos = z;
        pos = new BlockPos(x, y, z);
        thePlayer = player;
        hammer = tileEntity;
        addSlotToContainer(new Slot(hammer, 0, 27, 47) {
            @Override
            public void onSlotChanged() {
                ContainerSteamAnvil.this.onCraftMatrixChanged(hammer);
                super.onSlotChanged();
            }
        });
        addSlotToContainer(new Slot(hammer, 1, 76, 47) {
            @Override
            public void onSlotChanged() {
                ContainerSteamAnvil.this.onCraftMatrixChanged(hammer);
                super.onSlotChanged();
            }
        });
        addSlotToContainer(new Slot(hammer, 2, 134, 47) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }

            @Override
            public boolean canTakeStack(EntityPlayer player) {
                return hammer.cost > 0 && hammer.cost == hammer.progress && getHasStack();
            }

            @Override
            public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
                hammer.progress = 0;
                hammer.setInventorySlotContents(0, null);
                onCraftMatrixChanged(hammer);

                if (ContainerSteamAnvil.this.stackSizeToBeUsedInRepair > 0) {
                    ItemStack itemstack1 = hammer.getStackInSlot(1);

                    if (itemstack1 != null && itemstack1.stackSize > ContainerSteamAnvil.this.stackSizeToBeUsedInRepair) {
                        itemstack1.stackSize -= ContainerSteamAnvil.this.stackSizeToBeUsedInRepair;
                        hammer.setInventorySlotContents(1, itemstack1);
                        onCraftMatrixChanged(hammer);
                    } else {
                        hammer.setInventorySlotContents(1, null);
                        onCraftMatrixChanged(hammer);
                    }
                } else {
                    hammer.setInventorySlotContents(1, null);
                    onCraftMatrixChanged(hammer);
                }

                ContainerSteamAnvil.this.hammer.cost = 0;

                IBlockState state = world.getBlockState(pos);

                if (!player.capabilities.isCreativeMode && !world.isRemote &&
                  world.getBlockState(pos).getBlock() == Blocks.ANVIL && player.getRNG().nextFloat() < 0.12F) {
                    int l = state.getValue(BlockAnvil.DAMAGE);
                    ++l;

                    if (l > 2) {
                        world.setBlockToAir(pos);
                        world.playBroadcastSound(1029, pos, 0);
                    } else {
                        world.setBlockState(pos, state.withProperty(BlockAnvil.DAMAGE, l), 2);
                        world.playBroadcastSound(1030, pos, 0);
                    }
                } else if (!world.isRemote) {
                    world.playBroadcastSound(1030, pos, 0);
                }
            }
        });
        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 142));
        }
        onCraftMatrixChanged(hammer);
    }

    @Override
    public void onCraftMatrixChanged(IInventory inv) {
        super.onCraftMatrixChanged(inv);

        if (inv == hammer) {
            updateRepairOutput();
        }
    }

    public void updateRepairOutput() {
        ItemStack old = hammer.getStackInSlot(2);
        ItemStack itemstack = hammer.getStackInSlot(0);
        this.hammer.cost = 0;
        int i = 0;
        byte b0 = 0;
        int j = 0;

        if (itemstack == null) {
            hammer.setInventorySlotContents(2, null);
            hammer.cost = 0;
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
            int k1 = 0;
            int l1 = 0;
            Iterator iterator1;
            Enchantment enchantment;

            if (itemstack2 != null) {
                flag = itemstack2.getItem() == Items.ENCHANTED_BOOK && Items.ENCHANTED_BOOK.getEnchantments(itemstack2).tagCount() > 0;

                if (itemstack1.isItemStackDamageable() && itemstack1.getItem().getIsRepairable(itemstack, itemstack2)) {
                    k = Math.min(itemstack1.getItemDamage(), itemstack1.getMaxDamage() / 4);

                    if (k <= 0) {
                        hammer.setInventorySlotContents(2, null);
                        hammer.cost = 0;
                        return;
                    }

                    for (l = 0; k > 0 && l < itemstack2.stackSize; ++l) {
                        i1 = itemstack1.getItemDamage() - k;
                        itemstack1.setItemDamage(i1);
                        i += Math.max(1, k / 100) + map.size();
                        k = Math.min(itemstack1.getItemDamage(), itemstack1.getMaxDamage() / 4);
                    }

                    this.stackSizeToBeUsedInRepair = l;
                } else {
                    if (!flag && (itemstack1.getItem() != itemstack2.getItem() || !itemstack1.isItemStackDamageable())) {
                        hammer.setInventorySlotContents(2, null);
                        hammer.cost = 0;
                        return;
                    }

                    if (itemstack1.isItemStackDamageable() && !flag) {
                        k = itemstack.getMaxDamage() - itemstack.getItemDamage();
                        l = itemstack2.getMaxDamage() - itemstack2.getItemDamage();
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
                        i1 = (Integer) iterator1.next();
                        enchantment = Enchantment.getEnchantmentByID(i1);
                        if (enchantment == null) {
                            continue;
                        }
                        k1 = map.containsKey(i1) ? ((Integer) map.get(i1)) : 0;
                        l1 = ((Integer) map1.get(i1));
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

                        if (this.thePlayer.capabilities.isCreativeMode || itemstack.getItem() == Items.ENCHANTED_BOOK) {
                            flag1 = true;
                        }

                        for (Object o : map.keySet()) {
                            int j2 = ((Integer) o);

                            if (j2 != i1 && !enchantment.canApplyTogether(Enchantment.getEnchantmentByID(j2))) {
                                flag1 = false;
                                i += i2;
                            }
                        }

                        if (flag1) {
                            if (l1 > enchantment.getMaxLevel()) {
                                l1 = enchantment.getMaxLevel();
                            }

                            map.put(i1, l1);
                            int l2 = 0;

                            switch (enchantment.getRarity()) {
                                case COMMON: {
                                    l2 = 1;
                                    break;
                                }
                                case UNCOMMON: {
                                    l2 = 2;
                                    break;
                                }
                                case RARE: {
                                    l2 = 4;
                                    break;
                                }
                                case VERY_RARE: {
                                    l2 = 8;
                                    break;
                                }
                                default: {}
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
                    itemstack1.clearCustomName();
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
                i1 = ((Integer) iterator1.next());
                enchantment = Enchantment.getEnchantmentByID(i1);
                if (enchantment == null) {
                    continue;
                }
                k1 = ((Integer) map.get(i1));
                l1 = 0;
                ++k;

                switch (enchantment.getRarity()) {
                    case COMMON: {
                        l1 = 1;
                        break;
                    }
                    case UNCOMMON: {
                        l1 = 2;
                        break;
                    }
                    case RARE: {
                        l1 = 4;
                        break;
                    }
                    case VERY_RARE: {
                        l1 = 8;
                        break;
                    }
                    default: {
                        break;
                    }
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
        ItemStack stackInSlotTwo = hammer.getStackInSlot(2);
        if (!ItemStack.areItemStacksEqual(stackInSlotTwo, old) || !ItemStack.areItemStackTagsEqual(stackInSlotTwo, old)) {
            hammer.progress = 0;
        }
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        listener.sendProgressBarUpdate(this, 0, hammer.cost);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        if (par1 == 0) {
            hammer.cost = par2;
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return theWorld.getBlockState(pos).getBlock() == Blocks.ANVIL &&
          player.getDistanceSq(xPos + 0.5D, yPos + 0.5D, zPos + 0.5D) <= 64D;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        ItemStack itemstack = null;
        Slot slot = inventorySlots.get(par2);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            assert itemstack1 != null;
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
                slot.putStack(null);
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
    public void updateItemName(String s) {
        hammer.itemName = s;
        Slot slot = getSlot(2);
        if (slot.getHasStack()) {
            ItemStack itemstack = slot.getStack();
            assert itemstack != null;

            if (StringUtils.isBlank(s)) {
                itemstack.clearCustomName();
            } else {
                itemstack.setStackDisplayName(hammer.itemName);
            }
        }

        this.updateRepairOutput();
    }
}