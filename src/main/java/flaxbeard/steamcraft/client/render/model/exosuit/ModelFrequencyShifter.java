package flaxbeard.steamcraft.client.render.model.exosuit;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.EntityLivingBase;
import flaxbeard.steamcraft.api.exosuit.ModelExosuitUpgrade;
import flaxbeard.steamcraft.client.ExosuitTexture;

/**
 * ModelBiped - Either Mojang or a mod author
 * Created using Tabula 4.1.1
 */
public class ModelFrequencyShifter extends ModelExosuitUpgrade {
    public ModelRenderer EarR;
    public ModelRenderer EarL;
    public ModelRenderer Stem;
    public ModelRenderer Headband;
    public ModelRenderer Mic;

    public ModelFrequencyShifter() {
        this.textureWidth = 34;
        this.textureHeight = 32;
        
        this.EarR = new ModelRenderer(this, 0, 0);
        this.EarR.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.EarR.addBox(-5.0F, -6.5F, -2.0F, 1, 4, 4, 0.0F);
        
        this.EarL = new ModelRenderer(this, 0, 0);
        this.EarL.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.EarL.addBox(4.0F, -6.5F, -2.0F, 1, 4, 4, 0.0F);
        
        this.Stem = new ModelRenderer(this, 0, 15);
        this.Stem.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Stem.addBox(4.0F, -10.5F, -4.5F, 1, 16, 1, -0.2F);
        this.setRotateAngle(Stem, -1.0471975511965976F, 0.0F, 0.0F);
        
        this.Headband = new ModelRenderer(this, 10, 0);
        this.Headband.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Headband.addBox(-5.0F, -9.0F, -1.0F, 10, 4, 2, -0.5F);
        
        this.Mic = new ModelRenderer(this, 0, 15);
        this.Mic.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Mic.addBox(2.2F, 4.5F, -4.5F, 2, 1, 1, 0.0F);
        this.Stem.addChild(this.Mic);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public void copyRotationAngles(ModelRenderer parent) {
        float x = parent.rotateAngleX;
        float y = parent.rotateAngleY;
        float z = parent.rotateAngleZ;

        setRotateAngle(this.EarR, x, y, z);
        setRotateAngle(this.EarL, x, y, z);
        setRotateAngle(this.Headband, x, y, z);
    }

    @Override
    public void renderModel(ModelBiped parentModel, EntityLivingBase entityLivingBase) {
        ExosuitTexture.FREQUENCY_SHIFTER.bindTexturePart(1);
        this.EarR.render(0.0625F);
        this.EarL.render(0.0625F);
        this.Stem.render(0.0625F);
        this.Headband.render(0.0625F);

        copyRotationAngles(parentModel.bipedHeadwear);
        setRotateAngle(this.Stem, parentModel.bipedHeadwear.rotateAngleX - 1.0471975511965976F,
          parentModel.bipedHeadwear.rotateAngleY, parentModel.bipedHeadwear.rotateAngleZ);
    }
}
