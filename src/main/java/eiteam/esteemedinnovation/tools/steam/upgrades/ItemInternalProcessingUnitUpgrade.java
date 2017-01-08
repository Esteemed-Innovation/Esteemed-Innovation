package eiteam.esteemedinnovation.tools.steam.upgrades;

import eiteam.esteemedinnovation.api.SmasherRegistry;
import eiteam.esteemedinnovation.api.SteamChargable;
import eiteam.esteemedinnovation.api.tool.SteamToolSlot;
import eiteam.esteemedinnovation.commons.util.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.List;

import static eiteam.esteemedinnovation.tools.ToolsModule.upgradeResource;

public class ItemInternalProcessingUnitUpgrade extends ItemSteamToolUpgrade {
    public ItemInternalProcessingUnitUpgrade() {
        super(SteamToolSlot.DRILL_CORE, upgradeResource("processor"), null, 0);
    }

    @Override
    public void onPlayerHarvestDropsWithTool(BlockEvent.HarvestDropsEvent event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        World world = event.getWorld();
        IBlockState state = event.getState();
        Block block = state.getBlock();
        Item blockItem = Item.getItemFromBlock(block);
        int meta = block.getMetaFromState(state);
        if (OreDictHelper.cobblestones.contains(Pair.of(blockItem, meta))) {
            return;
        }

        SteamChargable drill = (SteamChargable) toolStack.getItem();
        List<ItemStack> out = SmasherRegistry.getOutput(new ItemStack(block, 1, meta), world);
        if (!out.isEmpty()) {
            for (int i = 0; i < event.getDrops().size(); i++) {
                ItemStack drop = event.getDrops().get(i);
                if (drop.getItem() == blockItem && drop.getItemDamage() == meta) {
                    event.getDrops().remove(i);
                }
            }
            event.getDrops().addAll(out);
            drill.addSteam(toolStack, -(2 * drill.steamPerDurability()), event.getHarvester());
        }
    }

    @Override
    public void onUpdateBreakSpeedWithTool(PlayerEvent.BreakSpeed event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        event.setNewSpeed(event.getNewSpeed() / 2);
    }
}
