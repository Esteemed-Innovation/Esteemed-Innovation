package eiteam.esteemedinnovation.commons.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class JumpValueChangePacket implements IMessage {
    private boolean isJumping;

    public JumpValueChangePacket() {}

    public JumpValueChangePacket(boolean isJumping) {
        this.isJumping = isJumping;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        isJumping = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(isJumping);
    }

    public boolean getIsJumping() {
        return isJumping;
    }
}
