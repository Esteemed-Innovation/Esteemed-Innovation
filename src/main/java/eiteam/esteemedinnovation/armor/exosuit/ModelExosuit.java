package eiteam.esteemedinnovation.armor.exosuit;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import eiteam.esteemedinnovation.api.exosuit.IExosuitUpgrade;
import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import eiteam.esteemedinnovation.api.exosuit.UtilPlates;
import eiteam.esteemedinnovation.commons.visual.Texture;
import eiteam.esteemedinnovation.init.items.armor.ExosuitUpgradeItems;
import eiteam.esteemedinnovation.init.misc.OreDictEntries;
import eiteam.esteemedinnovation.armor.exosuit.upgrades.ComparatorUpgrade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.*;

public class ModelExosuit extends ModelBiped {
    public static final ComparatorUpgrade COMPARATOR_UPGRADE = new ComparatorUpgrade();

    public static final String[] DYES = {"Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "LightGray", "Gray", "Pink", "Lime", "Yellow", "LightBlue", "Magenta", "Orange", "White"};

    private final Map<Class<? extends ModelExosuitUpgrade>, ModelExosuitUpgrade> internalModelCache = Maps.newHashMap();

    private ModelExosuitUpgrade getModel(Class<? extends ModelExosuitUpgrade> clazz) {
        if (!internalModelCache.containsKey(clazz)) {
            try {
                internalModelCache.put(clazz, clazz.newInstance());
            } catch (IllegalAccessException | InstantiationException ignore) {
            }
        }

        return internalModelCache.get(clazz);
    }

    /**
     * @param dyeOreDict The OreDictionary entry to use as a query. Its first 3 characters (probably "dye" will be
     *                   removed in order to find its match.
     * @return The index in {@link ModelExosuit#DYES} that points to the String matching the provided OreDict entry.
     * Returns -1 if it does not find any matches.
     */
    public static int findDyeIndexFromOreDict(String dyeOreDict) {
        String dictSubstring = dyeOreDict.substring(OreDictEntries.PREFIX_DYE.length());
        for (int dyeIndex = 0; dyeIndex < DYES.length; dyeIndex++) {
            if (dictSubstring.equals(DYES[dyeIndex])) {
                return dyeIndex;
            }
        }
        return -1;
    }

    /**
     * @param itemStack The ItemStack to use as a query. It will search every OreDictionary entry that this ItemStack
     *                  is registered for, so this might take a while for some common items. It shouldn't be horrible
     *                  though.
     * @return The index in {@link ModelExosuit#DYES} that points to the *first* String matching the provided ItemStack.
     * If an ItemStack is registered under, for example, dyeRed and dyeBlue, the index for whichever is returned
     * first from {@link OreDictionary#getOreIDs(ItemStack)} will be returned.
     * Returns -1 if it does not find any matches.
     * @see #findDyeIndexFromOreDict(String)
     */
    public static int findDyeIndexFromItemStack(ItemStack itemStack) {
        if (itemStack == null) {
            return -1;
        }
        for (int id : OreDictionary.getOreIDs(itemStack)) {
            String str = OreDictionary.getOreName(id);
            int tryFind = findDyeIndexFromOreDict(str);
            if (tryFind != -1) {
                return tryFind;
            }
        }
        return -1;
    }

    /**
     * @return The String in {@link ModelExosuit#DYES} that is equivalent to this OreDictionary entry.
     * For example: dyeRed would return Red.
     * Returns null if it does not find any matches (if findDyeIndex returns -1).
     * @see #findDyeIndexFromOreDict(String)
     */
    @Nullable
    public static String findDyeStringFromOreDict(String dyeOreDict) {
        int index = findDyeIndexFromOreDict(dyeOreDict);
        return index == -1 ? null : DYES[index];
    }

    private ModelRenderer[] horn1;
    private ModelRenderer[] horn2;
    private ModelRenderer[] horn3;

    private ModelRenderer penguinBody;
    private ModelRenderer penguinHead;
    private ModelRenderer penguinArm1;
    private ModelRenderer penguinArm2;
    private ModelRenderer penguinNose;
    private ModelRenderer hornLeftBase;
    private ModelRenderer hornLeftPart1;
    private ModelRenderer hornLeftPart2;
    private ModelRenderer hornLeftPart3;
    private ModelRenderer hornLeftPart4;
    private ModelRenderer hornLeftPart5;
    private ModelRenderer hornRightBase;
    private ModelRenderer hornRightPart1;
    private ModelRenderer hornRightPart2;
    private ModelRenderer hornRightPart3;
    private ModelRenderer hornRightPart4;
    private ModelRenderer hornRightPart5;

    private final List<ResourceLocation> overlayTextures = Lists.newArrayList();
    private final List<Class<? extends ModelExosuitUpgrade>> modelClasses = Lists.newArrayList();

    private ResourceLocation plateOverlayTexture;

    private float shroudModifier = 0F;

    private int dye = -1;
    private EntityEquipmentSlot slot;

    private boolean shroudEnabled = false;
    private boolean yetiHorns = false;
    private boolean hasPlateOverlay = false;

    public ModelExosuit(EntityEquipmentSlot slot) {
        super(slot == EntityEquipmentSlot.CHEST ? 1.0F : 0.5F, 0, 64, 32);
        hasPlateOverlay = false;
        this.slot = slot;

        // Yeti horns
        horn1 = addPairHorns(-8.0F, 35.0F);
        horn2 = addPairHorns(-6.0F, 15.0F);
        horn3 = addPairHorns(-4.0F, -5.0F);

        penguinBody = new ModelRenderer(this, 0, 16).setTextureSize(64, 32);
        penguinBody.addBox(-1.5F, -14F, -1.5F, 3, 5, 3);
        bipedHead.addChild(penguinBody);
        penguinArm1 = new ModelRenderer(this, 28, 16).setTextureSize(64, 32);
        penguinArm1.addBox(-2.5F, -14F, -1.0F, 1, 3, 2);
        bipedHead.addChild(penguinArm1);
        penguinArm2 = new ModelRenderer(this, 28, 16).setTextureSize(64, 32);
        penguinArm2.addBox(1.5F, -14F, -1.0F, 1, 3, 2);
        bipedHead.addChild(penguinArm2);
        penguinHead = new ModelRenderer(this, 12, 16).setTextureSize(64, 32);
        penguinHead.addBox(-2.0F, -18F, -2.0F, 4, 4, 4);
        bipedHead.addChild(penguinHead);
        penguinNose = new ModelRenderer(this, 34, 16).setTextureSize(64, 32);
        penguinNose.addBox(-0.5F, -16.5F, -4.0F, 1, 1, 2);
        bipedHead.addChild(penguinNose);

        hornLeftBase = new ModelRenderer(this, 0, 0);
        hornLeftBase.addBox(3F, -9F, -2F, 2, 2, 2);
        hornLeftBase.setRotationPoint(0F, 0F, 0F);
        hornLeftPart1 = new ModelRenderer(this, 0, 4);
        hornLeftPart1.addBox(4F, -10F, -1F, 2, 2, 4);
        hornLeftBase.addChild(hornLeftPart1);
        hornLeftPart2 = new ModelRenderer(this, 0, 10);
        hornLeftPart2.addBox(5F, -9F, 1F, 2, 2, 3);
        hornLeftBase.addChild(hornLeftPart2);
        hornLeftPart3 = new ModelRenderer(this, 0, 15);
        hornLeftPart3.addBox(6F, -8F, 2F, 2, 3, 2);
        hornLeftBase.addChild(hornLeftPart3);
        hornLeftPart4 = new ModelRenderer(this, 0, 20);
        hornLeftPart4.addBox(7F, -6F, 1F, 2, 2, 2);
        hornLeftBase.addChild(hornLeftPart4);
        hornLeftPart5 = new ModelRenderer(this, 0, 24);
        hornLeftPart5.addBox(6F, -5F, -2F, 2, 2, 4);
        hornLeftBase.addChild(hornLeftPart5);

        hornRightBase = new ModelRenderer(this, 0, 0);
        hornRightBase.addBox(-5F, -9F, -2F, 2, 2, 2);
        hornRightBase.setRotationPoint(0F, 0F, 0F);
        hornRightPart1 = new ModelRenderer(this, 0, 4);
        hornRightPart1.addBox(-6F, -10F, -1F, 2, 2, 4);
        hornRightBase.addChild(hornRightPart1);
        hornRightPart2 = new ModelRenderer(this, 0, 10);
        hornRightPart2.addBox(-7F, -9F, 1F, 2, 2, 3);
        hornRightBase.addChild(hornRightPart2);
        hornRightPart3 = new ModelRenderer(this, 0, 15);
        hornRightPart3.addBox(-8F, -8F, 2F, 2, 3, 2);
        hornRightBase.addChild(hornRightPart3);
        hornRightPart4 = new ModelRenderer(this, 0, 20);
        hornRightPart4.addBox(-9F, -6F, 1F, 2, 2, 2);
        hornRightBase.addChild(hornRightPart4);
        hornRightPart5 = new ModelRenderer(this, 0, 24);
        hornRightPart5.addBox(-8F, -5F, -2F, 2, 2, 4);
        hornRightBase.addChild(hornRightPart5);
    }

    public void updateModel(EntityLivingBase entityLivingBase, ItemStack itemStack) {
        ItemExosuitArmor exosuitArmor = (ItemExosuitArmor) itemStack.getItem();

        // Yeti Horns
        // TODO: Abstract
        yetiHorns = slot == EntityEquipmentSlot.HEAD && exosuitArmor.hasPlates(itemStack) && UtilPlates.getPlate(itemStack.getTagCompound().getString("plate")).getIdentifier().equals("Yeti");

        // Plates
        if (exosuitArmor.hasPlates(itemStack)) {
            hasPlateOverlay = true;
            plateOverlayTexture = new ResourceLocation(UtilPlates.getArmorLocationFromPlate(itemStack.getTagCompound().getString("plate"), exosuitArmor, slot));
        } else {
            hasPlateOverlay = false;
        }

        // Ender Shroud
        // TODO: Abstract
        if (exosuitArmor.hasUpgrade(itemStack, ExosuitUpgradeItems.Items.ENDER_SHROUD.getItem())) {
            shroudEnabled = true;
            if (entityLivingBase.hurtTime != 0) {
                shroudModifier = entityLivingBase.hurtTime / 9F;
            } else {
                shroudModifier = 0;
            }
        } else {
            shroudEnabled = false;
        }

        // Dye
        dye = -1;
        if (exosuitArmor.getStackInSlot(itemStack, 2) != null) {
            dye = findDyeIndexFromItemStack(exosuitArmor.getStackInSlot(itemStack, 2));
        }

        // Upgrades
        overlayTextures.clear();
        modelClasses.clear();
        ArrayList<IExosuitUpgrade> upgrades = new ArrayList<>(Arrays.asList(exosuitArmor.getUpgrades(itemStack)));
        Collections.sort(upgrades, COMPARATOR_UPGRADE);
        for (IExosuitUpgrade upgrade : upgrades) {
            ResourceLocation overlay = upgrade.getOverlay();
            Class<? extends ModelExosuitUpgrade> model = upgrade.getModel();

            if (overlay != null) {
                overlayTextures.add(overlay);
            }

            if (model != null) {
                modelClasses.add(model);
                upgrade.updateModel(this, entityLivingBase, itemStack, getModel(model));
            }
        }
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        // Ender Shroud is installed and the player is not being hurt, cancel all rendering for this frame.
        if (shroudEnabled && shroudModifier == 0F) {
            return;
        }
        super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.pushMatrix();

        // Yeti Horns
        for (ModelRenderer horn : horn1) {
            horn.showModel = yetiHorns;
        }

        for (ModelRenderer horn : horn2) {
            horn.showModel = yetiHorns;
        }

        for (ModelRenderer horn : horn3) {
            horn.showModel = yetiHorns;
        }

        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);

        penguinBody.showModel = false;
        penguinArm1.showModel = false;
        penguinArm2.showModel = false;
        penguinHead.showModel = false;
        penguinNose.showModel = false;

        // Begin special additions
        if (entity instanceof EntityPlayer && ((EntityPlayer) entity).getDisplayNameString().equals("joshiejack")) {
            penguinBody.showModel = true;
            penguinArm1.showModel = true;
            penguinArm2.showModel = true;
            penguinHead.showModel = true;
            penguinNose.showModel = true;
//          Minecraft.getMinecraft().renderEngine.bindTexture(test);
            bipedHead.render(scale);
        }

        if (slot == EntityEquipmentSlot.HEAD && entity instanceof EntityPlayer && ((EntityPlayer) entity).getDisplayNameString().equals("Succubism")) {
            hornLeftBase.rotateAngleY = bipedHead.rotateAngleY;
            hornLeftBase.rotateAngleX = bipedHead.rotateAngleX;
            hornRightBase.rotateAngleY = bipedHead.rotateAngleY;
            hornRightBase.rotateAngleX = bipedHead.rotateAngleX;
            Texture.HORNS.bindTexture();
            hornLeftBase.render(scale);
            hornRightBase.render(scale);
        }
        // End special additions

        // Plates
        if (hasPlateOverlay) {
            Minecraft.getMinecraft().renderEngine.bindTexture(plateOverlayTexture);
            super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }

        // Dye
        if (dye != -1) {
            EnumDyeColor dyeColor = EnumDyeColor.byDyeDamage(dye);
            float[] color = EntitySheep.getDyeRgb(dyeColor);

            GlStateManager.color(color[0], color[1], color[2]);

            ExosuitTexture.EXOSUIT_GREY.bindTexturePart(slot == EntityEquipmentSlot.LEGS ? 2 : 1);

            if (slot == EntityEquipmentSlot.HEAD && entity instanceof EntityPlayer && ((EntityPlayer) entity).getDisplayNameString().equals("Succubism")) {
                hornLeftBase.rotateAngleY = bipedHead.rotateAngleY;
                hornLeftBase.rotateAngleX = bipedHead.rotateAngleX;
                hornRightBase.rotateAngleY = bipedHead.rotateAngleY;
                hornRightBase.rotateAngleX = bipedHead.rotateAngleX;
                Texture.HORNS.bindTexture();
                hornLeftBase.render(scale);
                hornRightBase.render(scale);
            }

            GlStateManager.color(1F, 1F, 1F);
        }

        for (ResourceLocation resourceLocation : overlayTextures) {
            Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation);
            super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }

