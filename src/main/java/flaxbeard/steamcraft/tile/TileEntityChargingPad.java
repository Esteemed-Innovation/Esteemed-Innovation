package flaxbeard.steamcraft.tile;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.tile.SteamTransporterTileEntity;
import flaxbeard.steamcraft.item.ItemExosuitArmor;

public class TileEntityChargingPad extends SteamTransporterTileEntity implements ISteamTransporter {
	
	public EntityLivingBase target;
	public int extendTicks;
	public boolean descending = false;
	public float rotation = -1;
	public boolean lastDescending = false;

	public TileEntityChargingPad(){
		super(new ForgeDirection[]{ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.DOWN});
		this.addSidesToGaugeBlacklist(new ForgeDirection[]{ForgeDirection.UP, ForgeDirection.DOWN});
	}
	@Override
	public Packet getDescriptionPacket()
	{
        NBTTagCompound access = super.getDescriptionTag();

        access.setBoolean("descending", this.descending);
        if (this.target != null) {
        	access.setInteger("target", target.getEntityId());
        }
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, access);
	}
	    

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
    	super.onDataPacket(net, pkt);
    	NBTTagCompound access = pkt.func_148857_g();
    	this.descending = access.getBoolean("descending");
    	if (access.hasKey("target")) {
    		this.target = (EntityLivingBase) this.worldObj.getEntityByID(access.getInteger("target"));
    	}
    	else
    	{
    		this.target = null;
    	}
    	worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

		switch (meta) {
		case 2:
			rotation = 180;
			break;
		case 3:
			rotation = 0;
			break;
		case 4:
			rotation = 270;
			break;
		case 5:
			rotation = 90;
			break;
		}
		
		if (!this.worldObj.isRemote) {
			EntityLivingBase entity = null;
			List list = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(xCoord+0.25F, yCoord, zCoord+0.25F, xCoord+0.75F, yCoord+2, zCoord+0.75F));
			for (Object obj : list) {
				if (entity == null) {
					entity = (EntityLivingBase) obj;
				}
				if (obj == target) {
					entity = (EntityLivingBase) obj;
				}
			}
			if (entity != null && entity.getEquipmentInSlot(3) != null && entity.getEquipmentInSlot(3).getItem() instanceof ItemExosuitArmor && entity == target && Math.abs(entity.posX - (this.xCoord+0.5F)) <= 0.05F && Math.abs(entity.posZ - (this.zCoord+0.5F)) <= 0.06F) {
			
				ItemStack armor = entity.getEquipmentInSlot(3);
				ItemExosuitArmor armorItem = (ItemExosuitArmor) armor.getItem();
				if (armorItem.getStackInSlot(armor, 5) != null) {
	
					
					if (extendTicks < 40) {
						extendTicks++;
					}
					// && (extendTicks < 14 || Math.abs(entity.renderYawOffset%360 + this.rotation) <= 60 || Math.abs((360-entity.renderYawOffset)%360 + this.rotation) <= 60
					
					if (extendTicks == 40) {
						if (armor.getItemDamage() > 0) {
			 				int i = 0;
			 				while (i<19 && (this.getSteamShare() > armorItem.steamPerDurability() && armor.getItemDamage() > 0)) {
			 					this.decrSteam(armorItem.steamPerDurability());
			 					armor.setItemDamage(armor.getItemDamage()-1);
			 					i++;
			 				}
							if (entity instanceof EntityPlayer) {
								((EntityPlayer)entity).inventoryContainer.detectAndSendChanges();
							}
						}
					}
					
		
					//System.out.println(entity.renderYawOffset + " " + this.rotation);
					descending = false;
				}
				else
				{
					descending = true;
				}
				
			}
			else if (entity != null && entity == target) {
				if (!(Math.abs(entity.posX - (this.xCoord+0.5F)) <= 0.05F)) {
					if (entity.posX > this.xCoord+0.5F) {
						if (Math.abs(entity.motionX) <= 0.1F) {
							entity.motionX -= 0.01F;
						}
					}
					if (entity.posX < this.xCoord+0.5F) {
						if (Math.abs(entity.motionX) <= 0.1F) {
							entity.motionX += 0.01F;
						}				
					}	
				}
				if (!(Math.abs(entity.posZ - (this.zCoord+0.5F)) <= 0.05F)) {
					if (entity.posZ > this.zCoord+0.5F) {
						if (Math.abs(entity.motionZ) <= 0.1F) {
							entity.motionZ -= 0.01F;
						}
					}
					if (entity.posZ < this.zCoord+0.5F) {
						if (Math.abs(entity.motionZ) <= 0.1F) {
							entity.motionZ += 0.01F;
						}				
					}	
				}
				
				descending = true;
	
			}
			else
			{
				descending = true;
			}
			target = entity;
		

		
