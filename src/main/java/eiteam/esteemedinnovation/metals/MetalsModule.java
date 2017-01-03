package eiteam.esteemedinnovation.metals;

import eiteam.esteemedinnovation.api.Constants;
import eiteam.esteemedinnovation.api.SmasherRegistry;
import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.api.crucible.CrucibleFormula;
import eiteam.esteemedinnovation.api.crucible.CrucibleLiquid;
import eiteam.esteemedinnovation.api.crucible.CrucibleRegistry;
import eiteam.esteemedinnovation.armor.ArmorModule;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import eiteam.esteemedinnovation.metals.raw.BlockGenericOre;
import eiteam.esteemedinnovation.metals.raw.BlockOreDepositGenerator;
import eiteam.esteemedinnovation.metals.raw.ExtraDimensionalOreGenerator;
import eiteam.esteemedinnovation.metals.raw.SurfaceOreGenerator;
import eiteam.esteemedinnovation.metals.refined.BlockBeacon;
import eiteam.esteemedinnovation.metals.refined.BlockBeacon.MetalBlockTypes;
import eiteam.esteemedinnovation.metals.refined.ItemMetalIngot;
import eiteam.esteemedinnovation.metals.refined.ItemMetalNugget;
import eiteam.esteemedinnovation.metals.refined.plates.BlockClassSensitivePlate;
import eiteam.esteemedinnovation.metals.refined.plates.BlockWeightedPlate;
import eiteam.esteemedinnovation.metals.refined.plates.ItemMetalPlate;
import eiteam.esteemedinnovation.misc.BlockManyMetadataItem;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static eiteam.esteemedinnovation.commons.EsteemedInnovation.BASICS_CATEGORY;
import static eiteam.esteemedinnovation.commons.OreDictEntries.*;
import static eiteam.esteemedinnovation.commons.EsteemedInnovation.CASTING_CATEGORY;
import static eiteam.esteemedinnovation.metals.raw.BlockGenericOre.OreBlockTypes.*;
import static eiteam.esteemedinnovation.metals.refined.ItemMetalIngot.Types.*;
import static eiteam.esteemedinnovation.metals.refined.ItemMetalNugget.Types.*;
import static eiteam.esteemedinnovation.metals.refined.plates.ItemMetalPlate.Types.*;
import static eiteam.esteemedinnovation.tools.ToolsModule.*;
import static eiteam.esteemedinnovation.transport.TransportationModule.BRASS_PIPE;
import static eiteam.esteemedinnovation.transport.TransportationModule.COPPER_PIPE;
import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;

public class MetalsModule extends ContentModule {
    public static final ResourceLocation WORKED_OUT_ORE_DEPOSIT_LOOTTABLE = new ResourceLocation(Constants.EI_MODID, "worked_out_ore_deposit");
    public static Block STORAGE_BLOCK;
    public static Block ORE_DEPOSIT_GENERATOR;
    public static Block ORE_BLOCK;
    public static Item METAL_INGOT;
    public static Item METAL_NUGGET;
    public static Item METAL_PLATE;
    public static CrucibleLiquid IRON_LIQUID;
    public static CrucibleLiquid GOLD_LIQUID;
    public static CrucibleLiquid ZINC_LIQUID;
    public static CrucibleLiquid COPPER_LIQUID;
    public static CrucibleLiquid BRASS_LIQUID;
    public static CrucibleLiquid LEAD_LIQUID;
    public static CrucibleFormula BRASS_FORMULA;
    public static Block COPPER_PRESSURE_PLATE;
    public static Block ZINC_PRESSURE_PLATE;
    public static Block BRASS_PRESSURE_PLATE;
    public static Block GILDED_IRON_PRESSURE_PLATE;
    public static Block IRON_PRESSURE_PLATE;
    public static Block GOLD_PRESSURE_PLATE;

    private static final List<Block> pressurePlatesByMetadata = new ArrayList<>();

