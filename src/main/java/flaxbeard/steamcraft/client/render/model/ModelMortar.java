package flaxbeard.steamcraft.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelMortar extends ModelBase {
    public ModelRenderer mortarBottom;
    public ModelRenderer mortarSide1;
    public ModelRenderer mortarSide2;
    public ModelRenderer mortarBody1;
    public ModelRenderer mortarBody2;


    public ModelMortar() {
    	this.mortarBody2 = (new ModelRenderer(this, 96, 0)).setTextureSize(128, 64);
    	mortarBody2.addBox(4.0F,23.0F,4.0F, 8, 16, 8);
    	this.mortarBody1 = (new ModelRenderer(this, 88, 24)).setTextureSize(128, 64);
    	mortarBody1.addBox(3.0F,7.0F,3.0F, 10, 16, 10);
    	this.mortarSide2 = (new ModelRenderer(this, 0, 20)).setTextureSize(128, 64);
    	mortarSide2.addBox(13.0F,6.0F,2.0F, 2, 10, 12);
    	this.mortarSide1 = (new ModelRenderer(this, 0, 20)).setTextureSize(128, 64);
    	mortarSide1.addBox(1.0F,6.0F,2.0F, 2, 10, 12);
    }
    
    
    public void renderBase()
    {
    	this.mortarBottom = (new ModelRenderer(this, 0, 0)).setTextureSize(128, 64);
    	mortarBottom.addBox(0.0F,0.0F,0.0F, 16, 4, 16);
        this.mortarBottom.render(0.0625F);

    }
    
    public void render()
    {
    	this.mortarBottom = (new ModelRenderer(this, 0, 46)).setTextureSize(128, 64);
    	mortarBottom.addBox(1.0F,4.0F,1.0F, 14, 2, 14);
        this.mortarBottom.render(0.0625F);
        
        this.mortarSide1.render(0.0625F);
        this.mortarSide2.render(0.0625F);
    }
    
    public void renderCannon1()
    {
        this.mortarBody1.render(0.0625F);
    }
    
    public void renderCannon2()
    {
        this.mortarBody2.render(0.0625F);
    }
}
