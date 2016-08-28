package eiteam.esteemedinnovation.client.render.model.exosuit;

import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import eiteam.esteemedinnovation.client.ExosuitTexture;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.EntityLivingBase;

public class ModelWings extends ModelExosuitUpgrade {

    private static final int PARTS = 5;

    public ModelRenderer[] wing1;
    public ModelRenderer[] wing2;

    public ModelRenderer frame1;
    public ModelRenderer frame2;

    public ModelWings() {
        wing1 = new ModelRenderer[PARTS];
        wing2 = new ModelRenderer[PARTS];

        for (int i = 0; i < PARTS; i++) {
            wing1[i] = new ModelRenderer(this, 32, 0).setTextureSize(64, 64);
            wing2[i] = new ModelRenderer(this, 32, 0).setTextureSize(64, 64);
            wing1[i].addBox(0F, 0F, -0.5F, 2, 12, 1);
            wing1[i].setRotationPoint(-1.0F - ((5 - i)), 1F - ((5 - i)), 6.5F);
            wing2[i].addBox(0.0F, 0F, 0F, 2, 12, 1);
            wing2[i].setRotationPoint(1.0F + ((5 - i)), 1F - ((5 - i)), 6.0F);
        }

        frame1 = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);
        frame2 = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);

        frame1.addBox(0F, -0.4F + 1F / 2F, -1.0F, 2, 9, 2);
        frame1.setRotationPoint(-1.2F, 1F, 6.5F);

        frame2.addBox(0.0F, -0.4F + 1F / 2F, 0F, 2, 9, 2);
        frame2.setRotationPoint(1.2F, 1F, 5.5F);
    }

    @Override
    public void renderModel(ModelBiped parentModel, EntityLivingBase entityLivingBase) {
        ExosuitTexture.WINGS.bindTexturePart(1);

        float expansion = nbtTagCompound.getFloat("expansion");

        for (int i = 0; i < PARTS; i++) {
            wing1[i].rotateAngleZ = expansion * (float) (Math.PI / 1.8F - (Math.PI * i / 10F));
            wing1[i].rotateAngleY = (float) (Math.PI);

            wing2[i].rotateAngleZ = -expansion * (float) (Math.PI / 1.8F - (Math.PI * i / 10F));

            wing1[i].render(0.0625F);
            wing2[i].render(0.0625F);
        }

        frame1.rotateAngleZ = (float) (Math.PI / (1.5F - (expansion / 5F)));
        frame1.rotateAngleY = (float) (Math.PI);

        frame2.rotateAngleZ = -(float) (Math.PI / (1.5F - (expansion / 5F)));

        frame1.render(0.0625F);
        frame2.render(0.0625F);
    }
}