    @Override
    public void create(Side side) {
        GameRegistry.registerWorldGenerator(new ExtraDimensionalOreGenerator(), 1);
        GameRegistry.registerWorldGenerator(new SurfaceOreGenerator(), 1);

        STORAGE_BLOCK = setup(new BlockBeacon(), "metal_storage_block", BlockManyMetadataItem::new);
        ORE_DEPOSIT_GENERATOR = setup(new BlockOreDepositGenerator(), "ore_deposit_generator", BlockManyMetadataItem::new);
        ORE_BLOCK = setup(new BlockGenericOre(), "ore", BlockManyMetadataItem::new);
        METAL_INGOT = setup(new ItemMetalIngot(), "ingot");
        METAL_NUGGET = setup(new ItemMetalNugget(), "nugget");
        METAL_PLATE = setup(new ItemMetalPlate(), "plate");

        IRON_LIQUID = new CrucibleLiquid("iron", new ItemStack(IRON_INGOT), new ItemStack(METAL_PLATE, 1, IRON_PLATE.getMeta()), new ItemStack(METAL_NUGGET, 1, IRON_NUGGET.getMeta()), 200, 200, 200);
        GOLD_LIQUID = new CrucibleLiquid("gold", new ItemStack(GOLD_INGOT), new ItemStack(METAL_PLATE, 1, GOLD_PLATE.getMeta()), new ItemStack(GOLD_NUGGET), 220, 157, 11);
        ZINC_LIQUID = new CrucibleLiquid("zinc", new ItemStack(METAL_INGOT, 1, ZINC_INGOT.getMeta()), new ItemStack(METAL_PLATE, 1, ZINC_PLATE.getMeta()), new ItemStack(METAL_NUGGET, 1, ZINC_NUGGET.getMeta()), 225, 225, 225);
        COPPER_LIQUID = new CrucibleLiquid("copper", new ItemStack(METAL_INGOT, 1, COPPER_INGOT.getMeta()), new ItemStack(METAL_PLATE, 1, COPPER_PLATE.getMeta()), new ItemStack(METAL_NUGGET, 1, COPPER_NUGGET.getMeta()), 140, 66, 12);
        BRASS_LIQUID = new CrucibleLiquid("brass", new ItemStack(METAL_INGOT, 1, BRASS_INGOT.getMeta()), new ItemStack(METAL_PLATE, 1, BRASS_PLATE.getMeta()), new ItemStack(METAL_NUGGET, 1, BRASS_NUGGET.getMeta()), 242, 191, 66);
        LEAD_LIQUID = new CrucibleLiquid("lead", findFirstOre(INGOT_LEAD), findFirstOre(PLATE_THIN_LEAD), findFirstOre(NUGGET_LEAD), 118, 128, 157);

        BRASS_FORMULA = new CrucibleFormula(BRASS_LIQUID, 4, ZINC_LIQUID, 1, COPPER_LIQUID, 3);

        COPPER_PRESSURE_PLATE = setup(new BlockClassSensitivePlate<>(COPPER_PLATE.getMeta(), EntityMob.class), "copper_pressure_plate", (Function<Block, ItemBlock>) null);
        pressurePlatesByMetadata.add(COPPER_PLATE.getMeta(), COPPER_PRESSURE_PLATE);
        ZINC_PRESSURE_PLATE = setup(new BlockClassSensitivePlate<>(ZINC_PLATE.getMeta(), EntityAgeable.class, EntityAgeable::isChild), "zinc_pressure_plate", (Function<Block, ItemBlock>) null);
        pressurePlatesByMetadata.add(ZINC_PLATE.getMeta(), ZINC_PRESSURE_PLATE);
        BRASS_PRESSURE_PLATE = setup(new BlockClassSensitivePlate<>(BRASS_PLATE.getMeta(), EntityAgeable.class, e -> !e.isChild()), "brass_pressure_plate", (Function<Block, ItemBlock>) null);
        pressurePlatesByMetadata.add(BRASS_PLATE.getMeta(), BRASS_PRESSURE_PLATE);
        // maxWeight values come from Block.registerBlocks
        GILDED_IRON_PRESSURE_PLATE = setup(new BlockWeightedPlate(150, GILDED_IRON_PLATE.getMeta()), "gilded_iron_pressure_plate", (Function<Block, ItemBlock>) null);
        pressurePlatesByMetadata.add(GILDED_IRON_PLATE.getMeta(), GILDED_IRON_PRESSURE_PLATE);
        IRON_PRESSURE_PLATE = setup(new BlockWeightedPlate(150, IRON_PLATE.getMeta()), "iron_pressure_plate", (Function<Block, ItemBlock>) null);
        pressurePlatesByMetadata.add(IRON_PLATE.getMeta(), IRON_PRESSURE_PLATE);
        GOLD_PRESSURE_PLATE = setup(new BlockWeightedPlate(15, GOLD_PLATE.getMeta()), "gold_pressure_plate", (Function<Block, ItemBlock>) null);
        pressurePlatesByMetadata.add(GOLD_PLATE.getMeta(), GOLD_PRESSURE_PLATE);
    }

