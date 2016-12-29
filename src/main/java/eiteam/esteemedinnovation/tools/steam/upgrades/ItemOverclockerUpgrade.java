package eiteam.esteemedinnovation.tools.steam.upgrades;

import eiteam.esteemedinnovation.api.SteamChargable;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.api.tool.SteamToolSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;

import javax.annotation.Nonnull;

public class ItemOverclockerUpgrade extends ItemSteamToolUpgrade {
    public ItemOverclockerUpgrade() {
        super(SteamToolSlot.TOOL_CORE, EsteemedInnovation.MOD_ID + ":items/toolUpgrades/overclocker", null, 0);
    }

    @Override
    public boolean onBlockBreakWithTool(BlockEvent.BreakEvent event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        SteamChargable tool = (SteamChargable) toolStack.getItem();
        tool.addSteam(toolStack, -tool.steamPerDurability(), event.getPlayer());
        return true;
    }

    @Override
    public float onUpdateBreakSpeedWithTool(PlayerEvent.BreakSpeed event, float newSpeed, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        return 2.5F / (newSpeed == 0F ? event.getOriginalSpeed() : newSpeed);
    }
}
