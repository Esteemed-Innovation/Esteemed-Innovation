package flaxbeard.steamcraft.block;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.integration.CrossMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class BlockSteamcraftOre extends Block {

    public IIcon[] icon = new IIcon[7];

    public BlockSteamcraftOre() {
        super(Material.rock);
        setResistance(5.0F);
        setHardness(1.5F);
        setStepSound(Block.soundTypeStone);
        setHarvestLevel("pickaxe", 1);
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
    	int dimensionID;
    	try{
            dimensionID = Minecraft.getMinecraft().theWorld.provider.dimensionId;
    	}catch(NullPointerException e){
    	    return this.icon[0];
    	}

        switch(dimensionID){
        case 0:	//Overworld
        	switch(meta){
        	case 0: return this.icon[0];
        	case 1: return this.icon[1];
        	case 2: return this.icon[2];
        	default: break;    		
        	}
        	break;
        case -1: //End
        	switch(meta){
        	case 0: return this.icon[3];
        	case 1: return this.icon[4];
        	case 2: return this.icon[2];
        	default: break;
        	}
        	break;
        case 1:	//Nether
        	switch(meta){
        	case 0: return this.icon[5];
        	case 1: return this.icon[6];
        	case 2: return this.icon[2];
        	default: break;
        	}
        	break;
        default: //Same as overworld
        	switch(meta){
        	case 0: return this.icon[0];
        	case 1: return this.icon[1];
        	case 2: return this.icon[2];
        	default: break;
        	}
        }
		return this.icon[0]; //Shouldn't happen, but whatever.
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
        if (CrossMod.RAILCRAFT) {
            par3List.add(new ItemStack(par1, 1, 2));
        }
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }
}
