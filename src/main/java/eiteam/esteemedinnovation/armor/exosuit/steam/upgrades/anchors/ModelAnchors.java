package eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.anchors;

import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import eiteam.esteemedinnovation.armor.exosuit.ExosuitTexture;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;

public class ModelAnchors extends ModelExosuitUpgrade {
    private ModelRenderer AnchorL;
    private ModelRenderer AnchorR;

    public ModelAnchors() {
        textureWidth = 16;
        textureHeight = 8;

        AnchorL = new ModelRenderer(this, 0, 0);
        AnchorL.mirror = true;
        AnchorL.setRotationPoint(1.9F, 12.0F, 0.1F);
        AnchorL.addBox(-2.0F, 7.8F, -2.0F, 4, 4, 4, 0.25F);

        AnchorR = new ModelRenderer(this, 0, 0);
        AnchorR.setRotationPoint(-1.9F, 12.0F, 0.1F);
        AnchorR.addBox(-2.0F, 7.8F, -2.0F, 4, 4, 4, 0.25F);
    }

    @Override
    public void renderModel(ModelBiped parentModel, EntityLivingBase entityLivingBase) {
        copyRotationAngles(parentModel, entityLivingBase);
        if (entityLivingBase.isSneaking()) {
            GlStateManager.translate(0, 0, 0.25F);
        }
        ExosuitTexture.ANCHOR_HEELS.bindTexturePart(1);
        AnchorL.render(0.0625F);
        AnchorR.render(0.0625F);
    }

    @Override
    public void copyRotationAngles(ModelBiped parentModel, EntityLivingBase entityLivingBase) {
        copyRotateAngles(AnchorL, parentModel.bipedLeftLeg);
        copyRotateAngles(AnchorR, parentModel.bipedRightLeg);
    }
}
