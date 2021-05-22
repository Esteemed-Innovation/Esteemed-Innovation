package eiteam.esteemedinnovation.metalcasting.hut;

import eiteam.esteemedinnovation.metalcasting.MetalcastingModule;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.List;
import java.util.Random;

public class MetalcastingHutCreationHandler implements VillagerRegistry.IVillageCreationHandler {
    @Override
    public StructureVillagePieces.PieceWeight getVillagePieceWeight(Random random, int i) {
        return new StructureVillagePieces.PieceWeight(MetalcastingHutComponent.class, MetalcastingModule.metalcastingHutWeight, MetalcastingModule.metalcastingHutLimit);
    }

    @Override
    public Class<?> getComponentClass() {
        return MetalcastingHutComponent.class;
    }

    @Override
    public StructureVillagePieces.Village buildComponent(StructureVillagePieces.PieceWeight pieceWeight, StructureVillagePieces.Start startPiece, List<StructureComponent> pieces, Random random, int x, int y, int z, EnumFacing facing, int p5) {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 9, 6, 6, facing);
        return new MetalcastingHutComponent(startPiece, structureboundingbox, facing);
    }
}
