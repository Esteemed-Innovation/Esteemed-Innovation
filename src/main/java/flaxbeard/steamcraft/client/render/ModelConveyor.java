package flaxbeard.steamcraft.client.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelConveyor extends ModelBase {
    public ModelRenderer main;

    public ModelConveyor() {
    	this.main = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
    	main.addBox(0.0F,1.0F,0.0F, 16, 4, 16);
    }
   
    
    public void render()
    {
        this.main.render(0.0625F);
    }
}
