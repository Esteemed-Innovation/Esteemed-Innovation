package eiteam.esteemedinnovation.boiler;

import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static eiteam.esteemedinnovation.commons.EsteemedInnovation.BASICS_CATEGORY;
import static eiteam.esteemedinnovation.commons.EsteemedInnovation.STEAMPOWER_CATEGORY;
import static eiteam.esteemedinnovation.commons.OreDictEntries.INGOT_BRASS;
import static eiteam.esteemedinnovation.commons.OreDictEntries.PLATE_THIN_BRASS;
import static eiteam.esteemedinnovation.transport.TransportationModule.BRASS_PIPE;
import static net.minecraft.init.Blocks.FURNACE;

public class BoilerModule extends ContentModule {
    public static Block BOILER;

    @Override
    public void create(Side side) {
        BOILER = setup(new BlockBoiler(), "boiler");
        registerTileEntity(TileEntityBoiler.class, "boiler");
    }

    @Override
    public void recipes(Side side) {
        if (Config.enableBoiler) {
            BookRecipeRegistry.addRecipe("boiler1", new ShapedOreRecipe(BOILER,
              "xxx",
              "xfx",
              "xxx",
              'x', INGOT_BRASS,
              'f', FURNACE
            ));
            BookRecipeRegistry.addRecipe("boiler2", new ShapedOreRecipe(BOILER,
              "xxx",
              "xfx",
              "xxx",
              'x', PLATE_THIN_BRASS,
              'f', FURNACE
            ));
        }
    }

    @Override
    public void finish(Side side) {
        BookPageRegistry.addEntryToCategory(BASICS_CATEGORY, new BookEntry("research.SteamSystem.name",
          new BookPageItem("research.SteamSystem.name", "research.SteamSystem.0", new ItemStack(BOILER), new ItemStack(BRASS_PIPE)),
          new BookPageText("research.SteamSystem.name", "research.SteamSystem.1"),
          new BookPageText("research.SteamSystem.name", "research.SteamSystem.2"),
          new BookPageText("research.SteamSystem.name", "research.SteamSystem.3"),
          new BookPageText("research.SteamSystem.name", "research.SteamSystem.4"),
          new BookPageText("research.SteamSystem.name", "research.SteamSystem.5")));
        BookPageRegistry.addEntryToCategory(STEAMPOWER_CATEGORY, new BookEntry("research.Boiler.name",
          new BookPageItem("research.Boiler.name", "research.Boiler.0", new ItemStack(BOILER)),
          new BookPageCrafting("", "boiler1", "boiler2")));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void preInitClient() {
        registerModel(BOILER);
    }
}
