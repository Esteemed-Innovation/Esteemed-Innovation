package flaxbeard.steamcraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.client.render.BlockSteamPipeRenderer;
import flaxbeard.steamcraft.tile.TileEntityCustomFurnace;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public class BlockCustomFurnace extends BlockFurnace {

    private static boolean fieldIsOn;
    private final Random field_149933_a = new Random();
    public IIcon camoIcon;
    public IIcon camoOnIcon;
    public IIcon frontIcon;

    public BlockCustomFurnace(boolean on) {
        super(on);
    }

    public static void updateFurnaceBlockState(boolean isOn, World world, int x, int y, int z) {
        int l = world.getBlockMetadata(x, y, z);
        TileEntity tileentity = world.getTileEntity(x, y, z);
        fieldIsOn = true;

        if (isOn) {
            world.setBlock(x, y, z, SteamcraftBlocks.customFurnace);
        } else {
            world.setBlock(x, y, z, SteamcraftBlocks.customFurnaceOff);
        }

        fieldIsOn = false;
        world.setBlockMetadataWithNotify(x, y, z, l, 2);

        if (tileentity != null) {
            tileentity.validate();
            world.setTileEntity(x, y, z, tileentity);
        }
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return Steamcraft.furnaceRenderID;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
        ForgeDirection dir = ForgeDirection.getOrientation(side).getOpposite();
        int x2 = x + dir.offsetX;
        int y2 = y + dir.offsetY;
        int z2 = z + dir.offsetZ;

        if (world.getTileEntity(x2, y2, z2) instanceof TileEntityCustomFurnace) {
            TileEntityCustomFurnace boiler = (TileEntityCustomFurnace) world.getTileEntity(x2, y2, z2);
            int l = world.getBlockMetadata(x2, y2, z2);
            if (boiler != null && boiler.disguiseBlock != null && boiler.disguiseBlock != Blocks.air && !BlockSteamPipeRenderer.updateWrenchStatus()) {

                return side == l ? super.shouldSideBeRendered(world, x, y, z, side) : false;
            }
        }
        return super.shouldSideBeRendered(world, x, y, z, side);
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return Item.getItemFromBlock(SteamcraftBlocks.customFurnaceOff);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityCustomFurnace();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Item getItem(World world, int x, int y, int z) {
        return Item.getItemFromBlock(SteamcraftBlocks.customFurnaceOff);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        if (!fieldIsOn) {
            TileEntityFurnace tileentityfurnace = (TileEntityFurnace) world.getTileEntity(x, y, z);

            if (tileentityfurnace != null) {
                for (int i1 = 0; i1 < tileentityfurnace.getSizeInventory(); ++i1) {
                    ItemStack itemstack = tileentityfurnace.getStackInSlot(i1);

                    if (itemstack != null) {
                        float f = this.field_149933_a.nextFloat() * 0.8F + 0.1F;
                        float f1 = this.field_149933_a.nextFloat() * 0.8F + 0.1F;
                        float f2 = this.field_149933_a.nextFloat() * 0.8F + 0.1F;

                        while (itemstack.stackSize > 0) {
                            int j1 = this.field_149933_a.nextInt(21) + 10;

                            if (j1 > itemstack.stackSize) {
                                j1 = itemstack.stackSize;
                            }

                            itemstack.stackSize -= j1;
                            EntityItem entityitem = new EntityItem(world, (double) ((float) x + f), (double) ((float) y + f1), (double) ((float) z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

                            if (itemstack.hasTagCompound()) {
                                entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                            }

                            float f3 = 0.05F;
                            entityitem.motionX = (double) ((float) this.field_149933_a.nextGaussian() * f3);
                            entityitem.motionY = (double) ((float) this.field_149933_a.nextGaussian() * f3 + 0.2F);
                            entityitem.motionZ = (double) ((float) this.field_149933_a.nextGaussian() * f3);
                            world.spawnEntityInWorld(entityitem);
                        }
                    }
                }

                world.func_147453_f(x, y, z, block);
            }
        }
        world.removeTileEntity(x, y, z);
    }

    @Override
    public void registerBlockIcons(IIconRegister ir) {
        super.registerBlockIcons(ir);
        this.camoOnIcon = ir.registerIcon("steamcraft:camoFurnaceOn");
        this.camoIcon = ir.registerIcon("steamcraft:camoFurnace");
        this.frontIcon = ir.registerIcon("furnace_front_off");
    }

}
