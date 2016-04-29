package flaxbeard.steamcraft.api.tool;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import flaxbeard.steamcraft.api.ISteamChargable;

public interface ISteamTool extends ISteamChargable {
    /**
     * Checks if the tool is wound up.
     * @param stack The tool
     * @return Whether the tool has been wound up.
     */
    boolean isWound(ItemStack stack);

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
