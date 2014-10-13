package flaxbeard.steamcraft.item.tool;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.api.exosuit.ExosuitSlot;
import flaxbeard.steamcraft.api.exosuit.IExosuitUpgrade;
import flaxbeard.steamcraft.client.render.model.ModelExosuit;
import flaxbeard.steamcraft.client.render.model.ModelTophat;
import flaxbeard.steamcraft.item.ItemExosuitArmor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class ItemTophat extends ItemArmor implements IExosuitUpgrade {

    public ResourceLocation tophatTexture = new ResourceLocation("steamcraft:textures/models/armor/tophat.png");
    public ResourceLocation tophatTextureEmerald = new ResourceLocation("steamcraft:textures/models/armor/tophatemerald.png");

    boolean emerald;

    public ItemTophat(ArmorMaterial p_i45325_1_, int p_i45325_2_,
                      int p_i45325_3_, boolean em) {
        super(p_i45325_1_, p_i45325_2_, p_i45325_3_);
        emerald = em;
    }

    public void onCreated(ItemStack me, World p_77622_2_, EntityPlayer player) {
        if (player.getDisplayName() == "Flaxbeard" || player.getDisplayName() == "ForgeDevName") {
            me.setTagCompound(new NBTTagCompound());
            me.stackTagCompound.setBoolean("Flaxbeard", true);
        }
    }

    @Override
    public EnumRarity getRarity(ItemStack me) {
        if (me.hasTagCompound() && me.stackTagCompound.hasKey("Flaxbeard")) {
            return EnumRarity.epic;
        }
        return super.getRarity(me);
    }

    public String getUnlocalizedName(ItemStack me) {
        if (me.hasTagCompound() && me.stackTagCompound.hasKey("Flaxbeard")) {
            return "item.steamcraft:kek";
        }
        return super.getUnlocalizedName(me);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return emerald ? "steamcraft:textures/models/armor/tophatemerald.png" : "steamcraft:textures/models/armor/tophat.png";
    }


    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return par2ItemStack.isItemEqual(new ItemStack(Items.leather)) ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
    }

    @Override
    public int renderPriority() {
        return 0;
    }

    @Override
    public ExosuitSlot getSlot() {
        return ExosuitSlot.headHelm;
    }

    @Override
    public boolean hasOverlay() {
        return false;
    }

    @Override
    public ResourceLocation getOverlay() {
        return null;
    }

    @Override
    public boolean hasModel() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int par2) {
        ModelTophat modelbiped = new ModelTophat(itemStack, par2);
        return modelbiped;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderModel(ModelExosuit model, Entity entity, int armor,
                            float par7, ItemStack me) {

        ModelRenderer tophatBase = new ModelRenderer(model, 64, 32).setTextureOffset(32, 0);
        tophatBase.addBox(-4.0F, -17.0F, -4.0F, 8, 7, 8);
        model.bipedHead.addChild(tophatBase);

        ModelRenderer tophatHat = new ModelRenderer(model, 64, 32).setTextureOffset(0, 16);
        tophatHat.addBox(-5.5F, -10.0F, -5.5F, 11, 1, 11);
        model.bipedHead.addChild(tophatHat);

        Minecraft.getMinecraft().renderEngine.bindTexture(emerald ? tophatTextureEmerald : tophatTexture);
        model.bipedHead.render(par7);
        tophatHat = null;
        tophatBase = null;

        GL11.glPushMatrix();
        GL11.glTranslatef(model.bipedHead.rotationPointX, model.bipedHead.rotationPointY + (1F / 16F), model.bipedHead.rotationPointZ);
        GL11.glRotated(Math.toDegrees(model.bipedHead.rotateAngleY), 0, 1, 0);
        GL11.glRotated(Math.toDegrees(model.bipedHead.rotateAngleX), 1, 0, 0);
        GL11.glRotated(Math.toDegrees(model.bipedHead.rotateAngleZ), 0, 0, 1);
        GL11.glTranslatef(-model.bipedHead.rotationPointX, -model.bipedHead.rotationPointY, -model.bipedHead.rotationPointZ);

        ItemStack hat = ((ItemExosuitArmor) me.getItem()).getStackInSlot(me, 3);

        int level = 0;
        if (hat.hasTagCompound() && hat.stackTagCompound.hasKey("level")) {
            level = hat.stackTagCompound.getInteger("level");
        }
        ItemStack itemStack = new ItemStack(Items.emerald);
        if (level >= 18) {
            level = 18;
        }
        if (level >= 9) {
            level -= 8;
            itemStack = new ItemStack(Blocks.emerald_block);
        }
        for (int i = 0; i < level; i++) {
            GL11.glPushMatrix();
            EntityItem item = new EntityItem(entity.worldObj, 0.0F, 0.0F, 0.0F, itemStack);
            item.hoverStart = 0.0F;
            GL11.glRotated((Minecraft.getMinecraft().thePlayer.ticksExisted * 10.0D) % 360 + (360F / level) * i, 0, 1, 0);
            GL11.glTranslatef(0.75F, 0.0F, 0.0F);
            GL11.glRotated((Minecraft.getMinecraft().thePlayer.ticksExisted * 11D) % 360, 0, 1, 0);
            RenderManager.instance.renderEntityWithPosYaw(item, 0.0D, -1.0D + 0.25F * Math.sin(Math.toRadians((Minecraft.getMinecraft().thePlayer.ticksExisted * 5) % 360) + (360F / level) * i), 0.0D, 0.0F, 0.0F);
            item = null;
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
    }

    @Override
    public void writeInfo(List list) {

    }

    @Override
    public void addInformation(ItemStack me, EntityPlayer player, List list, boolean par4) {
        super.addInformation(me, player, list, par4);
        if (me.hasTagCompound()) {
            if (me.stackTagCompound.hasKey("level")) {
                int level = me.stackTagCompound.getInteger("level");
                list.add(EnumChatFormatting.GREEN + StatCollector.translateToLocal("steamcraft.exosuit.level") + " " + level);
            }
        }
    }
}
