package flaxbeard.steamcraft.integration.ic2;

import ic2.api.recipe.IRecipeInput;

import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * Part/parts of this code was taken from IndustrialEx, which was taken from Bluepower, I 
 * love the compact way they handle ic2's recipe input stuff. Original class comes from 
 * <a href="https://github.com/Qmunity/BluePower/blob/master/src/main/java/com/bluepowermod/compat/ic2/IC2RecipeInput.java">here</a>.
 * IndustrialEx's class <a href="https://github.com/xbony2/IndustrialEx/blob/master/src/main/java/xbony2/IndustrialEx/api/recipe/IC2RecipeInput.java">here</a>.
 * 
 * @author xbony2, MineMaarten
 *
 */
public class IC2RecipeInput implements IRecipeInput {
	private final ItemStack input;

	public IC2RecipeInput(ItemStack input) {
        this.input = input;
    }

	@Override
    public boolean matches(ItemStack subject) {
        return subject != null && input.isItemEqual(subject) && input.getItemDamage() == subject.getItemDamage();
    }

    @Override
    public int getAmount() {
        return input.stackSize;
    }

    @Override
    public List<ItemStack> getInputs() {
    	return Arrays.asList(input);
    }
}
