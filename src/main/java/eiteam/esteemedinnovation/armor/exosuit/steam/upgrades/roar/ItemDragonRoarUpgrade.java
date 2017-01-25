package eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.roar;

import eiteam.esteemedinnovation.api.ChargableUtility;
import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import eiteam.esteemedinnovation.armor.exosuit.steam.ItemSteamExosuitArmor;
import eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.ItemSteamExosuitUpgrade;
import eiteam.esteemedinnovation.commons.Config;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class ItemDragonRoarUpgrade extends ItemSteamExosuitUpgrade {
    public ItemDragonRoarUpgrade() {
        super(ExosuitSlot.HEAD_GOGGLES, "", null, 0);
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return ModelDragonsRoar.class;
    }

    @Override
    public void onPlayerAttacksOther(LivingAttackEvent event, EntityPlayer victim, ItemStack armorStack, EntityEquipmentSlot slot) {
        // Explosions must be ignored in order to prevent infinite recursive hearMeRoar calls.
        DamageSource source = event.getSource();
        Entity sourceEntity = source.getSourceOfDamage();
        if (sourceEntity instanceof EntityLivingBase && !source.isExplosion()) {
            EntityLivingBase entity = (EntityLivingBase) sourceEntity;
            World world = entity.worldObj;
            ItemStack chest = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            if (entity.getHeldItemMainhand() == null && entity.isSneaking() && chest != null &&
              chest.getItem() instanceof ItemSteamExosuitArmor && chest.hasTagCompound()) {
                int consumption = (chest.getTagCompound().getInteger("maxFill") / 2) + Config.dragonRoarConsumption;
                if (ChargableUtility.hasPower(entity, consumption)) {
                    if (world.isRemote) {
                        world.playSound(entity.posX, entity.posY, entity.posZ,
                          SoundEvents.ENTITY_ENDERDRAGON_GROWL, SoundCategory.PLAYERS, 5.0F,
                          0.8F + world.rand.nextFloat() * 0.3F, false);
                    } else {
                        world.createExplosion(entity, entity.posX + 0.5F, entity.posY,
                          entity.posZ + 0.5F, 10.0F, false);
                    }
                    ChargableUtility.drainSteam(chest, consumption, entity);
                }
            }
        }
    }
}
