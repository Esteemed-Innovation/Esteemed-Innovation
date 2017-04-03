package eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.frequency;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.common.capabilities.Capability;

public class AnimalDataStorage implements Capability.IStorage<AnimalData> {
    public static final String[] POSSIBLE_NAMES = {
      "Jose",
      "Jaunita",
      "Olga",
      "Benito",
      "Bagwis",
      "Jon",
      "Clifford",
      "Garfield",
      "Pilar",
      "Francisco",
      "Peter",
      "Will",
      "Eric",
      "Emilio",
      "George",
      "Ricardo",
      "Arizbeth",
      "Robert",
      "Roberto",
      "Cornelius",
      "Claritia",
      "Paskal",
      "Aydan",
      "Jaida",
      "Dorean",
      "Hana",
      "Kalyani",
      "Iria",
      "Raimundo",
      "Yenifer",
      "Tung",
      "Talia",
      "Lana",
      "Mila",
      "Bobby",
      "Tru",
      "Sammie",
      "Miranda"
    };


    @Override
    public NBTBase writeNBT(Capability<AnimalData> capability, AnimalData instance, EnumFacing side) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("MaximumTrades", instance.getMaximumTotalTrades());
        nbt.setInteger("TotalTrades", instance.getTotalTrades());
        nbt.setString("MerchantName", instance.getMerchantName());
        MerchantRecipeList stock = instance.getStock();
        if (stock != null) {
            nbt.setTag("Stock", stock.getRecipiesAsTags());
        }
        return nbt;
    }

    @Override
    public void readNBT(Capability<AnimalData> capability, AnimalData instance, EnumFacing side, NBTBase nbtBase) {
        NBTTagCompound nbt = (NBTTagCompound) nbtBase;
        instance.setMaximumTotalTrades(nbt.getInteger("MaximumTrades"));
        instance.setTotalTrades(nbt.getInteger("TotalTrades"));
        instance.setMerchantName(nbt.getString("MerchantName"));
        if (nbt.hasKey("Stock")) {
            NBTTagCompound stock = nbt.getCompoundTag("Stock");
            MerchantRecipeList recipes = stock.hasNoTags() ? new MerchantRecipeList() : new MerchantRecipeList(stock);
            instance.setStock(recipes);
        }
    }
}
