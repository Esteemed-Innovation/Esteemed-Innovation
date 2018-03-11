package eiteam.esteemedinnovation.storage.item.canister;

import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderCanister extends Render<EntityCanisterItem> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/models/mortar_item.png");
    private static final ModelCanister model = new ModelCanister();

    public RenderCanister(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityCanisterItem myItem, double x, double y, double z, float entityYaw, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);

        bindEntityTexture(myItem);
        GL11.glRotatef(myItem.randomDir, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(myItem.randomDir2, 1.0F, 0.0F, 0.0F);

        model.renderAll();
        GL11.glRotatef(myItem.randomDir, 0.0F, -1.0F, 0.0F);

        EntityItem item = new EntityItem(myItem.world, 0.0F, 0.0F, 0.0F, myItem.getItem());
        item.hoverStart = 0.0F;

        GL11.glTranslated(0, 0.85F - (4F / 16F), 0);
        if (!renderManager.options.fancyGraphics) {
            GL11.glScalef(1.25F, 1.25F, 1.25F);
        }
        if (renderManager.options.fancyGraphics || item.getItem().getItem() instanceof ItemBlock) {
            GL11.glRotatef(Minecraft.getMinecraft().player.ticksExisted * 3 % 360, 0.0F, 1.0F, 0.0F);
        }
        renderManager.renderEntity(item, 0D, 0D, 0D, 0F, 0F, false);
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityCanisterItem var1) {
        return TEXTURE;
    }
}