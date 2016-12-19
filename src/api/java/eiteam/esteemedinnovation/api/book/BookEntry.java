package eiteam.esteemedinnovation.api.book;

import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A BookEntry is a set of pages tied to a single concept.
 */
public class BookEntry implements BookPiece {
    private final String entryName;
    private BookPage[] pages;

    public BookEntry(String entryName, BookPage... pages) {
        this.entryName = entryName;
        this.pages = pages;
    }

    /**
     * @return The unlocalized name of the entry.
     */
    public String getEntryName() {
        return entryName;
    }

    /**
     * @return An array of all of the pages in this entry.
     */
    public BookPage[] getPages() {
        return pages;
    }

    /**
     * Appends the provided pages to the entry.
     * @param pages The pages to add to the entry.
     */
    public void appendPages(BookPage... pages) {
        this.pages = ArrayUtils.addAll(this.pages, pages);
    }
}
