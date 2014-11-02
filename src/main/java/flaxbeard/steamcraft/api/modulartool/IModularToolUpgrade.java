package flaxbeard.steamcraft.api.modulartool;

import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * @author SatanicSanta
 */
public interface IModularToolUpgrade {

    public int renderPriority();

    public ToolSlot getSlot();

    public ResourceLocation getOverlay();

    public void writeInfo(List list);
}
