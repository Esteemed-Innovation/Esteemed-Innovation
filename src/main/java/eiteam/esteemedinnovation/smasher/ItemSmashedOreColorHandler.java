package eiteam.esteemedinnovation.smasher;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

public class ItemSmashedOreColorHandler implements IItemColor {
    @Override
    public int colorMultiplier(ItemStack stack, int tintIndex) {
        if (tintIndex == 1) {
            return ItemSmashedOre.colors.get(stack.getItemDamage());
        }
        return -1;
    }
}
