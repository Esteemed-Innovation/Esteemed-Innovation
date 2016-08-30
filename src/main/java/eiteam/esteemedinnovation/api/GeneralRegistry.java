package eiteam.esteemedinnovation.api;

import eiteam.esteemedinnovation.api.exosuit.ExosuitPlate;
import net.minecraft.inventory.EntityEquipmentSlot;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.HashMap;

public class GeneralRegistry {

    /**
     * All of the registered Exosuit Plates. Key is the plate ID, which is typically the material's
     * name. Value is the actual plate.
     */
    public static HashMap<String, ExosuitPlate> plates = new HashMap<>();

    /**
     * All of the Exosuit Plate icons. Key is a pair of the exosuit slot and the plate. Value is the
     * IIcon for that slot.
     */
    public static HashMap<MutablePair<EntityEquipmentSlot, ExosuitPlate>, String> plateIcons = new HashMap<>();

    /**
     * Registers an ExosuitPlate.
     * @param plate The plate.
     */
    public static void addExosuitPlate(ExosuitPlate plate) {
        plates.put(plate.getIdentifier(), plate);
    }

}
