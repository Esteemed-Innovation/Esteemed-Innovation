package flaxbeard.steamcraft.gui;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.book.BookPage;
import flaxbeard.steamcraft.integration.CrossMod;
import flaxbeard.steamcraft.integration.EnchiridionIntegration;
import flaxbeard.steamcraft.item.ItemSteamcraftBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.apache.commons.lang3.tuple.MutablePair;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiSteamcraftBook extends GuiScreen {
    private static final ResourceLocation bookGuiTextures = new ResourceLocation("steamcraft:textures/gui/book.png");
    private static final ResourceLocation book2 = new ResourceLocation("steamcraft:textures/gui/book2.png");
    private static final ResourceLocation reverseBookGuiTextures = new ResourceLocation("steamcraft:textures/gui/bookReverse.png");
    /**
     * The GuiButton to sign this book.
     */
    private static final String __OBFID = "CL_00000744";
    public static int bookTotalPages = 1;
    public static int currPage = 0;
    public static int lastIndexPage = 0;
    public static String viewing = "";
    private static ItemStack book;
    private static boolean mustReleaseMouse = false;
    /**
     * The player editing the book
     */
    private final EntityPlayer editingPlayer;
    private final ItemStack bookObj;
    public int bookImageWidth = 192;
    public int bookImageHeight = 192;
    private boolean field_146481_r;
    /**
     * Update ticks since the gui was opened
     */
    private int updateCount;
    private NBTTagList bookPages;
    private String bookTitle = "";
    private GuiSteamcraftBook.NextPageButton buttonNextPage;
    private GuiSteamcraftBook.NextPageButton buttonPreviousPage;
    private ArrayList<String> categories;

    public GuiSteamcraftBook(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack, boolean par3) {
        categories = new ArrayList<String>();
        for (String cat : SteamcraftRegistry.categories) {
            int pages = 0;
            for (MutablePair research : SteamcraftRegistry.research) {
                if (research.right.equals(cat) && SteamcraftRegistry.researchPages.get(research.left).length > 0) {
                    pages++;
                }
            }
            for (int s = 0; s < MathHelper.ceiling_float_int(pages / 9.0F); s++) {
                categories.add(cat + (s == 0 ? "" : s));
            }
        }
        this.editingPlayer = par1EntityPlayer;
        this.bookObj = par2ItemStack;
        int i = 0;
        String lastCategory = "";
        boolean canDo = true;
        bookTotalPages = MathHelper.ceiling_float_int(categories.size() / 2F) + 1;
        if (viewing != "") {
            bookTotalPages = MathHelper.ceiling_float_int(SteamcraftRegistry.researchPages.get(viewing).length / 2F);
        }
        if (par1EntityPlayer.getHeldItem() != null && par1EntityPlayer.getHeldItem().getItem() instanceof ItemSteamcraftBook) {
            book = par1EntityPlayer.getHeldItem();
        } else {
            if (CrossMod.ENCHIRIDION) {
                book = EnchiridionIntegration.findBook(SteamcraftItems.book, par1EntityPlayer);
            }
            for (int p = 0; p < par1EntityPlayer.inventory.getSizeInventory(); p++) {
                if (par1EntityPlayer.inventory.getStackInSlot(p) != null && par1EntityPlayer.inventory.getStackInSlot(p).getItem() instanceof ItemSteamcraftBook) {
                    book = par1EntityPlayer.inventory.getStackInSlot(p);
                    break;
                }
            }
        }

    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {
        super.updateScreen();
        ++this.updateCount;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui() {
        this.buttonList.clear();


        int i = (this.width - this.bookImageWidth) / 2;
        int b0 = (this.height - this.bookImageHeight) / 2;
        this.buttonList.add(this.buttonNextPage = new GuiSteamcraftBook.NextPageButton(1, i + 120 + 67, b0 + 154, true));
        this.buttonList.add(this.buttonPreviousPage = new GuiSteamcraftBook.NextPageButton(2, i + 38 - 67, b0 + 154, false));

        this.updateButtons();
    }

    public void updateButtons() {
        this.buttonNextPage.visible = (currPage < bookTotalPages - 1);
        this.buttonPreviousPage.visible = currPage > 0;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button.id == 0) {
                this.mc.displayGuiScreen(null);
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
//        		this.bookTotalPages = MathHelper.ceiling_float_int(SteamcraftRegistry.categories.size()/2F)+1;
//                this.updateButtons();
//            }

            if (button instanceof GuiButtonSelect) {
                GuiButtonSelect buttonSelect = (GuiButtonSelect) button;
                lastIndexPage = currPage;
                viewing = buttonSelect.name.substring(0, 1).equals("#") ? buttonSelect.name.substring(1) : buttonSelect.name;
                currPage = 0;
                bookTotalPages = MathHelper.ceiling_float_int(SteamcraftRegistry.researchPages.get(viewing).length / 2F);
                this.updateButtons();
                mustReleaseMouse = true;
            }


            this.initGui();
        }
    }

    private void addNewPage() {
        if (this.bookPages != null && this.bookPages.tagCount() < 50) {
            this.bookPages.appendTag(new NBTTagString(""));
            ++bookTotalPages;
            this.field_146481_r = true;
        }
    }

    private String func_146456_p() {
        return this.bookPages != null && currPage >= 0 && currPage < this.bookPages.tagCount() ? this.bookPages.getStringTagAt(
          currPage) : "";
    }

    private void func_146457_a(String p_146457_1_) {
        if (this.bookPages != null && currPage >= 0 && currPage < this.bookPages.tagCount()) {
            this.bookPages.func_150304_a(currPage, new NBTTagString(p_146457_1_));
            this.field_146481_r = true;
        }
    }

    private void func_146459_b(String p_146459_1_) {
        String s1 = this.func_146456_p();
        String s2 = s1 + p_146459_1_;
        int i = this.fontRendererObj.splitStringWidth(s2 + "" + EnumChatFormatting.BLACK + "_", 118);

        if (i <= 118 && s2.length() < 256) {
            this.func_146457_a(s2);
        }
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        if (par2 == 1 && viewing != "") {
            viewing = "";
            currPage = lastIndexPage;
            int i = 0;
            String lastCategory = "";
            boolean canDo = true;
            bookTotalPages = MathHelper.ceiling_float_int(categories.size() / 2F) + 1;
            this.updateButtons();
        } else {
            super.keyTyped(par1, par2);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3) {
        int d = Mouse.getDWheel();
        if (d < 0 && this.buttonNextPage.visible) {
            this.actionPerformed(this.buttonNextPage);
        }
        if (d > 0 && this.buttonPreviousPage.visible) {
            this.actionPerformed(this.buttonPreviousPage);
        }
        if (mustReleaseMouse && !Mouse.isButtonDown(0)) {
            mustReleaseMouse = false;
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int k = (this.width - this.bookImageWidth) / 2;
        int b0 = (this.height - this.bookImageHeight) / 2;
        if (currPage == 0 && viewing == "") {
            this.mc.getTextureManager().bindTexture(book2);
            this.drawTexturedModalRect(k + 67, b0, 0, 0, this.bookImageWidth, this.bookImageHeight);
        } else {
            this.mc.getTextureManager().bindTexture(reverseBookGuiTextures);
            this.drawTexturedModalRect(k - 67, b0, 0, 0, this.bookImageWidth, this.bookImageHeight);
            this.mc.getTextureManager().bindTexture(bookGuiTextures);
            this.drawTexturedModalRect(k + 67, b0, 0, 0, this.bookImageWidth, this.bookImageHeight);
        }


        String s;
        String s1;
        int l;


        s = book.getDisplayName();
        l = this.fontRendererObj.getStringWidth(s);

        this.fontRendererObj.drawStringWithShadow(s, k + this.bookImageWidth / 2 - l / 2 - 3, b0 - 15, 0xFFFFFF);

        boolean unicode = fontRendererObj.getUnicodeFlag();
        fontRendererObj.setUnicodeFlag(true);
        if (!(currPage == 0 && viewing == "")) {
            s = I18n.format("book.pageIndicator", Integer.valueOf((currPage - 1) * 2 + 4),
              Integer.valueOf(bookTotalPages * 2));
            if (viewing == "") {
                s = I18n.format("book.pageIndicator", Integer.valueOf((currPage - 1) * 2 + 2),
                  Integer.valueOf(bookTotalPages * 2 - 2));
            }
            l = this.fontRendererObj.getStringWidth(s);
            this.fontRendererObj.drawString(s, k - l + this.bookImageWidth - 44 + 67, b0 + 16, 0x3F3F3F);

            s = I18n.format("book.pageIndicator", Integer.valueOf((currPage - 1) * 2 + 3),
              Integer.valueOf(bookTotalPages * 2));
            if (viewing == "") {
                s = I18n.format("book.pageIndicator", Integer.valueOf((currPage - 1) * 2 + 1),
                  Integer.valueOf(bookTotalPages * 2 - 2));
            }
            this.fontRendererObj.drawString(s, k + l - this.bookImageWidth + 54 + 67, b0 + 16, 0x3F3F3F);

        } else {
            fontRendererObj.setUnicodeFlag(unicode);
            this.fontRendererObj.drawStringWithShadow(s, k + 67 + this.bookImageWidth / 2 - l / 2 - 3, b0 + 80, 0xC19E51);
            fontRendererObj.setUnicodeFlag(true);
            s = I18n.format("steamcraft.book.info");
            l = this.fontRendererObj.getStringWidth(s);
            this.fontRendererObj.drawString("\u00A7o" + s, k + 67 + this.bookImageWidth / 2 - l / 2 - 3, b0 + 90, 0xC19E51);
            s = I18n.format("steamcraft.book.by");
            l = this.fontRendererObj.getStringWidth(s + " " + Minecraft.getMinecraft().thePlayer.getCommandSenderName());
            this.fontRendererObj.drawString("\u00A7o" + s + " " + Minecraft.getMinecraft().thePlayer.getCommandSenderName(), k + 67 + this.bookImageWidth / 2 - l / 2 - 3, b0 + 105, 0xC19E51);
        }


        if (viewing == "") {
            if (currPage > 0) {
                s = I18n.format("steamcraft.book.index");
                l = this.fontRendererObj.getStringWidth(s);
                this.fontRendererObj.drawString("\u00A7l" + "\u00A7n" + s, k - 67 + this.bookImageWidth / 2 - l / 2 - 5, b0 + 30, 0x3F3F3F);
                this.fontRendererObj.drawString("\u00A7l" + "\u00A7n" + s, k + 67 + this.bookImageWidth / 2 - l / 2 - 5, b0 + 30, 0x3F3F3F);


                String lastCategory = "";
                boolean canDo = true;
                ArrayList<Object> thingsToRemove = new ArrayList<Object>();
                for (Object button : this.buttonList) {
                    if (button instanceof GuiButtonSelect) {
                        thingsToRemove.add(button);
                    }
                }
                for (Object button : thingsToRemove) {
                    this.buttonList.remove(button);
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
                this.fontRendererObj.drawString("\u00A7n" + s, k + 40 - 67, 44 + b0, 0x3F3F3F);
                for (MutablePair research : SteamcraftRegistry.research) {
                    if (research.right.equals(category) && SteamcraftRegistry.researchPages.get(research.left).length > 0) {
                        offsetCounter++;
                        if (offsetCounter > offset && offsetCounter < offset + 10) {
                            s = (String) research.left;
                            this.buttonList.add(new GuiButtonSelect(4, k + 50 - 67, b0 + 44 + i, 110, 10, s));
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
                    this.fontRendererObj.drawString("\u00A7n" + s, k + 40 + 67, 44 + b0, 0x3F3F3F);
                    for (MutablePair research : SteamcraftRegistry.research) {
                        if (research.right.equals(category) && SteamcraftRegistry.researchPages.get(research.left).length > 0) {
                            offsetCounter++;
                            if (offsetCounter > offset && offsetCounter < offset + 10) {
                                s = (String) research.left;
                                this.buttonList.add(new GuiButtonSelect(4, k + 50 + 67, b0 + 44 + i, 110, 10, s));
                                i += 10;
                            }
                        }
                    }
                }
            }
            fontRendererObj.setUnicodeFlag(unicode);
            super.drawScreen(par1, par2, par3);
        } else {
            ////Steamcraft.log.debug(SteamcraftRegistry.researchPages.keySet().toArray()[0]);
            fontRendererObj.setUnicodeFlag(unicode);
            super.drawScreen(par1, par2, par3);
            fontRendererObj.setUnicodeFlag(true);
            if (SteamcraftRegistry.researchPages.containsKey(viewing)) {
                GL11.glEnable(GL11.GL_BLEND);
                BookPage[] pages = SteamcraftRegistry.researchPages.get(viewing);
                BookPage page = pages[(currPage) * 2];
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glPushMatrix();
                page.renderPage(k - 67, b0, this.fontRendererObj, this, itemRender, currPage == 0, par1, par2);
                GL11.glPopMatrix();
                BookPage originalPage = page;
                GL11.glEnable(GL11.GL_BLEND);
                if (pages.length > (currPage) * 2 + 1) {
                    page = pages[(currPage) * 2 + 1];
                    GL11.glPushMatrix();
                    page.renderPage(k + 67, b0, this.fontRendererObj, this, itemRender, false, par1, par2);
                    GL11.glPopMatrix();

                    page.renderPageAfter(k + 67, b0, this.fontRendererObj, this, itemRender, false, par1, par2);
                }
                originalPage.renderPageAfter(k - 67, b0, this.fontRendererObj, this, itemRender, currPage == 0, par1, par2);

            }
            fontRendererObj.setUnicodeFlag(unicode);

        }

    }

    public void renderToolTip(ItemStack stack0, int p_146285_2_, int p_146285_3_, boolean renderHyperlink) {
        List list = stack0.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
        this.zLevel = 1.0F;
        for (int k = 0; k < list.size(); ++k) {
            if (k == 0) {
                list.set(k, stack0.getRarity().rarityColor + (String) list.get(k));
            } else {
                list.set(k, EnumChatFormatting.GRAY + (String) list.get(k));
            }
        }
        if (renderHyperlink) {
            for (ItemStack stack : SteamcraftRegistry.bookRecipes.keySet()) {
                if (stack.getItem() == stack0.getItem() && stack.getItemDamage() == stack0.getItemDamage()) {
                    list.add(EnumChatFormatting.ITALIC + "" + EnumChatFormatting.GRAY + StatCollector.translateToLocal("steamcraft.book.clickme"));
                }
            }
        }

        FontRenderer font = stack0.getItem().getFontRenderer(stack0);
        this.func_146283_a(list, p_146285_2_, p_146285_3_);
        drawHoveringText(list, p_146285_2_, p_146285_3_, (font == null ? fontRendererObj : font));
        this.zLevel = 0.0F;
    }

    public void renderText(String str, int p_146285_2_, int p_146285_3_) {
        List list = new ArrayList<String>();
        list.add(I18n.format(str));
        this.func_146283_a(list, p_146285_2_, p_146285_3_);
        drawHoveringText(list, p_146285_2_, p_146285_3_, fontRendererObj);
    }

    public void itemClicked(ItemStack itemStack) {
         for (ItemStack stack : SteamcraftRegistry.bookRecipes.keySet()) {
            if (!mustReleaseMouse && stack.getItem() == itemStack.getItem() && stack.getItemDamage() == itemStack.getItemDamage()) {
                viewing = SteamcraftRegistry.bookRecipes.get(stack).left;
                currPage = MathHelper.floor_float((float) SteamcraftRegistry.bookRecipes.get(stack).right / 2.0F);
                bookTotalPages = MathHelper.ceiling_float_int(SteamcraftRegistry.researchPages.get(viewing).length / 2F);
                mustReleaseMouse = true;
                this.updateButtons();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static class NextPageButton extends GuiButton {
        private static final String __OBFID = "CL_00000745";
        private final boolean field_146151_o;

        public NextPageButton(int par1, int par2, int par3, boolean par4) {
            super(par1, par2, par3, 23, 13, "");
            this.field_146151_o = par4;
        }

        /**
         * Draws this button to the screen.
         */
        public void drawButton(Minecraft minecraft, int p_146112_2_, int p_146112_3_) {
            if (this.visible) {
                boolean flag = p_146112_2_ >= this.xPosition && p_146112_3_ >= this.yPosition && p_146112_2_ < this.xPosition + this.width && p_146112_3_ < this.yPosition + this.height;
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                minecraft.getTextureManager().bindTexture(GuiSteamcraftBook.bookGuiTextures);
                int k = 0;
                int l = 192;

                if (flag) {
                    k += 23;
                }

                if (!this.field_146151_o) {
                    l += 13;
                }

                this.drawTexturedModalRect(this.xPosition, this.yPosition, k, l, 23, 13);
            }
        }
    }

    class GuiButtonSelect extends GuiButton {
        public String name;

        public GuiButtonSelect(int par1, int par2, int par3, int par4, int par5, String par6Str) {

            super(par1, par2, par3, par4, par5, par6Str.contains("#") ? I18n.format(par6Str.substring(1)) : I18n.format(par6Str));
            name = par6Str;
        }

//    	public GuiButtonSelect(int par1, int par2, int par3, String par6Str) {
//    		super(par1, par2, par3,  par6Str);
//    	}

        public void drawCenteredStringWithoutShadow(FontRenderer par1FontRenderer, String par2Str, int par3, int par4, int par5) {
            par1FontRenderer.drawString(par2Str, par3, par4, par5);
        }

        @Override
        public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
            if (this.visible) {
                FontRenderer var4 = par1Minecraft.fontRenderer;
                //GL11.glBindTexture(GL11.GL_TEXTURE_2D, par1Minecraft.renderEngine.getTexture(SteamCraft.guiLocation + "paperbuttons.png"));
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                boolean test = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
                int var5 = this.getHoverState(test);
                //this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + var5 * 20, this.width / 2, this.height);
                // this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + var5 * 20, this.width / 2, this.height);
                this.mouseDragged(par1Minecraft, par2, par3);
                int var6 = 0x3F3F3F;

                boolean unicode = fontRendererObj.getUnicodeFlag();
                fontRendererObj.setUnicodeFlag(true);
                this.drawCenteredStringWithoutShadow(var4, "\u2022 " + this.displayString, this.xPosition + (test ? 1 : 0), this.yPosition + (this.height - 8) / 2, var6);
                fontRendererObj.setUnicodeFlag(unicode);
            }
        }

    }

    /**
     * Opens the entry for the ItemStack
     * @param recipeStack The ItemStack to get the recipe entry for.
     * @param player The player opening the GUI.
     */
    public static void openRecipeFor(ItemStack recipeStack, EntityPlayer player) {
        viewing = SteamcraftRegistry.bookRecipes.get(recipeStack).left;
        currPage = 0;
        lastIndexPage = 1;
        bookTotalPages = MathHelper.ceiling_float_int(SteamcraftRegistry.researchPages.get(GuiSteamcraftBook.viewing).length / 2F);
        player.openGui(Steamcraft.instance, 1, player.worldObj, 0, 0, 0);
        ((GuiSteamcraftBook) Minecraft.getMinecraft().currentScreen).updateButtons();
    }

}
