package flaxbeard.steamcraft.client.render.model.exosuit;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.exosuit.IExosuitUpgrade;
import flaxbeard.steamcraft.api.exosuit.ModelExosuitUpgrade;
import flaxbeard.steamcraft.api.exosuit.UtilPlates;
import flaxbeard.steamcraft.client.ExosuitTexture;
import flaxbeard.steamcraft.client.Texture;
import flaxbeard.steamcraft.client.render.model.ModelPointer;
import flaxbeard.steamcraft.item.ItemExosuitArmor;
import flaxbeard.steamcraft.misc.ComparatorUpgrade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class ModelExosuit extends ModelBiped {

    public static final ModelPointer MODEL_POINTER = new ModelPointer();
    public static final ComparatorUpgrade COMPARATOR_UPGRADE = new ComparatorUpgrade();

    public static String[] DYES = {"Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "LightGray", "Gray", "Pink", "Lime", "Yellow", "LightBlue", "Magenta", "Orange", "White"};

    private final Map<Class<? extends ModelExosuitUpgrade>, ModelExosuitUpgrade> internalModelCache = Maps.newHashMap();

    private ModelExosuitUpgrade getModel(Class<? extends ModelExosuitUpgrade> clazz) {
        if (!internalModelCache.containsKey(clazz))
            try {
                internalModelCache.put(clazz, clazz.newInstance());
            } catch (Exception ex) {}

        return internalModelCache.get(clazz);
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
    private int armor;

    private boolean shroudEnabled = false;
    private boolean yetiHorns = false;
    private boolean hasPlateOverlay = false;

    public ModelExosuit(int armorType) {
        super(armorType == 3 ? 1.0F : 0.5F, 0, 64, 32);
        hasPlateOverlay = false;
        armor = armorType;

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
        ItemExosuitArmor exosuitArmor = ((ItemExosuitArmor) itemStack.getItem());

        // Yeti Horns
        if (armor == 0) {
            yetiHorns = exosuitArmor.hasPlates(itemStack) && UtilPlates.getPlate(itemStack.stackTagCompound.getString("plate")).getIdentifier().equals("Yeti");
        } else {
            yetiHorns = false;
        }

        // Plates
        if (exosuitArmor.hasPlates(itemStack)) {
            hasPlateOverlay = true;
            plateOverlayTexture = new ResourceLocation(UtilPlates.getPlate(itemStack.getTagCompound().getString("plate")).getArmorLocation(exosuitArmor, armor));
        } else {
            hasPlateOverlay = false;
        }

        // Ender Shroud
        if (exosuitArmor.hasUpgrade(itemStack, SteamcraftItems.enderShroud)) {
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
            ItemStack vanity = exosuitArmor.getStackInSlot(itemStack, 2);
            int[] ids = OreDictionary.getOreIDs(vanity);
            outerloop:
            for (int id : ids) {
                String str = OreDictionary.getOreName(id);
                if (str.contains("dye")) {
                    for (int i = 0; i < DYES.length; i++) {
                        if (DYES[i].equals(str.substring(3))) {
                            dye = 15 - i;
                            break outerloop;
                        }
                    }
                }
            }
        }

        // Upgrades
        overlayTextures.clear();
        modelClasses.clear();
        ArrayList<IExosuitUpgrade> upgrades = new ArrayList<IExosuitUpgrade>(Arrays.asList(exosuitArmor.getUpgrades(itemStack)));
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
    public void render(Entity entity, float par2, float par3, float par4, float par5, float par6, float par7) {
        GL11.glPushMatrix();

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

        // Ender Shroud
        if (shroudEnabled) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, shroudModifier);
            GL11.glDepthMask(false);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
        }

        this.setRotationAngles(par2, par3, par4, par5, par6, par7, entity);

        this.bipedHead.render(par7);
        this.bipedBody.render(par7);
        this.bipedRightArm.render(par7);
        this.bipedLeftArm.render(par7);
        this.bipedRightLeg.render(par7);
        this.bipedLeftLeg.render(par7);
        this.bipedHeadwear.render(par7);

        penguinBody.showModel = false;
        penguinArm1.showModel = false;
        penguinArm2.showModel = false;
        penguinHead.showModel = false;
        penguinNose.showModel = false;

        // Begin special additions
        if (entity instanceof EntityPlayer && ((EntityPlayer) entity).getCommandSenderName().equals("joshiejack")) {
            penguinBody.showModel = true;
            penguinArm1.showModel = true;
            penguinArm2.showModel = true;
            penguinHead.showModel = true;
            penguinNose.showModel = true;
//          Minecraft.getMinecraft().renderEngine.bindTexture(test);
            this.bipedHead.render(par7);
        }

        if (armor == 0 && entity instanceof EntityPlayer && entity.getCommandSenderName().equals("Succubism")) {
            this.hornLeftBase.rotateAngleY = this.bipedHead.rotateAngleY;
            this.hornLeftBase.rotateAngleX = this.bipedHead.rotateAngleX;
            this.hornRightBase.rotateAngleY = this.bipedHead.rotateAngleY;
            this.hornRightBase.rotateAngleX = this.bipedHead.rotateAngleX;
            Texture.HORNS.bindTexture();
            this.hornLeftBase.render(par7);
            this.hornRightBase.render(par7);
        }
        // End special additions

        // Plates
        if (hasPlateOverlay) {
            Minecraft.getMinecraft().renderEngine.bindTexture(plateOverlayTexture);
            this.bipedHead.render(par7);
            this.bipedBody.render(par7);
            this.bipedRightArm.render(par7);
            this.bipedLeftArm.render(par7);
            this.bipedRightLeg.render(par7);
            this.bipedLeftLeg.render(par7);
            this.bipedHeadwear.render(par7);
        }

        // Dye
        if (dye != -1) {
            float[] color = EntitySheep.fleeceColorTable[dye];

            GL11.glColor3f(color[0], color[1], color[2]);

            if (armor == 2) ExosuitTexture.EXOSUIT_GREY.bindTexturePart(2);
            else ExosuitTexture.EXOSUIT_GREY.bindTexturePart(1);

            this.bipedHead.render(par7);
            this.bipedBody.render(par7);
            this.bipedRightArm.render(par7);
            this.bipedLeftArm.render(par7);
            this.bipedRightLeg.render(par7);
            this.bipedLeftLeg.render(par7);
            this.bipedHeadwear.render(par7);

            if (armor == 0 && entity instanceof EntityPlayer && entity.getCommandSenderName().equals("Succubism")) {
                this.hornLeftBase.rotateAngleY = this.bipedHead.rotateAngleY;
                this.hornLeftBase.rotateAngleX = this.bipedHead.rotateAngleX;
                this.hornRightBase.rotateAngleY = this.bipedHead.rotateAngleY;
                this.hornRightBase.rotateAngleX = this.bipedHead.rotateAngleX;
                Texture.HORNS.bindTexture();
                this.hornLeftBase.render(par7);
                this.hornRightBase.render(par7);
            }

            GL11.glColor3f(1.0F, 1.0F, 1.0F);
        }

        for (ResourceLocation resourceLocation : overlayTextures) {
            Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation);
            this.bipedHead.render(par7);
            this.bipedBody.render(par7);
            this.bipedRightArm.render(par7);
            this.bipedLeftArm.render(par7);
            this.bipedRightLeg.render(par7);
            this.bipedLeftLeg.render(par7);
            this.bipedHeadwear.render(par7);
        }

        for (Class<? extends ModelExosuitUpgrade> modelClass : modelClasses) {
            getModel(modelClass).renderModel(this, (EntityLivingBase) entity);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDepthMask(true);
        GL11.glPopMatrix();
    }

    private ModelRenderer[] addPairHorns(float height, float zangle) {
        ModelRenderer[] hornParts = new ModelRenderer[4];
        ModelRenderer horn1a = new ModelRenderer(this, 0, 19);
        horn1a.addBox(-3.0F, -1.5F, -1.5F, 3, 3, 3);
        horn1a.setRotationPoint(-4.5F, height, -1.0F);
        horn1a.rotateAngleY = -0.5235988F;
        horn1a.rotateAngleZ = (zangle / 57.295776F);
        this.bipedHead.addChild(horn1a);
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
        this.bipedHead.addChild(horn2a);
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

    @Override
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {
        EntityLivingBase living = (EntityLivingBase) par7Entity;
        isSneak = living != null ? living.isSneaking() : false;
        if (living != null && living instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) living;

            ItemStack itemstack = player.inventory.getCurrentItem();
            heldItemRight = itemstack != null ? 1 : 0;

            if (itemstack != null && player.getItemInUseCount() > 0) {
                EnumAction enumaction = itemstack.getItemUseAction();

                if (enumaction == EnumAction.block)
                    heldItemRight = 3;
                else if (enumaction == EnumAction.bow)
                    aimedBow = true;
            }
        }
        super.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);
    }
}