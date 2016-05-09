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
    public ModelRenderer earR;
    public ModelRenderer earL;
    public ModelRenderer stem;
    public ModelRenderer headBand;
    public ModelRenderer mic;

    public ModelFrequencyShifter() {
        this.textureWidth = 34;
        this.textureHeight = 32;
        
        this.earR = new ModelRenderer(this, 0, 0);
        this.earR.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.earR.addBox(-5.0F, -6.5F, -2.0F, 1, 4, 4, 0.0F);
        
        this.earL = new ModelRenderer(this, 0, 0);
        this.earL.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.earL.addBox(4.0F, -6.5F, -2.0F, 1, 4, 4, 0.0F);
        
        this.stem = new ModelRenderer(this, 0, 15);
        this.stem.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.stem.addBox(4.0F, -10.5F, -4.5F, 1, 16, 1, -0.2F);
        this.setRotateAngle(stem, -1.0471975511965976F, 0.0F, 0.0F);
        
        this.headBand = new ModelRenderer(this, 10, 0);
        this.headBand.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.headBand.addBox(-5.0F, -9.0F, -1.0F, 10, 4, 2, -0.5F);
        
        this.mic = new ModelRenderer(this, 0, 15);
        this.mic.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.mic.addBox(2.2F, 4.5F, -4.5F, 2, 1, 1, 0.0F);
        this.stem.addChild(this.mic);
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

        setRotateAngle(this.earR, x, y, z);
        setRotateAngle(this.earL, x, y, z);
        setRotateAngle(this.headBand, x, y, z);
    }

    @Override
    public void renderModel(ModelBiped parentModel, EntityLivingBase entityLivingBase) {
        ExosuitTexture.FREQUENCY_SHIFTER.bindTexturePart(1);
        this.earR.render(0.0625F);
        this.earL.render(0.0625F);
        this.stem.render(0.0625F);
        this.headBand.render(0.0625F);

        copyRotationAngles(parentModel.bipedHeadwear);
        setRotateAngle(this.stem, parentModel.bipedHeadwear.rotateAngleX - 1.0471975511965976F,
          parentModel.bipedHeadwear.rotateAngleY, parentModel.bipedHeadwear.rotateAngleZ);
    }
}
