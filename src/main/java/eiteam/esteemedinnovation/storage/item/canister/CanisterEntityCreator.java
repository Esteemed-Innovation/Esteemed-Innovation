package eiteam.esteemedinnovation.storage.item.canister;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CanisterEntityCreator {
    @SubscribeEvent
    public void spawnCanisterEntities(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EntityItem && !(entity instanceof EntityCanisterItem)) {
            EntityItem item = (EntityItem) entity;
            ItemStack stack = item.getEntityItem();
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Canned")) {
                if (!event.getWorld().isRemote) {
                    EntityCanisterItem item2 = new EntityCanisterItem(item.worldObj, item.posX, item.posY, item.posZ, item);
                    item2.motionX = item.motionX;
                    item2.motionY = item.motionY;
                    item2.motionZ = item.motionZ;
//                    item2.delayBeforeCanPickup = item.delayBeforeCanPickup;
                    item.worldObj.spawnEntityInWorld(item2);
                }
                item.setDead();
            }
        }
    }
}
