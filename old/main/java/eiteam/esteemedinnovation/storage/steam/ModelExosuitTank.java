package eiteam.esteemedinnovation.storage.steam;

import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import eiteam.esteemedinnovation.armor.exosuit.ExosuitTexture;
import eiteam.esteemedinnovation.commons.visual.Texture;
import eiteam.esteemedinnovation.misc.ModelPointer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;

public class ModelExosuitTank extends ModelExosuitUpgrade {
    private ModelPointer pointer = new ModelPointer();
    private ModelRenderer tank = new ModelRenderer(this, 0, 0);
    private static final float PX = 1F / 16F;

    public ModelExosuitTank() {
        tank.addBox(-4.0F, -1F, 2F, 8, 12, 6);
    }

    @Override
    public void copyRotationAngles(ModelBiped parentModel, EntityLivingBase entityLivingBase) {
        copyRotateAngles(tank, parentModel.bipedBody);
    }

    @Override
    public void renderModel(ModelBiped parentModel, EntityLivingBase entityLivingBase) {
        super.renderModel(parentModel, entityLivingBase);
        ExosuitTexture.TANK.bindTexturePart(1);

        float pressure = nbtTagCompound.getFloat("pressure");
        int dye = nbtTagCompound.getInteger("dye");

        tank.render(0.0625F);

        GlStateManager.pushMatrix();

        if (dye != -1) {
            ExosuitTexture.TANK_GREY.bindTexturePart(1);
            float[] color = EntitySheep.getDyeRgb(EnumDyeColor.byDyeDamage(dye));
            GlStateManager.color(color[0], color[1], color[2]);
            tank.render(0.0625F);
            GlStateManager.color(0.5F, 0.5F, 0.5F);
        }

        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();

        // Kinda hacky, but it works... Not sure what else I could do at this point to be honest.
        if (entityLivingBase.isSneaking()) {
            GlStateManager.rotate(28F, 1, 0, 0);
        }

        GlStateManager.translate(0F, 6 * PX, 8 * PX);

        GlStateManager.rotate(90F, 0, 1, 0);
        GlStateManager.rotate(95.0F, 1, 0, 0);

        float rand = 0.0F;

        if (pressure > 0.0F) {
            rand = (float) ((Math.random() - 0.5F));
            if (pressure >= 1.0F) {
                rand = (float) ((Math.random() * 20.0F - 10.0F));
            }
        }

        GlStateManager.rotate((Math.min(190.0F * pressure, 190.0F) + rand), 1, 0, 0);
        Texture.POINTER.bindTexture();
        pointer.render();
        GlStateManager.popMatrix();
    }
}
