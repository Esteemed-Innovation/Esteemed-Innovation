package eiteam.esteemedinnovation.api.book;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * A BookCategory is a set of BookEntries tied to a single concept.
 * For example, the Helmet Slot BookCategory might contain some Entries about various Helmet Slot upgrades.
 *
 * Here is an example tree:
 * * Tool: {@link BookSection}
 * ** Piece A: BookCategory
 * *** Upgrade X: {@link BookEntry}
 * **** Description page: {@link BookPage}
 * **** Crafting page: {@link BookPage}
 * *** Upgrade Y: {@link BookEntry}
 * **** Description page: {@link BookPage}
 * **** Crafting page: {@link BookPage}
 */
public class BookSection implements BookPiece {
    @Nonnull
    private final String name;
    private final TreeMap<Integer, BookCategory> categories = new TreeMap<>();

    public BookSection(@Nonnull String name, BookCategory... categories) {
        this.name = name;
        addCategories(categories);
    }

    /**
     * Adds a set of categories to this section.
     */
    public void addCategories(BookCategory... categories) {
        int max = this.categories.isEmpty() ? 0 : this.categories.lastKey() + 1;
        int catIndex = 0;
        for (int i = max; i < max + categories.length; i++) {
            BookCategory cat = categories[catIndex];
            if (cat != null) {
                this.categories.put(i, cat);
            }
            catIndex++;
        }
    }

    /**
     * Position-sensitive version of {@link #addCategories(BookCategory...)}.
     */
    public void addCategory(int position, @Nonnull BookCategory category) {
        categories.put(position, category);
    }

    @Nonnull
    public String getName() {
        return name;
    }

    public Collection<BookCategory> getCategories() {
        return categories.values();
    }
}
