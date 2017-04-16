package eiteam.esteemedinnovation.api.book;

import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.*;

/**
 * The registry for all book related stuff.
 * <p>
 * It is very important to use *unique* keynames (unlocalized names) for your sections, categories, and entries.
 */
public class BookPageRegistry {
    /**
     * All of the Esteemed Innovation sections. This, logically, contains all categories, entries, and pages as well.
     * The key is simply the stable position of the section in the book.
     */
    public static final TreeMap<Integer, BookSection> sections = new TreeMap<>();

    /**
     * All of the research pages showcasing crafting recipes.
     * <ul>
     *     <li>Key: The output item.</li>
     *     <li>Value: A pair of the entry name and the page number.</li>
     * </ul>
     */
    public static final Map<ItemStack, Pair<String, Integer>> bookRecipes = new HashMap<>();

    /**
     * Adds a simple section with no categories. It is probably better in most cases to use the version of this
     * method that explicitly adds a {@link BookSection} instance ({@link #addSection(BookSection)}).
     * <p>
     * In order to use this, you first register the section with this name, and then use the other addTHING methods
     * to append to the section. Again, it's recommended to use the other method instead with a fully constructed
     * section.
     * @param name The unlocalized name for the section.
     */
    public static void addSection(String name) {
        addSection(new BookSection(name));
    }

    /**
     * Adds a research section.
     * @param section The section.
     */
    public static void addSection(BookSection section) {
        sections.put(sections.lastKey() + 1, section);
    }

    /**
     * Position-sensitive version of {@link #addSection(BookSection)}.
     */
    public static void addSection(int position, BookSection section) {
        sections.put(position, section);
    }

    /**
     * Adds a research category to a section.
     * @param sectionName The name of the section being added to.
     * @param category The category to add.
     */
    public static void addCategoryToSection(String sectionName, BookCategory category) {
        BookSection section = getSectionFromName(sectionName);
        if (section != null) {
            section.addCategories(category);
            category.getEntries().forEach(BookPageRegistry::addEntryPagesToRecipeList);
        }
    }

    /**
     * Position-sensitive version of {@link #addCategoryToSection(String, BookCategory)}.
     */
    public static void addCategoryToSection(String sectionName, int position, BookCategory category) {
        BookSection section = getSectionFromName(sectionName);
        if (section != null) {
            section.addCategory(position, category);
            category.getEntries().forEach(BookPageRegistry::addEntryPagesToRecipeList);
        }
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
     * Position-sensitive version of {@link #addEntryToCategory(String, BookEntry)}.
     */
    public static void addEntryToCategory(String categoryName, int position, BookEntry entry) {
        BookCategory category = getCategoryFromName(categoryName);
        if (category != null) {
            category.insertEntry(position, entry);
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
     * Helper method that adds all of an entry's crafting pages ({@link CraftingPage} subclasses) to {@link #bookRecipes}.
     * @param entry The entry to add.
     */
    private static void addEntryPagesToRecipeList(BookEntry entry) {
        int pageNum = 0;
        for (BookPage page : entry.getPages()) {
            if (page instanceof CraftingPage) {
                for (ItemStack craftedItem : ((CraftingPage) page).getCraftedItem()) {
                    bookRecipes.put(craftedItem, Pair.of(entry.getName(), pageNum));
                }
            }
            pageNum++;
        }
    }

    /**
     * @param name The entry name to search for
     * @return The BookEntry tied to the provided name. Null if it is not present in any category.
     */
    public static BookEntry getEntryFromName(String name) {
        for (BookSection section : sections.values()) {
            for (BookCategory category : section.getCategories()) {
                for (BookEntry entry : category.getEntries()) {
                    if (entry.getName().equals(name)) {
                        return entry;
                    }
                }
            }
        }
        return null;
    }

    /**
     * @param name The category name to search for.
     * @return The BookCategory tied to the provided name. Null if no category is found.
     */
    public static BookCategory getCategoryFromName(String name) {
        for (BookSection section : sections.values()) {
            for (BookCategory category : section.getCategories()) {
                if (category.getName().equals(name)) {
                    return category;
                }
            }
        }
        return null;
    }

    /**
     * @param name The section name to search for.
     * @return The BookSection tied to the provided name. Null if no section is found.
     */
    public static BookSection getSectionFromName(String name) {
        for (BookSection section : sections.values()) {
            if (section.getName().equals(name)) {
                return section;
            }
        }
        return null;
    }

    /**
     * @param name {@inheritDoc}
     * @return The first BookPiece found with this name, or null.
     */
    @Nullable
    public static BookPiece getFirstPieceFromName(String name) {
        for (BookSection section : sections.values()) {
            if (section.getName().equals(name)) {
                return section;
            }
            for (BookCategory category : section.getCategories()) {
                if (category.getName().equals(name)) {
                    return category;
                }
                for (BookEntry entry : category.getEntries()) {
                    if (entry.getName().equals(name)) {
                        return entry;
                    }
                }
            }
        }
        return null;
    }
}
