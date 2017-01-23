package eiteam.esteemedinnovation.api.exosuit;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

public abstract class ModelExosuitUpgrade extends ModelBase {
    public final NBTTagCompound nbtTagCompound = new NBTTagCompound();

    /**
     * Called in {ModelSteamExosuit#render. Handle the rendering of
     * your model here. Call the super method to handle rotation angle copying and sneak translation. You probably want
     * to call the super method at the start of renderModel rather than elsewhere.
     * @param parentModel The exosuit model
     * @param entityLivingBase The player wearing the suit
     */
    public void renderModel(ModelBiped parentModel, EntityLivingBase entityLivingBase) {
        copyRotationAngles(parentModel, entityLivingBase);
        if (entityLivingBase.isSneaking()) {
            // Taken from ModelBiped#render.
            GlStateManager.translate(0, 0.2F, 0);
        }
    }

    /**
     * Called in {@link ModelExosuitUpgrade#renderModel(ModelBiped, EntityLivingBase)}. Override this method to handle
     * copying the parent model's rotation angles into your model renderers.
     */
    public abstract void copyRotationAngles(ModelBiped parentModel, EntityLivingBase entityLivingBase);

    /**
     * Copies the rotate angles from one ModelRenderer to another.
     * @param child The model to copy to
     * @param parent The model to copy from
     */
    protected static void copyRotateAngles(ModelRenderer child, ModelRenderer parent) {
        child.rotateAngleX = parent.rotateAngleX;
        child.rotateAngleY = parent.rotateAngleY;
        child.rotateAngleZ = parent.rotateAngleZ;
    }
}
