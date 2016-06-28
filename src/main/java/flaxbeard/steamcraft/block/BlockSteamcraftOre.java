package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.init.blocks.OreBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockSteamcraftOre extends Block {
    public BlockSteamcraftOre(String name) {
        super(Material.ROCK);
        setResistance(5F);
        setHardness(3F);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 1);
        setCreativeTab(Steamcraft.tab);
        setUnlocalizedName(name);
        setRegistryName(Steamcraft.MOD_ID, name);
        GameRegistry.register(this);
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
}
