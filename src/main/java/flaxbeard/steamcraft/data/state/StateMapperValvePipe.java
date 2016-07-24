package flaxbeard.steamcraft.data.state;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;

import flaxbeard.steamcraft.block.BlockValvePipe;
import flaxbeard.steamcraft.init.blocks.SteamNetworkBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class StateMapperValvePipe extends StateMapperBase {
    private static final ResourceLocation PIPE_NAME = SteamNetworkBlocks.Blocks.PIPE.getBlock().getRegistryName();

    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        Map<IProperty<?>, Comparable<?>> map = Maps.newLinkedHashMap(state.getProperties());
        map.remove(BlockValvePipe.FACING);
        return new ModelResourceLocation(PIPE_NAME, getPropertyString(map));
    }
}
