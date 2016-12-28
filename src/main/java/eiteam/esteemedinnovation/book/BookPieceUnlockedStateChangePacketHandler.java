package eiteam.esteemedinnovation.book;

import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.commons.capabilities.player.IPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BookPieceUnlockedStateChangePacketHandler implements IMessageHandler<BookPieceUnlockedStateChangePacket, IMessage> {
    @Override
    public IMessage onMessage(BookPieceUnlockedStateChangePacket message, MessageContext ctx) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        IPlayerData data = player.getCapability(EsteemedInnovation.PLAYER_DATA, null);
        data.setHasUnlockedBookPiece(message.getPieceChanged(), message.getNewValueForPiece());
        return null;
    }
}
