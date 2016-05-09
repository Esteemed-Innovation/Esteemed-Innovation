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
public class ModelReloadingHolster extends ModelExosuitUpgrade {
    public ModelRenderer holster;

    public ModelReloadingHolster() {
        this.textureWidth = 40;
        this.textureHeight = 11;
        this.holster = new ModelRenderer(this, 0, 0);
        this.holster.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.holster.addBox(-5.01F, 8.5F, -4.0F, 1, 5, 4, 0.0F);
        this.setRotateAngle(holster, 0.2617993877991494F, 0.0F, 0.0F);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void renderModel(ModelBiped parentModel, EntityLivingBase entityLivingBase) {
        ExosuitTexture.RELOADING_HOLSTER.bindTexturePart(1);
        this.holster.render(0.0625F);
    }
}
