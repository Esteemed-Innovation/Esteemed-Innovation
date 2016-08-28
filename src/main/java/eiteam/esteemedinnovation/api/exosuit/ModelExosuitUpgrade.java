package eiteam.esteemedinnovation.api.exosuit;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author dmillerw
 */
public abstract class ModelExosuitUpgrade extends ModelBase {
    public final NBTTagCompound nbtTagCompound = new NBTTagCompound();

    public abstract void renderModel(ModelBiped parentModel, EntityLivingBase entityLivingBase);
}
