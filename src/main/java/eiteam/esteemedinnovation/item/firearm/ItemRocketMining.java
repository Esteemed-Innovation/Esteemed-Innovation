package eiteam.esteemedinnovation.item.firearm;

import eiteam.esteemedinnovation.api.enhancement.IRocket;
import eiteam.esteemedinnovation.entity.projectile.EntityMiningRocket;
import eiteam.esteemedinnovation.entity.projectile.EntityRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

public class ItemRocketMining extends Item implements IRocket {
    @Override
    public EntityRocket changeBullet(EntityRocket bullet) {
        return new EntityMiningRocket(bullet.worldObj, (EntityPlayer) bullet.shootingEntity, bullet.inputParam4, bullet.explosionSize);
    }
}
