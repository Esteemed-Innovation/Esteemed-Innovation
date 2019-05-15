package eiteam.esteemedinnovation.commons.capabilities.player;

import eiteam.esteemedinnovation.api.book.BookPiece;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public interface PlayerData {
    /**
     * Gets the prevStep for the player.
     * @return Float. Can be null.
     */
    @Nullable
    Float getPreviousStepHeight();

    /**
     * Gets the tick cache for the player. Used b the exosuit wings.
     * @return int
     */
    int getTickCache();


    /**
     * Gets the player's previous X, Z motions.
     * @return Pair of doubles.
     */
    @Nullable
    Pair<Double, Double> getLastMotions();

    /**
     * Sets the previous step height
     * @param value Float, can be null
     */
    void setPreviousStepHeight(@Nullable Float value);

    /**
     * Sets the tick cache
     * @param value int
     */
    void setTickCache(int value);


    /**
     * Sets the player's previous X, Z motions.
     * @param value a pair of doubles
     */
    void setLastMotions(@Nullable Pair<Double, Double> value);

    /**
     * Sets whether the player has unlocked this specific piece in their EI journal.
     * @param piece The keyname for the piece. This is ideally how you should be handling your
     *              {@link BookPiece#isUnlocked(EntityPlayer)} implementer.
     * @param unlocked True if it is being unlocked, false if it is being locked.
     * @return True if the value changed in any way.
     */
    boolean setHasUnlockedBookPiece(String piece, boolean unlocked);

    /**
     * @return A set of all unlocked pieces. The strings are the keys as utilized in {@link #setHasUnlockedBookPiece(String, boolean)}.
     */
    Set<String> getAllUnlockedPieces();

    class DefaultImplementation implements PlayerData {
        private Float previousStepHeight = null;
        private int tickCache = -1;
        private Pair<Double, Double> lastMotions = null;
        private Set<String> unlockedPieces = new HashSet<>();

        @Override
        public Float getPreviousStepHeight() {
            return previousStepHeight;
        }

        @Override
        public int getTickCache() {
            return tickCache;
        }

        @Override
        public Pair<Double, Double> getLastMotions() {
            return lastMotions;
        }

        @Override
        public void setPreviousStepHeight(Float value) {
            previousStepHeight = value;
        }

        @Override
        public void setTickCache(int value) {
            tickCache = value;
        }

        @Override
        public void setLastMotions(Pair<Double, Double> value) {
            lastMotions = value;
        }

        @Override
        public boolean setHasUnlockedBookPiece(String piece, boolean unlocked) {
            return unlocked ? unlockedPieces.add(piece) : unlockedPieces.remove(piece);
        }

        @Override
        public Set<String> getAllUnlockedPieces() {
            return unlockedPieces;
        }
    }
}
