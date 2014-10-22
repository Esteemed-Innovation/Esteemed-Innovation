package flaxbeard.steamcraft.common.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;

public class ItemAstrolabe extends Item {

    public ItemAstrolabe() {
        this.setMaxStackSize(1);
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        if (item.hasTagCompound() && item.getTagCompound().hasKey("targetX")) {
            String dimension = I18n.format("steamcraft.astrolabe.dimension") + " " + item.stackTagCompound.getInteger("dim");
            if (item.stackTagCompound.getInteger("dim") == 0) {
                dimension = I18n.format("steamcraft.astrolabe.dimension.overworld");
            }
            if (item.stackTagCompound.getInteger("dim") == -1) {
                dimension = I18n.format("steamcraft.astrolabe.dimension.nether");
            }
            if (item.stackTagCompound.getInteger("dim") == 1) {
                dimension = I18n.format("steamcraft.astrolabe.dimension.end");
            }
            par3List.add(I18n.format("steamcraft.astrolabe.target") + " " + item.stackTagCompound.getInteger("targetX") + ", " + item.stackTagCompound.getInteger("targetZ") + " " + I18n.format("steamcraft.astrolabe.in") + " " + dimension);
        } else {
            par3List.add(I18n.format("steamcraft.astrolabe.noTarget"));
        }
    }

    public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10) {
        if (player.isSneaking()) {
            if (!item.hasTagCompound()) {
                item.setTagCompound(new NBTTagCompound());
            }
            item.getTagCompound().setInteger("targetX", x);
            item.getTagCompound().setInteger("targetZ", z);
            item.getTagCompound().setInteger("dim", world.provider.dimensionId);
            return true;
        }
        return false;
    }
}
