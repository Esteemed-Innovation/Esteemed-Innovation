package eiteam.esteemedinnovation.item.firearm;

import eiteam.esteemedinnovation.api.enhancement.IRocket;
import eiteam.esteemedinnovation.api.entity.EntityRocket;
import net.minecraft.item.Item;

public class ItemRocketBasic extends Item implements IRocket {

    @Override
    public EntityRocket changeBullet(EntityRocket bullet) {
        return bullet;
    }

}
