package flaxbeard.steamcraft.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.misc.ExplosionRocket;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.util.*;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class EntityRocket extends Entity {
    private static final String __OBFID = "CL_00001717"; //<- wtf is that? -xbony2
    protected static Random itemRand = new Random();
    public boolean inGround;
    public EntityLivingBase shootingEntity;
    public float displayRotationPitch;
    public float displayRotationYaw;
    public float explosionSize;
    public float inputParam4;
    private int field_145795_e = -1;
    private int field_145793_f = -1;
    private int field_145794_g = -1;
    private Block field_145796_h;
    private int ticksAlive;
    private int ticksInAir;


    public EntityRocket(World world) {
        super(world);
        this.setSize(1.0F, 1.0F);
    }

    public EntityRocket(World world, double p_i1760_2_, double p_i1760_4_, double p_i1760_6_, double p_i1760_8_, double p_i1760_10_, double p_i1760_12_) {
        super(world);
        this.setSize(1.0F, 1.0F);
        this.setLocationAndAngles(p_i1760_2_, p_i1760_4_, p_i1760_6_, this.rotationYaw, this.rotationPitch);
        this.setPosition(p_i1760_2_, p_i1760_4_, p_i1760_6_);
        double d6 = (double) MathHelper.sqrt_double(p_i1760_8_ * p_i1760_8_ + p_i1760_10_ * p_i1760_10_ + p_i1760_12_ * p_i1760_12_);
    }

    public EntityRocket(World world, EntityPlayer player, float par4, float size) {
        super(world);
        this.inputParam4 = par4;
        this.shootingEntity = player;
        this.explosionSize = size;
        this.setSize(0.25F, 0.25F);
        this.setLocationAndAngles(player.posX, player.posY + (double) player.getEyeHeight(), player.posZ, player.rotationYaw + ((itemRand.nextFloat() - 0.5F) * par4 * 15F), player.rotationPitch + ((itemRand.nextFloat() - 0.5F) * par4 * 15F));
        this.setPosition(this.posX, this.posY, this.posZ);
        this.posX -= 1.0D * (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
        this.posZ -= 1.0D * (double) (MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);

        this.yOffset = 0.0F;
        this.motionX = this.motionY = this.motionZ = 0.0D;
        double p_i1760_8_ = player.getLookVec().xCoord;
        double p_i1760_10_ = player.getLookVec().yCoord;
        double p_i1760_12_ = player.getLookVec().zCoord;

        double d6 = (double) MathHelper.sqrt_double(p_i1760_8_ * p_i1760_8_ + p_i1760_10_ * p_i1760_10_ + p_i1760_12_ * p_i1760_12_);
        this.motionX = p_i1760_8_ / d6 * 1.0D;
        this.motionY = p_i1760_10_ / d6 * 1.0D;
        this.motionZ = p_i1760_12_ / d6 * 1.0D;
//        this.posX += this.motionX*1.0D;
//        this.posY += this.motionY*1.0D;
//        this.posZ += this.motionZ*1.0D;
        this.displayRotationYaw = this.rotationYaw;
        float f3 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);

        this.displayRotationPitch = (float) (Math.atan2(motionY, (double) f3) * 180.0D / Math.PI);

    }

    protected void entityInit() {}

    /**
     * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
     * length * 64 * renderDistanceWeight
     * 
     * @param distance
     */
    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double distance) {
        double d1 = this.boundingBox.getAverageEdgeLength() * 4.0D;
        d1 *= 64.0D;
        return distance < d1 * d1;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        if (!this.worldObj.isRemote && (this.shootingEntity != null && this.shootingEntity.isDead || !this.worldObj.blockExists((int) this.posX, (int) this.posY, (int) this.posZ))) {
            this.setDead();
        } else {
            super.onUpdate();
            // this.setFire(1);

            if (this.inGround) {
                if (this.worldObj.getBlock(this.field_145795_e, this.field_145793_f, this.field_145794_g) == this.field_145796_h) {
                    ++this.ticksAlive;

                    if (this.ticksAlive == 200) {
                        this.setDead();
                    }

                    return;
                }

                ++this.ticksAlive;

                if (this.ticksAlive >= 200) {
                    this.setDead();
                }

                //  this.inGround = false;
                this.motionX = 0;
                this.motionY = 0;
                this.motionZ = 0;
                this.ticksAlive = 0;
                this.ticksInAir = 0;
            } else {
                ++this.ticksInAir;
            }

            Vec3 vec3 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            Vec3 vec31 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3, vec31);
            vec3 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            vec31 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (movingobjectposition != null) {
                vec31 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
            }

            Entity entity = null;
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(0.5D, 0.5D, 0.5D));
            double d0 = 0.0D;

            for (int i = 0; i < list.size(); ++i) {
                Entity entity1 = (Entity) list.get(i);

                if (entity1.canBeCollidedWith() && (!entity1.isEntityEqual(this.shootingEntity) || this.ticksInAir >= 25)) {
                    float f = 0.3F;
                    AxisAlignedBB axisalignedbb = entity1.boundingBox.expand((double) f, (double) f, (double) f);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);

                    if (movingobjectposition1 != null) {
                        double d1 = vec3.distanceTo(movingobjectposition1.hitVec);

                        if (d1 < d0 || d0 == 0.0D) {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null) {
                movingobjectposition = new MovingObjectPosition(entity);
            }

            if (movingobjectposition != null) {
                this.onImpact(movingobjectposition);
            }


            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float) (Math.atan2(this.motionZ, this.motionX) * 180.0D / Math.PI) + 90.0F;

            for (this.rotationPitch = (float) (Math.atan2((double) f1, this.motionY) * 180.0D / Math.PI) - 90.0F; this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
                ;
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
            float f2 = this.getMotionFactor();

            if (this.isInWater()) {
                for (int j = 0; j < 4; ++j) {
                    float f3 = 0.25F;
                    this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double) f3, this.posY - this.motionY * (double) f3, this.posZ - this.motionZ * (double) f3, this.motionX, this.motionY, this.motionZ);
                }

                f2 = 0.8F;
            }
