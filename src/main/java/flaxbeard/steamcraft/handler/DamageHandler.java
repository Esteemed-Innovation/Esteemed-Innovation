package flaxbeard.steamcraft.handler;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.exosuit.UtilPlates;
import flaxbeard.steamcraft.integration.BloodMagicIntegration;
import flaxbeard.steamcraft.integration.CrossMod;
import flaxbeard.steamcraft.item.ItemExosuitArmor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;

public class DamageHandler {
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void handleFallDamage(LivingHurtEvent event) {
		if (CrossMod.BLOOD_MAGIC) {
			BloodMagicIntegration.handleAttack(event);
		}

		if (event.source == DamageSource.fall) {
			boolean hasPower = SteamcraftEventHandler.hasPower(event.entityLiving, (int) (event.ammount / Config.fallAssistDivisor));
			int armor = SteamcraftEventHandler.getExoArmor(event.entityLiving);
			EntityLivingBase entity = event.entityLiving;
			if (hasPower && entity.getEquipmentInSlot(3) != null && entity.getEquipmentInSlot(1) != null && entity.getEquipmentInSlot(1).getItem() instanceof ItemExosuitArmor) {
				ItemExosuitArmor boots = (ItemExosuitArmor) entity.getEquipmentInSlot(1).getItem();
				if (boots.hasUpgrade(entity.getEquipmentInSlot(1), SteamcraftItems.fallAssist)) {
					if (event.ammount <= 6.0F) {
						event.ammount = 0.0F;
					}
					event.ammount = event.ammount / 3.0F;
					SteamcraftEventHandler.drainSteam(entity.getEquipmentInSlot(3), (int) (event.ammount / Config.fallAssistDivisor));
					if (event.ammount == 0.0F) {
						event.setResult(Event.Result.DENY);
						event.setCanceled(true);
					}
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void handleMobDamage(LivingHurtEvent event) {
		boolean isPlayer = event.entityLiving instanceof EntityPlayer;
		boolean isMob = event.source.damageType.equals("mob");
		if (isPlayer && isMob && event.source.getEntity() != null) {
			Entity mob = event.source.getEntity();
			EntityPlayer player = (EntityPlayer) event.entityLiving;

			fireHandler(player, mob);

			// Server side
			if (!event.entityLiving.worldObj.isRemote) {
				vibrantHandler(player);
				enderiumHandler(player, mob);
			}


			boolean inflictedByLiving = event.source.getEntity() instanceof EntityLivingBase;
			if (inflictedByLiving) {
				inflictedByLivingHandler(player, (EntityLivingBase) mob);
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void handleArmorDamage(LivingHurtEvent event) {
		boolean isPlayer = event.entity instanceof EntityPlayer;
		if (isPlayer && hasExosuit((EntityPlayer) event.entity)) {
			EntityPlayer player = (EntityPlayer) event.entity;
			ItemStack stack = player.inventory.armorItemInSlot(1);
			ItemExosuitArmor item = (ItemExosuitArmor) stack.getItem();

			//if (item.hasUpgrade(stack, SteamcraftItems.doubleJump)) {
			float amount = event.ammount;
			;
			DamageSource src = event.source;
			if (!player.isEntityInvulnerable()) {
				if (amount <= 0) return;
				if (!src.isUnblockable() && player.isBlocking() && amount > 0.0F) {
					amount = (1.0F + amount) * 0.5F;
				}

				amount = ISpecialArmor.ArmorProperties.ApplyArmor(player, player.inventory.armorInventory, src, amount);
				if (amount <= 0) return;
				float f1 = amount;
				amount = Math.max(amount - player.getAbsorptionAmount(), 0.0F);
			}
			if (amount > 0.0F) {
				//				stack.stackTagCompound.setFloat("damageAmount", amount);
				//				stack.stackTagCompound.setInteger("aidTicks", 100);

			}
		}
	}

	private boolean hasExosuit(EntityPlayer player) {
		return (player.inventory.armorItemInSlot(1) != null) && (player.inventory.armorItemInSlot(1).getItem() instanceof ItemExosuitArmor);
	}

	private void vibrantHandler(EntityPlayer player) {
		if (player.getHealth() <= 5.0F) {
			int vibrantLevel = 0;
			for (int i = 0; i < player.inventory.armorInventory.length; i++) {
				ItemStack armor = player.inventory.armorInventory[i];
				if (armor != null && armor.getItem() instanceof ItemExosuitArmor) {
					ItemExosuitArmor armorItem = (ItemExosuitArmor) armor.getItem();
					if (armorItem.hasPlates(armor) && UtilPlates.getPlate(armor.stackTagCompound.getString("plate")).getIdentifier() == "Vibrant") {
						vibrantLevel += 1;
					}
				}
			}

			if ((vibrantLevel > 0) && (player.worldObj.rand.nextInt(5 - vibrantLevel) == 0)) {
				int startRotation = player.worldObj.rand.nextInt(360);
				boolean foundSpot = false;
				int range = 14;
				int counter = 0;
				int yO = 2;
				int tX = 0;
				int tY = 0;
				int tZ = 0;
				int safeRange = 7;
				int safe = 0;
				while (!foundSpot && range < 28 && safe < 10000) {
					safe++;
					tX = (int) (player.posX + range * Math.sin(Math.toRadians(startRotation)));
					tZ = (int) (player.posZ + range * Math.cos(Math.toRadians(startRotation)));
					tY = (int) player.posY + yO;
					List mobs = player.worldObj.getEntitiesWithinAABB(EntityMob.class, AxisAlignedBB.getBoundingBox(tX + 0.5F - safeRange, tY + 0.5F - safeRange, tZ + 0.5F - safeRange, tX + 0.5F + safeRange, tY + 0.5F + safeRange, tZ + 0.5F + safeRange));
					if (mobs.size() == 0 && player.worldObj.isSideSolid(tX, tY - 1, tZ, ForgeDirection.UP) && !player.worldObj.isAnyLiquid(AxisAlignedBB.getBoundingBox(tX, tY - 1, tZ, tX, tY + 1, tZ)) && player.worldObj.isAirBlock(tX, tZ, tY) && player.worldObj.isAirBlock(tX, tZ, tY + 1)) {
						foundSpot = true;
					} else {
						if (counter >= 36) {
							if (yO > -2) {
								yO--;
								counter = 0;
							} else {
								counter = 0;
								yO = 2;
								range += 2;
							}
						} else {
							startRotation += 10;
							counter++;
						}
					}
				}

				if (foundSpot) {
					((EntityPlayerMP) player).playerNetServerHandler.setPlayerLocation(tX, tY, tZ, player.worldObj.rand.nextInt(360), player.rotationPitch);
				}
			}
		}
	}

	private void enderiumHandler(EntityPlayer player, Entity mob) {
		int enderiumLevel = 0;
		for (int i = 0; i < player.inventory.armorInventory.length; i++) {
			ItemStack armor = player.inventory.armorInventory[i];
			if (armor != null && armor.getItem() instanceof ItemExosuitArmor) {
				ItemExosuitArmor armorItem = (ItemExosuitArmor) armor.getItem();
				if (armorItem.hasPlates(armor) && UtilPlates.getPlate(armor.stackTagCompound.getString("plate")).getIdentifier() == "Enderium") {
					enderiumLevel += 1;
				}
			}
		}
		if ((enderiumLevel > 0) && (player.worldObj.rand.nextFloat() < (enderiumLevel * 0.075F))) {
			int startRotation = player.worldObj.rand.nextInt(360);
			boolean foundSpot = false;
			int range = 8;
			int counter = 0;
			int yO = 2;
			int tX = 0;
			int tY = 0;
			int tZ = 0;
			int safe = 0;
			while (!foundSpot && range < 16 && safe < 10000) {
				safe++;
				tX = (int) (mob.posX + range * Math.sin(Math.toRadians(startRotation)));
				tZ = (int) (mob.posZ + range * Math.cos(Math.toRadians(startRotation)));
				tY = (int) mob.posY + yO;
				if (player.worldObj.isSideSolid(tX, tY - 1, tZ, ForgeDirection.UP) && !player.worldObj.isAnyLiquid(AxisAlignedBB.getBoundingBox(tX, tY - 1, tZ, tX, tY + 1, tZ)) && player.worldObj.isAirBlock(tX, tZ, tY) && player.worldObj.isAirBlock(tX, tZ, tY + 1)) {
					foundSpot = true;
				} else {
					if (counter >= 36) {
						if (yO > -2) {
							yO--;
							counter = 0;
						} else {
							counter = 0;
							yO = 2;
							range += 2;
						}
					} else {
						startRotation += 10;
						counter++;
					}
				}
			}

			if (foundSpot) {
				mob.setPositionAndRotation(tX, tY, tZ, mob.rotationYaw, mob.rotationPitch);
			}
		}
	}

	private void fireHandler(EntityPlayer player, Entity mob) {
		int fireLevel = 0;
		for (int i = 0; i < player.inventory.armorInventory.length; i++) {
			ItemStack armor = player.inventory.armorInventory[i];
			if (armor != null && armor.getItem() instanceof ItemExosuitArmor) {
				ItemExosuitArmor armorItem = (ItemExosuitArmor) armor.getItem();
				if (armorItem.hasPlates(armor) && UtilPlates.getPlate(armor.stackTagCompound.getString("plate")).getIdentifier() == "Fiery") {
					fireLevel += 3;
				}
			}
		}
		if ((fireLevel > 0) && (player.worldObj.rand.nextInt(25) < fireLevel)) {
			mob.setFire(fireLevel / 2);
		}
	}

	private void inflictedByLivingHandler(EntityPlayer player, EntityLivingBase mob) {
		int chillLevel = 0;
		for (int i = 0; i < player.inventory.armorInventory.length; i++) {
			ItemStack armor = player.inventory.armorInventory[i];
			if (armor != null && armor.getItem() instanceof ItemExosuitArmor) {
				ItemExosuitArmor armorItem = (ItemExosuitArmor) armor.getItem();
				if (armorItem.hasPlates(armor) && UtilPlates.getPlate(armor.stackTagCompound.getString("plate")).getIdentifier().equals("Yeti")) {
					chillLevel += 1;
				}
			}
		}
		if (chillLevel > 0) {
			mob.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, chillLevel * 3 + 5, MathHelper.ceiling_float_int((float) chillLevel / 2F)));
		}
	}
}
