package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.tile.TileEntityItemMortar;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Random;

public class BlockItemMortar extends BlockContainer {

    private final Random rand = new Random();

    public BlockItemMortar() {
        super(Material.iron);
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntityItemMortar();
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        TileEntityItemMortar tile = (TileEntityItemMortar) world.getTileEntity(x, y, z);

        if (player.getHeldItem() != null) {
            ItemStack item = player.getHeldItem();
            if (item.getItem() == SteamcraftItems.astrolabe) {
                if (item.hasTagCompound() && item.stackTagCompound.hasKey("targetX")) {
                    if (world.provider.dimensionId == item.stackTagCompound.getInteger("dim")) {
                        tile.xT = item.stackTagCompound.getInteger("targetX");
                        tile.zT = item.stackTagCompound.getInteger("targetZ");
                        tile.hasTarget = true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntityItemMortar tileentitymortar = (TileEntityItemMortar) world.getTileEntity(x, y, z);
        if (tileentitymortar != null) {
            for (int i1 = 0; i1 < tileentitymortar.getSizeInventory(); ++i1) {
                ItemStack itemstack = tileentitymortar.getStackInSlot(i1);

                if (itemstack != null) {
                    float f = this.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = this.rand.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.stackSize > 0) {
                        int j1 = this.rand.nextInt(21) + 10;

                        if (j1 > itemstack.stackSize) {
                            j1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= j1;
                        EntityItem entityitem = new EntityItem(world, (double) ((float) x + f), (double) ((float) y + f1), (double) ((float) z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

                        if (itemstack.hasTagCompound()) {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                        }

                        float f3 = 0.05F;
                        entityitem.motionX = (double) ((float) this.rand.nextGaussian() * f3);
                        entityitem.motionY = (double) ((float) this.rand.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double) ((float) this.rand.nextGaussian() * f3);
                        world.spawnEntityInWorld(entityitem);
                    }
                }
            }

            world.func_147453_f(x, y, z, block);
        }
        super.breakBlock(world, x, y, z, block, meta);
    }

}