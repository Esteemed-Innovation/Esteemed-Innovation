package eiteam.esteemedinnovation.book;

import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.HashSet;
import java.util.Set;

import static eiteam.esteemedinnovation.commons.EsteemedInnovation.channel;
import static eiteam.esteemedinnovation.commons.OreDictEntries.ORE_COPPER;
import static eiteam.esteemedinnovation.commons.OreDictEntries.ORE_ZINC;

public class BookModule extends ContentModule {
    public static Item BOOK;
    /**
     * A list of all the pieces' base names that have hints. This does not contain ".name" or ".hint". For example,
     * the MetalCasting section would be in this list as "section.MetalCasting".
     */
    public static final Set<String> ALL_UNLOCALIZED_PIECES_WITH_HINTS = new HashSet<>();

    @Override
    public void create(Side side) {
        channel.registerMessage(BookPieceUnlockedStateChangePacketHandler.class, BookPieceUnlockedStateChangePacket.class, 4, Side.CLIENT);
    }

    @Override
    public void registerItems(RegistryEvent.Register<Item> event) {
        BOOK = setup(event, new ItemEsteemedInnovationJournal(), "book");
    }

    @Override
    public void recipes(Side side) {
        //TODO: transfer recipes to json
        //BookRecipeRegistry.addRecipe("book", new ShapelessOreRecipe(BOOK, Items.BOOK, ORE_COPPER, ORE_ZINC));
    }

    public static void generateAllHints() {
        for (BookSection section : BookPageRegistry.sections.values()) {
            addHint(section);
            for (BookCategory category : section.getCategories()) {
                addHint(category);
                for (BookEntry entry : category.getEntries()) {
                    addHint(entry);
                }
            }
        }
    }

    private static void addHint(BookPiece bookPiece) {
        String hint = bookPiece.getUnlocalizedHint();
        if (hint != null) {
            hint = hint.replace(".hint", "");
            ALL_UNLOCALIZED_PIECES_WITH_HINTS.add(hint);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void preInitClient() {
        registerModel(BOOK);
    }
}
