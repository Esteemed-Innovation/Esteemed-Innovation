package eiteam.esteemedinnovation.misc;

import eiteam.esteemedinnovation.api.SmasherRegistry;
import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.commons.init.ConfigurableModule;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import eiteam.esteemedinnovation.commons.util.RecipeUtility;
import eiteam.esteemedinnovation.misc.ItemCraftingComponent.Types;
import eiteam.esteemedinnovation.transport.TransportationModule;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Arrays;

import static eiteam.esteemedinnovation.commons.Config.CATEGORY_WEAPONS;
import static eiteam.esteemedinnovation.commons.EsteemedInnovation.BASICS_SECTION;
import static eiteam.esteemedinnovation.commons.OreDictEntries.*;
import static net.minecraft.init.Blocks.NETHER_BRICK;
import static net.minecraft.init.Blocks.PISTON;
import static net.minecraft.init.Items.FLINT_AND_STEEL;
import static net.minecraft.init.Items.NETHERBRICK;

public class MiscellaneousModule extends ContentModule implements ConfigurableModule {
    public static Item COMPONENT;
    private static boolean disableMainBarrelRecipe;

    @Override
    public void registerItems(RegistryEvent.Register<Item> event) {
        COMPONENT = setup(event, new ItemCraftingComponent(), "crafting");

        OreDictionary.registerOre(DUST_NETHERBRICK, new ItemStack(COMPONENT, 1, Types.NETHERBRICK_DUST.getMetadata()));
        OreDictionary.registerOre(BRICK_HELLFORGE, new ItemStack(COMPONENT, 1, Types.HELLFORGE_BRICK.getMetadata()));
    }

