package flaxbeard.steamcraft.common.integration;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.book.BookRecipeRegistry;
import flaxbeard.steamcraft.api.exosuit.ExosuitPlate;
import flaxbeard.steamcraft.api.exosuit.UtilPlates;
import flaxbeard.steamcraft.common.item.exosuit.ItemExosuitArmor;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.lang.reflect.Method;

public class BloodMagicIntegration {

    public static Item reinforcedSlate;
    public static Block runeOfSelfSacrifice;

    @SuppressWarnings("unchecked")
    public static void clickLeft(PlayerInteractEvent event) {
        if (!event.world.isRemote && (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK)) {
            try {
                Class energyBatteryClass = Class.forName("WayofTime.alchemicalWizardry.common.items.EnergyBattery");
                if (event.entityPlayer.getHeldItem() != null &&  energyBatteryClass.isInstance(event.entityPlayer.getHeldItem().getItem())) {
                    LifeEssenceCap data = getData(event.entityPlayer.getCommandSenderName());
                    int cap = ((Integer)ReflectionHelper.getPrivateValue(energyBatteryClass, event.entityPlayer.getHeldItem().getItem(), 0)).intValue();
                    if (cap > data.cap) {
                        data.cap = cap;
                        data.markDirty();
                    }
                }
            } catch (ClassNotFoundException e) {
                // Dump, shouldn't ever happen
            }
        }
    }

    public static LifeEssenceCap getData(String name) {
        World world = MinecraftServer.getServer().worldServers[0];
        LifeEssenceCap data = (LifeEssenceCap) world.loadItemData(LifeEssenceCap.class, name + "cap");
        if (data == null) {
            data = new LifeEssenceCap(name + "cap");
            world.setItemData(name + "cap", data);
        }
        return data;
    }

    public static void grabItems() {
        reinforcedSlate = GameRegistry.findItem("AWWayofTime", "reinforcedSlate");
        runeOfSelfSacrifice = GameRegistry.findBlock("AWWayofTime", "runeOfSelfSacrifice");
    }

    public static void addBloodMagicStuff() {
        if (Config.enableSadistPlate) {
            SteamcraftRegistry.addExosuitPlate(new ExosuitPlate("Sadist", new ItemStack(SteamcraftItems.exosuitPlate, 1, 10), "Sadist", "Sadist", "steamcraft.plate.sadist"));
            BookRecipeRegistry.addRecipe("exoSadist", new ShapedOreRecipe(new ItemStack(SteamcraftItems.exosuitPlate, 1, 10), " s ", "sbs", " s ",
                    's', reinforcedSlate, 'b', runeOfSelfSacrifice));
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
                try {
                    Class energyItemsClass = Class.forName("WayofTime.alchemicalWizardry.common.items.EnergyItems");
                    int lp = (int) event.ammount * 12 * bmPlates;
                    Method addEssence = energyItemsClass.getMethod("addEssenceToMaximum", String.class, int.class, int.class);
                    addEssence.invoke(energyItemsClass, player.getCommandSenderName(), lp, getData(player.getCommandSenderName()).cap);
                } catch (Exception ex) {
                    // Dump, shouldn't every happen
                }
            }
        }
    }
}
