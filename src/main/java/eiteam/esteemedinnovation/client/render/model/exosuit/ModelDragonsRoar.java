package eiteam.esteemedinnovation.client.render.model.exosuit;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.EntityLivingBase;
import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import eiteam.esteemedinnovation.client.ExosuitTexture;

/**
 * ModelBiped - Either Mojang or a mod author
 * Created using Tabula 4.1.1
 */
public class ModelDragonsRoar extends ModelExosuitUpgrade {
    public ModelRenderer Face;
    public ModelRenderer TopJaw;
    public ModelRenderer Jaw;
    public ModelRenderer CheekFinL;
    public ModelRenderer FinL;
    public ModelRenderer FinR;
    public ModelRenderer Fin;
    public ModelRenderer CheekFinR;
    public ModelRenderer Snoot;

    public ModelDragonsRoar() {
        this.textureWidth = 44;
        this.textureHeight = 33;
        this.Fin = new ModelRenderer(this, 0, 25);
        this.Fin.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Fin.addBox(-1.0F, -8.5F, -1.6F, 2, 2, 6, 0.2F);
        this.setRotateAngle(Fin, 0.2617993877991494F, 0.0F, 0.0F);
        
        this.Face = new ModelRenderer(this, 0, 0);
        this.Face.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Face.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        
        this.TopJaw = new ModelRenderer(this, 0, 16);
        this.TopJaw.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.TopJaw.addBox(-3.0F, -3.0F, -8.0F, 6, 3, 6, 0.0F);
        
        this.FinL = new ModelRenderer(this, 16, 25);
        this.FinL.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.FinL.addBox(-4.0F, -7.7F, -1.8F, 1, 1, 6, 0.2F);
        this.setRotateAngle(FinL, 0.2617993877991494F, 0.0F, 0.7853981633974483F);
        
        this.Snoot = new ModelRenderer(this, 24, 16);
        this.Snoot.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Snoot.addBox(-2.0F, -4.0F, -8.0F, 4, 2, 6, 0.0F);
        this.setRotateAngle(Snoot, 0.08726646259971647F, 0.0F, 0.0F);
        
        this.FinR = new ModelRenderer(this, 16, 25);
        this.FinR.mirror = true;
        this.FinR.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.FinR.addBox(3.0F, -7.7F, -1.8F, 1, 1, 6, 0.2F);
        this.setRotateAngle(FinR, 0.2617993877991494F, 0.0F, -0.7853981633974483F);
        
        this.CheekFinR = new ModelRenderer(this, 0, 25);
        this.CheekFinR.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.CheekFinR.addBox(-3.9F, 2.7F, -2.6F, 2, 2, 6, 0.2F);
        this.setRotateAngle(CheekFinR, -0.2617993877991494F, 0.0F, 1.5707963267948966F);
        
        this.Jaw = new ModelRenderer(this, 0, 16);
        this.Jaw.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Jaw.addBox(-3.0F, 0.0F, -7.5F, 6, 3, 6, -0.1F);
        this.setRotateAngle(Jaw, -0.3490658503988659F, 0.0F, 3.141592653589793F);
        
        this.CheekFinL = new ModelRenderer(this, 0, 25);
        this.CheekFinL.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.CheekFinL.addBox(-3.9F, -4.7F, -2.6F, 2, 2, 6, 0.2F);
        this.setRotateAngle(CheekFinL, 0.2617993877991494F, 0.0F, 1.5707963267948966F);
        this.TopJaw.addChild(this.Snoot);
        this.TopJaw.addChild(this.Jaw);
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

        setRotateAngle(this.Fin, x, y, z);
        setRotateAngle(this.Face, x, y, z);
        setRotateAngle(this.TopJaw, x, y, z);
        setRotateAngle(this.FinL, x, y, z);
        setRotateAngle(this.FinR, x, y, z);
        setRotateAngle(this.CheekFinL, x, y, z);
        setRotateAngle(this.CheekFinR, x, y, z);
    }

    @Override
    public void renderModel(ModelBiped parentModel, EntityLivingBase entityLivingBase) {
        ExosuitTexture.DRAGON_ROAR.bindTexturePart(1);
        float f = 0.065F;
        this.Fin.render(f);
        this.Face.render(f);
        this.TopJaw.render(f);
        this.FinL.render(f);
        this.FinR.render(f);
        this.CheekFinR.render(f);
        this.CheekFinL.render(f);

        copyRotationAngles(parentModel.bipedHead);
    }
}
