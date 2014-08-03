package flaxbeard.steamcraft.integration;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.api.exosuit.IExosuitTank;
import flaxbeard.steamcraft.api.exosuit.IExosuitUpgrade;
import flaxbeard.steamcraft.client.render.model.ModelExosuit;
import flaxbeard.steamcraft.item.ItemExosuitArmor;
import flaxbeard.steamcraft.item.ItemExosuitArmor.ExosuitSlot;

public class ItemThaumcraftPowerSource extends Item implements IExosuitUpgrade, IExosuitTank {
	private static final ResourceLocation texture = new ResourceLocation("steamcraft:textures/models/armor/thaumTank.png");

	@Override
	public boolean canFill(ItemStack stack) {
		return false;
	}

	@Override
	public int getStorage(ItemStack stack) {
		ItemStack stack2 = ((ItemExosuitArmor)stack.getItem()).getStackInSlot(stack, 4);
//		if (stack2.hasTagCompound()) {
//			if (stack2.stackTagCompound.hasKey("item")) {
//				ItemStack wand = ItemStack.loadItemStackFromNBT(stack2.stackTagCompound.getCompoundTag("item"));
//				if (wand != null && wand.getItem() instanceof ItemWandCasting) {
//					ItemWandCasting wandItem = (ItemWandCasting) wand.getItem();
//					return wandItem.getRod(wand).getCapacity()*10;
//				}
//			}
//		}
		return 0;
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
//		if (armor == 1) {
//			Minecraft.getMinecraft().renderEngine.bindTexture(texture);
//
//			ModelRenderer Tank = new ModelRenderer(model, 0, 0);
//			Tank.addBox(-4F, -3F, 2F, 8, 16, 6);
//			model.bipedBody.addChild(Tank);
//			Tank.render(par7);
//			
//			ModelRenderer Bar = new ModelRenderer(model, 32, 0);
//			Bar.addBox(-1F, -3F, 8F, 2, 2, 3);
//			model.bipedBody.addChild(Bar);
//			Bar.render(par7);
//			
//			Bar = new ModelRenderer(model, 32, 0);
//			Bar.addBox(-1F, 11F, 8F, 2, 2, 3);
//			model.bipedBody.addChild(Bar);
//			Bar.render(par7);
//			
//			ItemStack stack = ((ItemExosuitArmor)itemStack.getItem()).getStackInSlot(itemStack, 4);
//			if (stack.hasTagCompound()) {
//				if (stack.stackTagCompound.hasKey("item")) {
//					ItemStack wand = ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("item"));
//					if (wand != null && wand.getItem() instanceof ItemWandCasting) {
//						 GL11.glPushMatrix();
//						 GL11.glTranslatef(0.0F, 1.0F/32.0F , 10.0F/16.0F);
//						 GL11.glRotatef(Minecraft.getMinecraft().thePlayer.ticksExisted*5.0F, 0.0F, 1.0F, 0.0F);
//						 ItemStack is = wand.copy();
//						 is.stackSize = 1;
//						 EntityItem item = new EntityItem(par1Entity.worldObj, 0.0F, 0.0F, 0.0F,is);
//						 item.hoverStart = 0.0F;
//						 RenderManager.instance.renderEntityWithPosYaw(item, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
////					     GL11.glEnable(GL11.GL_BLEND);
////
////						 GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
////						 RenderManager.instance.renderEntityWithPosYaw(item, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
//						 GL11.glPopMatrix();
//					}
//				}
//			}
//		   
//		
//		}
			
	}

	@Override
	public void writeInfo(List list) {
	}

}
