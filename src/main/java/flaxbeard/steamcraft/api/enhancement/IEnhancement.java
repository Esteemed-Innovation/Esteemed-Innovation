package flaxbeard.steamcraft.api.enhancement;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IEnhancement {
    public boolean canApplyTo(ItemStack stack);

    public int cost(ItemStack stack);

    public String getID();

    public String getIcon(Item item);

    public String getName(Item item);

    public String getEnhancementName(Item item);
}
