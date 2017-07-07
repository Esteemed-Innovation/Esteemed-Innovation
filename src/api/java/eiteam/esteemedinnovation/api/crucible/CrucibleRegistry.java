package eiteam.esteemedinnovation.api.crucible;

import eiteam.esteemedinnovation.api.util.ItemStackUtility;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

public class CrucibleRegistry {
    /**
     * All of the CrucibleLiquids that the mod knows about.
     */
    public static List<CrucibleLiquid> liquids = new ArrayList<>();

    /**
     * All of the CrucibleFormulas
     */
    public static List<CrucibleFormula> alloyFormulas = new ArrayList<>();

    /**
     * All of the CrucibleLiquid recipes.
     * <ul>
     *     <li>Key: pair of the item and its metadata, -1 if it does not use metadata.</li>
     *     <li>Value: pair of the liquid and the amount created.</li>
     * </ul>
     */
    public static Map<Pair<Item, Integer>, Pair<CrucibleLiquid, Integer>> liquidRecipes = new HashMap<>();

    /**
     * All of the Crucible dunking recipes.
     * <ul>
     *     <li>Key: A triplet of the Item, the metadata, and the {@link CrucibleLiquid}.</li>
     *     <li>Value: A pair of the required liquid amount and the output ItemStack.</li>
     * </ul>
     */
    public static Map<Triple<Item, Integer, CrucibleLiquid>, Pair<Integer, ItemStack>> dunkRecipes = new HashMap<>();

    /**
     * Gets the given CrucibleLiquid from the name.
     * @param name The liquid's name.
     * @return Null if it cannot find that liquid, otherwise, the liquid.
     */
    public static CrucibleLiquid getLiquidFromName(String name) {
        for (CrucibleLiquid liquid : liquids) {
            if (liquid.getName().equals(name)) {
                return liquid;
            }
        }
        return null;
    }

    /**
     * Registers a Crucible dunking recipe.
     * @param item The input item
     * @param meta The input metadata
     * @param liquid The input liquid.
     * @param liquidAmount The amount of liquid needed.
     * @param result The output ItemStack.
     */
    public static void registerDunkRecipe(Item item, int meta, CrucibleLiquid liquid, int liquidAmount, ItemStack result) {
        dunkRecipes.put(new ImmutableTriple<>(item, meta, liquid), Pair.of(liquidAmount, result));
    }

    /**
     * Registers a Crucible dunking recipe with no input metadata.
     * @param item The input item
     * @param liquid The input liquid.
     * @param liquidAmount The amount of the liquid needed.
     * @param result The output ItemStack.
     */
    public static void registerDunkRecipe(Item item, CrucibleLiquid liquid, int liquidAmount, ItemStack result) {
        registerDunkRecipe(item, -1, liquid, liquidAmount, result);
    }

