package flaxbeard.steamcraft.data.capabilities.player;


import org.apache.commons.lang3.tuple.MutablePair;

public interface IPlayerData {
    /**
     * Gets the prevStep for the player.
     * @return Float. Can be null.
     */
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
    MutablePair<Double, Double> getLastMotions();

    /**
     * Sets the previous step height
     * @param value Float, can be null
     */
    void setPreviousStepHeight(Float value);

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
    void setLastMotions(MutablePair<Double, Double> value);

    class DefaultImplementation implements IPlayerData {
        private Float previousStepHeight;
        private int tickCache;
        private boolean isRangeExtended;
        private MutablePair<Double, Double> lastMotions;

        @Override
        public Float getPreviousStepHeight() {
            return null;
        }

        @Override
        public int getTickCache() {
            return -1;
        }

        @Override
        public boolean isRangeExtended() {
            return false;
        }

        @Override
        public MutablePair<Double, Double> getLastMotions() {
            return null;
        }

        @Override
        public void setPreviousStepHeight(Float value) {
            this.previousStepHeight = value;
        }

        @Override
        public void setTickCache(int value) {
            this.tickCache = value;
        }

        @Override
        public void setRangeExtended(boolean value) {
            this.isRangeExtended = value;
        }

        @Override
        public void setLastMotions(MutablePair<Double, Double> value) {
            this.lastMotions = value;
        }
    }
}
