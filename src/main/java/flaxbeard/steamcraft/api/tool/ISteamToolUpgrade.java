package flaxbeard.steamcraft.api.tool;

import net.minecraft.util.ResourceLocation;

import java.util.List;

public interface ISteamToolUpgrade {

    /**
     * The priority of the upgrade's rendering
     *
     * Use 1 if it should overwrite the tool's rendering
     * Use 0 if it should not overwrite the tool's rendering
     */
    public int renderPriority();

    /**
     * The slot that the upgrade can be used on
     *
     * See SteamToolSlot.java for the list of slots.
     */
    public SteamToolSlot getSlot();

    public ResourceLocation getOverlay();

    public void writeInfo(List list);
}
