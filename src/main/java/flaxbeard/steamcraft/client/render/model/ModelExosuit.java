package flaxbeard.steamcraft.client.render.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.exosuit.IExosuitUpgrade;
import flaxbeard.steamcraft.api.exosuit.UtilPlates;
import flaxbeard.steamcraft.item.ItemExosuitArmor;
import flaxbeard.steamcraft.misc.ComparatorUpgrade;

public class ModelExosuit extends ModelBiped {
	private ResourceLocation texture;
	private boolean hasOverlay;
	private static final ModelPointer model = new ModelPointer();
	public ResourceLocation tankTexture = new ResourceLocation("steamcraft:textures/models/armor/exo_3.png");
	private static final ComparatorUpgrade comparator = new ComparatorUpgrade();
	
	private int armor;
	private ItemStack me;
	public ModelExosuit(ItemStack itemStack,int armorType) {
		super(armorType == 2 || armorType == 1 ? 0.5F : 1.0F, 0, 64, 32);
		hasOverlay = false;
		armor = armorType;
		me = itemStack;
		if (itemStack.hasTagCompound()) {
			if (itemStack.stackTagCompound.hasKey("plate")) {
				hasOverlay = true;
				String key = itemStack.stackTagCompound.getString("plate");
				texture = new ResourceLocation(UtilPlates.getArmorLocationFromPlate(key, (ItemExosuitArmor) itemStack.getItem(), armorType));
			}
		}
//		Jetpack1 = new ModelRenderer(this, 28, 0);
//		Jetpack2 = new ModelRenderer(this, 28, 0);
//		if (itemStack.getItem() == SteamcraftItems.exoArmorBody && ((ItemExosuitArmor)itemStack.getItem()).getStackInSlot(itemStack, 2) != null && ((ItemExosuitArmor)itemStack.getItem()).getStackInSlot(itemStack, 2).getItem() == SteamcraftItems.jetpack) {
//			Jetpack1.addBox(-7.0F, -2F, 3F, 4, 14, 4);
//			bipedBody.addChild(Jetpack1);
//			
//			Jetpack2.addBox(3.0F, -2F, 3F, 4, 14, 4);
//			bipedBody.addChild(Jetpack2);
//		}
	}

	@Override
	public void render(Entity entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
        GL11.glPushMatrix();
        if ((entity instanceof EntityLivingBase) && ((EntityLivingBase) entity).isPotionActive(Steamcraft.semiInvisible)) {
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.15F);
	        GL11.glDepthMask(false);
	        GL11.glEnable(GL11.GL_BLEND);
	        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	        GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
        }
	    this.setRotationAngles(par2, par3, par4, par5, par6, par7, entity);
		//this.Jetpack1.showModel = false;
	//	this.Jetpack2.showModel = false;

		this.bipedHead.render(par7);
		this.bipedBody.render(par7);
		this.bipedRightArm.render(par7);
		this.bipedLeftArm.render(par7);
		this.bipedRightLeg.render(par7);
		this.bipedLeftLeg.render(par7);
		this.bipedHeadwear.render(par7);
		
		if (hasOverlay) {
			Minecraft.getMinecraft().renderEngine.bindTexture(texture);
			this.bipedHead.render(par7);
			this.bipedBody.render(par7);
			this.bipedRightArm.render(par7);
			this.bipedLeftArm.render(par7);
			this.bipedRightLeg.render(par7);
			this.bipedLeftLeg.render(par7);
			this.bipedHeadwear.render(par7);
		}
		IExosuitUpgrade[] upgrades = ((ItemExosuitArmor) me.getItem()).getUpgrades(me);
		ArrayList<IExosuitUpgrade> upgrades2 = new ArrayList<IExosuitUpgrade>(Arrays.asList(upgrades));
		Collections.sort(upgrades2, comparator);
		for (IExosuitUpgrade upgrade: upgrades2) {
			if (upgrade.hasOverlay()) {
				Minecraft.getMinecraft().renderEngine.bindTexture(upgrade.getOverlay());
				this.bipedHead.render(par7);
				this.bipedBody.render(par7);
				this.bipedRightArm.render(par7);
				this.bipedLeftArm.render(par7);
				this.bipedRightLeg.render(par7);
				this.bipedLeftLeg.render(par7);
				this.bipedHeadwear.render(par7);
			}
			if (upgrade.hasModel()) {
				upgrade.renderModel(this,entity,armor,par7,me);
			}
		}
		GL11.glPopMatrix();
	}
	

	@Override
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
    {
		EntityLivingBase living = (EntityLivingBase) par7Entity;
    	isSneak = living != null ? living.isSneaking() : false;
        if(living != null && living instanceof EntityPlayer) {
        	EntityPlayer player = (EntityPlayer) living;

            ItemStack itemstack = player.inventory.getCurrentItem();
            heldItemRight = itemstack != null ? 1 : 0;

            if (itemstack != null && player.getItemInUseCount() > 0) {
                EnumAction enumaction = itemstack.getItemUseAction();

                if (enumaction == EnumAction.block)
                   heldItemRight = 3;
                else if (enumaction == EnumAction.bow)
                   aimedBow = true;
            }
        }
    	super.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);
    }
}
