package eiteam.esteemedinnovation.init.misc;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class CraftingHelpers {
    public static void add3x3Recipe(ItemStack out, String in) {
        GameRegistry.addRecipe(new ShapedOreRecipe(out,
          "xxx",
          "xxx",
          "xxx",
          'x', in
        ));
    }
}
