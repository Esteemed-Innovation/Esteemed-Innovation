package flaxbeard.steamcraft.item.tool;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.api.exosuit.ExosuitSlot;
import flaxbeard.steamcraft.api.exosuit.IExosuitUpgrade;
import flaxbeard.steamcraft.api.exosuit.ModelExosuitUpgrade;
import flaxbeard.steamcraft.client.render.model.ModelTophat;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemTophat extends ItemArmor implements IExosuitUpgrade {

    private static ModelTophat modelTophat;

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
    public ResourceLocation getOverlay() {
        return null;
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return null;
    }

    @Override
    public void updateModel(ModelBiped parentModel, EntityLivingBase entityLivingBase, ItemStack itemStack, ModelExosuitUpgrade modelExosuitUpgrade) {

    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
        if (modelTophat == null)
            modelTophat = new ModelTophat();

        if (itemStack.hasTagCompound() && itemStack.stackTagCompound.hasKey("level")) {
            modelTophat.level = itemStack.stackTagCompound.getInteger("level");
        }

        return modelTophat;
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
