package flaxbeard.steamcraft.init.items.armor;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.book.BookRecipeRegistry;
import flaxbeard.steamcraft.init.IInitCategory;
import flaxbeard.steamcraft.init.blocks.SteamNetworkBlocks;
import flaxbeard.steamcraft.init.items.CraftingComponentItems;
import flaxbeard.steamcraft.init.items.tools.GadgetItems;
import flaxbeard.steamcraft.item.armor.exosuit.ItemExosuitArmor;
import flaxbeard.steamcraft.item.armor.ItemSteamcraftArmor;
import flaxbeard.steamcraft.item.armor.ItemSteamcraftGoggles;
import flaxbeard.steamcraft.item.armor.ItemTophat;

import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;
import static flaxbeard.steamcraft.init.misc.OreDictEntries.*;

public class ArmorItems implements IInitCategory {
    public enum Items {
        EXOSUIT_HEADPIECE(new ItemExosuitArmor(EntityEquipmentSlot.HEAD, Materials.EXOSUIT.getMaterial()), "exoArmorHead", Steamcraft.tab),
        EXOSUIT_CHESTPIECE(new ItemExosuitArmor(EntityEquipmentSlot.CHEST, Materials.EXOSUIT.getMaterial()), "exoArmorBody", Steamcraft.tab),
        EXOSUIT_LEGPIECE(new ItemExosuitArmor(EntityEquipmentSlot.LEGS, Materials.EXOSUIT.getMaterial()), "exoArmorLegs", Steamcraft.tab),
        EXOSUIT_FOOTPIECE(new ItemExosuitArmor(EntityEquipmentSlot.FEET, Materials.EXOSUIT.getMaterial()), "exoArmorFeet", Steamcraft.tab),
        GILDED_HELMET(new ItemSteamcraftArmor(Materials.GILDED.getMaterial(), 2, EntityEquipmentSlot.HEAD, "ingotGildedIron", "GildedIron"), "helmetGildedIron", Steamcraft.tabTools),
        GILDED_CHESTPLATE(new ItemSteamcraftArmor(Materials.GILDED.getMaterial(), 2, EntityEquipmentSlot.CHEST, "ingotGildedIron", "GildedIron"), "chestGildedIron", Steamcraft.tabTools),
        GILDED_LEGGINGS(new ItemSteamcraftArmor(Materials.GILDED.getMaterial(), 2, EntityEquipmentSlot.LEGS, "ingotGildedIron", "GildedIron"), "legsGildedIron", Steamcraft.tabTools),
        GILDED_BOOTS(new ItemSteamcraftArmor(Materials.GILDED.getMaterial(), 2, EntityEquipmentSlot.FEET, "ingotGildedIron", "GildedIron"), "feetGildedIron", Steamcraft.tabTools),
        BRASS_HELMET(new ItemSteamcraftArmor(Materials.BRASS.getMaterial(), 2, EntityEquipmentSlot.HEAD, "ingotBrass", "Brass"), "helmetBrass", Steamcraft.tabTools),
        BRASS_CHESTPLATE(new ItemSteamcraftArmor(Materials.BRASS.getMaterial(), 2, EntityEquipmentSlot.CHEST, "ingotBrass", "Brass"), "chestBrass", Steamcraft.tabTools),
        BRASS_LEGGINGS(new ItemSteamcraftArmor(Materials.BRASS.getMaterial(), 2, EntityEquipmentSlot.LEGS, "ingotBrass", "Brass"), "legsBrass", Steamcraft.tabTools),
        BRASS_BOOTS(new ItemSteamcraftArmor(Materials.BRASS.getMaterial(), 2, EntityEquipmentSlot.FEET, "ingotBrass", "Brass"), "feetBrass", Steamcraft.tabTools),
        MONOCLE(new ItemSteamcraftGoggles(Materials.MONOCLE.getMaterial(), 2, EntityEquipmentSlot.HEAD, LEATHER, "Monocle"), "monacle", Steamcraft.tabTools),
        GOGGLES(new ItemSteamcraftGoggles(Materials.MONOCLE.getMaterial(), 2, EntityEquipmentSlot.HEAD, LEATHER, "Goggles"), "goggles", Steamcraft.tabTools),
        TOP_HAT(new ItemTophat(Materials.MONOCLE.getMaterial(), 2, EntityEquipmentSlot.HEAD, false), "tophatNoEmerald", Steamcraft.tabTools),
        ENTREPRENEUR_TOP_HAT(new ItemTophat(Materials.MONOCLE.getMaterial(), 2, EntityEquipmentSlot.HEAD, true), "tophat", Steamcraft.tabTools);

