package flaxbeard.steamcraft.item;

import flaxbeard.steamcraft.api.exosuit.UtilPlates;
import flaxbeard.steamcraft.integration.thaumcraft.ThaumcraftIntegration;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import thaumcraft.api.IGoggles;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.nodes.IRevealer;

import java.util.List;

public class ItemExosuitArmorThaum extends ItemExosuitArmor implements IRevealer, IGoggles, IVisDiscountGear {

    public ItemExosuitArmorThaum(int i, ArmorMaterial mat) {
        super(i, mat);
    }

    @Override
    public boolean showIngamePopups(ItemStack me, EntityLivingBase arg1) {
        return this.hasUpgrade(me, ThaumcraftIntegration.goggleUpgrade);
    }

    @Override
    public boolean showNodes(ItemStack me, EntityLivingBase arg1) {
        return this.hasUpgrade(me, ThaumcraftIntegration.goggleUpgrade);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        if (getVisDiscount(stack, player, null) > 0) {
            list.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("tc.visdiscount") + ": " + getVisDiscount(stack, player, null) + "%");
        }
        super.addInformation(stack, player, list, par4);

    }

    @Override
    public int getVisDiscount(ItemStack me, EntityPlayer arg1, Aspect arg2) {
        boolean hasThaumPlate = (((ItemExosuitArmor) me.getItem()).hasPlates(me) && UtilPlates.getPlate(me.stackTagCompound.getString("plate")).getIdentifier() == "Thaumium");
        int discount = 0;
        if (hasThaumPlate) {
            discount += this.armorType == 3 || this.armorType == 0 ? 1 : 2;
        }
        if (this.hasUpgrade(me, ThaumcraftIntegration.goggleUpgrade)) {
            discount += 5;
        }
        return discount;
    }
}
