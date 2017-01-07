package eiteam.esteemedinnovation.tools.steam.upgrades;

import eiteam.esteemedinnovation.api.tool.SteamTool;
import eiteam.esteemedinnovation.api.tool.SteamToolSlot;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

import javax.annotation.Nonnull;

import static eiteam.esteemedinnovation.tools.ToolsModule.upgradeResource;

public class ItemForestFireUpgrade extends ItemSteamToolUpgrade {
    public ItemForestFireUpgrade() {
        super(SteamToolSlot.SAW_HEAD, upgradeResource("fire"), null, 1);
    }

    @Override
    public boolean onBlockBreakWithTool(BlockEvent.BreakEvent event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        World world = event.getWorld();
        EntityPlayer player = event.getPlayer();
        RayTraceResult rayTraceResult = ((SteamTool) toolStack.getItem()).rayTrace(world, player, false);
        if (rayTraceResult != null) {
            burnBlocks(world, event.getPos(), rayTraceResult.sideHit);
        }
        return true;
    }

    /**
     * Burns all log blocks within a 5 block radius.
     * @param world The world
     * @param startPos The starting Block Position
     */
    private static void burnBlocks(World world, BlockPos startPos, EnumFacing side) {
        int startX = startPos.getX();
        int startY = startPos.getY();
        int startZ = startPos.getZ();
        for (int x = startX - 5; x < startX + 5; x++) {
            for (int y = startY - 5; y < startY + 5; y++) {
                for (int z = startZ - 5; z < startZ + 5; z++) {
                    BlockPos curPos = new BlockPos(x, y, z);
                    IBlockState state = world.getBlockState(curPos);
                    Block block = state.getBlock();
                    if (block == null || world.isAirBlock(curPos)) {
                        continue;
                    }
                    if (block.isFlammable(world, curPos, side)) {
                        world.setBlockState(curPos, Blocks.FIRE.getDefaultState());
                    }
                }
            }
        }
    }
}
