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
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockCarvingTable extends Block {

    @SideOnly(Side.CLIENT)
    private IIcon field_150035_a;

    public BlockCarvingTable() {
        super(Material.wood);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return side == 1 ? this.field_150035_a : (side == 0 ? Blocks.planks.getBlockTextureFromSide(side) : this.blockIcon);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister ir) {
        this.blockIcon = ir.registerIcon(this.getTextureName() + "_side");
        this.field_150035_a = ir.registerIcon(this.getTextureName() + "_top");
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int int0, float float0, float float1, float float2) {
        if (player.getHeldItem() != null) {
            if (player.getHeldItem().getItem() instanceof ICrucibleMold || player.getHeldItem().getItem() == SteamcraftItems.blankMold) {
                int index = 0;
                int i = 0;
                for (ICrucibleMold mold : SteamcraftRegistry.molds) {
                    if (mold == player.getHeldItem().getItem()) {
                        index = i;
                    }
                    i++;
                }
                if (index + 1 == SteamcraftRegistry.molds.size()) {
                    index = -1;
                }
                ICrucibleMold next = SteamcraftRegistry.molds.get(index + 1);
                player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack((Item) next));
                return true;
            }
        }
        return false;
    }
}