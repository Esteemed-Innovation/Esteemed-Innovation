package eiteam.esteemedinnovation.api.book;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

/**
 * The implementer of this *must* have a superclass of Gui.
 */
public interface GuiJournal {
    int BOOK_IMAGE_WIDTH = 192;
    void renderToolTip(ItemStack stack, int mouseX, int mouseY, boolean renderHyperlink);
    void itemClicked(ItemStack itemStack);

    /**
     * @return The instance of Minecraft for this Gui.
     */
    Minecraft getMC();
    void renderText(String str, int mouseX, int mouseY);

    /**
     * @return The entry that the player is currently viewing
     */
    String getCurrentEntry();
}
