package eiteam.esteemedinnovation.metalcasting.hut;

import eiteam.esteemedinnovation.metalcasting.MetalcastingModule;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.storage.loot.LootTableList;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class MetalcastingHutComponent extends StructureVillagePieces.Village {
    private static final IBlockState SANDSTONE_STATE = Blocks.SANDSTONE.getDefaultState();
    private static final IBlockState SPRUCE_STATE = Blocks.LOG.getDefaultState()
      .withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
    private static final IBlockState CLAY_STATE = Blocks.STAINED_HARDENED_CLAY.getDefaultState()
      .withProperty(BlockColored.COLOR, EnumDyeColor.RED);
    private static final IBlockState SLAB_STATE = Blocks.WOODEN_SLAB.getDefaultState()
      .withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP)
      .withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA);
    private static final IBlockState BUTTON_STATE = Blocks.WOODEN_BUTTON.getDefaultState();
    private static final IBlockState FENCE_STATE = Blocks.OAK_FENCE.getDefaultState();
    private static final IBlockState BRICKS_STATE = Blocks.BRICK_BLOCK.getDefaultState();
    private static final IBlockState BRICKS_STAIRS_STATE = Blocks.BRICK_STAIRS.getDefaultState();
    private static final IBlockState AIR_STATE = Blocks.AIR.getDefaultState();
    private static final IBlockState LAVA_STATE = Blocks.LAVA.getDefaultState();
    private static final IBlockState LADDER_STATE = Blocks.LADDER.getDefaultState().withRotation(Rotation.CLOCKWISE_90);
    private static final IBlockState CRUCIBLE_STATE = MetalcastingModule.CRUCIBLE.getDefaultState();
    private static final IBlockState MOLD_STATE = MetalcastingModule.MOLD.getDefaultState();
    private static final IBlockState CARVING_TABLE_STATE = MetalcastingModule.CARVING_TABLE.getDefaultState();

    public MetalcastingHutComponent() {}

    MetalcastingHutComponent(StructureVillagePieces.Start villagePiece, StructureBoundingBox sbb, EnumFacing direction) {
        setCoordBaseMode(direction);
        boundingBox = sbb;
        startPiece = villagePiece;
    }

    @ParametersAreNonnullByDefault
    @Override
    public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
        averageGroundLvl = getAverageGroundLevel(world, sbb);
        if (averageGroundLvl < 0) {
            return true;
        }
        boundingBox.offset(0, averageGroundLvl - boundingBox.maxY + 5, 0);

        EnumFacing dir = getCoordBaseMode();
        if (dir == null) {
            return true;
        }

        generateFloor(world, sbb);
        generateFrontWall(world, rand, sbb, dir);
        generateLogicalRightWall(world, sbb);
        generateLogicalLeftWall(world, sbb);
        generateBackWall(world, sbb);
        generateFrontCorners(world, sbb);
        generateBackCorners(world, sbb);
        generateCeiling(world, sbb);
        generateChimney(world, sbb);
        generateFurniture(world, rand, sbb);
        return true;
    }

    private void generateFloor(World world, StructureBoundingBox sbb) {
        fillWithBlocks(world, sbb, 0, -1, 0, 6, -1, 6, SANDSTONE_STATE, SANDSTONE_STATE, false);
        setBlockState(world, AIR_STATE, 5, -1, 5, sbb);
        setBlockState(world, SANDSTONE_STATE, 5, -2, 5, sbb);
        setBlockState(world, LAVA_STATE, 5, -1, 5, sbb);
    }

    private void generateFrontWall(World world, Random rand, StructureBoundingBox sbb, EnumFacing dir) {
        fillWithBlocks(world, sbb, 1, 0, 0, 5, 3, 0, CLAY_STATE, CLAY_STATE, false);
        setBlockState(world, AIR_STATE, 4, 0, 1, sbb);
        setBlockState(world, AIR_STATE, 4, 1, 1, sbb);
        createVillageDoor(world, sbb, rand, 3, 0, 0, dir);
        setBlockState(world, BUTTON_STATE.withRotation(Rotation.CLOCKWISE_180), 1, 2, -1, sbb);
        setBlockState(world, BUTTON_STATE.withRotation(Rotation.CLOCKWISE_180), 3, 2, -1, sbb);
        setBlockState(world, BUTTON_STATE.withRotation(Rotation.CLOCKWISE_180), 5, 2, -1, sbb);
        fillWithBlocks(world, sbb, 1, 4, 0, 5, 4, 0, FENCE_STATE, FENCE_STATE, false);
    }

    private void generateLogicalLeftWall(World world, StructureBoundingBox sbb) {
        fillWithBlocks(world, sbb, 0, 0, 1, 0, 3, 5, CLAY_STATE, CLAY_STATE, false);
        setBlockState(world, BUTTON_STATE.withRotation(Rotation.COUNTERCLOCKWISE_90), -1, 2, 1, sbb);
        setBlockState(world, BUTTON_STATE.withRotation(Rotation.COUNTERCLOCKWISE_90), -1, 2, 5, sbb);
        setBlockState(world, FENCE_STATE, 0, 1, 3, sbb);
        setBlockState(world, FENCE_STATE, 0, 2, 3, sbb);
        fillWithBlocks(world, sbb, 0, 4, 1, 0, 4, 5, FENCE_STATE, FENCE_STATE, false);
    }

    private void generateLogicalRightWall(World world, StructureBoundingBox sbb) {
        fillWithBlocks(world, sbb, 6, 0, 1, 6, 3, 5, CLAY_STATE, CLAY_STATE, false);
        setBlockState(world, BUTTON_STATE.withRotation(Rotation.CLOCKWISE_90), 7, 2, 2, sbb);
        setBlockState(world, FENCE_STATE, 6, 1, 3, sbb);
        setBlockState(world, FENCE_STATE, 6, 2, 3, sbb);
        fillWithBlocks(world, sbb, 6, 4, 1, 6, 4, 5, FENCE_STATE, FENCE_STATE, false);
    }

    private void generateBackWall(World world, StructureBoundingBox sbb) {
        fillWithBlocks(world, sbb, 1, 0, 6, 5, 3, 6, CLAY_STATE, CLAY_STATE, false);
        setBlockState(world, BUTTON_STATE, 2, 2, 7, sbb);
        setBlockState(world, FENCE_STATE, 3, 1, 6, sbb);
        setBlockState(world, FENCE_STATE, 3, 2, 6, sbb);
        fillWithBlocks(world, sbb, 1, 4, 6, 5, 4, 6, FENCE_STATE, FENCE_STATE, false);
    }

    private void generateFrontCorners(World world, StructureBoundingBox sbb) {
        fillWithBlocks(world, sbb, 0, 0, 0, 0, 4, 0, SPRUCE_STATE, SPRUCE_STATE, false);
        fillWithBlocks(world, sbb, 6, 0, 0, 6, 4, 0, SPRUCE_STATE, SPRUCE_STATE, false);
    }

    private void generateBackCorners(World world, StructureBoundingBox sbb) {
        fillWithBlocks(world, sbb, 0, 0, 6, 0, 4, 6, SPRUCE_STATE, SPRUCE_STATE, false);
        fillWithBlocks(world, sbb, 6, 0, 6, 6, 4, 6, SPRUCE_STATE, SPRUCE_STATE, false);
    }

    private void generateCeiling(World world, StructureBoundingBox sbb) {
        fillWithBlocks(world, sbb, 1, 3, 1, 5, 3, 5, SLAB_STATE, SLAB_STATE, false);
        fillWithBlocks(world, sbb, 1, 0, 1, 1, 3, 1, LADDER_STATE, LADDER_STATE, false);
    }

    private void generateChimney(World world, StructureBoundingBox sbb) {
        fillWithBlocks(world, sbb, 5, -1, 7, 5, 2, 7, BRICKS_STATE, BRICKS_STATE, false);
        setBlockState(world, BRICKS_STAIRS_STATE.withRotation(Rotation.CLOCKWISE_90), 5, 3, 7, sbb);

        fillWithBlocks(world, sbb, 6, -1, 8, 7, 2, 8, BRICKS_STATE, BRICKS_STATE, false);
        setBlockState(world, BRICKS_STAIRS_STATE.withRotation(Rotation.CLOCKWISE_90), 6, 3, 8, sbb);

        fillWithBlocks(world, sbb, 6, -1, 7, 6, 6, 7, BRICKS_STATE, BRICKS_STATE, false);
        setBlockState(world, BRICKS_STAIRS_STATE.withRotation(Rotation.CLOCKWISE_90), 6, 7, 7, sbb);

        fillWithBlocks(world, sbb, 7, -1, 8, 7, 6, 8, BRICKS_STATE, BRICKS_STATE, false);
        setBlockState(world, BRICKS_STAIRS_STATE.withRotation(Rotation.CLOCKWISE_180), 7, 7, 8, sbb);

        fillWithBlocks(world, sbb, 7, -1, 5, 7, 2, 5, BRICKS_STATE, BRICKS_STATE, false);
        setBlockState(world, BRICKS_STAIRS_STATE, 7, 3, 5, sbb);

        fillWithBlocks(world, sbb, 7, -1, 6, 7, 6, 6, BRICKS_STATE, BRICKS_STATE, false);
        setBlockState(world, BRICKS_STAIRS_STATE, 7, 7, 6, sbb);

        fillWithBlocks(world, sbb, 8, -1, 7, 8, 6, 7, BRICKS_STATE, BRICKS_STATE, false);
        setBlockState(world, BRICKS_STAIRS_STATE.withRotation(Rotation.COUNTERCLOCKWISE_90), 8, 7, 7, sbb);

        fillWithBlocks(world, sbb, 7, -1, 7, 7, 11, 7, BRICKS_STATE, BRICKS_STATE, false);
    }

    private void generateFurniture(World world, Random rand, StructureBoundingBox sbb) {
        setBlockState(world, CRUCIBLE_STATE.withRotation(Rotation.COUNTERCLOCKWISE_90), 5, 0, 5, sbb);
        setBlockState(world, MOLD_STATE.withRotation(Rotation.CLOCKWISE_90), 4, 0, 5, sbb);
        setBlockState(world, CARVING_TABLE_STATE, 1, 0, 5, sbb);
        generateChest(world, sbb, rand, 5, 0, 2, LootTableList.CHESTS_VILLAGE_BLACKSMITH);
        generateChest(world, sbb, rand, 1, 0, 3, MetalcastingModule.CARVING_TABLE_LOOT);
    }
}
