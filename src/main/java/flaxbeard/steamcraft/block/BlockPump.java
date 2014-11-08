package flaxbeard.steamcraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.tile.TileEntityPump;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockPump extends BlockSteamTransporter implements IWrenchable {

    public BlockPump() {
        super(Material.iron);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase elb, ItemStack stack) {
        int l = MathHelper.floor_double((double) (elb.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
        world.setBlockMetadataWithNotify(x, y, z, l, 2);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

//    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
//		return null;
//    }
//    
//    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int xp, int yp, int zp)
//    {
//    	int meta = blockAccess.getBlockMetadata(xp, yp, zp);
//    	float px = 1.0F/16.0F;
//		float x = 0;
//		float y = 0;
//		float z = 5*px;
//		float x2 = 15*px;
//		float y2 = 12*px;
//		float z2 = 11*px;
//		switch (meta) {
//		case 0:
//			this.setBlockBounds(z, y, x, z2, y2, x2);
//			break;
//		case 1:
//			this.setBlockBounds(1-x2, y, 1-z2, 1-x, y2, 1-z);
//			break;
//		case 2:	
//			this.setBlockBounds(1-z2, y, 1-x2, 1-z, y2, 1-x);
//			break;
//		case 3:
//			this.setBlockBounds(x, y, z, x2, y2, z2);
//			break;
//		
//		}
//    }
//    

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister ir) {
        this.blockIcon = ir.registerIcon("steamcraft:blankTexture");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta){
        return new TileEntityPump();
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