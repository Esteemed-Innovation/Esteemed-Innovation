package eiteam.esteemedinnovation.client.render.model.exosuit;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.EntityLivingBase;
import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import eiteam.esteemedinnovation.client.ExosuitTexture;

public class ModelFrequencyShifter extends ModelExosuitUpgrade {
    private ModelRenderer EarR;
    private ModelRenderer EarL;
    private ModelRenderer Stem;
    private ModelRenderer Headband;
    private ModelRenderer Mic;

    public ModelFrequencyShifter() {
        textureWidth = 34;
        textureHeight = 32;

        EarR = new ModelRenderer(this, 0, 0);
        EarR.setRotationPoint(0.0F, 0.0F, 0.0F);
        EarR.addBox(-5.0F, -6.5F, -2.0F, 1, 4, 4, 0.0F);

        EarL = new ModelRenderer(this, 0, 0);
        EarL.setRotationPoint(0.0F, 0.0F, 0.0F);
        EarL.addBox(4.0F, -6.5F, -2.0F, 1, 4, 4, 0.0F);

        Stem = new ModelRenderer(this, 0, 15);
        Stem.setRotationPoint(0.0F, 0.0F, 0.0F);
        Stem.addBox(4.0F, -10.5F, -4.5F, 1, 16, 1, -0.2F);
        setRotateAngle(Stem, -1.0471975511965976F, 0.0F, 0.0F);

        Headband = new ModelRenderer(this, 10, 0);
        Headband.setRotationPoint(0.0F, 0.0F, 0.0F);
        Headband.addBox(-5.0F, -9.0F, -1.0F, 10, 4, 2, -0.5F);

        Mic = new ModelRenderer(this, 0, 15);
        Mic.setRotationPoint(0.0F, 0.0F, 0.0F);
        Mic.addBox(2.2F, 4.5F, -4.5F, 2, 1, 1, 0.0F);
        Stem.addChild(Mic);
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
        setRotateAngle(Stem, parentModel.bipedHeadwear.rotateAngleX - 1.0471975511965976F,
          parentModel.bipedHeadwear.rotateAngleY, parentModel.bipedHeadwear.rotateAngleZ);
        super.renderModel(parentModel, entityLivingBase);
        ExosuitTexture.FREQUENCY_SHIFTER.bindTexturePart(1);
        EarR.render(0.0625F);
        EarL.render(0.0625F);
        Stem.render(0.0625F);
        Headband.render(0.0625F);
    }

    @Override
    public void copyRotationAngles(ModelBiped parentModel, EntityLivingBase entityLivingBase) {
        copyRotateAngles(EarR, parentModel.bipedHeadwear);
        copyRotateAngles(EarL, parentModel.bipedHeadwear);
        copyRotateAngles(Headband, parentModel.bipedHeadwear);
    }
}
