package eiteam.esteemedinnovation.hammer;

import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static eiteam.esteemedinnovation.commons.EsteemedInnovation.STEAMPOWER_SECTION;
import static eiteam.esteemedinnovation.commons.EsteemedInnovation.channel;
import static eiteam.esteemedinnovation.commons.OreDictEntries.*;

public class HammerModule extends ContentModule {
    public static Block STEAM_HAMMER;

    @Override
    public void create(Side side) {
        channel.registerMessage(ItemNamePacketHandler.class, ItemNamePacket.class, 1, Side.SERVER);
        STEAM_HAMMER = setup(new BlockSteamHammer(), "hammer");
        registerTileEntity(TileEntitySteamHammer.class, "steamHammer");
    }

    @Override
    public void recipes(Side side) {
        //TODO: transfer recipes to json
        /*if (Config.enableHammer) {
            BookRecipeRegistry.addRecipe("hammer1", new ShapedOreRecipe(STEAM_HAMMER,
              " ix",
              "bix",
              'x', INGOT_BRASS,
              'i', INGOT_IRON,
              'b', BLOCK_IRON
            ));
            BookRecipeRegistry.addRecipe("hammer2", new ShapedOreRecipe(STEAM_HAMMER,
              " ix",
              "bix",
              'x', PLATE_THIN_BRASS,
              'i', INGOT_IRON,
              'b', BLOCK_IRON
            ));
        }
        */
    }

    @Override
    public void finish(Side side) {
        if (Config.enableHammer) {
            BookPageRegistry.addCategoryToSection(STEAMPOWER_SECTION, 10,
              new BookCategory("category.Hammer.name",
                new BookEntry("research.Hammer.name",
                  new BookPageItem("research.Hammer.name", "research.Hammer.0", new ItemStack(STEAM_HAMMER)),
                  new BookPageText("research.Hammer.name", "research.Hammer.1"),
                  new BookPageCrafting("", "hammer1", "hammer2"))));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initClient() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySteamHammer.class, new TileEntitySteamHammerRenderer());
    }
}
