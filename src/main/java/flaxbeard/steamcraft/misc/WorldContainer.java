package flaxbeard.steamcraft.misc;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldContainer implements IBlockAccess {
    IBlockAccess myWorld;
    int allMeta;
    int allBrightness;

    public WorldContainer(IBlockAccess world, int meta) {
        this(world, meta, -1);
    }

    public WorldContainer(IBlockAccess world, int meta, int brightness) {
        myWorld = world;
        allMeta = meta;
        allBrightness = brightness;
    }

    @Override
    public IBlockState getBlockState(BlockPos pos) {
        return myWorld.getBlockState(pos);
    }

    @Override
    public TileEntity getTileEntity(BlockPos pos) {
        return myWorld.getTileEntity(pos);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getCombinedLight(BlockPos pos, int light) {
        return allBrightness != -1 ? allBrightness : myWorld.getCombinedLight(pos, light);
    }

    @Override
    public boolean isAirBlock(BlockPos pos) {
        return myWorld.isAirBlock(pos);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean extendedLevelsInChunkCache() {
        return myWorld.extendedLevelsInChunkCache();
    }

    @Override
    public int getStrongPower(BlockPos pos, EnumFacing facing) {
        return myWorld.getStrongPower(pos, facing);
    }

    @Override
    public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
        return myWorld.isSideSolid(pos, side, _default);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Biome getBiome(BlockPos pos) {
        return myWorld.getBiome(pos);
    }

    @Override
    public WorldType getWorldType() {
        return myWorld.getWorldType();
    }
}
