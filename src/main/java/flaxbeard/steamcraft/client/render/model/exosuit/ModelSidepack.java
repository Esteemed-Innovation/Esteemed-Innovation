package flaxbeard.steamcraft.client.render.model.exosuit;

import flaxbeard.steamcraft.api.exosuit.ModelExosuitUpgrade;
import flaxbeard.steamcraft.client.ExosuitTexture;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

/**
 * @author dmillerw
 */
public class ModelSidepack extends ModelExosuitUpgrade {

    public ModelRenderer holder1;
    public ModelRenderer holder2;
    public ModelRenderer holder3;
    public ModelRenderer holder4;

    public ModelRenderer jetpack1;
    public ModelRenderer jetpack2;

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
        ExosuitTexture.TANK.bindTexturePart(1);

        float rotation = nbtTagCompound.getFloat("rotation");

        holder1.render(0.0625F);
        holder2.render(0.0625F);
        holder3.render(0.0625F);
        holder4.render(0.0625F);

        GL11.glPushMatrix();

        GL11.glTranslatef(-8.0F / 16.0F, 10F / 16.0F, 0F / 16.0F);

        if (entityLivingBase instanceof EntityPlayer) {
            GL11.glRotated(-((EntityPlayer) entityLivingBase).renderYawOffset, 0F, 1F, 0F);
            GL11.glRotated(rotation - 90.0F, 0F, 1F, 0F);
        }

        jetpack1.render(0.0625F);

        GL11.glPopMatrix();

        GL11.glPushMatrix();

        GL11.glTranslatef(8.0F / 16.0F, 10F / 16.0F, 0F / 16.0F);

        if (entityLivingBase instanceof EntityPlayer) {
            GL11.glRotated(-((EntityPlayer) entityLivingBase).renderYawOffset, 0F, 1F, 0F);
            GL11.glRotated(rotation - 90.0F, 0F, 1F, 0F);
        }

        jetpack2.render(0.0625F);

        GL11.glPopMatrix();
    }
}
