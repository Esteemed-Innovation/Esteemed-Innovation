package flaxbeard.steamcraft.handler;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import org.apache.commons.lang3.tuple.MutablePair;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.enhancement.IEnhancement;
import flaxbeard.steamcraft.api.enhancement.UtilEnhancements;
import flaxbeard.steamcraft.integration.BotaniaIntegration;
import flaxbeard.steamcraft.item.ItemExosuitArmor;
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

	
//	@SubscribeEvent
//	public void handleWorldLoad(WorldEvent.Load event) {
//		AetherBlockData.get(event.world);
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
	
	@SubscribeEvent
	public void handleFirePunch(LivingAttackEvent event) {
		if (event.source.getSourceOfDamage() instanceof EntityLivingBase) {
			boolean hasPower = hasPower(event.entityLiving);
			int armor = getExoArmor(event.entityLiving);
			EntityLivingBase entity = (EntityLivingBase) event.source.getSourceOfDamage();
			if (entity.getEquipmentInSlot(3) != null) {
				ItemStack stack = entity.getEquipmentInSlot(3);
				if (stack.getItem() instanceof ItemExosuitArmor) {
					if (stack.getItemDamage() < stack.getMaxDamage()-1) {
						hasPower = true;
					}
				}
			}
			if (hasPower && entity.getEquipmentInSlot(3) != null && entity.getHeldItem() == null) {
				ItemExosuitArmor chest = (ItemExosuitArmor) entity.getEquipmentInSlot(3).getItem();
				if (chest.hasUpgrade(entity.getEquipmentInSlot(3), SteamcraftItems.powerFist)) {
			        entity.worldObj.playSoundEffect(entity.posX, entity.posY, entity.posZ, "random.explode", 4.0F, (1.0F + (entity.worldObj.rand.nextFloat() - entity.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
					event.entityLiving.motionX += 3.0F*entity.getLookVec().normalize().xCoord;
					event.entityLiving.motionY += (entity.getLookVec().normalize().yCoord > 0.0F ? 2.0F*entity.getLookVec().normalize().yCoord : 0.0F) + 1.5F;
					event.entityLiving.motionZ += 3.0F*entity.getLookVec().normalize().zCoord;
				
					entity.motionX += -1.0F*entity.getLookVec().normalize().xCoord;
					entity.motionZ += -1.0F*entity.getLookVec().normalize().zCoord;
				}
			}
		}
	}
	
	@SubscribeEvent
	public void handleFallDamage(LivingHurtEvent event) {
		if (event.source == DamageSource.fall) {
			boolean hasPower = hasPower(event.entityLiving);
			int armor = getExoArmor(event.entityLiving);
			EntityLivingBase entity = event.entityLiving;
			if (entity.getEquipmentInSlot(3) != null) {
				ItemStack stack = entity.getEquipmentInSlot(3);
				if (stack.getItem() instanceof ItemExosuitArmor) {
					if (stack.getItemDamage() < stack.getMaxDamage()-1) {
						hasPower = true;
					}
				}
			}
			if (hasPower && entity.getEquipmentInSlot(3) != null && entity.getEquipmentInSlot(1) != null && entity.getEquipmentInSlot(1).getItem() instanceof ItemExosuitArmor) {

				ItemExosuitArmor boots = (ItemExosuitArmor) entity.getEquipmentInSlot(1).getItem();
				if (boots.hasUpgrade(entity.getEquipmentInSlot(1), SteamcraftItems.fallAssist)) {
			        if (event.ammount <= 1.0F) {
			        	event.ammount = 0.0F;
			        }
					event.ammount = event.ammount/1.5F;
					System.out.println("T");
				}
			}
		}
	}
	
//	public void checkForFall(World world, int x, int y, int z) {
//		ArrayList<ChunkCoordinates> cc = AetherBlockData.get(world).cc;
//		ChunkCoordinates c = new ChunkCoordinates(x, y, z);
//
//		if (cc.contains(c)) {
//			Block block = world.getBlock(x, y, z);
//			if (block != Blocks.air) {
//				boolean canFall = true;
//				for (int y2 = y-1; y>=0; y--) {
//					if (cc.contains(new ChunkCoordinates(x, y2, z))) {
//						canFall = false;
//					}
//				}
//				if (canFall) {
//					if (!world.isRemote) {
//						EntityFallingBlock entityfallingblock = new EntityFallingBlock(world, (double)((float)c.posX + 0.5F), (double)((float)c.posY + 0.5F), (double)((float)c.posZ + 0.5F), block, world.getBlockMetadata(c.posX, c.posY, c.posZ));
//						world.spawnEntityInWorld(entityfallingblock);
//					}
//					else
//					{
//						world.setBlockToAir(c.posX, c.posY, c.posZ);
//					}
//					cc.remove(c);
//				}
//				for (int i = 0; i<6; i++) {
//
//	  				ForgeDirection dir = ForgeDirection.getOrientation(i);
//	  				if (cc.contains(new ChunkCoordinates(c.posX + dir.offsetX, c.posY + dir.offsetY, c.posZ + dir.offsetZ))) {
//	  					checkForFall(world, c.posX + dir.offsetX, c.posY + dir.offsetY, c.posZ + dir.offsetZ);
//	  				}
//	  			}
//			}
//		}
//	}
	
	@SubscribeEvent
	public void rightClick(PlayerInteractEvent event) {
		if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
			//checkForFall(event.entityPlayer.worldObj, event.x, event.y, event.z);
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
		boolean hasPower = hasPower(event.entityLiving);

		if (((event.entity instanceof EntityPlayer)) && (((EntityPlayer)event.entity).inventory.armorItemInSlot(0) != null) && (((EntityPlayer)event.entity).inventory.armorItemInSlot(0).getItem() instanceof ItemExosuitArmor)) {
			ItemStack stack = ((EntityPlayer)event.entity).inventory.armorItemInSlot(0);
			ItemExosuitArmor item = (ItemExosuitArmor) stack.getItem();
			if (item.getStackInSlot(stack,3) != null && hasPower) {
				if (event.entityLiving.getEquipmentInSlot(3).getItemDamage() < event.entityLiving.getEquipmentInSlot(3).getMaxDamage()-2) {
					if (item.getStackInSlot(stack, 3).getItem() == SteamcraftItems.pistol) {
						event.entityLiving.getEquipmentInSlot(3).damageItem(1, event.entityLiving);
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
	
				      
						}
						else
						{
							event.entityLiving.motionY += 0.2750000059604645D;
						}
					}
				}		
			}
		}	
	}

	@SubscribeEvent
	public void handleSteamcraftArmorMining(PlayerEvent.BreakSpeed event) {

		boolean hasPower = hasPower(event.entityLiving);
		int armor = getExoArmor(event.entityLiving);
		EntityLivingBase entity = event.entityLiving;
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if (player.getHeldItem() != null) {
				if (player.getHeldItem().getItem() instanceof ItemSteamDrill) {
					ItemSteamDrill.checkNBT(player);
					MutablePair info = ItemSteamDrill.stuff.get(player.getEntityId());
			    	int ticks = (Integer) info.left;
			    	int speed = (Integer) info.right;
			    	//System.out.println(Math.max(1.0F, 12.0F*(speed/100.0F)));
			    	if (speed > 0 && event.block.isToolEffective("pickaxe", event.metadata)) {
			    		event.newSpeed *= 1.0F+11.0F*(speed/100.0F);
			    	}
				}
				if (player.getHeldItem().getItem() instanceof ItemSteamAxe) {
					ItemSteamAxe.checkNBT(player);
					MutablePair info = ItemSteamAxe.stuff.get(player.getEntityId());
			    	int ticks = (Integer) info.left;
			    	int speed = (Integer) info.right;
			    	//System.out.println(Math.max(1.0F, 12.0F*(speed/100.0F)));
			    	if (speed > 0 && event.block.isToolEffective("axe", event.metadata)) {
			    		System.out.println(event.newSpeed);

			    		event.newSpeed *= 1.0F+11.0F*(speed/100.0F);
			    		System.out.println(event.newSpeed);
			    	}
				}
				if (player.getHeldItem().getItem() instanceof ItemSteamShovel) {
					ItemSteamShovel.checkNBT(player);
					ItemSteamShovel shovel = (ItemSteamShovel) player.getHeldItem().getItem(); 
					MutablePair info = ItemSteamShovel.stuff.get(player.getEntityId());
			    	int ticks = (Integer) info.left;
			    	int speed = (Integer) info.right;
			    	//System.out.println(Math.max(1.0F, 12.0F*(speed/100.0F)));
			    	if (speed > 0 && ForgeHooks.isToolEffective(player.getHeldItem(), event.block, event.metadata)) {
			    		System.out.println(event.newSpeed);

			    		event.newSpeed *= 1.0F+19.0F*(speed/100.0F);
			    		System.out.println(event.newSpeed);
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
	
	@SubscribeEvent
	public void handleFlippers(LivingEvent.LivingUpdateEvent event) {
		boolean hasPower = hasPower(event.entityLiving);
		EntityLivingBase entity = event.entityLiving;
		boolean hasFlippers = false;

		if (entity.getEquipmentInSlot(1) != null && entity.getEquipmentInSlot(1).getItem() instanceof ItemExosuitArmor) {
			ItemStack stack = entity.getEquipmentInSlot(1);
			ItemExosuitArmor item = (ItemExosuitArmor) stack.getItem();
			if (item.getStackInSlot(stack,3) != null && entity.isInWater()) {
				if (item.getStackInSlot(stack, 3).getItem() == SteamcraftItems.upgradeFlippers) {
					entity.moveEntity(entity.motionX*3, 0, entity.motionZ*3);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void handleSteamcraftArmor(LivingEvent.LivingUpdateEvent event) {
		boolean hasPower = hasPower(event.entityLiving);
		int armor = getExoArmor(event.entityLiving);
		EntityLivingBase entity = event.entityLiving;
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
				stack.damageItem(1, entity);
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
	
	public boolean hasPower(EntityLivingBase entityLiving) {
		if (entityLiving.getEquipmentInSlot(3) != null) {
			ItemStack stack = entityLiving.getEquipmentInSlot(3);
			if (stack.getItem() instanceof ItemExosuitArmor) {
				if (stack.getItemDamage() < stack.getMaxDamage()-1) {
					return true;
				}
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
				System.out.println(((TileEntitySteamHeater)event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z)).master);
			}
			if (event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z) instanceof ISteamTransporter) {
				System.out.println(((ISteamTransporter)event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z)).getSteam() + " " + ((ISteamTransporter)event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z)).getPressure());
			}
		
		}
	}
	
	@SubscribeEvent
	public void handleEnhancement(AnvilUpdateEvent event) {
		if (event.right.getItem() instanceof IEnhancement) {
			IEnhancement enhancement = (IEnhancement) event.right.getItem();
			if (enhancement.canApplyTo(event.left) && UtilEnhancements.canEnhance(event.left)) {
				event.cost = enhancement.cost(event.left);
				event.output = UtilEnhancements.getEnhancedItem(event.left, event.right);
			}
		}
	}
}
