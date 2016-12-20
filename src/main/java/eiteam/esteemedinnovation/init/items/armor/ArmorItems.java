package eiteam.esteemedinnovation.init.items.armor;

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
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.api.book.BookRecipeRegistry;
import eiteam.esteemedinnovation.init.IInitCategory;
import eiteam.esteemedinnovation.init.blocks.SteamNetworkBlocks;
import eiteam.esteemedinnovation.init.items.CraftingComponentItems;
import eiteam.esteemedinnovation.init.items.tools.GadgetItems;
import eiteam.esteemedinnovation.armor.exosuit.ItemExosuitArmor;
import eiteam.esteemedinnovation.armor.ItemGenericArmor;
import eiteam.esteemedinnovation.armor.ItemGoggles;
import eiteam.esteemedinnovation.armor.tophat.ItemTophat;

import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;
import static eiteam.esteemedinnovation.init.misc.OreDictEntries.*;

public class ArmorItems implements IInitCategory {
    public enum Items {
        EXOSUIT_HEADPIECE(new ItemExosuitArmor(EntityEquipmentSlot.HEAD, Materials.EXOSUIT.getMaterial()), "exosuit_head", EsteemedInnovation.tab),
        EXOSUIT_CHESTPIECE(new ItemExosuitArmor(EntityEquipmentSlot.CHEST, Materials.EXOSUIT.getMaterial()), "exosuit_body", EsteemedInnovation.tab),
        EXOSUIT_LEGPIECE(new ItemExosuitArmor(EntityEquipmentSlot.LEGS, Materials.EXOSUIT.getMaterial()), "exosuit_legs", EsteemedInnovation.tab),
        EXOSUIT_FOOTPIECE(new ItemExosuitArmor(EntityEquipmentSlot.FEET, Materials.EXOSUIT.getMaterial()), "exosuit_feet", EsteemedInnovation.tab),
        GILDED_HELMET(new ItemGenericArmor(Materials.GILDED.getMaterial(), 2, EntityEquipmentSlot.HEAD, "ingotGildedIron", "GildedIron"), "gilded_iron_helmet", EsteemedInnovation.tabTools),
        GILDED_CHESTPLATE(new ItemGenericArmor(Materials.GILDED.getMaterial(), 2, EntityEquipmentSlot.CHEST, "ingotGildedIron", "GildedIron"), "gilded_iron_chestplate", EsteemedInnovation.tabTools),
        GILDED_LEGGINGS(new ItemGenericArmor(Materials.GILDED.getMaterial(), 2, EntityEquipmentSlot.LEGS, "ingotGildedIron", "GildedIron"), "gilded_iron_leggings", EsteemedInnovation.tabTools),
        GILDED_BOOTS(new ItemGenericArmor(Materials.GILDED.getMaterial(), 2, EntityEquipmentSlot.FEET, "ingotGildedIron", "GildedIron"), "gilded_iron_boots", EsteemedInnovation.tabTools),
        BRASS_HELMET(new ItemGenericArmor(Materials.BRASS.getMaterial(), 2, EntityEquipmentSlot.HEAD, "ingotBrass", "Brass"), "brass_helmet", EsteemedInnovation.tabTools),
        BRASS_CHESTPLATE(new ItemGenericArmor(Materials.BRASS.getMaterial(), 2, EntityEquipmentSlot.CHEST, "ingotBrass", "Brass"), "brass_chestplate", EsteemedInnovation.tabTools),
        BRASS_LEGGINGS(new ItemGenericArmor(Materials.BRASS.getMaterial(), 2, EntityEquipmentSlot.LEGS, "ingotBrass", "Brass"), "brass_leggings", EsteemedInnovation.tabTools),
        BRASS_BOOTS(new ItemGenericArmor(Materials.BRASS.getMaterial(), 2, EntityEquipmentSlot.FEET, "ingotBrass", "Brass"), "brass_boots", EsteemedInnovation.tabTools),
        MONOCLE(new ItemGoggles(Materials.MONOCLE.getMaterial(), 2, EntityEquipmentSlot.HEAD, LEATHER, "Monocle"), "monocle", EsteemedInnovation.tabTools),
        GOGGLES(new ItemGoggles(Materials.MONOCLE.getMaterial(), 2, EntityEquipmentSlot.HEAD, LEATHER, "Goggles"), "goggles", EsteemedInnovation.tabTools),
        TOP_HAT(new ItemTophat(Materials.MONOCLE.getMaterial(), 2, EntityEquipmentSlot.HEAD, false), "tophat_no_emerald", EsteemedInnovation.tabTools),
        ENTREPRENEUR_TOP_HAT(new ItemTophat(Materials.MONOCLE.getMaterial(), 2, EntityEquipmentSlot.HEAD, true), "tophat", EsteemedInnovation.tabTools);

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
            item.setUnlocalizedName(EsteemedInnovation.MOD_ID + ":" + name);
            item.setCreativeTab(tab);
            item.setRegistryName(EsteemedInnovation.MOD_ID, name);
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
        EXOSUIT(EnumHelper.addArmorMaterial("EXOSUIT", EsteemedInnovation.MOD_ID + ":exo", 15, new int[] { 2, 5, 4, 1 }, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0F)),
        GILDED(EnumHelper.addArmorMaterial("GILDEDGOLD", "minecraft:gold", 15, new int[] {2, 6, 5, 2 }, 9, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0F)),
        BRASS(EnumHelper.addArmorMaterial("BRASS", EsteemedInnovation.MOD_ID + ":brass", 11, new int[] { 2, 7, 6, 3 }, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0F)),
        MONOCLE(EnumHelper.addArmorMaterial("MONOCLE", EsteemedInnovation.MOD_ID + ":monocle", 5, new int[] { 1, 3, 2, 1 }, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0F));

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
                      'x', PLATE_THIN_BRASS,
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
                      'x', PLATE_THIN_BRASS,
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
                      'x', PLATE_THIN_BRASS,
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
                      'x', PLATE_THIN_BRASS,
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
                      'b', PLATE_THIN_BRASS,
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
                      'b', PLATE_THIN_BRASS,
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
