package flaxbeard.steamcraft.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent17;
import net.minecraftforge.common.ISpecialArmor.ArmorProperties;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.world.WorldEvent;

import org.apache.commons.lang3.tuple.MutablePair;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.IWrenchDisplay;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.exosuit.UtilPlates;
import flaxbeard.steamcraft.api.steamnet.SteamNetworkRegistry;
import flaxbeard.steamcraft.api.steamnet.data.SteamNetworkData;
import flaxbeard.steamcraft.gui.GuiSteamcraftBook;
import flaxbeard.steamcraft.integration.BaublesIntegration;
import flaxbeard.steamcraft.integration.BloodMagicIntegration;
import flaxbeard.steamcraft.integration.BotaniaIntegration;
import flaxbeard.steamcraft.integration.EnchiridionIntegration;
import flaxbeard.steamcraft.integration.ThaumcraftIntegration;
import flaxbeard.steamcraft.item.ItemExosuitArmor;
import flaxbeard.steamcraft.item.ItemSteamcraftBook;
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
	private static final ResourceLocation icons = new ResourceLocation("steamcraft:textures/gui/icons.png");
	@SubscribeEvent
	public void handleWorldLoad(WorldEvent.Load event) {
		if (!event.world.isRemote) {
			SteamNetworkData.get(event.world);
			SteamNetworkRegistry.initialize();
		}
	}

	@SideOnly(Side.CLIENT)
	private static final RenderItem itemRender = new RenderItem();
	
    public void renderTexture(int screenX, int screenY, int screenEndX, int screenEndY, double startU, double startV, double endU, double endV)
    {
    	
    	int zLevel = 1;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(screenX + 0), (double)(screenY + screenEndY), (double)zLevel, (double)startU, (double)endV);
        tessellator.addVertexWithUV((double)(screenX + screenEndX), (double)(screenY + screenEndY), (double)zLevel, (double)endU, (double)endV);
        tessellator.addVertexWithUV((double)(screenX + screenEndX), (double)(screenY + 0), (double)zLevel, (double)endU, (double)startV);
        tessellator.addVertexWithUV((double)(screenX + 0), (double)(screenY + 0), (double)zLevel, (double)startU, (double)startV);
        tessellator.draw();
    }
    
    
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onDrawScreen(RenderGameOverlayEvent.Post event) {
		if(event.type == ElementType.ALL) {
			Minecraft mc = Minecraft.getMinecraft();
			EntityPlayer player = mc.thePlayer;
			if (!player.capabilities.isCreativeMode && player.inventory.armorItemInSlot(1) != null && player.inventory.armorItemInSlot(1).getItem() instanceof ItemExosuitArmor) {
				ItemStack stack = player.inventory.armorItemInSlot(1);
				ItemExosuitArmor item = (ItemExosuitArmor) stack.getItem();
				//if (item.hasUpgrade(stack, SteamcraftItems.doubleJump)) {
					if (!stack.stackTagCompound.hasKey("aidTicks")) {
						stack.stackTagCompound.setInteger("aidTicks", -1);
					}
					int aidTicks = stack.stackTagCompound.getInteger("aidTicks");
					
					int aidTicksScaled = 7-(int)(aidTicks*7.0F / 100.0F);
					int screenX = event.resolution.getScaledWidth() / 2  - 101;
					int screenY = event.resolution.getScaledHeight() - 39;
					mc.getTextureManager().bindTexture(icons);
					renderTexture(screenX,screenY,9,9,0,0,9D/256D,9D/256D);
					if (aidTicks > 0) {
						renderTexture(screenX+1,screenY,aidTicksScaled,9,10D/256D,0,(10D+aidTicksScaled)/256D,9D/256D);
					}
					else if (aidTicks == 0) {
						renderTexture(screenX,screenY,9,9,18D/256D,0,27D/256D,9D/256D);
					}
					//}
					
			}
			MovingObjectPosition pos = mc.objectMouseOver;
			if(pos != null && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemWrench) {
				TileEntity te = mc.theWorld.getTileEntity(pos.blockX, pos.blockY, pos.blockZ);
				if (te instanceof IWrenchDisplay) {
					((IWrenchDisplay)te).displayWrench(event);
				}
			}
			if (Loader.isModLoaded("Botania")) {
				if(pos != null && player.getEquipmentInSlot(3) != null && player.getEquipmentInSlot(3).getItem() instanceof ItemExosuitArmor && (player.getHeldItem() == null || player.getHeldItem().getItem() != BotaniaIntegration.twigWand())) {
					ItemExosuitArmor chest = (ItemExosuitArmor) player.getEquipmentInSlot(3).getItem();
					if (chest.hasUpgrade(player.getEquipmentInSlot(3), BotaniaIntegration.floralLaurel)) {
						Block block = mc.theWorld.getBlock(pos.blockX, pos.blockY, pos.blockZ);
						BotaniaIntegration.displayThings(pos, event);
					}
				}
			}
			if(pos != null && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() == SteamcraftItems.book) {
				Block block = mc.theWorld.getBlock(pos.blockX, pos.blockY, pos.blockZ);
				ItemStack stack = block.getPickBlock(pos,player.worldObj, pos.blockX, pos.blockY, pos.blockZ);
				if (stack != null) {
					for (ItemStack stack2 : SteamcraftRegistry.bookRecipes.keySet()) {
			    		if (stack2.getItem() == stack.getItem() && stack2.getItemDamage() == stack.getItemDamage()) {
			    			GL11.glPushMatrix();
			    			int x = event.resolution.getScaledWidth() / 2  -8;
			    			int y = event.resolution.getScaledHeight() / 2  - 8;

			    			int color = 0x6600FF00;
			    			new RenderItem().renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack(SteamcraftItems.book), x, y);
			    			GL11.glDisable(GL11.GL_LIGHTING);
			    			mc.fontRenderer.drawStringWithShadow("", x + 15, y + 13, 0xC6C6C6);
			    			GL11.glPopMatrix();
			    			GL11.glEnable(GL11.GL_BLEND);
			    			if (Mouse.isButtonDown(1)) {
			        			player.openGui(Steamcraft.instance, 1, player.worldObj, 0,0,0);
			        			GuiSteamcraftBook.viewing = SteamcraftRegistry.bookRecipes.get(stack2).left;	                	
			        			GuiSteamcraftBook.currPage = MathHelper.floor_float((float)SteamcraftRegistry.bookRecipes.get(stack2).right/2.0F);
			                	GuiSteamcraftBook.lastIndexPage = 1;
			                	GuiSteamcraftBook.bookTotalPages = MathHelper.ceiling_float_int(SteamcraftRegistry.researchPages.get(GuiSteamcraftBook.viewing).length/2F);
			                	((GuiSteamcraftBook)Minecraft.getMinecraft().currentScreen).updateButtons();
			    			}
			    		}
					}
				}
			}
			
		}
	}
	
	private HashMap<Integer, Boolean> lastHadCustomer = new HashMap<Integer,Boolean>();
	
	public static boolean lastViewVillagerGui = false;
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent 
	public void updateVillagersClientside(GuiScreenEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		if (event.gui instanceof GuiMerchant && !lastViewVillagerGui) {
			GuiMerchant gui = (GuiMerchant) event.gui;
			if (mc.thePlayer.inventory.armorInventory[3] != null && (mc.thePlayer.inventory.armorInventory[3].getItem() == SteamcraftItems.tophat
					|| (mc.thePlayer.inventory.armorInventory[3].getItem() == SteamcraftItems.exoArmorHead && ((ItemExosuitArmor)mc.thePlayer.inventory.armorInventory[3].getItem()).hasUpgrade(mc.thePlayer.inventory.armorInventory[3],SteamcraftItems.tophat)))) {
				IMerchant merch = ReflectionHelper.getPrivateValue(GuiMerchant.class, gui, 2);
				MerchantRecipeList recipeList = merch.getRecipes(mc.thePlayer);
				if (recipeList != null) {
					for (Object obj : recipeList) {
						MerchantRecipe recipe = (MerchantRecipe) obj;
						if (recipe.getItemToSell().stackSize > 1 && recipe.getItemToSell().stackSize != MathHelper.floor_float(recipe.getItemToSell().stackSize * 1.25F)) {
							recipe.getItemToSell().stackSize = MathHelper.floor_float(recipe.getItemToSell().stackSize * 1.25F);
						}
						else if (recipe.getItemToBuy().stackSize > 1 && recipe.getItemToBuy().stackSize != MathHelper.ceiling_float_int(recipe.getItemToBuy().stackSize / 1.25F)) {
							recipe.getItemToBuy().stackSize = MathHelper.ceiling_float_int(recipe.getItemToBuy().stackSize / 1.25F);
						}
						else if (recipe.getSecondItemToBuy() != null && recipe.getSecondItemToBuy().stackSize > 1 && recipe.getSecondItemToBuy().stackSize != MathHelper.ceiling_float_int(recipe.getSecondItemToBuy().stackSize / 1.25F)) {
							recipe.getSecondItemToBuy().stackSize = MathHelper.ceiling_float_int(recipe.getSecondItemToBuy().stackSize / 1.25F);
						}
					}
					lastViewVillagerGui = true;
				}
				merch.setRecipes(recipeList);
				ReflectionHelper.setPrivateValue(GuiMerchant.class, gui, merch, 2);
			}
		}
	}
	
	@SubscribeEvent
	public void updateVillagers(LivingUpdateEvent event) {
		if (event.entityLiving instanceof EntityVillager) {
			EntityVillager villager = (EntityVillager) event.entityLiving;
			int timeUntilReset = ReflectionHelper.getPrivateValue(EntityVillager.class, villager, 6);
			String lastBuyingPlayer = ReflectionHelper.getPrivateValue(EntityVillager.class, villager, 9);
			if (!villager.isTrading() && timeUntilReset == 39 && lastBuyingPlayer != null) {
				EntityPlayer player = villager.worldObj.getPlayerEntityByName(lastBuyingPlayer);
				if (player != null) {
					if (player.inventory.armorInventory[3] != null && (player.inventory.armorInventory[3].getItem() == SteamcraftItems.tophat)) {
						ItemStack hat = player.inventory.armorInventory[3];
						if (!hat.hasTagCompound()) {
							hat.setTagCompound(new NBTTagCompound());
						}
						if (!hat.stackTagCompound.hasKey("level")) {
							hat.stackTagCompound.setInteger("level", 0);
						}
						int level = hat.stackTagCompound.getInteger("level");
						level++;
						hat.stackTagCompound.setInteger("level", level);
					}
					else if (player.inventory.armorInventory[3] != null && player.inventory.armorInventory[3].getItem() == SteamcraftItems.exoArmorHead && ((ItemExosuitArmor)player.inventory.armorInventory[3].getItem()).hasUpgrade(player.inventory.armorInventory[3],SteamcraftItems.tophat)) {
						ItemStack hat = ((ItemExosuitArmor)player.inventory.armorInventory[3].getItem()).getStackInSlot(player.inventory.armorInventory[3], 3);
						if (!hat.hasTagCompound()) {
							hat.setTagCompound(new NBTTagCompound());
						}
						if (!hat.stackTagCompound.hasKey("level")) {
							hat.stackTagCompound.setInteger("level", 0);
						}
						int level = hat.stackTagCompound.getInteger("level");
						level++;
						hat.stackTagCompound.setInteger("level", level);
						((ItemExosuitArmor)player.inventory.armorInventory[3].getItem()).setInventorySlotContents(player.inventory.armorInventory[3], 3, hat);
					}
				}
			}
		}
		if (event.entityLiving instanceof EntityVillager && event.entityLiving.worldObj.isRemote == false) {
			EntityVillager villager = (EntityVillager) event.entityLiving;
			if (!lastHadCustomer.containsKey(villager.getEntityId())) {
				lastHadCustomer.put(villager.getEntityId(), false);
			}
			boolean hadCustomer = lastHadCustomer.get(villager.getEntityId());
			boolean hasCustomer = false;
			if (villager.getCustomer() != null && villager.getCustomer().inventory.armorInventory[3] != null && (villager.getCustomer().inventory.armorInventory[3].getItem() == SteamcraftItems.tophat
					|| (villager.getCustomer().inventory.armorInventory[3].getItem() == SteamcraftItems.exoArmorHead && ((ItemExosuitArmor)villager.getCustomer().inventory.armorInventory[3].getItem()).hasUpgrade(villager.getCustomer().inventory.armorInventory[3],SteamcraftItems.tophat)))) {
				EntityPlayer customer = villager.getCustomer();
				hasCustomer = true;

				if (!hadCustomer) {
					MerchantRecipeList recipeList = ReflectionHelper.getPrivateValue(EntityVillager.class, villager, 5);
					for (Object obj : recipeList) {
						MerchantRecipe recipe = (MerchantRecipe) obj;
						if (recipe.getItemToSell().stackSize > 1 && recipe.getItemToSell().stackSize != MathHelper.floor_float(recipe.getItemToSell().stackSize * 1.25F)) {
							recipe.getItemToSell().stackSize = MathHelper.floor_float(recipe.getItemToSell().stackSize * 1.25F);
						}
						else if (recipe.getItemToBuy().stackSize > 1 && recipe.getItemToBuy().stackSize != MathHelper.ceiling_float_int(recipe.getItemToBuy().stackSize / 1.25F)) {
							recipe.getItemToBuy().stackSize = MathHelper.ceiling_float_int(recipe.getItemToBuy().stackSize / 1.25F);
						}
						else if (recipe.getSecondItemToBuy() != null && recipe.getSecondItemToBuy().stackSize > 1 && recipe.getSecondItemToBuy().stackSize != MathHelper.ceiling_float_int(recipe.getSecondItemToBuy().stackSize / 1.25F)) {
							recipe.getSecondItemToBuy().stackSize = MathHelper.ceiling_float_int(recipe.getSecondItemToBuy().stackSize / 1.25F);
						}
					}
					ReflectionHelper.setPrivateValue(EntityVillager.class, villager, recipeList, 5);
					//customer.closeScreen();
					//customer.displayGUIMerchant(villager, villager.getCustomNameTag());
				}
			}
			
			if (!hasCustomer && hadCustomer) {
				MerchantRecipeList recipeList = ReflectionHelper.getPrivateValue(EntityVillager.class, villager, 5);
				if (recipeList != null) {
					for (Object obj : recipeList) {
						MerchantRecipe recipe = (MerchantRecipe) obj;
						if (recipe.getItemToSell().stackSize > 1 && recipe.getItemToSell().stackSize != MathHelper.ceiling_float_int(recipe.getItemToSell().stackSize / 1.25F)) {
							recipe.getItemToSell().stackSize = MathHelper.ceiling_float_int(recipe.getItemToSell().stackSize / 1.25F);
						}
						else if (recipe.getItemToBuy().stackSize > 1 && recipe.getItemToBuy().stackSize != MathHelper.floor_float(recipe.getItemToBuy().stackSize * 1.25F)) {
							recipe.getItemToBuy().stackSize = MathHelper.floor_float(recipe.getItemToBuy().stackSize * 1.25F);
						}
						else if (recipe.getSecondItemToBuy() != null && recipe.getSecondItemToBuy().stackSize > 1 && recipe.getSecondItemToBuy().stackSize != MathHelper.floor_float(recipe.getSecondItemToBuy().stackSize * 1.25F)) {
							recipe.getSecondItemToBuy().stackSize = MathHelper.floor_float(recipe.getSecondItemToBuy().stackSize * 1.25F);
						}
					}
				}
				ReflectionHelper.setPrivateValue(EntityVillager.class, villager, recipeList, 5);
			}

			lastHadCustomer.remove(villager.getEntityId());
			lastHadCustomer.put(villager.getEntityId(), hasCustomer);
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
	public void muffleSounds(PlaySoundEvent event) {
		//System.out.println(event.name);
	}
	
	@SubscribeEvent
	public void muffleSounds(PlaySoundEvent17 event) {
//		if (event.name.contains("step")) {
//			float x = event.sound.getXPosF();
//			float y = event.sound.getYPosF();
//			float z = event.sound.getZPosF();
//			List entities = Minecraft.getMinecraft().thePlayer.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(x-0.5F, y-0.5F, z-0.5F, x+0.5F, y+0.5F, z+0.5F));
//			for (Object obj : entities) {
//				EntityLivingBase entity = (EntityLivingBase) obj;
//				if (entity.getEquipmentInSlot(2) != null && entity.getEquipmentInSlot(2).getItem() instanceof ItemExosuitArmor) {
//					ItemExosuitArmor chest = (ItemExosuitArmor) entity.getEquipmentInSlot(2).getItem();
//					if (chest.hasUpgrade(entity.getEquipmentInSlot(2), SteamcraftItems.stealthUpgrade)) {
//						event.result = null;
//					}
//				}
//			}
//		}
	}
	
	@SubscribeEvent
	public void hideCloakedPlayers(LivingUpdateEvent event) {
//		if (event.entityLiving instanceof EntityLiving) {
//			EntityLiving entity = (EntityLiving) event.entityLiving;
//			if (entity.getAttackTarget() != null && entity.getAttackTarget().isPotionActive(Steamcraft.semiInvisible)) {
//		        IAttributeInstance iattributeinstance = entity.getEntityAttribute(SharedMonsterAttributes.followRange);
//		        double d0 = iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
//			    d0 = d0 / 3D;
//		        List list = entity.worldObj.getEntitiesWithinAABB(Entity.class, entity.boundingBox.expand(d0, 4.0D, d0));
//			    boolean foundPlayer = false;
//			    for (Object mob : list) {
//			    	Entity ent = (Entity) mob;
//			    	if (ent == entity.getAttackTarget()) {
//			    		foundPlayer = true;
//			    	}
//			    }
//			    if (!foundPlayer) {
//			    	entity.setAttackTarget(null);
//			    }
//			}
//		}
	}
	
	@SubscribeEvent
	public void hideCloakedPlayers(LivingSetAttackTargetEvent event) {
//		if (event.entityLiving instanceof EntityLiving) {
//			EntityLiving entity = (EntityLiving) event.entityLiving;
//			if (event.target != null && event.target.isPotionActive(Steamcraft.semiInvisible)) {
//		        IAttributeInstance iattributeinstance = entity.getEntityAttribute(SharedMonsterAttributes.followRange);
//		        double d0 = iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
//			    d0 = d0 / 3D;
//		        List list = entity.worldObj.getEntitiesWithinAABB(Entity.class, entity.boundingBox.expand(d0, 4.0D, d0));
//			    boolean foundPlayer = false;
//			    for (Object mob : list) {
//			    	Entity ent = (Entity) mob;
//			    	if (ent == event.target) {
//			    		foundPlayer = true;
//			    	}
//			    }
//			    if (!foundPlayer) {
//			    	entity.setAttackTarget(null);
//			    }
//			}
//		}
	}
	
	@SubscribeEvent
	public void preRender(RenderLivingEvent.Pre event) {
		if (event.entity.isPotionActive(Steamcraft.semiInvisible)) {
	        GL11.glPushMatrix();
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.10F);
	        GL11.glDepthMask(false);
	        GL11.glEnable(GL11.GL_BLEND);
	        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	        GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
		}
	}
	
	@SubscribeEvent
	public void postRender(RenderLivingEvent.Post event) {
		if (event.entity.isPotionActive(Steamcraft.semiInvisible)) {
	        GL11.glDisable(GL11.GL_BLEND);
	        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
	        GL11.glPopMatrix();
	        GL11.glDepthMask(true);
		}
	}
	
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void plateTooltip(ItemTooltipEvent event) {
		ItemStack stack = event.itemStack;
		if (UtilPlates.getPlate(stack) != null) {
			event.toolTip.add(EnumChatFormatting.BLUE + StatCollector.translateToLocal("steamcraft.plate.bonus") + UtilPlates.getPlate(stack).effect());
		}
		if (stack.getItem() instanceof ItemExosuitArmor || stack.getItem() instanceof ItemSteamShovel) {
			ArrayList<String> linesToRemove = new ArrayList<String>();
			for (String str : event.toolTip) {
				if (str == "") {
					linesToRemove.add(str);
				}
				if (str.contains("+")) {
					linesToRemove.add(str);
				}
			}
			for (String str : linesToRemove) {
				if (str.contains("+") && !str.contains("+0.25")) {
					event.toolTip.remove(str);
					event.toolTip.add(1,str);
				}
				else
				{
					event.toolTip.remove(str);
				}
			}
		}
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		if (Minecraft.getMinecraft().currentScreen instanceof GuiContainer) {
			if (Loader.isModLoaded("Thaumcraft") && Config.openThaum) {
				ThaumcraftIntegration.addTooltip(event);
			}
	    	for (ItemStack stack2 : SteamcraftRegistry.bookRecipes.keySet()) {
	    		if (stack2.getItem() == stack.getItem() && (stack2.getItemDamage() == stack.getItemDamage() || stack.getItem() instanceof ItemArmor || stack.getItem() instanceof ItemTool)) {
	    			boolean foundBook = Loader.isModLoaded("Enchiridion") ? EnchiridionIntegration.hasBook(SteamcraftItems.book, player) : false;
	    			for (int p = 0; p < player.inventory.getSizeInventory(); p++) {
						if (player.inventory.getStackInSlot(p) != null && player.inventory.getStackInSlot(p).getItem() instanceof ItemSteamcraftBook) {
							foundBook = true;
							break;
						}
	    			}
	    			if (foundBook) {
		    			event.toolTip.add(EnumChatFormatting.ITALIC+""+EnumChatFormatting.GRAY+StatCollector.translateToLocal("steamcraft.book.shiftright"));
		    			if (Mouse.isButtonDown(0) && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
		        			player.openGui(Steamcraft.instance, 1, player.worldObj, 0,0,0);
		        			GuiSteamcraftBook.viewing = SteamcraftRegistry.bookRecipes.get(stack2).left;	                	
		        			GuiSteamcraftBook.currPage = MathHelper.floor_float((float)SteamcraftRegistry.bookRecipes.get(stack2).right/2.0F);
		                	GuiSteamcraftBook.lastIndexPage = 1;
		                	GuiSteamcraftBook.bookTotalPages = MathHelper.ceiling_float_int(SteamcraftRegistry.researchPages.get(GuiSteamcraftBook.viewing).length/2F);
		                	((GuiSteamcraftBook)Minecraft.getMinecraft().currentScreen).updateButtons();
		    			}
	    			}
	    		}
	    	}
		}
	}
	
	@SubscribeEvent
	public void doubleExp(PlayerPickupXpEvent event) {
		EntityPlayer player = event.entityPlayer;
		for (int i = 1; i<5; i++) {
			float multValu = 1;
			if (player.getEquipmentInSlot(i) != null) {
				ItemStack stack = player.getEquipmentInSlot(i);
				if (stack.getItem() instanceof ItemExosuitArmor) {
					if (((ItemExosuitArmor)stack.getItem()).hasPlates(stack) && UtilPlates.getPlate(stack.stackTagCompound.getString("plate")).getIdentifier() == "Gold") {
						multValu *= 1.25F;
					}
				}
			}
			event.orb.xpValue = MathHelper.ceiling_float_int(event.orb.xpValue*multValu);
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
			boolean hasPower = hasPower(entity,Config.powerFistConsumption);
			if (hasPower && entity.getEquipmentInSlot(3) != null && entity.getHeldItem() == null) {
				ItemExosuitArmor chest = (ItemExosuitArmor) entity.getEquipmentInSlot(3).getItem();
				if (chest.hasUpgrade(entity.getEquipmentInSlot(3), SteamcraftItems.powerFist)) {
			        entity.worldObj.playSoundEffect(entity.posX, entity.posY, entity.posZ, "random.explode", 4.0F, (1.0F + (entity.worldObj.rand.nextFloat() - entity.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
					event.entityLiving.motionX += 3.0F*entity.getLookVec().normalize().xCoord;
					event.entityLiving.motionY += (entity.getLookVec().normalize().yCoord > 0.0F ? 2.0F*entity.getLookVec().normalize().yCoord : 0.0F) + 1.5F;
					event.entityLiving.motionZ += 3.0F*entity.getLookVec().normalize().zCoord;
				
					entity.motionX += -0.5F*entity.getLookVec().normalize().xCoord;
					entity.motionZ += -0.5F*entity.getLookVec().normalize().zCoord;
					entity.getEquipmentInSlot(3).damageItem(Config.powerFistConsumption,entity);
				}
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void handleFallDamage(LivingHurtEvent event) {
		if (Loader.isModLoaded("AWWayofTime")) {
			BloodMagicIntegration.handleAttack(event);
		}
		if (((event.entityLiving instanceof EntityPlayer)) && (event.source.damageType.equals("mob")) && (event.source.getEntity() != null) && (!event.entityLiving.worldObj.isRemote))
	    {
			EntityPlayer player = (EntityPlayer)event.entityLiving;
			if (player.getHealth() <= 5.0F) {
				int vibrantLevel = 0;
				for (int i = 0; i < player.inventory.armorInventory.length; i++) {
					ItemStack armor = player.inventory.armorInventory[i];
					if (armor != null && armor.getItem() instanceof ItemExosuitArmor) {
						ItemExosuitArmor armorItem = (ItemExosuitArmor) armor.getItem();
						if (armorItem.hasPlates(armor) && UtilPlates.getPlate(armor.stackTagCompound.getString("plate")).getIdentifier() == "Vibrant") {
							vibrantLevel+=1;
						}
					}
				}
				if ((vibrantLevel > 0) && (player.worldObj.rand.nextInt(5-vibrantLevel) == 0)) {
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
						tX = (int) (player.posX + range*Math.sin(Math.toRadians(startRotation)));
						tZ = (int) (player.posZ + range*Math.cos(Math.toRadians(startRotation)));
						tY = (int) player.posY+yO;
						List mobs = player.worldObj.getEntitiesWithinAABB(EntityMob.class, AxisAlignedBB.getBoundingBox(tX+0.5F-safeRange, tY+0.5F-safeRange, tZ+0.5F-safeRange, tX+0.5F+safeRange, tY+0.5F+safeRange, tZ+0.5F+safeRange));
						if (mobs.size() == 0 &&player.worldObj.isSideSolid(tX, tY-1, tZ, ForgeDirection.UP) && !player.worldObj.isAnyLiquid(AxisAlignedBB.getBoundingBox(tX, tY-1, tZ, tX, tY+1, tZ)) && player.worldObj.isAirBlock(tX, tZ, tY) && player.worldObj.isAirBlock(tX, tZ, tY+1)) {
							foundSpot = true;				
						}
						else
						{
							if (counter >= 36) {
								if (yO > -2) {
									yO--;
									counter = 0;
								}
								else
								{
									counter = 0;
									yO = 2;
									range += 2;
								}
							}
							else
							{
								startRotation+= 10;
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
		if (((event.entityLiving instanceof EntityPlayer)) && (event.source.damageType.equals("mob")) && (event.source.getEntity() != null) && (!event.entityLiving.worldObj.isRemote))
	    {
			EntityPlayer player = (EntityPlayer)event.entityLiving;
			Entity mob = event.source.getEntity();
			int enderiumLevel = 0;
			for (int i = 0; i < player.inventory.armorInventory.length; i++) {
				ItemStack armor = player.inventory.armorInventory[i];
				if (armor != null && armor.getItem() instanceof ItemExosuitArmor) {
					ItemExosuitArmor armorItem = (ItemExosuitArmor) armor.getItem();
					if (armorItem.hasPlates(armor) && UtilPlates.getPlate(armor.stackTagCompound.getString("plate")).getIdentifier() == "Enderium") {
						enderiumLevel+=1;
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
					tX = (int) (mob.posX + range*Math.sin(Math.toRadians(startRotation)));
					tZ = (int) (mob.posZ + range*Math.cos(Math.toRadians(startRotation)));
					tY = (int) mob.posY+yO;
					if (player.worldObj.isSideSolid(tX, tY-1, tZ, ForgeDirection.UP) && !player.worldObj.isAnyLiquid(AxisAlignedBB.getBoundingBox(tX, tY-1, tZ, tX, tY+1, tZ)) && player.worldObj.isAirBlock(tX, tZ, tY) && player.worldObj.isAirBlock(tX, tZ, tY+1)) {
						foundSpot = true;				
					}
					else
					{
						if (counter >= 36) {
							if (yO > -2) {
								yO--;
								counter = 0;
							}
							else
							{
								counter = 0;
								yO = 2;
								range += 2;
							}
						}
						else
						{
							startRotation+= 10;
							counter++;
						}
					}
				}

				if (foundSpot) {
					mob.setPositionAndRotation(tX, tY, tZ, mob.rotationYaw, mob.rotationPitch);
				}
			}
	    }
		if (((event.entityLiving instanceof EntityPlayer)) && (event.source.damageType.equals("mob")) && (event.source.getEntity() != null))
	    {
			EntityPlayer player = (EntityPlayer)event.entityLiving;
			int fireLevel = 0;
			for (int i = 0; i < player.inventory.armorInventory.length; i++) {
				ItemStack armor = player.inventory.armorInventory[i];
				if (armor != null && armor.getItem() instanceof ItemExosuitArmor) {
					ItemExosuitArmor armorItem = (ItemExosuitArmor) armor.getItem();
					if (armorItem.hasPlates(armor) && UtilPlates.getPlate(armor.stackTagCompound.getString("plate")).getIdentifier() == "Fiery") {
						fireLevel+=3;
					}
				}
			}
			if ((fireLevel > 0) && (player.worldObj.rand.nextInt(25) < fireLevel)) {
				event.source.getEntity().setFire(fireLevel / 2);
			}
	    }
		if (((event.entityLiving instanceof EntityPlayer)) && (event.source.damageType.equals("mob")) && 
			      (event.source.getEntity() != null) && ((event.source.getEntity() instanceof EntityLivingBase)))
    	{
			EntityPlayer player = (EntityPlayer)event.entityLiving;
			int chillLevel = 0;
			for (int i = 0; i < player.inventory.armorInventory.length; i++) {
				ItemStack armor = player.inventory.armorInventory[i];
				if (armor != null && armor.getItem() instanceof ItemExosuitArmor) {
					ItemExosuitArmor armorItem = (ItemExosuitArmor) armor.getItem();
					if (armorItem.hasPlates(armor) && UtilPlates.getPlate(armor.stackTagCompound.getString("plate")).getIdentifier() == "Yeti") {
						chillLevel+=1;
					}
				}
			}
			if (chillLevel > 0) {
		        ((EntityLivingBase)event.source.getEntity()).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, chillLevel * 3 + 5, MathHelper.ceiling_float_int((float)chillLevel/2F)));
			}
	    }
		if (event.source == DamageSource.fall) {
			boolean hasPower = hasPower(event.entityLiving,(int)((float)event.ammount/Config.fallAssistDivisor));
			int armor = getExoArmor(event.entityLiving);
			EntityLivingBase entity = event.entityLiving;
			if (hasPower && entity.getEquipmentInSlot(3) != null && entity.getEquipmentInSlot(1) != null && entity.getEquipmentInSlot(1).getItem() instanceof ItemExosuitArmor) {
				ItemExosuitArmor boots = (ItemExosuitArmor) entity.getEquipmentInSlot(1).getItem();
				if (boots.hasUpgrade(entity.getEquipmentInSlot(1), SteamcraftItems.fallAssist)) {
			        if (event.ammount <= 6.0F) {
			        	event.ammount = 0.0F;
			        }
					event.ammount = event.ammount/3.0F;
					entity.getEquipmentInSlot(3).damageItem((int)((float)event.ammount/Config.fallAssistDivisor), event.entityLiving);
					if (event.ammount == 0.0F) {
				        event.setResult(Event.Result.DENY);
				        event.setCanceled(true);
					}
				}
			}
		}
		if (((event.entity instanceof EntityPlayer)) && (((EntityPlayer)event.entity).inventory.armorItemInSlot(1) != null) && (((EntityPlayer)event.entity).inventory.armorItemInSlot(1).getItem() instanceof ItemExosuitArmor)) {
			ItemStack stack = ((EntityPlayer)event.entity).inventory.armorItemInSlot(1);
			ItemExosuitArmor item = (ItemExosuitArmor) stack.getItem();
			//if (item.hasUpgrade(stack, SteamcraftItems.doubleJump)) {
			float amount = event.ammount;
			EntityPlayer player = ((EntityPlayer)event.entity);
			DamageSource src = event.source;
			if (!player.isEntityInvulnerable())
	        {
	            if (amount <= 0) return;
	            if (!src.isUnblockable() && player.isBlocking() && amount > 0.0F)
	            {
	                amount = (1.0F + amount) * 0.5F;
	            }

	            amount = ArmorProperties.ApplyArmor(player, player.inventory.armorInventory, src, amount);
	            if (amount <= 0) return;
	            float f1 = amount;
	            amount = Math.max(amount - player.getAbsorptionAmount(), 0.0F);
	        }
			if (amount > 0.0F) {
				stack.stackTagCompound.setFloat("damageAmount", amount);
				stack.stackTagCompound.setInteger("aidTicks", 100);

			}
			//}
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
		boolean hasPower = hasPower(event.entityLiving,Config.jumpBoostConsumptionShiftJump);
		if (((event.entity instanceof EntityPlayer)) && (((EntityPlayer)event.entity).inventory.armorItemInSlot(0) != null) && (((EntityPlayer)event.entity).inventory.armorItemInSlot(0).getItem() instanceof ItemExosuitArmor)) {
			ItemStack stack = ((EntityPlayer)event.entity).inventory.armorItemInSlot(0);
			ItemExosuitArmor item = (ItemExosuitArmor) stack.getItem();

			if ((((EntityPlayer)event.entity).isSneaking() && hasPower) || hasPower(event.entityLiving,Config.jumpBoostConsumption)) {

				if (item.hasUpgrade(stack, SteamcraftItems.jumpAssist)) {
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
						event.entityLiving.motionZ += (jump+1)*vector.zCoord*2;
						event.entityLiving.motionX += (jump+1)*vector.xCoord*2;
						event.entityLiving.getEquipmentInSlot(3).damageItem(Config.jumpBoostConsumptionShiftJump, event.entityLiving);
					}
					else
					{
						event.entityLiving.getEquipmentInSlot(3).damageItem(Config.jumpBoostConsumption, event.entityLiving);
						event.entityLiving.motionY += 0.2750000059604645D;
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

		if (((event.entity instanceof EntityPlayer)) && (((EntityPlayer)event.entity).inventory.armorItemInSlot(1) != null) && (((EntityPlayer)event.entity).inventory.armorItemInSlot(1).getItem() instanceof ItemExosuitArmor)) {
			ItemStack stack = ((EntityPlayer)event.entity).inventory.armorItemInSlot(1);
			ItemExosuitArmor item = (ItemExosuitArmor) stack.getItem();
			//if (item.hasUpgrade(stack, SteamcraftItems.doubleJump)) {
				if (!stack.stackTagCompound.hasKey("aidTicks")) {
					stack.stackTagCompound.setInteger("aidTicks", -1);
				}
				int aidTicks = stack.stackTagCompound.getInteger("aidTicks");

				if (aidTicks > 0) {
					aidTicks--;
				}
				if (aidTicks == 0) {
					if (!stack.stackTagCompound.hasKey("ticksNextHeal")) {
						stack.stackTagCompound.setInteger("ticksNextHeal", 0);
					}
					float damageAmount = stack.stackTagCompound.getInteger("damageAmount");
					int ticksNextHeal = stack.stackTagCompound.getInteger("ticksNextHeal");
					if (ticksNextHeal > 0) {
						ticksNextHeal--;
					}
					if (ticksNextHeal == 0) {
						//event.entityLiving.heal(1.0F);
						damageAmount -=1.0F;
						stack.stackTagCompound.setFloat("damageAmount", damageAmount);
						ticksNextHeal=5;
					}
					if (damageAmount == 0.0F) {
						aidTicks = -1;
					}
					stack.stackTagCompound.setInteger("ticksNextHeal", ticksNextHeal);
				}
				stack.stackTagCompound.setInteger("aidTicks", aidTicks);
			//}
		}
		
		if (((event.entity instanceof EntityPlayer)) && (((EntityPlayer)event.entity).inventory.armorItemInSlot(0) != null) && (((EntityPlayer)event.entity).inventory.armorItemInSlot(0).getItem() instanceof ItemExosuitArmor)) {
			ItemStack stack = ((EntityPlayer)event.entity).inventory.armorItemInSlot(0);
			ItemExosuitArmor item = (ItemExosuitArmor) stack.getItem();
			if (item.hasUpgrade(stack, SteamcraftItems.doubleJump) && event.entity.onGround) {
				stack.stackTagCompound.setBoolean("usedJump", false);
			}
		}
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
			}
		}
		
		if (hasPower && entity.getEquipmentInSlot(2) != null && entity.getEquipmentInSlot(2).getItem() instanceof ItemExosuitArmor) {
			ItemExosuitArmor chest = (ItemExosuitArmor) entity.getEquipmentInSlot(2).getItem();
			if (chest.hasUpgrade(entity.getEquipmentInSlot(2), SteamcraftItems.runAssist)) {
				if (!lastMotions.containsKey(entity.getEntityId())) {
					lastMotions.put(entity.getEntityId(), MutablePair.of(entity.posX,entity.posZ));
				}
				double lastX = lastMotions.get(entity.getEntityId()).left;
				double lastZ = lastMotions.get(entity.getEntityId()).right;
				if ((entity.moveForward > 0.0F) &&(lastX != entity.posX || lastZ != entity.posZ) && entity.onGround && !entity.isInWater()) {
					entity.moveFlying(0.0F, 1.0F, 0.075F);
					if (!event.entityLiving.getEquipmentInSlot(3).stackTagCompound.hasKey("ticksUntilConsume")) {
						event.entityLiving.getEquipmentInSlot(3).stackTagCompound.setInteger("ticksUntilConsume", 2);
					}
					if (event.entityLiving.getEquipmentInSlot(3).stackTagCompound.getInteger("ticksUntilConsume") <= 0) {
						event.entityLiving.getEquipmentInSlot(3).damageItem(1, event.entityLiving);
					}
				}
			}
		}
//		
//		if (hasPower(entity,100) && entity.getEquipmentInSlot(2) != null && entity.getEquipmentInSlot(2).getItem() instanceof ItemExosuitArmor && !entity.worldObj.isRemote) {
//			ItemExosuitArmor chest = (ItemExosuitArmor) entity.getEquipmentInSlot(2).getItem();
//			if (chest.hasUpgrade(entity.getEquipmentInSlot(2), SteamcraftItems.antiFire)) {
//				if (entity.isBurning()) {
//					System.out.println("Y");
//
//					event.entityLiving.getEquipmentInSlot(3).damageItem(10, event.entityLiving);
//					if (entity.worldObj.isAirBlock((int)entity.posX, (int)entity.posY, (int)entity.posZ) || entity.worldObj.getBlock((int)entity.posX, (int)entity.posY, (int)entity.posZ).isReplaceable(entity.worldObj, (int)entity.posX, (int)entity.posY, (int)entity.posZ) || entity.worldObj.getBlock((int)entity.posX, (int)entity.posY, (int)entity.posZ) == Blocks.fire) {
//
//						entity.worldObj.setBlock((int)entity.posX, (int)entity.posY, (int)entity.posZ, Blocks.water, 1, 1);
//					}
//				}
//			}
//		}
	}
	
	public HashMap<Integer, Float> prevStep = new HashMap();
	
	@SubscribeEvent
	public void handleSteamcraftArmor(LivingEvent.LivingUpdateEvent event) {
		boolean hasPower = hasPower(event.entityLiving, 1);
		int armor = getExoArmor(event.entityLiving);
		EntityLivingBase entity = event.entityLiving;
		ItemStack armor2 = entity.getEquipmentInSlot(1);
		
		if (hasPower) {
			if (entity.isSneaking()) {
//				if ((!event.entityLiving.isPotionActive(Steamcraft.semiInvisible) || event.entityLiving.getActivePotionEffect(Steamcraft.semiInvisible).getDuration() < 2)) {
//					event.entityLiving.addPotionEffect(new PotionEffect(Steamcraft.semiInvisible.id, 2, 0, false));
//				}
			}
			if (!lastMotions.containsKey(entity.getEntityId())) {
				lastMotions.put(entity.getEntityId(), MutablePair.of(entity.posX,entity.posZ));
			}
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
			double lastX = lastMotions.get(entity.getEntityId()).left;
			double lastZ = lastMotions.get(entity.getEntityId()).right;
			if (ticksLeft <= 0) {
				if (Config.passiveDrain && (lastX != entity.posX || lastZ != entity.posZ)) {
					stack.damageItem(1, entity);
				}
				ticksLeft = 2;
			}
			lastMotions.put(entity.getEntityId(), MutablePair.of(entity.posX,entity.posZ));

			ticksLeft--;
			stack.stackTagCompound.setInteger("ticksUntilConsume", ticksLeft);
			if (armor == 4) {
				if (entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(uuid) == null) {
					entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(exoBoost);
				}
				if (entity.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getModifier(uuid) == null) {
					entity.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).applyModifier(exoBoost);
				}
		        if (!prevStep.containsKey(Integer.valueOf(entity.getEntityId()))) {
		            prevStep.put(Integer.valueOf(entity.getEntityId()), Float.valueOf(entity.stepHeight));
		        }
		        entity.stepHeight = 1.0F;
			}
			else
			{
				removeGoodExoBoost(entity);
			}
		}
		else
		{
			removeGoodExoBoost(entity);
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
			removeBadExoBoost(entity);
		}
	}
	
	private void removeGoodExoBoost(EntityLivingBase entity) {
		if (entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(uuid) != null) {
			entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(exoBoost);
		}
		if (entity.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getModifier(uuid) != null) {
			entity.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).removeModifier(exoBoost);
		}
		if (this.prevStep.containsKey(Integer.valueOf(entity.getEntityId()))) {
			entity.stepHeight = ((Float)this.prevStep.get(Integer.valueOf(entity.getEntityId()))).floatValue();
	        this.prevStep.remove(Integer.valueOf(entity.getEntityId()));
		}
	}
	
	private void removeBadExoBoost(EntityLivingBase entity) {
		if (entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(uuid2) != null) {
			entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(exoBoostBad);
		}
		if (entity.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getModifier(uuid2) != null) {
			entity.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).removeModifier(exoBoostBad);
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
		if (Loader.isModLoaded("AWWayofTime")) {
			BloodMagicIntegration.clickLeft(event);
		}
		if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
			Minecraft mc = Minecraft.getMinecraft();
			if(mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() == SteamcraftItems.book) {
				event.setCanceled(true);
			}
		}

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
