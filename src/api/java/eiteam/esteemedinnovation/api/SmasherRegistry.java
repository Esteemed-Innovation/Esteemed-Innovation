package eiteam.esteemedinnovation.api;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public final class SmasherRegistry {
    /**
     * The smasher recipe map. Key: Input, Value: Function that passes the input and world and returns the output
     */
    public static final Map<ItemStack, BiFunction<ItemStack, World, List<ItemStack>>> registry = new HashMap<>();

    /**
     * Gets the smasher output for the provided ItemStack.
     * @param input The input ItemStack
     * @param world The current world
     * @return The output ItemStacks.
     */
    @Nonnull
    public static List<ItemStack> getOutput(ItemStack input, World world) {
        if (input == null) {
            return Collections.emptyList();
        }

        List<ItemStack> output = new ArrayList<>();
        for (Map.Entry<ItemStack, BiFunction<ItemStack, World, List<ItemStack>>> entry : registry.entrySet()) {
            if (ItemStack.areItemStacksEqual(entry.getKey(), input)) {
                output = entry.getValue().apply(input, world);
                if (!output.isEmpty()) {
                    break;
                }
            }
        }

        return output.stream().map(ItemStack::copy).collect(Collectors.toList());
    }

    /**
     * Gets all of the input ItemStacks that will produce the provided output. This does not respect stacksize.
     * @param output The desired output
     * @param world The current world
     * @return A list of all ItemStacks that produce the output. Can be null if the output is null. Because the function
     *         might use randomization (or other such things) to return its outputs, this might not be 100% accurate all of the time.
     */
    public static List<ItemStack> getInputs(ItemStack output, World world) {
        if (output == null) {
            return null;
        }

        List<ItemStack> inputs = new ArrayList<>();
        for (Map.Entry<ItemStack, BiFunction<ItemStack, World, List<ItemStack>>> entry : registry.entrySet()) {
            for (ItemStack out : entry.getValue().apply(entry.getKey(), world)) {
                if (out.isItemEqual(output)) {
                    inputs.add(entry.getKey());
                }
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
     * @param input The input OreDictionary entry. Will register a recipe for every single ItemSTack in that OreDict.
     * @param function The function that returns the output.
     */
    public static void registerSmashable(String input, BiFunction<ItemStack, World, List<ItemStack>> function) {
        for (ItemStack stack : OreDictionary.getOres(input)) {
            registerSmashable(stack, function);
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
     * @param input The input block, or the block being smashed.
     * @param function The function that returns the output.
     */
    public static void registerSmashable(Block input, BiFunction<ItemStack, World, List<ItemStack>> function) {
        registerSmashable(new ItemStack(input), function);
    }

    /**
     * Registers a smasher recipe
     * @param input The input ItemStack
     * @param output The output ItemStack
     */
    public static void registerSmashable(ItemStack input, ItemStack output) {
        registerSmashable(input, new TypicalBiFunction(output));
    }

    /**
     * Registers a smasher recipe
     * @param input The input ItemStack
     * @param function The function that returns the output.
     */
    public static void registerSmashable(ItemStack input, BiFunction<ItemStack, World, List<ItemStack>> function) {
        registry.put(input, function);
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

    /**
     * A helper function class that takes a input ItemStack and returns the provided output.
     * <p>
     * You should probably just use this unless you need special behavior (e.g., doubling, randomization, etc.), or just
     * use the above helper methods. Those methods (the ones that don't take a function) use this anyway.
     */
    public static class TypicalBiFunction implements BiFunction<ItemStack, World, List<ItemStack>> {
        private final List<ItemStack> out;

        public TypicalBiFunction(ItemStack out) {
            this(Collections.singletonList(out));
        }

        public TypicalBiFunction(List<ItemStack> out) {
            this.out = out;
        }

        @Override
        public List<ItemStack> apply(ItemStack input, World world) {
            return out;
        }
    }
}
