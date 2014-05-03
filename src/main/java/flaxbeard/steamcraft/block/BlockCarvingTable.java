package flaxbeard.steamcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.ICrucibleMold;
import flaxbeard.steamcraft.api.SteamcraftRegistry;

public class BlockCarvingTable extends Block
{
    @SideOnly(Side.CLIENT)
    private IIcon field_150035_a;

    public BlockCarvingTable()
    {
        super(Material.wood);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return p_149691_1_ == 1 ? this.field_150035_a : (p_149691_1_ == 0 ? Blocks.planks.getBlockTextureFromSide(p_149691_1_) : this.blockIcon);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blockIcon = p_149651_1_.registerIcon(this.getTextureName() + "_side");
        this.field_150035_a = p_149651_1_.registerIcon(this.getTextureName() + "_top");
    }

    public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
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
	    		if (index+1 == SteamcraftRegistry.molds.size()) {
	    			index = -1;
	    		}
	    		ICrucibleMold next = SteamcraftRegistry.molds.get(index+1);
	    		player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack((Item) next));
	    		return true;
	    	}
    	}
    	return false;
    }
}