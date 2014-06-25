package flaxbeard.steamcraft.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelPump extends ModelBase {
    public ModelRenderer box;
    
    public ModelPump() {
    
    }
    
    public void renderNoRotate()
    {
    	this.box = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box.addBox(7.0F,0.0F,-6.0F, 2, 13, 2);
        this.box.render(0.0625F);
        
       	this.box = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box.addBox(7.0F,11.0F,-4.0F, 2, 2, 4);
        this.box.render(0.0625F);
        
    	this.box = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box.addBox(7.0F,0.0F,16.0F, 2, 6, 2);
        this.box.render(0.0625F);
    }
   
    
    public void render()
    {
    	this.box = (new ModelRenderer(this, 0, 0)).setTextureSize(64,64);
    	box.addBox(4.0F,4.0F,-2.0F, 8, 8, 20);
        this.box.render(0.0625F);
    	
    	this.box = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box.addBox(7.0F,7.0F,-2.0F, 2, 2, 20);
        this.box.render(0.0625F);
        
    	this.box = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box.addBox(8.0F,5.0F,-2.0F, 3, 3, 1);
        this.box.render(0.0625F);
    	this.box = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box.addBox(8.0F,8.0F,-1.0F, 3, 3, 1);
        this.box.render(0.0625F);
    	this.box = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box.addBox(5.0F,8.0F,0.0F, 3, 3, 1);
        this.box.render(0.0625F);
    	this.box = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box.addBox(5.0F,5.0F,1.0F, 3, 3, 1);
        this.box.render(0.0625F);
        
    	this.box = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box.addBox(8.0F,5.0F,2.0F, 3, 3, 1);
        this.box.render(0.0625F);
    	this.box = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box.addBox(8.0F,8.0F,3.0F, 3, 3, 1);
        this.box.render(0.0625F);
    	this.box = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box.addBox(5.0F,8.0F,4.0F, 3, 3, 1);
        this.box.render(0.0625F);
    	this.box = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box.addBox(5.0F,5.0F,5.0F, 3, 3, 1);
        this.box.render(0.0625F);
        
    	this.box = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box.addBox(8.0F,5.0F,6.0F, 3, 3, 1);
        this.box.render(0.0625F);
    	this.box = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box.addBox(8.0F,8.0F,7.0F, 3, 3, 1);
        this.box.render(0.0625F);
    	this.box = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box.addBox(5.0F,8.0F,8.0F, 3, 3, 1);
        this.box.render(0.0625F);
    	this.box = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box.addBox(5.0F,5.0F,9.0F, 3, 3, 1);
        this.box.render(0.0625F);
        
    	this.box = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box.addBox(8.0F,5.0F,10.0F, 3, 3, 1);
        this.box.render(0.0625F);
    	this.box = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box.addBox(8.0F,8.0F,11.0F, 3, 3, 1);
        this.box.render(0.0625F);
    	this.box = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box.addBox(5.0F,8.0F,12.0F, 3, 3, 1);
        this.box.render(0.0625F);
    	this.box = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box.addBox(5.0F,5.0F,13.0F, 3, 3, 1);
        this.box.render(0.0625F);
        
    	this.box = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box.addBox(8.0F,5.0F,14.0F, 3, 3, 1);
        this.box.render(0.0625F);
    	this.box = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box.addBox(8.0F,8.0F,15.0F, 3, 3, 1);
        this.box.render(0.0625F);
    	this.box = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box.addBox(5.0F,8.0F,16.0F, 3, 3, 1);
        this.box.render(0.0625F);
    	this.box = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box.addBox(5.0F,5.0F,17.0F, 3, 3, 1);
        this.box.render(0.0625F);
    }
}
