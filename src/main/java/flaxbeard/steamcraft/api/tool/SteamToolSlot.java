package flaxbeard.steamcraft.api.tool;

public enum SteamToolSlot {
    drillHead(0, 0),
    sawHead(1, 0),
    shovelHead(2, 0),
    toolCore(-1, 1);

    public int tool;
    public int slot;

    SteamToolSlot(int t, int s) {
        tool = t;
        slot = s;
    }
}
