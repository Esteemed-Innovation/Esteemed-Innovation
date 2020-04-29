package eiteam.esteemedinnovation.firearms.flintlock;

import eiteam.esteemedinnovation.api.book.BookCategory;
import eiteam.esteemedinnovation.api.book.BookEntry;
import eiteam.esteemedinnovation.book.BookPieceUnlockedStateChangePacket;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.commons.capabilities.player.PlayerData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashSet;

public class FlintlockBookCategory extends BookCategory {
    private static final Collection<String> ALL_NAMES = new HashSet<>();

    public FlintlockBookCategory(@Nonnull String name, BookEntry... entries) {
        super(name, entries);
        ALL_NAMES.add(name);
    }

    @Override
    public boolean isUnlocked(EntityPlayer player) {
        PlayerData data = player.getCapability(EsteemedInnovation.PLAYER_DATA, null);
        return data.getAllUnlockedPieces().contains(getName());
    }

    @Override
    public String getUnlocalizedHint() {
        return "category.Flintlock.hint";
    }

    public static class EventHandler {
        @SubscribeEvent
        public void inspirePlayerWithBoom(ExplosionEvent.Detonate event) {
            for (Entity entity : event.getAffectedEntities()) {
                if (entity instanceof EntityPlayerMP) {
                    EntityPlayerMP player = (EntityPlayerMP) entity;
                    PlayerData data = player.getCapability(EsteemedInnovation.PLAYER_DATA, null);
                    for (String name : ALL_NAMES) {
                        if (data.setHasUnlockedBookPiece(name, true)) {
                            EsteemedInnovation.channel.sendTo(new BookPieceUnlockedStateChangePacket(name, true), player);
                        }
                    }
                }
            }
        }
    }
}
