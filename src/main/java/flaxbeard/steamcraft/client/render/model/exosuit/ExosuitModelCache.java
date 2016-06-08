package flaxbeard.steamcraft.client.render.model.exosuit;

import com.google.common.collect.Maps;
import flaxbeard.steamcraft.item.armor.exosuit.ItemExosuitArmor;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
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

    public ModelExosuit getModel(EntityPlayer entityPlayer, int armorType) {
        if (!modelCache.containsKey(entityPlayer.getCommandSenderName())) {
            modelCache.put(entityPlayer.getCommandSenderName(), generateNewArray());
        }
        return modelCache.get(entityPlayer.getCommandSenderName())[armorType];
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPlayerTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (event.side == Side.CLIENT && mc.thePlayer != null) {
            if (event.phase == TickEvent.Phase.START) {
                return;
            }

            if (!(mc.thePlayer instanceof EntityClientPlayerMP)) {
                return;
            }

            for (int i = 0; i < 4; i++) {
                ItemStack itemStack = mc.thePlayer.inventory.armorInventory[3 - i];

                if (itemStack != null && itemStack.getItem() instanceof ItemExosuitArmor) {
                    getModel(mc.thePlayer, i).updateModel(mc.thePlayer, itemStack);
                }
            }
        }
    }
}
