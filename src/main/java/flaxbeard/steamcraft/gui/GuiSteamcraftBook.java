package flaxbeard.steamcraft.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.apache.commons.lang3.tuple.MutablePair;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.book.BookPage;
import flaxbeard.steamcraft.integration.EnchiridionIntegration;
import flaxbeard.steamcraft.item.ItemSteamcraftBook;

public class GuiSteamcraftBook extends GuiScreen {
    private static final ResourceLocation bookGuiTextures = new ResourceLocation("steamcraft:textures/gui/book.png");
    private static final ResourceLocation book2 = new ResourceLocation("steamcraft:textures/gui/book2.png");
    private static final ResourceLocation reverseBookGuiTextures = new ResourceLocation("steamcraft:textures/gui/bookReverse.png");

    /** The player editing the book */
    private final EntityPlayer editingPlayer;
    private final ItemStack bookObj;
    private boolean field_146481_r;
    /** Update ticks since the gui was opened */
    private int updateCount;
    public int bookImageWidth = 192;
    public int bookImageHeight = 192;
    public static int bookTotalPages = 1;
    public static int currPage = 0;
    public static int lastIndexPage = 0;
    private NBTTagList bookPages;
    private String bookTitle = "";
    private GuiSteamcraftBook.NextPageButton buttonNextPage;
    private GuiSteamcraftBook.NextPageButton buttonPreviousPage;
    public static String viewing = "";
    /** The GuiButton to sign this book. */
    private static final String __OBFID = "CL_00000744";
    private static ItemStack book;
    private static boolean mustReleaseMouse = false;
    private ArrayList<String> categories;
    
    class GuiButtonSelect extends GuiButton
    {
    	public String name;
    	public GuiButtonSelect(int par1, int par2, int par3, int par4, int par5, String par6Str) {
    		
    		super(par1, par2, par3, par4, par5, par6Str.contains("#") ? I18n.format(par6Str.substring(1)) : I18n.format(par6Str));
    		name = par6Str;
    	}

//    	public GuiButtonSelect(int par1, int par2, int par3, String par6Str) {
//    		super(par1, par2, par3,  par6Str);
//    	}

        public void drawCenteredStringWithoutShadow(FontRenderer par1FontRenderer, String par2Str, int par3, int par4, int par5)
        {
            par1FontRenderer.drawString(par2Str, par3, par4, par5);
        }

