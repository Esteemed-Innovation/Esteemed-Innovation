package flaxbeard.steamcraft.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class EntityMusketBall extends Entity implements IProjectile {
    protected static Random itemRand = new Random();
    public boolean isWub;
    public boolean pullMob = true;
    /**
     * Seems to be some sort of timer for animating an arrow.
     */
    public int arrowShake = 0;
    /**
     * The owner of this arrow.
     */
    public Entity shootingEntity;
    public boolean silenced = false;
    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;

    /** 1 if the player can pick up the arrow */
    private Block inTile = null;
    private int inData = 0;
    private boolean inGround = false;
    private int ticksInGround;
    private int ticksInAir = 0;
    private double damage = 1.0D;
    /**
     * The amount of knockback an arrow applies when it hits a mob.
     */
    private int knockbackStrength = 1;

    public EntityMusketBall(World par1World) {
        super(par1World);
        this.renderDistanceWeight = 10.0D;
        this.setSize(0.5F, 0.5F);
    }

    public EntityMusketBall(World par1World, double par2, double par4, double par6) {
        super(par1World);
        this.renderDistanceWeight = 10.0D;
        this.setSize(0.5F, 0.5F);
        this.setPosition(par2, par4, par6);
        this.yOffset = 0.0F;
    }

    public EntityMusketBall(World par1World, EntityPlayer par3EntityPlayer, float par3, float par4, float par5, boolean par6) {

        super(par1World);
        this.damage = par5;
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = par3EntityPlayer;

        this.setSize(0.5F, 0.5F);
        this.setLocationAndAngles(par3EntityPlayer.posX, par3EntityPlayer.posY + (double) par3EntityPlayer.getEyeHeight(), par3EntityPlayer.posZ, par3EntityPlayer.rotationYaw + ((itemRand.nextFloat() - 0.5F) * par4 * 15F), par3EntityPlayer.rotationPitch + ((itemRand.nextFloat() - 0.5F) * par4 * 15F));
        this.posX -= (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
        this.posY -= 0.10000000149011612D;
        this.posZ -= (double) (MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.yOffset = 0.0F;
        this.motionX = (double) (-MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
        this.motionZ = (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
        this.motionY = (double) (-MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI));
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, par3 * 10F, 1.0F);
        this.isWub = par6;
    }

    protected void entityInit() {
        this.dataWatcher.addObject(16, Byte.valueOf((byte) 0));
    }

    /**
     * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
     */
    public void setThrowableHeading(double par1, double par3, double par5, float par7, float par8) {
        float var9 = MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5 * par5);
        par1 /= (double) var9;
        par3 /= (double) var9;
        par5 /= (double) var9;
        par1 += this.rand.nextGaussian() * 0.007499999832361937D * (double) par8;
        par3 += this.rand.nextGaussian() * 0.007499999832361937D * (double) par8;
        par5 += this.rand.nextGaussian() * 0.007499999832361937D * (double) par8;
        par1 *= (double) par7;
        par3 *= (double) par7;
        par5 *= (double) par7;
        this.motionX = par1;
        this.motionY = par3;
        this.motionZ = par5;
        float var10 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
        this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(par1, par5) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(par3, (double) var10) * 180.0D / Math.PI);
        this.ticksInGround = 0;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Sets the position and rotation. Only difference from the other one is no bounding on the rotation. Args: posX,
     * posY, posZ, yaw, pitch
     */
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
        this.setPosition(par1, par3, par5);
        this.setRotation(par7, par8);
    }

    @SideOnly(Side.CLIENT)

    /**
     * Sets the velocity to the args. Args: x, y, z
     */
    public void setVelocity(double par1, double par3, double par5) {
        this.motionX = par1;
        this.motionY = par3;
        this.motionZ = par5;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float var7 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(par1, par5) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(par3, (double) var7) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        super.onUpdate();

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float var1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(this.motionY, (double) var1) * 180.0D / Math.PI);
        }

        Block var16 = this.worldObj.getBlock(this.xTile, this.yTile, this.zTile);

        if (var16 != Blocks.air && var16 != null) {
            var16.setBlockBoundsBasedOnState(this.worldObj, this.xTile, this.yTile, this.zTile);
            AxisAlignedBB var2 = var16.getCollisionBoundingBoxFromPool(this.worldObj, this.xTile, this.yTile, this.zTile);

            if (var2 != null && var2.isVecInside(Vec3.createVectorHelper(this.posX, this.posY, this.posZ))) {
                //if (!this.inGround) {
                //}
                this.inGround = true;
            }
        }

        if (this.arrowShake > 0) {
            --this.arrowShake;
        }

        if (this.inGround) {
            Block var18 = this.worldObj.getBlock(this.xTile, this.yTile, this.zTile);
            int var19 = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);

            if (var18 == this.inTile && var19 == this.inData) {
                ++this.ticksInGround;

                if (this.ticksInGround == 1) {
//					Steamcraft.instance.proxy.spawnBreakParticles(worldObj, (float)this.posX,(float)this.posY, (float)this.posZ, var16, (float)(Math.random()-0.5F)/12.0F, 0.3F, (float)(Math.random()-0.5F)/12.0F);
//					Steamcraft.instance.proxy.spawnBreakParticles(worldObj, (float)this.posX,(float)this.posY, (float)this.posZ, var16, (float)(Math.random()-0.5F)/12.0F, 0.3F, (float)(Math.random()-0.5F)/12.0F);
//					Steamcraft.instance.proxy.spawnBreakParticles(worldObj, (float)this.posX,(float)this.posY, (float)this.posZ, var16, (float)(Math.random()-0.5F)/12.0F, 0.3F, (float)(Math.random()-0.5F)/12.0F);
//					Steamcraft.instance.proxy.spawnBreakParticles(worldObj, (float)this.posX,(float)this.posY, (float)this.posZ, var16, (float)(Math.random()-0.5F)/12.0F, 0.3F, (float)(Math.random()-0.5F)/12.0F);
//					Steamcraft.instance.proxy.spawnBreakParticles(worldObj, (float)this.posX,(float)this.posY, (float)this.posZ, var16, (float)(Math.random()-0.5F)/12.0F, 0.3F, (float)(Math.random()-0.5F)/12.0F);
//					Steamcraft.instance.proxy.spawnBreakParticles(worldObj, (float)this.posX,(float)this.posY, (float)this.posZ, var16, (float)(Math.random()-0.5F)/12.0F, 0.3F, (float)(Math.random()-0.5F)/12.0F);
                    this.setDead();
                }
            } else {
                this.inGround = false;
                this.motionX *= (double) (this.rand.nextFloat() * 0.2F);
                this.motionY *= (double) (this.rand.nextFloat() * 0.2F);
                this.motionZ *= (double) (this.rand.nextFloat() * 0.2F);
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
        } else {
            ++this.ticksInAir;
            Vec3 var17 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            Vec3 var3 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition var4 = this.worldObj.rayTraceBlocks(var17, var3, false);
            var17 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            var3 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (var4 != null) {
                var3 = Vec3.createVectorHelper(var4.hitVec.xCoord, var4.hitVec.yCoord, var4.hitVec.zCoord);
            }

            Entity var5 = null;
            List var6 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double var7 = 0.0D;
            int var9;
            float var11;

            for (var9 = 0; var9 < var6.size(); ++var9) {
                Entity var10 = (Entity) var6.get(var9);

                if (var10.canBeCollidedWith() && (var10 != this.shootingEntity || this.ticksInAir >= 5)) {
                    var11 = 0.3F;
                    AxisAlignedBB var12 = var10.boundingBox.expand((double) var11, (double) var11, (double) var11);
                    MovingObjectPosition var13 = var12.calculateIntercept(var17, var3);

                    if (var13 != null) {
                        double var14 = var17.distanceTo(var13.hitVec);

                        if (var14 < var7 || var7 == 0.0D) {
                            var5 = var10;
                            var7 = var14;
                        }
                    }
                }
            }

            if (var5 != null) {
                var4 = new MovingObjectPosition(var5);
            }

            float var20;
            float var26;

            if (var4 != null) {
                if (var4.entityHit != null) {
                    var20 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    int var23 = (int) this.damage;


                    DamageSource var21 = null;

                    if (this.shootingEntity == null || this.silenced) {
                        var21 = DamageSource.causeThrownDamage(this, this);
                    } else {
                        var21 = DamageSource.causeThrownDamage(this, this.shootingEntity);
                    }

                    if (this.isBurning() && !(var4.entityHit instanceof EntityEnderman)) {
                        var4.entityHit.setFire(5);
                    }

                    if (var4.entityHit.attackEntityFrom(var21, var23)) {
                        if (var4.entityHit instanceof EntityLiving) {
                            EntityLiving var24 = (EntityLiving) var4.entityHit;

                            if (!this.worldObj.isRemote) {
                                var24.setArrowCountInEntity(var24.getArrowCountInEntity() + 1);
                            }

                            if (this.knockbackStrength > 0) {
                                var26 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);

                                if (var26 > 0.0F) {
//                                	if (!this.pullMob) {
//                                		var4.entityHit.addVelocity(this.motionX * (double)this.knockbackStrength * 0.6000000238418579D / (double)var26, 0.1D, this.motionZ * (double)this.knockbackStrength * 0.6000000238418579D / (double)var26);
//                                	}
//                                	else {
                                    //	var4.entityHit.addVelocity(-this.motionX * 4.0F * (double)this.knockbackStrength * 0.6000000238418579D / (double)var26, -this.motionY * 0.25F * (double)this.knockbackStrength * 0.6000000238418579D / (double)var26, -this.motionZ * 4.0F * (double)this.knockbackStrength * 0.6000000238418579D / (double)var26);
                                    //}//
                                }
                            }

                            if (this.shootingEntity != null) {
                                //EnchantmentThorns.func_151367_b(this.shootingEntity, var24, this.rand);
                            }

                            if (this.shootingEntity != null && var4.entityHit != this.shootingEntity && var4.entityHit instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP) {
                                //((EntityPlayerMP)this.shootingEntity).playerNetServerHandler.sendPacketToPlayer(new Packet70GameEvent(6, 0));
                            }
                        }


                        if (!(var4.entityHit instanceof EntityEnderman)) {
                            this.setDead();
                        }
                    } else {
                        this.motionX *= -0.10000000149011612D;
                        this.motionY *= -0.10000000149011612D;
                        this.motionZ *= -0.10000000149011612D;
                        this.rotationYaw += 180.0F;
                        this.prevRotationYaw += 180.0F;
                        this.ticksInAir = 0;
                    }
                } else {
                    this.xTile = var4.blockX;
                    this.yTile = var4.blockY;
                    this.zTile = var4.blockZ;
                    this.inTile = this.worldObj.getBlock(this.xTile, this.yTile, this.zTile);
                    this.inData = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
                    this.motionX = (double) ((float) (var4.hitVec.xCoord - this.posX));
                    this.motionY = (double) ((float) (var4.hitVec.yCoord - this.posY));
                    this.motionZ = (double) ((float) (var4.hitVec.zCoord - this.posZ));
                    var20 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    this.posX -= this.motionX / (double) var20 * 0.05000000074505806D;
                    this.posY -= this.motionY / (double) var20 * 0.05000000074505806D;
                    this.posZ -= this.motionZ / (double) var20 * 0.05000000074505806D;
                    //this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    this.inGround = true;
                    this.arrowShake = 7;


                    if (this.inTile != null) {
                        this.inTile.onEntityCollidedWithBlock(this.worldObj, this.xTile, this.yTile, this.zTile, this);
                    }
                }
            }


            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            var20 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

            for (this.rotationPitch = (float) (Math.atan2(this.motionY, (double) var20) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
            }

            while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
                this.prevRotationPitch += 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
                this.prevRotationYaw -= 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
                this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
            this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
            float var22 = 0.99F;
            var11 = 0.05F;

            if (this.isInWater()) {
                for (int var25 = 0; var25 < 4; ++var25) {
                    var26 = 0.25F;
                    this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double) var26, this.posY - this.motionY * (double) var26, this.posZ - this.motionZ * (double) var26, this.motionX, this.motionY, this.motionZ);
                }

                var22 = 0.8F;
            }

            this.motionX *= (double) var22;
            this.motionY *= (double) var22;
            this.motionZ *= (double) var22;
            this.motionY -= (double) var11;
            this.setPosition(this.posX, this.posY, this.posZ);
            //this.do();
            //this.f
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
        par1NBTTagCompound.setShort("xTile", (short) this.xTile);
        par1NBTTagCompound.setShort("yTile", (short) this.yTile);
        par1NBTTagCompound.setShort("zTile", (short) this.zTile);
        //  par1NBTTagCompound.setByte("inTile", (byte)this.inTile);
        par1NBTTagCompound.setByte("inData", (byte) this.inData);
        par1NBTTagCompound.setByte("shake", (byte) this.arrowShake);
        par1NBTTagCompound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
        par1NBTTagCompound.setDouble("damage", this.damage);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
        this.xTile = par1NBTTagCompound.getShort("xTile");
        this.yTile = par1NBTTagCompound.getShort("yTile");
        this.zTile = par1NBTTagCompound.getShort("zTile");
        // this.inTile = par1NBTTagCompound.getByte("inTile") & 255;
        this.inData = par1NBTTagCompound.getByte("inData") & 255;
        this.arrowShake = par1NBTTagCompound.getByte("shake") & 255;
        this.inGround = par1NBTTagCompound.getByte("inGround") == 1;

        if (par1NBTTagCompound.hasKey("damage")) {
            this.damage = par1NBTTagCompound.getDouble("damage");
        }

    }

    /**
     * Called by a player entity when they collide with an entity
     */


    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize() {
        return 0.0F;
    }

    public double getDamage() {
        return this.damage;
    }

    public void setDamage(double par1) {
        //this.damage = par1;
    }

    /**
     * Sets the amount of knockback the arrow applies when it hits a mob.
     */
    public void setKnockbackStrength(int par1) {
        this.knockbackStrength = par1;
    }

    /**
     * If returns false, the item will not inflict any damage against entities.
     */
    public boolean canAttackWithItem() {
        return false;
    }

    /**
     * Whether the arrow has a stream of critical hit particles flying behind it.
     */

    /**
     * Whether the arrow has a stream of critical hit particles flying behind it.
     */
}
