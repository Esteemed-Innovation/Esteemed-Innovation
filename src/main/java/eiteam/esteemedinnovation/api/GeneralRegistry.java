package eiteam.esteemedinnovation.api;

import eiteam.esteemedinnovation.api.book.BookPage;
import eiteam.esteemedinnovation.api.book.ICraftingPage;
import eiteam.esteemedinnovation.api.enhancement.IEnhancement;
import eiteam.esteemedinnovation.api.enhancement.IRocket;
import eiteam.esteemedinnovation.api.exosuit.ExosuitPlate;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GeneralRegistry {

    /**
     * All of the Esteemed Innovation categories.
     */
    public static ArrayList<String> categories = new ArrayList<>();

    /**
     * All of the research entries in Esteemed Innovation book. Each entry is a pair of the name and category.
     */
    public static ArrayList<MutablePair<String, String>> research = new ArrayList<>();

    /**
     * All of the research pages. Key is the entry name, and the value is all of the pages in that entry.
     */
    public static HashMap<String, BookPage[]> researchPages = new HashMap<>();

    /**
     * All of the research pages showcasing crafting recipes.
     * Key: The output item.
     * Value: A pair of the entry name and the page number.
     */
    public static HashMap<ItemStack, MutablePair<String, Integer>> bookRecipes = new HashMap<>();

    /**
     * List of all entries that had entries added to the musing ! syntax. Used by the ctrl/shift click journal feature.
     */
    public static ArrayList<String> entriesWithSubEntries = new ArrayList<>();

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

    /**
     * Adds a research category.
     * @param string The name of the category.
     */
    public static void addCategory(String string) {
        categories.add(string);
    }

    /**
     * Adds a research entry. Typically all of these should be unlocalized, linking up to lang entries.
     * @param string The name of the entry.
     * @param category The category name.
     * @param pages The pages in that entry.
     */
    public static void addResearch(String string, String category, BookPage... pages) {
        if (!category.substring(0, 1).equals("!")) {
            research.add(MutablePair.of(string, category));
            researchPages.put(string, pages);
            int pageNum = 0;
            for (BookPage page : pages) {
                if (page instanceof ICraftingPage) {
                    for (ItemStack craftedItem : ((ICraftingPage) page).getCraftedItem()) {
                        bookRecipes.put(craftedItem, MutablePair.of(string, pageNum));
                    }
                }
                pageNum++;
            }
        } else {
            entriesWithSubEntries.add(category.substring(1));
            BookPage[] targetPages = researchPages.get(category.substring(1));
            int pageNum = targetPages.length;
            for (BookPage page : pages) {
                if (page instanceof ICraftingPage) {
                    for (ItemStack craftedItem : ((ICraftingPage) page).getCraftedItem()) {
                        bookRecipes.put(craftedItem, MutablePair.of(category.substring(1), pageNum));
                    }
                }
                pageNum++;
            }
            ArrayList<BookPage> pages2 = new ArrayList<>(Arrays.asList(targetPages));
            pages2.addAll(Arrays.asList(pages));
            researchPages.put(category.substring(1), pages2.toArray(new BookPage[pages2.size()]));
        }
    }

}