        for (Class<? extends ModelExosuitUpgrade> modelClass : modelClasses) {
            GlStateManager.pushMatrix();
            getModel(modelClass).renderModel(this, (EntityLivingBase) entity);
            GlStateManager.popMatrix();
        }

        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
    }

    private ModelRenderer[] addPairHorns(float height, float zangle) {
        ModelRenderer[] hornParts = new ModelRenderer[4];
        ModelRenderer horn1a = new ModelRenderer(this, 0, 19);
        horn1a.addBox(-3.0F, -1.5F, -1.5F, 3, 3, 3);
        horn1a.setRotationPoint(-4.5F, height, -1.0F);
        horn1a.rotateAngleY = -0.5235988F;
        horn1a.rotateAngleZ = (zangle / 57.295776F);
        bipedHead.addChild(horn1a);
        hornParts[0] = horn1a;

        ModelRenderer horn1b = new ModelRenderer(this, 0, 26);
        horn1b.addBox(-4.0F, -1.0F, -1.0F, 5, 2, 2);
        horn1b.setRotationPoint(-3.0F, 0.0F, 0.0F);
        horn1b.rotateAngleY = -0.3490659F;
        horn1b.rotateAngleZ = (zangle / 57.295776F);
        horn1a.addChild(horn1b);
        hornParts[1] = horn1b;

        ModelRenderer horn2a = new ModelRenderer(this, 0, 19);
        horn2a.addBox(0.0F, -1.5F, -1.5F, 3, 3, 3);
        horn2a.setRotationPoint(4.5F, height, -1.0F);
        horn2a.rotateAngleY = 0.5235988F;
        horn2a.rotateAngleZ = (-zangle / 57.295776F);
        bipedHead.addChild(horn2a);
        hornParts[2] = horn2a;

        ModelRenderer horn2b = new ModelRenderer(this, 0, 26);
        horn2b.addBox(-1.0F, -1.0F, -1.0F, 5, 2, 2);
        horn2b.setRotationPoint(3.0F, 0.0F, 0.0F);
        horn2b.rotateAngleY = 0.3490659F;
        horn2b.rotateAngleZ = (-zangle / 57.295776F);
        horn2a.addChild(horn2b);
        hornParts[3] = horn2b;
        return hornParts;
    }
}