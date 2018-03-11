package eiteam.esteemedinnovation.transport.item;

import eiteam.esteemedinnovation.transport.TransportationModule;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.List;

public class EntityMortarItem extends EntityItem {
    public int randomDir = 0;
    public int randomDir2 = 0;
    public int randomSprite = 0;
    public int xTarget = 0;
    public int zTarget = 0;
    public boolean goingUp = true;
    private double lastPos = 0;
    private boolean lastOnGround = false;

    public EntityMortarItem(World world) {
        super(world);
        setRenderDistanceWeight(getRenderDistanceWeight() * 3);
    }

    public EntityMortarItem(World world, double x, double y, double z, ItemStack stack, int xTarget, int zTarget) {
        super(world, x, y, z, stack);
        setPickupDelay(20);
        this.xTarget = xTarget;
        this.zTarget = zTarget;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("goingUp", goingUp);
        nbt.setBoolean("lastOnGround", lastOnGround);
        nbt.setDouble("lastPos", lastPos);
        nbt.setInteger("xTarget", xTarget);
        nbt.setInteger("zTarget", zTarget);

        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        goingUp = nbt.getBoolean("goingUp");
        lastOnGround = nbt.getBoolean("lastOnGround");
        lastPos = nbt.getDouble("lastPos");
        xTarget = nbt.getInteger("xTarget");
        zTarget = nbt.getInteger("zTarget");
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (onGround) {
            motionX = 0;
            motionZ = 0;
            if (!lastOnGround) {
                randomDir = world.rand.nextInt(360);
                randomDir2 = world.rand.nextInt(25) + 10;
                randomSprite = world.rand.nextInt(5);
            }
        }
        if (goingUp) {
            if (posY <= lastPos) {
                motionY = 0.0F;
                goingUp = false;
            }
            if (posY > 256) {
                motionY = 0.0F;
                int r = TransportationModule.mortarRadius;
                setPosition(xTarget + (world.rand.nextInt((2 * r) + 1) - r), 256,
                  zTarget + (world.rand.nextInt((2 * r) + 1) - r));
                goingUp = false;
            } else {
                motionY = 2.0F;
            }
        }
        lastPos = posY;
        lastOnGround = onGround;
        List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(this,
          getEntityBoundingBox().grow(motionX, motionY, motionZ));
        for (Entity entity : list) {
            if (entity.canBeCollidedWith() && motionY < -1.0F) {
                entity.attackEntityFrom(DamageSource.FALLING_BLOCK, 3.0F);
            }
        }
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer player) {
        super.onCollideWithPlayer(player);
    }
}
