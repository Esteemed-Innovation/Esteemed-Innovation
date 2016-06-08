package flaxbeard.steamcraft.item;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSteamedFood extends ItemFood {
    private ItemFood baseFood;
    private ItemStack baseFoodStack;
    /**
     * The base constructor for ItemSteamedFood. Use this for basic use cases where the output item
     * does not have any metadata (other than 0) that needs to be worried about.
     * @param food The input item
     */
    public ItemSteamedFood(ItemFood food) {
        this(food, new ItemStack(food));
    }

    /**
     * The base constructor for more complicated ItemSteamedFoods. Use this for cases where the
     * output item has metadata (other than 0). For example: Steamed Salmon
     * @param food The input item
     * @param foodStack The input item as an ItemStack
     */
    public ItemSteamedFood(ItemFood food, ItemStack foodStack) {
        super(food.getHealAmount(foodStack) + 2, food.getSaturationModifier(foodStack) + 0.2F, food.isWolfsFavoriteMeat());
        baseFood = food;
        baseFoodStack = foodStack;
    }

    public ItemSteamedFood(ItemStack foodStack) {
        this((ItemFood) foodStack.getItem(), foodStack);
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return baseFood.getIconFromDamage(baseFoodStack.getItemDamage());
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
        return (0xFFDBDBDB);
    }
}
