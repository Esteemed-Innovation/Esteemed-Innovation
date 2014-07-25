package flaxbeard.steamcraft.item.tool;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.api.exosuit.ExosuitSlot;
import flaxbeard.steamcraft.api.exosuit.IExosuitUpgrade;
import flaxbeard.steamcraft.client.render.model.ModelExosuit;
import flaxbeard.steamcraft.client.render.model.ModelTophat;

public class ItemTophat extends ItemArmor implements IExosuitUpgrade {
	
	public ResourceLocation tophatTexture = new ResourceLocation("steamcraft:textures/models/armor/tophat.png");
	public ResourceLocation tophatTextureEmerald = new ResourceLocation("steamcraft:textures/models/armor/tophatemerald.png");

	boolean emerald;
	
	public ItemTophat(ArmorMaterial p_i45325_1_, int p_i45325_2_,
			int p_i45325_3_, boolean em) {
		super(p_i45325_1_, p_i45325_2_, p_i45325_3_);
		emerald = em;
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		return emerald ? "steamcraft:textures/models/armor/tophatemerald.png" : "steamcraft:textures/models/armor/tophat.png";
	}

	
	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
	{
		return par2ItemStack.isItemEqual(new ItemStack(Items.leather)) ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
	}

	@Override
	public int renderPriority() {
		return 0;
	}

	@Override
	public ExosuitSlot getSlot() {
		return ExosuitSlot.headHelm;
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
    public ModelBiped getArmorModel (EntityLivingBase entityLiving, ItemStack itemStack, int par2)
    {
		ModelTophat modelbiped = new ModelTophat(itemStack,par2);
		return modelbiped;
    }

	@Override
	public void renderModel(ModelExosuit model, Entity par1Entity, int armor,
			float par7, ItemStack me) {
		
		ModelRenderer tophatBase = new ModelRenderer(model, 64, 32).setTextureOffset(32, 0);
		tophatBase.addBox(-4.0F, -17.0F, -4.0F, 8, 7, 8);
		model.bipedHead.addChild(tophatBase);
		
		ModelRenderer tophatHat = new ModelRenderer(model, 64, 32).setTextureOffset(0, 16);
		tophatHat.addBox(-5.5F, -10.0F, -5.5F, 11, 1, 11);
		model.bipedHead.addChild(tophatHat);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(emerald ? tophatTextureEmerald : tophatTexture);
		model.bipedHead.render(par7);
		tophatHat = null;
		tophatBase = null;
	}

	@Override
	public void writeInfo(List list) {		
	}
}
