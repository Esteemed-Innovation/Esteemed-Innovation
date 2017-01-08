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
        BlockPos pos = event.getPos();
        World world = event.getWorld();
        for (int[] aCoordinateArray : WorldHelper.EXTRA_BLOCKS_VERTICAL) {
            int thisX = pos.getX() + aCoordinateArray[0];
            int thisY = pos.getY() + aCoordinateArray[1];
            int thisZ = pos.getZ() + aCoordinateArray[2];

            BlockPos thisPos = new BlockPos(thisX, thisY, thisZ);
            Block block1 = world.getBlockState(thisPos).getBlock();

            if (WorldHelper.isFarmable(block1)) {
                world.setBlockToAir(thisPos);
                world.setBlockState(thisPos, Blocks.FARMLAND.getDefaultState());
            }
        }

        return true;
    }
}
