package eiteam.esteemedinnovation.tools.steam;

import eiteam.esteemedinnovation.api.tool.UtilSteamTool;
import eiteam.esteemedinnovation.commons.util.RenderUtility;
import eiteam.esteemedinnovation.tools.steam.upgrades.drillhead.ItemDrillHeadUpgrade;
import eiteam.esteemedinnovation.tools.steam.upgrades.drillhead.DrillHeadMaterial;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class SteamDrillColorHandler implements IItemColor {
    @Override
    public int getColorFromItemstack(ItemStack self, int tintIndex) {
        ArrayList<ItemStack> upgrades = UtilSteamTool.getUpgradeStacks(self);
        for (ItemStack upgrade : upgrades) {
            if (upgrade.getItem() instanceof ItemDrillHeadUpgrade && tintIndex == 1) {
                String materialName = ItemDrillHeadUpgrade.getMyMaterial(upgrade);
                DrillHeadMaterial material = DrillHeadMaterial.materials.get(materialName);
                return material.getColorInt();
            }
        }
        return RenderUtility.WHITE;
    }
}
