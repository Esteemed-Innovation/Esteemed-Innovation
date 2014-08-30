package flaxbeard.steamcraft.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelCrucible extends ModelBase {
    public ModelRenderer crucibleBox;
    public ModelRenderer wall1;
    public ModelRenderer wall2;
    public ModelRenderer wall3;
    public ModelRenderer wall4;
    public ModelRenderer support1;
    public ModelRenderer support2;
	//private static float px = (1.0F/16.0F);
    public ModelCrucible() {
    	this.crucibleBox = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
    	crucibleBox.addBox(0.0F,13.0F,0.0F, 14, 1, 14);
    	crucibleBox.setRotationPoint(-7.0F, -7.0F, -7.0F);
    	this.wall1 = (new ModelRenderer(this, 0, 15)).setTextureSize(64, 64);
    	wall1.addBox(0.0F,0.0F,0.0F, 14, 13, 1);
    	wall1.setRotationPoint(-7.0F, -7.0F, -7.0F);
    	this.wall2 = (new ModelRenderer(this, 0, 20)).setTextureSize(64, 64);
    	wall2.addBox(0.0F,0.0F,1.0F, 1, 13, 12);
    	wall2.setRotationPoint(-7.0F, -7.0F, -7.0F);
       	this.wall3 = (new ModelRenderer(this, 0, 15)).setTextureSize(64, 64);
    	wall3.addBox(0.0F,0.0F,13.0F, 14, 13, 1);
    	wall3.setRotationPoint(-7.0F, -7.0F, -7.0F);
    	this.wall4 = (new ModelRenderer(this, 0, 20)).setTextureSize(64, 64);
    	wall4.addBox(13.0F,0.0F,1.0F, 1, 13, 12);
    	wall4.setRotationPoint(-7.0F, -7.0F, -7.0F);
    	
    	this.support1 = (new ModelRenderer(this, 0, 0)).setTextureSize(16,16);
    	support1.addBox(-1.0F,6.0F,6.0F, 1, 2, 2);
    	support1.setRotationPoint(-7.0F, -7.0F, -7.0F);
    	
    	this.support2 = (new ModelRenderer(this, 0, 0)).setTextureSize(16, 16);
    	support2.addBox(14.0F,6.0F,6.0F, 1, 2, 2);
    	support2.setRotationPoint(-7.0F, -7.0F, -7.0F);
    }
    
    
    public void renderAll()
    {

        this.crucibleBox.render(0.0625F);
        this.wall1.render(0.0625F);
        this.wall2.render(0.0625F);
        this.wall3.render(0.0625F);
        this.wall4.render(0.0625F);
    }
    
    public void renderNoRotate()
    {
    	this.support1.render(0.0625F);
    	this.support2.render(0.0625F);
    }
}
