package eiteam.esteemedinnovation.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelCanister extends ModelBase {
    public ModelRenderer part1;
    public ModelRenderer part2;
    public ModelRenderer part3;
    public ModelRenderer part4;


    public ModelCanister() {
        this.part1 = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
        part1.addBox(-1.5F, -2.0F, -1.5F, 3, 1, 3);
        this.part2 = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
        part2.addBox(-2.0F, -1.0F, -2.0F, 4, 8, 4);
        this.part3 = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
        part3.addBox(-1.5F, 7.0F, -1.5F, 3, 1, 3);
    }


    public void renderAll() {

        part1.render(0.0625F);
        part2.render(0.0625F);
        part3.render(0.0625F);
    }


}
