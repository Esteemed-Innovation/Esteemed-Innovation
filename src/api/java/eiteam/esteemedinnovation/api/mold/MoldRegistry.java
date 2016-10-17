package eiteam.esteemedinnovation.api.mold;

import net.minecraft.item.Item;

import java.util.ArrayList;

public class MoldRegistry {
    /**
     * The Items that can be used on the Carving Table.
     */
    public static ArrayList<Item> molds = new ArrayList<>();

    /**
     * Allows the mold to be used on the Carving Table.
     * @param mold The item
     */
    public static void addCarvableMold(Item mold) {
        molds.add(mold);
    }

    /**
     * Removes the mold from the list of Items that can be used on the Carving Table.
     * @param mold The item
     * @return The return value of ArrayList#remove.
     * @see ArrayList#remove(Object)
     */
    public static boolean removeMold(Item mold) {
        return molds.remove(mold);
    }
}
