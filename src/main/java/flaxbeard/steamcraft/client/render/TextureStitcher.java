package flaxbeard.steamcraft.client.render;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import flaxbeard.steamcraft.block.BlockSteamcraftCrucible;

public class TextureStitcher {
    @SubscribeEvent
    public void stitch(TextureStitchEvent.Pre event) {
        event.getMap().registerSprite(BlockSteamcraftCrucible.LIQUID_ICON_RL);
    }
}
