package eiteam.esteemedinnovation.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class ItemAstrolabe extends Item {
    public ItemAstrolabe() {
        setMaxStackSize(1);
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean advanced) {
        if (item.hasTagCompound()) {
            NBTTagCompound nbt = item.getTagCompound();
            if (nbt.hasKey("targetX")) {
                int x = nbt.getInteger("targetX");
                int z = nbt.getInteger("targetZ");
                int dim = nbt.getInteger("dim");
                String dimension;
                switch (dim) {
                    case -1: {
                        dimension = I18n.format("esteemedinnovation.astrolabe.dimension.nether");
                        break;
                    }
                    case 0: {
                        dimension = I18n.format("esteemedinnovation.astrolabe.dimension.overworld");
                        break;
                    }
                    case 1: {
                        dimension = I18n.format("esteemedinnovation.astrolabe.dimension.end");
                        break;
                    }
                    default: {
                        dimension = I18n.format("esteemedinnovation.astrolabe.dimension", dim);
                        break;
                    }
                }
                tooltip.add(I18n.format("esteemedinnovation.astrolabe.target", x, z, dimension));
                return;
            }
        }
        tooltip.add(I18n.format("esteemedinnovation.astrolabe.noTarget"));
    }

    @Override
    public EnumActionResult onItemUse(ItemStack item, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            if (!item.hasTagCompound()) {
                item.setTagCompound(new NBTTagCompound());
            }
            item.getTagCompound().setInteger("targetX", pos.getX());
            item.getTagCompound().setInteger("targetZ", pos.getZ());
            item.getTagCompound().setInteger("dim", world.provider.getDimension());
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }
}
