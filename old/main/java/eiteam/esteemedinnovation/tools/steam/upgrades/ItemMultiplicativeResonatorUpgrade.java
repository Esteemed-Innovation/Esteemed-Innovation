package eiteam.esteemedinnovation.tools.steam.upgrades;

import eiteam.esteemedinnovation.api.tool.SteamToolSlot;
import eiteam.esteemedinnovation.commons.util.EnchantmentUtility;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

import javax.annotation.Nonnull;

import static eiteam.esteemedinnovation.tools.ToolsModule.upgradeResource;

public class ItemMultiplicativeResonatorUpgrade extends ItemSteamToolUpgrade {
    public ItemMultiplicativeResonatorUpgrade() {
        super(SteamToolSlot.DRILL_HEAD, upgradeResource("fortune"), null, 1);
    }

    @Override
    public void onPlayerHarvestDropsWithTool(BlockEvent.HarvestDropsEvent event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        BlockPos pos = event.getPos();
        EntityPlayer player = event.getHarvester();
        World world = event.getWorld();
        IBlockState state = event.getState();
        Block block = state.getBlock();

        event.getDrops().clear();
        event.getDrops().addAll(block.getDrops(world, pos, state, EnchantmentUtility.getFortuneModifier(player) + 2));
    }
}
