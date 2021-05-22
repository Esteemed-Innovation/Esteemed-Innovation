package eiteam.esteemedinnovation.tools.steam.upgrades;

import eiteam.esteemedinnovation.api.SteamChargable;
import eiteam.esteemedinnovation.api.tool.SteamToolSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;

import javax.annotation.Nonnull;

import static eiteam.esteemedinnovation.tools.ToolsModule.upgradeResource;

public class ItemOverclockerUpgrade extends ItemSteamToolUpgrade {
    public ItemOverclockerUpgrade() {
        super(SteamToolSlot.TOOL_CORE, upgradeResource("overclocker"), null, 0);
    }

    @Override
    public boolean onBlockBreakWithTool(BlockEvent.BreakEvent event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        SteamChargable tool = (SteamChargable) toolStack.getItem();
        tool.addSteam(toolStack, -tool.steamPerDurability(), event.getPlayer());
        return true;
    }

    @Override
    public void onUpdateBreakSpeedWithTool(PlayerEvent.BreakSpeed event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        event.setNewSpeed(2F * event.getNewSpeed());
    }
}
