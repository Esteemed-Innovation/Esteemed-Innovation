package flaxbeard.steamcraft.integration.minetweaker;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import flaxbeard.steamcraft.api.SteamcraftRegistry;

@ZenClass("mods.fsp.SteamHeater")
public class SteamHeaterTweaker {
    
	private SteamHeaterTweaker() throws InstantiationException{
		throw new InstantiationException("This class is not meant to be instantiated");
	}
	
	@ZenMethod
    public static void addSteamingRecipe(IItemStack originalI, IItemStack steamedI) {
        ItemStack original = MineTweakerMC.getItemStack(originalI);
        ItemStack steamed = MineTweakerMC.getItemStack(steamedI);
        MineTweakerAPI.apply(new Add(original.getItem(), original.getItemDamage(),
          steamed.getItem(), steamed.getItemDamage()));
    }

    private static class Add implements IUndoableAction {
        private final Item originalItem;
        private final int originalMeta;
        private final Item steamedItem;
        private final int steamedMeta;

        public Add(Item originalItem, int originalMeta, Item steamedItem, int steamedMeta) {
            this.originalItem = originalItem;
            this.originalMeta = originalMeta;
            this.steamedItem = steamedItem;
            this.steamedMeta = steamedMeta;
        }

        @Override
        public void apply() {
            SteamcraftRegistry.addSteamingRecipe(originalItem, originalMeta, steamedItem, steamedMeta);
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            SteamcraftRegistry.removeSteamingRecipe(originalItem, originalMeta);
        }

        @Override
        public String describe() {
            return "Adding steaming recipe alternative for " + originalItem.getUnlocalizedName();
        }

        @Override
        public String describeUndo() {
            return "Removing steaming recipe alternative for " + originalItem.getUnlocalizedName();
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }

    @ZenMethod
    public static void removeSteamingRecipe(IItemStack original) {
        ItemStack stack = MineTweakerMC.getItemStack(original);
        MineTweakerAPI.apply(new Remove(stack.getItem(), stack.getItemDamage()));
    }

    private static class Remove implements IUndoableAction {
        private final Item item;
        private final int meta;

        public Remove(Item item, int meta) {
            this.item = item;
            this.meta = meta;
        }

        @Override
        public void apply() {
            SteamcraftRegistry.removeSteamingRecipe(item, meta);
        }

        @Override
        public boolean canUndo() {
            return false;
        }

        @Override
        public void undo() {}

        @Override
        public String describe() {
            return "Removing steaming alternative recipe for " + item.getUnlocalizedName();
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
