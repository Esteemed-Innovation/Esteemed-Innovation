package eiteam.esteemedinnovation.entity.item;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityCanisterItem extends EntityItem {

    public int randomDir = 0;
    public int randomDir2 = 0;

    public EntityCanisterItem(World par1World) {
        super(par1World);
    }

    public EntityCanisterItem(World world, double x, double y, double z, EntityItem item) {
        super(world, x, y, z, item.getEntityItem());
        //this.lifespan = 36000;
        this.motionX = item.motionX;
        this.motionY = item.motionY;
        this.motionZ = item.motionZ;
    }

    @Override
    public boolean isEntityInvulnerable(DamageSource source) {
        return true;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (randomDir == 0) {
            this.randomDir = worldObj.rand.nextInt(360);
        }
        if (randomDir2 == 0) {
            this.randomDir2 = worldObj.rand.nextInt(15) + 10;
        }
    }


}
