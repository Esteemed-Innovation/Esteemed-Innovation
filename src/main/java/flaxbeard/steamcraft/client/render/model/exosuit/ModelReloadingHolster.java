package flaxbeard.steamcraft.client.render.model.exosuit;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelBiped - Either Mojang or a mod author
 * Created using Tabula 4.1.1
 */
public class ModelReloadingHolster extends ModelBase {
    public ModelRenderer Holster;

    public ModelReloadingHolster() {
        this.textureWidth = 40;
        this.textureHeight = 11;
        this.Holster = new ModelRenderer(this, 0, 0);
        this.Holster.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Holster.addBox(-5.01F, 8.5F, -4.0F, 1, 5, 4, 0.0F);
        this.setRotateAngle(Holster, 0.2617993877991494F, 0.0F, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.Holster.render(f5);
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
