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
    public static ArrayList<ICrucibleMold> molds = new ArrayList<ICrucibleMold>();
    public static ArrayList<CrucibleLiquid> liquids = new ArrayList<CrucibleLiquid>();
    public static HashMap<MutablePair<Item, Integer>, MutablePair<CrucibleLiquid, Integer>> smeltThings = new HashMap<MutablePair<Item, Integer>, MutablePair<CrucibleLiquid, Integer>>();
    public static HashMap<Tuple3, MutablePair<Integer, ItemStack>> dunkThings = new HashMap<Tuple3, MutablePair<Integer, ItemStack>>();
    public static HashMap<MutablePair<Item, IEnhancement>, IIcon> enhancementIcons = new HashMap<MutablePair<Item, IEnhancement>, IIcon>();
    public static HashMap<String, IEnhancement> enhancements = new HashMap<String, IEnhancement>();
    public static ArrayList<IRocket> rockets = new ArrayList<IRocket>();
    public static ArrayList<String> categories = new ArrayList<String>();
    public static ArrayList<MutablePair<String, String>> research = new ArrayList<MutablePair<String, String>>();
    public static HashMap<String, BookPage[]> researchPages = new HashMap<String, BookPage[]>();
    public static HashMap<ItemStack, MutablePair<String, Integer>> bookRecipes = new HashMap<ItemStack, MutablePair<String, Integer>>();
    public static HashMap<String, ExosuitPlate> plates = new HashMap<String, ExosuitPlate>();
    public static HashMap<MutablePair<Integer, ExosuitPlate>, IIcon> plateIcons = new HashMap<MutablePair<Integer, ExosuitPlate>, IIcon>();
    public static HashMap<MutablePair<Item, Integer>, MutablePair<Item, Integer>> steamedFoods = new HashMap<MutablePair<Item, Integer>, MutablePair<Item, Integer>>();
    private static int nextEnhancementID = 0;

    public static void addSteamFood(Item food1, int i,
                                    Item food2, int j) {
        steamedFoods.put(MutablePair.of(food1, i), MutablePair.of(food2, j));
    }

    public static void addSteamFood(Item food1, Item food2) {
        steamedFoods.put(MutablePair.of(food1, -1), MutablePair.of(food2, -1));
    }

    public static void addExosuitPlate(ExosuitPlate plate) {
        plates.put(plate.getIdentifier(), plate);
    }

    public static void addCarvableMold(ICrucibleMold mold) {
        molds.add(mold);
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
            ArrayList<BookPage> pages2 = new ArrayList<BookPage>(Arrays.asList(targetPages));
            for (BookPage page : pages) {
                pages2.add(page);
            }
            researchPages.put(category.substring(1), (BookPage[]) pages2.toArray(new BookPage[0]));
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
