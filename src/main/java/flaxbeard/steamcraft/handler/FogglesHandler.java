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
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.client.ClientProxy;
import flaxbeard.steamcraft.item.ItemExosuitArmor;
import flaxbeard.steamcraft.item.ItemFoggles;

public class FogglesHandler {
    public int maximumFogPosition = 16;
    public int currentFogPosition = maximumFogPosition;

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void toggleFogColorChange(InputEvent.KeyInputEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        ItemStack equipment = player.getEquipmentInSlot(4);
        if (ClientProxy.keyBindings.get("foggles").getIsKeyPressed() && canFogify(player, equipment)) {
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
        if (entity != null && helmet != null && entity instanceof EntityPlayer) {
            if (helmet.getItem() instanceof ItemExosuitArmor) {
                ItemExosuitArmor helmetArmor = (ItemExosuitArmor) helmet.getItem();
                EntityLivingBase elb = (EntityLivingBase) entity;
                if (SteamcraftEventHandler.hasPower(elb, 1) &&
                  helmetArmor.hasUpgrade(helmet, SteamcraftItems.foggles)) {
                    return true;
                }
            } else if (helmet.getItem() instanceof ItemFoggles) {
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
            switch (currentFogPosition) {
                // Black #191919
                case 0: {
                    event.red = 0.098F;
                    event.green = 0.098F;
                    event.blue = 0.098F;
                    break;
                }
                // Red #993333
                case 1: {
                    event.red = 0.6F;
                    event.green = 0.2F;
                    event.blue = 0.2F;
                    break;
                }
                // Green #667F33
                case 2: {
                    event.red = 0.4F;
                    event.green = 0.498F;
                    event.blue = 0.2F;
                    break;
                }
                // Brown #664C33
                case 3: {
                    event.red = 0.4F;
                    event.green = 0.298F;
                    event.blue = 0.2F;
                    break;
                }
                // Blue #334CB2
                case 4: {
                    event.red = 0.2F;
                    event.green = 0.298F;
                    event.blue = 0.698F;
                    break;
                }
                // Purple #7F3FB2
                case 5: {
                    event.red = 0.498F;
                    event.green = 0.247F;
                    event.blue = 0.698F;
                    break;
                }
                // Cyan #4C7F99
                case 6: {
                    event.red = 0.298F;
                    event.green = 0.498F;
                    event.blue = 0.6F;
                    break;
                }
                // Light Gray #999999
                case 7: {
                    event.red = 0.6F;
                    event.green = 0.6F;
                    event.blue = 0.6F;
                    break;
                }
                // Gray #4C4C4C
                case 8: {
                    event.red = 0.298F;
                    event.green = 0.298F;
                    event.blue = 0.298F;
                    break;
                }
                // Pink #F27FA5
                case 9: {
                    event.red = 0.949F;
                    event.green = 0.498F;
                    event.blue = 0.647F;
                    break;
                }
                // Lime #7FCC19
                case 10: {
                    event.red = 0.498F;
                    event.green = 0.8F;
                    event.blue = 0.098F;
                    break;
                }
                // Dandelion #E5E533
                case 11: {
                    event.red = 0.898F;
                    event.green = 0.898F;
                    event.blue = 0.2F;
                    break;
                }
                // Light Blue #6699D8
                case 12: {
                    event.red = 0.4F;
                    event.green = 0.6F;
                    event.blue = 0.847F;
                    break;
                }
                // Magenta #B24CD8
                case 13: {
                    event.red = 0.698F;
                    event.green = 0.298F;
                    event.blue = 0.847F;
                    break;
                }
                // Orange #D87F33
                case 14: {
                    event.red = 0.847F;
                    event.green = 0.498F;
                    event.blue = 0.2F;
                    break;
                }
                // White #FFFFFF
                case 15: {
                    event.red = 1F;
                    event.green = 1F;
                    event.blue = 1F;
                    break;
                }
            }
        }
    }
}
