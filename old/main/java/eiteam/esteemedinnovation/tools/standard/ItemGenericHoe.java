package eiteam.esteemedinnovation.tools.standard;

import eiteam.esteemedinnovation.api.util.ItemStackUtility;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;

public class ItemGenericHoe extends ItemHoe {
    private int harvestLevel;
    private Object repairMaterial;

    public ItemGenericHoe(ToolMaterial toolMaterial, Object repairMat) {
        super(toolMaterial);
        harvestLevel = toolMaterial.getHarvestLevel();
        repairMaterial = repairMat;
        this.setCreativeTab(EsteemedInnovation.tabTools);
        this.setHarvestLevel(this.getClass().getName(), toolMaterial.getHarvestLevel());
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass, EntityPlayer player, IBlockState state) {
        return harvestLevel;
    }

    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        if (repairMaterial instanceof ItemStack) {
            return par2ItemStack.isItemEqual((ItemStack) repairMaterial) ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
        }
        if (repairMaterial instanceof String) {
            return ItemStackUtility.isItemOreDictedAs(par2ItemStack, (String) repairMaterial) ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
        }
        return super.getIsRepairable(par1ItemStack, par2ItemStack);
    }

}
