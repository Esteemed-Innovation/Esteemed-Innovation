package eiteam.esteemedinnovation.client.render.colorhandlers;

import eiteam.esteemedinnovation.client.render.model.exosuit.ModelExosuit;
import eiteam.esteemedinnovation.init.items.armor.ArmorItems;
import eiteam.esteemedinnovation.item.armor.exosuit.ItemExosuitArmor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class ItemExosuitColorHandler implements IItemColor {
    private ArmorItems.Items item;

    public ItemExosuitColorHandler(ArmorItems.Items item) {
        this.item = item;
    }

    @Override
    public int getColorFromItemstack(ItemStack stack, int tintIndex) {
        Item item = this.item.getItem();
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
