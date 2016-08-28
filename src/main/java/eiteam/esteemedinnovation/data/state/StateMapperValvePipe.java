package eiteam.esteemedinnovation.data.state;

import com.google.common.collect.Maps;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;

import eiteam.esteemedinnovation.block.BlockValvePipe;
import eiteam.esteemedinnovation.init.blocks.SteamNetworkBlocks;

import java.util.Map;

public class StateMapperValvePipe extends StateMapperBase {
    private static final ResourceLocation PIPE_NAME = SteamNetworkBlocks.Blocks.PIPE.getBlock().getRegistryName();

    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        Map<IProperty<?>, Comparable<?>> map = Maps.newLinkedHashMap(state.getProperties());
        map.remove(BlockValvePipe.FACING);
        return new ModelResourceLocation(PIPE_NAME, getPropertyString(map));
    }
}
