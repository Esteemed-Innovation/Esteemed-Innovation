package flaxbeard.steamcraft.api.modulartool;

/**
 * Created by elijahfoster-wysocki on 10/25/14.
 */
public enum ToolSlot {
    tank(0),
    slot(1);

    public int slotInt;

    private ToolSlot(int s){
        slotInt = s;
    }
}
