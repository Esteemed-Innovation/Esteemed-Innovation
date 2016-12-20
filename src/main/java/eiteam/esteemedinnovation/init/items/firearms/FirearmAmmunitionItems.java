package eiteam.esteemedinnovation.init.items.firearms;

import eiteam.esteemedinnovation.api.firearm.EnhancementRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.api.book.BookRecipeRegistry;
import eiteam.esteemedinnovation.api.firearm.rocketlauncher.IRocket;
import eiteam.esteemedinnovation.init.IInitCategory;
import eiteam.esteemedinnovation.firearms.rocket.ammo.ItemRocketBasic;
import eiteam.esteemedinnovation.firearms.rocket.ammo.ItemRocketConcussive;
import eiteam.esteemedinnovation.firearms.rocket.ammo.ItemRocketMining;

import static net.minecraft.init.Items.*;
import static net.minecraft.init.Blocks.*;
import static eiteam.esteemedinnovation.init.misc.OreDictEntries.*;

public class FirearmAmmunitionItems implements IInitCategory {
    public enum Items {
        MUSKET_CARTRIDGE(new Item(), "musket_cartridge"),
        ROCKET(new ItemRocketBasic(), "rocket"),
        CONCUSSIVE_ROCKET(new ItemRocketConcussive(), "rocket_concussive"),
        MINING_ROCKET(new ItemRocketMining(), "rocket_miner");

        private Item item;

        public static Items[] LOOKUP = new Items[values().length];

        static {
            for (Items item : values()) {
                if (item.isEnabled()) {
                    LOOKUP[item.ordinal()] = item;
                }
            }
        }

        Items(Item item, String name) {
            item.setUnlocalizedName(EsteemedInnovation.MOD_ID + ":" + name);
            item.setCreativeTab(EsteemedInnovation.tab);
            item.setRegistryName(EsteemedInnovation.MOD_ID, name);
            GameRegistry.register(item);
            if (item instanceof IRocket) {
                EnhancementRegistry.registerRocket((IRocket) item);
            }
            this.item = item;
        }

        public boolean isEnabled() {
            switch (this) {
                case MUSKET_CARTRIDGE: {
                    return FirearmItems.Items.MUSKET.isEnabled();
                }
                case ROCKET: {
                    return FirearmItems.Items.ROCKET_LAUNCHER.isEnabled() && Config.enableRocket;
                }
                case CONCUSSIVE_ROCKET: {
                    return FirearmItems.Items.ROCKET_LAUNCHER.isEnabled() && Config.enableRocketConcussive;
                }
                case MINING_ROCKET: {
                    return FirearmItems.Items.ROCKET_LAUNCHER.isEnabled() && Config.enableRocketMining;
                }
            }
            return false;
        }

        public Item getItem() {
            return item;
        }
    }

    @Override
    public void oreDict() {}

    @Override
    public void recipes() {
        for (Items item : Items.LOOKUP) {
            switch (item) {
                case MUSKET_CARTRIDGE: {
                    String[] ores = new String[] { NUGGET_IRON, NUGGET_LEAD, NUGGET_STEEL, NUGGET_SILVER };
                    if (!Config.expensiveMusketRecipes) {
                        int i = 1;
                        for (String ore : ores) {
                            BookRecipeRegistry.addRecipe("cartridge" + i, new ShapelessOreRecipe(
                              new ItemStack(item.getItem(), 2, 0), ore, ore, PAPER, PAPER, GUNPOWDER));
                            i++;
                        }
                    } else {
                        int i = 1;
                        for (String ore : ores) {
                            BookRecipeRegistry.addRecipe("cartridge" + i, new ShapelessOreRecipe(item.getItem(),
                              ore, PAPER, GUNPOWDER
                            ));
                            i++;
                        }
                    }
                }
                case ROCKET: {
                    BookRecipeRegistry.addRecipe("normalRocket1", new ShapedOreRecipe(item.getItem(),
                      " i ",
                      "igi",
                      'i', INGOT_IRON,
                      'g', GUNPOWDER
                    ));
                    BookRecipeRegistry.addRecipe("normalRocket2", new ShapedOreRecipe(item.getItem(),
                      " i ",
                      "igi",
                      'i', PLATE_THIN_IRON,
                      'g', GUNPOWDER
                    ));
                }
                case CONCUSSIVE_ROCKET: {
                    if (Items.ROCKET.isEnabled()) {
                        BookRecipeRegistry.addRecipe("concussiveRocket", new ShapelessOreRecipe(item.getItem(),
                          Items.ROCKET.getItem(), SAND));
                    } else {
                        BookRecipeRegistry.addRecipe("concussiveRocket1", new ShapedOreRecipe(item.getItem(),
                          " i ",
                          "igi",
                          'i', INGOT_IRON,
                          'g', GUNPOWDER
                        ));
                        BookRecipeRegistry.addRecipe("concussiveRocket2", new ShapedOreRecipe(item.getItem(),
                          " i ",
                          "igi",
                          'i', PLATE_THIN_IRON,
                          'g', GUNPOWDER
                        ));
                    }
                }
                case MINING_ROCKET: {
                    if (Items.ROCKET.isEnabled()) {
                        BookRecipeRegistry.addRecipe("miningRocket", new ShapelessOreRecipe(item.getItem(),
                          Items.ROCKET.getItem(), STRING, STRING, GUNPOWDER
                        ));
                    } else {
                        BookRecipeRegistry.addRecipe("miningRocket", new ShapelessOreRecipe(item.getItem(),
                          Items.CONCUSSIVE_ROCKET.getItem(), STRING, STRING, GUNPOWDER
                        ));
                    }
                }
            }
        }
    }
}
