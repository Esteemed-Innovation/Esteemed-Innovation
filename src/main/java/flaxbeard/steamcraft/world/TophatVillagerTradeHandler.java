package flaxbeard.steamcraft.world;

import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.item.armor.exosuit.ItemExosuitArmor;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

import java.util.Random;

public class TophatVillagerTradeHandler implements IVillageTradeHandler {
    @Override
    public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random) {
        if (villager.getCustomer().inventory.armorInventory[0] != null && (villager.getCustomer().inventory.armorInventory[0].getItem() == SteamcraftItems.tophat
                || (villager.getCustomer().inventory.armorInventory[0].getItem() == SteamcraftItems.exoArmorHead && ((ItemExosuitArmor) villager.getCustomer().inventory.armorInventory[0].getItem()).hasUpgrade(villager.getCustomer().inventory.armorInventory[0], SteamcraftItems.tophat)))) {
            for (Object obj : recipeList) {
                MerchantRecipe recipe = (MerchantRecipe) obj;
                if (recipe.getItemToSell().stackSize > 1 && recipe.getItemToSell().stackSize != MathHelper.floor_float(recipe.getItemToSell().stackSize * 1.1F)) {
                    recipe.getItemToSell().stackSize = MathHelper.floor_float(recipe.getItemToSell().stackSize * 1.1F);
                } else if (recipe.getItemToBuy().stackSize > 1 && recipe.getItemToBuy().stackSize != MathHelper.floor_float(recipe.getItemToBuy().stackSize / 1.1F)) {
                    recipe.getItemToBuy().stackSize = MathHelper.ceiling_float_int(recipe.getItemToBuy().stackSize / 1.1F);
                } else if (recipe.getSecondItemToBuy().stackSize > 1 && recipe.getSecondItemToBuy().stackSize != MathHelper.floor_float(recipe.getSecondItemToBuy().stackSize / 1.1F)) {
                    recipe.getItemToBuy().stackSize = MathHelper.ceiling_float_int(recipe.getItemToBuy().stackSize / 1.1F);
                }
            }
        }
    }
}
