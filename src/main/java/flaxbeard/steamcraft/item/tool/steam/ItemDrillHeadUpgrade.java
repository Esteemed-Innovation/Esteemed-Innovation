package flaxbeard.steamcraft.item.tool.steam;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import flaxbeard.steamcraft.api.tool.ISteamToolUpgrade;
import flaxbeard.steamcraft.api.tool.SteamToolSlot;
import flaxbeard.steamcraft.misc.DrillHeadMaterial;

import java.util.List;

public class ItemDrillHeadUpgrade extends Item implements ISteamToolUpgrade {
    public IIcon icon;

    public ItemDrillHeadUpgrade() {
        this.setTextureName("steamcraft:drillHead");
    }

    @Override
    public boolean isUniversal() {
        return false;
    }

    @Override
    public int renderPriority() {
        return 1;
    }

    @Override
    public SteamToolSlot getToolSlot() {
        return SteamToolSlot.DRILL_HEAD;
    }

    @Override
    public String getInformation() {
        // Don't call this method. Call getInformation(ItemStack) instead.
        return "";
    }

    /**
     * @param self The ItemStack of this upgrade.
     * @return The localized name of the material.
     */
    public String getInformation(ItemStack self) {
        String name = DrillHeadMaterial.materials.get(getMyMaterial(self)).locName;
        return StatCollector.translateToLocal(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack self, EntityPlayer player, List list, boolean par4) {
        super.addInformation(self, player, list, par4);
        list.add(getInformation(self));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        return DrillHeadMaterial.materials.get(getMyMaterial(stack)).getColorInt();
    }

    /**
     * Sets the material name to the itemstack's NBT data.
     * @param self The ItemStack to set.
     * @param material The material to apply to the ItemStack.
     */
    public void setMyMaterial(ItemStack self, String material) {
        if (!self.hasTagCompound()) {
            self.setTagCompound(new NBTTagCompound());
        }
        self.getTagCompound().setString("material", material);
    }

    /**
     * Gets the material string for the ItemStack.
     * @param self The ItemStack to get.
     * @return The ItemStack's material name. Defaults to "Iron".
     */
    public static String getMyMaterial(ItemStack self) {
        if (!self.hasTagCompound()) {
            return "Iron";
        }
        if (self.getTagCompound().hasKey("material")) {
            return self.getTagCompound().getString("material");
        } else {
            return "Iron";
        }
    }

    @Override
    public IIcon[] getIIcons() {
        // The Drill Head Upgrade has custom rendering techniques that do not require new textures.
        return null;
    }
}
