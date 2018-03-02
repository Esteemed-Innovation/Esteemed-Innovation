package eiteam.esteemedinnovation.commons.init;

import eiteam.esteemedinnovation.api.Constants;
import eiteam.esteemedinnovation.api.book.BookRecipeRegistry;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.function.Function;

/**
 * A class for handling base initialization of content. See {@link eiteam.esteemedinnovation.armor.ArmorModule} for
 * a good example. Register it (so that the content actually gets initialized) in {@link ContentModuleHandler}. That
 * class will handle calling all of the methods within each ContentModule.
 * <br />
 * The order that the methods are called is as follows:
 * <ol>
 *     <li>{@link #create(Side)}</li>
 *     <li>{@link #preInitClient()}</li>
 *     <li>{@link #initClient()}</li>
 *     <li>{@link #postInitClient()}</li>
 *     <li>{@link #finish(Side)}</li>
 * </ol>
 * <br />
 * It also includes {@link RegistryEvent} methods which are automatically called by the ContentModuleHandler. Registration
 * of supported types should be handled in these methods:
 * <ul>
 *     <li>{@link #registerBlocks(RegistryEvent.Register)}</li>
 *     <li>{@link #registerItems(RegistryEvent.Register)}</li>
 *     <li>{@link #registerModels(ModelRegistryEvent)}</li>
 *     <li>{@link #recipes(RegistryEvent.Register)}</li>
 * </ul>
 * <br />
 * Register {@link net.minecraftforge.oredict.OreDictionary} entries in {@link #registerItems(RegistryEvent.Register)}.
 */
public class ContentModule {
    /**
     * Register your blocks. Use the setup methods below to easily handle registration,
     * or just handle everything on your own.
     * @param event The block registry event
     */
    public void registerBlocks(RegistryEvent.Register<Block> event) {}

    /**
     * Register your items. Use the setup methods below to easily handle registration,
     * or just handle everything on your own. OreDictionary entries should be registered in this method.
     * @param event The item registry event
     */
    public void registerItems(RegistryEvent.Register<Item> event) {}

    /**
     * Register your models. Use {@link #postInitClient()} for special item models.
     * Use the setup methods below to easily handle registration, or just handle everything on your own.
     * @param event The model registry event
     */
    @SideOnly(Side.CLIENT)
    public void registerModels(ModelRegistryEvent event) {}

    /**
     * Called during init in both client and server. Use to register miscellaneous things for the module.
     * @param side The side currently on
     */
    public void create(Side side) {}

    /**
     * Register your recipes. Called between preinit and init, after item & block registrations.
     * @param event
     */
    public void recipes(RegistryEvent.Register<IRecipe> event) {}

    /**
     * Handle any client-side preInit stuff here. Called in preInit on the client.
     */
    @SideOnly(Side.CLIENT)
    public void preInitClient() {}

    /**
     * Handle any client-side init stuff here. Use this to, for example, register client-side event handlers or register
     * color handlers. Called in init on the client.
     */
    @SideOnly(Side.CLIENT)
    public void initClient() {}

    /**
     * Handle any client-side postInit stuff here. Called in postInit before {@link #finish(Side)}.
     */
    @SideOnly(Side.CLIENT)
    public void postInitClient() {}

    /**
     * Handle any finishing up here. This is called in postInit on client and server after {@link #postInitClient()}.
     * @param side
     */
    public void finish(Side side) {}

    /**
     * Overload for {@link #setup(RegistryEvent.Register, Item, String, CreativeTabs)} that uses {@link EsteemedInnovation#tab} for the tab argument.
     */
    protected Item setup(RegistryEvent.Register<Item> event, Item startingItem, String path) {
        return setup(event, startingItem, path, EsteemedInnovation.tab);
    }

    /**
     * Sets up an Item with an unlocalized name, a registry name, an optional creative tab, and registers it to the
     * Item registry.
     * @param startingItem The item to start with
     * @param path The name of the item, excluding the EI mod ID.
     * @param tab The creative tab to add it to. Use null to not add it to a tab at all.
     * @return The registered item.
     */
    protected Item setup(RegistryEvent.Register<Item> event, Item startingItem, String path, CreativeTabs tab) {
        startingItem.setUnlocalizedName(Constants.EI_MODID + ":" + path);
        if (tab != null) {
            startingItem.setCreativeTab(tab);
        }
        startingItem.setRegistryName(Constants.EI_MODID, path);
        event.getRegistry().register(startingItem);
        return startingItem;
    }

