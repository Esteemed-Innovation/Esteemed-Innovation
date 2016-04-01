package flaxbeard.steamcraft.api.exosuit;

public enum ExosuitSlot {
    BOOTS_FEET(3, 3),
    BOOTS_TOP(3, 2),
    BODY_FRONT(1, 2),
    BODY_HAND(1, 3),
    BODY_TANK(1, 4),
    HEAD_GOGGLES(0, 3),
    HEAD_HELM(0, 2),
    LEGS_HIPS(2, 2),
    LEGS_LEGS(2, 3),
    VANITY(0, 1);

    public int slot;
    public int armor;

    ExosuitSlot(int a, int s) {
        slot = s;
        armor = a;
    }
}
