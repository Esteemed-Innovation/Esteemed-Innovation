package eiteam.esteemedinnovation.firearms.rocket.ammo;

import eiteam.esteemedinnovation.api.enhancement.IRocket;
import eiteam.esteemedinnovation.api.entity.EntityRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

public class ItemRocketMining extends Item implements IRocket {
    @Override
    public EntityRocket changeBullet(EntityRocket bullet) {
        return new EntityMiningRocket(bullet.worldObj, (EntityPlayer) bullet.shootingEntity, bullet.inputParam4, bullet.explosionSize);
    }
}
