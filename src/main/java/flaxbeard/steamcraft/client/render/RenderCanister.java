package flaxbeard.steamcraft.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.client.render.model.ModelCanister;
import flaxbeard.steamcraft.entity.EntityCanisterItem;

@SideOnly(Side.CLIENT)
public class RenderCanister extends Render
{
	private static final ResourceLocation texture = new ResourceLocation("steamcraft:textures/models/mortarItem.png");
	private static final ModelCanister model = new ModelCanister();

	@Override
	public void doRender(Entity var1, double x, double y, double z,
			float var8, float var9) {
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		EntityCanisterItem myItem = (EntityCanisterItem) var1;
		
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		GL11.glRotatef(myItem.randomDir, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(myItem.randomDir2, 1.0F, 0.0F, 0.0F);
	
		model.renderAll();
		GL11.glRotatef(myItem.randomDir, 0.0F, -1.0F, 0.0F);
		
		EntityItem item = new EntityItem(myItem.worldObj, 0.0F, 0.0F, 0.0F,myItem.getEntityItem());
		item.hoverStart = 0.0F;
		
		GL11.glTranslated(0, 0.85F - (4F/16F), 0);
		if (this.renderManager.options.fancyGraphics == false) {
			GL11.glScalef(1.25F, 1.25F, 1.25F);
		}
		if (this.renderManager.options.fancyGraphics == true || item.getEntityItem().getItem() instanceof ItemBlock) {
			GL11.glRotatef(Minecraft.getMinecraft().thePlayer.ticksExisted*3 % 360, 0.0F, 1.0F, 0.0F);
		}
	    RenderManager.instance.renderEntityWithPosYaw(item, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
		GL11.glPopMatrix();
		
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity var1) {
		// TODO Auto-generated method stub
		return null;
	}
    
}