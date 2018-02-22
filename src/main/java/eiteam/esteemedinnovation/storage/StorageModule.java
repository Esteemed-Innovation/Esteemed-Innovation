package eiteam.esteemedinnovation.storage;

import eiteam.esteemedinnovation.api.Constants;
import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.commons.init.ConfigurableModule;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import eiteam.esteemedinnovation.storage.item.ItemKitBag;
import eiteam.esteemedinnovation.storage.item.canister.EntityCanisterItem;
import eiteam.esteemedinnovation.storage.item.canister.RenderCanister;
import eiteam.esteemedinnovation.storage.steam.BlockSteamTank;
import eiteam.esteemedinnovation.storage.steam.BlockTankItem;
import eiteam.esteemedinnovation.storage.steam.TileEntityCreativeTank;
import eiteam.esteemedinnovation.storage.steam.TileEntitySteamTank;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static eiteam.esteemedinnovation.commons.Config.CATEGORY_EXOSUIT_UPGRADES;
import static eiteam.esteemedinnovation.commons.Config.CATEGORY_ITEMS;
import static eiteam.esteemedinnovation.commons.Config.CATEGORY_STEAM_SYSTEM;
import static eiteam.esteemedinnovation.commons.EsteemedInnovation.GADGET_SECTION;
import static eiteam.esteemedinnovation.commons.EsteemedInnovation.STEAMPOWER_SECTION;

public class StorageModule extends ContentModule implements ConfigurableModule {
    private static final int BASIC_TANK_CAPACITY_DEFAULT = 36000;
    public static Block STEAM_TANK;
    public static Item KIT_BAG;
    public static Item ITEM_CANISTER;
    private static boolean enableCanister;
    public static boolean enableTank;
    public static int basicTankCapacity;

    @Override
    public void create(Side side) {
        EntityRegistry.registerModEntity(new ResourceLocation(Constants.EI_MODID, "CanisterItem"), EntityCanisterItem.class, "CanisterItem", 2, EsteemedInnovation.instance, 64, 20, true);
    }

    @Override
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        STEAM_TANK = setup(event, new BlockSteamTank(), "steam_tank");

        registerTileEntity(TileEntitySteamTank.class, "steamTank");
        registerTileEntity(TileEntityCreativeTank.class, "creativeSteamTank");
    }

    @Override
    public void registerItems(RegistryEvent.Register<Item> event) {
        setupItemBlock(event, STEAM_TANK, BlockTankItem::new);

        KIT_BAG = setup(event, new ItemKitBag(), "kit_bag");
        ITEM_CANISTER = setup(event, new Item(), "canister");
    }

    @Override
    public void recipes(Side side) {
        //TODO: transfer recipes to json
        /*if (Config.enableTank) {
            BookRecipeRegistry.addRecipe("tank1", new ShapedOreRecipe(STEAM_TANK,
              "iii",
              "i i",
              "iii",
              'i', PLATE_THIN_BRASS
            ));
            BookRecipeRegistry.addRecipe("tank2", new ShapedOreRecipe(STEAM_TANK,
              "iii",
              "i i",
              "iii",
              'i', INGOT_BRASS
            ));
        }

        GameRegistry.addRecipe(new ShapedOreRecipe(KIT_BAG,
          "SSS",
          "LWL",
          " L ",
          'S', STRING_ORE,
          'L', LEATHER_ORE,
          'W', new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE)));
        GameRegistry.addRecipe(new ShapedOreRecipe(KIT_BAG,
          "SSS",
          "LWL",
          " L ",
          'S', STRING_ORE,
          'L', RABBIT_HIDE,
          'W', new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE)));

        if (Config.enableCanister) {
            GameRegistry.addRecipe(new CanisterRecipe());
            BookRecipeRegistry.addRecipe("canister", new ShapedOreRecipe(ITEM_CANISTER,
              " i ",
              "i i",
              " i ",
              'i', NUGGET_ZINC
            ));
            MinecraftForge.EVENT_BUS.register(new CanisterEntityCreator());
        }
        */
    }

    @Override
    public void finish(Side side) {
        if (enableCanister) {
            ItemStack output = new ItemStack(Items.DIAMOND_SWORD);
            output.setTagCompound(new NBTTagCompound());
            output.getTagCompound().setInteger("Canned", 0);
            BookPageRegistry.addCategoryToSection(GADGET_SECTION, 3,
              new BookCategory("category.Canister.name",
                new BookEntry("research.Canister.name",
                  new BookPageItem("research.Canister.name", "research.Canister.0", new ItemStack(ITEM_CANISTER)),
                  new BookPageCrafting("", "canister"),
                  new BookPageCrafting("", true, output, Items.DIAMOND_SWORD, ITEM_CANISTER))));
        }

        BookPageRegistry.addCategoryToSection(STEAMPOWER_SECTION, 5,
          new BookCategory("category.Tank.name",
            new BookEntry("research.Tank.name",
              new BookPageItem("research.Tank.name", "research.Tank.0", new ItemStack(STEAM_TANK)),
              new BookPageCrafting("", "tank1", "tank2")),
            new BookEntry("research.CreativeTank.name",
              new BookPageItem("research.CreativeTank.name", "research.CreativeTank.0", new ItemStack(Items.BOWL))) {
                @Override
                public boolean isHidden(EntityPlayer player) {
                    return true;
                }
            }));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(ModelRegistryEvent event) {
        registerModel(STEAM_TANK, 0, "is_creative=false");
        registerModel(STEAM_TANK, 1, "is_creative=true");
        registerModel(ITEM_CANISTER);
        registerModel(KIT_BAG);
        RenderingRegistry.registerEntityRenderingHandler(EntityCanisterItem.class, RenderCanister::new);
    }

    @Override
    public void loadConfigurationOptions(Configuration config) {
        enableTank = config.get(CATEGORY_STEAM_SYSTEM, "Enable Steam Tank (Crucial)", true).getBoolean();
        basicTankCapacity = config.get(CATEGORY_EXOSUIT_UPGRADES, "The amount of steam the basic tank can hold", StorageModule.BASIC_TANK_CAPACITY_DEFAULT).getInt();
        enableCanister = config.get(CATEGORY_ITEMS, "Enable Canisters", true).getBoolean();
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
