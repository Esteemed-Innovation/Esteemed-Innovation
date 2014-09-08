package flaxbeard.steamcraft.integration;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.client.gui.GuiResearchRecipe;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.relics.ItemThaumonomicon;
import thaumcraft.common.lib.research.ResearchManager;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
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
import flaxbeard.steamcraft.item.ItemExosuitUpgrade;

public class ThaumcraftIntegration {
    public static Item goggleUpgrade;
    public static Item thaumSource;
    
	public static void addThaumiumLiquid() {
		CrucibleLiquid liquidThaumium = new CrucibleLiquid("thaumium", new ItemStack(ConfigItems.itemResource,1,2), new ItemStack(SteamcraftItems.steamcraftPlate,1,5), new ItemStack(ConfigItems.itemNugget,1,6), null,105,87,163);
		SteamcraftRegistry.liquids.add(liquidThaumium);
		
		goggleUpgrade = new ItemExosuitUpgrade(ExosuitSlot.headGoggles, "steamcraft:textures/models/armor/gogglesUpgrade.png",null,0).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:goggleUpgrade").setTextureName("steamcraft:gogglesUpgrade");
		GameRegistry.registerItem(goggleUpgrade, "goggleUpgrade");
		BookRecipeRegistry.addRecipe("mask",new ShapedOreRecipe(new ItemStack(goggleUpgrade), " x ", "xgx", " x " ,
		        'x',"nuggetBrass", 'g', ConfigItems.itemGoggles));
		
		SteamcraftRegistry.registerSmeltThingOredict("ingotThaumium", liquidThaumium, 9);
		SteamcraftRegistry.registerSmeltThingOredict("nuggetThaumium", liquidThaumium, 1);
		SteamcraftRegistry.registerSmeltThingOredict("plateSteamcraftThaumium", liquidThaumium, 6);
		if (Config.enableThaumiumPlate) {
			SteamcraftRegistry.addExosuitPlate(new ExosuitPlate("Thaumium",new ItemStack(SteamcraftItems.exosuitPlate,1,5),"Thaumium","Thaumium","steamcraft.plate.thaumium"));
			SteamcraftRecipes.addExosuitPlateRecipes("exoThaumium","plateSteamcraftThaumium",new ItemStack(SteamcraftItems.exosuitPlate,1,5),liquidThaumium);
		}
		SteamcraftRegistry.registerSmeltTool(ConfigItems.itemSwordThaumium, liquidThaumium, 18);
		SteamcraftRegistry.registerSmeltTool(ConfigItems.itemPickThaumium, liquidThaumium, 27);
		SteamcraftRegistry.registerSmeltTool(ConfigItems.itemAxeThaumium, liquidThaumium, 27);
		SteamcraftRegistry.registerSmeltTool(ConfigItems.itemHoeThaumium, liquidThaumium, 18);
		SteamcraftRegistry.registerSmeltTool(ConfigItems.itemShovelThaumium, liquidThaumium, 9);
		SteamcraftRegistry.registerSmeltTool(ConfigItems.itemBootsThaumium, liquidThaumium, 36);
		SteamcraftRegistry.registerSmeltTool(ConfigItems.itemChestThaumium, liquidThaumium, 81);
		SteamcraftRegistry.registerSmeltTool(ConfigItems.itemHelmetThaumium, liquidThaumium, 45);
		SteamcraftRegistry.registerSmeltTool(ConfigItems.itemLegsThaumium, liquidThaumium, 63);
	
		
		AspectList list = ThaumcraftApiHelper.getObjectAspects(new ItemStack(SteamcraftItems.steamcraftPlate, 1, OreDictionary.WILDCARD_VALUE));
		if (list == null || list.size() == 0){
			list = new AspectList();
			list.add(Aspect.METAL, 2);
			ThaumcraftApi.registerObjectTag(new ItemStack(SteamcraftItems.steamcraftPlate, 1, OreDictionary.WILDCARD_VALUE), list);
		}
		
		list = ThaumcraftApiHelper.getObjectAspects(new ItemStack(SteamcraftItems.steamcraftIngot, 1, 1));
		if (list == null || list.size() == 0){
			list = new AspectList();
			list.add(Aspect.METAL, 3);
			list.add(Aspect.HEAL, 1);
			ThaumcraftApi.registerObjectTag(new ItemStack(SteamcraftItems.steamcraftIngot, 1, 1), list);
		}
		
		list = ThaumcraftApiHelper.getObjectAspects(new ItemStack(SteamcraftItems.steamcraftNugget, 1, 1));
		if (list == null || list.size() == 0){
			list = new AspectList();
			list.add(Aspect.METAL, 1);
			ThaumcraftApi.registerObjectTag(new ItemStack(SteamcraftItems.steamcraftNugget, 1, 1), list);
		}
		
		list = ThaumcraftApiHelper.getObjectAspects(new ItemStack(SteamcraftItems.steamcraftNugget, 1, 3));
		if (list == null || list.size() == 0){
			list = new AspectList();
			list.add(Aspect.METAL, 1);
			ThaumcraftApi.registerObjectTag(new ItemStack(SteamcraftItems.steamcraftNugget, 1, 3), list);
		}
	}

	public static Item gogglesRevealing() {
		return ConfigItems.itemGoggles;
	}

	@SideOnly(Side.CLIENT)
	public static void addTooltip(ItemTooltipEvent event) {
//		Minecraft mc = Minecraft.getMinecraft();
//		EntityPlayer player = mc.thePlayer;
//		Object[] ref = ThaumcraftApi.getCraftingRecipeKey(player, event.itemStack);
//        if ((ref != null))
//        {
//        	ResearchItem research = ResearchCategories.getResearch((String)ref[0]);
//			boolean foundBook = Loader.isModLoaded("Enchiridion") ? EnchiridionIntegration.hasBook(ConfigItems.itemThaumonomicon, player) : false;
//			for (int p = 0; p < player.inventory.getSizeInventory(); p++) {
//				if (player.inventory.getStackInSlot(p) != null && player.inventory.getStackInSlot(p).getItem() instanceof ItemThaumonomicon) {
//					foundBook = true;
//					break;
//				}
//			}
//			if (foundBook && ResearchManager.isResearchComplete(player.getCommandSenderName(), research.key)) {
//    			event.toolTip.add(EnumChatFormatting.ITALIC+""+EnumChatFormatting.GRAY+StatCollector.translateToLocal("steamcraft.book.shiftright"));
//    			if (Mouse.isButtonDown(0) && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
//        			mc.displayGuiScreen(new GuiResearchRecipe(research, ((Integer)ref[1]).intValue(), 0, 0));
//    			}
//			}
//        }
	}
	
	
}
