package flaxbeard.steamcraft.handler;

import java.util.HashMap;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.world.WorldEvent;

import org.apache.commons.lang3.tuple.MutablePair;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.FMLRelaunchLog;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.steamnet.SteamNetworkRegistry;
import flaxbeard.steamcraft.api.steamnet.data.SteamNetworkData;
import flaxbeard.steamcraft.integration.BaublesIntegration;
import flaxbeard.steamcraft.integration.BotaniaIntegration;
import flaxbeard.steamcraft.item.ItemExosuitArmor;
import flaxbeard.steamcraft.item.firearm.ItemFirearm;
import flaxbeard.steamcraft.item.tool.steam.ItemSteamAxe;
import flaxbeard.steamcraft.item.tool.steam.ItemSteamDrill;
import flaxbeard.steamcraft.item.tool.steam.ItemSteamShovel;
import flaxbeard.steamcraft.tile.TileEntitySteamHeater;

public class SteamcraftEventHandler {
	private static final UUID uuid = UUID.fromString("bbd786a9-611f-4c31-88ad-36dc9da3e15c");
	private static final AttributeModifier exoBoost = new AttributeModifier(uuid,"EXOMOD", 0.2D, 2).setSaved(true);
	private static final UUID uuid2 = UUID.fromString("33235dc2-bf3d-40e4-ae0e-78037c7535e6");
	private static final AttributeModifier exoBoostBad = new AttributeModifier(uuid2,"EXOMODBAD", -0.2D, 2).setSaved(true);
	private static final UUID uuid3 = UUID.fromString("33235dc2-bf3d-40e4-ae0e-78037c7535e7");
	private static final AttributeModifier exoSwimBoost = new AttributeModifier(uuid3,"EXOSWIMBOOST", 1.0D, 2).setSaved(true);

	
	@SubscribeEvent
	public void handleWorldLoad(WorldEvent.Load event) {
		if (!event.world.isRemote) {
			SteamNetworkData.get(event.world);
			SteamNetworkRegistry.initialize();
		}
	}
	
//	@SubscribeEvent
//	public void handleMobDrop(LivingDropsEvent event) {
//		if (event.entityLiving instanceof EntityCreeper) {
//			int gunpowder = 0;
//			for (EntityItem drop : event.drops) {
//				if (drop.getEntityItem().getItem() == Items.gunpowder) {
//					gunpowder+=drop.getEntityItem().stackSize;
//				}
//			}
//			if (gunpowder >= 2 && !event.entityLiving.worldObj.isRemote && event.entityLiving.worldObj.rand.nextBoolean()) {
//				int dropsLeft = 2;
//				ArrayList<EntityItem> dropsToRemove = new ArrayList<EntityItem>();
//				EntityItem baseItem = null;
//				for (EntityItem drop : event.drops) {
//					if (baseItem == null && drop.getEntityItem().getItem() == Items.gunpowder) {
//						baseItem = drop;
//					}
//					if (dropsLeft > 0 && drop.getEntityItem().getItem() == Items.gunpowder) {
//						if (drop.getEntityItem().stackSize <= dropsLeft) {
//							dropsLeft -= drop.getEntityItem().stackSize;
//							dropsToRemove.add(drop);
//						}
//						else
//						{
//							drop.getEntityItem().stackSize -= dropsLeft;
//							dropsLeft = 0;
//						}
//					}
//				}
//				for (EntityItem drop : dropsToRemove) {
//					event.drops.remove(drop);
//				}
//				baseItem.setEntityItemStack(new ItemStack(SteamcraftItems.steamcraftCrafting,1,5));
//                event.drops.add(baseItem);
//				System.out.println("REMOVED GUNPOWDER");
//			}
//		}
//	}

	
	@SubscribeEvent
	public void onDrawScreen(RenderGameOverlayEvent.Post event) {
		if(event.type == ElementType.ALL) {
			if (Loader.isModLoaded("Botania")) {
				Minecraft mc = Minecraft.getMinecraft();
				MovingObjectPosition pos = mc.objectMouseOver;
				if(pos != null && !(mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() == BotaniaIntegration.twigWand()) && mc.thePlayer.getCurrentArmor(3) != null && mc.thePlayer.getCurrentArmor(3).getItem() == SteamcraftItems.exoArmorHead) {
					if (((ItemExosuitArmor)mc.thePlayer.getCurrentArmor(3).getItem()).hasUpgrade(mc.thePlayer.getCurrentArmor(3), BotaniaIntegration.floralLaurel)) {
						BotaniaIntegration.displayThings(pos, event);
					}
				}
			}
		}
	}
	
