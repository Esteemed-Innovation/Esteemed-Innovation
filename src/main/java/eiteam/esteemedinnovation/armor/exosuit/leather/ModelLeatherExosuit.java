package eiteam.esteemedinnovation.armor.exosuit.leather;

import eiteam.esteemedinnovation.api.Engineerable;
import eiteam.esteemedinnovation.api.exosuit.ExosuitPlate;
import eiteam.esteemedinnovation.api.exosuit.UtilPlates;
import eiteam.esteemedinnovation.armor.exosuit.ModelExosuit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

import static eiteam.esteemedinnovation.armor.exosuit.leather.ItemLeatherExosuitArmor.MODEL_TEXTURE;

public class ModelLeatherExosuit extends ModelBiped implements ModelExosuit {
    private final ModelRenderer footRight;
    private final ModelRenderer pauldronLeft;
    private final ModelRenderer pauldronRight;
    private final ModelRenderer breastPlate;
    private final ModelRenderer footLeft;
    private final EntityEquipmentSlot slot;
    @Nullable
    private ResourceLocation plateOverlay;

    ModelLeatherExosuit(EntityEquipmentSlot slot) {
        this.slot = slot;
        textureWidth = 64;
        textureHeight = 64;
        bipedLeftLeg = new ModelRenderer(this, 0, 16);
        bipedLeftLeg.mirror = true;
        bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.1F);
        bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        footLeft = new ModelRenderer(this, 0, 61);
        footLeft.mirror = true;
        footLeft.setRotationPoint(1.9F, 12.0F, 0.1F);
        footLeft.addBox(-2.0F, 10.0F, -3.0F, 4, 2, 1, 0.0F);
        pauldronLeft = new ModelRenderer(this, 32, 32);
        pauldronLeft.mirror = true;
        pauldronLeft.setRotationPoint(5.0F, 2.0F, -0.0F);
        pauldronLeft.addBox(-1.0F, -2.0F, -2.0F, 4, 9, 4, 0.2F);
        setRotateAngle(pauldronLeft, 0.0F, 0.0F, -0.10000736613927509F);
        pauldronRight = new ModelRenderer(this, 32, 32);
        pauldronRight.setRotationPoint(-5.0F, 2.0F, 0.0F);
        pauldronRight.addBox(-3.0F, -2.0F, -2.5F, 4, 9, 4, 0.2F);
        setRotateAngle(pauldronRight, 0.0F, 0.0F, 0.10000736613927509F);
        bipedHead = new ModelRenderer(this, 32, 0);
        bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F);
        breastPlate = new ModelRenderer(this, 30, 45);
        breastPlate.setRotationPoint(0.0F, 0.0F, 0.0F);
        breastPlate.addBox(-4.0F, 0.0F, -4.5F, 8, 6, 4, 0.1F);
        setRotateAngle(breastPlate, 0.2617993877991494F, 0.0F, 0.0F);
        bipedRightLeg = new ModelRenderer(this, 0, 16);
        bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.1F);
        bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        bipedRightArm = new ModelRenderer(this, 40, 16);
        bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        bipedRightArm.addBox(-3.0F, -2.0F, -2.5F, 4, 12, 4, 0.0F);
        setRotateAngle(bipedRightArm, 0.0F, 0.0F, 0.10000736613927509F);
        bipedLeftArm = new ModelRenderer(this, 40, 16);
        bipedLeftArm.mirror = true;
        bipedLeftArm.setRotationPoint(5.0F, 2.0F, -0.0F);
        bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        setRotateAngle(bipedLeftArm, 0.0F, 0.0F, -0.10000736613927509F);
        footRight = new ModelRenderer(this, 0, 61);
        footRight.setRotationPoint(-1.9F, 12.0F, 0.1F);
        footRight.addBox(-2.0F, 10.0F, -3.0F, 4, 2, 1, 0.0F);
        bipedBody = new ModelRenderer(this, 16, 16);
        bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
        bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
    }

    @Override
    public void render(@Nullable Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        if (plateOverlay != null) {
            Minecraft.getMinecraft().renderEngine.bindTexture(plateOverlay);
        }
        GlStateManager.pushMatrix();
        copyRotateAngles(footLeft, bipedLeftLeg);
        copyRotateAngles(footRight, bipedRightLeg);
        copyRotateAngles(pauldronLeft, bipedLeftArm);
        copyRotateAngles(pauldronRight, bipedRightArm);
        copyRotateAngles(breastPlate, bipedBody);
        if (entity != null && entity.isSneaking()) {
            // Taken from ModelBiped#render.
            GlStateManager.translate(0, 0.2F, 0);
        }
        footLeft.render(scale);
        footRight.render(scale);
        pauldronLeft.render(scale);
        pauldronRight.render(scale);
        breastPlate.render(scale);
        GlStateManager.popMatrix();
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     * TODO: Move into RenderUtility.
     */
    private void setRotateAngle(ModelRenderer modelRenderer, float xAngle, float yAngle, float zAngle) {
        modelRenderer.rotateAngleX = xAngle;
        modelRenderer.rotateAngleY = yAngle;
        modelRenderer.rotateAngleZ = zAngle;
    }

    /**
     * Copies the rotation angles from `from` into `into`.
     * TODO: Move into RenderUtility.
     * @param into The renderer to copy the angles into
     * @param from The renderer to copy the angles from
     */
    private void copyRotateAngles(ModelRenderer into, ModelRenderer from) {
        setRotateAngle(into, from.rotateAngleX, from.rotateAngleY, from.rotateAngleZ);
    }

    @Override
    public void updateModel(EntityLivingBase entityLivingBase, ItemStack itemStack, EntityEquipmentSlot slot) {
        ItemStack stack = ((Engineerable) itemStack.getItem()).getStackInSlot(itemStack, 0);
        ExosuitPlate plate = UtilPlates.getPlate(stack);
        if (plate != null) {
            plateOverlay = new ResourceLocation(MODEL_TEXTURE.getResourceDomain(),
              MODEL_TEXTURE.getResourcePath().replace(".png", "_" + plate.getArmorMod() + ".png"));
            return;
        }
        plateOverlay = null;
    }

    void showHead(boolean show) {
        bipedHead.showModel = show;
        bipedHeadwear.showModel = show;
    }

    void showChest(boolean show) {
        bipedBody.showModel = show;
        bipedLeftArm.showModel = show;
        bipedRightArm.showModel = show;
        breastPlate.showModel = show;
        pauldronLeft.showModel = show;
        pauldronRight.showModel = show;
    }

    void showLegs(boolean show) {
        bipedLeftLeg.showModel = show;
        bipedRightLeg.showModel = show;
    }

    void showBoots(boolean show) {
        footLeft.showModel = show;
        footRight.showModel = show;
    }
}
