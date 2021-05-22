package eiteam.esteemedinnovation.storage.item.canister;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityCanisterItem extends EntityItem {
    int randomDir;
    int randomDir2;

    public EntityCanisterItem(World par1World) {
        super(par1World);
    }

    public EntityCanisterItem(World world, double x, double y, double z, EntityItem item) {
        super(world, x, y, z, item.getItem());
        motionX = item.motionX;
        motionY = item.motionY;
        motionZ = item.motionZ;
        setPickupDelay(10);
    }

    @Override
    public boolean isEntityInvulnerable(@Nonnull DamageSource source) {
        return true;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (randomDir == 0) {
            randomDir = world.rand.nextInt(360);
        }
        if (randomDir2 == 0) {
            randomDir2 = world.rand.nextInt(15) + 10;
        }
    }
}
