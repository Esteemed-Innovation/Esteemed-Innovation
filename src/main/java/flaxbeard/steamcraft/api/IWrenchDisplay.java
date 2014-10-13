package flaxbeard.steamcraft.api;

import net.minecraftforge.client.event.RenderGameOverlayEvent;

public interface IWrenchDisplay {
    public void displayWrench(RenderGameOverlayEvent.Post event);
}
