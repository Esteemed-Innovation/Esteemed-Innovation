package flaxbeard.steamcraft.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.tuple.MutablePair;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.ai.EntityAIFirearmAttack;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.IWrenchDisplay;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.exosuit.ExosuitPlate;
import flaxbeard.steamcraft.api.steamnet.SteamNetworkRegistry;
import flaxbeard.steamcraft.api.steamnet.data.SteamNetworkData;
import flaxbeard.steamcraft.data.ChunkScoreWorldData;
import flaxbeard.steamcraft.integration.BaublesIntegration;
import flaxbeard.steamcraft.item.ItemExosuitArmor;
import flaxbeard.steamcraft.item.ItemExosuitArmor.ExosuitSlot;
import flaxbeard.steamcraft.item.ItemWrench;
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

	ArrayList<MutablePair<ExosuitSlot,ItemStack>> upgrades = new ArrayList<MutablePair<ExosuitSlot,ItemStack>>();

	public SteamcraftEventHandler() {
		upgrades.add(upgrade(ExosuitSlot.bodyHand,  new ItemStack(SteamcraftItems.powerFist)));
		upgrades.add(upgrade(ExosuitSlot.bootsTop, new ItemStack(SteamcraftItems.fallAssist)));
	}
	
	@SubscribeEvent
	public void handleWorldLoad(WorldEvent.Load event) {
		if (!event.world.isRemote) {
			ChunkScoreWorldData.get(event.world);
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
//			}
//		}
//	}

	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onDrawScreen(RenderGameOverlayEvent.Post event) {

		if(event.type == ElementType.ALL) {
			Minecraft mc = Minecraft.getMinecraft();
			EntityPlayer player = mc.thePlayer;
			MovingObjectPosition pos = mc.objectMouseOver;
			if(pos != null && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemWrench) {
				TileEntity te = mc.theWorld.getTileEntity(pos.blockX, pos.blockY, pos.blockZ);
				if (te instanceof IWrenchDisplay) {
					((IWrenchDisplay)te).displayWrench(event);
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
	public void spawnInEvent(EntityJoinWorldEvent event) {
		if (event.entity instanceof EntitySkeleton && !event.world.isRemote) {
			Chunk chunk = event.world.getChunkFromBlockCoords(MathHelper.floor_double(event.entity.posX), MathHelper.floor_double(event.entity.posZ));
			int fspMachines = 0;
			ChunkScoreWorldData data = ChunkScoreWorldData.get(event.world);
			for (int z = -3; z<4; z++) {
				for (int x = -3; x<4; x++) {
					fspMachines += data.getScore(chunk.xPosition+z,chunk.zPosition+x);
				}
			}
			System.out.println("THERE ARE " + fspMachines + " FSP MACHINES BY THIS MOB");

			EntitySkeleton mob = (EntitySkeleton) event.entity;
			
			if (fspMachines > 20 && event.world.rand.nextInt(Math.max(4, 28-fspMachines)) == 0) {
				if (mob.getHeldItem() != null && mob.getHeldItem().getItem() == Items.bow) {
					List<EntityAITaskEntry> tasksToRemove = new ArrayList<EntityAITaskEntry>();
					for ( Object entry : mob.tasks.taskEntries)
					{
						EntityAITaskEntry entry2 = (EntityAITaskEntry)entry;
						if (entry2.action instanceof EntityAIArrowAttack || entry2.action instanceof EntityAIAttackOnCollide)
						{
							tasksToRemove.add((EntityAITaskEntry) entry);
						}
					}
					for (EntityAITaskEntry entry : tasksToRemove)
					{
						mob.tasks.removeTask(entry.action);
					}
					mob.setCurrentItemOrArmor(0, new ItemStack(SteamcraftItems.blunderbuss));
					mob.tasks.addTask(4, new EntityAIFirearmAttack(mob, 1.0D, 20, 10, 5.0F)); 
				}
			}
			else if (fspMachines > 10 && event.world.rand.nextInt(Math.max(3, 17-fspMachines)) == 0) {
				if (mob.getHeldItem() != null && mob.getHeldItem().getItem() == Items.bow) {
					List<EntityAITaskEntry> tasksToRemove = new ArrayList<EntityAITaskEntry>();
					for ( Object entry : mob.tasks.taskEntries)
					{
						EntityAITaskEntry entry2 = (EntityAITaskEntry)entry;
						if (entry2.action instanceof EntityAIArrowAttack || entry2.action instanceof EntityAIAttackOnCollide)
						{
							tasksToRemove.add((EntityAITaskEntry) entry);
						}
					}
					for (EntityAITaskEntry entry : tasksToRemove)
					{
						mob.tasks.removeTask(entry.action);
					}
					mob.setCurrentItemOrArmor(0, new ItemStack(SteamcraftItems.musket));
					mob.tasks.addTask(4, new EntityAIFirearmAttack(mob, 1.0D, 20, 10, 15.0F)); 
				}
			}
			else if (fspMachines > 5 && event.world.rand.nextInt(Math.max(fspMachines > 10 ? 2 : 3, 10-fspMachines)) == 0) {
				if (mob.getHeldItem() != null && mob.getHeldItem().getItem() == Items.bow) {
					List<EntityAITaskEntry> tasksToRemove = new ArrayList<EntityAITaskEntry>();
					for ( Object entry : mob.tasks.taskEntries)
					{
						EntityAITaskEntry entry2 = (EntityAITaskEntry)entry;
						if (entry2.action instanceof EntityAIArrowAttack || entry2.action instanceof EntityAIAttackOnCollide)
						{
							tasksToRemove.add((EntityAITaskEntry) entry);
						}
					}
					for (EntityAITaskEntry entry : tasksToRemove)
					{
						mob.tasks.removeTask(entry.action);
					}
					mob.setCurrentItemOrArmor(0, new ItemStack(SteamcraftItems.pistol));
					mob.tasks.addTask(4, new EntityAIFirearmAttack(mob, 1.0D, 20, 10, 10.0F)); 
				}
			}
			
		}
		
		if (event.entity instanceof EntityZombie && !event.world.isRemote) {
			Chunk chunk = event.world.getChunkFromBlockCoords(MathHelper.floor_double(event.entity.posX), MathHelper.floor_double(event.entity.posZ));
			int fspMachines = 0;
			ChunkScoreWorldData data = ChunkScoreWorldData.get(event.world);
			for (int z = -3; z<4; z++) {
				for (int x = -3; x<4; x++) {
					fspMachines += data.getScore(chunk.xPosition+z,chunk.zPosition+x);
				}
			}
		

		
			EntityZombie mob = (EntityZombie) event.entity;
			if (fspMachines > 15 && event.world.rand.nextInt(Math.max(2, 22-fspMachines)) == 0) {
				ItemStack armor = new ItemStack(SteamcraftItems.exoArmorBody);
				mob.setCurrentItemOrArmor(3, armor);
				MutablePair<ExosuitSlot,ItemStack> upgrade = null;
				int rand = mob.worldObj.rand.nextInt(3);
				switch (rand) {
				case 0:
					mob.setCurrentItemOrArmor(1, new ItemStack(SteamcraftItems.exoArmorFeet));
					break;
				case 1:
					mob.setCurrentItemOrArmor(2, new ItemStack(SteamcraftItems.exoArmorLegs));
					break;
				case 2:
					mob.setCurrentItemOrArmor(2, new ItemStack(SteamcraftItems.exoArmorHead));
					break;
				}
				while (upgrade == null || mob.getEquipmentInSlot(4-upgrade.left.armor) == null || !(mob.getEquipmentInSlot(4-upgrade.left.armor).getItem() instanceof ItemExosuitArmor) || ((ItemExosuitArmor)mob.getEquipmentInSlot(4-upgrade.left.armor).getItem()).getStackInSlot(mob.getEquipmentInSlot(4-upgrade.left.armor), upgrade.left.slot) != null) {
					upgrade = upgrades.get(mob.worldObj.rand.nextInt(upgrades.size()-1));
				}

				ItemStack targetStack = mob.getEquipmentInSlot(4-upgrade.left.armor);
				((ItemExosuitArmor)targetStack.getItem()).setInventorySlotContents(targetStack, upgrade.left.slot, upgrade.right);
				mob.setCurrentItemOrArmor(4-upgrade.left.armor, targetStack);
				
				if (mob.worldObj.rand.nextBoolean()) {
					ItemStack setItem = null;
					ExosuitPlate[] options = SteamcraftRegistry.plates.values().toArray(new ExosuitPlate[0]);
					ExosuitPlate plate = options[mob.worldObj.rand.nextInt(options.length-1)];
					if (plate.getItem() instanceof Item) {
						setItem = new ItemStack((Item)plate.getItem());
					}
					if (plate.getItem() instanceof String) {

						setItem = OreDictionary.getOres((String)plate.getItem()).get(0);
					}
					for (int i = 1; i<4; i++) {
						if (mob.getEquipmentInSlot(i) != null && mob.getEquipmentInSlot(i).getItem() instanceof ItemExosuitArmor && setItem != null) {

							targetStack = mob.getEquipmentInSlot(i);
							((ItemExosuitArmor)targetStack.getItem()).setInventorySlotContents(targetStack, 1, setItem);
						}
					}
				}
			}
			else 
			if (fspMachines > 10 && event.world.rand.nextInt(Math.max(fspMachines > 15 ? 2 : 3, 17-fspMachines)) == 0) {
				ItemStack armor = new ItemStack(SteamcraftItems.exoArmorBody);
				mob.setCurrentItemOrArmor(3, armor);
				MutablePair<ExosuitSlot,ItemStack> upgrade = null;
				while (upgrade == null || mob.getEquipmentInSlot(4-upgrade.left.armor) == null || !(mob.getEquipmentInSlot(4-upgrade.left.armor).getItem() instanceof ItemExosuitArmor) || ((ItemExosuitArmor)mob.getEquipmentInSlot(4-upgrade.left.armor).getItem()).getStackInSlot(mob.getEquipmentInSlot(4-upgrade.left.armor), upgrade.left.slot) != null) {
					upgrade = upgrades.get(mob.worldObj.rand.nextInt(upgrades.size()-1));
				}
				System.out.println(upgrade.right.toString());

				ItemStack targetStack = mob.getEquipmentInSlot(4-upgrade.left.armor);
				((ItemExosuitArmor)targetStack.getItem()).setInventorySlotContents(targetStack, upgrade.left.slot, upgrade.right);
				mob.setCurrentItemOrArmor(4-upgrade.left.armor, targetStack);
				
				if (mob.worldObj.rand.nextBoolean()) {
					ItemStack setItem = null;
					ExosuitPlate[] options = SteamcraftRegistry.plates.values().toArray(new ExosuitPlate[0]);
					ExosuitPlate plate = options[options.length-1];
					if (plate.getItem() instanceof Item) {
						setItem = new ItemStack((Item)plate.getItem());
					}
					if (plate.getItem() instanceof String) {
						setItem = OreDictionary.getOres((String)plate.getItem()).get(0);
					}
					for (int i = 1; i<4; i++) {
						if (mob.getEquipmentInSlot(i) != null && mob.getEquipmentInSlot(i).getItem() instanceof ItemExosuitArmor && setItem != null) {

							targetStack = mob.getEquipmentInSlot(i);
							((ItemExosuitArmor)targetStack.getItem()).setInventorySlotContents(targetStack, 1, setItem);
						}
					}
				}
			}	
		}
	}
	
	public MutablePair<ItemExosuitArmor.ExosuitSlot,ItemStack> upgrade(ItemExosuitArmor.ExosuitSlot slot,ItemStack stack) {
		return MutablePair.of(slot, stack);
	}
	
	@SubscribeEvent
	public void keepYerGoddamnGuns(LivingEvent.LivingUpdateEvent event) {
		if (event.entity instanceof EntitySkeleton) {
			EntitySkeleton mob = (EntitySkeleton) event.entity;
			if (mob.getHeldItem() != null && mob.getHeldItem().getItem() instanceof ItemFirearm) {
				List<EntityAITaskEntry> tasksToRemove = new ArrayList<EntityAITaskEntry>();
				for ( Object entry : mob.tasks.taskEntries)
				{
					EntityAITaskEntry entry2 = (EntityAITaskEntry)entry;
					if (entry2.action instanceof EntityAIArrowAttack || entry2.action instanceof EntityAIAttackOnCollide)
					{
						tasksToRemove.add((EntityAITaskEntry) entry);
					}
				}
				for (EntityAITaskEntry entry : tasksToRemove)
				{
					mob.tasks.removeTask(entry.action);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void handleFirePunch(LivingAttackEvent event) {
		if (event.source.getSourceOfDamage() instanceof EntityLivingBase) {

			EntityLivingBase entity = (EntityLivingBase) event.source.getSourceOfDamage();
			boolean hasPower = hasPower(entity,5);
			System.out.println(hasPower);

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
			    	if (speed > 0 && Items.iron_pickaxe.func_150893_a(player.getHeldItem(), event.block) != 1.0F) {
			    		event.newSpeed *= 1.0F+11.0F*(speed/1000.0F);
			    	}
				}
				if (player.getHeldItem().getItem() instanceof ItemSteamAxe) {
					ItemSteamAxe.checkNBT(player);
					MutablePair info = ItemSteamAxe.stuff.get(player.getEntityId());
			    	int ticks = (Integer) info.left;
			    	int speed = (Integer) info.right;
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
		if (entityLiving instanceof EntityPlayer) {
			if (entityLiving.getEquipmentInSlot(3) != null) {
				ItemStack stack = entityLiving.getEquipmentInSlot(3);
				if (stack.getItem() instanceof ItemExosuitArmor) {
					return ((ItemExosuitArmor)stack.getItem()).hasPower(stack, i);
				}
			}
			return false;
		}
		else
		{
			if (entityLiving.getEquipmentInSlot(3) != null) {
				ItemStack stack = entityLiving.getEquipmentInSlot(3);
				if (stack.getItem() instanceof ItemExosuitArmor) {
					return true;
				}
			}
			return false;
		}
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
		if (event.action == Action.RIGHT_CLICK_BLOCK && event.entityPlayer.isSneaking() && (event.world.getBlock(event.x, event.y, event.z) == SteamcraftBlocks.boiler || event.world.getBlock(event.x, event.y, event.z) == SteamcraftBlocks.pipe) && event.entityPlayer.getHeldItem() != null && event.entityPlayer.getHeldItem().getItem() instanceof ItemBlock) {
			Block block = Block.getBlockFromItem(event.entityPlayer.getHeldItem().getItem());
			if (!(block instanceof BlockContainer) && !(block instanceof ITileEntityProvider) && (block.getRenderType() == 0 || block.getRenderType() == 39 || block.getRenderType() == 31) && (block.renderAsNormalBlock() || (block == Blocks.glass && event.world.getBlock(event.x, event.y, event.z) == SteamcraftBlocks.pipe))) {
				event.setCanceled(true);
			}
		}
		if (event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z) != null && !event.entityPlayer.worldObj.isRemote) { 
			if (event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z) instanceof TileEntitySteamHeater) {
			}
			if (event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z) instanceof ISteamTransporter) {
				ISteamTransporter trans = (ISteamTransporter)event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z);
				if (event.entityPlayer.worldObj.isRemote){
					//////System.out.println("I AM THE CLIENT");
				}
				//FMLRelaunchLog.info(trans.getSteam() + " " + trans.getPressure() + " " + trans.getNetworkName() + "; " + trans.getNetwork(), "Snap");
			//	////System.out.println("trans cap: "+trans.getCapacity()+" trans steam: "+trans.getSteam() + "; trans press: " + trans.getPressure() + "; network: " + trans.getNetworkName() + "; net cap: "+trans.getNetwork().getCapacity()+"; net steam: " + trans.getNetwork().getSteam()+"; net press: "+trans.getNetwork().getPressure());
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
