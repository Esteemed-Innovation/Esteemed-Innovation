package eiteam.esteemedinnovation.tools.steam.upgrades;

import eiteam.esteemedinnovation.api.tool.SteamToolSlot;
import eiteam.esteemedinnovation.commons.util.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;

import static eiteam.esteemedinnovation.tools.ToolsModule.upgradeResource;

public class ItemSifterUpgrade extends ItemSteamToolUpgrade {
    public ItemSifterUpgrade() {
        super(SteamToolSlot.SHOVEL_CORE, upgradeResource("sifter"), null, 0);
    }

    @Override
    public void onPlayerHarvestDropsWithTool(BlockEvent.HarvestDropsEvent event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        World world = event.getWorld();
        IBlockState state = event.getState();
        Block block = state.getBlock();
        Item otherBlockItem = block.getItemDropped(state, world.rand, 0);
        Item blockItem = Item.getItemFromBlock(block);
        int meta = block.getMetaFromState(state);
        Pair<Item, Integer> pair = Pair.of(blockItem, meta);

        for (int i = 0; i < event.getDrops().size(); i++) {
            Item item = event.getDrops().get(i).getItem();
            if (item == blockItem || item == otherBlockItem) {
                event.getDrops().remove(i);
            }
        }

        if (OreDictHelper.sands.contains(pair)) {
            if (world.rand.nextInt(8) == 5) {
                int index = world.rand.nextInt(OreDictHelper.goldNuggets.size());
                Pair nuggetPair = OreDictHelper.goldNuggets.get(index);
                int size = world.rand.nextInt(3) + 1;
                event.getDrops().add(new ItemStack((Item) nuggetPair.getLeft(), size, (int) nuggetPair.getRight()));
                return;
            }
        }

        if (block == Blocks.DIRT || block == Blocks.GRASS || block == Blocks.FARMLAND) {
            if (world.rand.nextInt(6) == 4) {
                int boneOrSeeds = world.rand.nextInt(2);
                int stackSize = world.rand.nextInt(3) + 1;
                event.getDrops().add(new ItemStack(boneOrSeeds == 1 ? Items.BONE : Items.WHEAT_SEEDS, stackSize));
                return;
            }
        }

        if (block == Blocks.GRAVEL) {
            for (int i = 0; i < event.getDrops().size(); i++) {
                if (event.getDrops().get(i).getItem() == Items.FLINT) {
                    event.setDropChance(90);
                    return;
                }
            }
        }
    }
}
