package eiteam.esteemedinnovation.armor.exosuit;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class ItemExosuitColorHandler implements IItemColor {
    @Override
    public int getColorFromItemstack(ItemStack stack, int tintIndex) {
        if (stack == null) {
            return -1;
        }
        Item item = stack.getItem();
        if (!(item instanceof ItemExosuitArmor)) {
            return -1;
        }
        ItemExosuitArmor armor = (ItemExosuitArmor) item;

        ItemStack vanity = armor.getStackInSlot(stack, 2);
        if (vanity != null && (tintIndex == 1 || (tintIndex > 1 && !stack.getTagCompound().hasKey("plate")))) {
            int dye = ModelExosuit.findDyeIndexFromItemStack(vanity);
            if (dye != -1) {
                float[] color = EntitySheep.getDyeRgb(EnumDyeColor.byDyeDamage(dye));
                return new Color(color[0], color[1], color[2]).getRGB();
            }
        }
        return -1;
    }
}
