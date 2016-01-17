package flaxbeard.steamcraft.api.tool;

public enum SteamToolSlot {
    drillHead(0, 0),
    drillCore(0, 1),
    sawHead(1, 0),
    sawCore(1, 1),
    shovelHead(2, 0),
    shovelCore(2, 1),
    toolCore(-1, 1);

    public int tool;
    public int slot;

    SteamToolSlot(int t, int s) {
        tool = t;
        slot = s;
    }
}
