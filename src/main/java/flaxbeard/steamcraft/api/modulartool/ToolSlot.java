package flaxbeard.steamcraft.api.modulartool;

/**
 * Created by elijahfoster-wysocki on 10/25/14.
 */
public enum ToolSlot {
    tank(5),
    slot1(4),
    slot2(3),
    slot3(2),
    slot4(1);

    public int slot;

    private ToolSlot(int s){
        slot = s;
    }
}
