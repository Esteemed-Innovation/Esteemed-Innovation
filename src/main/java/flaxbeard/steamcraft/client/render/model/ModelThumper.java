package flaxbeard.steamcraft.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelThumper extends ModelBase {
    public ModelRenderer mainPole;
    public ModelRenderer pole1;
    public ModelRenderer pole2;
    public ModelRenderer pole3;
    public ModelRenderer pole4;
    public ModelRenderer connector1;
    public ModelRenderer connector2;
    public ModelRenderer connector3;
    public ModelRenderer connector4;
    public ModelRenderer thumper;
    public ModelRenderer base1;
    public ModelRenderer base2;
    public ModelRenderer base3;
    public ModelRenderer base4;


    public ModelThumper() {
        this.mainPole = (new ModelRenderer(this, 0, 0)).setTextureSize(128, 128);
        mainPole.addBox(6.0F, 0.0F, 5.0F, 4, 64, 6);
        this.pole1 = (new ModelRenderer(this, 24, 0)).setTextureSize(128, 128);
        pole1.addBox(1.0F, 0.0F, 7.0F, 2, 64, 2);
        this.connector1 = (new ModelRenderer(this, 0, 0)).setTextureSize(128, 128);
        connector1.addBox(3.0F, 62.0F, 7.0F, 3, 2, 2);
        this.pole2 = (new ModelRenderer(this, 24, 0)).setTextureSize(128, 128);
        pole2.addBox(13.0F, 0.0F, 7.0F, 2, 64, 2);
        this.connector2 = (new ModelRenderer(this, 0, 0)).setTextureSize(128, 128);
        connector2.addBox(10.0F, 62.0F, 7.0F, 3, 2, 2);
        this.pole3 = (new ModelRenderer(this, 24, 0)).setTextureSize(128, 128);
        pole3.addBox(7.0F, 14.0F, 0.0F, 2, 50, 2);
        this.connector3 = (new ModelRenderer(this, 0, 0)).setTextureSize(128, 128);
        connector3.addBox(7.0F, 62.0F, 2.0F, 2, 2, 3);
        this.pole4 = (new ModelRenderer(this, 24, 0)).setTextureSize(128, 128);
        pole4.addBox(7.0F, 14.0F, 14.0F, 2, 50, 2);
        this.connector4 = (new ModelRenderer(this, 0, 0)).setTextureSize(128, 128);
        connector4.addBox(7.0F, 62.0F, 11.0F, 2, 2, 3);
        this.thumper = (new ModelRenderer(this, 36, 0)).setTextureSize(128, 128);
        thumper.addBox(3.0F, 0.0F, 2.0F, 10, 14, 12);

        this.base1 = (new ModelRenderer(this, 88, 64)).setTextureSize(128, 128);
        base1.addBox(0.0F, 0.0F, 0.0F, 2, 14, 16);
        this.base2 = (new ModelRenderer(this, 88, 0)).setTextureSize(128, 128);
        base2.addBox(14.0F, 0.0F, 0.0F, 2, 14, 16);
        this.base3 = (new ModelRenderer(this, 88, 36)).setTextureSize(128, 128);
        base3.addBox(2.0F, 0.0F, 0.0F, 12, 14, 2);
        this.base4 = (new ModelRenderer(this, 88, 36)).setTextureSize(128, 128);
        base4.addBox(2.0F, 0.0F, 14.0F, 12, 14, 2);
    }


    public void render() {

        this.mainPole.render(0.0625F);
        this.pole1.render(0.0625F);
        this.connector1.render(0.0625F);
        this.pole2.render(0.0625F);
        this.connector2.render(0.0625F);
        this.pole3.render(0.0625F);
        this.connector3.render(0.0625F);
        this.pole4.render(0.0625F);
        this.connector4.render(0.0625F);

        this.base1.render(0.0625F);
        this.base2.render(0.0625F);
        this.base3.render(0.0625F);
        this.base4.render(0.0625F);

    }

    public void renderThumper() {
        this.thumper.render(0.0625F);
    }
}
