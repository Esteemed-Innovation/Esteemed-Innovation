package flaxbeard.steamcraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.client.render.BlockSteamPipeRenderer;
import flaxbeard.steamcraft.misc.FluidHelper;
import flaxbeard.steamcraft.tile.TileEntityBoiler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.Random;

public class BlockBoiler extends BlockSteamTransporter implements IWrenchable {

    @SideOnly(Side.CLIENT)
    public static IIcon steamIcon;
    private static boolean field_149934_M;
    private final Random rand = new Random();
    private final boolean field_149932_b;
    public IIcon camoIcon;
    public IIcon camoOnIcon;
    @SideOnly(Side.CLIENT)
    private IIcon field_149935_N;
    @SideOnly(Side.CLIENT)
    private IIcon field_149936_O;
    private IIcon boilerOnIcon;
    private IIcon boilerOffIcon;

    public BlockBoiler(boolean on) {
        super(Material.iron);
        this.field_149932_b = on;
    }

    public static void updateFurnaceBlockState(boolean isOn, World world, int x, int y, int z) {
        int l = world.getBlockMetadata(x, y, z);
        TileEntity tileentity = world.getTileEntity(x, y, z);
        field_149934_M = true;

        if (isOn) {
            world.setBlock(x, y, z, SteamcraftBlocks.boilerOn);
        } else {
            world.setBlock(x, y, x, SteamcraftBlocks.boiler);
        }

        field_149934_M = false;
        world.setBlockMetadataWithNotify(x, y, x, l, 2);

        if (tileentity != null) {
            tileentity.validate();
            world.setTileEntity(x, y, x, tileentity);
        }
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return Steamcraft.boilerRenderID;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
        ForgeDirection dir = ForgeDirection.getOrientation(side).getOpposite();
        int x2 = x + dir.offsetX;
        int y2 = y + dir.offsetY;
        int z2 = z + dir.offsetZ;

        if (world.getTileEntity(x2, y2, z2) instanceof TileEntityBoiler) {
            TileEntityBoiler boiler = (TileEntityBoiler) world.getTileEntity(x2, y2, z2);
            int l = world.getBlockMetadata(x2, y2, z2);
            if (boiler != null && boiler.disguiseBlock != null && boiler.disguiseBlock != Blocks.air
                    && !BlockSteamPipeRenderer.updateWrenchStatus()) {

                return side == l ? super.shouldSideBeRendered(world, x, y, z, side) : false;
            }
        }
        return super.shouldSideBeRendered(world, x, y, z, side);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        TileEntityBoiler boiler = (TileEntityBoiler) world.getTileEntity(x, y, z);
        if (boiler.isBurning()) {
            int l = world.getBlockMetadata(x, y, z);
            float f = (float) x + 0.5F;
            float f1 = (float) y + 0.0F + rand.nextFloat() * 6.0F / 16.0F;
            float f2 = (float) z + 0.5F;
            float f3 = 0.52F;
            float f4 = rand.nextFloat() * 0.6F - 0.3F;

            if (l == 4) {
                world.spawnParticle("smoke", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
            } else if (l == 5) {
                world.spawnParticle("smoke", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
            } else if (l == 2) {
                world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
            } else if (l == 3) {
                world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        this.func_149930_e(world, x, y, z);
    }

    private void func_149930_e(World world, int x, int y, int z) {
        if (!world.isRemote) {
            Block block = world.getBlock(x, y, z - 1);
            Block block1 = world.getBlock(x, y, z + 1);
            Block block2 = world.getBlock(x - 1, y, z);
            Block block3 = world.getBlock(x + 1, y, z);
            byte b0 = 3;

            if (block.func_149730_j() && !block1.func_149730_j()) {
                b0 = 3;
            }

            if (block1.func_149730_j() && !block.func_149730_j()) {
                b0 = 2;
            }

            if (block2.func_149730_j() && !block3.func_149730_j()) {
                b0 = 5;
            }

            if (block3.func_149730_j() && !block2.func_149730_j()) {
                b0 = 4;
            }

            world.setBlockMetadataWithNotify(x, y, z, b0, 2);
        }
    }

    @Override
    public IIcon getIcon(IBlockAccess block, int x, int y, int z, int side) {
        int meta = block.getBlockMetadata(x, y, z);
        if (meta == 0) {
            meta = 3;
        }
        if (side == meta) {
            TileEntityBoiler boiler = (TileEntityBoiler) block.getTileEntity(x, y, z);
            return boiler.isBurning() ? boilerOnIcon : boilerOffIcon;
        } else {
            return side == 1 ? this.field_149935_N : this.blockIcon;
        }

    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if (meta == 0) {
            meta = 3;
        }
        return side == 1 ? this.field_149935_N
                : (side == 0 ? this.field_149935_N : (side != meta ? this.blockIcon : this.boilerOffIcon));
    }

    @Override
    public void registerBlockIcons(IIconRegister ir) {
        this.blockIcon = ir.registerIcon("steamcraft:blockBrass");
        steamIcon = ir.registerIcon("steamcraft:steam");

        this.boilerOnIcon = ir.registerIcon("steamcraft:boilerOn");
        this.boilerOffIcon = ir.registerIcon("steamcraft:boiler");
        this.camoOnIcon = ir.registerIcon("steamcraft:boilerCamoOn");
        this.camoIcon = ir.registerIcon("steamcraft:boilerCamo");
        // this.field_149936_O = p_149651_1_.registerIcon(this.field_149932_b ?
        // "steamcraft:boilerOn" : "steamcraft:boiler");
        this.field_149935_N = ir.registerIcon("steamcraft:blockBrass");
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase elb, ItemStack stack) {
        int l = MathHelper.floor_double((double) (elb.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l == 0) {
            world.setBlockMetadataWithNotify(x, y, z, 2, 2);
        }

        if (l == 1) {
            world.setBlockMetadataWithNotify(x, y, z, 5, 2);
        }

        if (l == 2) {
            world.setBlockMetadataWithNotify(x, y, z, 3, 2);
        }

        if (l == 3) {
            world.setBlockMetadataWithNotify(x, y, z, 4, 2);
        }

        if (stack.hasDisplayName()) {
            // ((TileEntityBoiler)world.getTileEntity(x, y,
            // z)).func_145951_a(stack.getDisplayName());
        }
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntityBoiler();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xf, float yf,
            float zf) {
        TileEntityBoiler tileEntity = (TileEntityBoiler) world.getTileEntity(x, y, z);

        boolean isClient = !world.isRemote;
        if (tileEntity != null) {
            if (!FluidHelper.playerIsHoldingWaterContainer(player) && isClient) {
                player.openGui(Steamcraft.instance, 0, world, x, y, z);
            } else {
                FluidHelper.fillTankFromHeldItem(player, tileEntity.myTank);
            }
        }
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        if (!field_149934_M) {
            TileEntityBoiler tileentityboiler = (TileEntityBoiler) world.getTileEntity(x, y, z);

            if (tileentityboiler != null) {
                for (int i1 = 0; i1 < tileentityboiler.getSizeInventory(); ++i1) {
                    ItemStack itemstack = tileentityboiler.getStackInSlot(i1);

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
                            EntityItem entityitem = new EntityItem(world, (double) ((float) x + f),
                                    (double) ((float) y + f1), (double) ((float) z + f2),
                                    new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

                            if (itemstack.hasTagCompound()) {
                                entityitem.getEntityItem()
                                        .setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
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
        }

        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        return new ItemStack(SteamcraftBlocks.boiler);
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xO,
            float yO, float zO) {
        int meta = world.getBlockMetadata(x, y, z);
        if (player.isSneaking()) {
            return true;
        } else if (side != 0 && side != 1) {
            world.setBlockMetadataWithNotify(x, y, z,
                    side == meta ? ForgeDirection.getOrientation(side).getOpposite().ordinal() : side, 2);
            return true;
        }
        return false;
    }
}
