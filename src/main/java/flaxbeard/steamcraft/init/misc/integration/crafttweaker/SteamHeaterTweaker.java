package flaxbeard.steamcraft.init.misc.integration.crafttweaker;

import flaxbeard.steamcraft.api.SteamingRegistry;
import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.fsp.SteamHeater")
public class SteamHeaterTweaker {
    @ZenMethod
    public static void addSteamingReplacementRecipe(IItemStack inputI, IItemStack outputI) {
        ItemStack input = MineTweakerMC.getItemStack(inputI);
        ItemStack output = MineTweakerMC.getItemStack(outputI);
        MineTweakerAPI.apply(new Add(input, output));
    }

    private static class Add implements IUndoableAction {
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
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            SteamingRegistry.removeSteamingRecipe(input);
        }

        @Override
        public String describe() {
            return "Adding steaming recipe for " + input.getUnlocalizedName() + " -> " + output.getUnlocalizedName();
        }

        @Override
        public String describeUndo() {
            return "Removing steaming recipe for " + input.getUnlocalizedName() + " -> " + output.getUnlocalizedName();
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }

    @ZenMethod
    public static void removeSteamingReplacementRecipe(IItemStack original) {
        ItemStack stack = MineTweakerMC.getItemStack(original);
        MineTweakerAPI.apply(new Remove(stack));
    }

    private static class Remove implements IUndoableAction {
        private final ItemStack input;

        public Remove(ItemStack input) {
            this.input = input;
        }

        @Override
        public void apply() {
            SteamingRegistry.removeSteamingRecipe(input);
        }

        @Override
        public boolean canUndo() {
            return false;
        }

        @Override
        public void undo() {}

        @Override
        public String describe() {
            return "Removing steaming recipe for " + input.getUnlocalizedName();
        }

        @Override
        public String describeUndo() {
            return null;
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }
}
