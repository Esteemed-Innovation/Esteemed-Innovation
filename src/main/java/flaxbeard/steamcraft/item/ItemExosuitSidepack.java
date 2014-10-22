package flaxbeard.steamcraft.item;

import flaxbeard.steamcraft.api.exosuit.ExosuitSlot;
import flaxbeard.steamcraft.api.exosuit.ModelExosuitUpgrade;
import flaxbeard.steamcraft.client.render.model.exosuit.ModelSidepack;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.vecmath.Vector2d;
import java.util.HashMap;

public class ItemExosuitSidepack extends ItemExosuitUpgrade {

    public static HashMap<Integer, Float> rotationCache = new HashMap<Integer, Float>();

    public ItemExosuitSidepack() {
        super(ExosuitSlot.legsHips, "", "", 0);
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return ModelSidepack.class;
    }

    @Override
    public void updateModel(ModelBiped modelBiped, EntityLivingBase entityLivingBase, ItemStack itemStack, ModelExosuitUpgrade modelExosuitUpgrade) {
        Vector2d vector = new Vector2d(entityLivingBase.motionX, entityLivingBase.motionZ);

        if (entityLivingBase instanceof EntityPlayer) {
            float targetRotation = 360.0F * ((float) (Math.atan2(vector.y, vector.x) / (2 * Math.PI)));

            if (!rotationCache.containsKey(entityLivingBase.getEntityId())) {
                rotationCache.put(entityLivingBase.getEntityId(), targetRotation);
            }

            float lastRotation = ItemExosuitSidepack.rotationCache.get(entityLivingBase.getEntityId());
            float rotation = lastRotation + (targetRotation - lastRotation) / 1000.0F;

            modelExosuitUpgrade.nbtTagCompound.setFloat("rotation", rotation);

            ItemExosuitSidepack.rotationCache.put(entityLivingBase.getEntityId(), rotation);
        }
    }
}
