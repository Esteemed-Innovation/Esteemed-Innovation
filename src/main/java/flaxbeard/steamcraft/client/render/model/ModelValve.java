package flaxbeard.steamcraft.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelValve extends ModelBase {
    public ModelRenderer box1;
    public ModelRenderer box2;
    public ModelRenderer box3;
    public ModelRenderer box4;
    public ModelRenderer box5;
    public ModelRenderer box6;
    public ModelRenderer box7;

    public ModelValve() {
        this.box1 = (new ModelRenderer(this, 1, 1)).setTextureSize(16, 16);
        box1.addBox(-1.5F, -3.5F, -3.5F, 1, 1, 7);

        this.box2 = (new ModelRenderer(this, 1, 1)).setTextureSize(16, 16);
        box2.addBox(-1.5F, 2.5F, -3.5F, 1, 1, 7);

        this.box3 = (new ModelRenderer(this, 1, 1)).setTextureSize(16, 16);
        box3.addBox(-1.5F, -2.5F, -3.5F, 1, 5, 1);

        this.box4 = (new ModelRenderer(this, 1, 1)).setTextureSize(16, 16);
        box4.addBox(-1.5F, -2.5F, 2.5F, 1, 5, 1);

        this.box5 = (new ModelRenderer(this, 1, 1)).setTextureSize(16, 16);
        box5.addBox(-1.5F, -0.5F, -2.5F, 1, 1, 5);

        this.box6 = (new ModelRenderer(this, 1, 1)).setTextureSize(16, 16);
        box6.addBox(-1.5F, -2.5F, -0.5F, 1, 5, 1);

        this.box7 = (new ModelRenderer(this, 1, 1)).setTextureSize(16, 16);
        box7.addBox(-1.5F, -1.5F, -1.5F, 2, 3, 3);
    }


    public void render() {
        this.box1.render(0.0625F);
        this.box2.render(0.0625F);
        this.box3.render(0.0625F);
        this.box4.render(0.0625F);
        this.box5.render(0.0625F);
        this.box6.render(0.0625F);
        this.box7.render(0.0625F);
    }
}
