package eiteam.esteemedinnovation.workshop;

import eiteam.esteemedinnovation.boiler.BlockBoiler;
import eiteam.esteemedinnovation.heater.BlockSteamHeater;
import eiteam.esteemedinnovation.init.blocks.PipeBlocks;
import eiteam.esteemedinnovation.init.blocks.SteamMachineryBlocks;
import eiteam.esteemedinnovation.init.blocks.SteamNetworkBlocks;
import eiteam.esteemedinnovation.init.items.FoodItems;
import eiteam.esteemedinnovation.boiler.TileEntityBoiler;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Random;

public class ComponentSteamWorkshop extends StructureVillagePieces.Village {
    private int averageGroundLevel = -1;
    private static final IBlockState COBBLESTONE_STATE = Blocks.COBBLESTONE.getDefaultState();
    private static final IBlockState FENCE_STATE = Blocks.OAK_FENCE.getDefaultState();
    private static final IBlockState PLANK_STATE = Blocks.PLANKS.getDefaultState();
    private static final IBlockState STONE_STAIR_STATE = Blocks.STONE_STAIRS.getDefaultState();
    private static final IBlockState LADDER_STATE = Blocks.LADDER.getDefaultState();
    private static final IBlockState LOG_STATE = Blocks.LOG.getDefaultState();
    private static final IBlockState AIR_STATE = Blocks.AIR.getDefaultState();
    private static final IBlockState PANE_STATE = Blocks.GLASS_PANE.getDefaultState();
    private static final IBlockState PIPE_STATE = PipeBlocks.Blocks.BRASS_PIPE.getBlock().getDefaultState();
    private static final IBlockState HEATER_STATE = SteamMachineryBlocks.Blocks.STEAM_HEATER.getBlock().getDefaultState();

    public ComponentSteamWorkshop() {}

    public ComponentSteamWorkshop(StructureVillagePieces.Start villagePiece, int par2, Random par3Random, StructureBoundingBox sbb, EnumFacing coordBaseMode) {
        this();
        setCoordBaseMode(coordBaseMode);
        boundingBox = sbb;
    }

