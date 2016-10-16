package eiteam.esteemedinnovation.item.armor.exosuit;

import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import eiteam.esteemedinnovation.client.render.model.exosuit.ModelSidepack;
import eiteam.esteemedinnovation.misc.EntityHelper;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.vecmath.Vector2d;

public class ItemExosuitSidepack extends ItemExosuitUpgrade {
    public ItemExosuitSidepack() {
        super(ExosuitSlot.LEGS_HIPS, "", "", 0);
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return ModelSidepack.class;
    }

    @Override
    public void updateModel(ModelBiped modelBiped, EntityLivingBase entityLivingBase, ItemStack itemStack, ModelExosuitUpgrade modelExosuitUpgrade) {
        Vector2d vector = new Vector2d(entityLivingBase.motionX, entityLivingBase.motionZ);

        if (entityLivingBase instanceof EntityPlayer && EntityHelper.hasEntityMoved(entityLivingBase)) {
            float targetRotation = 360F * ((float) (StrictMath.atan2(vector.y, vector.x) / (2 * Math.PI)));

            NBTTagCompound nbt = modelExosuitUpgrade.nbtTagCompound;

            if (!nbt.hasKey("rotation")) {
                nbt.setFloat("rotation", targetRotation);
            }

            modelExosuitUpgrade.nbtTagCompound.setFloat("rotation", targetRotation);
        }
    }
}
