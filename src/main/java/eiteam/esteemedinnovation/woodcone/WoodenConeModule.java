package eiteam.esteemedinnovation.woodcone;

import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.api.crucible.CrucibleRegistry;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.OreDictEntries;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import eiteam.esteemedinnovation.commons.util.RecipeUtility;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static eiteam.esteemedinnovation.commons.EsteemedInnovation.MISC_SECTION;
import static eiteam.esteemedinnovation.materials.MaterialsModule.IRON_LIQUID;

public class WoodenConeModule extends ContentModule {
    public static Block WOODEN_CONE;

    @Override
    public void create(Side side) {
        WOODEN_CONE = setup(new BlockWoodenCone(), "wooden_cone");
    }

    @Override
    public void recipes(Side side) {
        /*BookRecipeRegistry.addRecipe("woodenCone", new ShapedOreRecipe(WOODEN_CONE,
          " s ",
          "p p",
          "l l",
          's', OreDictEntries.STICK_WOOD,
          'p', OreDictEntries.PLANK_WOOD,
          'l', OreDictEntries.LOG_WOOD
        ));
        */
        if (Config.removeHopperRecipe) {
            RecipeUtility.removeRecipe(recipe -> {
                ItemStack output = recipe.getRecipeOutput();
                return output != null && output.getItem() == Item.getItemFromBlock(net.minecraft.init.Blocks.HOPPER);
            });
        }
        CrucibleRegistry.registerDunkRecipe(Item.getItemFromBlock(WOODEN_CONE), IRON_LIQUID, 45,
          new ItemStack(net.minecraft.init.Blocks.HOPPER));
    }

    @Override
    public void finish(Side side) {
        BookPageRegistry.addCategoryToSection(MISC_SECTION,
          new BookCategory("category.WoodenCone.name",
            new BookEntry("research.WoodenCone.name",
              new BookPageItem("research.WoodenCone.name", "research.WoodenCone.0", true, new ItemStack(WOODEN_CONE)),
              new BookPageCrafting("", "woodenCone"),
              new BookPageDip("", IRON_LIQUID, 45, new ItemStack(WOODEN_CONE), new ItemStack(Blocks.HOPPER)))));
    }
}
