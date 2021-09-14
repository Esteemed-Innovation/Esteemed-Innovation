package eiteam.esteemedinnovation.api.util;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.*;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.util.ResourceLocation;

public class TagUtility {
    
    public static INamedTag<EntityType<?>> createEntityTypeWrapper(ResourceLocation name) {
        return EntityTypeTags.getTagById(name.toString());
    }
    
    public static INamedTag<Item> createItemWrapper(ResourceLocation name) {
        return ItemTags.makeWrapperTag(name.toString());
    }
    
    public static INamedTag<Block> createBlockWrapper(ResourceLocation name) {
        return BlockTags.makeWrapperTag(name.toString());
    }
    
    public static INamedTag<Fluid> createFluidWrapper(ResourceLocation name) {
        return FluidTags.makeWrapperTag(name.toString());
    }
}
