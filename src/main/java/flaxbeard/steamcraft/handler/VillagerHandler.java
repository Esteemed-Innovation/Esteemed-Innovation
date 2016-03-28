package flaxbeard.steamcraft.handler;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.entity.ExtendedPropertiesVillager;
import flaxbeard.steamcraft.item.ItemExosuitArmor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.lang.reflect.Field;

public class VillagerHandler {
	public static boolean lastViewVillagerGui = false;
	private static Field lastBuyingPlayerField = null;
	private static Field timeUntilResetField = null;
	private static Field merchantField = null;
	private static Field buyingListField = null;

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void updateVillagersClientside(GuiScreenEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		if (merchantField != null && event.gui instanceof GuiMerchant && !lastViewVillagerGui) {
			GuiMerchant gui = (GuiMerchant) event.gui;

			ItemStack hat = mc.thePlayer.inventory.armorInventory[3];
			boolean hasHat = hat != null;

			if (hasHat) {
				Item hatItem = hat.getItem();
				boolean isTophat = hatItem == SteamcraftItems.tophat;


				if (tophatlike(hat)) {
					IMerchant merch = gui.func_147035_g();
					MerchantRecipeList recipeList = merch.getRecipes(mc.thePlayer);
					if (recipeList != null) {
						for (Object obj : recipeList) {
							MerchantRecipe recipe = (MerchantRecipe) obj;
							if (recipe.getItemToSell().stackSize > 1 && recipe.getItemToSell().stackSize != MathHelper.floor_float(recipe.getItemToSell().stackSize * 1.25F)) {
								recipe.getItemToSell().stackSize = MathHelper.floor_float(recipe.getItemToSell().stackSize * 1.25F);
							} else if (recipe.getItemToBuy().stackSize > 1 && recipe.getItemToBuy().stackSize != MathHelper.ceiling_float_int(recipe.getItemToBuy().stackSize / 1.25F)) {
								recipe.getItemToBuy().stackSize = MathHelper.ceiling_float_int(recipe.getItemToBuy().stackSize / 1.25F);
							} else if (recipe.getSecondItemToBuy() != null && recipe.getSecondItemToBuy().stackSize > 1 && recipe.getSecondItemToBuy().stackSize != MathHelper.ceiling_float_int(recipe.getSecondItemToBuy().stackSize / 1.25F)) {
								recipe.getSecondItemToBuy().stackSize = MathHelper.ceiling_float_int(recipe.getSecondItemToBuy().stackSize / 1.25F);
							}
						}
						lastViewVillagerGui = true;
					}
					merch.setRecipes(recipeList);
					try {
						merchantField.set(gui, merch);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void updateVillagers(LivingEvent.LivingUpdateEvent event) {
		if (event.entityLiving instanceof EntityVillager && timeUntilResetField != null &&
			lastBuyingPlayerField != null) {
			EntityVillager villager = (EntityVillager) event.entityLiving;
			Integer timeUntilReset = null;
			String lastBuyingPlayer = null;
			try {
				timeUntilReset = timeUntilResetField.getInt(villager);
				lastBuyingPlayer = (String) lastBuyingPlayerField.get(villager);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			if (!villager.isTrading() && timeUntilReset != null && timeUntilReset == 39 &&
				lastBuyingPlayer != null) {
				EntityPlayer player = villager.worldObj.getPlayerEntityByName(lastBuyingPlayer);
				if (player != null) {
					ItemStack hat = getHat(player);
					if (isTophat(hat)) {
						if (!hat.hasTagCompound()) {
							hat.setTagCompound(new NBTTagCompound());
						}
						if (!hat.stackTagCompound.hasKey("level")) {
							hat.stackTagCompound.setInteger("level", 0);
						}
						int level = hat.stackTagCompound.getInteger("level");
						level++;
						hat.stackTagCompound.setInteger("level", level);
					} else if (hasTophatUpgrade(hat)) {
						ItemStack exohead = ((ItemExosuitArmor) hat.getItem()).getStackInSlot(hat, 3);
						if (!exohead.hasTagCompound()) {
							exohead.setTagCompound(new NBTTagCompound());
						}
						if (!exohead.stackTagCompound.hasKey("level")) {
							exohead.stackTagCompound.setInteger("level", 0);
						}
						int level = exohead.stackTagCompound.getInteger("level");
						level++;
						exohead.stackTagCompound.setInteger("level", level);
						((ItemExosuitArmor) hat.getItem()).setInventorySlotContents(hat, 3, exohead);
					}
				}
			}
		}
		if (event.entityLiving instanceof EntityVillager && !event.entityLiving.worldObj.isRemote &&
			buyingListField != null) {
			EntityVillager villager = (EntityVillager) event.entityLiving;
			ExtendedPropertiesVillager nbt = (ExtendedPropertiesVillager)
				villager.getExtendedProperties(Steamcraft.VILLAGER_PROPERTY_ID);
			if (nbt.lastHadCustomer == null) {
				nbt.lastHadCustomer = false;
			}
			boolean hasCustomer = false;
			EntityPlayer customer = villager.getCustomer();
			ItemStack hat = getHat(customer);

			if (customer != null && tophatlike(hat)) {
				hasCustomer = true;

				if (!nbt.lastHadCustomer) {
					MerchantRecipeList recipeList = villager.getRecipes(customer);
					for (Object obj : recipeList) {
						MerchantRecipe recipe = (MerchantRecipe) obj;
						if (recipe.getItemToSell().stackSize > 1 && recipe.getItemToSell().stackSize != MathHelper.floor_float(recipe.getItemToSell().stackSize * 1.25F)) {
							recipe.getItemToSell().stackSize = MathHelper.floor_float(recipe.getItemToSell().stackSize * 1.25F);
						} else if (recipe.getItemToBuy().stackSize > 1 && recipe.getItemToBuy().stackSize != MathHelper.ceiling_float_int(recipe.getItemToBuy().stackSize / 1.25F)) {
							recipe.getItemToBuy().stackSize = MathHelper.ceiling_float_int(recipe.getItemToBuy().stackSize / 1.25F);
						} else if (recipe.getSecondItemToBuy() != null && recipe.getSecondItemToBuy().stackSize > 1 && recipe.getSecondItemToBuy().stackSize != MathHelper.ceiling_float_int(recipe.getSecondItemToBuy().stackSize / 1.25F)) {
							recipe.getSecondItemToBuy().stackSize = MathHelper.ceiling_float_int(recipe.getSecondItemToBuy().stackSize / 1.25F);
						}
					}

					try {
						buyingListField.set(villager, recipeList);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					//customer.closeScreen();
					//customer.displayGUIMerchant(villager, villager.getCustomNameTag());
				}
			}

			if (!hasCustomer && nbt.lastHadCustomer) {
				// We need to do reflection because we do not have the customer in this case.
				MerchantRecipeList recipeList = null;
				try {
					recipeList = (MerchantRecipeList) buyingListField.get(villager);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				if (recipeList != null) {
					for (Object obj : recipeList) {
						MerchantRecipe recipe = (MerchantRecipe) obj;
						if (recipe.getItemToSell().stackSize > 1 && recipe.getItemToSell().stackSize != MathHelper.ceiling_float_int(recipe.getItemToSell().stackSize / 1.25F)) {
							recipe.getItemToSell().stackSize = MathHelper.ceiling_float_int(recipe.getItemToSell().stackSize / 1.25F);
						} else if (recipe.getItemToBuy().stackSize > 1 && recipe.getItemToBuy().stackSize != MathHelper.floor_float(recipe.getItemToBuy().stackSize * 1.25F)) {
							recipe.getItemToBuy().stackSize = MathHelper.floor_float(recipe.getItemToBuy().stackSize * 1.25F);
						} else if (recipe.getSecondItemToBuy() != null && recipe.getSecondItemToBuy().stackSize > 1 && recipe.getSecondItemToBuy().stackSize != MathHelper.floor_float(recipe.getSecondItemToBuy().stackSize * 1.25F)) {
							recipe.getSecondItemToBuy().stackSize = MathHelper.floor_float(recipe.getSecondItemToBuy().stackSize * 1.25F);
						}
					}
				}
				try {
					buyingListField.set(villager, recipeList);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}

			nbt.lastHadCustomer = hasCustomer;
		}
	}

	private boolean tophatlike(ItemStack hat) {
		return isTophat(hat) || hasTophatUpgrade(hat);
	}

	private boolean hasTophatUpgrade(ItemStack hat) {
		boolean isExoHead = hat != null && hat.getItem() == SteamcraftItems.exoArmorHead;
		return isExoHead && ((ItemExosuitArmor) hat.getItem()).hasUpgrade(hat, SteamcraftItems.tophat);
	}

	private boolean isTophat(ItemStack hat) {
		return hat != null && (hat.getItem() == SteamcraftItems.tophat);
	}

	private boolean hasHat(EntityPlayer player) {
		return getHat(player) != null;
	}

	private ItemStack getHat(EntityPlayer player) {
		return player.inventory.armorInventory[3];
	}

	private static Field getField(String fieldName, String obfName, Class clazz) {
		Field field = null;
		try {
			field = clazz.getDeclaredField(obfName);
		} catch (NoSuchFieldException e) {
			FMLLog.warning("[FSP] Unable to find field " + fieldName + " with its obfuscated " +
				"name. Trying to find it by its name " + fieldName);
			try {
				field = clazz.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e1) {
				e1.printStackTrace();
				String fields = "";
				for (Field field1 : clazz.getDeclaredFields()) {
					if (fields.isEmpty()) {
						fields += field1.getName();
					} else {
						fields += ", " + field1.getName();
					}
				}
				FMLLog.warning("Unable to find " + fieldName + " field in " + clazz.getName() +
					"class. Available fields are: " + fields + ". Things are not going to work right.");
			}
		}
		return field;
	}

	static {
		FMLLog.info("[FSP] Getting some fields through reflection.");
		lastBuyingPlayerField = getField("lastBuyingPlayer", "field_82189_bL", EntityVillager.class);
		timeUntilResetField = getField("timeUntilReset", "field_70961_j", EntityVillager.class);
		buyingListField = getField("buyingList", "field_70963_i", EntityVillager.class);

		if (lastBuyingPlayerField != null) {
			lastBuyingPlayerField.setAccessible(true);
		}

		if (timeUntilResetField != null) {
			timeUntilResetField.setAccessible(true);
		}

		if (buyingListField != null) {
			buyingListField.setAccessible(true);
		}

		try {
			merchantField = getField("field_147037_w", "field_147037_w", GuiMerchant.class);
			if (merchantField != null) {
				merchantField.setAccessible(true);
			}
		} catch (NoClassDefFoundError ignore) {
			FMLLog.warning("[FSP] GuiMerchant class not found. You are probably a server.");
		}
	}
}
