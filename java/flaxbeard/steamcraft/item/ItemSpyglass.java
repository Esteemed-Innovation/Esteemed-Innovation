package flaxbeard.steamcraft.item;

import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.IEnhancement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSpyglass extends Item implements IEnhancement {
    @Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
		return par1ItemStack;
    }

    @Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 72000;
    }

    @Override
	public boolean isFull3D()
    {
    	return true;
    }
    
    @Override
    public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4)
    {
    }
    
    @Override
	public boolean canApplyTo(ItemStack stack) {
		return stack.getItem() == SteamcraftItems.musket;
	}

	@Override
	public int cost(ItemStack stack) {
		return 8;
	}

	@Override
	public String getID() {
		return "scope";
	}

	@Override
	public String getIcon(Item item) {
		return "steamcraft:weaponMusketSharpshooter";
	}

	@Override
	public String getName(Item item) {
		return "item.steamcraft:musketMarksman";
	}
	
	@Override
	public String getEnhancementName(Item item) {
		return "enhancement.steamcraft:musketMarksman";
	}

	@Override
	public float getAccuracyChange() {
		return -0.1F;
	}
}
