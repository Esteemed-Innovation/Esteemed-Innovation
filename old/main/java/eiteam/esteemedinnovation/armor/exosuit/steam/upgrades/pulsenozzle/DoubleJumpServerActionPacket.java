package eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.pulsenozzle;

import eiteam.esteemedinnovation.api.util.ItemStackUtility;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class DoubleJumpServerActionPacket implements IMessage {
    private EntityEquipmentSlot slot;

    public DoubleJumpServerActionPacket() {}

    public DoubleJumpServerActionPacket(EntityEquipmentSlot slot) {
        this.slot = slot;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        slot = ItemStackUtility.getSlotFromSlotIndex(buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(slot.getSlotIndex());
    }

    EntityEquipmentSlot getSlot() {
        return slot;
    }
}
