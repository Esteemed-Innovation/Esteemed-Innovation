package flaxbeard.steamcraft.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.client.render.*;
import flaxbeard.steamcraft.client.render.model.exosuit.ExosuitModelCache;
import flaxbeard.steamcraft.common.CommonProxy;
import flaxbeard.steamcraft.entity.EntityCanisterItem;
import flaxbeard.steamcraft.entity.EntityMortarItem;
import flaxbeard.steamcraft.entity.EntityRocket;
import flaxbeard.steamcraft.entity.EntitySteamHorse;
import flaxbeard.steamcraft.integration.BotaniaIntegration;
import flaxbeard.steamcraft.item.ItemExosuitArmor;
import flaxbeard.steamcraft.misc.SteamcraftPlayerController;
import flaxbeard.steamcraft.packet.SteamcraftClientPacketHandler;
import flaxbeard.steamcraft.tile.*;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import org.lwjgl.input.Keyboard;


public class ClientProxy extends CommonProxy {
    public static final ResourceLocation villagerTexture = new ResourceLocation("steamcraft:textures/models/villager.png");
    public static KeyBinding[] keyBindings;

    @Override
    public void registerHotkeys() {
        keyBindings = new KeyBinding[2];
        keyBindings[0] = new KeyBinding("key.foggles.desc", Keyboard.KEY_SEMICOLON, "key.flaxbeard.category.name");
        keyBindings[1] = new KeyBinding("Zoom using monocle", Keyboard.KEY_Z, "key.categories.misc");
        for (int i = 0; i < keyBindings.length; i++) {
            ClientRegistry.registerKeyBinding(keyBindings[i]);
        }
    }

