package flaxbeard.steamcraft.client.render.model;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import flaxbeard.steamcraft.api.exosuit.IExosuitUpgrade;
import flaxbeard.steamcraft.api.exosuit.UtilPlates;
import flaxbeard.steamcraft.item.ItemExosuitArmor;

public class ModelExosuit extends ModelBiped {
	private ResourceLocation texture;
	private boolean hasOverlay;
	public ResourceLocation tankTexture = new ResourceLocation("steamcraft:textures/models/armor/exo_3.png");
	public ResourceLocation test = new ResourceLocation("steamcraft:textures/models/armor/joshiePenguin.png");
	private ModelRenderer penguinBody; 
	private ModelRenderer penguinHead; 
	private ModelRenderer penguinArm1; 
	private ModelRenderer penguinArm2; 
	private ModelRenderer penguinNose; 
	
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
		
		penguinBody = new ModelRenderer(this, 0, 16).setTextureSize(64, 32);
		penguinBody.addBox(-1.5F, -14F, -1.5F, 3, 5, 3);
		bipedHead.addChild(penguinBody);
		penguinArm1 = new ModelRenderer(this, 28, 16).setTextureSize(64, 32);
		penguinArm1.addBox(-2.5F, -14F, -1.0F, 1, 3, 2);
		bipedHead.addChild(penguinArm1);
		penguinArm2 = new ModelRenderer(this, 28, 16).setTextureSize(64, 32);
		penguinArm2.addBox(1.5F, -14F, -1.0F, 1, 3, 2);
		bipedHead.addChild(penguinArm2);
		penguinHead = new ModelRenderer(this, 12, 16).setTextureSize(64, 32);
		penguinHead.addBox(-2.0F, -18F, -2.0F, 4, 4, 4);
		bipedHead.addChild(penguinHead);
		penguinNose = new ModelRenderer(this, 34, 16).setTextureSize(64, 32);
		penguinNose.addBox(-0.5F, -16.5F, -4.0F, 1, 1, 2);
		bipedHead.addChild(penguinNose);
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
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
        this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);
	//	this.Jetpack2.showModel = false;
        penguinBody.showModel = false;
        penguinArm1.showModel = false;
        penguinArm2.showModel = false;
        penguinHead.showModel = false;
        penguinNose.showModel = false;

		this.bipedHead.render(par7);
		this.bipedBody.render(par7);
		this.bipedRightArm.render(par7);
		this.bipedLeftArm.render(par7);
		this.bipedRightLeg.render(par7);
		this.bipedLeftLeg.render(par7);
		this.bipedHeadwear.render(par7);
		
		if (par1Entity instanceof EntityPlayer && ((EntityPlayer)par1Entity).getCommandSenderName().equals("joshiejack")) {
	        penguinBody.showModel = true;
	        penguinArm1.showModel = true;
	        penguinArm2.showModel = true;
	        penguinHead.showModel = true;
	        penguinNose.showModel = true;
			Minecraft.getMinecraft().renderEngine.bindTexture(test);
			this.bipedHead.render(par7);
		}

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
				upgrade.renderModel(this,par1Entity,armor,par7,me);
			}
		}

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
		if (living instanceof EntityZombie) {
	        float f6 = MathHelper.sin(this.onGround * (float)Math.PI);
	        float f7 = MathHelper.sin((1.0F - (1.0F - this.onGround) * (1.0F - this.onGround)) * (float)Math.PI);
	        this.bipedRightArm.rotateAngleZ = 0.0F;
	        this.bipedLeftArm.rotateAngleZ = 0.0F;
	        this.bipedRightArm.rotateAngleY = -(0.1F - f6 * 0.6F);
	        this.bipedLeftArm.rotateAngleY = 0.1F - f6 * 0.6F;
	        this.bipedRightArm.rotateAngleX = -((float)Math.PI / 2F);
	        this.bipedLeftArm.rotateAngleX = -((float)Math.PI / 2F);
	        this.bipedRightArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
	        this.bipedLeftArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
	        this.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
	        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
	        this.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
	        this.bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
		}
    }
}
