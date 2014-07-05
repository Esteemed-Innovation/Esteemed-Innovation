//package flaxbeard.steamcraft.item;
//
//import java.util.List;
//
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.EnumChatFormatting;
//import net.minecraft.util.StatCollector;
//import thaumcraft.api.IGoggles;
//import thaumcraft.api.IVisDiscountGear;
//import thaumcraft.api.aspects.Aspect;
//import thaumcraft.api.nodes.IRevealer;
//import flaxbeard.steamcraft.integration.ThaumcraftIntegration;
//
//public class ItemExosuitArmorThaum extends ItemExosuitArmor implements IRevealer, IGoggles, IVisDiscountGear {
//
//	public ItemExosuitArmorThaum(int i) {
//		super(i);
//	}
//
//	@Override
//	public boolean showIngamePopups(ItemStack me, EntityLivingBase arg1) {
//		return this.hasUpgrade(me, ThaumcraftIntegration.goggleUpgrade);
//	}
//
//	@Override
//	public boolean showNodes(ItemStack me, EntityLivingBase arg1) {
//		return this.hasUpgrade(me, ThaumcraftIntegration.goggleUpgrade);
//	}
//
//	@Override
//	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
//	{
//		super.addInformation(stack, player, list, par4);
//		if (this.hasUpgrade(stack, ThaumcraftIntegration.goggleUpgrade)) {
//			list.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("tc.visdiscount") + ": " + getVisDiscount(stack, player, null) + "%");
//		}
//	}
//
//	@Override
//	public int getVisDiscount(ItemStack me, EntityPlayer arg1, Aspect arg2) {
//		return (this.hasUpgrade(me, ThaumcraftIntegration.goggleUpgrade) ? 5 : 0);
//	}
//}
