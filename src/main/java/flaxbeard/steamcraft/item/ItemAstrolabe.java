package flaxbeard.steamcraft.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class ItemAstrolabe extends Item {
	
	@Override
	public void addInformation(ItemStack item, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
	    if (item.hasTagCompound() && item.getTagCompound().hasKey("targetX")) {
	    	String dimension = "dimension " + item.stackTagCompound.getInteger("dim");

	    	String dimension2 = 	    	DimensionManager.getWorld(item.stackTagCompound.getInteger("dim")).getWorldInfo().getWorldName();
	    	if (item.stackTagCompound.getInteger("dim") == 0) {
	    		dimension = "the Overworld";
	    	}
	    	if (item.stackTagCompound.getInteger("dim") == -1) {
	    		dimension = "the Nether";
	    	}
	    	if (item.stackTagCompound.getInteger("dim") == 1) {
	    		dimension = "the End";
	    	}
	    	par3List.add("Target: " + item.stackTagCompound.getInteger("targetX") + ", " + item.stackTagCompound.getInteger("targetZ") + " in " + dimension);
	    }
    }
	
	public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10)
	{
		if (player.isSneaking()) {
			if (!item.hasTagCompound()) {
				item.setTagCompound(new NBTTagCompound());
			}
			item.getTagCompound().setInteger("targetX", x);
			item.getTagCompound().setInteger("targetZ", z);
			item.getTagCompound().setInteger("dim", world.provider.dimensionId);
			return true;
		}
		return false;
	}
}
