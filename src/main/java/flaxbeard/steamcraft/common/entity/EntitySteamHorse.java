package flaxbeard.steamcraft.common.entity;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.inventory.AnimalChest;
import net.minecraft.world.World;

public class EntitySteamHorse extends EntityHorse {

    private static final IAttribute horseJumpStrength = (new RangedAttribute("horse.jumpStrength", 0.7D, 0.0D, 2.0D)).setDescription("Jump Strength").setShouldWatch(true);
    private AnimalChest horseChest;


    public EntitySteamHorse(World par1World) {
        super(par1World);
    }

    private float func_110267_cL() {
        return 25.0F;
    }


    public IEntityLivingData onSpawnWithEgg(IEntityLivingData par1EntityLivingData) {

        int i = 7;
        int l = 0;
        Object p_110161_1_1 = new EntityHorse.GroupData(l, i);

        this.setHorseType(l);
        this.setHorseVariant(i);


        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue((0.35 + 0.5D) * 0.25D);
        //    this.getEntityAttribute(horseJumpStrength).setBaseValue(0.5D);

        this.setHealth(this.getMaxHealth());
        return (IEntityLivingData) p_110161_1_1;
    }

    public boolean isTame() {
        return true;
    }

    public boolean canMateWith(EntityAnimal par1EntityAnimal) {
        return false;
    }

}
