package flaxbeard.steamcraft.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

/**
 * @author SatanicSanta
 */
public class ModelSaw extends ModelBase {

    public ModelRenderer base;
    public ModelRenderer wall1;
    public ModelRenderer wall2;
    public ModelRenderer wall3;
    public ModelRenderer wall4;

    public ModelRenderer pole;
    public ModelRenderer blade;

    public ModelSaw(){
        base = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
        base.addBox(0.0F, 0.0F, 0.0F, 16, 2, 16);
        wall1 = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
        wall1.addBox(0.0F, 0.0F, 0.0F, 2, 8, 16);
        wall2 = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
        wall2.addBox(0.0F, 1.0F, 0.0F, 2, 8, 16);
        wall3 = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
        wall3.addBox(0.0F, 2.0F, 0.0F, 2, 8, 16);
        wall4 = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
        wall4.addBox(0.0F, 3.0F, 0.0F, 2, 8, 16);
    }

    public void render(){
        this.pole.render(0.0625F);
    }

    public void renderBlade(){
        this.blade.render(0.0625F);
    }

    public void renderBase(){

    }
}
