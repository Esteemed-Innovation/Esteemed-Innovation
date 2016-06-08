package flaxbeard.steamcraft.integration;

import flaxbeard.steamcraft.*;
import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.exosuit.ExosuitPlate;
import flaxbeard.steamcraft.init.blocks.SteamMachineryBlocks;
import flaxbeard.steamcraft.init.blocks.SteamNetworkBlocks;
import flaxbeard.steamcraft.init.items.MetalItems;
import flaxbeard.steamcraft.init.items.armor.ExosuitUpgradeItems;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class MiscIntegration {

	public static void postInit() {
        if (Config.enableLeadPlate && OreDictionary.getOres("ingotLead").size() > 0) {
            // TODO: Perhaps we should add our own lead ingot rather than relying on the OD.
            CrucibleLiquid liquidLead = new CrucibleLiquid("lead",
              OreDictionary.getOres("ingotLead").get(0), MetalItems.Items.LEAD_PLATE.createItemStack(),
              OreDictionary.getOres("nuggetLead").size() > 0 ?
              OreDictionary.getOres("nuggetLead").get(0) : null, null, 118, 128, 157);
            SteamcraftRegistry.registerLiquid(liquidLead);

            SteamcraftRegistry.registerMeltRecipeOreDict("ingotLead", liquidLead, 9);
            SteamcraftRegistry.registerMeltRecipeOreDict("nuggetLead", liquidLead, 1);
            SteamcraftRegistry.registerMeltRecipeOreDict("plateSteamcraftLead", liquidLead, 6);
            SteamcraftRegistry.addExosuitPlate(new ExosuitPlate("Lead",
              ExosuitUpgradeItems.PlateItems.LEAD.createItemStack(), "Lead", "Lead", "steamcraft.plate.lead"));
            SteamcraftRecipes.addExosuitPlateRecipes("exoLead", "plateSteamcraftLead",
              MetalItems.Items.LEAD_PLATE.createItemStack(), liquidLead);
        }
	}
}
