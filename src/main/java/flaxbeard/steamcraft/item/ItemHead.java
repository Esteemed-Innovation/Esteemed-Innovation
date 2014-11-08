package flaxbeard.steamcraft.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;

import java.util.List;

/**
 * @author SatanicSanta
 */
public class ItemHead extends Item {

    private final String[] names = {"headAxeBrass", "headPickaxeBrass", "headHoeBrass", "headShovelBrass",
                                    "headAxeGilded", "headPickaxeGilded", "headHoeGilded", "headShovelGilded"};

    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    public ItemHead(){
        setHasSubtypes(true);
        setMaxStackSize(1);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list){
        for (int i = 0; i < names.length; i++)
            list.add(new ItemStack(item, 1, i));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack){
        return getUnlocalizedName() + "." + names[MathHelper.clamp_int(stack.getItemDamage(), 0, names.length - 1)];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage){
        return icons[MathHelper.clamp_int(damage, 0, icons.length - 1)];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir){
        icons = new IIcon[names.length];

        for (int i = 0; i < icons.length; i++)
            icons[i] = ir.registerIcon("steamcraft:" + names[i]);
    }
}
