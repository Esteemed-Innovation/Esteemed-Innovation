package eiteam.esteemedinnovation.api.exosuit;

import eiteam.esteemedinnovation.item.armor.exosuit.ItemExosuitArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.MutablePair;

public class UtilPlates {
    public static ExosuitPlate getPlate(ItemStack item) {
        for (ExosuitPlate plate : ExosuitRegistry.plates.values()) {
            if (plate.getItem() instanceof ItemStack) {
                if (((ItemStack) plate.getItem()).isItemEqual(item)) {
                    return plate;
                }
            }
            if (plate.getItem() instanceof String) {
                for (ItemStack i : OreDictionary.getOres(((String) plate.getItem()))) {
                    if (i.isItemEqual(item)) {
                        return plate;
                    }
                }
            }
        }
        return null;
    }

    public static void registerPlatesForItem(ItemExosuitArmor item) {
        for (ExosuitPlate plate : ExosuitRegistry.plates.values()) {
            ExosuitRegistry.plateIcons.put(MutablePair.of(item.slot, plate), plate.getIcon(item));
        }
    }

    public static String getIconFromPlate(String string, ItemExosuitArmor item) {
        ExosuitPlate plate = ExosuitRegistry.plates.get(string);
        return ExosuitRegistry.plateIcons.get(MutablePair.of(item.slot, plate));
    }

    public static String getArmorLocationFromPlate(String string, ItemExosuitArmor item, int armorType) {
        ExosuitPlate plate = ExosuitRegistry.plates.get(string);
        return plate.getArmorLocation(item, armorType);
    }

    public static ExosuitPlate getPlate(String string) {
        return ExosuitRegistry.plates.get(string);
    }

    /**
     * Removes Exosuit Plates from the given Exosuit piece.
     *
     * @param exosuitPiece The Exosuit Piece to remove the plates from.
     */
    public static void removePlate(ItemStack exosuitPiece) {
        if (exosuitPiece.hasTagCompound()) {
            NBTTagCompound nbt = exosuitPiece.getTagCompound();
            if (nbt.hasKey("plate")) {
                nbt.removeTag("plate");
            }
            if (nbt.hasKey("inv") && nbt.getCompoundTag("inv").hasKey("1")) {
                nbt.getCompoundTag("inv").removeTag("1");
            }
        }
    }
}
