package eiteam.esteemedinnovation.engineeringtable;

import eiteam.esteemedinnovation.api.Constants;
import eiteam.esteemedinnovation.api.Engineerable;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

@SideOnly(Side.CLIENT)
public class GuiEngineeringTable extends GuiContainer {
    private TileEntityEngineeringTable tileEntity;

    public GuiEngineeringTable(InventoryPlayer inv, TileEntityEngineeringTable tileEntity) {
        super(new ContainerEngineeringTable(inv, tileEntity));
        this.tileEntity = tileEntity;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {}

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(Constants.ENG_GUI_TEXTURES);
        int k = (width - xSize) / 2;
        int l = (height - ySize) / 2;
        GL11.glEnable(3042);
        drawTexturedModalRect(k, l, 0, 0, xSize, ySize);

        IItemHandler itemHandler = tileEntity.getCapability(ITEM_HANDLER_CAPABILITY, null);
        if (itemHandler == null) {
            return;
        }
        Item engineeringItem = itemHandler.getStackInSlot(0).getItem();
        if (engineeringItem instanceof Engineerable) {
            Engineerable item = (Engineerable) engineeringItem;
            item.drawBackground(this, 0, k + 52, l + 8);

            int i = 0;
            for (Pair<Integer, Integer> pair : item.engineerCoordinates()) {
                int x = pair.getLeft();
                int y = pair.getRight();
                item.drawSlot(this, i, k + x + 52, l + y + 8);
                i++;
            }
        }
    }
}
