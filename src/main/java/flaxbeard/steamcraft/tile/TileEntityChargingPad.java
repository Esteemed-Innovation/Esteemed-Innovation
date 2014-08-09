package flaxbeard.steamcraft.tile;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import flaxbeard.steamcraft.Steamcraft;

public class TileEntityChargingPad extends TileEntity {
	public EntityLivingBase target;
	public int extendTicks;
	public boolean descending = false;
	public float rotation = -1;
	
	@Override
	public void updateEntity() {
		//if (rotation == -1) {
			int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		//	Steamcraft.log.info(Integer.toString(meta));

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
		//}
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
		if (entity != null && entity == target && Math.abs(entity.posX - (this.xCoord+0.5F)) <= 0.05F && Math.abs(entity.posZ - (this.zCoord+0.5F)) <= 0.06F) {
			if (extendTicks < 40 && (extendTicks < 14 || Math.abs(entity.renderYawOffset%360 + this.rotation) <= 60 || Math.abs((360-entity.renderYawOffset)%360 + this.rotation) <= 60)) {
				extendTicks++;
			}

			//System.out.println(entity.renderYawOffset + " " + this.rotation);
			descending = false;
			
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
		
		if (entity != null && entity == target && this.extendTicks >= 15) {
			if (entity.renderYawOffset%360 != -this.rotation) {
				entity.renderYawOffset = -this.rotation;
			}
		}
		
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
		
		if (extendTicks <= 0) {
			descending = false;
		}
		if (descending) {
			extendTicks-= 1;
		}
		
	}
}