    /**
     * Overload for {@link #setup(RegistryEvent.Register, Block, String)} that uses the default constructor for {@link ItemBlock} as
     * the function.
     */
    protected Block setup(RegistryEvent.Register<Block> event, Block startingBlock, String path) {
        return setup(event, startingBlock, path, EsteemedInnovation.tab);
    }

    /**
     * Sets up a Block with an unlocalized name, a registry name, an optional creative tab, and registers it to the
     * Block registry.
     * @param event The event to register on
     * @param startingBlock The block to start with
     * @param path The name of the block, excluding the EI mod ID
     * @param tab The creative tab to add it to. Use null to not add it to any tab. You will have to cast the null to
     *            {@link CreativeTabs}.
     * @return The registered block (ItemBlock is not returned).
     */
    protected Block setup(RegistryEvent.Register<Block> event, Block startingBlock, String path, CreativeTabs tab) {
        startingBlock.setUnlocalizedName(Constants.EI_MODID + ":" + path);
        if (tab != null) {
            startingBlock.setCreativeTab(tab);
        }
        startingBlock.setRegistryName(Constants.EI_MODID, path);
        event.getRegistry().register(startingBlock);
        return startingBlock;
    }

    protected void setupItemBlock(RegistryEvent.Register<Item> event, Block startingBlock) {
        setupItemBlock(event, startingBlock, EsteemedInnovation.tab);
    }

    protected void setupItemBlock(RegistryEvent.Register<Item> event, Block startingBlock, Function<Block, ItemBlock> itemBlock) {
        setupItemBlock(event, startingBlock, EsteemedInnovation.tab, itemBlock);
    }

    protected void setupItemBlock(RegistryEvent.Register<Item> event, Block startingBlock, CreativeTabs tab) {
        setupItemBlock(event, startingBlock, tab, ItemBlock::new);
    }

    protected void setupItemBlock(RegistryEvent.Register<Item> event, Block startingBlock, CreativeTabs tab, Function<Block, ItemBlock> itemBlock) {
        ItemBlock item = itemBlock.apply(startingBlock);
        if (tab != null) {
            item.setCreativeTab(tab);
        }
        //item.setUnlocalizedName(Constants.EI_MODID + ":" + path);
        item.setRegistryName(startingBlock.getRegistryName());
        event.getRegistry().register(item);
    }

    /**
     * Registers a block model for all of the provided variants. This registers it specifically for the ItemBlock.
     * @param block The block
     * @param name The name of the property as passed to IProperty.create
     * @param variants All of the variants (probably Enum.values())
     */
    protected void registerModelAllVariants(Block block, String name, IStringSerializable[] variants) {
        registerModelAllVariants(Item.getItemFromBlock(block), name, variants);
    }

    /**
     * Registers an item model for all of the provided variants
     * @param item The item
     * @param name see #registerModelAllVariants
     * @param variants see #registerModelAllVariants
     */
    protected void registerModelAllVariants(Item item, String name, IStringSerializable[] variants) {
        for (int i = 0; i < variants.length; i++) {
            IStringSerializable string = variants[i];
            registerModel(item, i, name + "=" + string.getName());
        }
    }

    /**
     * Registers the block's model for metadata 0 and variant "inventory".
     * @param block the block
     */
    protected void registerModel(Block block) {
        registerModel(block, 0);
    }

    /**
     * Overload for {@link #registerModel(Block, int, String)} that passes "inventory" as the variant.
     */
    protected void registerModel(Block block, int meta)  {
        registerModel(block, meta, "inventory");
    }

    /**
     * Overload for {@link #registerModel(Item, int, String)} that passes the ItemBlock for the provided Block as the item.
     */
    protected void registerModel(Block block, int meta, String variant) {
        registerModel(Item.getItemFromBlock(block), meta, variant);
    }

    /**
     * Registers the item's model for metadata 0 and variant "inventory".
     * @param item the item
     */
    protected void registerModel(Item item) {
        registerModel(item, 0);
    }

