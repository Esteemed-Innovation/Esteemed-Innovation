package eiteam.esteemedinnovation.item.firearm;

import eiteam.esteemedinnovation.api.enhancement.IRocket;
import eiteam.esteemedinnovation.entity.projectile.EntityRocket;
import net.minecraft.item.Item;

public class ItemRocketBasic extends Item implements IRocket {

    @Override
    public EntityRocket changeBullet(EntityRocket bullet) {
        return bullet;
    }

}
