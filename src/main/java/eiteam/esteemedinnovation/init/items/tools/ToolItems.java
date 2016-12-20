package eiteam.esteemedinnovation.init.items.tools;

import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.api.book.BookRecipeRegistry;
import eiteam.esteemedinnovation.init.IInitCategory;
import eiteam.esteemedinnovation.init.blocks.PipeBlocks;
import eiteam.esteemedinnovation.init.items.CraftingComponentItems;
import eiteam.esteemedinnovation.tools.steam.ItemSteamAxe;
import eiteam.esteemedinnovation.tools.steam.ItemSteamDrill;
import eiteam.esteemedinnovation.tools.steam.ItemSteamShovel;
import eiteam.esteemedinnovation.tools.standard.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static eiteam.esteemedinnovation.init.misc.OreDictEntries.*;

public class ToolItems implements IInitCategory {
    public enum Items {
        STEAM_DRILL(new ItemSteamDrill(), "steam_drill"),
        STEAM_SAW(new ItemSteamAxe(), "steam_saw"),
        STEAM_SHOVEL(new ItemSteamShovel(), "steam_shovel"),
        BRASS_SWORD(new ItemGenericSword(Materials.BRASS.getMaterial(), INGOT_BRASS), "brass_sword"),
        BRASS_PICKAXE(new ItemGenericPickaxe(Materials.BRASS.getMaterial(), INGOT_BRASS), "brass_pickaxe"),
        BRASS_AXE(new ItemGenericAxe(Materials.BRASS.getMaterial(), INGOT_BRASS), "brass_axe"),
        BRASS_SHOVEL(new ItemGenericShovel(Materials.BRASS.getMaterial(), INGOT_BRASS), "brass_shovel"),
        BRASS_HOE(new ItemGenericHoe(Materials.BRASS.getMaterial(), INGOT_BRASS), "brass_hoe"),
        GILDED_IRON_SWORD(new ItemGenericSword(Materials.GILDED_IRON.getMaterial(), INGOT_GILDED_IRON), "gilded_iron_sword"),
        GILDED_IRON_PICKAXE(new ItemGenericPickaxe(Materials.GILDED_IRON.getMaterial(), INGOT_GILDED_IRON), "gilded_iron_pickaxe"),
        GILDED_IRON_AXE(new ItemGenericAxe(Materials.GILDED_IRON.getMaterial(), INGOT_GILDED_IRON), "gilded_iron_axe"),
        GILDED_IRON_SHOVEL(new ItemGenericShovel(Materials.GILDED_IRON.getMaterial(), INGOT_GILDED_IRON), "gilded_iron_shovel"),
        GILDED_IRON_HOE(new ItemGenericHoe(Materials.BRASS.getMaterial(), INGOT_GILDED_IRON), "gilded_iron_hoe");

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
            item.setCreativeTab(EsteemedInnovation.tabTools);
            item.setRegistryName(EsteemedInnovation.MOD_ID, name);
            GameRegistry.register(item);
            this.item = item;
        }

        public boolean isEnabled() {
            return !(this == STEAM_DRILL || this == STEAM_SAW || this == STEAM_SHOVEL) || Config.enableSteamTools;
        }

