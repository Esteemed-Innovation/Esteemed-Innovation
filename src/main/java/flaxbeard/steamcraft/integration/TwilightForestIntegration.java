package flaxbeard.steamcraft.integration;

import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.SteamcraftRecipes;
import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.exosuit.ExosuitPlate;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TwilightForestIntegration {

    public static Item fieryIngot;
    public static Item alphaFur;

    public static void grabItems() {
        try {
            Class clazz = Class.forName("twilightforest.item.TFItems");
            fieryIngot = (Item) clazz.getDeclaredField("fieryIngot").get(clazz);
            alphaFur = (Item) clazz.getDeclaredField("alphaFur").get(clazz);
        } catch (Exception ex) {
            // Should probably log something here
        }
    }

    public static void addTwilightForestLiquid() {
        CrucibleLiquid liquidFiery = new CrucibleLiquid("fiery", new ItemStack(fieryIngot), new ItemStack(SteamcraftItems.steamcraftPlate, 1, 8), null, null, 91, 69, 69);
        SteamcraftRegistry.liquids.add(liquidFiery);

        SteamcraftRegistry.registerSmeltThingOredict("fieryIngot", liquidFiery, 9);
        SteamcraftRegistry.registerSmeltThingOredict("nuggetFiery", liquidFiery, 1);
        SteamcraftRegistry.registerSmeltThingOredict("fieryNugget", liquidFiery, 1);
        SteamcraftRegistry.registerSmeltThingOredict("plateSteamcraftFiery", liquidFiery, 6);
        if (Config.enableFieryPlate) {
            SteamcraftRegistry.addExosuitPlate(new ExosuitPlate("Fiery", new ItemStack(SteamcraftItems.exosuitPlate, 1, 8), "Fiery", "Fiery", "steamcraft.plate.fiery"));
            SteamcraftRecipes.addExosuitPlateRecipes("exoFiery", "plateSteamcraftFiery", new ItemStack(SteamcraftItems.exosuitPlate, 1, 8), liquidFiery);
        }
        if (Config.enableYetiPlate) {
            SteamcraftRegistry.addExosuitPlate(new ExosuitPlate("Yeti", new ItemStack(SteamcraftItems.exosuitPlate, 1, 9), "Yeti", "Yeti", "steamcraft.plate.yeti"));
            SteamcraftRecipes.addExosuitPlateRecipes("exoYeti", new ItemStack(alphaFur), new ItemStack(SteamcraftItems.exosuitPlate, 1, 9));
        }
    }
}
