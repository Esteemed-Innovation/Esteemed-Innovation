package eiteam.esteemedinnovation.tools.steam.upgrades;

import eiteam.esteemedinnovation.api.tool.SteamToolSlot;
import eiteam.esteemedinnovation.commons.handler.FieldHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.world.BlockEvent;

import javax.annotation.Nonnull;

import static eiteam.esteemedinnovation.tools.ToolsModule.upgradeResource;

public class ItemPreciseCuttingHeadUpgrade extends ItemSteamToolUpgrade {
    public ItemPreciseCuttingHeadUpgrade() {
        super(SteamToolSlot.DRILL_HEAD, upgradeResource("preciseCuttingHead"), null, 1);
    }

    @Override
    public boolean onBlockBreakWithTool(BlockEvent.BreakEvent event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        event.setExpToDrop(0);
        return true;
    }

    @Override
    public void onPlayerHarvestDropsWithTool(BlockEvent.HarvestDropsEvent event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        EntityPlayer player = event.getHarvester();
        BlockPos pos = event.getPos();
        World world = event.getWorld();
        IBlockState state = event.getState();
        Block block = state.getBlock();

        if (ForgeHooks.canHarvestBlock(block, player, world, pos) && block.canSilkHarvest(world, pos, state, player)) {
            try {
                ItemStack toAdd = (ItemStack) FieldHandler.createStackedBlockMethod.invoke(block, state);
                if (toAdd != null) {
                    event.getDrops().clear();
                    event.getDrops().add(toAdd);
                }
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }
    }
}
