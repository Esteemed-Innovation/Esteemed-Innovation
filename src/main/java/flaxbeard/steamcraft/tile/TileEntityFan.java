package flaxbeard.steamcraft.tile;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import flaxbeard.steamcraft.Steamcraft;

public class TileEntityFan extends TileEntity {
	@Override
	public void updateEntity() {

		int meta = this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		ForgeDirection dir = ForgeDirection.getOrientation(meta);
		this.worldObj.spawnParticle("smoke", xCoord+(dir.offsetX == 0 ? Math.random() : 0.5F), yCoord+(dir.offsetY == 0 ? Math.random() : 0.5F), zCoord+(dir.offsetZ == 0 ? Math.random() : 0.5F), dir.offsetX*0.2F, dir.offsetY*0.2F, dir.offsetZ*0.2F);
		int blocksInFront = 0;
		boolean blocked = false;
		for (int i = 1; i<9; i++) {
			if (!this.worldObj.isRemote && this.worldObj.rand.nextInt(20) == 0 && !blocked && this.worldObj.getBlock(xCoord+dir.offsetX*i, yCoord+dir.offsetY*i, zCoord+dir.offsetZ*i) != Blocks.air && this.worldObj.getBlock(xCoord+dir.offsetX*i, yCoord+dir.offsetY*i, zCoord+dir.offsetZ*i).isReplaceable(worldObj, xCoord+dir.offsetX*i, yCoord+dir.offsetY*i, zCoord+dir.offsetZ*i)) {
				int tMeta = this.worldObj.getBlockMetadata(xCoord+dir.offsetX*i, yCoord+dir.offsetY*i, zCoord+dir.offsetZ*i);				
				this.worldObj.getBlock(xCoord+dir.offsetX*i, yCoord+dir.offsetY*i, zCoord+dir.offsetZ*i).dropBlockAsItem(worldObj, xCoord+dir.offsetX*i, yCoord+dir.offsetY*i, zCoord+dir.offsetZ*i, tMeta, 0);
				for (int v = 0; v<5; v++) {
					Steamcraft.instance.proxy.spawnBreakParticles(worldObj, xCoord+dir.offsetX*i+0.5F, yCoord+dir.offsetY*i+0.5F, zCoord+dir.offsetZ*i+0.5F, this.worldObj.getBlock(xCoord+dir.offsetX*i, yCoord+dir.offsetY*i, zCoord+dir.offsetZ*i), 0.0F, 0.0F, 0.0F);
				}
				this.worldObj.spawnParticle("smoke", xCoord+dir.offsetX*i+(dir.offsetX == 0 ? Math.random() : 0.5F), yCoord+dir.offsetY*i+(dir.offsetY == 0 ? Math.random() : 0.5F), zCoord+dir.offsetZ*i+(dir.offsetZ == 0 ? Math.random() : 0.5F), dir.offsetX*0.25F, dir.offsetY*0.25F, dir.offsetZ*0.25F);
				this.worldObj.setBlockToAir(xCoord+dir.offsetX*i, yCoord+dir.offsetY*i, zCoord+dir.offsetZ*i);
			}
			if (!blocked && (this.worldObj.getBlock(xCoord+dir.offsetX*i, yCoord+dir.offsetY*i, zCoord+dir.offsetZ*i).isReplaceable(worldObj, xCoord+dir.offsetX*i, yCoord+dir.offsetY*i, zCoord+dir.offsetZ*i) || this.worldObj.isAirBlock(xCoord+dir.offsetX*i, yCoord+dir.offsetY*i, zCoord+dir.offsetZ*i))) {
				blocksInFront = i;
				if (i != 8)
					this.worldObj.spawnParticle("smoke", xCoord+dir.offsetX*i+(dir.offsetX == 0 ? Math.random() : 0.5F), yCoord+dir.offsetY*i+(dir.offsetY == 0 ? Math.random() : 0.5F), zCoord+dir.offsetZ*i+(dir.offsetZ == 0 ? Math.random() : 0.5F), dir.offsetX*0.2F, dir.offsetY*0.2F, dir.offsetZ*0.2F);
			}
			else
			{
				blocked = true;
			}
		}
		List entities = worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(xCoord+(dir.offsetX < 0 ? dir.offsetX * blocksInFront : 0), yCoord+(dir.offsetY < 0 ? dir.offsetY * blocksInFront : 0), zCoord+(dir.offsetZ < 0 ? dir.offsetZ * blocksInFront : 0), xCoord+1+(dir.offsetX > 0 ? dir.offsetX * blocksInFront : 0), yCoord+1+(dir.offsetY > 0 ? dir.offsetY * blocksInFront : 0), zCoord+1+(dir.offsetZ > 0 ? dir.offsetZ * blocksInFront : 0)));
		for (Object obj : entities) {
			Entity entity = (Entity) obj;
			if (!(entity instanceof EntityPlayer) || !(((EntityPlayer)entity).capabilities.isFlying && ((EntityPlayer)entity).capabilities.isCreativeMode)) {
				if (entity instanceof EntityPlayer && ((EntityPlayer)entity).isSneaking()) {
					entity.motionX += dir.offsetX * 0.025F;
					entity.motionY += dir.offsetY * 0.05F;
					entity.motionZ += dir.offsetZ * 0.025F;
				}
				else
				{
					entity.motionX += dir.offsetX * 0.075F;
					entity.motionY += dir.offsetY * 0.1F;
					entity.motionZ += dir.offsetZ * 0.075F;
				}
				entity.fallDistance = 0.0F;
			}
		}
	}
}
