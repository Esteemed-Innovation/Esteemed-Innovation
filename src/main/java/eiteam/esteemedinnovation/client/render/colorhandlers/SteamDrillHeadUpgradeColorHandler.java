package eiteam.esteemedinnovation.client.render.colorhandlers;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import eiteam.esteemedinnovation.item.tool.steam.ItemDrillHeadUpgrade;
import eiteam.esteemedinnovation.misc.DrillHeadMaterial;

public class SteamDrillHeadUpgradeColorHandler implements IItemColor {
    @Override
    public int getColorFromItemstack(ItemStack stack, int renderPass) {
        return DrillHeadMaterial.materials.get(ItemDrillHeadUpgrade.getMyMaterial(stack)).getColorInt();
    }
}
