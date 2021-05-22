package eiteam.esteemedinnovation.materials.raw;

import eiteam.esteemedinnovation.commons.EsteemedInnovation;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

import static eiteam.esteemedinnovation.commons.OreDictEntries.MATERIAL_COPPER;
import static eiteam.esteemedinnovation.commons.OreDictEntries.MATERIAL_ZINC;

public class BlockGenericOre extends Block {
    public static final PropertyEnum<OreBlockTypes> VARIANT = PropertyEnum.create("variant", OreBlockTypes.class);

    public BlockGenericOre() {
        super(Material.ROCK);
        setResistance(5F);
        setHardness(3F);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 1);
    }

    @Nonnull
    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(VARIANT, OreBlockTypes.byMetadata(meta));
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (tab == EsteemedInnovation.tab) {
            for (OreBlockTypes type : OreBlockTypes.LOOKUP) {
                items.add(new ItemStack(this, 1, type.getMetadata()));
            }
        }
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    public enum OreBlockTypes implements IStringSerializable {
        OVERWORLD_COPPER(0, MATERIAL_COPPER, 0),
        OVERWORLD_ZINC(1, MATERIAL_ZINC, 0),
        NETHER_COPPER(2, MATERIAL_COPPER, -1),
        NETHER_ZINC(3, MATERIAL_ZINC, -1),
        END_COPPER(4, MATERIAL_COPPER, 1),
        END_ZINC(5, MATERIAL_ZINC, 1);

        private final int meta;
        private final String oreMaterial;
        private final int preferredDimension;
        public static final OreBlockTypes[] LOOKUP = new OreBlockTypes[values().length];

        OreBlockTypes(int meta, String oreMaterial, int preferredDimension) {
            this.meta = meta;
            this.oreMaterial = oreMaterial;
            this.preferredDimension = preferredDimension;
        }

        public int getMetadata() {
            return meta;
        }

        @Override
        public String getName() {
            return name().toLowerCase();
        }

        public String getOreMaterial() {
            return oreMaterial;
        }

        public int getPreferredDimension() {
            return preferredDimension;
        }

        static {
            for (OreBlockTypes type : values()) {
                LOOKUP[type.getMetadata()] = type;
            }
        }

        public static OreBlockTypes byMetadata(int meta) {
            return LOOKUP[meta];
        }
    }
}
