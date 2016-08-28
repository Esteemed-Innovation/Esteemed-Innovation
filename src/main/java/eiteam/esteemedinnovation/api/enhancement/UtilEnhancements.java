package eiteam.esteemedinnovation.api.enhancement;

import eiteam.esteemedinnovation.api.GeneralRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.tuple.MutablePair;

public class UtilEnhancements {
    public static void registerEnhancementsForItem(Item item) {
        for (IEnhancement enhancement : GeneralRegistry.enhancements.values()) {
            if (enhancement.canApplyTo(new ItemStack(item))) {
                GeneralRegistry.enhancementIcons.put(MutablePair.of(item, enhancement), enhancement.getIcon(item));
            }
        }
    }

    public static boolean hasEnhancement(ItemStack item) {
        return getEnhancementFromItem(item) != null;
    }

    public static IEnhancement getEnhancementFromItem(ItemStack item) {
        if (item.hasTagCompound()) {
            NBTTagCompound nbt = item.getTagCompound();
            if (nbt.hasKey("enhancements")) {
                NBTTagCompound enhancements = nbt.getCompoundTag("enhancements");
                return GeneralRegistry.enhancements.get(enhancements.getString("id"));
            }
        }
        return null;
    }

    public static ResourceLocation getIconFromEnhancement(ItemStack item, IEnhancement enhancement) {
        return GeneralRegistry.enhancementIcons.get(MutablePair.of(item.getItem(), enhancement));
    }

    public static ResourceLocation getIconFromEnhancement(ItemStack item) {
        return getIconFromEnhancement(item, getEnhancementFromItem(item));
    }

    public static String getNameFromEnhancement(ItemStack item, IEnhancement enhancement) {
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
        enhancements.setString("id", ((IEnhancement) enhancement.getItem()).getID());
        output.getTagCompound().setTag("enhancements", enhancements);

        return output;
    }

    public static void removeEnhancement(ItemStack item) {
        if (item.hasTagCompound() && item.getTagCompound().hasKey("enhancements")) {
            item.getTagCompound().removeTag("enhancements");
        }
    }
}
