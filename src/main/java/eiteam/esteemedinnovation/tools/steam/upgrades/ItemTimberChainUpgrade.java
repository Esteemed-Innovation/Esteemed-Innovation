package eiteam.esteemedinnovation.tools.steam.upgrades;

import eiteam.esteemedinnovation.api.SteamChargable;
import eiteam.esteemedinnovation.api.tool.SteamTool;
import eiteam.esteemedinnovation.api.tool.SteamToolSlot;
import eiteam.esteemedinnovation.commons.util.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;

import javax.annotation.Nonnull;

import static eiteam.esteemedinnovation.tools.ToolsModule.upgradeResource;

public class ItemTimberChainUpgrade extends ItemSteamToolUpgrade {
    public ItemTimberChainUpgrade() {
        super(SteamToolSlot.SAW_HEAD, upgradeResource("timberHead"), null, 1);
    }

    @Override
    public boolean onBlockBreakWithTool(BlockEvent.BreakEvent event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        IBlockState state = event.getState();
        Block block = state.getBlock();
        if (block.isToolEffective(((SteamTool) toolStack.getItem()).toolClass(), state)) {
            fellBlocks(event.getWorld(), event.getPos(), event.getPlayer(), toolStack);
        }
        return true;
    }

    /**
     * Mines all of the log blocks above the starting coordinate.
     * @param world The world instance.
     * @param startPos The starting Block Position
     * @param player The player doing the felling.
     * @param axe The axe's ItemStack
     */
    private static void fellBlocks(World world, BlockPos startPos, EntityPlayer player, ItemStack axe) {
        SteamChargable chargable = (SteamChargable) axe.getItem();
        BlockPos.MutableBlockPos curPos = new BlockPos.MutableBlockPos(startPos);
        for (int y = startPos.getY(); y < 256; y++) {
            curPos.setY(y);
            IBlockState state = world.getBlockState(curPos);
            Block block = state.getBlock();
            if (OreDictHelper.listHasItem(OreDictHelper.logs, Item.getItemFromBlock(block))) {
                world.setBlockToAir(curPos);
                block.harvestBlock(world, player, curPos, state, world.getTileEntity(curPos), axe);
                if (y % 2 == 0) {
                    if (!chargable.addSteam(axe, -chargable.steamPerDurability(), player)) {
                        break;
                    }
                }
            } else {
                break;
            }
        }
    }

    @Override
    public void onUpdateBreakSpeedWithTool(PlayerEvent.BreakSpeed event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        event.setNewSpeed(event.getNewSpeed() * 0.7F);
    }
}
