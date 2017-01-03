package eiteam.esteemedinnovation.book;

import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import static eiteam.esteemedinnovation.commons.EsteemedInnovation.BASICS_CATEGORY;
import static eiteam.esteemedinnovation.commons.EsteemedInnovation.channel;
import static eiteam.esteemedinnovation.commons.OreDictEntries.ORE_COPPER;
import static eiteam.esteemedinnovation.commons.OreDictEntries.ORE_ZINC;

public class BookModule extends ContentModule {
    public static Item BOOK;

    @Override
    public void create(Side side) {
        channel.registerMessage(BookPieceUnlockedStateChangePacketHandler.class, BookPieceUnlockedStateChangePacket.class, 4, Side.CLIENT);
        BOOK = setup(new ItemEsteemedInnovationJournal(), "book");
    }

    @Override
    public void recipes(Side side) {
        BookRecipeRegistry.addRecipe("book", new ShapelessOreRecipe(BOOK, Items.BOOK,
          ORE_COPPER, ORE_ZINC));
    }

    @Override
    public void preInitClient() {
        registerModel(BOOK);
    }

    @Override
    public void finish(Side side) {
        BookPageRegistry.addEntryToCategory(BASICS_CATEGORY, new BookEntry("research.Book.name",
          new BookPageItem("research.Book.name", "research.Book.0", new ItemStack(BOOK)),
          new BookPageCrafting("", "book")));
    }
}
