package eiteam.esteemedinnovation.misc;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelPointer extends ModelBase {
    private ModelRenderer box;

    public ModelPointer() {
        box = new ModelRenderer(this, 0, 0);
        box.setTextureSize(64, 64);
        box.addBox(-0.5F, -0.5F, -0.5F, 1, 4, 1);
        box.setRotationPoint(0.0F, 0.0F, 0.0F);
    }

    public void render() {
        box.render(0.0625F);
    }
}
