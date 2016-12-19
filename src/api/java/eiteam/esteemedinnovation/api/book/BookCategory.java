package eiteam.esteemedinnovation.api.book;

import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A BookCategory is a set of BookEntries and sub BookCategories tied to a single concept.
 * For example, the Exosuit BookCategory might contain some Entries about using an Exosuit, but it also might contain
 * other BookCategories for all the upgrades.
 *
 * Here is an example tree:
 * * Tool: BookCategory
 * ** Piece A: BookCategory
 * *** Upgrade X: BookEntry
 * **** Description page: BookPage
 * **** Crafting page: BookPage
 * *** Upgrade Y: BookEntry
 * **** Description page: BookPage
 * **** Crafting page: BookPage
 */
public class BookCategory implements BookPiece {
    private final String categoryName;
    private BookEntry[] entries;
    private BookCategory[] subcategories;

    public BookCategory(String categoryName, BookEntry[] entries, BookCategory... subcategories) {
        this.categoryName = categoryName;
        this.entries = entries;
        this.subcategories = subcategories;
    }

    /**
     * @return The unlocalized name of the category
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * @return An array of all the entries in the category (shallow, or not including the entries of the subcategories).
     */
    public BookEntry[] getEntries() {
        return entries;
    }

    /**
     * @return An array of all the subcategories in the category (shallow, or not including subsubcategories).
     */
    public BookCategory[] getSubcategories() {
        return subcategories;
    }

    /**
     * @return A flat array of all the pages within this category, including all of its subcategories.
     */
    public BookPage[] getAllPages() {
        List<BookPage> pages = new ArrayList<>();
        pages.addAll((Collection<BookPage>) Arrays.stream(getEntries()).flatMap(entry -> Arrays.stream(entry.getPages())).collect(Collectors.toList()));
        for (BookCategory subcategory : getSubcategories()) {
            pages.addAll(Arrays.asList(subcategory.getAllPages()));
        }
        return pages.toArray(new BookPage[pages.size()]);
    }

    /**
     * @param player The player who is using the book
     * @return Like getAllPages, but filters out all entries that the player that cannot view.
     */
    public BookPage[] getAllVisiblePages(EntityPlayer player) {
        if (isHidden(player) || !isUnlocked(player)) {
            return new BookPage[] {};
        }
        List<BookPage> pages = Arrays.stream(getEntries())
          .filter(entry -> entry.isUnlocked(player) && !entry.isHidden(player))
          .flatMap(entry -> Arrays.stream(entry.getPages()))
          .collect(Collectors.toList());
        return pages.toArray(new BookPage[pages.size()]);
    }

    /**
     * Appends the provided entries to the category.
     * @param entries The entries to add to the category.
     */
    public void appendEntries(BookEntry... entries) {
        this.entries = ArrayUtils.addAll(this.entries, entries);
    }

    /**
     * Appends subcategories to the list of subcategories.
     * @param categories The subcategories to add to this category.
     */
    public void appendCategories(BookCategory... categories) {
        subcategories = ArrayUtils.addAll(subcategories, categories);
    }

    /**
     * A simple helper factory class for constructing categories throughout code flow. This is useful for when the
     * existence of entries/subcategories is dependent on configuration options, for example.
     */
    public static class Factory {
        private final String categoryName;
        private List<BookCategory> subcategories = new ArrayList<>();
        private List<BookEntry> entries = new ArrayList<>();

        public Factory(String categoryName) {
            this.categoryName = categoryName;
        }

        /**
         * Adds the provided category to the list of subcategories.
         * @param category The category to append
         * @return The factory
         */
        public Factory append(BookCategory category) {
            subcategories.add(category);
            return this;
        }

        /**
         * Adds the provided entry to the list of entries.
         * @param entry The entry to append
         * @return The factory
         */
        public Factory append(BookEntry entry) {
            entries.add(entry);
            return this;
        }

        /**
         * Constructs a new BookCategory based on the preconstructed values in the factory.
         * @return A new BookCategory based on the factory's values.
         */
        public BookCategory build() {
            return new BookCategory(categoryName, entries.toArray(new BookEntry[entries.size()]),
              subcategories.toArray(new BookCategory[subcategories.size()]));
        }
    }
}
