package eiteam.esteemedinnovation.tools.steam.upgrades;

import eiteam.esteemedinnovation.api.tool.SteamTool;
import eiteam.esteemedinnovation.api.tool.SteamToolSlot;
import eiteam.esteemedinnovation.commons.util.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.Random;

import static eiteam.esteemedinnovation.tools.ToolsModule.upgradeResource;

public class ItemStoneGrinderUpgrade extends ItemSteamToolUpgrade {
    public ItemStoneGrinderUpgrade() {
        super(SteamToolSlot.DRILL_HEAD, upgradeResource("grinder"), null, 1);
    }

    @Override
    public void onPlayerHarvestDropsWithTool(BlockEvent.HarvestDropsEvent event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        Random rand = event.getWorld().rand;
        IBlockState state = event.getState();
        Block block = state.getBlock();
        Item blockItem = Item.getItemFromBlock(block);
        int meta = block.getMetaFromState(state);
        Pair<Item, Integer> pair = Pair.of(blockItem, meta);

        if (OreDictHelper.cobblestones.contains(pair)) {
            return;
        }

        String harvestTool = block.getHarvestTool(state);
        // Docs say it can be null.
        //noinspection ConstantConditions
        if (harvestTool == null || !harvestTool.equals(((SteamTool) toolStack.getItem()).toolClass())) {
            return;
        }
        boolean addedNugget = false;
        for (int i = 0; i < event.getDrops().size(); i++) {
            ItemStack drop = event.getDrops().get(i);
            Pair item = Pair.of(drop.getItem(), drop.getItemDamage());
            if (!OreDictHelper.stones.contains(item) && !OreDictHelper.cobblestones.contains(item)) {
                continue;
            }

            event.getDrops().remove(i);
            int chance = rand.nextInt(5);
            if (chance != 3 || addedNugget) {
                continue;
            }

            int index = rand.nextInt(OreDictHelper.stoneGrinderNuggets.size());
            Pair nuggetPair = OreDictHelper.stoneGrinderNuggets.get(index);
            int size = rand.nextInt(3) + 1;
            ItemStack nugget = new ItemStack((Item) nuggetPair.getLeft(), size, (int) nuggetPair.getRight());
            event.getDrops().add(nugget);
            addedNugget = true;
        }
    }
}
