package flaxbeard.steamcraft.client.render.model;

import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.exosuit.IExosuitUpgrade;
import flaxbeard.steamcraft.api.exosuit.UtilPlates;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ModelExosuit extends ModelBiped {
    private static final ModelPointer model = new ModelPointer();
    private static final ComparatorUpgrade comparator = new ComparatorUpgrade();
    public static String[] dyes = {"Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "LightGray", "Gray", "Pink", "Lime", "Yellow", "LightBlue", "Magenta", "Orange", "White"};
    public ResourceLocation tankTexture = new ResourceLocation("steamcraft:textures/models/armor/exo_3.png");
    public ResourceLocation hornTexture = new ResourceLocation("steamcraft:textures/models/armor/horns.png");
    public ResourceLocation g1 = new ResourceLocation("steamcraft:textures/models/armor/exo_1_grey.png");
    public ResourceLocation g2 = new ResourceLocation("steamcraft:textures/models/armor/exo_2_grey.png");
    public ResourceLocation g3 = new ResourceLocation("steamcraft:textures/models/armor/exo_3_grey.png");
    public ResourceLocation test = new ResourceLocation("steamcraft:textures/models/armor/joshiePenguin.png");
    public ResourceLocation horns = new ResourceLocation("steamcraft:textures/models/armor/docHorns.png");
    public ResourceLocation horns_g = new ResourceLocation("steamcraft:textures/models/armor/docHorns_grey.png");
    private ResourceLocation texture;
    private boolean hasOverlay;
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
    private int armor;
    private ItemStack me;

    public ModelExosuit(ItemStack itemStack, int armorType) {
        super(armorType == 3 ? 1.0F : 0.5F, 0, 64, 32);
        hasOverlay = false;
        armor = armorType;
        me = itemStack;
        if (itemStack.hasTagCompound()) {
            if (itemStack.stackTagCompound.hasKey("plate")) {
                hasOverlay = true;
                String key = itemStack.stackTagCompound.getString("plate");
                texture = new ResourceLocation(UtilPlates.getArmorLocationFromPlate(key, (ItemExosuitArmor) itemStack.getItem(), armorType));
            }
        }
        if (armor == 0) {
            if (((ItemExosuitArmor) me.getItem()).hasPlates(me) && UtilPlates.getPlate(me.stackTagCompound.getString("plate")).getIdentifier() == "Yeti") {
                horn1 = addPairHorns(-8.0F, 35.0F);
                horn2 = addPairHorns(-6.0F, 15.0F);
                horn3 = addPairHorns(-4.0F, -5.0F);

            }
        }

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
//		Jetpack2 = new ModelRenderer(this, 28, 0);
//		if (itemStack.getItem() == SteamcraftItems.exoArmorBody && ((ItemExosuitArmor)itemStack.getItem()).getStackInSlot(itemStack, 2) != null && ((ItemExosuitArmor)itemStack.getItem()).getStackInSlot(itemStack, 2).getItem() == SteamcraftItems.jetpack) {
//			Jetpack1.addBox(-7.0F, -2F, 3F, 4, 14, 4);
//			bipedBody.addChild(Jetpack1);
//			
//			Jetpack2.addBox(3.0F, -2F, 3F, 4, 14, 4);
//			bipedBody.addChild(Jetpack2);
//		}
    }

    @Override
    public void render(Entity entity, float par2, float par3, float par4, float par5, float par6, float par7) {
        GL11.glPushMatrix();
//        if ((entity instanceof EntityLivingBase) && ((EntityLivingBase) entity).isPotionActive(Steamcraft.semiInvisible)) {
//	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.15F);
//	        GL11.glDepthMask(false);
//	        GL11.glEnable(GL11.GL_BLEND);
//	        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//	        GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
//        }
        ItemExosuitArmor item = ((ItemExosuitArmor) me.getItem());
        if (item.hasUpgrade(me, SteamcraftItems.enderShroud)) {
            if ((entity instanceof EntityLivingBase) && ((EntityLivingBase) entity).hurtTime != 0) {
                GL11.glColor4f(1.0F, 1.0F, 1.0F, (float) ((EntityLivingBase) entity).hurtTime / 9F);
                GL11.glDepthMask(false);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
            } else {
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.0F);
                GL11.glDepthMask(false);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
            }
        }

        if (armor == 0) {
            if (((ItemExosuitArmor) me.getItem()).hasPlates(me) && UtilPlates.getPlate(me.stackTagCompound.getString("plate")).getIdentifier() == "Yeti") {
                for (ModelRenderer horn : horn1) {
                    horn.showModel = false;
                }
                for (ModelRenderer horn : horn2) {
                    horn.showModel = false;
                }
                for (ModelRenderer horn : horn3) {
                    horn.showModel = false;
                }
            }
        }
        //this.Jetpack1.showModel = false;
        this.setRotationAngles(par2, par3, par4, par5, par6, par7, entity);
        //	this.Jetpack2.showModel = false;
        penguinBody.showModel = false;
        penguinArm1.showModel = false;
        penguinArm2.showModel = false;
        penguinHead.showModel = false;
        penguinNose.showModel = false;

        this.bipedHead.render(par7);
        this.bipedBody.render(par7);
        this.bipedRightArm.render(par7);
        this.bipedLeftArm.render(par7);
        this.bipedRightLeg.render(par7);
        this.bipedLeftLeg.render(par7);
        this.bipedHeadwear.render(par7);


        if (entity instanceof EntityPlayer && ((EntityPlayer) entity).getCommandSenderName().equals("joshiejack")) {
            penguinBody.showModel = true;
            penguinArm1.showModel = true;
            penguinArm2.showModel = true;
            penguinHead.showModel = true;
            penguinNose.showModel = true;
            Minecraft.getMinecraft().renderEngine.bindTexture(test);
            this.bipedHead.render(par7);
        }
        if (armor == 0 && entity instanceof EntityPlayer && ((EntityPlayer) entity).getCommandSenderName().equals("Succubism")) {
            this.hornLeftBase.rotateAngleY = this.bipedHead.rotateAngleY;
            this.hornLeftBase.rotateAngleX = this.bipedHead.rotateAngleX;
            this.hornRightBase.rotateAngleY = this.bipedHead.rotateAngleY;
            this.hornRightBase.rotateAngleX = this.bipedHead.rotateAngleX;
            Minecraft.getMinecraft().renderEngine.bindTexture(horns);
            this.hornLeftBase.render(par7);
            this.hornRightBase.render(par7);
        }
        if (hasOverlay) {
            Minecraft.getMinecraft().renderEngine.bindTexture(texture);
            this.bipedHead.render(par7);
            this.bipedBody.render(par7);
            this.bipedRightArm.render(par7);
            this.bipedLeftArm.render(par7);
            this.bipedRightLeg.render(par7);
            this.bipedLeftLeg.render(par7);
            this.bipedHeadwear.render(par7);
        }

        if (item.getStackInSlot(me, 2) != null) {
            Item vanity = item.getStackInSlot(me, 2).getItem();
            int[] ids = OreDictionary.getOreIDs(item.getStackInSlot(me, 2));
            int dye = -1;
            outerloop:
            for (int id : ids) {
                String str = OreDictionary.getOreName(id);
                if (str.contains("dye")) {
                    for (int i = 0; i < dyes.length; i++) {
                        if (dyes[i].equals(str.substring(3))) {
                            dye = 15 - i;
                            break outerloop;
                        }
                    }
                }
            }
            if (dye != -1) {
                float[] color = EntitySheep.fleeceColorTable[dye];
                GL11.glColor3f(color[0], color[1], color[2]);
                //GL11.glColor3f(EntitySheep.fleeceColorTable[dye][0],EntitySheep.fleeceColorTable[dye][1],EntitySheep.fleeceColorTable[dye][2]);
                Minecraft.getMinecraft().renderEngine.bindTexture(armor == 2 ? g2 : g1);
                this.bipedHead.render(par7);
                this.bipedBody.render(par7);
                this.bipedRightArm.render(par7);
                this.bipedLeftArm.render(par7);
                this.bipedRightLeg.render(par7);
                this.bipedLeftLeg.render(par7);
                this.bipedHeadwear.render(par7);
                if (armor == 0 && entity instanceof EntityPlayer && ((EntityPlayer) entity).getCommandSenderName().equals("Succubism")) {
                    this.hornLeftBase.rotateAngleY = this.bipedHead.rotateAngleY;
                    this.hornLeftBase.rotateAngleX = this.bipedHead.rotateAngleX;
                    this.hornRightBase.rotateAngleY = this.bipedHead.rotateAngleY;
                    this.hornRightBase.rotateAngleX = this.bipedHead.rotateAngleX;
                    Minecraft.getMinecraft().renderEngine.bindTexture(horns_g);
                    this.hornLeftBase.render(par7);
                    this.hornRightBase.render(par7);
                }
                GL11.glColor3f(1.0F, 1.0F, 1.0F);
            }
        }

        Minecraft.getMinecraft().renderEngine.bindTexture(hornTexture);
        IExosuitUpgrade[] upgrades = ((ItemExosuitArmor) me.getItem()).getUpgrades(me);
        ArrayList<IExosuitUpgrade> upgrades2 = new ArrayList<IExosuitUpgrade>(Arrays.asList(upgrades));
        Collections.sort(upgrades2, comparator);
        for (IExosuitUpgrade upgrade : upgrades2) {
            if (upgrade.hasOverlay()) {
                Minecraft.getMinecraft().renderEngine.bindTexture(upgrade.getOverlay());
                this.bipedHead.render(par7);
                this.bipedBody.render(par7);
                this.bipedRightArm.render(par7);
                this.bipedLeftArm.render(par7);
                this.bipedRightLeg.render(par7);
                this.bipedLeftLeg.render(par7);
                this.bipedHeadwear.render(par7);
            }
            if (upgrade.hasModel()) {
                upgrade.renderModel(this, entity, armor, par7, me);
            }
        }


        if (armor == 0) {
            if (((ItemExosuitArmor) me.getItem()).hasPlates(me) && UtilPlates.getPlate(me.stackTagCompound.getString("plate")).getIdentifier() == "Yeti") {
                Minecraft.getMinecraft().renderEngine.bindTexture(hornTexture);

                for (ModelRenderer horn : horn1) {
                    horn.showModel = true;
                    //horn.render(par7);
                }
                for (ModelRenderer horn : horn2) {
                    horn.showModel = true;
                    //horn.render(par7);
                }
                for (ModelRenderer horn : horn3) {
                    horn.showModel = true;
                    //horn.render(par7);
                }
                this.bipedHead.render(par7);
            }
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
