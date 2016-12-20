package eiteam.esteemedinnovation.firearms.rocket.ammo;

import eiteam.esteemedinnovation.api.firearm.rocketlauncher.IRocket;
import eiteam.esteemedinnovation.api.firearm.rocketlauncher.EntityRocket;
import net.minecraft.item.Item;

public class ItemRocketBasic extends Item implements IRocket {

    @Override
    public EntityRocket changeBullet(EntityRocket bullet) {
        return bullet;
    }

}
