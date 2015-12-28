package flaxbeard.steamcraft.api;

import flaxbeard.steamcraft.api.book.BookPage;
import flaxbeard.steamcraft.api.book.ICraftingPage;
import flaxbeard.steamcraft.api.enhancement.IEnhancement;
import flaxbeard.steamcraft.api.enhancement.IRocket;
import flaxbeard.steamcraft.api.exosuit.ExosuitPlate;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SteamcraftRegistry {
    /**
     * The Items that can be used on the Carving Table.
     */
    public static ArrayList<Item> molds = new ArrayList<>();

    public static ArrayList<CrucibleLiquid> liquids = new ArrayList<>();
    public static HashMap<MutablePair<Item, Integer>, MutablePair<CrucibleLiquid, Integer>> smeltThings = new HashMap<>();
    public static HashMap<Tuple3, MutablePair<Integer, ItemStack>> dunkThings = new HashMap<>();
    public static HashMap<MutablePair<Item, IEnhancement>, IIcon> enhancementIcons = new HashMap<>();
    public static HashMap<String, IEnhancement> enhancements = new HashMap<>();
    public static ArrayList<IRocket> rockets = new ArrayList<>();
    public static ArrayList<String> categories = new ArrayList<>();
    public static ArrayList<MutablePair<String, String>> research = new ArrayList<>();
    public static HashMap<String, BookPage[]> researchPages = new HashMap<>();
    public static HashMap<ItemStack, MutablePair<String, Integer>> bookRecipes = new HashMap<>();
    public static HashMap<String, ExosuitPlate> plates = new HashMap<>();
    public static HashMap<MutablePair<Integer, ExosuitPlate>, IIcon> plateIcons = new HashMap<>();
    public static HashMap<MutablePair<Item, Integer>, MutablePair<Item, Integer>> steamedFoods = new HashMap<>();
    private static int nextEnhancementID = 0;

    /**
     * Adds a steamed food to the steamedFoods registry using metadata values.
     * @param food1 The input food item
     * @param i The input food item metadata
     * @param food2 The output food item
     * @param j The output food item metadata
     */
    public static void addSteamFood(Item food1, int i, Item food2, int j) {
        steamedFoods.put(MutablePair.of(food1, i), MutablePair.of(food2, j));
    }

    /**
     * Adds a steamed food to the steamedFoods registry without metadata.
     * @param food1 The input food item
     * @param food2 The output food item
     */
    public static void addSteamFood(Item food1, Item food2) {
        steamedFoods.put(MutablePair.of(food1, -1), MutablePair.of(food2, -1));
    }

    public static void addExosuitPlate(ExosuitPlate plate) {
        plates.put(plate.getIdentifier(), plate);
    }

    /**
     * Allows the mold to be used on the Carving Table.
     * @param mold The item
     */
    public static void addCarvableMold(Item mold) {
        molds.add(mold);
    }

    /**
     * Removes the mold from the list of Items that can be used on the Carving Table.
     * @param mold The item
     * @return The return value of ArrayList#add.
     * @see ArrayList#add(Object)
     */
    public static boolean removeMold(Item mold) {
        return molds.remove(mold);
    }

    public static void addCategory(String string) {
        categories.add(string);
    }

    public static void addResearch(String string, String category, BookPage... pages) {
        if (!category.substring(0, 1).equals("!")) {
            research.add(MutablePair.of(string, category));
            researchPages.put(string, pages);
            int pageNum = 0;
            for (BookPage page : pages) {
                if (page instanceof ICraftingPage) {
                    for (ItemStack craftedItem : ((ICraftingPage) page).getCraftedItem()) {
                        bookRecipes.put(craftedItem, MutablePair.of(string, pageNum));
                    }
                }
                pageNum++;
            }
        } else {

            BookPage[] targetPages = researchPages.get(category.substring(1));
            int pageNum = targetPages.length;
            for (BookPage page : pages) {
                if (page instanceof ICraftingPage) {
                    for (ItemStack craftedItem : ((ICraftingPage) page).getCraftedItem()) {
                        bookRecipes.put(craftedItem, MutablePair.of(category.substring(1), pageNum));
                    }
                }
                pageNum++;
            }
            ArrayList<BookPage> pages2 = new ArrayList<>(Arrays.asList(targetPages));
            pages2.addAll(Arrays.asList(pages));
            researchPages.put(category.substring(1), pages2.toArray(new BookPage[pages2.size()]));
        }
    }

    public static CrucibleLiquid getLiquidFromName(String name) {
        for (CrucibleLiquid liquid : liquids) {
            if (liquid.name.equals(name)) {
                return liquid;
            }
        }
        return null;
    }

    public static void registerDunkThing(Item item, int meta, CrucibleLiquid liquid, int liquidAmount, ItemStack result) {
        dunkThings.put(new Tuple3(item, meta, liquid), MutablePair.of(liquidAmount, result));
    }

    public static void registerDunkThing(Item item, CrucibleLiquid liquid, int liquidAmount, ItemStack result) {
        dunkThings.put(new Tuple3(item, -1, liquid), MutablePair.of(liquidAmount, result));
    }

    public static void registerDunkThingOredict(String dict, CrucibleLiquid liquid, int liquidAmount, ItemStack result) {
        ArrayList<ItemStack> ores = OreDictionary.getOres(dict);
        for (ItemStack ore : ores) {
            registerDunkThing(ore.getItem(), ore.getItemDamage(), liquid, liquidAmount, result);
        }
    }


    public static void registerSmeltThing(Item item, int i, CrucibleLiquid liquid, int m) {
        smeltThings.put(MutablePair.of(item, i), MutablePair.of(liquid, m));
    }

    public static void registerSmeltThing(Item item, CrucibleLiquid liquid, int m) {
        smeltThings.put(MutablePair.of(item, -1), MutablePair.of(liquid, m));
    }

    public static void registerSmeltThingOredict(String dict, CrucibleLiquid liquid, int m) {
        ArrayList<ItemStack> ores = OreDictionary.getOres(dict);
        for (ItemStack ore : ores) {
            registerSmeltThing(ore.getItem(), ore.getItemDamage(), liquid, m);
        }
    }

    public static void registerSmeltTool(Item item, CrucibleLiquid liquid, int m) {
        for (int i = 0; i < item.getMaxDamage(); i++) {
            smeltThings.put(MutablePair.of(item, i), MutablePair.of(liquid, MathHelper.floor_double(m * ((float) (item.getMaxDamage() - i) / (float) item.getMaxDamage()))));
        }
    }

    public static void registerLiquid(CrucibleLiquid liquid) {
        liquids.add(liquid);
    }

    public static void registerEnhancement(IEnhancement enhancement) {
        enhancements.put(enhancement.getID(), enhancement);
    }

    public static void registerRocket(IRocket rocket) {
        rockets.add(rocket);
    }
}
