package flaxbeard.steamcraft.gui;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.misc.FluidHelper;
import flaxbeard.steamcraft.tile.TileEntityBoiler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiBoiler extends GuiContainer {
    private static final ResourceLocation BOILER_TEXTURES = new ResourceLocation("steamcraft:textures/gui/boiler.png");
    public static final ResourceLocation STEAM_RL = new ResourceLocation(Steamcraft.MOD_ID, "blocks/steam");
    private TileEntityBoiler tileEntity;

    public GuiBoiler(InventoryPlayer inventoryPlayer, TileEntityBoiler tileEntity) {
        super(new ContainerBoiler(inventoryPlayer, tileEntity));
        this.tileEntity = tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {}

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(BOILER_TEXTURES);
        int k = (width - xSize) / 2;
        int l = (height - ySize) / 2;
        GL11.glEnable(3042);
        drawTexturedModalRect(k, l, 0, 0, xSize, ySize);

        int i1;

        i1 = tileEntity.getBurnTimeRemainingScaled(14);
        drawTexturedModalRect(k + 58, l + 15 + 14 - i1, 176, 14 - i1, 14, i1);
        GL11.glDisable(3042);
        FluidTank tank = tileEntity.getTank();
        if (tank == null) {
            return;
        }
        FluidStack fluidStack = tank.getFluid();
        if (fluidStack == null) {
            return;
        }

        float fill = fluidStack.amount / (float) tank.getCapacity();
        drawFluid(new FluidStack(FluidRegistry.WATER, 1), (int) (fill * 58.0F), k + 81, l + 14, 16, 58, false);
        mc.getTextureManager().bindTexture(BOILER_TEXTURES);
        drawTexturedModalRect(k + 80, l + 13, 190, 0, 18, 60);
        fill = tileEntity.getPressure();
        fill = Math.min(fill, 1.0F);
        FluidStack stack = new FluidStack(FluidRegistry.WATER, 1);
        if (FluidRegistry.isFluidRegistered("steam")) {
            stack = new FluidStack(FluidRegistry.getFluid("steam"), 1);
        }
        drawFluid(stack, (int) (fill * 58.0F), k + 104, l + 14, 16, 58, true);
        mc.getTextureManager().bindTexture(BOILER_TEXTURES);
        drawTexturedModalRect(k + 103, l + 13, 190, 0, 18, 60);

        GL11.glDisable(3042);
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
        int lastX = width - fullX * 16;
        int lastY = height - fullY * 16;
        int fullLvl = (height - level) / 16;
        int lastLvl = (height - level) - fullLvl * 16;
        for (int i = 0; i < fullX; i++) {
            for (int j = 0; j < fullY; j++) {
                if (j >= fullLvl) {
                    drawCutIcon(icon, x + i * 16, y + j * 16, 16, 16, j == fullLvl ? lastLvl : 0);
                }
            }
        }
        for (int i = 0; i < fullX; i++) {
            drawCutIcon(icon, x + i * 16, y + fullY * 16, 16, lastY, fullLvl == fullY ? lastLvl : 0);
        }
        for (int i = 0; i < fullY; i++) {
            if (i >= fullLvl) {
                drawCutIcon(icon, x + fullX * 16, y + i * 16, lastX, 16, i == fullLvl ? lastLvl : 0);
            }
        }
        drawCutIcon(icon, x + fullX * 16, y + fullY * 16, lastX, lastY, fullLvl == fullY ? lastLvl : 0);
    }

    private void drawCutIcon(TextureAtlasSprite icon, int x, int y, int width, int height, int cut) {
        Tessellator tess = Tessellator.getInstance();
        VertexBuffer buffer = tess.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y + height, zLevel).tex(icon.getMinU(), icon.getInterpolatedV(height)).endVertex();
        buffer.pos(x + width, y + height, zLevel).tex(icon.getInterpolatedU(width), icon.getInterpolatedV(height)).endVertex();
        buffer.pos(x + width, y + cut, zLevel).tex(icon.getInterpolatedU(width), icon.getInterpolatedV(cut)).endVertex();
        buffer.pos(x, y + cut, zLevel).tex(icon.getMinU(), icon.getInterpolatedV(cut)).endVertex();
        tess.draw();
    }
}
