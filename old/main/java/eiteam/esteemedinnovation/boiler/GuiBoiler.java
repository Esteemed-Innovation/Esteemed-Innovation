package eiteam.esteemedinnovation.boiler;

import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.api.util.FluidHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiBoiler extends GuiContainer {
    private static final ResourceLocation BOILER_TEXTURES = new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/gui/boiler.png");
    public static final ResourceLocation STEAM_RL = new ResourceLocation(EsteemedInnovation.MOD_ID, "blocks/steam");
    private TileEntityBoiler tileEntity;

    public GuiBoiler(InventoryPlayer inventoryPlayer, TileEntityBoiler tileEntity) {
        super(new ContainerBoiler(inventoryPlayer, tileEntity));
        this.tileEntity = tileEntity;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        int x = mouseX - guiLeft;
        int y = mouseY - guiTop;
        if(x > 80 && y > 13 && x < 97 && y < 72) {
            //We are hovering over the water tank
            FluidTank tank = tileEntity.getTank();
            if(tank.getFluid() != null) {
                drawHoveringText(Arrays.asList(
                  tank.getFluid().getLocalizedName(),
                  NumberFormat.getInstance().format(tank.getFluidAmount()) + " / " + NumberFormat.getInstance().format(TileEntityBoiler.TANK_CAPACITY) + " mb"), x, y);
            }
        }

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1F, 1F, 1F, 1F);
        mc.getTextureManager().bindTexture(BOILER_TEXTURES);
        int k = (width - xSize) / 2;
        int l = (height - ySize) / 2;
        GlStateManager.enableBlend();
        drawTexturedModalRect(k, l, 0, 0, xSize, ySize);

        int i1;

        i1 = tileEntity.getBurnTimeRemainingScaled(14);
        drawTexturedModalRect(k + 58, l + 15 + 14 - i1, 176, 14 - i1, 14, i1);
        GlStateManager.disableBlend();
        FluidTank tank = tileEntity.getTank();
        if (tank == null) {
            return;
        }
        FluidStack fluidStack = tank.getFluid();
        if (fluidStack != null) {
            float fill = fluidStack.amount / (float) tank.getCapacity();
            drawFluid(new FluidStack(FluidHelper.getWaterFluid(), 1), (int) (fill * 58.0F), k + 81, l + 14, 16, 58, false);
            mc.getTextureManager().bindTexture(BOILER_TEXTURES);
            drawTexturedModalRect(k + 80, l + 13, 190, 0, 18, 60);
        }
        float fill = Math.min(tileEntity.getPressure(), 1F);
        FluidStack stack = new FluidStack(FluidHelper.getWaterFluid(), 1);
        if (FluidRegistry.isFluidRegistered("steam")) {
            stack = new FluidStack(FluidRegistry.getFluid("steam"), 1);
        }
        drawFluid(stack, (int) (fill * 58.0F), k + 104, l + 14, 16, 58, true);
        mc.getTextureManager().bindTexture(BOILER_TEXTURES);
        drawTexturedModalRect(k + 103, l + 13, 190, 0, 18, 60);

        GlStateManager.disableBlend();
    }

    private void drawFluid(FluidStack fluid, int level, int x, int y, int width, int height, boolean steam) {
        if (fluid == null || fluid.getFluid() == null) {
            return;
        }
        TextureAtlasSprite icon = FluidHelper.getStillTexture(mc, fluid.getFluid());
        if (steam) {
            icon = mc.getTextureMapBlocks().getTextureExtry(STEAM_RL.toString());
        }
        mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
//        RenderUtils.setGLColorFromInt(fluid.getFluid().getColor(fluid));
        int fullX = width / 16;
        int fullY = height / 16;
        int fullLvl = (height - level) / 16;
        int lastLvl = (height - level) - fullLvl * 16;
        for (int i = 0; i < fullX; i++) {
            for (int j = 0; j < fullY; j++) {
                if (j >= fullLvl) {
                    drawCutIcon(icon, x + i * 16, y + j * 16, 16, 16, j == fullLvl ? lastLvl : 0);
                }
            }
        }
        int lastY = height - fullY * 16;
        for (int i = 0; i < fullX; i++) {
            drawCutIcon(icon, x + i * 16, y + fullY * 16, 16, lastY, fullLvl == fullY ? lastLvl : 0);
        }
        int lastX = width - fullX * 16;
        for (int i = 0; i < fullY; i++) {
            if (i >= fullLvl) {
                drawCutIcon(icon, x + fullX * 16, y + i * 16, lastX, 16, i == fullLvl ? lastLvl : 0);
            }
        }
        drawCutIcon(icon, x + fullX * 16, y + fullY * 16, lastX, lastY, fullLvl == fullY ? lastLvl : 0);
    }

    private void drawCutIcon(TextureAtlasSprite icon, int x, int y, int width, int height, int cut) {
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y + height, zLevel).tex(icon.getMinU(), icon.getInterpolatedV(height)).endVertex();
        buffer.pos(x + width, y + height, zLevel).tex(icon.getInterpolatedU(width), icon.getInterpolatedV(height)).endVertex();
        buffer.pos(x + width, y + cut, zLevel).tex(icon.getInterpolatedU(width), icon.getInterpolatedV(cut)).endVertex();
        buffer.pos(x, y + cut, zLevel).tex(icon.getMinU(), icon.getInterpolatedV(cut)).endVertex();
        tess.draw();
    }
}
