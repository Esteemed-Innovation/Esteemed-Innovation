package eiteam.esteemedinnovation.armor.exosuit.upgrades.pulsenozzle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class DoubleJumpClientResponsePacketHandler implements IMessageHandler<DoubleJumpClientResponsePacket, IMessage> {
    @Override
    public IMessage onMessage(DoubleJumpClientResponsePacket message, MessageContext ctx) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        player.motionY = 0.65D;
        player.fallDistance = 0.0F;
        return null;
    }
}
