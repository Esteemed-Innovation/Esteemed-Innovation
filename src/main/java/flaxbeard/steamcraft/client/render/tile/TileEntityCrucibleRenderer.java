package flaxbeard.steamcraft.client.render.tile;

import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.block.BlockSteamcraftCrucible;
import flaxbeard.steamcraft.client.render.RenderUtility;
import flaxbeard.steamcraft.client.render.model.ModelCrucible;
import flaxbeard.steamcraft.init.blocks.CastingBlocks;
import flaxbeard.steamcraft.tile.TileEntityCrucible;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class TileEntityCrucibleRenderer extends TileEntitySpecialRenderer implements IInventoryTESR {
    private static final ModelCrucible MODEL = new ModelCrucible();
    private static final ResourceLocation TEXTURE = new ResourceLocation("steamcraft:textures/models/crucible.png");
    private static final ResourceLocation NETHER_TEXTURE = new ResourceLocation("steamcraft:textures/models/crucible2.png");
    private static final ResourceLocation COBBLESTONE_TEXTURE = new ResourceLocation("minecraft:textures/blocks/cobblestone.png");
    private boolean isNether = false;

    public TileEntityCrucibleRenderer(boolean isHell) {
        isNether = isHell;
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        TileEntityCrucible crucible = (TileEntityCrucible) tileEntity;
        IBlockState state = crucible.getWorld().getBlockState(crucible.getPos());
        EnumFacing facing = state.getValue(BlockSteamcraftCrucible.FACING);
        int facingOrdinal = facing.ordinal();
        ////Steamcraft.log.debug(meta);
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glTranslated(x, y, z);

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        int ticks = crucible.tipTicks;
        Minecraft.getMinecraft().renderEngine.bindTexture(COBBLESTONE_TEXTURE);
        GL11.glRotatef(90.0F * (facingOrdinal + (facingOrdinal % 2 * 2)), 0F, 1F, 0F);
        GL11.glScalef(1F, -1F, -1F);
        MODEL.renderNoRotate();
        GL11.glScalef(1F, -1F, -1F);
        ResourceLocation resource = isNether ? NETHER_TEXTURE : TEXTURE;
        Minecraft.getMinecraft().renderEngine.bindTexture(resource);
        if (ticks > 135) {
            ticks = (int) ((ticks - 90) / 5.0F * 90);
            GL11.glRotatef((MathHelper.sin((float) (Math.PI * (ticks / 90.0F)))) * 5.0F, 1F, 0F, 0F);
        } else if (ticks > 120) {
            ticks = (int) ((ticks - 90) / 15.0F * 90);
            GL11.glRotatef((MathHelper.sin((float) (Math.PI * (ticks / 90.0F)))) * 15.0F, 1F, 0F, 0F);
        } else if (ticks > 90) {
            ticks = (int) ((ticks - 90) / 30.0F * 90);
            GL11.glRotatef((MathHelper.sin((float) (Math.PI * (ticks / 90.0F)))) * 30.0F, -1F, 0F, 0F);
        } else {
            GL11.glRotatef((MathHelper.sin((float) (Math.PI * (ticks / 90.0F)))) * 75.0F, 1F, 0F, 0F);
        }
        GL11.glScalef(1F, -1F, -1F);

        MODEL.renderAll();
//		ModelRenderer water = (new ModelRenderer(MODEL, 0, 0)).setTextureSize(64, 64);
        float height = (-5.0F + (crucible.getFill() / 90.0F) * 11.0F) / 16.0F;
//		//Steamcraft.log.debug(6.0F + (crucible.getFill()/90.0F)*-13.0F);
        if (crucible.getFill() > 0) {
            GL11.glRotatef(90.0F, 1F, 0F, 0F);
            GL11.glTranslatef(-0.5F, -0.5F, height);
            renderLiquid(crucible);
        }

        //	UtilsFX.renderQuadFromIcon(true, icon, 1.0F, 0.0F,0.0F, 0.0F, ConfigBlocks.blockMetalDevice.getMixedBrightnessForBlock(crucible.getWorldObj(), crucible.xCoord, crucible.yCoord, crucible.zCoord), 771, 1.0F);
        GL11.glScalef(1F, -1F, -1F);

        GL11.glPopMatrix();
    }

    private void renderLiquid(TileEntityCrucible crucible) {
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        TextureAtlasSprite icon = ((BlockSteamcraftCrucible) CastingBlocks.Blocks.CRUCIBLE.getBlock()).liquidIcon;
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
        float f1 = (float) (icon.getMinU() + (icon.getMaxU() - icon.getMinU()) * 0.8);
        float f2 = icon.getMinV();
        float f3 = icon.getMinU();
        float f4 = (float) (icon.getMinV() + (icon.getMaxV() - icon.getMinV()) * 0.8);
        //tessellator.setBrightness(brightness);
        CrucibleLiquid liquid = crucible.contents.get(0);
        buffer.putColorRGBA(VertexFormatElement.EnumUsage.COLOR.ordinal(), liquid.cr, liquid.cg, liquid.cb, 256);
        //Steamcraft.log.debug(liquid.cr);
        buffer.putNormal(0F, 0F, 1F);
        RenderUtility.addVertexWithUV(buffer, 0.125D, 0.125D, 0.0D, f1, f4);
        RenderUtility.addVertexWithUV(buffer, 0.875D, 0.125D, 0.0D, f3, f4);
        RenderUtility.addVertexWithUV(buffer, 0.875D, 0.875D, 0.0D, f3, f2);
        RenderUtility.addVertexWithUV(buffer, 0.125D, 0.875D, 0.0D, f1, f2);
        tessellator.draw();
    }

    @Override
    public void renderInventoryTileEntityAt(TileEntity tileEntity, double x, double y, double z, float var8) {
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glTranslated(x, y, z);

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        Minecraft.getMinecraft().renderEngine.bindTexture(COBBLESTONE_TEXTURE);
        GL11.glScalef(1F, -1F, -1F);
        MODEL.renderNoRotate();
        GL11.glScalef(1F, -1F, -1F);
        Minecraft.getMinecraft().renderEngine.bindTexture(isNether ? NETHER_TEXTURE : TEXTURE);
        GL11.glScalef(1F, -1F, -1F);

        MODEL.renderAll();
        GL11.glScalef(1F, -1F, -1F);

        GL11.glPopMatrix();
    }
}