//
//            this.motionX += this.accelerationX;
//            this.motionY += this.accelerationY;
//            this.motionZ += this.accelerationZ;
//            this.motionX *= (double)f2;
//            this.motionY *= (double)f2;
//            this.motionZ *= (double)f2;
            this.worldObj.spawnParticle("smoke", this.posX - this.motionX * 3.0F, this.posY - this.motionY * 3.0F, this.posZ - this.motionZ * 3.0F, 0.0D, 0.0D, 0.0D);
            this.worldObj.spawnParticle("smoke", this.posX - this.motionX * 1.5F, this.posY - this.motionY * 1.5F, this.posZ - this.motionZ * 1.5F, 0.0D, 0.0D, 0.0D);
            this.setPosition(this.posX, this.posY, this.posZ);
        }
    }

    /**
     * Return the motion factor for this projectile. The factor is multiplied by the original motion.
     */
    protected float getMotionFactor() {
        return 0.95F;
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    protected void onImpact(MovingObjectPosition position) {
        if (!this.worldObj.isRemote) {
            //newExplosion(world, (Entity)null, this.posX, this.posY, this.posZ, (float)2.0F, true, this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"));

            newExplosion(worldObj, (Entity) this.shootingEntity, this.posX, this.posY, this.posZ, this.explosionSize, true, this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"));
            this.setDead();
        }
    }

    public Explosion newExplosion(World world, Entity entity, double p_72885_2_, double p_72885_4_, double p_72885_6_, float p_72885_8_, boolean p_72885_9_, boolean p_72885_10_) {
        Explosion explosion = new ExplosionRocket(world, entity, p_72885_2_, p_72885_4_, p_72885_6_, p_72885_8_);
        explosion.isFlaming = p_72885_9_;
        explosion.isSmoking = p_72885_10_;
        explosion.doExplosionA();
        explosion.doExplosionB(true);

        if (!p_72885_10_) {
            explosion.affectedBlockPositions.clear();
        }

        Iterator iterator = world.playerEntities.iterator();

        while (iterator.hasNext()) {
            EntityPlayer entityplayer = (EntityPlayer) iterator.next();

            if (entityplayer.getDistanceSq(p_72885_2_, p_72885_4_, p_72885_6_) < 4096.0D) {
                ((EntityPlayerMP) entityplayer).playerNetServerHandler.sendPacket(new S27PacketExplosion(p_72885_2_, p_72885_4_, p_72885_6_, p_72885_8_, explosion.affectedBlockPositions, (Vec3) explosion.func_77277_b().get(entityplayer)));
            }
        }

        return explosion;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbt) {
    	nbt.setShort("xTile", (short) this.field_145795_e);
    	nbt.setShort("yTile", (short) this.field_145793_f);
    	nbt.setShort("zTile", (short) this.field_145794_g);
    	nbt.setByte("inTile", (byte) Block.getIdFromBlock(this.field_145796_h));
    	nbt.setByte("inGround", (byte) (this.inGround ? 1 : 0));
    	nbt.setTag("direction", this.newDoubleNBTList(new double[]{this.motionX, this.motionY, this.motionZ}));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.field_145795_e = nbt.getShort("xTile");
        this.field_145793_f = nbt.getShort("yTile");
        this.field_145794_g = nbt.getShort("zTile");
        this.field_145796_h = Block.getBlockById(nbt.getByte("inTile") & 255);
        this.inGround = nbt.getByte("inGround") == 1;

        if (nbt.hasKey("direction", 9)) {
            NBTTagList nbttaglist = nbt.getTagList("direction", 6);
            this.motionX = nbttaglist.func_150309_d(0);
            this.motionY = nbttaglist.func_150309_d(1);
            this.motionZ = nbttaglist.func_150309_d(2);
        } else {
            this.setDead();
        }
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith() {
        return true;
    }

    public float getCollisionBorderSize() {
        return 1.0F;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float p_70097_2_) {
        if (this.isEntityInvulnerable()) {
            return false;
        } else {
            this.setBeenAttacked();

            if (source.getEntity() != null) {
                Vec3 vec3 = source.getEntity().getLookVec();

                if (vec3 != null) {
                    this.motionX = vec3.xCoord;
                    this.motionY = vec3.yCoord;
                    this.motionZ = vec3.zCoord;
                }

                if (source.getEntity() instanceof EntityLivingBase) {
                    this.shootingEntity = (EntityLivingBase) source.getEntity();
                }

                return true;
            } else {
                return false;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize() {
        return 0.0F;
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness(float par1) {
        return 1.0F;
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender(float par1) {
        return 15728880;
    }
}