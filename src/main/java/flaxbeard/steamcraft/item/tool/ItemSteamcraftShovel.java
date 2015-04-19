package flaxbeard.steamcraft.item.tool;

import com.google.common.collect.ImmutableSet;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.UtilMisc;

import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;

import java.util.Set;

public class ItemSteamcraftShovel extends ItemSpade {
    private int harvestLevel;
    private Object repairMaterial;

    public ItemSteamcraftShovel(ToolMaterial toolMaterial, Object repairMat) {
        super(toolMaterial);
        harvestLevel = toolMaterial.getHarvestLevel();
        repairMaterial = repairMat;
        this.setCreativeTab(Steamcraft.tabTools);
        this.setHarvestLevel(this.getClass().getName(), toolMaterial.getHarvestLevel());
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return ImmutableSet.of("shovel");
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
