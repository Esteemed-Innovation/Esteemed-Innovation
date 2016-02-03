package flaxbeard.steamcraft.integration.nei.handlers;

import flaxbeard.steamcraft.api.SteamcraftRegistry;

import net.minecraft.client.gui.inventory.GuiFurnace;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.crafting.FurnaceRecipes;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import org.apache.commons.lang3.tuple.MutablePair;
import org.lwjgl.opengl.GL11;

import java.util.Map;

public class SteamHeaterHandler extends TemplateRecipeHandler {

	@Override
	public String getRecipeName(){
		return StatCollector.translateToLocal("tile.steamcraft:heater.name");
	}

	@Override
	public String getGuiTexture(){
		return "textures/gui/container/furnace.png";
	}
	
	@Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiFurnace.class;
    }
	
	@Override
	public int recipiesPerPage() {
		return 1;
	}
	
	@Override
	public void loadCraftingRecipes(ItemStack output) {
		
	}
	
	@Override
	public void loadUsageRecipes(ItemStack input) {
		input.stackSize = 1;
		ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(input);
		if(itemstack == null){
			return;
		}
		
		for(Map.Entry<MutablePair<Item, Integer>, MutablePair<Item, Integer>> recipe : SteamcraftRegistry.steamingRecipes.entrySet()){
			if(recipe.getKey().getKey().equals(itemstack.getItem()) && recipe.getKey().getValue().equals(itemstack.getItemDamage())){
				this.arecipes.add(new CachedSteamHeaterRecipe(input, new ItemStack(recipe.getValue().getKey(), 1, recipe.getValue().getValue())));
			}
		}
	}
	
	public class CachedSteamHeaterRecipe extends CachedRecipe {
		private PositionedStack input;
		private PositionedStack output;
		
		public CachedSteamHeaterRecipe(ItemStack input, ItemStack output){
			this.input = new PositionedStack(input, 51, 6);
			this.output = new PositionedStack(output, 111, 24);
		}

		@Override
		public PositionedStack getResult(){
			return this.output;
		}
		
		@Override
		public PositionedStack getIngredient() {
			return this.input;
		}
	}
}
