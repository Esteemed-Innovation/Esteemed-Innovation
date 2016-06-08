package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.tile.TileEntityCreativeTank;
import flaxbeard.steamcraft.tile.TileEntitySteamTank;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockSteamTank extends BlockContainer {
    public IIcon[] icon = new IIcon[2];

    public BlockSteamTank() {
        super(Material.iron);
        setHardness(5F);
        setResistance(10F);
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        if (meta == 0) {
            return this.icon[0];
        }
        if (meta == 1) {
            return this.icon[1];
        }
        return this.icon[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister ir) {
        this.icon[0] = ir.registerIcon("steamcraft:brassTank");
        this.icon[1] = ir.registerIcon("steamcraft:creativeTank");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        if (meta == 1) {
            return new TileEntityCreativeTank();
        } else {
            return new TileEntitySteamTank();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
    }

}
