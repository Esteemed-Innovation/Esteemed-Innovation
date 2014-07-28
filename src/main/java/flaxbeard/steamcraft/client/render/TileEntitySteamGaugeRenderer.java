package flaxbeard.steamcraft.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.client.render.model.ModelPointer;
import flaxbeard.steamcraft.tile.TileEntitySteamPipe;

public class TileEntitySteamGaugeRenderer extends TileEntitySpecialRenderer {
	private static final ModelPointer model = new ModelPointer();
	private static final ResourceLocation texture = new ResourceLocation("steamcraft:textures/models/pointer.png");


	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4,
			double var6, float var8) {
		    GL11.glPushMatrix();
			GL11.glTranslatef((float)var2+0.5F, (float)var4+0.5F, (float)var6+0.5F);
			int meta = var1.getWorldObj().getBlockMetadata(var1.xCoord, var1.yCoord, var1.zCoord);
			ForgeDirection dir = ForgeDirection.getOrientation(meta);
			GL11.glTranslatef(-7F*dir.offsetX/16.0F, -2.0F/16.0F, -7F*dir.offsetZ/16.0F);
			if (var1.getWorldObj().getTileEntity(var1.xCoord-dir.offsetX, var1.yCoord-dir.offsetY, var1.zCoord-dir.offsetZ) != null) {
				ISteamTransporter trans = (ISteamTransporter) var1.getWorldObj().getTileEntity(var1.xCoord-dir.offsetX, var1.yCoord-dir.offsetY, var1.zCoord-dir.offsetZ);
				if (trans instanceof TileEntitySteamPipe) {
					GL11.glTranslatef(-5.0F*dir.offsetX/16.0F, 0.0F, -5.0F*dir.offsetZ/16.0F);
				}
				if (meta == 2) {
					GL11.glRotated(-90.0F, 0, 1, 0);
				}
				if (meta == 3) {
					GL11.glRotated(90.0F, 0, 1, 0);
				}
				if (meta == 5) {
					GL11.glRotated(180.0F, 0, 1, 0);
				}
				GL11.glRotatef(-95.0F, 1, 0, 0);
				float rand = 0.0F;
				//System.out.println(trans.getPressure());
				if (trans.getPressure() > 0.0F) {
					rand = (float) ((Math.random()-0.5F)*5.0F);
					if (trans.getPressure() >= 1.0F) {
						rand = (float) ((Math.random()*50.0F-25.0F));
					}
				}
				GL11.glRotated((Math.min(190.0F*trans.getPressure(), 190.0F)+rand), 1, 0, 0);

				Minecraft.getMinecraft().renderEngine.bindTexture(texture);
				model.render();
				GL11.glPopMatrix();
			}
		
	}

}
