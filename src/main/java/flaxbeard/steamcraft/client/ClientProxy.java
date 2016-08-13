package flaxbeard.steamcraft.client;

import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.api.enhancement.UtilEnhancements;
import flaxbeard.steamcraft.api.tool.ISteamTool;
import flaxbeard.steamcraft.block.BlockBeacon;
import flaxbeard.steamcraft.block.BlockSteamcraftOre;
import flaxbeard.steamcraft.client.render.colorhandlers.*;
import flaxbeard.steamcraft.client.render.entity.*;
import flaxbeard.steamcraft.client.render.item.ItemFirearmRenderer;
import flaxbeard.steamcraft.client.render.item.ItemSteamToolRenderer;
import flaxbeard.steamcraft.client.render.model.exosuit.ExosuitModelCache;
import flaxbeard.steamcraft.client.render.tile.*;
import flaxbeard.steamcraft.common.CommonProxy;
import flaxbeard.steamcraft.data.state.StateMapperValvePipe;
import flaxbeard.steamcraft.entity.item.EntityCanisterItem;
import flaxbeard.steamcraft.entity.item.EntityMortarItem;
import flaxbeard.steamcraft.entity.projectile.EntityRocket;
import flaxbeard.steamcraft.init.blocks.*;
import flaxbeard.steamcraft.init.items.CraftingComponentItems;
import flaxbeard.steamcraft.init.items.FoodItems;
import flaxbeard.steamcraft.init.items.MetalItems;
import flaxbeard.steamcraft.init.items.MetalcastingItems;
import flaxbeard.steamcraft.init.items.armor.ArmorItems;
import flaxbeard.steamcraft.init.items.firearms.FirearmAmmunitionItems;
import flaxbeard.steamcraft.init.items.firearms.FirearmItems;
import flaxbeard.steamcraft.init.items.tools.GadgetItems;
import flaxbeard.steamcraft.init.items.tools.ToolItems;
import flaxbeard.steamcraft.init.items.tools.ToolUpgradeItems;
import flaxbeard.steamcraft.item.ItemSmashedOre;
import flaxbeard.steamcraft.item.armor.exosuit.ItemExosuitArmor;
import flaxbeard.steamcraft.misc.SteamcraftPlayerController;
import flaxbeard.steamcraft.tile.*;
import codechicken.lib.render.ModelRegistryHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;

public class ClientProxy extends CommonProxy {
    public static HashMap<String, KeyBinding> keyBindings = new HashMap<>();

    /**
     * Registers a block model for all of the provided variants. This registers it specifically for the ItemBlock.
     * @param block The block
     * @param name The name of the property as passed to IProperty.create
     * @param variants All of the variants (probably Enum.values())
     */
    private void registerModelAllVariants(Block block, String name, IStringSerializable[] variants) {
        registerModelAllVariants(Item.getItemFromBlock(block), name, variants);
    }

    /**
     * Registers an item model for all of the provided variants
     * @param item The item
     * @param name see #registerModelAllVariants
     * @param variants see #registerModelAllVariants
     */
    private void registerModelAllVariants(Item item, String name, IStringSerializable[] variants) {
        for (int i = 0; i < variants.length; i++) {
            IStringSerializable string = variants[i];
            registerModel(item, i, name + "=" + string.getName());
        }
    }

    /**
     * Registers the block's model for metadata 0 and variant "inventory".
     * @param block the block
     */
    private void registerModel(Block block) {
        registerModel(block, 0);
    }

    private void registerModel(Block block, int meta)  {
        registerModel(block, meta, "inventory");
    }

    private void registerModel(Block block, int meta, String variant) {
        registerModel(Item.getItemFromBlock(block), meta, variant);
    }

    /**
     * Registers the item's model for metadata 0 and variant "inventory".
     * @param item the item
     */
    private void registerModel(Item item) {
        registerModel(item, 0);
    }

    /**
     * Registers an item's model with a specific metadata value and variant "inventory".
     * @param item the item
     * @param meta the metadata
     */
    private void registerModel(Item item, int meta) {
        registerModel(item, meta, "inventory");
    }

