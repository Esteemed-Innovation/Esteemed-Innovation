package flaxbeard.steamcraft.client.render.colorhandlers;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import flaxbeard.steamcraft.client.render.model.exosuit.ModelExosuit;
import flaxbeard.steamcraft.init.items.armor.ArmorItems;
import flaxbeard.steamcraft.item.armor.exosuit.ItemExosuitArmor;

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
            int[] ids = OreDictionary.getOreIDs(vanity);
            int dye = -1;
            outerloop:
            for (int id : ids) {
                String str = OreDictionary.getOreName(id);
                if (str.contains("dye")) {
                    for (int i = 0; i < ModelExosuit.DYES.length; i++) {
                        if (ModelExosuit.DYES[i].equals(str.substring(3))) {
                            dye = 15 - i;
                            break outerloop;
                        }
                    }
                }
            }
            if (dye != -1) {
                float[] color = EntitySheep.getDyeRgb(EnumDyeColor.byDyeDamage(dye));
                return new Color(color[0], color[1], color[2]).getRGB();
            }
        }
        return -1;
    }
}