    /**
     * Registers an item's model with a specific metadata value and variant "inventory".
     * @param item the item
     * @param meta the metadata
     */
    protected void registerModel(Item item, int meta) {
        registerModel(item, meta, "inventory");
    }

    /**
     * Registers the item's model with the according variant.
     * @param item The item
     * @param meta The metadata
     * @param variant The variant. If a specific property, it should likely be "property=name"
     */
    protected void registerModel(Item item, int meta, String variant) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), variant));
    }

    /**
     * Overload for {@link #registerModelItemStack(ItemStack, String)} that passes "inventory" as the variant.
     */
    protected void registerModelItemStack(ItemStack stack) {
        registerModelItemStack(stack, "inventory");
    }

    /**
     * Registers an item model for the given itemstack, based on its unlocalized name.
     */
    protected void registerModelItemStack(ItemStack stack, String variant) {
        Item item = stack.getItem();
        String name = item.getRegistryName() + "." + stack.getItemDamage();
        ModelLoader.setCustomModelResourceLocation(item, stack.getItemDamage(), new ModelResourceLocation(name, variant));
    }

    public void registerTileEntity(Class<? extends TileEntity> clazz, String key) {
        GameRegistry.registerTileEntity(clazz, EsteemedInnovation.MOD_ID + ":" + key);
    }

    public static IRecipe addRecipe(RegistryEvent.Register<IRecipe> event, boolean createBookRecipeRegistry, String recipeName, Block block, Object... obj) {
        return addRecipe(event, createBookRecipeRegistry, recipeName, new ItemStack(block), obj);
    }

    public static IRecipe addRecipe(RegistryEvent.Register<IRecipe> event, boolean createBookRecipeRegistry, String recipeName, Item item, Object... obj) {
        return addRecipe(event, createBookRecipeRegistry, recipeName, new ItemStack(item), obj);
    }

    public static IRecipe addRecipe(RegistryEvent.Register<IRecipe> event, boolean createBookRecipeRegistry, String recipeName, ItemStack result, Object... obj) {
        ResourceLocation group = new ResourceLocation(Constants.EI_MODID, recipeName);
        ShapedOreRecipe recipe = new ShapedOreRecipe(group, result, obj);
        return addRecipe(event, createBookRecipeRegistry, recipeName, recipe);
    }

    public static IRecipe addShapelessRecipe(RegistryEvent.Register<IRecipe> event, boolean createBookRecipeRegistry, String recipeName, Block block, Object... obj) {
        return addShapelessRecipe(event, createBookRecipeRegistry, recipeName, new ItemStack(block), obj);
    }

    public static IRecipe addShapelessRecipe(RegistryEvent.Register<IRecipe> event, boolean createBookRecipeRegistry, String recipeName, Item item, Object... obj) {
        return addShapelessRecipe(event, createBookRecipeRegistry, recipeName, new ItemStack(item), obj);
    }

    public static IRecipe addShapelessRecipe(RegistryEvent.Register<IRecipe> event, boolean createBookRecipeRegistry, String recipeName, ItemStack result, Object... obj) {
        ResourceLocation group = new ResourceLocation(Constants.EI_MODID, recipeName);
        ShapelessOreRecipe recipe = new ShapelessOreRecipe(group, result, obj);
        return addRecipe(event, createBookRecipeRegistry, recipeName, recipe);
    }

    public static IRecipe addRecipe(RegistryEvent.Register<IRecipe> event, boolean createBookRecipeRegistry, String recipeName, IRecipe recipe) {
        recipe.setRegistryName(new ResourceLocation(Constants.EI_MODID, recipeName));
        event.getRegistry().register(recipe);
        if (createBookRecipeRegistry) {
            BookRecipeRegistry.addRecipe(recipeName, recipe);
        }
        return recipe;
    }

    public static IRecipe add3x3Recipe(RegistryEvent.Register<IRecipe> event, boolean createBookRecipeRegistry, String recipeName, ItemStack output, String input) {
        return addRecipe(event, createBookRecipeRegistry, recipeName, output,
          "xxx",
          "xxx",
          "xxx",
          'x', input);
    }
}
