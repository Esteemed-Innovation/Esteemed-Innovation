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
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nonnull;

/**
 * The ResearchRecipe is a ShapedOreRecipe that requires research to first be unlocked.
 * <p>
 * Well, it actually requires a predicate to match. By default, this predicate will check if the player has unlocked
 * a certain {@link BookPiece}. However, using the constructor which passes a {@link TriPredicate} allows for any number
 * of checks.
 */
public class ResearchRecipe extends ShapedOreRecipe {
    /**
     * The predicate used to check if the recipe is valid. When this is executed, it already checks that the shape is
     * valid, and the player is not null (e.g., autocrafting).
     * <p>
     * The predicate passes the current {@link InventoryCrafting}, the {@link EntityPlayer} crafting it, and the
     * {@link World} they are currently in.
     */
    private final TriPredicate<InventoryCrafting, EntityPlayer, World> matcher;

    /**
     * Provides a new ResearchRecipe with a custom predicate.
     * @param result The output ItemStack.
     * @param matcher The predicate (see {@link #matcher}).
     * @param recipe The recipe shape (see {@link ShapedOreRecipe}).
     */
    public ResearchRecipe(@Nonnull ItemStack result, TriPredicate<InventoryCrafting, EntityPlayer, World> matcher, Object... recipe) {
        super(new ResourceLocation(Constants.EI_MODID, "shaped_research"), result, recipe);
        this.matcher = matcher;
    }

    /**
     * Provides a new ResearchRecipe with the default predicate. This predicate checks that the player has unlocked
     * a particular {@link BookPiece} (see {@link BookPiece#isUnlocked(EntityPlayer)}), whose unlocalized name is
     * passed.
     * @param result The output ItemStack.
     * @param requiredBookPieceName The unlocalized name for the {@link BookPiece} to be checked against.
     * @param recipe The recipe shape (see {@link ShapedOreRecipe}).
     */
    public ResearchRecipe(@Nonnull ItemStack result, String requiredBookPieceName, Object... recipe) {
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
