package eiteam.esteemedinnovation.api.enhancement;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.HashMap;

public class EnhancementRegistry {
    /**
     * The icons for all of the IEnhancements.
     * Key: A pair of the item, the item as an enhancement.
     * Value: The ResourceLocation for the enhancement.
     */
    public static HashMap<MutablePair<Item, IEnhancement>, ResourceLocation> enhancementIcons = new HashMap<>();
    /**
     * The IEnhancements.
     * Key: Enhancement ID
     * Value: IEnhancement
     */
    public static HashMap<String, IEnhancement> enhancements = new HashMap<>();
    /**
     * All of the rockets that the rocket launcher can use.
     */
    public static ArrayList<IRocket> rockets = new ArrayList<>();

    /**
     * Adds an enhancement to the list of valid enhancements.
     * @param enhancement The IEnhancement to add.
     */
    public static void registerEnhancement(IEnhancement enhancement) {
        enhancements.put(enhancement.getID(), enhancement);
    }

    /**
     * Adds a rocket to the valid rockets for the Rocket Launcher.
     * @param rocket The IRocket to add.
     */
    public static void registerRocket(IRocket rocket) {
        rockets.add(rocket);
    }
}
