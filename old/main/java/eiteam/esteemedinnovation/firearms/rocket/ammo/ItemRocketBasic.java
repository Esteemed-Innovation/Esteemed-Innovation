package eiteam.esteemedinnovation.firearms.rocket.ammo;

import eiteam.esteemedinnovation.api.enhancement.Rocket;
import eiteam.esteemedinnovation.api.entity.EntityRocket;
import net.minecraft.item.Item;

public class ItemRocketBasic extends Item implements Rocket {
    @Override
    public EntityRocket changeBullet(EntityRocket bullet) {
        return bullet;
    }
}
