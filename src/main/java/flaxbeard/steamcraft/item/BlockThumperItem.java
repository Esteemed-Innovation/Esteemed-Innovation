package flaxbeard.steamcraft.item;

import flaxbeard.steamcraft.SteamcraftBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockThumperItem extends ItemBlock {

    public BlockThumperItem(Block block) {
        super(block);
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        Block block = par3World.getBlock(par4, par5, par6);

        if (block == Blocks.snow_layer && (par3World.getBlockMetadata(par4, par5, par6) & 7) < 1) {
            par7 = 1;
        } else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush && !block.isReplaceable(par3World, par4, par5, par6)) {
            if (par7 == 0) {
                --par5;
            }

            if (par7 == 1) {
                ++par5;
            }

            if (par7 == 2) {
                --par6;
            }

            if (par7 == 3) {
                ++par6;
            }

            if (par7 == 4) {
                --par4;
            }

            if (par7 == 5) {
                ++par4;
            }
        }

        if (par1ItemStack.stackSize == 0) {
            return false;
        } else if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack) && par2EntityPlayer.canPlayerEdit(par4, par5 + 1, par6, par7, par1ItemStack) && par2EntityPlayer.canPlayerEdit(par4, par5 + 2, par6, par7, par1ItemStack) && par2EntityPlayer.canPlayerEdit(par4, par5 + 3, par6, par7, par1ItemStack)) {
            return false;
        } else if (par5 == 255 && this.field_150939_a.getMaterial().isSolid()) {
            return false;
        } else if (par3World.canPlaceEntityOnSide(this.field_150939_a, par4, par5, par6, false, par7, par2EntityPlayer, par1ItemStack)) {
            for (int i = 1; i < 4; i++) {
                Block block2 = par3World.getBlock(par4, par5 + i, par6);
                ItemStack stack = new ItemStack(SteamcraftBlocks.thumperDummy, 1, 0);
                int j1 = this.field_150939_a.onBlockPlaced(par3World, par4, par5 + i, par6, par7, par8, par9, par10, i + 1);
                if (!par3World.isAirBlock(par4, par5 + i, par6) && !block2.isReplaceable(par3World, par4, par5 + i, par6)) {
                    return false;
                }
//                if (!checkPlaceBlockAt(SteamcraftBlocks.thumperDummy, stack, par2EntityPlayer, par3World, par4, par5+i, par6, par7, par8, par9, par10, j1)) {
//                	return false;
//                }
            }
            int i1 = this.getMetadata(par1ItemStack.getItemDamage());
            int j1 = this.field_150939_a.onBlockPlaced(par3World, par4, par5, par6, par7, par8, par9, par10, i1);
            if (placeBlockAt(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10, j1)) {
                for (int i = 1; i < 4; i++) {
                    Block block2 = par3World.getBlock(par4, par5 + i, par6);
                    ItemStack stack = new ItemStack(SteamcraftBlocks.thumperDummy, 1, 0);
                    j1 = this.field_150939_a.onBlockPlaced(par3World, par4, par5 + i, par6, par7, par8, par9, par10, i + 1);
                    placeBlockAt(SteamcraftBlocks.thumperDummy, stack, par2EntityPlayer, par3World, par4, par5 + i, par6, par7, par8, par9, par10, j1);
                }
                par3World.playSoundEffect((double) ((float) par4 + 0.5F), (double) ((float) par5 + 0.5F), (double) ((float) par6 + 0.5F), this.field_150939_a.stepSound.func_150496_b(), (this.field_150939_a.stepSound.getVolume() + 1.0F) / 2.0F, this.field_150939_a.stepSound.getPitch() * 0.8F);
                --par1ItemStack.stackSize;
            }

            return true;
        } else {
            return false;
        }
    }

    public boolean checkPlaceBlockAt(Block block, ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        if (!world.setBlock(x, y, z, block, metadata, 3)) {
            return false;
        }
        world.setBlockToAir(x, y, z);


        return true;
    }

    public boolean placeBlockAt(Block block, ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {

        if (!world.setBlock(x, y, z, block, metadata, 3)) {
            return false;
        }

        if (world.getBlock(x, y, z) == block) {
            block.onBlockPlacedBy(world, x, y, z, player, stack);
            block.onPostBlockPlaced(world, x, y, z, metadata);
        }

        return true;
    }
}
