package eiteam.esteemedinnovation.firearms.rocket.ammo;

import eiteam.esteemedinnovation.api.enhancement.Rocket;
import eiteam.esteemedinnovation.api.entity.EntityRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

public class ItemRocketConcussive extends Item implements Rocket {
    @Override
    public EntityRocket changeBullet(EntityRocket bullet) {
        return new EntityConcussiveRocket(bullet.world, (EntityPlayer) bullet.shootingEntity, bullet.inputParam4, bullet.explosionSize);
    }
}
