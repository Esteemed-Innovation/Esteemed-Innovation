package flaxbeard.steamcraft.item.firearm;

import net.minecraft.item.Item;
import flaxbeard.steamcraft.api.enhancement.IRocket;
import flaxbeard.steamcraft.entity.EntityRocket;

public class ItemRocketBasic extends Item implements IRocket {

	@Override
	public EntityRocket changeBullet(EntityRocket bullet) {
		return bullet;
	}

}
