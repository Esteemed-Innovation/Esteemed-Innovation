package flaxbeard.steamcraft.init.misc.integration.tinkers;

import flaxbeard.steamcraft.init.blocks.SteamNetworkBlocks;
import flaxbeard.steamcraft.init.misc.MiscellaneousCategory;

import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.tools.ToolCore;

public class TinkersIntegration extends MiscellaneousCategory {
    @Override
    public void postInit() {
        ModifyBuilder.registerModifier(new ModiferSteam(new ItemStack[] {
          new ItemStack(SteamNetworkBlocks.Blocks.TANK.getBlock()) }));

        for (ToolCore tool : TConstructRegistry.getToolMapping()) {
            TConstructClientRegistry.addEffectRenderMapping(tool, 18, "tinker", "steam", true);
        }
    }
}
