package flaxbeard.steamcraft.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.client.render.model.ModelExosuit;
import flaxbeard.steamcraft.item.ItemExosuitArmor.ExosuitSlot;

public class ItemExosuitJetpack extends ItemExosuitUpgrade {
	public ResourceLocation tankTexture = new ResourceLocation("steamcraft:textures/models/armor/exo_3.png");

	public ItemExosuitJetpack() {
		super(ExosuitSlot.bodyFront, "","", 0);
	}


	@Override
	public boolean hasOverlay() {
		return false;
	}

	@Override
	public boolean hasModel() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderModel(ModelExosuit model, Entity entity, int armor, float size, ItemStack me) {
		if (armor == 1) {
			Minecraft.getMinecraft().renderEngine.bindTexture(tankTexture);
			ModelRenderer Jetpack1 = new ModelRenderer(model, 28, 0);
			ModelRenderer Jetpack2 = new ModelRenderer(model, 28, 0);
			
			Jetpack1.addBox(-7.0F, -2F, 3F, 4, 14, 4);
			model.bipedBody.addChild(Jetpack1);
			
			Jetpack2.addBox(3.0F, -2F, 3F, 4, 14, 4);
			model.bipedBody.addChild(Jetpack2);
			
			Jetpack1.render(size);
			Jetpack2.render(size);
			Jetpack1 = null;
			Jetpack2 = null;
		}
	}

}
