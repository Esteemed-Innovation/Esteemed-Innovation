package flaxbeard.steamcraft.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.ISteamChargable;
import flaxbeard.steamcraft.tile.TileEntityItemMortar;
import flaxbeard.steamcraft.tile.TileEntityItemMortar;

public class BlockItemMortar extends BlockContainer
{

    public BlockItemMortar()
    {
        super(Material.iron);
    }
    
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityItemMortar();
	}
	
    public boolean isOpaqueCube()
    {
        return false;
    }
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
	{
		TileEntityItemMortar tile = (TileEntityItemMortar) world.getTileEntity(x,y,z);
	
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
}