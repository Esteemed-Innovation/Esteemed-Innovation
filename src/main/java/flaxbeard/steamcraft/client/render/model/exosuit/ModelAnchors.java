package flaxbeard.steamcraft.client.render.model.exosuit;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelBiped - Either Mojang or a mod author
 * Created using Tabula 4.1.1
 */
public class ModelAnchors extends ModelBase {
    public ModelRenderer AnchorL;
    public ModelRenderer AnchorR;

    public ModelAnchors() {
        this.textureWidth = 16;
        this.textureHeight = 8;
        
        this.AnchorL = new ModelRenderer(this, 0, 0);
        this.AnchorL.mirror = true;
        this.AnchorL.setRotationPoint(1.9F, 12.0F, 0.1F);
        this.AnchorL.addBox(-2.0F, 7.8F, -2.0F, 4, 4, 4, 0.25F);
        
        this.AnchorR = new ModelRenderer(this, 0, 0);
        this.AnchorR.setRotationPoint(-1.9F, 12.0F, 0.1F);
        this.AnchorR.addBox(-2.0F, 7.8F, -2.0F, 4, 4, 4, 0.25F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.AnchorL.render(f5);
        this.AnchorR.render(f5);
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
