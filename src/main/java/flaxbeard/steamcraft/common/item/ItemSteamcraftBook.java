package flaxbeard.steamcraft.common.item;

import flaxbeard.steamcraft.Steamcraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSteamcraftBook extends Item {

    public ItemSteamcraftBook() {
        this.setMaxStackSize(1);
    }

    public ItemStack onItemRightClick(ItemStack par1ItemStack, World world, EntityPlayer player) {
        player.openGui(Steamcraft.instance, 1, world, 0, 0, 0);
        return par1ItemStack;
    }

}
