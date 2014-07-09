package flaxbeard.steamcraft.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelFan extends ModelBase {
    public ModelRenderer box;
    public ModelRenderer pole;

    public ModelRenderer blade1;
    
    public ModelFan() {
    	this.box = (new ModelRenderer(this, 0, 0)).setTextureSize(64,64);
    	box.addBox(0.0F,0.0F,0.0F, 6, 16, 16);
    	this.pole = (new ModelRenderer(this, 0, 32)).setTextureSize(64, 64);
    	pole.addBox(0.9F,6.5F,6.5F, 5, 3, 3);
    	this.blade1 = (new ModelRenderer(this, 17, 33)).setTextureSize(64, 64);
    	blade1.addBox(1.5F,2.5F,6.5F, 1, 4, 3);
    }
    
    public void render()
    {
        this.pole.render(0.0625F);
    }
    
    public void renderBlade()
    {
        this.blade1.render(0.0625F);
    }
    
    public void renderBase()
    {
        this.box.render(0.0625F);
    }
}
