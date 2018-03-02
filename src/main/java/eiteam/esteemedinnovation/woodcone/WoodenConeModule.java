package eiteam.esteemedinnovation.woodcone;

import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.api.crucible.CrucibleRegistry;
import eiteam.esteemedinnovation.commons.OreDictEntries;
import eiteam.esteemedinnovation.commons.init.ConfigurableModule;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import eiteam.esteemedinnovation.commons.util.RecipeUtility;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.relauncher.Side;

import static eiteam.esteemedinnovation.commons.Config.CATEGORY_OTHER;
import static eiteam.esteemedinnovation.commons.EsteemedInnovation.MISC_SECTION;
import static eiteam.esteemedinnovation.materials.MaterialsModule.IRON_LIQUID;

public class WoodenConeModule extends ContentModule implements ConfigurableModule {
    public static Block WOODEN_CONE;
    private static boolean removeHopperRecipe;

    @Override
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        WOODEN_CONE = setup(event, new BlockWoodenCone(), "wooden_cone");
    }

    @Override
    public void registerItems(RegistryEvent.Register<Item> event) {
        setupItemBlock(event, WOODEN_CONE);
    }

    @Override
    public void recipes(RegistryEvent.Register<IRecipe> event) {
        RecipeUtility.addRecipe(event, true, "woodenCone", WOODEN_CONE,
          " s ",
          "p p",
          "l l",
          's', OreDictEntries.STICK_WOOD,
          'p', OreDictEntries.PLANK_WOOD,
          'l', OreDictEntries.LOG_WOOD
        );

        CrucibleRegistry.registerDunkRecipe(Item.getItemFromBlock(WOODEN_CONE), IRON_LIQUID, 45,
          new ItemStack(net.minecraft.init.Blocks.HOPPER));
    }

    @Override
    public void finish(Side side) {
        if (removeHopperRecipe) {
            RecipeUtility.removeRecipeByOutput(Item.getItemFromBlock(Blocks.HOPPER));
        }
        BookPageRegistry.addCategoryToSection(MISC_SECTION,
          new BookCategory("category.WoodenCone.name",
            new BookEntry("research.WoodenCone.name",
              new BookPageItem("research.WoodenCone.name", "research.WoodenCone.0", true, new ItemStack(WOODEN_CONE)),
              new BookPageCrafting("", "woodenCone"),
              new BookPageDip("", IRON_LIQUID, 45, new ItemStack(WOODEN_CONE), new ItemStack(Blocks.HOPPER)))));
    }

    @Override
    public void loadConfigurationOptions(Configuration config) {
        removeHopperRecipe = config.get(CATEGORY_OTHER, "Remove Hopper crafting recipes (can still be made with the crucible)", true).getBoolean();
    }

    @Override
    public boolean doesRecipeBelongTo(String configSetting) {
        return false;
    }

    @Override
    public boolean isRecipeEnabled(String configSetting) {
        return false;
    }
}
