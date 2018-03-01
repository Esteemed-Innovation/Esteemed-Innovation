package eiteam.esteemedinnovation.smasher;

import crafttweaker.CraftTweakerAPI;
import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.commons.CrossMod;
import eiteam.esteemedinnovation.commons.init.ConfigurableModule;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static eiteam.esteemedinnovation.commons.Config.CATEGORY_BLOCKS;
import static eiteam.esteemedinnovation.commons.Config.CATEGORY_MACHINES;
import static eiteam.esteemedinnovation.commons.EsteemedInnovation.STEAMPOWER_SECTION;
import static eiteam.esteemedinnovation.commons.OreDictEntries.*;
import static eiteam.esteemedinnovation.misc.ItemCraftingComponent.Types.BRASS_PISTON;
import static eiteam.esteemedinnovation.misc.MiscellaneousModule.COMPONENT;

public class SmasherModule extends ContentModule implements ConfigurableModule {
    public static Block ROCK_SMASHER;
    public static Block ROCK_SMASHER_DUMMY;
    public static ItemSmashedOre SMASHED_ORE;
    static boolean enableSmasher;
    static int smasherDoubleChance;

    @Override
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        ROCK_SMASHER = setup(event, new BlockSmasher(), "smasher");
        ROCK_SMASHER_DUMMY = setup(event, new BlockDummy(), "smasher_dummy", (CreativeTabs) null);

        registerTileEntity(TileEntitySmasher.class, "smasher");
        registerTileEntity(TileEntityDummyBlock.class, "dummy");
    }

    @Override
    public void registerItems(RegistryEvent.Register<Item> event) {
        setupItemBlock(event, ROCK_SMASHER);
        setupItemBlock(event, ROCK_SMASHER_DUMMY, (CreativeTabs) null);

        SMASHED_ORE = (ItemSmashedOre) setup(event, new ItemSmashedOre(), "smashed_ore");

        for (ItemSmashedOre.Types type : ItemSmashedOre.Types.values()) {
            SMASHED_ORE.registerEntry(type);
        }

        SMASHED_ORE.registerDusts();
    }

    @Override
    public void recipes(RegistryEvent.Register<IRecipe> event) {
        if (enableSmasher) {
            addRecipe(event, true, "smasher1", ROCK_SMASHER,
              "bpi",
              "bpi",
              "bpi",
              'i', PLATE_THIN_IRON,
              'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
              'b', PLATE_THIN_BRASS
            );
            addRecipe(event, true, "smasher2", ROCK_SMASHER,
              "bpi",
              "bpi",
              "bpi",
              'i', INGOT_IRON,
              'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
              'b', INGOT_BRASS
            );
            addRecipe(event, true, "smasher3", ROCK_SMASHER,
              "bpi",
              "bpi",
              "bpi",
              'i', INGOT_IRON,
              'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
              'b', PLATE_THIN_BRASS
            );
            addRecipe(event, true, "smasher4", ROCK_SMASHER,
              "bpi",
              "bpi",
              "bpi",
              'i', PLATE_THIN_IRON,
              'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
              'b', INGOT_BRASS
            );
        }
        SMASHED_ORE.addSmelting();
    }

    @Override
    public void finish(Side side) {
        if (CrossMod.CRAFTTWEAKER) {
            CraftTweakerAPI.registerClass(RockSmasherTweaker.class);
        }

        if (enableSmasher) {
            BookPageRegistry.addCategoryToSection(STEAMPOWER_SECTION, 12,
              new BookCategory("category.Smasher.name",
                new BookEntry("research.Smasher.name",
                  new BookPageItem("research.Smasher.name", "research.Smasher.0", new ItemStack(ROCK_SMASHER)),
                  new BookPageText("research.Smasher.name", "research.Smasher.1"),
                  new BookPageCrafting("", "smasher1", "smasher2", "smasher3", "smasher4"))));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(ModelRegistryEvent event) {
        registerModel(ROCK_SMASHER);
        for (Integer meta : ItemSmashedOre.map.keySet()) {
            registerModel(SMASHED_ORE, meta);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initClient() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySmasher.class, new TileEntitySmasherRenderer());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void postInitClient() {
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemSmashedOreColorHandler(), SMASHED_ORE);
    }

    @Override
    public void loadConfigurationOptions(Configuration config) {
        smasherDoubleChance = config.get(CATEGORY_MACHINES, "Chance of double drops from Rock Smasher (%)", 75).getInt();
        enableSmasher = config.get(CATEGORY_BLOCKS, "Enable Rock Smasher", true).getBoolean();
    }

    @Override
    public boolean doesRecipeBelongTo(String configSetting) {
        return "enableSmasher".equals(configSetting);
    }

    @Override
    public boolean isRecipeEnabled(String configSetting) {
        return enableSmasher;
    }
}
