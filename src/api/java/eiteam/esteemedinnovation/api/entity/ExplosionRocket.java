package eiteam.esteemedinnovation.api.entity;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.*;

// TODO: Fix the completely unreadable local variable names. This requires math so \o/
public class ExplosionRocket extends Explosion {
    /**
     * whether or not the explosion sets fire to blocks around it
     */
    public boolean isFlaming;
    /**
     * whether or not this explosion spawns smoke particles
     */
    public boolean isSmoking = true;
    public double explosionX;
    public double explosionY;
    public double explosionZ;
    public Entity exploder;
    public float explosionSize;
    public boolean destroyBlocks = true;
    /**
     * A list of ChunkPositions of blocks affected by this explosion
     */
    public List<BlockPos> affectedBlockPositions = new ArrayList<>();
    private static final int RADIUS = 16;
    private Random explosionRNG = new Random();
    private World worldObj;
    private Map<EntityPlayer, Vec3d> playerKnockbackMap = new HashMap<>();
    private boolean dropAllBlocks = false;

    public ExplosionRocket(World world, Entity entity, double x, double y, double z, float size, List<BlockPos> affectedPositions) {
        this(world, entity, x, y, z, size, false, true, affectedPositions);
    }

    public ExplosionRocket(World world, Entity entity, double x, double y, double z, float size, boolean blocks, boolean allBlocks) {
        this(world, entity, x, y, z, size, blocks, allBlocks, Lists.<BlockPos>newArrayList());
    }

    public ExplosionRocket(World world, Entity entity, double x, double y, double z, float size, boolean blocks, boolean allBlocks, List<BlockPos> affectedBlockPositions) {
        super(world, entity, x, y, z, size, blocks, allBlocks, affectedBlockPositions);
        worldObj = world;
        exploder = entity;
        explosionX = x;
        explosionY = y;
        explosionZ = z;
        explosionSize = size;
        destroyBlocks = blocks;
        dropAllBlocks = allBlocks;
        this.affectedBlockPositions.addAll(affectedBlockPositions);
    }

    @Override
    public void doExplosionA() {
        HashSet<BlockPos> hashset = new HashSet<>();
        int i;
        int j;
        int k;
        double d5;
        double d6;
        double d7;

        for (i = 0; i < RADIUS; ++i) {
            for (j = 0; j < RADIUS; ++j) {
                for (k = 0; k < RADIUS; ++k) {
                    if (i == 0 || i == RADIUS - 1 || j == 0 || j == RADIUS - 1 || k == 0 || k == RADIUS - 1) {
                        double d0 = (double) ((float) i / ((float) RADIUS - 1.0F) * 2.0F - 1.0F);
                        double d1 = (double) ((float) j / ((float) RADIUS - 1.0F) * 2.0F - 1.0F);
                        double d2 = (double) ((float) k / ((float) RADIUS - 1.0F) * 2.0F - 1.0F);
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 /= d3;
                        d1 /= d3;
                        d2 /= d3;
                        float f1 = explosionSize * (0.7F + worldObj.rand.nextFloat() * 0.6F);
                        d5 = explosionX;
                        d6 = explosionY;
                        d7 = explosionZ;

                        for (float f2 = 0.3F; f1 > 0.0F; f1 -= f2 * 0.75F) {
                            int j1 = MathHelper.floor(d5);
                            int k1 = MathHelper.floor(d6);
                            int l1 = MathHelper.floor(d7);
                            BlockPos currentPos = new BlockPos(j1, k1, l1);
                            IBlockState state = worldObj.getBlockState(currentPos);

                            if (state.getMaterial() != Material.AIR) {
                                float resistance = exploder != null ? exploder.getExplosionResistance(this, worldObj, currentPos, state) : state.getBlock().getExplosionResistance(exploder);
                                f1 -= (resistance + 0.3F) * f2;
                            }

                            if (f1 > 0.0F && (exploder == null || exploder.canExplosionDestroyBlock(this, worldObj, currentPos, state, f1))) {
                                hashset.add(currentPos);
                            }

                            d5 += d0 * (double) f2;
                            d6 += d1 * (double) f2;
                            d7 += d2 * (double) f2;
                        }
                    }
                }
            }
        }

        affectedBlockPositions.addAll(hashset);
        explosionSize *= 2.0F;
        int minX = MathHelper.floor(explosionX - (double) explosionSize - 1.0D);
        int maxX = MathHelper.floor(explosionX + (double) explosionSize + 1.0D);
        int minY = MathHelper.floor(explosionY - (double) explosionSize - 1.0D);
        int maxY = MathHelper.floor(explosionY + (double) explosionSize + 1.0D);
        int minZ = MathHelper.floor(explosionZ - (double) explosionSize - 1.0D);
        int maxZ = MathHelper.floor(explosionZ + (double) explosionSize + 1.0D);
        AxisAlignedBB explosionBounding = new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
        List<Entity> list = worldObj.getEntitiesWithinAABB(Entity.class, explosionBounding);
        Vec3d vec3 = new Vec3d(explosionX, explosionY, explosionZ);

        for (Entity entity : list) {
            double d4 = entity.getDistance(explosionX, explosionY, explosionZ) / (double) explosionSize;

            if (d4 <= 1.0D && !(entity instanceof EntityRocket) && (!dropAllBlocks || !(entity instanceof EntityItem))) {
                d5 = entity.posX - explosionX;
                d6 = entity.posY + (double) entity.getEyeHeight() + 0.5F - explosionY;
                d7 = entity.posZ - explosionZ;
                double d9 = (double) MathHelper.sqrt(d5 * d5 + d6 * d6 + d7 * d7);

                if (d9 != 0.0D) {
                    d5 /= d9;
                    d6 /= d9;
                    d7 /= d9;
                    double d10 = (double) worldObj.getBlockDensity(vec3, entity.getEntityBoundingBox());
                    double d11 = (1.0D - d4) * d10;
                    float damage;
                    boolean entityIsExploder = entity == exploder;
                    if (entityIsExploder) {
                        damage = 0.15F;
                    } else {
                        damage = dropAllBlocks ? 0.15F : 1F;
                    }
                    damage *= (float) ((int) ((d11 * d11 + d11) / 2.0D * 8.0D * (double) (explosionSize * 3.0F) + 1.0D));
                    entity.attackEntityFrom(DamageSource.causeExplosionDamage(this), damage);
                    entity.hurtResistantTime = 0;
                    double d8 = entityIsExploder ? d11 : EnchantmentProtection.getBlastDamageReduction((EntityLivingBase) entity, d11);
                    entity.motionX += d5 * d8 * (entityIsExploder ? 3.0F : 1.0F) * (dropAllBlocks ? 0.1F : 1.0F);
                    entity.motionY += d6 * d8 * (entityIsExploder ? 2.0F : 1.0F) * (dropAllBlocks ? 0.1F : 1.0F);
                    entity.motionZ += d7 * d8 * (entityIsExploder ? 3.0F : 1.0F) * (dropAllBlocks ? 0.1F : 1.0F);

                    if (entity instanceof EntityPlayer) {
                        EntityPlayer player = (EntityPlayer) entity;
                        if (player.capabilities.isCreativeMode) {
                            double xChange = d5 * d8 * (entityIsExploder ? 3.0F : 1.0F) * (dropAllBlocks ? 0.15F : 1.0F);
                            double yChange = d6 * d8 * (entityIsExploder ? 2.0F : 1.0F) * (dropAllBlocks ? 0.15F : 1.0F);
                            double zChange = d7 * d8 * (entityIsExploder ? 3.0F : 1.0F) * (dropAllBlocks ? 0.15F : 1.0F);
                            player.motionX += xChange;
                            player.motionY += yChange;
                            player.motionZ += zChange;
                        }
                        player.fallDistance = 0.1F;

                        playerKnockbackMap.put(player, new Vec3d(d5 * d11, d6 * d11, d7 * d11));
                    }
                }
            }
        }
    }

