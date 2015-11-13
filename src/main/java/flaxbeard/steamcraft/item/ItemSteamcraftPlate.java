package flaxbeard.steamcraft.item;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;

import flaxbeard.steamcraft.integration.CrossMod;


import java.util.List;

public class ItemSteamcraftPlate extends Item {
    public IIcon[] icon = new IIcon[12];

    public ItemSteamcraftPlate() {
        this.setHasSubtypes(true);
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
        this.icon[0] = ir.registerIcon("steamcraft:plateCopper");
        this.icon[1] = ir.registerIcon("steamcraft:plateZinc");
        this.icon[2] = ir.registerIcon("steamcraft:plateIron");
        this.icon[3] = ir.registerIcon("steamcraft:plateGold");
        this.icon[4] = ir.registerIcon("steamcraft:plateBrass");
        this.icon[5] = ir.registerIcon("steamcraft:plateThaumium");
        this.icon[6] = ir.registerIcon("steamcraft:plateTerrasteel");
        this.icon[7] = ir.registerIcon("steamcraft:plateElementum");
        this.icon[8] = ir.registerIcon("steamcraft:plateFiery");
        this.icon[9] = ir.registerIcon("steamcraft:plateLead");
        this.icon[10] = ir.registerIcon("steamcraft:plateVibrant");
        this.icon[11] = ir.registerIcon("steamcraft:plateEnderium");

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
        }
        if (OreDictionary.getOres("ingotLead").size() > 0) {
            par3List.add(new ItemStack(par1, 1, 9));
        }
        if (CrossMod.ENDER_IO) {
            par3List.add(new ItemStack(par1, 1, 10));
        }
        if (CrossMod.THERMAL_FOUNDATION) {
            par3List.add(new ItemStack(par1, 1, 11));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
    }
}
