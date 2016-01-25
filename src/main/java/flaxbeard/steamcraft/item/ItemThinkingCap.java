package flaxbeard.steamcraft.item;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.MutablePair;
import flaxbeard.steamcraft.api.IEngineerable;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.exosuit.ExosuitSlot;
import flaxbeard.steamcraft.api.exosuit.IExosuitUpgrade;
import flaxbeard.steamcraft.api.exosuit.ModelExosuitUpgrade;
import flaxbeard.steamcraft.gui.GuiEngineeringTable;

import java.util.List;

public class ItemThinkingCap extends ItemArmor implements IExosuitUpgrade, IEngineerable {
    public ItemThinkingCap(ArmorMaterial material, int renderIndex, int armorType) {
        super(material, renderIndex, armorType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack me, EntityPlayer player, List list, boolean par4) {
        super.addInformation(me, player, list, par4);
        if (!me.hasTagCompound() || !me.stackTagCompound.hasKey("filters")) {
            return;
        }
        for (int i = 3; i < 10; i++) {
            if (me.stackTagCompound.getCompoundTag("inv").hasKey(Integer.toString(i))) {
                ItemStack stack = ItemStack.loadItemStackFromNBT(me.stackTagCompound.getCompoundTag("inv").getCompoundTag(Integer.toString(i)));
                list.add(EnumChatFormatting.RED + stack.getDisplayName());
            }
        }
    }

    @Override
    public int getMaxDamage(ItemStack me) {
        return 0;
    }

    @Override
    public boolean showDurabilityBar(ItemStack me) {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public MutablePair<Integer, Integer>[] engineerCoordinates() {
        return new MutablePair[]{
          MutablePair.of(59, 36)
        };
    }

    @Override
    public ItemStack getStackInSlot(ItemStack me, int slot) {
        if (me == null) {
            return null;
        }
        if (me.hasTagCompound()) {
            if (me.stackTagCompound.hasKey("filters")) {
                NBTTagCompound nbt = me.stackTagCompound.getCompoundTag("filters");
                if (nbt.hasKey(Integer.toString(slot))) {
                    return ItemStack.loadItemStackFromNBT(nbt.getCompoundTag(Integer.toString(slot)));
                }
            }
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(ItemStack me, int slot, ItemStack stack) {
        if (!me.hasTagCompound()) {
            me.setTagCompound(new NBTTagCompound());
        }
        if (!me.stackTagCompound.hasKey("filters")) {
            me.stackTagCompound.setTag("filters", new NBTTagCompound());
        }
        if (me.stackTagCompound.getCompoundTag("filters").hasKey(Integer.toString(slot))) {
            me.stackTagCompound.getCompoundTag("filters").removeTag(Integer.toString(slot));
        }
        NBTTagCompound stc = new NBTTagCompound();
        if (stack != null) {
            stack.writeToNBT(stc);
            me.stackTagCompound.getCompoundTag("filters").setTag(Integer.toString(slot), stc);
        }
    }

    @Override
    public boolean isItemValidForSlot(ItemStack me, int slot, ItemStack var2) {
        return true;
    }

    @Override
    public ItemStack decrStackSize(ItemStack me, int slot, int par3) {
        if (this.getStackInSlot(me, slot) != null) {
            ItemStack itemstack;
            if (this.getStackInSlot(me, slot).stackSize <= par3) {
                itemstack = this.getStackInSlot(me, slot);
                this.setInventorySlotContents(me, slot, null);
                return itemstack;
            } else {
                ItemStack stack2 = this.getStackInSlot(me, slot);
                itemstack = stack2.splitStack(par3);
                this.setInventorySlotContents(me, slot, stack2);

                if (this.getStackInSlot(me, slot).stackSize == 0) {
                    this.setInventorySlotContents(me, slot, null);
                }
                return itemstack;
            }
        } else {
            return null;
        }
    }

    @Override
    public void drawSlot(GuiContainer guiEngineeringTable, int slotnum, int i, int j) {
        guiEngineeringTable.mc.getTextureManager().bindTexture(GuiEngineeringTable.furnaceGuiTextures);
        guiEngineeringTable.drawTexturedModalRect(i, j, 194, 0, 18, 18);
    }

    @Override
    public boolean canPutInSlot(ItemStack me, int slotNum, ItemStack upgrade) {
        Item item = upgrade.getItem();
        int meta = upgrade.getItemDamage() == OreDictionary.WILDCARD_VALUE ? 0 : upgrade.getItemDamage();
        return SteamcraftRegistry.thinkingCapUpgrades.contains(MutablePair.of(item, meta));
    }

    @Override
    public void drawBackground(GuiEngineeringTable guiEngineeringTable, int i, int j, int k) {
        guiEngineeringTable.mc.getTextureManager().bindTexture(ItemExosuitArmor.largeIcons);
        guiEngineeringTable.drawTexturedModalRect(j + 26, k + 3, 0, 0, 64, 64);
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
        // TODO
        return null;
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return null;
    }

    @Override
    public void updateModel(ModelBiped parentModel, EntityLivingBase entityLivingBase, ItemStack itemStack, ModelExosuitUpgrade modelExosuitUpgrade) {}

    @Override
    public void writeInfo(List list) {}
}
