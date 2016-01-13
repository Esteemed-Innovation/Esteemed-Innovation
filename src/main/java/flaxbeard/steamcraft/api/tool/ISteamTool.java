package flaxbeard.steamcraft.api.tool;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface ISteamTool {
    /**
     * Checks if the tool is wound up.
     * @param player The player to get the info for.
     * @return Whether the tool has been wound by the player.
     */
    boolean isWound(EntityPlayer player);

    /**
     * Checks if the tool has a particular upgrade.
     * @param me The ItemStack version of the tool
     * @param check The item that is being checked against, or the upgrade
     * @return Whether it has any upgrades.
     */
    boolean hasUpgrade(ItemStack me, Item check);

    /**
     * @return The Vanilla tool class associated with this tool.
     */
    String toolClass();
}
