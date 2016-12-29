package eiteam.esteemedinnovation.api.enhancement;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class UtilEnhancements {
    /**
     * Registers the ResourceLocations for the icons for every possible upgrade for the provided item.
     * @param item The item that can take enhancements.
     * @return A list of all registered resource locations for the item.
     */
    public static List<ResourceLocation> registerEnhancementsForItem(Item item) {
        List<ResourceLocation> locs = new ArrayList<>();
        for (Enhancement enhancement : EnhancementRegistry.enhancements.values()) {
            if (enhancement.canApplyTo(new ItemStack(item))) {
                ResourceLocation loc = enhancement.getModel(item);
                locs.add(loc);
                EnhancementRegistry.enhancementIcons.put(Pair.of(item, enhancement), loc);
            }
        }
        return locs;
    }

    public static boolean hasEnhancement(ItemStack item) {
        return getEnhancementFromItem(item) != null;
    }

    public static Enhancement getEnhancementFromItem(ItemStack item) {
        if (item.hasTagCompound()) {
            NBTTagCompound nbt = item.getTagCompound();
            if (nbt.hasKey("enhancements")) {
                NBTTagCompound enhancements = nbt.getCompoundTag("enhancements");
                return EnhancementRegistry.enhancements.get(enhancements.getString("id"));
            }
        }
        return null;
    }

    public static ResourceLocation getIconFromEnhancement(ItemStack item, Enhancement enhancement) {
        return EnhancementRegistry.enhancementIcons.get(Pair.of(item.getItem(), enhancement));
    }

    public static ResourceLocation getIconFromEnhancement(ItemStack item) {
        return getIconFromEnhancement(item, getEnhancementFromItem(item));
    }

    public static String getNameFromEnhancement(ItemStack item, Enhancement enhancement) {
        return enhancement.getName(item.getItem());
    }

    public static String getNameFromEnhancement(ItemStack item) {
        return getNameFromEnhancement(item, getEnhancementFromItem(item));
    }

    public static String getEnhancementDisplayText(ItemStack item) {
        if (hasEnhancement(item)) {
            return TextFormatting.RED + new ItemStack(((Item) getEnhancementFromItem(item))).getDisplayName();
        }
        return "";
    }

    public static boolean canEnhance(ItemStack item) {
        return !item.hasTagCompound() || !item.getTagCompound().hasKey("enhancements");
    }

    public static ItemStack getEnhancedItem(ItemStack item, ItemStack enhancement) {
        ItemStack output = item.copy();
        if (!output.hasTagCompound()) {
            output.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound enhancements = new NBTTagCompound();
        enhancements.setString("id", ((Enhancement) enhancement.getItem()).getID());
        output.getTagCompound().setTag("enhancements", enhancements);

        return output;
    }

    public static void removeEnhancement(ItemStack item) {
        if (item.hasTagCompound() && item.getTagCompound().hasKey("enhancements")) {
            item.getTagCompound().removeTag("enhancements");
        }
    }
}
