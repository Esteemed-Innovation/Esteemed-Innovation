package flaxbeard.steamcraft.client.render.model.exosuit;

import com.google.common.collect.Maps;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import flaxbeard.steamcraft.item.ItemExosuitArmor;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.Map;

/**
 * @author dmillerw
 */
public class ExosuitModelCache {

    public static final ExosuitModelCache INSTANCE = new ExosuitModelCache();

    private final Map<String, ModelExosuit[]> modelCache = Maps.newHashMap();

    private ModelExosuit[] generateNewArray() {
        ModelExosuit[] array = new ModelExosuit[4];
        for (int i=0; i<4; i++) {
            array[i] = new ModelExosuit(i);
        }
        return array;
    }

    public ModelExosuit getModel(EntityPlayer entityPlayer, int armorType) {
        if (!modelCache.containsKey(entityPlayer.getCommandSenderName())) {
            modelCache.put(entityPlayer.getCommandSenderName(), generateNewArray());
        }
        return modelCache.get(entityPlayer.getCommandSenderName())[armorType];
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            return;
        }

        if (!(event.player instanceof EntityClientPlayerMP)) {
            return;
        }

        for (int i = 0; i < 4; i++) {
            ItemStack itemStack = event.player.inventory.armorInventory[3 - i];

            if (itemStack != null && itemStack.getItem() instanceof ItemExosuitArmor) {
                getModel(event.player, i).updateModel(event.player, itemStack);
            }
        }
    }
}
