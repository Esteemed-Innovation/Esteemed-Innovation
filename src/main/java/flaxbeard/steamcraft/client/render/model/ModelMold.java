package flaxbeard.steamcraft.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelMold extends ModelBase {
    public ModelRenderer boxBottom;
    public ModelRenderer boxTop;

    //private static float px = (1.0F/16.0F);
    public ModelMold() {
        this.boxBottom = (new ModelRenderer(this, 0, 16)).setTextureSize(64, 64);
        boxBottom.addBox(1.0F, 11.0F, 1.0F, 12, 4, 12);
        boxBottom.setRotationPoint(-7.0F, -7.0F, -7.0F);
        this.boxTop = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
        boxTop.addBox(1.0F, 7.0F, 1.0F, 12, 4, 12);
        boxTop.setRotationPoint(-7.0F, -11.0F, -1.0F);
    }


    public void renderBottom() {

        this.boxBottom.render(0.0625F);
    }

    public void renderTop() {

        this.boxTop.render(0.0625F);
    }
}
