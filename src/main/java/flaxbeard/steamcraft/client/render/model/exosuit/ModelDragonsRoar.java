package flaxbeard.steamcraft.client.render.model.exosuit;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelBiped - Either Mojang or a mod author
 * Created using Tabula 4.1.1
 */
public class ModelDragonsRoar extends ModelBase {
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
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.Fin.render(f5);
        this.Face.render(f5);
        this.TopJaw.render(f5);
        this.FinL.render(f5);
        this.FinR.render(f5);
        this.CheekFinR.render(f5);
        this.Jaw.render(f5);
        this.CheekFinL.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
