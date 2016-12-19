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
}
