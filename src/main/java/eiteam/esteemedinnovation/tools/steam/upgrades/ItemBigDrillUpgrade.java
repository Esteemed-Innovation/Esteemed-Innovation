package eiteam.esteemedinnovation.tools.steam.upgrades;

import eiteam.esteemedinnovation.api.tool.SteamTool;
import eiteam.esteemedinnovation.api.tool.SteamToolSlot;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;

import javax.annotation.Nonnull;

import static eiteam.esteemedinnovation.commons.util.WorldHelper.getExtraBlockCoordinates;
import static eiteam.esteemedinnovation.commons.util.WorldHelper.mineExtraBlocks;
import static eiteam.esteemedinnovation.tools.ToolsModule.upgradeResource;

public class ItemBigDrillUpgrade extends ItemSteamToolUpgrade {
    public ItemBigDrillUpgrade() {
        super(SteamToolSlot.DRILL_HEAD, upgradeResource("big"), null, 1);
    }

    @Override
    public boolean onBlockBreakWithTool(BlockEvent.BreakEvent event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        EntityPlayer player = event.getPlayer();
        BlockPos pos = event.getPos();
        World world = event.getWorld();
        IBlockState state = event.getState();
        Block block = state.getBlock();
        Item toolItem = toolStack.getItem();

        RayTraceResult ray = ((SteamTool) toolItem).rayTrace(world, player, false);
        if (ray != null && block.isToolEffective(((SteamTool) toolItem).toolClass(), state)) {
            mineExtraBlocks(getExtraBlockCoordinates(ray.sideHit), pos, world, (ItemTool) toolItem, toolStack, player);
        }

        return true;
    }

    @Override
    public void onUpdateBreakSpeedWithTool(PlayerEvent.BreakSpeed event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        event.setNewSpeed(event.getNewSpeed() * 0.7F);
    }
}
