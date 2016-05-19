package flaxbeard.steamcraft.integration.minetweaker;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import static flaxbeard.steamcraft.tile.TileEntitySmasher.REGISTRY;

@ZenClass("mods.fsp.RockSmasher")
public class RockSmasherTweaker {
    
	private RockSmasherTweaker() throws InstantiationException{
		throw new InstantiationException("This class is not meant to be instantiated");
	}
	
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
                REGISTRY.registerSmashable((ItemStack) in, out);
            } else if (in instanceof String) {
                REGISTRY.registerSmashable((String) in, out);
            }
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            if (in instanceof ItemStack) {
                REGISTRY.removeSmashable((ItemStack) in, out);
            } else if (in instanceof String) {
                REGISTRY.removeSmashable((String) in, out);
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
                REGISTRY.removeSmashable((ItemStack) in, out);
            } else if (in instanceof String) {
                REGISTRY.removeSmashable((String) in, out);
            }
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            if (in instanceof ItemStack) {
                REGISTRY.registerSmashable((ItemStack) in, out);
            } else if (in instanceof String) {
                REGISTRY.registerSmashable((String) in, out);
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
