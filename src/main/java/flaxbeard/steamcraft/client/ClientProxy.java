package flaxbeard.steamcraft.client;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.client.render.BlockFishGenocideMachineRenderer;
import flaxbeard.steamcraft.client.render.BlockRuptureDiscRenderer;
import flaxbeard.steamcraft.client.render.BlockSteamChargerRenderer;
import flaxbeard.steamcraft.client.render.BlockSteamGaugeRenderer;
import flaxbeard.steamcraft.client.render.BlockSteamHeaterRenderer;
import flaxbeard.steamcraft.client.render.BlockSteamPipeRenderer;
import flaxbeard.steamcraft.client.render.IInventoryTESR;
import flaxbeard.steamcraft.client.render.ItemFirearmRenderer;
import flaxbeard.steamcraft.client.render.ItemSteamToolRenderer;
import flaxbeard.steamcraft.client.render.ItemTESRRenderer;
import flaxbeard.steamcraft.client.render.RenderMortarItem;
import flaxbeard.steamcraft.client.render.RenderSteamHorse;
import flaxbeard.steamcraft.client.render.TileEntityChargingPadRenderer;
import flaxbeard.steamcraft.client.render.TileEntityConveyorRenderer;
import flaxbeard.steamcraft.client.render.TileEntityCrucibleRenderer;
import flaxbeard.steamcraft.client.render.TileEntityItemMortarRenderer;
import flaxbeard.steamcraft.client.render.TileEntityMoldRenderer;
import flaxbeard.steamcraft.client.render.TileEntityPumpRenderer;
import flaxbeard.steamcraft.client.render.TileEntitySmasherRenderer;
import flaxbeard.steamcraft.client.render.TileEntitySteamChargerRenderer;
import flaxbeard.steamcraft.client.render.TileEntitySteamGaugeRenderer;
import flaxbeard.steamcraft.client.render.TileEntitySteamHammerRenderer;
import flaxbeard.steamcraft.client.render.TileEntityThumperRenderer;
import flaxbeard.steamcraft.client.render.TileEntityValvePipeRenderer;
import flaxbeard.steamcraft.common.CommonProxy;
import flaxbeard.steamcraft.entity.EntityMortarItem;
import flaxbeard.steamcraft.entity.EntitySteamHorse;
import flaxbeard.steamcraft.packet.SteamcraftClientPacketHandler;
import flaxbeard.steamcraft.tile.TileEntityChargingPad;
import flaxbeard.steamcraft.tile.TileEntityConveyor;
import flaxbeard.steamcraft.tile.TileEntityCrucible;
import flaxbeard.steamcraft.tile.TileEntityItemMortar;
import flaxbeard.steamcraft.tile.TileEntityMold;
import flaxbeard.steamcraft.tile.TileEntityPump;
import flaxbeard.steamcraft.tile.TileEntitySmasher;
import flaxbeard.steamcraft.tile.TileEntitySteamCharger;
import flaxbeard.steamcraft.tile.TileEntitySteamGauge;
import flaxbeard.steamcraft.tile.TileEntitySteamHammer;
import flaxbeard.steamcraft.tile.TileEntityThumper;
import flaxbeard.steamcraft.tile.TileEntityValvePipe;


