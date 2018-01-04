package eiteam.esteemedinnovation.book;

import eiteam.esteemedinnovation.api.Constants;
import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.commons.CrossMod;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.commons.util.MathUtility;
import eiteam.esteemedinnovation.misc.integration.EnchiridionIntegration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Mouse;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static eiteam.esteemedinnovation.book.BookModule.BOOK;

public class GuiJournal extends GuiScreen implements eiteam.esteemedinnovation.api.book.GuiJournal {
    private static final ResourceLocation BOOK_RIGHT_GUI_TEXTURES = new ResourceLocation(Constants.API_MODID, "textures/gui/book_right.png");
    private static final ResourceLocation BOOK_FRONT_TEXTURES = new ResourceLocation(Constants.API_MODID, "textures/gui/book_front.png");
    private static final ResourceLocation BOOK_LEFT_GUI_TEXTURES = new ResourceLocation(Constants.API_MODID, "textures/gui/book_left.png");
    private int bookTotalPages = 1;
    private int currPage;
    private int lastIndexPage;
    public String viewing = "";
    @Nonnull
    private ItemStack book = ItemStack.EMPTY;
    private boolean mustReleaseMouse;
    public static final int BOOK_IMAGE_WIDTH = 192;
    public static final int BOOK_IMAGE_HEIGHT = 192;
    private GuiJournal.NextPageButton buttonNextPage;
    private GuiJournal.NextPageButton buttonPreviousPage;
    private final List<String> sections;
    /**
     * The Ideas section is generated every time you open the book to that section, not during game load, so it cannot
     * be a real section. It is handled as a special case.
     */
    private static final String FAKE_SECTION_IDEAS = "section.Ideas.name";

    public GuiJournal(EntityPlayer player) {
        sections = new ArrayList<>();
        for (BookSection section : BookPageRegistry.sections.values()) {
            if (section.isHidden(player)) {
                continue;
            }
            if (section.isUnlocked(player)) {
                int pages = section.getCategories().size();
                for (int s = 0; s < MathHelper.ceil(pages / 9.0F); s++) {
                    sections.add(section.getName() + (s == 0 ? "" : s));
                }
            }
        }
        sections.add(FAKE_SECTION_IDEAS);
        bookTotalPages = MathHelper.ceil(sections.size() / 2F) + 1;
        if (!viewing.isEmpty()) {
            BookEntry entry = BookPageRegistry.getEntryFromName(viewing);
            if (entry != null) {
                bookTotalPages = MathHelper.ceil(entry.getPages().size() / 2F);
            }
        }
        ItemStack active = player.getHeldItemMainhand();
        if (active.getItem() instanceof ItemEsteemedInnovationJournal) {
            book = active;
        } else {
            if (CrossMod.ENCHIRIDION) {
                book = EnchiridionIntegration.findBook(BOOK, player);
            }
            for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if (stack.getItem() instanceof ItemEsteemedInnovationJournal) {
                    book = stack;
                    break;
                }
            }
        }
    }

    private int getWidth() {
        return (width - BOOK_IMAGE_WIDTH) / 2;
    }

    private int getHeight() {
        return (height - BOOK_IMAGE_HEIGHT) / 2;
    }

    @Override
    public Minecraft getMC() {
        return mc;
    }

    @Override
    public String getCurrentEntry() {
        return viewing;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        buttonList.clear();

        int i = (width - BOOK_IMAGE_WIDTH) / 2;
        int b0 = (height - BOOK_IMAGE_HEIGHT) / 2;
        buttonNextPage = new GuiJournal.NextPageButton(1, i + 120 + 67, b0 + 154, true);
        buttonPreviousPage = new GuiJournal.NextPageButton(2, i + 38 - 67, b0 + 154, false);
        buttonList.add(buttonNextPage);
        buttonList.add(buttonPreviousPage);

        updateButtons();
    }

    public void updateButtons() {
        buttonNextPage.visible = (currPage < bookTotalPages - 1);
        buttonPreviousPage.visible = currPage > 0;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button.id == 0) {
                mc.displayGuiScreen(null);
            } else if (button.id == 1) {
                if (currPage < bookTotalPages - 1) {
                    ++currPage;
                }
            } else if (button.id == 2) {
                if (currPage > 0) {
                    --currPage;
                }
            }
