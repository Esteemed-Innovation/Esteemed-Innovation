package flaxbeard.steamcraft.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.MathHelper;

public class ModelHammer extends ModelBase {
    public ModelRenderer box1;
    public ModelRenderer box2;
    public ModelRenderer box3;
    public ModelRenderer box4;

    public ModelHammer() {
    	this.box1 = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
    	box1.addBox(6.0F,2.0F,0.0F, 4, 10, 2);
    	this.box2 = (new ModelRenderer(this, 32, 0)).setTextureSize(64, 64);
    	box2.addBox(7.0F,-1.0F,0.0F, 2, 2, 9);
     	box2.setRotationPoint(0.0F,0.0F,0.0F);
    	this.box3 = (new ModelRenderer(this, 32, 32)).setTextureSize(64, 64);
    	box3.addBox(6.0F,-3.0F,9.0F, 4, 6, 4);
     	box3.setRotationPoint(0.0F,0.0F,0.0F);
     	this.box4 = (new ModelRenderer(this, 0, 32)).setTextureSize(64, 64);
    	box4.addBox(7.5F,-1.0F,0.0F, 1, 1, 13);
     	box4.setRotationPoint(0.0F,0.0F,0.0F);
    }
    
    
    public void renderNoRotate()
    {
     	this.box1.render(0.0625F);
    }
    
    public void renderConnector(int ticks)
    {
     	this.box4.render(0.0625F);
    }
    
    public void render()
    {
     	this.box2.render(0.0625F);
     	this.box3.render(0.0625F);
    }
}
