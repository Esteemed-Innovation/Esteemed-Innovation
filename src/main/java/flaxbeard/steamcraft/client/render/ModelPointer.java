package flaxbeard.steamcraft.client.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelPointer extends ModelBase {
    public ModelRenderer box;
    public ModelPointer() {
    	this.box = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
    	box.addBox(-0.5F,-0.5F,-0.5F, 1, 4, 1);
    	box.setRotationPoint(0.0F, 0.0F, 0.0F);
    }
    
    
    public void render()
    {
    	
        this.box.render(0.0625F);
    }
}
