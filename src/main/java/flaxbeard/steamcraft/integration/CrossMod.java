package flaxbeard.steamcraft.integration;

import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.integration.ic2.IndustrialCraftIntegration;
import flaxbeard.steamcraft.integration.natura.NaturaIntegration;
import flaxbeard.steamcraft.integration.thaumcraft.ThaumcraftIntegration;
import flaxbeard.steamcraft.world.PoorOreGeneratorZinc;

import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Loader;

public class CrossMod {
	public static final boolean BAUBLES = Loader.isModLoaded("Baubles") &&
	  Config.enableBaublesIntegration;

	public static final boolean BLOOD_MAGIC = Loader.isModLoaded("AWWayofTime") &&
	  Config.enableBloodMagicIntegration;

	public static final boolean BOTANIA = Loader.isModLoaded("Botania") &&
	  Config.enableBotaniaIntegration;

	public static final boolean ENCHIRIDION = Loader.isModLoaded("Enchridion") &&
	  Config.enableEnchiridionIntegration;

	public static final boolean ENDER_IO = Loader.isModLoaded("EnderIO") &&
	  Config.enableEnderIOIntegration;

	public static final boolean INDUSTRIALCRAFT = Loader.isModLoaded("IC2") &&
	  Config.enableIC2Integration;

	public static final boolean NATURA = Loader.isModLoaded("Natura") &&
	  Config.enableNaturaIntegration;

	public static final boolean RAILCRAFT = Loader.isModLoaded("Railcraft") &&
	  Config.enableRailcraftIntegration;

	public static final boolean THAUMCRAFT = Loader.isModLoaded("Thaumcraft") &&
	  Config.enableThaumcraftIntegration;

	public static final boolean THERMAL_FOUNDATION = Loader.isModLoaded("ThermalFoundation") &&
	  Config.enableThermalFoundationIntegration;

	public static final boolean TINKERS_CONSTRUCT = Loader.isModLoaded("TConstruct") &&
	  Config.enableTinkersConstruct;

	public static final boolean TWILIGHT_FOREST = Loader.isModLoaded("TwilightForest") &&
	  Config.enableTwilightForestIntegration;

	public static final EventType EVENT_TYPE = (EventType) EnumHelper.addEnum(EventType.class, "FSP_POOR_ZINC", new Class[0], new Object[0]);

	public static void init() {
		if (RAILCRAFT && Config.genPoorOre) {
			MinecraftForge.ORE_GEN_BUS.register(new PoorOreGeneratorZinc(EVENT_TYPE, 8, 70, 3, 29));
		}
	}

	public static void postInit() {
		if (BOTANIA) {
			BotaniaIntegration.postInit();
		}

		if (BLOOD_MAGIC) {
			BloodMagicIntegration.postInit();
		}

		if (ENDER_IO) {
			EnderIOIntegration.postInit();
		}

		if (INDUSTRIALCRAFT) {
			IndustrialCraftIntegration.postInit();
		}

		if (NATURA) {
			NaturaIntegration.postInit();
		}

		if (THAUMCRAFT) {
			ThaumcraftIntegration.postInit();
		}

		if (THERMAL_FOUNDATION) {
			ThermalFoundationIntegration.postInit();
		}

		if (TWILIGHT_FOREST) {
			TwilightForestIntegration.postInit();
		}

		MiscIntegration.postInit();
	}
}
