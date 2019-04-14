package eiteam.esteemedinnovation.charging;

import eiteam.esteemedinnovation.api.SteamChargable;
import eiteam.esteemedinnovation.api.tile.SteamTransporterTileEntity;
import eiteam.esteemedinnovation.armor.exosuit.steam.ItemSteamExosuitArmor;
import net.minecraft.block.state.IBlockState;
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
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class TileEntitySteamCharger extends SteamTransporterTileEntity {
    public int randomDegrees;
    private boolean isCharging = false;
    private boolean hadItem = false;
    private float prevPercent = 0F;
    protected ItemStackHandler inventory = new ItemStackHandler(1);

    public TileEntitySteamCharger() {
        super(new EnumFacing[] { EnumFacing.DOWN });
        addSidesToGaugeBlacklist(new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN });
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        randomDegrees = (int) (Math.random() * 360);
        if (nbt.hasKey("Inventory")) {
            inventory.deserializeNBT(nbt.getCompoundTag("Inventory"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        if (inventory != null) {
            NBTTagCompound nbttagcompound1 = inventory.serializeNBT();
            nbt.setTag("Inventory", nbttagcompound1);
        }
        return nbt;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound access = super.getUpdateTag();

        if (inventory != null) {
            NBTTagCompound nbttagcompound1 = inventory.serializeNBT();
            access.setTag("Inventory", nbttagcompound1);
        }
        access.setBoolean("IsCharging", isCharging);
        return new SPacketUpdateTileEntity(pos, 1, access);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound access = pkt.getNbtCompound();
        if (access.hasKey("Inventory")) {
            inventory.deserializeNBT(access.getCompoundTag("Inventory"));
        }
        isCharging = access.getBoolean("IsCharging");
        markForResync();
    }

    @Override
    public boolean canUpdate(IBlockState target) {
        return target.getBlock() == ChargingModule.STEAM_FILLER;
    }

    @Override
    public void safeUpdate() {
        if (world.isRemote) {
            if (inventory != null && isCharging) {
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + 0.5F, pos.getY() + 0.5F,
                  pos.getZ() + 0.5F, (Math.random() - 0.5F) / 12F, 0F, (Math.random() - 0.5F) / 12F);
            }
        } else {
            if (inventory != null) {
                // TODO: Abstract into API
                if (inventory.getStackInSlot(0).getItem() == ChargingModule.STEAM_CELL_EMPTY && getSteamShare() > ChargingModule.steamCellCapacity) {
                    inventory.setStackInSlot(0, ItemStack.EMPTY);
                    dropItem(new ItemStack(ChargingModule.STEAM_CELL_FULL));
                    decrSteam(ChargingModule.steamCellCapacity);
                    markForResync();
                    return;
                }
                if (!hadItem) {
                    hadItem = true;
                    markForResync();
                }

                if (inventory.getStackInSlot(0).getItem() instanceof SteamChargable) {
                    SteamChargable item = (SteamChargable) inventory.getStackInSlot(0).getItem();
                    ItemStack stack = inventory.getStackInSlot(0);
                    if (!(item instanceof ItemSteamExosuitArmor)) {
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
                                inventory.setStackInSlot(0, stack);
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
                        if (!stack.getTagCompound().hasKey("SteamStored")) {
                            stack.getTagCompound().setInteger("SteamStored", 0);
                        }
                        if (!stack.getTagCompound().hasKey("SteamCapacity")) {
                            stack.getTagCompound().setInteger("SteamCapacity", 0);
                        }
                        if (getSteamShare() > 0 &&
                          stack.getTagCompound().getInteger("SteamStored") < stack.getTagCompound().getInteger("SteamCapacity")) {
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
                          && stack.getTagCompound().getInteger("SteamStored") < stack.getTagCompound().getInteger("SteamCapacity")) {
                            int i = 0;

                            while (i < 19 && (getSteamShare() > item.steamPerDurability() &&
                              stack.getTagCompound().getInteger("SteamStored") < stack.getTagCompound().getInteger("SteamCapacity"))) {
                                decrSteam(item.steamPerDurability());
                                stack.getTagCompound().setInteger("SteamStored",
                                  stack.getTagCompound().getInteger("SteamStored") + 1);
                                inventory.setStackInSlot(0, stack);
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

        super.safeUpdate();
    }

    public void dropItem(ItemStack item) {
        EntityItem entityItem = new EntityItem(world, pos.getX() + 0.5F, pos.getY() + 1.25F, pos.getZ() + 0.5F, item);
        world.spawnEntity(entityItem);
    }

    private float getChargingPercent(@Nonnull ItemStack stack) {
        if (stack.isEmpty()) {
            return 0F;
        }
        if (stack.getItem() instanceof ItemSteamExosuitArmor) {
            if (!stack.hasTagCompound()) {
                stack.setTagCompound(new NBTTagCompound());
            }
            if (!stack.getTagCompound().hasKey("SteamStored")) {
                stack.getTagCompound().setInteger("SteamStored", 0);
            }
            if (!stack.getTagCompound().hasKey("SteamCapacity")) {
                stack.getTagCompound().setInteger("SteamCapacity", 0);
            }
            int maxFill = stack.getTagCompound().getInteger("SteamCapacity");
            int steamFill = stack.getTagCompound().getInteger("SteamStored");
            return ((float) steamFill / (float) maxFill);
        }
        return 1.0f - ((float) stack.getItemDamage() / (float) stack.getMaxDamage());
    }


    @Override
    public String getName() {
        return null;
    }


    @Override
    public ITextComponent getDisplayName() {
        return null;
    }

    public float getSteamInItem() {
        ItemStack stack = inventory.getStackInSlot(0);
        if (!stack.isEmpty()) {
            return 0.0f;
        }
        return getChargingPercent(stack);
    }
}
