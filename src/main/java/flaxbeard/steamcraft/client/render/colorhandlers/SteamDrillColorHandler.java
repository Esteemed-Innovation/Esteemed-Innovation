package flaxbeard.steamcraft.client.render.colorhandlers;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import flaxbeard.steamcraft.api.tool.ISteamToolUpgrade;
import flaxbeard.steamcraft.api.tool.UtilSteamTool;
import flaxbeard.steamcraft.client.render.RenderUtility;
import flaxbeard.steamcraft.item.tool.steam.ItemDrillHeadUpgrade;
import flaxbeard.steamcraft.misc.DrillHeadMaterial;

import java.util.ArrayList;

public class SteamDrillColorHandler implements IItemColor {
    @Override
    public int getColorFromItemstack(ItemStack self, int renderPass) {
        ArrayList<ItemStack> upgrades = UtilSteamTool.getUpgradeStacks(self);
        for (ItemStack upgrade : upgrades) {
            if (upgrade.getItem() instanceof ItemDrillHeadUpgrade &&
              ((ISteamToolUpgrade) upgrade.getItem()).renderPriority() == renderPass) {
                String materialName = ItemDrillHeadUpgrade.getMyMaterial(upgrade);
                DrillHeadMaterial material = DrillHeadMaterial.materials.get(materialName);
                return material.getColorInt();
            }
        }
        return RenderUtility.WHITE;
    }
}
