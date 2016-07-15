package flaxbeard.steamcraft.item.armor.exosuit;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.exosuit.ExosuitSlot;
import flaxbeard.steamcraft.api.exosuit.ModelExosuitUpgrade;
import flaxbeard.steamcraft.client.render.model.exosuit.ModelWings;
import flaxbeard.steamcraft.data.capabilities.player.IPlayerData;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class ItemExosuitWings extends ItemExosuitUpgrade {
    public ItemExosuitWings() {
        super(ExosuitSlot.BODY_FRONT, "", "", 0);
    }

    public static int getTicks(Entity entity) {
        IPlayerData nbt = entity.getCapability(Steamcraft.PLAYER_DATA, null);
        int ticks = nbt.getTickCache();
        if (ticks < 0) {
            nbt.setTickCache(0);
            ticks = 0;
        }
        return ticks;
    }

    public static void updateTicks(Entity entity, int ticks) {
        IPlayerData nbt = entity.getCapability(Steamcraft.PLAYER_DATA, null);
        nbt.setTickCache(ticks);
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return ModelWings.class;
    }

    @Override
    public void updateModel(ModelBiped modelBiped, EntityLivingBase entityLivingBase, ItemStack itemStack, ModelExosuitUpgrade modelExosuitUpgrade) {
        int ticks = getTicks(entityLivingBase);
        float expansion = (float) (0.1F + (Math.sin(Math.toRadians(ticks * (20.0F / 15.0F) * 4.5F))) * 0.8F);

        modelExosuitUpgrade.nbtTagCompound.setFloat("expansion", expansion);

        if (!entityLivingBase.onGround && entityLivingBase.motionY < 0.0F && !entityLivingBase.isSneaking() && entityLivingBase.fallDistance > 1.4F && ticks < 15) {
            ticks++;
        }

        if ((entityLivingBase.onGround || entityLivingBase.motionY >= 0.0F || entityLivingBase.isSneaking()) && ticks > 0) {
            ticks--;
        }

        updateTicks(entityLivingBase, ticks);
    }
}
