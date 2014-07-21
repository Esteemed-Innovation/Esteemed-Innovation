package flaxbeard.steamcraft.client.render.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
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
	public ResourceLocation hornTexture = new ResourceLocation("steamcraft:textures/models/armor/horns.png");
	private static final ComparatorUpgrade comparator = new ComparatorUpgrade();
	private ModelRenderer[] horn1;
	private ModelRenderer[] horn2;
	private ModelRenderer[] horn3;

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
		if (armor == 0) {
			if (((ItemExosuitArmor) me.getItem()).hasPlates(me) && UtilPlates.getPlate(me.stackTagCompound.getString("plate")).getIdentifier() == "Yeti") {
			    horn1 = addPairHorns(-8.0F, 35.0F);
			    horn2 = addPairHorns(-6.0F, 15.0F);
			    horn3 = addPairHorns(-4.0F, -5.0F);

			}
		}
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
		if (armor == 0) {
			if (((ItemExosuitArmor) me.getItem()).hasPlates(me) && UtilPlates.getPlate(me.stackTagCompound.getString("plate")).getIdentifier() == "Yeti") {
				for (ModelRenderer horn : horn1) {
					horn.showModel = false;
				}
				for (ModelRenderer horn : horn2) {
					horn.showModel = false;
				}
				for (ModelRenderer horn : horn3) {
					horn.showModel = false;
				}
			}
	    }
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
		Minecraft.getMinecraft().renderEngine.bindTexture(hornTexture);
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
		
		
		if (armor == 0) {
			if (((ItemExosuitArmor) me.getItem()).hasPlates(me) && UtilPlates.getPlate(me.stackTagCompound.getString("plate")).getIdentifier() == "Yeti") {
				Minecraft.getMinecraft().renderEngine.bindTexture(hornTexture);
				
				for (ModelRenderer horn : horn1) {
					horn.showModel = true;
					//horn.render(par7);
				}
				for (ModelRenderer horn : horn2) {
					horn.showModel = true;
					//horn.render(par7);
				}
				for (ModelRenderer horn : horn3) {
					horn.showModel = true;
					//horn.render(par7);
				}
				this.bipedHead.render(par7);
			}
		}
		GL11.glPopMatrix();
	}
	
	private ModelRenderer[] addPairHorns(float height, float zangle)
	{
		ModelRenderer[] hornParts = new ModelRenderer[4];
		ModelRenderer horn1a = new ModelRenderer(this, 0, 19);
	    horn1a.addBox(-3.0F, -1.5F, -1.5F, 3, 3, 3);
	    horn1a.setRotationPoint(-4.5F, height, -1.0F);
	    horn1a.rotateAngleY = -0.5235988F;
	    horn1a.rotateAngleZ = (zangle / 57.295776F);
	    this.bipedHead.addChild(horn1a);
	    hornParts[0] = horn1a;
	    
	    ModelRenderer horn1b = new ModelRenderer(this, 0, 26);
	    horn1b.addBox(-4.0F, -1.0F, -1.0F, 5, 2, 2);
	    horn1b.setRotationPoint(-3.0F, 0.0F, 0.0F);
	    horn1b.rotateAngleY = -0.3490659F;
	    horn1b.rotateAngleZ = (zangle / 57.295776F);
	    horn1a.addChild(horn1b);
	    hornParts[1] = horn1b;




	    ModelRenderer horn2a = new ModelRenderer(this, 0, 19);
	    horn2a.addBox(0.0F, -1.5F, -1.5F, 3, 3, 3);
	    horn2a.setRotationPoint(4.5F, height, -1.0F);
	    horn2a.rotateAngleY = 0.5235988F;
	    horn2a.rotateAngleZ = (-zangle / 57.295776F);
	    this.bipedHead.addChild(horn2a);
	    hornParts[2] = horn2a;

	    ModelRenderer horn2b = new ModelRenderer(this, 0, 26);
	    horn2b.addBox(-1.0F, -1.0F, -1.0F, 5, 2, 2);
	    horn2b.setRotationPoint(3.0F, 0.0F, 0.0F);
	    horn2b.rotateAngleY = 0.3490659F;
	    horn2b.rotateAngleZ = (-zangle / 57.295776F);
	    horn2a.addChild(horn2b);
	    hornParts[3] = horn2b;
	    return hornParts;
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
