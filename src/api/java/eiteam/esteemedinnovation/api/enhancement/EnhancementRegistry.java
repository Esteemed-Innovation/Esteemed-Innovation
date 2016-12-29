package eiteam.esteemedinnovation.api.enhancement;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EnhancementRegistry {
    /**
     * The icons for all of the IEnhancements.
     * Key: A pair of the item, the item as an enhancement.
     * Value: The ResourceLocation for the enhancement.
     */
    public static Map<Pair<Item, Enhancement>, ResourceLocation> enhancementIcons = new HashMap<>();

    /**
     * The IEnhancements.
     * Key: Enhancement ID
     * Value: Enhancement
     */
    public static Map<String, Enhancement> enhancements = new HashMap<>();

    /**
     * All of the rockets that the rocket launcher can use.
     */
    public static ArrayList<Rocket> rockets = new ArrayList<>();

    /**
     * Adds an enhancement to the list of valid enhancements.
     * @param enhancement The Enhancement to add.
     */
    public static void registerEnhancement(Enhancement enhancement) {
        enhancements.put(enhancement.getID(), enhancement);
    }

    /**
     * Adds a rocket to the valid rockets for the Rocket Launcher.
     * @param rocket The Rocket to add.
     */
    public static void registerRocket(Rocket rocket) {
        rockets.add(rocket);
    }
}
