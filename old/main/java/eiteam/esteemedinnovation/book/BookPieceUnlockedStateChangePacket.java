package eiteam.esteemedinnovation.book;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class BookPieceUnlockedStateChangePacket implements IMessage {
    private boolean newValue;
    private String piece;

    public BookPieceUnlockedStateChangePacket() {}

    public BookPieceUnlockedStateChangePacket(String piece, boolean newValue) {
        this.newValue = newValue;
        this.piece = piece;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        newValue = buf.readBoolean();
        piece = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(newValue);
        ByteBufUtils.writeUTF8String(buf, piece);
    }

    String getPieceChanged() {
        return piece;
    }

    boolean getNewValueForPiece() {
        return newValue;
    }
}
