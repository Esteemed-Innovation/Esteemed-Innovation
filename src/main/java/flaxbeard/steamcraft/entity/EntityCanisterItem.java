package flaxbeard.steamcraft.entity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityCanisterItem extends EntityItem {
	
	public int randomDir = 0;
	public int randomDir2 = 0;

	public EntityCanisterItem(World par1World) {
		super(par1World);
	}
	
	public EntityCanisterItem(World p_i1710_1_, double p_i1710_2_,
			double p_i1710_4_, double p_i1710_6_, EntityItem item) {
		super(p_i1710_1_, p_i1710_2_, p_i1710_4_, p_i1710_6_, item.getEntityItem());
		//this.lifespan = 36000;
		this.motionX = item.motionX;
		this.motionY = item.motionY;
		this.motionZ = item.motionZ;
	}
	
	@Override
	public boolean isEntityInvulnerable() {
		return true;
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		if (randomDir == 0) {
			this.randomDir = worldObj.rand.nextInt(360);
		}
		if (randomDir2 == 0) {
			this.randomDir2 = worldObj.rand.nextInt(15)+10;
		}
	}
	

}