    public static Block getPressurePlateFromItemMetadata(int meta) {
        return pressurePlatesByMetadata.get(meta);
    }

    private static ItemStack findFirstOre(String ore) {
        for (ItemStack stack : OreDictionary.getOres(ore)) {
            if (stack != null) {
                return stack;
            }
        }
        return null;
    }

    @Override
    public void oreDict(Side side) {
        for (MetalBlockTypes type : MetalBlockTypes.values()) {
            OreDictionary.registerOre(PREFIX_BLOCK + type.getOreMaterial(), new ItemStack(STORAGE_BLOCK, 1, type.getMetadata()));
        }
        for (BlockGenericOre.OreBlockTypes type : BlockGenericOre.OreBlockTypes.values()) {
            OreDictionary.registerOre(PREFIX_ORE + type.getOreMaterial(), new ItemStack(ORE_BLOCK, 1, type.getMetadata()));
        }
        OreDictionary.registerOre(INGOT_COPPER, new ItemStack(METAL_INGOT, 1, COPPER_INGOT.getMeta()));
        OreDictionary.registerOre(INGOT_ZINC, new ItemStack(METAL_INGOT, 1, ZINC_INGOT.getMeta()));
        OreDictionary.registerOre(INGOT_BRASS, new ItemStack(METAL_INGOT, 1, BRASS_INGOT.getMeta()));
        OreDictionary.registerOre(INGOT_GILDED_IRON, new ItemStack(METAL_INGOT, 1, GILDED_IRON_INGOT.getMeta()));

        OreDictionary.registerOre(NUGGET_COPPER, new ItemStack(METAL_NUGGET, 1, COPPER_NUGGET.getMeta()));
        OreDictionary.registerOre(NUGGET_ZINC, new ItemStack(METAL_NUGGET, 1, ZINC_NUGGET.getMeta()));
        OreDictionary.registerOre(NUGGET_BRASS, new ItemStack(METAL_NUGGET, 1, BRASS_NUGGET.getMeta()));
        OreDictionary.registerOre(NUGGET_GILDED_IRON, new ItemStack(METAL_NUGGET, 1, GILDED_IRON_NUGGET.getMeta()));
        OreDictionary.registerOre(NUGGET_IRON, new ItemStack(METAL_NUGGET, 1, IRON_NUGGET.getMeta()));

        OreDictionary.registerOre(PLATE_THIN_COPPER, new ItemStack(METAL_PLATE, 1, COPPER_PLATE.getMeta()));
        OreDictionary.registerOre(PLATE_THIN_ZINC, new ItemStack(METAL_PLATE, 1, ZINC_PLATE.getMeta()));
        OreDictionary.registerOre(PLATE_THIN_BRASS, new ItemStack(METAL_PLATE, 1, BRASS_PLATE.getMeta()));
        OreDictionary.registerOre(PLATE_THIN_GILDED_IRON, new ItemStack(METAL_PLATE, 1, GILDED_IRON_PLATE.getMeta()));
        OreDictionary.registerOre(PLATE_THIN_IRON, new ItemStack(METAL_PLATE, 1, IRON_PLATE.getMeta()));
        OreDictionary.registerOre(PLATE_THIN_GOLD, new ItemStack(METAL_PLATE, 1, GOLD_PLATE.getMeta()));
    }

