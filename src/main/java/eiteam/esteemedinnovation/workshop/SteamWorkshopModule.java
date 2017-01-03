package eiteam.esteemedinnovation.workshop;

import eiteam.esteemedinnovation.api.Constants;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class SteamWorkshopModule extends ContentModule {
    public static VillagerRegistry.VillagerProfession STEAM_ENGINEER_PROFESSION;
    public static VillagerRegistry.VillagerCareer STEAM_ENGINEER_CAREER;

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
          Constants.EI_MODID + ":textures/models/villager.png");
        STEAM_ENGINEER_CAREER = new SteamEngineerCareer();
        VillagerRegistry.instance().register(STEAM_ENGINEER_PROFESSION);
        VillagerRegistry.instance().registerVillageCreationHandler(new SteamWorkshopCreationHandler());
    }
}
