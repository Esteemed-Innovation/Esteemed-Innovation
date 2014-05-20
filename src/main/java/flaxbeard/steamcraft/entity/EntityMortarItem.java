package flaxbeard.steamcraft.entity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityMortarItem extends EntityItem {
	public int randomDir = 0;
	public int randomDir2 = 0;
	public int randomSprite = 0;
	private boolean lastOnGround = false;
	public EntityMortarItem(World par1World) {
		super(par1World);
		
	}
	
	public EntityMortarItem(World par1World, double par2, double par4,
			double par6, ItemStack par8ItemStack) {
		super(par1World, par2, par4, par6, par8ItemStack);
		super.delayBeforeCanPickup = 20;
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		if (onGround) {
			this.motionX = 0;
			this.motionZ = 0;
			if (!lastOnGround) {
				this.randomDir = worldObj.rand.nextInt(360);
				this.randomDir2 = worldObj.rand.nextInt(25)+10;
				this.randomSprite = worldObj.rand.nextInt(5);
			}
		}
		lastOnGround = onGround;
	}
}
