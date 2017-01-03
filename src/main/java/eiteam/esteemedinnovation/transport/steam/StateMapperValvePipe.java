package eiteam.esteemedinnovation.transport.steam;

import com.google.common.collect.Maps;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

import static eiteam.esteemedinnovation.transport.TransportationModule.BRASS_PIPE;

public class StateMapperValvePipe extends StateMapperBase {
    private static final ResourceLocation PIPE_NAME = BRASS_PIPE.getRegistryName();

    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        Map<IProperty<?>, Comparable<?>> map = Maps.newLinkedHashMap(state.getProperties());
        map.remove(BlockValvePipe.FACING);
        return new ModelResourceLocation(PIPE_NAME, getPropertyString(map));
    }
}
