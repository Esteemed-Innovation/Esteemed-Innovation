package flaxbeard.steamcraft.gui;

import flaxbeard.steamcraft.api.IEngineerable;
import flaxbeard.steamcraft.tile.TileEntityEngineeringTable;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.MutablePair;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiEngineeringTable extends GuiContainer {
    public static final ResourceLocation GUI_TEXTURES = new ResourceLocation("steamcraft:textures/gui/engineering.png");
    private TileEntityEngineeringTable tileEntity;

    public GuiEngineeringTable(InventoryPlayer inv, TileEntityEngineeringTable tileEntity) {
        super(new ContainerEngineeringTable(inv, tileEntity));
        this.tileEntity = tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {}

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(GUI_TEXTURES);
        int k = (width - xSize) / 2;
        int l = (height - ySize) / 2;
        GL11.glEnable(3042);
        this.drawTexturedModalRect(k, l, 0, 0, xSize, ySize);

        if (tileEntity.getStackInSlot(0) != null) {
            if (tileEntity.getStackInSlot(0).getItem() instanceof IEngineerable) {
                IEngineerable item = (IEngineerable) tileEntity.getStackInSlot(0).getItem();
                item.drawBackground(this, 0, k + 52, l + 8);

                int i = 0;
                for (MutablePair<Integer, Integer> pair : item.engineerCoordinates()) {
                    int x = pair.left;
                    int y = pair.right;
                    item.drawSlot(this, i, k + x + 52, l + y + 8);
                    i++;
                }
            }
        }
    }
}
