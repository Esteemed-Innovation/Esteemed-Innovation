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
public class ModelAnchors extends ModelExosuitUpgrade {
    public ModelRenderer anchorL;
    public ModelRenderer anchorR;

    public ModelAnchors() {
        this.textureWidth = 16;
        this.textureHeight = 8;
        
        this.anchorL = new ModelRenderer(this, 0, 0);
        this.anchorL.mirror = true;
        this.anchorL.setRotationPoint(1.9F, 12.0F, 0.1F);
        this.anchorL.addBox(-2.0F, 7.8F, -2.0F, 4, 4, 4, 0.25F);
        
        this.anchorR = new ModelRenderer(this, 0, 0);
        this.anchorR.setRotationPoint(-1.9F, 12.0F, 0.1F);
        this.anchorR.addBox(-2.0F, 7.8F, -2.0F, 4, 4, 4, 0.25F);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public void copyRotationAngles(ModelRenderer child, ModelRenderer parent) {
        setRotateAngle(child, parent.rotateAngleX, parent.rotateAngleY, parent.rotateAngleZ);
    }

    @Override
    public void renderModel(ModelBiped parentModel, EntityLivingBase entityLivingBase) {
        ExosuitTexture.ANCHOR_HEELS.bindTexturePart(1);
        this.anchorL.render(0.0625F);
        this.anchorR.render(0.0625F);
        copyRotationAngles(this.anchorL, parentModel.bipedLeftLeg);
        copyRotationAngles(this.anchorR, parentModel.bipedRightLeg);
    }
}
