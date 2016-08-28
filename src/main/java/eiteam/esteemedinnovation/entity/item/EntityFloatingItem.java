package eiteam.esteemedinnovation.entity.item;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityFloatingItem extends EntityItem {
    public EntityFloatingItem(World world) {
        super(world);
    }

    public EntityFloatingItem(World par1World, double par2, double par4, double par6, ItemStack par8ItemStack) {
        super(par1World, par2, par4, par6, par8ItemStack);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (worldObj.getBlockState(getPosition()).getBlock() == Blocks.WATER) {
            this.motionY = 0.1F;
        }
    }

}
