package flaxbeard.steamcraft.misc;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldContainer implements IBlockAccess {

    IBlockAccess myWorld;
    int allMeta;
    int allBrightness = -1;

    public WorldContainer(IBlockAccess world, int meta) {
        this.myWorld = world;
        this.allMeta = meta;
    }

    public WorldContainer(IBlockAccess world, int meta, int brightness) {
        this.myWorld = world;
        this.allMeta = meta;
        this.allBrightness = brightness;
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
    public int getLightBrightnessForSkyBlocks(int var1, int var2, int var3, int var4) {
        return allBrightness != -1 ? allBrightness : myWorld.getLightBrightnessForSkyBlocks(var1, var2, var3, var4);
    }

    @Override
    public int getBlockMetadata(int var1, int var2, int var3) {
        return allMeta;
    }

    @Override
    public boolean isAirBlock(int var1, int var2, int var3) {
        return myWorld.isAirBlock(var1, var2, var3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BiomeGenBase getBiomeGenForCoords(int var1, int var2) {
        return myWorld.getBiomeGenForCoords(var1, var2);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getHeight() {
        return myWorld.getHeight();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean extendedLevelsInChunkCache() {
        return myWorld.extendedLevelsInChunkCache();
    }

    @Override
    public int isBlockProvidingPowerTo(int var1, int var2, int var3, int var4) {
        return myWorld.isBlockProvidingPowerTo(var1, var2, var3, var4);
    }

    @Override
    public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
        return myWorld.isSideSolid(pos, side, _default);
    }
}
