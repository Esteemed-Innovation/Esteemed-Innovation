package eiteam.esteemedinnovation.api.book;

import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A BookSection is a set of BookCategories tied to a single concept.
 * For example, the Steam Power BookSection might contain some Categories about various steam machines.
 */
public class BookCategory implements BookPiece {
    @Nonnull
    private final String name;
    private final TreeMap<Integer, BookEntry> entries = new TreeMap<>();

    public BookCategory(@Nonnull String name, BookEntry... entries) {
        this.name = name;
        appendEntries(entries);
    }

    /**
     * @return The unlocalized name of the category
     */
    @Nonnull
    public String getName() {
        return name;
    }

    /**
     * @return An array of all the entries in the category (shallow, or not including the entries of the subcategories).
     */
    public Collection<BookEntry> getEntries() {
        return entries.values();
    }

    /**
     * @return A flat array of all the pages within this category, including all of its subcategories.
     */
    public BookPage[] getAllPages() {
        List<BookPage> pages = getEntries().stream()
          .flatMap(entry -> entry.getPages().stream())
          .collect(Collectors.toList());
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
        List<BookPage> pages = getEntries().stream()
          .filter(entry -> entry.isUnlocked(player) && !entry.isHidden(player))
          .flatMap(entry -> entry.getPages().stream())
          .collect(Collectors.toList());
        return pages.toArray(new BookPage[pages.size()]);
    }

    /**
     * Appends the provided entries to the category.
     * @param entries The entries to add to the category.
     */
    public void appendEntries(BookEntry... entries) {
        int max = this.entries.isEmpty() ? 0 : this.entries.lastKey() + 1;
        int entryIndex = 0;
        for (int i = max; i < max + entries.length; i++) {
            BookEntry entry = entries[entryIndex];
            if (entry != null) {
                this.entries.put(i, entry);
            }
            entryIndex++;
        }
    }

    /**
     * Position-sensitive version of {@link #appendEntries(BookEntry...)}.
     */
    public void insertEntry(int position, @Nonnull BookEntry entry) {
        entries.put(position, entry);
    }
}
