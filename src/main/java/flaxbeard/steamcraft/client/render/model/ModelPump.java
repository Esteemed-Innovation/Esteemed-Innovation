package flaxbeard.steamcraft.client.render.model;

import java.util.ArrayList;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelPump extends ModelBase {
    public ModelRenderer 
    	backLeg, //backLeg
    	backAxis, //backAxis
    	frontLeg, // frontLeg
    	glass, //glass 
    	shaft //shaft
    	
   ;
   public ModelRenderer[] screwModels = new ModelRenderer[20];
    	
    
    public ModelPump() {
    	backLeg = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	backLeg.addBox(7.0F,0.0F,-6.0F, 2, 13, 2);
        backAxis = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	backAxis.addBox(7.0F,11.0F,-4.0F, 2, 2, 4);
        frontLeg = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	frontLeg.addBox(7.0F,0.0F,16.0F, 2, 6, 2);
    	
    	
    	glass = (new ModelRenderer(this, 0, 0)).setTextureSize(64,64);
    	glass.addBox(4.0F,4.0F,-2.0F, 8, 8, 20);
    	
    	shaft = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	shaft.addBox(7.0F,7.0F,-2.0F, 2, 2, 20);
        
    	screwModels[0] = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	screwModels[0].addBox(8.0F,5.0F,-2.0F, 3, 3, 1);
    	
    	screwModels[1] = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	screwModels[1].addBox(8.0F,8.0F,-1.0F, 3, 3, 1);
        
    	screwModels[2] = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	screwModels[2].addBox(5.0F,8.0F,0.0F, 3, 3, 1);
        
    	screwModels[3] = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	screwModels[3].addBox(5.0F,5.0F,1.0F, 3, 3, 1);
        
    	screwModels[4] = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	screwModels[4].addBox(8.0F,5.0F,2.0F, 3, 3, 1);
        
    	screwModels[5] = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	screwModels[5].addBox(8.0F,8.0F,3.0F, 3, 3, 1);
        
    	screwModels[6] = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	screwModels[6].addBox(5.0F,8.0F,4.0F, 3, 3, 1);
        
    	screwModels[7] = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	screwModels[7].addBox(5.0F,5.0F,5.0F, 3, 3, 1);
        
    	screwModels[8] = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	screwModels[8].addBox(8.0F,5.0F,6.0F, 3, 3, 1);
        
    	screwModels[9] = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	screwModels[9].addBox(8.0F,8.0F,7.0F, 3, 3, 1);
        
    	screwModels[10] = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	screwModels[10].addBox(5.0F,8.0F,8.0F, 3, 3, 1);
        
    	screwModels[11] = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	screwModels[11].addBox(5.0F,5.0F,9.0F, 3, 3, 1);
        
    	screwModels[12] = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	screwModels[12].addBox(8.0F,5.0F,10.0F, 3, 3, 1);
        
    	screwModels[13] = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	screwModels[13].addBox(8.0F,8.0F,11.0F, 3, 3, 1);
        
    	screwModels[14] = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	screwModels[14].addBox(5.0F,8.0F,12.0F, 3, 3, 1);
        
    	screwModels[15] = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	screwModels[15].addBox(5.0F,5.0F,13.0F, 3, 3, 1);
        
        
    	screwModels[16] = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	screwModels[16].addBox(8.0F,5.0F,14.0F, 3, 3, 1);
        
    	screwModels[17] = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	screwModels[17].addBox(8.0F,8.0F,15.0F, 3, 3, 1);
        
    	screwModels[18] = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	screwModels[18].addBox(5.0F,8.0F,16.0F, 3, 3, 1);
        
    	screwModels[19] = (new ModelRenderer(this, 32, 32)).setTextureSize(64,64);
    	screwModels[19].addBox(5.0F,5.0F,17.0F, 3, 3, 1);
        
        
    }
    
    public void renderNoRotate()
    {
    	
    	backLeg.render(0.0625F);
        
       	backAxis.render(0.0625F);
        
    	frontLeg.render(0.0625F);
    }
   
    
    public void render()
    {
    	
    	glass.render(0.0625F);
    	
	    shaft.render(0.0625F);
	    
	    for (int i = 0; i < screwModels.length; i++){
	    	screwModels[i].render(0.0625f);
	    }
    
//    	screwModels[0].render(0.0625F);
//    	screwModels[1].render(0.0625F);
//    	screwModels[2].render(0.0625F);
//    	screwModels[3].render(0.0625F);
//        
//    	screwModels[4].render(0.0625F);
//    	screwModels[5].render(0.0625F);
//    	screwModels[6].render(0.0625F);
//    	screwModels[7].render(0.0625F);
//        
//    	screwModels[8].render(0.0625F);
//    	screwModels[9].render(0.0625F);
//    	screwModels[10].render(0.0625F);
//    	screwModels[11].render(0.0625F);
//        
//    	screwModels[12].render(0.0625F);
//    	screwModels[13].render(0.0625F);
//    	screwModels[14].render(0.0625F);
//    	screwModels[15].render(0.0625F);
//
//    	screwModels[16].render(0.0625F);
//    	screwModels[17].render(0.0625F);
//    	screwModels[18].render(0.0625F);
//    	screwModels[19].render(0.0625F);

    }
}