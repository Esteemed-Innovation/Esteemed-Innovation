package flaxbeard.steamcraft.block;

import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.WEST;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.tile.TileEntitySteamGauge;
import flaxbeard.steamcraft.tile.TileEntitySteamPipe;

public class BlockSteamGauge extends BlockContainer {
    public IIcon front;
    public IIcon back;
    public IIcon top;
    
	public BlockSteamGauge() {
		super(Material.iron);
	}
	
	public boolean hasComparatorInputOverride(){
		return true;
	}
	
	public int getComparatorInputOverride(World world, int x, int y, int z, int meta){
		TileEntity te = world.getTileEntity(x, y, z);
		if (te != null && te instanceof TileEntitySteamGauge){
			return ((TileEntitySteamGauge)te).getComparatorOutput();
		}
		return 0;
	}
	
	@Override
	public String getItemIconName() {
		return "steamcraft:gauge";
	}
    
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int xl, int yl, int zl)
    {
        int meta = world.getBlockMetadata(xl, yl, zl);
        float px = 1.0F/16.0F;
		float x = 4*px;
		float y = 4*px;
		float z = 0.0F;
		float x2 = 12*px;
		float y2 = 12*px;
		float z2 = px;
		ForgeDirection dir = ForgeDirection.getOrientation(meta).getOpposite();
		if (world.getTileEntity(xl+dir.offsetX, yl+dir.offsetY, zl+dir.offsetZ) != null && world.getTileEntity(xl+dir.offsetX, yl+dir.offsetY, zl+dir.offsetZ) instanceof TileEntitySteamPipe) {
			z = -5*px;
			z2 = -4*px + 0.0005F;
		}
        switch (meta) {
			case 5:
				this.setBlockBounds(z, y, x, z2, y2, x2);
				break;
			case 2:
				this.setBlockBounds(1-x2, y, 1-z2, 1-x, y2, 1-z);
				break;
			case 4:
				this.setBlockBounds(1-z2, y, 1-x2, 1-z, y2, 1-x);
				break;
			case 3:
				this.setBlockBounds(x, y, z, x2, y2, z2);
				break;
		}
	}
    
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side)
    {
        ForgeDirection dir = ForgeDirection.getOrientation(side);
        return (dir == NORTH && world.getTileEntity(x, y, z + 1) != null && world.getTileEntity(x, y, z + 1) instanceof ISteamTransporter && ((ISteamTransporter)world.getTileEntity(x, y, z + 1)).acceptsGauge(dir.getOpposite())) ||
               (dir == SOUTH && world.getTileEntity(x, y, z - 1) != null && world.getTileEntity(x, y, z - 1) instanceof ISteamTransporter && ((ISteamTransporter)world.getTileEntity(x, y, z - 1)).acceptsGauge(dir.getOpposite())) ||
               (dir == WEST  && world.getTileEntity(x + 1, y, z) != null && world.getTileEntity(x + 1, y, z) instanceof ISteamTransporter && ((ISteamTransporter)world.getTileEntity(x + 1, y, z)).acceptsGauge(dir.getOpposite())) ||
               (dir == EAST  && world.getTileEntity(x - 1, y, z) != null && world.getTileEntity(x - 1, y, z) instanceof ISteamTransporter && ((ISteamTransporter)world.getTileEntity(x - 1, y, z)).acceptsGauge(dir.getOpposite()));
    }
    
    public void onNeighborBlockChange(World world, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_)
    {
        if (p_149695_5_ != this)
        {
            int l = world.getBlockMetadata(p_149695_2_, p_149695_3_, p_149695_4_);
            boolean flag = false;
            if (!this.canPlaceBlockOnSide(world, p_149695_2_, p_149695_3_, p_149695_4_, l)) {
            	flag = true;
            }

            if (flag)
            {
                this.dropBlockAsItem(world, p_149695_2_, p_149695_3_, p_149695_4_, l, 0);
                world.setBlockToAir(p_149695_2_, p_149695_3_, p_149695_4_);
            }
        }
    }
    
    public int onBlockPlaced(World world, int x, int y, int z, int side, float p_149660_6_, float p_149660_7_, float p_149660_8_, int p_149660_9_)
    {
    	return side;
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        return side == meta || side == ForgeDirection.OPPOSITES[meta] ? (side == meta ? back : front) : blockIcon;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blockIcon = p_149651_1_.registerIcon("steamcraft:gaugeTop");
        this.back = p_149651_1_.registerIcon("steamcraft:gaugeFront");
        this.front = p_149651_1_.registerIcon("steamcraft:gaugeBack");
        this.top = blockIcon;
    }
	

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntitySteamGauge();
	}
	
    public boolean isOpaqueCube()
    {
        return false;
    }
    
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        return null;
    }

	
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    public int getRenderType()
    {
        return Steamcraft.gaugeRenderID;
    }
    
}
