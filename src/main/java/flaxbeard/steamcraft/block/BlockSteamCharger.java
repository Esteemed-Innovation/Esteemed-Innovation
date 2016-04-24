package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.ISteamChargable;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.integration.CrossMod;
import flaxbeard.steamcraft.tile.TileEntitySteamCharger;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import tconstruct.library.tools.ToolCore;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockSteamCharger extends BlockSteamTransporter implements IWrenchable {
    private final Random rand = new Random();
    @SideOnly(Side.CLIENT)
    public IIcon top;
    private IIcon bottom;

    public BlockSteamCharger() {
        super(Material.iron);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 0.5F, z + 1);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase elb, ItemStack stack) {
        int l = MathHelper.floor_double((double) (elb.rotationYaw * 4F / 360F) + 2.5D) & 3;
        world.setBlockMetadataWithNotify(x, y, z, l, 2);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        float px = 1.0F / 16.0F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
    }

    /**
     * Whether the item is chargeable (and can be charged) or it is an empty cell item.
     * @param item The ItemStack to check.
     * @return Whether the item can be placed in the TileEntity's inventory.
     */
    private boolean canItemBeCharged(ItemStack item) {
        if (item == null) {
            return false;
        }
        if (item.getItem() instanceof ISteamChargable) {
            return ((ISteamChargable) item.getItem()).canCharge(item);
        } else {
            if (CrossMod.TINKERS_CONSTRUCT && item.getItem() instanceof ToolCore) {
                NBTTagCompound nbt = item.getTagCompound();
                return nbt.getCompoundTag("InfiTool").hasKey("Steam");
            }
            return item.getItem() == SteamcraftItems.steamcellEmpty;
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        TileEntitySteamCharger tile = (TileEntitySteamCharger) world.getTileEntity(x, y, z);
        if (tile.getStackInSlot(0) != null) {
            if (!world.isRemote) {
                tile.dropItem(tile.getStackInSlot(0));
            }
            tile.setInventorySlotContents(0, null);
        } else {
            if (canItemBeCharged(player.getHeldItem())) {
                ItemStack copy = player.getCurrentEquippedItem().copy();
                copy.stackSize = 1;
                tile.setInventorySlotContents(0, copy);
                player.getCurrentEquippedItem().stackSize -= 1;
                tile.randomDegrees = world.rand.nextInt(361);
            }
        }
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return side == 1 ? this.bottom : (side == 0 ? this.bottom : this.blockIcon);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister ir) {
        this.blockIcon = ir.registerIcon("steamcraft:blockCharger");
        this.top = ir.registerIcon("steamcraft:blockChargerTop");
        this.bottom = ir.registerIcon("steamcraft:blockBrass");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntitySteamCharger();
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return Steamcraft.chargerRenderID;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntitySteamCharger tileentitysteamcharger =
          (TileEntitySteamCharger) world.getTileEntity(x, y, z);

        if (tileentitysteamcharger != null) {
            for (int i1 = 0; i1 < tileentitysteamcharger.getSizeInventory(); ++i1) {
                ItemStack itemstack = tileentitysteamcharger.getStackInSlot(i1);

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
                        EntityItem entityitem =
                            new EntityItem(world, (double) ((float) x + f),
                              (double) ((float) y + f1), (double) ((float) z + f2),
                              new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

                        if (itemstack.hasTagCompound()) {
                            entityitem.getEntityItem().setTagCompound(
                              (NBTTagCompound) itemstack.getTagCompound().copy());
                        }

                        float f3 = 0.05F;
                        entityitem.motionX = (double) ((float) this.rand.nextGaussian() * f3);
                        entityitem.motionY =
                          (double) ((float) this.rand.nextGaussian() * f3 + 0.2F);
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