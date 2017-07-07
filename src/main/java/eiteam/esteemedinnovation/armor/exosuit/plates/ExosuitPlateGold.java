package eiteam.esteemedinnovation.armor.exosuit.plates;

import eiteam.esteemedinnovation.api.Constants;
import eiteam.esteemedinnovation.api.exosuit.ExosuitPlate;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;

public class ExosuitPlateGold extends ExosuitPlate {
    public ExosuitPlateGold() {
        super("Gold", null, "gold", "gold", Constants.EI_MODID + ".plate.gold");
    }

    @Override
    public void onPlayerPickupXP(PlayerPickupXpEvent event, ItemStack armorStack, EntityEquipmentSlot slot) {
        event.getOrb().xpValue = MathHelper.ceil(event.getOrb().xpValue * 1.25F);
    }
}
