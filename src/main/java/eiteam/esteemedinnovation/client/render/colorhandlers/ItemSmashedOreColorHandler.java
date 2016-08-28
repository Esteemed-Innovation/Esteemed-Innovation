package eiteam.esteemedinnovation.client.render.colorhandlers;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import eiteam.esteemedinnovation.item.ItemSmashedOre;

public class ItemSmashedOreColorHandler implements IItemColor {
    @Override
    public int getColorFromItemstack(ItemStack stack, int tintIndex) {
        if (tintIndex == 1) {
            return ItemSmashedOre.colors.get(stack.getItemDamage());
        }
        return -1;
    }
}
