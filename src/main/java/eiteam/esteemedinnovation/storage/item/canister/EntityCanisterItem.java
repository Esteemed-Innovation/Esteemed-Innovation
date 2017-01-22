package eiteam.esteemedinnovation.storage.item.canister;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityCanisterItem extends EntityItem {
    int randomDir;
    int randomDir2;

    public EntityCanisterItem(World par1World) {
        super(par1World);
    }

    public EntityCanisterItem(World world, double x, double y, double z, EntityItem item) {
        super(world, x, y, z, item.getEntityItem());
        motionX = item.motionX;
        motionY = item.motionY;
        motionZ = item.motionZ;
        setPickupDelay(10);
    }

    @Override
    public boolean isEntityInvulnerable(DamageSource source) {
        return true;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (randomDir == 0) {
            randomDir = worldObj.rand.nextInt(360);
        }
        if (randomDir2 == 0) {
            randomDir2 = worldObj.rand.nextInt(15) + 10;
        }
    }
}
