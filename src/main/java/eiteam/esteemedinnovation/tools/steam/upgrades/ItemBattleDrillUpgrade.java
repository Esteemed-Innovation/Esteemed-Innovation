package eiteam.esteemedinnovation.tools.steam.upgrades;

import eiteam.esteemedinnovation.api.tool.SteamToolSlot;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.util.EntityHelper;
import eiteam.esteemedinnovation.tools.steam.ItemSteamDrill;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;

import static eiteam.esteemedinnovation.tools.ToolsModule.upgradeResource;

public class ItemBattleDrillUpgrade extends ItemSteamToolUpgrade {
    public ItemBattleDrillUpgrade() {
        super(SteamToolSlot.DRILL_HEAD, upgradeResource("combat"), null, 1);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onUpdateBreakSpeedWithTool(PlayerEvent.BreakSpeed event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        event.setNewSpeed(event.getNewSpeed() / 1.7F);
    }

    @SubscribeEvent
    public void toggleDrillDash(LivingEvent.LivingJumpEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        ItemStack equipped = player.getHeldItemMainhand();
        if (equipped == null || !player.isSneaking()) {
            return;
        }
        Item equippedItem = equipped.getItem();
        if (!(equippedItem instanceof ItemSteamDrill)) {
            return;
        }
        ItemSteamDrill drill = (ItemSteamDrill) equippedItem;
        if (!drill.isWound(equipped) || !drill.hasUpgrade(equipped, this)) {
            return;
        }

        Vec3d vector = player.getLook(0.5F);

//        double total = Math.abs(vector.zCoord + vector.xCoord);
//        if (vector.yCoord < total) {
//            vector.yCoord = total;
//        }

        player.motionZ += vector.zCoord * 2.5;
        player.motionX += vector.xCoord * 2.5;

        EntityLivingBase target = EntityHelper.getEntityFromPlayer(player);
        if (target == null) {
            return;
        }

        target.attackEntityFrom(DamageSource.causePlayerDamage(player), 9.0F);
        drill.addSteam(equipped, -(Config.battleDrillConsumption * drill.steamPerDurability()), player);
    }
}
