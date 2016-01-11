package flaxbeard.steamcraft.misc;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.*;

public class RecipeHelper {
    private static HashMap<MutablePair<Item, Integer>, MutablePair<Item, Integer>> ingotsBlocks = new HashMap<>();
    public static HashMap<Item, MutablePair<Integer, Integer>> blockMaterials = new HashMap<>();

    // TODO: Custom materials in XML or YAML config.
    public static void initializeRecipes() {
        CraftingManager manager = CraftingManager.getInstance();
        List recipes = manager.getRecipeList();
        // Block -> ingot recipes
        for (Object r : recipes) {
            if (!(r instanceof IRecipe)) {
                continue;
            }
            if (r instanceof ShapelessRecipes) {
                ShapelessRecipes recipe = (ShapelessRecipes) r;
                List inputItems = recipe.recipeItems;
                ItemStack input = null;
                for (Object inputItem : inputItems) {
                    if (inputItem == null || !(inputItem instanceof ItemStack)) {
                        continue;
                    }
                    ItemStack i = (ItemStack) inputItem;
                    if (OreDictHelper.containsKey(OreDictHelper.blocks,
                      MutablePair.of(i.getItem(), i.getItemDamage()))) {
                        input = i;
                        break;
                    }
                }
                ItemStack output = recipe.getRecipeOutput();
                Item outputItem = output.getItem();
                int meta = output.getItemDamage();
                if (input == null || (!OreDictHelper.containsKey(OreDictHelper.ingots, MutablePair.of(outputItem, meta)) &&
                  !OreDictHelper.containsKey(OreDictHelper.gems, MutablePair.of(outputItem, meta)))) {
                    continue;
                }
                ingotsBlocks.put(MutablePair.of(outputItem, meta),
                  MutablePair.of(input.getItem(), input.getItemDamage()));
            } else if (r instanceof ShapedRecipes) {
                ShapedRecipes recipe = (ShapedRecipes) r;
                if (recipe.getRecipeSize() != 1) {
                    continue;
                }
                ItemStack[] inputItems = recipe.recipeItems;
                ItemStack input = null;
                for (ItemStack inputItem : inputItems) {
                    if (inputItem == null) {
                        continue;
                    }
                    if (OreDictHelper.containsKey(OreDictHelper.blocks,
                      MutablePair.of(inputItem.getItem(), inputItem.getItemDamage()))) {
                        input = inputItem;
                        break;
                    }
                }
                ItemStack output = recipe.getRecipeOutput();
                Item outputItem = output.getItem();
                int meta = output.getItemDamage();
                if (input == null || (!OreDictHelper.containsKey(OreDictHelper.ingots, MutablePair.of(outputItem, meta)) &&
                  !OreDictHelper.containsKey(OreDictHelper.gems, MutablePair.of(outputItem, meta)))) {
                    continue;
                }
                ingotsBlocks.put(MutablePair.of(outputItem, meta),
                  MutablePair.of(input.getItem(), input.getItemDamage()));
            } else if (r instanceof ShapelessOreRecipe) {
                ShapelessOreRecipe recipe = (ShapelessOreRecipe) r;
                if (recipe.getRecipeSize() != 1) {
                    continue;
                }
                ItemStack output = recipe.getRecipeOutput();
                Item outputItem = output.getItem();
                int meta = output.getItemDamage();
                ArrayList<Object> input = recipe.getInput();
                for (Object obj : input) {
                    handleOreDictRecipe(obj, outputItem, meta);
                }
            } else if (r instanceof ShapedOreRecipe) {
                ShapedOreRecipe recipe = (ShapedOreRecipe) r;
                if (recipe.getRecipeSize() != 1) {
                    continue;
                }
                ItemStack output = recipe.getRecipeOutput();
                Item outputItem = output.getItem();
                int meta = output.getItemDamage();
                Object[] input = recipe.getInput();
                for (Object obj : input) {
                    handleOreDictRecipe(obj, outputItem, meta);
                }
            }
        }
        FMLLog.info("Finished initializing ingot/block recipes for FSP with " + ingotsBlocks.size() + " entries");

        // Pickaxe recipes
        for (Object r : recipes) {
            if (!(r instanceof IRecipe)) {
                continue;
            }
            Object[] inputItems;
            ItemStack output;
            IRecipe recipe = (IRecipe) r;
            if (r instanceof ShapedRecipes) {
                inputItems = ((ShapedRecipes) recipe).recipeItems;
            } else if (r instanceof ShapedOreRecipe) {
                inputItems = ((ShapedOreRecipe) recipe).getInput();
            } else {
                continue;
            }
            output = recipe.getRecipeOutput();

            ItemStack theValidItem = handlePickaxeRecipe(inputItems);
            if (theValidItem != null && output.getItem() instanceof ItemTool && output.getItem()
              instanceof ItemPickaxe) {
                Item.ToolMaterial material = ((ItemTool) output.getItem()).func_150913_i();
                if (material != null) {
                    int level = material.getHarvestLevel();
                    int meta = theValidItem.getItemDamage();
                    MutablePair<Item, Integer> block = ingotsBlocks.get(MutablePair.of(
                      theValidItem.getItem(), meta));
                    MutablePair<Integer, Integer> pair;
                    if (block.right == OreDictionary.WILDCARD_VALUE) {
                        pair = MutablePair.of(0, level);
                    } else {
                        pair = MutablePair.of(block.right, level);
                    }
                    blockMaterials.put(block.left, pair);
                }
            }
        }
        FMLLog.info("Finished initializing block materials for FSP with " + blockMaterials.size() + " entries");
        for (Map.Entry<Item, MutablePair<Integer, Integer>> entry : blockMaterials.entrySet()) {
            System.out.println("key: " + entry.getKey().getUnlocalizedName());
            System.out.println("value: " + entry.getValue().left + ", " + entry.getValue().right);
        }
    }

