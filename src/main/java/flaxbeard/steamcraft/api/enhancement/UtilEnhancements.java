package flaxbeard.steamcraft.api.enhancement;

import flaxbeard.steamcraft.api.SteamcraftRegistry;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import org.apache.commons.lang3.tuple.MutablePair;

public class UtilEnhancements {
    public static void registerEnhancementsForItem(IIconRegister registry, Item item) {
        for (IEnhancement enhancement : SteamcraftRegistry.enhancements.values()) {
            if (enhancement.canApplyTo(new ItemStack(item))) {
                SteamcraftRegistry.enhancementIcons.put(MutablePair.of(item, enhancement), registry.registerIcon(enhancement.getIcon(item)));
            }
        }
    }

    public static boolean hasEnhancement(ItemStack item) {
        return getEnhancementFromItem(item) != null;
    }

    public static IEnhancement getEnhancementFromItem(ItemStack item) {
        if (item.hasTagCompound()) {
            if (item.stackTagCompound.hasKey("enhancements")) {
                return SteamcraftRegistry.enhancements.get(item.stackTagCompound.getCompoundTag("enhancements").getString("id"));
            }
        }
        return null;
    }

    public static IIcon getIconFromEnhancement(ItemStack item, IEnhancement enhancement) {
        return SteamcraftRegistry.enhancementIcons.get(MutablePair.of(item.getItem(), enhancement));
    }

    public static IIcon getIconFromEnhancement(ItemStack item) {
        return getIconFromEnhancement(item, getEnhancementFromItem(item));
    }

    public static String getNameFromEnhancement(ItemStack item, IEnhancement enhancement) {
        return enhancement.getName(item.getItem());
    }

    public static String getNameFromEnhancement(ItemStack item) {
        return getNameFromEnhancement(item, getEnhancementFromItem(item));
    }

    public static String getEnhancementNameFromEnhancement(ItemStack item, IEnhancement enhancement) {
        return enhancement.getEnhancementName(item.getItem());
    }

    public static String getEnhancementNameFromEnhancement(ItemStack item) {
        return getEnhancementNameFromEnhancement(item, getEnhancementFromItem(item));
    }

    public static String getEnhancementDisplayText(ItemStack item) {
        if (hasEnhancement(item)) {
            return EnumChatFormatting.RED + new ItemStack(((Item) getEnhancementFromItem(item))).getDisplayName();
        }
        return "";
    }

    public static boolean canEnhance(ItemStack item) {
        if (!item.hasTagCompound()) {
            return true;
        }
        if (!item.stackTagCompound.hasKey("enhancements")) {
            return true;
        }
        return false;
    }

    public static ItemStack getEnhancedItem(ItemStack item, ItemStack enhancement) {
        ItemStack output = item.copy();
        if (!output.hasTagCompound()) {
            output.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound enhancements = new NBTTagCompound();
        enhancements.setString("id", ((IEnhancement) enhancement.getItem()).getID());
        output.stackTagCompound.setTag("enhancements", enhancements);

        return output;
    }

    public static void removeEnhancement(ItemStack item) {
        if (item.hasTagCompound() && item.stackTagCompound.hasKey("enhancements")) {
            item.stackTagCompound.removeTag("enhancements");
        }
    }
}
