package eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.frequency;

import net.minecraft.village.MerchantRecipeList;

public interface AnimalData {
    /**
     * @return The maximum trades this animal can handle before the frequency hurts it.
     */
    int getMaximumTotalTrades();

    /**
     * @return The total number of trades it has been in.
     */
    int getTotalTrades();

    /**
     * @return The custom merchant name it currently has.
     */
    String getMerchantName();

    /**
     * @return Its current stock.
     */
    MerchantRecipeList getStock();

    /**
     * Sets the maximum trades to the value.
     */
    void setMaximumTotalTrades(int value);

    /**
     * Sets the total trades it's been in to the value.
     */
    void setTotalTrades(int value);

    /**
     * Sets its name to the value string.
     */
    void setMerchantName(String value);

    /**
     * Sets its stock to the list.
     */
    void setStock(MerchantRecipeList value);

    class DefaultImplementation implements AnimalData {
        private int maximumTotalTrades;
        private int totalTrades;
        private String merchantName;
        private MerchantRecipeList stock;

        public DefaultImplementation(int maximumTotalTrades, int totalTrades, String merchantName, MerchantRecipeList stock) {
            this.maximumTotalTrades = maximumTotalTrades;
            this.totalTrades = totalTrades;
            this.merchantName = merchantName;
            this.stock = stock;
        }

        @Override
        public int getMaximumTotalTrades() {
            return maximumTotalTrades;
        }

        @Override
        public int getTotalTrades() {
            return totalTrades;
        }

        @Override
        public String getMerchantName() {
            return merchantName;
        }

        @Override
        public MerchantRecipeList getStock() {
            return stock;
        }

        @Override
        public void setMaximumTotalTrades(int value) {
            maximumTotalTrades = value;
        }

        @Override
        public void setTotalTrades(int value) {
            totalTrades = value;
        }

        @Override
        public void setMerchantName(String value) {
            merchantName = value;
        }

        @Override
        public void setStock(MerchantRecipeList value) {
            stock = value;
        }
    }
}
