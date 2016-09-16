package eiteam.esteemedinnovation.init.items.firearms;

import eiteam.esteemedinnovation.api.enhancement.EnhancementRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import eiteam.esteemedinnovation.Config;
import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.api.book.BookRecipeRegistry;
import eiteam.esteemedinnovation.api.enhancement.IEnhancement;
import eiteam.esteemedinnovation.init.blocks.SteamNetworkBlocks;
import eiteam.esteemedinnovation.init.items.CraftingComponentItems;
import eiteam.esteemedinnovation.init.IInitCategory;
import eiteam.esteemedinnovation.item.firearm.enhancement.*;

import static net.minecraft.init.Items.*;
import static net.minecraft.init.Blocks.*;
import static eiteam.esteemedinnovation.init.misc.OreDictEntries.*;

public class FirearmUpgradeItems implements IInitCategory {
    public enum Items {
        BLAZE_BARREL(new ItemEnhancementFireMusket(), "enhancement_blaze"),
        REVOLVER_CHAMBER(new ItemEnhancementRevolver(), "enhancement_revolver"),
        BREECH(new ItemEnhancementSpeedloader(), "enhancement_speedloader"),
        MAKESHIFT_SUPPRESSOR(new ItemEnhancementSilencer(), "enhancement_silencer"),
        RECOIL_PAD(new ItemEnhancementRecoil(), "enhancement_recoil"),
        BOLT_ACTION(new ItemEnhancementSpeedy(), "enhancement_speedy"),
        STREAMLINED_BARREL(new ItemEnhancementFastRockets(), "enhancement_fast_rockets"),
        AIR_STRIKE_CONVERSION_KIT(new ItemEnhancementAirStrike(), "enhancement_air_strike"),
        EXTENDED_MAGAZINE(new ItemEnhancementAmmo(), "enhancement_ammo");

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
            if (item instanceof IEnhancement) {
                EnhancementRegistry.registerEnhancement((IEnhancement) item);
            }
            this.item = item;
        }

        public Item getItem() {
            return item;
        }

        public boolean isEnabled() {
            switch (this) {
                case BLAZE_BARREL: {
                    return Config.enableFirearms && Config.enableEnhancementAblaze;
                }
                case REVOLVER_CHAMBER: {
                    return Config.enableFirearms && Config.enableEnhancementRevolver;
                }
                case BREECH: {
                    return Config.enableFirearms && Config.enableEnhancementSpeedloader;
                }
                case MAKESHIFT_SUPPRESSOR: {
                    return Config.enableFirearms && Config.enableEnhancementSilencer;
                }
                case RECOIL_PAD: {
                    return Config.enableFirearms && Config.enableEnhancementRecoil;
                }
                case BOLT_ACTION: {
                    return Config.enableFirearms && Config.enableEnhancementSpeedy;
                }
                case STREAMLINED_BARREL: {
                    return Config.enableRL && Config.enableEnhancementFastRockets;
                }
                case AIR_STRIKE_CONVERSION_KIT: {
                    return Config.enableRL && Config.enableEnhancementAirStrike;
                }
                case EXTENDED_MAGAZINE: {
                    return Config.enableRL && Config.enableEnhancementAmmo;
                }
            }
            return false;
        }
    }

    @Override
    public void oreDict() {}

    @Override
    public void recipes() {
        for (Items item : Items.LOOKUP) {
            switch (item) {
                case BLAZE_BARREL: {
                    BookRecipeRegistry.addRecipe("ablaze", new ShapedOreRecipe(item.getItem(),
                      "rp ",
                      "pbp",
                      " pr",
                      'r', BLAZE_ROD,
                      'p', BLAZE_POWDER,
                      'b', CraftingComponentItems.Items.IRON_BARREL.createItemStack()
                    ));
                    break;
                }
                case REVOLVER_CHAMBER: {
                    BookRecipeRegistry.addRecipe("revolver", new ShapedOreRecipe(item.getItem(),
                      "ni ",
                      "iii",
                      " in",
                      'n', NUGGET_IRON,
                      'i', INGOT_IRON
                    ));
                    break;
                }
                case BREECH: {
                    BookRecipeRegistry.addRecipe("speedy", new ShapedOreRecipe(item.getItem(),
                      "iii",
                      "iii",
                      " n ",
                      'i', INGOT_IRON,
                      'n', NUGGET_IRON
                    ));
                    break;
                }
                case MAKESHIFT_SUPPRESSOR: {
                    BookRecipeRegistry.addRecipe("silencer", new ShapedOreRecipe(item.getItem(),
                      "wls",
                      "lll",
                      "slw",
                      'l', LEATHER,
                      'w', WOOL,
                      's', STRING
                    ));
                    break;
                }
                case RECOIL_PAD: {
                    BookRecipeRegistry.addRecipe("recoil", new ShapedOreRecipe(item.getItem(),
                      "ss ",
                      " ss",
                      "ss ",
                      's', SLIMEBALL_ORE
                    ));
                    break;
                }
                case BOLT_ACTION: {
                    BookRecipeRegistry.addRecipe("speedloader1", new ShapedOreRecipe(item.getItem(),
                      "  n",
                      "iii",
                      "iri",
                      'n', NUGGET_IRON,
                      'i', INGOT_IRON,
                      'r', DUST_REDSTONE
                    ));
                    BookRecipeRegistry.addRecipe("speedloader2", new ShapedOreRecipe(item.getItem(),
                      "  n",
                      "iii",
                      "iri",
                      'n', NUGGET_IRON,
                      'i', PLATE_THIN_IRON,
                      'r', DUST_REDSTONE
                    ));
                    break;
                }
                case STREAMLINED_BARREL: {
                    BookRecipeRegistry.addRecipe("fastRockets", new ShapedOreRecipe(item.getItem(),
                      "b  ",
                      "gid",
                      "  i",
                      'b', CraftingComponentItems.Items.BLUNDERBUSS_BARREL.createItemStack(),
                      'g', SteamNetworkBlocks.Blocks.STEAM_GAUGE.getBlock(),
                      'i', CraftingComponentItems.Items.IRON_BARREL.createItemStack(),
                      'd', SteamNetworkBlocks.Blocks.RUPTURE_DISC.getBlock()
                    ));
                    break;
                }
                case AIR_STRIKE_CONVERSION_KIT: {
                    BookRecipeRegistry.addRecipe("airStrike1", new ShapelessOreRecipe(item.getItem(),
                      INGOT_IRON, INGOT_IRON, PLANK_WOOD, PLANK_WOOD,
                      CraftingComponentItems.Items.FLINTLOCK.createItemStack()));
                    BookRecipeRegistry.addRecipe("airStrike2", new ShapelessOreRecipe(item.getItem(),
                      PLATE_THIN_IRON, PLATE_THIN_IRON, PLANK_WOOD, PLANK_WOOD,
                      CraftingComponentItems.Items.FLINTLOCK.createItemStack()));
                    break;
                }
                case EXTENDED_MAGAZINE: {
                    BookRecipeRegistry.addRecipe("ammo1", new ShapedOreRecipe(item.getItem(),
                      "icc",
                      "icc",
                      "cc ",
                      'i', NUGGET_IRON,
                      'c', INGOT_COPPER
                    ));
                    BookRecipeRegistry.addRecipe("ammo2", new ShapedOreRecipe(item.getItem(),
                      "icc",
                      "icc",
                      "cc ",
                      'i', NUGGET_IRON,
                      'c', PLATE_THIN_COPPER
                    ));
                    break;
                }
            }
        }
    }
}
