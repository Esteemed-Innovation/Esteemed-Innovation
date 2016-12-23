package eiteam.esteemedinnovation.metals.raw;

import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.init.blocks.OreBlocks;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockGenericOre extends Block {
    public static final PropertyEnum<OreBlockTypes> VARIANT = PropertyEnum.create("variant", OreBlockTypes.class);

    public BlockGenericOre(String name) {
        super(Material.ROCK);
        setResistance(5F);
        setHardness(3F);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 1);
        setCreativeTab(EsteemedInnovation.tab);
        setUnlocalizedName(EsteemedInnovation.MOD_ID + ":" + name);
        setRegistryName(EsteemedInnovation.MOD_ID, name);
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
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(VARIANT, OreBlockTypes.byMetadata(meta));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tabs, List<ItemStack> list) {
        for (OreBlocks.Blocks block : OreBlocks.Blocks.values()) {
            list.add(block.createItemStack());
        }
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    public enum OreBlockTypes implements IStringSerializable {
        OVERWORLD_COPPER(0),
        OVERWORLD_ZINC(1),
        NETHER_COPPER(2),
        NETHER_ZINC(3),
        END_COPPER(4),
        END_ZINC(5);

        private int meta;
        public static OreBlockTypes[] LOOKUP = new OreBlockTypes[values().length];

        OreBlockTypes(int meta) {
            this.meta = meta;
        }

        public int getMetadata() {
            return meta;
        }

        @Override
        public String getName() {
            return name().toLowerCase();
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
