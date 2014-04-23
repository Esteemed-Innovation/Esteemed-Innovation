package flaxbeard.steamcraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import flaxbeard.steamcraft.Steamcraft;

public class ItemSteamcraftBook extends Item {
	
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World world, EntityPlayer player)
	{
    		player.openGui(Steamcraft.instance, 1, world, 0,0,0);

        
    	return par1ItemStack;
    }

}
