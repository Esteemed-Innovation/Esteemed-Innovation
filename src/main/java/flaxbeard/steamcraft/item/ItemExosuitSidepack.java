package flaxbeard.steamcraft.item;

import java.util.HashMap;

import javax.vecmath.Vector2d;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.client.render.model.ModelExosuit;
import flaxbeard.steamcraft.item.ItemExosuitArmor.ExosuitSlot;

public class ItemExosuitSidepack extends ItemExosuitUpgrade {
	public ResourceLocation tankTexture = new ResourceLocation("steamcraft:textures/models/armor/exo_3.png");

	public ItemExosuitSidepack() {
		super(ExosuitSlot.legsHips, "","", 0);
	}


	@Override
	public boolean hasOverlay() {
		return false;
	}

	@Override
	public boolean hasModel() {
		return true;
	}
	public static HashMap<Integer,Float> lastRotation = new HashMap<Integer,Float>();

	@Override
	@SideOnly(Side.CLIENT)
	public void renderModel(ModelExosuit model, Entity entity, int armor, float size, ItemStack me) {
		if (armor == 2) {
			Minecraft.getMinecraft().renderEngine.bindTexture(tankTexture);
			ModelRenderer Holder1 = new ModelRenderer(model, 0, 6);
			ModelRenderer Holder2 = new ModelRenderer(model, 0, 6);
			
			Holder1.addBox(-8F, 14F, -0.5F, 6, 1, 1);
			model.bipedBody.addChild(Holder1);
			Holder1.render(size);

			Holder2.addBox(2F, 14F, -0.5F, 6, 1, 1);
			model.bipedBody.addChild(Holder2);
			Holder2.render(size);
			
			ModelRenderer Holder3 = new ModelRenderer(model, 0, 6);
			ModelRenderer Holder4 = new ModelRenderer(model, 0, 6);
			
			Holder3.addBox(-8F, 9F, -0.5F, 6, 1, 1);
			model.bipedBody.addChild(Holder3);
			Holder3.render(size);

			Holder4.addBox(2F, 9F, -0.5F, 6, 1, 1);
			model.bipedBody.addChild(Holder4);
			Holder4.render(size);

			
			ModelRenderer Jetpack1 = new ModelRenderer(model, 44, 0);
			ModelRenderer Jetpack2 = new ModelRenderer(model, 44, 0);
			
			Jetpack1.addBox(-2F, 0F, -2.5F, 4, 4, 5);
			model.bipedBody.addChild(Jetpack1);
			
			Jetpack2.addBox(-2F, 0F, -2.5F, 4, 4, 5);
			model.bipedBody.addChild(Jetpack2);
			
			Vector2d vector = new Vector2d(entity.motionX,entity.motionZ);
			
			GL11.glPushMatrix();
			GL11.glTranslatef(-8.0F/16.0F, 10F/16.0F, 0F/16.0F);
			if (entity instanceof EntityPlayer) {
				float targetRotation = 360.0F * ((float)(Math.atan2(vector.y, vector.x) / (2 * Math.PI)));

				if (!lastRotation.containsKey(entity.getEntityId())) {
					lastRotation.put(entity.getEntityId(),targetRotation);
				}
				float last = lastRotation.get(entity.getEntityId());
				float myRot = last + (targetRotation-last)/1000.0F;
				GL11.glRotated(-((EntityPlayer)entity).renderYawOffset, 0F, 1F, 0F);
				GL11.glRotated(myRot-90.0F, 0F, 1F, 0F);
				lastRotation.put(entity.getEntityId(),myRot);
			}
			Jetpack1.render(size);
			GL11.glPopMatrix();
		
			GL11.glPushMatrix();
			GL11.glTranslatef(8.0F/16.0F, 10F/16.0F, 0F/16.0F);
			if (entity instanceof EntityPlayer) {
				float targetRotation = 360.0F * ((float)(Math.atan2(vector.y, vector.x) / (2 * Math.PI)));

				if (!lastRotation.containsKey(entity.getEntityId())) {
					lastRotation.put(entity.getEntityId(),targetRotation);
				}
				float last = lastRotation.get(entity.getEntityId());
				float myRot = last + (targetRotation-last)/10.0F;
				GL11.glRotated(-((EntityPlayer)entity).renderYawOffset, 0F, 1F, 0F);
				GL11.glRotated(myRot-90.0F, 0F, 1F, 0F);
				lastRotation.put(entity.getEntityId(),myRot);
			}
			
			Jetpack2.render(size);
			GL11.glPopMatrix();
			
			Jetpack1 = null;
			Jetpack2 = null;
			Holder1 = null;
			Holder2 = null;
			Holder3 = null;
			Holder4 = null;
		}
	}

}
