package eiteam.esteemedinnovation.metals.refined.plates;

import eiteam.esteemedinnovation.metals.MetalsModule;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemMetalPlate extends Item {
    public ItemMetalPlate() {
        setHasSubtypes(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> subItems) {
        for (Types type : Types.values()) {
            subItems.add(new ItemStack(this, 1, type.getMeta()));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + stack.getItemDamage();
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return EnumActionResult.SUCCESS;
        }
        if (facing != EnumFacing.UP) {
            return EnumActionResult.FAIL;
        }

        Block plateBlock = MetalsModule.getPressurePlateFromItemMetadata(stack.getItemDamage());
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
                --stack.stackSize;
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
