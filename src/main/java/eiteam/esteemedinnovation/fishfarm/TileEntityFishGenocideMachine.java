package eiteam.esteemedinnovation.fishfarm;

import eiteam.esteemedinnovation.api.tile.SteamTransporterTileEntity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTableList;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class TileEntityFishGenocideMachine extends SteamTransporterTileEntity {
    public TileEntityFishGenocideMachine() {
        super(new EnumFacing[] {
          EnumFacing.UP,
          EnumFacing.DOWN
        });

        addSidesToGaugeBlacklist(EnumFacing.HORIZONTALS);
    }

    /**
     * @return A new Pair: Chunk, # of water source blocks in chunk at the Y position.
     */
    private Pair<Chunk, Integer> randSourceBlock() {
        List<Chunk> chunks = new ArrayList<>();
        int water = 0;
        for (int x = -3; x < 4; x++) {
            for (int z = -3; z < 4; z++) {
                BlockPos pos = new BlockPos(this.pos.getX() + x, this.pos.getY(), this.pos.getZ() + z);
                if (worldObj.getBlockState(pos).getBlock() == Blocks.WATER) {
                    chunks.add(new Chunk(worldObj, pos.getX(), pos.getZ()));
                    water++;
                }
            }
        }
        return Pair.of(chunks.get(worldObj.rand.nextInt(chunks.size())), water);
    }


    @Override
    public void update() {
        super.update();
        Pair<Chunk, Integer> pair = randSourceBlock();
        int src = pair.getRight();
        if (getSteamShare() > src) {
            decrSteam(src);
            if (worldObj.rand.nextInt((int) (300.0F / src)) == 0 && !worldObj.isRemote) {
                Chunk loc = pair.getLeft();
                LootContext lootContext = new LootContext.Builder((WorldServer) worldObj).build();

                List<ItemStack> fishes = worldObj.getLootTableManager().getLootTableFromLocation(
                  LootTableList.GAMEPLAY_FISHING_FISH).generateLootForPools(worldObj.rand, lootContext);
                ItemStack output = fishes.get(worldObj.rand.nextInt(fishes.size()));

                ItemStack smeltingResult = FurnaceRecipes.instance().getSmeltingResult(output);
                if (smeltingResult != null) {
                    output = smeltingResult;
                }
                dropItem(output, loc.xPosition + 0.5F, pos.getY() + 1.0F, loc.zPosition + 0.5F);
            }
        }
    }

    /**
     * Drops a floating item of the ItemStack at the position.
     * @param item The ItemStack
     * @param x X position
     * @param y Y position
     * @param z Z position
     */
    public void dropItem(ItemStack item, float x, float y, float z) {
        EntityFloatingItem entityItem = new EntityFloatingItem(worldObj, x, y, z, item);
        worldObj.spawnEntityInWorld(entityItem);
    }
}
