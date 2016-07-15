package flaxbeard.steamcraft.client;

import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.enhancement.UtilEnhancements;
import flaxbeard.steamcraft.client.render.block.*;
import flaxbeard.steamcraft.client.render.colorhandlers.ItemExosuitColorHandler;
import flaxbeard.steamcraft.client.render.colorhandlers.ItemSmashedOreColorHandler;
import flaxbeard.steamcraft.client.render.colorhandlers.SteamDrillColorHandler;
import flaxbeard.steamcraft.client.render.colorhandlers.SteamDrillHeadUpgradeColorHandler;
import flaxbeard.steamcraft.client.render.entity.RenderCanister;
import flaxbeard.steamcraft.client.render.entity.RenderMortarItem;
import flaxbeard.steamcraft.client.render.entity.RenderRocket;
import flaxbeard.steamcraft.client.render.item.ItemFirearmRenderer;
import flaxbeard.steamcraft.client.render.item.ItemSteamToolRenderer;
import flaxbeard.steamcraft.client.render.item.ItemTESRRenderer;
import flaxbeard.steamcraft.client.render.model.exosuit.ExosuitModelCache;
import flaxbeard.steamcraft.client.render.tile.*;
import flaxbeard.steamcraft.common.CommonProxy;
import flaxbeard.steamcraft.entity.item.EntityCanisterItem;
import flaxbeard.steamcraft.entity.item.EntityMortarItem;
import flaxbeard.steamcraft.entity.projectile.EntityRocket;
import flaxbeard.steamcraft.init.blocks.CastingBlocks;
import flaxbeard.steamcraft.init.blocks.SteamMachineryBlocks;
import flaxbeard.steamcraft.init.blocks.SteamNetworkBlocks;
import flaxbeard.steamcraft.init.items.MetalItems;
import flaxbeard.steamcraft.init.items.armor.ArmorItems;
import flaxbeard.steamcraft.init.items.firearms.FirearmItems;
import flaxbeard.steamcraft.init.items.tools.ToolItems;
import flaxbeard.steamcraft.init.items.tools.ToolUpgradeItems;
import flaxbeard.steamcraft.item.armor.exosuit.ItemExosuitArmor;
import flaxbeard.steamcraft.misc.SteamcraftPlayerController;
import flaxbeard.steamcraft.tile.*;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;

