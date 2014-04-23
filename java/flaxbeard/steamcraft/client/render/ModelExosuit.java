package flaxbeard.steamcraft.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ModelExosuit extends ModelBiped {

	public ModelBiped overlay;
	private ResourceLocation texture;
	public ModelExosuit(ItemStack itemStack,int armorType) {
		super(armorType == 2 || armorType == 1 ? 0.5F : 1.0F, 0, 64, 32);
		
		overlay = new ModelBiped(armorType == 2 || armorType == 1 ? 0.6F : 1.1F, 0, 64, 32);
		bipedBody.addChild(overlay.bipedHead);
		bipedBody.addChild(overlay.bipedLeftArm);
		bipedBody.addChild(overlay.bipedLeftLeg);
		bipedBody.addChild(overlay.bipedRightLeg);
		bipedBody.addChild(overlay.bipedRightArm);
		bipedBody.addChild(overlay.bipedBody);
		bipedBody.addChild(overlay.bipedHeadwear);
		if (armorType != 2) {
			texture = new ResourceLocation("steamcraft:textures/models/armor/exoPlate_1.png");
		}
		else
		{
			texture = new ResourceLocation("steamcraft:textures/models/armor/exoPlate_2.png");

		}
	}

	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
        this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);
		this.bipedHead.render(par7);
		this.bipedBody.render(par7);
		this.bipedRightArm.render(par7);
		this.bipedLeftArm.render(par7);
		this.bipedRightLeg.render(par7);
		this.bipedLeftLeg.render(par7);
		this.bipedHeadwear.render(par7);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		overlay.bipedHead.render(par7);
		overlay.bipedBody.render(par7);
		overlay.bipedRightArm.render(par7);
		overlay.bipedLeftArm.render(par7);
		overlay.bipedRightLeg.render(par7);
		overlay.bipedLeftLeg.render(par7);
		overlay.bipedHeadwear.render(par7);
	}
	@Override
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
    {
		EntityLivingBase living = (EntityLivingBase) par7Entity;
    	isSneak = living != null ? living.isSneaking() : false;
    	overlay.isSneak = isSneak;
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
        overlay.aimedBow = aimedBow;
        overlay.heldItemRight = heldItemRight;
        
    	super.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);
    	overlay.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);
    }
}
