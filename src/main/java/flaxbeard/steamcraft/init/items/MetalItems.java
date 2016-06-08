package flaxbeard.steamcraft.init.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.init.IInitCategory;
import flaxbeard.steamcraft.init.blocks.OreBlocks;
import flaxbeard.steamcraft.item.ItemSmashedOre;
import flaxbeard.steamcraft.item.ItemSteamcraftIngot;
import flaxbeard.steamcraft.item.ItemSteamcraftNugget;
import flaxbeard.steamcraft.item.ItemSteamcraftPlate;

import static net.minecraft.init.Items.IRON_INGOT;
import static net.minecraft.init.Items.GLOWSTONE_DUST;
import static net.minecraft.init.Blocks.COBBLESTONE_WALL;
import static net.minecraft.init.Blocks.GRAVEL;
import static net.minecraft.init.Blocks.SAND;
import static flaxbeard.steamcraft.init.misc.OreDictEntries.*;
import static flaxbeard.steamcraft.init.misc.CraftingHelpers.add3x3Recipe;
import static flaxbeard.steamcraft.tile.TileEntitySmasher.REGISTRY;

public class MetalItems implements IInitCategory {
    public enum Items {
        // TODO Lead
        COPPER_INGOT(0, Types.INGOT),
        ZINC_INGOT(1, Types.INGOT),
        BRASS_INGOT(2, Types.INGOT),
        GILDED_IRON_INGOT(3, Types.INGOT),
        COPPER_NUGGET(0, Types.NUGGET),
        ZINC_NUGGET(1, Types.NUGGET),
        BRASS_NUGGET(2, Types.NUGGET),
        GILDED_IRON_NUGGET(3, Types.NUGGET),
        IRON_NUGGET(4, Types.NUGGET), // I hate you, Vanilla, and your lack of iron nuggets.
        COPPER_PLATE(0, Types.PLATE),
        ZINC_PLATE(1, Types.PLATE),
        BRASS_PLATE(2, Types.PLATE),
        GILDED_IRON_PLATE(3, Types.PLATE),
        IRON_PLATE(4, Types.PLATE),
        GOLD_PLATE(5, Types.PLATE),
        SMASHED_ORE(new ItemSmashedOre(), "smashedOre");

        private Types type;
        private int metadata;
        private Item item;
        private boolean hasType;

        Items(int metadata, Types type) {
            this.metadata = metadata;
            this.type = type;
            hasType = true;
        }

        Items(Item item, String name) {
            item.setCreativeTab(Steamcraft.tab);
            item.setUnlocalizedName(Steamcraft.MOD_ID + ":" + name);
            item.setRegistryName(Steamcraft.MOD_ID, name);
            GameRegistry.register(item);
            this.item = item;
            hasType = false;
        }

        public ItemStack createItemStack() {
            return createItemStack(1);
        }

        public ItemStack createItemStack(int size) {
            // Currently, the only item with no Types set is SMASHED_ORE, whose meta is only accessible from ItemSmashedOre.
            return hasType() ? new ItemStack(getItem(), size, metadata) : new ItemStack(getItem(), size);
        }

        public enum Types {
            INGOT(new ItemSteamcraftIngot(), "ingot"),
            NUGGET(new ItemSteamcraftNugget(), "nugget"),
            PLATE(new ItemSteamcraftPlate(), "plate");

            private Item item;

            Types(Item item, String name) {
                item.setUnlocalizedName(Steamcraft.MOD_ID + ":" + name);
                item.setRegistryName(Steamcraft.MOD_ID, name);
                item.setCreativeTab(Steamcraft.tab);
                GameRegistry.register(item);
                this.item = item;
            }

            public Item getItem() {
                return item;
            }
        }

        public Item getItem() {
            return hasType() ? type.getItem() : item;
        }

        public boolean hasType() {
            return hasType;
        }
    }

