package eiteam.esteemedinnovation.armor.exosuit.upgrades;

import eiteam.esteemedinnovation.api.exosuit.ExosuitArmor;
import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.commons.util.ReflectionHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static eiteam.esteemedinnovation.armor.ArmorModule.resource;

public class ItemDoubleJumpUpgrade extends ItemExosuitUpgrade {
    public ItemDoubleJumpUpgrade() {
        super(ExosuitSlot.BOOTS_FEET, resource("doubleJump"), null, 1);
    }

    @Override
    public void onPlayerUpdate(LivingEvent.LivingUpdateEvent event, EntityPlayer player, ItemStack armorStack, EntityEquipmentSlot slot) {
        if (player.onGround && armorStack.hasTagCompound()) {
            armorStack.getTagCompound().setBoolean("usedJump", false);
        }
    }

    /**
     * A map of each player's isJumping value in the previous tick.
     */
    private final Map<UUID, Boolean> prevJumpers = new HashMap<>();

    /**
     * A map of each player's onGround value in the previous tick.
     */
    private final Map<UUID, Boolean> prevOnGrounds = new HashMap<>();

    @Override
    public void onPlayerTick(TickEvent.PlayerTickEvent event, ItemStack armorStack, EntityEquipmentSlot slot) {
        EntityPlayer player = event.player;
        boolean isJumping = false;
        try {
            isJumping = ReflectionHelper.getIsEntityJumping(player);
        } catch (IllegalAccessException e) {}
        boolean isServer = event.side == Side.SERVER;
        ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (chest == null) {
            return;
        }
        Item chestItem = chest.getItem();
        if (!(chestItem instanceof ExosuitArmor)) {
            return;
        }
        ExosuitArmor chestArmor = (ExosuitArmor) chestItem;

        UUID playerID = player.getUniqueID();

        if (isJumping) {
            if (chestArmor.hasPower(chest, 15)) {
                if (isServer) {
                    if (!armorStack.getTagCompound().hasKey("usedJump")) {
                        armorStack.getTagCompound().setBoolean("usedJump", false);
                    }
                    if (!armorStack.getTagCompound().hasKey("releasedSpace")) {
                        armorStack.getTagCompound().setBoolean("releasedSpace", false);
                    }
                }
                if (!player.onGround && armorStack.getTagCompound().getBoolean("releasedSpace") &&
                  !armorStack.getTagCompound().getBoolean("usedJump") &&
                  !player.capabilities.isFlying && !prevJumpers.get(playerID) && !prevOnGrounds.get(playerID)) {
                    if (isServer) {
                        armorStack.getTagCompound().setBoolean("usedJump", true);
                        chestArmor.drainSteam(chest, 10);
                    }
                    player.motionY = 0.65D;
                    player.fallDistance = 0.0F;
                }
                if (isServer) {
                    armorStack.getTagCompound().setBoolean("releasedSpace", false);
                }
            }
        } else if (!player.onGround && isServer) {
            armorStack.getTagCompound().setBoolean("releasedSpace", true);
        }

        prevJumpers.put(playerID, isJumping);
        prevOnGrounds.put(playerID, player.onGround);
    }
}
