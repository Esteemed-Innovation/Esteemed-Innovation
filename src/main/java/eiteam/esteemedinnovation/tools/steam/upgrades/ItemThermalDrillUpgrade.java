package eiteam.esteemedinnovation.tools.steam.upgrades;

import eiteam.esteemedinnovation.api.tool.SteamToolSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static eiteam.esteemedinnovation.tools.ToolsModule.upgradeResource;

public class ItemThermalDrillUpgrade extends ItemSteamToolUpgrade {
    /**
     * The Hash of quick lava blocks to delete.
     * Key: Pair of dimension ID and BlockPos.
     * Value: Integer, number of ticks to wait. Cannot be more than 30 or bad things will happen.
     */
    private Map<Pair<Integer, BlockPos>, Integer> quickLavaBlocks = new HashMap<>();
    private int lavaTicks;

    public ItemThermalDrillUpgrade() {
        super(SteamToolSlot.DRILL_HEAD, upgradeResource("thermal"), null, 1);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onPlayerHarvestDropsWithTool(BlockEvent.HarvestDropsEvent event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        EntityPlayer player = event.getHarvester();
        BlockPos pos = event.getPos();
        World world = event.getWorld();
        world.setBlockState(pos, Blocks.LAVA.getDefaultState());
        quickLavaBlocks.put(Pair.of(player.dimension, pos), world.rand.nextInt(30) + 1);
        event.getDrops().clear();
    }

    @Override
    public void onUpdateBreakSpeedWithTool(PlayerEvent.BreakSpeed event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        event.setNewSpeed(event.getNewSpeed() * 5);
    }

    @SubscribeEvent
    public void deleteLava(TickEvent.WorldTickEvent event) {
        if (event.side.isClient()) {
            return;
        }
        lavaTicks++;

        Iterator<Map.Entry<Pair<Integer, BlockPos>, Integer>> lava = quickLavaBlocks.entrySet().iterator();
        while (lava.hasNext()) {
            Map.Entry<Pair<Integer, BlockPos>, Integer> entry = lava.next();
            Pair<Integer, BlockPos> dimCoords = entry.getKey();
            BlockPos pos = dimCoords.getRight();
            int dim = dimCoords.getLeft();
            WorldServer worldServer = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(dim);
            int waitTicks = entry.getValue();
            if (lavaTicks == waitTicks) {
                worldServer.setBlockToAir(pos);
                lava.remove();
            }
        }

        if (lavaTicks >= 30) {
            lavaTicks = 0;
        }
    }
}
