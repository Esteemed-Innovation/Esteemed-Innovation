package eiteam.esteemedinnovation.charging;

import eiteam.esteemedinnovation.api.tile.SteamTransporterTileEntity;
import eiteam.esteemedinnovation.armor.exosuit.ItemExosuitArmor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntityChargingPad extends SteamTransporterTileEntity {
    public EntityLivingBase target;
    public int extendTicks;
    public boolean descending = false;
    public float rotation = -1;
    public boolean lastDescending = false;

    public TileEntityChargingPad() {
        super(new EnumFacing[] { EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.DOWN });
        addSidesToGaugeBlacklist(new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN });
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        return new AxisAlignedBB(x, y, z, x + 1, y + 2.5F, z + 1);
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound access = super.getUpdateTag();

        access.setBoolean("descending", descending);
        if (target != null) {
            access.setInteger("target", target.getEntityId());
        }
        return new SPacketUpdateTileEntity(pos, 1, access);
    }


    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound access = pkt.getNbtCompound();
        descending = access.getBoolean("descending");
        target = access.hasKey("target") ? (EntityLivingBase) worldObj.getEntityByID(access.getInteger("target")) : null;
        markForResync();
    }

    @Override
    public void update() {
        super.update();
        EnumFacing facing = worldObj.getBlockState(pos).getValue(BlockChargingPad.FACING);

        switch (facing) {
            case NORTH: {
                rotation = 180;
                break;
            }
            case SOUTH: {
                rotation = 0;
                break;
            }
            case WEST: {
                rotation = 270;
                break;
            }
            case EAST: {
                rotation = 90;
                break;
            }
            default: {
                break;
            }
        }

        if (!worldObj.isRemote) {
            EntityLivingBase entity = null;
            List<EntityLivingBase> list = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos.getX() + 0.25F, pos.getY(), pos.getZ() + 0.25F, pos.getX() + 0.75F, pos.getY() + 2, pos.getZ() + 0.75F));
            for (EntityLivingBase ent : list) {
                if (entity == null) {
                    entity = ent;
                }
                if (ent == target) {
                    entity = ent;
                }
            }

            if (entity != null) {
                ItemStack equipment = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                if (equipment != null && equipment.getItem() instanceof ItemExosuitArmor && entity == target &&
                  Math.abs(entity.posX - (pos.getX() + 0.5F)) <= 0.05F && Math.abs(entity.posZ - (pos.getZ() + 0.5F)) <= 0.06F) {
                    ItemExosuitArmor armorItem = (ItemExosuitArmor) equipment.getItem();
                    if (armorItem.getStackInSlot(equipment, 5) != null) {
                        if (extendTicks < 40) {
                            extendTicks++;
                        }
                        // && (extendTicks < 14 || Math.abs(entity.renderYawOffset%360 + this.rotation) <= 60 || Math.abs((360-entity.renderYawOffset)%360 + this.rotation) <= 60

                        if (extendTicks == 40 && equipment.hasTagCompound()) {
                            NBTTagCompound compound = equipment.getTagCompound();
                            if (compound.getInteger("steamFill") < compound.getInteger("maxFill")) {
                                int i = 0;
                                while (i < 39 && (this.getSteamShare() > armorItem.steamPerDurability() &&
                                  compound.getInteger("steamFill") < compound.getInteger("maxFill"))) {
                                    decrSteam(armorItem.steamPerDurability());
                                    compound.setInteger("steamFill", compound.getInteger("steamFill") + 1);
                                    i++;
                                }
                                if (entity instanceof EntityPlayer) {
                                    ((EntityPlayer) entity).inventoryContainer.detectAndSendChanges();
                                }
                            }
                        }

                        descending = false;
                    } else {
                        descending = true;
                    }
                } else if (entity == target) {
                    if (!(Math.abs(entity.posX - (pos.getX() + 0.5F)) <= 0.05F)) {
                        if (entity.posX > pos.getX() + 0.5F) {
                            if (Math.abs(entity.motionX) <= 0.1F) {
                                entity.motionX -= 0.01F;
                            }
                        }
                        if (entity.posX < pos.getX() + 0.5F) {
                            if (Math.abs(entity.motionX) <= 0.1F) {
                                entity.motionX += 0.01F;
                            }
                        }
                    }
                    if (!(Math.abs(entity.posZ - (pos.getZ() + 0.5F)) <= 0.05F)) {
                        if (entity.posZ > pos.getZ() + 0.5F) {
                            if (Math.abs(entity.motionZ) <= 0.1F) {
                                entity.motionZ -= 0.01F;
                            }
                        }
                        if (entity.posZ < pos.getZ() + 0.5F) {
                            if (Math.abs(entity.motionZ) <= 0.1F) {
                                entity.motionZ += 0.01F;
                            }
                        }
                    }

                    descending = true;
                }
            } else {
                descending = true;
            }
            target = entity;


