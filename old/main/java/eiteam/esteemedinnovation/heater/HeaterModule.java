package eiteam.esteemedinnovation.heater;

import crafttweaker.CraftTweakerAPI;
import eiteam.esteemedinnovation.api.heater.SteamingRegistry;
import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.api.heater.HeatableRegistry;
import eiteam.esteemedinnovation.commons.CrossMod;
import eiteam.esteemedinnovation.commons.init.ConfigurableModule;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import eiteam.esteemedinnovation.commons.util.RecipeUtility;
import eiteam.esteemedinnovation.transport.TransportationModule;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import static eiteam.esteemedinnovation.commons.Config.CATEGORY_BLOCKS;
import static eiteam.esteemedinnovation.commons.Config.CATEGORY_CONSUMPTION;
import static eiteam.esteemedinnovation.commons.EsteemedInnovation.STEAMPOWER_SECTION;
import static eiteam.esteemedinnovation.commons.OreDictEntries.*;
import static net.minecraft.init.Blocks.FURNACE;
import static net.minecraft.init.Items.*;

public class HeaterModule extends ContentModule implements ConfigurableModule {
    public static Block STEAM_HEATER;
    public static Item STEAMED_FISH;
    public static Item STEAMED_SALMON;
    public static Item STEAMED_CHICKEN;
    public static Item STEAMED_BEEF;
    public static Item STEAMED_PORKCHOP;
    public static Item STEAMED_MUTTON;
    public static Item STEAMED_RABBIT;
    public static Item STEAMED_CARROT;
    public static Item STEAMED_BEETROOT;
    public static Item STEAMED_POTATO;
    static boolean enableHeater;
    static int heaterConsumption;

    @Override
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        STEAM_HEATER = setup(event, new BlockSteamHeater(), "heater");