    @Override
    public void recipes(Side side) {
        for (MetalBlockTypes type : MetalBlockTypes.values()) {
            add3x3Recipe(new ItemStack(STORAGE_BLOCK, 1, type.getMetadata()), PREFIX_INGOT + type.getOreMaterial());
        }

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(METAL_INGOT, 9, BRASS_INGOT.getMeta()), BLOCK_BRASS));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(METAL_INGOT, 9, COPPER_INGOT.getMeta()), BLOCK_COPPER));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(METAL_INGOT, 9, ZINC_INGOT.getMeta()), BLOCK_ZINC));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(METAL_INGOT, 9, GILDED_IRON_INGOT.getMeta()), BLOCK_GILDED_IRON));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(METAL_NUGGET, 9, COPPER_NUGGET.getMeta()), INGOT_COPPER));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(METAL_NUGGET, 9, ZINC_NUGGET.getMeta()), INGOT_ZINC));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(METAL_NUGGET, 9, IRON_NUGGET.getMeta()), INGOT_IRON));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(METAL_NUGGET, 9, BRASS_NUGGET.getMeta()), INGOT_BRASS));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(METAL_NUGGET, 9, GILDED_IRON_NUGGET.getMeta()), INGOT_GILDED_IRON));

        add3x3Recipe(new ItemStack(METAL_INGOT, 1, COPPER_INGOT.getMeta()), NUGGET_COPPER);
        add3x3Recipe(new ItemStack(METAL_INGOT, 1, ZINC_INGOT.getMeta()), NUGGET_ZINC);
        add3x3Recipe(new ItemStack(METAL_INGOT, 1, BRASS_INGOT.getMeta()), NUGGET_BRASS);
        add3x3Recipe(new ItemStack(METAL_INGOT, 1, GILDED_IRON_INGOT.getMeta()), NUGGET_GILDED_IRON);
        add3x3Recipe(new ItemStack(IRON_INGOT), NUGGET_IRON);

        GameRegistry.addSmelting(new ItemStack(ORE_BLOCK, 1, OVERWORLD_COPPER.getMetadata()), new ItemStack(METAL_INGOT, 1, COPPER_INGOT.getMeta()), 0.5F);
        GameRegistry.addSmelting(new ItemStack(ORE_BLOCK, 1, NETHER_COPPER.getMetadata()), new ItemStack(METAL_INGOT, 1, COPPER_INGOT.getMeta()), 0.5F);
        GameRegistry.addSmelting(new ItemStack(ORE_BLOCK, 1, END_COPPER.getMetadata()), new ItemStack(METAL_INGOT, 1, COPPER_INGOT.getMeta()), 0.5F);
        GameRegistry.addSmelting(new ItemStack(ORE_BLOCK, 1, OVERWORLD_ZINC.getMetadata()), new ItemStack(METAL_INGOT, 1, ZINC_INGOT.getMeta()), 0.5F);
        GameRegistry.addSmelting(new ItemStack(ORE_BLOCK, 1, NETHER_ZINC.getMetadata()), new ItemStack(METAL_INGOT, 1, ZINC_INGOT.getMeta()), 0.5F);
        GameRegistry.addSmelting(new ItemStack(ORE_BLOCK, 1, END_ZINC.getMetadata()), new ItemStack(METAL_INGOT, 1, ZINC_INGOT.getMeta()), 0.5F);

        SmasherRegistry.registerSmashable(COBBLESTONE_ORE, new ItemStack(GRAVEL));
        SmasherRegistry.registerSmashable(COBBLESTONE_WALL, new ItemStack(GRAVEL));
        SmasherRegistry.registerSmashable(GRAVEL_ORE, new ItemStack(SAND));
        SmasherRegistry.registerSmashable(GLOWSTONE_ORE, new ItemStack(GLOWSTONE_DUST, 4));
        SmasherRegistry.registerSmashable(SANDSTONE_ORE, new ItemStack(SAND));

        LootTableList.register(WORKED_OUT_ORE_DEPOSIT_LOOTTABLE);

        CrucibleRegistry.registerLiquid(IRON_LIQUID);
        CrucibleRegistry.registerLiquid(GOLD_LIQUID);
        CrucibleRegistry.registerLiquid(COPPER_LIQUID);
        CrucibleRegistry.registerLiquid(BRASS_LIQUID);
        CrucibleRegistry.registerLiquid(LEAD_LIQUID);

        CrucibleRegistry.registerFormula(BRASS_FORMULA);

        CrucibleRegistry.registerMeltRecipeOreDict(INGOT_GOLD, GOLD_LIQUID, 9);
        CrucibleRegistry.registerMeltRecipeOreDict(INGOT_IRON, IRON_LIQUID, 9);
        CrucibleRegistry.registerMeltRecipeOreDict(INGOT_ZINC, ZINC_LIQUID, 9);
        CrucibleRegistry.registerMeltRecipeOreDict(INGOT_COPPER, COPPER_LIQUID, 9);
        CrucibleRegistry.registerMeltRecipeOreDict(INGOT_BRASS, BRASS_LIQUID, 9);
        CrucibleRegistry.registerMeltRecipeOreDict(INGOT_LEAD, LEAD_LIQUID, 9);

        CrucibleRegistry.registerMeltRecipeOreDict(DUST_TINY_GOLD, GOLD_LIQUID, 1);
        CrucibleRegistry.registerMeltRecipeOreDict(DUST_TINY_IRON, IRON_LIQUID, 1);
        CrucibleRegistry.registerMeltRecipeOreDict(DUST_TINY_ZINC, ZINC_LIQUID, 1);
        CrucibleRegistry.registerMeltRecipeOreDict(DUST_TINY_COPPER, COPPER_LIQUID, 1);
        CrucibleRegistry.registerMeltRecipeOreDict(DUST_TINY_BRASS, BRASS_LIQUID, 1);
        CrucibleRegistry.registerMeltRecipeOreDict(DUST_TINY_LEAD, LEAD_LIQUID, 1);

        CrucibleRegistry.registerMeltRecipeOreDict(PLATE_THIN_GOLD, GOLD_LIQUID, 6);
        CrucibleRegistry.registerMeltRecipeOreDict(PLATE_THIN_IRON, IRON_LIQUID, 6);
        CrucibleRegistry.registerMeltRecipeOreDict(PLATE_THIN_ZINC, ZINC_LIQUID, 6);
        CrucibleRegistry.registerMeltRecipeOreDict(PLATE_THIN_COPPER, COPPER_LIQUID, 6);
        CrucibleRegistry.registerMeltRecipeOreDict(PLATE_THIN_BRASS, BRASS_LIQUID, 6);
        CrucibleRegistry.registerMeltRecipeOreDict(PLATE_THIN_LEAD, LEAD_LIQUID, 6);

        CrucibleRegistry.registerMeltRecipeOreDict(NUGGET_GOLD, GOLD_LIQUID, 1);
        CrucibleRegistry.registerMeltRecipeOreDict(NUGGET_IRON, IRON_LIQUID, 1);
        CrucibleRegistry.registerMeltRecipeOreDict(NUGGET_ZINC, ZINC_LIQUID, 1);
        CrucibleRegistry.registerMeltRecipeOreDict(NUGGET_COPPER, COPPER_LIQUID, 1);
        CrucibleRegistry.registerMeltRecipeOreDict(NUGGET_BRASS, BRASS_LIQUID, 1);
        CrucibleRegistry.registerMeltRecipeOreDict(NUGGET_LEAD, LEAD_LIQUID, 1);

        CrucibleRegistry.registerMeltRecipeOreDict(DUST_GOLD, GOLD_LIQUID, 9);
        CrucibleRegistry.registerMeltRecipeOreDict(DUST_IRON, IRON_LIQUID, 9);
        CrucibleRegistry.registerMeltRecipeOreDict(DUST_ZINC, ZINC_LIQUID, 9);
        CrucibleRegistry.registerMeltRecipeOreDict(DUST_COPPER, COPPER_LIQUID, 9);
        CrucibleRegistry.registerMeltRecipeOreDict(DUST_BRASS, BRASS_LIQUID, 9);
        CrucibleRegistry.registerMeltRecipeOreDict(DUST_LEAD, LEAD_LIQUID, 9);

        CrucibleRegistry.registerMeltRecipeTool(IRON_SWORD, IRON_LIQUID, 18);
        CrucibleRegistry.registerMeltRecipeTool(IRON_PICKAXE, IRON_LIQUID, 27);
        CrucibleRegistry.registerMeltRecipeTool(IRON_AXE, IRON_LIQUID, 27);
        CrucibleRegistry.registerMeltRecipeTool(IRON_HOE, IRON_LIQUID, 18);
        CrucibleRegistry.registerMeltRecipeTool(IRON_SHOVEL, IRON_LIQUID, 9);
        CrucibleRegistry.registerMeltRecipeTool(IRON_BOOTS, IRON_LIQUID, 36);
        CrucibleRegistry.registerMeltRecipeTool(IRON_CHESTPLATE, IRON_LIQUID, 72);
        CrucibleRegistry.registerMeltRecipeTool(IRON_HELMET, IRON_LIQUID, 45);
        CrucibleRegistry.registerMeltRecipeTool(IRON_LEGGINGS, IRON_LIQUID, 63);

        CrucibleRegistry.registerMeltRecipeTool(GOLDEN_SWORD, GOLD_LIQUID, 18);
        CrucibleRegistry.registerMeltRecipeTool(GOLDEN_PICKAXE, GOLD_LIQUID, 27);
        CrucibleRegistry.registerMeltRecipeTool(GOLDEN_AXE, GOLD_LIQUID, 27);
        CrucibleRegistry.registerMeltRecipeTool(GOLDEN_HOE, GOLD_LIQUID, 18);
        CrucibleRegistry.registerMeltRecipeTool(GOLDEN_SHOVEL, GOLD_LIQUID, 9);
        CrucibleRegistry.registerMeltRecipeTool(GOLDEN_BOOTS, GOLD_LIQUID, 36);
        CrucibleRegistry.registerMeltRecipeTool(GOLDEN_CHESTPLATE, GOLD_LIQUID, 72);
        CrucibleRegistry.registerMeltRecipeTool(GOLDEN_HELMET, GOLD_LIQUID, 45);
        CrucibleRegistry.registerMeltRecipeTool(GOLDEN_LEGGINGS, GOLD_LIQUID, 63);

        CrucibleRegistry.registerMeltRecipeTool(BRASS_SWORD, BRASS_LIQUID, 18);
        CrucibleRegistry.registerMeltRecipeTool(BRASS_PICKAXE, BRASS_LIQUID, 27);
        CrucibleRegistry.registerMeltRecipeTool(BRASS_AXE, BRASS_LIQUID, 27);
        CrucibleRegistry.registerMeltRecipeTool(BRASS_HOE, BRASS_LIQUID, 18);
        CrucibleRegistry.registerMeltRecipeTool(BRASS_SHOVEL, BRASS_LIQUID, 9);
        CrucibleRegistry.registerMeltRecipeTool(ArmorModule.BRASS_BOOTS, BRASS_LIQUID, 36);
        CrucibleRegistry.registerMeltRecipeTool(ArmorModule.BRASS_CHEST, BRASS_LIQUID, 72);
        CrucibleRegistry.registerMeltRecipeTool(ArmorModule.BRASS_HEAD, BRASS_LIQUID, 45);
        CrucibleRegistry.registerMeltRecipeTool(ArmorModule.BRASS_LEGS, BRASS_LIQUID, 63);

        CrucibleRegistry.registerOreDictDunkRecipe(INGOT_IRON, GOLD_LIQUID, 1, new ItemStack(METAL_INGOT, 1, GILDED_IRON_INGOT.getMeta()));
        CrucibleRegistry.registerOreDictDunkRecipe(PLATE_THIN_IRON, GOLD_LIQUID, 1, new ItemStack(METAL_PLATE, 1, GILDED_IRON_PLATE.getMeta()));
        CrucibleRegistry.registerOreDictDunkRecipe(NUGGET_IRON, GOLD_LIQUID, 1, new ItemStack(METAL_NUGGET, 1, GILDED_IRON_NUGGET.getMeta()));

        CrucibleRegistry.registerMeltRecipe(Item.getItemFromBlock(BRASS_PIPE), BRASS_LIQUID, 54);
        CrucibleRegistry.registerMeltRecipe(Item.getItemFromBlock(COPPER_PIPE), COPPER_LIQUID, 54);
    }

    @Override
    public void finish(Side side) {
        BookPageRegistry.addEntryToCategory(BASICS_CATEGORY, new BookEntry("research.Ores.name",
          new BookPageItem("research.Ores.name", "research.Ores.0",
            new ItemStack(ORE_BLOCK, 1, OVERWORLD_COPPER.getMetadata()),
            new ItemStack(ORE_BLOCK, 1, NETHER_COPPER.getMetadata()),
            new ItemStack(ORE_BLOCK, 1, END_COPPER.getMetadata()),
            new ItemStack(ORE_BLOCK, 1, OVERWORLD_ZINC.getMetadata()),
            new ItemStack(ORE_BLOCK, 1, NETHER_ZINC.getMetadata()),
            new ItemStack(ORE_BLOCK, 1, END_ZINC.getMetadata()))));

        BookPageRegistry.addEntryToCategory(CASTING_CATEGORY, new BookEntry("research.Plates.name",
          new BookPageItem("research.Plates.name", "research.Plates.0",
            Arrays.stream(ItemMetalPlate.Types.values()).map(type -> new ItemStack(METAL_PLATE, 1, type.getMeta())).collect(Collectors.toList()).toArray(new ItemStack[ItemMetalPlate.Types.values().length]))));
        BookPageRegistry.addEntryToCategory(CASTING_CATEGORY, new BookEntry("research.Brass.name",
          new BookPageItem("research.Brass.name", "research.Brass.0", new ItemStack(METAL_INGOT, 1, BRASS_INGOT.getMeta())),
          new BookPageAlloy("", BRASS_LIQUID, BRASS_FORMULA)));

        if (Config.enableCrucible) {
            BookPageRegistry.addEntryToCategory(CASTING_CATEGORY, new BookEntry("research.GildedGold.name",
              new BookPageItem("research.GildedGold.name", "research.GildedGold.0", new ItemStack(METAL_INGOT, 1, GILDED_IRON_INGOT.getMeta())),
              new BookPageText("research.GildedGold.name", "research.GildedGold.1"),
              new BookPageDip("", GOLD_LIQUID, 1, new ItemStack(IRON_INGOT), new ItemStack(METAL_INGOT, 1, GILDED_IRON_INGOT.getMeta()))));
        }
    }

    @Override
    public void preInitClient() {
        registerModelAllVariants(STORAGE_BLOCK, BlockBeacon.VARIANT.getName(), BlockBeacon.MetalBlockTypes.values());
        registerModelAllVariants(ORE_BLOCK, BlockGenericOre.VARIANT.getName(), BlockGenericOre.OreBlockTypes.LOOKUP);
        for (int i = 0; i < 4; i++) {
            String variant = "variant=" + (i % 2 == 0 ? "copper" : "zinc") + ",worked_out=" + (i > 1 ? "true" : "false");
            registerModel(ORE_DEPOSIT_GENERATOR, i, variant);
        }
        for (ItemMetalIngot.Types type : ItemMetalIngot.Types.values()) {
            registerModelItemStack(new ItemStack(METAL_INGOT, 1, type.getMeta()));
        }
        for (ItemMetalNugget.Types type : ItemMetalNugget.Types.values()) {
            registerModelItemStack(new ItemStack(METAL_NUGGET, 1, type.getMeta()));
        }
        for (ItemMetalPlate.Types type : ItemMetalPlate.Types.values()) {
            registerModelItemStack(new ItemStack(METAL_PLATE, 1, type.getMeta()));
        }
    }
}
