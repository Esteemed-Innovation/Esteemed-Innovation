package eiteam.esteemedinnovation.data.capabilities.player;


import org.apache.commons.lang3.tuple.MutablePair;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public interface IPlayerData {
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
     * Gets whether the player's range has been extended (for example by the Extendo Fist)
     * @return boolean
     */
    boolean isRangeExtended();

    /**
     * Gets the player's previous X, Z motions.
     * @return MutablePair of doubles.
     */
    @Nullable
    MutablePair<Double, Double> getLastMotions();

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
     * Sets the player's range-extended status.
     * @param value boolean
     */
    void setRangeExtended(boolean value);

    /**
     * Sets the player's previous X, Z motions.
     * @param value a pair of doubles
     */
    void setLastMotions(@Nullable MutablePair<Double, Double> value);

    /**
     * Sets whether the player has unlocked the provided {@link eiteam.esteemedinnovation.api.book.BookEntry} key.
     * @param entry The entry name
     * @param unlocked Whether it is being unlocked (true) or locked (false).
     */
    void setHasUnlockedAnEntry(String entry, boolean unlocked);

    class DefaultImplementation implements IPlayerData {
        private Float previousStepHeight = null;
        private int tickCache = -1;
        private boolean isRangeExtended = false;
        private MutablePair<Double, Double> lastMotions = null;
        private List<String> entriesUnlocked = new ArrayList<>();

        @Override
        public Float getPreviousStepHeight() {
            return previousStepHeight;
        }

        @Override
        public int getTickCache() {
            return tickCache;
        }

        @Override
        public boolean isRangeExtended() {
            return isRangeExtended;
        }

        @Override
        public MutablePair<Double, Double> getLastMotions() {
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
        public void setRangeExtended(boolean value) {
            isRangeExtended = value;
        }

        @Override
        public void setLastMotions(MutablePair<Double, Double> value) {
            lastMotions = value;
        }

        @Override
        public void setHasUnlockedAnEntry(String entry, boolean unlocked) {
            if (unlocked) {
                entriesUnlocked.add(entry);
            } else {
                entriesUnlocked.remove(entry);
            }
        }
    }
}
