package eiteam.esteemedinnovation.tools.steam.upgrades.drillhead;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

public class SteamDrillHeadUpgradeColorHandler implements IItemColor {
    @Override
    public int colorMultiplier(ItemStack stack, int renderPass) {
        return DrillHeadMaterial.materials.get(ItemDrillHeadUpgrade.getMyMaterial(stack)).getColorInt();
    }
}