        private Item item;

        public static Items[] LOOKUP = new Items[values().length];

        static {
            for (Items item : values()) {
                if (item.isEnabled()) {
                    LOOKUP[item.ordinal()] = item;
                }
            }
        }

        Items(Item item, String name, CreativeTabs tab) {
            item.setUnlocalizedName(Steamcraft.MOD_ID + ":" + name);
            item.setCreativeTab(tab);
            item.setRegistryName(Steamcraft.MOD_ID, name);
            GameRegistry.register(item);
            this.item = item;
        }

        public boolean isEnabled() {
            Item me = getItem();
            if (me == MONOCLE.getItem() || me == GOGGLES.getItem()) {
                return Config.enableGoggles;
            } else if (me == TOP_HAT.getItem()) {
                return Config.enableTopHat;
            } else if (me == ENTREPRENEUR_TOP_HAT.getItem()) {
                return TOP_HAT.isEnabled() && Config.enableEmeraldHat;
            }
            return !(me instanceof ItemExosuitArmor) || Config.enableExosuit;
        }

        public Item getItem() {
            return item;
        }
    }

    public enum Materials {
        EXOSUIT(EnumHelper.addArmorMaterial("EXOSUIT", Steamcraft.MOD_ID + ":exo", 15, new int[]{2, 5, 4, 1}, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0F)),
        GILDED(EnumHelper.addArmorMaterial("GILDEDGOLD", "minecraft:gold", 15, new int[]{2, 6, 5, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0F)),
        BRASS(EnumHelper.addArmorMaterial("BRASS", Steamcraft.MOD_ID + ":brass", 11, new int[]{2, 7, 6, 3}, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0F)),
        MONOCLE(EnumHelper.addArmorMaterial("MONOCLE", Steamcraft.MOD_ID + ":monacle", 5, new int[]{1, 3, 2, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0F));

        private ItemArmor.ArmorMaterial material;

        Materials(ItemArmor.ArmorMaterial material) {
            this.material = material;
        }

        public ItemArmor.ArmorMaterial getMaterial() {
            return material;
        }
    }

    @Override
    public void oreDict() {}

    @Override
    public void recipes() {
        for (Items item : Items.LOOKUP) {
            switch (item) {
                case BRASS_BOOTS: {
                    addFootRecipe(item.getItem(), INGOT_BRASS);
                    break;
                }
                case BRASS_CHESTPLATE: {
                    addChestRecipe(item.getItem(), INGOT_BRASS);
                    break;
                }
                case BRASS_HELMET: {
                    addHelmetRecipe(item.getItem(), INGOT_BRASS);
                    break;
                }
                case BRASS_LEGGINGS: {
                    addLegRecipe(item.getItem(), INGOT_BRASS);
                    break;
                }
                case GILDED_BOOTS: {
                    addFootRecipe(item.getItem(), INGOT_GILDED_IRON);
                    break;
                }
                case GILDED_CHESTPLATE: {
                    addChestRecipe(item.getItem(), INGOT_GILDED_IRON);
                    break;
                }
                case GILDED_HELMET: {
                    addHelmetRecipe(item.getItem(), INGOT_GILDED_IRON);
                    break;
                }
                case GILDED_LEGGINGS: {
                    addLegRecipe(item.getItem(), INGOT_GILDED_IRON);
                    break;
                }
                case EXOSUIT_CHESTPIECE: {
                    BookRecipeRegistry.addRecipe("exoBody", new ShapedOreRecipe(
                      new ItemStack(item.getItem(), 1, item.getItem().getMaxDamage() - 1),
                      "p p",
                      "ygy",
                      "xxx",
                      'x', PLATE_BRASS,
                      'y', NUGGET_BRASS,
                      'g', SteamNetworkBlocks.Blocks.STEAM_GAUGE.getBlock(),
                      'p', CraftingComponentItems.Items.BRASS_PISTON.createItemStack()
                    ));
                    break;
                }
                case EXOSUIT_FOOTPIECE: {
                    BookRecipeRegistry.addRecipe("exoFeet", new ShapedOreRecipe(item.getItem(),
                      "p p",
                      "x x",
                      'x', PLATE_BRASS,
                      'y', NUGGET_BRASS,
                      'p', CraftingComponentItems.Items.BRASS_PISTON.createItemStack()
                    ));
                    break;
                }
                case EXOSUIT_HEADPIECE: {
                    BookRecipeRegistry.addRecipe("exoHead", new ShapedOreRecipe(item.getItem(),
                      "xyx",
                      "p p",
                      "xyx",
                      'x', PLATE_BRASS,
                      'y', NUGGET_BRASS,
                      'p', CraftingComponentItems.Items.BRASS_PISTON.createItemStack()
                    ));
                    break;
                }
                case EXOSUIT_LEGPIECE: {
                    BookRecipeRegistry.addRecipe("exoLegs", new ShapedOreRecipe(item.getItem(),
                      "yxy",
                      "p p",
                      "x x",
                      'x', PLATE_BRASS,
                      'y', NUGGET_BRASS,
                      'p', CraftingComponentItems.Items.BRASS_PISTON.createItemStack()
                    ));
                    break;
                }
                case MONOCLE: {
                    BookRecipeRegistry.addRecipe("monocle1", new ShapedOreRecipe(item.getItem(),
                      " l ",
                      "l l",
                      "btb",
                      'b', INGOT_BRASS,
                      'l', LEATHER_ORE,
                      't', GadgetItems.Items.SPYGLASS.getItem()
                    ));
                    BookRecipeRegistry.addRecipe("monocle2", new ShapedOreRecipe(item.getItem(),
                      " l ",
                      "l l",
                      "btb",
                      'b', PLATE_BRASS,
                      'l', LEATHER_ORE,
                      't', GadgetItems.Items.SPYGLASS.getItem()
                    ));
                    break;
                }
                case GOGGLES: {
                    BookRecipeRegistry.addRecipe("goggles1", new ShapedOreRecipe(item.getItem(),
                      " l ",
                      "l l",
                      "tbg",
                      'b', INGOT_BRASS,
                      'l', LEATHER_ORE,
                      't', GadgetItems.Items.SPYGLASS.getItem(),
                      'g', BLOCK_GLASS_COLORLESS
                    ));
                    BookRecipeRegistry.addRecipe("goggles2", new ShapedOreRecipe(item.getItem(),
                      " l ",
                      "l l",
                      "tbg",
                      'b', PLATE_BRASS,
                      'l', LEATHER_ORE,
                      't', GadgetItems.Items.SPYGLASS.getItem(),
                      'g', BLOCK_GLASS_COLORLESS
                    ));
                    break;
                }
                case TOP_HAT: {
                    BookRecipeRegistry.addRecipe("hat", new ShapedOreRecipe(item.getItem(),
                      " l ",
                      " l ",
                      "lll",
                      'e', GEM_EMERALD,
                      'l', new ItemStack(WOOL, 1, EnumDyeColor.BLACK.getMetadata())
                    ));
                    break;
                }
                case ENTREPRENEUR_TOP_HAT: {
                    BookRecipeRegistry.addRecipe("hatEmerald", new ShapelessOreRecipe(
                      item.getItem(), Items.TOP_HAT.getItem(), BLOCK_EMERALD));
                    break;
                }
            }
        }
    }

    private void addLegRecipe(Item item, String ore) {
        GameRegistry.addRecipe(new ShapedOreRecipe(item,
          "xxx",
          "x x",
          "x x",
          'x', ore
        ));
    }

    private void addHelmetRecipe(Item item, String ore) {
        GameRegistry.addRecipe(new ShapedOreRecipe(item,
          "xxx",
          "x x",
          'x', ore
        ));
    }

    private void addChestRecipe(Item item, String ore) {
        GameRegistry.addRecipe(new ShapedOreRecipe(item,
          "x x",
          "xxx",
          "xxx",
          'x', ore
        ));
    }

    private void addFootRecipe(Item item, String ore) {
        GameRegistry.addRecipe(new ShapedOreRecipe(item,
          "x x",
          "x x",
          'x', ore
        ));
    }
}