	public static int use = -1;
	
	@SubscribeEvent
	public void useItem(PlayerUseItemEvent.Tick event) {
		if (event.item.getItem() instanceof ItemFirearm) {
        	use = event.duration;
		}
	}
	
	@SubscribeEvent
	public void useItemEnd(PlayerUseItemEvent.Finish event) {
		if (event.item.getItem() instanceof ItemFirearm) {
			use = -1;
		}
	}
	
	@SubscribeEvent
	public void useItemEnd(PlayerUseItemEvent.Stop event) {
		if (event.item.getItem() instanceof ItemFirearm) {
			use = -1;
		}
	}
	
	
	
	@SubscribeEvent
	public void handleFirePunch(LivingAttackEvent event) {
		if (event.source.getSourceOfDamage() instanceof EntityLivingBase) {

			EntityLivingBase entity = (EntityLivingBase) event.source.getSourceOfDamage();
			boolean hasPower = hasPower(entity,5);
			if (hasPower && entity.getEquipmentInSlot(3) != null && entity.getHeldItem() == null) {
				ItemExosuitArmor chest = (ItemExosuitArmor) entity.getEquipmentInSlot(3).getItem();
				if (chest.hasUpgrade(entity.getEquipmentInSlot(3), SteamcraftItems.powerFist)) {
			        entity.worldObj.playSoundEffect(entity.posX, entity.posY, entity.posZ, "random.explode", 4.0F, (1.0F + (entity.worldObj.rand.nextFloat() - entity.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
					event.entityLiving.motionX += 3.0F*entity.getLookVec().normalize().xCoord;
					event.entityLiving.motionY += (entity.getLookVec().normalize().yCoord > 0.0F ? 2.0F*entity.getLookVec().normalize().yCoord : 0.0F) + 1.5F;
					event.entityLiving.motionZ += 3.0F*entity.getLookVec().normalize().zCoord;
				
					entity.motionX += -0.5F*entity.getLookVec().normalize().xCoord;
					entity.motionZ += -0.5F*entity.getLookVec().normalize().zCoord;
					entity.getEquipmentInSlot(3).damageItem(5,entity);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void handleFallDamage(LivingHurtEvent event) {
		if (event.source == DamageSource.fall) {
			boolean hasPower = hasPower(event.entityLiving,(int)event.ammount/2);
			int armor = getExoArmor(event.entityLiving);
			EntityLivingBase entity = event.entityLiving;
			if (hasPower && entity.getEquipmentInSlot(3) != null && entity.getEquipmentInSlot(1) != null && entity.getEquipmentInSlot(1).getItem() instanceof ItemExosuitArmor) {
				ItemExosuitArmor boots = (ItemExosuitArmor) entity.getEquipmentInSlot(1).getItem();
				entity.getEquipmentInSlot(3).damageItem((int)event.ammount/2, event.entityLiving);
				if (boots.hasUpgrade(entity.getEquipmentInSlot(1), SteamcraftItems.fallAssist)) {
			        if (event.ammount <= 6.0F) {
			        	event.ammount = 0.0F;
			        }
					event.ammount = event.ammount/3.0F;
				}
			}
		}
	}
	
	@SubscribeEvent
	public void rightClick(PlayerInteractEvent event) {
		if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
			if (event.entityPlayer.getHeldItem() != null) {
				if ((event.entityPlayer.getHeldItem().getItem() instanceof ItemSteamDrill || event.entityPlayer.getHeldItem().getItem() instanceof ItemSteamAxe || event.entityPlayer.getHeldItem().getItem() instanceof ItemSteamShovel) && (event.entityPlayer.worldObj.getBlock(event.x, event.y, event.z) == null || event.entityPlayer.worldObj.getBlock(event.x, event.y, event.z) != SteamcraftBlocks.charger)) {
					event.setCanceled(true);
				}
			}
		}
		
	}
	@SubscribeEvent
	public void playerJumps(LivingEvent.LivingJumpEvent event)
	{
		boolean hasPower = hasPower(event.entityLiving,3);
		
		if (((event.entity instanceof EntityPlayer)) && (((EntityPlayer)event.entity).inventory.armorItemInSlot(0) != null) && (((EntityPlayer)event.entity).inventory.armorItemInSlot(0).getItem() instanceof ItemExosuitArmor)) {
			ItemStack stack = ((EntityPlayer)event.entity).inventory.armorItemInSlot(0);
			ItemExosuitArmor item = (ItemExosuitArmor) stack.getItem();
			if (item.getStackInSlot(stack,3) != null && hasPower) {
				if (event.entityLiving.getEquipmentInSlot(3).getItemDamage() < event.entityLiving.getEquipmentInSlot(3).getMaxDamage()-2) {
					if (item.getStackInSlot(stack, 3).getItem() == SteamcraftItems.pistol) {
						if (((EntityPlayer)event.entity).isSneaking()) {
							Vec3 vector = event.entityLiving.getLook(0.5F);
							double total = Math.abs(vector.zCoord + vector.xCoord);
							EntityPlayer player = (EntityPlayer)event.entity;
							double jump = 0;
							if(jump >= 1) {
								jump = (jump + 2D)/4D;
							}
					  
							if (vector.yCoord < total)
								vector.yCoord = total;
				      
							event.entityLiving.motionY += ((jump+1)*vector.yCoord)/1.5F;
							event.entityLiving.motionZ += (jump+1)*vector.zCoord*4;
							event.entityLiving.motionX += (jump+1)*vector.xCoord*4;
							event.entityLiving.getEquipmentInSlot(3).damageItem(3, event.entityLiving);
						}
						else
						{
							event.entityLiving.getEquipmentInSlot(3).damageItem(1, event.entityLiving);
							event.entityLiving.motionY += 0.2750000059604645D;
						}
					}
				}		
			}
			if (item.hasUpgrade(stack, SteamcraftItems.doubleJump)) {
				stack.stackTagCompound.setBoolean("releasedSpace", false);
			}
			
		}	
	}
	
	public boolean hasItemInHotbar(EntityPlayer player, Item item) {
		for (int i = 0; i<10; i++) {
			if (player.inventory.getStackInSlot(i) != null && player.inventory.getStackInSlot(i).getItem() == item) {
				return true;
			}
		}
		return false;
	}

	@SubscribeEvent
	public void handleSteamcraftArmorMining(PlayerEvent.BreakSpeed event) {
		

		boolean hasPower = hasPower(event.entityLiving,1);
		int armor = getExoArmor(event.entityLiving);
		EntityLivingBase entity = event.entityLiving;
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if (Loader.isModLoaded("Baubles")) {
				if (player.getHeldItem() != null && BaublesIntegration.checkForSurvivalist(player)) {
					if (player.getHeldItem().getItem() instanceof ItemTool) {
						if (player.getHeldItem().getItemDamage() >= player.getHeldItem().getMaxDamage() - 1) {

							event.newSpeed = 0.0F;
						}
					}
				
				}
			}
			else if (player.getHeldItem() != null && hasItemInHotbar(player, SteamcraftItems.survivalist)) {
				if (player.getHeldItem().getItem() instanceof ItemTool) {
					if (player.getHeldItem().getItemDamage() >= player.getHeldItem().getMaxDamage() - 1) {
						event.newSpeed = 0.0F;
					}
				}
			}
			if (player.getHeldItem() != null) {
				if (player.getHeldItem().getItem() instanceof ItemSteamDrill) {
					ItemSteamDrill.checkNBT(player);
					MutablePair info = ItemSteamDrill.stuff.get(player.getEntityId());
			    	int ticks = (Integer) info.left;
			    	int speed = (Integer) info.right;
			    	//System.out.println(Math.max(1.0F, 12.0F*(speed/100.0F)));
			    	if (speed > 0 && Items.iron_pickaxe.func_150893_a(player.getHeldItem(), event.block) != 1.0F) {
			    		event.newSpeed *= 1.0F+11.0F*(speed/1000.0F);
			    	}
				}
				if (player.getHeldItem().getItem() instanceof ItemSteamAxe) {
					ItemSteamAxe.checkNBT(player);
					MutablePair info = ItemSteamAxe.stuff.get(player.getEntityId());
			    	int ticks = (Integer) info.left;
			    	int speed = (Integer) info.right;
			    	//System.out.println(Math.max(1.0F, 12.0F*(speed/100.0F)));
			    	if (speed > 0 && Items.diamond_axe.func_150893_a(player.getHeldItem(), event.block) != 1.0F) {
			    		event.newSpeed *= 1.0F+11.0F*(speed/1000.0F);
			    	}
				}
				if (player.getHeldItem().getItem() instanceof ItemSteamShovel) {
					ItemSteamShovel.checkNBT(player);
					ItemSteamShovel shovel = (ItemSteamShovel) player.getHeldItem().getItem(); 
					MutablePair info = ItemSteamShovel.stuff.get(player.getEntityId());
			    	int ticks = (Integer) info.left;
			    	int speed = (Integer) info.right;
			    	//System.out.println(Math.max(1.0F, 12.0F*(speed/100.0F)));
			    	
			    	if (speed > 0 && Items.diamond_shovel.func_150893_a(player.getHeldItem(), event.block) != 1.0F) {
			    		event.newSpeed *= 1.0F+19.0F*(speed/3000.0F);
			    	}
				}
			}
		}
		
		if (entity.getEquipmentInSlot(3) != null) {
			ItemStack stack = entity.getEquipmentInSlot(3);

			if (stack.getItem() instanceof ItemExosuitArmor) {

				if (stack.getItemDamage() < stack.getMaxDamage()-1) {
					hasPower = true;
				}
			}
		}
		if (hasPower) {
			if (armor == 4) {
				event.newSpeed = event.newSpeed * 1.2F;
			}
				
		}
	}
	public static HashMap<Integer,MutablePair<Double,Double>> lastMotions = new HashMap<Integer,MutablePair<Double,Double>>();

	
	@SubscribeEvent
	public void handleFlippers(LivingEvent.LivingUpdateEvent event) {	
		
		int armor = getExoArmor(event.entityLiving);
		EntityLivingBase entity = (EntityLivingBase) event.entityLiving;
		boolean hasPower = hasPower(entity,1);

		
		if (entity.getEquipmentInSlot(3) != null && entity.getEquipmentInSlot(3).getItem() instanceof ItemExosuitArmor) {
			ItemExosuitArmor chest = (ItemExosuitArmor) entity.getEquipmentInSlot(3).getItem();
			if (chest.hasUpgrade(entity.getEquipmentInSlot(3), SteamcraftItems.wings)) {
				if (entity.fallDistance > 1.5F && !entity.isSneaking()) {
					entity.fallDistance = 1.5F;
					entity.motionY = Math.max(entity.motionY, -0.1F);
					entity.moveEntity(entity.motionX, 0, entity.motionZ);
				}
			}
		}
		
		if (hasPower && entity.getEquipmentInSlot(2) != null && entity.getEquipmentInSlot(2).getItem() instanceof ItemExosuitArmor) {
			ItemExosuitArmor chest = (ItemExosuitArmor) entity.getEquipmentInSlot(2).getItem();
			if (chest.hasUpgrade(entity.getEquipmentInSlot(2), SteamcraftItems.thrusters)) {
				if (!lastMotions.containsKey(entity.getEntityId())) {
					lastMotions.put(entity.getEntityId(), MutablePair.of(entity.posX,entity.posZ));
				}
				double lastX = lastMotions.get(entity.getEntityId()).left;
				double lastZ = lastMotions.get(entity.getEntityId()).right;
				if ((lastX != entity.posX || lastZ != entity.posZ) && !entity.onGround && !entity.isInWater() && (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).capabilities.isFlying)) {
					entity.moveEntity(entity.motionX, 0, entity.motionZ);
					if (!event.entityLiving.getEquipmentInSlot(3).stackTagCompound.hasKey("ticksUntilConsume")) {
						event.entityLiving.getEquipmentInSlot(3).stackTagCompound.setInteger("ticksUntilConsume", 2);
					}
					if (event.entityLiving.getEquipmentInSlot(3).stackTagCompound.getInteger("ticksUntilConsume") <= 0) {
						event.entityLiving.getEquipmentInSlot(3).damageItem(1, event.entityLiving);
					}
				}
				lastMotions.put(entity.getEntityId(), MutablePair.of(entity.posX,entity.posZ));
			}
		}
	}
	
	@SubscribeEvent
	public void handleSteamcraftArmor(LivingEvent.LivingUpdateEvent event) {
		boolean hasPower = hasPower(event.entityLiving, 1);
		int armor = getExoArmor(event.entityLiving);
		EntityLivingBase entity = event.entityLiving;
		ItemStack armor2 = entity.getEquipmentInSlot(1);
		
		if (hasPower) {
			if (entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(uuid2) != null) {
				entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(exoBoostBad);
			}
			if (entity.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getModifier(uuid2) != null) {
				entity.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).removeModifier(exoBoostBad);
			}
			ItemStack stack = entity.getEquipmentInSlot(3);
			if (!stack.hasTagCompound()) {
				stack.setTagCompound(new NBTTagCompound());
			}
			if (!stack.stackTagCompound.hasKey("ticksUntilConsume")) {
				stack.stackTagCompound.setInteger("ticksUntilConsume", 2);
			}
			int ticksLeft = stack.stackTagCompound.getInteger("ticksUntilConsume");
			//System.out.println(ticksLeft);

			if (ticksLeft <= 0) {
				if (Config.passiveDrain) {
					stack.damageItem(1, entity);
				}
				ticksLeft = 2;
			}
			ticksLeft--;
			stack.stackTagCompound.setInteger("ticksUntilConsume", ticksLeft);
			if (armor == 4) {
				if (entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(uuid) == null) {
					entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(exoBoost);
				}
				if (entity.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getModifier(uuid) == null) {
					entity.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).applyModifier(exoBoost);
				}
			}
			else
			{
				if (entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(uuid) != null) {
					entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(exoBoost);
				}
				if (entity.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getModifier(uuid) != null) {
					entity.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).removeModifier(exoBoost);
				}
			}
		}
		else
		{
			if (entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(uuid) != null) {
				entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(exoBoost);
			}
			if (entity.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getModifier(uuid) != null) {
				entity.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).removeModifier(exoBoost);
			}
		}
		
		if (armor > 0 && !hasPower) {
			if (entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(uuid2) == null) {
				entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(exoBoostBad);
			}
			if (entity.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getModifier(uuid2) == null) {
				entity.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).applyModifier(exoBoostBad);
			}
		}
		else
		{
			if (entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(uuid2) != null) {
				entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(exoBoostBad);
			}
			if (entity.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getModifier(uuid2) != null) {
				entity.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).removeModifier(exoBoostBad);
			}
		}
	}
	
