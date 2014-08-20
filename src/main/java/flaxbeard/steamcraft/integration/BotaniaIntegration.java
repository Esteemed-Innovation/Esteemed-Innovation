package flaxbeard.steamcraft.integration;

import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;
import vazkii.botania.api.item.IExtendedPlayerController;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibObfuscation;

import com.google.common.collect.Multimap;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.SteamcraftRecipes;
import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.book.BookRecipeRegistry;
import flaxbeard.steamcraft.api.exosuit.ExosuitPlate;
import flaxbeard.steamcraft.api.exosuit.ExosuitSlot;
import flaxbeard.steamcraft.api.exosuit.UtilPlates;
import flaxbeard.steamcraft.item.ItemExosuitArmor;
import flaxbeard.steamcraft.item.ItemExosuitUpgrade;
import flaxbeard.steamcraft.misc.SteamcraftPlayerController;

public class BotaniaIntegration {
    public static Item floralLaurel;

    @SideOnly(Side.CLIENT)
	public static void displayThings(MovingObjectPosition pos, RenderGameOverlayEvent.Post event) {
		Minecraft mc = Minecraft.getMinecraft();
		Block block = mc.theWorld.getBlock(pos.blockX, pos.blockY, pos.blockZ);
		if(block instanceof IWandHUD)
			((IWandHUD) block).renderHUD(mc, event.resolution, mc.theWorld, pos.blockX, pos.blockY, pos.blockZ);
	}
	
	public static Item twigWand() {
		//return null;
		return ModItems.twigWand;
	}

	public static void addBotaniaLiquid() {
    	floralLaurel = new ItemExosuitUpgrade(ExosuitSlot.headHelm, "steamcraft:textures/models/armor/floralLaurel.png",null,5).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:floralLaurel").setTextureName("steamcraft:floralLaurel");
		GameRegistry.registerItem(floralLaurel, "floralLaurel");
		CrucibleLiquid liquidTerrasteel = new CrucibleLiquid("terrasteel", new ItemStack(ModItems.manaResource,1,4), new ItemStack(SteamcraftItems.steamcraftPlate,1,6), null, null,64,191,13);
		SteamcraftRegistry.liquids.add(liquidTerrasteel);
		
		SteamcraftRegistry.registerSmeltThingOredict("ingotTerrasteel", liquidTerrasteel, 9);
		SteamcraftRegistry.registerSmeltThingOredict("nuggetTerrasteel", liquidTerrasteel, 1);
		SteamcraftRegistry.registerSmeltThingOredict("plateSteamcraftTerrasteel", liquidTerrasteel, 6);
		if (Config.enableTerrasteelPlate) {
			SteamcraftRegistry.addExosuitPlate(new ExosuitPlate("Terrasteel",new ItemStack(SteamcraftItems.exosuitPlate,1,6),"Terrasteel","Terrasteel","steamcraft.plate.terrasteel"));
			SteamcraftRecipes.addExosuitPlateRecipes("exoTerrasteel","plateSteamcraftTerrasteel",new ItemStack(SteamcraftItems.exosuitPlate,1,6),liquidTerrasteel);
		}
		CrucibleLiquid liquidElementium = new CrucibleLiquid("Elementium", new ItemStack(ModItems.manaResource,1,7), new ItemStack(SteamcraftItems.steamcraftPlate,1,7), null, null,230,66,247);
		SteamcraftRecipes.addExosuitPlateRecipes("exoElementium","plateSteamcraftElementium",new ItemStack(SteamcraftItems.exosuitPlate,1,7),liquidElementium);

		SteamcraftRegistry.liquids.add(liquidElementium);
		for (int i = 0; i<16; i++) {
			BookRecipeRegistry.addRecipe("floralLaurel"+i,new ShapedOreRecipe(new ItemStack(floralLaurel), "fff","flf","fff",
			        'f', new ItemStack(ModItems.petal,1,i), 'l', new ItemStack(ModItems.manaResource,1,3)));
		}
		SteamcraftRegistry.registerSmeltThing(ModItems.manaResource,7, liquidElementium, 9);
		SteamcraftRegistry.registerSmeltThingOredict("ingotElementium", liquidElementium, 9);
		SteamcraftRegistry.registerSmeltThingOredict("nuggetElementium", liquidElementium, 1);
		SteamcraftRegistry.registerSmeltThingOredict("plateSteamcraftElementium", liquidElementium, 6);
		if (Config.enableElementiumPlate) {
			SteamcraftRecipes.addExosuitPlateRecipes("exoElementium","plateSteamcraftElementium",new ItemStack(SteamcraftItems.exosuitPlate,1,7),liquidElementium);
			SteamcraftRegistry.addExosuitPlate(new ExosuitPlate("Elementium",new ItemStack(SteamcraftItems.exosuitPlate,1,7),"Elementum","Elementum","steamcraft.plate.elementum"));
		}
	}

	public static Multimap addModifiers(Multimap map, ItemStack stack, int armorType) {
		if ((((ItemExosuitArmor)stack.getItem()).hasPlates(stack) && UtilPlates.getPlate(stack.stackTagCompound.getString("plate")).getIdentifier() == "Terrasteel")) {
			int hp = 1;
			switch (armorType) {
				case 0:
					hp = 2;
					break;
				case 1:
					hp = 4;
					break;
				case 2:
					hp = 2;
					break;
				case 4:
					hp = 2;
			}
			map.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier(new UUID(171328 /** Random number **/, armorType), "Armor modifier" + armorType, hp, 0));
		}
		return map;
	}

	@SideOnly(Side.CLIENT)
	public static void extendRange(Entity entity, float amount) {
		Minecraft mc = Minecraft.getMinecraft();
		if(!(mc.playerController instanceof IExtendedPlayerController)) {
			GameType type = ReflectionHelper.getPrivateValue(PlayerControllerMP.class, mc.playerController, LibObfuscation.CURRENT_GAME_TYPE);
			NetHandlerPlayClient net = ReflectionHelper.getPrivateValue(PlayerControllerMP.class, mc.playerController, LibObfuscation.NET_CLIENT_HANDLER);
			SteamcraftPlayerController controller = new SteamcraftPlayerController(mc, net);
			controller.setGameType(type);
			mc.playerController = controller;
		}
		System.out.println(((IExtendedPlayerController) mc.playerController).getReachDistanceExtension());
		((IExtendedPlayerController) mc.playerController).setReachDistanceExtension(((IExtendedPlayerController) mc.playerController).getReachDistanceExtension() + amount);
	}

	@SideOnly(Side.CLIENT)
	public static void checkRange(EntityLivingBase entity) {
		Minecraft mc = Minecraft.getMinecraft();
		if(!(mc.playerController instanceof IExtendedPlayerController)) {
			GameType type = ReflectionHelper.getPrivateValue(PlayerControllerMP.class, mc.playerController, LibObfuscation.CURRENT_GAME_TYPE);
			NetHandlerPlayClient net = ReflectionHelper.getPrivateValue(PlayerControllerMP.class, mc.playerController, LibObfuscation.NET_CLIENT_HANDLER);
			SteamcraftPlayerController controller = new SteamcraftPlayerController(mc, net);
			controller.setGameType(type);
			mc.playerController = controller;
		}
		if (((IExtendedPlayerController) mc.playerController).getReachDistanceExtension() <= 2.0F) {
			extendRange(entity, 2.0F-((IExtendedPlayerController) mc.playerController).getReachDistanceExtension());
		}
	}

}
