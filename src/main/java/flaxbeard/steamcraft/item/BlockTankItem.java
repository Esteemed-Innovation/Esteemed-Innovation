package flaxbeard.steamcraft.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.api.exosuit.IExosuitTank;
import flaxbeard.steamcraft.api.exosuit.IExosuitUpgrade;
import flaxbeard.steamcraft.client.render.model.ModelExosuit;
import flaxbeard.steamcraft.client.render.model.ModelPointer;
import flaxbeard.steamcraft.item.ItemExosuitArmor.ExosuitSlot;

public class BlockTankItem extends BlockManyMetadataItem implements IExosuitTank, IExosuitUpgrade {
	private static final ResourceLocation gauge = new ResourceLocation("steamcraft:textures/models/pointer.png");
	private static final ModelPointer pointer = new ModelPointer();

	public BlockTankItem(Block p_i45328_1_) {
		super(p_i45328_1_);
	}

	@Override
	public int renderPriority() {
		return 0;
	}

	@Override
	public ExosuitSlot getSlot() {
		return ExosuitSlot.bodyTank;
	}

	@Override
	public boolean hasOverlay() {
		return false;
	}

	@Override
	public ResourceLocation getOverlay() {
		return null;
	}

	@Override
	public boolean hasModel() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderModel(ModelExosuit model, Entity par1Entity, int armor,
			float par7, ItemStack itemStack) {	
		if (armor == 1) {
			Minecraft.getMinecraft().renderEngine.bindTexture(model.tankTexture);

			float pressure = 0.0F;
			if (itemStack.getMaxDamage() != 0) {
				pressure = ((float)itemStack.getMaxDamage()-itemStack.getItemDamage())/(float)itemStack.getMaxDamage();
			}
			ModelRenderer Tank = new ModelRenderer(model, 0, 0);
			Tank.addBox(-4.0F, -1F, 2F, 8, 12, 6);
			model.bipedBody.addChild(Tank);
			Tank.render(par7);

			float px = 1.0F/16.0F;
			GL11.glPushMatrix();
			GL11.glTranslatef(0.0F, 6*px, 8*px);
			GL11.glRotatef(90F, 0, 1, 0);
			GL11.glRotatef(-95.0F, 1, 0, 0); 
			GL11.glRotatef(180F, 1, 0, 0);
			float rand = 0.0F;
			if (pressure > 0.0F) {
				rand = (float) ((Math.random()-0.5F)*5.0F);
				if (pressure >= 1.0F) {
					rand = (float) ((Math.random()*50.0F-25.0F));
				}
			}
			GL11.glRotated((Math.min(190.0F*pressure, 190.0F)+rand), 1, 0, 0);

			Minecraft.getMinecraft().renderEngine.bindTexture(gauge);
			pointer.render();
			GL11.glPopMatrix();
			Tank = null;
		}
	}

	@Override
	public void writeInfo(List list) {
	}

	@Override
	public boolean canFill(ItemStack stack) {
		return true;
	}

	@Override
	public int getStorage(ItemStack stack) {
		return 18000;
	}

}