    /**
     * Registers the item's model with the according variant.
     * @param item The item
     * @param meta The metadata
     * @param variant The variant. If a specific property, it should likely be "property=name"
     */
    private void registerModel(Item item, int meta, String variant) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), variant));
    }

    private void registerModelItemStack(ItemStack stack) {
        registerModelItemStack(stack, "inventory");
    }

    /**
     * Registers an item model for the given itemstack, based on its unlocalized name.
     */
    private void registerModelItemStack(ItemStack stack, String variant) {
        Item item = stack.getItem();
        String name = item.getRegistryName() + "." + stack.getItemDamage();
        ModelLoader.setCustomModelResourceLocation(item, stack.getItemDamage(), new ModelResourceLocation(name, variant));
    }

    @Override
    public void registerModels() {
        registerModel(SteamNetworkBlocks.Blocks.BOILER.getBlock());
        registerModel(SteamNetworkBlocks.Blocks.TANK.getBlock(), 0, "is_creative=false");
        registerModel(SteamNetworkBlocks.Blocks.TANK.getBlock(), 1, "is_creative=true");
        registerModel(SteamNetworkBlocks.Blocks.PIPE.getBlock());
        ModelLoader.setCustomStateMapper(SteamNetworkBlocks.Blocks.VALVE_PIPE.getBlock(), new StateMapperValvePipe());
        registerModel(SteamNetworkBlocks.Blocks.VALVE_PIPE.getBlock());
        registerModelItemStack(new ItemStack(SteamNetworkBlocks.Blocks.RUPTURE_DISC.getBlock(), 1, 0));
        registerModelItemStack(new ItemStack(SteamNetworkBlocks.Blocks.RUPTURE_DISC.getBlock(), 1, 1));
        registerModel(SteamNetworkBlocks.Blocks.STEAM_GAUGE.getBlock());
        registerModel(SteamNetworkBlocks.Blocks.STEAM_WHISTLE.getBlock());

        registerModel(SteamMachineryBlocks.Blocks.FAN.getBlock());
        registerModel(SteamMachineryBlocks.Blocks.STEAM_HEATER.getBlock());

        registerModelAllVariants(MetalBlocks.Blocks.BLOCK, BlockBeacon.VARIANT.getName(),
          BlockBeacon.MetalBlockTypes.values());

        registerModelAllVariants(OreBlocks.Blocks.ORE_BLOCK, BlockSteamcraftOre.VARIANT.getName(),
          BlockSteamcraftOre.OreBlockTypes.LOOKUP);

        registerModel(MiscellaneousBlocks.Blocks.ENGINEERING_TABLE.getBlock());

        registerModel(CastingBlocks.Blocks.CRUCIBLE.getBlock());
        registerModel(CastingBlocks.Blocks.NETHER_CRUCIBLE.getBlock());
        registerModel(CastingBlocks.Blocks.CARVING_TABLE.getBlock());

        for (CraftingComponentItems.Items item : CraftingComponentItems.Items.values()) {
            registerModelItemStack(item.createItemStack());
        }
        for (FoodItems.Items item : FoodItems.Items.values()) {
            registerModel(item.getItem());
        }
        for (MetalcastingItems.Items item : MetalcastingItems.Items.values()) {
            registerModel(item.getItem());
        }

        {
            Item item = MetalItems.Items.SMASHED_ORE.getItem();
            for (Integer meta : ItemSmashedOre.map.keySet()) {
                registerModel(item, meta);
            }
        }

        for (MetalItems.Items item : MetalItems.Items.values()) {
            // Skip the smashed ore because we just registered its model.
            if (item.hasType()) {
                registerModelItemStack(item.createItemStack());
            }
        }

        for (GadgetItems.Items item : GadgetItems.Items.LOOKUP) {
            registerModel(item.getItem());
        }

        for (ToolItems.Items item : ToolItems.Items.LOOKUP) {
            if (!(item.getItem() instanceof ISteamTool)) {
                registerModel(item.getItem());
            }
        }

        for (ToolUpgradeItems.Items item : ToolUpgradeItems.Items.LOOKUP) {
            registerModel(item.getItem());
        }

        for (FirearmAmmunitionItems.Items item : FirearmAmmunitionItems.Items.LOOKUP) {
            registerModel(item.getItem());
        }
    }

    @Override
    public void registerHotkeys() {
        keyBindings.put("monocle", new KeyBinding("key.monocle.desc", Keyboard.KEY_Z, "key.flaxbeard.category"));

        for (KeyBinding bind : keyBindings.values()) {
            ClientRegistry.registerKeyBinding(bind);
        }
    }

    @Override
    public void registerRenderers() {
        ItemColors colors = Minecraft.getMinecraft().getItemColors();
        colors.registerItemColorHandler(new ItemSmashedOreColorHandler(), MetalItems.Items.SMASHED_ORE.getItem());
        for (ArmorItems.Items item : ArmorItems.Items.LOOKUP) {
            if (item.getItem() instanceof ItemExosuitArmor) {
                colors.registerItemColorHandler(new ItemExosuitColorHandler(item), item.getItem());
            }
        }
        colors.registerItemColorHandler(new SteamDrillColorHandler(), ToolItems.Items.STEAM_DRILL.getItem());
        colors.registerItemColorHandler(new SteamDrillHeadUpgradeColorHandler(), ToolUpgradeItems.Items.DRILL_HEAD.getItem());

        for (FirearmItems.Items item : FirearmItems.Items.LOOKUP) {
            UtilEnhancements.registerEnhancementsForItem(item.getItem());
            ModelRegistryHelper.registerItemRenderer(item.getItem(), new ItemFirearmRenderer());
        }
        ModelRegistryHelper.registerItemRenderer(ToolItems.Items.STEAM_DRILL.getItem(), new ItemSteamToolRenderer());
        ModelRegistryHelper.registerItemRenderer(ToolItems.Items.STEAM_SAW.getItem(), new ItemSteamToolRenderer());
        ModelRegistryHelper.registerItemRenderer(ToolItems.Items.STEAM_SHOVEL.getItem(), new ItemSteamToolRenderer());

        MinecraftForge.EVENT_BUS.register(ExosuitModelCache.INSTANCE);
        FMLCommonHandler.instance().bus().register(ExosuitModelCache.INSTANCE);

        RenderingRegistry.registerEntityRenderingHandler(EntityMortarItem.class, new IRenderFactory<EntityMortarItem>() {
            @Override
            public Render<? super EntityMortarItem> createRenderFor(RenderManager manager) {
                return new RenderMortarItem(manager);
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(EntityCanisterItem.class, new IRenderFactory<EntityCanisterItem>() {
            @Override
            public Render<? super EntityCanisterItem> createRenderFor(RenderManager manager) {
                return new RenderCanister(manager);
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(EntityRocket.class, new IRenderFactory<EntityRocket>() {
            @Override
            public Render<? super EntityRocket> createRenderFor(RenderManager manager) {
                return new RenderRocket(manager);
            }
        });

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrucible.class, new TileEntityCrucibleRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMold.class, new TileEntityMoldRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySteamCharger.class, new TileEntitySteamChargerRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySteamGauge.class, new TileEntitySteamGaugeRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySteamHammer.class, new TileEntitySteamHammerRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityItemMortar.class, new TileEntityItemMortarRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPump.class, new TileEntityPumpRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityValvePipe.class, new TileEntityValvePipeRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySmasher.class, new TileEntitySmasherRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityThumper.class, new TileEntityThumperRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFan.class, new TileEntityFanRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityVacuum.class, new TileEntityVacuumRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidSteamConverter.class, new TileEntityFluidSteamRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChargingPad.class, new TileEntityChargingPadRenderer());
    }

    @Override
    public void spawnBreakParticles(World world, float x, float y, float z, Block block, float xv, float yv, float zv) {
        if (!Config.disableParticles) {
            // The first argument of getEntityFX is the particle ID, and it is not used in the method at all.
            Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleDigging.Factory().getEntityFX(0, world, x, y, z, xv, yv, zv, 2));
        }
    }

    private void setController(Minecraft minecraft) {
        PlayerControllerMP controller = minecraft.playerController;
        World world = minecraft.theWorld;
        if (!(controller instanceof SteamcraftPlayerController)) {
            GameType type = world.getWorldInfo().getGameType();
            NetHandlerPlayClient net = minecraft.getConnection();
            SteamcraftPlayerController ourController = new SteamcraftPlayerController(minecraft, net);
            ourController.setGameType(type);
            minecraft.playerController = ourController;
        }
    }

    @Override
    public void extendRange(Entity entity, double amount) {
        super.extendRange(entity, amount);
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;
        if (entity == player) {
            setController(mc);

            ((SteamcraftPlayerController) mc.playerController).setReachDistanceExtension(((SteamcraftPlayerController) mc.playerController).getReachDistanceExtension() + amount);
        }
    }

    @Override
    public void checkRange(EntityLivingBase entity) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;
        if (entity == player) {
            setController(mc);
            if (((SteamcraftPlayerController) mc.playerController).getReachDistanceExtension() <= 2.0F) {
                extendRange(entity, 2.0F - ((SteamcraftPlayerController) mc.playerController).getReachDistanceExtension());
            }
        }
    }

}