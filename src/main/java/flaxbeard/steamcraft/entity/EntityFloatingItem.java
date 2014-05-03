package flaxbeard.steamcraft.entity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityFloatingItem extends EntityItem {


	
	public EntityFloatingItem(World par1World) {
		super(par1World);
		
	}
	
	public EntityFloatingItem(World par1World, double par2, double par4,
			double par6, ItemStack par8ItemStack) {
		super(par1World, par2, par4, par6, par8ItemStack);
	}

	@Override
    public void onUpdate()
	{
		super.onUpdate();
		if (this.worldObj.getBlock((int) this.posX, (int) this.posY, (int) this.posZ) == Blocks.water) {
			this.motionY = 0.1F;
		}
	}

}
