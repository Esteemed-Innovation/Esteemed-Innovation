package eiteam.esteemedinnovation.api.exosuit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExosuitRegistry {
    /**
     * All of the registered Exosuit Plates. Key is the plate ID, which is typically the material's
     * name. Value is the actual plate.
     */
    public static HashMap<String, ExosuitPlate> plates = new HashMap<>();

    public static List<IExosuitUpgrade> upgrades = new ArrayList<>();

    /**
     * Registers an ExosuitPlate.
     * @param plate The plate.
     */
    public static void addExosuitPlate(ExosuitPlate plate) {
        plates.put(plate.getIdentifier(), plate);
    }

    /**
     * Registers an IExosuitUpgrade.
     * @param upgrade The upgrade.
     */
    public static void addExosuitUpgrade(IExosuitUpgrade upgrade) {
        upgrades.add(upgrade);
    }
}
