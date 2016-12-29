package eiteam.esteemedinnovation.tools.steam.upgrades;

import eiteam.esteemedinnovation.api.tool.SteamTool;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.api.SteamingRegistry;
import eiteam.esteemedinnovation.api.tool.SteamToolSlot;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;

import javax.annotation.Nonnull;

public class ItemExothermicProjector extends ItemSteamToolUpgrade {
    public ItemExothermicProjector() {
        super(SteamToolSlot.TOOL_CORE, EsteemedInnovation.MOD_ID + ":items/toolUpgrades/furnace", null, 0);
    }

    @Override
    public void onPlayerHarvestDropsWithTool(BlockEvent.HarvestDropsEvent event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        IBlockState state = event.getState();
        Block block = state.getBlock();
        SteamTool tool = (SteamTool) toolStack.getItem();
        if (event.getDrops().isEmpty() || !block.isToolEffective(tool.toolClass(), state)) {
            return;
        }
        int itemsSmelted = 0;
        for (int i = 0; i < event.getDrops().size(); i++) {
            ItemStack drop = event.getDrops().get(i);
            if (drop == null || drop.getItem() == null) {
                continue;
            }

            ItemStack output = SteamingRegistry.getSteamingResult(drop);
            if (output == null || output.getItem() == null) {
                continue;
            }
            event.getDrops().remove(i);
            event.getDrops().add(i, output.copy());
            itemsSmelted += 1;
        }
        if (itemsSmelted > 0) {
            tool.addSteam(toolStack, -(itemsSmelted * tool.steamPerDurability()), event.getHarvester());
        }
    }
}