        registerTileEntity(TileEntitySteamHeater.class, "heater");
    }

    @Override
    public void registerItems(RegistryEvent.Register<Item> event) {
        setupItemBlock(event, STEAM_HEATER);

        STEAMED_FISH = setup(event, new ItemSteamedFood((ItemFood) COOKED_FISH), "steamed_fish");
        STEAMED_SALMON = setup(event, new ItemSteamedFood(new ItemStack(COOKED_FISH, 1, 1)), "steamed_salmon");
        STEAMED_CHICKEN = setup(event, new ItemSteamedFood((ItemFood) COOKED_CHICKEN), "steamed_chicken");
        STEAMED_BEEF = setup(event, new ItemSteamedFood((ItemFood) COOKED_BEEF), "steamed_beef");
        STEAMED_PORKCHOP = setup(event, new ItemSteamedFood((ItemFood) COOKED_PORKCHOP), "steamed_porkchop");
        STEAMED_MUTTON = setup(event, new ItemSteamedFood((ItemFood) COOKED_MUTTON), "steamed_mutton");
        STEAMED_RABBIT = setup(event, new ItemSteamedFood((ItemFood) COOKED_RABBIT), "steamed_rabbit");
        STEAMED_CARROT = setup(event, new ItemSteamedFood((ItemFood) CARROT), "steamed_carrot");
        STEAMED_BEETROOT = setup(event, new ItemSteamedFood((ItemFood) BEETROOT), "steamed_beetroot");
        STEAMED_POTATO = setup(event, new ItemSteamedFood((ItemFood) BAKED_POTATO), "steamed_potato");

        OreDictionary.registerOre(ALL_FISH_COOKED, STEAMED_FISH);
        OreDictionary.registerOre(ALL_FISH_COOKED, STEAMED_SALMON);
        OreDictionary.registerOre(ALL_CHICKEN_COOKED, STEAMED_CHICKEN);
        OreDictionary.registerOre(ALL_PORK_COOKED, STEAMED_PORKCHOP);
        OreDictionary.registerOre(ALL_BEEF_COOKED, STEAMED_BEEF);
        OreDictionary.registerOre(ALL_MEAT_COOKED, STEAMED_FISH);
        OreDictionary.registerOre(ALL_MEAT_COOKED, STEAMED_SALMON);
        OreDictionary.registerOre(ALL_MEAT_COOKED, STEAMED_CHICKEN);
        OreDictionary.registerOre(ALL_MEAT_COOKED, STEAMED_PORKCHOP);
        OreDictionary.registerOre(ALL_MEAT_COOKED, STEAMED_BEEF);

        OreDictionary.registerOre(ALL_VEGGIE, STEAMED_CARROT);
        OreDictionary.registerOre(ALL_VEGGIE, STEAMED_BEETROOT);
        OreDictionary.registerOre(ALL_VEGGIE, STEAMED_POTATO);
    }

    @Override
    public void recipes(RegistryEvent.Register<IRecipe> event) {
        if (enableHeater) {
            RecipeUtility.addRecipe(event, true, "heater1", STEAM_HEATER,
              "ccc",
              "xfx",
              " p ",
              'x', INGOT_BRASS,
              'c', NUGGET_COPPER,
              'f', FURNACE,
              'p', TransportationModule.BRASS_PIPE
            );
            RecipeUtility.addRecipe(event, true, "heater2", STEAM_HEATER,
              "ccc",
              "xfx",
              " p ",
              'x', PLATE_THIN_BRASS,
              'c', NUGGET_COPPER,
              'f', FURNACE,
              'p', TransportationModule.BRASS_PIPE
            );
        }
        SteamingRegistry.addSteamingRecipe(new ItemStack(BEEF), new ItemStack(STEAMED_BEEF));
        SteamingRegistry.addSteamingRecipe(new ItemStack(CHICKEN), new ItemStack(STEAMED_CHICKEN));
        SteamingRegistry.addSteamingRecipe(new ItemStack(FISH), new ItemStack(STEAMED_FISH));
        SteamingRegistry.addSteamingRecipe(new ItemStack(PORKCHOP), new ItemStack(STEAMED_PORKCHOP));
        SteamingRegistry.addSteamingRecipe(new ItemStack(FISH, 1, 1), new ItemStack(STEAMED_SALMON));
        SteamingRegistry.addSteamingRecipe(new ItemStack(MUTTON), new ItemStack(STEAMED_MUTTON));
        SteamingRegistry.addSteamingRecipe(new ItemStack(RABBIT), new ItemStack(STEAMED_RABBIT));
        SteamingRegistry.addSteamingRecipe(new ItemStack(BEETROOT), new ItemStack(STEAMED_BEETROOT));
        SteamingRegistry.addSteamingRecipe(new ItemStack(CARROT), new ItemStack(STEAMED_CARROT));
        SteamingRegistry.addSteamingRecipe(new ItemStack(POTATO), new ItemStack(STEAMED_POTATO));
    }

    @Override
    public void finish(Side side) {
        if (CrossMod.CRAFTTWEAKER) {
            CraftTweakerAPI.registerClass(SteamHeaterTweaker.class);
        }

        if (enableHeater) {
            BookPageRegistry.addCategoryToSection(STEAMPOWER_SECTION, 8,
              new BookCategory("category.Heater.name",
                new BookEntry("research.Heater.name",
                  new BookPageItem("research.Heater.name", "research.Heater.0", new ItemStack(STEAM_HEATER)),
                  new BookPageCrafting("", "heater1", "heater2"))));
        }

        MinecraftForge.EVENT_BUS.register(new DisableTileEntityHandler());

        //Vanilla heater handler
        HeatableRegistry.addHeatable(new VanillaFurnaceHandler());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(ModelRegistryEvent event) {
        registerModel(STEAM_HEATER);
        registerModel(STEAMED_BEEF);
        registerModel(STEAMED_BEETROOT);
        registerModel(STEAMED_CARROT);
        registerModel(STEAMED_CHICKEN);
        registerModel(STEAMED_FISH);
        registerModel(STEAMED_MUTTON);
        registerModel(STEAMED_PORKCHOP);
        registerModel(STEAMED_POTATO);
        registerModel(STEAMED_RABBIT);
        registerModel(STEAMED_SALMON);
    }

    @Override
    public void loadConfigurationOptions(Configuration config) {
        enableHeater = config.get(CATEGORY_BLOCKS, "Enable Steam Heater", true).getBoolean();
        heaterConsumption = config.get(CATEGORY_CONSUMPTION, "Steam Heater consumption", 20).getInt();
    }
}
