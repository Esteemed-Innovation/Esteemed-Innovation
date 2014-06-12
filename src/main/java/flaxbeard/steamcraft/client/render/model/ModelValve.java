package flaxbeard.steamcraft.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelValve extends ModelBase {
    public ModelRenderer box;
    public ModelValve() {
    }
    
    
    public void render()
    {
    	this.box = (new ModelRenderer(this, 1, 1)).setTextureSize(16,16);
    	box.addBox(-1.5F,-3.5F,-3.5F, 1, 1, 7);
        this.box.render(0.0625F);
        
    	this.box = (new ModelRenderer(this, 1, 1)).setTextureSize(16,16);
    	box.addBox(-1.5F,2.5F,-3.5F, 1, 1, 7);
        this.box.render(0.0625F);
        
    	this.box = (new ModelRenderer(this, 1, 1)).setTextureSize(16,16);
    	box.addBox(-1.5F,-2.5F,-3.5F, 1, 5, 1);
        this.box.render(0.0625F);
        
       	this.box = (new ModelRenderer(this, 1, 1)).setTextureSize(16,16);
    	box.addBox(-1.5F,-2.5F,2.5F, 1, 5, 1);
        this.box.render(0.0625F);
        
    	this.box = (new ModelRenderer(this, 1, 1)).setTextureSize(16,16);
    	box.addBox(-1.5F,-0.5F,-2.5F, 1, 1, 5);
        this.box.render(0.0625F);
        
    	this.box = (new ModelRenderer(this, 1, 1)).setTextureSize(16,16);
    	box.addBox(-1.5F,-2.5F,-0.5F, 1, 5, 1);
        this.box.render(0.0625F);

    	this.box = (new ModelRenderer(this, 1, 1)).setTextureSize(16,16);
    	box.addBox(-1.5F,-1.5F,-1.5F, 2, 3, 3);
        this.box.render(0.0625F);
    }
}
