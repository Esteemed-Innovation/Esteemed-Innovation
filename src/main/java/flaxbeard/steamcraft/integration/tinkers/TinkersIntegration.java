package flaxbeard.steamcraft.integration.tinkers;

import flaxbeard.steamcraft.SteamcraftBlocks;

import net.minecraft.item.ItemStack;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.client.TConstructClientRegistry;
import tconstruct.library.crafting.ModifyBuilder;
import tconstruct.library.tools.ToolCore;

public class TinkersIntegration {
    
	private TinkersIntegration() throws InstantiationException{
		throw new InstantiationException("This class is not meant to be instantiated");
	}
	
	public static void postInit() {
        ModifyBuilder.registerModifier(new ModiferSteam(new ItemStack[] { new ItemStack(
          SteamcraftBlocks.tank) }));

        for (ToolCore tool : TConstructRegistry.getToolMapping()) {
            TConstructClientRegistry.addEffectRenderMapping(tool, 18, "tinker", "steam", true);
        }
    }
}
