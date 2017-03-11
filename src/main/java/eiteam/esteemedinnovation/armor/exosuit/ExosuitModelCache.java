package eiteam.esteemedinnovation.armor.exosuit;

import eiteam.esteemedinnovation.api.exosuit.ExosuitArmor;
import eiteam.esteemedinnovation.api.util.ItemStackUtility;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class ExosuitModelCache {
    /**
     * Key: Player UUID, Value: An array of all the armors they are wearing.
     */
    private final Map<UUID, ModelExosuit[]> cache = new HashMap<>();

    /**
     * The armor type to check for.
     */
    private final Class<? extends ExosuitArmor> armorType;

    public ExosuitModelCache(Class<? extends ExosuitArmor> armorType) {
        this.armorType = armorType;
    }

    /**
     * @return All of the armor piece models for this suit.
     */
    protected abstract ModelExosuit[] generateNewArray();

    public ModelExosuit getModel(EntityPlayer entityPlayer, EntityEquipmentSlot armorType) {
        UUID id = entityPlayer.getUniqueID();
        if (!cache.containsKey(id)) {
            cache.put(id, generateNewArray());
        }
        return cache.get(id)[armorType.getIndex()];
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPlayerTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (event.side == Side.CLIENT && mc.thePlayer != null) {
            if (event.phase == TickEvent.Phase.START) {
                return;
            }

            for (EntityEquipmentSlot slot : ItemStackUtility.ARMOR_SLOTS) {
                ItemStack armorStack = mc.thePlayer.getItemStackFromSlot(slot);
                if (armorStack != null && armorType.isAssignableFrom(armorStack.getItem().getClass())) {
                    getModel(mc.thePlayer, slot).updateModel(mc.thePlayer, armorStack, slot);
                }
            }
        }
    }
}
