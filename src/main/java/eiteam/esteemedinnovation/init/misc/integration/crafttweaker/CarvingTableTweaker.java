package eiteam.esteemedinnovation.init.misc.integration.crafttweaker;

import eiteam.esteemedinnovation.EsteemedInnovation;

import eiteam.esteemedinnovation.api.mold.MoldRegistry;
import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.Item;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods." + EsteemedInnovation.MOD_ID + ".CarvingTable")
public class CarvingTableTweaker {
    @ZenMethod
    public static void addCarvable(IItemStack stack) {
        Item item = MineTweakerMC.getItemStack(stack).getItem();
        MineTweakerAPI.apply(new Add(item));
    }

    @ZenMethod
    public static void removeCarvable(IItemStack stack) {
        Item item = MineTweakerMC.getItemStack(stack).getItem();
        MineTweakerAPI.apply(new Remove(item));
    }

    private static class Add implements IUndoableAction {
        private final Item item;

        public Add(Item item) {
            this.item = item;
        }

        @Override
        public void apply() {
            MoldRegistry.addCarvableMold(item);
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            MoldRegistry.removeMold(item);
        }

        @Override
        public String describe() {
            return "Adding " + item.getUnlocalizedName() + " to list of Carving Table molds";
        }

        @Override
        public String describeUndo() {
            return "Removing " + item.getUnlocalizedName() + " from the list of Carving Table molds";
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }

    private static class Remove implements IUndoableAction {
        private final Item item;

        public Remove(Item item) {
            this.item = item;
        }

        @Override
        public void apply() {
            MoldRegistry.removeMold(item);
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            MoldRegistry.addCarvableMold(item);
        }

        @Override
        public String describe() {
            return "Removing " + item.getUnlocalizedName() + " from the list of Carving Table molds";
        }

        @Override
        public String describeUndo() {
            return "Adding " + item.getUnlocalizedName() + " to the list of Carving Table molds";
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }
}
