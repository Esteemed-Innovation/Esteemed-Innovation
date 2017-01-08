package eiteam.esteemedinnovation.tools.steam.upgrades;

import eiteam.esteemedinnovation.api.tool.SteamToolSlot;
import eiteam.esteemedinnovation.commons.util.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import javax.annotation.Nonnull;

import static eiteam.esteemedinnovation.tools.ToolsModule.upgradeResource;

public class ItemLeafBlowerUpgrade extends ItemSteamToolUpgrade {
    public ItemLeafBlowerUpgrade() {
        super(SteamToolSlot.SAW_HEAD, upgradeResource("blower"), null, 1);
    }

    @Override
    public void onUpdateBreakSpeedWithTool(PlayerEvent.BreakSpeed event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        event.setNewSpeed(event.getNewSpeed() / 5F);
    }

    @Override
    public boolean onLeftClickBlockWithTool(PlayerInteractEvent.LeftClickBlock event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        blowLeaves(WorldHelper.getExtraBlock9Coordinates(event.getFace()), event.getPos(), event.getWorld(), event.getEntityPlayer(), toolStack);
        return true;
    }

    /**
     * Harvests the coordinates in the coordinate array.
     * @param coordinateArray The two-dimensional array containing coordinates to add to x, y, z.
     * @param startPos The starting position
     * @param world The world.
     * @param player The player mining.
     * @param stack The tool being used to mine.
     */
    private void blowLeaves(int[][] coordinateArray, BlockPos startPos, World world, EntityPlayer player, ItemStack stack) {
        for (int[] aCoordinateArray : coordinateArray) {
            int thisX = startPos.getX() + aCoordinateArray[0];
            int thisY = startPos.getY() + aCoordinateArray[1];
            int thisZ = startPos.getZ() + aCoordinateArray[2];
            BlockPos thisPos = new BlockPos(thisX, thisY, thisZ);
            IBlockState state = world.getBlockState(thisPos);
            Block block = state.getBlock();
            if (block == null || world.isAirBlock(thisPos)) {
                continue;
            }
            if (WorldHelper.isLeaves(block, world, thisPos)) {
                world.setBlockToAir(thisPos);
                block.harvestBlock(world, player, thisPos, state, world.getTileEntity(thisPos), stack);
            }
        }
    }
}
