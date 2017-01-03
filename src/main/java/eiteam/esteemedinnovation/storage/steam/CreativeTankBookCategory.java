package eiteam.esteemedinnovation.storage.steam;

import eiteam.esteemedinnovation.api.book.BookCategory;
import eiteam.esteemedinnovation.api.book.BookEntry;
import eiteam.esteemedinnovation.api.book.BookPageItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class CreativeTankBookCategory extends BookCategory {
    public static final String NAME = "category.NOTREAL.name";
    public CreativeTankBookCategory() {
        super(NAME, new BookEntry[] {
          new BookEntry("research.CreativeTank.name",
            new BookPageItem("research.CreativeTank.name", "research.CreativeTank.0", new ItemStack(Items.BOWL)))});
    }

    @Override
    public boolean isHidden(EntityPlayer player) {
        return true;
    }
}
