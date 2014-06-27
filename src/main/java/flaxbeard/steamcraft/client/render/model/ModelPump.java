package flaxbeard.steamcraft.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelPump extends ModelBase {
    public ModelRenderer 
    	box, box1, box2, box3, // renderNoRotate
    	box4, box5, box6, box7, box8, box9, box10, box11, box12, box13, box14, box15, box16, box17, box18, box19, box20, box21, box22, box23, box24, box25;
    
    public ModelPump() {
    	this.box1 = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box1.addBox(7.0F,0.0F,-6.0F, 2, 13, 2);
        this.box2 = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box2.addBox(7.0F,11.0F,-4.0F, 2, 2, 4);
        this.box3 = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box3.addBox(7.0F,0.0F,16.0F, 2, 6, 2);
    	
    	
    	this.box4 = (new ModelRenderer(this, 0, 0)).setTextureSize(64,64);
    	box4.addBox(4.0F,4.0F,-2.0F, 8, 8, 20);
    	
    	this.box5 = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box5.addBox(7.0F,7.0F,-2.0F, 2, 2, 20);
        
    	this.box6 = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box6.addBox(8.0F,5.0F,-2.0F, 3, 3, 1);
    	
    	this.box7 = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box7.addBox(8.0F,8.0F,-1.0F, 3, 3, 1);
        
    	this.box8 = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box8.addBox(5.0F,8.0F,0.0F, 3, 3, 1);
        
    	this.box9 = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box9.addBox(5.0F,5.0F,1.0F, 3, 3, 1);
        
    	this.box10 = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box10.addBox(8.0F,5.0F,2.0F, 3, 3, 1);
        
    	this.box11 = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box11.addBox(8.0F,8.0F,3.0F, 3, 3, 1);
        
    	this.box12 = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box12.addBox(5.0F,8.0F,4.0F, 3, 3, 1);
        
    	this.box13 = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box13.addBox(5.0F,5.0F,5.0F, 3, 3, 1);
        
    	this.box14 = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box14.addBox(8.0F,5.0F,6.0F, 3, 3, 1);
        
    	this.box15 = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box15.addBox(8.0F,8.0F,7.0F, 3, 3, 1);
        
    	this.box16 = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box16.addBox(5.0F,8.0F,8.0F, 3, 3, 1);
        
    	this.box17 = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box17.addBox(5.0F,5.0F,9.0F, 3, 3, 1);
        
    	this.box18 = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box18.addBox(8.0F,5.0F,10.0F, 3, 3, 1);
        
    	this.box19 = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box19.addBox(8.0F,8.0F,11.0F, 3, 3, 1);
        
    	this.box20 = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box20.addBox(5.0F,8.0F,12.0F, 3, 3, 1);
        
    	this.box21 = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box21.addBox(5.0F,5.0F,13.0F, 3, 3, 1);
        
        
    	this.box22 = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box22.addBox(8.0F,5.0F,14.0F, 3, 3, 1);
        
    	this.box23 = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box23.addBox(8.0F,8.0F,15.0F, 3, 3, 1);
        
    	this.box24 = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box24.addBox(5.0F,8.0F,16.0F, 3, 3, 1);
        
    	this.box25 = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	box25.addBox(5.0F,5.0F,17.0F, 3, 3, 1);
        
        
    }
    
    public void renderNoRotate()
    {
    	
    	this.box1.render(0.0625F);
        
       	this.box2.render(0.0625F);
        
    	this.box3.render(0.0625F);
    }
   
    
    public void render()
    {
    	
    	this.box4.render(0.0625F);
    	
	    this.box5.render(0.0625F);
        
    	this.box6.render(0.0625F);
    	this.box7.render(0.0625F);
    	this.box8.render(0.0625F);
    	this.box9.render(0.0625F);
        
    	this.box10.render(0.0625F);
    	this.box11.render(0.0625F);
    	this.box12.render(0.0625F);
    	this.box13.render(0.0625F);
        
    	this.box14.render(0.0625F);
    	this.box15.render(0.0625F);
    	this.box16.render(0.0625F);
    	this.box17.render(0.0625F);
        
    	this.box18.render(0.0625F);
    	this.box19.render(0.0625F);
    	this.box20.render(0.0625F);
    	this.box21.render(0.0625F);
        
    	this.box22.render(0.0625F);
    	this.box23.render(0.0625F);
    	this.box24.render(0.0625F);
    	this.box25.render(0.0625F);
    }
}