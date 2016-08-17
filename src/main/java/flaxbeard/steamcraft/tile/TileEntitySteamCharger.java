package flaxbeard.steamcraft.tile;

import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.api.ISteamChargable;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.tile.SteamTransporterTileEntity;
import flaxbeard.steamcraft.item.armor.exosuit.ItemExosuitArmor;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.ITextComponent;

import static flaxbeard.steamcraft.init.items.tools.GadgetItems.Items.STEAM_CELL_EMPTY;
import static flaxbeard.steamcraft.init.items.tools.GadgetItems.Items.STEAM_CELL_FULL;

public class TileEntitySteamCharger extends SteamTransporterTileEntity implements ISteamTransporter, IInventory {
    public int randomDegrees;
    private boolean isCharging = false;
    private boolean hadItem = false;
    private float prevPercent = 0F;
    private ItemStack inventory = null;

    public TileEntitySteamCharger() {
        super(new EnumFacing[] { EnumFacing.DOWN });
        addSidesToGaugeBlacklist(new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN });
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        randomDegrees = (int) (Math.random() * 360);
        if (nbt.hasKey("inventory")) {
            inventory = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("inventory"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        if (inventory != null) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            inventory.writeToNBT(nbttagcompound1);
            nbt.setTag("inventory", nbttagcompound1);
        }
        return nbt;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound access = super.getUpdateTag();

        if (inventory != null) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            inventory.writeToNBT(nbttagcompound1);
            access.setTag("inventory", nbttagcompound1);
        }
        access.setBoolean("isCharging", isCharging);
        return new SPacketUpdateTileEntity(pos, 1, access);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound access = pkt.getNbtCompound();
        if (access.hasKey("inventory")) {
            inventory = ItemStack.loadItemStackFromNBT(access.getCompoundTag("inventory"));
        } else {
            clear();
        }
        isCharging = access.getBoolean("isCharging");
        markForResync();
    }

    @Override
    public void update() {
        super.update();
        if (worldObj.isRemote) {
            if (inventory != null && isCharging) {
                worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + 0.5F, pos.getY() + 0.5F,
                  pos.getZ() + 0.5F, (Math.random() - 0.5F) / 12F, 0F, (Math.random() - 0.5F) / 12F);
            }
        } else {
            if (inventory != null) {
                if (inventory.getItem() == STEAM_CELL_EMPTY.getItem() && getSteamShare() > Config.steamCellCapacity) {
                    clear();
                    dropItem(new ItemStack(STEAM_CELL_FULL.getItem()));
                    decrSteam(Config.steamCellCapacity);
                    markForResync();
                    return;
                }
                if (!hadItem) {
                    hadItem = true;
                    markForResync();
                }

                if (inventory.getItem() instanceof ISteamChargable) {
                    ISteamChargable item = (ISteamChargable) inventory.getItem();
                    ItemStack stack = inventory.copy();
                    if (!(item instanceof ItemExosuitArmor)) {
                        if (getSteamShare() > 0 && stack.getItemDamage() > 0) {
                            if (!isCharging) {
                                isCharging = true;
                                markForResync();
                            }
                        } else {
                            if (isCharging) {
                                isCharging = false;
                                markForResync();
                            }
                        }
                        if (getSteamShare() > item.steamPerDurability() && stack.getItemDamage() > 0) {
                            int i = 0;
                            while (i < 4
                              && (getSteamShare() > item.steamPerDurability() && stack.getItemDamage() > 0)) {
                                decrSteam(item.steamPerDurability());
                                stack.setItemDamage(stack.getItemDamage() - 1);
                                setInventorySlotContents(0, stack);
                                i++;
                            }
                            float currentPerc = getChargingPercent(stack);
                            if (prevPercent != currentPerc
                              && Math.abs(prevPercent - currentPerc) > 0.01) {
                                // log.debug("New percent: "+currentPerc);
                                prevPercent = currentPerc;
                                markForResync();
                            }
                        }
                    } else {
                        if (!stack.hasTagCompound()) {
                            stack.setTagCompound(new NBTTagCompound());
                        }
                        if (!stack.getTagCompound().hasKey("steamFill")) {
                            stack.getTagCompound().setInteger("steamFill", 0);
                        }
                        if (!stack.getTagCompound().hasKey("maxFill")) {
                            stack.getTagCompound().setInteger("maxFill", 0);
                        }
                        if (getSteamShare() > 0 &&
                          stack.getTagCompound().getInteger("steamFill") < stack.getTagCompound().getInteger("maxFill")) {
                            if (!isCharging) {
                                isCharging = true;
                                markForResync();
                            }
                        } else {
                            if (isCharging) {
                                isCharging = false;
                                markForResync();
                            }
                        }
                        if (getSteamShare() > item.steamPerDurability()
                          && stack.getTagCompound().getInteger("steamFill") < stack.getTagCompound().getInteger("maxFill")) {
                            int i = 0;

                            while (i < 19 && (getSteamShare() > item.steamPerDurability() &&
                              stack.getTagCompound().getInteger("steamFill") < stack.getTagCompound().getInteger("maxFill"))) {
                                decrSteam(item.steamPerDurability());
                                stack.getTagCompound().setInteger("steamFill",
                                  stack.getTagCompound().getInteger("steamFill") + 1);
                                this.setInventorySlotContents(0, stack);
                                i++;
                            }
                            float currentPerc = getChargingPercent(stack);
                            if (prevPercent != currentPerc && Math.abs(prevPercent - currentPerc) > 0.01) {
                                prevPercent = currentPerc;
                                markForResync();
                            }
                        }
                    }
                /*
                } else if (CrossMod.TINKERS_CONSTRUCT && inventory.getItem() instanceof ToolCore) {

                    ItemStack stack = inventory.copy();
                    NBTTagCompound tags = stack.getTagCompound();
                    int damage = tags.getCompoundTag("InfiTool").getInteger("Damage");
                    if (!tags.getCompoundTag("InfiTool").getBoolean("Broken")) {
                        if (getSteamShare() > 0 && tags.getCompoundTag("InfiTool").getInteger("Damage") > 0) {
                            if (!isCharging) {
                                isCharging = true;
                                markForResync();
                            }
                        } else {
                            if (isCharging) {
                                isCharging = false;
                                markForResync();
                            }
                        }
                        if (getSteamShare() > 8 && tags.getCompoundTag("InfiTool").getInteger("Damage") > 0) {
                            int i = 0;
                            while (i < 4 && (getSteamShare() > 8 &&
                              tags.getCompoundTag("InfiTool").getInteger("Damage") > 0)) {
                                decrSteam(8);
                                damage -= 1;
                                tags.getCompoundTag("InfiTool").setInteger("Damage", damage);
                                setInventorySlotContents(0, stack);
                                i++;
                            }
                            int total = tags.getCompoundTag("InfiTool").getInteger("TotalDurability");
                            int dura = tags.getCompoundTag("InfoTool").getInteger("Damage");
                            float currentPerc = (float) (total - dura) / (float) total;
                            
                            if (prevPercent != currentPerc && Math.abs(prevPercent - currentPerc) > 0.01) {
                                prevPercent = currentPerc;
                                markForResync();
                            }
                        }
                    }
                */
                } else {
                    if (hadItem) {
                        hadItem = false;
                        prevPercent = 0F;
                        markForResync();
                    }
                }
            }
        }

    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    public void dropItem(ItemStack item) {
        EntityItem entityItem = new EntityItem(worldObj, pos.getX() + 0.5F, pos.getY() + 1.25F, pos.getZ() + 0.5F, item);
        worldObj.spawnEntityInWorld(entityItem);
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory;
    }

    @Override
    public ItemStack decrStackSize(int slot, int count) {
        if (inventory != null) {
            ItemStack itemstack;

            if (inventory.stackSize <= count) {
                itemstack = inventory;
                clear();
                return itemstack;
            } else {
                itemstack = inventory.splitStack(count);

                if (inventory.stackSize == 0) {
                    clear();
                }

                return itemstack;
            }
        } else {
            return null;
        }
    }

    private float getChargingPercent(ItemStack stack) {
        if (stack == null) {
            return 0F;
        }
        if (stack.getItem() instanceof ItemExosuitArmor) {
            if (!stack.hasTagCompound()) {
                stack.setTagCompound(new NBTTagCompound());
            }
            if (!stack.getTagCompound().hasKey("steamFill")) {
                stack.getTagCompound().setInteger("steamFill", 0);
            }
            if (!stack.getTagCompound().hasKey("maxFill")) {
                stack.getTagCompound().setInteger("maxFill", 0);
            }
            int maxFill = stack.getTagCompound().getInteger("maxFill");
            int steamFill = stack.getTagCompound().getInteger("steamFill");
            return ((float) steamFill / (float) maxFill);
        }
        return 1.0f - ((float) stack.getItemDamage() / (float) stack.getMaxDamage());
    }

    @Override
    public ItemStack removeStackFromSlot(int slot) {
        return inventory;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory = stack;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {}

    @Override
    public void closeInventory(EntityPlayer player) {}

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return stack.getItem() instanceof ISteamChargable || stack.getItem() == STEAM_CELL_EMPTY.getItem();
    }

    @Override
    public int getField(int id) {
        return randomDegrees;
    }

    @Override
    public void setField(int id, int value) {
        randomDegrees = value;
    }

    @Override
    public int getFieldCount() {
        return 1;
    }

    @Override
    public void clear() {
        inventory = null;
    }

    public float getSteamInItem() {
        ItemStack stack = inventory;
        if (stack != null) {
            return getChargingPercent(stack);
        }
        return 0.0f;
    }
}