    /**
     * Does the second part of the explosion (sound, particles, drop spawn)
     */
    @Override
    public void doExplosionB(boolean spawnParticles) {
        worldObj.playSound(explosionX, explosionY, explosionZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS,
          4F, (1F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F * 0.7F), false);

        if (explosionSize >= 2.0F && isSmoking) {
            worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, explosionX, explosionY, explosionZ, 1D, 0D, 0D);
        } else {
            worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, explosionX, explosionY, explosionZ, 1.0D, 0.0D, 0.0D);
        }

        if (isSmoking) {
            for (BlockPos pos : affectedBlockPositions) {
                if (spawnParticles) {
                    double d0 = (double) ((float) pos.getX() + worldObj.rand.nextFloat());
                    double d1 = (double) ((float) pos.getY() + worldObj.rand.nextFloat());
                    double d2 = (double) ((float) pos.getZ() + worldObj.rand.nextFloat());
                    double d3 = d0 - explosionX;
                    double d4 = d1 - explosionY;
                    double d5 = d2 - explosionZ;
                    double d6 = (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
                    d3 /= d6;
                    d4 /= d6;
                    d5 /= d6;
                    double d7 = 0.5D / (d6 / (double) explosionSize + 0.1D);
                    d7 *= (double) (worldObj.rand.nextFloat() * worldObj.rand.nextFloat() + 0.3F);
                    d3 *= d7;
                    d4 *= d7;
                    d5 *= d7;
                    worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (d0 + explosionX * 1.0D) / 2.0D, (d1 + explosionY * 1.0D) / 2.0D, (d2 + explosionZ * 1.0D) / 2.0D, d3, d4, d5);
                    worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, d3, d4, d5);
                }

                IBlockState state = worldObj.getBlockState(pos);

                if (state.getMaterial() != Material.AIR && destroyBlocks) {
                    Block block = state.getBlock();
                    if (block.canDropFromExplosion(this)) {
                        float chance = 1F;
                        if (!dropAllBlocks) {
                            chance /= explosionSize;
                        }
                        block.dropBlockAsItemWithChance(worldObj, pos, state, chance, 0);
                    }

                    block.onBlockExploded(worldObj, pos, this);
                }
            }
        }

        if (isFlaming) {
            for (BlockPos pos : affectedBlockPositions) {
                IBlockState state = worldObj.getBlockState(pos);
                Block block = state.getBlock();
                if (state.getMaterial() == Material.AIR && block.canBeReplacedByLeaves(state, worldObj, pos) &&
                  explosionRNG.nextInt(3) == 0) {
                    worldObj.setBlockState(pos, Blocks.FIRE.getDefaultState());
                }
            }
        }
    }

    @Override
    public Map<EntityPlayer, Vec3d> getPlayerKnockbackMap() {
        return playerKnockbackMap;
    }

    @Override
    public EntityLivingBase getExplosivePlacedBy() {
        if (exploder == null) {
            return null;
        }
        if (exploder instanceof EntityTNTPrimed) {
            EntityTNTPrimed tnt = (EntityTNTPrimed) exploder;
            return tnt.getTntPlacedBy();
        }
        if (exploder instanceof EntityLivingBase) {
            return (EntityLivingBase) exploder;
        }
        return null;
    }
}