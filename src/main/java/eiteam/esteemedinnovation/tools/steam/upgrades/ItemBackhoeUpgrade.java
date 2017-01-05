package eiteam.esteemedinnovation.tools.steam.upgrades;

import eiteam.esteemedinnovation.api.tool.SteamTool;
import eiteam.esteemedinnovation.api.tool.SteamToolSlot;
import eiteam.esteemedinnovation.commons.Config;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

import javax.annotation.Nonnull;

import static eiteam.esteemedinnovation.tools.ToolsModule.upgradeResource;

public class ItemBackhoeUpgrade extends ItemSteamToolUpgrade {
    public ItemBackhoeUpgrade() {
        super(SteamToolSlot.SHOVEL_HEAD, upgradeResource("backhoe"), null, 1);
    }

    @Override
    public boolean onBlockBreakWithTool(BlockEvent.BreakEvent event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        EntityPlayer player = event.getPlayer();
        BlockPos pos = event.getPos();
        World world = event.getWorld();
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        boolean isFalling = block instanceof BlockFalling;
        int end = isFalling ? pos.getY() + Config.backhoeRange : pos.getY();
        for (int i = pos.getY() - Config.backhoeRange; i < end; i++) {
            if (i < 0) {
                continue;
            }
            BlockPos pos1 = new BlockPos(pos.getX(), i, pos.getZ());
            IBlockState state1 = world.getBlockState(pos1);
            Block block1 = state1.getBlock();
            if (!block1.isToolEffective(((SteamTool) toolStack.getItem()).toolClass(), state1) ||
              !block1.canHarvestBlock(world, pos1, player)) {
                continue;
            }
            if (Item.getItemFromBlock(block) == Item.getItemFromBlock(block1)) {
                world.setBlockToAir(pos1);
                block.harvestBlock(world, player, pos1, state1, world.getTileEntity(pos1), toolStack);
            } else {
                break;
            }
        }
        return true;
    }
}
