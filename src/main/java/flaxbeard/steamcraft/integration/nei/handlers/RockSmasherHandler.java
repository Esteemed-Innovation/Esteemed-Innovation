package flaxbeard.steamcraft.integration.nei.handlers;

import flaxbeard.steamcraft.tile.TileEntitySmasher;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Map;

public class RockSmasherHandler extends TemplateRecipeHandler {

	@Override
	public String getRecipeName(){
		return StatCollector.translateToLocal("tile.steamcraft:smasher.name");
	}

	@Override
	public String getGuiTexture(){
		return "minecraft:textures/gui/container/furnace.png"; //TODO: we need a GUI
	}
	
	@Override
	public void drawBackground(int recipe) {
		GL11.glColor4f(1, 1, 1, 1);
		GuiDraw.changeTexture(getGuiTexture());
		GuiDraw.drawTexturedModalRect(0, 0, 11, 10, 123, 126);
		GuiDraw.drawTexturedModalRect(126, 0, 147, 10, 75, 100);
	}
	
	@Override
	public void drawForeground(int recipe) {
		super.drawForeground(recipe);
	}
	
	@Override
	public int recipiesPerPage() {
		return 1; //I guess
	}
	
	@Override
	public void loadCraftingRecipes(ItemStack output) {
		List<ItemStack> inputs = TileEntitySmasher.REGISTRY.getInputs(output);
		if(inputs != null){
			for(ItemStack input : inputs){
				this.arecipes.add(new CachedRockSmasherRecipe(input, TileEntitySmasher.REGISTRY.getOutput(input))); //For proper stacking support
			}
		}
	}
	
	@Override
	public void loadUsageRecipes(ItemStack input) {
		input.stackSize = 1;
		ItemStack output = TileEntitySmasher.REGISTRY.getOutput(input);
		if(output != null){
			this.arecipes.add(new CachedRockSmasherRecipe(input, output));
		}
	}
	
	public class CachedRockSmasherRecipe extends CachedRecipe {
		private PositionedStack input;
		private PositionedStack output;
		
		public CachedRockSmasherRecipe(ItemStack input, ItemStack output){
			this.input = new PositionedStack(input, 29, 53);
			this.output = new PositionedStack(output, 139, 54);
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
	
	@Override
	public String getOverlayIdentifier(){
		return "smasher";
	}
}
