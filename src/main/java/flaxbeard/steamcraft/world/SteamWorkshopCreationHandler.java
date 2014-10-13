package flaxbeard.steamcraft.world;

import cpw.mods.fml.common.registry.VillagerRegistry;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.StructureVillagePieces.PieceWeight;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;

import java.util.List;
import java.util.Random;

public class SteamWorkshopCreationHandler implements VillagerRegistry.IVillageCreationHandler {

    @Override
    public PieceWeight getVillagePieceWeight(Random random, int i) {
        return new StructureVillagePieces.PieceWeight(ComponentSteamWorkshop.class, 4, 1);
    }

    @Override
    public Class<?> getComponentClass() {
        return ComponentSteamWorkshop.class;
    }

    @Override
    public Object buildComponent(PieceWeight villagePiece, Start startPiece,
                                 List pieces, Random random, int p1, int p2, int p3, int p4, int p5) {
        // TODO Auto-generated method stub
        return ComponentSteamWorkshop.buildComponent(startPiece, pieces, random, p1, p2, p3, p4, p5);
    }

}
