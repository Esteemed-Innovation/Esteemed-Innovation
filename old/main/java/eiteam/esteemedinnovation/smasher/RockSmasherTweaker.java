package eiteam.esteemedinnovation.smasher;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.api.SmasherRegistry;

import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods." + EsteemedInnovation.MOD_ID + ".RockSmasher")
public class RockSmasherTweaker {
    @ZenMethod
    public static void addSmashingRecipe(IItemStack in, IItemStack out) {
        ItemStack inStack = CraftTweakerMC.getItemStack(in);
        ItemStack outStack = CraftTweakerMC.getItemStack(out);
        CraftTweakerAPI.apply(new Add(inStack, outStack));
    }

    @ZenMethod
    public static void addSmashingOreRecipe(String dict, IItemStack out) {
        ItemStack outStack = CraftTweakerMC.getItemStack(out);
        CraftTweakerAPI.apply(new Add(dict, outStack));
    }

    private static class Add implements IAction {
        private final Object in;
        private final ItemStack out;

        public Add(Object in, ItemStack out) {
            this.in = in;
            this.out = out;
        }

        @Override
        public void apply() {
            if (in instanceof ItemStack) {
                SmasherRegistry.registerSmashable((ItemStack) in, out);
            } else if (in instanceof String) {
                SmasherRegistry.registerSmashable((String) in, out);
            }
        }

        @Override
        public String describe() {
            if (in instanceof ItemStack) {
                return "Adding smashing recipe for " + ((ItemStack) in).getTranslationKey();
            } else if (in instanceof String) {
                return "Adding smashing recipe for " + in;
            }
            return null;
        }

    }

    @ZenMethod
    public static void removeSmashingRecipe(IItemStack in, IItemStack out) {
        ItemStack inStack = CraftTweakerMC.getItemStack(in);
        ItemStack outStack = CraftTweakerMC.getItemStack(out);
        CraftTweakerAPI.apply(new Remove(inStack, outStack));
    }

    @ZenMethod
    public static void removeSmashingOreRecipe(String dict, IItemStack out) {
        ItemStack outStack = CraftTweakerMC.getItemStack(out);
        CraftTweakerAPI.apply(new Remove(dict, outStack));
    }

    private static class Remove implements IAction {
        private final Object in;
        private final ItemStack out;

        public Remove(Object in, ItemStack out) {
            this.in = in;
            this.out = out;
        }

        @Override
        public void apply() {
            if (in instanceof ItemStack) {
                SmasherRegistry.removeSmashable((ItemStack) in);
            } else if (in instanceof String) {
                SmasherRegistry.removeSmashable((String) in);
            }
        }

        @Override
        public String describe() {
            if (in instanceof ItemStack) {
                return "Removing smashing recipe for " + ((ItemStack) in).getTranslationKey();
            } else if (in instanceof String) {
                return "Removing smashing recipe for " + in;
            }
            return null;
        }
    }
}
