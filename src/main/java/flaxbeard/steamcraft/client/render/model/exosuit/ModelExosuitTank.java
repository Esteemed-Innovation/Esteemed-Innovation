package flaxbeard.steamcraft.client.render.model.exosuit;

import flaxbeard.steamcraft.api.exosuit.ModelExosuitUpgrade;
import flaxbeard.steamcraft.client.ExosuitTexture;
import flaxbeard.steamcraft.client.Texture;
import flaxbeard.steamcraft.client.render.model.ModelPointer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import org.lwjgl.opengl.GL11;

public class ModelExosuitTank extends ModelExosuitUpgrade {

    public static final ModelPointer MODEL_POINTER = new ModelPointer();

    public ModelRenderer tank = new ModelRenderer(this, 0, 0);

    public ModelExosuitTank() {
        tank.addBox(-4.0F, -1F, 2F, 8, 12, 6);
    }

    @Override
    public void renderModel(ModelBiped parentModel, EntityLivingBase entityLivingBase) {
        ExosuitTexture.TANK.bindTexturePart(1);

        float pressure = nbtTagCompound.getFloat("pressure");
        int dye = nbtTagCompound.getInteger("dye");

        tank.render(0.0625F);

        GL11.glPushMatrix();

        if (dye != -1) {
            ExosuitTexture.TANK_GREY.bindTexturePart(1);
            float[] color = EntitySheep.fleeceColorTable[dye];
            GL11.glColor3f(color[0], color[1], color[2]);
            tank.render(0.0625F);
            GL11.glColor3f(0.5F, 0.5F, 0.5F);
        }

        GL11.glPopMatrix();

        final float px = 1.0F / 16.0F;

        GL11.glPushMatrix();

        GL11.glTranslatef(0.0F, 6 * px, 8 * px);

        GL11.glRotatef(90F, 0, 1, 0);
        GL11.glRotatef(-95.0F, 1, 0, 0);
        GL11.glRotatef(180F, 1, 0, 0);

        float rand = 0.0F;

        if (pressure > 0.0F) {
            rand = (float) ((Math.random() - 0.5F) * 5.0F);
            if (pressure >= 1.0F) {
                rand = (float) ((Math.random() * 50.0F - 25.0F));
            }
        }

        GL11.glRotated((Math.min(190.0F * pressure, 190.0F) + rand), 1, 0, 0);
        Texture.POINTER.bindTexture();
        MODEL_POINTER.render();
        GL11.glPopMatrix();
    }
}
