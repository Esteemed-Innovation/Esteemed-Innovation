package eiteam.esteemedinnovation.gui;

import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.init.items.tools.GadgetItems;
import eiteam.esteemedinnovation.init.misc.integration.CrossMod;
import eiteam.esteemedinnovation.init.misc.integration.EnchiridionIntegration;
import eiteam.esteemedinnovation.item.ItemEsteemedInnovationJournal;
import eiteam.esteemedinnovation.misc.MathUtility;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class GuiJournal extends GuiScreen implements IGuiJournal {
    private static final ResourceLocation BOOK_GUI_TEXTURES = new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/gui/book.png");
    private static final ResourceLocation BOOK_FRONT_TEXTURES = new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/gui/book2.png");
    private static final ResourceLocation REVERSE_BOOK_GUI_TEXTURES = new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/gui/bookReverse.png");
    private int bookTotalPages = 1;
    private int currPage;
    private int lastIndexPage;
    public String viewing = "";
    private ItemStack book;
    private boolean mustReleaseMouse;
    public static final int BOOK_IMAGE_WIDTH = 192;
    public static final int BOOK_IMAGE_HEIGHT = 192;
    private GuiJournal.NextPageButton buttonNextPage;
    private GuiJournal.NextPageButton buttonPreviousPage;
    private List<String> categories;

    public GuiJournal(EntityPlayer player) {
        categories = new ArrayList<>();
        for (BookCategory cat : BookPageRegistry.categories) {
            if (cat.isHidden(player) || !cat.isUnlocked(player)) {
                continue;
            }
            int pages = Arrays.stream(cat.getEntries()).filter(entry -> entry.getPages().length > 0).toArray().length;
            for (int s = 0; s < MathHelper.ceiling_float_int(pages / 9.0F); s++) {
                categories.add(cat.getCategoryName() + (s == 0 ? "" : s));
            }
        }
        bookTotalPages = MathHelper.ceiling_float_int(categories.size() / 2F) + 1;
        if (!viewing.isEmpty()) {
            BookEntry entry = BookPageRegistry.getEntryFromName(viewing);
            if (entry != null) {
                bookTotalPages = MathHelper.ceiling_float_int(entry.getPages().length / 2F);
            }
        }
        ItemStack active = player.getHeldItemMainhand();
        if (active != null && active.getItem() instanceof ItemEsteemedInnovationJournal) {
            book = active;
        } else {
            if (CrossMod.ENCHIRIDION) {
                book = EnchiridionIntegration.findBook(GadgetItems.Items.BOOK.getItem(), player);
            }
            for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if (stack != null && stack.getItem() instanceof ItemEsteemedInnovationJournal) {
                    book = stack;
                    break;
                }
            }
        }
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
    public void updateScreen() {
        super.updateScreen();
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
//        		this.bookTotalPages = MathHelper.ceiling_float_int(GeneralRegistry.categories.size()/2F)+1;
//                this.updateButtons();
//            }

            if (button instanceof GuiButtonSelect) {
                GuiButtonSelect buttonSelect = (GuiButtonSelect) button;
                viewing = buttonSelect.name.substring(0, 1).equals("#") ? buttonSelect.name.substring(1) : buttonSelect.name;
                BookEntry entry = BookPageRegistry.getEntryFromName(viewing);
                int numPages = -1;
                if (entry == null) {
                    BookCategory category = BookPageRegistry.getCategoryFromName(viewing);
                    if (category != null && category.isUnlocked(mc.thePlayer)) {
                        numPages = category.getAllVisiblePages(mc.thePlayer).length;
                    }
                } else if (entry.isUnlocked(mc.thePlayer)) {
                    numPages = entry.getPages().length;
                }
                if (numPages != -1) {
                    lastIndexPage = currPage;
                    currPage = 0;
                    bookTotalPages = MathHelper.ceiling_float_int(numPages / 2F);
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
            bookTotalPages = MathHelper.ceiling_float_int(categories.size() / 2F) + 1;
            updateButtons();
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int dWheelRecentMovement = Mouse.getDWheel();
        if (dWheelRecentMovement < 0 && buttonNextPage.visible) {
            actionPerformed(buttonNextPage);
        }
        if (dWheelRecentMovement > 0 && buttonPreviousPage.visible) {
            actionPerformed(buttonPreviousPage);
        }
        if (mustReleaseMouse && !Mouse.isButtonDown(0)) {
            mustReleaseMouse = false;
        }
        GlStateManager.color(1, 1, 1, 1);
        int k = (width - BOOK_IMAGE_WIDTH) / 2;
        int b0 = (height - BOOK_IMAGE_HEIGHT) / 2;
        if (currPage == 0 && viewing.isEmpty()) {
            mc.getTextureManager().bindTexture(BOOK_FRONT_TEXTURES);
            drawTexturedModalRect(k + 67, b0, 0, 0, BOOK_IMAGE_WIDTH, BOOK_IMAGE_HEIGHT);
        } else {
            mc.getTextureManager().bindTexture(REVERSE_BOOK_GUI_TEXTURES);
            drawTexturedModalRect(k - 67, b0, 0, 0, BOOK_IMAGE_WIDTH, BOOK_IMAGE_HEIGHT);
            mc.getTextureManager().bindTexture(BOOK_GUI_TEXTURES);
            drawTexturedModalRect(k + 67, b0, 0, 0, BOOK_IMAGE_WIDTH, BOOK_IMAGE_HEIGHT);
        }


        String s = book.getDisplayName();
        int l = fontRendererObj.getStringWidth(s);

        fontRendererObj.drawStringWithShadow(s, k + BOOK_IMAGE_WIDTH / 2 - l / 2 - 3, b0 - 15, 0xFFFFFF);

        boolean unicode = fontRendererObj.getUnicodeFlag();
        fontRendererObj.setUnicodeFlag(true);
        if (currPage != 0 || !viewing.isEmpty()) {
            s = I18n.format("book.pageIndicator", (currPage - 1) * 2 + 4, bookTotalPages * 2);
            if (viewing.isEmpty()) {
                s = I18n.format("book.pageIndicator", (currPage - 1) * 2 + 2, bookTotalPages * 2 - 2);
            }

            l = fontRendererObj.getStringWidth(s);
            fontRendererObj.drawString(s, k - l + BOOK_IMAGE_WIDTH - 44 + 67, b0 + 16, 0x3F3F3F);

            s = I18n.format("book.pageIndicator", (currPage - 1) * 2 + 3, bookTotalPages * 2);
            if (viewing.isEmpty()) {
                s = I18n.format("book.pageIndicator", (currPage - 1) * 2 + 1, bookTotalPages * 2 - 2);
            }

            fontRendererObj.drawString(s, k + l - BOOK_IMAGE_WIDTH + 54 + 67, b0 + 16, 0x3F3F3F);
        } else {
            fontRendererObj.setUnicodeFlag(unicode);
            fontRendererObj.drawStringWithShadow(s, k + 67 + BOOK_IMAGE_WIDTH / 2 - l / 2 - 3, b0 + 80, 0xC19E51);
            fontRendererObj.setUnicodeFlag(true);
            s = I18n.format("esteemedinnovation.book.info");
            l = fontRendererObj.getStringWidth(s);
            fontRendererObj.drawString("\u00A7o" + s, k + 67 + BOOK_IMAGE_WIDTH / 2 - l / 2 - 3, b0 + 90, 0xC19E51);
            s = I18n.format("esteemedinnovation.book.by");
            l = fontRendererObj.getStringWidth(s + " " + mc.thePlayer.getDisplayNameString());
            fontRendererObj.drawString("\u00A7o" + s + " " + mc.thePlayer.getDisplayNameString(),
              k + 67 + BOOK_IMAGE_WIDTH / 2 - l / 2 - 3, b0 + 105, 0xC19E51);
        }

        if (viewing.isEmpty()) {
            if (currPage > 0) {
                s = I18n.format("esteemedinnovation.book.index");
                l = fontRendererObj.getStringWidth(s);
                fontRendererObj.drawString("\u00A7l" + "\u00A7n" + s, k - 67 + BOOK_IMAGE_WIDTH / 2 - l / 2 - 5, b0 + 30, 0x3F3F3F);
                fontRendererObj.drawString("\u00A7l" + "\u00A7n" + s, k + 67 + BOOK_IMAGE_WIDTH / 2 - l / 2 - 5, b0 + 30, 0x3F3F3F);

                Collection<GuiButtonSelect> thingsToRemove = new ArrayList<>();
                for (GuiButton button : buttonList) {
                    if (button instanceof GuiButtonSelect) {
                        thingsToRemove.add((GuiButtonSelect) button);
                    }
                }
                for (GuiButtonSelect button : thingsToRemove) {
                    buttonList.remove(button);
                }

                String category = categories.get((currPage - 1) * 2);
                int offset = 0;
                if (category.substring(category.length() - 1, category.length()).matches("-?\\d+")) {
                    offset = 9 * Integer.parseInt(category.substring(category.length() - 1, category.length()));
                    category = category.substring(0, category.length() - 1);
                }
                s = I18n.format(category);
                int i = 10;
                int offsetCounter = 0;
                fontRendererObj.drawString("\u00A7n" + s, k + 40 - 67, 44 + b0, 0x3F3F3F);
                BookCategory actualCategory = BookPageRegistry.getCategoryFromName(category);
                if (actualCategory != null) {
                    for (BookEntry entry : Arrays.stream(actualCategory.getEntries()).filter(entry -> entry.getPages().length > 0).collect(Collectors.toList())) {
                        offsetCounter++;
                        if (offsetCounter > offset && offsetCounter < offset + 10) {
                            s = entry.getEntryName();
                            buttonList.add(new GuiButtonSelect(4, k + 50 - 67, b0 + 44 + i, 110, 10, s));
                            i += 10;
                        }
                    }
                    for (BookCategory subcat : actualCategory.getSubcategories()) {
                        offsetCounter++;
                        if (offsetCounter > offset && offsetCounter < offset + 10) {
                            s = subcat.getCategoryName();
                            buttonList.add(new GuiButtonSelect(4, k + 50 - 67, b0 + 44 + i, 110, 10, s));
                            i += 10;
                        }
                    }
                }

                if (categories.size() > 1 + ((currPage - 1) * 2)) {
                    category = categories.get(((currPage - 1) * 2) + 1);
                    offset = 0;
                    offsetCounter = 0;
                    if (category.substring(category.length() - 1, category.length()).matches("-?\\d+")) {
                        offset = 9 * Integer.parseInt(category.substring(category.length() - 1, category.length()));
                        category = category.substring(0, category.length() - 1);
                    }
                    s = I18n.format(category);
                    i = 10;
                    fontRendererObj.drawString("\u00A7n" + s, k + 40 + 67, 44 + b0, 0x3F3F3F);
                    actualCategory = BookPageRegistry.getCategoryFromName(category);
                    if (actualCategory != null) {
                        for (BookEntry entry : Arrays.stream(actualCategory.getEntries()).filter(entry -> entry.getPages().length > 0).collect(Collectors.toList())) {
                            offsetCounter++;
                            if (offsetCounter > offset && offsetCounter < offset + 10) {
                                s = entry.getEntryName();
                                buttonList.add(new GuiButtonSelect(4, k + 50 + 67, b0 + 44 + i, 110, 10, s));
                                i += 10;
                            }
                        }
                        for (BookCategory subcat : actualCategory.getSubcategories()) {
                            offsetCounter++;
                            if (offsetCounter > offset && offsetCounter < offset + 10) {
                                s = subcat.getCategoryName();
                                buttonList.add(new GuiButtonSelect(4, k + 50 + 67, b0 + 44 + i, 110, 10, s));
                                i += 10;
                            }
                        }
                    }
                }
            }
            fontRendererObj.setUnicodeFlag(unicode);
            super.drawScreen(mouseX, mouseY, partialTicks);
        } else {
            fontRendererObj.setUnicodeFlag(unicode);
            super.drawScreen(mouseX, mouseY, partialTicks);
            fontRendererObj.setUnicodeFlag(true);
            BookCategory category = BookPageRegistry.getCategoryFromName(viewing);
            BookPage[] pages = null;
            if (category == null) {
                BookEntry entry = BookPageRegistry.getEntryFromName(viewing);
                if (entry != null) {
                    pages = entry.getPages();
                }
            } else {
                pages = category.getAllVisiblePages(mc.thePlayer);
            }
            if (pages != null) {
                bookTotalPages = MathHelper.ceiling_float_int(pages.length / 2F);
                GlStateManager.enableBlend();
                int leftPageIndex = currPage * 2;
                BookPage leftPage = pages[leftPageIndex];
                GlStateManager.enableBlend();
                GlStateManager.pushMatrix();
                leftPage.renderPage(k - 67, b0, fontRendererObj, this, itemRender, currPage == 0, mouseX, mouseY);
                GlStateManager.popMatrix();
                GlStateManager.enableBlend();
                int rightPageIndex = leftPageIndex + 1;
                if (pages.length > rightPageIndex) {
                    BookPage rightPage = pages[rightPageIndex];
                    GlStateManager.pushMatrix();
                    rightPage.renderPage(k + 67, b0, fontRendererObj, this, itemRender, false, mouseX, mouseY);
                    GlStateManager.popMatrix();

                    rightPage.renderPageAfter(k + 67, b0, fontRendererObj, this, itemRender, false, mouseX, mouseY);
                }
                leftPage.renderPageAfter(k - 67, b0, fontRendererObj, this, itemRender, currPage == 0, mouseX, mouseY);
                updateButtons();
            }
            fontRendererObj.setUnicodeFlag(unicode);
        }
    }

    @Override
    public void renderToolTip(ItemStack stack0, int mouseX, int mouseY, boolean renderHyperlink) {
        List<String> list = stack0.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);
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
        drawHoveringText(list, mouseX, mouseY, (font == null ? fontRendererObj : font));
        zLevel = 0.0F;
    }

    @Override
    public void renderText(String str, int mouseX, int mouseY) {
        List<String> list = new ArrayList<>();
        list.add(I18n.format(str));
        drawHoveringText(list, mouseX, mouseY);
        drawHoveringText(list, mouseX, mouseY, fontRendererObj);
    }

    @Override
    public void itemClicked(ItemStack itemStack) {
         for (ItemStack stack : BookPageRegistry.bookRecipes.keySet()) {
            if (!mustReleaseMouse && stack.getItem() == itemStack.getItem() && stack.getItemDamage() == itemStack.getItemDamage()) {
                BookEntry entry = BookPageRegistry.getEntryFromName(viewing);
                if (entry != null && entry.isUnlocked(mc.thePlayer)) {
                    viewing = BookPageRegistry.bookRecipes.get(stack).getLeft();
                    currPage = MathHelper.floor_float(BookPageRegistry.bookRecipes.get(stack).getRight() / 2.0F);
                    bookTotalPages = MathHelper.ceiling_float_int(entry.getPages().length / 2F);
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
        public void drawButton(Minecraft minecraft, int mouseX, int mouseY) {
            if (visible) {
                boolean isMouseInBookBounds = MathUtility.isBetweenMinInclusive(xPosition, mouseX, xPosition + width) &&
                  MathUtility.isBetweenMinInclusive(yPosition, mouseY, yPosition + height);
                GlStateManager.color(1, 1, 1, 1);
                minecraft.getTextureManager().bindTexture(BOOK_GUI_TEXTURES);
                int k = 0;
                int l = 192;

                if (isMouseInBookBounds) {
                    k += 23;
                }

                if (!isNext) {
                    l += 13;
                }

                drawTexturedModalRect(xPosition, yPosition, k, l, 23, 13);
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
        public void drawButton(Minecraft mc, int mouseX, int mouseY) {
            if (visible) {
                FontRenderer fontRenderer = mc.fontRendererObj;
                GlStateManager.color(1, 1, 1, 1);
                boolean isMouseOverBook = MathUtility.isBetweenMinInclusive(xPosition, mouseX, xPosition + width) &&
                  MathUtility.isBetweenMinInclusive(yPosition, mouseY, yPosition + height);
                mouseDragged(mc, mouseX, mouseY);
                boolean unicodeBeforeSet = fontRendererObj.getUnicodeFlag();
                fontRendererObj.setUnicodeFlag(true);
                drawCenteredStringWithoutShadow(fontRenderer, "\u2022 " + displayString, xPosition + (isMouseOverBook ? 1 : 0),
                  yPosition + (height - 8) / 2, 0x3F3F3F);
                fontRendererObj.setUnicodeFlag(unicodeBeforeSet);
            }
        }
    }

    /**
     * Opens the entry for the ItemStack
     * @param recipeStack The ItemStack to get the recipe entry for.
     * @param player The player opening the GUI.
     */
    public static void openRecipeFor(ItemStack recipeStack, EntityPlayer player) {
        player.openGui(EsteemedInnovation.instance, 1, player.worldObj, 0, 0, 0);
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
            journal.currPage = entry != null && entry.getPages().length > 0 ? pageNumber / 2 : 0;
            journal.lastIndexPage = 1;
            journal.bookTotalPages = pageNumber;
            journal.updateButtons();
        }
    }
}
