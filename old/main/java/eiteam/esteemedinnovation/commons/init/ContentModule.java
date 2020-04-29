package eiteam.esteemedinnovation.commons.init;

import eiteam.esteemedinnovation.api.Constants;
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
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
 * <br />
 * To handle configuration options, your ContentModule class should inherit {@link ConfigurableModule}.
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
    public void postInitClient() {
    }

    /**
     * Handle any finishing up here. This is called in postInit on client and server after {@link #postInitClient()}.
     * @param side
     */
    public void finish(Side side) {}

    /**
     * Overload for {@link #setup(RegistryEvent.Register, Item, String, CreativeTabs)} that uses {@link EsteemedInnovation#tab} for the tab argument.
     * @param event
     * @param startingItem
     * @param path
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
        startingItem.setTranslationKey(Constants.EI_MODID + ":" + path);
        if (tab != null) {
            startingItem.setCreativeTab(tab);
        }
        startingItem.setRegistryName(Constants.EI_MODID, path);
        event.getRegistry().register(startingItem);
        return startingItem;
    }

    /**
     * Overload for {@link #setup(RegistryEvent.Register, Block, String, CreativeTabs)} that uses {@link EsteemedInnovation#tab} for the tab argument.
     * @param event
     * @param startingBlock
     * @param path
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
     *                      {@link CreativeTabs}.
     * @return The registered block (ItemBlock is not returned).
     */
    protected Block setup(RegistryEvent.Register<Block> event, Block startingBlock, String path, CreativeTabs tab) {
        startingBlock.setTranslationKey(Constants.EI_MODID + ":" + path);
        if (tab != null) {
            startingBlock.setCreativeTab(tab);
        }
        startingBlock.setRegistryName(Constants.EI_MODID, path);
        event.getRegistry().register(startingBlock);
        return startingBlock;
    }


    /**
     * Overload for {@link #setupItemBlock(RegistryEvent.Register, Block, CreativeTabs)} which uses {@link EsteemedInnovation#tab} for the tab argument.
     * @param event
     * @param startingBlock
     */
    protected void setupItemBlock(RegistryEvent.Register<Item> event, Block startingBlock) {
        setupItemBlock(event, startingBlock, EsteemedInnovation.tab);
    }


    /**
     * Overload for {@link #setupItemBlock(RegistryEvent.Register, Block, CreativeTabs, Function)} which uses {@link EsteemedInnovation#tab} for the tab argument.
     * @param event
     * @param startingBlock
     * @param itemBlock
     */
    protected void setupItemBlock(RegistryEvent.Register<Item> event, Block startingBlock, Function<Block, ItemBlock> itemBlock) {
        setupItemBlock(event, startingBlock, EsteemedInnovation.tab, itemBlock);
    }

    /**
     * Overload for {@link #setupItemBlock(RegistryEvent.Register, Block, CreativeTabs, Function)} which uses {@link ItemBlock}'s constructor for the function argument.
     * @param event
     * @param startingBlock
     * @param tab
     */
    protected void setupItemBlock(RegistryEvent.Register<Item> event, Block startingBlock, CreativeTabs tab) {
        setupItemBlock(event, startingBlock, tab, ItemBlock::new);
    }

    /**
     * Sets up an ItemBlock with the {@param startingBlock}'s registry name, an optional creative tab, and registers it to the Item registry.
     * @param event The event to register on
     * @param startingBlock The block to register an {@link ItemBlock} with
     * @param tab The creative tab to add it to. Use null to not add it to any tab. You will have to cast the null to {@link CreativeTabs}.
     * @param itemBlock The Function called to create the ItemBlock
     */
    protected void setupItemBlock(RegistryEvent.Register<Item> event, Block startingBlock, CreativeTabs tab, Function<Block, ItemBlock> itemBlock) {
        ItemBlock item = itemBlock.apply(startingBlock);
        if (tab != null) {
            item.setCreativeTab(tab);
        }
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
     * @param block
     * @param meta
     */
    protected void registerModel(Block block, int meta) {
        registerModel(block, meta, "inventory");
    }

    /**
     * Overload for {@link #registerModel(Item, int, String)} that passes the ItemBlock for the provided Block as the item.
     * @param block
     * @param meta
     * @param variant
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
     * @param stack
     */
    protected void registerModelItemStack(ItemStack stack) {
        registerModelItemStack(stack, "inventory");
    }

    /**
     * Registers an item model for the given itemstack, based on its registry name.
     * @param stack
     * @param variant
     */
    protected void registerModelItemStack(ItemStack stack, String variant) {
        Item item = stack.getItem();
        String name = item.getRegistryName() + "." + stack.getItemDamage();
        ModelLoader.setCustomModelResourceLocation(item, stack.getItemDamage(), new ModelResourceLocation(name, variant));
    }

    public void registerTileEntity(Class<? extends TileEntity> clazz, String key) {
        GameRegistry.registerTileEntity(clazz, EsteemedInnovation.MOD_ID + ":" + key);
    }
}
