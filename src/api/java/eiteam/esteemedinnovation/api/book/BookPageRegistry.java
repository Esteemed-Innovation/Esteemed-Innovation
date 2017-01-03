package eiteam.esteemedinnovation.api.book;

import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 * The registry for all book related stuff. If you are creating your own categories in the book, the best way is to use
 * the BookCategory Factory (assuming you are not making your own subclasses of BookCategory) and simply add it to the
 * registry with {@link #addTopCategory(BookCategory)}. It's much more complicated and confusing to use the other methods
 * to try to construct an entire category. If you are adding stuff to the existing things, then use the other methods.
 *
 * Because the methods, {@link #getCategoryFromName(String)} and {@link #getEntryFromName(String)} search the flat list
 * of all categories, it is very important to use *unique* keynames (unlocalized names) for your entries and categories.
 */
public class BookPageRegistry {
    /**
     * All of the Esteemed Innovation categories. This, logically, contains all entries and all pages as well.
     * These are actually just the top level categories.
     */
    public static final List<BookCategory> categories = new ArrayList<>();

    /**
     * All of the categories, including other categories' subcategories.
     */
    public static final List<BookCategory> allCategoriesFlat = new ArrayList<>();

    /**
     * All of the research pages showcasing crafting recipes.
     * Key: The output item.
     * Value: A pair of the entry name and the page number.
     */
    public static final Map<ItemStack, Pair<String, Integer>> bookRecipes = new HashMap<>();

    /**
     * Adds a simple top level category with no entries. It is recommended to use a factory to construct your category
     * entirely, and then register with {@link #addTopCategory(BookCategory)}, but you can use this.
     *
     * In order to use this, you first register the category with this entry, and then use the other addTHING methods
     * to append to the category. Again, it's recommended to use the other method instead with a fully constructed
     * category.
     * @param categoryName The unlocalized name for the category.
     */
    public static void addTopCategory(String categoryName) {
        addTopCategory(new BookCategory(categoryName, new BookEntry[] {}));
    }

    /**
     * Adds a top level research category.
     * @param category The category.
     */
    public static void addTopCategory(BookCategory category) {
        categories.add(category);
        addCategoriesToFlatList(category);
    }

    /**
     * Adds a top level research category in a specific location in the list. Shifts everything to the right.
     * @see List#add(int, Object)
     * @param index The index
     * @param category The category
     */
    public static void addTopCategory(int index, BookCategory category) {
        categories.add(index, category);
        addCategoriesToFlatList(category);
    }

    /**
     * Adds a research entry to a category. This will use the default BookEntry which is always unlocked. Use the other
     * addEntryToCategory method to use your own custom BookEntry.
     * @param name The name of the entry.
     * @param category The category name.
     * @param pages The pages in that entry.
     */
    public static void addEntryToCategory(String name, String category, BookPage... pages) {
        addEntryToCategory(category, new BookEntry(name, pages));
    }

    /**
     * @see #addEntryToCategory(String, String, BookPage...)
     */
    public static void addEntryToCategory(String categoryName, BookEntry entry) {
        BookCategory category = getCategoryFromName(categoryName);
        if (category != null) {
            category.appendEntries(entry);
            addEntryPagesToRecipeList(entry);
        }
    }

    /**
     * Adds a set of pages to the given entry.
     * @param entryName The name of the entry.
     * @param pages The pages to add to the entry.
     */
    public static void addPagesToEntry(String entryName, BookPage... pages) {
        BookEntry entry = getEntryFromName(entryName);
        if (entry != null) {
            entry.appendPages(pages);
            addEntryPagesToRecipeList(entry);
        }
    }

    /**
     * Adds a subcategory to a preexisting already registered category. The subcategory will also be added to
     * {@link #allCategoriesFlat}.
     * @param categoryName The name of the already-registered category to append to.
     * @param subcategory The subcategory to add to the category.
     */
    public static void addSubcategoryToCategory(String categoryName, BookCategory subcategory) {
        BookCategory category = getCategoryFromName(categoryName);
        if (category != null) {
            category.appendCategories(subcategory);
            addCategoriesToFlatList(subcategory);
        }
    }

    /**
     * Helper method that recursively adds all categories within a category to {@link #allCategoriesFlat}.
     * @param category The category to add to the list.
     */
    private static void addCategoriesToFlatList(BookCategory category) {
        allCategoriesFlat.add(category);
        for (BookCategory sub : category.getSubcategories()) {
            addCategoriesToFlatList(sub);
        }
    }

    /**
     * Helper method that adds all of an entry's crafting pages ({@link CraftingPage} subclasses) to {@link #bookRecipes}.
     * @param entry The entry to add.
     */
    private static void addEntryPagesToRecipeList(BookEntry entry) {
        int pageNum = 0;
        for (BookPage page : entry.getPages()) {
            if (page instanceof CraftingPage) {
                for (ItemStack craftedItem : ((CraftingPage) page).getCraftedItem()) {
                    bookRecipes.put(craftedItem, Pair.of(entry.getEntryName(), pageNum));
                }
            }
            pageNum++;
        }
    }

    /**
     * @param name The entry name to search for
     * @return The BookEntry tied to the provided name. It searches in {@link #allCategoriesFlat}, so this will search
     *         entries of subcategories as well. Null if it is not present in any category.
     */
    public static BookEntry getEntryFromName(String name) {
        for (BookCategory category : allCategoriesFlat) {
            for (BookEntry entry : category.getEntries()) {
                if (entry.getEntryName().equals(name)) {
                    return entry;
                }
            }
        }
        return null;
    }

    /**
     * @param name The category name to search for.
     * @return The BookCategory tied to the provided name. It searches in {@link #allCategoriesFlat}, so this will
     *         yield subcategories as well. Null if no category is found.
     */
    public static BookCategory getCategoryFromName(String name) {
        for (BookCategory category : allCategoriesFlat) {
            if (category.getCategoryName().equals(name)) {
                return category;
            }
        }
        return null;
    }
}
