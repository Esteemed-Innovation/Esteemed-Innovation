package eiteam.esteemedinnovation.armor.exosuit.upgrades.frequency;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.common.capabilities.Capability;

public class AnimalDataStorage implements Capability.IStorage<IAnimalData> {
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
    public NBTBase writeNBT(Capability<IAnimalData> capability, IAnimalData instance, EnumFacing side) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("maximumTrades", instance.getMaximumTotalTrades());
        nbt.setInteger("totalTrades", instance.getTotalTrades());
        nbt.setString("merchantName", instance.getMerchantName());
        MerchantRecipeList stock = instance.getStock();
        if (stock != null) {
            nbt.setTag("stock", stock.getRecipiesAsTags());
        }
        return nbt;
    }

    @Override
    public void readNBT(Capability<IAnimalData> capability, IAnimalData instance, EnumFacing side, NBTBase nbtBase) {
        NBTTagCompound nbt = (NBTTagCompound) nbtBase;
        instance.setMaximumTotalTrades(nbt.getInteger("maximumTrades"));
        instance.setTotalTrades(nbt.getInteger("totalTrades"));
        instance.setMerchantName(nbt.getString("merchantName"));
        if (nbt.hasKey("stock")) {
            NBTTagCompound stock = nbt.getCompoundTag("stock");
            MerchantRecipeList recipes = stock.hasNoTags() ? new MerchantRecipeList() : new MerchantRecipeList(stock);
            instance.setStock(recipes);
        }
    }
}
