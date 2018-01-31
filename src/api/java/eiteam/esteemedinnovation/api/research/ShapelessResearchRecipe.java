package eiteam.esteemedinnovation.api.research;

import eiteam.esteemedinnovation.api.Constants;
import eiteam.esteemedinnovation.api.book.BookPageRegistry;
import eiteam.esteemedinnovation.api.book.BookPiece;
import eiteam.esteemedinnovation.api.util.InventoryUtility;
import eiteam.esteemedinnovation.api.util.TriPredicate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;

/**
 * A version of the {@link ResearchRecipe} which is a shapeless ore recipe rather than a shaped ore recipe.
 * @see ResearchRecipe for detailed information
 */
public class ShapelessResearchRecipe extends ShapelessOreRecipe {
    /**
     * @see ResearchRecipe#matcher
     */
    private final TriPredicate<InventoryCrafting, EntityPlayer, World> matcher;

    /**
     * @param recipe The shapeless recipe params (see {@link ShapelessOreRecipe})
     * @see ResearchRecipe#ResearchRecipe(ItemStack, TriPredicate, Object...)
     */
    public ShapelessResearchRecipe(@Nonnull ItemStack result, TriPredicate<InventoryCrafting, EntityPlayer, World> matcher, Object... recipe) {
        super(new ResourceLocation(Constants.EI_MODID, "shapeless_research"), result, recipe);
        this.matcher = matcher;
    }

    /**
     * @param recipe The shapeless recipe params (see {@link ShapelessOreRecipe})
     * @see ResearchRecipe#ResearchRecipe(ItemStack, String, Object...)
     */
    public ShapelessResearchRecipe(@Nonnull ItemStack result, String requiredBookPieceName, Object... recipe) {
        this(result, (inv, player, world) -> {
            BookPiece piece = BookPageRegistry.getFirstPieceFromName(requiredBookPieceName);
            return piece != null && piece.isUnlocked(player);
        }, recipe);
    }

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        EntityPlayer player = InventoryUtility.getCurrentPlayerFromInventoryCrafting(inv);
        return player != null && super.matches(inv, world) && matcher.test(inv, player, world);
    }
}
