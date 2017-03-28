package eiteam.esteemedinnovation.api.book;

import net.minecraft.entity.player.EntityPlayer;

public interface BookPiece {
    /**
     * @param player The player who is trying to open this piece.
     * @return Whether the player has unlocked this piece. If they haven't, it shouldn't be shown.
     */
    default boolean isUnlocked(EntityPlayer player) {
        return true;
    }

    /**
     * @param player The player who is trying to open this piece.
     * @return If this piece is ever shown to the player through standard browsing of the journal.
     */
    default boolean isHidden(EntityPlayer player) {
        return false;
    }

    /**
     * @return The unlocalized string that is translated to this piece's locked hint. Return null to have no hint. If
     *         the piece is locked but has no hint, it will not be rendered.
     */
    default String getUnlocalizedHint() {
        return null;
    }
}