    /**
     * Removes a dunking recipe.
     * @param item The input item.
     * @param meta The input item metadata.
     * @param liquid The input liquid.
     */
    public static void removeDunkRecipe(Item item, int meta, CrucibleLiquid liquid) {
        if (dunkRecipes != null) {
            Iterator<Map.Entry<Triple<Item, Integer, CrucibleLiquid>, Pair<Integer, ItemStack>>> iter = dunkRecipes.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<Triple<Item, Integer, CrucibleLiquid>, Pair<Integer, ItemStack>> entry = iter.next();
                Triple<Item, Integer, CrucibleLiquid> key = entry.getKey();
                if (key.getLeft() == item && key.getMiddle() == meta && key.getRight() == liquid) {
                    iter.remove();
                }
            }
        }
    }

    /**
     * Removes a dunking recipe that does not take metadata.
     * @param item The input item.
     * @param liquid The input liquid.
     */
    public static void removeDunkRecipe(Item item, CrucibleLiquid liquid) {
        removeDunkRecipe(item, -1, liquid);
    }

    /**
     * Registers a Crucible dunking recipe using the OreDictionary.
     * @param dict The OreDict tag.
     * @param liquid The input liquid.
     * @param liquidAmount The liquid needed.
     * @param result The result ItemStack.
     */
    public static void registerOreDictDunkRecipe(String dict, CrucibleLiquid liquid, int liquidAmount, ItemStack result) {
        List<ItemStack> ores = OreDictionary.getOres(dict);
        for (ItemStack ore : ores) {
            registerDunkRecipe(ore.getItem(), ore.getItemDamage(), liquid, liquidAmount, result);
        }
    }

    /**
     * Removes a Crucible dunking recipe that uses the OreDictionary.
     * @param dict The input OreDict tag.
     * @param liquid The input liquid.
     */
    public static void removeOreDictDunkRecipe(String dict, CrucibleLiquid liquid) {
        List<ItemStack> ores = OreDictionary.getOres(dict);
        for (ItemStack ore : ores) {
            removeDunkRecipe(ore.getItem(), ore.getItemDamage(), liquid);
        }
    }

    /**
     * Registers a Crucible melting recipe.
     * @param item The input item.
     * @param i The input metadata.
     * @param liquid The output liquid.
     * @param m The output liquid amount.
     */
    public static void registerMeltRecipe(Item item, int i, CrucibleLiquid liquid, int m) {
        liquidRecipes.put(Pair.of(item, i), Pair.of(liquid, m));
    }

    /**
     * Registers a Crucible melting recipe with no input metadata.
     * @param item The input item.
     * @param liquid The output liquid.
     * @param m The amount of liquid.
     */
    public static void registerMeltRecipe(Item item, CrucibleLiquid liquid, int m) {
        liquidRecipes.put(Pair.of(item, -1), Pair.of(liquid, m));
    }

    /**
     * Registers a Crucible melting recipe that takes an OreDict entry as input.
     * @param dict The OreDict tag.
     * @param liquid The output liquid.
     * @param m The amount of liquid.
     */
    public static void registerMeltRecipeOreDict(String dict, CrucibleLiquid liquid, int m) {
        List<ItemStack> ores = OreDictionary.getOres(dict);
        for (ItemStack ore : ores) {
            registerMeltRecipe(ore.getItem(), ore.getItemDamage(), liquid, m);
        }
    }

    /**
     * Registers a Crucible melting recipe for a tool.
     * @param item The input item.
     * @param liquid The output liquid.
     * @param m The amount of liquid.
     */
    public static void registerMeltRecipeTool(Item item, CrucibleLiquid liquid, int m) {
        for (int i = 0; i < item.getMaxDamage(); i++) {
            liquidRecipes.put(Pair.of(item, i), Pair.of(liquid, MathHelper.floor(m * ((float) (item.getMaxDamage() - i) / (float) item.getMaxDamage()))));
        }
    }

    /**
     * Removes a melt recipe for an OreDict entry.
     * @param dict The oredict tag.
     * @param liquid The output liquid.
     */
    public static void removeMeltRecipeOreDict(String dict, CrucibleLiquid liquid) {
        List<ItemStack> ores = OreDictionary.getOres(dict);
        for (ItemStack ore : ores) {
            removeMeltRecipe(ore.getItem(), ore.getItemDamage(), liquid);
        }
    }

    /**
     * Removes a melting recipe for a tool.
     * @param item The tool
     * @param liquid The output liquid
     */
    public static void removeMeltRecipeTool(Item item, CrucibleLiquid liquid) {
        for (int j = 0; j < item.getMaxDamage(); j++) {
            removeMeltRecipe(item, j, liquid);
        }
    }

    /**
     * Removes a melting recipe.
     * @param item Input item
     * @param meta Input item metadata.
     * @param liquid Output liquid.
     */
    public static void removeMeltRecipe(Item item, int meta, CrucibleLiquid liquid) {
        Pair input = Pair.of(item, meta);
        if (liquidRecipes.containsKey(input)) {
            Pair output = liquidRecipes.get(input);
            if (output.getLeft() == liquid) {
                liquidRecipes.remove(input);
            }
        }
    }

    /**
     * Removes a melting recipe without metadata.
     * @param item Input item.
     * @param liquid Output liquid.
     */
    public static void removeMeltRecipe(Item item, CrucibleLiquid liquid) {
        removeMeltRecipe(item, -1, liquid);
    }

    /**
     * Sets the given CrucibleLiquid as an actual CrucibleLiquid that can be used.
     * @param liquid The liquid
     */
    public static void registerLiquid(CrucibleLiquid liquid) {
        liquids.add(liquid);
    }

    /**
     * Registers the provided CrucibleFormula
     * @param formula The formula
     */
    public static void registerFormula(CrucibleFormula formula) {
        alloyFormulas.add(formula);
    }

    /**
     * Removes the liquid from the list of registered liquids. It also removes all of the liquid's
     * recipes.
     * @param liquid The item
     */
    public static void removeLiquid(CrucibleLiquid liquid) {
        liquids.remove(liquid);
        if (liquidRecipes != null) {
            for (Map.Entry<Pair<Item, Integer>, Pair<CrucibleLiquid, Integer>> entry : liquidRecipes.entrySet()) {
                if ((entry.getValue()).getLeft() == liquid) {
                    liquidRecipes.remove(entry.getKey());
                }
            }
        }
    }

    /**
     * Removes the provided CrucibleFormula from the registry.
     * @param formula The formula
     */
    public static void removeFormula(CrucibleFormula formula) {
        alloyFormulas.remove(formula);
    }

    /**
     * Finds all of the alloy formulas that result in the provided liquid.
     * @param resultLiquid The output liquid
     * @return A set of all matching formulas
     */
    public static List<CrucibleFormula> findRecipesThatResultInLiquid(CrucibleLiquid resultLiquid) {
        return alloyFormulas.stream().filter(formula -> formula.getOutputLiquid().equals(resultLiquid)).collect(Collectors.toList());
    }

    /**
     * The recipes for casting molds.
     * Key: Input liquid, input mold.
     * Value: Output item.
     */
    private static final Map<Pair<CrucibleLiquid, ItemStack>, ItemStack> moldingRecipes = new HashMap<>();

    /**
     * Registers a casting recipe.
     * @param inputLiquid The input liquid
     * @param mold The input mold
     * @param out The output
     */
    public static void registerMoldingRecipe(CrucibleLiquid inputLiquid, @Nonnull ItemStack mold, @Nonnull ItemStack out) {
        moldingRecipes.put(Pair.of(inputLiquid, mold), out);
    }

    /**
     * @param inputLiquid The input liquid
     * @param mold The input mold
     * @return The output ItemStack
     */
    @Nonnull
    public static ItemStack getMoldingOutput(CrucibleLiquid inputLiquid, @Nonnull ItemStack mold) {
        for (Map.Entry<Pair<CrucibleLiquid, ItemStack>, ItemStack> entry : moldingRecipes.entrySet()) {
            Pair<CrucibleLiquid, ItemStack> input = entry.getKey();
            if (ItemStackUtility.compareItemStacks(mold, input.getRight()) && input.getLeft().equals(inputLiquid)) {
                return entry.getValue();
            }
        }
        return ItemStack.EMPTY;
    }
}
