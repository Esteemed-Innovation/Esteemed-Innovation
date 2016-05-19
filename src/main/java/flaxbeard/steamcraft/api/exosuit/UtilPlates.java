package flaxbeard.steamcraft.api.exosuit;

import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.item.ItemExosuitArmor;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.MutablePair;

public class UtilPlates {
	
	private UtilPlates() throws InstantiationException{
		throw new InstantiationException("This class is not meant to be instantiated");
	}
    public static ExosuitPlate getPlate(ItemStack item) {
        for (ExosuitPlate plate : SteamcraftRegistry.plates.values()) {
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

    public static void registerPlatesForItem(IIconRegister registry, ItemExosuitArmor item) {
        for (ExosuitPlate plate : SteamcraftRegistry.plates.values()) {
            SteamcraftRegistry.plateIcons.put(MutablePair.of(item.slot, plate), registry.registerIcon(plate.getIcon(item)));
        }
    }

    public static IIcon getIconFromPlate(String string, ItemExosuitArmor item) {
        ExosuitPlate plate = SteamcraftRegistry.plates.get(string);
        return SteamcraftRegistry.plateIcons.get(MutablePair.of(item.slot, plate));
    }

    public static String getArmorLocationFromPlate(String string, ItemExosuitArmor item, int armorType) {
        ExosuitPlate plate = SteamcraftRegistry.plates.get(string);
        return plate.getArmorLocation(item, armorType);
    }

    public static ExosuitPlate getPlate(String string) {
        return SteamcraftRegistry.plates.get(string);
    }

    /**
     * Removes Exosuit Plates from the given Exosuit piece.
     *
     * @param exosuitPiece The Exosuit Piece to remove the plates from.
     */
    public static void removePlate(ItemStack exosuitPiece) {
        if (exosuitPiece.hasTagCompound()) {
            NBTTagCompound nbt = exosuitPiece.stackTagCompound;
            if (nbt.hasKey("plate")) {
                nbt.removeTag("plate");
            }
            if (nbt.hasKey("inv") && nbt.getCompoundTag("inv").hasKey("1")) {
                nbt.getCompoundTag("inv").removeTag("1");
            }
        }
    }
}