public class ClientProxy extends CommonProxy
{
	public static final ResourceLocation villagerTexture = new ResourceLocation("steamcraft:textures/models/villager.png");
    @Override
    public void registerRenderers()
    {
   	 	Steamcraft.channel.register(new SteamcraftClientPacketHandler());
   	 	
   	 	RenderingRegistry.registerEntityRenderingHandler(EntityMortarItem.class, new RenderMortarItem());
   	 	RenderingRegistry.registerEntityRenderingHandler(EntitySteamHorse.class, new RenderSteamHorse(new ModelHorse(), 0));

    	TileEntitySpecialRenderer renderCrucible = new TileEntityCrucibleRenderer();
    	ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrucible.class, renderCrucible);
    	MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SteamcraftBlocks.crucible), new ItemTESRRenderer((IInventoryTESR) renderCrucible, new TileEntityCrucible()));
    	
    	TileEntitySpecialRenderer renderMold = new TileEntityMoldRenderer();
    	ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMold.class, new TileEntityMoldRenderer());
    	MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SteamcraftBlocks.mold), new ItemTESRRenderer((IInventoryTESR) renderMold, new TileEntityMold()));
    
    	ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySteamCharger.class, new TileEntitySteamChargerRenderer());
    	ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySteamGauge.class, new TileEntitySteamGaugeRenderer());

    	
    	TileEntitySpecialRenderer renderSteamHammer = new TileEntitySteamHammerRenderer();
    	ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySteamHammer.class, new TileEntitySteamHammerRenderer());
    	MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SteamcraftBlocks.hammer), new ItemTESRRenderer((IInventoryTESR) renderSteamHammer, new TileEntitySteamHammer()));
    	
    	TileEntitySpecialRenderer renderConveyor = new TileEntityConveyorRenderer();
    	ClientRegistry.bindTileEntitySpecialRenderer(TileEntityConveyor.class, new TileEntityConveyorRenderer());
    	MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SteamcraftBlocks.conveyor), new ItemTESRRenderer((IInventoryTESR) renderConveyor, new TileEntityConveyor()));
    	
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

    	TileEntitySpecialRenderer renderChargingPad = new TileEntityChargingPadRenderer();
    	ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChargingPad.class, new TileEntityChargingPadRenderer());
    	MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SteamcraftBlocks.chargingPad), new ItemTESRRenderer((IInventoryTESR) renderChargingPad, new TileEntityChargingPad()));
  	
    	
    	MinecraftForgeClient.registerItemRenderer(SteamcraftItems.steamDrill, new ItemSteamToolRenderer(0));
    	MinecraftForgeClient.registerItemRenderer(SteamcraftItems.steamAxe, new ItemSteamToolRenderer(1));
    	MinecraftForgeClient.registerItemRenderer(SteamcraftItems.steamShovel, new ItemSteamToolRenderer(2));

    	
    	RenderingRegistry.registerBlockHandler(Steamcraft.tubeRenderID, new BlockSteamPipeRenderer());
    	RenderingRegistry.registerBlockHandler(Steamcraft.heaterRenderID, new BlockSteamHeaterRenderer());
    	RenderingRegistry.registerBlockHandler(Steamcraft.chargerRenderID, new BlockSteamChargerRenderer());
    	RenderingRegistry.registerBlockHandler(Steamcraft.genocideRenderID, new BlockFishGenocideMachineRenderer());
    	RenderingRegistry.registerBlockHandler(Steamcraft.gaugeRenderID, new BlockSteamGaugeRenderer());
    	RenderingRegistry.registerBlockHandler(Steamcraft.ruptureDiscRenderID, new BlockRuptureDiscRenderer());


    	MinecraftForgeClient.registerItemRenderer(SteamcraftItems.musket, new ItemFirearmRenderer());
    	MinecraftForgeClient.registerItemRenderer(SteamcraftItems.blunderbuss, new ItemFirearmRenderer());
    	MinecraftForgeClient.registerItemRenderer(SteamcraftItems.pistol, new ItemFirearmRenderer());
    	MinecraftForgeClient.registerItemRenderer(SteamcraftItems.revolver, new ItemFirearmRenderer());

        int id = Config.villagerId;
        VillagerRegistry.instance().registerVillagerSkin(id, villagerTexture);
    }
    
    @Override
    public void spawnBreakParticles(World world, float x, float y, float z, Block block, float xv, float yv, float zv) {
        Minecraft.getMinecraft().effectRenderer.addEffect((new EntityDiggingFX(world, x, y, z, xv, yv, zv, block, 0)));
    }
}