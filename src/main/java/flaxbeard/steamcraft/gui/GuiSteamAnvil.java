package flaxbeard.steamcraft.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.network.ItemNamePacket;
import flaxbeard.steamcraft.tile.TileEntitySteamHammer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.apache.commons.io.Charsets;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiSteamAnvil extends GuiContainer implements ICrafting {
    private static final ResourceLocation field_147093_u = new ResourceLocation("textures/gui/container/anvil.png");
    private static final ResourceLocation arrow = new ResourceLocation("textures/gui/container/furnace.png");
    private ContainerSteamAnvil field_147092_v;
    private GuiTextField field_147091_w;
    private InventoryPlayer field_147094_x;
    private TileEntitySteamHammer hammer;
    private boolean canEdit = false;

    public GuiSteamAnvil(InventoryPlayer par1InventoryPlayer, TileEntitySteamHammer par2Hammer, World par2World, int par3, int par4, int par5) {
        super(new ContainerSteamAnvil(par1InventoryPlayer, par2Hammer, par2World, par3, par4, par5, Minecraft.getMinecraft().thePlayer));
        this.field_147094_x = par1InventoryPlayer;
        this.field_147092_v = (ContainerSteamAnvil) this.inventorySlots;
        this.hammer = par2Hammer;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.field_147091_w = new GuiTextField(this.fontRendererObj, i + 62, j + 24, 103, 12);
        this.field_147091_w.setTextColor(-1);
        this.field_147091_w.setDisabledTextColour(-1);
        this.field_147091_w.setEnableBackgroundDrawing(false);
        this.field_147091_w.setMaxStringLength(40);
        this.inventorySlots.removeCraftingFromCrafters(this);
        this.inventorySlots.addCraftingToCrafters(this);
        this.field_147091_w.setText(hammer.itemName);
        canEdit = true;
        /*
        if (!this.field_147092_v.getSlot(0).getHasStack()) {
            this.field_147091_w.setText("");
            hammer.itemName = "";
            this.field_147092_v.updateItemName("");
        }
        */
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
        this.inventorySlots.removeCraftingFromCrafters(this);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        this.fontRendererObj.drawString(I18n.format("container.repair"), 60, 6, 4210752);


        if (this.field_147092_v.hammer.cost > 0) {
            int k = 8453920;
            boolean flag = true;
            String s = I18n.format("container.repair.cost", Integer.valueOf(this.field_147092_v.hammer.cost));

            if (this.field_147092_v.hammer.cost >= 40 && !this.mc.thePlayer.capabilities.isCreativeMode) {
                s = I18n.format("container.repair.expensive");
                k = 16736352;
            } else if (!this.field_147092_v.getSlot(2).getHasStack()) {
                flag = false;
            } else if (!this.field_147092_v.getSlot(2).canTakeStack(this.field_147094_x.player)) {
                k = 16736352;
            }

            if (flag) {
                int l = -16777216 | (k & 16579836) >> 2 | k & -16777216;
                int i1 = this.xSize - 8 - this.fontRendererObj.getStringWidth(s);
                byte b0 = 67;

                if (this.fontRendererObj.getUnicodeFlag()) {
                    drawRect(i1 - 3, b0 - 2, this.xSize - 7, b0 + 10, -16777216);
                    drawRect(i1 - 2, b0 - 1, this.xSize - 8, b0 + 9, -12895429);
                } else {
                    //  this.fontRendererObj.drawString(s, i1, b0 + 1, l);
                    // this.fontRendererObj.drawString(s, i1 + 1, b0, l);
                    //  this.fontRendererObj.drawString(s, i1 + 1, b0 + 1, l);
                }

                // this.fontRendererObj.drawString(s, i1, b0, k);
            }
        }

        GL11.glEnable(GL11.GL_LIGHTING);


    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2) {
        if (this.field_147091_w.textboxKeyTyped(par1, par2)) {
            this.func147090G();
        } else {
            super.keyTyped(par1, par2);
        }
    }

    private void func147090G() {
        String s = this.field_147091_w.getText();
        Slot slot = this.field_147092_v.getSlot(0);

        if ((slot != null && slot.getHasStack() && !slot.getStack().hasDisplayName() &&
          s.equals(slot.getStack().getDisplayName())) || slot != null && slot.getStack() == null) {
            s = "";
        }
        if (!s.equals("") && canEdit) {
            ItemNamePacket packet = new ItemNamePacket(hammer.xCoord, hammer.yCoord, hammer.zCoord, s);
            Steamcraft.channel.sendToServer(packet);
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
        this.field_147091_w.mouseClicked(par1, par2, par3);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3) {
        super.drawScreen(par1, par2, par3);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        this.field_147091_w.drawTextBox();
    }

    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(field_147093_u);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(k + 59, l + 20, 0, this.ySize + (this.field_147092_v.getSlot(0).getHasStack() ? 0 : 16), 110, 16);

        if ((this.field_147092_v.getSlot(0).getHasStack() || this.field_147092_v.getSlot(1).getHasStack()) && !this.field_147092_v.getSlot(2).getHasStack()) {
            this.drawTexturedModalRect(k + 99, l + 45, this.xSize, 0, 28, 21);
        }

        if (this.hammer.cost > 0 && this.hammer.getStackInSlot(2) != null) {
            this.mc.getTextureManager().bindTexture(arrow);
            this.drawTexturedModalRect(k + 102, l + 48, 177, 14, MathHelper.floor_float(22.0F * (((float) this.hammer.progress + (float) this.hammer.hammerTicks / 360.0F) / (float) this.hammer.cost)), 16);
        }

        if (!this.field_147092_v.getSlot(2).canTakeStack(this.field_147094_x.player) && this.field_147092_v.getSlot(2).getHasStack()) {
            GL11.glPushMatrix();

            int j1 = this.field_147092_v.getSlot(2).xDisplayPosition;
            int k1 = this.field_147092_v.getSlot(2).yDisplayPosition;
            GL11.glColorMask(true, true, true, false);
            this.drawGradientRect(k + 134, l + 47, k + 134 + 16, l + 47 + 16, 0x59000000, 0x59000000);
            GL11.glColorMask(true, true, true, true);
            GL11.glPopMatrix();
        }
    }

    @Override
    public void sendContainerAndContentsToPlayer(Container par1Container, List par2List) {
        this.sendSlotContents(par1Container, 0, par1Container.getSlot(0).getStack());
    }

    /**
     * Sends the contents of an inventory slot to the client-side Container. This doesn't have to match the actual
     * contents of that slot. Args: Container, slot number, slot contents
     */
    public void sendSlotContents(Container par1Container, int par2, ItemStack par3ItemStack) {
        if (par2 == 0) {
            this.field_147091_w.setText(par3ItemStack == null ? "" : par3ItemStack.getDisplayName());
            this.field_147091_w.setEnabled(par3ItemStack != null);

            if (par3ItemStack != null) {
                this.func147090G();
            }
        }
    }

    /**
     * Sends two ints to the client-side Container. Used for furnace burning time, smelting progress, brewing progress,
     * and enchanting level. Normally the first int identifies which variable to update, and the second contains the new
     * value. Both are truncated to shorts in non-local SMP.
     */
    public void sendProgressBarUpdate(Container par1Container, int par2, int par3) {
    }
}