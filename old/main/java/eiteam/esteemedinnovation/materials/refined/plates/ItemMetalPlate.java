package eiteam.esteemedinnovation.materials.refined.plates;

import eiteam.esteemedinnovation.materials.MaterialsModule;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemMetalPlate extends Item {
    public ItemMetalPlate() {
        setHasSubtypes(true);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            for (Types type : Types.values()) {
                items.add(new ItemStack(this, 1, type.getMeta()));
            }
        }
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return super.getTranslationKey() + "." + stack.getItemDamage();
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return EnumActionResult.SUCCESS;
        }
        if (facing != EnumFacing.UP) {
            return EnumActionResult.FAIL;
        }

        ItemStack stack = player.getHeldItem(hand);
        Block plateBlock = MaterialsModule.getPressurePlateFromItemMetadata(stack.getItemDamage());
        if (plateBlock == null) {
            return EnumActionResult.FAIL;
        }

        IBlockState iblockstate = world.getBlockState(pos);
        Block block = iblockstate.getBlock();
        boolean replace = block.isReplaceable(world, pos);

        if (!replace) {
            pos = pos.up();
        }

        if (player.canPlayerEdit(pos, facing, stack)) {
            BlockPos below = pos.down();
            boolean canPlace = (replace || world.isAirBlock(pos)) && world.getBlockState(below).isSideSolid(world, below, EnumFacing.UP);

            if (canPlace) {
                world.setBlockState(pos, plateBlock.getDefaultState());

                SoundType sound = plateBlock.getSoundType(plateBlock.getDefaultState(), world, pos, player);
                world.playSound(null, pos, sound.getPlaceSound(), SoundCategory.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
                stack.shrink(1);
                return EnumActionResult.SUCCESS;
            } else {
                return EnumActionResult.FAIL;
            }
        } else {
            return EnumActionResult.FAIL;
        }
    }

    public enum Types {
        COPPER_PLATE(0),
        ZINC_PLATE(1),
        BRASS_PLATE(2),
        GILDED_IRON_PLATE(3),
        IRON_PLATE(4),
        GOLD_PLATE(5);

        private final int meta;

        Types(int meta) {
            this.meta = meta;
        }

        public int getMeta() {
            return meta;
        }
    }
}
