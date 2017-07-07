package eiteam.esteemedinnovation.api.entity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class EntityMusketBall extends Entity implements IProjectile {
    protected static Random itemRand = new Random();
    /**
     * Seems to be some sort of timer for animating an arrow.
     */
    public int arrowShake = 0;
    public Entity shootingEntity;
    public boolean silenced = false;
    private BlockPos tilePos = new BlockPos(-1, -1, -1);

    private Block inTile = null;
    private int inData = 0;
    private boolean inGround = false;
    private int ticksInGround;
    private int ticksInAir = 0;
    private double damage = 1.0D;
    private int knockbackStrength = 1;

    public EntityMusketBall(World par1World) {
        super(par1World);
        setRenderDistanceWeight(10.0D);
        setSize(0.5F, 0.5F);
    }

    public EntityMusketBall(World par1World, double par2, double par4, double par6) {
        super(par1World);
        setRenderDistanceWeight(10.0D);
        setSize(0.5F, 0.5F);
        setPosition(par2, par4, par6);
    }

    public EntityMusketBall(World world, EntityPlayer player, float par3, float par4, float damage, boolean par6) {
        super(world);
        this.damage = damage;
        setRenderDistanceWeight(10.0D);
        shootingEntity = player;

        setSize(0.5F, 0.5F);
        setLocationAndAngles(player.posX, player.posY + (double) player.getEyeHeight(), player.posZ, player.rotationYaw + ((itemRand.nextFloat() - 0.5F) * par4 * 15F), player.rotationPitch + ((itemRand.nextFloat() - 0.5F) * par4 * 15F));
        posX -= (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
        posY -= 0.10000000149011612D;
        posZ -= (double) (MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
        setPosition(this.posX, this.posY, this.posZ);
        motionX = (double) (-MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
        motionZ = (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
        motionY = (double) (-MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI));
        setThrowableHeading(this.motionX, this.motionY, this.motionZ, par3 * 10F, 1.0F);
    }

    @Override
    protected void entityInit() {
        dataManager.register(EntityDataManager.createKey(EntityMusketBall.class, DataSerializers.BYTE), (byte) 0);
    }

    @Override
    public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy) {
        float var9 = MathHelper.sqrt(x * x + y * y + z * z);
        x /= (double) var9;
        y /= (double) var9;
        z /= (double) var9;
        x += this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
        y += this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
        z += this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
        x *= (double) velocity;
        y *= (double) velocity;
        z *= (double) velocity;
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        float var10 = MathHelper.sqrt(x * x + z * z);
        //noinspection SuspiciousNameCombination
        this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(x, z) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(y, (double) var10) * 180.0D / Math.PI);
        this.ticksInGround = 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setVelocity(double x, double y, double z) {
        motionX = x;
        motionY = y;
        motionZ = z;

        if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F) {
            float var7 = MathHelper.sqrt(x * x + z * z);
            //noinspection SuspiciousNameCombination
            prevRotationYaw = rotationYaw = (float) (Math.atan2(x, z) * 180.0D / Math.PI);
            prevRotationPitch = rotationPitch = (float) (Math.atan2(y, (double) var7) * 180.0D / Math.PI);
            prevRotationPitch = rotationPitch;
            prevRotationYaw = rotationYaw;
            setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
            ticksInGround = 0;
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F) {
            float var1 = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);
            //noinspection SuspiciousNameCombination
            prevRotationYaw = rotationYaw = (float) (Math.atan2(motionX, motionZ) * 180.0D / Math.PI);
            prevRotationPitch = rotationPitch = (float) (Math.atan2(motionY, (double) var1) * 180.0D / Math.PI);
        }

        IBlockState state = world.getBlockState(tilePos);
        Block block = state.getBlock();

        if (block != null) {
            if (state.getMaterial() != Material.AIR) {
                AxisAlignedBB aabb = block.getCollisionBoundingBox(state, world, tilePos);
                if (aabb != null && aabb.contains(new Vec3d(posX, posY, posZ))) {
                    inGround = true;
                }
            }
        }

        if (arrowShake > 0) {
            --arrowShake;
        }

        if (inGround && block != null) {
            int meta = block.getMetaFromState(state);

            if (block == inTile && meta == inData) {
                ++ticksInGround;

                if (ticksInGround == 1200) {
//					EsteemedInnovation.instance.proxy.spawnBreakParticles(worldObj, (float)this.posX,(float)this.posY, (float)this.posZ, var16, (float)(Math.random()-0.5F)/12.0F, 0.3F, (float)(Math.random()-0.5F)/12.0F);
//					EsteemedInnovation.instance.proxy.spawnBreakParticles(worldObj, (float)this.posX,(float)this.posY, (float)this.posZ, var16, (float)(Math.random()-0.5F)/12.0F, 0.3F, (float)(Math.random()-0.5F)/12.0F);
//					EsteemedInnovation.instance.proxy.spawnBreakParticles(worldObj, (float)this.posX,(float)this.posY, (float)this.posZ, var16, (float)(Math.random()-0.5F)/12.0F, 0.3F, (float)(Math.random()-0.5F)/12.0F);
//					EsteemedInnovation.instance.proxy.spawnBreakParticles(worldObj, (float)this.posX,(float)this.posY, (float)this.posZ, var16, (float)(Math.random()-0.5F)/12.0F, 0.3F, (float)(Math.random()-0.5F)/12.0F);
//					EsteemedInnovation.instance.proxy.spawnBreakParticles(worldObj, (float)this.posX,(float)this.posY, (float)this.posZ, var16, (float)(Math.random()-0.5F)/12.0F, 0.3F, (float)(Math.random()-0.5F)/12.0F);
//					EsteemedInnovation.instance.proxy.spawnBreakParticles(worldObj, (float)this.posX,(float)this.posY, (float)this.posZ, var16, (float)(Math.random()-0.5F)/12.0F, 0.3F, (float)(Math.random()-0.5F)/12.0F);
                    setDead();
                }
            } else {
                inGround = false;
                motionX *= (double) (rand.nextFloat() * 0.2F);
                motionY *= (double) (rand.nextFloat() * 0.2F);
                motionZ *= (double) (rand.nextFloat() * 0.2F);
                ticksInGround = 0;
                ticksInAir = 0;
            }
        } else {
            ++ticksInAir;
            Vec3d posVec = new Vec3d(posX, posY, posZ);
            Vec3d motionVec = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
            RayTraceResult rayTraceResult = world.rayTraceBlocks(posVec, motionVec, false, true, false);
            posVec = new Vec3d(posX, posY, posZ);
            motionVec = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);

            if (rayTraceResult != null) {
                motionVec = rayTraceResult.hitVec;
            }

            Entity entity = null;
            List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(this,
              getEntityBoundingBox().grow(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
            double d1 = 0.0D;
            float f1 = 0.3F;

            for (Entity entity1 : entities) {
                if (entity1.canBeCollidedWith() && (entity1 != shootingEntity || ticksInAir >= 5)) {
                    AxisAlignedBB aabb = entity1.getEntityBoundingBox().expand((double) f1, (double) f1, (double) f1);
                    RayTraceResult intercept = aabb.calculateIntercept(posVec, motionVec);

                    if (intercept != null) {
                        double distanceTo = posVec.distanceTo(intercept.hitVec);

                        if (distanceTo < d1 || d1 == 0.0D) {
                            entity = entity1;
                            d1 = distanceTo;
                        }
                    }
                }
            }

            if (entity != null) {
                rayTraceResult = new RayTraceResult(entity);
            }

            if (rayTraceResult != null) {
                if (rayTraceResult.entityHit != null) {
                    int damage = (int) this.damage;
                    DamageSource source;

                    if (shootingEntity == null || silenced) {
                        source = DamageSource.causeThrownDamage(this, this);
                    } else {
                        source = DamageSource.causeThrownDamage(this, shootingEntity);
                    }

                    if (isBurning() && !(rayTraceResult.entityHit instanceof EntityEnderman)) {
                        rayTraceResult.entityHit.setFire(5);
                    }

                    if (rayTraceResult.entityHit.attackEntityFrom(source, damage)) {
                        if (rayTraceResult.entityHit instanceof EntityLiving) {
                            EntityLiving entityHit = (EntityLiving) rayTraceResult.entityHit;

                            if (!world.isRemote) {
                                entityHit.setArrowCountInEntity(entityHit.getArrowCountInEntity() + 1);
                            }
                        }

                        if (!(rayTraceResult.entityHit instanceof EntityEnderman)) {
                            setDead();
                        }
                    } else {
                        motionX *= -0.10000000149011612D;
                        motionY *= -0.10000000149011612D;
                        motionZ *= -0.10000000149011612D;
                        rotationYaw += 180.0F;
                        prevRotationYaw += 180.0F;
                        ticksInAir = 0;
                    }
                } else {
                    tilePos = rayTraceResult.getBlockPos();
                    IBlockState inState = world.getBlockState(tilePos);
                    inTile = inState.getBlock();
                    inData = inTile.getMetaFromState(inState);
                    motionX = (double) ((float) (rayTraceResult.hitVec.x - posX));
                    motionY = (double) ((float) (rayTraceResult.hitVec.y - posY));
                    motionZ = (double) ((float) (rayTraceResult.hitVec.z - posZ));
                    float root = MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
                    posX -= motionX / (double) root * 0.05000000074505806D;
                    posY -= motionY / (double) root * 0.05000000074505806D;
                    posZ -= motionZ / (double) root * 0.05000000074505806D;
                    //this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    inGround = true;
                    arrowShake = 7;

                    if (inTile != null) {
                        inTile.onEntityCollidedWithBlock(world, tilePos, inState, this);
                    }
                }
            }

            posX += motionX;
            posY += motionY;
            posZ += motionZ;
            rotationYaw = (float) (Math.atan2(motionX, motionZ) * 180.0D / Math.PI);

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
            float f3 = 0.99F;
            f1 = 0.05F;

            if (this.isInWater()) {
                double f2 = 0.25D;
                for (int i = 0; i < 4; ++i) {
                    world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, posX - motionX * f2, posY - motionY * f2,
                      posZ - motionZ * f2, motionX, motionY, motionZ);
                }

                f3 = 0.8F;
            }

            motionX *= (double) f3;
            motionY *= (double) f3;
            motionZ *= (double) f3;
            motionY -= (double) f1;
            setPosition(posX, posY, posZ);
            //this.do();
            //this.f
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setShort("xTile", (short) tilePos.getX());
        nbt.setShort("yTile", (short) tilePos.getY());
        nbt.setShort("zTile", (short) tilePos.getZ());
        //  nbt.setByte("inTile", (byte)this.inTile);
        nbt.setByte("inData", (byte) inData);
        nbt.setByte("shake", (byte) arrowShake);
        nbt.setBoolean("inGround", inGround);
        nbt.setDouble("damage", damage);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        int x = nbt.getShort("xTile");
        int y = nbt.getShort("yTile");
        int z = nbt.getShort("zTile");
        tilePos = new BlockPos(x, y, z);

        // this.inTile = nbt.getByte("inTile") & 255;
        inData = nbt.getByte("inData") & 255;
        arrowShake = nbt.getByte("shake") & 255;
        inGround = nbt.getBoolean("inGround");

        if (nbt.hasKey("damage")) {
            damage = nbt.getDouble("damage");
        }
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }
}
