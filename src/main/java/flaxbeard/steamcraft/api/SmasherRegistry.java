package flaxbeard.steamcraft.api;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public final class SmasherRegistry {
    /**
     * The smasher recipe map. Key: Input, Value: Output
     */
    public static final Map<ItemStack, ItemStack> registry = new HashMap<>();

    /**
     * Gets the smasher output for the provided ItemStack.
     * @param input The input ItemStack
     * @return The output ItemStack. Can be null.
     */
    public static ItemStack getOutput(ItemStack input) {
        if (input == null) {
            return null;
        }

        ItemStack output = null;
        for (Map.Entry<ItemStack, ItemStack> entry : registry.entrySet()) {
            if (ItemStack.areItemStacksEqual(entry.getKey(), input)) {
                output = entry.getValue();
                if (output != null) {
                    break;
                }
            }
        }

        return ItemStack.copyItemStack(output);
    }

    /**
     * Gets all of the input ItemStacks that will produce the provided output. This does not respect stacksize.
     * @param output The desired output
     * @return A list of all ItemStacks that produce the output. Can be null if the output is null.
     */
    public static List<ItemStack> getInputs(ItemStack output) {
        if (output == null) {
            return null;
        }

        List<ItemStack> inputs = new ArrayList<>();

        for (Map.Entry<ItemStack, ItemStack> entry : registry.entrySet()) {
            if (output.isItemEqual(entry.getValue())) {
                inputs.add(entry.getKey());
            }
        }

        return inputs;
    }

    /**
     * Registers a smasher recipe
     * @param input The input OreDictionary entry. Will register a recipe for every single ItemStack in that OreDict.
     * @param output The output ItemStack
     */
    public static void registerSmashable(String input, ItemStack output) {
        for (ItemStack stack : OreDictionary.getOres(input)) {
            registerSmashable(stack, output);
        }
    }

    /**
     * Registers a smasher recipe
     * @param input The input block, or the block being smashed.
     * @param output The output ItemStack
     */
    public static void registerSmashable(Block input, ItemStack output) {
        registerSmashable(new ItemStack(input), output);
    }

    /**
     * Registers a smasher recipe
     * @param input The input ItemStack
     * @param output The output ItemStack
     */
    public static void registerSmashable(ItemStack input, ItemStack output) {
        registry.put(input, output);
    }

    /**
     * Removes smasher recipes
     * @param input The input OreDictionary entry whose recipe is being removed. This will remove the recipe for every
     *              single ItemStack in that entry.
     */
    public static void removeSmashable(String input) {
        for (ItemStack stack : OreDictionary.getOres(input)) {
            removeSmashable(stack);
        }
    }

    /**
     * Removes a smasher recipe
     * @param input The input ItemStack whose recipe is being removed
     */
    public static void removeSmashable(ItemStack input) {
        registry.remove(input);
    }
}
