package flaxbeard.steamcraft.ai;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import flaxbeard.steamcraft.api.enhancement.IEnhancementFirearm;
import flaxbeard.steamcraft.api.enhancement.UtilEnhancements;
import flaxbeard.steamcraft.entity.EntityMusketBall;
import flaxbeard.steamcraft.item.firearm.ItemFirearm;

public class EntityAIFirearmAttack extends EntityAIBase
{
    /** The entity the AI instance has been applied to */
    private final EntityLiving entityHost;
    /**
     * The entity (as a RangedAttackMob) the AI instance has been applied to.
     */
    private final EntityLiving rangedAttackEntityHost;
    private EntityLivingBase attackTarget;
    /**
     * A decrementing tick that spawns a ranged attack once this value reaches 0. It is then set back to the
     * maxRangedAttackTime.
     */
    private int rangedAttackTime;
    private double entityMoveSpeed;
    private int field_75318_f;
    private int field_96561_g;
    /**
     * The maximum time the AI has to wait before peforming another ranged attack.
     */
    private int maxRangedAttackTime;
    private float field_96562_i;
    private float field_82642_h;
    private static final String __OBFID = "CL_00001609";
    private boolean canShoot = true;

    public EntityAIFirearmAttack(EntityLiving par1IRangedAttackMob, double par2, int par4, float par5)
    {
        this(par1IRangedAttackMob, par2, par4, par4, par5);
    }

