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
public class ModelDragonsRoar extends ModelExosuitUpgrade {
    public ModelRenderer face;
    public ModelRenderer topJaw;
    public ModelRenderer jaw;
    public ModelRenderer cheekFinL;
    public ModelRenderer finL;
    public ModelRenderer finR;
    public ModelRenderer fin;
    public ModelRenderer cheekFinR;
    public ModelRenderer snoot;

    public ModelDragonsRoar() {
        this.textureWidth = 44;
        this.textureHeight = 33;
        this.fin = new ModelRenderer(this, 0, 25);
        this.fin.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.fin.addBox(-1.0F, -8.5F, -1.6F, 2, 2, 6, 0.2F);
        this.setRotateAngle(fin, 0.2617993877991494F, 0.0F, 0.0F);
        
        this.face = new ModelRenderer(this, 0, 0);
        this.face.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.face.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        
        this.topJaw = new ModelRenderer(this, 0, 16);
        this.topJaw.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.topJaw.addBox(-3.0F, -3.0F, -8.0F, 6, 3, 6, 0.0F);
        
        this.finL = new ModelRenderer(this, 16, 25);
        this.finL.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.finL.addBox(-4.0F, -7.7F, -1.8F, 1, 1, 6, 0.2F);
        this.setRotateAngle(finL, 0.2617993877991494F, 0.0F, 0.7853981633974483F);
        
        this.snoot = new ModelRenderer(this, 24, 16);
        this.snoot.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.snoot.addBox(-2.0F, -4.0F, -8.0F, 4, 2, 6, 0.0F);
        this.setRotateAngle(snoot, 0.08726646259971647F, 0.0F, 0.0F);
        
        this.finR = new ModelRenderer(this, 16, 25);
        this.finR.mirror = true;
        this.finR.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.finR.addBox(3.0F, -7.7F, -1.8F, 1, 1, 6, 0.2F);
        this.setRotateAngle(finR, 0.2617993877991494F, 0.0F, -0.7853981633974483F);
        
        this.cheekFinR = new ModelRenderer(this, 0, 25);
        this.cheekFinR.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.cheekFinR.addBox(-3.9F, 2.7F, -2.6F, 2, 2, 6, 0.2F);
        this.setRotateAngle(cheekFinR, -0.2617993877991494F, 0.0F, 1.5707963267948966F);
        
        this.jaw = new ModelRenderer(this, 0, 16);
        this.jaw.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.jaw.addBox(-3.0F, 0.0F, -7.5F, 6, 3, 6, -0.1F);
        this.setRotateAngle(jaw, -0.3490658503988659F, 0.0F, 3.141592653589793F);
        
        this.cheekFinL = new ModelRenderer(this, 0, 25);
        this.cheekFinL.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.cheekFinL.addBox(-3.9F, -4.7F, -2.6F, 2, 2, 6, 0.2F);
        this.setRotateAngle(cheekFinL, 0.2617993877991494F, 0.0F, 1.5707963267948966F);
        this.topJaw.addChild(this.snoot);
        this.topJaw.addChild(this.jaw);
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

        setRotateAngle(this.fin, x, y, z);
        setRotateAngle(this.face, x, y, z);
        setRotateAngle(this.topJaw, x, y, z);
        setRotateAngle(this.finL, x, y, z);
        setRotateAngle(this.finR, x, y, z);
        setRotateAngle(this.cheekFinL, x, y, z);
        setRotateAngle(this.cheekFinR, x, y, z);
    }

    @Override
    public void renderModel(ModelBiped parentModel, EntityLivingBase entityLivingBase) {
        ExosuitTexture.DRAGON_ROAR.bindTexturePart(1);
        float f = 0.065F;
        this.fin.render(f);
        this.face.render(f);
        this.topJaw.render(f);
        this.finL.render(f);
        this.finR.render(f);
        this.cheekFinR.render(f);
        this.cheekFinL.render(f);

        copyRotationAngles(parentModel.bipedHead);
    }
}
