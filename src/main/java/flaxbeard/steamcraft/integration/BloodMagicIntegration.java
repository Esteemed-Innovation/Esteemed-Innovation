package flaxbeard.steamcraft.integration;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.oredict.ShapedOreRecipe;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.common.items.EnergyBattery;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import cpw.mods.fml.relauncher.ReflectionHelper;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.book.BookRecipeRegistry;
import flaxbeard.steamcraft.api.exosuit.ExosuitPlate;
import flaxbeard.steamcraft.api.exosuit.UtilPlates;
import flaxbeard.steamcraft.item.ItemExosuitArmor;

public class BloodMagicIntegration {

	public static void clickLeft(PlayerInteractEvent event) {
		if (!event.world.isRemote && (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK)) {
			if (event.entityPlayer.getHeldItem() != null && event.entityPlayer.getHeldItem().getItem() instanceof EnergyBattery) {
				LifeEssenceCap data = getData(event.entityPlayer.getCommandSenderName());
				EnergyBattery bat = (EnergyBattery) event.entityPlayer.getHeldItem().getItem();
				int cap = ReflectionHelper.getPrivateValue(EnergyBattery.class, bat, 0);
				if (cap > data.cap) {
					data.cap = cap;
					data.markDirty();
				}
			}
		}
	}
	
	public static LifeEssenceCap getData(String name) {
		World world = MinecraftServer.getServer().worldServers[0];
		LifeEssenceCap data = (LifeEssenceCap)world.loadItemData(LifeEssenceCap.class, name+"cap");
		if (data == null)
		{
			data = new LifeEssenceCap(name+"cap");
	        world.setItemData(name+"cap", data);
		}
		return data;
	}
	
	public static void addBloodMagicStuff() {
		if (Config.enableSadistPlate) {
			SteamcraftRegistry.addExosuitPlate(new ExosuitPlate("Sadist",new ItemStack(SteamcraftItems.exosuitPlate,1,10),"Sadist","Sadist","steamcraft.plate.sadist"));
			BookRecipeRegistry.addRecipe("exoSadist",new ShapedOreRecipe(new ItemStack(SteamcraftItems.exosuitPlate,1,10), " s ","sbs"," s ",
			        's', ModItems.reinforcedSlate, 'b', ModBlocks.runeOfSelfSacrifice));
		}
	}

	public static void handleAttack(LivingHurtEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			int bmPlates = 0;
			for (int i = 0; i < player.inventory.armorInventory.length; i++) {
				ItemStack armor = player.inventory.armorInventory[i];
				if (armor != null && armor.getItem() instanceof ItemExosuitArmor) {
					ItemExosuitArmor armorItem = (ItemExosuitArmor) armor.getItem();
					if (armorItem.hasPlates(armor) && UtilPlates.getPlate(armor.stackTagCompound.getString("plate")).getIdentifier() == "Sadist") {
						bmPlates++;
					}
				}
			}
			if (bmPlates > 0) {
				int lp = (int)event.ammount * 12 * bmPlates;
				EnergyItems.addEssenceToMaximum(player.getCommandSenderName(), lp, getData(player.getCommandSenderName()).cap);
			}
		}
	}
}
