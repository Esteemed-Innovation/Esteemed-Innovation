package eiteam.esteemedinnovation.api.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

/**
 * A simple utility class for interacting with Minecraft's inventories, containers, and slots.
 */
public final class InventoryUtility {
    /**
     * A Field for use with reflection which retrieves the {@link InventoryCrafting#eventHandler} field.
     */
    private static final Field INVENTORYCRAFTING_EVENT_HANDLER_FIELD = ReflectionHelper.findField(InventoryCrafting.class, "eventHandler", "field_70465_c");

    /**
     * A Field for use with reflection which retrieves the {@link ContainerPlayer#thePlayer} field.
     */
    private static final Field CONTAINERPLAYER_PLAYER_FIELD = ReflectionHelper.findField(ContainerPlayer.class, "thePlayer", "field_82862_h");

    /**
     * A Field for use with reflection which retrieves the {@link SlotCrafting#thePlayer} field.
     */
    private static final Field SLOTCRAFTING_PLAYER_FIELD = ReflectionHelper.findField(SlotCrafting.class, "thePlayer", "field_75238_b");

    /**
     * Gets the player currently using this {@link InventoryCrafting}. This has support for the crafting inventory
     * in {@link ContainerPlayer} (player's inventory) and implementers of {@link ContainerWorkbench}.
     * @param inv The current crafting inventory.
     * @return The player, or null if there either is no player, or they are using a weird crafting table we don't know about.
     */
    @Nullable
    public static EntityPlayer getCurrentPlayerFromInventoryCrafting(InventoryCrafting inv) {
        try {
            Container container = (Container) INVENTORYCRAFTING_EVENT_HANDLER_FIELD.get(inv);
            if (container instanceof ContainerPlayer) {
                return (EntityPlayer) CONTAINERPLAYER_PLAYER_FIELD.get(container);
            } else if (container instanceof ContainerWorkbench) {
                return (EntityPlayer) SLOTCRAFTING_PLAYER_FIELD.get(container.getSlot(0));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
