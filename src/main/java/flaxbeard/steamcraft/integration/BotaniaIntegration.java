package flaxbeard.steamcraft.integration;

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

import com.google.common.collect.Multimap;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
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

import java.util.UUID;

public class BotaniaIntegration {

    // PlayerControllerMP
    public static final String[] NET_CLIENT_HANDLER = new String[] { "netClientHandler",
      "field_78774_b", "b" };
    public static final String[] CURRENT_GAME_TYPE = new String[] { "currentGameType",
      "field_78779_k", "k" };

    // Our Items
    public static Item floralLaurel;
    
    private BotaniaIntegration() throws InstantiationException{
		throw new InstantiationException("This class is not meant to be instantiated");
	}

    @SideOnly(Side.CLIENT)
    public static void displayThings(MovingObjectPosition pos, RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getMinecraft();
        Block block = mc.theWorld.getBlock(pos.blockX, pos.blockY, pos.blockZ);
        if (block instanceof IWandHUD) {
            ((IWandHUD) block).renderHUD(mc, event.resolution, mc.theWorld, pos.blockX, pos.blockY,
              pos.blockZ);
        }
    }

    public static Item twigWand() {
        return ModItems.twigWand;
    }

    public static void postInit() {
        floralLaurel = new ItemExosuitUpgrade(ExosuitSlot.HEAD_HELM,
          "steamcraft:textures/models/armor/floralLaurel.png", null, 5)
          .setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:floralLaurel")
          .setTextureName("steamcraft:floralLaurel");
        GameRegistry.registerItem(floralLaurel, "floralLaurel");
        CrucibleLiquid liquidTerrasteel = new CrucibleLiquid("terrasteel",
          new ItemStack(ModItems.manaResource, 1, 4),
          new ItemStack(SteamcraftItems.steamcraftPlate, 1, 6), null, null, 64, 191, 13);
        SteamcraftRegistry.registerLiquid(liquidTerrasteel);

        SteamcraftRegistry.registerMeltRecipeOreDict("ingotTerrasteel", liquidTerrasteel, 9);
        SteamcraftRegistry.registerMeltRecipeOreDict("nuggetTerrasteel", liquidTerrasteel, 1);
        SteamcraftRegistry.registerMeltRecipeOreDict("plateSteamcraftTerrasteel", liquidTerrasteel,
          6);
        if (Config.enableTerrasteelPlate) {
            SteamcraftRegistry.addExosuitPlate(new ExosuitPlate("Terrasteel",
              new ItemStack(SteamcraftItems.exosuitPlate, 1, 6), "Terrasteel", "Terrasteel",
              "steamcraft.plate.terrasteel"));
            SteamcraftRecipes.addExosuitPlateRecipes("exoTerrasteel", "plateSteamcraftTerrasteel",
              new ItemStack(SteamcraftItems.exosuitPlate, 1, 6), liquidTerrasteel);
        }
        CrucibleLiquid liquidElementium = new CrucibleLiquid("Elementium",
          new ItemStack(ModItems.manaResource, 1, 7), new ItemStack(SteamcraftItems.steamcraftPlate,
          1, 7), null, null, 230, 66, 247);
        SteamcraftRecipes.addExosuitPlateRecipes("exoElementium", "plateSteamcraftElementium",
          new ItemStack(SteamcraftItems.exosuitPlate, 1, 7), liquidElementium);

        SteamcraftRegistry.registerLiquid(liquidElementium);
        for (int i = 0; i < 16; i++) {
            BookRecipeRegistry.addRecipe("floralLaurel" + i, new ShapedOreRecipe(
              new ItemStack(floralLaurel), "fff", "flf", "fff",
              'f', new ItemStack(ModItems.petal, 1, i),
              'l', new ItemStack(ModItems.manaResource, 1, 3)));
        }
        SteamcraftRegistry.registerMeltRecipe(ModItems.manaResource, 7, liquidElementium, 9);
        SteamcraftRegistry.registerMeltRecipeOreDict("ingotElementium", liquidElementium, 9);
        SteamcraftRegistry.registerMeltRecipeOreDict("nuggetElementium", liquidElementium, 1);
        SteamcraftRegistry.registerMeltRecipeOreDict("plateSteamcraftElementium", liquidElementium,
          6);
        if (Config.enableElementiumPlate) {
            SteamcraftRecipes.addExosuitPlateRecipes("exoElementium", "plateSteamcraftElementium",
              new ItemStack(SteamcraftItems.exosuitPlate, 1, 7), liquidElementium);
            SteamcraftRegistry.addExosuitPlate(new ExosuitPlate("Elementium",
              new ItemStack(SteamcraftItems.exosuitPlate, 1, 7), "Elementum", "Elementum",
              "steamcraft.plate.elementum"));
        }
    }

    public static Multimap addModifiers(Multimap map, ItemStack stack, int armorType) {
      int numberRandom = 171328; //Consider making this number actually random. Unsure if thatll work
        if ((((ItemExosuitArmor) stack.getItem()).hasPlates(stack) &&
          UtilPlates.getPlate(stack.stackTagCompound.getString("plate")).getIdentifier() ==
          "Terrasteel")) {
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
            map.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(),
              new AttributeModifier(new UUID(numberRandom, armorType),
              "Armor modifier" + armorType, hp, 0));
        }
        return map;
    }

    @SideOnly(Side.CLIENT)
    public static void extendRange(Entity entity, float amount) {
        Minecraft mc = Minecraft.getMinecraft();
        if (!(mc.playerController instanceof IExtendedPlayerController)) {
            GameType type = Minecraft.getMinecraft().theWorld.getWorldInfo().getGameType();
            NetHandlerPlayClient net = Minecraft.getMinecraft().getNetHandler();
            SteamcraftPlayerController controller = new SteamcraftPlayerController(mc, net);
            controller.setGameType(type);
            mc.playerController = controller;
        }
        ((IExtendedPlayerController) mc.playerController).setReachDistanceExtension(
          ((IExtendedPlayerController) mc.playerController).getReachDistanceExtension() + amount);
    }

    @SideOnly(Side.CLIENT)
    public static void checkRange(EntityLivingBase entity) {
        Minecraft mc = Minecraft.getMinecraft();
        if (!(mc.playerController instanceof IExtendedPlayerController)) {
            GameType type = Minecraft.getMinecraft().theWorld.getWorldInfo().getGameType();
            NetHandlerPlayClient net = Minecraft.getMinecraft().getNetHandler();
            SteamcraftPlayerController controller = new SteamcraftPlayerController(mc, net);
            controller.setGameType(type);
            mc.playerController = controller;
        }

        if (((IExtendedPlayerController) mc.playerController).getReachDistanceExtension() <= 2.0F) {
            extendRange(entity, 2.0F - ((IExtendedPlayerController) mc.playerController)
              .getReachDistanceExtension());
        }
    }
}
