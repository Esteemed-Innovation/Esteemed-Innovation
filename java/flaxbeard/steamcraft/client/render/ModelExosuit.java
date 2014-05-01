package flaxbeard.steamcraft.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import flaxbeard.steamcraft.api.exosuit.UtilPlates;
import flaxbeard.steamcraft.item.ItemExosuitArmor;

public class ModelExosuit extends ModelBiped {

	private ResourceLocation texture;
	private ResourceLocation tankTexture = new ResourceLocation("steamcraft:textures/models/armor/exo_3.png");
	//private ModelRenderer 
	private boolean hasOverlay;
	private ModelRenderer Wing1;
	private int armor;
	public ModelExosuit(ItemStack itemStack,int armorType) {
		super(armorType == 2 || armorType == 1 ? 0.5F : 1.0F, 0, 64, 32);
		hasOverlay = false;
		armor = armorType;
		if (itemStack.hasTagCompound()) {
			if (itemStack.stackTagCompound.hasKey("plate")) {
				hasOverlay = true;
				String key = itemStack.stackTagCompound.getString("plate");
				texture = new ResourceLocation(UtilPlates.getArmorLocationFromPlate(key, (ItemExosuitArmor) itemStack.getItem(), armorType));
			}
		}
		Wing1 = new ModelRenderer(this, 0, 0);
		Wing1.addBox(-4.0F, -1F, 2F, 8, 12, 6);
		bipedBody.addChild(Wing1);
	}

	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
        this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);
		this.Wing1.showModel = false;

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
		if (armor == 1) {
			Minecraft.getMinecraft().renderEngine.bindTexture(tankTexture);
			this.Wing1.showModel = true;
			this.Wing1.render(par7);
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
    }
}
