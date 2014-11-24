package flaxbeard.steamcraft.integration.thaumcraft;

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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class ThaumcraftIntegration {
    
    // Thaumcraft Items
    public static Item itemResource;
    public static Item itemGoggles;
    public static Item itemNugget;
    public static Item itemSwordThaumium;
    public static Item itemPickThaumium;
    public static Item itemAxeThaumium;
    public static Item itemHoeThaumium;
    public static Item itemShovelThaumium;
    public static Item itemBootsThaumium;
    public static Item itemChestThaumium;
    public static Item itemHelmetThaumium;
    public static Item itemLegsThaumium;
    
    // Our Items
    public static Item goggleUpgrade;
    public static Item thaumSource;

    public static void grabItems() {
        itemResource = GameRegistry.findItem("Thaumcraft", "ItemResource");
        itemGoggles = GameRegistry.findItem("Thaumcraft", "ItemGoggles");
        itemNugget = GameRegistry.findItem("Thaumcraft", "ItemNugget");
        itemSwordThaumium = GameRegistry.findItem("Thaumcraft", "ItemSwordThaumium");
        itemPickThaumium = GameRegistry.findItem("Thaumcraft", "ItemPickThaumium");
        itemAxeThaumium = GameRegistry.findItem("Thaumcraft", "ItemAxeThaumium");
        itemHoeThaumium = GameRegistry.findItem("Thaumcraft", "ItemHoeThaumium");
        itemShovelThaumium = GameRegistry.findItem("Thaumcraft", "ItemShovelThaumium");
        itemBootsThaumium = GameRegistry.findItem("Thaumcraft", "ItemBootsThaumium");
        itemChestThaumium = GameRegistry.findItem("Thaumcraft", "ItemChestplateThaumium");
        itemHelmetThaumium = GameRegistry.findItem("Thaumcraft", "ItemHelmetThaumium");
        itemLegsThaumium = GameRegistry.findItem("Thaumcraft", "ItemLeggingsThaumium");
    }

    public static void addThaumiumLiquid() {
        CrucibleLiquid liquidThaumium = new CrucibleLiquid("thaumium", new ItemStack(itemResource, 1, 2), new ItemStack(SteamcraftItems.steamcraftPlate, 1, 5), new ItemStack(itemNugget, 1, 6), null, 105, 87, 163);
        SteamcraftRegistry.liquids.add(liquidThaumium);

        goggleUpgrade = new ItemExosuitUpgrade(ExosuitSlot.headGoggles, "steamcraft:textures/models/armor/gogglesUpgrade.png", null, 0).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:goggleUpgrade").setTextureName("steamcraft:gogglesUpgrade");
        GameRegistry.registerItem(goggleUpgrade, "goggleUpgrade");
        BookRecipeRegistry.addRecipe("mask", new ShapedOreRecipe(new ItemStack(goggleUpgrade), " x ", "xgx", " x ",
                'x', "nuggetBrass", 'g', itemGoggles));

        SteamcraftRegistry.registerSmeltThingOredict("ingotThaumium", liquidThaumium, 9);
        SteamcraftRegistry.registerSmeltThingOredict("nuggetThaumium", liquidThaumium, 1);
        SteamcraftRegistry.registerSmeltThingOredict("plateSteamcraftThaumium", liquidThaumium, 6);
        if (Config.enableThaumiumPlate) {
            SteamcraftRegistry.addExosuitPlate(new ExosuitPlate("Thaumium", new ItemStack(SteamcraftItems.exosuitPlate, 1, 5), "Thaumium", "Thaumium", "steamcraft.plate.thaumium"));
            SteamcraftRecipes.addExosuitPlateRecipes("exoThaumium", "plateSteamcraftThaumium", new ItemStack(SteamcraftItems.exosuitPlate, 1, 5), liquidThaumium);
        }
        SteamcraftRegistry.registerSmeltTool(itemSwordThaumium, liquidThaumium, 18);
        SteamcraftRegistry.registerSmeltTool(itemPickThaumium, liquidThaumium, 27);
        SteamcraftRegistry.registerSmeltTool(itemAxeThaumium, liquidThaumium, 27);
        SteamcraftRegistry.registerSmeltTool(itemHoeThaumium, liquidThaumium, 18);
        SteamcraftRegistry.registerSmeltTool(itemShovelThaumium, liquidThaumium, 9);
        SteamcraftRegistry.registerSmeltTool(itemBootsThaumium, liquidThaumium, 36);
        SteamcraftRegistry.registerSmeltTool(itemChestThaumium, liquidThaumium, 81);
        SteamcraftRegistry.registerSmeltTool(itemHelmetThaumium, liquidThaumium, 45);
        SteamcraftRegistry.registerSmeltTool(itemLegsThaumium, liquidThaumium, 63);


        AspectList list = ThaumcraftApiHelper.getObjectAspects(new ItemStack(SteamcraftItems.steamcraftPlate, 1, OreDictionary.WILDCARD_VALUE));
        if (list == null || list.size() == 0) {
            list = new AspectList();
            list.add(Aspect.METAL, 2);
            ThaumcraftApi.registerObjectTag(new ItemStack(SteamcraftItems.steamcraftPlate, 1, OreDictionary.WILDCARD_VALUE), list);
        }

        list = ThaumcraftApiHelper.getObjectAspects(new ItemStack(SteamcraftItems.steamcraftIngot, 1, 1));
        if (list == null || list.size() == 0) {
            list = new AspectList();
            list.add(Aspect.METAL, 3);
            list.add(Aspect.HEAL, 1);
            ThaumcraftApi.registerObjectTag(new ItemStack(SteamcraftItems.steamcraftIngot, 1, 1), list);
        }

        list = ThaumcraftApiHelper.getObjectAspects(new ItemStack(SteamcraftItems.steamcraftNugget, 1, 1));
        if (list == null || list.size() == 0) {
            list = new AspectList();
            list.add(Aspect.METAL, 1);
            ThaumcraftApi.registerObjectTag(new ItemStack(SteamcraftItems.steamcraftNugget, 1, 1), list);
        }

        list = ThaumcraftApiHelper.getObjectAspects(new ItemStack(SteamcraftItems.steamcraftNugget, 1, 3));
        if (list == null || list.size() == 0) {
            list = new AspectList();
            list.add(Aspect.METAL, 1);
            ThaumcraftApi.registerObjectTag(new ItemStack(SteamcraftItems.steamcraftNugget, 1, 3), list);
        }
    }

    public static Item gogglesRevealing() {
        return itemGoggles;
    }

    @SideOnly(Side.CLIENT)
    public static void addTooltip(ItemTooltipEvent event) {
//		Minecraft mc = Minecraft.getMinecraft();
//		EntityPlayer player = mc.thePlayer;
//		Object[] ref = ThaumcraftApi.getCraftingRecipeKey(player, event.itemStack);
//        if ((ref != null))
//        {
//        	ResearchItem research = ResearchCategories.getResearch((String)ref[0]);
//			boolean foundBook = Loader.isModLoaded("Enchiridion") ? EnchiridionIntegration.hasBook(itemThaumonomicon, player) : false;
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
