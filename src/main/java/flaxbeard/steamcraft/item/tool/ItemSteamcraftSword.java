package flaxbeard.steamcraft.item.tool;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.util.UtilMisc;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ItemSteamcraftSword extends ItemSword {
    private int harvestLevel;
    private Object repairMaterial;

    public ItemSteamcraftSword(ToolMaterial toolMaterial, Object repairMat) {
        super(toolMaterial);
        harvestLevel = toolMaterial.getHarvestLevel();
        repairMaterial = repairMat;
        this.setCreativeTab(Steamcraft.tabTools);
        this.setHarvestLevel(this.getClass().getName(), toolMaterial.getHarvestLevel());
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
