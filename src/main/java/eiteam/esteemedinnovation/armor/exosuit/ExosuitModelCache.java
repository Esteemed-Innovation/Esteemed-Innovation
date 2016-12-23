package eiteam.esteemedinnovation.armor.exosuit;

import com.google.common.collect.Maps;
import eiteam.esteemedinnovation.api.util.ItemStackUtility;
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

    private static ModelExosuit[] generateNewArray() {
        ModelExosuit[] array = new ModelExosuit[4];
        for (EntityEquipmentSlot slot : ItemStackUtility.EQUIPMENT_SLOTS) {
            if (slot.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
                array[slot.getIndex()] = new ModelExosuit(slot);
            }
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

            for (EntityEquipmentSlot slot : ItemStackUtility.EQUIPMENT_SLOTS) {
                if (slot.getSlotType() != EntityEquipmentSlot.Type.ARMOR) {
                    continue;
                }

                ItemStack armorStack = mc.thePlayer.getItemStackFromSlot(slot);
                if (armorStack != null && armorStack.getItem() instanceof ItemExosuitArmor) {
                    getModel(mc.thePlayer, slot).updateModel(mc.thePlayer, armorStack);
                }
            }
        }
    }
}
