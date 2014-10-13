package flaxbeard.steamcraft.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelFluidSteam extends ModelBase {
    public ModelRenderer box1;
    public ModelRenderer box2;
    public ModelRenderer box3;

    public ModelFluidSteam() {
        this.box1 = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
        box1.addBox(12.0F, 2.5F, 2.5F, 4, 11, 11);

        this.box2 = (new ModelRenderer(this, 30, 0)).setTextureSize(64, 64);
        box2.addBox(0.0F, 3.0F, 3.0F, 5, 10, 10);

        this.box3 = (new ModelRenderer(this, 0, 22)).setTextureSize(64, 64);
        box3.addBox(3.0F, 2.5F, 2.5F, 1, 11, 11);
    }


    public void renderAnchored() {

        this.box1.render(0.0625F);
    }

    public void renderSquish() {
        this.box2.render(0.0625F);

    }

    public void renderMoving() {
        this.box3.render(0.0625F);
    }
}
