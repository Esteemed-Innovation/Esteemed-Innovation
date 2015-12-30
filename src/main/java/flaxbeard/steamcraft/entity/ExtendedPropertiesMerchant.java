package flaxbeard.steamcraft.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import java.util.Random;

public class ExtendedPropertiesMerchant implements IExtendedEntityProperties {
    public EntityLiving entity;
    public World world;
    public int maximumTrades;
    public int totalTrades;
    public String merchantName;
    public MerchantRecipeList stock = null;
    public Random rand = new Random();

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
    public void saveNBTData(NBTTagCompound compound) {
        compound.setInteger("maximumTrades", this.maximumTrades);
        compound.setInteger("totalTrades", this.totalTrades);
        compound.setString("merchantName", this.merchantName);
        if (this.stock != null) {
            compound.setTag("stock", this.stock.getRecipiesAsTags());
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        this.maximumTrades = compound.getInteger("maximumTrades");
        this.totalTrades = compound.getInteger("totalTrades");
        this.merchantName = compound.getString("merchantName");
        if (compound.hasKey("stock")) {
            NBTTagCompound nbt = compound.getCompoundTag("stock");
            if (nbt.hasNoTags()) {
                this.stock = new MerchantRecipeList();
            } else {
                this.stock = new MerchantRecipeList(nbt);
            }
        }
    }

    @Override
    public void init(Entity entity, World world) {
        this.entity = (EntityLiving) entity;
        this.world = world;
        this.maximumTrades = rand.nextInt(7);
        this.totalTrades = 0;
        this.merchantName = POSSIBLE_NAMES[rand.nextInt(POSSIBLE_NAMES.length)];
        this.stock = null;
    }
}
