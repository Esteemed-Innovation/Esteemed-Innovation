package eiteam.esteemedinnovation.materials.refined;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import static eiteam.esteemedinnovation.commons.OreDictEntries.*;

public class BlockBeacon extends Block {
    public static PropertyEnum<MetalBlockTypes> VARIANT = PropertyEnum.create("variant", MetalBlockTypes.class);

    public BlockBeacon() {
        super(Material.IRON);
        setHardness(5F);
        setResistance(10F);
        setDefaultState(blockState.getBaseState().withProperty(VARIANT, MetalBlockTypes.byMetadata(0)));
        setSoundType(SoundType.METAL);
    }

    @Override
    public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) {
        return true;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, NonNullList<ItemStack> list) {
        for (MetalBlockTypes type : MetalBlockTypes.values()) {
            list.add(new ItemStack(this, 1, type.getMetadata()));
        }
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }

    @Override
    public IBlockState getStateFromMeta(int metadata) {
        return getDefaultState().withProperty(VARIANT, MetalBlockTypes.byMetadata(metadata));
    }

    @Override
    public ItemStack getSilkTouchDrop(IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this), 1, damageDropped(state));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }

    public enum MetalBlockTypes implements IStringSerializable {
        COPPER("copper", MATERIAL_COPPER, 0),
        ZINC("zinc", MATERIAL_ZINC, 1),
        BRASS("brass", MATERIAL_BRASS, 2),
        GILDED_IRON("gilded_iron", MATERIAL_GILDED_IRON, 3);

        private String name;
        private String oreMaterial;
        private int metadata;
        private static final MetalBlockTypes[] META_LOOKUP = new MetalBlockTypes[values().length];

        MetalBlockTypes(String name, String oreMaterial, int metadata) {
            this.name = name;
            this.oreMaterial = oreMaterial;
            this.metadata = metadata;
        }

        @Override
        public String getName() {
            return name;
        }

        public String getOreMaterial() {
            return oreMaterial;
        }

        public int getMetadata() {
            return metadata;
        }

        public static MetalBlockTypes byMetadata(int metadata) {
            return META_LOOKUP[metadata];
        }

        static {
            for (MetalBlockTypes type : values()) {
                META_LOOKUP[type.getMetadata()] = type;
            }
        }
    }
}
