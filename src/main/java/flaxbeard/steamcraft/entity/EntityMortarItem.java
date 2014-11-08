package flaxbeard.steamcraft.entity;

import flaxbeard.steamcraft.Config;
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
    public int xT = 0;
    public int zT = 0;
    public boolean goingUp = true;
    private double lastPos = 0;
    private boolean lastOnGround = false;

    public EntityMortarItem(World par1World) {
        super(par1World);
        this.renderDistanceWeight *= 3;
    }

    public EntityMortarItem(World par1World, double par2, double par4, double par6, ItemStack par8ItemStack, int xTarget, int zTarget) {
        super(par1World, par2, par4, par6, par8ItemStack);
        super.delayBeforeCanPickup = 20;
        this.xT = xTarget;
        this.zT = zTarget;
    }

    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setBoolean("goingUp", this.goingUp);
        par1NBTTagCompound.setBoolean("lastOnGround", this.lastOnGround);
        par1NBTTagCompound.setDouble("lastPos", this.lastPos);
        par1NBTTagCompound.setInteger("xT", this.xT);
        par1NBTTagCompound.setInteger("zT", this.zT);

        super.writeEntityToNBT(par1NBTTagCompound);
    }

    /**
     * Reads the entity from NBT (calls an abstract helper method to read specialized data)
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readEntityFromNBT(par1NBTTagCompound);
        this.goingUp = par1NBTTagCompound.getBoolean("goingUp");
        this.lastOnGround = par1NBTTagCompound.getBoolean("lastOnGround");
        this.lastPos = par1NBTTagCompound.getDouble("lastPos");
        this.xT = par1NBTTagCompound.getInteger("xT");
        this.zT = par1NBTTagCompound.getInteger("zT");
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (onGround) {
            this.motionX = 0;
            this.motionZ = 0;
            if (!lastOnGround) {
                this.randomDir = worldObj.rand.nextInt(360);
                this.randomDir2 = worldObj.rand.nextInt(25) + 10;
                this.randomSprite = worldObj.rand.nextInt(5);
            }
        }
        if (goingUp) {
            if (!(this.posY > this.lastPos)) {
                this.motionY = 0.0F;
                this.goingUp = false;
            }
            if (this.posY > 256) {
                this.motionY = 0.0F;
                int r = Config.mortarRadius;
                this.setPosition(xT + (this.worldObj.rand.nextInt((2 * r) + 1) - r), 256, zT + (this.worldObj.rand.nextInt((2 * r) + 1) - r));
                this.goingUp = false;
            } else {
                this.motionY = 2.0F;
            }
        }
        lastPos = this.posY;
        lastOnGround = onGround;
        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ));
        for (Object obj : list) {
            Entity entity = (Entity) obj;
            if (entity.canBeCollidedWith() && this.motionY < -1.0F) {
                entity.attackEntityFrom(DamageSource.fallingBlock, 3.0F);
            }
        }
    }

    public void onCollideWithPlayer(EntityPlayer par1EntityPlayer) {
//		if (this.motionY < -1.0F) {
//			par1EntityPlayer.attackEntityFrom(DamageSource.fallingBlock, 2.0F);
//		}
        super.onCollideWithPlayer(par1EntityPlayer);
    }
}
