package flaxbeard.steamcraft.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

/**
 * Created by elijahfoster-wysocki on 10/19/14.
 */
public class ModelSaw extends ModelBase {

    public ModelRenderer box0;
    public ModelRenderer box1;
    public ModelRenderer box2;
    public ModelRenderer box3;
    public ModelRenderer box4;

    public ModelRenderer pole;
    public ModelRenderer blade;

    public ModelSaw(){

    }

    public void render(){
        this.pole.render(0.0625F);
    }

    public void renderBlade(){
        this.blade.render(0.0625F);
    }

    public void renderBase(){

    }
}
