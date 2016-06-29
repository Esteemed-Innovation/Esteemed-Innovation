package flaxbeard.steamcraft.data.capabilities.villager;

public interface IVillagerData {
    /**
     * Gets whether the villager previously had a customer.
     * @return Whether it had a customer, or null.
     */
    Boolean hadCustomer();

    /**
     * Sets whether it previously had a customer.
     * @param value The value. Can be null.
     */
    void setHadCustomer(Boolean value);

    class DefaultImplementation implements IVillagerData {
        private Boolean lastHadCustomer = null;

        @Override
        public Boolean hadCustomer() {
            return lastHadCustomer;
        }

        @Override
        public void setHadCustomer(Boolean value) {
            lastHadCustomer = value;
        }
    }
}