    @Override
    public void recipes(RegistryEvent.Register<IRecipe> event) {
        RecipeUtility.addRecipe(event, true, "piston1", new ItemStack(COMPONENT, 1, Types.BRASS_PISTON.getMetadata()),
          " x ",
          "xpx",
          " i ",
          'x', INGOT_BRASS,
          'p', PISTON,
          'i', TransportationModule.BRASS_PIPE
        );
        RecipeUtility.addRecipe(event, true, "piston2", new ItemStack(COMPONENT, 1, Types.BRASS_PISTON.getMetadata()),
          " x ",
          "xpx",
          " i ",
          'x', PLATE_THIN_BRASS,
          'p', PISTON,
          'i', TransportationModule.BRASS_PIPE
        );
        RecipeUtility.addRecipe(event, true, "turbine1", new ItemStack(COMPONENT, 1, Types.BRASS_TURBINE.getMetadata()),
          " x ",
          "xnx",
          " x ",
          'x', INGOT_BRASS,
          'n', NUGGET_BRASS
        );
        RecipeUtility.addRecipe(event, true, "turbine2", new ItemStack(COMPONENT, 1, Types.BRASS_TURBINE.getMetadata()),
          " x ",
          "xnx",
          " x ",
          'x', PLATE_THIN_BRASS,
          'n', NUGGET_BRASS
        );
        RecipeUtility.addRecipe(event, true, "stock", new ItemStack(COMPONENT, 1, Types.GUN_STOCK.getMetadata()),
          "p  ",
          " p ",
          " pp",
          'p', PLANK_WOOD
        );
        RecipeUtility.addRecipe(event, true, "flintlock1", new ItemStack(COMPONENT, 1, Types.FLINTLOCK.getMetadata()),
          "f i",
          "iri",
          'i', INGOT_IRON,
          'r', DUST_REDSTONE,
          'f', FLINT_AND_STEEL
        );
        RecipeUtility.addRecipe(event, true, "flintlock2", new ItemStack(COMPONENT, 1, Types.FLINTLOCK.getMetadata()),
          "f i",
          "iri",
          'i', PLATE_THIN_IRON,
          'r', DUST_REDSTONE,
          'f', FLINT_AND_STEEL
        );
        if (disableMainBarrelRecipe) {
            RecipeUtility.addRecipe(event, true, "barrel1", new ItemStack(COMPONENT, 1, Types.IRON_BARREL.getMetadata()),
              "i  ",
              " i ",
              "  i",
              'i', INGOT_IRON
            );
        } else {
            RecipeUtility.addRecipe(event, true, "barrel1", new ItemStack(COMPONENT, 1, Types.IRON_BARREL.getMetadata()),
              "i  ",
              " i ",
              "  i",
              'i', PLATE_THIN_IRON
            );
        }
        RecipeUtility.addRecipe(event, true, "barrel2", new ItemStack(COMPONENT, 1, Types.IRON_BARREL.getMetadata()),
          "i  ",
          " i ",
          "  i",
          'i', PLATE_THIN_IRON
        );
        RecipeUtility.addRecipe(event, true, "blunderBarrel1", new ItemStack(COMPONENT, 1, Types.BLUNDERBUSS_BARREL.getMetadata()),
          "i  ",
          " i ",
          "  i",
          'i', INGOT_BRASS
        );
        RecipeUtility.addRecipe(event, true, "blunderBarrel2", new ItemStack(COMPONENT, 1, Types.BLUNDERBUSS_BARREL.getMetadata()),
          "i  ",
          " i ",
          "  i",
          'i', PLATE_THIN_BRASS
        );

        SmasherRegistry.registerSmashable(NETHER_BRICK, new SmasherRegistry.TypicalBiFunction(Arrays.asList(
          new ItemStack(COMPONENT, 1, Types.NETHERBRICK_DUST.getMetadata()),
          new ItemStack(NETHERBRICK, 3))));

        RecipeUtility.addShapelessRecipe(event, true, "hellforgeBrick", new ItemStack(COMPONENT, 1, Types.HELLFORGE_BRICK_RAW.getMetadata()),
          DUST_ZINC, Items.MAGMA_CREAM, new ItemStack(COMPONENT, 1, Types.NETHERBRICK_DUST.getMetadata()));
        GameRegistry.addSmelting(new ItemStack(COMPONENT, 1, Types.HELLFORGE_BRICK_RAW.getMetadata()),
          new ItemStack(COMPONENT, 1, Types.HELLFORGE_BRICK.getMetadata()), 0F);
        GameRegistry.addSmelting(new ItemStack(COMPONENT, 1, Types.NETHERBRICK_DUST.getMetadata()), new ItemStack(NETHERBRICK), 0F);

    }

    @Override
    public void finish(Side side) {
        BookPageRegistry.addCategoryToSection(BASICS_SECTION, 2,
          new BookCategory("category.Bits.name",
            new BookEntry("research.Bits.name",
              new BookPageItem("research.Bits.name", "research.Bits.0",
                new ItemStack(COMPONENT, 1, Types.BRASS_PISTON.getMetadata())),
              new BookPageItem("research.Bits.name", "research.Bits.1", false,
                new ItemStack(COMPONENT, 1, Types.BRASS_TURBINE.getMetadata())),
              new BookPageItem("research.HellforgeMaterials.name", "research.HellforgeMaterials.0",
                new ItemStack(COMPONENT, 1, Types.NETHERBRICK_DUST.getMetadata()),
                new ItemStack(COMPONENT, 1, Types.HELLFORGE_BRICK_RAW.getMetadata()),
                new ItemStack(COMPONENT, 1, Types.HELLFORGE_BRICK.getMetadata())),
              new BookPageCrafting("", "hellforgeBrick"))));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(ModelRegistryEvent event) {
        for (ItemCraftingComponent.Types type : ItemCraftingComponent.Types.values()) {
            registerModelItemStack(new ItemStack(COMPONENT, 1, type.getMetadata()));
        }
    }

    @Override
    public void loadConfigurationOptions(Configuration config) {
        disableMainBarrelRecipe = config.get(CATEGORY_WEAPONS, "Remove ingot barrel recipe in case of conflicts (keeps plate recipe)", false).getBoolean();
    }
}
