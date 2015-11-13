package flaxbeard.steamcraft.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemSteamedFood extends ItemFood {
    private ItemFood baseFood;

    public ItemSteamedFood(ItemFood food) {
        super(food.func_150905_g(new ItemStack(food)) + 2, food.func_150906_h(new ItemStack(food)) + 0.2F, food.isWolfsFavoriteMeat());
        baseFood = food;
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return baseFood.getIconFromDamage(meta);
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
        return (0xFFDBDBDB);
    }
}
