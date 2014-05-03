package flaxbeard.steamcraft.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class BlockManyMetadataItem extends ItemBlock {

	public BlockManyMetadataItem(Block p_i45328_1_) {
		super(p_i45328_1_);
		this.setHasSubtypes(true);
	}
	
	public int getMetadata(int par1)
	{
		return par1;
	}
	  
	public String getUnlocalizedName(ItemStack par1ItemStack)
	{
		return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
	}
}
