package flaxbeard.steamcraft.client.render.model;

import com.google.common.collect.Maps;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.Map;

/**
 * @author dmillerw
 */
@SideOnly(Side.CLIENT)
public class PlayerExosuitCache {

    private static Map<String, ModelHash[]> modelCache = Maps.newHashMap();

    public static ModelExosuit getModel(EntityPlayer entityPlayer, ItemStack itemStack, int armorType) {
        if (!modelCache.containsKey(entityPlayer.getCommandSenderName()))
            modelCache.put(entityPlayer.getCommandSenderName(), new ModelHash[4]);

        ModelHash[] hashArray = modelCache.get(entityPlayer.getCommandSenderName());

        if (hashArray[armorType] == null) {
            hashArray[armorType] = new ModelHash(itemStack, armorType);
        } else {
            ModelHash modelHash = hashArray[armorType];
            if (modelHash.hashCode() != quickHash(itemStack, armorType))
                hashArray[armorType] = new ModelHash(itemStack, armorType);
        }

        return hashArray[armorType].modelExosuit;
    }

    private static int quickHash(ItemStack itemStack, int armorType) {
        return itemStack.getTagCompound().hashCode() ^ armorType;
    }

    private static class ModelHash {

        public final ModelExosuit modelExosuit;
        public final int lastHash;

        public ModelHash(ItemStack itemStack, int armorType) {
            this.modelExosuit = new ModelExosuit(itemStack, armorType);
            this.lastHash = itemStack.getTagCompound().hashCode() ^ armorType;
        }

        @Override
        public int hashCode() {
            return this.lastHash;
        }
    }
}
