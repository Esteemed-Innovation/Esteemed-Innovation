package flaxbeard.steamcraft.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import org.lwjgl.opengl.GL11;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.client.ClientProxy;
import flaxbeard.steamcraft.item.ItemExosuitArmor;

public class FogglesHandler {
    public int maximumFogPosition = 16;
    public int currentFogPosition = maximumFogPosition;

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void toggleFogColorChange(InputEvent.KeyInputEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        ItemStack equipment = player.getEquipmentInSlot(4);
        if (ClientProxy.keyBindings[0].isPressed() && canFogify(player, equipment)) {
            if (currentFogPosition == maximumFogPosition) {
                currentFogPosition = 0;
            } else {
                currentFogPosition += 1;
            }
        }
    }

    /**
     * Gets whether the Foggles can be used.
     * @param entity The entity wearing them.
     * @param helmet The ItemStack to check.
     * @return boolean
     */
    private boolean canFogify(Entity entity, ItemStack helmet) {
        if (entity != null && helmet != null &&helmet.getItem() instanceof ItemExosuitArmor &&
          (entity instanceof EntityLivingBase || entity instanceof EntityPlayer)) {
            ItemExosuitArmor helmetArmor = (ItemExosuitArmor) helmet.getItem();
            EntityLivingBase elb = (EntityLivingBase) entity;
            if (SteamcraftEventHandler.hasPower(elb, 1) &&
              helmetArmor.hasUpgrade(helmet, SteamcraftItems.foggles)) {
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void disableFog(EntityViewRenderEvent.FogDensity event) {
        EntityLivingBase entity = event.entity;
        ItemStack equipment = entity.getEquipmentInSlot(4);
        if (canFogify(entity, equipment) && currentFogPosition == maximumFogPosition) {
            event.density = (float) 0;
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void changeFogColor(EntityViewRenderEvent.FogColors event) {
        EntityLivingBase entity = event.entity;
        ItemStack equipment = entity.getEquipmentInSlot(4);
        if (canFogify(entity, equipment)) {
            // These colors were based off of the dye hex codes from the Minecraft Wiki.
            // Black #191919
            if (currentFogPosition == 0) {
                event.red = 0.098F;
                event.green = 0.098F;
                event.blue = 0.098F;
            }
            // Red #993333
            if (currentFogPosition == 1) {
                event.red = 0.6F;
                event.green = 0.2F;
                event.blue = 0.2F;
            }
            // Green #667F33
            if (currentFogPosition == 2) {
                event.red = 0.4F;
                event.green = 0.498F;
                event.blue = 0.2F;
            }
            // Brown #664C33
            if (currentFogPosition == 3) {
                event.red = 0.4F;
                event.green = 0.298F;
                event.blue = 0.2F;
            }
            // Blue #334CB2
            if (currentFogPosition == 4) {
                event.red = 0.2F;
                event.green = 0.298F;
                event.blue = 0.698F;
            }
            // Purple #7F3FB2
            if (currentFogPosition == 5) {
                event.red = 0.498F;
                event.green = 0.247F;
                event.blue = 0.698F;
            }
            // Cyan #4C7F99
            if (currentFogPosition == 6) {
                event.red = 0.298F;
                event.green = 0.498F;
                event.blue = 0.6F;
            }
            // Light Gray #999999
            if (currentFogPosition == 7) {
                event.red = 0.6F;
                event.green = 0.6F;
                event.blue = 0.6F;
            }
            // Gray #4C4C4C
            if (currentFogPosition == 8) {
                event.red = 0.298F;
                event.green = 0.298F;
                event.blue = 0.298F;
            }
            // Pink #F27FA5
            if (currentFogPosition == 9) {
                event.red = 0.949F;
                event.green = 0.498F;
                event.blue = 0.647F;
            }
            // Lime #7FCC19
            if (currentFogPosition == 10) {
                event.red = 0.498F;
                event.green = 0.8F;
                event.blue = 0.098F;
            }
            // Dandelion #E5E533
            if (currentFogPosition == 11) {
                event.red = 0.898F;
                event.green = 0.898F;
                event.blue = 0.2F;
            }
            // Light Blue #6699D8
            if (currentFogPosition == 12) {
                event.red = 0.4F;
                event.green = 0.6F;
                event.blue = 0.847F;
            }
            // Magenta #B24CD8
            if (currentFogPosition == 13) {
                event.red = 0.698F;
                event.green = 0.298F;
                event.blue = 0.847F;
            }
            // Orange #D87F33
            if (currentFogPosition == 14) {
                event.red = 0.847F;
                event.green = 0.498F;
                event.blue = 0.2F;
            }
            // White #FFFFFF
            if (currentFogPosition == 15) {
                event.red = 1F;
                event.green = 1F;
                event.blue = 1F;
            }
        }
    }
}
