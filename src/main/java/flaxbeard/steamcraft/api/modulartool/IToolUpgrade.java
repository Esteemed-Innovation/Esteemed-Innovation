package flaxbeard.steamcraft.api.modulartool;

import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * Created by elijahfoster-wysocki on 10/25/14.
 */
public interface IToolUpgrade {

    public ToolSlot getSlot();

    public ResourceLocation getOverlay();

    public void writeInfo(List list);
}
