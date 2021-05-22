package eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.roar;

import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import eiteam.esteemedinnovation.armor.exosuit.ExosuitTexture;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.EntityLivingBase;

public class ModelDragonsRoar extends ModelExosuitUpgrade {
    private ModelRenderer Face;
    private ModelRenderer TopJaw;
    private ModelRenderer Jaw;
    private ModelRenderer CheekFinL;
    private ModelRenderer FinL;
    private ModelRenderer FinR;
    private ModelRenderer Fin;
    private ModelRenderer CheekFinR;
    private ModelRenderer Snoot;

    public ModelDragonsRoar() {
        textureWidth = 44;
        textureHeight = 33;
        Fin = new ModelRenderer(this, 0, 25);
        Fin.setRotationPoint(0.0F, 0.0F, 0.0F);
        Fin.addBox(-1.0F, -8.5F, -1.6F, 2, 2, 6, 0.2F);
        setRotateAngle(Fin, 0.2617993877991494F, 0.0F, 0.0F);

        Face = new ModelRenderer(this, 0, 0);
        Face.setRotationPoint(0.0F, 0.0F, 0.0F);
        Face.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);

        TopJaw = new ModelRenderer(this, 0, 16);
        TopJaw.setRotationPoint(0.0F, 0.0F, 0.0F);
        TopJaw.addBox(-3.0F, -3.0F, -8.0F, 6, 3, 6, 0.0F);

        FinL = new ModelRenderer(this, 16, 25);
        FinL.setRotationPoint(0.0F, 0.0F, 0.0F);
        FinL.addBox(-4.0F, -7.7F, -1.8F, 1, 1, 6, 0.2F);
        setRotateAngle(FinL, 0.2617993877991494F, 0.0F, 0.7853981633974483F);

        Snoot = new ModelRenderer(this, 24, 16);
        Snoot.setRotationPoint(0.0F, 0.0F, 0.0F);
        Snoot.addBox(-2.0F, -4.0F, -8.0F, 4, 2, 6, 0.0F);
        setRotateAngle(Snoot, 0.08726646259971647F, 0.0F, 0.0F);

        FinR = new ModelRenderer(this, 16, 25);
        FinR.mirror = true;
        FinR.setRotationPoint(0.0F, 0.0F, 0.0F);
        FinR.addBox(3.0F, -7.7F, -1.8F, 1, 1, 6, 0.2F);
        setRotateAngle(FinR, 0.2617993877991494F, 0.0F, -0.7853981633974483F);

        CheekFinR = new ModelRenderer(this, 0, 25);
        CheekFinR.setRotationPoint(0.0F, 0.0F, 0.0F);
        CheekFinR.addBox(-3.9F, 2.7F, -2.6F, 2, 2, 6, 0.2F);
        setRotateAngle(CheekFinR, -0.2617993877991494F, 0.0F, 1.5707963267948966F);

        Jaw = new ModelRenderer(this, 0, 16);
        Jaw.setRotationPoint(0.0F, 0.0F, 0.0F);
        Jaw.addBox(-3.0F, 0.0F, -7.5F, 6, 3, 6, -0.1F);
        setRotateAngle(Jaw, -0.3490658503988659F, 0.0F, 3.141592653589793F);

        CheekFinL = new ModelRenderer(this, 0, 25);
        CheekFinL.setRotationPoint(0.0F, 0.0F, 0.0F);
        CheekFinL.addBox(-3.9F, -4.7F, -2.6F, 2, 2, 6, 0.2F);
        setRotateAngle(CheekFinL, 0.2617993877991494F, 0.0F, 1.5707963267948966F);

        TopJaw.addChild(Snoot);
        TopJaw.addChild(Jaw);
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
        super.renderModel(parentModel, entityLivingBase);
        ExosuitTexture.DRAGON_ROAR.bindTexturePart(1);
        float f = 0.065F;
        Fin.render(f);
        Face.render(f);
        TopJaw.render(f);
        FinL.render(f);
        FinR.render(f);
        CheekFinR.render(f);
        CheekFinL.render(f);
    }

    @Override
    public void copyRotationAngles(ModelBiped parentModel, EntityLivingBase entityLivingBase) {
        copyRotateAngles(Fin, parentModel.bipedHead);
        copyRotateAngles(Face, parentModel.bipedHead);
        copyRotateAngles(TopJaw, parentModel.bipedHead);
        copyRotateAngles(FinL, parentModel.bipedHead);
        copyRotateAngles(FinR, parentModel.bipedHead);
        copyRotateAngles(CheekFinL, parentModel.bipedHead);
        copyRotateAngles(CheekFinR, parentModel.bipedHead);
    }
}
