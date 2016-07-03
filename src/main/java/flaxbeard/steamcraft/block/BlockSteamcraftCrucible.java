package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.Tuple3;
import flaxbeard.steamcraft.integration.CrossMod;
import flaxbeard.steamcraft.tile.TileEntityCrucible;
import flaxbeard.steamcraft.tile.TileEntitySteamHeater;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.MutablePair;

public class BlockSteamcraftCrucible extends BlockContainer implements IWrenchable {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    private static final float PX = (1.0F / 16.0F);
    public IIcon innerIcon;
    public IIcon topIcon;
    public IIcon bottomIcon;
    public TextureAtlasSprite liquidIcon;
    public IIcon blank;

    public BlockSteamcraftCrucible() {
        super(Material.rock);
        setHardness(3.5F);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
        return AxisAlignedBB.getBoundingBox(i + PX, j + 0.0F + PX, k + PX, i + 1.0F - PX, j + 1.0F - PX, k + 1.0F - PX);
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int meta) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileEntityCrucible) {
            TileEntityCrucible crucible = (TileEntityCrucible) te;
            return crucible.getComparatorOutput();
        }
        return 0;
    }

    public boolean isCrucibleHeated(World world, int x, int y, int z) {
        Block blockUnderCrucible = world.getBlock(x, y - 1, z);
        TileEntity tileUnderCrucible = world.getTileEntity(x, y - 1, z);
        int steam = Config.heaterConsumption;

        if (this == SteamcraftBlocks.hellCrucible ||
          blockUnderCrucible.getMaterial() == Material.fire ||
          blockUnderCrucible.getMaterial() == Material.lava || Config.enableThaumcraftIntegration &&
          Config.enableNitorPoweredCrucible && CrossMod.THAUMCRAFT &&
          ThaumcraftIntegration.isNitorUnderBlock(world, x, y, z)) {
            return true;
        }

        if (tileUnderCrucible instanceof TileEntitySteamHeater) {
            TileEntitySteamHeater steamHeater = (TileEntitySteamHeater) tileUnderCrucible;

            if (steamHeater.myDir() == ForgeDirection.UP && steamHeater.getSteam() >= steam) {
                steamHeater.decrSteam(steam);
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if (entity instanceof EntityItem) {
            EntityItem item = (EntityItem) entity;
            if (isCrucibleHeated(world, x, y, z)) {
                MutablePair output;
                if (SteamcraftRegistry.liquidRecipes.containsKey(MutablePair.of(item.getEntityItem().getItem(), item.getEntityItem().getItemDamage()))) {
                    output = SteamcraftRegistry.liquidRecipes.get(MutablePair.of(item.getEntityItem().getItem(), item.getEntityItem().getItemDamage()));
                } else if (SteamcraftRegistry.liquidRecipes.containsKey(MutablePair.of(item.getEntityItem().getItem(), -1))) {
                    output = SteamcraftRegistry.liquidRecipes.get(MutablePair.of(item.getEntityItem().getItem(), -1));

                } else {
                    return;
                }
                TileEntityCrucible crucible = (TileEntityCrucible) world.getTileEntity(x, y, z);
                int amount = (Integer) output.right;
                if (crucible != null){
                    if (item.delayBeforeCanPickup > 2){
                        item.delayBeforeCanPickup = 2;
                    } else if (item.delayBeforeCanPickup <= 1) {
                        ItemStack stack = item.getEntityItem();
                        ItemStack out = crucible.fillWith(stack, amount, output);
                        if (crucible.getFill() + amount <= 90) {
                            item.delayBeforeCanPickup = 2;
                        }

                        if (out.stackSize <= 0) {
                            entity.setDead();
                        } else {
                            item.setEntityItemStack(out);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
        int l = MathHelper.floor_double((double) (p_149689_5_.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
        p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, l, 2);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1iBlockAccess, int par2, int par3, int par4) {
        setBlockBounds(PX, 0.0F + PX, PX, 1.0F - PX, 1.0F - PX, 1.0F - PX);
        super.setBlockBoundsBasedOnState(par1iBlockAccess, par2, par3, par4);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        return blank;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister ir) {
        this.innerIcon = ir.registerIcon(this.getTextureName() + "_" + "inner");
        this.topIcon = ir.registerIcon(this.getTextureName() + "_top");
        this.bottomIcon = ir.registerIcon(this.getTextureName() + "_" + "bottom");
        this.blockIcon = ir.registerIcon(this.getTextureName() + "_side");
        this.liquidIcon = ir.registerIcon(this.getTextureName() + "_liquid");
        this.blank = ir.registerIcon("steamcraft:blankTexture");
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntityCrucible();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        if (!player.isSneaking() && player.getHeldItem() == null) {
            TileEntityCrucible tile = (TileEntityCrucible) world.getTileEntity(x, y, z);
            if (!tile.isTipping()) {
                tile.setTipping();
                tile.needsUpdate = true;
            }
        } else if (player.getHeldItem() != null) {
            TileEntityCrucible tile = (TileEntityCrucible) world.getTileEntity(x, y, z);
            for (CrucibleLiquid liquid : tile.contents) {
                Tuple3 tuple = new Tuple3(player.getHeldItem().getItem(), player.getHeldItem().getItemDamage(), liquid);
                if (SteamcraftRegistry.dunkRecipes.containsKey(tuple)) {
                    int needed = SteamcraftRegistry.dunkRecipes.get(tuple).left;
                    ItemStack result = SteamcraftRegistry.dunkRecipes.get(tuple).right.copy();
                    if (tile.number.get(liquid) >= needed) {
                        player.inventory.decrStackSize(player.inventory.currentItem, 1);
                        int currNum = tile.number.get(liquid);
                        currNum -= needed;
                        if (currNum == 0) {
                            tile.contents.remove(liquid);
                        }
                        tile.number.remove(liquid);
                        tile.number.put(liquid, currNum);
                        if (!player.inventory.addItemStackToInventory(result)) {
                            if (!player.worldObj.isRemote) {
                                player.entityDropItem(result, 0.0F);
                            }
                        }
                        tile.needsUpdate = true;

                        break;
                    }
                } else {
                    Tuple3 tuple1 = new Tuple3(player.getHeldItem(), -1, liquid);
                    if (SteamcraftRegistry.dunkRecipes.containsKey(tuple1)) {
                        int needed = SteamcraftRegistry.dunkRecipes.get(tuple1).left;
                        ItemStack result = SteamcraftRegistry.dunkRecipes.get(tuple1).right.copy();
                        if (tile.number.get(liquid) >= needed) {
                            player.inventory.decrStackSize(player.inventory.currentItem, 1);
                            int currNum = tile.number.get(liquid);
                            currNum -= needed;
                            if (currNum == 0) {
                                tile.contents.remove(liquid);
                            }
                            tile.number.remove(liquid);
                            tile.number.put(liquid, currNum);
                            if (!player.inventory.addItemStackToInventory(result)) {
                                if (!player.worldObj.isRemote) {
                                    player.entityDropItem(result, 0.0F);
                                }
                            }
                            tile.needsUpdate = true;

                            break;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world,
                            int x, int y, int z, int side, float xO, float yO, float zO) {
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