    @Override
    public void oreDict() {
        OreDictionary.registerOre(INGOT_COPPER, Items.COPPER_INGOT.createItemStack());
        OreDictionary.registerOre(INGOT_ZINC, Items.ZINC_INGOT.createItemStack());
        OreDictionary.registerOre(INGOT_BRASS, Items.BRASS_INGOT.createItemStack());
        OreDictionary.registerOre(INGOT_GILDED_IRON, Items.GILDED_IRON_INGOT.createItemStack());

        OreDictionary.registerOre(NUGGET_COPPER, Items.COPPER_NUGGET.createItemStack());
        OreDictionary.registerOre(NUGGET_ZINC, Items.ZINC_NUGGET.createItemStack());
        OreDictionary.registerOre(NUGGET_BRASS, Items.BRASS_NUGGET.createItemStack());
        OreDictionary.registerOre(NUGGET_GILDED_IRON, Items.GILDED_IRON_NUGGET.createItemStack());
        OreDictionary.registerOre(NUGGET_IRON, Items.IRON_NUGGET.createItemStack());

        OreDictionary.registerOre(PLATE_COPPER, Items.COPPER_PLATE.createItemStack());
        OreDictionary.registerOre(PLATE_ZINC, Items.ZINC_PLATE.createItemStack());
        OreDictionary.registerOre(PLATE_BRASS, Items.BRASS_PLATE.createItemStack());
        OreDictionary.registerOre(PLATE_GILDED_IRON, Items.GILDED_IRON_PLATE.createItemStack());
        OreDictionary.registerOre(PLATE_IRON, Items.IRON_PLATE.createItemStack());
        OreDictionary.registerOre(PLATE_GOLD, Items.GOLD_PLATE.createItemStack());
    }

    @Override
    public void recipes() {
        GameRegistry.addRecipe(new ShapelessOreRecipe(Items.BRASS_INGOT.createItemStack(9), BLOCK_BRASS));
        GameRegistry.addRecipe(new ShapelessOreRecipe(Items.COPPER_INGOT.createItemStack(9),BLOCK_COPPER));
        GameRegistry.addRecipe(new ShapelessOreRecipe(Items.ZINC_INGOT.createItemStack(9), BLOCK_ZINC));
        GameRegistry.addRecipe(new ShapelessOreRecipe(Items.GILDED_IRON_INGOT.createItemStack(9), BLOCK_GILDED_IRON));

        GameRegistry.addRecipe(new ShapelessOreRecipe(Items.COPPER_NUGGET.createItemStack(9), INGOT_COPPER));
        GameRegistry.addRecipe(new ShapelessOreRecipe(Items.ZINC_NUGGET.createItemStack(9), INGOT_ZINC));
        GameRegistry.addRecipe(new ShapelessOreRecipe(Items.IRON_NUGGET.createItemStack(9), INGOT_IRON));
        GameRegistry.addRecipe(new ShapelessOreRecipe(Items.BRASS_NUGGET.createItemStack(9), INGOT_BRASS));
        GameRegistry.addRecipe(new ShapelessOreRecipe(Items.GILDED_IRON_NUGGET.createItemStack(9), INGOT_GILDED_IRON));

        add3x3Recipe(Items.COPPER_INGOT.createItemStack(), NUGGET_COPPER);
        add3x3Recipe(Items.ZINC_INGOT.createItemStack(), NUGGET_ZINC);
        add3x3Recipe(Items.BRASS_INGOT.createItemStack(), NUGGET_BRASS);
        add3x3Recipe(Items.GILDED_IRON_INGOT.createItemStack(), NUGGET_GILDED_IRON);
        add3x3Recipe(new ItemStack(IRON_INGOT), NUGGET_IRON);

        GameRegistry.addSmelting(OreBlocks.Blocks.OVERWORLD_COPPER_ORE.createItemStack(), Items.COPPER_INGOT.createItemStack(), 0.5F);
        GameRegistry.addSmelting(OreBlocks.Blocks.OVERWORLD_ZINC_ORE.createItemStack(), Items.ZINC_INGOT.createItemStack(), 0.5F);

        REGISTRY.registerSmashable(COBBLESTONE_ORE, new ItemStack(GRAVEL));
        REGISTRY.registerSmashable(COBBLESTONE_WALL, new ItemStack(GRAVEL));
        REGISTRY.registerSmashable(GRAVEL_ORE, new ItemStack(SAND));
        REGISTRY.registerSmashable(GLOWSTONE_ORE, new ItemStack(GLOWSTONE_DUST, 4));
        REGISTRY.registerSmashable(SANDSTONE_ORE, new ItemStack(SAND));
    }
}