    @Override
    public void registerRenderers() {
        Steamcraft.channel.register(new SteamcraftClientPacketHandler());

        FMLCommonHandler.instance().bus().register(ExosuitModelCache.INSTANCE);

        RenderingRegistry.registerEntityRenderingHandler(EntityMortarItem.class, new RenderMortarItem());
        RenderingRegistry.registerEntityRenderingHandler(EntityCanisterItem.class, new RenderCanister());

        RenderingRegistry.registerEntityRenderingHandler(EntitySteamHorse.class, new RenderSteamHorse(new ModelHorse(), 0));
        RenderingRegistry.registerEntityRenderingHandler(EntityRocket.class, new RenderRocket());

        TileEntitySpecialRenderer renderCrucible = new TileEntityCrucibleRenderer(false);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrucible.class, renderCrucible);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SteamcraftBlocks.crucible), new ItemTESRRenderer((IInventoryTESR) renderCrucible, new TileEntityCrucible()));
        renderCrucible = new TileEntityCrucibleRenderer(true);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SteamcraftBlocks.hellCrucible), new ItemTESRRenderer((IInventoryTESR) renderCrucible, new TileEntityCrucible()));

        TileEntitySpecialRenderer renderMold = new TileEntityMoldRenderer();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMold.class, new TileEntityMoldRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SteamcraftBlocks.mold), new ItemTESRRenderer((IInventoryTESR) renderMold, new TileEntityMold()));

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySteamCharger.class, new TileEntitySteamChargerRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySteamGauge.class, new TileEntitySteamGaugeRenderer());


        TileEntitySpecialRenderer renderSteamHammer = new TileEntitySteamHammerRenderer();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySteamHammer.class, new TileEntitySteamHammerRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SteamcraftBlocks.hammer), new ItemTESRRenderer((IInventoryTESR) renderSteamHammer, new TileEntitySteamHammer()));

        TileEntitySpecialRenderer renderItemMortar = new TileEntityItemMortarRenderer();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityItemMortar.class, new TileEntityItemMortarRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SteamcraftBlocks.itemMortar), new ItemTESRRenderer((IInventoryTESR) renderItemMortar, new TileEntityItemMortar()));

        TileEntitySpecialRenderer renderPump = new TileEntityPumpRenderer();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPump.class, new TileEntityPumpRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SteamcraftBlocks.pump), new ItemTESRRenderer((IInventoryTESR) renderPump, new TileEntityPump()));

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityValvePipe.class, new TileEntityValvePipeRenderer());

        TileEntitySpecialRenderer renderSmasher = new TileEntitySmasherRenderer();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySmasher.class, new TileEntitySmasherRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SteamcraftBlocks.smasher), new ItemTESRRenderer((IInventoryTESR) renderSmasher, new TileEntitySmasher()));

        TileEntitySpecialRenderer renderThumper = new TileEntityThumperRenderer();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityThumper.class, new TileEntityThumperRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SteamcraftBlocks.thumper), new ItemTESRRenderer((IInventoryTESR) renderThumper, new TileEntityThumper()));

        TileEntitySpecialRenderer renderFan = new TileEntityFanRenderer();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFan.class, new TileEntityFanRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SteamcraftBlocks.fan), new ItemTESRRenderer((IInventoryTESR) renderFan, new TileEntityFan()));

        TileEntitySpecialRenderer renderVacuum = new TileEntityVacuumRenderer();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityVacuum.class, new TileEntityVacuumRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SteamcraftBlocks.vacuum), new ItemTESRRenderer((IInventoryTESR) renderVacuum, new TileEntityVacuum()));

        TileEntitySpecialRenderer renderFluidSteam = new TileEntityFluidSteamRenderer();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidSteamConverter.class, new TileEntityFluidSteamRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SteamcraftBlocks.fluidSteamConverter), new ItemTESRRenderer((IInventoryTESR) renderFluidSteam, new TileEntityFluidSteamConverter(), true));

        MinecraftForgeClient.registerItemRenderer(SteamcraftItems.steamDrill, new ItemSteamToolRenderer(0));
        MinecraftForgeClient.registerItemRenderer(SteamcraftItems.steamAxe, new ItemSteamToolRenderer(1));
        MinecraftForgeClient.registerItemRenderer(SteamcraftItems.steamShovel, new ItemSteamToolRenderer(2));

        TileEntitySpecialRenderer renderChargingPad = new TileEntityChargingPadRenderer();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChargingPad.class, new TileEntityChargingPadRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SteamcraftBlocks.chargingPad), new ItemTESRRenderer((IInventoryTESR) renderChargingPad, new TileEntityChargingPad(), true));

        RenderingRegistry.registerBlockHandler(Steamcraft.tubeRenderID, new BlockSteamPipeRenderer());
        RenderingRegistry.registerBlockHandler(Steamcraft.heaterRenderID, new BlockSteamHeaterRenderer());
        RenderingRegistry.registerBlockHandler(Steamcraft.chargerRenderID, new BlockSteamChargerRenderer());
        //RenderingRegistry.registerBlockHandler(Steamcraft.genocideRenderID, new BlockFishGenocideMachineRenderer());
        RenderingRegistry.registerBlockHandler(Steamcraft.gaugeRenderID, new BlockSteamGaugeRenderer());
        RenderingRegistry.registerBlockHandler(Steamcraft.ruptureDiscRenderID, new BlockRuptureDiscRenderer());
        RenderingRegistry.registerBlockHandler(Steamcraft.whistleRenderID, new BlockWhistleRenderer());
        RenderingRegistry.registerBlockHandler(Steamcraft.boilerRenderID, new BlockBoilerRenderer());
        RenderingRegistry.registerBlockHandler(Steamcraft.customCraftingTableRenderID, new BlockCustomCraftingTableRenderer());
        RenderingRegistry.registerBlockHandler(Steamcraft.furnaceRenderID, new BlockCustomFurnaceRenderer());


        MinecraftForgeClient.registerItemRenderer(SteamcraftItems.musket, new ItemFirearmRenderer());
        MinecraftForgeClient.registerItemRenderer(SteamcraftItems.blunderbuss, new ItemFirearmRenderer());
        MinecraftForgeClient.registerItemRenderer(SteamcraftItems.pistol, new ItemFirearmRenderer());
        MinecraftForgeClient.registerItemRenderer(SteamcraftItems.rocketLauncher, new ItemFirearmRenderer());

        int id = Config.villagerId;
        VillagerRegistry.instance().registerVillagerSkin(id, villagerTexture);
    }

    @Override
    public void spawnBreakParticles(World world, float x, float y, float z, Block block, float xv, float yv, float zv) {
        if (!Config.disableParticles) {
            Minecraft.getMinecraft().effectRenderer.addEffect((new EntityDiggingFX(world, x, y, z, xv, yv, zv, block, 0)));
        }
    }

    @Override
    public void extendRange(Entity entity, float amount) {
        super.extendRange(entity, amount);
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;
        if (entity == player) {
            if (Loader.isModLoaded("Botania")) {
                BotaniaIntegration.extendRange(entity, amount);
            } else {
                if (!(mc.playerController instanceof SteamcraftPlayerController)) {
                    GameType type = ReflectionHelper.getPrivateValue(PlayerControllerMP.class, mc.playerController, new String[]{"currentGameType", "field_78779_k", "k"});
                    NetHandlerPlayClient net = ReflectionHelper.getPrivateValue(PlayerControllerMP.class, mc.playerController, new String[]{"netClientHandler", "field_78774_b", "b"});
                    SteamcraftPlayerController controller = new SteamcraftPlayerController(mc, net);
                    controller.setGameType(type);
                    mc.playerController = controller;
                }

                ((SteamcraftPlayerController) mc.playerController).setReachDistanceExtension(((SteamcraftPlayerController) mc.playerController).getReachDistanceExtension() + amount);

            }
        }
    }

    @Override
    public void checkRange(EntityLivingBase entity) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;
        if (entity == player) {
            if (Loader.isModLoaded("Botania")) {
                BotaniaIntegration.checkRange(entity);
            } else {
                if (!(mc.playerController instanceof SteamcraftPlayerController)) {
                    GameType type = ReflectionHelper.getPrivateValue(PlayerControllerMP.class, mc.playerController, new String[]{"currentGameType", "field_78779_k", "k"});
                    NetHandlerPlayClient net = ReflectionHelper.getPrivateValue(PlayerControllerMP.class, mc.playerController, new String[]{"netClientHandler", "field_78774_b", "b"});
                    SteamcraftPlayerController controller = new SteamcraftPlayerController(mc, net);
                    controller.setGameType(type);
                    mc.playerController = controller;
                }
                if (((SteamcraftPlayerController) mc.playerController).getReachDistanceExtension() <= 2.0F) {
                    extendRange(entity, 2.0F - ((SteamcraftPlayerController) mc.playerController).getReachDistanceExtension());
                }
            }
        }
    }

}