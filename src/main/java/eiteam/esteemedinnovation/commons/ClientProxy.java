package eiteam.esteemedinnovation.commons;

import codechicken.lib.render.CCIconRegister;
import eiteam.esteemedinnovation.api.enhancement.UtilEnhancements;
import eiteam.esteemedinnovation.api.entity.EntityRocket;
import eiteam.esteemedinnovation.armor.exosuit.ExosuitItemModelLoader;
import eiteam.esteemedinnovation.armor.exosuit.ExosuitModelCache;
import eiteam.esteemedinnovation.armor.exosuit.ItemExosuitArmor;
import eiteam.esteemedinnovation.armor.exosuit.ItemExosuitColorHandler;
import eiteam.esteemedinnovation.boiler.GuiBoiler;
import eiteam.esteemedinnovation.charging.TileEntityChargingPad;
import eiteam.esteemedinnovation.charging.TileEntityChargingPadRenderer;
import eiteam.esteemedinnovation.charging.TileEntitySteamCharger;
import eiteam.esteemedinnovation.charging.TileEntitySteamChargerRenderer;
import eiteam.esteemedinnovation.commons.particle.ParticleAlphabeticGeneric;
import eiteam.esteemedinnovation.converter.TileEntityFluidSteamConverter;
import eiteam.esteemedinnovation.converter.TileEntityFluidSteamRenderer;
import eiteam.esteemedinnovation.firearms.rocket.ammo.RenderRocket;
import eiteam.esteemedinnovation.init.blocks.*;
import eiteam.esteemedinnovation.init.items.*;
import eiteam.esteemedinnovation.init.items.armor.ArmorItems;
import eiteam.esteemedinnovation.init.items.armor.ExosuitUpgradeItems;
import eiteam.esteemedinnovation.init.items.firearms.FirearmAmmunitionItems;
import eiteam.esteemedinnovation.init.items.firearms.FirearmItems;
import eiteam.esteemedinnovation.init.items.firearms.FirearmUpgradeItems;
import eiteam.esteemedinnovation.init.items.tools.GadgetItems;
import eiteam.esteemedinnovation.init.items.tools.ToolItems;
import eiteam.esteemedinnovation.init.items.tools.ToolUpgradeItems;
import eiteam.esteemedinnovation.metalcasting.BlockCrucible;
import eiteam.esteemedinnovation.metalcasting.TileEntityCrucible;
import eiteam.esteemedinnovation.metalcasting.TileEntityCrucibleRenderer;
import eiteam.esteemedinnovation.metalcasting.mold.TileEntityMold;
import eiteam.esteemedinnovation.metalcasting.mold.TileEntityMoldRenderer;
import eiteam.esteemedinnovation.metals.raw.BlockGenericOre;
import eiteam.esteemedinnovation.metals.refined.BlockBeacon;
import eiteam.esteemedinnovation.misc.PlayerController;
import eiteam.esteemedinnovation.hammer.TileEntitySteamHammer;
import eiteam.esteemedinnovation.hammer.TileEntitySteamHammerRenderer;
import eiteam.esteemedinnovation.smasher.ItemSmashedOre;
import eiteam.esteemedinnovation.smasher.ItemSmashedOreColorHandler;
import eiteam.esteemedinnovation.smasher.TileEntitySmasher;
import eiteam.esteemedinnovation.smasher.TileEntitySmasherRenderer;
import eiteam.esteemedinnovation.thumper.TileEntityThumper;
import eiteam.esteemedinnovation.thumper.TileEntityThumperRenderer;
import eiteam.esteemedinnovation.steamsafety.gauge.TileEntitySteamGauge;
import eiteam.esteemedinnovation.steamsafety.gauge.TileEntitySteamGaugeRenderer;
import eiteam.esteemedinnovation.storage.item.canister.EntityCanisterItem;
import eiteam.esteemedinnovation.storage.item.canister.RenderCanister;
import eiteam.esteemedinnovation.tools.steam.SteamDrillColorHandler;
import eiteam.esteemedinnovation.tools.steam.SteamToolModelLoader;
import eiteam.esteemedinnovation.tools.steam.upgrades.drillhead.SteamDrillHeadUpgradeColorHandler;
import eiteam.esteemedinnovation.transport.entity.TileEntityFan;
import eiteam.esteemedinnovation.transport.entity.TileEntityFanRenderer;
import eiteam.esteemedinnovation.transport.entity.TileEntityVacuum;
import eiteam.esteemedinnovation.transport.entity.TileEntityVacuumRenderer;
import eiteam.esteemedinnovation.transport.fluid.screw.TileEntityPump;
import eiteam.esteemedinnovation.transport.fluid.screw.TileEntityPumpRenderer;
import eiteam.esteemedinnovation.transport.item.EntityMortarItem;
import eiteam.esteemedinnovation.transport.item.RenderMortarItem;
import eiteam.esteemedinnovation.transport.item.TileEntityItemMortar;
import eiteam.esteemedinnovation.transport.item.TileEntityItemMortarRenderer;
import eiteam.esteemedinnovation.transport.steam.StateMapperValvePipe;
import eiteam.esteemedinnovation.transport.steam.TileEntityValvePipe;
import eiteam.esteemedinnovation.transport.steam.TileEntityValvePipeRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ClientProxy extends CommonProxy {
    public static HashMap<String, KeyBinding> keyBindings = new HashMap<>();
    public static final ResourceLocation FONT_ASCII = new ResourceLocation("minecraft", "textures/font/ascii.png");

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
        ModelLoaderRegistry.registerLoader(new SteamToolModelLoader());
        ModelLoaderRegistry.registerLoader(new ExosuitItemModelLoader());

        registerModel(SteamNetworkBlocks.Blocks.BOILER.getBlock());
        registerModel(SteamNetworkBlocks.Blocks.TANK.getBlock(), 0, "is_creative=false");
        registerModel(SteamNetworkBlocks.Blocks.TANK.getBlock(), 1, "is_creative=true");
        registerModel(PipeBlocks.Blocks.BRASS_PIPE.getBlock());
        ModelLoader.setCustomStateMapper(SteamNetworkBlocks.Blocks.VALVE_PIPE.getBlock(), new StateMapperValvePipe());
        registerModel(SteamNetworkBlocks.Blocks.VALVE_PIPE.getBlock());
        registerModelItemStack(new ItemStack(SteamNetworkBlocks.Blocks.RUPTURE_DISC.getBlock(), 1, 0));
        registerModelItemStack(new ItemStack(SteamNetworkBlocks.Blocks.RUPTURE_DISC.getBlock(), 1, 1));
        registerModel(SteamNetworkBlocks.Blocks.STEAM_GAUGE.getBlock());
        registerModel(SteamNetworkBlocks.Blocks.STEAM_WHISTLE.getBlock());
        registerModel(SteamNetworkBlocks.Blocks.PRESSURE_CONVERTER.getBlock());

        registerModel(SteamMachineryBlocks.Blocks.FAN.getBlock());
        registerModel(SteamMachineryBlocks.Blocks.STEAM_HEATER.getBlock());
        registerModel(SteamMachineryBlocks.Blocks.VACUUM.getBlock());
        registerModel(SteamMachineryBlocks.Blocks.STEAM_FILLER.getBlock());
        registerModel(SteamMachineryBlocks.Blocks.THUMPER.getBlock());
        registerModel(SteamMachineryBlocks.Blocks.THUMPER_DUMMY.getBlock());
        registerModel(SteamMachineryBlocks.Blocks.ROCK_SMASHER.getBlock());
        registerModel(SteamMachineryBlocks.Blocks.FILLING_PAD.getBlock());
        registerModel(SteamMachineryBlocks.Blocks.ITEM_MORTAR.getBlock());

        registerModelAllVariants(MetalBlocks.Blocks.BLOCK, BlockBeacon.VARIANT.getName(),
          BlockBeacon.MetalBlockTypes.values());

        registerModelAllVariants(OreBlocks.Blocks.ORE_BLOCK, BlockGenericOre.VARIANT.getName(),
          BlockGenericOre.OreBlockTypes.LOOKUP);

        registerModel(MiscellaneousBlocks.Blocks.ENGINEERING_TABLE.getBlock());
        registerModel(MiscellaneousBlocks.Blocks.FUNNEL.getBlock());

        for (int i = 0; i < 4; i++) {
            String variant = "variant=" + (i % 2 == 0 ? "copper" : "zinc") + ",worked_out=" + (i > 1 ? "true" : "false");
            registerModel(MiscellaneousBlocks.Blocks.ORE_DEPOSIT_BLOCK.getBlock(), i, variant);
        }

        registerModel(CastingBlocks.Blocks.CRUCIBLE.getBlock());
        registerModel(CastingBlocks.Blocks.NETHER_CRUCIBLE.getBlock());
        registerModel(CastingBlocks.Blocks.CARVING_TABLE.getBlock());
        registerModel(CastingBlocks.Blocks.MOLD.getBlock());

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
            registerModel(item.getItem());
        }

        for (ToolUpgradeItems.Items item : ToolUpgradeItems.Items.LOOKUP) {
            registerModel(item.getItem());
        }

        for (FirearmUpgradeItems.Items item : FirearmUpgradeItems.Items.LOOKUP) {
            registerModel(item.getItem());
        }

        for (FirearmAmmunitionItems.Items item : FirearmAmmunitionItems.Items.LOOKUP) {
            registerModel(item.getItem());
        }

        for (NaturalPhilosophyItems.Items item : NaturalPhilosophyItems.Items.LOOKUP) {
            registerModel(item.getItem());
        }

        for (ArmorItems.Items item : ArmorItems.Items.LOOKUP) {
            registerModel(item.getItem());
        }

        for (ExosuitUpgradeItems.Items item : ExosuitUpgradeItems.Items.LOOKUP) {
            if (item == ExosuitUpgradeItems.Items.EXOSUIT_PLATE) {
                for (ExosuitUpgradeItems.PlateItems plate : ExosuitUpgradeItems.PlateItems.LOOKUP) {
                    registerModelItemStack(plate.createItemStack());
                }
            } else {
                registerModel(item.getItem());
            }
        }

        // TODO: Durability bars for load progress and current ammo contents.
        for (FirearmItems.Items item : FirearmItems.Items.LOOKUP) {
            List<ModelResourceLocation> locations = UtilEnhancements.registerEnhancementsForItem(item.getItem())
              .stream()
              .map(r -> new ModelResourceLocation(r, "inventory"))
              .collect(Collectors.toList());
            // No enhancement.
            locations.add(new ModelResourceLocation(item.getItem().getRegistryName(), "inventory"));
            ModelBakery.registerItemVariants(item.getItem(), locations.toArray(new ModelResourceLocation[locations.size()]));
            ModelLoader.setCustomMeshDefinition(item.getItem(), stack -> {
                ResourceLocation loc = UtilEnhancements.getIconFromEnhancement(stack);
                if (loc == null) {
                    loc = stack.getItem().getRegistryName();
                }
                return new ModelResourceLocation(loc, "inventory");
            });
        }
    }

    @Override
    public void registerHotkeys() {
        keyBindings.put("monocle", new KeyBinding("key.monocle.desc", Keyboard.KEY_Z, "key.esteemedinnovation.category"));

        keyBindings.values().forEach(ClientRegistry::registerKeyBinding);
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

        MinecraftForge.EVENT_BUS.register(ExosuitModelCache.INSTANCE);

        RenderingRegistry.registerEntityRenderingHandler(EntityMortarItem.class, RenderMortarItem::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityCanisterItem.class, RenderCanister::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityRocket.class, RenderRocket::new);

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
    public void registerTexturesToStitch() {
        CCIconRegister.registerTexture(BlockCrucible.LIQUID_ICON_RL);
        CCIconRegister.registerTexture(GuiBoiler.STEAM_RL);
    }

    @Override
    public void spawnBreakParticles(World world, float x, float y, float z, Block block, float xv, float yv, float zv) {
//      The first argument of getEntityFX is the particle ID, and it is not used in the method at all.
        spawnParticles(new ParticleDigging.Factory().getEntityFX(0, world, x, y, z, xv, yv, zv, 2));
    }

    @Override
    public void spawnAsteriskParticles(World world, float x, float y, float z) {
        // #00c300
        for (int i = 0; i < world.rand.nextInt(4) + 1; i++) {
            spawnParticles(new ParticleAlphabeticGeneric(world, x, y, z, 0, 0.765F, 0, 0.75F, 10, 2));
        }
    }

    @Override
    public void spawnExclamationParticles(World world, float x, float y, float z) {
        // #FFD700
        for (int i = 0; i < world.rand.nextInt(4) + 1; i++) {
            spawnParticles(new ParticleAlphabeticGeneric(world, x, y, z, 1, 0.843F, 0, 0.75F, 1, 2));
        }
    }

    private void spawnParticles(Particle particle) {
        if (!Config.disableParticles) {
            Minecraft.getMinecraft().effectRenderer.addEffect(particle);
        }
    }

    private void setController(Minecraft minecraft) {
        PlayerControllerMP controller = minecraft.playerController;
        World world = minecraft.theWorld;
        if (!(controller instanceof PlayerController)) {
            GameType type = world.getWorldInfo().getGameType();
            NetHandlerPlayClient net = minecraft.getConnection();
            PlayerController ourController = new PlayerController(minecraft, net);
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

            ((PlayerController) mc.playerController).setReachDistanceExtension(((PlayerController) mc.playerController).getReachDistanceExtension() + amount);
        }
    }

    @Override
    public void checkRange(EntityLivingBase entity) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;
        if (entity == player) {
            setController(mc);
            if (((PlayerController) mc.playerController).getReachDistanceExtension() <= 2.0F) {
                extendRange(entity, 2.0F - ((PlayerController) mc.playerController).getReachDistanceExtension());
            }
        }
    }

}