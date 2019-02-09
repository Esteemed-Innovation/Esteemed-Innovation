package eiteam.esteemedinnovation.metalcasting.mold;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;

import eiteam.esteemedinnovation.api.mold.MoldRegistry;
import net.minecraft.item.Item;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods." + EsteemedInnovation.MOD_ID + ".CarvingTable")
public class CarvingTableTweaker {
    @ZenMethod
    public static void addCarvable(IItemStack stack) {
        Item item = CraftTweakerMC.getItemStack(stack).getItem();
        CraftTweakerAPI.apply(new Add(item));
    }

    @ZenMethod
    public static void removeCarvable(IItemStack stack) {
        Item item = CraftTweakerMC.getItemStack(stack).getItem();
        CraftTweakerAPI.apply(new Remove(item));
    }

    private static class Add implements IAction {
        private final Item item;

        public Add(Item item) {
            this.item = item;
        }

        @Override
        public void apply() {
            MoldRegistry.addCarvableMold(item);
        }

        @Override
        public String describe() {
            return "Adding " + item.getUnlocalizedName() + " to list of Carving Table molds";
        }
    }

    private static class Remove implements IAction {
        private final Item item;

        public Remove(Item item) {
            this.item = item;
        }

        @Override
        public void apply() {
            MoldRegistry.removeMold(item);
        }

        @Override
        public String describe() {
            return "Removing " + item.getUnlocalizedName() + " from the list of Carving Table molds";
        }
    }
}
