package flaxbeard.steamcraft.client.render.model.exosuit;

import com.google.common.collect.Maps;
import flaxbeard.steamcraft.item.armor.exosuit.ItemExosuitArmor;
import flaxbeard.steamcraft.misc.ItemStackUtility;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

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

    public ModelExosuit getModel(EntityPlayer entityPlayer, EntityEquipmentSlot armorType) {
        String name = entityPlayer.getDisplayNameString();
        if (!modelCache.containsKey(name)) {
            modelCache.put(name, generateNewArray());
        }
        return modelCache.get(name)[armorType.getIndex()];
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPlayerTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (event.side == Side.CLIENT && mc.thePlayer != null) {
            if (event.phase == TickEvent.Phase.START) {
                return;
            }

            for (int i = 0; i < 4; i++) {
                ItemStack itemStack = mc.thePlayer.inventory.armorInventory[3 - i];

                if (itemStack != null && itemStack.getItem() instanceof ItemExosuitArmor) {
                    getModel(mc.thePlayer, ItemStackUtility.getSlotFromSlotIndex(i)).updateModel(mc.thePlayer, itemStack);
                }
            }
        }
    }
}
