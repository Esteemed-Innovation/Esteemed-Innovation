package eiteam.esteemedinnovation.api.exosuit;

import net.minecraft.inventory.EntityEquipmentSlot;

public enum ExosuitSlot {
    BOOTS_FEET(EntityEquipmentSlot.FEET, 3),
    BOOTS_TOP(EntityEquipmentSlot.FEET, 2),
    BODY_FRONT(EntityEquipmentSlot.CHEST, 2),
    BODY_HAND(EntityEquipmentSlot.CHEST, 3),
    BODY_TANK(EntityEquipmentSlot.CHEST, 4),
    HEAD_GOGGLES(EntityEquipmentSlot.HEAD, 3),
    HEAD_HELM(EntityEquipmentSlot.HEAD, 2),
    LEGS_HIPS(EntityEquipmentSlot.LEGS, 2),
    LEGS_LEGS(EntityEquipmentSlot.LEGS, 3),
    VANITY(EntityEquipmentSlot.HEAD, 1);

    private final int slot;
    private final EntityEquipmentSlot armor;

    ExosuitSlot(EntityEquipmentSlot armor, int slot) {
        this.armor = armor;
        this.slot = slot;
    }

    public int getEngineeringSlot() {
        return slot;
    }

    public EntityEquipmentSlot getArmorPiece() {
        return armor;
    }
}
