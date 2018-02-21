package eiteam.esteemedinnovation.hammer;

import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.commons.init.ConfigurableModule;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static eiteam.esteemedinnovation.commons.Config.CATEGORY_BLOCKS;
import static eiteam.esteemedinnovation.commons.Config.CATEGORY_CONSUMPTION;
import static eiteam.esteemedinnovation.commons.EsteemedInnovation.STEAMPOWER_SECTION;
import static eiteam.esteemedinnovation.commons.EsteemedInnovation.channel;

public class HammerModule extends ContentModule implements ConfigurableModule {
    public static Block STEAM_HAMMER;
    static boolean enableHammer;
    static int hammerConsumption;

    @Override
    public void create(Side side) {
        channel.registerMessage(ItemNamePacketHandler.class, ItemNamePacket.class, 1, Side.SERVER);
    }

    @Override
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        STEAM_HAMMER = setup(event, new BlockSteamHammer(), "hammer");
        registerTileEntity(TileEntitySteamHammer.class, "steamHammer");
    }

    @Override
    public void registerItems(RegistryEvent.Register<Item> event) {
        setupItemBlock(event, STEAM_HAMMER);
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
        if (enableHammer) {
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

    @Override
    public void loadConfigurationOptions(Configuration config) {
        enableHammer = config.get(CATEGORY_BLOCKS, "Enable Steam Hammer", true).getBoolean();
        hammerConsumption = config.get(CATEGORY_CONSUMPTION, "Steam Hammer consumption", 4000).getInt();
    }

    @Override
    public boolean doesRecipeBelongTo(String configSetting) {
        return "enableHammer".equals(configSetting);
    }

    @Override
    public boolean isRecipeEnabled(String configSetting) {
        return enableHammer;
    }
}
