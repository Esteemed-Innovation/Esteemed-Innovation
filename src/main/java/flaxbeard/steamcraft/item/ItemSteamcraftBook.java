package flaxbeard.steamcraft.item;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.gui.GuiSteamcraftBook;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemSteamcraftBook extends Item {

    public ItemSteamcraftBook() {
        this.setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        player.openGui(Steamcraft.instance, 1, world, 0, 0, 0);
        return itemStack;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float fx, float fy, float fz) {
        if(player.isSneaking() && !world.isRemote) {
            Block block = world.getBlock(x, y, z);
            ItemStack stack = block.getPickBlock(new MovingObjectPosition(x, y, z, side, Vec3.createVectorHelper(fx, fy, fz)), world, x, y, z, player);

            if (stack != null) {
                for (ItemStack stack2 : SteamcraftRegistry.bookRecipes.keySet()) {
                    if (stack2.getItem() == stack.getItem() && stack2.getItemDamage() == stack.getItemDamage()) {
                        player.openGui(Steamcraft.instance, 1, player.worldObj, 0, 0, 0);
                        GuiSteamcraftBook.viewing = SteamcraftRegistry.bookRecipes.get(stack2).left;
                        GuiSteamcraftBook.currPage = MathHelper.floor_float((float) SteamcraftRegistry.bookRecipes.get(stack2).right / 2.0F);
                        GuiSteamcraftBook.lastIndexPage = 1;
                        GuiSteamcraftBook.bookTotalPages = MathHelper.ceiling_float_int(SteamcraftRegistry.researchPages.get(GuiSteamcraftBook.viewing).length / 2F);
                        ((GuiSteamcraftBook) Minecraft.getMinecraft().currentScreen).updateButtons();
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
