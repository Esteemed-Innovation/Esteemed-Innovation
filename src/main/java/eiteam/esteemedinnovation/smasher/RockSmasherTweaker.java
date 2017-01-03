package eiteam.esteemedinnovation.smasher;

import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.api.SmasherRegistry;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods." + EsteemedInnovation.MOD_ID + ".RockSmasher")
public class RockSmasherTweaker {
    @ZenMethod
    public static void addSmashingRecipe(IItemStack in, IItemStack out) {
        ItemStack inStack = MineTweakerMC.getItemStack(in);
        ItemStack outStack = MineTweakerMC.getItemStack(out);
        MineTweakerAPI.apply(new Add(inStack, outStack));
    }

    @ZenMethod
    public static void addSmashingOreRecipe(String dict, IItemStack out) {
        ItemStack outStack = MineTweakerMC.getItemStack(out);
        MineTweakerAPI.apply(new Add(dict, outStack));
    }

    private static class Add implements IUndoableAction {
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
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            if (in instanceof ItemStack) {
                SmasherRegistry.removeSmashable((ItemStack) in);
            } else if (in instanceof String) {
                SmasherRegistry.removeSmashable((String) in);
            }
        }

        @Override
        public String describe() {
            if (in instanceof ItemStack) {
                return "Adding smashing recipe for " + ((ItemStack) in).getUnlocalizedName();
            } else if (in instanceof String) {
                return "Adding smashing recipe for " + in;
            }
            return null;
        }

        @Override
        public String describeUndo() {
            if (in instanceof ItemStack) {
                return "Removing smashing recipe for " + ((ItemStack) in).getUnlocalizedName();
            } else if (in instanceof String) {
                return "Removing smashing recipe for " + in;
            }
            return null;
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }

    @ZenMethod
    public static void removeSmashingRecipe(IItemStack in, IItemStack out) {
        ItemStack inStack = MineTweakerMC.getItemStack(in);
        ItemStack outStack = MineTweakerMC.getItemStack(out);
        MineTweakerAPI.apply(new Remove(inStack, outStack));
    }

    @ZenMethod
    public static void removeSmashingOreRecipe(String dict, IItemStack out) {
        ItemStack outStack = MineTweakerMC.getItemStack(out);
        MineTweakerAPI.apply(new Remove(dict, outStack));
    }

    private static class Remove implements IUndoableAction {
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
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            if (in instanceof ItemStack) {
                SmasherRegistry.registerSmashable((ItemStack) in, out);
            } else if (in instanceof String) {
                SmasherRegistry.registerSmashable((String) in, out);
            }
        }

        @Override
        public String describe() {
            if (in instanceof ItemStack) {
                return "Removing smashing recipe for " + ((ItemStack) in).getUnlocalizedName();
            } else if (in instanceof String) {
                return "Removing smashing recipe for " + in;
            }
            return null;
        }

        @Override
        public String describeUndo() {
            if (in instanceof ItemStack) {
                return "Adding smashing recipe for " + ((ItemStack) in).getUnlocalizedName();
            } else if (in instanceof String) {
                return "Adding smashing recipe for " + in;
            }
            return null;
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }
}
