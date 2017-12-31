package eiteam.esteemedinnovation.metalcasting;

import eiteam.esteemedinnovation.api.book.BookCategory;
import eiteam.esteemedinnovation.api.book.BookSection;
import eiteam.esteemedinnovation.book.BookPieceUnlockedStateChangePacket;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.commons.capabilities.player.PlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.Collection;

public class MetalcastingBookSection extends BookSection {
    public static final String NAME = EsteemedInnovation.CASTING_SECTION;

    public MetalcastingBookSection(BookCategory... categories) {
        super(NAME, categories);
    }

    @Override
    public boolean isUnlocked(EntityPlayer player) {
        PlayerData data = player.getCapability(EsteemedInnovation.PLAYER_DATA, null);
        return data.getAllUnlockedPieces().contains(NAME);
    }

    @Override
    public String getUnlocalizedHint() {
        return "section.MetalCasting.hint";
    }

    @Override
    public Collection<BookCategory> getCategories() {
        return super.getCategories();
    }

    public static class Unlocker {
        @SubscribeEvent
        public void unlockSection(PlayerEvent.ItemCraftedEvent event) {
            EntityPlayer crafter = event.player;
            ItemStack output = event.crafting;
            Item anvil = Item.getItemFromBlock(Blocks.ANVIL);
            if (anvil != Items.AIR && output.getItem() == anvil && crafter instanceof EntityPlayerMP) {
                PlayerData data = crafter.getCapability(EsteemedInnovation.PLAYER_DATA, null);
                if (data.setHasUnlockedBookPiece(NAME, true)) {
                    EsteemedInnovation.channel.sendTo(new BookPieceUnlockedStateChangePacket(NAME, true), (EntityPlayerMP) crafter);
                }
            }
        }
    }
}
