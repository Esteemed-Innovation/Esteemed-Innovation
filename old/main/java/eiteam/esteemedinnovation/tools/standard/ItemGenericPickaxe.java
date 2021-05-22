package eiteam.esteemedinnovation.tools.standard;

import com.google.common.collect.ImmutableSet;
import eiteam.esteemedinnovation.api.util.ItemStackUtility;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;

import java.util.Set;

public class ItemGenericPickaxe extends ItemPickaxe {
    private int harvestLevel;
    private Object repairMaterial;

    public ItemGenericPickaxe(ToolMaterial toolMaterial, Object repairMat) {
        super(toolMaterial);
        harvestLevel = toolMaterial.getHarvestLevel();
        repairMaterial = repairMat;
        this.setCreativeTab(EsteemedInnovation.tabTools);
        this.setHarvestLevel(this.getClass().getName(), toolMaterial.getHarvestLevel());
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return ImmutableSet.of("pickaxe");
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
