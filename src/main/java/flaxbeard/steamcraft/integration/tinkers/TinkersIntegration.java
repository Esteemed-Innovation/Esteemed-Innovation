package flaxbeard.steamcraft.integration.tinkers;

import net.minecraft.item.ItemStack;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.client.TConstructClientRegistry;
import tconstruct.library.crafting.ModifyBuilder;
import tconstruct.library.tools.ToolCore;
import flaxbeard.steamcraft.SteamcraftBlocks;

public class TinkersIntegration 
{
	public static void postInit()
	{
		ModifyBuilder.registerModifier(new modiferSteam(new ItemStack[] {new ItemStack(SteamcraftBlocks.tank)}));
		
		for (ToolCore tool : TConstructRegistry.getToolMapping())
        {
			TConstructClientRegistry.addEffectRenderMapping(tool, 18, "tinker", "steam", true);
        }
	}
}
