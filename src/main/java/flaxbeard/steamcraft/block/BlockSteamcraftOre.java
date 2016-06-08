package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.integration.CrossMod;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockSteamcraftOre extends Block {
    public IIcon[] icon = new IIcon[7];

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
    public void registerBlockIcons(IIconRegister ir) {
        this.icon[0] = ir.registerIcon("steamcraft:oreCopper");
        this.icon[3] = ir.registerIcon("steamcraft:copper_nether");
        this.icon[5] = ir.registerIcon("steamcraft:copper_end");


        this.icon[1] = ir.registerIcon("steamcraft:oreZinc");
        this.icon[4] = ir.registerIcon("steamcraft:zinc_nether");
        this.icon[6] = ir.registerIcon("steamcraft:zinc_end");

        this.icon[2] = ir.registerIcon("steamcraft:poorOreZinc");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        int dimensionID = Minecraft.getMinecraft().theWorld.provider.dimensionId;
        switch(dimensionID){
        case 0:	//Overworld
        	switch(meta){
        	case 0: return this.icon[0];
        	case 1: return this.icon[1];
        	case 2: return this.icon[2];
        	}
        case -1: //End
        	switch(meta){
        	case 0: return this.icon[3];
        	case 1: return this.icon[4];
        	case 2: return this.icon[2];
        	}
        case 1:	//Nether
        	switch(meta){
        	case 0: return this.icon[5];
        	case 1: return this.icon[6];
        	case 2: return this.icon[2];
        	}
        default: //Same as overworld
        	switch(meta){
        	case 0: return this.icon[0];
        	case 1: return this.icon[1];
        	case 2: return this.icon[2];
        	}
        }
		return this.icon[0]; //Shouldn't happen, but whatever.
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }
}