//		if (this.target != null) {
//			if (this.target instanceof EntityPlayer) {
//				float targetRotation = -((EntityPlayer)this.target).renderYawOffset;
//				if (this.rotation < targetRotation) {
//					((EntityPlayer)this.target).renderYawOffset += Math.min(targetRotation - this.rotation, 20.2F);
//				}
//				if (this.rotation > targetRotation) {
//					((EntityPlayer)this.target).renderYawOffset += Math.max(targetRotation - this.rotation, -20.2F);
//				}
//			}
//		}
		
//			if (extendTicks <= 0) {
//				descending = false;
//			}
			if (descending && extendTicks >= 0) {
				extendTicks-= 1;
			}
			if (lastDescending != descending) {
				this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				lastDescending = descending;
				//System.out.println(descending);
			}

			if (entity != null && entity == target && this.extendTicks >= 15) {
				if (entity.renderYawOffset%360 != -this.rotation) {
					entity.renderYawOffset = -this.rotation;
				}
			}
		}
		else
		{

			if (extendTicks < 40 && !descending) {
				extendTicks++;
			}
			else if (extendTicks > 0 && descending) {
				extendTicks--;
			}
			EntityLivingBase entity = null;
			List list = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(xCoord+0.25F, yCoord, zCoord+0.25F, xCoord+0.75F, yCoord+2, zCoord+0.75F));
			for (Object obj : list) {
				if (entity == null) {
					entity = (EntityLivingBase) obj;
				}
				if (obj == target) {
					entity = (EntityLivingBase) obj;
				}
			}
			if (entity != null && !(Math.abs(entity.posX - (this.xCoord+0.5F)) <= 0.05F && Math.abs(entity.posZ - (this.zCoord+0.5F)) <= 0.06F)) {
				if (!(Math.abs(entity.posX - (this.xCoord+0.5F)) <= 0.05F)) {
					if (entity.posX > this.xCoord+0.5F) {
						if (Math.abs(entity.motionX) <= 0.1F) {
							entity.motionX -= 0.01F;
						}
					}
					if (entity.posX < this.xCoord+0.5F) {
						if (Math.abs(entity.motionX) <= 0.1F) {
							entity.motionX += 0.01F;
						}				
					}	
				}
				if (!(Math.abs(entity.posZ - (this.zCoord+0.5F)) <= 0.05F)) {
					if (entity.posZ > this.zCoord+0.5F) {
						if (Math.abs(entity.motionZ) <= 0.1F) {
							entity.motionZ -= 0.01F;
						}
					}
					if (entity.posZ < this.zCoord+0.5F) {
						if (Math.abs(entity.motionZ) <= 0.1F) {
							entity.motionZ += 0.01F;
						}				
					}	
				}
			}
			if (entity != null && entity == target && this.extendTicks >= 15) {
				if (entity.renderYawOffset%360 != -this.rotation) {
					entity.renderYawOffset = -this.rotation;
				}
			}
		}
	}
}