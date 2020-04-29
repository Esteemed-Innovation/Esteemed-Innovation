package eiteam.esteemedinnovation.workshop;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.StructureVillagePieces.PieceWeight;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.List;
import java.util.Random;

public class SteamWorkshopCreationHandler implements VillagerRegistry.IVillageCreationHandler {

    @Override
    public PieceWeight getVillagePieceWeight(Random random, int i) {
        return new StructureVillagePieces.PieceWeight(ComponentSteamWorkshop.class, SteamWorkshopModule.workshopWeight, SteamWorkshopModule.workshopLimit);
    }

    @Override
    public Class<?> getComponentClass() {
        return ComponentSteamWorkshop.class;
    }

    @Override
    public StructureVillagePieces.Village buildComponent(PieceWeight villagePiece, Start startPiece, List<StructureComponent> pieces, Random random, int p1, int p2, int p3, EnumFacing facing, int p5) {
        return ComponentSteamWorkshop.buildComponent(startPiece, pieces, random, p1, p2, p3, facing, p5);
    }
}
