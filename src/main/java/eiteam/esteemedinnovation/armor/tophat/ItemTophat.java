package eiteam.esteemedinnovation.armor.tophat;

import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.api.exosuit.ExosuitUpgrade;
import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ItemTophat extends ItemArmor implements ExosuitUpgrade {
    private static Map<UUID, ModelTophat> tophatModels = new HashMap<>();
    private boolean emerald;

    public ItemTophat(ArmorMaterial armorMaterial, int renderIndex, EntityEquipmentSlot armorType, boolean isEmerald) {
        super(armorMaterial, renderIndex, armorType);
        emerald = isEmerald;
    }

    @Override
    public void onCreated(ItemStack me, World world, EntityPlayer player) {
        String name = player.getDisplayName().getFormattedText();
        if ("Flaxbeard".equals(name) || "ForgeDevName".equals(name)) {
            me.setTagCompound(new NBTTagCompound());
            me.getTagCompound().setBoolean("Flaxbeard", true);
        }
    }

    @Override
    public EnumRarity getRarity(ItemStack me) {
        if (me.hasTagCompound() && me.getTagCompound().hasKey("Flaxbeard")) {
            return EnumRarity.EPIC;
        }
        return super.getRarity(me);
    }

    @Override
    public String getUnlocalizedName(ItemStack me) {
        if (me.hasTagCompound() && me.getTagCompound().hasKey("Flaxbeard")) {
            return "item.esteemedinnovation:kek";
        }
        return super.getUnlocalizedName(me);
    }

    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return par2ItemStack.isItemEqual(new ItemStack(Items.LEATHER)) || super.getIsRepairable(par1ItemStack, par2ItemStack);
    }

    @Override
    public int renderPriority() {
        return 0;
    }

    @Override
    public ExosuitSlot getSlot() {
        return ExosuitSlot.HEAD_HELM;
    }

    @Override
    public ResourceLocation getOverlay() {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        UUID entityID = entityLiving.getUniqueID();
        boolean contains = tophatModels.containsKey(entityID);
        ModelTophat hatModel = contains ? tophatModels.get(entityID) : new ModelTophat();
        if (itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("NewTradesLevel")) {
            hatModel.setLevel(itemStack.getTagCompound().getInteger("NewTradesLevel"));
        }

        if (!contains) {
            tophatModels.put(entityID, hatModel);
        }

        return hatModel;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return EsteemedInnovation.MOD_ID + ":textures/models/armor/tophat" + (emerald ? "emerald" : "") + ".png";
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return null;
    }

    @Override
    public void updateModel(ModelBiped parentModel, EntityLivingBase entityLivingBase, ItemStack itemStack, ModelExosuitUpgrade modelExosuitUpgrade) {}

    @Override
    public void writeInfo(List list) {}

    @Override
    public void addInformation(ItemStack me, EntityPlayer player, List<String> list, boolean par4) {
        super.addInformation(me, player, list, par4);
        if (me.hasTagCompound() && me.getTagCompound().hasKey("NewTradesLevel")) {
            int level = me.getTagCompound().getInteger("NewTradesLevel");
            list.add(TextFormatting.GREEN + I18n.format("esteemedinnovation.exosuit.level", level));
        }
    }
}
