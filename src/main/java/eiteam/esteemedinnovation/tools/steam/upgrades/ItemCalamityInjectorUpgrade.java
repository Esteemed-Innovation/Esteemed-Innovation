package eiteam.esteemedinnovation.tools.steam.upgrades;

import eiteam.esteemedinnovation.api.SteamChargable;
import eiteam.esteemedinnovation.api.tool.SteamToolSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
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
import java.util.Random;

import static eiteam.esteemedinnovation.tools.ToolsModule.upgradeResource;

public class ItemCalamityInjectorUpgrade extends ItemSteamToolUpgrade {
    private Map<Pair<EntityPlayer, BlockPos>, Integer> charges = new HashMap<>();
    private int chargeTicks;
    private static final int PEACEFUL_CHARGE = 12 * 20;
    private static final int EASY_CHARGE_CAP = 14 * 20;
    private static final int EASY_CHARGE_MIN = 8 * 20;
    private static final int NORMAL_CHARGE_CAP = 16 * 20;
    private static final int NORMAL_CHARGE_MIN = 6 * 20;
    private static final int HARD_CHARGE_CAP = 18 * 20;
    private static final int HARD_CHARGE_MIN = 4 * 20;

    public ItemCalamityInjectorUpgrade() {
        super(SteamToolSlot.DRILL_HEAD, upgradeResource("charge_placer"), null, 1);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean onBlockBreakWithTool(BlockEvent.BreakEvent event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        EntityPlayer player = event.getPlayer();
        BlockPos pos = event.getPos();
        SteamChargable drill = (SteamChargable) toolStack.getItem();
        World world = player.world;
        Random rand = world.rand;
        drill.addSteam(toolStack, -(2 * drill.steamPerDurability()), player);
        if (world.getDifficulty() == EnumDifficulty.HARD && rand.nextInt(100) < 15) {
            return true;
        }
        int max = 0;
        int min = 0;
        int constant = 0;
        boolean useConstant = false;
        switch (player.world.getDifficulty()) {
            case HARD: {
                max = HARD_CHARGE_CAP;
                min = HARD_CHARGE_MIN;
                break;
            }
            case NORMAL: {
                max = NORMAL_CHARGE_CAP;
                min = NORMAL_CHARGE_MIN;
                break;
            }
            case EASY: {
                max = EASY_CHARGE_CAP;
                min = EASY_CHARGE_MIN;
                break;
            }
            case PEACEFUL: {
                constant = PEACEFUL_CHARGE;
                useConstant = true;
                break;
            }
            default: {}
        }
        charges.put(Pair.of(player, pos), useConstant ? constant : rand.nextInt((max - min) + 1) + min);

        return true;
    }

    @Override
    public void onUpdateBreakSpeedWithTool(PlayerEvent.BreakSpeed event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        event.setNewSpeed(event.getNewSpeed() * 5);
    }

    @SubscribeEvent
    public void explodeCharges(TickEvent.WorldTickEvent event) {
        if (event.side.isClient()) {
            return;
        }
        chargeTicks++;

        Iterator<Map.Entry<Pair<EntityPlayer, BlockPos>, Integer>> charge = charges.entrySet().iterator();
        while (charge.hasNext()) {
            Map.Entry<Pair<EntityPlayer, BlockPos>, Integer> entry = charge.next();
            Pair<EntityPlayer, BlockPos> playerCoords = entry.getKey();
            BlockPos pos = playerCoords.getRight();
            EntityPlayer player = playerCoords.getLeft();
            int dim = player.dimension;
            WorldServer worldServer = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dim);
            int waitTicks = entry.getValue();
            if (chargeTicks >= waitTicks) {
                // Strength is half the size of a TNT explosion.
                worldServer.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 2.0F, true);
                charge.remove();
            }
        }

        if (chargeTicks >= HARD_CHARGE_CAP) {
            chargeTicks = 0;
        }
    }
}
