package eiteam.esteemedinnovation.heater;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.api.SteamingRegistry;

import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods." + EsteemedInnovation.MOD_ID + ".SteamHeater")
public class SteamHeaterTweaker {
    @ZenMethod
    public static void addSteamingReplacementRecipe(IItemStack inputI, IItemStack outputI) {
        ItemStack input = CraftTweakerMC.getItemStack(inputI);
        ItemStack output = CraftTweakerMC.getItemStack(outputI);
        CraftTweakerAPI.apply(new Add(input, output));
    }

    private static class Add implements IAction {
        private final ItemStack input;
        private final ItemStack output;

        public Add(ItemStack input, ItemStack output) {
            this.input = input;
            this.output = output;
        }

        @Override
        public void apply() {
            SteamingRegistry.addSteamingRecipe(input, output);
        }

        @Override
        public String describe() {
            return "Adding steaming recipe for " + input.getTranslationKey() + " -> " + output.getTranslationKey();
        }

    }

    @ZenMethod
    public static void removeSteamingReplacementRecipe(IItemStack original) {
        ItemStack stack = CraftTweakerMC.getItemStack(original);
        CraftTweakerAPI.apply(new Remove(stack));
    }

    private static class Remove implements IAction {
        private final ItemStack input;

        public Remove(ItemStack input) {
            this.input = input;
        }

        @Override
        public void apply() {
            SteamingRegistry.removeSteamingRecipe(input);
        }

        @Override
        public String describe() {
            return "Removing steaming recipe for " + input.getTranslationKey();
        }

    }
}