public class ClientProxy extends CommonProxy {
    public static final ResourceLocation villagerTexture = new ResourceLocation("steamcraft:textures/models/villager.png");
    public static HashMap<String, KeyBinding> keyBindings = new HashMap<>();

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
        }

        MinecraftForge.EVENT_BUS.register(ExosuitModelCache.INSTANCE);
        FMLCommonHandler.instance().bus().register(ExosuitModelCache.INSTANCE);

        RenderingRegistry.registerEntityRenderingHandler(EntityMortarItem.class, new RenderMortarItem());
        RenderingRegistry.registerEntityRenderingHandler(EntityCanisterItem.class, new RenderCanister());

        RenderingRegistry.registerEntityRenderingHandler(EntityRocket.class, new RenderRocket());

        TileEntitySpecialRenderer renderCrucible = new TileEntityCrucibleRenderer(false);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrucible.class, renderCrucible);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(CastingBlocks.Blocks.CRUCIBLE.getBlock()), new ItemTESRRenderer((IInventoryTESR) renderCrucible, new TileEntityCrucible()));
        renderCrucible = new TileEntityCrucibleRenderer(true);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(CastingBlocks.Blocks.NETHER_CRUCIBLE.getBlock()), new ItemTESRRenderer((IInventoryTESR) renderCrucible, new TileEntityCrucible()));

        TileEntitySpecialRenderer renderMold = new TileEntityMoldRenderer();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMold.class, new TileEntityMoldRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(CastingBlocks.Blocks.MOLD.getBlock()), new ItemTESRRenderer((IInventoryTESR) renderMold, new TileEntityMold()));

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySteamCharger.class, new TileEntitySteamChargerRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySteamGauge.class, new TileEntitySteamGaugeRenderer());


        TileEntitySpecialRenderer renderSteamHammer = new TileEntitySteamHammerRenderer();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySteamHammer.class, new TileEntitySteamHammerRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SteamMachineryBlocks.Blocks.STEAM_HAMMER.getBlock()), new ItemTESRRenderer((IInventoryTESR) renderSteamHammer, new TileEntitySteamHammer()));

        TileEntitySpecialRenderer renderItemMortar = new TileEntityItemMortarRenderer();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityItemMortar.class, new TileEntityItemMortarRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SteamMachineryBlocks.Blocks.ITEM_MORTAR.getBlock()), new ItemTESRRenderer((IInventoryTESR) renderItemMortar, new TileEntityItemMortar()));

        TileEntitySpecialRenderer renderPump = new TileEntityPumpRenderer();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPump.class, new TileEntityPumpRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SteamNetworkBlocks.Blocks.PIPE.getBlock()), new ItemTESRRenderer((IInventoryTESR) renderPump, new TileEntityPump()));

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityValvePipe.class, new TileEntityValvePipeRenderer());

        TileEntitySpecialRenderer renderSmasher = new TileEntitySmasherRenderer();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySmasher.class, new TileEntitySmasherRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SteamMachineryBlocks.Blocks.ROCK_SMASHER.getBlock()), new ItemTESRRenderer((IInventoryTESR) renderSmasher, new TileEntitySmasher()));

        TileEntitySpecialRenderer renderThumper = new TileEntityThumperRenderer();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityThumper.class, new TileEntityThumperRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SteamMachineryBlocks.Blocks.THUMPER.getBlock()), new ItemTESRRenderer((IInventoryTESR) renderThumper, new TileEntityThumper()));

        TileEntitySpecialRenderer renderFan = new TileEntityFanRenderer();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFan.class, new TileEntityFanRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SteamMachineryBlocks.Blocks.FAN.getBlock()), new ItemTESRRenderer((IInventoryTESR) renderFan, new TileEntityFan()));

        TileEntitySpecialRenderer renderVacuum = new TileEntityVacuumRenderer();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityVacuum.class, new TileEntityVacuumRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SteamMachineryBlocks.Blocks.VACUUM.getBlock()), new ItemTESRRenderer((IInventoryTESR) renderVacuum, new TileEntityVacuum()));

        TileEntitySpecialRenderer renderFluidSteam = new TileEntityFluidSteamRenderer();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidSteamConverter.class, new TileEntityFluidSteamRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SteamNetworkBlocks.Blocks.PRESSURE_CONVERTER.getBlock()), new ItemTESRRenderer((IInventoryTESR) renderFluidSteam, new TileEntityFluidSteamConverter(), true));

        TileEntitySpecialRenderer renderChargingPad = new TileEntityChargingPadRenderer();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChargingPad.class, new TileEntityChargingPadRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SteamMachineryBlocks.Blocks.FILLING_PAD.getBlock()), new ItemTESRRenderer((IInventoryTESR) renderChargingPad, new TileEntityChargingPad(), true));

        RenderingRegistry.registerBlockHandler(Steamcraft.tubeRenderID, new BlockSteamPipeRenderer());
        RenderingRegistry.registerBlockHandler(Steamcraft.heaterRenderID, new BlockSteamHeaterRenderer());
        RenderingRegistry.registerBlockHandler(Steamcraft.chargerRenderID, new BlockSteamChargerRenderer());
        //RenderingRegistry.registerBlockHandler(Steamcraft.genocideRenderID, new BlockFishGenocideMachineRenderer());
        RenderingRegistry.registerBlockHandler(Steamcraft.gaugeRenderID, new BlockSteamGaugeRenderer());
        RenderingRegistry.registerBlockHandler(Steamcraft.ruptureDiscRenderID, new BlockRuptureDiscRenderer());
        RenderingRegistry.registerBlockHandler(Steamcraft.whistleRenderID, new BlockWhistleRenderer());
        RenderingRegistry.registerBlockHandler(Steamcraft.boilerRenderID, new BlockBoilerRenderer());


        MinecraftForgeClient.registerItemRenderer(FirearmItems.Items.MUSKET.getItem(), new ItemFirearmRenderer());
        MinecraftForgeClient.registerItemRenderer(FirearmItems.Items.BLUNDERBUSS.getItem(), new ItemFirearmRenderer());
        MinecraftForgeClient.registerItemRenderer(FirearmItems.Items.PISTOL.getItem(), new ItemFirearmRenderer());
        MinecraftForgeClient.registerItemRenderer(FirearmItems.Items.ROCKET_LAUNCHER.getItem(), new ItemFirearmRenderer());

        MinecraftForgeClient.registerItemRenderer(ToolItems.Items.STEAM_DRILL.getItem(), new ItemSteamToolRenderer());
        MinecraftForgeClient.registerItemRenderer(ToolItems.Items.STEAM_SAW.getItem(), new ItemSteamToolRenderer());
        MinecraftForgeClient.registerItemRenderer(ToolItems.Items.STEAM_SHOVEL.getItem(), new ItemSteamToolRenderer());

        int id = Config.villagerId;
        VillagerRegistry.instance().registerVillagerSkin(id, villagerTexture);
    }

    @Override
    public void spawnBreakParticles(World world, float x, float y, float z, Block block, float xv, float yv, float zv) {
        if (!Config.disableParticles) {
            Minecraft.getMinecraft().effectRenderer.addEffect((new EntityDiggingFX(world, x, y, z, xv, yv, zv, block, 0)));
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