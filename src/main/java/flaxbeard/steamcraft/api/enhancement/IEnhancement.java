package flaxbeard.steamcraft.api.enhancement;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IEnhancement {
    boolean canApplyTo(ItemStack stack);

    int cost(ItemStack stack);

    String getID();

    String getIcon(Item item);

    String getName(Item item);

    String getEnhancementName(Item item);
}