//	public boolean isMoving(EntityLivingBase entity) {
//		return (entity.isp)
//	}
	
	public static boolean hasPower(EntityLivingBase entityLiving, int i) {
		if (entityLiving.getEquipmentInSlot(3) != null) {
			ItemStack stack = entityLiving.getEquipmentInSlot(3);
			if (stack.getItem() instanceof ItemExosuitArmor) {
				return ((ItemExosuitArmor)stack.getItem()).hasPower(stack, i);
			}
		}
		return false;
	}
	
	public int getExoArmor(EntityLivingBase entityLiving) {
		int num = 0;
		for (int i = 1; i<5; i++) {
			if (entityLiving.getEquipmentInSlot(i) != null) {
				ItemStack stack = entityLiving.getEquipmentInSlot(i);
				if (stack.getItem() instanceof ItemExosuitArmor) {
					num++;
				}
			}
		}
		return num;
	}
	
	@SubscribeEvent
	public void clickLeft(PlayerInteractEvent event) {
		if (event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z) != null && !event.entityPlayer.worldObj.isRemote) { 
			if (event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z) instanceof TileEntitySteamHeater) {
				//System.out.println(((TileEntitySteamHeater)event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z)).master);
			}
			if (event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z) instanceof ISteamTransporter) {
				ISteamTransporter trans = (ISteamTransporter)event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z);
				if (event.entityPlayer.worldObj.isRemote){
					System.out.println("I AM THE CLIENT");
				}
				//FMLRelaunchLog.info(trans.getSteam() + " " + trans.getPressure() + " " + trans.getNetworkName() + "; " + trans.getNetwork(), "Snap");
				System.out.println(trans.getSteam() + " " + trans.getPressure() + " " + trans.getNetworkName() + "; " + trans.getNetwork());
			}
		
		}
	}
	
	@SubscribeEvent
	public void handleEnhancement(AnvilUpdateEvent event) {
//		if (event.right.getItem() instanceof IEnhancement) {
//			IEnhancement enhancement = (IEnhancement) event.right.getItem();
//			if (enhancement.canApplyTo(event.left) && UtilEnhancements.canEnhance(event.left)) {
//				event.cost = enhancement.cost(event.left);
//				event.output = UtilEnhancements.getEnhancedItem(event.left, event.right);
//			}
//		}
	}
}