//            else if (button.id == 3)
//            {
//            	this.viewing = "";
//            	this.currPage = lastIndexPage;
//            	int i = 0;
//        		String lastCategory = "";
//        		boolean canDo = true;
//        		this.bookTotalPages = MathHelper.ceil(GeneralRegistry.categories.size()/2F)+1;
//                this.updateButtons();
//            }

            if (button instanceof GuiButtonSelect) {
                GuiButtonSelect buttonSelect = (GuiButtonSelect) button;
                viewing = buttonSelect.name.startsWith("#") ? buttonSelect.name.substring(1) : buttonSelect.name;
                int numPages = -1;
                BookCategory category = BookPageRegistry.getCategoryFromName(viewing);
                if (category != null && category.isUnlocked(mc.player)) {
                    numPages = category.getAllVisiblePages(mc.player).length;
                }
                if (numPages != -1) {
                    lastIndexPage = currPage;
                    currPage = 0;
                    bookTotalPages = MathHelper.ceil(numPages / 2F);
                    updateButtons();
                    mustReleaseMouse = true;
                }
            }

            initGui();
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1 && !viewing.isEmpty()) {
            viewing = "";
            currPage = lastIndexPage;
            bookTotalPages = MathHelper.ceil(sections.size() / 2F) + 1;
            updateButtons();
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    private void handleDWheelMovement() {
        int dWheelRecentMovement = Mouse.getDWheel();
        if (dWheelRecentMovement < 0 && buttonNextPage.visible) {
            actionPerformed(buttonNextPage);
        }
        if (dWheelRecentMovement > 0 && buttonPreviousPage.visible) {
            actionPerformed(buttonPreviousPage);
        }
    }

    private boolean isOnFrontPage() {
        return currPage == 0 && viewing.isEmpty();
    }

    private void drawBookCover(int width, int height) {
        mc.getTextureManager().bindTexture(BOOK_FRONT_TEXTURES);
        drawTexturedModalRect(width + 67, height, 0, 0, BOOK_IMAGE_WIDTH, BOOK_IMAGE_HEIGHT);

        String bookTitle = book.getDisplayName();
        int titleWidth = fontRenderer.getStringWidth(bookTitle);
        fontRenderer.drawStringWithShadow(bookTitle, width + 67 + BOOK_IMAGE_WIDTH / 2 - titleWidth / 2 - 3, height + 80, 0xC19E51);

        String bookSubtitle = I18n.format(Constants.EI_MODID + ".book.info");
        int subtitleWidth = fontRenderer.getStringWidth(bookSubtitle);
        fontRenderer.drawString("\u00A7o" + bookSubtitle, width + 67 + BOOK_IMAGE_WIDTH / 2 - subtitleWidth / 2 - 3, height + 90, 0xC19E51);

        String author = I18n.format(Constants.EI_MODID + ".book.by", mc.player.getDisplayNameString());
        int authorWidth = fontRenderer.getStringWidth(author);
        fontRenderer.drawString("\u00A7o" + author, width + 67 + BOOK_IMAGE_WIDTH / 2 - authorWidth / 2 - 3, height + 105, 0xC19E51);
    }

    private void drawBookPagesBase(int width, int height) {
        mc.getTextureManager().bindTexture(BOOK_LEFT_GUI_TEXTURES);
        drawTexturedModalRect(width - 67, height, 0, 0, BOOK_IMAGE_WIDTH, BOOK_IMAGE_HEIGHT);
        mc.getTextureManager().bindTexture(BOOK_RIGHT_GUI_TEXTURES);
        drawTexturedModalRect(width + 67, height, 0, 0, BOOK_IMAGE_WIDTH, BOOK_IMAGE_HEIGHT);

        int pageIncr = viewing.isEmpty() ? -2 : 0;

        String pageOfLeft = I18n.format("book.pageIndicator", (currPage - 1) * 2 + 3 + pageIncr, bookTotalPages * 2 + pageIncr);
        int pageOfLeftWidth = fontRenderer.getStringWidth(pageOfLeft);
        fontRenderer.drawString(pageOfLeft, width + pageOfLeftWidth - BOOK_IMAGE_WIDTH + 54 + 67, height + 16, 0x3F3F3F);

        String pageOfRight = I18n.format("book.pageIndicator", (currPage - 1) * 2 + 4 + pageIncr, bookTotalPages * 2 + pageIncr);
        int pageOfRightWidth = fontRenderer.getStringWidth(pageOfRight);
        fontRenderer.drawString(pageOfRight, width - pageOfRightWidth + BOOK_IMAGE_WIDTH - 44 + 67, height + 16, 0x3F3F3F);
    }

    private void drawTitleOnTopOfScreen(int width, int height) {
        String title = book.getDisplayName();
        int titleWidth = fontRenderer.getStringWidth(title);
        fontRenderer.drawStringWithShadow(title, width + BOOK_IMAGE_WIDTH / 2 - titleWidth / 2 - 3, height - 15, 0xFFFFFF);
    }

    private void drawIndexString(int width, int height) {
        String indexString = I18n.format("esteemedinnovation.book.index");
        int indexWidth = fontRenderer.getStringWidth(indexString);
        fontRenderer.drawString("\u00A7l" + "\u00A7n" + indexString, width - 67 + BOOK_IMAGE_WIDTH / 2 - indexWidth / 2 - 5, height + 30, 0x3F3F3F);
        fontRenderer.drawString("\u00A7l" + "\u00A7n" + indexString, width + 67 + BOOK_IMAGE_WIDTH / 2 - indexWidth / 2 - 5, height + 30, 0x3F3F3F);
    }

    private void populateButtonListWithCategories(boolean isRightPage, int width, int height) {
        String sectionName = sections.get(((currPage - 1) * 2) + (isRightPage ? 1 : 0));
        int offset = 0;
        if (Character.isDigit(sectionName.charAt(sectionName.length() - 1))) {
            offset = 9 * Integer.parseInt(String.valueOf(sectionName.charAt(sectionName.length() - 1)));
            sectionName = sectionName.substring(0, sectionName.length() - 1);
        }
        String s = I18n.format(sectionName);
        int widthOffset = isRightPage ? 67 : -67;
        fontRenderer.drawString("\u00A7n" + s, width + 40 + widthOffset, 44 + height, 0x3F3F3F);
        if (sectionName.equals(FAKE_SECTION_IDEAS)) {
            int hintNum = 1;
            for (String hint : BookModule.ALL_UNLOCALIZED_PIECES_WITH_HINTS) {
                String name = hint + ".name";
                BookPiece piece = BookPageRegistry.getFirstPieceFromName(name);
                StringBuilder hintBuilder = new StringBuilder()
                  .append("\u2022 ");
                if (piece != null && piece.isUnlocked(mc.player)) {
                    hintBuilder.append(TextFormatting.STRIKETHROUGH);
                }
                hintBuilder.append(TextFormatting.ITALIC)
                  .append(I18n.format(hint + ".hint"));

                List<String> splitHint = fontRenderer.listFormattedStringToWidth(hintBuilder.toString(), 100);
                int i = 10 * hintNum;
                for (String splitHintStr : splitHint) {
                    fontRenderer.drawString(splitHintStr, width + 50 + widthOffset, height + 44 + i, 0x3F3F3F);
                    i += 10;
                }
                hintNum++;
            }
        } else {
            BookSection section = BookPageRegistry.getSectionFromName(sectionName);
            if (section != null && section.isUnlocked(mc.player)) {
                int offsetCounter = 0;
                int i = 10;
                for (BookCategory cat : section.getCategories()) {
                    if (!cat.isUnlocked(mc.player)) {
                        continue;
                    }
                    offsetCounter++;
                    if (offsetCounter > offset && offsetCounter < offset + 10) {
                        s = cat.getName();
                        buttonList.add(new GuiButtonSelect(4, width + 50 + widthOffset, height + 44 + i, 110, 10, s));
                        i += 10;
                    }
                }
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        handleDWheelMovement();
        if (mustReleaseMouse && !Mouse.isButtonDown(0)) {
            mustReleaseMouse = false;
        }
        GlStateManager.color(1, 1, 1, 1);
        int width = getWidth();
        int height = getHeight();
        boolean prevUnicode = fontRenderer.getUnicodeFlag();
        fontRenderer.setUnicodeFlag(true);
        if (isOnFrontPage()) {
            drawBookCover(width, height);
        } else {
            drawBookPagesBase(width, height);
        }

        if (viewing.isEmpty()) {
            if (currPage > 0) {
                drawIndexString(width, height);

                // Remove and replace category buttons.
                buttonList.removeIf(button -> button instanceof GuiButtonSelect);
                populateButtonListWithCategories(false, width, height);
                if (sections.size() > 1 + ((currPage - 1) * 2)) {
                    populateButtonListWithCategories(true, width, height);
                }
            }
            fontRenderer.setUnicodeFlag(prevUnicode);
            super.drawScreen(mouseX, mouseY, partialTicks);
        } else {
            fontRenderer.setUnicodeFlag(prevUnicode);
            super.drawScreen(mouseX, mouseY, partialTicks);
            fontRenderer.setUnicodeFlag(true);
            BookCategory category = BookPageRegistry.getCategoryFromName(viewing);
            BookPage[] pages = null;
            if (category == null) {
                BookEntry entry = BookPageRegistry.getEntryFromName(viewing);
                if (entry != null) {
                    pages = entry.getPages().toArray(new BookPage[entry.getPages().size()]);
                }
            } else {
                pages = category.getAllVisiblePages(mc.player);
            }
            int leftPageIndex = currPage * 2;
            if (pages != null && pages.length > leftPageIndex) {
                bookTotalPages = MathHelper.ceil(pages.length / 2F);
                GlStateManager.enableBlend();
                BookPage leftPage = pages[leftPageIndex];
                GlStateManager.enableBlend();
                GlStateManager.pushMatrix();
                leftPage.renderPage(width - 67, height, fontRenderer, this, itemRender, currPage == 0, mouseX, mouseY);
                GlStateManager.popMatrix();
                GlStateManager.enableBlend();
                int rightPageIndex = leftPageIndex + 1;
                if (pages.length > rightPageIndex) {
                    BookPage rightPage = pages[rightPageIndex];
                    GlStateManager.pushMatrix();
                    rightPage.renderPage(width + 67, height, fontRenderer, this, itemRender, false, mouseX, mouseY);
                    GlStateManager.popMatrix();

                    rightPage.renderPageAfter(width + 67, height, fontRenderer, this, itemRender, false, mouseX, mouseY);
                }
                leftPage.renderPageAfter(width - 67, height, fontRenderer, this, itemRender, currPage == 0, mouseX, mouseY);
                updateButtons();
            }
            fontRenderer.setUnicodeFlag(prevUnicode);
        }
        drawTitleOnTopOfScreen(width, height);
    }

    @Override
    public void renderToolTip(ItemStack stack0, int mouseX, int mouseY, boolean renderHyperlink) {
        List<String> list = stack0.getTooltip(mc.player, mc.gameSettings.advancedItemTooltips);
        zLevel = 1.0F;
        for (int k = 0; k < list.size(); ++k) {
            if (k == 0) {
                list.set(k, stack0.getRarity().rarityColor + list.get(k));
            } else {
                list.set(k, TextFormatting.GRAY + list.get(k));
            }
        }
        if (renderHyperlink) {
            for (ItemStack stack : BookPageRegistry.bookRecipes.keySet()) {
                if (stack.getItem() == stack0.getItem() && stack.getItemDamage() == stack0.getItemDamage()) {
                    list.add(TextFormatting.ITALIC + "" + TextFormatting.GRAY + I18n.format("esteemedinnovation.book.clickme"));
                }
            }
        }

        FontRenderer font = stack0.getItem().getFontRenderer(stack0);
        drawHoveringText(list, mouseX, mouseY);
        drawHoveringText(list, mouseX, mouseY, (font == null ? fontRenderer : font));
        zLevel = 0.0F;
    }

    @Override
    public void renderText(String str, int mouseX, int mouseY) {
        List<String> list = new ArrayList<>();
        list.add(I18n.format(str));
        drawHoveringText(list, mouseX, mouseY);
        drawHoveringText(list, mouseX, mouseY, fontRenderer);
    }

    @Override
    public void itemClicked(ItemStack itemStack) {
         for (ItemStack stack : BookPageRegistry.bookRecipes.keySet()) {
            if (!mustReleaseMouse && stack.getItem() == itemStack.getItem() && stack.getItemDamage() == itemStack.getItemDamage()) {
                BookEntry entry = BookPageRegistry.getEntryFromName(viewing);
                if (entry != null && entry.isUnlocked(mc.player)) {
                    viewing = BookPageRegistry.bookRecipes.get(stack).getLeft();
                    currPage = MathHelper.floor(BookPageRegistry.bookRecipes.get(stack).getRight() / 2.0F);
                    bookTotalPages = MathHelper.ceil(entry.getPages().size() / 2F);
                    mustReleaseMouse = true;
                    updateButtons();
                }
            }
        }
    }

    private static class NextPageButton extends GuiButton {
        /**
         * True if it is the next page button, false if it is the previous page button.
         */
        private final boolean isNext;

        NextPageButton(int buttonID, int x, int y, boolean isNext) {
            super(buttonID, x, y, 23, 13, "");
            this.isNext = isNext;
        }

        @Override
        public void drawButton(@Nonnull Minecraft minecraft, int mouseX, int mouseY) {
            if (visible) {
                boolean isMouseInBookBounds = MathUtility.isBetweenMinInclusive(x, mouseX, x + width) &&
                  MathUtility.isBetweenMinInclusive(y, mouseY, y + height);
                GlStateManager.color(1, 1, 1, 1);
                minecraft.getTextureManager().bindTexture(BOOK_RIGHT_GUI_TEXTURES);
                int k = 0;
                int l = 192;

                if (isMouseInBookBounds) {
                    k += 23;
                }

                if (!isNext) {
                    l += 13;
                }

                drawTexturedModalRect(x, y, k, l, 23, 13);
            }
        }
    }

    private class GuiButtonSelect extends GuiButton {
        public String name;

        GuiButtonSelect(int buttonID, int x, int y, int width, int height, String buttonText) {
            super(buttonID, x, y, width, height, I18n.format(buttonText.contains("#") ? buttonText.substring(1) : buttonText));
            name = buttonText;
        }

        void drawCenteredStringWithoutShadow(FontRenderer fontRenderer, String str, int x, int y, int color) {
            fontRenderer.drawString(str, x, y, color);
        }

        @Override
        public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY) {
            if (visible) {
                FontRenderer fontRenderer = mc.fontRenderer;
                GlStateManager.color(1, 1, 1, 1);
                boolean isMouseOverBook = MathUtility.isBetweenMinInclusive(x, mouseX, x + width) &&
                  MathUtility.isBetweenMinInclusive(y, mouseY, y + height);
                mouseDragged(mc, mouseX, mouseY);
                boolean unicodeBeforeSet = fontRenderer.getUnicodeFlag();
                fontRenderer.setUnicodeFlag(true);
                drawCenteredStringWithoutShadow(fontRenderer, "\u2022 " + displayString, x + (isMouseOverBook ? 1 : 0),
                  y + (height - 8) / 2, 0x3F3F3F);
                fontRenderer.setUnicodeFlag(unicodeBeforeSet);
            }
        }
    }

    /**
     * Opens the entry for the ItemStack
     * @param recipeStack The ItemStack to get the recipe entry for.
     * @param player The player opening the GUI.
     */
    public static void openRecipeFor(ItemStack recipeStack, EntityPlayer player) {
        player.openGui(EsteemedInnovation.instance, 1, player.world, 0, 0, 0);
        GuiScreen gui = Minecraft.getMinecraft().currentScreen;
        if (!(gui instanceof GuiJournal)) {
            return;
        }
        GuiJournal journal = (GuiJournal) gui;
        Pair<String, Integer> page = BookPageRegistry.bookRecipes.get(recipeStack);
        String pageName = page.getLeft();
        BookEntry entry = BookPageRegistry.getEntryFromName(pageName);
        if (entry != null) {
            journal.viewing = pageName;
            int pageNumber = page.getRight();
            journal.currPage = 0;
            journal.lastIndexPage = 1;
            journal.bookTotalPages = pageNumber;
            journal.updateButtons();
        }
    }
}
