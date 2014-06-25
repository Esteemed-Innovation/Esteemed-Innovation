package flaxbeard.steamcraft.client.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderHorse;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.util.ResourceLocation;

public class RenderSteamHorse extends RenderHorse {

    private static final ResourceLocation textures = new ResourceLocation("steamcraft:textures/models/steamHorse.png");

    
	public RenderSteamHorse(ModelBase par1ModelBase, float par2) {
		super(par1ModelBase, par2);
	}

	protected ResourceLocation getEntityTexture(EntityHorse par1EntityHorse)
    {
        return textures;
    }
}
