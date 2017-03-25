package eiteam.esteemedinnovation.api.book;

import codechicken.lib.util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A BookEntry is a set of pages tied to a single concept.
 */
public class BookEntry implements BookPiece {
    private final String name;
    private final List<BookPage> pages = new ArrayList<>();

    public BookEntry(String name, BookPage... pages) {
        this.name = name;
        appendPages(pages);
    }

    /**
     * @return The unlocalized name of the entry.
     */
    public String getName() {
        return name;
    }

    /**
     * @return An array of all of the pages in this entry.
     */
    public List<BookPage> getPages() {
        return pages;
    }

    /**
     * Appends the provided pages to the entry.
     * @param pages The pages to add to the entry.
     */
    public void appendPages(BookPage... pages) {
        ArrayUtils.addAllNoNull(pages, this.pages);
    }
}
