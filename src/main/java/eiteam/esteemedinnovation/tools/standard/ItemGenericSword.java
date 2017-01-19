package eiteam.esteemedinnovation.tools.standard;

import eiteam.esteemedinnovation.api.util.ItemStackUtility;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ItemGenericSword extends ItemSword {
    private int harvestLevel;
    private Object repairMaterial;

    public ItemGenericSword(ToolMaterial toolMaterial, Object repairMat) {
        super(toolMaterial);
        harvestLevel = toolMaterial.getHarvestLevel();
        repairMaterial = repairMat;
        this.setCreativeTab(EsteemedInnovation.tabTools);
        this.setHarvestLevel(this.getClass().getName(), toolMaterial.getHarvestLevel());
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
