package eiteam.esteemedinnovation.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelMortarItem extends ModelBase {
    public ModelRenderer part1;
    public ModelRenderer part2;
    public ModelRenderer part3;
    public ModelRenderer part4;
    public ModelRenderer part5;
    public ModelRenderer part6;

    public ModelRenderer minePart;

    public ModelMortarItem() {
        this.part1 = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
        part1.addBox(-1.5F, -2.0F, -1.5F, 3, 1, 3);
        this.part2 = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
        part2.addBox(-2.0F, -1.0F, -2.0F, 4, 8, 4);
        this.part3 = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
        part3.addBox(-1.5F, 7.0F, -1.5F, 3, 2, 3);
        this.part4 = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
        part4.addBox(-1.0F, 9.0F, -1.0F, 2, 3, 2);
        this.part5 = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
        part5.addBox(-0.5F, 10.0F, -2.5F, 1, 2, 5);
        this.part6 = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
        part6.addBox(-2.5F, 10.0F, -0.5F, 5, 2, 1);

        this.minePart = (new ModelRenderer(this, 0, 0)).setTextureSize(16, 16);
        minePart.addBox(-8.0F, -1.9F, -8.0F, 16, 0, 16);
    }


    public void renderAll() {
        part1.render(0.0625F);
        part2.render(0.0625F);
        part3.render(0.0625F);
        part4.render(0.0625F);
        part5.render(0.0625F);
        part6.render(0.0625F);
    }

    public void renderMinePart() {
        minePart.render(0.0625F);
    }

}
