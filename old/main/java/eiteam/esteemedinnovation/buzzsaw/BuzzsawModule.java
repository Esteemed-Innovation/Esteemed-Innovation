package eiteam.esteemedinnovation.buzzsaw;

import eiteam.esteemedinnovation.commons.init.ConfigurableModule;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import eiteam.esteemedinnovation.commons.util.RecipeUtility;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;

import static eiteam.esteemedinnovation.commons.Config.CATEGORY_BLOCKS;
import static eiteam.esteemedinnovation.commons.Config.CATEGORY_MACHINES;
import static eiteam.esteemedinnovation.commons.OreDictEntries.PLATE_THIN_BRASS;
import static eiteam.esteemedinnovation.misc.ItemCraftingComponent.Types.BRASS_PISTON;
import static eiteam.esteemedinnovation.misc.ItemCraftingComponent.Types.BRASS_TURBINE;
import static eiteam.esteemedinnovation.misc.MiscellaneousModule.COMPONENT;
import static eiteam.esteemedinnovation.tools.ToolsModule.TIMBER_CHAIN;

public class BuzzsawModule extends ContentModule implements ConfigurableModule {
    public static Block BUZZSAW;
    static int duplicateLogs;
    static boolean enableSaw;

    @Override
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        BUZZSAW = setup(event, new BlockSaw(), "saw");
        registerTileEntity(TileEntitySaw.class, "saw");
    }

    @Override
    public void registerItems(RegistryEvent.Register<Item> event) {
        setupItemBlock(event, BUZZSAW);
    }

    @Override
    public void recipes(RegistryEvent.Register<IRecipe> event) {
        if (enableSaw) {
            RecipeUtility.addRecipe(event, true, "saw", BUZZSAW,
              "btb",
              "p p",
              "mmm",
              'b', TIMBER_CHAIN,
              't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata()),
              'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
              'm', PLATE_THIN_BRASS
            );
        }
    }

    @Override
    public void loadConfigurationOptions(Configuration config) {
        enableSaw = config.get(CATEGORY_BLOCKS, "Enable the Buzzsaw", true).getBoolean();
        duplicateLogs = config.get(CATEGORY_MACHINES, "Chance of duplicate drops from Buzzsaw (1 in X)", 6).getInt();
    }
}
