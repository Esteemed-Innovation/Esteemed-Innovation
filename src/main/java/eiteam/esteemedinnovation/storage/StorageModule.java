package eiteam.esteemedinnovation.storage;

import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import eiteam.esteemedinnovation.storage.item.ItemKitBag;
import eiteam.esteemedinnovation.storage.item.canister.CanisterRecipe;
import eiteam.esteemedinnovation.storage.item.canister.EntityCanisterItem;
import eiteam.esteemedinnovation.storage.item.canister.RenderCanister;
import eiteam.esteemedinnovation.storage.steam.BlockSteamTank;
import eiteam.esteemedinnovation.storage.steam.BlockTankItem;
import eiteam.esteemedinnovation.storage.steam.TileEntityCreativeTank;
import eiteam.esteemedinnovation.storage.steam.TileEntitySteamTank;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static eiteam.esteemedinnovation.commons.EsteemedInnovation.GADGET_SECTION;
import static eiteam.esteemedinnovation.commons.EsteemedInnovation.STEAMPOWER_SECTION;
import static eiteam.esteemedinnovation.commons.OreDictEntries.*;
import static net.minecraft.init.Items.RABBIT_HIDE;

public class StorageModule extends ContentModule {
    public static Block STEAM_TANK;
    public static Item KIT_BAG;
    public static Item ITEM_CANISTER;

    @Override
    public void create(Side side) {
        STEAM_TANK = setup(new BlockSteamTank(), "steam_tank", BlockTankItem::new);
        KIT_BAG = setup(new ItemKitBag(), "kit_bag");
        ITEM_CANISTER = setup(new Item(), "canister");

        EntityRegistry.registerModEntity(EntityCanisterItem.class, "CanisterItem", 2, EsteemedInnovation.instance, 64, 20, true);
        registerTileEntity(TileEntitySteamTank.class, "steamTank");
        registerTileEntity(TileEntityCreativeTank.class, "creativeSteamTank");
    }

    @Override
    public void recipes(Side side) {
        if (Config.enableTank) {
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
        }
    }

    @Override
    public void finish(Side side) {
        if (Config.enableCanister) {
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
    public void preInitClient() {
        registerModel(STEAM_TANK, 0, "is_creative=false");
        registerModel(STEAM_TANK, 1, "is_creative=true");
        registerModel(ITEM_CANISTER);
        registerModel(KIT_BAG);
        RenderingRegistry.registerEntityRenderingHandler(EntityCanisterItem.class, RenderCanister::new);
    }
}
