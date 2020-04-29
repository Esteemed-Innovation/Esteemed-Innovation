package eiteam.esteemedinnovation.commons.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class JumpValueChangePacketHandler implements IMessageHandler<JumpValueChangePacket, IMessage> {
    @Override
    public IMessage onMessage(JumpValueChangePacket message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().player;
        player.setJumping(message.getIsJumping());
        return null;
    }
}
