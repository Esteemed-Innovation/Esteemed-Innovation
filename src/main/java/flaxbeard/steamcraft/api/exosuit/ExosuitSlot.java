package flaxbeard.steamcraft.api.exosuit;

public enum ExosuitSlot {
    bootsFeet(3, 3),
    bootsTop(3, 2),
    bodyFront(1, 2),
    bodyHand(1, 3),
    bodyTank(1, 4),
    headGoggles(0, 3),
    headHelm(0, 2),
    legsHips(2, 2),
    legsLegs(2, 3),
    vanity(0, 1);

    public int slot;
    public int armor;

    private ExosuitSlot(int a, int s) {
        slot = s;
        armor = a;
    }
}
