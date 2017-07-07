package eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.phobiccoatings;

import eiteam.esteemedinnovation.api.ChargableUtility;
import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.ItemSteamExosuitUpgrade;
import eiteam.esteemedinnovation.commons.util.EntityHelper;
import eiteam.esteemedinnovation.commons.util.ReflectionHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.annotation.Nonnull;
import java.util.UUID;

import static net.minecraft.entity.SharedMonsterAttributes.MOVEMENT_SPEED;

/**
 * A base class for "phobic coatings"— exosuit upgrades which allow the player to walk on a certain type of fluid.
 */
public abstract class ItemPhobicCoatingUpgrade extends ItemSteamExosuitUpgrade {
    protected final AttributeModifier modifier;
    protected final UUID modifierID;
    protected final Material requiredMaterial;

    protected ItemPhobicCoatingUpgrade(String loc, AttributeModifier modifier, UUID modifierID, Material requiredMaterial) {
        super(ExosuitSlot.BOOTS_TOP, loc, null, 0);
        this.modifier = modifier;
        this.modifierID = modifierID;
        this.requiredMaterial = requiredMaterial;
    }

    protected abstract int getConsumption();

    /**
     * Gets whether the Entity can walk on fluids.
     * @param player The EntityPlayer testing against.
     * @param modifierCannotHave The AttributeModifier that the player cannot have.
     * @return True if the player can walk on top of fluids.
     */
    private boolean canWalkOnFluid(EntityPlayer player, AttributeModifier modifierCannotHave) {
        return modifierCannotHave == null && isInstalled(player) && ChargableUtility.hasPower(player, getConsumption());
    }

    @Override
    public void onPlayerTick(TickEvent.PlayerTickEvent event, @Nonnull ItemStack armorStack, EntityEquipmentSlot slot) {
        EntityPlayer player = event.player;
        IAttributeInstance attributes = player.getEntityAttribute(MOVEMENT_SPEED);
        AttributeModifier modifierFromAttributes = attributes.getModifier(modifierID);
        if (canWalkOnFluid(player, modifierFromAttributes)) {
            beforeFluidWalk(player);
            IBlockState underState = EntityHelper.getBlockUnderEntity(player);
            if (underState.getMaterial() == requiredMaterial) {
                handleFluidWalk(player, attributes);
            }
            afterFluidWalk(player);
        } else {
            attributes.removeModifier(modifier);
        }
    }

    /**
     * Called before the tick event listener determines that the player is on top of the required material. It is
     * confirmed that the player {@link #canWalkOnFluid(EntityPlayer, AttributeModifier)} at this point, however.
     * @param player The player wearing the coatings.
     */
    protected abstract void beforeFluidWalk(EntityPlayer player);

    /**
     * Called between {@link #beforeFluidWalk(EntityPlayer)} and {@link #afterFluidWalk(EntityPlayer)}.
     * <p>
     * Called if the player is currently above a block of the {@link #requiredMaterial}. It is used to actually perform
     * the operations needed to simulate walking on the fluid.
     * <p>
     * The default implementation sets the player's Y motion value (if they are jumping), applies the {@link #modifier},
     * and drains steam from the player's {@link eiteam.esteemedinnovation.api.SteamChargable} armor.
     * @param player The player wearing the coatings.
     * @param attributes The current movement speed attributes this player has.
     */
    protected void handleFluidWalk(EntityPlayer player, IAttributeInstance attributes) {
        boolean isJumping = false;
        try {
            isJumping = ReflectionHelper.getIsEntityJumping(player);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        player.motionY = isJumping ? 0.5D : 0;

        attributes.applyModifier(modifier);
        ItemStack chargable = ChargableUtility.findFirstChargableArmor(player);
        if (chargable != null) {
            ChargableUtility.drainSteam(chargable, getConsumption(), player);
        }
    }

    /**
     * Called after the tick event listener tries to call {@link #handleFluidWalk(EntityPlayer, IAttributeInstance)}.
     * It is confirmed that the player {@link #canWalkOnFluid(EntityPlayer, AttributeModifier)} at this point, however.
     * <p>
     * Provides no default implementation. It is a no-op, instead of abstract, because not all need it.
     * @param player The player wearing the coatings.
     */
    protected abstract void afterFluidWalk(EntityPlayer player);
}
