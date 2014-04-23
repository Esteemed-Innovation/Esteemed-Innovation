package flaxbeard.steamcraft.item;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.client.render.ModelExosuit;

public class ItemExosuitArmor extends ItemArmor {

	public ItemExosuitArmor(int i) {
		super(ItemArmor.ArmorMaterial.CHAIN, 1, i);
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
  	{
		if (stack.getItem() == SteamcraftItems.exoArmorLegs) {
			return "steamcraft:textures/models/armor/exo_2.png";
		}
		return "steamcraft:textures/models/armor/exo_1.png";
  	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel (EntityLivingBase entityLiving, ItemStack itemStack, int par2)
    {
		ModelExosuit modelbiped = new ModelExosuit(itemStack,par2);
        modelbiped.bipedHead.showModel = par2 == 0;
        modelbiped.bipedHeadwear.showModel = par2 == 0;
        modelbiped.bipedBody.showModel = par2 == 1 || par2 == 2;
        modelbiped.bipedRightArm.showModel = par2 == 1;
        modelbiped.bipedLeftArm.showModel = par2 == 1;
        modelbiped.bipedRightLeg.showModel = par2 == 2 || par2 == 3;
        modelbiped.bipedLeftLeg.showModel = par2 == 2 || par2 == 3;

        modelbiped.overlay.bipedHead.showModel = par2 == 0;
        modelbiped.overlay.bipedHeadwear.showModel = par2 == 0;
        modelbiped.overlay.bipedBody.showModel = par2 == 1 || par2 == 2;
        modelbiped.overlay.bipedRightArm.showModel = par2 == 1;
        modelbiped.overlay.bipedLeftArm.showModel = par2 == 1;
        modelbiped.overlay.bipedRightLeg.showModel = par2 == 2 || par2 == 3;
        modelbiped.overlay.bipedLeftLeg.showModel = par2 == 2 || par2 == 3;
        return modelbiped;
    }

}
