package flaxbeard.steamcraft.client;

import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.api.enhancement.UtilEnhancements;
import flaxbeard.steamcraft.client.render.colorhandlers.*;
import flaxbeard.steamcraft.client.render.entity.*;
import flaxbeard.steamcraft.client.render.item.ItemFirearmRenderer;
import flaxbeard.steamcraft.client.render.item.ItemSteamToolRenderer;
import flaxbeard.steamcraft.client.render.model.exosuit.ExosuitModelCache;
import flaxbeard.steamcraft.client.render.tile.*;
import flaxbeard.steamcraft.common.CommonProxy;
import flaxbeard.steamcraft.entity.item.EntityCanisterItem;
import flaxbeard.steamcraft.entity.item.EntityMortarItem;
import flaxbeard.steamcraft.entity.projectile.EntityRocket;
import flaxbeard.steamcraft.init.items.MetalItems;
import flaxbeard.steamcraft.init.items.armor.ArmorItems;
import flaxbeard.steamcraft.init.items.firearms.FirearmItems;
import flaxbeard.steamcraft.init.items.tools.ToolItems;
import flaxbeard.steamcraft.init.items.tools.ToolUpgradeItems;
import flaxbeard.steamcraft.item.armor.exosuit.ItemExosuitArmor;
import flaxbeard.steamcraft.misc.SteamcraftPlayerController;
import flaxbeard.steamcraft.tile.*;
import codechicken.lib.render.ModelRegistryHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Optional;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;

public class ClientProxy extends CommonProxy {
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

        // TODO: Figure out what to do about the normal/nether crucible differences.
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrucible.class, new TileEntityCrucibleRenderer(true));
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