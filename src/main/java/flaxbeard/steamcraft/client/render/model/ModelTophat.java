package flaxbeard.steamcraft.client.render.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import org.lwjgl.opengl.GL11;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.exosuit.IExosuitUpgrade;
import flaxbeard.steamcraft.api.exosuit.UtilPlates;
import flaxbeard.steamcraft.item.ItemExosuitArmor;
import flaxbeard.steamcraft.misc.ComparatorUpgrade;

public class ModelTophat extends ModelBiped {
	private ModelRenderer tophatBase;
	private ModelRenderer tophatHat;

	public ModelTophat(ItemStack itemStack,int armorType) {
		super(1.0F, 0, 64, 32);
		tophatBase = new ModelRenderer(this, 64, 32).setTextureOffset(32, 0);
		tophatBase.addBox(-4.0F, -16.0F, -4.0F, 8, 7, 8);
		this.bipedHead.addChild(tophatBase);
		
		tophatHat = new ModelRenderer(this, 64, 32).setTextureOffset(0, 16);
		tophatHat.addBox(-5.5F, -9.0F, -5.5F, 11, 1, 11);
		this.bipedHead.addChild(tophatHat);
	}

	@Override
	public void render(Entity entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
        
	    this.setRotationAngles(par2, par3, par4, par5, par6, par7, entity);

		this.bipedHead.render(par7);
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