    public static ComponentSteamWorkshop buildComponent(StructureVillagePieces.Start villagePiece, List<StructureComponent> pieces, Random random, int x, int y, int z, EnumFacing facing, int p5) {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 9, 6, 6, facing);
        return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new ComponentSteamWorkshop(villagePiece, p5, random,
                structureboundingbox, facing) : null;
    }

    @Override
    public boolean addComponentParts(World world, Random random, StructureBoundingBox sbb) {
        if (averageGroundLevel < 0) {
            averageGroundLevel = getAverageGroundLevel(world, sbb);

            if (averageGroundLevel < 0) {
                return true;
            }

            boundingBox.offset(0, averageGroundLevel - boundingBox.maxY + 5, 0);
        }

        fillWithBlocks(world, sbb, 0, 0, 0, 8, 0, 5, COBBLESTONE_STATE, COBBLESTONE_STATE, false); // Base
        fillWithBlocks(world, sbb, 0, 5, 0, 8, 5, 5, FENCE_STATE, FENCE_STATE, false);
        fillWithBlocks(world, sbb, 1, 0, 1, 7, 0, 4, PLANK_STATE, PLANK_STATE, false);

        Material fromPos = getBlockStateFromPos(world, 4, 0, -1, sbb).getMaterial();
        Material fromPos1 = getBlockStateFromPos(world, 1, -1, -1, sbb).getMaterial();

        if (fromPos == Material.AIR) {
            if (fromPos1 == Material.AIR) {
                setBlockState(world, LADDER_STATE.withProperty(BlockLadder.FACING, EnumFacing.SOUTH), 4, 0, -1, sbb);
                int f = 0;
                while (getBlockStateFromPos(world, 4, -1 - f, -1, sbb).getMaterial() == Material.AIR && f < 10) {
                    setBlockState(world, LADDER_STATE.withProperty(BlockLadder.FACING, EnumFacing.SOUTH), 4, -1 - f, -1, sbb);
                    f++;
                }
            } else {
                setBlockState(world, STONE_STAIR_STATE.withProperty(BlockStairs.FACING, EnumFacing.NORTH), 4, 0, -1, sbb);
            }
        }

        // this.fillWithBlocks(world, sbb, 0, 5, 0, 6, 5, 6, Blocks.log, Blocks.log, false);

        fillWithBlocks(world, sbb, 0, 1, 0, 0, 4, 0, LOG_STATE, LOG_STATE, false); // Edges
        fillWithBlocks(world, sbb, 0, 1, 5, 0, 4, 5, LOG_STATE, LOG_STATE, false);
        fillWithBlocks(world, sbb, 8, 1, 0, 8, 4, 0, LOG_STATE, LOG_STATE, false);
        fillWithBlocks(world, sbb, 8, 1, 5, 8, 4, 5, LOG_STATE, LOG_STATE, false);

        fillWithBlocks(world, sbb, 0, 1, 1, 0, 4, 4, PLANK_STATE, PLANK_STATE, false); // Edges
        fillWithBlocks(world, sbb, 8, 1, 1, 8, 4, 4, PLANK_STATE, PLANK_STATE, false);
        fillWithBlocks(world, sbb, 1, 1, 0, 7, 4, 0, PLANK_STATE, PLANK_STATE, false);
        fillWithBlocks(world, sbb, 1, 1, 5, 7, 4, 5, PLANK_STATE, PLANK_STATE, false);

        fillWithBlocks(world, sbb, 0, 4, 1, 0, 4, 4, LOG_STATE, LOG_STATE, false); // Edges
        fillWithBlocks(world, sbb, 8, 4, 1, 8, 4, 4, LOG_STATE, LOG_STATE, false);
        fillWithBlocks(world, sbb, 1, 4, 0, 7, 4, 0, LOG_STATE, LOG_STATE, false);
        fillWithBlocks(world, sbb, 1, 4, 5, 7, 4, 5, LOG_STATE, LOG_STATE, false);

        fillWithBlocks(world, sbb, 1, 1, 1, 7, 5, 4, AIR_STATE, AIR_STATE, false);
        fillWithBlocks(world, sbb, 1, 4, 1, 7, 4, 4, PLANK_STATE, PLANK_STATE, false);
        // world, blockID, metadata, x, y, z, bounds

        setBlockState(world, PANE_STATE, 2, 2, 0, sbb);
        //  this.placeBlockAtCurrentPosition(world, Blocks.planks, 0, 2, 2, 0, sbb);
        func_189927_a(world, sbb, random, 4, 1, 0, getCoordBaseMode());
        // this.placeBlockAtCurrentPosition(world, Blocks.planks, 0, 4, 2, 0, sbb);
        setBlockState(world, PANE_STATE, 6, 2, 0, sbb);
        setBlockState(world, PANE_STATE, 6, 2, 0, sbb);

        setBlockState(world, PANE_STATE, 2, 2, 5, sbb);
        setBlockState(world, PANE_STATE, 3, 2, 5, sbb);
        setBlockState(world, PANE_STATE, 5, 2, 5, sbb);
        setBlockState(world, PANE_STATE, 6, 2, 5, sbb);

        setBlockState(world, PANE_STATE, 0, 2, 2, sbb);
        setBlockState(world, PANE_STATE, 0, 2, 3, sbb);

        setBlockState(world, PANE_STATE, 8, 2, 2, sbb);
        setBlockState(world, PANE_STATE, 8, 2, 3, sbb);

        setBlockState(world, LADDER_STATE.withProperty(BlockLadder.FACING, EnumFacing.SOUTH), 7, 1, 4, sbb);
        setBlockState(world, LADDER_STATE.withProperty(BlockLadder.FACING, EnumFacing.SOUTH), 7, 2, 4, sbb);
        setBlockState(world, LADDER_STATE.withProperty(BlockLadder.FACING, EnumFacing.SOUTH), 7, 3, 4, sbb);
        setBlockState(world, LADDER_STATE.withProperty(BlockLadder.FACING, EnumFacing.SOUTH), 7, 4, 4, sbb);

        setBlockState(world, Blocks.STICKY_PISTON.getDefaultState(), 7, 1, 1, sbb);

        setBlockState(world, SteamNetworkBlocks.Blocks.BOILER.getBlock().getDefaultState().withProperty(BlockBoiler.FACING, EnumFacing.NORTH), 1, 1, 4, sbb);
        int x = getXWithOffset(1, 4);
        int y = getYWithOffset(1);
        int z = getZWithOffset(1, 4);
        populateBoiler(world, x, y, z);

        setBlockState(world, PIPE_STATE, 1, 2, 4, sbb);
        setBlockState(world, PIPE_STATE, 2, 2, 4, sbb);
        setBlockState(world, PIPE_STATE, 3, 2, 4, sbb);
        setBlockState(world, PIPE_STATE, 4, 2, 4, sbb);

        setBlockState(world, HEATER_STATE.withProperty(BlockSteamHeater.FACING, EnumFacing.WEST), 4, 1, 4, sbb);
        setBlockState(world, HEATER_STATE.withProperty(BlockSteamHeater.FACING, EnumFacing.EAST), 2, 1, 4, sbb);

        setBlockState(world, Blocks.FURNACE.getDefaultState().withProperty(BlockFurnace.FACING, EnumFacing.NORTH), 3, 1, 4, sbb);
        x = getXWithOffset(3, 4);
        y = getYWithOffset(1);
        z = getZWithOffset(3, 4);
        populateFurnace(world, x, y, z);

        for (int l = 0; l < 6; ++l) {
            for (int i1 = 0; i1 < 9; ++i1) {
                clearCurrentPositionBlocksUpwards(world, i1, 9, l, sbb);
                replaceAirAndLiquidDownwards(world, COBBLESTONE_STATE, i1, -1, l, sbb);
            }
        }
        spawnVillagers(world, sbb, 3, 1, 3, 1);

        return true;
    }

    private void populateFurnace(World world, int x, int y, int z) {
        Random rand = new Random(world.getSeed() + x + y + z);
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityFurnace) {
            TileEntityFurnace furnace = (TileEntityFurnace) tile;
            ItemStack[] possibleLoot = {
              new ItemStack(FoodItems.Items.STEAMED_BEEF.getItem()),
              new ItemStack(FoodItems.Items.STEAMED_PORKCHOP.getItem()),
              new ItemStack(FoodItems.Items.STEAMED_CHICKEN.getItem())
            };
            ItemStack loot = possibleLoot[rand.nextInt(possibleLoot.length)];
            loot.stackSize = rand.nextInt(3) + 1;
            furnace.setInventorySlotContents(2, loot);
        }
    }

    private void populateBoiler(World world, int x, int y, int z) {
        Random rand = new Random(world.getSeed() + x + y + z);
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityBoiler) {
            TileEntityBoiler boiler = (TileEntityBoiler) tile;
            boiler.refresh();
            boiler.insertSteam(1000 + rand.nextInt(3000), EnumFacing.UP);
            boiler.myTank.setFluid(new FluidStack(FluidRegistry.WATER, 2000 + rand.nextInt(3000)));
        }
    }
/* TODO
    @Override
    protected int chooseProfession(int villagersSpawned, int currentProfession) {
        return Config.villagerId;
    }
    */
}
