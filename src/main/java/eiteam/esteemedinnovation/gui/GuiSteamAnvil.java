package eiteam.esteemedinnovation.gui;

import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.network.ItemNamePacket;
import eiteam.esteemedinnovation.tile.TileEntitySteamHammer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiSteamAnvil extends GuiContainer implements IContainerListener {
    private static final ResourceLocation ANVIL_TEXTURE = new ResourceLocation("textures/gui/container/anvil.png");
    private static final ResourceLocation ARROW = new ResourceLocation("textures/gui/container/furnace.png");
    private ContainerSteamAnvil container;
    private GuiTextField textField;
    private InventoryPlayer playerInv;
    private TileEntitySteamHammer hammer;
    private boolean canEdit = false;

    public GuiSteamAnvil(InventoryPlayer inv, TileEntitySteamHammer hammer, World world, int x, int y, int z) {
        super(new ContainerSteamAnvil(inv, hammer, world, x, y, z, Minecraft.getMinecraft().thePlayer));
        this.playerInv = inv;
        this.container = (ContainerSteamAnvil) this.inventorySlots;
        this.hammer = hammer;
    }

    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        textField = new GuiTextField(0, fontRendererObj, i + 62, j + 24, 103, 12);
        textField.setTextColor(-1);
        textField.setDisabledTextColour(-1);
        textField.setEnableBackgroundDrawing(false);
        textField.setMaxStringLength(40);
        inventorySlots.removeListener(this);
        inventorySlots.addListener(this);
        textField.setText(hammer.itemName);
        canEdit = true;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
        this.inventorySlots.removeListener(this);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        fontRendererObj.drawString(I18n.format("container.repair"), 60, 6, 4210752);


        if (this.container.hammer.cost > 0) {
            boolean flag = true;
            String s = I18n.format("container.repair.cost", container.hammer.cost);

            if (this.container.hammer.cost >= 40 && !mc.thePlayer.capabilities.isCreativeMode) {
                s = I18n.format("container.repair.expensive");
            } else if (!container.getSlot(2).getHasStack()) {
                flag = false;
            }

            if (flag) {
                int i1 = this.xSize - 8 - fontRendererObj.getStringWidth(s);
                byte b0 = 67;

                if (fontRendererObj.getUnicodeFlag()) {
                    drawRect(i1 - 3, b0 - 2, this.xSize - 7, b0 + 10, -16777216);
                    drawRect(i1 - 2, b0 - 1, this.xSize - 8, b0 + 9, -12895429);
                }
            }
        }

        GL11.glEnable(GL11.GL_LIGHTING);
    }

    @Override
    protected void keyTyped(char charTyped, int keyCode) throws IOException {
        if (textField.textboxKeyTyped(charTyped, keyCode)) {
            renameItem();
        } else {
            super.keyTyped(charTyped, keyCode);
        }
    }

    private void renameItem() {
        String s = textField.getText();
        Slot slot = container.getSlot(0);

        if ((slot != null && slot.getHasStack() && !slot.getStack().hasDisplayName() &&
          s.equals(slot.getStack().getDisplayName())) || slot != null && slot.getStack() == null) {
            s = "";
        }
        if (!s.isEmpty() && canEdit && !s.equals(container.hammer.itemName)) {
            ItemNamePacket packet = new ItemNamePacket(hammer.getPos(), s);
            EsteemedInnovation.channel.sendToServer(packet);
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int z) throws IOException {
        super.mouseClicked(x, y, z);
        textField.mouseClicked(x, y, z);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        textField.drawTextBox();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(ANVIL_TEXTURE);
        int k = (width - xSize) / 2;
        int l = (height - ySize) / 2;
        drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
        drawTexturedModalRect(k + 59, l + 20, 0, ySize + (container.getSlot(0).getHasStack() ? 0 : 16), 110, 16);

        if ((container.getSlot(0).getHasStack() || container.getSlot(1).getHasStack()) && !container.getSlot(2).getHasStack()) {
            drawTexturedModalRect(k + 99, l + 45, xSize, 0, 28, 21);
        }

        if (hammer.cost > 0 && hammer.getStackInSlot(2) != null) {
            mc.getTextureManager().bindTexture(ARROW);
            drawTexturedModalRect(k + 102, l + 48, 177, 14, MathHelper.floor_float(22.0F *
              (((float) hammer.progress + (float) hammer.hammerTicks / 360.0F) / (float) hammer.cost)), 16);
        }

        if (!this.container.getSlot(2).canTakeStack(this.playerInv.player) && this.container.getSlot(2).getHasStack()) {
            GL11.glPushMatrix();

            GL11.glColorMask(true, true, true, false);
            this.drawGradientRect(k + 134, l + 47, k + 134 + 16, l + 47 + 16, 0x59000000, 0x59000000);
            GL11.glColorMask(true, true, true, true);
            GL11.glPopMatrix();
        }
    }

    @Override
    public void updateCraftingInventory(Container containerToSend, List<ItemStack> itemsList) {
        sendSlotContents(containerToSend, 0, containerToSend.getSlot(0).getStack());
    }

    @Override
    public void sendSlotContents(Container container, int slotIndex, ItemStack stack) {
        if (slotIndex == 0) {
            textField.setText(stack == null ? "" : stack.getDisplayName());
            textField.setEnabled(stack != null);

            if (stack != null) {
                renameItem();
            }
        }
    }

    @Override
    public void sendProgressBarUpdate(Container container, int varToUpdate, int newVal) {}

    @Override
    public void sendAllWindowProperties(Container containerIn, IInventory inventory) {}
}