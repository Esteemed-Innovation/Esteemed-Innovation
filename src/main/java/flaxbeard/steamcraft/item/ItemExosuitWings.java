package flaxbeard.steamcraft.item;

import flaxbeard.steamcraft.api.exosuit.ExosuitSlot;
import flaxbeard.steamcraft.api.exosuit.ModelExosuitUpgrade;
import flaxbeard.steamcraft.client.render.model.exosuit.ModelWings;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class ItemExosuitWings extends ItemExosuitUpgrade {

    public static HashMap<Integer, Integer> tickCache = new HashMap<Integer, Integer>();

    public ItemExosuitWings() {
        super(ExosuitSlot.bodyFront, "", "", 0);
    }

    public static int getTicks(Entity entity) {
        if (!tickCache.containsKey(entity.getEntityId())) {
            tickCache.put(entity.getEntityId(), 0);
        }
        return tickCache.get(entity.getEntityId());
    }

    public static void updateTicks(Entity entity, int ticks) {
        tickCache.remove(entity.getEntityId());
        tickCache.put(entity.getEntityId(), ticks);
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return ModelWings.class;
    }

    @Override
    public void updateModel(ModelBiped modelBiped, EntityLivingBase entityLivingBase, ItemStack itemStack, ModelExosuitUpgrade modelExosuitUpgrade) {
        int ticks = getTicks(entityLivingBase);
        float expansion = (float) (0.1F + (Math.sin(Math.toRadians(ticks * (20.0F / 15.0F) * 4.5F))) * 0.8F);

        modelExosuitUpgrade.nbtTagCompound.setFloat("expansion", expansion);

        if (!entityLivingBase.onGround && entityLivingBase.motionY < 0.0F && !entityLivingBase.isSneaking() && entityLivingBase.fallDistance > 1.4F && ticks < 15) {
            ticks++;
        }

        if ((entityLivingBase.onGround || entityLivingBase.motionY >= 0.0F || entityLivingBase.isSneaking()) && ticks > 0) {
            ticks--;
        }

        updateTicks(entityLivingBase, ticks);
    }
}
