package eiteam.esteemedinnovation.entity.projectile;

import eiteam.esteemedinnovation.misc.ExplosionRocket;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class EntityRocket extends Entity {
    protected static Random itemRand = new Random();
    public boolean inGround;
    public EntityLivingBase shootingEntity;
    public float displayRotationPitch;
    public float displayRotationYaw;
    public float explosionSize;
    public float inputParam4;
    private int x = -1;
    private int y = -1;
    private int z = -1;
    private Block blockInside;
    private int ticksAlive;
    private int ticksInAir;

    public EntityRocket(World world) {
        super(world);
        this.setSize(1.0F, 1.0F);
    }

    public EntityRocket(World world, double x, double y, double z, double p_i1760_8_, double p_i1760_10_, double p_i1760_12_) {
        super(world);
        this.setSize(1.0F, 1.0F);
        this.setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
        this.setPosition(x, y, z);
    }

    public EntityRocket(World world, EntityPlayer player, float par4, float size) {
        super(world);
        inputParam4 = par4;
        shootingEntity = player;
        explosionSize = size;
        setSize(0.25F, 0.25F);
        setLocationAndAngles(player.posX, player.posY + (double) player.getEyeHeight(), player.posZ, player.rotationYaw + ((itemRand.nextFloat() - 0.5F) * par4 * 15F), player.rotationPitch + ((itemRand.nextFloat() - 0.5F) * par4 * 15F));
        setPosition(this.posX, this.posY, this.posZ);
        posX -= 1.0D * (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
        posZ -= 1.0D * (double) (MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
        motionX = motionY = motionZ = 0.0D;
        double p_i1760_8_ = player.getLookVec().xCoord;
        double p_i1760_10_ = player.getLookVec().yCoord;
        double p_i1760_12_ = player.getLookVec().zCoord;

        double d6 = (double) MathHelper.sqrt_double(p_i1760_8_ * p_i1760_8_ + p_i1760_10_ * p_i1760_10_ + p_i1760_12_ * p_i1760_12_);
        motionX = p_i1760_8_ / d6 * 1.0D;
        motionY = p_i1760_10_ / d6 * 1.0D;
        motionZ = p_i1760_12_ / d6 * 1.0D;
//        this.posX += this.motionX*1.0D;
//        this.posY += this.motionY*1.0D;
//        this.posZ += this.motionZ*1.0D;
        displayRotationYaw = rotationYaw;
        float f3 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);

        displayRotationPitch = (float) (Math.atan2(motionY, (double) f3) * 180.0D / Math.PI);
    }

    @Override
    public void entityInit() {}

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double distance) {
        double d1 = getEntityBoundingBox().getAverageEdgeLength() * 4D * 64D;
        return distance < d1 * d1;
    }

    @Override
    public void onUpdate() {
        IBlockState state = worldObj.getBlockState(getPosition());
        Block block = worldObj.getBlockState(getPosition()).getBlock();
        if (!worldObj.isRemote && (shootingEntity != null && shootingEntity.isDead ||
          (block.isAir(state, worldObj, getPosition())))) {
            setDead();
        } else {
            super.onUpdate();
            // this.setFire(1);

            if (inGround) {
                // TODO: Test if this BlockPos is any different from the one given by getPosition().
                if (worldObj.getBlockState(new BlockPos(x, y, z)) == blockInside) {
                    ++ticksAlive;

                    if (ticksAlive == 200) {
                        setDead();
                    }

                    return;
                }

                ++ticksAlive;

                if (ticksAlive >= 200) {
                    setDead();
                }

                //  this.inGround = false;
                motionX = 0;
                motionY = 0;
                motionZ = 0;
                ticksAlive = 0;
                ticksInAir = 0;
            } else {
                ++ticksInAir;
            }

            Vec3d vec3 = new Vec3d(posX, posY, posZ);
            Vec3d vec31 = new Vec3d(posX + motionX, posY + motionY, posZ + motionY);
            RayTraceResult trace = this.worldObj.rayTraceBlocks(vec3, vec31, false, true, false);
            vec3 = new Vec3d(posX, posY, posZ);
            vec31 = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);

            if (trace != null) {
                vec31 = trace.hitVec;
            }

            Entity entity = null;
            List<Entity> list = worldObj.getEntitiesWithinAABBExcludingEntity(this,
              getEntityBoundingBox().addCoord(motionX, motionY, motionZ).expand(0.5D, 0.5D, 0.5D));
            double d0 = 0.0D;

            for (Entity entity1 : list) {
                if (entity1.canBeCollidedWith() && (!entity1.isEntityEqual(shootingEntity) || this.ticksInAir >= 25)) {
                    float f = 0.3F;
                    AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double) f, (double) f, (double) f);
                    RayTraceResult trace1 = axisalignedbb.calculateIntercept(vec3, vec31);

                    if (trace1 != null) {
                        double d1 = vec3.distanceTo(trace1.hitVec);

                        if (d1 < d0 || d0 == 0.0D) {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null) {
                trace = new RayTraceResult(entity);
            }

            if (trace != null) {
                this.onImpact();
            }


            posX += motionX;
            posY += motionY;
            posZ += motionZ;
            rotationYaw = (float) (Math.atan2(motionZ, motionX) * 180.0D / Math.PI) + 90.0F;

            while (rotationPitch - prevRotationPitch >= 180.0F) {
                prevRotationPitch += 360.0F;
            }

            while (rotationYaw - prevRotationYaw < -180.0F) {
                prevRotationYaw -= 360.0F;
            }

            while (rotationYaw - prevRotationYaw >= 180.0F) {
                prevRotationYaw += 360.0F;
            }

            rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
            rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;

            if (isInWater()) {
                for (int j = 0; j < 4; ++j) {
                    double f3 = 0.25D;

                    worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, posX - motionX * f3, posY - motionY * f3,
                      posZ - motionZ * f3, motionX, motionY, motionZ);
                }
            }
//
//            this.motionX += this.accelerationX;
//            this.motionY += this.accelerationY;
//            this.motionZ += this.accelerationZ;
//            this.motionX *= (double)f2;
//            this.motionY *= (double)f2;
//            this.motionZ *= (double)f2;
            worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX - motionX * 3F, posY - motionY * 3F,
              posZ - motionZ * 3F, 0D, 0D, 0D);
            worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX - motionX * 1.5F, posY - motionY * 1.5F,
              posZ - motionZ * 1.5F, 0D, 0D, 0D);
            setPosition(posX, posY, posZ);
        }
    }


    protected void onImpact() {
        if (!this.worldObj.isRemote) {
            //newExplosion(world, (Entity)null, this.posX, this.posY, this.posZ, (float)2.0F, true, this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"));

            newExplosion(worldObj, shootingEntity, posX, posY, posZ, explosionSize, true,
              worldObj.getGameRules().getBoolean("mobGriefing"));
            this.setDead();
        }
    }

    public Explosion newExplosion(World world, Entity entity, double x, double y, double z, float explosionSize, boolean doFire, boolean doSmokeAndGrief) {
        ExplosionRocket explosion = new ExplosionRocket(world, entity, x, y, z, explosionSize, doFire, doSmokeAndGrief);
        explosion.isFlaming = doFire;
        explosion.isSmoking = doSmokeAndGrief;
        explosion.doExplosionA();
        explosion.doExplosionB(true);

        for (EntityPlayer entityplayer : world.playerEntities) {
            if (entityplayer.getDistanceSq(x, y, z) < 4096.0D) {
                SPacketExplosion packet = new SPacketExplosion(x, y, z, explosionSize,
                  explosion.affectedBlockPositions, explosion.getPlayerKnockbackMap().get(entityplayer));
                ((EntityPlayerMP) entityplayer).connection.sendPacket(packet);
            }
        }

        return explosion;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
    	nbt.setShort("xTile", (short) x);
    	nbt.setShort("yTile", (short) y);
    	nbt.setShort("zTile", (short) z);
    	nbt.setByte("inTile", (byte) Block.getIdFromBlock(blockInside));
        nbt.setBoolean("inGround", inGround);
    	nbt.setTag("direction", newDoubleNBTList(motionX, motionY, motionZ));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        x = nbt.getShort("xTile");
        y = nbt.getShort("yTile");
        z = nbt.getShort("zTile");
        blockInside = Block.getBlockById(nbt.getByte("inTile") & 255);
        inGround = nbt.getBoolean("inGround");

        if (nbt.hasKey("direction", Constants.NBT.TAG_LIST)) {
            NBTTagList nbttaglist = nbt.getTagList("direction", Constants.NBT.TAG_DOUBLE);
            motionX = nbttaglist.getDoubleAt(0);
            motionY = nbttaglist.getDoubleAt(1);
            motionZ = nbttaglist.getDoubleAt(2);
        } else {
            setDead();
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public float getCollisionBorderSize() {
        return 1.0F;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isEntityInvulnerable(source)) {
            return false;
        } else {
            setBeenAttacked();
            Entity entity = source.getEntity();
            if (entity != null) {
                Vec3d vec3 = entity.getLookVec();

                motionX = vec3.xCoord;
                motionY = vec3.yCoord;
                motionZ = vec3.zCoord;

                if (entity instanceof EntityLivingBase) {
                    shootingEntity = (EntityLivingBase) entity;
                }

                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public float getBrightness(float partialTicks) {
        return 1.0F;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender(float partialTicks) {
        return 15728880;
    }
}