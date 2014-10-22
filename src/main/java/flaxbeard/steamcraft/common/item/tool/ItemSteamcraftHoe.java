package flaxbeard.steamcraft.common.item.tool;

import flaxbeard.steamcraft.api.UtilMisc;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;

public class ItemSteamcraftHoe extends ItemHoe {
    private int harvestLevel;
    private Object repairMaterial;

    public ItemSteamcraftHoe(ToolMaterial p_i45347_1_, Object rM) {
        super(p_i45347_1_);
        harvestLevel = p_i45347_1_.getHarvestLevel();
        repairMaterial = rM;
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass) {
        return harvestLevel;
    }

    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        if (repairMaterial instanceof ItemStack) {
            return par2ItemStack.isItemEqual((ItemStack) repairMaterial) ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
        }
        if (repairMaterial instanceof String) {
            return UtilMisc.doesMatch(par2ItemStack, (String) repairMaterial) ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
        }
        return super.getIsRepairable(par1ItemStack, par2ItemStack);
    }

}
