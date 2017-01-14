package eiteam.esteemedinnovation.tools.steam.upgrades;

import eiteam.esteemedinnovation.api.tool.SteamToolSlot;
import eiteam.esteemedinnovation.commons.util.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import javax.annotation.Nonnull;

import static eiteam.esteemedinnovation.tools.ToolsModule.upgradeResource;

public class ItemCultivatorUpgrade extends ItemSteamToolUpgrade {
    public ItemCultivatorUpgrade() {
        super(SteamToolSlot.SHOVEL_HEAD, upgradeResource("cultivatorHead"), null, 1);
    }

    @Override
    public boolean onLeftClickBlockWithTool(PlayerInteractEvent.LeftClickBlock event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        BlockPos startPos = event.getPos();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(startPos);
        World world = event.getWorld();
        // TODO: See ItemLeafBlowerUpgrade#blowLeaves.
        for (int[] aCoordinateArray : WorldHelper.EXTRA_BLOCKS_VERTICAL) {
            int thisX = startPos.getX() + aCoordinateArray[0];
            int thisY = startPos.getY() + aCoordinateArray[1];
            int thisZ = startPos.getZ() + aCoordinateArray[2];
            pos.setPos(thisX, thisY, thisZ);
            Block block1 = world.getBlockState(pos).getBlock();

            if (WorldHelper.isFarmable(block1)) {
                world.setBlockToAir(pos);
                world.setBlockState(pos, Blocks.FARMLAND.getDefaultState());
            }
        }

        return true;
    }
}
