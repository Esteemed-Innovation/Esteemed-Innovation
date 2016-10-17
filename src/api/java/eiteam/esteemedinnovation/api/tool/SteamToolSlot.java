package eiteam.esteemedinnovation.api.tool;

public enum SteamToolSlot {
    DRILL_HEAD(0, 0),
    DRILL_CORE(0, 1),
    SAW_HEAD(1, 0),
    SAW_CORE(1, 1),
    SHOVEL_HEAD(2, 0),
    SHOVEL_CORE(2, 1),
    TOOL_CORE(-1, 1);

    public int tool;
    public int slot;

    SteamToolSlot(int t, int s) {
        tool = t;
        slot = s;
    }
}
