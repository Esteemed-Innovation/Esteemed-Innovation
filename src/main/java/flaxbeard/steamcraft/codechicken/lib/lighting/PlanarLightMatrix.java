package flaxbeard.steamcraft.codechicken.lib.lighting;


import flaxbeard.steamcraft.codechicken.lib.render.CCRenderState;
import flaxbeard.steamcraft.codechicken.lib.vec.BlockCoord;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;

public class PlanarLightMatrix extends PlanarLightModel {
    public static final int OPERATION_INDEX = CCRenderState.registerOperation();
    public static PlanarLightMatrix instance = new PlanarLightMatrix();

    public IBlockAccess access;
    public BlockCoord pos = new BlockCoord();
    public int[] brightness = new int[6];
    private int sampled = 0;

    public PlanarLightMatrix() {
        super(PlanarLightModel.standardLightModel.colours);
    }

    public PlanarLightMatrix locate(IBlockAccess a, int x, int y, int z) {
        access = a;
        pos.set(x, y, z);
        sampled = 0;
        return this;
    }

    public int brightness(int side) {
        if ((sampled & 1 << side) == 0) {
            Block b = access.getBlock(pos.x, pos.y, pos.z);
            brightness[side] = access.getLightBrightnessForSkyBlocks(pos.x, pos.y, pos.z, b.getLightValue(access, pos.x, pos.y, pos.z));
            sampled |= 1 << side;
        }
        return brightness[side];
    }

    @Override
    public boolean load() {
        CCRenderState.pipeline.addDependency(CCRenderState.sideAttrib);
        return true;
    }

    @Override
    public void operate() {
        super.operate();
        CCRenderState.setBrightness(brightness(CCRenderState.side));
    }

    @Override
    public int operationID() {
        return OPERATION_INDEX;
    }
}
