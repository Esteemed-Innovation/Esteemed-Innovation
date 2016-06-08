package flaxbeard.steamcraft.item.firearm;

import flaxbeard.steamcraft.api.enhancement.IRocket;
import flaxbeard.steamcraft.entity.projectile.EntityRocket;
import net.minecraft.item.Item;

public class ItemRocketBasic extends Item implements IRocket {

    @Override
    public EntityRocket changeBullet(EntityRocket bullet) {
        return bullet;
    }

}
