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



    public ModelThumper() {
    	this.mainPole = (new ModelRenderer(this, 96, 0)).setTextureSize(128, 64);
    	mainPole.addBox(6.0F,0.0F,5.0F, 4, 64, 6);
    	this.pole1 = (new ModelRenderer(this, 96, 0)).setTextureSize(128, 64);
    	pole1.addBox(1.0F,0.0F,7.0F, 2, 64, 2);
    	this.connector1 = (new ModelRenderer(this, 96, 0)).setTextureSize(128, 64);
    	connector1.addBox(3.0F,62.0F,7.0F, 3, 2, 2);
    	this.pole2 = (new ModelRenderer(this, 96, 0)).setTextureSize(128, 64);
    	pole2.addBox(13.0F,0.0F,7.0F, 2, 64, 2);
    	this.connector2 = (new ModelRenderer(this, 96, 0)).setTextureSize(128, 64);
    	connector2.addBox(10.0F,62.0F,7.0F, 3, 2, 2);
    	this.pole3 = (new ModelRenderer(this, 96, 0)).setTextureSize(128, 64);
    	pole3.addBox(7.0F,0.0F,0.0F, 2, 64, 2);
    	this.connector3 = (new ModelRenderer(this, 96, 0)).setTextureSize(128, 64);
    	connector3.addBox(7.0F,62.0F,2.0F, 2, 2, 3);
    	this.pole4 = (new ModelRenderer(this, 96, 0)).setTextureSize(128, 64);
    	pole4.addBox(7.0F,0.0F,14.0F, 2, 64, 2);
    	this.connector4 = (new ModelRenderer(this, 96, 0)).setTextureSize(128, 64);
    	connector4.addBox(7.0F,62.0F,11.0F, 2, 2, 3);
    	this.thumper = (new ModelRenderer(this, 96, 0)).setTextureSize(128, 64);
    	thumper.addBox(3.0F,0.0F,2.0F, 10, 14, 12);
    }
    
    
    
    public void render()
    {
        this.mainPole.render(0.0625F);
        this.pole1.render(0.0625F);
        this.connector1.render(0.0625F);
        this.pole2.render(0.0625F);
        this.connector2.render(0.0625F);
        this.pole3.render(0.0625F);
        this.connector3.render(0.0625F);
        this.pole4.render(0.0625F);
        this.connector4.render(0.0625F);
    }
    
    public void renderThumper()
    {
        this.thumper.render(0.0625F);
    }
}
