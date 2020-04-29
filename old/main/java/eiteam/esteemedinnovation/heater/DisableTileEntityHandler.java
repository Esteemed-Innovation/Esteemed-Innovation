package eiteam.esteemedinnovation.heater;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class DisableTileEntityHandler {
    public static List<TileEntity> tileEntitiesToRemove = new ArrayList<>();
    public static List<TileEntity> tileEntitiesToAdd = new ArrayList<>();

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            for (TileEntity tileEntity : tileEntitiesToAdd) {
                if (!event.world.isBlockLoaded(tileEntity.getPos())) {
                    continue;
                }
                if (!event.world.tickableTileEntities.contains(tileEntity)) {
                    event.world.tickableTileEntities.add(tileEntity);
                }
            }
            tileEntitiesToAdd.clear();
            for (TileEntity tileEntity : tileEntitiesToRemove) {
                if (!event.world.isBlockLoaded(tileEntity.getPos())) {
                    continue;
                }
                event.world.tickableTileEntities.remove(tileEntity);
            }
            tileEntitiesToRemove.clear();
        }
    }
}