    public EntityAIFirearmAttack(EntityLiving par1IRangedAttackMob, double par2, int par4, int par5, float par6)
    {
        this.rangedAttackTime = -1;

        if (!(par1IRangedAttackMob instanceof EntityLivingBase))
        {
            throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
        }
        else
        {
            this.rangedAttackEntityHost = par1IRangedAttackMob;
            this.entityHost = (EntityLiving)par1IRangedAttackMob;
            this.entityMoveSpeed = par2;
            this.field_96561_g = par4;
            this.maxRangedAttackTime = par5;
            if (rangedAttackEntityHost.getHeldItem().getItem() != null && rangedAttackEntityHost.getHeldItem().getItem() instanceof ItemFirearm) {
        		ItemFirearm gun = (ItemFirearm) rangedAttackEntityHost.getHeldItem().getItem();
        		this.maxRangedAttackTime = gun.reloadTime;
        		System.out.println(maxRangedAttackTime);
            }
            this.field_96562_i = par6;
            this.field_82642_h = par6 * par6;
            this.setMutexBits(3);
        }
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        EntityLivingBase entitylivingbase = this.entityHost.getAttackTarget();

        if (entitylivingbase == null)
        {
            return false;
        }
        else
        {
            this.attackTarget = entitylivingbase;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return this.shouldExecute() || !this.entityHost.getNavigator().noPath();
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.attackTarget = null;
        this.field_75318_f = 0;
        this.rangedAttackTime = -1;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        double d0 = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.boundingBox.minY, this.attackTarget.posZ);
        boolean flag = this.entityHost.getEntitySenses().canSee(this.attackTarget);

        if (flag)
        {
            ++this.field_75318_f;
        }
        else
        {
            this.field_75318_f = 0;
        }

        if (!canShoot || d0 <= (double)this.field_82642_h && this.field_75318_f >= 20)
        {
            this.entityHost.getNavigator().clearPathEntity();
            float f;
            if (canShoot) {
            	if (d0 > (double)this.field_82642_h || !flag)
                {
                    return;
                }

                f = MathHelper.sqrt_double(d0) / this.field_96562_i;
                float f1 = f;

                if (f < 0.1F)
                {
                    f1 = 0.1F;
                }

                if (f1 > 1.0F)
                {
                    f1 = 1.0F;
                }
                this.attackEntityWithRangedAttack(this.attackTarget, f1);
                //this.rangedAttackEntityHost.attackEntityWithRangedAttack(this.attackTarget, f1);
                this.rangedAttackTime = MathHelper.floor_float(f * (float)(this.maxRangedAttackTime - this.field_96561_g) + (float)this.field_96561_g);
                canShoot = false;
            }
        }
        else
        {
            this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
        }

        this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 90.0F, 10.0F);

//        for (int i = 0; i<100; i++) {
//            this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 1.0F, 1.0F);
//        }
        if (--this.rangedAttackTime <= 0 && !canShoot)
        {
        	entityHost.worldObj.playSoundAtEntity(entityHost, "random.click", 1F, entityHost.worldObj.rand.nextFloat() * 0.1F + 0.9F);
            canShoot = true;
        }
    }
    
    public void attackEntityWithRangedAttack(EntityLivingBase par1EntityLivingBase, float par2)
    {
    	World worldObj = rangedAttackEntityHost.worldObj;
    	if (rangedAttackEntityHost.getHeldItem().getItem() != null && rangedAttackEntityHost.getHeldItem().getItem() instanceof ItemFirearm) {
    		ItemFirearm gun = (ItemFirearm) rangedAttackEntityHost.getHeldItem().getItem();
	        float damage = (gun.damage/2.0F);
	        
            if (worldObj.difficultySetting == EnumDifficulty.EASY)
            {
            	damage = damage / 2.0F + 1.0F;
            }

            if (worldObj.difficultySetting == EnumDifficulty.HARD)
            {
            	damage = damage * 3.0F / 2.0F;
            }
	       // EntityMusketBall entityMusketBall = new EntityMusketBall(worldObj, rangedAttackEntityHost, 2.0F, ((0.0F + gun.accuracy)), damage, true);
	        EntityMusketBall entityMusketBall = new EntityMusketBall(worldObj, rangedAttackEntityHost, par1EntityLivingBase, 10F, (float)(14 - worldObj.difficultySetting.getDifficultyId() * 4));

	        worldObj.playSoundAtEntity(rangedAttackEntityHost, "random.explode", ((gun.knockback) * (2F / 5F)), 1.0F / (worldObj.rand.nextFloat() * 0.4F + 1.2F) + 0.5F);
            for (int i = 1; i < 16; i++)
            {
            	worldObj.spawnParticle("smoke", rangedAttackEntityHost.posX + worldObj.rand.nextFloat() - 0.5F, rangedAttackEntityHost.posY + (worldObj.rand.nextFloat() / 2) - 0.25F, rangedAttackEntityHost.posZ + worldObj.rand.nextFloat() - 0.5F, 0.0D, 0.01D, 0.0D);
            }
	        //EntityArrow entityarrow = new EntityArrow(worldObj, rangedAttackEntityHost, par1EntityLivingBase, 1.6F, (float)(14 - worldObj.difficultySetting.getDifficultyId() * 4));
//	        int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, rangedAttackEntityHost.getHeldItem());
//	        int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, rangedAttackEntityHost.getHeldItem());
//	
//	        if (i > 0)
//	        {
//	            entityarrow.setDamage(entityarrow.getDamage() + (double)i * 0.5D + 0.5D);
//	        }
//	
//	        if (j > 0)
//	        {
//	            entityarrow.setKnockbackStrength(j);
//	        }
//	
//	        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, rangedAttackEntityHost.getHeldItem()) > 0)
//	        {
//	            entityarrow.setFire(100);
//	        }
//	
//	        rangedAttackEntityHost.playSound("random.bow", 1.0F, 1.0F / (worldObj.rand.nextFloat() * 0.4F + 0.8F));
            if (gun.shotgun)
            {
                for (int i = 1; i < 21; i++)
                {
        	        EntityMusketBall entityMusketBall2 = new EntityMusketBall(worldObj, rangedAttackEntityHost, 2.0F, ((0.0F + gun.accuracy)), (gun.damage-worldObj.difficultySetting.getDifficultyId()), true);
                    worldObj.spawnEntityInWorld(entityMusketBall2);
                }
            }
            else
            {
                worldObj.spawnEntityInWorld(entityMusketBall);
            }
    	}
    }
}