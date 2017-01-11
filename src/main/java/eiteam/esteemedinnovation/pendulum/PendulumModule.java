package eiteam.esteemedinnovation.pendulum;

import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.commons.OreDictEntries;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static eiteam.esteemedinnovation.commons.EsteemedInnovation.MISC_CATEGORY;

public class PendulumModule extends ContentModule {
    public static Block PENDULUM_TORCH;
    public static Block PENDULUM_STRING;

    @Override
    public void create(Side side) {
        PENDULUM_STRING = setup(new BlockPendulumString(), "pendulum_string", (CreativeTabs) null);
        PENDULUM_TORCH = setup(new BlockPendulumTorch(), "pendulum_torch", ItemRedstonePendulum::new);
        registerTileEntity(TileEntityPendulumTorch.class, "pendulum_torch");
    }

    @Override
    public void recipes(Side side) {
        BookRecipeRegistry.addRecipe("pendulum_torch", new ShapedOreRecipe(PENDULUM_TORCH,
          "  x",
          " x ",
          "t  ",
          't', Blocks.REDSTONE_TORCH,
          'x', OreDictEntries.STRING_ORE));
    }

    @Override
    public void finish(Side side) {
        BookPageRegistry.addEntryToCategory(MISC_CATEGORY, new BookEntry("research.Pendulum.name",
          new BookPageItem("research.Pendulum.name", "research.Pendulum.0", true, new ItemStack(PENDULUM_TORCH)),
          new BookPageCrafting("", "pendulum_torch")));
    }

    @Override
    public void preInitClient() {
        registerModel(PENDULUM_TORCH);
    }
}
