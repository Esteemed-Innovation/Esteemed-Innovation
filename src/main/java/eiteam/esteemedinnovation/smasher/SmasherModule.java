package eiteam.esteemedinnovation.smasher;

import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.CrossMod;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import minetweaker.MineTweakerAPI;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static eiteam.esteemedinnovation.commons.EsteemedInnovation.STEAMPOWER_CATEGORY;
import static eiteam.esteemedinnovation.commons.OreDictEntries.*;
import static eiteam.esteemedinnovation.misc.ItemCraftingComponent.Types.BRASS_PISTON;
import static eiteam.esteemedinnovation.misc.MiscellaneousModule.COMPONENT;

public class SmasherModule extends ContentModule {
    public static Block ROCK_SMASHER;
    public static Block ROCK_SMASHER_DUMMY;
    public static ItemSmashedOre SMASHED_ORE;

    @Override
    public void create(Side side) {
        ROCK_SMASHER = setup(new BlockSmasher(), "smasher");
        ROCK_SMASHER_DUMMY = setup(new BlockDummy(), "smasher_dummy", (CreativeTabs) null);
        SMASHED_ORE = (ItemSmashedOre) setup(new ItemSmashedOre(), "smashed_ore");

        for (ItemSmashedOre.Types type : ItemSmashedOre.Types.values()) {
            SMASHED_ORE.registerEntry(type);
        }
        registerTileEntity(TileEntitySmasher.class, "smasher");
        registerTileEntity(TileEntityDummyBlock.class, "dummy");
    }

    @Override
    public void oreDict(Side side) {
        SMASHED_ORE.registerDusts();
    }

    @Override
    public void recipes(Side side) {
        if (Config.enableSmasher) {
            BookRecipeRegistry.addRecipe("smasher1", new ShapedOreRecipe(ROCK_SMASHER,
              "bpi",
              "bpi",
              "bpi",
              'i', PLATE_THIN_IRON,
              'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
              'b', PLATE_THIN_BRASS
            ));
            BookRecipeRegistry.addRecipe("smasher2", new ShapedOreRecipe(ROCK_SMASHER,
              "bpi",
              "bpi",
              "bpi",
              'i', INGOT_IRON,
              'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
              'b', INGOT_BRASS
            ));
            BookRecipeRegistry.addRecipe("smasher3", new ShapedOreRecipe(ROCK_SMASHER,
              "bpi",
              "bpi",
              "bpi",
              'i', INGOT_IRON,
              'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
              'b', PLATE_THIN_BRASS
            ));
            BookRecipeRegistry.addRecipe("smasher4", new ShapedOreRecipe(ROCK_SMASHER,
              "bpi",
              "bpi",
              "bpi",
              'i', PLATE_THIN_IRON,
              'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
              'b', INGOT_BRASS
            ));
        }
        SMASHED_ORE.addSmelting();
    }

    @Override
    public void finish(Side side) {
        if (CrossMod.CRAFTTWEAKER) {
            MineTweakerAPI.registerClass(RockSmasherTweaker.class);
        }

        if (Config.enableSmasher) {
            BookPageRegistry.addEntryToCategory(STEAMPOWER_CATEGORY, new BookEntry("research.Smasher.name",
              new BookPageItem("research.Smasher.name", "research.Smasher.0", new ItemStack(ROCK_SMASHER)),
              new BookPageText("research.Smasher.name", "research.Smasher.1"),
              new BookPageCrafting("", "smasher1", "smasher2", "smasher3", "smasher4")));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void preInitClient() {
        registerModel(ROCK_SMASHER);
        for (Integer meta : ItemSmashedOre.map.keySet()) {
            registerModel(SMASHED_ORE, meta);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initClient() {
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemSmashedOreColorHandler(), SMASHED_ORE);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySmasher.class, new TileEntitySmasherRenderer());
    }
}
