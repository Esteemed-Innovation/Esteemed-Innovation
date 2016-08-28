package eiteam.esteemedinnovation.client.render.model.exosuit;

import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import eiteam.esteemedinnovation.client.ExosuitTexture;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.EntityLivingBase;

/**
 * @author dmillerw
 */
public class ModelJetpack extends ModelExosuitUpgrade {

    public ModelRenderer jetpack1;
    public ModelRenderer jetpack2;

    public ModelJetpack() {
        jetpack1 = new ModelRenderer(this, 28, 0);
        jetpack2 = new ModelRenderer(this, 28, 0);
        jetpack1.addBox(-7.0F, -2F, 3F, 4, 14, 4);
        jetpack1.addBox(3.0F, -2F, 3F, 4, 14, 4);
    }

    @Override
    public void renderModel(ModelBiped parentModel, EntityLivingBase entityLivingBase) {
        ExosuitTexture.TANK.bindTexturePart(1);

        jetpack1.render(0.0625F);
        jetpack2.render(0.0625F);
    }
}
