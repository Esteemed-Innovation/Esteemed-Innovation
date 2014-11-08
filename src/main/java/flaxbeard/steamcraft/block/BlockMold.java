package flaxbeard.steamcraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.api.ICrucibleMold;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.tile.TileEntityMold;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public class BlockMold extends BlockContainer implements IWrenchable {
    private static float px = (1.0F / 16.0F);
    private final Random rand = new Random();
    public IIcon blank;


    public BlockMold() {
        super(Material.rock);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntityMold tileentitymold = (TileEntityMold) world.getTileEntity(x, y, z);

        if (tileentitymold != null) {
            for (int i1 = 0; i1 < tileentitymold.getSizeInventory(); ++i1) {
                ItemStack itemstack = tileentitymold.getStackInSlot(i1);

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
                itemstack = tileentitymold.mold[0];

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


    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityMold();
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase elb, ItemStack stack) {
        int l = MathHelper.floor_double((double) (elb.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
        world.setBlockMetadataWithNotify(x, y, z, l, 2);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        return blank;

    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1iBlockAccess, int par2, int par3, int par4) {
        setBlockBounds(2 * px, 0.0F, 2 * px, 1.0F - 2 * px, 1.0F - 8 * px, 1.0F - 2 * px);
        super.setBlockBoundsBasedOnState(par1iBlockAccess, par2, par3, par4);
    }


    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
        return AxisAlignedBB.getBoundingBox(i + 2 * px, j + 0.0F, k + 2 * px, i + 1.0F - 2 * px, j + 1.0F - 8 * px, k + 1.0F - 2 * px);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        TileEntityMold tile = (TileEntityMold) world.getTileEntity(x, y, z);
        boolean editingMold = false;
        if (player.getHeldItem() != null) {
            if (player.getHeldItem().getItem() instanceof ICrucibleMold) {
                editingMold = true;
            }
        }
        if (player.isSneaking()) {
            editingMold = true;
        }
        if (editingMold) {
            if (tile.open) {
                if (tile.mold[0] != null) {
                    if (!world.isRemote) {
                        if (!player.capabilities.isCreativeMode) {
                            tile.dropItem(tile.mold[0]);
                        }
                    }
                    tile.mold[0] = null;
                    world.markBlockForUpdate(x, y, z);

                }
                if (player.getHeldItem() != null) {
                    if (player.getHeldItem().getItem() instanceof ICrucibleMold) {
                        tile.mold[0] = player.getHeldItem();
                        if (!player.capabilities.isCreativeMode) {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                        }
                        world.markBlockForUpdate(x, y, z);

                    }
                }
            }
        } else {
            if (tile.changeTicks == 0 && (player.getHeldItem() == null || !(player.getHeldItem().getItem() instanceof ItemBlock))) {
                tile.open = !tile.open;
                tile.changeTicks = 20;
                world.markBlockForUpdate(x, y, z);

            }

        }

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister ir) {
        this.blank = ir.registerIcon("steamcraft:blankTexture");
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xO, float yO, float zO) {
        int meta = world.getBlockMetadata(x, y, z);
        if (side != 0 && side != 1) {
            int output = meta;
            switch (side) {
                case 2:
                    output = 2;
                    break;
                case 3:
                    output = 0;
                    break;
                case 4:
                    output = 1;
                    break;
                case 5:
                    output = 3;
                    break;
            }
            if (output == meta && side > 1 && side < 6) {
                switch (ForgeDirection.getOrientation(side).getOpposite().ordinal()) {
                    case 2:
                        output = 2;
                        break;
                    case 3:
                        output = 0;
                        break;
                    case 4:
                        output = 1;
                        break;
                    case 5:
                        output = 3;
                        break;
                }
            }
            world.setBlockMetadataWithNotify(x, y, z, output, 2);
            return true;
        }
        return false;
    }

}
