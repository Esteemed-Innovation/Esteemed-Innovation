package flaxbeard.steamcraft.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.common.tile.TileEntityCustomCraftingTable;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCustomCraftingTable extends BlockWorkbench implements ITileEntityProvider {
    @SideOnly(Side.CLIENT)
    private IIcon field_150035_a;
    @SideOnly(Side.CLIENT)
    private IIcon field_150034_b;

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityCustomCraftingTable();
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public int getRenderType() {
        return Steamcraft.customCraftingTableRenderID;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
//    	ForgeDirection dir =ForgeDirection.getOrientation(side).getOpposite();
//    	int x2 = x + dir.offsetX;
//    	int y2 = y + dir.offsetY;
//    	int z2 = z + dir.offsetZ;
//
//    	if (world.getTileEntity(x2, y2, z2) instanceof TileEntityBoiler) {
//	    	TileEntityBoiler boiler = (TileEntityBoiler) world.getTileEntity(x2, y2, z2);
//	    	if (boiler != null && boiler.disguiseBlock != null && boiler.disguiseBlock != Blocks.air && !BlockSteamPipeRenderer.updateWrenchStatus()) {
//	    		
//	    		return true;
//	    	}
//    	}
        return super.shouldSideBeRendered(world, x, y, z, side);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        this.blockIcon = p_149651_1_.registerIcon("steamcraft:customTable1");
        this.field_150035_a = p_149651_1_.registerIcon("steamcraft:customTable3");
        this.field_150034_b = p_149651_1_.registerIcon("steamcraft:customTable2");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
        return p_149691_1_ == 1 ? this.field_150035_a : (p_149691_1_ == 0 ? Blocks.planks.getBlockTextureFromSide(p_149691_1_) : (p_149691_1_ != 2 && p_149691_1_ != 4 ? this.field_150034_b : this.blockIcon));
    }

    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 0;
    }

}
