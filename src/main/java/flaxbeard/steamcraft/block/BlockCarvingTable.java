package flaxbeard.steamcraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.ICrucibleMold;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockCarvingTable extends Block {
    @SideOnly(Side.CLIENT)
    private IIcon topIcon;

    public BlockCarvingTable() {
        super(Material.wood);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (side == 1) {
            return this.topIcon;
        } else {
            if (side == 0) {
                return Blocks.planks.getBlockTextureFromSide(side);
            } else {
                return this.blockIcon;
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister ir) {
        this.blockIcon = ir.registerIcon(this.getTextureName() + "_side");
        this.topIcon = ir.registerIcon(this.getTextureName() + "_top");
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int int0, float float0, float float1, float float2) {
        ItemStack held = player.getHeldItem();
        if (held != null) {
            Item heldItem = held.getItem();
            if (SteamcraftRegistry.molds.contains(heldItem)) {
                int index = 0;
                int i = 0;
                for (Item item : SteamcraftRegistry.molds) {
                    if (heldItem == item) {
                        index = i;
                    }
                    i++;
                }
                if (index + 1 == SteamcraftRegistry.molds.size()) {
                    index = -1;
                }
                Item next = SteamcraftRegistry.molds.get(index + 1);
                InventoryPlayer inventory = player.inventory;
                ItemStack stack = new ItemStack(next);
                inventory.setInventorySlotContents(inventory.currentItem, stack);
                return true;
            }
        }
        return false;
    }
}