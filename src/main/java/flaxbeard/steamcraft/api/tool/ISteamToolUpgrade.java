package flaxbeard.steamcraft.api.tool;

import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public interface ISteamToolUpgrade {

    /**
     * The priority of the upgrade's rendering. This is essentially the render pass.
     * Typically, you will use 1 for head upgrades, and 2 for core upgrades because head should go
     * on top of the body, logically. Each upgrade should have a 0 and 1 texture, for the two
     * different icons rendered at alternating ticks.
     */
    int renderPriority();

    /**
     * The slot that the upgrade can be used on
     *
     * See SteamToolSlot.java for the list of slots.
     */
    SteamToolSlot getToolSlot();

    void writeInfo(List list);

    /**
     * The two icons for the upgrade.
     * @return The IIcon array.
     */
    IIcon[] getIIcons();
}
