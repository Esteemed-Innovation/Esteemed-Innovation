package eiteam.esteemedinnovation.armor.exosuit.steam;

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

public class SteamExosuitModelCache {
    public static final SteamExosuitModelCache INSTANCE = new SteamExosuitModelCache();

    private final Map<String, ModelSteamExosuit[]> modelCache = Maps.newHashMap();

    private static ModelSteamExosuit[] generateNewArray() {
        ModelSteamExosuit[] array = new ModelSteamExosuit[4];
        for (EntityEquipmentSlot slot : ItemStackUtility.ARMOR_SLOTS) {
            array[slot.getIndex()] = new ModelSteamExosuit(slot);
        }
        return array;
    }

    public ModelSteamExosuit getModel(EntityPlayer entityPlayer, EntityEquipmentSlot armorType) {
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

            for (EntityEquipmentSlot slot : ItemStackUtility.ARMOR_SLOTS) {
                ItemStack armorStack = mc.thePlayer.getItemStackFromSlot(slot);
                if (armorStack != null && armorStack.getItem() instanceof ItemSteamExosuitArmor) {
                    getModel(mc.thePlayer, slot).updateModel(mc.thePlayer, armorStack);
                }
            }
        }
    }
}
