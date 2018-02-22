package eiteam.esteemedinnovation.workshop;

import eiteam.esteemedinnovation.api.Constants;
import eiteam.esteemedinnovation.commons.init.ConfigurableModule;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.relauncher.Side;

import static eiteam.esteemedinnovation.commons.Config.CATEGORY_WORLD_GENERATION;

public class SteamWorkshopModule extends ContentModule implements ConfigurableModule {
    public static VillagerRegistry.VillagerProfession STEAM_ENGINEER_PROFESSION;
    public static VillagerRegistry.VillagerCareer STEAM_ENGINEER_CAREER;
    static int workshopWeight;
    static int workshopLimit;

    @Override
    public void create(Side side) {
        VillagerRegistry.instance().registerVillageCreationHandler(new SteamWorkshopCreationHandler());
        MapGenStructureIO.registerStructureComponent(ComponentSteamWorkshop.class, Constants.EI_MODID + ":workshop");
    }

    @Override
    public void recipes(Side side) {
        // This deprecation is moderately useless unless we add our own engineer zombie texture.
        //noinspection deprecation
        STEAM_ENGINEER_PROFESSION = new VillagerRegistry.VillagerProfession(Constants.EI_MODID + ":steam_engineer",
          Constants.EI_MODID + ":textures/models/villager.png",
          Constants.EI_MODID + ":textures/models/zombie_villager.png");
        STEAM_ENGINEER_CAREER = new SteamEngineerCareer();
        ForgeRegistries.VILLAGER_PROFESSIONS.register(STEAM_ENGINEER_PROFESSION);
        VillagerRegistry.instance().registerVillageCreationHandler(new SteamWorkshopCreationHandler());
    }

    @Override
    public void loadConfigurationOptions(Configuration config) {
        workshopLimit = config.get(CATEGORY_WORLD_GENERATION, "Maximum number of Workshops allowed to generate per village", 1).getInt();
        workshopWeight = config.get(CATEGORY_WORLD_GENERATION, "Workshop spawn weight", 7).getInt(7);
    }

    @Override
    public boolean doesRecipeBelongTo(String configSetting) {
        return false;
    }

    @Override
    public boolean isRecipeEnabled(String configSetting) {
        return false;
    }
}
