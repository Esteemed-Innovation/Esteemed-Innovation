package eiteam.esteemedinnovation.transport.item;

import eiteam.esteemedinnovation.api.tile.SteamTransporterTileEntity;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.transport.TransportationModule;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class TileEntityItemMortar extends SteamTransporterTileEntity {
    public int xTarget;
    public int zTarget;
    public boolean hasTarget = false;
    public int fireTicks = 0;
    protected ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };

    public TileEntityItemMortar() {
        super(new EnumFacing[] {
          EnumFacing.DOWN,
          EnumFacing.NORTH,
          EnumFacing.SOUTH,
          EnumFacing.EAST,
          EnumFacing.WEST
        });
        addSidesToGaugeBlacklist(EnumFacing.VALUES);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        xTarget = nbt.getShort("xTarget");
        zTarget = nbt.getShort("zTarget");
        fireTicks = nbt.getShort("fireTicks");
        hasTarget = nbt.getBoolean("hasTarget");

        if (nbt.hasKey("inventory")) {
            inventory.deserializeNBT(nbt.getCompoundTag("inventory"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setShort("xTarget", (short) xTarget);
        nbt.setShort("zTarget", (short) zTarget);
        nbt.setShort("fireTicks", (short) fireTicks);

        nbt.setBoolean("hasTarget", hasTarget);
        nbt.setTag("inventory", inventory.serializeNBT());


        return nbt;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound access = super.getUpdateTag();

        access.setInteger("fireTicks", fireTicks);
        access.setInteger("xTarget", xTarget);
        access.setInteger("zTarget", zTarget);

        return new SPacketUpdateTileEntity(pos, 1, access);
    }


    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound access = pkt.getNbtCompound();
        xTarget = access.getInteger("xTarget");
        zTarget = access.getInteger("zTarget");
        fireTicks = access.getInteger("fireTicks");
    }

    @Override
    public boolean canUpdate(IBlockState target) {
        return target.getBlock() == TransportationModule.ITEM_MORTAR;
    }

    @Override
    public void safeUpdate() {
        if (!world.isRemote) {
            BlockPos thisPos = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
            ItemStack stackInSlotZero = inventory.getStackInSlot(0);
            if ((!stackInSlotZero.isEmpty() && world.canBlockSeeSky(thisPos)) || fireTicks >= 60) {
                ItemStack stack = ItemStack.EMPTY;
                if (fireTicks < 60 && !stackInSlotZero.isEmpty()) {
                    stack = stackInSlotZero.copy();
                }
                if (getSteamShare() > 2000 && hasTarget) {
                    if (fireTicks == 0) {
                        markForResync();
                    }
                    fireTicks++;
                    if (fireTicks == 10) {
                        world.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F,
                          EsteemedInnovation.SOUND_HISS, SoundCategory.BLOCKS,
                          Blocks.ANVIL.getSoundType(world.getBlockState(thisPos), world, thisPos, null).getVolume(),
                          0.9F, false);
                    }
                    if (fireTicks == 60 && stack != null) {
                        world.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F,
                          EsteemedInnovation.SOUND_CANNON, SoundCategory.BLOCKS, 2F, 0.8F, false);
                        decrSteam(2000);
                        ItemStack stack2 = stack.copy();
                        stack2.setCount(1);
                        EntityMortarItem entityItem = new EntityMortarItem(world, pos.getX() + 0.5F,
                          pos.getY() + 1.25F, pos.getZ() + 0.5F, stack2, xTarget, zTarget);
                        world.spawnEntity(entityItem);
                        entityItem.motionY = 1.0F;
                        if (stack.getCount() > 1) {
                            stack.shrink(1);
                            inventory.setStackInSlot(0, stack);
                        } else {
                            inventory.setStackInSlot(0, ItemStack.EMPTY);
                        }
                    }
                    if (fireTicks == 80) {
                        fireTicks = 0;
                    }
                } else {
                    fireTicks = 0;
                    markForResync();
                }
            } else {
                fireTicks = 0;
                markForResync();
            }
        } else {
            if (fireTicks > 0) {
                fireTicks++;
                if (fireTicks == 80) {
                    fireTicks = 0;
                }
            }
        }

        super.safeUpdate();
    }


    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
        }
        return super.getCapability(capability, facing);
    }
}
