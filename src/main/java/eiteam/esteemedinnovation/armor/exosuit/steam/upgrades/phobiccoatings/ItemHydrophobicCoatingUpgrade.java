package eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.phobiccoatings;

import eiteam.esteemedinnovation.commons.Config;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

import static eiteam.esteemedinnovation.armor.ArmorModule.resource;

public class ItemHydrophobicCoatingUpgrade extends ItemPhobicCoatingUpgrade {
    private static final UUID MODIFIER_ID = UUID.fromString("ddb705e1-f95d-40e7-8cda-1be73a4739a1");
    private static final AttributeModifier MODIFIER = new AttributeModifier(MODIFIER_ID, "EXOWATERBOOST", 0.4D, 2).setSaved(true);

    public ItemHydrophobicCoatingUpgrade() {
        super(resource("hydrophobiccoating"), MODIFIER, MODIFIER_ID, Material.WATER);
    }

    @Override
    protected int getConsumption() {
        return Config.hydrophobicConsumption;
    }

    @Override
    protected void beforeFluidWalk(EntityPlayer player) {}

    @Override
    protected void handleFluidWalk(EntityPlayer player, IAttributeInstance attributes) {
        // Water saves the player from fall damage.
        player.fallDistance = 0;
        super.handleFluidWalk(player, attributes);
    }

    @Override
    protected void afterFluidWalk(EntityPlayer player) {
        if (player.isInWater()) {
            player.jump();
            player.fallDistance = 0;
        }
    }
}
