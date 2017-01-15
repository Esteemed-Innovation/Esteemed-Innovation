package eiteam.esteemedinnovation.armor.exosuit.upgrades.pulsenozzle;

import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.armor.exosuit.upgrades.ItemExosuitUpgrade;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static eiteam.esteemedinnovation.armor.ArmorModule.resource;

public class ItemDoubleJumpUpgrade extends ItemExosuitUpgrade {
    public ItemDoubleJumpUpgrade() {
        super(ExosuitSlot.BOOTS_FEET, resource("doubleJump"), null, 1);
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            MinecraftForge.EVENT_BUS.register(new EventHandler());
        }
    }

    @Override
    public void onPlayerUpdate(LivingEvent.LivingUpdateEvent event, EntityPlayer player, ItemStack armorStack, EntityEquipmentSlot slot) {
        if (player.onGround && armorStack.hasTagCompound()) {
            armorStack.getTagCompound().setBoolean("usedJump", false);
        }
    }

    public class EventHandler {
        @SideOnly(Side.CLIENT)
        @SubscribeEvent
        public void jumpAgain(InputEvent.KeyInputEvent event) {
            if (FMLClientHandler.instance().isGUIOpen(GuiChat.class)) {
                return;
            }

            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayer player = mc.thePlayer;
            if (mc.gameSettings.keyBindJump.isKeyDown() && !player.onGround && isInstalled(player)) {
                EsteemedInnovation.channel.sendToServer(new DoubleJumpServerActionPacket(getSlot().getArmorPiece()));
            }
        }
    }
}