//        if (this.target != null) {
//            if (this.target instanceof EntityPlayer) {
//                float targetRotation = -((EntityPlayer)this.target).renderYawOffset;
//                if (this.rotation < targetRotation) {
//                    ((EntityPlayer)this.target).renderYawOffset += Math.min(targetRotation - this.rotation, 20.2F);
//                }
//                if (this.rotation > targetRotation) {
//                    ((EntityPlayer)this.target).renderYawOffset += Math.max(targetRotation - this.rotation, -20.2F);
//                }
//            }
//        }

//            if (extendTicks <= 0) {
//                descending = false;
//            }
            if (descending && extendTicks >= 0) {
                extendTicks -= 1;
            }
            if (lastDescending != descending) {
                markForResync();
                lastDescending = descending;
            }

            if (entity != null && entity == target && extendTicks >= 15) {
                if (entity.renderYawOffset % 360 != -rotation) {
                    entity.renderYawOffset = -rotation;
                }
            }
        } else {
            if (extendTicks < 40 && !descending) {
                extendTicks++;
            } else if (extendTicks > 0 && descending) {
                extendTicks--;
            }
            EntityLivingBase entity = null;
            List list = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos.getX() + 0.25F, pos.getY(), pos.getZ() + 0.25F, pos.getX() + 0.75F, pos.getY() + 2, pos.getZ() + 0.75F));
            for (Object obj : list) {
                if (entity == null) {
                    entity = (EntityLivingBase) obj;
                }
                if (obj == target) {
                    entity = (EntityLivingBase) obj;
                }
            }
            if (entity != null && !(Math.abs(entity.posX - (pos.getX() + 0.5F)) <= 0.05F && Math.abs(entity.posZ - (pos.getZ() + 0.5F)) <= 0.06F)) {
                if (!(Math.abs(entity.posX - (pos.getX() + 0.5F)) <= 0.05F)) {
                    if (entity.posX > pos.getX() + 0.5F) {
                        if (Math.abs(entity.motionX) <= 0.1F) {
                            entity.motionX -= 0.01F;
                        }
                    }
                    if (entity.posX < pos.getX() + 0.5F) {
                        if (Math.abs(entity.motionX) <= 0.1F) {
                            entity.motionX += 0.01F;
                        }
                    }
                }
                if (!(Math.abs(entity.posZ - (pos.getZ() + 0.5F)) <= 0.05F)) {
                    if (entity.posZ > pos.getZ() + 0.5F) {
                        if (Math.abs(entity.motionZ) <= 0.1F) {
                            entity.motionZ -= 0.01F;
                        }
                    }
                    if (entity.posZ < pos.getZ() + 0.5F) {
                        if (Math.abs(entity.motionZ) <= 0.1F) {
                            entity.motionZ += 0.01F;
                        }
                    }
                }
            }
            if (entity != null && entity == target && extendTicks >= 15) {
                if (entity.renderYawOffset % 360 != -rotation) {
                    entity.renderYawOffset = -rotation;
                }
            }
        }
    }
}