    /**
     * Adds the given input based on the type of Object it is; String, ItemStack, Block, or Item.
     * @param input The input Object.
     * @param outputItem The output Item for the recipe.
     * @param meta The output Item's metadata.
     */
    private static void handleOreDictRecipe(Object input, Item outputItem, int meta) {
        if (input instanceof ItemStack) {
            ItemStack s = (ItemStack) input;
            if (OreDictHelper.containsKey(OreDictHelper.blocks,
              MutablePair.of(s.getItem(), s.getItemDamage()))) {
                ingotsBlocks.put(MutablePair.of(outputItem, meta),
                  MutablePair.of(s.getItem(), s.getItemDamage()));
            }
        } else if (input instanceof Block) {
            Block block = (Block) input;
            ArrayList<MutablePair<Item, Integer>> ary = new ArrayList<>(OreDictHelper.blocks.keySet());
            Item item = Item.getItemFromBlock(block);
            if (OreDictHelper.arrayHasItem(ary, item)) {
                ingotsBlocks.put(MutablePair.of(outputItem, meta), MutablePair.of(item, 0));
            }
        } else if (input instanceof Item) {
            Item item = (Item) input;
            ArrayList<MutablePair<Item, Integer>> ary = new ArrayList<>(OreDictHelper.blocks.keySet());
            if (OreDictHelper.arrayHasItem(ary, item)) {
                ingotsBlocks.put(MutablePair.of(outputItem, meta), MutablePair.of(item, 0));
            }
        } else if (input instanceof ArrayList) {
            for (Object o : (ArrayList) input) {
                handleOreDictRecipe(o, outputItem, meta);
            }
        }
    }

    private static ItemStack handlePickaxeRecipe(Object[] inputItems) {
        return handlePickaxeRecipe(inputItems, new MutableInt(0), new MutableInt(0), null);
    }

    /**
     * Handles the Pickaxe Recipes similar to handleOreDictRecipe.
     * @param inputItems The input items given by the IRecipe.
     * @param sticks The number of sticks in the recipe at the start of the iteration through input.
     * @param resources The number of X RESOURCE in the recipe at the start of the input iteration.
     * @param theValidItem The valid resource itemstack at the start of the input iteration.
     * @return The ItemStack that is valid, or null, if the conditions are not met.
     */
    private static ItemStack handlePickaxeRecipe(Object[] inputItems, MutableInt sticks, MutableInt resources, ItemStack theValidItem) {
        boolean multipleIngotTypes = false;
        boolean onlyHasIngotsAndSticks = true;
        ItemStack theValidItem1 = theValidItem;
        for (Object input : inputItems) {
            if (input == null) {
                continue;
            }
            if (input instanceof ItemStack) {
                ItemStack stack = (ItemStack) input;
                Item item = stack.getItem();
                int meta = stack.getItemDamage();
                if (OreDictHelper.arrayContains(OreDictHelper.sticks, MutablePair.of(item, meta))) {
                    sticks.increment();
                } else if (ingotsBlocks.keySet().contains(MutablePair.of(item, meta))) {
                    if (theValidItem1 == null) {
                        theValidItem1 = stack;
                        resources.increment();
                    } else if (theValidItem1 == stack) {
                        resources.increment();
                    } else {
                        multipleIngotTypes = true;
                    }
                } else {
                    onlyHasIngotsAndSticks = false;
                }
            } else if (input instanceof Item) {
                Item item = (Item) input;
                if (OreDictHelper.arrayHasItem(OreDictHelper.sticks, item)) {
                    sticks.increment();
                } else if (ingotsBlocks.keySet().contains(MutablePair.of(item, 0))) {
                    ItemStack stack = new ItemStack(item);
                    if (theValidItem1 == null) {
                        theValidItem1 = stack;
                        resources.increment();
                    } else if (theValidItem1 == stack) {
                        resources.increment();
                    } else {
                        multipleIngotTypes = true;
                    }
                } else {
                    onlyHasIngotsAndSticks = false;
                }
            } else if (input instanceof Block) {
                Item item = Item.getItemFromBlock((Block) input);
                if (OreDictHelper.arrayHasItem(OreDictHelper.sticks, item)) {
                    sticks.increment();
                } else if (ingotsBlocks.keySet().contains(MutablePair.of(item, 0))) {
                    ItemStack stack = new ItemStack(item);
                    if (theValidItem1 == null) {
                        theValidItem1 = stack;
                        resources.increment();
                    } else if (theValidItem1 == stack) {
                        resources.increment();
                    } else {
                        multipleIngotTypes = true;
                    }
                } else {
                    onlyHasIngotsAndSticks = false;
                }
            } else if (input instanceof ArrayList) {
                ArrayList obj = (ArrayList) input;
                if (obj.isEmpty() || obj.size() < 1 || obj.get(0) == null) {
                    continue;
                }
                Object[] ary = new Object[]{ obj.get(0) };
                ItemStack s = handlePickaxeRecipe(ary, sticks, resources, theValidItem1);
                if (theValidItem1 == null) {
                    theValidItem1 = s;
                } else if (theValidItem1 == s) {
                    resources.increment();
                } else {
                    multipleIngotTypes = true;
                }
            }
        }
        if (sticks.intValue() == 2 && resources.intValue() == 3 && !multipleIngotTypes &&
          onlyHasIngotsAndSticks) {
            return theValidItem;
        }
        return theValidItem1;
    }
}
