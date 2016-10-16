package eiteam.esteemedinnovation.client.render.model.exosuit;

import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import eiteam.esteemedinnovation.client.ExosuitTexture;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;

public class ModelReloadingHolster extends ModelExosuitUpgrade {
    private ModelRenderer Holster;

    public ModelReloadingHolster() {
        textureWidth = 40;
        textureHeight = 11;
        Holster = new ModelRenderer(this, 0, 0);
        Holster.setRotationPoint(0.0F, 0.0F, 0.0F);
        Holster.addBox(-5.01F, 8.5F, -4.0F, 1, 5, 4, 0.0F);
        setRotateAngle(Holster, 0.2617993877991494F, 0.0F, 0.0F);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    private static void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void renderModel(ModelBiped parentModel, EntityLivingBase entityLivingBase) {
        if (entityLivingBase.isSneaking()) {
            GlStateManager.translate(0, 0, 0.25F);
        }
        ExosuitTexture.RELOADING_HOLSTER.bindTexturePart(1);
        Holster.render(0.0625F);
    }

    @Override
    public void copyRotationAngles(ModelBiped parentModel, EntityLivingBase entityLivingBase) {}
}
