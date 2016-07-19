package flaxbeard.steamcraft.init.misc.integration.tinkers;

import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import flaxbeard.steamcraft.init.misc.MiscellaneousCategory;

// TODO: Wait for Lance to figure out the new Tinkers' Construct API.
public class TinkersIntegration extends MiscellaneousCategory {
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        /*
        ModifyBuilder.registerModifier(new ModiferSteam(new ItemStack[] {
          new ItemStack(SteamNetworkBlocks.Blocks.TANK.getBlock()) }));

        for (ToolCore tool : TConstructRegistry.getToolMapping()) {
            TConstructClientRegistry.addEffectRenderMapping(tool, 18, "tinker", "steam", true);
        }
        */
    }
}