    	@Override
    	public void drawButton(Minecraft par1Minecraft, int par2, int par3)
        {
            if (this.visible)
            {
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

    public GuiSteamcraftBook(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack, boolean par3)
    {
		categories = new ArrayList<String>();
		for (String cat : SteamcraftRegistry.categories) {
			int pages = 0;
			for (MutablePair research : SteamcraftRegistry.research) {
				if (research.right.equals(cat) && SteamcraftRegistry.researchPages.get(research.left).length > 0) {
					pages++;
				}
			}
			for (int s = 0; s<MathHelper.ceiling_float_int(pages/9.0F); s++) {
				categories.add(cat+(s == 0 ? "" : s));
			}
		}
        this.editingPlayer = par1EntityPlayer;
        this.bookObj = par2ItemStack;
    	int i = 0;
		String lastCategory = "";
		boolean canDo = true;
		this.bookTotalPages = MathHelper.ceiling_float_int(categories.size()/2F)+1;
		if (viewing != "") {
    		this.bookTotalPages = MathHelper.ceiling_float_int(SteamcraftRegistry.researchPages.get(this.viewing).length/2F);
		}
		if (par1EntityPlayer.getHeldItem() != null && par1EntityPlayer.getHeldItem().getItem() instanceof ItemSteamcraftBook) {
			book = par1EntityPlayer.getHeldItem();
		}
		else
		{
			if (Loader.isModLoaded("Enchiridion")) {
				book = EnchiridionIntegration.findBook(SteamcraftItems.book,par1EntityPlayer);
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
    public void updateScreen()
    {
        super.updateScreen();
        ++this.updateCount;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.buttonList.clear();


        int i = (this.width - this.bookImageWidth) / 2;
        int b0 = (this.height - this.bookImageHeight) / 2;
        this.buttonList.add(this.buttonNextPage = new GuiSteamcraftBook.NextPageButton(1, i + 120 + 67, b0 + 154, true));
        this.buttonList.add(this.buttonPreviousPage = new GuiSteamcraftBook.NextPageButton(2, i + 38 - 67, b0 + 154, false));
     
        this.updateButtons();
    }


    public void updateButtons()
    {
        this.buttonNextPage.visible = (this.currPage < this.bookTotalPages - 1);
        this.buttonPreviousPage.visible = this.currPage > 0;
    }

    protected void actionPerformed(GuiButton p_146284_1_)
    {
        if (p_146284_1_.enabled)
        {
            if (p_146284_1_.id == 0)
            {
                this.mc.displayGuiScreen((GuiScreen)null);
            }
            else if (p_146284_1_.id == 1)
            {
                if (this.currPage < this.bookTotalPages - 1)
                {
                    ++this.currPage;
                }
            }
            else if (p_146284_1_.id == 2)
            {
                if (this.currPage > 0)
                {
                    --this.currPage;
                }
            }
//            else if (p_146284_1_.id == 3)
//            {
//            	this.viewing = "";
//            	this.currPage = lastIndexPage;
//            	int i = 0;
//        		String lastCategory = "";
//        		boolean canDo = true;
//        		this.bookTotalPages = MathHelper.ceiling_float_int(SteamcraftRegistry.categories.size()/2F)+1;
//                this.updateButtons();
//            }
            
            if (p_146284_1_ instanceof GuiButtonSelect) {
            	GuiButtonSelect button = (GuiButtonSelect) p_146284_1_;
            	this.lastIndexPage = currPage;
            	this.viewing = button.name.substring(0, 1).equals("#") ? button.name.substring(1) : button.name;
            	this.currPage = 0;
        		this.bookTotalPages = MathHelper.ceiling_float_int(SteamcraftRegistry.researchPages.get(this.viewing).length/2F);
                this.updateButtons();
            	this.mustReleaseMouse = true;
            }
            

            this.initGui();
        }
    }

    private void addNewPage()
    {
        if (this.bookPages != null && this.bookPages.tagCount() < 50)
        {
            this.bookPages.appendTag(new NBTTagString(""));
            ++this.bookTotalPages;
            this.field_146481_r = true;
        }
    }

    private String func_146456_p()
    {
        return this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount() ? this.bookPages.getStringTagAt(this.currPage) : "";
    }

    private void func_146457_a(String p_146457_1_)
    {
        if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount())
        {
            this.bookPages.func_150304_a(this.currPage, new NBTTagString(p_146457_1_));
            this.field_146481_r = true;
        }
    }

    private void func_146459_b(String p_146459_1_)
    {
        String s1 = this.func_146456_p();
        String s2 = s1 + p_146459_1_;
        int i = this.fontRendererObj.splitStringWidth(s2 + "" + EnumChatFormatting.BLACK + "_", 118);

        if (i <= 118 && s2.length() < 256)
        {
            this.func_146457_a(s2);
        }
    }
    
    
    protected void keyTyped(char par1, int par2)
    {
        if (par2 == 1 && this.viewing != "")
        {
        	this.viewing = "";
        	this.currPage = lastIndexPage;
        	int i = 0;
    		String lastCategory = "";
    		boolean canDo = true;
    		this.bookTotalPages = MathHelper.ceiling_float_int(categories.size()/2F)+1;
            this.updateButtons();
        }
        else
        {
        	super.keyTyped(par1, par2);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
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
            this.drawTexturedModalRect(k+67, b0, 0, 0, this.bookImageWidth, this.bookImageHeight);
        }
        else
        {
            this.mc.getTextureManager().bindTexture(reverseBookGuiTextures);
            this.drawTexturedModalRect(k-67, b0, 0, 0, this.bookImageWidth, this.bookImageHeight);
            this.mc.getTextureManager().bindTexture(bookGuiTextures);
            this.drawTexturedModalRect(k+67, b0, 0, 0, this.bookImageWidth, this.bookImageHeight);
        }

  
        String s;
        String s1;
        int l;
        
        
        s = book.getDisplayName();
        l = this.fontRendererObj.getStringWidth(s);
      
        this.fontRendererObj.drawStringWithShadow(s, k + this.bookImageWidth/2 - l/2 - 3, b0-15, 0xFFFFFF);
    
        boolean unicode = fontRendererObj.getUnicodeFlag();
		fontRendererObj.setUnicodeFlag(true);
        if (!(currPage == 0 && viewing == "")) {
	        s = I18n.format("book.pageIndicator", new Object[] {Integer.valueOf((this.currPage-1)*2 + 4), Integer.valueOf(this.bookTotalPages*2)});
	        if (this.viewing == "") {
		        s = I18n.format("book.pageIndicator", new Object[] {Integer.valueOf((this.currPage-1)*2 + 2), Integer.valueOf(this.bookTotalPages*2-2)});
	        }
	        l = this.fontRendererObj.getStringWidth(s);
	        this.fontRendererObj.drawString(s, k - l + this.bookImageWidth - 44 + 67, b0 + 16, 0x3F3F3F);
	        
	        s = I18n.format("book.pageIndicator", new Object[] {Integer.valueOf((this.currPage-1)*2 + 3), Integer.valueOf(this.bookTotalPages*2)});
	        if (this.viewing == "") {
		        s = I18n.format("book.pageIndicator", new Object[] {Integer.valueOf((this.currPage-1)*2 + 1), Integer.valueOf(this.bookTotalPages*2-2)});
	        }
	        this.fontRendererObj.drawString(s, k + l - this.bookImageWidth + 54 + 67, b0 + 16, 0x3F3F3F);

        }
        else
        {
    		fontRendererObj.setUnicodeFlag(unicode);
            this.fontRendererObj.drawStringWithShadow(s, k + 67 + this.bookImageWidth/2 - l/2 - 3, b0+80, 0xC19E51);
    		fontRendererObj.setUnicodeFlag(true);
    		s = I18n.format("steamcraft.book.info");
	        l = this.fontRendererObj.getStringWidth(s);
	        this.fontRendererObj.drawString("\u00A7o" + s, k + 67 + this.bookImageWidth/2 - l/2 - 3, b0+90, 0xC19E51);
	        s = I18n.format("steamcraft.book.by");
	        l = this.fontRendererObj.getStringWidth(s + " " + Minecraft.getMinecraft().thePlayer.getCommandSenderName());
	        this.fontRendererObj.drawString("\u00A7o" + s + " " + Minecraft.getMinecraft().thePlayer.getCommandSenderName(), k + 67 + this.bookImageWidth/2 - l/2 - 3, b0+105, 0xC19E51);
        }

        
        if (this.viewing == "") {
        	if (this.currPage > 0) {
    			s = I18n.format("steamcraft.book.index");
    			l = this.fontRendererObj.getStringWidth(s);
    			this.fontRendererObj.drawString("\u00A7l"+"\u00A7n"+s, k - 67 + this.bookImageWidth/2 - l/2 - 5, b0+30, 0x3F3F3F);
    			this.fontRendererObj.drawString("\u00A7l"+"\u00A7n"+s, k + 67 + this.bookImageWidth/2 - l/2 - 5, b0+30, 0x3F3F3F);

				
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

				String category = categories.get((currPage-1)*2);
				int offset = 0;
				if (category.substring(category.length()-1, category.length()).matches("-?\\d+")) {
					offset = 9*Integer.parseInt(category.substring(category.length()-1, category.length()));
					category = category.substring(0, category.length()-1);
				}
		        s = I18n.format(category);
				int i = 10;
				int offsetCounter = 0;
	        	this.fontRendererObj.drawString("\u00A7n"+s, k + 40 -67, 44+b0, 0x3F3F3F);
				for (MutablePair research : SteamcraftRegistry.research) {
					if (research.right.equals(category) && SteamcraftRegistry.researchPages.get(research.left).length > 0) {
						offsetCounter++;
						if (offsetCounter > offset && offsetCounter < offset+10) {
							s = (String)research.left;
			        		this.buttonList.add(new GuiButtonSelect(4, k+50-67, b0 + 44 + i, 110, 10, s));
			        		i+= 10;
						}
					}
				}
				
				if (categories.size() > 1+((currPage-1)*2)) {
					category = categories.get(((currPage-1)*2)+1);
					offset = 0;
					offsetCounter = 0;
					if (category.substring(category.length()-1, category.length()).matches("-?\\d+")) {
						offset = 9*Integer.parseInt(category.substring(category.length()-1, category.length()));
						category = category.substring(0, category.length()-1);
					}
			        s = I18n.format(category);
					i = 10;
		        	this.fontRendererObj.drawString("\u00A7n"+s, k + 40 + 67, 44+b0, 0x3F3F3F);
					for (MutablePair research : SteamcraftRegistry.research) {
						if (research.right.equals(category) && SteamcraftRegistry.researchPages.get(research.left).length > 0) {
							offsetCounter++;
							if (offsetCounter > offset && offsetCounter < offset+10) {
								s = (String)research.left;
				        		this.buttonList.add(new GuiButtonSelect(4, k+50+67, b0 + 44 + i, 110, 10, s));
				        		i+= 10;
							}
						}
					}
				}
        	}
			fontRendererObj.setUnicodeFlag(unicode);
			super.drawScreen(par1, par2, par3);
        }
        else
        {
        	////Steamcraft.log.debug(SteamcraftRegistry.researchPages.keySet().toArray()[0]);
    		fontRendererObj.setUnicodeFlag(unicode);
    		super.drawScreen(par1, par2, par3);
    		fontRendererObj.setUnicodeFlag(true);
        	if (SteamcraftRegistry.researchPages.containsKey(this.viewing)) {
        		GL11.glEnable(GL11.GL_BLEND);
        		BookPage[] pages = SteamcraftRegistry.researchPages.get(this.viewing);
        		BookPage page = pages[(this.currPage)*2];
        		GL11.glEnable(GL11.GL_BLEND);
        		GL11.glPushMatrix();
        		page.renderPage(k-67, b0, this.fontRendererObj, this, this.itemRender, this.currPage == 0, par1, par2);
        		GL11.glPopMatrix();
        		BookPage originalPage = page;
        		GL11.glEnable(GL11.GL_BLEND);
        		if (pages.length > (this.currPage)*2+1) {
        			page = pages[(this.currPage)*2+1];
            		GL11.glPushMatrix();
            		page.renderPage(k+67, b0, this.fontRendererObj, this, this.itemRender, false, par1, par2);
            		GL11.glPopMatrix();

            		page.renderPageAfter(k+67, b0, this.fontRendererObj, this, this.itemRender, false, par1, par2);
        		}
        		originalPage.renderPageAfter(k-67, b0, this.fontRendererObj, this, this.itemRender, this.currPage == 0, par1, par2);

        	}
    		fontRendererObj.setUnicodeFlag(unicode);
        	
        }

    }

    @SideOnly(Side.CLIENT)
	public
    static class NextPageButton extends GuiButton
        {
            private final boolean field_146151_o;
            private static final String __OBFID = "CL_00000745";

            public NextPageButton(int par1, int par2, int par3, boolean par4)
            {
                super(par1, par2, par3, 23, 13, "");
                this.field_146151_o = par4;
            }

            /**
             * Draws this button to the screen.
             */
            public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_)
            {
                if (this.visible)
                {
                    boolean flag = p_146112_2_ >= this.xPosition && p_146112_3_ >= this.yPosition && p_146112_2_ < this.xPosition + this.width && p_146112_3_ < this.yPosition + this.height;
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    p_146112_1_.getTextureManager().bindTexture(GuiSteamcraftBook.bookGuiTextures);
                    int k = 0;
                    int l = 192;

                    if (flag)
                    {
                        k += 23;
                    }

                    if (!this.field_146151_o)
                    {
                        l += 13;
                    }

                    this.drawTexturedModalRect(this.xPosition, this.yPosition, k, l, 23, 13);
                }
            }
        }
    
    public void renderToolTip(ItemStack p_146285_1_, int p_146285_2_, int p_146285_3_, boolean renderHyperlink)
    {
        List list = p_146285_1_.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
        this.zLevel = 1.0F;
        for (int k = 0; k < list.size(); ++k)
        {
            if (k == 0)
            {
                list.set(k, p_146285_1_.getRarity().rarityColor + (String)list.get(k));
            }
            else
            {
                list.set(k, EnumChatFormatting.GRAY + (String)list.get(k));
            }
        }
        if (renderHyperlink) {
        	for (ItemStack stack : SteamcraftRegistry.bookRecipes.keySet()) {
        		if (stack.getItem() == p_146285_1_.getItem() && stack.getItemDamage() == p_146285_1_.getItemDamage()) {
                	list.add(EnumChatFormatting.ITALIC+""+EnumChatFormatting.GRAY+StatCollector.translateToLocal("steamcraft.book.clickme"));
        		}
        	}
        }

        FontRenderer font = p_146285_1_.getItem().getFontRenderer(p_146285_1_);
        this.func_146283_a(list, p_146285_2_, p_146285_3_);
        drawHoveringText(list, p_146285_2_, p_146285_3_, (font == null ? fontRendererObj : font));
        this.zLevel = 0.0F;
    }
    
    public void renderText(String str, int p_146285_2_, int p_146285_3_)
    {
        List list = new ArrayList<String>();
        list.add(I18n.format(str));
        this.func_146283_a(list, p_146285_2_, p_146285_3_);
        drawHoveringText(list, p_146285_2_, p_146285_3_, fontRendererObj);
    }

	public void itemClicked(ItemStack p_146285_1_) {
		for (ItemStack stack : SteamcraftRegistry.bookRecipes.keySet()) {
    		if (!mustReleaseMouse && stack.getItem() == p_146285_1_.getItem() && stack.getItemDamage() == p_146285_1_.getItemDamage()) {
            	this.viewing = SteamcraftRegistry.bookRecipes.get(stack).left;
            	this.currPage = MathHelper.floor_float((float)SteamcraftRegistry.bookRecipes.get(stack).right/2.0F);
            	this.bookTotalPages = MathHelper.ceiling_float_int(SteamcraftRegistry.researchPages.get(this.viewing).length/2F);
            	this.mustReleaseMouse = true;
            	this.updateButtons();
    		}
    	}
	}

}
