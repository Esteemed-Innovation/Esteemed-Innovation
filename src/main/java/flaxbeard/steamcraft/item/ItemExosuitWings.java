package flaxbeard.steamcraft.item;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.client.render.model.ModelExosuit;
import flaxbeard.steamcraft.item.ItemExosuitArmor.ExosuitSlot;

public class ItemExosuitWings extends ItemExosuitUpgrade {
	public static HashMap<Integer,Integer> stuff = new HashMap<Integer,Integer>();

	public ResourceLocation tankTexture = new ResourceLocation("steamcraft:textures/models/armor/wings.png");

	public ItemExosuitWings() {
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
			int ticks = getTicks(entity);
			Minecraft.getMinecraft().renderEngine.bindTexture(tankTexture);
			float f = (float) (0.1F + (Math.sin(Math.toRadians(ticks*(20.0F/15.0F)*4.5F)))*0.8F);
			for (int i = 0; i<5; i++) {
				ModelRenderer Wing1 = new ModelRenderer(model, 32, 0).setTextureSize(64, 64);
				ModelRenderer Wing2 = new ModelRenderer(model, 32, 0).setTextureSize(64, 64);
				
				Wing1.addBox(0F, 0F, -0.5F, 2, 12, 1);
				Wing1.setRotationPoint(-1.0F-((5-i)), 1F-((5-i)), 6.5F);
				Wing1.rotateAngleZ = f*(float) (Math.PI/1.8F-(Math.PI*i/10F));
				Wing1.rotateAngleY = (float)(Math.PI);
				model.bipedBody.addChild(Wing1);
				
	
				Wing2.addBox(0.0F, 0F, 0F, 2, 12, 1);
				Wing2.setRotationPoint(1.0F+((5-i)), 1F-((5-i)), 6.0F);
				Wing2.rotateAngleZ = -f*(float) (Math.PI/1.8F-(Math.PI*i/10F));
				model.bipedBody.addChild(Wing2);
				
				Wing1.render(size);
				Wing2.render(size);
				Wing1 = null;
				Wing2 = null;
			}
			ModelRenderer Wing1 = new ModelRenderer(model, 0, 0).setTextureSize(64, 64);
			ModelRenderer Wing2 = new ModelRenderer(model, 0, 0).setTextureSize(64, 64);
			
			Wing1.addBox(0F, -0.4F+f/2F, -1.0F, 2, 9, 2);
			Wing1.setRotationPoint(-1.2F, 1F, 6.5F);
			Wing1.rotateAngleZ = (float) (Math.PI/(1.5F-(f/5F)));
			Wing1.rotateAngleY = (float)(Math.PI);
			model.bipedBody.addChild(Wing1);
			

			Wing2.addBox(0.0F, -0.4F+f/2F, 0F, 2, 9, 2);
			Wing2.setRotationPoint(1.2F, 1F, 5.5F);
			Wing2.rotateAngleZ = -(float) (Math.PI/(1.5F-(f/5F)));
			model.bipedBody.addChild(Wing2);
			
			Wing1.render(size);
			Wing2.render(size);
			if (!entity.onGround && entity.motionY < 0.0F && !entity.isSneaking() && entity.fallDistance > 1.4F && ticks < 15) {
				ticks++;
			}
			if ((entity.onGround || entity.motionY >= 0.0F || entity.isSneaking()) && ticks > 0) {
				ticks--;
			}
			updateTicks(entity,ticks);
			Wing1 = null;
			Wing2 = null;
		}
	}
	
	public static int getTicks(Entity entity) {
		if (!stuff.containsKey(entity.getEntityId())) {
			stuff.put(entity.getEntityId(), 0);
		}
		return stuff.get(entity.getEntityId());
	}
	
	public static void updateTicks(Entity entity, int ticks) {
		stuff.remove(entity.getEntityId());
		stuff.put(entity.getEntityId(), ticks);
	}

}
