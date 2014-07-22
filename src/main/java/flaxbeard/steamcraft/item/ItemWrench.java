package flaxbeard.steamcraft.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import flaxbeard.steamcraft.api.IWrenchable;

public class ItemWrench extends Item {
	
    @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
        return true;
    }
    
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xO, float yO, float zO)
    {
		if (world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof IWrenchable) {
			return ((IWrenchable)world.getTileEntity(x, y, z)).onWrench(stack, player, world, x, y, z, side, xO, yO, zO);
		}
		else if (world.getBlock(x, y, z) != null && world.getBlock(x, y, z) instanceof IWrenchable) {
			return ((IWrenchable)world.getBlock(x, y, z)).onWrench(stack, player, world, x, y, z, side, xO, yO, zO);
		}
		return false;
    }
}
