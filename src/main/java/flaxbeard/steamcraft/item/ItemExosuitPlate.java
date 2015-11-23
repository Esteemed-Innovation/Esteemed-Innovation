package flaxbeard.steamcraft.item;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.integration.CrossMod;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class ItemExosuitPlate extends Item {
    public IIcon[] icon = new IIcon[14];

    public ItemExosuitPlate() {
        this.setHasSubtypes(true);
    }

    @Override
    public EnumRarity getRarity(ItemStack p_77613_1_) {
        return Steamcraft.upgrade;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int par1) {
        if (par1 < icon.length) {
            return this.icon[par1];
        }
        return this.icon[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
        this.icon[0] = ir.registerIcon("steamcraft:exosuitPlateCopper");
        this.icon[1] = ir.registerIcon("steamcraft:exosuitPlateZinc");
        this.icon[2] = ir.registerIcon("steamcraft:exosuitPlateIron");
        this.icon[3] = ir.registerIcon("steamcraft:exosuitPlateGold");
        this.icon[4] = ir.registerIcon("steamcraft:exosuitPlateBrass");
        this.icon[5] = ir.registerIcon("steamcraft:exosuitPlateThaumium");
        this.icon[6] = ir.registerIcon("steamcraft:exosuitPlateTerrasteel");
        this.icon[7] = ir.registerIcon("steamcraft:exosuitPlateElementium");
        this.icon[8] = ir.registerIcon("steamcraft:exosuitPlateFiery");
        this.icon[9] = ir.registerIcon("steamcraft:exosuitPlateYeti");
        this.icon[10] = ir.registerIcon("steamcraft:exosuitPlateSadist");
        this.icon[11] = ir.registerIcon("steamcraft:exosuitPlateLead");
        this.icon[12] = ir.registerIcon("steamcraft:exosuitPlateVibrant");
        this.icon[13] = ir.registerIcon("steamcraft:exosuitPlateEnderium");

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 2));
        par3List.add(new ItemStack(par1, 1, 3));
        par3List.add(new ItemStack(par1, 1, 4));
        if (CrossMod.THAUMCRAFT) {
            par3List.add(new ItemStack(par1, 1, 5));
        }
        if (CrossMod.BOTANIA) {
            par3List.add(new ItemStack(par1, 1, 6));
            par3List.add(new ItemStack(par1, 1, 7));
        }
        if (CrossMod.TWILIGHT_FOREST) {
            par3List.add(new ItemStack(par1, 1, 8));
            par3List.add(new ItemStack(par1, 1, 9));
        }
        if (CrossMod.BLOOD_MAGIC) {
            par3List.add(new ItemStack(par1, 1, 10));
        }
        if (OreDictionary.getOres("ingotLead").size() > 0) {
            par3List.add(new ItemStack(par1, 1, 11));
        }
        if (CrossMod.ENDER_IO) {
            par3List.add(new ItemStack(par1, 1, 12));
        }
        if (CrossMod.THERMAL_FOUNDATION) {
            par3List.add(new ItemStack(par1, 1, 13));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
    }
}
