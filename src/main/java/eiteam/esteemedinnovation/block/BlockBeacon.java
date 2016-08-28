package eiteam.esteemedinnovation.block;

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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.init.blocks.MetalBlocks;

import java.util.List;

public class BlockBeacon extends Block {
    public static PropertyEnum<MetalBlockTypes> VARIANT = PropertyEnum.create("variant", MetalBlockTypes.class);

    public BlockBeacon(Material material) {
        super(material);
        setCreativeTab(EsteemedInnovation.tab);
        setUnlocalizedName(EsteemedInnovation.MOD_ID + ":metal_storage_block");
        setHardness(5F);
        setResistance(10F);
        setDefaultState(blockState.getBaseState().withProperty(VARIANT, MetalBlockTypes.byMetadata(0)));
        setSoundType(SoundType.METAL);
        setRegistryName(EsteemedInnovation.MOD_ID, "metal_storage_block");
    }

    @Override
    public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) {
        return true;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (MetalBlocks.Blocks block : MetalBlocks.Blocks.values()) {
            list.add(block.createItemStack());
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
    public ItemStack createStackedBlock(IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this), 1, damageDropped(state));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }

    public enum MetalBlockTypes implements IStringSerializable {
        COPPER("copper", MetalBlocks.Blocks.COPPER.getMetadata()),
        ZINC("zinc", MetalBlocks.Blocks.ZINC.getMetadata()),
        BRASS("brass", MetalBlocks.Blocks.BRASS.getMetadata()),
        GILDED_IRON("gilded_iron", MetalBlocks.Blocks.GILDED_IRON.getMetadata());

        private String name;
        private int metadata;
        private static final MetalBlockTypes[] META_LOOKUP = new MetalBlockTypes[values().length];

        MetalBlockTypes(String name, int metadata) {
            this.name = name;
            this.metadata = metadata;
        }

        @Override
        public String getName() {
            return name;
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
