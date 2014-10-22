package flaxbeard.steamcraft.common.block;

import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.api.IWrenchable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockThumperDummy extends Block implements IWrenchable {

    public BlockThumperDummy() {
        super(Material.iron);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int i, int j, int k) {
        int meta = world.getBlockMetadata(i, j, k) - 1;
        setBlockBounds(0.0F, 0.0F - meta, 0.0F, 1.0F, 4.0F - meta, 1.0F);
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, Block p_149695_5_) {
        int meta = world.getBlockMetadata(x, y, z) - 1;
        if (world.getBlock(x, y - meta, z) != SteamcraftBlocks.thumper) {
            world.setBlockToAir(x, y, z);
        }
        if (meta != 1) {
            for (int i = 1; i < meta; i++) {
                if (world.getBlock(x, y - i, z) != SteamcraftBlocks.thumperDummy) {
                    world.setBlockToAir(x, y, z);
                }
            }
        }
        if (meta != 3) {
            for (int i = 1; i < (3 - meta); i++) {
                if (world.getBlock(x, y + i, z) != SteamcraftBlocks.thumperDummy) {
                    world.setBlockToAir(x, y, z);
                }
            }
        }
    }


    public int quantityDropped(Random p_149745_1_) {
        return 0;
    }

    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return new ItemStack(Item.getItemFromBlock(SteamcraftBlocks.thumper), 1, 0);
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world,
                            int x, int y, int z, int side, float xO, float yO, float zO) {
        int meta = world.getBlockMetadata(x, y, z) - 1;
        if (world.getBlock(x, y - meta, z) == SteamcraftBlocks.thumper) {
            if (side != 0 && side != 1) {
                y = y - meta;
                switch (side) {
                    case 2:
                        world.setBlockMetadataWithNotify(x, y, z, 2, 2);
                        break;
                    case 3:
                        world.setBlockMetadataWithNotify(x, y, z, 0, 2);
                        break;
                    case 4:
                        world.setBlockMetadataWithNotify(x, y, z, 1, 2);
                        break;
                    case 5:
                        world.setBlockMetadataWithNotify(x, y, z, 3, 2);
                        break;
                }
                return true;
            }
            return false;
        }
        return false;
    }

}
