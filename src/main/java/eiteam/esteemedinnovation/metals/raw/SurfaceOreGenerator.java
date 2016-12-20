package eiteam.esteemedinnovation.metals.raw;

import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.init.blocks.MiscellaneousBlocks;
import eiteam.esteemedinnovation.init.misc.LootTablesCategory;
import eiteam.esteemedinnovation.commons.util.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class SurfaceOreGenerator implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.getDimension() == 0) {
            boolean doCopper = random.nextInt(16) == 1;
            boolean doZinc = random.nextInt(16) == 2;
            if (doCopper) {
                if (Config.genCopperOverworld) {
                    generateDepositGenerators(random, chunkX * 16, chunkZ * 16, world, BlockOreDepositGenerator.Types.COPPER);
                }
            } else if (doZinc) {
                if (Config.genZincOverworld) {
                    generateDepositGenerators(random, chunkX * 16, chunkZ * 16, world, BlockOreDepositGenerator.Types.ZINC);
                }
            }
        }
    }

    private void generateDepositGenerators(Random random, int baseX, int baseZ, World world, BlockOreDepositGenerator.Types type) {
        Block block = MiscellaneousBlocks.Blocks.ORE_DEPOSIT_BLOCK.getBlock();
        // 30% of deposits are worked out
        boolean workedOut = random.nextInt(10) <= 3;
        WorldGenSingleMinable minable = new WorldGenSingleMinable(block.getDefaultState()
          .withProperty(BlockOreDepositGenerator.VARIANT, type)
          .withProperty(BlockOreDepositGenerator.WORKED_OUT, workedOut));
        int baseY = random.nextInt(40);
        for (int i = 0; i < 9; i++) {
            boolean generate = i <= 5 || random.nextBoolean();
            if (!generate) {
                continue;
            }
            BlockPos pos = new BlockPos(baseX + random.nextInt(16), baseY + random.nextInt(16), baseZ + random.nextInt(16));
            boolean generated = minable.generate(world, random, pos);
            if (!workedOut || !generated) {
                continue;
            }
            if (!random.nextBoolean()) {
                boolean genChest = random.nextBoolean();
                boolean genBench = random.nextBoolean();
                boolean genSmelt = random.nextBoolean();
                for (EnumFacing dir : EnumFacing.HORIZONTALS) {
                    BlockPos offset = pos.offset(dir, random.nextInt(3) + 1);
                    if (BlockOreDepositGenerator.canReplace(offset, world)) {
                        continue;
                    }
                    if (genChest) {
                        world.setTileEntity(offset, new TileEntityChest());
                        world.setBlockState(offset, Blocks.CHEST.correctFacing(world, offset, Blocks.CHEST.getDefaultState()));
                        TileEntity tileentity = world.getTileEntity(offset);
                        if (tileentity instanceof TileEntityChest) {
                            TileEntityChest chest = (TileEntityChest) tileentity;
                            chest.setLootTable(LootTablesCategory.LootTables.WORKED_OUT_ORE_DEPOSIT_TABLE.getResource(), random.nextLong());
                        }
                        genChest = false;
                    } else if (genBench) {
                        world.setBlockState(offset, Blocks.CRAFTING_TABLE.getDefaultState());
                        genBench = false;
                    } else if (genSmelt) {
                        world.setBlockState(offset,
                          Blocks.FURNACE.getDefaultState().withProperty(BlockFurnace.FACING, WorldHelper.randomHorizontal(random)));
                        genSmelt = false;
                    }
                }
            }

            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(pos);
            for (int x = -3; x < 4; x++) {
                for (int y = 0; y < 2; y++) {
                    for (int z = -3; z < 4; z++) {
                        mutablePos.setPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                        if (BlockOreDepositGenerator.canReplace(mutablePos, world)) {
                            world.setBlockToAir(mutablePos);
                        }
                    }
                }
            }
        }
    }
}
