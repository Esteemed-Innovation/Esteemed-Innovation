package flaxbeard.steamcraft.item.tool;

import com.google.common.collect.ImmutableSet;
import flaxbeard.steamcraft.api.UtilMisc;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;

import java.util.Set;

public class ItemSteamcraftAxe extends ItemAxe {
    private int harvestLevel;
    private Object repairMaterial;

    public ItemSteamcraftAxe(ToolMaterial toolMaterial, Object rM) {
        super(toolMaterial);
        harvestLevel = toolMaterial.getHarvestLevel();
        repairMaterial = rM;
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return ImmutableSet.of("axe");
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
