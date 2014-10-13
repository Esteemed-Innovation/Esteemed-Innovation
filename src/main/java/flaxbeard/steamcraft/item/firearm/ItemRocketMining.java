package flaxbeard.steamcraft.item.firearm;

import flaxbeard.steamcraft.api.enhancement.IRocket;
import flaxbeard.steamcraft.entity.EntityMiningRocket;
import flaxbeard.steamcraft.entity.EntityRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

public class ItemRocketMining extends Item implements IRocket {
    @Override
    public EntityRocket changeBullet(EntityRocket bullet) {
        return new EntityMiningRocket(bullet.worldObj, (EntityPlayer) bullet.shootingEntity, bullet.inputParam4, bullet.explosionSize);
    }
}
