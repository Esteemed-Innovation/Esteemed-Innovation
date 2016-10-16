package eiteam.esteemedinnovation.client.render.model.exosuit;

import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import eiteam.esteemedinnovation.client.ExosuitTexture;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class ModelSidepack extends ModelExosuitUpgrade {
    // Holders 1 and 2 are the bottom ones
    private ModelRenderer holder1;
    private ModelRenderer holder2;

    // Holders 3 and 4 are the top ones.
    private ModelRenderer holder3;
    private ModelRenderer holder4;

    private ModelRenderer jetpack1;
    private ModelRenderer jetpack2;

    public ModelSidepack() {
        holder1 = new ModelRenderer(this, 0, 6);
        holder1.addBox(-8F, 14F, -0.5F, 6, 1, 1);
        holder2 = new ModelRenderer(this, 0, 6);
        holder2.addBox(2F, 14F, -0.5F, 6, 1, 1);
        holder3 = new ModelRenderer(this, 0, 6);
        holder3.addBox(-8F, 9F, -0.5F, 6, 1, 1);
        holder4 = new ModelRenderer(this, 0, 6);
        holder4.addBox(2F, 9F, -0.5F, 6, 1, 1);

        jetpack1 = new ModelRenderer(this, 44, 0);
        jetpack2 = new ModelRenderer(this, 44, 0);
        jetpack1.addBox(-2F, 0F, -2.5F, 4, 4, 5);
        jetpack2.addBox(-2F, 0F, -2.5F, 4, 4, 5);
    }

    @Override
    public void renderModel(ModelBiped parentModel, EntityLivingBase entityLivingBase) {
        super.renderModel(parentModel, entityLivingBase);
        ExosuitTexture.TANK.bindTexturePart(1);

        float rotation = nbtTagCompound.getFloat("rotation");

        holder1.render(0.0625F);
        holder2.render(0.0625F);
        holder3.render(0.0625F);
        holder4.render(0.0625F);

        GlStateManager.pushMatrix();

        GlStateManager.translate(-8F / 16F, 10F / 16F, 0F);

        if (entityLivingBase instanceof EntityPlayer) {
            GL11.glRotated(-((EntityPlayer) entityLivingBase).renderYawOffset, 0F, 1F, 0F);
            GL11.glRotated(rotation - 90.0F, 0F, 1F, 0F);
        }

        jetpack1.render(0.0625F);

        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();

        GlStateManager.translate(8F / 16F, 10F / 16F, 0F);

        if (entityLivingBase instanceof EntityPlayer) {
            GlStateManager.rotate(-((EntityPlayer) entityLivingBase).renderYawOffset, 0F, 1F, 0F);
            GlStateManager.rotate(rotation - 90.0F, 0F, 1F, 0F);
        }

        jetpack2.render(0.0625F);

        GlStateManager.popMatrix();
    }

    @Override
    public void copyRotationAngles(ModelBiped parentModel, EntityLivingBase entityLivingBase) {
        copyRotateAngles(holder1, parentModel.bipedBody);
        copyRotateAngles(holder2, parentModel.bipedBody);
        copyRotateAngles(holder3, parentModel.bipedBody);
        copyRotateAngles(holder4, parentModel.bipedBody);
        copyRotateAngles(jetpack1, parentModel.bipedBody);
        copyRotateAngles(jetpack2, parentModel.bipedBody);
    }
}
