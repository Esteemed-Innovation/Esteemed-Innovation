package flaxbeard.steamcraft.integration;

import net.minecraft.item.Item;

public class ThaumcraftIntegration {
    public static Item goggleUpgrade;
  //  public static Item thaumSource;
    
	public static void addThaumiumLiquid() {
//		CrucibleLiquid liquidThaumium = new CrucibleLiquid("thaumium", new ItemStack(ConfigItems.itemResource,1,2), new ItemStack(SteamcraftItems.steamcraftPlate,1,5), new ItemStack(ConfigItems.itemNugget,1,6), null,105,87,163);
//		SteamcraftRegistry.liquids.add(liquidThaumium);
//		
//		goggleUpgrade = new ItemExosuitUpgrade(ExosuitSlot.headGoggles, "steamcraft:textures/models/armor/gogglesUpgrade.png",null,0).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:goggleUpgrade").setTextureName("steamcraft:gogglesUpgrade");
//		GameRegistry.registerItem(goggleUpgrade, "goggleUpgrade");
//		BookRecipeRegistry.addRecipe("mask",new ShapedOreRecipe(new ItemStack(goggleUpgrade), " x ", "xgx", " x " ,
//		        'x',"nuggetBrass", 'g', ConfigItems.itemGoggles));
//		
//		SteamcraftRegistry.registerSmeltThingOredict("ingotThaumium", liquidThaumium, 9);
//		SteamcraftRegistry.registerSmeltThingOredict("nuggetThaumium", liquidThaumium, 1);
//		SteamcraftRegistry.registerSmeltThingOredict("plateThaumium", liquidThaumium, 6);
//		SteamcraftRegistry.addExosuitPlate(new ExosuitPlate("Thaumium","plateThaumium","Thaumium","Thaumium"));
//
//		
//		SteamcraftRegistry.registerSmeltTool(ConfigItems.itemSwordThaumium, liquidThaumium, 18);
//		SteamcraftRegistry.registerSmeltTool(ConfigItems.itemPickThaumium, liquidThaumium, 27);
//		SteamcraftRegistry.registerSmeltTool(ConfigItems.itemAxeThaumium, liquidThaumium, 27);
//		SteamcraftRegistry.registerSmeltTool(ConfigItems.itemHoeThaumium, liquidThaumium, 18);
//		SteamcraftRegistry.registerSmeltTool(ConfigItems.itemShovelThaumium, liquidThaumium, 9);
//		SteamcraftRegistry.registerSmeltTool(ConfigItems.itemBootsThaumium, liquidThaumium, 36);
//		SteamcraftRegistry.registerSmeltTool(ConfigItems.itemChestThaumium, liquidThaumium, 81);
//		SteamcraftRegistry.registerSmeltTool(ConfigItems.itemHelmetThaumium, liquidThaumium, 45);
//		SteamcraftRegistry.registerSmeltTool(ConfigItems.itemLegsThaumium, liquidThaumium, 63);
//	
//		
//		AspectList list = ThaumcraftApiHelper.getObjectAspects(new ItemStack(SteamcraftItems.steamcraftPlate, 1, OreDictionary.WILDCARD_VALUE));
//		if (list == null || list.size() == 0){
//			list = new AspectList();
//			list.add(Aspect.METAL, 2);
//			ThaumcraftApi.registerObjectTag(new ItemStack(SteamcraftItems.steamcraftPlate, 1, OreDictionary.WILDCARD_VALUE), list);
//		}
//		
//		list = ThaumcraftApiHelper.getObjectAspects(new ItemStack(SteamcraftItems.steamcraftIngot, 1, 1));
//		if (list == null || list.size() == 0){
//			list = new AspectList();
//			list.add(Aspect.METAL, 3);
//			list.add(Aspect.HEAL, 1);
//			ThaumcraftApi.registerObjectTag(new ItemStack(SteamcraftItems.steamcraftIngot, 1, 1), list);
//		}
//		
//		list = ThaumcraftApiHelper.getObjectAspects(new ItemStack(SteamcraftItems.steamcraftNugget, 1, 1));
//		if (list == null || list.size() == 0){
//			list = new AspectList();
//			list.add(Aspect.METAL, 1);
//			ThaumcraftApi.registerObjectTag(new ItemStack(SteamcraftItems.steamcraftNugget, 1, 1), list);
//		}
//		
//		list = ThaumcraftApiHelper.getObjectAspects(new ItemStack(SteamcraftItems.steamcraftNugget, 1, 3));
//		if (list == null || list.size() == 0){
//			list = new AspectList();
//			list.add(Aspect.METAL, 1);
//			ThaumcraftApi.registerObjectTag(new ItemStack(SteamcraftItems.steamcraftNugget, 1, 3), list);
//		}
	}

	public static Item gogglesRevealing() {
		return null;
		//return ConfigItems.itemGoggles;
	}
	
	
}
