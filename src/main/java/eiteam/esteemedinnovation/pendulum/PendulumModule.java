package eiteam.esteemedinnovation.pendulum;

import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.commons.OreDictEntries;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static eiteam.esteemedinnovation.commons.EsteemedInnovation.MISC_SECTION;

public class PendulumModule extends ContentModule {
    public static Block PENDULUM_TORCH;
    public static Block PENDULUM_STRING;

    @Override
    public void create(Side side) {

    }

    @Override
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        PENDULUM_STRING = setup(event, new BlockPendulumString(), "pendulum_string", (CreativeTabs) null);
        PENDULUM_TORCH = setup(event, new BlockPendulumTorch(), "pendulum_torch");
        registerTileEntity(TileEntityPendulumTorch.class, "pendulum_torch");
    }

    @Override
    public void registerItems(RegistryEvent.Register<Item> event) {
        setupItemBlock(event, PENDULUM_STRING);
        setupItemBlock(event, PENDULUM_TORCH, ItemRedstonePendulum::new);
    }

    @Override
    public void recipes(RegistryEvent.Register<IRecipe> event) {
        addRecipe(event, true, "pendulum_torch", PENDULUM_TORCH,
          "  x",
          " x ",
          "t  ",
          't', Blocks.REDSTONE_TORCH,
          'x', OreDictEntries.STRING_ORE);
    }

    @Override
    public void finish(Side side) {
        BookPageRegistry.addCategoryToSection(MISC_SECTION,
          new BookCategory("category.Pendulum.name",
            new BookEntry("research.Pendulum.name",
              new BookPageItem("research.Pendulum.name", "research.Pendulum.0", true, new ItemStack(PENDULUM_TORCH)),
              new BookPageCrafting("", "pendulum_torch"))));
    }

    @Override
    public void registerModels(ModelRegistryEvent event) {
        registerModel(PENDULUM_TORCH);
    }
}
