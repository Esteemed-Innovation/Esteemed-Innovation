package eiteam.esteemedinnovation.api.util;

import net.minecraft.item.ItemStack;

import java.util.Comparator;

/**
 * Comparator that compares the ItemStacks' metadata values.
 */
public class ItemStackMetaComparator implements Comparator<ItemStack> {
    @Override
    public int compare(ItemStack o1, ItemStack o2) {
        int meta1 = o1.getItemDamage();
        int meta2 = o2.getItemDamage();
        if (meta1 == meta2) {
            return 0;
        } else if (meta1 < meta2) {
            return -1;
        } else {
            return 1;
        }
    }
}