        public Item getItem() {
            return item;
        }
    }

    public enum Materials {
        GILDED_IRON(EnumHelper.addToolMaterial("GILDEDGOLD", 2, 250, 6.0F, 2.0F, 22)),
        BRASS(EnumHelper.addToolMaterial("BRASS_LIQUID", 2, 191, 7.0F, 2.5F, 14)),
        STEAM_DRILL(EnumHelper.addToolMaterial("STEAMDRILL", 2, 320, 1.0F, -1.0F, 0)),
        STEAM_SAW(EnumHelper.addToolMaterial("STEAMSAW", 2, 320, 1.0F, -1.0F, 0)),
        STEAM_SHOVEL(EnumHelper.addToolMaterial("STEAMSHOVEL", 2, 320, 1.0F, -1.0F, 0));

        private Item.ToolMaterial material;

        Materials(Item.ToolMaterial material) {
            this.material = material;
        }
        public Item.ToolMaterial getMaterial() {
            return material;
        }
    }

    @Override
    public void oreDict() {}

    @Override
    public void recipes() {
        for (Items item : Items.LOOKUP) {
            switch (item) {
                case STEAM_DRILL: {
                    ItemStack out = new ItemStack(item.getItem(), 1, item.getItem().getMaxDamage() - 1);
                    BookRecipeRegistry.addRecipe("drill1", new ShapedOreRecipe(out,
                      "xii",
                      "pti",
                      "xpx",
                      'x', INGOT_BRASS,
                      'i', INGOT_IRON,
                      'p', PipeBlocks.Blocks.BRASS_PIPE.getBlock(),
                      't', CraftingComponentItems.Items.BRASS_TURBINE.createItemStack()
                    ));
                    BookRecipeRegistry.addRecipe("drill2", new ShapedOreRecipe(out,
                      "xii",
                      "pti",
                      "xpx",
                      'x', PLATE_THIN_BRASS,
                      'i', INGOT_IRON,
                      'p', PipeBlocks.Blocks.BRASS_PIPE.getBlock(),
                      't', CraftingComponentItems.Items.BRASS_TURBINE.createItemStack()
                    ));
                    BookRecipeRegistry.addRecipe("drill3", new ShapedOreRecipe(out,
                      "xii",
                      "pti",
                      "xpx",
                      'x', INGOT_BRASS,
                      'i', PLATE_THIN_IRON,
                      'p', PipeBlocks.Blocks.BRASS_PIPE.getBlock(),
                      't', CraftingComponentItems.Items.BRASS_TURBINE.createItemStack()
                    ));
                    BookRecipeRegistry.addRecipe("drill4", new ShapedOreRecipe(out,
                      "xii",
                      "pti",
                      "xpx",
                      'x', PLATE_THIN_BRASS,
                      'i', PLATE_THIN_IRON,
                      'p', PipeBlocks.Blocks.BRASS_PIPE.getBlock(),
                      't', CraftingComponentItems.Items.BRASS_TURBINE.createItemStack()
                    ));
                    break;
                }
                case STEAM_SAW: {
                    ItemStack out = new ItemStack(item.getItem(), 1, item.getItem().getMaxDamage() - 1);
                    BookRecipeRegistry.addRecipe("axe1", new ShapedOreRecipe(out,
                      "ini",
                      "ptn",
                      "xpi",
                      'x', INGOT_BRASS,
                      'i', INGOT_IRON,
                      'p', PipeBlocks.Blocks.BRASS_PIPE.getBlock(),
                      't', CraftingComponentItems.Items.BRASS_TURBINE.createItemStack(),
                      'n', NUGGET_IRON
                    ));
                    BookRecipeRegistry.addRecipe("axe2", new ShapedOreRecipe(out,
                      "ini",
                      "ptn",
                      "xpi",
                      'x', PLATE_THIN_BRASS,
                      'i', INGOT_IRON,
                      'p', PipeBlocks.Blocks.BRASS_PIPE.getBlock(),
                      't', CraftingComponentItems.Items.BRASS_TURBINE.createItemStack(),
                      'n', NUGGET_IRON
                    ));
                    BookRecipeRegistry.addRecipe("axe3", new ShapedOreRecipe(out,
                      "ini",
                      "ptn",
                      "xpi",
                      'x', INGOT_BRASS,
                      'i', PLATE_THIN_IRON,
                      'p', PipeBlocks.Blocks.BRASS_PIPE.getBlock(),
                      't', CraftingComponentItems.Items.BRASS_TURBINE.createItemStack(),
                      'n', NUGGET_IRON
                    ));
                    BookRecipeRegistry.addRecipe("axe4", new ShapedOreRecipe(out,
                      "ini",
                      "ptn",
                      "xpi",
                      'x', PLATE_THIN_BRASS,
                      'i', PLATE_THIN_IRON,
                      'p', PipeBlocks.Blocks.BRASS_PIPE.getBlock(),
                      't', CraftingComponentItems.Items.BRASS_TURBINE.createItemStack(),
                      'n', NUGGET_IRON
                    ));
                    break;
                }
                case STEAM_SHOVEL: {
                    ItemStack out = new ItemStack(item.getItem(), 1, item.getItem().getMaxDamage() - 1);
                    BookRecipeRegistry.addRecipe("shovel1", new ShapedOreRecipe(out,
                      "ixi",
                      "ptx",
                      "xpi",
                      'x', INGOT_BRASS,
                      'i', INGOT_IRON,
                      'p', PipeBlocks.Blocks.BRASS_PIPE.getBlock(),
                      't', CraftingComponentItems.Items.BRASS_TURBINE.createItemStack()
                    ));
                    BookRecipeRegistry.addRecipe("shovel2", new ShapedOreRecipe(out,
                      "ixi",
                      "ptx",
                      "xpi",
                      'x', PLATE_THIN_BRASS,
                      'i', INGOT_IRON,
                      'p', PipeBlocks.Blocks.BRASS_PIPE.getBlock(),
                      't', CraftingComponentItems.Items.BRASS_TURBINE.createItemStack()
                    ));
                    BookRecipeRegistry.addRecipe("shovel3", new ShapedOreRecipe(out,
                      "ixi",
                      "ptx",
                      "xpi",
                      'x', INGOT_BRASS,
                      'i', PLATE_THIN_IRON,
                      'p', PipeBlocks.Blocks.BRASS_PIPE.getBlock(),
                      't', CraftingComponentItems.Items.BRASS_TURBINE.createItemStack()
                    ));
                    BookRecipeRegistry.addRecipe("shovel4", new ShapedOreRecipe(out,
                      "ixi",
                      "ptx",
                      "xpi",
                      'x', PLATE_THIN_BRASS,
                      'i', PLATE_THIN_IRON,
                      'p', PipeBlocks.Blocks.BRASS_PIPE.getBlock(),
                      't', CraftingComponentItems.Items.BRASS_TURBINE.createItemStack()
                    ));
                    break;
                }
                case BRASS_AXE: {
                    addAxeRecipe(item.getItem(), INGOT_BRASS);
                    break;
                }
                case BRASS_PICKAXE: {
                    addPickaxeRecipe(item.getItem(), INGOT_BRASS);
                    break;
                }
                case BRASS_SHOVEL: {
                    addShovelRecipe(item.getItem(), INGOT_BRASS);
                    break;
                }
                case BRASS_SWORD: {
                    addSwordRecipe(item.getItem(), INGOT_BRASS);
                    break;
                }
                case GILDED_IRON_AXE: {
                    addAxeRecipe(item.getItem(), INGOT_GILDED_IRON);
                    break;
                }
                case GILDED_IRON_PICKAXE: {
                    addPickaxeRecipe(item.getItem(), INGOT_GILDED_IRON);
                    break;
                }
                case GILDED_IRON_SHOVEL: {
                    addShovelRecipe(item.getItem(), INGOT_GILDED_IRON);
                    break;
                }
                case GILDED_IRON_SWORD: {
                    addSwordRecipe(item.getItem(), INGOT_GILDED_IRON);
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    private void addAxeRecipe(Item out, String material) {
        addAxeRecipe(new ItemStack(out), material);
    }

    private void addAxeRecipe(ItemStack out, String material) {
        GameRegistry.addRecipe(new ShapedOreRecipe(out,
          "xx",
          "xs",
          " s",
          'x', material,
          's', STICK_WOOD
        ));
    }

    private void addPickaxeRecipe(Item out, String material) {
        addPickaxeRecipe(new ItemStack(out), material);
    }

    private void addPickaxeRecipe(ItemStack out, String material) {
        GameRegistry.addRecipe(new ShapedOreRecipe(out,
          "xxx",
          " s ",
          " s ",
          'x', material,
          's', STICK_WOOD
        ));
    }

    private void addShovelRecipe(Item out, String material) {
        addShovelRecipe(new ItemStack(out), material);
    }

    private void addShovelRecipe(ItemStack out, String material) {
        GameRegistry.addRecipe(new ShapedOreRecipe(out,
          "x",
          "s",
          "s",
          'x', material,
          's', STICK_WOOD
        ));
    }

    private void addSwordRecipe(Item out, String material) {
        addSwordRecipe(new ItemStack(out), material);
    }

    private void addSwordRecipe(ItemStack out, String material) {
        GameRegistry.addRecipe(new ShapedOreRecipe(out,
          "x",
          "x",
          "s",
          'x', material,
          's', STICK_WOOD
        ));
    }
}
